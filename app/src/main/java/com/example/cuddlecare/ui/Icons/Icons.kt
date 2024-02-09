package com.example.cuddlecare.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.cuddlecare.R

@Composable
fun CommunityIcon(){

    Icon(
        painter = painterResource(id = R.drawable.outline_diversity_2_24),
        contentDescription = "community",
    )
}