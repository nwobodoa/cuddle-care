package com.ebony.cuddlecare

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import com.ebony.cuddlecare.ui.documents.UserViewModel
import com.ebony.cuddlecare.ui.screen.AuthenticatedScreens
import com.ebony.cuddlecare.ui.screen.LoginScreen
import com.ebony.cuddlecare.ui.screen.RegisterScreen
import com.ebony.cuddlecare.ui.screen.Screen
import com.ebony.cuddlecare.ui.theme.CuddleCareTheme
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            FirebaseApp.initializeApp(LocalContext.current)
            CuddleCareTheme(content = { CuddleCareApp() })
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CuddleCareApp(
    firebaseAuthViewModel: FirebaseAuthViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val userUIState by userViewModel.userUIState.collectAsState()
    val navHostController = rememberNavController()

    LaunchedEffect(Unit) {
        userViewModel.loadUser(firebaseAuthViewModel.currentUser())
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
                firebaseAuthViewModel = firebaseAuthViewModel,
                onNavigation = { dest -> navHostController.navigate(dest) })
        }
        composable(Screen.Register.name) {
            RegisterScreen(
                navigationController = navHostController,
                firebaseAuthViewModel = firebaseAuthViewModel
            )
        }
        composable(Screen.AuthenticatedLandingScreen.name) {
            AuthenticatedScreens(userViewModel = userViewModel)
        }

    }
}

@Composable
private fun Loading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()

    }

}
