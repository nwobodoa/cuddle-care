package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ebony.cuddlecare.ui.components.BottomNavBar
import com.ebony.cuddlecare.ui.components.TopBar


@Composable
fun HomeScreen(){
    Scaffold (
        topBar = { TopBar()},
        bottomBar={ BottomNavBar()},

    ){innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            TipsCard()
            Text(text = "Body goes here")
        }



    }


}


