package com.ebony.cuddlecare.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.BottomNavBar
import com.ebony.cuddlecare.ui.components.ToggableButton
import com.ebony.cuddlecare.ui.components.TopBar
import com.ebony.cuddlecare.ui.viewmodel.CommunityViewModel
import com.ebony.cuddlecare.ui.viewmodel.SelectedTab

@Composable
fun CommunityScreen(
    onTopNavigation: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    communityViewModel: CommunityViewModel = viewModel(),
    innerPadding: PaddingValues
) {
    val navController: NavHostController = rememberNavController()
    val communityUIState by communityViewModel.communityUIState.collectAsState()

    Column(modifier = Modifier.padding(innerPadding))
    {

        TabSelection(communityViewModel)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavTopBar(onNavigateBack: () -> Unit = {}, text: String) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.orange),
            titleContentColor = Color.Black,
        ),

        navigationIcon = {
            IconButton(
                onClick = onNavigateBack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },

        title = {
            Text(
                text = text
            )
        }


    )

}

@Composable
private fun TabSelection(communityViewModel: CommunityViewModel) {
    val communityUIState by communityViewModel.communityUIState.collectAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(50))
    ) {

        ToggableButton(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            activated = communityUIState.selectedTab == SelectedTab.INBOX,
            onClick = { communityViewModel.setSelectedTab(SelectedTab.INBOX) },
        ) {
            Text(text = "INBOX")
        }

        ToggableButton(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            activated = communityUIState.selectedTab == SelectedTab.MY_POSTS,
            onClick = { communityViewModel.setSelectedTab(SelectedTab.MY_POSTS) },

            ) {
            Text(text = "MY POSTS")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListItem() {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        border = BorderStroke(2.dp, Color.Green),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .size(width = 150.dp, height = 150.dp)
    ) {
        Text(
            text = "Sleep", fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp),
            textAlign = TextAlign.Center,
        )
        Text(
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            text = "Always place your baby on their back to sleep. Do not put them to sleep on their side or stomach. This can cause suffocating or choking, which keeps them from breathing."
        )
    }
}
