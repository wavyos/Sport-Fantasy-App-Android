package com.example.sportsfantasy.Model

data class RegisterResponse(
    val status_code: Int,
    val access_token: String,
    val message: String,
    val userDetails: UserDetails,
)