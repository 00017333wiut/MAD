package com.example.myapp.data

import kotlinx.serialization.Serializable

@Serializable
open class MyResponse () {
    val code: String = ""
    val status: String = ""
    val message: String = ""
}