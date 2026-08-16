package com.nezco.app.data.model

enum class NezcoRole(
    val title: String,
    val subtitle: String,
    val iconName: String
) {
    SUPER_ADMIN(
        title = "Super Administrador",
        subtitle = "Acceso total, auditoría, nómina y configuración",
        iconName = "Security"
    ),
    ADMIN(
        title = "Administrador",
        subtitle = "Gestión operativa, compras, inventario y despachos",
        iconName = "AdminPanelSettings"
    ),
    CHOFER(
        title = "Chofer / Móvil",
        subtitle = "Rutas asignadas, entregas, préstamos y gastos",
        iconName = "LocalShipping"
    ),
    DESPACHADOR(
        title = "Despachador",
        subtitle = "Asignación de rutas y radar GPS de camiones",
        iconName = "AltRoute"
    ),
    VENTA(
        title = "Ventas & Asesoría",
        subtitle = "Catálogo, cotizaciones, pedidos y cobranzas",
        iconName = "Storefront"
    ),
    ALMACENISTA(
        title = "Almacenista",
        subtitle = "Control de stock, entradas, salidas y préstamos",
        iconName = "Inventory2"
    ),
    TALLER(
        title = "Personal de Taller",
        subtitle = "Recargas, pruebas hidrostáticas y repuestos",
        iconName = "Build"
    ),
    POS_LOCAL(
        title = "Asistente POS Local",
        subtitle = "Punto de venta directo en mostrador y facturación",
        iconName = "PointOfSale"
    )
}

enum class ProductCategory(val label: String, val coveninCode: String) {
    PQS(label = "Extintores PQS (ABC)", coveninCode = "COVENIN 1040"),
    CO2(label = "Extintores CO2 (BC)", coveninCode = "COVENIN 1114"),
    AGUA_ESPUMA(label = "Agua y Espuma AFFF", coveninCode = "COVENIN 1040"),
    SOLKAFLAM(label = "Agente Limpio / HFC", coveninCode = "NFPA 2001"),
    GABINETES(label = "Gabinetes y Soportes", coveninCode = "COVENIN 751"),
    MANGUERAS(label = "Mangueras y Pitones", coveninCode = "COVENIN 1330"),
    SENALIZACION(label = "Señalización Fotoluminiscente", coveninCode = "COVENIN 187"),
    DETECCION(label = "Sistemas de Detección y Alarmas", coveninCode = "COVENIN 1041"),
    REPUESTOS_INSUMOS(label = "Repuestos y Polvo Químico", coveninCode = "Nezco Tech")
}

enum class OrderStatus(val label: String) {
    COTIZACION("Cotización"),
    PEDIDO_CONFIRMADO("Pedido Confirmado"),
    EN_DESPACHO("En Despacho"),
    ENTREGADO_FACTURADO("Entregado / Facturado"),
    CANCELADO("Cancelado")
}

enum class PaymentStatus(val label: String) {
    PENDIENTE("Pendiente"),
    ABONO_PARCIAL("Abono Parcial"),
    PAGADO("Pagado Total")
}

enum class DispatchStatus(val label: String) {
    PROGRAMADA("Programada"),
    EN_RUTA("En Ruta Activa"),
    COMPLETADA("Completada")
}

enum class DeliveryStopStatus(val label: String) {
    PENDIENTE("Pendiente"),
    EN_CAMINO("En Camino"),
    ENTREGADO("Entregado con Éxito"),
    DEVOLUCION_PARCIAL("Devolución Registrada"),
    NO_ENTREGADO("No Entregado / Reprogramado")
}

enum class LoanStatus(val label: String) {
    ACTIVO_EN_CLIENTE("En Cliente (Préstamo Activo)"),
    DEVUELTO_A_ALMACEN("Devuelto al Almacén"),
    VENCIDO_ALERTA("Plazo Vencido (Alerta)")
}

enum class WorkshopStage(val label: String, val stepNumber: Int) {
    RECIBIDO("1. Recibido e Inspección", 1),
    DESPRESURIZADO("2. Despresurizado y Limpieza", 2),
    PRUEBA_HIDROSTATICA("3. Prueba Hidrostática", 3),
    RECARGA_POLVO_GAS("4. Recarga Polvo / Gas CO2", 4),
    SELLADO_MARBETE("5. Sellado, Válvula y Marbete", 5),
    LISTO_ENTREGA("6. Listo para Entrega", 6),
    ENTREGADO_CLIENTE("7. Entregado al Cliente", 7)
}

enum class ExpenseCategory(val label: String) {
    COMBUSTIBLE("Combustible (Gasoil / Gasolina)"),
    PEAJES("Peajes y Tasas Viales"),
    REPARACION_MECANICA("Reparación Mecánica / Cauchos"),
    VIATICOS_COMIDA("Viáticos y Alimentación"),
    SERVICIOS_TALLER("Servicios de Taller / Gas Nitrógeno"),
    OFICINA_ADMIN("Gastos Administrativos")
}

enum class ExpenseStatus(val label: String) {
    PENDIENTE_APROBACION("Pendiente Aprobación"),
    APROBADO("Aprobado por Admin"),
    RECHAZADO("Rechazado")
}

enum class WarehouseLocation(val label: String) {
    MARACAIBO_PRINCIPAL("Sede Principal (Maracaibo)"),
    TALLER_CENTRAL("Taller Central de Recarga"),
    CAMION_DESPACHO_01("Móvil Camión #1 (Nezco-01)"),
    CAMION_DESPACHO_02("Móvil Camión #2 (Nezco-02)")
}
