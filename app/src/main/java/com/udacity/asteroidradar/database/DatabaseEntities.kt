package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.AsteroidImage
import com.udacity.asteroidradar.api.convertDateToString
import java.util.*

@Entity(tableName = "asteroid_table")
data class DatabaseAsteroid(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: Date,
    val absoluteMagnitude: Double, val estimatedDiameter: Double,
    val relativeVelocity: Double, val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean)

@Entity(tableName = "asteroid_image")
data class DatabaseAsteroidImage(
    val title: String,
    val media_type: String,
    @PrimaryKey
    val url: String
)

fun DatabaseAsteroidImage.toDomainAsteroidImage(): AsteroidImage {
    return AsteroidImage(
        title = title,
        media_type = media_type,
        url = url
    )
}

fun List<DatabaseAsteroid>.toDomainAsteroid(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = convertDateToString(it.closeApproachDate),
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}