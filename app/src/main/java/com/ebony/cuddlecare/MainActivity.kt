package com.ebony.cuddlecare

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import com.ebony.cuddlecare.ui.auth.FirebaseAuthViewModel
import com.ebony.cuddlecare.ui.screen.AddBabyScaffold
import com.ebony.cuddlecare.ui.screen.BottleFeeding
import com.ebony.cuddlecare.ui.screen.BreastfeedingScreen
import com.ebony.cuddlecare.ui.screen.HomeScreen
import com.ebony.cuddlecare.ui.screen.LoginScreen
import com.ebony.cuddlecare.ui.screen.RecordDiaperStateScreen
import com.ebony.cuddlecare.ui.screen.RegisterScreen
import com.ebony.cuddlecare.ui.screen.ReminderScreen
import com.ebony.cuddlecare.ui.screen.ReminderSetting
import com.ebony.cuddlecare.ui.screen.Screen
import com.ebony.cuddlecare.ui.screen.VaccinationScreen
import com.ebony.cuddlecare.ui.theme.CuddleCareTheme
import com.google.firebase.FirebaseApp
import drawable.MedicineScreen


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)
        setContent {

            FirebaseApp.initializeApp(LocalContext.current)
            CuddleCareTheme(content = { CuddleCareApp()})
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
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
            if (firebaseAuthViewModel.isAuthenticated()) Screen.HomeScreen.name else Screen.Login.name

        NavHost(navController = navController, startDestination=Screen.HomeScreen.name) {
//            composable(Screen.Welcome.name){
//                WelcomePage(navController = navController)
//            }
            composable(Screen.HomeScreen.name) {
                HomeScreen(onNotificationClick = {navController.navigate(Screen.ReminderScreen.name)}, onTopNavigation = { dest -> navController.navigate(dest) })
            }

            composable(Screen.BreastfeedingScreen.name){
                BreastfeedingScreen { navController.popBackStack() }
            }

            composable(Screen.VaccineScreen.name) {
                VaccinationScreen{navController.popBackStack()}
            }
            composable(Screen.Register.name){
                RegisterScreen(
                    navigationController = navController
                )
            }
            composable(Screen.Bottle.name){
                BottleFeeding {navController.popBackStack()}
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
            composable(Screen.Diaper.name){
                RecordDiaperStateScreen{navController.popBackStack()}
            }
            composable(Screen.MedicationScreen.name){
                MedicineScreen{navController.popBackStack()}
            }
            composable(Screen.ReminderScreen.name) {
                ReminderScreen {navController.popBackStack()}
            }
        }
    }

}

