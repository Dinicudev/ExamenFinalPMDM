package net.azarquiel.gafas.navigation

import net.azarquiel.gafas.viewmodel.MainViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.azarquiel.gafas.screens.DetailGafaScreen
import net.azarquiel.gafas.screens.DetailScreen
import net.azarquiel.gafas.screens.MasterScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = AppScreens.MasterScreen.route){
        composable(AppScreens.MasterScreen.route){
            MasterScreen(navController, viewModel)
        }
        composable(AppScreens.DetailScreen.route){
            DetailScreen(navController, viewModel)
        }
        composable(AppScreens.DetailGafaScreen.route){
            DetailGafaScreen(navController, viewModel)
        }
        composable(AppScreens.DetailRecursosScreen.route){
           // DetailRecursosScreen(navController, viewModel)
        }
    }
}
sealed class AppScreens(val route: String) {
    object MasterScreen: AppScreens(route = "MasterScreen")
    object DetailScreen: AppScreens(route = "DetailScreen")
    object DetailGafaScreen: AppScreens(route = "DetailGafaScreen")
    object DetailRecursosScreen: AppScreens(route = "DetailRecursosScreen")
}
