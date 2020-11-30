package com.gabriel_codarcea.core.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gabriel_codarcea.core.data.dao.BreedsDao
import com.gabriel_codarcea.core.data.model.Breed

@Database(entities = [Breed::class], version = 1, exportSchema = false)
abstract class BreedsDatabase : RoomDatabase() {

    abstract fun breedsDao(): BreedsDao

    companion object {
        private const val DB_NAME = "my_breeds.db"

        private var instance: BreedsDatabase? = null

        fun getDatabase(context: Context): BreedsDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    BreedsDatabase::class.java,
                    DB_NAME
                ).build()
            }
            return instance!!
        }
    }
}
