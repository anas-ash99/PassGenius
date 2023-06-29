package com.example.passgenius.common.states

import androidx.lifecycle.MutableLiveData
import com.example.passgenius.common.enums.MainScreenTypes

data class MainActivityState (
    var isAuthenticated:Boolean = true,
    var currentScreen:MutableLiveData<MainScreenTypes> = MutableLiveData(),
    var plusButton:Boolean = false,
    var isPaymentLayoutShown:Boolean = false,
    var isActivityCreated:Boolean = false
)