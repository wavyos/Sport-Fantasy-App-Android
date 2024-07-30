package com.example.sportsfantasy.Model

data class Register(
    val email: String,
    val name: String,
    val password: String,
    val device_token: String,
    val device_type: String = "1"
)