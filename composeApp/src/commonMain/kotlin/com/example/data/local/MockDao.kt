package com.example.data.local

import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockDao : NezcoDao {
    override fun getAllProducts(): Flow<List<ProductEntity>> = flowOf(emptyList())
    override suspend fun insertProducts(products: List<ProductEntity>) {}
    override suspend fun insertProduct(product: ProductEntity) {}
    override suspend fun updateProduct(product: ProductEntity) {}
    override suspend fun updateStockPrincipal(id: String, newStock: Int) {}
    override fun getAllOrders(): Flow<List<OrderEntity>> = flowOf(emptyList())
    override suspend fun insertOrders(orders: List<OrderEntity>) {}
    override suspend fun insertOrder(order: OrderEntity) {}
    override suspend fun updateOrder(order: OrderEntity) {}
    override fun getAllRoutes(): Flow<List<DispatchRouteEntity>> = flowOf(emptyList())
    override suspend fun insertRoutes(routes: List<DispatchRouteEntity>) {}
    override suspend fun updateRoute(route: DispatchRouteEntity) {}
    override fun getAllDeliveryStops(): Flow<List<DeliveryStopEntity>> = flowOf(emptyList())
    override fun getStopsForRoute(routeId: String): Flow<List<DeliveryStopEntity>> = flowOf(emptyList())
    override suspend fun insertStops(stops: List<DeliveryStopEntity>) {}
    override suspend fun updateStop(stop: DeliveryStopEntity) {}
    override fun getAllLoans(): Flow<List<ExtinguisherLoanEntity>> = flowOf(emptyList())
    override suspend fun insertLoans(loans: List<ExtinguisherLoanEntity>) {}
    override suspend fun insertLoan(loan: ExtinguisherLoanEntity) {}
    override suspend fun updateLoan(loan: ExtinguisherLoanEntity) {}
    override fun getAllWorkshopOrders(): Flow<List<WorkshopOrderEntity>> = flowOf(emptyList())
    override suspend fun insertWorkshopOrders(orders: List<WorkshopOrderEntity>) {}
    override suspend fun insertWorkshopOrder(order: WorkshopOrderEntity) {}
    override suspend fun updateWorkshopOrder(order: WorkshopOrderEntity) {}
    override fun getAllRouteExpenses(): Flow<List<RouteExpenseEntity>> = flowOf(emptyList())
    override suspend fun insertExpenses(expenses: List<RouteExpenseEntity>) {}
    override suspend fun insertExpense(expense: RouteExpenseEntity) {}
    override suspend fun updateExpense(expense: RouteExpenseEntity) {}
    override fun getAllEmployees(): Flow<List<PayrollEmployeeEntity>> = flowOf(emptyList())
    override suspend fun insertEmployees(employees: List<PayrollEmployeeEntity>) {}
    override suspend fun updateEmployee(employee: PayrollEmployeeEntity) {}
    override fun getAuditLogs(): Flow<List<AuditLogEntity>> = flowOf(emptyList())
    override suspend fun insertAuditLog(log: AuditLogEntity) {}
    override fun getSystemConfig(): Flow<SystemConfigEntity?> = flowOf(null)
    override suspend fun insertSystemConfig(config: SystemConfigEntity) {}
}
