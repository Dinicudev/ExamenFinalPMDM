package net.azarquiel.gafas.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import net.azarquiel.gafas.R
import net.azarquiel.gafas.model.Gafa
import net.azarquiel.gafas.model.Marca
import net.azarquiel.gafas.viewmodel.MainViewModel


@Composable
fun DetailScreen(navController: NavHostController, viewModel: MainViewModel) {
    val marca = navController.previousBackStackEntry?.savedStateHandle?.get<Marca>("marca")
    marca?.let { nonNullMarca ->
        viewModel.getGafasPorMarca(nonNullMarca.id)
    }
    Scaffold(
        topBar = { GafasTopBar(viewModel) },
        content = { padding ->
            GafasContent(padding, navController, viewModel)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GafasTopBar(viewModel: MainViewModel) {
    val usuario = viewModel.usuario.observeAsState()
    TopAppBar(
        title = { Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Gafas",
                modifier = Modifier
                    .padding(0.dp, 0.dp, 10.dp, 0.dp)
                    .weight(1f),
                textAlign = TextAlign.Start
            )
            Text(
                text = if (usuario.value != null) "  ${usuario.value!!.nick}" else "",
                modifier = Modifier
                    .padding(0.dp, 0.dp, 10.dp, 0.dp)
                    .weight(1f),
                textAlign = TextAlign.End
            )
        }
        },
        colors = topAppBarColors(
            containerColor = colorResource(R.color.azul),
            titleContentColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun GafasContent(
    padding: PaddingValues,
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val gafas = viewModel.gafas.observeAsState(listOf())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
    )
    {
        LazyColumn {
            itemsIndexed(gafas.value)
            { i, gafa ->
                CardRecurso(gafa, navController, viewModel)
            }
        }
    }
}

@Composable
fun CardRecurso(gafa: Gafa, navController: NavHostController, viewModel: MainViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set("gafa", gafa)
                //navController.currentBackStackEntry?.savedStateHandle?.set("marca", marca)
                navController.navigate("DetailGafaScreen")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.azul),
            contentColor = Color.Black
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "http://www.ies-azarquiel.es/paco/apigafas/img/gafas/${gafa.imagen}",
                contentDescription = gafa.nombre,
                modifier = Modifier
                    .size(140.dp,80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = gafa.nombre,
                modifier = Modifier.weight(8f).fillMaxWidth(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.white),
                textAlign = TextAlign.Center
            )
        }
    }
}