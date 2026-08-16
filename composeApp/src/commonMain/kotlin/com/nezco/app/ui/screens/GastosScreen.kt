package com.nezco.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.nezco.app.data.model.*
import com.nezco.app.ui.theme.*
import com.nezco.app.ui.viewmodel.NezcoViewModel

@Composable
fun GastosScreen(
    viewModel: NezcoViewModel
) {
    val expenses by viewModel.expenses.collectAsStateWithLifecycle()
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    var showNewExpenseDialog by remember { mutableStateOf(false) }

    val totalExpensesUsd = expenses.sumOf { it.amountUsd }
    val isSuperOrAdmin = currentRole == NezcoRole.SUPER_ADMIN || currentRole == NezcoRole.ADMIN

    Scaffold(
        containerColor = EditorialBg,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewExpenseDialog = true },
                containerColor = EditorialRed,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Nuevo Gasto")
                    Text("Cargar Gasto", fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
        ) {
            // Header Editorial Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorder)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "CONTROL DE GASTOS · FLOTA",
                                    color = EditorialRed,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.6.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Gastos de Ruta y Viáticos",
                                    color = EditorialTextPrimary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Surface(
                                color = EditorialSurfaceElevated,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "${expenses.size} REGISTROS",
                                    color = EditorialTextMuted,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = EditorialBorderLight)
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("TOTAL GASTOS EN RUTA", color = EditorialTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                                Text(viewModel.formatPrice(totalExpensesUsd), color = EditorialTextPrimary, fontWeight = FontWeight.Light, fontSize = 24.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("APROBADOS", color = EditorialTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                                Text(
                                    "${expenses.count { it.status == ExpenseStatus.APROBADO }} de ${expenses.size}",
                                    color = EditorialGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Section Title
            item {
                Text(
                    text = "HISTORIAL DE COMPROBANTES",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = EditorialTextMuted,
                        letterSpacing = 1.6.sp
                    )
                )
            }

            items(expenses) { expense ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = EditorialSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Surface(
                                    color = when (expense.category) {
                                        ExpenseCategory.COMBUSTIBLE -> EditorialRedContainer
                                        ExpenseCategory.PEAJES -> EditorialBlueContainer
                                        ExpenseCategory.REPARACION_MECANICA -> EditorialAmberContainer
                                        else -> EditorialGreenContainer
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = when (expense.category) {
                                                ExpenseCategory.COMBUSTIBLE -> Icons.Default.LocalGasStation
                                                ExpenseCategory.PEAJES -> Icons.Default.Toll
                                                ExpenseCategory.REPARACION_MECANICA -> Icons.Default.CarRepair
                                                else -> Icons.Default.Restaurant
                                            },
                                            contentDescription = null,
                                            tint = when (expense.category) {
                                                ExpenseCategory.COMBUSTIBLE -> EditorialRed
                                                ExpenseCategory.PEAJES -> EditorialBlue
                                                ExpenseCategory.REPARACION_MECANICA -> EditorialAmber
                                                else -> EditorialGreen
                                            },
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = expense.category.label,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 13.sp,
                                        color = EditorialTextPrimary
                                    )
                                    Text(
                                        text = "${expense.driverName} · ${expense.date}",
                                        fontSize = 10.sp,
                                        color = EditorialTextMuted
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = viewModel.formatPrice(expense.amountUsd),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = EditorialTextPrimary
                                )
                                Surface(
                                    color = when (expense.status) {
                                        ExpenseStatus.APROBADO -> EditorialGreenContainer
                                        ExpenseStatus.PENDIENTE_APROBACION -> EditorialAmberContainer
                                        else -> EditorialRedContainer
                                    },
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = expense.status.label.uppercase(),
                                        color = when (expense.status) {
                                            ExpenseStatus.APROBADO -> EditorialGreen
                                            ExpenseStatus.PENDIENTE_APROBACION -> EditorialAmber
                                            else -> EditorialRed
                                        },
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            color = EditorialSurfaceSub,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = expense.description,
                                fontSize = 11.sp,
                                color = EditorialTextBody,
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                        if (expense.receiptPhotoAttached) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(imageVector = Icons.Default.Attachment, contentDescription = null, tint = EditorialRed, modifier = Modifier.size(12.dp))
                                Text("Comprobante / Ticket de pago digitalizado", fontSize = 10.sp, color = EditorialTextMuted)
                            }
                        }

                        // Approval buttons for Admin
                        if (isSuperOrAdmin && expense.status == ExpenseStatus.PENDIENTE_APROBACION) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { viewModel.approveExpense(expense, false) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = EditorialRed),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, EditorialRed),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("RECHAZAR", fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                                }
                                Button(
                                    onClick = { viewModel.approveExpense(expense, true) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = EditorialGreen),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("APROBAR", fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showNewExpenseDialog) {
        RouteExpenseDialog(
            onDismiss = { showNewExpenseDialog = false },
            onSubmit = { category, description, amountUsd ->
                viewModel.submitRouteExpense(category, description, amountUsd)
                showNewExpenseDialog = false
            }
        )
    }
}
