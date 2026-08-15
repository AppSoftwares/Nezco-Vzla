package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val code: String,
    val name: String,
    val category: ProductCategory,
    val capacity: String,
    val priceUsd: Double,
    val stockPrincipal: Int,
    val stockTaller: Int,
    val stockCamion: Int,
    val minStock: Int,
    val description: String,
    val technicalSpecs: String,
    val isExtinguisher: Boolean = false,
    val imageUrl: String = ""
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val orderNumber: String,
    val clientName: String,
    val clientRif: String,
    val clientPhone: String,
    val clientAddress: String,
    val dateIso: String,
    val itemsSummary: String,
    val totalUsd: Double,
    val status: OrderStatus,
    val paymentStatus: PaymentStatus,
    val paidAmountUsd: Double,
    val assignedDriverName: String,
    val isPosDirectSale: Boolean = false,
    val notes: String = ""
)

@Entity(tableName = "dispatch_routes")
data class DispatchRouteEntity(
    @PrimaryKey val id: String,
    val routeCode: String,
    val driverId: String,
    val driverName: String,
    val truckPlate: String,
    val truckModel: String,
    val date: String,
    val status: DispatchStatus,
    val totalStops: Int,
    val completedStops: Int,
    val currentZone: String,
    val estimatedKm: Double,
    val startedAt: String = "",
    val completedAt: String = ""
)

@Entity(tableName = "delivery_stops")
data class DeliveryStopEntity(
    @PrimaryKey val id: String,
    val routeId: String,
    val stopOrder: Int,
    val clientName: String,
    val clientRif: String,
    val address: String,
    val phone: String,
    val orderId: String,
    val itemsDescription: String,
    val status: DeliveryStopStatus,
    val recipientName: String = "",
    val signatureCaptured: Boolean = false,
    val photoEvidenceCaptured: Boolean = false,
    val returnedItemsDetail: String = "",
    val extinguishersTakenForRecharge: String = "",
    val loanExtinguisherLeftCode: String = "",
    val completedAt: String = "",
    val notes: String = ""
)

@Entity(tableName = "extinguisher_loans")
data class ExtinguisherLoanEntity(
    @PrimaryKey val id: String,
    val clientName: String,
    val clientRif: String,
    val clientPhone: String,
    val clientAddress: String,
    val loanExtinguisherCode: String,
    val loanTypeDescription: String,
    val clientOriginalExtinguisherCode: String,
    val loanDate: String,
    val commitmentReturnDate: String,
    val status: LoanStatus,
    val daysRemaining: Int,
    val returnedDate: String = "",
    val registeredByDriver: String,
    val notes: String = ""
)

@Entity(tableName = "workshop_orders")
data class WorkshopOrderEntity(
    @PrimaryKey val id: String,
    val orderNumber: String,
    val clientName: String,
    val clientRif: String,
    val extinguisherType: String,
    val capacity: String,
    val serialNumber: String,
    val cylinderYear: Int,
    val lastHydrostaticYear: Int,
    val serviceType: String,
    val stage: WorkshopStage,
    val technicianName: String,
    val coveninStandard: String,
    val partsConsumedSummary: String,
    val totalCostUsd: Double,
    val receivedDate: String,
    val targetDeliveryDate: String,
    val completedDate: String = "",
    val passedHydrostaticTest: Boolean = true,
    val newInspectionTagNumber: String = ""
)

@Entity(tableName = "route_expenses")
data class RouteExpenseEntity(
    @PrimaryKey val id: String,
    val routeId: String,
    val driverName: String,
    val category: ExpenseCategory,
    val description: String,
    val amountUsd: Double,
    val amountBs: Double,
    val date: String,
    val receiptPhotoAttached: Boolean = true,
    val status: ExpenseStatus,
    val approvedBy: String = "",
    val rejectionReason: String = ""
)

@Entity(tableName = "payroll_employees")
data class PayrollEmployeeEntity(
    @PrimaryKey val id: String,
    val cedula: String,
    val fullName: String,
    val jobTitle: String,
    val roleType: NezcoRole,
    val baseSalaryUsd: Double,
    val cestaticketUsd: Double,
    val bankAccount: String,
    val hireDate: String,
    val yearsOfService: Double,
    val monthlyIvssUsd: Double,
    val monthlyFaovUsd: Double,
    val monthlyIncesUsd: Double,
    val accumulatedSeveranceUsd: Double,
    val pendingVacationDays: Int
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: String,
    val userName: String,
    val role: NezcoRole,
    val action: String,
    val module: String,
    val details: String
)

@Entity(tableName = "system_config")
data class SystemConfigEntity(
    @PrimaryKey val id: Int = 1,
    val bcvRateBs: Double = 68.50,
    val companyName: String = "Grupo Nezco Venezuela, C.A.",
    val companyRif: String = "J-40982341-2",
    val ivssEmployeeRatePercent: Double = 4.0,
    val faovEmployeeRatePercent: Double = 1.0,
    val incesEmployeeRatePercent: Double = 0.5,
    val standardCestaticketUsd: Double = 40.0,
    val alertLoanDaysThreshold: Int = 7
)
