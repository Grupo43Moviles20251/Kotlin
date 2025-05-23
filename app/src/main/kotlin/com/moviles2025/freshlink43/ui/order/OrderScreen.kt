package com.moviles2025.freshlink43.ui.order

import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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

@Composable
fun OrderScreen(
    navController: NavController,
    viewModel: OrderViewModel
) {
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val isConnected = viewModel.isConnected.collectAsState(initial = false).value

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: "order"

    LaunchedEffect(Unit) {
        AnalyticsManager.logFeatureUsage("OrderScreen")
        viewModel.getOrders()
    }

    Scaffold(
        topBar = { Header { navController.navigate("profile") } },
        bottomBar = { BottomNavManager(navController, currentRoute) },
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "My Orders",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(orders.size) { index ->
                    val order = orders[index]
                    OrderCard(
                        order = order,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: Order,
    viewModel: OrderViewModel
) {
    val isConnected = viewModel.isConnected.collectAsState(initial = false).value
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F3F7))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = order.productName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Total: $${order.price}",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Date: ${order.date}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "ID: ${order.orderId}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            val (color, text) = when (order.state.lowercase()) {
                "pending" -> Pair(Color(0xFFFFA500), "PENDING")
                "cancelled" -> Pair(Color(0xFFFF4C4C), "CANCELLED")
                else -> Pair(Color.Gray, order.state.uppercase())
            }

            Button(
                onClick = {
                if(isConnected){
                    viewModel.cancelOrder(order.orderId)
                }
                },
                colors = ButtonDefaults.buttonColors(containerColor = color),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(40.dp)
                    .width(130.dp)
            ) {
                Text(text = text, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
