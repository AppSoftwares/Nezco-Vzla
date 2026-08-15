package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NezcoRole
import com.example.ui.theme.*

data class NavItem(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val description: String
)

val allNavItems = listOf(
    NavItem("dashboard", "Dashboard", Icons.Default.Dashboard, "Resumen general y métricas"),
    NavItem("chofer_ruta", "Ruta Conductor", Icons.Default.LocalShipping, "Entregas, firma y evidencias"),
    NavItem("despacho_radar", "Radar GPS", Icons.Default.AltRoute, "Monitoreo de flota en vivo"),
    NavItem("catalogo", "Catálogo & POS", Icons.Default.Storefront, "Venta y fichas COVENIN"),
    NavItem("taller", "Taller Recarga", Icons.Default.Build, "Mantenimiento y pruebas COVENIN"),
    NavItem("almacen", "Almacén", Icons.Default.Inventory2, "Stock multicentral"),
    NavItem("prestamos", "Comodato", Icons.Default.SwapHoriz, "Control de extintores cedidos"),
    NavItem("cobranzas", "CxC / CxP", Icons.Default.ReceiptLong, "Cobranzas y proveedores"),
    NavItem("gastos", "Gastos Ruta", Icons.Default.AccountBalanceWallet, "Combustible y viáticos"),
    NavItem("nomina", "Nómina LOTTT", Icons.Default.Groups, "Leyes laborales venezolanas"),
    NavItem("auditoria", "Auditoría", Icons.Default.History, "Trazabilidad inmutable")
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
                NavItem("chofer_ruta", "Ruta", Icons.Default.LocalShipping, "Entregas activas"),
                NavItem("gastos", "Gastos", Icons.Default.AccountBalanceWallet, "Gastos de ruta"),
                NavItem("prestamos", "Comodato", Icons.Default.SwapHoriz, "Extintores prestados")
            )
            NezcoRole.DESPACHADOR -> listOf(
                NavItem("despacho_radar", "Radar GPS", Icons.Default.AltRoute, "Monitoreo flota"),
                NavItem("chofer_ruta", "Despachos", Icons.Default.LocalShipping, "Rutas activas"),
                NavItem("prestamos", "Comodato", Icons.Default.SwapHoriz, "Préstamos")
            )
            NezcoRole.VENTA, NezcoRole.POS_LOCAL -> listOf(
                NavItem("catalogo", "Catálogo", Icons.Default.Storefront, "Ventas y POS"),
                NavItem("cobranzas", "Cobranzas", Icons.Default.ReceiptLong, "CxC Clientes"),
                NavItem("almacen", "Stock", Icons.Default.Inventory2, "Disponibilidad")
            )
            NezcoRole.TALLER -> listOf(
                NavItem("taller", "Taller", Icons.Default.Build, "Órdenes de recarga"),
                NavItem("almacen", "Insumos", Icons.Default.Inventory2, "Polvo y repuestos"),
                NavItem("prestamos", "Comodato", Icons.Default.SwapHoriz, "Cilindros prestados")
            )
            NezcoRole.ALMACENISTA -> listOf(
                NavItem("almacen", "Almacén", Icons.Default.Inventory2, "Stock multicentral"),
                NavItem("prestamos", "Comodato", Icons.Default.SwapHoriz, "Extintores cliente"),
                NavItem("catalogo", "Catálogo", Icons.Default.Storefront, "Equipos")
            )
            NezcoRole.ADMIN, NezcoRole.SUPER_ADMIN -> listOf(
                NavItem("dashboard", "Métricas", Icons.Default.Dashboard, "Panel general"),
                NavItem("catalogo", "Ventas/POS", Icons.Default.Storefront, "Catálogo"),
                NavItem("chofer_ruta", "Ruta", Icons.Default.LocalShipping, "Despachos"),
                NavItem("taller", "Taller", Icons.Default.Build, "Recargas")
            )
        }
    }

    Surface(
        color = EditorialSurface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorder),
        tonalElevation = 12.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
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
                    if (isSelected) {
                        Surface(
                            color = EditorialRedContainer,
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.size(width = 48.dp, height = 28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(EditorialRed, CircleShape)
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.size(width = 48.dp, height = 28.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = EditorialTextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Text(
                        text = item.label.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        letterSpacing = 1.sp,
                        color = if (isSelected) EditorialRed else EditorialTextMuted
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
                        imageVector = Icons.Default.Widgets,
                        contentDescription = "Módulos",
                        tint = EditorialTextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "MÓDULOS",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp,
                    color = EditorialTextMuted
                )
            }
        }
    }

    if (showMoreSheet) {
        ModalBottomSheet(
            onDismissRequest = { showMoreSheet = false },
            containerColor = EditorialSurface,
            scrimColor = Color.Black.copy(alpha = 0.7f),
            dragHandle = {
                Surface(
                    color = EditorialBorderLight,
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
                    Box(modifier = Modifier.size(8.dp).background(EditorialRed, CircleShape))
                    Text(
                        text = "MÓDULOS DEL SISTEMA NEZCO",
                        color = EditorialTextPrimary,
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
                                containerColor = if (isSelected) EditorialSurfaceElevated else EditorialSurfaceSub
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = if (isSelected) EditorialRed else EditorialBorder
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    color = if (isSelected) EditorialRedContainer else EditorialBg,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = null,
                                            tint = if (isSelected) EditorialRed else EditorialTextBody,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = item.label,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = EditorialTextPrimary
                                    )
                                    Text(
                                        text = item.description,
                                        fontSize = 10.sp,
                                        color = EditorialTextMuted,
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
