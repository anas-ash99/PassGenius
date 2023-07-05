package com.example.passgenius.domain.models

import java.io.Serializable

data class FavouriteItems (
    val list:MutableList<String>
    ): Serializable