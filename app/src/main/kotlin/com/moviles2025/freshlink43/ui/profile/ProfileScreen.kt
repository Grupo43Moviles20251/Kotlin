package com.moviles2025.freshlink43.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.moviles2025.freshlink43.R
import com.moviles2025.freshlink43.data.AnalyticsManager
import com.moviles2025.freshlink43.utils.*
import com.moviles2025.freshlink43.ui.navigation.NavRoutes
import com.moviles2025.freshlink43.utils.corporationGreen
import com.moviles2025.freshlink43.utils.corporationOrange
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import com.moviles2025.freshlink43.ui.navigation.BottomNavManager
import com.moviles2025.freshlink43.ui.navigation.Header

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel) {
    LaunchedEffect(Unit) {
        AnalyticsManager.logFeatureUsage("ProfileScreen")
    }

    val user by viewModel.user.collectAsStateWithLifecycle()
    val photoUrl by viewModel.photoUrl.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.uploadPhoto(it)
        }
    }
    Scaffold(
        topBar = { Header()},
        bottomBar = {
            BottomNavManager(
                navController = navController,
                selectedTab = "profile" // O el nombre que desees usar
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(120.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(context)
                                .data(photoUrl ?: R.drawable.profileicon)
                                .crossfade(true)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .build()
                        ),
                        contentDescription = "User Profile",
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
                                Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG)
                                    .show()
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(32.dp)
                            .background(Color.White, shape = CircleShape)
                            .padding(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "Edit Photo",
                            tint = corporationOrange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = user?.name ?: "User",
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.montserratalternates_bold))
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 180.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        ProfileField(
                            icon = R.drawable.ic_mail,
                            label = "Email",
                            value = user?.email ?: "No email"
                        )
                        ProfileField(
                            icon = R.drawable.marcador,
                            label = "Address",
                            value = user?.address ?: "No address"
                        )
                        ProfileField(
                            icon = R.drawable.ic_birthday,
                            label = "Birthday",
                            value = user?.birthday ?: "No birthday"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                /*
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = corporationGreen),
                    //modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Go Back", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))
    */
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
}

@Composable
fun ProfileField(icon: Int, label: String, value: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = corporationGreen,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = value, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)))
    }
}
