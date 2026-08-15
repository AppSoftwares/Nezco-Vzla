package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.CurrencyMode
import com.example.ui.viewmodel.NezcoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogPosScreen(
    viewModel: NezcoViewModel,
    isPosModeDefault: Boolean = false
) {
    val products by viewModel.products.collectAsStateWithLifecycle()
    val cart by viewModel.cart.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val currencyMode by viewModel.currencyMode.collectAsStateWithLifecycle()
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()

    var isPosMode by remember { mutableStateOf(isPosModeDefault || currentRole == NezcoRole.POS_LOCAL) }
    var selectedProductForDetail by remember { mutableStateOf<ProductEntity?>(null) }
    var showCartSheet by remember { mutableStateOf(false) }
    var showCheckoutDialog by remember { mutableStateOf(false) }

    val filteredProducts = remember(products, selectedCategory, searchQuery) {
        products.filter { product ->
            val matchesCat = selectedCategory == null || product.category == selectedCategory
            val matchesQuery = searchQuery.isBlank() ||
                    product.name.contains(searchQuery, ignoreCase = true) ||
                    product.code.contains(searchQuery, ignoreCase = true) ||
                    product.description.contains(searchQuery, ignoreCase = true)
            matchesCat && matchesQuery
        }
    }

    val totalCartUsd = cart.sumOf { it.product.priceUsd * it.quantity }
    val totalCartItems = cart.sumOf { it.quantity }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
        ) {
            // Mode Switcher (Catálogo Técnico vs POS Mostrador)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isPosMode) "Punto de Venta (POS) Mostrador" else "Catálogo de Equipos y Extintores",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "Grupo Nezco • Normas COVENIN & NFPA",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    FilterChip(
                        selected = isPosMode,
                        onClick = { isPosMode = !isPosMode },
                        label = { Text(if (isPosMode) "Modo POS" else "Modo Asesor") },
                        leadingIcon = {
                            Icon(
                                imageVector = if (isPosMode) Icons.Default.PointOfSale else Icons.Default.Storefront,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchQuery.value = it },
                    placeholder = { Text("Buscar extintor, gabinete, COVENIN, repuesto...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Borrar")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            // Category Filter Pills
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { viewModel.selectedCategory.value = null },
                            label = { Text("Todos (${products.size})") }
                        )
                    }
                    items(ProductCategory.values()) { cat ->
                        val count = products.count { it.category == cat }
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = {
                                viewModel.selectedCategory.value = if (selectedCategory == cat) null else cat
                            },
                            label = { Text("${cat.label} ($count)") }
                        )
                    }
                }
            }

            // Products list
            items(filteredProducts) { product ->
                ProductCard(
                    product = product,
                    formattedPrice = viewModel.formatPrice(product.priceUsd),
                    onAddToCart = { viewModel.addToCart(product) },
                    onDetailsClick = { selectedProductForDetail = product },
                    isPosMode = isPosMode
                )
            }
        }

        // Floating Cart Bottom Pill
        if (totalCartItems > 0) {
            Surface(
                color = NezcoNavy,
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 10.dp,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { showCartSheet = true }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Badge(containerColor = NezcoSafetyRed) {
                            Text("$totalCartItems", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text(
                                text = "Carrito / Pedido Actual",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Toca para revisar y facturar",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 10.sp
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = viewModel.formatPrice(totalCartUsd),
                            color = NezcoAmberGold,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = "Abrir Carrito",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }

    // Product Detail Bottom Sheet
    if (selectedProductForDetail != null) {
        val prod = selectedProductForDetail!!
        ModalBottomSheet(
            onDismissRequest = { selectedProductForDetail = null }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = NezcoSafetyRed.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = prod.category.coveninCode,
                            color = NezcoSafetyRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Text(
                        text = viewModel.formatPrice(prod.priceUsd),
                        color = NezcoNavy,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )
                }

                Text(
                    text = prod.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = "Código: ${prod.code} • Capacidad: ${prod.capacity}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Divider()

                Text(
                    text = "Ficha Técnica y Aplicación:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = prod.description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Especificaciones: ${prod.technicalSpecs}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Stock by warehouse
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Sede Principal", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${prod.stockPrincipal} un.", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Taller Central", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${prod.stockTaller} un.", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Móvil Camión 01", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${prod.stockCamion} un.", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                Button(
                    onClick = {
                        viewModel.addToCart(prod)
                        selectedProductForDetail = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NezcoNavy),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.AddShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AGREGAR AL CARRITO / POS", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

    // Shopping Cart Bottom Sheet
    if (showCartSheet) {
        ModalBottomSheet(
            onDismissRequest = { showCartSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null, tint = NezcoNavy)
                        Text(
                            text = if (isPosMode) "POS: Resumen de Compra" else "Carrito de Pedido / Cotización",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    TextButton(onClick = { viewModel.clearCart() }) {
                        Text("Vaciar", color = NezcoSafetyRed)
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cart) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.product.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "${viewModel.formatPrice(item.product.priceUsd)} c/u",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    IconButton(
                                        onClick = { viewModel.updateCartQuantity(item.product.id, -1) },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Menos", modifier = Modifier.size(16.dp))
                                    }
                                    Text(
                                        text = "${item.quantity}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    IconButton(
                                        onClick = { viewModel.updateCartQuantity(item.product.id, 1) },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = "Más", modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                Divider()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total a Facturar:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(
                        text = viewModel.formatPrice(totalCartUsd),
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = NezcoNavy
                    )
                }

                Button(
                    onClick = {
                        showCartSheet = false
                        showCheckoutDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (isPosMode) NezcoGreen else NezcoNavy),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = if (isPosMode) Icons.Default.PointOfSale else Icons.Default.Send,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isPosMode) "COBRAR Y EMITIR FACTURA POS" else "GENERAR PEDIDO / COTIZACIÓN",
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

    // Checkout Modal
    if (showCheckoutDialog) {
        CheckoutDialog(
            isPos = isPosMode,
            totalUsd = totalCartUsd,
            formattedTotal = viewModel.formatPrice(totalCartUsd),
            onDismiss = { showCheckoutDialog = false },
            onConfirm = { name, rif, phone, address, paymentStatus ->
                viewModel.checkoutOrder(name, rif, phone, address, isPosMode, paymentStatus)
                showCheckoutDialog = false
            }
        )
    }
}

@Composable
fun ProductCard(
    product: ProductEntity,
    formattedPrice: String,
    onAddToCart: () -> Unit,
    onDetailsClick: () -> Unit,
    isPosMode: Boolean
) {
    val isLowStock = product.stockPrincipal <= product.minStock

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onDetailsClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Extinguisher / Category Badge Icon
            Surface(
                color = when (product.category) {
                    ProductCategory.PQS -> NezcoSafetyRed.copy(alpha = 0.15f)
                    ProductCategory.CO2 -> NezcoNavy.copy(alpha = 0.15f)
                    ProductCategory.SOLKAFLAM -> Color(0xFF0284C7).copy(alpha = 0.15f)
                    ProductCategory.GABINETES -> Color(0xFFEA580C).copy(alpha = 0.15f)
                    ProductCategory.MANGUERAS -> Color(0xFF7C3AED).copy(alpha = 0.15f)
                    else -> NezcoGreen.copy(alpha = 0.15f)
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (product.isExtinguisher) Icons.Default.FireExtinguisher else Icons.Default.Shield,
                        contentDescription = null,
                        tint = when (product.category) {
                            ProductCategory.PQS -> NezcoSafetyRed
                            ProductCategory.CO2 -> NezcoNavy
                            ProductCategory.SOLKAFLAM -> Color(0xFF0284C7)
                            ProductCategory.GABINETES -> Color(0xFFEA580C)
                            ProductCategory.MANGUERAS -> Color(0xFF7C3AED)
                            else -> NezcoGreen
                        },
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = product.code,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = product.category.coveninCode,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = NezcoNavyLight,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = formattedPrice,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = NezcoNavy
                    )
                    Surface(
                        color = if (isLowStock) NezcoAmberContainer else NezcoGreenContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Stock: ${product.stockPrincipal}",
                            color = if (isLowStock) NezcoAmber else NezcoGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            IconButton(
                onClick = onAddToCart,
                modifier = Modifier
                    .size(40.dp)
                    .background(NezcoNavy, RoundedCornerShape(10.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun CheckoutDialog(
    isPos: Boolean,
    totalUsd: Double,
    formattedTotal: String,
    onDismiss: () -> Unit,
    onConfirm: (name: String, rif: String, phone: String, address: String, paymentStatus: PaymentStatus) -> Unit
) {
    var clientName by remember { mutableStateOf(if (isPos) "Cliente Mostrador Nezco" else "") }
    var clientRif by remember { mutableStateOf(if (isPos) "J-00000000-0" else "J-") }
    var clientPhone by remember { mutableStateOf("") }
    var clientAddress by remember { mutableStateOf(if (isPos) "Tienda Física Nezco Maracaibo" else "") }
    var paymentStatus by remember { mutableStateOf(if (isPos) PaymentStatus.PAGADO else PaymentStatus.PENDIENTE) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    imageVector = if (isPos) Icons.Default.Receipt else Icons.Default.LocalShipping,
                    contentDescription = null,
                    tint = NezcoNavy
                )
                Text(
                    text = if (isPos) "Emitir Factura POS Directa" else "Crear Orden de Despacho",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    color = NezcoNavy.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Monto Total:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(formattedTotal, fontWeight = FontWeight.Black, fontSize = 16.sp, color = NezcoNavy)
                    }
                }

                OutlinedTextField(
                    value = clientName,
                    onValueChange = { clientName = it },
                    label = { Text("Razón Social / Nombre Cliente") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = clientRif,
                    onValueChange = { clientRif = it },
                    label = { Text("RIF / Cédula") },
                    placeholder = { Text("Ej. J-12345678-9") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = clientPhone,
                    onValueChange = { clientPhone = it },
                    label = { Text("Teléfono de Contacto") },
                    placeholder = { Text("Ej. 0414-1234567") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = clientAddress,
                    onValueChange = { clientAddress = it },
                    label = { Text("Dirección de Entrega / Despacho") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Condición de Pago:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = paymentStatus == PaymentStatus.PAGADO,
                        onClick = { paymentStatus = PaymentStatus.PAGADO },
                        label = { Text("Pagado Total") }
                    )
                    FilterChip(
                        selected = paymentStatus == PaymentStatus.PENDIENTE,
                        onClick = { paymentStatus = PaymentStatus.PENDIENTE },
                        label = { Text("Crédito (CxC)") }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        clientName.ifBlank { "Cliente Mostrador" },
                        clientRif.ifBlank { "J-00000000-0" },
                        clientPhone.ifBlank { "0261-0000000" },
                        clientAddress.ifBlank { "Retiro en Sede Nezco" },
                        paymentStatus
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = if (isPos) NezcoGreen else NezcoNavy)
            ) {
                Text(if (isPos) "CONFIRMAR VENTA POS" else "CONFIRMAR PEDIDO", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
