package com.gabriel_codarcea.core.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gabriel_codarcea.core.data.model.Breed

@Dao
interface BreedsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBreeds(breeds: List<Breed>)

    @Query("SELECT * FROM breeds ORDER BY name")
    fun getAllBreeds(): List<Breed>

    @Query("SELECT * FROM breeds WHERE dbID=:dbID")
    fun getBreedById(dbID: Int): LiveData<Breed>

    @Query("DELETE FROM breeds")
    fun emptyBreedsDB()
}
