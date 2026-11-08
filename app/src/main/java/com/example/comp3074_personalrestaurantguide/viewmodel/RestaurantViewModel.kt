package com.example.comp3074_personalrestaurantguide.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.comp3074_personalrestaurantguide.data.AppDatabase
import com.example.comp3074_personalrestaurantguide.data.Restaurant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RestaurantViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).restaurantDao()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    @OptIn(ExperimentalCoroutinesApi::class)
    val restaurants: StateFlow<List<Restaurant>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isEmpty()) dao.getAllRestaurants()
            else dao.searchRestaurants(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addRestaurant(restaurant: Restaurant) = viewModelScope.launch {
        dao.insert(restaurant)
    }

    fun updateRestaurant(restaurant: Restaurant) = viewModelScope.launch {
        dao.update(restaurant)
    }

    fun deleteRestaurant(restaurant: Restaurant) = viewModelScope.launch {
        dao.delete(restaurant)
    }
}