package com.example.comp3074_personalrestaurantguide.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class Restaurant(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val address: String,
    val phone: String,
    val description: String,
    val tags: String,
    val rating: Int,
    val latitude: Double = 43.6532,  // to Toronto
    val longitude: Double = -79.3832 // Toronto
)