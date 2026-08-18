package com.nezco.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nezco.app.data.model.NezcoRole
import com.nezco.app.ui.theme.*
import com.nezco.app.ui.viewmodel.CurrencyMode

@Composable
fun NezcoTopBar(
    currentRole: NezcoRole,
    currencyMode: CurrencyMode,
    bcvRate: Double,
    cartItemCount: Int,
    onRoleClick: () -> Unit,
    onCurrencyToggle: () -> Unit,
    onCartClick: () -> Unit,
    onConfigClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Editorial Title & Micro-Tracking Label
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                        )
                        Text(
                            text = "GRUPO NEZCO · ${currentRole.title.uppercase()}",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.8.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Nezco",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Light,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "App",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp
                        )
                    }
                }

                // Right Action Group
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Currency Toggle Pill (USD / Bs. BCV)
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onCurrencyToggle() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = if (currencyMode == CurrencyMode.USD) "$ USD" else "Bs. $bcvRate",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Cart Icon (if active items)
                    if (cartItemCount > 0) {
                        IconButton(
                            onClick = onCartClick,
                            modifier = Modifier
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), CircleShape)
                        ) {
                            BadgedBox(
                                badge = {
                                    Badge(containerColor = MaterialTheme.colorScheme.primary) {
                                        Text("$cartItemCount", color = MaterialTheme.colorScheme.onPrimary, fontSize = 9.sp)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Carrito",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    // Avatar Role Selector Monogram (JD / Role)
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .clickable { onRoleClick() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = when (currentRole) {
                                    NezcoRole.SUPER_ADMIN -> "SA"
                                    NezcoRole.ADMIN -> "AD"
                                    NezcoRole.CHOFER -> "CH"
                                    NezcoRole.DESPACHADOR -> "DP"
                                    NezcoRole.VENTA -> "VT"
                                    NezcoRole.ALMACENISTA -> "AL"
                                    NezcoRole.TALLER -> "TL"
                                    NezcoRole.POS_LOCAL -> "PS"
                                },
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
