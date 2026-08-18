package com.nezco.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nezco.app.data.model.*
import com.nezco.app.ui.theme.*
import com.nezco.app.ui.viewmodel.NezcoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoferRouteScreen(
    viewModel: NezcoViewModel,
    onNavigateToExpenses: () -> Unit
) {
    val routes by viewModel.routes.collectAsStateWithLifecycle()
    val stops by viewModel.deliveryStops.collectAsStateWithLifecycle()
    val activeRoute = routes.firstOrNull()
    val nextStop = stops.firstOrNull { it.status != DeliveryStopStatus.ENTREGADO } ?: stops.firstOrNull()

    var activeStopForDelivery by remember { mutableStateOf<DeliveryStopEntity?>(null) }
    var showExpenseDialog by remember { mutableStateOf(false) }

    // Pulsing animation for GPS banner
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp)
    ) {
        // Hero Editorial Next Stop Card (as in Editorial Design)
        if (nextStop != null) {
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
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "PRÓXIMA PARADA",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.6.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = nextStop.clientName,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 24.sp
                                )
                            }
                            Surface(
                                color = if (nextStop.status == DeliveryStopStatus.EN_CAMINO) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    text = if (nextStop.status == DeliveryStopStatus.EN_CAMINO) "EN CAMINO" else "ACTIVO",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Nested Merchandise Item Box
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    color = MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "0${nextStop.stopOrder}",
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 14.sp
                                        )
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = nextStop.itemsDescription,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "DIRECCIÓN: ${nextStop.address.uppercase()}",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                        letterSpacing = 0.5.sp,
                                        maxLines = 1
                                    )
                                }

                                Text(
                                    text = "#E-4492",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Big Action Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (nextStop.status == DeliveryStopStatus.EN_CAMINO) {
                                        activeStopForDelivery = nextStop
                                    } else {
                                        viewModel.setStopEnCamino(nextStop)
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (nextStop.status == DeliveryStopStatus.EN_CAMINO) EditorialGreen else MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = if (nextStop.status == DeliveryStopStatus.EN_CAMINO) "MARCAR ENTREGADO" else "LLEGUÉ AL PUNTO",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    letterSpacing = 1.5.sp,
                                    color = Color.White
                                )
                            }

                            // Secondary Expense Quick Trigger
                            IconButton(
                                onClick = { showExpenseDialog = true },
                                modifier = Modifier
                                    .size(52.dp)
                                    .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(16.dp))
                                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ReceiptLong,
                                    contentDescription = "Gasto",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Dual Stat Metric Cards (Combustible / Entregas)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Combustible Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "COMBUSTIBLE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            letterSpacing = 1.6.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "78",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Light,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "%",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.78f)
                                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                            )
                        }
                    }
                }

                // Entregas Card
                val completedStops = activeRoute?.completedStops ?: 1
                val totalStops = activeRoute?.totalStops ?: 3

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "ENTREGAS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            letterSpacing = 1.6.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "$completedStops",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Light,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "/ $totalStops",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            for (i in 1..totalStops) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(4.dp)
                                        .background(
                                            if (i <= completedStops) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                                            RoundedCornerShape(4.dp)
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Live GPS Pulse Banner
        item {
                Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = alphaAnim), CircleShape)
                    )
                    Text(
                        text = "GPS Transmitiendo ubicación en vivo · Maracaibo Zulia",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.4.sp
                    )
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "HOJA DE RUTA DETALLADA",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    letterSpacing = 1.8.sp
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Other stops list
        items(stops) { stop ->
            StopCard(
                stop = stop,
                onSaliaEntregar = { viewModel.setStopEnCamino(stop) },
                onMarcarEntregado = { activeStopForDelivery = stop }
            )
        }
    }

    // Modal for Delivery Evidence, Return Goods & Extinguisher Loan Exchange
    if (activeStopForDelivery != null) {
        DeliveryCompletionDialog(
            stop = activeStopForDelivery!!,
            onDismiss = { activeStopForDelivery = null },
            onConfirm = { recipient, notes, returnedItems, loanCode, clientExtTaken ->
                viewModel.completeDeliveryStop(
                    stop = activeStopForDelivery!!,
                    recipient = recipient,
                    notes = notes,
                    returnedItems = returnedItems,
                    loanExtinguisherCode = loanCode,
                    clientExtinguisherTaken = clientExtTaken
                )
                activeStopForDelivery = null
            }
        )
    }

    // Dialog for Driver Route Expenses
    if (showExpenseDialog) {
        RouteExpenseDialog(
            onDismiss = { showExpenseDialog = false },
            onSubmit = { category, description, amountUsd ->
                viewModel.submitRouteExpense(category, description, amountUsd)
                showExpenseDialog = false
            }
        )
    }
}

@Composable
fun StopCard(
    stop: DeliveryStopEntity,
    onSaliaEntregar: () -> Unit,
    onMarcarEntregado: () -> Unit
) {
    val isDelivered = stop.status == DeliveryStopStatus.ENTREGADO
    val isEnCamino = stop.status == DeliveryStopStatus.EN_CAMINO

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDelivered) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isEnCamino) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        color = if (isDelivered) EditorialGreenContainer else MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "0${stop.stopOrder}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (isDelivered) EditorialGreen else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Column {
                        Text(
                            text = stop.clientName,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stop.address,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            maxLines = 1
                        )
                    }
                }

                Surface(
                    color = when (stop.status) {
                        DeliveryStopStatus.ENTREGADO -> EditorialGreenContainer
                        DeliveryStopStatus.EN_CAMINO -> EditorialAmberContainer
                        else -> MaterialTheme.colorScheme.secondaryContainer
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = stop.status.label.uppercase(),
                        color = when (stop.status) {
                            DeliveryStopStatus.ENTREGADO -> EditorialGreen
                            DeliveryStopStatus.EN_CAMINO -> EditorialAmber
                            else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        },
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "📦 Carga: ${stop.itemsDescription}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(10.dp)
                )
            }

            if (!isDelivered) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!isEnCamino) {
                        OutlinedButton(
                            onClick = onSaliaEntregar,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.tertiary)
                        ) {
                            Text("SALIR AL PUNTO", fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        }
                    }

                    Button(
                        onClick = onMarcarEntregado,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("ENTREGAR / FIRMA", fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun DeliveryCompletionDialog(
    stop: DeliveryStopEntity,
    onDismiss: () -> Unit,
    onConfirm: (recipient: String, notes: String, returnedItems: String, loanExtinguisherCode: String, clientExtTaken: String) -> Unit
) {
    var recipient by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var returnedItems by remember { mutableStateOf("") }
    var isLoanExchange by remember { mutableStateOf(false) }
    var loanExtCode by remember { mutableStateOf("") }
    var clientExtTaken by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Column {
                Text("REGISTRAR ENTREGA", color = MaterialTheme.colorScheme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                Text(stop.clientName, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = recipient,
                        onValueChange = { recipient = it },
                        label = { Text("Nombre de quien recibe y C.I.") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Observaciones / Novedad") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = returnedItems,
                        onValueChange = { returnedItems = it },
                        label = { Text("Devoluciones / Rechazos (si aplica)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("¿Préstamo / Dejó extintor Nezco?", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Switch(
                            checked = isLoanExchange,
                            onCheckedChange = { isLoanExchange = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary, checkedTrackColor = MaterialTheme.colorScheme.primaryContainer)
                        )
                    }
                }
                if (isLoanExchange) {
                    item {
                        OutlinedTextField(
                            value = loanExtCode,
                            onValueChange = { loanExtCode = it },
                            label = { Text("Cód. Extintor Prestado Nezco (ej: NZ-PREST-09)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = clientExtTaken,
                            onValueChange = { clientExtTaken = it },
                            label = { Text("Serial Extintor Cliente Retirado (a taller)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        if (recipient.isBlank()) "Encargado de Local" else recipient,
                        notes,
                        returnedItems,
                        loanExtCode,
                        clientExtTaken
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = EditorialGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("GUARDAR Y FIRMAR", fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 1.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
            }
        }
    )
}

@Composable
fun RouteExpenseDialog(
    onDismiss: () -> Unit,
    onSubmit: (category: ExpenseCategory, description: String, amountUsd: Double) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(ExpenseCategory.COMBUSTIBLE) }
    var description by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Column {
                Text("GASTO DE RUTA", color = MaterialTheme.colorScheme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                Text("Cargar Comprobante / Gasto", color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Categoría:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        ExpenseCategory.COMBUSTIBLE to "Combustible",
                        ExpenseCategory.PEAJES to "Peaje",
                        ExpenseCategory.VIATICOS_COMIDA to "Viáticos"
                    ).forEach { (cat, label) ->
                        Surface(
                            color = if (selectedCategory == cat) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedCategory == cat) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline),
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { selectedCategory = cat }
                        ) {
                            Text(
                                text = label,
                                color = if (selectedCategory == cat) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Monto en USD ($)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Detalle / E/S Gasolinera / Factura") },
                    modifier = Modifier.fillMaxWidth()
                )

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Text("Foto de recibo digitalizada", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amt = amountText.toDoubleOrNull() ?: 10.0
                    onSubmit(selectedCategory, description.ifBlank { "Gasto de Ruta Chofer" }, amt)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("ENVIAR A REVISIÓN", fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 1.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
            }
        }
    )
}

