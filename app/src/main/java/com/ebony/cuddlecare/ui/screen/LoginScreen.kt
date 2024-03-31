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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.ebony.cuddlecare.ui.auth.UserAuthUIState
import com.ebony.cuddlecare.ui.components.EmailField
import com.ebony.cuddlecare.ui.components.ErrorText
import com.ebony.cuddlecare.ui.components.PasswordField
import com.ebony.cuddlecare.ui.components.ScreenScaffold
import com.ebony.cuddlecare.ui.components.TopBar

@Composable
fun LoginScreen(
    onNavigation: (String) -> Unit,
    userAuthUIState: UserAuthUIState,
    signInWithEmailPassword: () -> Unit,
    setEmail: (String) -> Unit,
    setPassword: (String) -> Unit

) {

    var enabledSignInButton by remember { mutableStateOf(false) }
    val hasError = userAuthUIState.error?.isNotEmpty() ?: false


    LaunchedEffect(key1 = userAuthUIState.email, key2 = userAuthUIState.password) {
        val isValidEmail =
            Patterns.EMAIL_ADDRESS.matcher(userAuthUIState.email.trim()).matches()
        val isValidPassword = userAuthUIState.password.length >= 6
        enabledSignInButton = isValidEmail && isValidPassword
    }


    ScreenScaffold(
        topBar = { TopBar(title = "Sign In") },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.padding(top = 32.dp)) {
            EmailField(
                modifier = Modifier.fillMaxWidth(),
                email = userAuthUIState.email,
                isError = hasError,
                onChange = setEmail
            )
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
            PasswordField(
                modifier = Modifier.fillMaxWidth(),
                password = userAuthUIState.password,
                isError = hasError,
                onChange = setPassword
            )

            userAuthUIState.error?.let { Column { ErrorText(it) } }

            Spacer(modifier = Modifier.padding(bottom = 16.dp, top = 16.dp))
            Button(
                onClick = signInWithEmailPassword,
                modifier = Modifier
                    .height(55.dp)
                    .fillMaxWidth(), enabled = enabledSignInButton
            ) {
                if (userAuthUIState.loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                } else {
                    Text("Sign in", fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
        }
        Row {
            Text(
                "Forgot Password?",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(end = 8.dp))

            Text(text = "No Account? ")

            Text(
                "Signup",
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigation(Screen.Register.name) })

        }
    }

}
