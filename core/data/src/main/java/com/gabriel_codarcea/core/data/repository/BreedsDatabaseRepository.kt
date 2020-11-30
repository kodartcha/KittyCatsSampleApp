package com.gabriel_codarcea.core.data.repository

import androidx.lifecycle.LiveData
import com.gabriel_codarcea.core.data.dao.BreedsDao
import com.gabriel_codarcea.core.data.database.BreedsDatabase
import com.gabriel_codarcea.core.data.model.Breed

class BreedsDatabaseRepository(breedsDatabase: BreedsDatabase) {

    private val breedsDao: BreedsDao = breedsDatabase.breedsDao()

    fun insertBreeds(breeds: List<Breed>) {
        breedsDao.insertBreeds(breeds)
    }

    fun getBreeds(): List<Breed> = breedsDao.getAllBreeds()

    fun getBreedById(dbID: Int): LiveData<Breed> = breedsDao.getBreedById(dbID)

    fun emptyBreedsDB() = breedsDao.emptyBreedsDB()
}
