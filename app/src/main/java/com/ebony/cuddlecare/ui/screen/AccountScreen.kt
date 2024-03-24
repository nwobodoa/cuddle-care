package com.ebony.cuddlecare.ui.screen

import CareGiver
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ebony.cuddlecare.ui.components.AccountAvatar
import com.ebony.cuddlecare.ui.components.BottomNavBar
import com.ebony.cuddlecare.ui.components.ProfileAvatar
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.theme.backcolor
import kotlinx.coroutines.launch


@Composable
fun AccountScreen(user: CareGiver, babies: List<Baby>, onTopNavigation: (String) -> Unit) {
    var isAccountManagementOpen by remember { mutableStateOf(false) }
    Scaffold(
        bottomBar = { BottomNavBar(onTopNavigation) },
        topBar = { HeaderText(text = "Account") }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .padding(16.dp),
            Arrangement.Center
        ) {
            MainPageContent(
                onClick = { isAccountManagementOpen = true },
                onTopNavigation = onTopNavigation,
                user = user,
                babies = babies
            )
            AccountManagement(
                isOpen = isAccountManagementOpen,
                onClose = { isAccountManagementOpen = false })
        }
    }
}


@Composable
fun HeaderText(text: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(color = backcolor)
            .padding(16.dp)
    ) {
        Text(text = text, fontWeight = FontWeight.Bold, fontSize = 28.sp)
    }

}

@Composable
fun MainPageContent(
    onClick: () -> Unit,
    onTopNavigation: (String) -> Unit,
    user: CareGiver,
    babies: List<Baby>
) {

    Log.i(TAG, "MainPageContent: *** $babies")
    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(color = Color.White)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AccountAvatar(id = user.uuid, firstName = user.firstname, radius = 75f)
            Column {
                Text(text = "${user.firstname} ${user.lastname}")
                Text(text = user.email)

            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Text(text = "...", fontSize = 28.sp, modifier = Modifier
                    .clickable { onClick() })
            }

        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Canvas(
                    modifier = Modifier.wrapContentSize(),
                ) {
                    drawCircle(SolidColor(Color.Green), radius = 20f)
                }
                Text(text = "Connected")
            }
            Icon(imageVector = Icons.Default.Repeat, contentDescription = null)
        }
    }
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(text = "Babies", fontSize = 18.sp)
        Icon(imageVector = Icons.Default.AddCircle, contentDescription = null,
            modifier = Modifier.clickable { onTopNavigation(Screen.AddBabyScreen.name) })
    }
    LazyColumn(
        modifier = Modifier
            .wrapContentSize()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(babies.count()) { item ->
            BabyDetails(onNavigate = { onTopNavigation(Screen.Caregiver.name) }, babies[item])
        }
    }
}

@Composable
fun BabyDetails(onNavigate: () -> Unit, baby: Baby) {
    val careGivers = baby.careGivers.plus(baby.primaryCareGivers)
    Column(modifier = Modifier
        .background(color = Color.White)
        .padding(16.dp)) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProfileAvatar(id = baby.id, firstName = baby.name)
            Column(modifier = Modifier.padding(end = 16.dp)) {
                Text(text = baby.name)
                Text(text = "0d (week 1)")

            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Text(text = "...", fontSize = 28.sp)
            }

        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LazyRow {
                items(careGivers.count()) {
                    AccountAvatar(id = careGivers[it].uuid, firstName = careGivers[it].firstname, radius = 50f, font = 15.sp)
                }
            }
            OutlinedButton(onClick = onNavigate) {
                Icon(imageVector = Icons.Outlined.PeopleOutline, contentDescription = null)
                Text(text = "Caregivers")

            }
        }
    }
}

@Composable
fun SheetPageContent(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(375.dp)
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Row(modifier = Modifier.padding(bottom = 32.dp)) {

                AccountAvatar(id = "A9309404911", firstName = "Adanwa", radius = 140f)
            }
            Row {

                Text(text = "adanwobodo84@gmail.com")
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Icon(imageVector = Icons.Default.PermIdentity, contentDescription = null)
            Text(text = "Adanwa", modifier = Modifier.padding(start = 8.dp))
            Row(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {

                Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
            Text(text = "Sign out", modifier = Modifier.clickable { /*TODO:implement logout*/ })
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = Icons.Outlined.Delete, contentDescription = null, tint = Color.Red)
            Text(
                text = "Delete account",
                color = Color.Red,
                modifier = Modifier.clickable { /*TODO:implement account deletion*/ })
        }
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .height(40.dp)
                .padding(start = 24.dp, end = 24.dp)
                .fillMaxWidth(),
        )
        {
            Text(text = "Close", fontSize = 20.sp)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountManagement(isOpen: Boolean, onClose: () -> Unit) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    if (isOpen) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = onClose,
            dragHandle = {
                Column(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                }
            }) {

            SheetPageContent(onClick = {
                scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                    if (!bottomSheetState.isVisible) {
                        onClose()
                    }
                }

            }
            )
        }
    }
}

