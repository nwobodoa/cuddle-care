package com.ebony.cuddlecare.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ebony.cuddlecare.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
        TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.orange),
            titleContentColor = Color.Black,
        ),

        navigationIcon = {
            ProfileAvatar(
                modifier = Modifier.padding(start = 16.dp), id = "D", firstName = "David"
            )
        },

        title = {
            Text(
                modifier = Modifier.padding(start = 24.dp),
                text = "David"
            )
        },
        actions = {
            //search icon
            IconButton(onClick = {

            }) {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search")
            }

            // lock icon
            IconButton(onClick = {

            }) {
                Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "Lock")
            }
        }

    )
}

@Composable
fun ProfileAvatar(
    id: String,
    firstName: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    textStyle: TextStyle = androidx.compose.material.MaterialTheme.typography.subtitle1,
) {
    Box(
        modifier.size(size), contentAlignment = Alignment.Center
    ) {
        val color = remember(firstName) {
            val name = firstName
                .uppercase()
            Color("$id / $name".toHslColor())
        }
        val initials = (firstName.take(1).uppercase())
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(SolidColor(color), radius = 70f, style = Stroke(width = 15f), alpha = 0.3f)
            drawCircle(SolidColor(color),radius = 55f)
        }
        Text(text = initials, style = textStyle, color = Color.White, fontSize= 18.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MTopBar(navController: NavHostController = rememberNavController()) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.orange),
            titleContentColor = Color.Black,
        ),

        navigationIcon = {
            IconButton(
                onClick = navController::popBackStack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            ProfileAvatar(
                modifier = Modifier.padding(start = 46.dp ,end=8.dp,bottom=8.dp,top=8.dp), id = "D", firstName = "David"
            )
        },

        title = {
            Text(
                text = "David"
            )
        }


    )
}

