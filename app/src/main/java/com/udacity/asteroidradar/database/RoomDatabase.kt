package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate > :today ORDER BY closeApproachDate ASC")
    fun getAsteroids(today: Date): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate BETWEEN :startDate AND :endDate")
    fun getAsteroids1(startDate: Date, endDate: Date): LiveData<List<DatabaseAsteroid>>

    @Query("Select * from asteroid_image")
    fun getAsteroidImage(): LiveData<DatabaseAsteroidImage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: List<DatabaseAsteroid>)

    @Query("Delete from asteroid_table WHERE closeApproachDate < :today")
    fun deleteAsteroidsBeforeToday(today: Date)

}

@Database(entities = [DatabaseAsteroid::class, DatabaseAsteroidImage::class], version = 1)
@TypeConverters(Converters::class)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao

    companion object {
        private lateinit var INSTANCE: AsteroidsDatabase

        fun getDatabase(context: Context): AsteroidsDatabase {
            synchronized(AsteroidsDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AsteroidsDatabase::class.java,
                        "asteroids").build()
                }
            }
            return INSTANCE
        }
    }
}