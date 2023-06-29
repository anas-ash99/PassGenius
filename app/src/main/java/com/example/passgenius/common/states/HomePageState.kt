package com.example.passgenius.common.states


data class HomePageState(
    val currentCategory: String? = null,
    val isListEmpty: Boolean  = false,
    val isMainLayoutShown:Boolean  = false,
    val isLoading:Boolean = true,
    val isError:Boolean = false
)
