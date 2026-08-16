package com.nezco.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nezco.app.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * TODO: [MEDIO] La base de datos Room no está cifrada. Implementar SQLCipher si se guardan datos sensibles.
 */
@Database(
    entities = [
        ProductEntity::class,
        OrderEntity::class,
        DispatchRouteEntity::class,
        DeliveryStopEntity::class,
        ExtinguisherLoanEntity::class,
        WorkshopOrderEntity::class,
        RouteExpenseEntity::class,
        PayrollEmployeeEntity::class,
        AuditLogEntity::class,
        SystemConfigEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NezcoDatabase : RoomDatabase() {

    abstract fun nezcoDao(): NezcoDao

    // Removed getDatabase companion method to be KMP-compatible.
    // Use platform-specific database builder instead.
}

class NezcoDatabaseCallback(
    private val scope: CoroutineScope
) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // Initial data population should be handled in a platform-independent way
        // or via the platform-specific builder callback.
    }

        suspend fun populateInitialData(dao: NezcoDao) {
            // 1. Initial Products & Extinguishers
            val initialProducts = listOf(
                ProductEntity(
                    id = "EXT-PQS-10",
                    code = "EXT-PQS-10LBS",
                    name = "Extintor PQS 10 Lbs (ABC) Nezco",
                    category = ProductCategory.PQS,
                    capacity = "10 Lbs (4.5 Kg)",
                    priceUsd = 38.00,
                    stockPrincipal = 45,
                    stockTaller = 18,
                    stockCamion = 12,
                    minStock = 15,
                    description = "Extintor presurizado polvo químico seco ABC al 75% MAP. Válvula de bronce cromado, manómetro certificado.",
                    technicalSpecs = "Norma COVENIN 1040. Rango de descarga 4-6 mts. Tiempo descarga 14 seg.",
                    isExtinguisher = true
                ),
                ProductEntity(
                    id = "EXT-PQS-20",
                    code = "EXT-PQS-20LBS",
                    name = "Extintor PQS 20 Lbs (ABC) Industrial",
                    category = ProductCategory.PQS,
                    capacity = "20 Lbs (9.0 Kg)",
                    priceUsd = 55.00,
                    stockPrincipal = 30,
                    stockTaller = 8,
                    stockCamion = 6,
                    minStock = 10,
                    description = "Extintor industrial de alto rendimiento para plantas, almacenes y transporte de carga pesada.",
                    technicalSpecs = "Norma COVENIN 1040. Cilindro de acero de alta resistencia hidro-probado.",
                    isExtinguisher = true
                ),
                ProductEntity(
                    id = "EXT-CO2-10",
                    code = "EXT-CO2-10LBS",
                    name = "Extintor CO2 10 Lbs Dióxido de Carbono (BC)",
                    category = ProductCategory.CO2,
                    capacity = "10 Lbs (4.5 Kg)",
                    priceUsd = 85.00,
                    stockPrincipal = 14,
                    stockTaller = 6,
                    stockCamion = 4,
                    minStock = 5,
                    description = "Agente limpio que no deja residuos para centros de cómputo, quirófanos y tableros eléctricos de alta tensión.",
                    technicalSpecs = "Norma COVENIN 1114 / CGA-320. Cilindro aluminio sin costura.",
                    isExtinguisher = true
                ),
                ProductEntity(
                    id = "EXT-SOLK-5",
                    code = "EXT-SOLK-5LBS",
                    name = "Extintor Solkaflam / HFC-236fa 5 Lbs",
                    category = ProductCategory.SOLKAFLAM,
                    capacity = "5 Lbs (2.3 Kg)",
                    priceUsd = 72.00,
                    stockPrincipal = 12,
                    stockTaller = 4,
                    stockCamion = 2,
                    minStock = 4,
                    description = "Agente extintor ecológico no conductor para equipos electrónicos delicados, aviación y salas de servidores.",
                    technicalSpecs = "NFPA 2001. Cero residuo corrosivo, aprobado ambientalmente.",
                    isExtinguisher = true
                ),
                ProductEntity(
                    id = "GAB-ROJO-10",
                    code = "GAB-MET-10",
                    name = "Gabinete Metálico Rojo 10/20 Lbs con Vidrio",
                    category = ProductCategory.GABINETES,
                    capacity = "Para extintores hasta 20 Lbs",
                    priceUsd = 28.50,
                    stockPrincipal = 25,
                    stockTaller = 0,
                    stockCamion = 8,
                    minStock = 8,
                    description = "Gabinete en lámina cold-rolled calibre 20, acabado en pintura electrostática roja horneada.",
                    technicalSpecs = "Norma COVENIN 751. Incluye cerradura tipo aldaba y martillo rompividrio."
                ),
                ProductEntity(
                    id = "MANG-INC-15",
                    code = "MANG-SINT-1.5",
                    name = "Manguera Contra Incendio 1.5\" x 30m Poliéster",
                    category = ProductCategory.MANGUERAS,
                    capacity = "30 Metros / 1.5 Pulgadas",
                    priceUsd = 65.00,
                    stockPrincipal = 16,
                    stockTaller = 3,
                    stockCamion = 4,
                    minStock = 5,
                    description = "Manguera simple chaqueta 100% poliéster con revestimiento interno EPDM vulcanizado.",
                    technicalSpecs = "Norma COVENIN 1330. Presión de trabajo 250 PSI, acoples en aluminio bronce."
                ),
                ProductEntity(
                    id = "SEN-EXT-FOTO",
                    code = "SEN-FOTO-20X20",
                    name = "Señalética Fotoluminiscente 'EXTINTOR' 20x20cm",
                    category = ProductCategory.SENALIZACION,
                    capacity = "20 x 20 cm en PVC 1mm",
                    priceUsd = 4.50,
                    stockPrincipal = 110,
                    stockTaller = 20,
                    stockCamion = 30,
                    minStock = 30,
                    description = "Señal de alta reflectividad y fotoluminiscencia visible en oscuridad total hasta por 8 horas.",
                    technicalSpecs = "Norma COVENIN 187. Resistente a rayos UV y químicos industriales."
                ),
                ProductEntity(
                    id = "DET-HUMO-OPT",
                    code = "DET-HUMO-24V",
                    name = "Detector de Humo Fotoeléctrico 24V Convencional",
                    category = ProductCategory.DETECCION,
                    capacity = "Cobertura 60 m2",
                    priceUsd = 18.00,
                    stockPrincipal = 40,
                    stockTaller = 10,
                    stockCamion = 10,
                    minStock = 12,
                    description = "Detector óptico de humo con indicador LED 360° para centrales de alarma contra incendio.",
                    technicalSpecs = "Norma COVENIN 1041 / UL 268. Contacto seco normalmente abierto/cerrado."
                ),
                ProductEntity(
                    id = "REP-POLVO-75",
                    code = "INS-PQS-75-KG",
                    name = "Polvo Químico Seco ABC 75% (Saco 25 Kg)",
                    category = ProductCategory.REPUESTOS_INSUMOS,
                    capacity = "Saco 25 Kg",
                    priceUsd = 42.00,
                    stockPrincipal = 35,
                    stockTaller = 22,
                    stockCamion = 0,
                    minStock = 10,
                    description = "Fosfato Monoamónico al 75% micro-siliconizado antihumedad para recargas de taller.",
                    technicalSpecs = "Norma COVENIN 1040. Alta fluidez para presurización."
                )
            )
            dao.insertProducts(initialProducts)

            // 2. Initial Orders
            val initialOrders = listOf(
                OrderEntity(
                    id = "ORD-2026-001",
                    orderNumber = "NEZ-1048",
                    clientName = "Distribuidora Lácteos Los Andes, C.A.",
                    clientRif = "J-30491823-1",
                    clientPhone = "0261-7548921",
                    clientAddress = "Zona Industrial II, Galpón #14, Maracaibo",
                    dateIso = "2026-08-14",
                    itemsSummary = "4x Extintor PQS 10 Lbs, 2x Gabinete Rojo, 4x Señalética",
                    totalUsd = 227.00,
                    status = OrderStatus.EN_DESPACHO,
                    paymentStatus = PaymentStatus.ABONO_PARCIAL,
                    paidAmountUsd = 100.00,
                    assignedDriverName = "Carlos Mendoza (Camión 01)",
                    notes = "Entregar en garita de seguridad con factura original"
                ),
                OrderEntity(
                    id = "ORD-2026-002",
                    orderNumber = "NEZ-1049",
                    clientName = "Supermercado Enne Bella Vista",
                    clientRif = "J-07012938-4",
                    clientPhone = "0261-7921144",
                    clientAddress = "Av. 4 Bella Vista c/c Calle 72, Maracaibo",
                    dateIso = "2026-08-14",
                    itemsSummary = "6x Recarga Anual PQS 10 Lbs, 2x Extintor CO2 10 Lbs",
                    totalUsd = 258.00,
                    status = OrderStatus.EN_DESPACHO,
                    paymentStatus = PaymentStatus.PENDIENTE,
                    paidAmountUsd = 0.00,
                    assignedDriverName = "Carlos Mendoza (Camión 01)",
                    notes = "Cobro contra entrega en caja central"
                ),
                OrderEntity(
                    id = "ORD-2026-003",
                    orderNumber = "NEZ-1050",
                    clientName = "Clínica Paraíso San Francisco",
                    clientRif = "J-29834190-2",
                    clientPhone = "0261-7629000",
                    clientAddress = "Urb. Coromoto, Av. 40, San Francisco",
                    dateIso = "2026-08-13",
                    itemsSummary = "3x Extintor Solkaflam 5 Lbs, 1x Manguera 1.5\"",
                    totalUsd = 281.00,
                    status = OrderStatus.ENTREGADO_FACTURADO,
                    paymentStatus = PaymentStatus.PAGADO,
                    paidAmountUsd = 281.00,
                    assignedDriverName = "Carlos Mendoza (Camión 01)"
                )
            )
            dao.insertOrders(initialOrders)

            // 3. Dispatch Route and Stops
            val initialRoute = DispatchRouteEntity(
                id = "ROUTE-2026-08-14",
                routeCode = "RUTA-MCBO-NORTE-01",
                driverId = "DRV-01",
                driverName = "Carlos Mendoza",
                truckPlate = "A82BD9G",
                truckModel = "Ford Cargo 815 Nezco",
                date = "2026-08-14",
                status = DispatchStatus.EN_RUTA,
                totalStops = 3,
                completedStops = 1,
                currentZone = "Bella Vista / Zona Industrial",
                estimatedKm = 34.5,
                startedAt = "08:30 AM"
            )
            dao.insertRoutes(listOf(initialRoute))

            val initialStops = listOf(
                DeliveryStopEntity(
                    id = "STOP-001",
                    routeId = "ROUTE-2026-08-14",
                    stopOrder = 1,
                    clientName = "Distribuidora Lácteos Los Andes, C.A.",
                    clientRif = "J-30491823-1",
                    address = "Zona Industrial II, Galpón #14, Maracaibo",
                    phone = "0414-6382910",
                    orderId = "ORD-2026-001",
                    itemsDescription = "4x Extintor PQS 10 Lbs, 2x Gabinetes, 4x Señales",
                    status = DeliveryStopStatus.EN_CAMINO,
                    notes = "Debe recoger 4 extintores vacíos para recarga de taller"
                ),
                DeliveryStopEntity(
                    id = "STOP-002",
                    routeId = "ROUTE-2026-08-14",
                    stopOrder = 2,
                    clientName = "Supermercado Enne Bella Vista",
                    clientRif = "J-07012938-4",
                    address = "Av. 4 Bella Vista c/c Calle 72, Maracaibo",
                    phone = "0424-7890123",
                    orderId = "ORD-2026-002",
                    itemsDescription = "6x Recargas entregadas, 2x Extintores CO2 10 Lbs",
                    status = DeliveryStopStatus.PENDIENTE,
                    notes = "Dejar extintores de comodato si quedan pendientes 2 por recargar"
                ),
                DeliveryStopEntity(
                    id = "STOP-003",
                    routeId = "ROUTE-2026-08-14",
                    stopOrder = 3,
                    clientName = "Farmacia Saas Cecilio Acosta",
                    clientRif = "J-40192837-5",
                    address = "Calle 67 Cecilio Acosta con Av. 10",
                    phone = "0412-5551234",
                    orderId = "ORD-2026-003",
                    itemsDescription = "1x Extintor PQS 10 Lbs + Inspección Anual",
                    status = DeliveryStopStatus.ENTREGADO,
                    recipientName = "Lic. Maria Romero",
                    signatureCaptured = true,
                    photoEvidenceCaptured = true,
                    completedAt = "09:45 AM"
                )
            )
            dao.insertStops(initialStops)

            // 4. Extinguisher Loans (Comodato)
            val initialLoans = listOf(
                ExtinguisherLoanEntity(
                    id = "LOAN-001",
                    clientName = "Panadería Flor de Maracaibo, C.A.",
                    clientRif = "J-31982736-0",
                    clientPhone = "0261-7832910",
                    clientAddress = "Av. 5 de Julio c/c Av. 13",
                    loanExtinguisherCode = "NEZ-COMODATO-019",
                    loanTypeDescription = "PQS 10 Lbs Nezco (Cilindro Amarillo Nezco)",
                    clientOriginalExtinguisherCode = "CLI-BUCK-9912 (PQS 10 Lbs Buckeye)",
                    loanDate = "2026-08-10",
                    commitmentReturnDate = "2026-08-17",
                    status = LoanStatus.ACTIVO_EN_CLIENTE,
                    daysRemaining = 3,
                    registeredByDriver = "Carlos Mendoza",
                    notes = "Extintor de cliente está en taller para prueba hidrostática quinquenal"
                ),
                ExtinguisherLoanEntity(
                    id = "LOAN-002",
                    clientName = "Colegio Los Robles Maracaibo",
                    clientRif = "J-08291029-3",
                    clientPhone = "0261-7410022",
                    clientAddress = "Av. Fuerzas Armadas, Sector Canchancha",
                    loanExtinguisherCode = "NEZ-COMODATO-008",
                    loanTypeDescription = "CO2 10 Lbs Nezco",
                    clientOriginalExtinguisherCode = "CLI-AMER-4421 (CO2 10 Lbs Amerex)",
                    loanDate = "2026-08-01",
                    commitmentReturnDate = "2026-08-08",
                    status = LoanStatus.VENCIDO_ALERTA,
                    daysRemaining = -6,
                    registeredByDriver = "Carlos Mendoza",
                    notes = "¡Alerta de vencimiento! Orden de taller lista hace 4 días. Programar retorno."
                )
            )
            dao.insertLoans(initialLoans)

            // 5. Workshop Orders (Taller de Recarga)
            val initialWorkshopOrders = listOf(
                WorkshopOrderEntity(
                    id = "WKP-101",
                    orderNumber = "TAL-2026-089",
                    clientName = "Panadería Flor de Maracaibo, C.A.",
                    clientRif = "J-31982736-0",
                    extinguisherType = "PQS Polvo Químico Seco (ABC)",
                    capacity = "10 Lbs (4.5 Kg)",
                    serialNumber = "CLI-BUCK-9912",
                    cylinderYear = 2018,
                    lastHydrostaticYear = 2018,
                    serviceType = "Recarga Anual + Prueba Hidrostática Quinquenal COVENIN",
                    stage = WorkshopStage.PRUEBA_HIDROSTATICA,
                    technicianName = "José Gregorio Colina",
                    coveninStandard = "COVENIN 1040 / 1114",
                    partsConsumedSummary = "4.5 Kg Polvo PQS 75%, Vástago válvula, O-ring nitrilo, Marbete 2026",
                    totalCostUsd = 16.50,
                    receivedDate = "2026-08-10",
                    targetDeliveryDate = "2026-08-16",
                    passedHydrostaticTest = true,
                    newInspectionTagNumber = "MARB-2026-0941"
                ),
                WorkshopOrderEntity(
                    id = "WKP-102",
                    orderNumber = "TAL-2026-090",
                    clientName = "Hotel Kristoff Bella Vista",
                    clientRif = "J-07001928-1",
                    extinguisherType = "Dióxido de Carbono CO2",
                    capacity = "15 Lbs (6.8 Kg)",
                    serialNumber = "KRIS-EXT-04",
                    cylinderYear = 2021,
                    lastHydrostaticYear = 2021,
                    serviceType = "Recarga Gas CO2 + Mantenimiento Válvula de Descarga",
                    stage = WorkshopStage.SELLADO_MARBETE,
                    technicianName = "José Gregorio Colina",
                    coveninStandard = "COVENIN 1114",
                    partsConsumedSummary = "6.8 Kg Gas CO2 Líquido Criogénico, Disco de ruptura 3000 PSI",
                    totalCostUsd = 24.00,
                    receivedDate = "2026-08-11",
                    targetDeliveryDate = "2026-08-15",
                    passedHydrostaticTest = true,
                    newInspectionTagNumber = "MARB-2026-0942"
                ),
                WorkshopOrderEntity(
                    id = "WKP-103",
                    orderNumber = "TAL-2026-091",
                    clientName = "Colegio Los Robles Maracaibo",
                    clientRif = "J-08291029-3",
                    extinguisherType = "Dióxido de Carbono CO2",
                    capacity = "10 Lbs (4.5 Kg)",
                    serialNumber = "CLI-AMER-4421",
                    cylinderYear = 2019,
                    lastHydrostaticYear = 2019,
                    serviceType = "Recarga y Certificación de Operatividad",
                    stage = WorkshopStage.LISTO_ENTREGA,
                    technicianName = "José Gregorio Colina",
                    coveninStandard = "COVENIN 1114",
                    partsConsumedSummary = "4.5 Kg CO2, Corneta difusora nueva, Precinto de seguridad",
                    totalCostUsd = 20.00,
                    receivedDate = "2026-08-01",
                    targetDeliveryDate = "2026-08-08",
                    completedDate = "2026-08-08",
                    passedHydrostaticTest = true,
                    newInspectionTagNumber = "MARB-2026-0899"
                )
            )
            dao.insertWorkshopOrders(initialWorkshopOrders)

            // 6. Route Expenses (Chofer)
            val initialExpenses = listOf(
                RouteExpenseEntity(
                    id = "EXP-001",
                    routeId = "ROUTE-2026-08-14",
                    driverName = "Carlos Mendoza",
                    category = ExpenseCategory.COMBUSTIBLE,
                    description = "Gasoil 40 Litros para Camión Ford Cargo 815 en E/S Las Banderas",
                    amountUsd = 20.00,
                    amountBs = 1370.00,
                    date = "2026-08-14 08:15 AM",
                    receiptPhotoAttached = true,
                    status = ExpenseStatus.PENDIENTE_APROBACION
                ),
                RouteExpenseEntity(
                    id = "EXP-002",
                    routeId = "ROUTE-2026-08-14",
                    driverName = "Carlos Mendoza",
                    category = ExpenseCategory.PEAJES,
                    description = "Peaje Puente Gral. Rafael Urdaneta (Punta Iguana)",
                    amountUsd = 3.50,
                    amountBs = 239.75,
                    date = "2026-08-14 09:30 AM",
                    receiptPhotoAttached = true,
                    status = ExpenseStatus.APROBADO,
                    approvedBy = "Super Admin"
                )
            )
            dao.insertExpenses(initialExpenses)

            // 7. Venezuelan LOTTT Payroll Employees
            val initialEmployees = listOf(
                PayrollEmployeeEntity(
                    id = "EMP-001",
                    cedula = "V-18.492.301",
                    fullName = "Carlos Eduardo Mendoza",
                    jobTitle = "Chofer de Despacho y Logística",
                    roleType = NezcoRole.CHOFER,
                    baseSalaryUsd = 280.00,
                    cestaticketUsd = 40.00,
                    bankAccount = "0102-0192-88-0001928374 (Banco de Venezuela)",
                    hireDate = "2023-03-15",
                    yearsOfService = 3.4,
                    monthlyIvssUsd = 11.20,
                    monthlyFaovUsd = 2.80,
                    monthlyIncesUsd = 1.40,
                    accumulatedSeveranceUsd = 512.40,
                    pendingVacationDays = 18
                ),
                PayrollEmployeeEntity(
                    id = "EMP-002",
                    cedula = "V-15.920.114",
                    fullName = "José Gregorio Colina",
                    jobTitle = "Técnico Especialista de Taller y Recarga",
                    roleType = NezcoRole.TALLER,
                    baseSalaryUsd = 320.00,
                    cestaticketUsd = 40.00,
                    bankAccount = "0105-0021-44-1029384756 (Banco Mercantil)",
                    hireDate = "2021-06-01",
                    yearsOfService = 5.2,
                    monthlyIvssUsd = 12.80,
                    monthlyFaovUsd = 3.20,
                    monthlyIncesUsd = 1.60,
                    accumulatedSeveranceUsd = 890.00,
                    pendingVacationDays = 21
                ),
                PayrollEmployeeEntity(
                    id = "EMP-003",
                    cedula = "V-20.194.882",
                    fullName = "Ana Valentina Rincón",
                    jobTitle = "Asesora de Ventas Técnicas y Cotizaciones",
                    roleType = NezcoRole.VENTA,
                    baseSalaryUsd = 300.00,
                    cestaticketUsd = 40.00,
                    bankAccount = "0134-0821-33-0192837465 (Banesco)",
                    hireDate = "2022-09-10",
                    yearsOfService = 3.9,
                    monthlyIvssUsd = 12.00,
                    monthlyFaovUsd = 3.00,
                    monthlyIncesUsd = 1.50,
                    accumulatedSeveranceUsd = 620.00,
                    pendingVacationDays = 16
                ),
                PayrollEmployeeEntity(
                    id = "EMP-004",
                    cedula = "V-17.883.190",
                    fullName = "Luis Fernando Pirela",
                    jobTitle = "Jefe de Almacén y Control de Stock",
                    roleType = NezcoRole.ALMACENISTA,
                    baseSalaryUsd = 310.00,
                    cestaticketUsd = 40.00,
                    bankAccount = "0114-0291-77-2019283741 (Bancaribe)",
                    hireDate = "2022-01-15",
                    yearsOfService = 4.6,
                    monthlyIvssUsd = 12.40,
                    monthlyFaovUsd = 3.10,
                    monthlyIncesUsd = 1.55,
                    accumulatedSeveranceUsd = 745.00,
                    pendingVacationDays = 19
                )
            )
            dao.insertEmployees(initialEmployees)

            // 8. Audit Logs
            val initialLogs = listOf(
                AuditLogEntity(
                    timestamp = "2026-08-14 08:30 AM",
                    userName = "Despachador Central",
                    role = NezcoRole.DESPACHADOR,
                    action = "ASIGNACIÓN_RUTA",
                    module = "Logística y Despacho",
                    details = "Ruta RUTA-MCBO-NORTE-01 asignada a Chofer Carlos Mendoza (Camión Ford Cargo 815)."
                ),
                AuditLogEntity(
                    timestamp = "2026-08-14 09:45 AM",
                    userName = "Carlos Mendoza",
                    role = NezcoRole.CHOFER,
                    action = "ENTREGA_COMPLETADA",
                    module = "Chofer Móvil",
                    details = "Parada Farmacia Saas entregada con firma y foto de evidencia."
                ),
                AuditLogEntity(
                    timestamp = "2026-08-14 10:15 AM",
                    userName = "Super Admin",
                    role = NezcoRole.SUPER_ADMIN,
                    action = "APROBACIÓN_GASTO",
                    module = "Gastos de Ruta",
                    details = "Gasto EXP-002 (Peaje $3.50) aprobado para Chofer Carlos Mendoza."
                )
            )
            initialLogs.forEach { dao.insertAuditLog(it) }

            // 9. System Config
            dao.insertSystemConfig(
                SystemConfigEntity(
                    id = 1,
                    bcvRateBs = 68.50,
                    companyName = "Grupo Nezco Venezuela, C.A.",
                    companyRif = "J-40982341-2",
                    ivssEmployeeRatePercent = 4.0,
                    faovEmployeeRatePercent = 1.0,
                    incesEmployeeRatePercent = 0.5,
                    standardCestaticketUsd = 40.0,
                    alertLoanDaysThreshold = 7
                )
            )
        }
    }
