package com.ebony.cuddlecare.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class SelectedTab{
    INBOX,
    MY_POSTS,
}
data class CommunityUIState(
   val selectedTab: SelectedTab = SelectedTab.INBOX,
)
class CommunityViewModel : ViewModel(){

    private val _selectionUIState: MutableStateFlow<CommunityUIState> =
        MutableStateFlow(CommunityUIState())
    val communityUIState: StateFlow<CommunityUIState> = _selectionUIState.asStateFlow()

    fun setSelectedTab(tab: SelectedTab){
        _selectionUIState.update { it.copy(selectedTab = tab) }
    }



}