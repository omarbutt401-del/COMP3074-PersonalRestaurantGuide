package com.example.comp3074_personalrestaurantguide.model

data class Restaurant(
    val id: Long,
    var name: String,
    var address: String,
    var phone: String,
    var description: String,
    var tags: List<String>,
    var rating: Int,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)