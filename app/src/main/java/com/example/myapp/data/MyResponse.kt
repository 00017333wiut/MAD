package com.example.myapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyResponse<T>(
    @SerialName("code")
    val code: Int = 0,
    @SerialName("status")
    val status: String = "",
    @SerialName("message")
    val message: String = "",
    @SerialName("data")
    val data: T
)
