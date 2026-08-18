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
import com.nezco.app.data.model.PayrollEmployeeEntity
import com.nezco.app.ui.theme.*
import com.nezco.app.ui.viewmodel.NezcoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NominaLotttScreen(
    viewModel: NezcoViewModel
) {
    val employees by viewModel.employees.collectAsStateWithLifecycle()
    val systemConfig by viewModel.systemConfig.collectAsStateWithLifecycle()

    var selectedEmployeeForPayslip by remember { mutableStateOf<PayrollEmployeeEntity?>(null) }
    var showConfigModal by remember { mutableStateOf(false) }

    val bcvRate = systemConfig?.bcvRateBs ?: 68.50
    val cestaticket = systemConfig?.standardCestaticketUsd ?: 40.0
    val totalNetPayrollUsd = employees.sumOf { emp ->
        val ivss = emp.baseSalaryUsd * ((systemConfig?.ivssEmployeeRatePercent ?: 4.0) / 100.0)
        val faov = emp.baseSalaryUsd * ((systemConfig?.faovEmployeeRatePercent ?: 1.0) / 100.0)
        val inces = emp.baseSalaryUsd * ((systemConfig?.incesEmployeeRatePercent ?: 0.5) / 100.0)
        emp.baseSalaryUsd + cestaticket - (ivss + faov + inces)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp)
    ) {
        // Hero Card LOTTT
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "LEGISLACIÓN LABORAL · LOTTT",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.6.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Nómina Legal Venezolana",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        IconButton(
                            onClick = { showConfigModal = true },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(12.dp))
                                .size(38.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Tune, contentDescription = "Parámetros", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("TOTAL NÓMINA MENSUAL", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                            Text(viewModel.formatPrice(totalNetPayrollUsd), color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Light, fontSize = 24.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("TRABAJADORES", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                            Text("${employees.size} Activos", color = EditorialGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "RECIBOS DE PAGO DE TRABAJADORES",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    letterSpacing = 1.6.sp
                )
            )
        }

        // Employee Cards
        items(employees) { emp ->
            val ivss = emp.baseSalaryUsd * ((systemConfig?.ivssEmployeeRatePercent ?: 4.0) / 100.0)
            val faov = emp.baseSalaryUsd * ((systemConfig?.faovEmployeeRatePercent ?: 1.0) / 100.0)
            val inces = emp.baseSalaryUsd * ((systemConfig?.incesEmployeeRatePercent ?: 0.5) / 100.0)
            val totalDeductions = ivss + faov + inces
            val netPayUsd = emp.baseSalaryUsd + cestaticket - totalDeductions

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { selectedEmployeeForPayslip = emp },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = emp.fullName,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${emp.jobTitle} · CI: ${emp.cedula}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = emp.roleType.title.uppercase(),
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("SALARIO BASE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f), letterSpacing = 0.8.sp)
                                Text(viewModel.formatPrice(emp.baseSalaryUsd), fontWeight = FontWeight.Medium, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("CESTATICKET", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f), letterSpacing = 0.8.sp)
                                Text(viewModel.formatPrice(cestaticket), fontWeight = FontWeight.Medium, fontSize = 12.sp, color = EditorialGreen)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("NETO A COBRAR", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f), letterSpacing = 0.8.sp)
                                Text(viewModel.formatPrice(netPayUsd), fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Garantía Prestaciones: ${viewModel.formatPrice(emp.accumulatedSeveranceUsd)}",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "Vacaciones: ${emp.pendingVacationDays} días",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }

    // Individual Payslip Bottom Sheet
    if (selectedEmployeeForPayslip != null) {
        val emp = selectedEmployeeForPayslip!!
        val ivss = emp.baseSalaryUsd * ((systemConfig?.ivssEmployeeRatePercent ?: 4.0) / 100.0)
        val faov = emp.baseSalaryUsd * ((systemConfig?.faovEmployeeRatePercent ?: 1.0) / 100.0)
        val inces = emp.baseSalaryUsd * ((systemConfig?.incesEmployeeRatePercent ?: 0.5) / 100.0)
        val totalDeductions = ivss + faov + inces
        val netPayUsd = emp.baseSalaryUsd + cestaticket - totalDeductions

        ModalBottomSheet(
            onDismissRequest = { selectedEmployeeForPayslip = null },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("GRUPO NEZCO VENEZUELA, C.A.", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, letterSpacing = 1.sp)
                        Text("Recibo Oficial de Pago LOTTT", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
                    }
                    Surface(color = EditorialGreenContainer, shape = RoundedCornerShape(6.dp)) {
                        Text("MENSUAL", color = EditorialGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }

                Divider(color = MaterialTheme.colorScheme.outlineVariant)

                Text("Trabajador: ${emp.fullName} (${emp.cedula})", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                Text("Cargo: ${emp.jobTitle} · Ingreso: ${emp.hireDate}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))

                // Asignaciones
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("➕ ASIGNACIONES:", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = EditorialGreen, letterSpacing = 1.sp)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Salario Base Mensual", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(viewModel.formatPrice(emp.baseSalaryUsd), fontSize = 11.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Cestaticket Alimentación (Decreto)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(viewModel.formatPrice(cestaticket), fontSize = 11.sp, fontWeight = FontWeight.Medium, color = EditorialGreen)
                        }
                    }
                }

                // Deducciones
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("➖ DEDUCCIONES LEGALES (LOTTT):", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, letterSpacing = 1.sp)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Seguro Social (IVSS 4%)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("- ${viewModel.formatPrice(ivss)}", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Ahorro Habitacional (FAOV 1%)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("- ${viewModel.formatPrice(faov)}", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("INCES Trabajador (0.5%)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("- ${viewModel.formatPrice(inces)}", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                Divider(color = MaterialTheme.colorScheme.outlineVariant)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("NETO A PAGAR:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, letterSpacing = 1.sp)
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$${netPayUsd.formatTo2Decimals()} USD",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Bs. ${(netPayUsd * bcvRate).formatTo2Decimals()} (BCV ${bcvRate.formatTo2Decimals()})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

    // Config Legal Parameters Modal
    if (showConfigModal) {
        var ivssInput by remember { mutableStateOf((systemConfig?.ivssEmployeeRatePercent ?: 4.0).toString()) }
        var faovInput by remember { mutableStateOf((systemConfig?.faovEmployeeRatePercent ?: 1.0).toString()) }
        var incesInput by remember { mutableStateOf((systemConfig?.incesEmployeeRatePercent ?: 0.5).toString()) }
        var cstaticketInput by remember { mutableStateOf((systemConfig?.standardCestaticketUsd ?: 40.0).toString()) }
        var bcvInput by remember { mutableStateOf((systemConfig?.bcvRateBs ?: 68.50).toString()) }

        AlertDialog(
            onDismissRequest = { showConfigModal = false },
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text("Parámetros Laborales LOTTT & BCV", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = bcvInput,
                        onValueChange = { bcvInput = it },
                        label = { Text("Tasa Oficial BCV (Bs. / USD)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = cstaticketInput,
                        onValueChange = { cstaticketInput = it },
                        label = { Text("Cestaticket Alimentación ($ USD)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = ivssInput,
                        onValueChange = { ivssInput = it },
                        label = { Text("Deducción IVSS Trabajador (%)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = faovInput,
                        onValueChange = { faovInput = it },
                        label = { Text("Deducción FAOV Trabajador (%)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = incesInput,
                        onValueChange = { incesInput = it },
                        label = { Text("Deducción INCES Trabajador (%)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val bcv = bcvInput.toDoubleOrNull() ?: 68.50
                        val ivss = ivssInput.toDoubleOrNull() ?: 4.0
                        val faov = faovInput.toDoubleOrNull() ?: 1.0
                        val inces = incesInput.toDoubleOrNull() ?: 0.5
                        val cesta = cstaticketInput.toDoubleOrNull() ?: 40.0

                        viewModel.updateBcvRate(bcv)
                        viewModel.updateLegalParameters(ivss, faov, inces, cesta)
                        showConfigModal = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("GUARDAR PARÁMETROS", fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 1.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfigModal = false }) {
                    Text("CANCELAR", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
                }
            }
        )
    }
}

private fun Double.formatTo2Decimals(): String {
    val rounded = kotlin.math.round(this * 100) / 100.0
    val intPart = rounded.toLong()
    val decimalDigits = kotlin.math.abs(((rounded - intPart) * 100).toInt())
    val decimalStr = decimalDigits.toString().padStart(2, '0')
    return "$intPart.$decimalStr"
}
