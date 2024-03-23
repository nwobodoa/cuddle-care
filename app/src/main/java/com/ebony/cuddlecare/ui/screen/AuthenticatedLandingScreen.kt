package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ebony.cuddlecare.ui.viewmodel.BabyViewModel
import com.ebony.cuddlecare.ui.viewmodel.Profile
import drawable.MedicineScreen


@Composable
fun AuthenticatedScreens(
    user: Profile,
    navController: NavHostController = rememberNavController(),
    babyViewModel: BabyViewModel = viewModel(),
    setActiveBaby: (String) -> Unit,
    setUpdatedUser: (Profile) -> Unit
) {
    val babyUIState by babyViewModel.babyUIState.collectAsState()


    LaunchedEffect(
        key1 = user.primaryCareGiverTo,
        key2 = user.careGiverTo,
        key3 = user.activeBabyId
    ) {
        babyViewModel.fetchBabies(user)
    }



    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val startDestination =
            if (user.hasAtLeastABabyInCare()) Screen.HomeScreen.name else Screen.AddBabyScreen.name

        NavHost(navController = navController, startDestination = startDestination) {

            composable(Screen.HomeScreen.name) {
                HomeScreen(
                    onNotificationClick = { navController.navigate(Screen.ReminderScreen.name) },
                    onTopNavigation = { dest -> navController.navigate(dest) },
                    babies = babyUIState.listOfBabies,
                    activeBaby = babyUIState.activeBaby,
                    setActiveBaby = setActiveBaby
                )
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
                BottleFeeding(
                    onNavigateBack = { navController.popBackStack() },
                    activeBaby = babyUIState.activeBaby
                )
            }

            composable(Screen.AddBabyScreen.name) {
                AddBaby(
                    navController,
                    babyUIState = babyUIState,
                    createBaby = {
                        babyViewModel.createBaby(user, setUpdatedUser)
                    },
                    setBabyName = babyViewModel::setBabyName,
                    setIsPremature = babyViewModel::setIsPremature,
                    setSelectedDate = babyViewModel::setSelectedDate,
                    setSelectedGender = babyViewModel::setSelectedGender,
                    toggleDatePicker = babyViewModel::toggableDatePicker
                )
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

