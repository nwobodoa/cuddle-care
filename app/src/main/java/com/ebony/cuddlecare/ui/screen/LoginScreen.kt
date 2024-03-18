package com.ebony.cuddlecare.ui.screen

import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ebony.cuddlecare.ui.auth.FirebaseAuthViewModel
import com.ebony.cuddlecare.ui.components.EmailField
import com.ebony.cuddlecare.ui.components.ErrorText
import com.ebony.cuddlecare.ui.components.PasswordField
import com.ebony.cuddlecare.ui.components.ScreenScaffold
import com.ebony.cuddlecare.ui.components.TopBar
import com.ebony.cuddlecare.ui.documents.UserViewModel

@Composable
fun LoginScreen(
    onBackNavigation: () -> Unit,
    onNavigation: (String) -> Unit,
    firebaseAuthViewModel: FirebaseAuthViewModel,

) {

    var enabledSignInButton by remember { mutableStateOf(false) }
    val firebaseAuthUIState by firebaseAuthViewModel.firebaseAuthUiState.collectAsState()
    val hasError = firebaseAuthUIState.errors.isNotEmpty()


    LaunchedEffect(key1 = firebaseAuthUIState.email, key2 = firebaseAuthUIState.password) {
        val isValidEmail =
            Patterns.EMAIL_ADDRESS.matcher(firebaseAuthUIState.email.trim()).matches()
        val isValidPassword = firebaseAuthUIState.password.length >= 6
        enabledSignInButton = isValidEmail && isValidPassword
    }

    LaunchedEffect(key1 = firebaseAuthUIState.currentUser) {
        if (firebaseAuthUIState.currentUser != null) {
            onNavigation(Screen.AuthenticatedLandingScreen.name)
        }
    }

    ScreenScaffold(
        topBar = { TopBar(title = "Sign In") },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.padding(top = 32.dp)) {
            EmailField(
                modifier = Modifier.fillMaxWidth(),
                email = firebaseAuthUIState.email,
                isError = hasError
            ) {
                firebaseAuthViewModel.setEmail(it)
            }
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
            PasswordField(
                modifier = Modifier.fillMaxWidth(),
                password = firebaseAuthUIState.password,
                isError = hasError
            ) {
                firebaseAuthViewModel.setPassword(it)
            }
            if (hasError) {
                Column { firebaseAuthUIState.errors.forEach { e -> ErrorText(e) } }
            }
            Spacer(modifier = Modifier.padding(bottom = 16.dp, top = 16.dp))
            Button(onClick = {
                firebaseAuthViewModel
                    .signInWithEmailAndPassword()
            }, modifier = Modifier
                .height(55.dp)
                .fillMaxWidth(), enabled = enabledSignInButton
            ) {
                if (firebaseAuthUIState.loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                } else {
                    Text("Sign in", fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
        }
        Row {
            Text("Forgot Password?", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(end = 8.dp))

            Text(text = "No Account? ")

                Text("Signup", color = Color.Blue, fontWeight = FontWeight.Bold, modifier= Modifier.clickable { onNavigation(Screen.Register.name) })

        }
    }

}
