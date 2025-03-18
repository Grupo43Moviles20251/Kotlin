package com.moviles2025.freshlink43.ui.search

import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moviles2025.freshlink43.R
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import coil.compose.rememberImagePainter
import coil.size.Size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.moviles2025.freshlink43.ui.home.HomeViewModel
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.material.icons.filled.Search



@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateToProfile: () -> Unit,
    onSearchClick: () -> Unit
) {
    val context = LocalContext.current
    val query = remember { mutableStateOf("") }

    // Cargar los restaurantes cuando se crea la pantalla
    LaunchedEffect(query.value) {
        if (query.value.isNotEmpty()) {
            viewModel.getFilteredRestaurants(query.value)
        }
    }

    // Observar la lista de restaurantes
    val restaurants by viewModel.restaurants.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Barra de búsqueda
        SearchBar(query = query)

        // Botones de filtro
        FilterButtons(viewModel)

        Text(
            text = "Last Offers",
            fontSize = 24.sp,
            color = Color(0xFF2A9D8F),
            fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
            modifier = Modifier.padding(bottom = 8.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(top = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            // Recorre la lista de restaurantes usando un for y crea una tarjeta para cada uno
            items(restaurants.size) { index ->
                val restaurant = restaurants[index]
                PlaceholderRestaurantCard(
                    placeName = restaurant.name,
                    productName = restaurant.products[0].productName,
                    originalPrice = restaurant.products[0].originalPrice.toInt(),
                    discountPrice = restaurant.products[0].discountPrice.toInt(),
                    rating = restaurant.rating,
                    image = restaurant.imageUrl
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.3f)
        )

        BottomNavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            onSearchClick = onSearchClick
        )
    }
}

@Composable
fun SearchBar(query: MutableState<String>) {
    TextField(
        value = query.value,
        onValueChange = { query.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 65.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),

        placeholder = {
            Text(
                text = "Search",
                fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
                fontSize = 15.sp
            )
        },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "Search Icon")
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun FilterButtons(viewModel: SearchViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 1.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FilterButton(text = "Café", R.drawable.iconoseacrh) { viewModel.getFilteredRestaurantsByType("1") }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 1.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        FilterButton(text = "Restaurants", R.drawable.iconorest) { viewModel.getFilteredRestaurantsByType("2") }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 1.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        FilterButton(text = "Supermarkets", R.drawable.iconosuper) { viewModel.getFilteredRestaurantsByType("3") }
    }
}

@Composable
fun FilterButton(text: String, image:Int , onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A9D8F)),
        shape = RoundedCornerShape(40.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, // Para alinear el texto e icono verticalmente
            horizontalArrangement = Arrangement.Center // Para centrar el contenido
        ) {
            Icon(
                painter = painterResource(id = image), // Reemplaza 'ic_cafe' con el nombre de tu ícono
                contentDescription = "Café Icon",
                modifier = Modifier.size(35.dp), // Ajusta el tamaño del ícono
                tint = Color.White // Color del ícono
            )
            Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el ícono y el texto
            Text(
                text = text,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
                fontSize = 35.sp
            )
        }
    }
}






fun formatAmount(amount: Int): String {
    val formatter = DecimalFormat("#,###")
    formatter.decimalFormatSymbols = formatter.decimalFormatSymbols.apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
    return formatter.format(amount)
}

@Composable
fun PlaceholderRestaurantCard(
    placeName: String,
    productName: String,
    originalPrice: Int,
    discountPrice: Int,
    rating: Double,
    image: String

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(220.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val painter = rememberImagePainter(
                data = image,
                builder = {
                    size(Size.ORIGINAL)
                    crossfade(true)
                }
            )
            Image(
                painter = painter,
                contentDescription = "Restaurant Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = placeName,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.montserratalternates_bold)),
                    color = Color(0xFF2A9D8F),
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                )

                Text(
                    text = productName,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // ⭐ Sección de Rating con Icono
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFF2A9D8F),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = rating.toString(),
                            fontSize = 14.sp,
                            color = Color(0xFF2A9D8F),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }


                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Precio tachado
                        Text(
                            text = "$${formatAmount(discountPrice)}",
                            fontSize = 15.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        // Precio final en verde
                        Text(
                            text = "$${formatAmount(originalPrice)}",
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
                            color = Color(0xFF2A9D8F)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier, onSearchClick: () -> Unit) {
    val items = listOf(R.drawable.restaurante, R.drawable.corazon, R.drawable.busqueda, R.drawable.marcador)
    var selectedItem by remember { mutableStateOf(0) }

    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = Color.White
    ) {
        items.forEachIndexed { index, icon ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    if (index == 2) { // Cuando el ítem de búsqueda es clickeado (ítem 2)
                        onSearchClick() // Llamamos a la función de navegación
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Color(0xFF38677A),
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}
