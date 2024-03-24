package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.SnackbarHost
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.SnackbarHostState
//noinspection UsingMaterialAndMaterial3Libraries
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
import com.ebony.cuddlecare.ui.components.EmailField
import com.ebony.cuddlecare.ui.components.ErrorText
import com.ebony.cuddlecare.ui.components.PasswordField
import com.ebony.cuddlecare.ui.components.TopBar
import com.ebony.cuddlecare.ui.auth.RegisterViewModel

@Composable
fun RegisterScreen(
    navigationController: NavController,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val snackbarHostState = remember { SnackbarHostState() }
    val registerUIState by registerViewModel.registerUIState.collectAsState()

    LaunchedEffect(registerUIState.isRegistrationSuccessful) {
        if (registerUIState.isRegistrationSuccessful) {
            navigationController.navigate(Screen.Login.name)
            scaffoldState.snackbarHostState.showSnackbar("Successfully Registered")
        }
    }
    val errors = registerUIState.errors
    val hasError = errors.isNotEmpty()
    Scaffold(
        //TODO:check why snackbar not showing up after registration is successful
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
                    OutlinedTextField(value = registerUIState.firstname,
                        onValueChange = registerViewModel::setFirstname,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Firstname") })

                    OutlinedTextField(value = registerUIState.lastname,
                        onValueChange = registerViewModel::setLastname,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Lastname") })

                    EmailField(
                        modifier = Modifier.fillMaxWidth(),
                        email = registerUIState.email,
                        isError = hasError,
                        onChange = registerViewModel::setEmail
                    )

                    PasswordField(
                        modifier = Modifier.fillMaxWidth(),
                        password = registerUIState.password,
                        isError = hasError,
                        onChange = registerViewModel::setPassword
                    )

                    PasswordField(
                        modifier = Modifier.fillMaxWidth(),
                        password = registerUIState.confirmPassword,
                        label = "confirm password",
                        isError = hasError,
                        onChange = registerViewModel::setConfirmPassword
                    )

                    if (hasError) {
                        Column { errors.forEach { e -> ErrorText(e) } }
                    }
                }
                Button(
                    onClick = registerViewModel::createAccount,
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
