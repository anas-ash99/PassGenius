package com.example.passgenius.domain.models

data class Category(
    val title: String,
    val items: List<PassItemModel>
)