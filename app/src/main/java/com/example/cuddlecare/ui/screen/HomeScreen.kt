package com.example.cuddlecare.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cuddlecare.ui.components.BottomBar
import com.example.cuddlecare.ui.components.TopBar


@Composable
fun HomeSceen(){
    Scaffold (
        topBar = { TopBar()},
        bottomBar={ BottomBar()},

    ){innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            TipsCard()
            Text(text = "Body goes here")
        }



    }


}


