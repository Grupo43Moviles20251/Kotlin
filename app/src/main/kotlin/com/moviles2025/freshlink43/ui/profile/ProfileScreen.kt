package com.moviles2025.freshlink43.ui.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.moviles2025.freshlink43.R
import com.moviles2025.freshlink43.data.AnalyticsManager
import com.moviles2025.freshlink43.ui.navigation.BottomNavManager
import com.moviles2025.freshlink43.ui.navigation.Header
import com.moviles2025.freshlink43.ui.navigation.NavRoutes
import com.moviles2025.freshlink43.utils.corporationOrange
import com.moviles2025.freshlink43.utils.corporationGreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        AnalyticsManager.logFeatureUsage("ProfileScreen")
    }

    val user by viewModel.user.collectAsStateWithLifecycle()
    val photoUrl by viewModel.photoUrl.collectAsStateWithLifecycle()
    val isEditing by viewModel.isEditing.collectAsStateWithLifecycle()
    val nameField by viewModel.editableName.collectAsStateWithLifecycle()
    val addressField by viewModel.editableAddress.collectAsStateWithLifecycle()
    val birthdayField by viewModel.editableBirthday.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.uploadPhoto(it) }
    }

    Scaffold(
        topBar = { Header() },
        bottomBar = {
            BottomNavManager(navController = navController, selectedTab = "profile")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar y botón de edición de foto
            Box(modifier = Modifier.size(120.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(photoUrl ?: R.drawable.profileicon)
                            .crossfade(true)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .build()
                    ),
                    contentDescription = "User Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .align(Alignment.Center)
                )
                IconButton(
                    onClick = {
                        if (viewModel.isConnectedToInternet(context)) {
                            imagePickerLauncher.launch("image/*")
                        } else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp)
                        .background(Color.White, shape = CircleShape)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Photo",
                        tint = corporationOrange,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Nombre (Text o TextField)
            if (isEditing) {
                OutlinedTextField(
                    value = nameField,
                    onValueChange = { viewModel.editableName.value = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = user?.name ?: "User",
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.montserratalternates_bold))
                )
            }

            Spacer(Modifier.height(24.dp))

            // Card con campos y botón de editar/guardar integrado
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box {
                    // IconButton de editar o guardar usando painterResource
                    IconButton(
                        onClick = {
                            if (isEditing) viewModel.saveProfile()
                            else viewModel.toggleEdit()
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isEditing) Icons.Default.Save else Icons.Default.Edit,
                            contentDescription = if (isEditing) "Guardar" else "Editar",
                            tint = corporationOrange
                        )
                    }

                    Column(modifier = Modifier.padding(24.dp)) {
                        // Email (siempre lectura)
                        ProfileField(
                            icon = R.drawable.ic_mail,
                            label = "Email",
                            value = user?.email ?: "No email"
                        )
                        Spacer(Modifier.height(12.dp))

                        // Address
                        if (isEditing) {
                            OutlinedTextField(
                                value = addressField,
                                onValueChange = { viewModel.editableAddress.value = it },
                                label = { Text("Address") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            ProfileField(
                                icon = R.drawable.marcador,
                                label = "Address",
                                value = user?.address ?: "No address"
                            )
                        }
                        Spacer(Modifier.height(12.dp))

                        // Birthday
                        if (isEditing) {
                            OutlinedTextField(
                                value = birthdayField,
                                onValueChange = { viewModel.editableBirthday.value = it },
                                label = { Text("Birthday") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            ProfileField(
                                icon = R.drawable.ic_birthday,
                                label = "Birthday",
                                value = user?.birthday ?: "No birthday"
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Sign Out
            Button(
                onClick = {
                    viewModel.signOut()
                    navController.navigate(NavRoutes.Main.route) {
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = corporationOrange)
            ) {
                Text("Sign Out", color = Color.White)
            }
        }
    }
}

@Composable
fun ProfileField(icon: Int, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = corporationGreen,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.montserratalternates_semibold))
        )
    }
}
