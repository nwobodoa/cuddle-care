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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cuddlecare.ui.auth.FirebaseAuthViewModel
import com.example.cuddlecare.ui.components.TopBar
import com.example.cuddlecare.ui.screen.AddBabyScaffold
import com.example.cuddlecare.ui.screen.HomeSceen
import com.example.cuddlecare.ui.screen.LoginScreen
import com.example.cuddlecare.ui.screen.RegisterScreen
import com.example.cuddlecare.ui.screen.Screen
import com.example.cuddlecare.ui.theme.CuddleCareTheme
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)
        setContent {

            FirebaseApp.initializeApp(LocalContext.current)
            CuddleCareTheme(content = { AddBabyScaffold() })
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
            if (firebaseAuthViewModel.isAuthenticated()) Screen.AddBabyScreen.name else Screen.Login.name

        NavHost(navController = navController, startDestination=Screen.AddBabyScreen.name) {
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
            composable(Screen.AddBabyScreen.name){
                AddBabyScaffold()
            }
        }
    }

}

