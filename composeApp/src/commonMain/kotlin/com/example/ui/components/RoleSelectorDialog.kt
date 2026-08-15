package com.example.ui.components

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
import androidx.compose.runtime.Composable
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

@Composable
fun RoleSelectorDialog(
    currentRole: NezcoRole,
    onRoleSelected: (NezcoRole) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = EditorialSurface,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    color = EditorialRedContainer,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.SwitchAccount,
                            contentDescription = null,
                            tint = EditorialRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Column {
                    Text(
                        text = "PERFILES DE USUARIO",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = EditorialRed,
                            letterSpacing = 1.6.sp
                        )
                    )
                    Text(
                        text = "Seleccionar Rol Operativo",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = EditorialTextPrimary
                        )
                    )
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(NezcoRole.values()) { role ->
                    val isSelected = role == currentRole
                    val icon: ImageVector = when (role) {
                        NezcoRole.SUPER_ADMIN -> Icons.Default.AdminPanelSettings
                        NezcoRole.ADMIN -> Icons.Default.Shield
                        NezcoRole.CHOFER -> Icons.Default.LocalShipping
                        NezcoRole.DESPACHADOR -> Icons.Default.AltRoute
                        NezcoRole.VENTA -> Icons.Default.Storefront
                        NezcoRole.ALMACENISTA -> Icons.Default.Inventory2
                        NezcoRole.TALLER -> Icons.Default.Build
                        NezcoRole.POS_LOCAL -> Icons.Default.PointOfSale
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                onRoleSelected(role)
                                onDismiss()
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                color = if (isSelected) EditorialRedContainer else EditorialBg,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = if (isSelected) EditorialRed else EditorialTextMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = role.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = EditorialTextPrimary
                                        )
                                    )
                                    if (isSelected) {
                                        Surface(
                                            color = EditorialRed,
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text(
                                                text = "ACTIVO",
                                                color = Color.White,
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Black,
                                                letterSpacing = 0.5.sp,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = role.subtitle,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = EditorialTextMuted,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("CERRAR", color = EditorialRed, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }
        }
    )
}
