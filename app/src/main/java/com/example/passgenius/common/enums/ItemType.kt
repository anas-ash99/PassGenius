package com.example.passgenius.common.enums

import java.io.Serializable

enum class ItemType(name:String): Serializable {
    LOGIN("LOGIN"),
    NOTE("NOTE"),
    BANK_ACCOUNT("BANK_ACCOUNT"),

}