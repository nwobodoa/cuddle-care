package com.ebony.cuddlecare.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ebony.cuddlecare.ui.auth.FirebaseAuthUiState
import com.ebony.cuddlecare.ui.auth.FirebaseAuthViewModel
import com.ebony.cuddlecare.ui.documents.UserProfile
import com.ebony.cuddlecare.ui.documents.UserUIState
import com.ebony.cuddlecare.ui.documents.UserViewModel
import drawable.MedicineScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthenticatedScreens(
    user: UserProfile,
    navController: NavHostController = rememberNavController()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val startDestination = if(user.hasAtLeastABabyInCare()) Screen.HomeScreen.name else Screen.AddBabyScreen.name

        NavHost(navController = navController, startDestination = startDestination) {

            composable(Screen.HomeScreen.name) {
                HomeScreen(onNotificationClick = { navController.navigate(Screen.ReminderScreen.name) },
                    onTopNavigation = { dest -> navController.navigate(dest) })
            }
            composable(Screen.SleepingScreen.name) {
                SleepingScreen { navController.popBackStack() }
            }

            composable(Screen.BreastfeedingScreen.name) {
                BreastfeedingScreen { navController.popBackStack() }
            }

            composable(Screen.VaccineScreen.name) {
                VaccinationScreen { navController.popBackStack() }
            }

            composable(Screen.Bottle.name) {
                BottleFeeding { navController.popBackStack() }
            }

            composable(Screen.AddBabyScreen.name) {
                AddBaby(navController)
            }
            composable(Screen.Diaper.name) {
                RecordDiaperStateScreen { navController.popBackStack() }
            }
            composable(Screen.MedicationScreen.name) {
                MedicineScreen { navController.popBackStack() }
            }
            composable(Screen.ReminderScreen.name) {
                ReminderScreen { navController.popBackStack() }
            }
            composable(Screen.CommunityScreen.name) {

            }
            composable(Screen.Profile.name) {
                AccountScreen(onTopNavigation = { dest -> navController.navigate(dest) })
            }
            composable(Screen.Statistics.name) {

            }
            composable(Screen.Caregiver.name) {
                Caregivers { navController.popBackStack() }
            }
        }
    }

}

