package id.bangkumis.tradewise.ui.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import id.bangkumis.tradewise.ui.screens.Screen
import id.bangkumis.tradewise.ui.screens.detail.CoinDetailScreen
import id.bangkumis.tradewise.ui.screens.portfolio.PortfolioScreen
import id.bangkumis.tradewise.ui.theme.TradeWiseTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TradeWiseTheme {
                val viewModel: MainViewModel = hiltViewModel()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ){
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    Scaffold(
                        bottomBar = {
                            BottomAppBar {
                                NavigationBarItem(
                                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Dashboard") },
                                    label = { Text("Dashboard") },
                                    selected = currentRoute == Screen.DashboardScreen.route,
                                    onClick = { navController.navigate(Screen.DashboardScreen.route) }
                                )
                                NavigationBarItem(
                                    icon = { Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = "Portfolio") },
                                    label = { Text("Portfolio") },
                                    selected = currentRoute == Screen.PortfolioScreen.route,
                                    onClick = { navController.navigate(Screen.PortfolioScreen.route) }
                                )
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.DashboardScreen.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(route = Screen.DashboardScreen.route) {
                                DashboardScreen(navController = navController)
                            }
                            composable(
                                route = Screen.CoinDetailScreen.route,
                                arguments = listOf(navArgument("coinID"){ type = NavType.StringType})
                            ) {
                                CoinDetailScreen()
                            }
                            composable(route = Screen.PortfolioScreen.route) {
                                PortfolioScreen(navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}