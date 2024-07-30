package com.example.sportsfantasy.Model

data class Login(
    val email: String,
    val password: String,
    val device_token: String,
    val device_type: String = "1"
)