package com.ebony.cuddlecare

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
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
import com.ebony.cuddlecare.ui.auth.UserAuthViewModel
import com.ebony.cuddlecare.ui.components.Loading
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
    userAuthViewModel: UserAuthViewModel = viewModel()
) {
    val navHostController = rememberNavController()
    val userAuthUIState by userAuthViewModel.userAuthUIState.collectAsState()


    if (userAuthUIState.loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Loading()
        }
        return
    }
    Log.i(TAG, "CuddleCareApp: recomposed $userAuthUIState")
    val startDestination =
        if (userAuthUIState.user == null) Screen.Login.name else Screen.AuthenticatedLandingScreen.name

    NavHost(navController = navHostController, startDestination = startDestination) {
        composable(Screen.Login.name) {
            LoginScreen(
                signInWithEmailPassword = userAuthViewModel::signInWithEmailAndPassword,
                setEmail = userAuthViewModel::setEmail,
                setPassword = userAuthViewModel::setPassword,
                userAuthUIState = userAuthUIState,
                onNavigation = { dest -> navHostController.navigate(dest) })
        }

        composable(Screen.Register.name) {
            RegisterScreen(navigationController = navHostController)
        }
        composable(Screen.AuthenticatedLandingScreen.name) {
            userAuthUIState.user?.let { user ->
                AuthenticatedScreens(
                    user = user,
                    setActiveBaby = userAuthViewModel::setActiveBaby,
                    setUpdatedUser = userAuthViewModel::setUser,
                    onSignOut = userAuthViewModel::signOut
                )
            }
        }

    }
}
