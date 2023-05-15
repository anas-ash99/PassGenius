package com.example.passgenius.domain.models.logosApi

data class LogoApiModel(
    val domain: Domain,
    val domain_registration_data: DomainRegistrationData,
    val objects: Objects,
    val similar_domains: List<String>,
    val success: Boolean
)