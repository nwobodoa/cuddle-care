package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ebony.cuddlecare.ui.components.BottomNavBar
import com.ebony.cuddlecare.ui.components.TopBar
import com.ebony.cuddlecare.ui.documents.Baby

@Composable
fun StatisticsScreen (
    onNotificationClick: () -> Unit = {},
    onTopNavigation: (String) -> Unit,
    babies: List<Baby>,
    setActiveBaby: (String) -> Unit,
    activeBaby: Baby?){
    Scaffold(
    topBar = {
        TopBar(
            onNotificationClick = {},
            babies = babies,
            setActiveBaby = setActiveBaby,
            activeBaby = activeBaby

        )
    },
    bottomBar = { BottomNavBar(onTopNavigation) },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

        }
        }
}