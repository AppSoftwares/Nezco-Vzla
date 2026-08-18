package com.nezco.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nezco.app.data.model.*
import com.nezco.app.ui.theme.*
import com.nezco.app.ui.viewmodel.NezcoViewModel

@Composable
fun DashboardScreen(
    viewModel: NezcoViewModel,
    onNavigate: (String) -> Unit
) {
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val products by viewModel.products.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val routes by viewModel.routes.collectAsStateWithLifecycle()
    val loans by viewModel.loans.collectAsStateWithLifecycle()
    val workshopOrders by viewModel.workshopOrders.collectAsStateWithLifecycle()
    val systemConfig by viewModel.systemConfig.collectAsStateWithLifecycle()

    val totalSalesUsd = orders.sumOf { it.totalUsd }
    val activeWorkshopCount = workshopOrders.count { it.stage != WorkshopStage.ENTREGADO_CLIENTE }
    val expiredLoansCount = loans.count { it.status == LoanStatus.VENCIDO_ALERTA }
    val lowStockCount = products.count { it.stockPrincipal <= it.minStock }
    val activeRoute = routes.firstOrNull { it.status == DispatchStatus.EN_RUTA }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp)
    ) {
        // Editorial Hero Metric Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "OPERACIONES · VENEZUELA",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.8.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Panel de Control",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Light,
                                letterSpacing = (-0.5).sp
                            )
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = currentRole.title.uppercase(),
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "VENTAS REGISTRADAS",
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.4.sp
                            )
                            Text(
                                text = viewModel.formatPrice(totalSalesUsd),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Light,
                                fontSize = 26.sp
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "TASA BCV",
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.4.sp
                            )
                            Text(
                                text = "Bs. ${systemConfig?.bcvRateBs ?: 68.50}",
                                color = MaterialTheme.colorScheme.tertiary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

        // Quick Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EditorialKpiCard(
                    modifier = Modifier.weight(1f),
                    title = "TALLER RECARGA",
                    value = "$activeWorkshopCount",
                    subtitle = "Extintores en proceso",
                    icon = Icons.Default.Build,
                    color = MaterialTheme.colorScheme.primary,
                    onClick = { onNavigate("taller") }
                )
                EditorialKpiCard(
                    modifier = Modifier.weight(1f),
                    title = "COMODATOS",
                    value = "${loans.size}",
                    subtitle = if (expiredLoansCount > 0) "¡$expiredLoansCount Vencidos!" else "Al día",
                    icon = Icons.Default.SwapHoriz,
                    color = if (expiredLoansCount > 0) MaterialTheme.colorScheme.primary else EditorialGreen,
                    onClick = { onNavigate("prestamos") }
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EditorialKpiCard(
                    modifier = Modifier.weight(1f),
                    title = "ALERTAS STOCK",
                    value = "$lowStockCount",
                    subtitle = if (lowStockCount > 0) "Mínimo alcanzado" else "Stock óptimo",
                    icon = Icons.Default.WarningAmber,
                    color = if (lowStockCount > 0) MaterialTheme.colorScheme.tertiary else EditorialGreen,
                    onClick = { onNavigate("almacen") }
                )
                EditorialKpiCard(
                    modifier = Modifier.weight(1f),
                    title = "DESPACHOS",
                    value = let {
                        val completed = activeRoute?.completedStops ?: 0
                        val total = activeRoute?.totalStops ?: 0
                        "$completed/$total"
                    },
                    subtitle = activeRoute?.truckPlate ?: "Sin ruta activa",
                    icon = Icons.Default.LocalShipping,
                    color = EditorialBlue,
                    onClick = { onNavigate("chofer_ruta") }
                )
            }
        }

        // Module Direct Action Pills
        item {
            Text(
                text = "ACCIONES RÁPIDAS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    letterSpacing = 1.8.sp
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    EditorialQuickAction(
                        icon = Icons.Default.PointOfSale,
                        label = "Punto de Venta",
                        badge = "POS",
                        onClick = { onNavigate("catalogo") }
                    )
                }
                item {
                    EditorialQuickAction(
                        icon = Icons.Default.AltRoute,
                        label = "Radar GPS",
                        badge = "EN VIVO",
                        onClick = { onNavigate("despacho_radar") }
                    )
                }
                item {
                    EditorialQuickAction(
                        icon = Icons.Default.PostAdd,
                        label = "Ingresar Taller",
                        badge = "COVENIN",
                        onClick = { onNavigate("taller") }
                    )
                }
                item {
                    EditorialQuickAction(
                        icon = Icons.Default.Groups,
                        label = "Nómina Legal",
                        badge = "LOTTT",
                        onClick = { onNavigate("nomina") }
                    )
                }
            }
        }

        // Recent Orders Feed
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ÚLTIMOS PEDIDOS Y FACTURAS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        letterSpacing = 1.8.sp
                    )
                )
                TextButton(onClick = { onNavigate("catalogo") }) {
                    Text("VER TODOS", color = MaterialTheme.colorScheme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
        }

        items(orders.take(3)) { order ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = order.orderNumber,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = order.dateIso,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = order.clientName,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = order.itemsSummary,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            maxLines = 1
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = viewModel.formatPrice(order.totalUsd),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Surface(
                            color = when (order.status) {
                                OrderStatus.ENTREGADO_FACTURADO -> EditorialGreenContainer
                                OrderStatus.EN_DESPACHO -> MaterialTheme.colorScheme.tertiaryContainer
                                else -> MaterialTheme.colorScheme.primaryContainer
                            },
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = order.status.label.uppercase(),
                                color = when (order.status) {
                                    OrderStatus.ENTREGADO_FACTURADO -> EditorialGreen
                                    OrderStatus.EN_DESPACHO -> MaterialTheme.colorScheme.tertiary
                                    else -> MaterialTheme.colorScheme.primary
                                },
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditorialKpiCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.4.sp
                )
                Surface(
                    color = color.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Light,
                fontSize = 26.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = color,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}

@Composable
fun EditorialQuickAction(
    icon: ImageVector,
    label: String,
    badge: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(136.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = badge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
            }
            Text(
                text = label,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 16.sp
            )
        }
    }
}
