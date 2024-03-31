package com.ebony.cuddlecare.ui.screen

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.outlined.PermIdentity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.AccountAvatar
import com.ebony.cuddlecare.ui.components.MTopBar
import com.ebony.cuddlecare.ui.documents.Baby
import com.ebony.cuddlecare.ui.documents.CareGiver
import com.ebony.cuddlecare.ui.documents.Invite
import com.ebony.cuddlecare.ui.viewmodel.CareGiverUIState
import kotlinx.coroutines.launch

@Composable
fun CareGiverScreen(
    onNavigateBack: () -> Unit = {},
    careGiverUIState: CareGiverUIState,
    onSendInvite: (String) -> Unit,
    resetResponse: () -> Unit,
    currentUser: CareGiver,
    fetchPendingSentInvites: (String, String) -> Unit,
    pendingSentInvites: List<Invite>
) {
    val babyToUpdate = careGiverUIState.baby ?: return
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val isPrimaryCareGiver = babyToUpdate.primaryCareGiverIds.any { it == currentUser.uuid }

//    TODO Handle this properly and this should never happen
    var isCaregiverDialogOpen by remember { mutableStateOf(false) }
    var inviteeEmailAddress by remember { mutableStateOf("") }

    LaunchedEffect(key1 = currentUser, key2 = careGiverUIState.baby) {
        if (isPrimaryCareGiver) {
            fetchPendingSentInvites(babyToUpdate.id, currentUser.uuid)
        }
    }

    LaunchedEffect(key1 = careGiverUIState.inviteResponse) {
        Log.i(TAG, "CareGiverScreen inviteResponse: ${careGiverUIState.inviteResponse}")
        if (careGiverUIState.inviteResponse != null) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = careGiverUIState.inviteResponse,
                    duration = SnackbarDuration.Long
                )

            }

        }
    }

    Scaffold(topBar = { MTopBar(onNavigateBack = onNavigateBack, activeBaby = babyToUpdate) })
    { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues))
        {
            HeaderText(text = "Caregivers")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(color = colorResource(id = R.color.backcolor)),
                verticalArrangement = Arrangement.spacedBy(16.dp)

            ) {

                PrimaryCareGivers(babyToUpdate)
                PendingSentInvites(pendingSentInvites = pendingSentInvites)
                CareGivers(
                    babyToUpdate = babyToUpdate,
                    setOpenSheet = { isCaregiverDialogOpen = true },
                    currentUser = currentUser
                )

                CaregiverDialog(
                    isOpen = isCaregiverDialogOpen,
                    onClose = { isCaregiverDialogOpen = false },
                    setInviteeEmailAddress = { email: String -> inviteeEmailAddress = email },
                    onConfirm = {
                        onSendInvite(inviteeEmailAddress)
                    },
                    baby = babyToUpdate,
                    inviteeEmailAddress = inviteeEmailAddress,

                    )


            }
        }
    }
}

@Composable
private fun PrimaryCareGivers(babyToUpdate: Baby) {
    Text(text = "Primary caregiver", fontSize = 18.sp)
    LazyColumn {
        items(babyToUpdate.primaryCareGivers.count()) {
            CareGiverRow(careGiver = babyToUpdate.primaryCareGivers[it])
        }
    }
}

@Composable
private fun CareGivers(babyToUpdate: Baby, setOpenSheet: () -> Unit, currentUser: CareGiver) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Caregivers", fontSize = 18.sp)
        val isPrimaryCareGiver =
            babyToUpdate.primaryCareGiverIds.any { id -> id == currentUser.uuid }
        if (isPrimaryCareGiver) {
            IconButton(onClick = setOpenSheet) {
                Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
            }
        }

    }

    LazyColumn {
        items(babyToUpdate.careGivers.count()) {
            CareGiverRow(
                careGiver = babyToUpdate.careGivers[it],
                showClose = babyToUpdate.primaryCareGiverIds.contains(currentUser.uuid)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaregiverDialog(
    isOpen: Boolean,
    onClose: () -> Unit,
    onConfirm: () -> Unit,
    baby: Baby,
    setInviteeEmailAddress: (String) -> Unit,
    inviteeEmailAddress: String,

    ) {

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (isOpen) {
        ModalBottomSheet(
            onDismissRequest = onClose,
            sheetState = bottomSheetState,
            dragHandle = null
        ) {
            DialogSheet(
                bottomSheetState,
                onClose,
                onConfirm,
                baby,
                setInviteeEmailAddress = setInviteeEmailAddress,
                inviteeEmailAddress = inviteeEmailAddress,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogSheet(
    bottomSheetState: SheetState,
    onClose: () -> Unit,
    onConfirm: () -> Unit,
    baby: Baby,
    setInviteeEmailAddress: (String) -> Unit,
    inviteeEmailAddress: String,
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .background(Color.White)
            .wrapContentSize()
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        val onBottomSheetClose = {
            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                if (!bottomSheetState.isVisible) {
                    onClose()
                }
            }
        }

        Text(text = "Add Caregiver", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(
            text = "Share ${baby.name}'s data with your partner or nanny. Enter" +
                    " caregiver's email address to send an invite.",
            fontSize = 16.sp
        )
        TextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = colorResource(id = R.color.backcolor),
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier.fillMaxWidth(),
            value = inviteeEmailAddress,
            onValueChange = setInviteeEmailAddress,
            label = { Text(text = "Caregiver's email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        HorizontalDivider()
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(modifier = Modifier.padding(end = 16.dp),
                onClick = { onBottomSheetClose() }
            ) {
                Text(text = "Cancel")
            }
            Button(onClick = {
                onConfirm()
                onBottomSheetClose()
            }) {
                Text(text = "Send")
            }

        }
    }


}


@Composable
fun CareGiverRow(careGiver: CareGiver, showClose: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            AccountAvatar(id = careGiver.uuid, firstName = careGiver.firstname, radius = 75f)
        }

        Column {
            Text(text = "${careGiver.firstname} ${careGiver.lastname}")
            Text(text = careGiver.email)
        }
        if (showClose) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = null)
            }
        }

    }
}

@Composable
@Preview(showBackground = true)
fun PendingSentInvites(pendingSentInvites: List<Invite> = emptyList()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        if (pendingSentInvites.isNotEmpty()) {
            Text(text = "Pending invites")
        }
        LazyColumn {
            items(pendingSentInvites.size) {

                PendingSentInvite(invite = pendingSentInvites[it])
            }
        }
    }
}

@Composable
private fun PendingSentInvite(invite: Invite) {
    val openAlertDialog = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .padding(start = 16.dp),
            imageVector = Icons.Outlined.PermIdentity,
            contentDescription = null
        )
        Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
            Text(text = "Waiting for response", fontWeight = FontWeight.Bold)
            Text(text = invite.careGiverEmail)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { /*TODO*/ }) {

                Icon(
                    modifier = Modifier.padding(end = 16.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun DeleteAlertDialog() {

    AlertDialog(
        containerColor = Color.White,
        icon = { Icon(imageVector = Icons.Default.WarningAmber, contentDescription = null) },
        title = { Text(text = "Delete invite for caregiver" + "cinwobi@gmail.com") },
        onDismissRequest = { /*TODO*/ },
        dismissButton = {
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Delete")
            }
        })
}

// TODO Only babies that the current is a primary giver for will have the add caregiver button
// TODO Babes should be sorted by something
// TODO For each baby, we should show pending invites only when the current user is a primary caregiver
// TODO Primary caregiver should be able to cancel a pending request
// TODO [Stretch Goal] Send a notification to a user when the a request for caregiving is rejected
// TODO Handle accepting, and rejecting invites

