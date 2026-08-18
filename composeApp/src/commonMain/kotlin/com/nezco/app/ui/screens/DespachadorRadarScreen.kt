package com.nezco.app.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nezco.app.data.model.*
import com.nezco.app.ui.theme.*
import com.nezco.app.ui.viewmodel.NezcoViewModel

@Composable
fun DespachadorRadarScreen(
    viewModel: NezcoViewModel,
    onNavigateToRoute: () -> Unit
) {
    val routes by viewModel.routes.collectAsStateWithLifecycle()
    val stops by viewModel.deliveryStops.collectAsStateWithLifecycle()
    val activeRoute = routes.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        // Radar Map Simulation Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape,
                                modifier = Modifier.size(10.dp)
                            ) {}
                            Text(
                                text = "Radar GPS de Flota Nezco",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "TELEMETRÍA EN VIVO",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Radar Canvas Drawing
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF071526)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val centerX = size.width / 2
                            val centerY = size.height / 2

                            // Concentric radar rings
                            drawCircle(
                                color = Color(0xFF1E3A8A).copy(alpha = 0.4f),
                                radius = size.height * 0.42f,
                                center = Offset(centerX, centerY),
                                style = Stroke(width = 1.5f)
                            )
                            drawCircle(
                                color = Color(0xFF1E3A8A).copy(alpha = 0.3f),
                                radius = size.height * 0.28f,
                                center = Offset(centerX, centerY),
                                style = Stroke(width = 1.5f)
                            )
                            drawCircle(
                                color = Color(0xFF1E3A8A).copy(alpha = 0.2f),
                                radius = size.height * 0.14f,
                                center = Offset(centerX, centerY),
                                style = Stroke(width = 1.5f)
                            )

                            // Crosshairs
                            drawLine(
                                color = Color(0xFF1E3A8A).copy(alpha = 0.3f),
                                start = Offset(centerX, 0f),
                                end = Offset(centerX, size.height),
                                strokeWidth = 1f
                            )
                            drawLine(
                                color = Color(0xFF1E3A8A).copy(alpha = 0.3f),
                                start = Offset(0f, centerY),
                                end = Offset(size.width, centerY),
                                strokeWidth = 1f
                            )

                            // Base Station (Central Maracaibo)
                            drawCircle(
                                color = Color(0xFF38BDF8),
                                radius = 6f,
                                center = Offset(centerX, centerY)
                            )

                            // Truck Pin (Moving)
                            drawCircle(
                                color = Color(0xFFFACC15),
                                radius = 9f,
                                center = Offset(centerX + 65f, centerY - 35f)
                            )
                            drawCircle(
                                color = Color(0xFFFACC15).copy(alpha = 0.4f),
                                radius = 18f,
                                center = Offset(centerX + 65f, centerY - 35f)
                            )

                            // Delivery Stop Pins
                            drawCircle(
                                color = Color(0xFF10B981),
                                radius = 6f,
                                center = Offset(centerX - 90f, centerY + 30f)
                            )
                            drawCircle(
                                color = Color(0xFFEF4444),
                                radius = 6f,
                                center = Offset(centerX + 110f, centerY + 20f)
                            )
                        }

                        // Overlay labels
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "🚚 Camión 01 (Ford Cargo 815) • Lat 10.642, Lng -71.612",
                                color = NezcoAmberGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Zona: Bella Vista / Cecilio Acosta • Vel: 42 Km/h",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 9.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Distancia Estimada", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                            Text("${activeRoute?.estimatedKm ?: 34.5} Km", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Column {
                            Text("Salida", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                            Text(activeRoute?.startedAt ?: "08:30 AM", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Estatus de Ruta", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                            Text("En Tránsito", color = NezcoGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // Active Deliveries List
        item {
            Text(
                text = "Control y Seguimiento de Paradas Asignadas",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        items(stops) { stop ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = when (stop.status) {
                                DeliveryStopStatus.ENTREGADO -> NezcoGreen
                                DeliveryStopStatus.EN_CAMINO -> NezcoAmber
                                else -> NezcoNavy
                            },
                            shape = CircleShape,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${stop.stopOrder}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Column {
                            Text(
                                text = stop.clientName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = stop.address,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                            Text(
                                text = "Detalle: ${stop.itemsDescription}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Surface(
                        color = when (stop.status) {
                            DeliveryStopStatus.ENTREGADO -> EditorialGreenContainer
                            DeliveryStopStatus.EN_CAMINO -> MaterialTheme.colorScheme.tertiaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = stop.status.label,
                            color = when (stop.status) {
                                DeliveryStopStatus.ENTREGADO -> EditorialGreen
                                DeliveryStopStatus.EN_CAMINO -> MaterialTheme.colorScheme.tertiary
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}
