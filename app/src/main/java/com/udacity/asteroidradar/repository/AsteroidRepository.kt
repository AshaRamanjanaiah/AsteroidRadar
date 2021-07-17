package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.toDomainAsteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidsDatabase) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids(
        getTodaysDate())) {
        it.toDomainAsteroid()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroidsResponse = AsteroidApi.retrofitService
                    .getAsteroids(getTodaysDateInString(), getDateAfterSevenDays(), Constants.API_KEY)
                val asteroidsList = parseAsteroidsJsonResult(JSONObject(asteroidsResponse))
                database.asteroidDao.insertAll(asteroidsList.asDatabaseAsteroid())
            } catch (e: Exception) {
                Log.d("AsteroidRepository", "Failed to get Asteroid data")
            }
        }
    }

    suspend fun deleteAsteroids() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deleteAsteroidsBeforeToday(getTodaysDate())
        }
    }

    suspend fun getImageOfTheDay(): AsteroidImage? {
        var imageData: AsteroidImage? = null
        withContext(Dispatchers.IO) {
            try {
                val asteroidImageData = AsteroidApi.retrofitService.getTodaysAsteroid(Constants.API_KEY)
                imageData = asteroidImageData
            } catch (exception: java.lang.Exception) {
                Log.d("AsteroidRepository", "Failed to get Asteroid list ${exception.message}")
            }
        }
        return imageData
    }

    suspend fun getImageOfTheDay1() = withContext(Dispatchers.IO) {
        AsteroidApi.retrofitService.getTodaysAsteroid(Constants.API_KEY)
    }


}