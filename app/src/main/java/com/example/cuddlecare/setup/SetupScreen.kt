package com.example.cuddlecare.setup

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cuddlecare.ui.auth.FirebaseAuthViewModel
import com.example.cuddlecare.ui.theme.ui.viewmodel.ProfileViewModel
import com.example.cuddlecare.ui.theme.ui.viewmodel.SetupViewModel

@Composable
fun SetupScreen(
    setupViewModel: SetupViewModel = viewModel(),
    topNavController: NavHostController,
    firebaseAuthViewModel: FirebaseAuthViewModel,
    profileViewModel: ProfileViewModel
){

}