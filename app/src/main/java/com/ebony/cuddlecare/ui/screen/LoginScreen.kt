package com.ebony.cuddlecare.ui.screen

import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ebony.cuddlecare.ui.auth.FirebaseAuthViewModel
import com.ebony.cuddlecare.ui.components.EmailField
import com.ebony.cuddlecare.ui.components.ErrorText
import com.ebony.cuddlecare.ui.components.PasswordField
import com.ebony.cuddlecare.ui.components.ScreenScaffold
import com.ebony.cuddlecare.ui.components.TopBar

@Composable
fun LoginScreen(
   navigationController: NavHostController,
   firebaseAuthViewModel: FirebaseAuthViewModel
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
         navigationController.navigate(Screen.AuthenticatedLandingScreen.name)
      }
   }

   ScreenScaffold(
      topBar = { TopBar(navigationController = navigationController, title = "Sign In") },
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
         Spacer(modifier = Modifier.padding(bottom = 16.dp))
         Button(onClick = {
            firebaseAuthViewModel
               .signInWithEmailAndPassword()
         }, modifier = Modifier.fillMaxWidth(), enabled = enabledSignInButton) {
            if (firebaseAuthUIState.loading) {
               CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
               Text("Sign in")
            }
         }
         Spacer(modifier = Modifier.padding(bottom = 16.dp))
      }
      TextButton(onClick = { /*TODO*/ }) {
         Text("Forgot Password?")
      }
   }

}
