package com.example.data.repository

import com.example.data.local.NezcoDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

class NezcoRepository(private val dao: NezcoDao) {

    val products: Flow<List<ProductEntity>> = dao.getAllProducts()
    val orders: Flow<List<OrderEntity>> = dao.getAllOrders()
    val routes: Flow<List<DispatchRouteEntity>> = dao.getAllRoutes()
    val deliveryStops: Flow<List<DeliveryStopEntity>> = dao.getAllDeliveryStops()
    val loans: Flow<List<ExtinguisherLoanEntity>> = dao.getAllLoans()
    val workshopOrders: Flow<List<WorkshopOrderEntity>> = dao.getAllWorkshopOrders()
    val expenses: Flow<List<RouteExpenseEntity>> = dao.getAllRouteExpenses()
    val employees: Flow<List<PayrollEmployeeEntity>> = dao.getAllEmployees()
    val auditLogs: Flow<List<AuditLogEntity>> = dao.getAuditLogs()
    val systemConfig: Flow<SystemConfigEntity?> = dao.getSystemConfig()

    fun getStopsForRoute(routeId: String): Flow<List<DeliveryStopEntity>> = dao.getStopsForRoute(routeId)

    // --- Product & Inventory Actions ---
    suspend fun insertProduct(product: ProductEntity) = dao.insertProduct(product)
    suspend fun updateProduct(product: ProductEntity) = dao.updateProduct(product)
    suspend fun updateStockPrincipal(id: String, newStock: Int) = dao.updateStockPrincipal(id, newStock)

    // --- Orders & Sales ---
    suspend fun insertOrder(order: OrderEntity) = dao.insertOrder(order)
    suspend fun updateOrder(order: OrderEntity) = dao.updateOrder(order)

    // --- Routes & Delivery Stops ---
    suspend fun updateRoute(route: DispatchRouteEntity) = dao.updateRoute(route)
    suspend fun updateStop(stop: DeliveryStopEntity) = dao.updateStop(stop)

    // --- Extinguisher Loans ---
    suspend fun insertLoan(loan: ExtinguisherLoanEntity) = dao.insertLoan(loan)
    suspend fun updateLoan(loan: ExtinguisherLoanEntity) = dao.updateLoan(loan)

    // --- Workshop Orders ---
    suspend fun insertWorkshopOrder(order: WorkshopOrderEntity) = dao.insertWorkshopOrder(order)
    suspend fun updateWorkshopOrder(order: WorkshopOrderEntity) = dao.updateWorkshopOrder(order)

    // --- Route Expenses ---
    suspend fun insertExpense(expense: RouteExpenseEntity) = dao.insertExpense(expense)
    suspend fun updateExpense(expense: RouteExpenseEntity) = dao.updateExpense(expense)

    // --- Payroll ---
    suspend fun updateEmployee(employee: PayrollEmployeeEntity) = dao.updateEmployee(employee)

    // --- Audit Log ---
    suspend fun logAction(userName: String, role: NezcoRole, action: String, module: String, details: String) {
        val timestamp = "2026-08-15 13:50" // Placeholder for SimpleDateFormat
        dao.insertAuditLog(
            AuditLogEntity(
                timestamp = timestamp,
                userName = userName,
                role = role,
                action = action,
                module = module,
                details = details
            )
        )
    }

    // --- System Config ---
    suspend fun updateSystemConfig(config: SystemConfigEntity) = dao.insertSystemConfig(config)
}
