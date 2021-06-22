package com.example.android.moviedb.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.moviedb.R

@Dao
interface MediaDao {

    @Query("select * from databasemovie")
    fun getMovies(): LiveData<List<DatabaseMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg movies: DatabaseMovie)

    @Query("select * from databasetvshow")
    fun getTVShows(): LiveData<List<DatabaseTVShow>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tvShows: DatabaseTVShow)

}

@Database(entities = [DatabaseMovie::class, DatabaseTVShow::class], version = 1)
abstract class MediaDatabase: RoomDatabase() {
    abstract val mediaDao: MediaDao
}

private lateinit var INSTANCE: MediaDatabase
