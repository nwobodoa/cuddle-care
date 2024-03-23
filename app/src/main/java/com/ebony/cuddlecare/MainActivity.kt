package com.ebony.cuddlecare

import com.ebony.cuddlecare.ui.viewmodel.ProfileViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ebony.cuddlecare.ui.auth.FirebaseAuthViewModel
import com.ebony.cuddlecare.ui.screen.AuthenticatedScreens
import com.ebony.cuddlecare.ui.screen.LoginScreen
import com.ebony.cuddlecare.ui.screen.RegisterScreen
import com.ebony.cuddlecare.ui.screen.Screen
import com.ebony.cuddlecare.ui.theme.CuddleCareTheme
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            FirebaseApp.initializeApp(LocalContext.current)
            CuddleCareTheme(content = { CuddleCareApp() })
        }
    }
}


@Composable
fun CuddleCareApp(
    firebaseAuthViewModel: FirebaseAuthViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    val userUIState by profileViewModel.userUIState.collectAsState()
    val navHostController = rememberNavController()
    val firebaseAuthUIState by firebaseAuthViewModel.firebaseAuthUiState.collectAsState()
    val loadUser = { profileViewModel.loadUser(firebaseAuthUIState.currentUser) }

    LaunchedEffect(key1 = firebaseAuthUIState.currentUser) {
        loadUser()
    }

    if (userUIState.loading) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            Loading()
        }
        return
    }

    val startDestination =
        if (userUIState.user == null) Screen.Login.name else Screen.AuthenticatedLandingScreen.name

    NavHost(navController = navHostController, startDestination = startDestination) {
        composable(Screen.Login.name) {
            LoginScreen(onBackNavigation = { navHostController.popBackStack() },
                signInWithEmailPassword = firebaseAuthViewModel::signInWithEmailAndPassword,
                setEmail = firebaseAuthViewModel::setEmail,
                setPassword = firebaseAuthViewModel::setPassword,
                firebaseAuthUIState = firebaseAuthUIState,
                onNavigation = { dest -> navHostController.navigate(dest) })
        }

        composable(Screen.Register.name) {
            RegisterScreen(
                navigationController = navHostController,
                reset = firebaseAuthViewModel::reset,
                setPassword = firebaseAuthViewModel::setPassword,
                setConfirmPassword = firebaseAuthViewModel::setConfirmPassword,
                setFirstname = firebaseAuthViewModel::setFirstname,
                setLastname = firebaseAuthViewModel::setLastname,
                setEmail = firebaseAuthViewModel::setEmail,
                firebaseAuthUiState = firebaseAuthUIState,
                createAccount = firebaseAuthViewModel::createAccount
            )
        }
        composable(Screen.AuthenticatedLandingScreen.name) {
            AuthenticatedScreens(user = userUIState.user!!,
                setActiveBaby = profileViewModel::setActiveBaby,
                setUpdatedUser = profileViewModel::setUser
            )
        }

    }
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(

            Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

            ) {
            CircularProgressIndicator()

        }
    }
}
