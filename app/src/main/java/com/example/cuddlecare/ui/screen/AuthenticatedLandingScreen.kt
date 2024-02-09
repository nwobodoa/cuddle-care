//package com.example.cuddlecare.ui.screen
//TODO finish of the implemnetation
//import android.app.Application
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.cuddlecare.ui.auth.FirebaseAuthViewModel
//import com.example.cuddlecare.ui.viewmodel.ProfileViewModel
//
//enum class AuthenticatedLandingScreen {
//    setup,
//    Application
//}
//@Composable
//fun AuthenticatedLandingScreen(
//    firebaseAuthViewModel: FirebaseAuthViewModel = viewModel(),
//){
//    //val profileUiState by ProfileViewModel.profileUiState.collectAsState()
//    val navHostController = rememberNavController()
//
//    LaunchedEffect(Unit ) {
//  //      ProfileViewModel.getUserProfile(firebaseAuthViewModel.currentUser()!!)
//    }
//    if (profileUiState.loading) {
//        Column(modifier = Modifier.padding(start = 16.dp,end =16.dp)){
//            Loading()
//        }
//        return
//    }
//    val startDestination =
//        if (profileUiState.profile == null) AuthenticatedLandingScreen.setup.name else AuthenticatedLandingScreen.Application.name
//
//    NavHost(navController = navHostController, startDestination = startDestination ) {
//        composable(AuthenticatedLandingScreen.setup.name){
//            Column (modifier = Modifier.padding(start = 16.dp, end = 16.dp)){
//                SetupScreen(
//                    firebaseAuthViewModel = firebaseAuthViewModel,
//                    topNavController = topNavHostController,
//                    profileViewmodel = ProfileViewModel
//                )
//
//            }
//        }
//        composable(AuthenticatedLandingScreen.Application.name){
//            //Screen.ApplicationScreen()
//        }
//    }
//}
//
//@Composable
//private fun Loading(){
//    Column(
//    modifier = Modifier.fillMaxSize(),
//    verticalArrangement = Arrangement.Center,
//    horizontalAlignment = Alignment.CenterHorizontally
//    ){
//        CircularProgressIndicator()
//    }
//
//}
