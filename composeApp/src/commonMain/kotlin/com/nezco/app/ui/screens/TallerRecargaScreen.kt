package com.nezco.app.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.random.Random
import com.nezco.app.data.model.*
import com.nezco.app.ui.theme.*
import com.nezco.app.ui.viewmodel.NezcoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TallerRecargaScreen(
    viewModel: NezcoViewModel
) {
    val workshopOrders by viewModel.workshopOrders.collectAsStateWithLifecycle()
    var selectedFilterStage by remember { mutableStateOf<WorkshopStage?>(null) }
    var showNewOrderDialog by remember { mutableStateOf(false) }

    val filteredOrders = remember(workshopOrders, selectedFilterStage) {
        if (selectedFilterStage == null) workshopOrders else workshopOrders.filter { it.stage == selectedFilterStage }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewOrderDialog = true },
                containerColor = NezcoNavy,
                contentColor = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Nueva Orden")
                    Text("Ingresar Extintor", fontWeight = FontWeight.Bold)
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
            // Header Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Taller Central de Recarga & Mantenimiento",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "Normas COVENIN 1040 (PQS) • 1114 (CO2) • 751",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 11.sp
                                )
                            }
                            Surface(
                                color = Color(0xFFEA580C),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "${workshopOrders.size} Órdenes",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Stage Filter Pills
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedFilterStage == null,
                            onClick = { selectedFilterStage = null },
                            label = { Text("Todas las Etapas (${workshopOrders.size})") }
                        )
                    }
                    items(WorkshopStage.values()) { stage ->
                        val count = workshopOrders.count { it.stage == stage }
                        FilterChip(
                            selected = selectedFilterStage == stage,
                            onClick = { selectedFilterStage = if (selectedFilterStage == stage) null else stage },
                            label = { Text("${stage.label} ($count)") }
                        )
                    }
                }
            }

            // Workshop Orders Cards
            items(filteredOrders) { order ->
                WorkshopOrderCard(
                    order = order,
                    formattedCost = viewModel.formatPrice(order.totalCostUsd),
                    onAdvanceStage = { viewModel.advanceWorkshopStage(order) }
                )
            }
        }
    }

    if (showNewOrderDialog) {
        NewWorkshopOrderDialog(
            onDismiss = { showNewOrderDialog = false },
            onConfirm = { name, rif, type, cap, serial, year, service ->
                viewModel.createWorkshopOrder(name, rif, type, cap, serial, year, service)
                showNewOrderDialog = false
            }
        )
    }
}

@Composable
fun WorkshopOrderCard(
    order: WorkshopOrderEntity,
    formattedCost: String,
    onAdvanceStage: () -> Unit
) {
    val isComplete = order.stage == WorkshopStage.LISTO_ENTREGA || order.stage == WorkshopStage.ENTREGADO_CLIENTE

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        color = Color(0xFFEA580C).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = order.orderNumber,
                            color = Color(0xFFEA580C),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = "Recibido: ${order.receivedDate}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = if (isComplete) NezcoGreenContainer else NezcoAmberContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = order.stage.label,
                        color = if (isComplete) NezcoGreen else NezcoAmber,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${order.extinguisherType} • ${order.capacity}",
                fontWeight = FontWeight.Black,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Cliente: ${order.clientName} (${order.clientRif})",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "Serial: ${order.serialNumber} • Cilindro Año: ${order.cylinderYear} • Norma: ${order.coveninStandard}",
                fontSize = 11.sp,
                color = NezcoNavyLight
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Step Indicator Bar
            LinearProgressIndicator(
                progress = { order.stage.stepNumber / 7f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (isComplete) NezcoGreen else Color(0xFFEA580C),
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Technical details box
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "🛠️ Servicio: ${order.serviceType}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "Insumos: ${order.partsConsumedSummary}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🏷️ Marbete: ${order.newInspectionTagNumber}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NezcoNavyLight
                        )
                        Text(
                            text = "Técnico: ${order.technicianName}",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Costo Servicio:", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(formattedCost, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NezcoNavy)
                }

                if (order.stage != WorkshopStage.ENTREGADO_CLIENTE) {
                    Button(
                        onClick = onAdvanceStage,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA580C)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.FastForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when (order.stage) {
                                WorkshopStage.RECIBIDO -> "INICIAR DESPRESURIZADO"
                                WorkshopStage.DESPRESURIZADO -> "PASAR A PRUEBA HIDRO"
                                WorkshopStage.PRUEBA_HIDROSTATICA -> "RECARGAR POLVO/GAS"
                                WorkshopStage.RECARGA_POLVO_GAS -> "SELLAR & COLOCAR MARBETE"
                                WorkshopStage.SELLADO_MARBETE -> "MARCAR LISTO ENTREGA"
                                WorkshopStage.LISTO_ENTREGA -> "ENTREGAR A CLIENTE"
                                else -> "COMPLETADO"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NewWorkshopOrderDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, rif: String, type: String, cap: String, serial: String, year: Int, service: String) -> Unit
) {
    var clientName by remember { mutableStateOf("") }
    var clientRif by remember { mutableStateOf("J-") }
    var extinguisherType by remember { mutableStateOf("Extintor PQS Polvo Químico Seco (ABC)") }
    var capacity by remember { mutableStateOf("10 Lbs (4.5 Kg)") }
    var serialNumber by remember { mutableStateOf("") }
    var cylinderYearText by remember { mutableStateOf("2020") }
    var serviceType by remember { mutableStateOf("Recarga Anual + Cambio de Sellos & Marbete") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(imageVector = Icons.Default.Build, contentDescription = null, tint = Color(0xFFEA580C))
                Text("Ingresar Extintor a Taller", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        label = { Text("Cliente / Empresa") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = clientRif,
                        onValueChange = { clientRif = it },
                        label = { Text("RIF Cliente") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = extinguisherType,
                        onValueChange = { extinguisherType = it },
                        label = { Text("Tipo de Agente Extintor") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = capacity,
                        onValueChange = { capacity = it },
                        label = { Text("Capacidad") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = serialNumber,
                        onValueChange = { serialNumber = it },
                        label = { Text("Serial del Cilindro") },
                        placeholder = { Text("Ej. BUCK-19283") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = cylinderYearText,
                        onValueChange = { cylinderYearText = it },
                        label = { Text("Año de Fabricación del Cilindro") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = serviceType,
                        onValueChange = { serviceType = it },
                        label = { Text("Servicio Requerido (COVENIN)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val year = cylinderYearText.toIntOrNull() ?: 2020
                    onConfirm(
                        clientName.ifBlank { "Cliente Industrial" },
                        clientRif.ifBlank { "J-00000000-0" },
                        extinguisherType,
                        capacity,
                        serialNumber.ifBlank { "SER-${Random.nextInt(100000, 999999)}" }, // TODO: reemplazar por generador de ID único antes de producción con persistencia real
                        year,
                        serviceType
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA580C))
            ) {
                Text("INGRESAR A TALLER", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
