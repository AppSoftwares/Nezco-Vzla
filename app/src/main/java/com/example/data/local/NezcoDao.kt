package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NezcoDao {

    // --- Products ---
    @Query("SELECT * FROM products ORDER BY category ASC, name ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE products SET stockPrincipal = :newStock WHERE id = :id")
    suspend fun updateStockPrincipal(id: String, newStock: Int)

    // --- Orders ---
    @Query("SELECT * FROM orders ORDER BY orderNumber DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<OrderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Update
    suspend fun updateOrder(order: OrderEntity)

    // --- Routes & Stops ---
    @Query("SELECT * FROM dispatch_routes ORDER BY date DESC")
    fun getAllRoutes(): Flow<List<DispatchRouteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutes(routes: List<DispatchRouteEntity>)

    @Update
    suspend fun updateRoute(route: DispatchRouteEntity)

    @Query("SELECT * FROM delivery_stops ORDER BY stopOrder ASC")
    fun getAllDeliveryStops(): Flow<List<DeliveryStopEntity>>

    @Query("SELECT * FROM delivery_stops WHERE routeId = :routeId ORDER BY stopOrder ASC")
    fun getStopsForRoute(routeId: String): Flow<List<DeliveryStopEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStops(stops: List<DeliveryStopEntity>)

    @Update
    suspend fun updateStop(stop: DeliveryStopEntity)

    // --- Extinguisher Loans ---
    @Query("SELECT * FROM extinguisher_loans ORDER BY status ASC, daysRemaining ASC")
    fun getAllLoans(): Flow<List<ExtinguisherLoanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoans(loans: List<ExtinguisherLoanEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoan(loan: ExtinguisherLoanEntity)

    @Update
    suspend fun updateLoan(loan: ExtinguisherLoanEntity)

    // --- Workshop Orders ---
    @Query("SELECT * FROM workshop_orders ORDER BY receivedDate DESC")
    fun getAllWorkshopOrders(): Flow<List<WorkshopOrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkshopOrders(orders: List<WorkshopOrderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkshopOrder(order: WorkshopOrderEntity)

    @Update
    suspend fun updateWorkshopOrder(order: WorkshopOrderEntity)

    // --- Route Expenses ---
    @Query("SELECT * FROM route_expenses ORDER BY date DESC")
    fun getAllRouteExpenses(): Flow<List<RouteExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenses(expenses: List<RouteExpenseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: RouteExpenseEntity)

    @Update
    suspend fun updateExpense(expense: RouteExpenseEntity)

    // --- Payroll ---
    @Query("SELECT * FROM payroll_employees ORDER BY fullName ASC")
    fun getAllEmployees(): Flow<List<PayrollEmployeeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployees(employees: List<PayrollEmployeeEntity>)

    @Update
    suspend fun updateEmployee(employee: PayrollEmployeeEntity)

    // --- Audit Logs ---
    @Query("SELECT * FROM audit_logs ORDER BY id DESC LIMIT 100")
    fun getAuditLogs(): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: AuditLogEntity)

    // --- System Config ---
    @Query("SELECT * FROM system_config WHERE id = 1 LIMIT 1")
    fun getSystemConfig(): Flow<SystemConfigEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSystemConfig(config: SystemConfigEntity)
}
