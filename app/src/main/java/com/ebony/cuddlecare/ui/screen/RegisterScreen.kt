package com.ebony.cuddlecare.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ebony.cuddlecare.ui.auth.FirebaseAuthViewModel
import com.ebony.cuddlecare.ui.components.EmailField
import com.ebony.cuddlecare.ui.components.ErrorText
import com.ebony.cuddlecare.ui.components.PasswordField
import com.ebony.cuddlecare.ui.components.TopBar

@Composable
fun RegisterScreen(
    navigationController: NavController,
    firebaseAuthViewModel: FirebaseAuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val snackbarHostState = remember {SnackbarHostState()}
    val firebaseAuthUiState by firebaseAuthViewModel.firebaseAuthUiState.collectAsState()
    LaunchedEffect(firebaseAuthUiState.isRegistrationSuccessful) {
        if (firebaseAuthUiState.isRegistrationSuccessful) {
            scaffoldState.snackbarHostState.showSnackbar("Successfully Registered")
            navigationController.navigate(Screen.Login.name)
        }
    }
    val errors = firebaseAuthUiState.errors
    val hasError = errors.isNotEmpty()
    Scaffold(
        snackbarHost = {SnackbarHost(hostState = snackbarHostState)},
        topBar = { TopBar(navigationController = navigationController, title = "Create Account") },

        ) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, bottom = 40.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    EmailField(
                        modifier = Modifier.fillMaxWidth(),
                        email = firebaseAuthUiState.email,
                        isError = hasError
                    ) { email ->
                        firebaseAuthViewModel.setEmail(email)
                    }
                    PasswordField(
                        modifier = Modifier.fillMaxWidth(),
                        password = firebaseAuthUiState.password,
                        isError = hasError
                    ) { password ->
                        firebaseAuthViewModel.setPassword(password)
                    }
                    PasswordField(
                        modifier = Modifier.fillMaxWidth(),
                        password = firebaseAuthUiState.confirmPassword,
                        label = "confirm password",
                        isError = hasError
                    ) { confirmPassword ->
                        firebaseAuthViewModel.setConfirmPassword(confirmPassword)
                    }
                    if (hasError) {
                        Column { errors.forEach { e -> ErrorText(e) } }
                    }
                }
                Button(
                    onClick = { firebaseAuthViewModel.createAccount() },
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    Text("Register")
                }
            }
        }
    }
}