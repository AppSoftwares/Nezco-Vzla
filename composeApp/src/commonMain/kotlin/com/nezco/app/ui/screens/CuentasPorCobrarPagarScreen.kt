package com.nezco.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nezco.app.data.model.OrderEntity
import com.nezco.app.data.model.PaymentStatus
import com.nezco.app.ui.theme.*
import com.nezco.app.ui.viewmodel.NezcoViewModel

@Composable
fun CuentasPorCobrarPagarScreen(
    viewModel: NezcoViewModel
) {
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(0) } // 0: CxC Clientes, 1: CxP Proveedores
    var orderForPayment by remember { mutableStateOf<OrderEntity?>(null) }

    val pendingCxCOrders = orders.filter { it.paymentStatus != PaymentStatus.PAGADO }
    val totalPendingCxCUsd = pendingCxCOrders.sumOf { it.totalUsd - it.paidAmountUsd }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        // Banner Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NezcoNavy)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Gestión Financiera CxC & CxP",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Cuentas por Cobrar (Clientes) y Cuentas por Pagar (Proveedores)",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Por Cobrar a Clientes (CxC)", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                            Text(viewModel.formatPrice(totalPendingCxCUsd), color = NezcoAmberGold, fontWeight = FontWeight.Black, fontSize = 18.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("CxP Proveedores (Insumos)", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                            Text(viewModel.formatPrice(2350.0), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }

        // Tabs
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = NezcoNavy
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("CxC Clientes (${pendingCxCOrders.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("CxP Proveedores (3)", fontWeight = FontWeight.Bold) }
                )
            }
        }

        if (selectedTab == 0) {
            // CxC List
            if (pendingCxCOrders.isEmpty()) {
                item {
                    Text(
                        text = "¡Excelente! No hay facturas de clientes pendientes por cobrar.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = NezcoGreen, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(pendingCxCOrders) { order ->
                    val debtUsd = order.totalUsd - order.paidAmountUsd

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = order.clientName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Factura: ${order.orderNumber} • Fecha: ${order.dateIso}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Surface(
                                    color = if (order.paymentStatus == PaymentStatus.ABONO_PARCIAL) NezcoAmberContainer else NezcoSafetyRedContainer,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = order.paymentStatus.label,
                                        color = if (order.paymentStatus == PaymentStatus.ABONO_PARCIAL) NezcoAmber else NezcoSafetyRed,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Factura: ${viewModel.formatPrice(order.totalUsd)}", fontSize = 11.sp)
                                Text("Abonado: ${viewModel.formatPrice(order.paidAmountUsd)}", fontSize = 11.sp, color = NezcoGreen, fontWeight = FontWeight.SemiBold)
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Saldo Pendiente: ${viewModel.formatPrice(debtUsd)}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    color = NezcoSafetyRed
                                )

                                Button(
                                    onClick = { orderForPayment = order },
                                    colors = ButtonDefaults.buttonColors(containerColor = NezcoGreen),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.AddCard, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("REGISTRAR PAGO", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // CxP Mock Items
            val supplierItems = listOf(
                Triple("Químicos Industriales del Zulia C.A.", "Polvo Químico Seco PQS 90% ABC (100 Saco)", 1250.0),
                Triple("Válvulas & Conexiones Maracaibo", "Manómetros 195 PSI, Mangueras y Sellos", 680.0),
                Triple("Gases de Occidente C.A.", "Nitrógeno Alta Pureza y Cargas CO2 Líquido", 420.0)
            )

            items(supplierItems) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.first,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = viewModel.formatPrice(item.third),
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = NezcoNavy
                            )
                        }
                        Text(
                            text = "Concepto: ${item.second}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Condición: Crédito 30 Días", fontSize = 10.sp, color = NezcoAmber, fontWeight = FontWeight.SemiBold)
                            Surface(
                                color = NezcoAmberContainer,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text("PENDIENTE POR PAGAR", color = NezcoAmber, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    if (orderForPayment != null) {
        val order = orderForPayment!!
        var paymentAmountText by remember { mutableStateOf((order.totalUsd - order.paidAmountUsd).toString()) }

        AlertDialog(
            onDismissRequest = { orderForPayment = null },
            title = {
                Text("Registrar Cobro / Abono", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Cliente: ${order.clientName}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Total Factura: ${viewModel.formatPrice(order.totalUsd)}", fontSize = 12.sp)
                    val remainingBalance = order.totalUsd - order.paidAmountUsd
                    Text("Saldo Restante: ${viewModel.formatPrice(remainingBalance)}", fontSize = 12.sp, color = NezcoSafetyRed, fontWeight = FontWeight.Bold)

                    OutlinedTextField(
                        value = paymentAmountText,
                        onValueChange = { paymentAmountText = it },
                        label = { Text("Monto a Abonar ($ USD)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = paymentAmountText.toDoubleOrNull() ?: 0.0
                        if (amount > 0) {
                            viewModel.registerOrderPayment(order, amount)
                            orderForPayment = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NezcoGreen)
                ) {
                    Text("CONFIRMAR ABONO", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { orderForPayment = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
