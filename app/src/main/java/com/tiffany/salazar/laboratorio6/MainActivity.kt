package com.tiffany.salazar.laboratorio6

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

/*import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.delay
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.snapshotFlow
import java.util.Properties*/


/*import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.Properties*/
import com.tiffany.salazar.laboratorio6.ui.theme.Laboratorio6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent { //Configuración de navegación y pantalla principal
            Laboratorio6Theme{
                AppNavigation()
            }
        }
    }
}

/*data class Photo(val id: String, val imageUrl: String, val description: String? = null, val author: String = "Autor desconocido", val likes: Int = 0) //Modelo para una foto*/
data class Photo(val id: String, val imageUrl: String, val description: String? = null, val author: String = "Autor desconocido", val likes: Int = 0)

//Clases que representan la respuesta de la API de Pexels
data class PexelsResponse(val page: Int, val perPage: Int, val photos: List<PexelsPhoto>, val totalResults: Int)
data class PexelsPhoto(val id: Int, val width: Int, val height: Int, val url: String, val photographer: String, val photographerUrl: String, val photographerId: Int, val avgColor: String?, val src: PexelsPhotoSrc, val liked: Boolean, val alt: String?)
data class PexelsPhotoSrc(val original: String, val large2x: String, val large: String, val medium: String, val small: String, val portrait: String, val landscape: String, val tiny: String)

object AppDestinations{ //Rutas de navegación de la app
    const val HOME_ROUTE = "home"
    const val PROFILE_ROUTE = "profile"
    const val DETAILS_ROUTE = "details"
    const val PHOTO_ID_KEY = "photoId"
    const val DETAILS_WITH_ARG_ROUTE = "$DETAILS_ROUTE/{$PHOTO_ID_KEY}"
}

@Composable
fun AppNavigation(){ //Configuración de la navegación principal
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppDestinations.HOME_ROUTE) {
        composable(AppDestinations.HOME_ROUTE) { HomeScreen(navController) }
        composable(AppDestinations.PROFILE_ROUTE) { ProfileScreen(navController) }
        composable(AppDestinations.DETAILS_WITH_ARG_ROUTE) { backStackEntry ->
            val photoId = backStackEntry.arguments?.getString(AppDestinations.PHOTO_ID_KEY)
            DetailsScreen(navController, photoId)
        }
    }
}

/*@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController){ //Pantalla principal con búsqueda y paginación de fotos
    var searchQuery by rememberSaveable { mutableStateOf("") }      //Query actual
    var debouncedQuery by rememberSaveable { mutableStateOf("") }   //Query tras debounce

    LaunchedEffect(Unit){ //Debounce de la búsqueda
        snapshotFlow{ searchQuery }.debounce(500L).distinctUntilChanged().collect { q -> debouncedQuery = q }
    }

    val photosFlow: Flow<PagingData<Photo>> = remember(debouncedQuery){ //Flujo de fotos paginadas dependientes de query
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                RealPhotoPagingSource(apiKey = localProperties.getProperty("PEXELS_API_KEY"), searchQuery = debouncedQuery)
            }
        ).flow
    }
    val lazyPagingItems = photosFlow.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fotos") },
                actions = {
                    IconButton(onClick = { navController.navigate(AppDestinations.PROFILE_ROUTE) }){
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Perfil")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)){
            OutlinedTextField( //Barra de búsqueda
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar fotos...") },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                singleLine = true
            )

            LazyVerticalGrid(columns = GridCells.Adaptive(128.dp), modifier = Modifier.fillMaxSize()){ //Grid de fotos
                items(lazyPagingItems.itemCount) { idx ->
                    lazyPagingItems[idx]?.let { photo ->
                        PhotoCard(photo){
                            navController.navigate("${AppDestinations.DETAILS_ROUTE}/${photo.id}")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var debouncedQuery by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        snapshotFlow { searchQuery }
            .debounce(500L)
            .distinctUntilChanged()
            .collect { q -> debouncedQuery = q }
    }

    val apiKey = BuildConfig.PEXELS_API_KEY

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fotos") },
                actions = {
                    IconButton(onClick = { navController.navigate(AppDestinations.PROFILE_ROUTE) }) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Perfil")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar fotos...") },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                singleLine = true
            )

            if (apiKey.isNotEmpty()) {
                val photosFlow: Flow<PagingData<Photo>> = remember(debouncedQuery) {
                    Pager(
                        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                        pagingSourceFactory = {
                            RealPhotoPagingSource(apiKey = apiKey, searchQuery = debouncedQuery)
                        }
                    ).flow
                }
                val lazyPagingItems = photosFlow.collectAsLazyPagingItems()

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(128.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(lazyPagingItems.itemCount) { idx ->
                        lazyPagingItems[idx]?.let { photo ->
                            PhotoCard(photo) {
                                navController.navigate("${AppDestinations.DETAILS_ROUTE}/${photo.id}")
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("API key no configurada. Agrega PEXELS_API_KEY en local.properties")
                }
            }
        }
    }
}*/


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var debouncedQuery by rememberSaveable { mutableStateOf("") }

    // Use a LaunchedEffect to implement debounce without snapshotFlow
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            delay(500L) // Wait for 500ms
            if (searchQuery == debouncedQuery) return@LaunchedEffect
            debouncedQuery = searchQuery
        }
    }

    val apiKey = BuildConfig.PEXELS_API_KEY

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fotos") },
                actions = {
                    IconButton(onClick = { navController.navigate(AppDestinations.PROFILE_ROUTE) }) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Perfil")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar fotos...") },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                singleLine = true
            )

            if (apiKey.isNotEmpty()) {
                val photosFlow: Flow<PagingData<Photo>> = remember(debouncedQuery) {
                    Pager(
                        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                        pagingSourceFactory = {
                            RealPhotoPagingSource(apiKey = apiKey, searchQuery = debouncedQuery)
                        }
                    ).flow
                }
                val lazyPagingItems = photosFlow.collectAsLazyPagingItems()

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(128.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(lazyPagingItems.itemCount) { idx ->
                        lazyPagingItems[idx]?.let { photo ->
                            PhotoCard(photo) {
                                navController.navigate("${AppDestinations.DETAILS_ROUTE}/${photo.id}")
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("API key no configurada. Agrega PEXELS_API_KEY en local.properties")
                }
            }
        }
    }
}

@Composable
fun PhotoCard(photo: Photo, onClick: () -> Unit){ //Card para mostrar una foto
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = photo.imageUrl,
                contentDescription = photo.description ?: "Foto",
                modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            photo.description?.let{
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavController, photoId: String?){ //Pantalla de detalles de la foto seleccionada
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(photoId ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }){
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center){
            if (photoId == null){
                Text("ID no válido")
            }else{
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    Text("Detalle de la foto con ID: $photoId")
                    Spacer(Modifier.height(16.dp))

                    Button(onClick = {
                        val sendIntent = Intent().apply{
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Mira esta foto! id=$photoId")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, null))
                    }){
                        Icon(Icons.Filled.Share, contentDescription = "Compartir")
                        Text("Compartir")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController){ //Pantalla de perfil de usuario
    var isDarkThemeEnabled by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Perfil") }) }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Avatar",
                modifier = Modifier.size(120.dp).clip(CircleShape)
            )

            Text("Nombre Mock", style = MaterialTheme.typography.headlineSmall)
            Text("email@mock.com", style = MaterialTheme.typography.bodyLarge)

            Spacer(Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tema Oscuro", style = MaterialTheme.typography.bodyLarge)
                Switch(checked = isDarkThemeEnabled, onCheckedChange = { isDarkThemeEnabled = it })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Laboratorio6Theme { HomeScreen(navController = rememberNavController()) }
}


