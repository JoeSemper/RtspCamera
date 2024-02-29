package com.joesemper.rtspcamera.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joesemper.rtspcamera.ui.screens.home.HomeScreen
import com.joesemper.rtspcamera.ui.screens.stream.StreamScreen

const val HOME_ROUTE: String = "home"
const val STREAM_ROUTE: String = "stream"

@Composable
fun AppNavHost(
    startDestination: String = STREAM_ROUTE
) {

    val navController = rememberNavController()

    Scaffold() { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = startDestination
        ) {
            composable(route = HOME_ROUTE) {
                HomeScreen(
                    navigateToStream = {
                        navController.navigate(route = STREAM_ROUTE) {
                            launchSingleTop = true
                            popUpTo(STREAM_ROUTE)
                        }
                    }
                )
            }

            composable(route = STREAM_ROUTE) {
                StreamScreen(
                    navigateHome = {
                        navController.navigate(HOME_ROUTE){
                            launchSingleTop = true
                            popUpTo(STREAM_ROUTE)
                        }
                    }
                )
            }
        }
    }
}
