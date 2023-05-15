package com.example.passgenius.domain.models.logosApi

data class DomainRegistrationData(
    val domain_age_date: String,
    val domain_age_days_ago: String,
    val domain_expiration_date: String,
    val domain_expiration_days_left: String
)