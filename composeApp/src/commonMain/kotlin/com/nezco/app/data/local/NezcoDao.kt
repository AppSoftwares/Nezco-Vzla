package com.nezco.app.data.local

import com.nezco.app.data.model.*
import kotlinx.coroutines.flow.Flow

interface NezcoDao {

    // --- Products ---
    fun getAllProducts(): Flow<List<ProductEntity>>

    suspend fun insertProducts(products: List<ProductEntity>)

    suspend fun insertProduct(product: ProductEntity)

    suspend fun updateProduct(product: ProductEntity)

    suspend fun updateStockPrincipal(id: String, newStock: Int)

    // --- Orders ---
    fun getAllOrders(): Flow<List<OrderEntity>>

    suspend fun insertOrders(orders: List<OrderEntity>)

    suspend fun insertOrder(order: OrderEntity)

    suspend fun updateOrder(order: OrderEntity)

    // --- Routes & Stops ---
    fun getAllRoutes(): Flow<List<DispatchRouteEntity>>

    suspend fun insertRoutes(routes: List<DispatchRouteEntity>)

    suspend fun updateRoute(route: DispatchRouteEntity)

    fun getAllDeliveryStops(): Flow<List<DeliveryStopEntity>>

    fun getStopsForRoute(routeId: String): Flow<List<DeliveryStopEntity>>

    suspend fun insertStops(stops: List<DeliveryStopEntity>)

    suspend fun updateStop(stop: DeliveryStopEntity)

    // --- Extinguisher Loans ---
    fun getAllLoans(): Flow<List<ExtinguisherLoanEntity>>

    suspend fun insertLoans(loans: List<ExtinguisherLoanEntity>)

    suspend fun insertLoan(loan: ExtinguisherLoanEntity)

    suspend fun updateLoan(loan: ExtinguisherLoanEntity)

    // --- Workshop Orders ---
    fun getAllWorkshopOrders(): Flow<List<WorkshopOrderEntity>>

    suspend fun insertWorkshopOrders(orders: List<WorkshopOrderEntity>)

    suspend fun insertWorkshopOrder(order: WorkshopOrderEntity)

    suspend fun updateWorkshopOrder(order: WorkshopOrderEntity)

    // --- Route Expenses ---
    fun getAllRouteExpenses(): Flow<List<RouteExpenseEntity>>

    suspend fun insertExpenses(expenses: List<RouteExpenseEntity>)

    suspend fun insertExpense(expense: RouteExpenseEntity)

    suspend fun updateExpense(expense: RouteExpenseEntity)

    // --- Payroll ---
    fun getAllEmployees(): Flow<List<PayrollEmployeeEntity>>

    suspend fun insertEmployees(employees: List<PayrollEmployeeEntity>)

    suspend fun updateEmployee(employee: PayrollEmployeeEntity)

    // --- Audit Logs ---
    fun getAuditLogs(): Flow<List<AuditLogEntity>>

    suspend fun insertAuditLog(log: AuditLogEntity)

    // --- System Config ---
    fun getSystemConfig(): Flow<SystemConfigEntity?>

    suspend fun insertSystemConfig(config: SystemConfigEntity)
}
