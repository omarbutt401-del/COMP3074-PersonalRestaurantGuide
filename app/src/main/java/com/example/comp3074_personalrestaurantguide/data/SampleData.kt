package com.example.comp3074_personalrestaurantguide.data

import com.example.comp3074_personalrestaurantguide.model.Restaurant

object SampleData {
    val restaurants: MutableList<Restaurant> = mutableListOf(
        Restaurant(1, "Bella Italia", "123 King St W, Toronto, ON",
            "416-555-0101", "Cozy Italian trattoria.",
            listOf("Italian", "date-night"), 4, 43.6481, -79.3867),
        Restaurant(2, "Green Leaf Thai", "88 Queen St E, Toronto, ON",
            "416-555-0202", "Great vegan pad thai.",
            listOf("Thai", "vegetarian"), 5, 43.6532, -79.3730),
        Restaurant(3, "Taco Corner", "45 Bloor St W, Toronto, ON",
            "416-555-0303", "Cheap and cheerful tacos.",
            listOf("Mexican", "quick-bite"), 3, 43.6698, -79.3927)
    )
    private var nextId = 4L

    fun addRestaurant(r: Restaurant): Restaurant {
        val withId = r.copy(id = nextId++)
        restaurants.add(withId)
        return withId
    }
    fun updateRestaurant(r: Restaurant) {
        val i = restaurants.indexOfFirst { it.id == r.id }
        if (i >= 0) restaurants[i] = r
    }
    fun deleteRestaurant(id: Long) {
        restaurants.removeAll { it.id == id }
    }
}