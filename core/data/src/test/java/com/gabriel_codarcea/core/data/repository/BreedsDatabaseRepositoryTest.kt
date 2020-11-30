package com.gabriel_codarcea.core.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.gabriel_codarcea.core.data.dao.BreedsDao
import com.gabriel_codarcea.core.data.database.BreedsDatabase
import com.gabriel_codarcea.core.data.helpers.getBreedTest1
import com.gabriel_codarcea.core.data.helpers.getBreedTest2
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.AutoCloseKoinTest

class BreedsDatabaseRepositoryTest: AutoCloseKoinTest() {

    @get:Rule
    var instantTaskRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var breedsDatabase: BreedsDatabase

    @MockK
    private lateinit var breedsDao: BreedsDao

    lateinit var breedsDatabaseRepository: BreedsDatabaseRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { breedsDatabase.breedsDao() } returns breedsDao

        breedsDatabaseRepository = BreedsDatabaseRepository(breedsDatabase)
    }

    @Test
    fun `test insertBreeds propagates a list of breeds to breedsDao`() {
        val list = listOf(getBreedTest1(), getBreedTest2())

        every { breedsDao.insertBreeds(any()) } just runs

        breedsDatabaseRepository.insertBreeds(list)

        verify { breedsDao.insertBreeds(list) }
    }

    @Test
    fun `test getBreeds returns the list of all available breeds from breedsDao`() {
        val expectedList = listOf(getBreedTest1(), getBreedTest2())
        every { breedsDao.getAllBreeds() } returns expectedList

        val result = breedsDatabaseRepository.getBreeds()

        assertEquals(expectedList, result)
    }

    @Test
    fun `test getBreedByID returns the correct breed from breedsDao`() {
        val expectedBreed = getBreedTest1()
        every { breedsDao.getBreedById(any()) } returns MutableLiveData(expectedBreed)

        val result = breedsDatabaseRepository.getBreedById(1)

        verify { breedsDao.getBreedById(1) }
        assertNotNull(result.value)
        assertEquals(result.value, expectedBreed)
    }

    @Test
    fun `test emptyBreedsDB removes the breeds from breedsDao`() {
        every { breedsDao.emptyBreedsDB() } just runs

        breedsDatabaseRepository.emptyBreedsDB()

        verify { breedsDao.emptyBreedsDB() }
    }
}
