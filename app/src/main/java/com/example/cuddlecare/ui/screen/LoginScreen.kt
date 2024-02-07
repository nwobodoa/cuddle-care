package com.example.cuddlecare.ui.screen

import androidx.compose.material3.Text
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
   Text(text = "I am the login page")
}