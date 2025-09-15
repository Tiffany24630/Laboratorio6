package com.tiffany.salazar.laboratorio6

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
import androidx.compose.runtime.snapshotFlow
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
import androidx.paging.PagingData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.tiffany.salazar.laboratorio6.ui.theme.Laboratorio6Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.collect

/**
 * Actividad principal de la aplicación.
 * Configura el tema, la navegación y la pantalla inicial.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // API key tomada de BuildConfig (definida en Gradle)
        val apiKey = "\"1JggYpijM42g4lIlc38xam2PqbYinwDqMy6BbO5IDGKRH3YXrvGxIEuI\""

        setContent {
            Laboratorio6Theme {
                AppNavigation(apiKey = apiKey)
            }
        }
    }
}

/**
 * Modelo de dominio para una foto.
 */
data class Photo(
    val id: String,
    val imageUrl: String,
    val description: String? = null,
    val author: String = "Autor desconocido",
    val likes: Int = 0
)

/**
 * Rutas de navegación de la aplicación.
 */
object AppDestinations {
    const val HOME_ROUTE = "home"
    const val PROFILE_ROUTE = "profile"
    const val DETAILS_ROUTE = "details"
    const val PHOTO_ID_KEY = "photoId"
    const val DETAILS_WITH_ARG_ROUTE = "$DETAILS_ROUTE/{$PHOTO_ID_KEY}"
}

/**
 * Configuración de la navegación principal.
 */
@Composable
fun AppNavigation(apiKey: String) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppDestinations.HOME_ROUTE) {
        composable(AppDestinations.HOME_ROUTE) { HomeScreen(navController = navController, apiKey = apiKey) }
        composable(AppDestinations.PROFILE_ROUTE) { ProfileScreen(navController = navController) }
        composable(AppDestinations.DETAILS_WITH_ARG_ROUTE) { backStackEntry ->
            val photoId = backStackEntry.arguments?.getString(AppDestinations.PHOTO_ID_KEY)
            DetailsScreen(navController = navController, photoId = photoId)
        }
    }
}

/**
 * Tarjeta para mostrar una foto.
 */
@Composable
fun PhotoCard(photo: Photo, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = photo.imageUrl,
                contentDescription = photo.description ?: "Foto",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            photo.description?.let {
                Text(text = it, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

/**
 * Pantalla principal con búsqueda y paginación de fotos.
 *
 * Maneja todo el estado localmente con remember / rememberSaveable
 * y aplica debounce sobre la query con snapshotFlow + debounce(500L).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, apiKey: String) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var debouncedQuery by rememberSaveable { mutableStateOf("") }

    // Debounce usando snapshotFlow
    LaunchedEffect(Unit) {
        snapshotFlow { searchQuery }
            .debounce(500L)
            .distinctUntilChanged()
            .collect { q -> debouncedQuery = q }
    }

    // Crear Pager recordado por query
    val photosFlow: Flow<PagingData<Photo>> = remember(debouncedQuery) {
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { RealPhotoPagingSource(apiKey = apiKey, searchQuery = debouncedQuery) }
        ).flow
    }

    val lazyPagingItems = photosFlow.collectAsLazyPagingItems()

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
                label = { Text("Buscar fotos (Pexels)...") },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                singleLine = true
            )

            LazyVerticalGrid(columns = GridCells.Adaptive(128.dp), modifier = Modifier.fillMaxSize()) {
                items(lazyPagingItems.itemCount) { idx ->
                    lazyPagingItems[idx]?.let { photo ->
                        PhotoCard(photo) {
                            navController.navigate("${AppDestinations.DETAILS_ROUTE}/${photo.id}")
                        }
                    }
                }

                // Load state handlers (append, prepend, refresh)
                lazyPagingItems.apply {
                    when (loadState.append) {
                        is androidx.paging.LoadState.Loading -> item { CircularProgressIndicator() }
                        is androidx.paging.LoadState.Error -> {
                            val e = loadState.append as androidx.paging.LoadState.Error
                            item { Text("Error al cargar más: ${e.error.localizedMessage}") }
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}

/**
 * Pantalla de detalles de la foto seleccionada.
 * Carga details via FakePhotoApi para demo; sustituir por llamada real si se desea.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavController, photoId: String?) {
    var photoDetail by remember { mutableStateOf<Photo?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    LaunchedEffect(photoId) {
        if (photoId == null) {
            error = "ID de foto no válido."
            isLoading = false
            return@LaunchedEffect
        }
        isLoading = true
        error = null
        try {
            val fetchedPhoto = FakePhotoApiGetById(photoId) // Replaceable hook for real API
            photoDetail = fetchedPhoto
            if (fetchedPhoto == null) {
                error = "Foto no encontrada."
            }
        } catch (e: Exception) {
            error = "Error al cargar detalles: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(photoDetail?.author ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> CircularProgressIndicator()
                error != null -> Text("Error: $error")
                photoDetail != null -> {
                    val photo = photoDetail!!
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = photo.imageUrl,
                            contentDescription = photo.description,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(4f / 3f),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(photo.description ?: "Sin descripción", style = MaterialTheme.typography.titleLarge)
                        Text("Autor: ${photo.author}", style = MaterialTheme.typography.bodyMedium)
                        Text("Likes: ${photo.likes}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "¡Mira esta foto de ${photo.author}! ${photo.imageUrl}")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, null))
                        }) {
                            Icon(Icons.Filled.Share, "Compartir")
                            Text("Compartir")
                        }
                    }
                }
                else -> Text("Foto no disponible.")
            }
        }
    }
}

/**
 * Perfil de usuario (estado local).
 * En una app real, el tema persistiría con DataStore.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    var isDarkThemeEnabled by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Perfil") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
            Text("Nombre Mock", style = MaterialTheme.typography.headlineSmall)
            Text("email@mock.com", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tema Oscuro (Local)", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = isDarkThemeEnabled,
                    onCheckedChange = { isDarkThemeEnabled = it }
                )
            }
        }
    }
}

/**
 * Helper: busca foto por id en FakePhotoApi.
 * Mantener separado para poder reemplazar por llamada real.
 */
fun FakePhotoApiGetById(photoId: String): Photo? {
    // Simplemente busca en una lista generada
    val idx = photoId.filter { it.isDigit() }.takeLast(3).toIntOrNull() ?: return null
    return Photo(
        id = photoId,
        imageUrl = "https://picsum.photos/seed/${idx}/600/800",
        description = "Foto demo id=$photoId",
        author = "Autor $photoId",
        likes = 123
    )
}
