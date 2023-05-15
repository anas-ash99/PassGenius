package com.example.passgenius.domain.models

import java.time.LocalDateTime

data class UserModel(
    var username:String = "",
    var email:String = "",
    var password:String = "",
    var lastSeen:String = LocalDateTime.now().toString(),
    var favourites:List<String> = listOf(),
    var settings: SettingsModel = SettingsModel()
) :java.io.Serializable
