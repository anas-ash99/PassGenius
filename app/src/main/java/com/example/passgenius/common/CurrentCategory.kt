package com.example.passgenius.common

import com.example.passgenius.domain.models.Category

sealed class CurrentCategory(val category: String){
    object All:CurrentCategory("All")
    object Payments:CurrentCategory("Payment")
    object Logins:CurrentCategory("Logins")
    object Notes:CurrentCategory("Notes")
    object Identities:CurrentCategory("Identities")
}
