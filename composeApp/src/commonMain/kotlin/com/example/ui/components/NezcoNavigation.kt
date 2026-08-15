package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NezcoRole
import com.example.ui.theme.*
import nezco.composeapp.generated.resources.Res
import nezco.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

data class NavItem(
    val id: String,
    val label: String,
    val iconRes: DrawableResource,
    val description: String,
)

val allNavItems = listOf(
    NavItem("dashboard", "Dashboard", Res.drawable.ic_nav_metricas, "Resumen general y métricas"),
    NavItem("chofer_ruta", "Ruta Conductor", Res.drawable.ic_nav_ruta, "Entregas, firma y evidencias"),
    NavItem("despacho_radar", "Radar GPS", Res.drawable.ic_nav_modulos, "Monitoreo de flota en vivo"),
    NavItem("catalogo", "Catálogo & POS", Res.drawable.ic_nav_ventas, "Venta y fichas COVENIN"),
    NavItem("taller", "Taller Recarga", Res.drawable.ic_nav_taller, "Mantenimiento y pruebas COVENIN"),
    NavItem("almacen", "Almacén", Res.drawable.ic_nav_modulos, "Stock multicentral"),
    NavItem("prestamos", "Comodato", Res.drawable.ic_nav_modulos, "Control de extintores cedidos"),
    NavItem("cobranzas", "CxC / CxP", Res.drawable.ic_nav_modulos, "Cobranzas y proveedores"),
    NavItem("gastos", "Gastos Ruta", Res.drawable.ic_nav_modulos, "Combustible y viáticos"),
    NavItem("nomina", "Nómina LOTTT", Res.drawable.ic_nav_modulos, "Leyes laborales venezolanas"),
    NavItem("auditoria", "Auditoría", Res.drawable.ic_nav_modulos, "Trazabilidad inmutable")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NezcoBottomBar(
    currentTab: String,
    currentRole: NezcoRole,
    onTabSelected: (String) -> Unit
) {
    var showMoreSheet by remember { mutableStateOf(false) }

    val primaryItems = remember(currentRole) {
        when (currentRole) {
            NezcoRole.CHOFER -> listOf(
                NavItem("chofer_ruta", "Ruta", Res.drawable.ic_nav_ruta, "Entregas activas"),
                NavItem("gastos", "Gastos", Res.drawable.ic_nav_modulos, "Gastos de ruta"),
                NavItem("prestamos", "Comodato", Res.drawable.ic_nav_modulos, "Extintores prestados")
            )
            NezcoRole.DESPACHADOR -> listOf(
                NavItem("despacho_radar", "Radar GPS", Res.drawable.ic_nav_modulos, "Monitoreo flota"),
                NavItem("chofer_ruta", "Despachos", Res.drawable.ic_nav_ruta, "Rutas activas"),
                NavItem("prestamos", "Comodato", Res.drawable.ic_nav_modulos, "Préstamos")
            )
            NezcoRole.VENTA, NezcoRole.POS_LOCAL -> listOf(
                NavItem("catalogo", "Catálogo", Res.drawable.ic_nav_ventas, "Ventas y POS"),
                NavItem("cobranzas", "Cobranzas", Res.drawable.ic_nav_modulos, "CxC Clientes"),
                NavItem("almacen", "Stock", Res.drawable.ic_nav_modulos, "Disponibilidad")
            )
            NezcoRole.TALLER -> listOf(
                NavItem("taller", "Taller", Res.drawable.ic_nav_taller, "Órdenes de recarga"),
                NavItem("almacen", "Insumos", Res.drawable.ic_nav_modulos, "Polvo y repuestos"),
                NavItem("prestamos", "Comodato", Res.drawable.ic_nav_modulos, "Cilindros prestados")
            )
            NezcoRole.ALMACENISTA -> listOf(
                NavItem("almacen", "Almacén", Res.drawable.ic_nav_modulos, "Stock multicentral"),
                NavItem("prestamos", "Comodato", Res.drawable.ic_nav_modulos, "Extintores cliente"),
                NavItem("catalogo", "Catálogo", Res.drawable.ic_nav_ventas, "Equipos")
            )
            NezcoRole.ADMIN, NezcoRole.SUPER_ADMIN -> listOf(
                NavItem("dashboard", "Métricas", Res.drawable.ic_nav_metricas, "Panel general"),
                NavItem("catalogo", "Ventas/POS", Res.drawable.ic_nav_ventas, "Catálogo"),
                NavItem("chofer_ruta", "Ruta", Res.drawable.ic_nav_ruta, "Despachos"),
                NavItem("taller", "Taller", Res.drawable.ic_nav_taller, "Recargas")
            )
        }
    }

    Surface(
        color = NezcoBarDark,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF332223)),
        tonalElevation = 12.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            Image(
                painter = painterResource(Res.drawable.bg_nav_manguera),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                contentScale = ContentScale.FillWidth
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                primaryItems.forEach { item ->
                    val isSelected = currentTab == item.id
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onTabSelected(item.id) }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(width = 48.dp, height = 28.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(width = 42.dp, height = 28.dp)
                                        .background(NezcoOrangeActive.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                                )
                            }
                            Icon(
                                painter = painterResource(item.iconRes),
                                contentDescription = item.label,
                                tint = if (isSelected) NezcoOrangeActive else NezcoCreamInactive,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Text(
                            text = item.label.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            letterSpacing = 1.sp,
                            color = if (isSelected) NezcoOrangeActive else NezcoCreamInactive
                        )
                    }
                }

                // More Modules Pill
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { showMoreSheet = true }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier.size(width = 48.dp, height = 28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_nav_modulos),
                            contentDescription = "Módulos",
                            tint = NezcoCreamInactive,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "MÓDULOS",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp,
                        color = NezcoCreamInactive
                    )
                }
            }
        }
    }

    if (showMoreSheet) {
        ModalBottomSheet(
            onDismissRequest = { showMoreSheet = false },
            containerColor = NezcoBarDark,
            scrimColor = Color.Black.copy(alpha = 0.7f),
            dragHandle = {
                Surface(
                    color = NezcoCreamInactive.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(2.dp),
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .size(width = 36.dp, height = 4.dp)
                ) {}
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .navigationBarsPadding()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Box(modifier = Modifier.size(8.dp).background(NezcoOrangeActive, CircleShape))
                    Text(
                        text = "MÓDULOS DEL SISTEMA NEZCO",
                        color = NezcoCreamInactive,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.heightIn(max = 440.dp)
                ) {
                    items(allNavItems) { item ->
                        val isSelected = currentTab == item.id
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    onTabSelected(item.id)
                                    showMoreSheet = false
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFF2A1B1C) else Color(0xFF221112)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = if (isSelected) NezcoOrangeActive else Color(0xFF332223)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    color = if (isSelected) NezcoOrangeActive.copy(alpha = 0.2f) else NezcoBarDark,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            painter = painterResource(item.iconRes),
                                            contentDescription = null,
                                            tint = if (isSelected) NezcoOrangeActive else NezcoCreamInactive,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = item.label,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = NezcoCreamInactive
                                    )
                                    Text(
                                        text = item.description,
                                        fontSize = 10.sp,
                                        color = NezcoCreamInactive.copy(alpha = 0.7f),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
