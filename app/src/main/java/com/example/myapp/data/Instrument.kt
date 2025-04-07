package com.example.myapp.data

enum class InstrumentType {
    GUITAR,
    BASS,
    UKULELE,
}

enum class Material{
    WOOD,
    CARBON_FIBER,
    METAL
}

enum class Warranty{
    ONE_YEAR,
    TWO_YEARS,
    NONE
}

data class Instrument(
    val model: String,
    val type: InstrumentType,
    val price: Double,
    val brand: String,
    val availability: Boolean,
    val color: String?,
    val material: Material?,
    val stringCount: Int?,
    val warranty: Warranty?
){
//init {
//    require(model.length in 5..10) { "Model must be between 5 and 10 characters long." }
//    require(brand.length in 3..20) { "Brand must be between 3 and 20 characters long." }
//    require(price > 0) { "Price must be greater than 0." }
//    stringCount?.let {
//        require(it in 2..12) { "String count must be between 2 and 12." }
//    }
//}
}
