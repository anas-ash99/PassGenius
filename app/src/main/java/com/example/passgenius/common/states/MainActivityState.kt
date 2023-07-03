package com.example.passgenius.common.states

import androidx.lifecycle.MutableLiveData
import com.example.passgenius.common.enums.MainScreenTypes
import com.example.passgenius.domain.models.ItemListModel

data class MainActivityState (
    var isAuthenticated:Boolean = true,
    var currentScreen:MutableLiveData<MainScreenTypes> = MutableLiveData(),
    var plusButton:Boolean = false,
    var isPaymentLayoutShown:Boolean = false,
    var isActivityCreated:Boolean = false,
    var isBottomDialogShown:Boolean = false,
    var currentShownDialogItemPosition:Int? = null,
    var currentShownDialogItem:ItemListModel? = null,
)