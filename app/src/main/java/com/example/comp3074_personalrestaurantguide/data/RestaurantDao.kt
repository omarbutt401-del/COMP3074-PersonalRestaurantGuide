package com.example.comp3074_personalrestaurantguide.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurants ORDER BY name ASC")
    fun getAllRestaurants(): Flow<List<Restaurant>>

    @Query("SELECT * FROM restaurants WHERE id = :id")
    fun getRestaurantById(id: Long): Flow<Restaurant?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(restaurant: Restaurant): Long

    @Update
    suspend fun update(restaurant: Restaurant)

    @Delete
    suspend fun delete(restaurant: Restaurant)

    @Query("SELECT * FROM restaurants WHERE name LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%'")
    fun searchRestaurants(query: String): Flow<List<Restaurant>>
}