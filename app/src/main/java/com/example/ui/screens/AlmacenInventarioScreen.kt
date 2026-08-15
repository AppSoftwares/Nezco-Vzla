package com.example.ui.screens

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
import com.example.data.model.ProductEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.NezcoViewModel

@Composable
fun AlmacenInventarioScreen(
    viewModel: NezcoViewModel
) {
    val products by viewModel.products.collectAsStateWithLifecycle()
    var productForAdjustment by remember { mutableStateOf<ProductEntity?>(null) }

    val totalStockPrincipal = products.sumOf { it.stockPrincipal }
    val totalStockTaller = products.sumOf { it.stockTaller }
    val totalStockCamion = products.sumOf { it.stockCamion }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        // Multi-warehouse Overview Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF134E4A))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Inventario Multicentral Nezco",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Almacén Principal • Taller Central • Camión Despacho",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Almacén Principal", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                            Text("$totalStockPrincipal un.", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Taller Central", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                            Text("$totalStockTaller un.", color = NezcoAmberGold, fontWeight = FontWeight.Black, fontSize = 16.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Móvil Camión 01", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                            Text("$totalStockCamion un.", color = Color(0xFF67E8F9), fontWeight = FontWeight.Black, fontSize = 16.sp)
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "Existencias por Ubicación (Toca para Ajustar o Transferir)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        // Product stock list
        items(products) { product ->
            val isLowStock = product.stockPrincipal <= product.minStock

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { productForAdjustment = product },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = if (isLowStock) NezcoAmber else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = product.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Código: ${product.code} • ${product.category.label} • Mínimo: ${product.minStock}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (isLowStock) {
                            Surface(
                                color = NezcoAmberContainer,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "¡STOCK BAJO!",
                                    color = NezcoAmber,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Principal", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${product.stockPrincipal}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Taller", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${product.stockTaller}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Camión", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${product.stockCamion}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    // Stock adjustment dialog
    if (productForAdjustment != null) {
        val prod = productForAdjustment!!
        StockAdjustmentDialog(
            product = prod,
            onDismiss = { productForAdjustment = null },
            onConfirm = { p, t, c ->
                viewModel.updateProductStock(prod, p, t, c)
                productForAdjustment = null
            }
        )
    }
}

@Composable
fun StockAdjustmentDialog(
    product: ProductEntity,
    onDismiss: () -> Unit,
    onConfirm: (principal: Int, taller: Int, camion: Int) -> Unit
) {
    var principalText by remember { mutableStateOf(product.stockPrincipal.toString()) }
    var tallerText by remember { mutableStateOf(product.stockTaller.toString()) }
    var camionText by remember { mutableStateOf(product.stockCamion.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Ajuste de Stock Multicentral",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = NezcoNavy
                )
                Text("Ingresa las nuevas cantidades disponibles por ubicación:", fontSize = 11.sp)

                OutlinedTextField(
                    value = principalText,
                    onValueChange = { principalText = it },
                    label = { Text("Stock Almacén Principal") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = tallerText,
                    onValueChange = { tallerText = it },
                    label = { Text("Stock Taller Central") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = camionText,
                    onValueChange = { camionText = it },
                    label = { Text("Stock Camión Despacho 01") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = principalText.toIntOrNull() ?: product.stockPrincipal
                    val t = tallerText.toIntOrNull() ?: product.stockTaller
                    val c = camionText.toIntOrNull() ?: product.stockCamion
                    onConfirm(p, t, c)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF134E4A))
            ) {
                Text("GUARDAR CAMBIOS", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
