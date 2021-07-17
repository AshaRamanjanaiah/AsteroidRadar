package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidsDatabase.Companion.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

enum class AsteroidApiStatus { LOADING, ERROR, DONE }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = javaClass.simpleName

    private val _asteroidImage = MutableLiveData<AsteroidImage>()
    val asteroidImage: LiveData<AsteroidImage>
    get() = _asteroidImage

    private val _status = MutableLiveData<AsteroidApiStatus>()
    val status: LiveData<AsteroidApiStatus>
        get() = _status

    private val database =  getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    init {
        viewModelScope.launch {
            getTodaysAsteroid()
            asteroidRepository.refreshAsteroids()
        }
    }

    val asteroids = asteroidRepository.asteroids

    suspend fun getTodaysAsteroid() {
        _status.value = AsteroidApiStatus.LOADING
       withContext(Dispatchers.Main) {
           try {
              _asteroidImage.value =  asteroidRepository.getImageOfTheDay1()
               _status.value = AsteroidApiStatus.DONE
           } catch (e: Exception) {
               _status.value = AsteroidApiStatus.ERROR
               Log.d(TAG, "Failed to get Todays Asteroid ${e.message}")
           }
       }
    }
}