package com.moviles2025.freshlink43.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.* // para botones, diálogos, etc.
import androidx.compose.material.icons.* // para íconos
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun OrderSuccessDialog(
    orderId: String,
    onDismiss: () -> Unit
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                // Close button
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                        .clickable(onClick = onDismiss)
                )

                // Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your order has been placed successfully!",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = "Order ID:",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = orderId,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copiar código",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    clipboardManager.setText(AnnotatedString(orderId))
                                }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cerrar")
                    }
                }
            }
        }
    }
}
