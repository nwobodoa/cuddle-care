package com.ebony.cuddlecare.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandMore
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
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.documents.Gender
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TopBar(
    onNotificationClick: () -> Unit = {}, babies: List<Baby> = emptyList(),
    activeBaby: Baby? = Baby(
        id = "",
        gender = Gender.GIRL,
        name = "utaka",
        dateOfBirth = LocalDate.of(1987, 4, 11).atStartOfDay().toEpochSecond(
            ZoneOffset.UTC
        ),
        isPremature = false
    ),
    setActiveBaby: (String) -> Unit = {}
) {
   activeBaby?.let {
       TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
           containerColor = colorResource(id = R.color.orange),
           titleContentColor = Color.Black
       ), navigationIcon = {
           ProfileAvatarWithShowMore(
               modifier = Modifier.padding(start = 8.dp),
               id = activeBaby.id,
               firstName = activeBaby.name,
               showMore = babies.size > 1
           )
       },
           title = { Text(text = activeBaby.name) },
           actions = {
               //search icon
               IconButton(onClick = {

               }) {
                   Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search")
               }

               // lock icon
               IconButton(onClick = onNotificationClick) {
                   Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "Lock")
               }
           }

       )
   }
}

@Composable
fun ProfileAvatarWithShowMore(
    modifier: Modifier = Modifier,
    id: String = "",
    firstName: String = "",
    size: Dp = 40.dp,
    textStyle: TextStyle = androidx.compose.material.MaterialTheme.typography.subtitle1,
    showMore: Boolean = true,
) {
    val color = remember(firstName) {
        val name = firstName.uppercase()
        Color("$id / $name".toHslColor())
    }

    Row(modifier = Modifier.padding(end = if(showMore) 0.dp else 8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = modifier.size(size), contentAlignment = Alignment.Center
        ) {

            val initials = (firstName.take(1).uppercase())
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    SolidColor(color), radius = 70f, style = Stroke(width = 15f), alpha = 0.3f
                )
                drawCircle(SolidColor(color), radius = 55f)
            }

            Text(text = initials, style = textStyle, color = Color.White, fontSize = 18.sp)
        }
        if (showMore) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.ExpandMore, contentDescription = "", tint = color)
            }

        }

    }
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
            val name = firstName.uppercase()
            Color("$id / $name".toHslColor())
        }
        val initials = (firstName.take(1).uppercase())
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(SolidColor(color), radius = 70f, style = Stroke(width = 15f), alpha = 0.3f)
            drawCircle(SolidColor(color), radius = 55f)
        }
        Text(text = initials, style = textStyle, color = Color.White, fontSize = 18.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MTopBar(onNavigateBack: () -> Unit = {}) {
    TopAppBar(modifier = Modifier.fillMaxWidth(), colors = TopAppBarDefaults.topAppBarColors(
        containerColor = colorResource(id = R.color.orange),
        titleContentColor = Color.Black,
    ),

        navigationIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onNavigateBack,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                ProfileAvatar(
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp, top = 8.dp),
                    id = "D",
                    firstName = "David"
                )
            }
        },

        title = {
            Text(
                text = "David"
            )
        })
}

