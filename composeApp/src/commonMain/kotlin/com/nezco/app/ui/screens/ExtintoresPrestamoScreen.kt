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
fun ExtintoresPrestamoScreen(
    viewModel: NezcoViewModel
) {
    val loans by viewModel.loans.collectAsStateWithLifecycle()
    var selectedLoanForReturn by remember { mutableStateOf<ExtinguisherLoanEntity?>(null) }

    val activeLoans = loans.filter { it.status != LoanStatus.DEVUELTO_A_ALMACEN }
    val expiredLoans = loans.filter { it.status == LoanStatus.VENCIDO_ALERTA }

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
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                color = MaterialTheme.colorScheme.tertiary,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.SwapHoriz,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onTertiary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "Extintores en Préstamo (Comodato)",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "Rastreo y alertas de cilindros prestados",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                        if (expiredLoans.isNotEmpty()) {
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "¡${expiredLoans.size} Vencidos!",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Préstamos Activos", color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f), fontSize = 10.sp)
                            Text("${activeLoans.size} Extintores", color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Black, fontSize = 16.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Retornados a Almacén", color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f), fontSize = 10.sp)
                            Text("${loans.count { it.status == LoanStatus.DEVUELTO_A_ALMACEN }} Devueltos", color = EditorialGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "Control de Cilindros Cedidos a Clientes",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        items(loans) { loan ->
            val isExpired = loan.status == LoanStatus.VENCIDO_ALERTA
            val isReturned = loan.status == LoanStatus.DEVUELTO_A_ALMACEN

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isExpired) MaterialTheme.colorScheme.primaryContainer else if (isReturned) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = if (isExpired) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = loan.clientName,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "RIF: ${loan.clientRif} • Tlf: ${loan.clientPhone}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            color = when (loan.status) {
                                LoanStatus.VENCIDO_ALERTA -> NezcoSafetyRed
                                LoanStatus.DEVUELTO_A_ALMACEN -> NezcoGreen
                                else -> NezcoAmber
                            },
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = loan.status.label,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("🔁 Extintor Cedido Nezco:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(loan.loanExtinguisherCode, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("📥 Extintor Cliente Retirado:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(loan.clientOriginalExtinguisherCode, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("📅 Fecha Préstamo: ${loan.loanDate}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("🎯 Compromiso: ${loan.commitmentReturnDate}", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    if (isExpired) {
                        val expiredDays = -loan.daysRemaining
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "⚠️ ATENCIÓN: El plazo de devolución expiró hace $expiredDays días. Coordinar retiro de comodato o entrega de extintor recargado.",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (!isReturned) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { selectedLoanForReturn = loan },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EditorialGreen),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("REGISTRAR RETORNO A ALMACÉN", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }

    if (selectedLoanForReturn != null) {
        val loan = selectedLoanForReturn!!
        AlertDialog(
            onDismissRequest = { selectedLoanForReturn = null },
            title = {
                Text("Confirmar Devolución de Comodato", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Text(
                    text = "¿Confirmas que el extintor '${loan.loanExtinguisherCode}' fue devuelto por el cliente '${loan.clientName}' y reintegrado físicamente al almacén?",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.markLoanAsReturned(loan)
                        selectedLoanForReturn = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NezcoGreen)
                ) {
                    Text("SÍ, CONFIRMAR RETORNO", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedLoanForReturn = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
