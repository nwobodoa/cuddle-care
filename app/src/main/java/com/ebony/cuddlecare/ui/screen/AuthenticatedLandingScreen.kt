package com.ebony.cuddlecare.ui.screen

import com.ebony.cuddlecare.ui.documents.CareGiver
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
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ebony.cuddlecare.ui.viewmodel.BabyViewModel
import com.ebony.cuddlecare.ui.viewmodel.BreastfeedingViewModel
import com.ebony.cuddlecare.ui.viewmodel.CareGiverViewModel
import com.ebony.cuddlecare.ui.viewmodel.SleepingViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

//TODO What are we doing with names

@Composable
fun AuthenticatedScreens(
    user: CareGiver,
    navController: NavHostController = rememberNavController(),
    babyViewModel: BabyViewModel = viewModel(),
    careGiverViewModel: CareGiverViewModel = viewModel(),
    breastfeedingViewModel: BreastfeedingViewModel = viewModel(),
    sleepingViewModel: SleepingViewModel = viewModel(),
    setActiveBaby: (String) -> Unit,
    setUpdatedUser: (CareGiver) -> Unit,
    onSignOut: () -> Unit
) {
    val babyUIState by babyViewModel.babyUIState.collectAsState()
    val careGiverUIState by careGiverViewModel.careGiverUIState.collectAsState()
    val breastfeedingUIState by breastfeedingViewModel.breastfeedingUIState.collectAsState()
    val leftBreastUIState by breastfeedingViewModel.leftBreastUIState.collectAsState()
    val rightBreastUIState by breastfeedingViewModel.rightBreastUIState.collectAsState()
    val sleepingUIState by sleepingViewModel.sleepingUIState.collectAsState()

    LaunchedEffect(key1 = user) {
        babyViewModel.fetchBabies(user)
    }

    LaunchedEffect(key1 = babyUIState.activeBaby) {
        breastfeedingViewModel.reset()
    }

    LaunchedEffect(key1 = sleepingUIState.timerState) {
        while (sleepingUIState.timerState == TimerState.PAUSED) {
            delay(1.seconds)
            sleepingViewModel.incrementPauseTimer()
        }

        while (sleepingUIState.timerState == TimerState.STARTED) {
            delay(1.seconds)
            sleepingViewModel.incrementTimer()
        }
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
                SleepingScreen(
                    onNavigateBack = navController::popBackStack,
                    sleepingUIState = sleepingUIState,
                    incrementTimer = sleepingViewModel::incrementTimer,
                    incrementPauseTimer = sleepingViewModel::incrementPauseTimer,
                    toggleTimerState = sleepingViewModel::toggleTimerState,
                    saveSleep = {
                        sleepingViewModel.save(babyUIState.activeBaby)
                    },
                    setNotes = sleepingViewModel::setNotes,
                    reset = sleepingViewModel::reset
                )

            }

            composable(Screen.BreastfeedingScreen.name) {
                BreastfeedingScreen(
                    onNavigateBack = { navController.popBackStack() },
                    breastfeedingUIState = breastfeedingUIState,
                    rightBreastUIState = rightBreastUIState,
                    leftBreastUIState = leftBreastUIState,
                    toggleRightButton = breastfeedingViewModel::toggleRightBtnActiveState,
                    toggleLeftButton = breastfeedingViewModel::toggleLeftBtnActiveState,
                    increaseLeftTimer = breastfeedingViewModel::incrementLeftTimer,
                    increaseRightTimer = breastfeedingViewModel::incrementRightTimer,
                    incrementPauseTimer = breastfeedingViewModel::incrementPauseTimer,
                    onNotesValueChange = breastfeedingViewModel::onNotesValueChange,
                    saveBreastFeeding = { breastfeedingViewModel.save(babyUIState.activeBaby) },
                    resetSaved = { breastfeedingViewModel.setSaved(false) }
                )
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
                    navController = navController,
                    user = user,
                    setUpdatedUser = setUpdatedUser,
                    onTopNavigation = { dest -> navController.navigate(dest) },

                )
            }

            composable(Screen.Diaper.name) {
                DiaperScreen(
                    onNavigateBack = navController::popBackStack,
                    activeBaby = babyUIState.activeBaby
                )
            }

            composable(Screen.MedicationScreen.name) {
                MedicineScreen(
                    onNavigateBack = navController::popBackStack,
                    activeBaby = babyUIState.activeBaby
                )

            }
            composable(Screen.ReminderScreen.name) {
                ReminderScreen { navController.popBackStack() }
            }
            composable(Screen.CommunityScreen.name) {
                CommunityScreen(
                    onTopNavigation = {dest -> navController.navigate(dest)},
                    onNavigateBack = {navController.popBackStack()},
                )
            }
            composable(Screen.Profile.name) {
                AccountScreen(
                    onTopNavigation = { dest -> navController.navigate(dest) },
                    babies = babyUIState.listOfBabies,
                    user = user,
                    setBabyToUpdate = { baby -> careGiverViewModel.setBaby(baby) },
                    onSignOut = onSignOut
                )
            }
            composable(Screen.Statistics.name) {
                StatisticsScreen(
                    onNotificationClick = { navController.navigate(Screen.ReminderScreen.name) },
                    onTopNavigation = { dest -> navController.navigate(dest) },
                    babies = babyUIState.listOfBabies,
                    activeBaby = babyUIState.activeBaby,
                    setActiveBaby = setActiveBaby
                )
            }
            composable(Screen.Caregiver.name) {
                CareGiverScreen(
                    onNavigateBack = { navController.popBackStack() },
                    careGiverUIState = careGiverUIState,
                    currentUser = user,
                    onSendInvite = { inviteeEmail ->
                        careGiverViewModel.sendInvite(
                            inviteeEmail,
                            user.uuid
                        )
                    },
                    resetResponse = careGiverViewModel::resetResponse,
                    fetchPendingSentInvites = careGiverViewModel::fetchPendingSentInvites,
                    pendingSentInvites = careGiverUIState.pendingSentInvites
                )
            }
        }
    }

}

