package com.example.myapp.data

import kotlinx.serialization.SerialName

class InstrumentRequest {

    @SerialName("title")
    val model: String = ""

    val type: String = ""

    val price: Double = 0.0

    @SerialName("description")
    val brand: String = ""

    @SerialName("is_it_true")
    val availability: Boolean = true

    val color: String = ""

    @SerialName("integer_one")
    val material: Int = 0

    @SerialName("integer_two")
    val stringCount: Int = 0

    @SerialName("integer_three")
    val warranty: Int = 0

}