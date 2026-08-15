package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.NezcoTheme
import com.example.ui.viewmodel.NezcoViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NezcoTheme {
                NezcoApp()
            }
        }
    }
}

@Composable
fun NezcoApp(
    viewModel: NezcoViewModel = viewModel()
) {
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val currencyMode by viewModel.currencyMode.collectAsStateWithLifecycle()
    val cart by viewModel.cart.collectAsStateWithLifecycle()
    val systemConfig by viewModel.systemConfig.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    var showRoleDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NezcoTopBar(
                currentRole = currentRole,
                currencyMode = currencyMode,
                bcvRate = systemConfig?.bcvRateBs ?: 68.50,
                cartItemCount = cart.sumOf { it.quantity },
                onRoleClick = { showRoleDialog = true },
                onCurrencyToggle = { viewModel.toggleCurrency() },
                onCartClick = { viewModel.setTab("catalogo") },
                onConfigClick = { viewModel.setTab("nomina") }
            )
        },
        bottomBar = {
            NezcoBottomBar(
                currentTab = currentTab,
                currentRole = currentRole,
                onTabSelected = { viewModel.setTab(it) }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentTab,
                label = "ScreenTransition"
            ) { targetScreen ->
                when (targetScreen) {
                    "dashboard" -> DashboardScreen(
                        viewModel = viewModel,
                        onNavigate = { viewModel.setTab(it) }
                    )
                    "chofer_ruta" -> ChoferRouteScreen(
                        viewModel = viewModel,
                        onNavigateToExpenses = { viewModel.setTab("gastos") }
                    )
                    "despacho_radar" -> DespachadorRadarScreen(
                        viewModel = viewModel,
                        onNavigateToRoute = { viewModel.setTab("chofer_ruta") }
                    )
                    "catalogo", "pos_venta" -> CatalogPosScreen(
                        viewModel = viewModel,
                        isPosModeDefault = targetScreen == "pos_venta"
                    )
                    "taller" -> TallerRecargaScreen(
                        viewModel = viewModel
                    )
                    "almacen" -> AlmacenInventarioScreen(
                        viewModel = viewModel
                    )
                    "prestamos" -> ExtintoresPrestamoScreen(
                        viewModel = viewModel
                    )
                    "cobranzas" -> CuentasPorCobrarPagarScreen(
                        viewModel = viewModel
                    )
                    "gastos" -> GastosScreen(
                        viewModel = viewModel
                    )
                    "nomina" -> NominaLotttScreen(
                        viewModel = viewModel
                    )
                    "auditoria" -> AuditoriaScreen(
                        viewModel = viewModel
                    )
                    else -> DashboardScreen(
                        viewModel = viewModel,
                        onNavigate = { viewModel.setTab(it) }
                    )
                }
            }
        }
    }

    if (showRoleDialog) {
        RoleSelectorDialog(
            currentRole = currentRole,
            onRoleSelected = { viewModel.setRole(it) },
            onDismiss = { showRoleDialog = false }
        )
    }
}
