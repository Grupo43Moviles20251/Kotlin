package com.moviles2025.freshlink43.ui.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moviles2025.freshlink43.data.AnalyticsManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.*
import androidx.navigation.compose.currentBackStackEntryAsState
import com.moviles2025.freshlink43.model.Order
import com.moviles2025.freshlink43.ui.navigation.BottomNavManager
import com.moviles2025.freshlink43.ui.navigation.Header
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*


@Composable
fun OrderScreen(
    navController: NavController,
    viewModel: OrderViewModel
) {
    // 1) Recolectamos sólo las órdenes visibles
    val orders by viewModel.visibleOrders.collectAsStateWithLifecycle()
    // 2) Recolectamos el estado de conexión
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()

    val currentRoute = navController
        .currentBackStackEntryAsState()
        .value
        ?.destination
        ?.route
        ?: "order"

    LaunchedEffect(viewModel) {
        AnalyticsManager.logFeatureUsage("OrderScreen")
        viewModel.getOrders()
    }

    Scaffold(
        topBar    = { Header { navController.navigate("profile") } },
        bottomBar = { BottomNavManager(navController, currentRoute) },
        modifier  = Modifier.windowInsetsPadding(WindowInsets.statusBars)
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(8.dp))
                Text("My Orders", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            LazyColumn(Modifier.fillMaxSize()) {
                // ——> items correctamente importado
                items(
                    items = orders,
                    key   = { it.orderId }
                ) { order ->
                    OrderCard(
                        order       = order,
                        isConnected = isConnected,
                        onCancel    = { viewModel.cancelOrder(order.orderId) }
                    )
                }

                // Footer: botón “Load more” si hay más páginas
                if (viewModel.hasMorePages()) {
                    item {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(onClick = { viewModel.loadNextPage() }) {
                                Text("Load more orders")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: Order,
    isConnected: Boolean,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape  = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F3F7))
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(order.productName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(4.dp))
                Text("Total: $${order.price}", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(4.dp))
                Text("Date: ${order.date}", fontSize = 12.sp, color = Color.Gray)
                Text("ID: ${order.orderId}", fontSize = 12.sp, color = Color.Gray)
            }

            Spacer(Modifier.width(16.dp))

            val (color, label) = when (order.state.lowercase()) {
                "pending"   -> Pair(Color(0xFFFFA500), "PENDING")
                "cancelled" -> Pair(Color(0xFFFF4C4C), "CANCELLED")
                else        -> Pair(Color.Gray, order.state.uppercase())
            }

            Button(
                onClick = onCancel,
                enabled = isConnected,  // opcionalmente deshabilitas si no hay red
                colors  = ButtonDefaults.buttonColors(containerColor = color),
                shape   = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(40.dp)
                    .width(130.dp)
            ) {
                Text(label, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
