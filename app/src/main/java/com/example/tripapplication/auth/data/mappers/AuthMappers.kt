package com.example.tripapplication.auth.data.mappers

import com.example.tripapplication.auth.data.networking.dto.LoginRequestDto
import com.example.tripapplication.auth.data.networking.dto.RegisterRequestDto
import com.example.tripapplication.auth.domain.RegisterRequest
import com.example.tripapplication.auth.domain.utils.LoginRequest

fun RegisterRequest.toRegisterRequestDto() : RegisterRequestDto {
    return RegisterRequestDto(
        firstname = firstname,
        lastname = lastname,
        email = email,
        password = password
    )
}

fun LoginRequest.toLoginRequestDto() : LoginRequestDto {
    return LoginRequestDto(
        email = email,
        password = password
    )

}