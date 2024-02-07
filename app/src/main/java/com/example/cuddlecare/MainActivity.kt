package com.example.cuddlecare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cuddlecare.ui.auth.FirebaseAuthViewModel
import com.example.cuddlecare.ui.screen.RegisterScreen
import com.example.cuddlecare.ui.screen.LoginScreen
import com.example.cuddlecare.ui.screen.Screen
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)
        setContent {
            FirebaseApp.initializeApp(LocalContext.current)
        }
    }
}


@Composable
fun CuddleCareApp(
    firebaseAuthViewModel: FirebaseAuthViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){
        val startDestination =
            if (firebaseAuthViewModel.isAuthenticated()) Screen.AuthenticatedLandingScreen.name else Screen.Welcome.name

        NavHost(navController = navController, startDestination=startDestination) {
//            composable(Screen.Welcome.name){
//                WelcomePage(navController = navController)
//            }
            composable(Screen.Register.name){
                RegisterScreen(
                    navigationController = navController
                )
            }
            composable(Screen.Login.name) {
                LoginScreen(
                    navigationController = navController,
                    firebaseAuthViewModel = firebaseAuthViewModel
                )
            }
            composable(Screen.AuthenticatedLandingScreen.name){
                Screen.AuthenticatedLandingScreen(
                    firebaseAuthViewModel = firebaseAuthViewModel,
                    topNavHostController = navController
                )
            }
        }
    }
}

