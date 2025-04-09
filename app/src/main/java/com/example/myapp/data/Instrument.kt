package com.example.myapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//enum class InstrumentType {
//    GUITAR,
//    BASS,
//    UKULELE
//}
//
//enum class Material{
//    WOOD,
//    CARBON_FIBER,
//    METAL
//}
//
//enum class Warranty{
//    ONE_YEAR,
//    TWO_YEARS,
//    NONE
//}
@Serializable
data class Instrument(
    val id: Int? = null,

    @SerialName("title")
    val model: String = "",

    val type: String = "",

    val price: Double = 0.0,

    @SerialName("description")
    val brand: String = "",

    @SerialName("is_it_true")
    val availability: String = "0",

    val color: String = "",

    @SerialName("integer_one")
    val material: Int = 0,

    @SerialName("integer_two")
    val stringCount: Int = 0,

    @SerialName("integer_three")
    val warranty: Int = 0
)

