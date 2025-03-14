package net.azarquiel.gafas.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import net.azarquiel.gafas.R
import net.azarquiel.gafas.model.Comentario
import net.azarquiel.gafas.model.Gafa
import net.azarquiel.gafas.model.Marca
import net.azarquiel.gafas.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailGafaScreen(navController: NavHostController, viewModel: MainViewModel) {
    val gafa = navController.previousBackStackEntry?.savedStateHandle?.get<Gafa>("gafa")
    val marcas = viewModel.marcas.observeAsState(listOf())
    val marca = marcas.value.find { it.id == gafa?.marca }

    gafa?.let {
        Scaffold(
            topBar = { DetailTopBar(viewModel) },
            floatingActionButton = { AddCommentButton { viewModel.setDialogComentario(true) } },
            content = { padding -> DetailContent(padding, viewModel, gafa, marca) }
        )
    }

    if (viewModel.openDialogComentario.observeAsState(false).value) {
        gafa?.let { DialogComment(viewModel, it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar(viewModel: MainViewModel) {
    val usuario = viewModel.usuario.observeAsState()
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Detail",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
                Text(
                    text = usuario.value?.nick ?: "",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(R.color.azul),
            titleContentColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun DetailContent(padding: PaddingValues, viewModel: MainViewModel, gafa: Gafa, marca: Marca?) {
   // val comentarios = viewModel.getComentarios(gafa.id).observeAsState(emptyList())
    val comentarios = viewModel.getComentarios(gafa.id).observeAsState(emptyList())


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        ShowGafa(gafa, marca)
        LazyColumn {
            items(comentarios.value) { comentario ->
                CommentCard(comentario)
            }
        }
    }
}

@Composable
fun ShowGafa(gafa: Gafa, marca: Marca?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = "http://www.ies-azarquiel.es/paco/apigafas/img/gafas/${gafa.imagen}",
                contentDescription = gafa.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = gafa.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${gafa.precio} €", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                AsyncImage(
                    model = "http://www.ies-azarquiel.es/paco/apigafas/img/marcas/${marca?.imagen}",
                    contentDescription = "Marca",
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}

@Composable
fun CommentCard(comentario: Comentario) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.azul),
            contentColor = colorResource(R.color.black)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = comentario.fecha, fontSize = 14.sp)
                Text(text = comentario.id.toString(), fontSize = 14.sp)
                Text(text = comentario.nick, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = comentario.comentario, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AddCommentButton(onClick: () -> Unit) {
    FloatingActionButton(
        containerColor = colorResource(R.color.black),
        contentColor = MaterialTheme.colorScheme.background,
        onClick = onClick
    ) {
        Icon(Icons.Filled.Edit, contentDescription = "Add Comment")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DialogComment(viewModel: MainViewModel, gafa: Gafa) {
    val context = LocalContext.current
    var comment by remember { mutableStateOf("") }
    val usuario = viewModel.usuario.observeAsState().value ?: return
    val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    val comentario = Comentario(0, usuario.id, "" , gafa.id, fecha, comment)

    AlertDialog(
        onDismissRequest = { viewModel.setDialogComentario(false) },
        title = { Text(text = "Añadir Comentario") },
        text = {
            TextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Comentario") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        },
        confirmButton = {
            Button(onClick = { viewModel.saveComentario(gafa.id, comentario); viewModel.setDialogComentario(false) }) {
                Text("Ok")
            }
        },
        dismissButton = {
            Button(onClick = { viewModel.setDialogComentario(false) }) {
                Text("Cancelar")
            }
        }
    )
}
