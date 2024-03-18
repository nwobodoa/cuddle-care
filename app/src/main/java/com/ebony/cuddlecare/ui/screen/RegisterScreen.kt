package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ebony.cuddlecare.ui.auth.FirebaseAuthUiState
import com.ebony.cuddlecare.ui.auth.FirebaseAuthViewModel
import com.ebony.cuddlecare.ui.components.EmailField
import com.ebony.cuddlecare.ui.components.ErrorText
import com.ebony.cuddlecare.ui.components.PasswordField
import com.ebony.cuddlecare.ui.components.TopBar

@Composable
fun RegisterScreen(
    navigationController: NavController,
    reset:() -> Unit,
    firebaseAuthUiState: FirebaseAuthUiState,
    setFirstname:(String) -> Unit,
    setLastname:(String) -> Unit,
    setEmail: (String) -> Unit,
    setPassword: (String) -> Unit,
    setConfirmPassword: (String) -> Unit,
    createAccount: () -> Unit
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(firebaseAuthUiState.isRegistrationSuccessful) {
        if (firebaseAuthUiState.isRegistrationSuccessful) {
            navigationController.navigate(Screen.Login.name)
            scaffoldState.snackbarHostState.showSnackbar("Successfully Registered")
            reset()
        }
    }
    val errors = firebaseAuthUiState.errors
    val hasError = errors.isNotEmpty()
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopBar(

                title = "Create Account"
            )
        },

        ) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, bottom = 40.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween

            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(value = firebaseAuthUiState.firstname,
                        onValueChange = setFirstname,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Firstname") })

                    OutlinedTextField(value = firebaseAuthUiState.lastname,
                        onValueChange = setLastname,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Lastname") })

                    EmailField(
                        modifier = Modifier.fillMaxWidth(),
                        email = firebaseAuthUiState.email,
                        isError = hasError,
                        onChange = setEmail
                    )

                    PasswordField(
                        modifier = Modifier.fillMaxWidth(),
                        password = firebaseAuthUiState.password,
                        isError = hasError,
                        onChange = setPassword
                    )

                    PasswordField(
                        modifier = Modifier.fillMaxWidth(),
                        password = firebaseAuthUiState.confirmPassword,
                        label = "confirm password",
                        isError = hasError,
                        onChange = setConfirmPassword
                    )

                    if (hasError) {
                        Column { errors.forEach { e -> ErrorText(e) } }
                    }
                }
                Button(
                    onClick = createAccount,
                    modifier = Modifier
                        .height(55.dp)
                        .fillMaxWidth()
                )
                {
                    Text("Register", fontSize = 20.sp)
                }
            }
        }
    }
}
