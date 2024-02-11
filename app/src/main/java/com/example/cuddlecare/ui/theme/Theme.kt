package com.example.cuddlecare.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.cuddlecare.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    primary = myRed,
    secondary = myOrange,
    tertiary = myOrange,
    background = backcolor
)

private val LightColorScheme = lightColorScheme(
    primary = myOrange,
    secondary = myRed,
    tertiary = myOrange,
    background = backcolor

    /* Other default colors to override   ,
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun CuddleCareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val systemUiController = rememberSystemUiController()
    if (darkTheme){
        systemUiController.setSystemBarsColor(Color.Transparent)
    }
        else{


        systemUiController.setStatusBarColor(colorResource(id = R.color.orange),darkIcons = true)
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}