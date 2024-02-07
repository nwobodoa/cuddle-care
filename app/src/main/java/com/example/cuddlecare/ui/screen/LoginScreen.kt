package com.example.cuddlecare.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.example.cuddlecare.ui.auth.FirebaseAuthViewModel

@Composable
fun LoginScreen(
    navigationController: NavHostController,
    firebaseAuthViewModel: FirebaseAuthViewModel
){
    var enabledSignInButton by remember{ mutableStateOf(false) }
    val firebaseAuthUiState by firebaseAuthViewModel.firebaseAuthUiState.collectAsState()
    val hasError = firebaseAuthUiState.errors.isNotEmpty()

    LaunchedEffect(key1 = firebaseAuthUiState.currentUser){
        if (firebaseAuthUiState.currentUser != null){
            navigationController.navigate(Screen.AuthenticatedLandingScreen.name)
        }
    }
}