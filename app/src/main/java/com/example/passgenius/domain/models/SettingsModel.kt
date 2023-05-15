package com.example.passgenius.domain.models

data class SettingsModel(
    var lockAppTime:Int = 0,
    var darkMode:Boolean = false,
    var biometricUnlock:Boolean = false,
    var pinCode:Boolean = false,
    var requireAppPassword:Int = 0, ///////0 means never
    var allowScreenShots:Boolean = false,
    var sendNewsLetter:Boolean = false,
    var sendNotifications:Boolean = true
)
