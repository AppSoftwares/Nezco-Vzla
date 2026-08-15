package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.NezcoDatabase
import com.example.data.model.*
import com.example.data.repository.NezcoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class CartItem(
    val product: ProductEntity,
    val quantity: Int
)

enum class CurrencyMode { USD, VES }

class NezcoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NezcoRepository

    init {
        val database = NezcoDatabase.getDatabase(application, viewModelScope)
        repository = NezcoRepository(database.nezcoDao())
    }

    // Role & Navigation
    private val _currentRole = MutableStateFlow(NezcoRole.SUPER_ADMIN)
    val currentRole: StateFlow<NezcoRole> = _currentRole.asStateFlow()

    private val _currentTab = MutableStateFlow("dashboard")
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    private val _currencyMode = MutableStateFlow(CurrencyMode.USD)
    val currencyMode: StateFlow<CurrencyMode> = _currencyMode.asStateFlow()

    // Data Streams from Room
    val products: StateFlow<List<ProductEntity>> = repository.products
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<OrderEntity>> = repository.orders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val routes: StateFlow<List<DispatchRouteEntity>> = repository.routes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val deliveryStops: StateFlow<List<DeliveryStopEntity>> = repository.deliveryStops
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val loans: StateFlow<List<ExtinguisherLoanEntity>> = repository.loans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val workshopOrders: StateFlow<List<WorkshopOrderEntity>> = repository.workshopOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenses: StateFlow<List<RouteExpenseEntity>> = repository.expenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val employees: StateFlow<List<PayrollEmployeeEntity>> = repository.employees
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val auditLogs: StateFlow<List<AuditLogEntity>> = repository.auditLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val systemConfig: StateFlow<SystemConfigEntity?> = repository.systemConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Catalog & Filter state
    val selectedCategory = MutableStateFlow<ProductCategory?>(null)
    val searchQuery = MutableStateFlow("")

    // Shopping Cart State (Sales & POS)
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    // Notification toast / message
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun showMessage(msg: String) {
        _snackbarMessage.value = msg
    }

    // Role switcher
    fun setRole(role: NezcoRole) {
        _currentRole.value = role
        // Suggest a default tab suitable for the role
        _currentTab.value = when (role) {
            NezcoRole.CHOFER -> "chofer_ruta"
            NezcoRole.DESPACHADOR -> "despacho_radar"
            NezcoRole.VENTA -> "catalogo"
            NezcoRole.POS_LOCAL -> "pos_venta"
            NezcoRole.ALMACENISTA -> "almacen"
            NezcoRole.TALLER -> "taller"
            NezcoRole.ADMIN, NezcoRole.SUPER_ADMIN -> "dashboard"
        }
        viewModelScope.launch {
            repository.logAction(
                userName = "Usuario Activo",
                role = role,
                action = "CAMBIO_ROL",
                module = "Autenticación & Roles",
                details = "Sesión cambiada a perfil: ${role.title}"
            )
        }
    }

    fun setTab(tab: String) {
        _currentTab.value = tab
    }

    fun toggleCurrency() {
        _currentRole // trigger read
        _currencyMode.value = if (_currencyMode.value == CurrencyMode.USD) CurrencyMode.VES else CurrencyMode.USD
    }

    fun formatPrice(amountUsd: Double): String {
        val bcvRate = systemConfig.value?.bcvRateBs ?: 68.50
        return if (_currencyMode.value == CurrencyMode.USD) {
            String.format(Locale.US, "$%.2f", amountUsd)
        } else {
            val amountBs = amountUsd * bcvRate
            String.format(Locale.US, "Bs. %.2f", amountBs)
        }
    }

    // Cart operations
    fun addToCart(product: ProductEntity) {
        val current = _cart.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            current[index] = current[index].copy(quantity = current[index].quantity + 1)
        } else {
            current.add(CartItem(product = product, quantity = 1))
        }
        _cart.value = current
        showMessage("Añadido: ${product.name}")
    }

    fun updateCartQuantity(productId: String, delta: Int) {
        val current = _cart.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == productId }
        if (index >= 0) {
            val newQty = current[index].quantity + delta
            if (newQty <= 0) {
                current.removeAt(index)
            } else {
                current[index] = current[index].copy(quantity = newQty)
            }
            _cart.value = current
        }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    fun checkoutOrder(
        clientName: String,
        clientRif: String,
        clientPhone: String,
        clientAddress: String,
        isPos: Boolean,
        paymentStatus: PaymentStatus
    ) {
        val currentCart = _cart.value
        if (currentCart.isEmpty()) return

        val totalUsd = currentCart.sumOf { it.product.priceUsd * it.quantity }
        val itemsSummary = currentCart.joinToString(", ") { "${it.quantity}x ${it.product.name}" }
        val orderNumber = "NEZ-${1050 + (orders.value.size + 1)}"
        val dateIso = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val newOrder = OrderEntity(
            id = UUID.randomUUID().toString(),
            orderNumber = orderNumber,
            clientName = clientName.ifBlank { "Cliente Mostrador Nezco" },
            clientRif = clientRif.ifBlank { "J-00000000-0" },
            clientPhone = clientPhone.ifBlank { "0261-0000000" },
            clientAddress = clientAddress.ifBlank { "Venta en Tienda Nezco" },
            dateIso = dateIso,
            itemsSummary = itemsSummary,
            totalUsd = totalUsd,
            status = if (isPos) OrderStatus.ENTREGADO_FACTURADO else OrderStatus.PEDIDO_CONFIRMADO,
            paymentStatus = paymentStatus,
            paidAmountUsd = if (paymentStatus == PaymentStatus.PAGADO) totalUsd else 0.0,
            assignedDriverName = if (isPos) "Venta en Mostrador" else "Carlos Mendoza (Camión 01)",
            isPosDirectSale = isPos,
            notes = if (isPos) "Facturado directamente por POS Local" else "Pedido de ventas para despacho"
        )

        viewModelScope.launch {
            repository.insertOrder(newOrder)
            // Deduct stock
            currentCart.forEach { item ->
                val newStock = (item.product.stockPrincipal - item.quantity).coerceAtLeast(0)
                repository.updateStockPrincipal(item.product.id, newStock)
            }
            repository.logAction(
                userName = if (isPos) "Asistente POS" else "Asesor de Ventas",
                role = if (isPos) NezcoRole.POS_LOCAL else NezcoRole.VENTA,
                action = if (isPos) "VENTA_POS_EMITIDA" else "PEDIDO_CREADO",
                module = if (isPos) "POS Local" else "Catálogo & Ventas",
                details = "Orden $orderNumber por $totalUsd USD para ${newOrder.clientName}"
            )
            clearCart()
            showMessage(if (isPos) "¡Venta completada! Factura $orderNumber emitida." else "¡Pedido $orderNumber generado con éxito!")
        }
    }

    // --- Driver / Chofer Actions ---
    fun setStopEnCamino(stop: DeliveryStopEntity) {
        viewModelScope.launch {
            repository.updateStop(stop.copy(status = DeliveryStopStatus.EN_CAMINO))
            repository.logAction(
                userName = "Carlos Mendoza",
                role = NezcoRole.CHOFER,
                action = "EN_CAMINO_A_PARADA",
                module = "Logística & Chofer",
                details = "Chofer en camino hacia: ${stop.clientName}"
            )
            showMessage("En camino hacia: ${stop.clientName}")
        }
    }

    fun completeDeliveryStop(
        stop: DeliveryStopEntity,
        recipient: String,
        notes: String,
        returnedItems: String,
        loanExtinguisherCode: String,
        clientExtinguisherTaken: String
    ) {
        viewModelScope.launch {
            val time = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())
            val updatedStop = stop.copy(
                status = if (returnedItems.isNotBlank()) DeliveryStopStatus.DEVOLUCION_PARCIAL else DeliveryStopStatus.ENTREGADO,
                recipientName = recipient.ifBlank { "Recepción de Seguridad" },
                signatureCaptured = true,
                photoEvidenceCaptured = true,
                completedAt = time,
                notes = notes,
                returnedItemsDetail = returnedItems,
                extinguishersTakenForRecharge = clientExtinguisherTaken,
                loanExtinguisherLeftCode = loanExtinguisherCode
            )
            repository.updateStop(updatedStop)

            // If loan or client extinguisher taken was registered, create ExtinguisherLoan
            if (loanExtinguisherCode.isNotBlank() || clientExtinguisherTaken.isNotBlank()) {
                val cal = Calendar.getInstance()
                cal.add(Calendar.DAY_OF_YEAR, 7)
                val targetReturn = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                val newLoan = ExtinguisherLoanEntity(
                    id = UUID.randomUUID().toString(),
                    clientName = stop.clientName,
                    clientRif = stop.clientRif,
                    clientPhone = stop.phone,
                    clientAddress = stop.address,
                    loanExtinguisherCode = loanExtinguisherCode.ifBlank { "NEZ-COMODATO-TEMP" },
                    loanTypeDescription = "Extintor Nezco de Reemplazo en Comodato",
                    clientOriginalExtinguisherCode = clientExtinguisherTaken.ifBlank { "EXT-CLIENTE-RECARGA" },
                    loanDate = todayStr,
                    commitmentReturnDate = targetReturn,
                    status = LoanStatus.ACTIVO_EN_CLIENTE,
                    daysRemaining = 7,
                    registeredByDriver = "Carlos Mendoza",
                    notes = "Registrado desde la parada de entrega: ${stop.address}"
                )
                repository.insertLoan(newLoan)
            }

            // Update route progress
            val route = routes.value.firstOrNull { it.id == stop.routeId }
            if (route != null) {
                val newCompleted = (route.completedStops + 1).coerceAtMost(route.totalStops)
                val newRouteStatus = if (newCompleted >= route.totalStops) DispatchStatus.COMPLETADA else DispatchStatus.EN_RUTA
                repository.updateRoute(route.copy(completedStops = newCompleted, status = newRouteStatus))
            }

            repository.logAction(
                userName = "Carlos Mendoza",
                role = NezcoRole.CHOFER,
                action = "ENTREGA_FINALIZADA",
                module = "Logística & Chofer",
                details = "Parada ${stop.clientName} marcada como entregada. Recibió: $recipient"
            )
            showMessage("¡Parada entregada y firmada con éxito!")
        }
    }

    // Register Driver Route Expense
    fun submitRouteExpense(
        category: ExpenseCategory,
        description: String,
        amountUsd: Double
    ) {
        val bcvRate = systemConfig.value?.bcvRateBs ?: 68.50
        val amountBs = amountUsd * bcvRate
        val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

        val newExpense = RouteExpenseEntity(
            id = "EXP-${UUID.randomUUID().toString().take(6).uppercase()}",
            routeId = "ROUTE-2026-08-14",
            driverName = "Carlos Mendoza",
            category = category,
            description = description,
            amountUsd = amountUsd,
            amountBs = amountBs,
            date = dateStr,
            receiptPhotoAttached = true,
            status = ExpenseStatus.PENDIENTE_APROBACION
        )

        viewModelScope.launch {
            repository.insertExpense(newExpense)
            repository.logAction(
                userName = "Carlos Mendoza",
                role = NezcoRole.CHOFER,
                action = "GASTO_RUTA_REGISTRADO",
                module = "Gastos de Chofer",
                details = "Gasto $category por $amountUsd USD ($description) enviado a aprobación."
            )
            showMessage("Gasto de ruta cargado y enviado para aprobación de Administración.")
        }
    }

    // Admin Expense Approval
    fun approveExpense(expense: RouteExpenseEntity, approved: Boolean) {
        viewModelScope.launch {
            val updated = expense.copy(
                status = if (approved) ExpenseStatus.APROBADO else ExpenseStatus.RECHAZADO,
                approvedBy = "Super Administrador"
            )
            repository.updateExpense(updated)
            repository.logAction(
                userName = "Super Admin",
                role = NezcoRole.SUPER_ADMIN,
                action = if (approved) "GASTO_APROBADO" else "GASTO_RECHAZADO",
                module = "Control de Gastos",
                details = "Gasto ${expense.id} (${expense.amountUsd} USD) ${if (approved) "aprobado" else "rechazado"}."
            )
            showMessage(if (approved) "Gasto aprobado exitosamente." else "Gasto rechazado.")
        }
    }

    // --- Workshop / Taller Actions ---
    fun advanceWorkshopStage(order: WorkshopOrderEntity) {
        val nextStage = when (order.stage) {
            WorkshopStage.RECIBIDO -> WorkshopStage.DESPRESURIZADO
            WorkshopStage.DESPRESURIZADO -> WorkshopStage.PRUEBA_HIDROSTATICA
            WorkshopStage.PRUEBA_HIDROSTATICA -> WorkshopStage.RECARGA_POLVO_GAS
            WorkshopStage.RECARGA_POLVO_GAS -> WorkshopStage.SELLADO_MARBETE
            WorkshopStage.SELLADO_MARBETE -> WorkshopStage.LISTO_ENTREGA
            WorkshopStage.LISTO_ENTREGA -> WorkshopStage.ENTREGADO_CLIENTE
            WorkshopStage.ENTREGADO_CLIENTE -> WorkshopStage.ENTREGADO_CLIENTE
        }

        viewModelScope.launch {
            val updated = order.copy(
                stage = nextStage,
                completedDate = if (nextStage == WorkshopStage.LISTO_ENTREGA) SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) else order.completedDate
            )
            repository.updateWorkshopOrder(updated)
            repository.logAction(
                userName = "José Gregorio Colina",
                role = NezcoRole.TALLER,
                action = "AVANCE_ETAPA_TALLER",
                module = "Taller de Recarga",
                details = "Orden ${order.orderNumber} avanzada a: ${nextStage.label}"
            )
            showMessage("Orden de taller ${order.orderNumber} actualizada a: ${nextStage.label}")
        }
    }

    fun createWorkshopOrder(
        clientName: String,
        clientRif: String,
        extinguisherType: String,
        capacity: String,
        serialNumber: String,
        cylinderYear: Int,
        serviceType: String
    ) {
        val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, 5)
        val targetDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)

        val newOrder = WorkshopOrderEntity(
            id = UUID.randomUUID().toString(),
            orderNumber = "TAL-2026-${100 + workshopOrders.value.size + 1}",
            clientName = clientName.ifBlank { "Cliente Industrial" },
            clientRif = clientRif.ifBlank { "J-00000000-0" },
            extinguisherType = extinguisherType,
            capacity = capacity,
            serialNumber = serialNumber.ifBlank { "SER-${System.currentTimeMillis().toString().takeLast(6)}" },
            cylinderYear = cylinderYear,
            lastHydrostaticYear = cylinderYear,
            serviceType = serviceType,
            stage = WorkshopStage.RECIBIDO,
            technicianName = "José Gregorio Colina",
            coveninStandard = if (extinguisherType.contains("CO2")) "COVENIN 1114" else "COVENIN 1040",
            partsConsumedSummary = "Inspección inicial, Marbete Nezco 2026",
            totalCostUsd = if (extinguisherType.contains("CO2")) 22.00 else 15.00,
            receivedDate = dateStr,
            targetDeliveryDate = targetDate,
            passedHydrostaticTest = true,
            newInspectionTagNumber = "MARB-2026-${1000 + workshopOrders.value.size + 1}"
        )

        viewModelScope.launch {
            repository.insertWorkshopOrder(newOrder)
            repository.logAction(
                userName = "Personal de Taller",
                role = NezcoRole.TALLER,
                action = "NUEVA_ORDEN_TALLER",
                module = "Taller de Recarga",
                details = "Ingresado extintor $extinguisherType ($capacity) de $clientName para servicio."
            )
            showMessage("Orden ${newOrder.orderNumber} creada en Taller.")
        }
    }

    // --- Inventory Adjustments ---
    fun updateProductStock(product: ProductEntity, principal: Int, taller: Int, camion: Int) {
        viewModelScope.launch {
            val updated = product.copy(
                stockPrincipal = principal,
                stockTaller = taller,
                stockCamion = camion
            )
            repository.updateProduct(updated)
            repository.logAction(
                userName = "Luis Pirela",
                role = NezcoRole.ALMACENISTA,
                action = "AJUSTE_INVENTARIO",
                module = "Almacén e Inventario",
                details = "Stock ajustado para ${product.name}: Principal=$principal, Taller=$taller, Camión=$camion"
            )
            showMessage("Inventario de '${product.name}' actualizado.")
        }
    }

    // --- Return Extinguisher Loan ---
    fun markLoanAsReturned(loan: ExtinguisherLoanEntity) {
        viewModelScope.launch {
            val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val updated = loan.copy(
                status = LoanStatus.DEVUELTO_A_ALMACEN,
                returnedDate = dateStr,
                daysRemaining = 0
            )
            repository.updateLoan(updated)
            repository.logAction(
                userName = "Luis Pirela",
                role = NezcoRole.ALMACENISTA,
                action = "RETORNO_COMODATO",
                module = "Control de Préstamos",
                details = "Extintor de préstamo ${loan.loanExtinguisherCode} retornado a almacén por cliente ${loan.clientName}."
            )
            showMessage("Préstamo devuelto y reintegrado a almacén.")
        }
    }

    // --- Account Receivable Payment Registration ---
    fun registerOrderPayment(order: OrderEntity, amountPaidUsd: Double) {
        viewModelScope.launch {
            val newTotalPaid = (order.paidAmountUsd + amountPaidUsd).coerceAtMost(order.totalUsd)
            val newStatus = if (newTotalPaid >= order.totalUsd) PaymentStatus.PAGADO else PaymentStatus.ABONO_PARCIAL
            val updated = order.copy(
                paidAmountUsd = newTotalPaid,
                paymentStatus = newStatus
            )
            repository.updateOrder(updated)
            repository.logAction(
                userName = "Super Admin",
                role = NezcoRole.SUPER_ADMIN,
                action = "PAGO_REGISTRADO",
                module = "Cuentas por Cobrar (CxC)",
                details = "Abono de $amountPaidUsd USD registrado a orden ${order.orderNumber} (${order.clientName})."
            )
            showMessage("Pago registrado: +$amountPaidUsd USD a la orden ${order.orderNumber}")
        }
    }

    // --- System & LOTTT Configuration Updates ---
    fun updateBcvRate(newRate: Double) {
        viewModelScope.launch {
            val current = systemConfig.value ?: SystemConfigEntity()
            val updated = current.copy(bcvRateBs = newRate)
            repository.updateSystemConfig(updated)
            repository.logAction(
                userName = "Super Admin",
                role = NezcoRole.SUPER_ADMIN,
                action = "ACTUALIZACION_TASA_BCV",
                module = "Configuración del Sistema",
                details = "Tasa oficial BCV actualizada a Bs. $newRate por USD."
            )
            showMessage("Tasa BCV actualizada a Bs. $newRate")
        }
    }

    fun updateLegalParameters(
        ivssRate: Double,
        faovRate: Double,
        incesRate: Double,
        cestaticketUsd: Double
    ) {
        viewModelScope.launch {
            val current = systemConfig.value ?: SystemConfigEntity()
            val updated = current.copy(
                ivssEmployeeRatePercent = ivssRate,
                faovEmployeeRatePercent = faovRate,
                incesEmployeeRatePercent = incesRate,
                standardCestaticketUsd = cestaticketUsd
            )
            repository.updateSystemConfig(updated)
            repository.logAction(
                userName = "Super Admin",
                role = NezcoRole.SUPER_ADMIN,
                action = "ACTUALIZACION_PARAMETROS_LOTTT",
                module = "Nómina Legal Venezolana",
                details = "Parámetros LOTTT actualizados: Cestaticket=$cestaticketUsd USD, IVSS=$ivssRate%, FAOV=$faovRate%, INCES=$incesRate%"
            )
            showMessage("Parámetros laborales LOTTT actualizados correctamente.")
        }
    }
}
