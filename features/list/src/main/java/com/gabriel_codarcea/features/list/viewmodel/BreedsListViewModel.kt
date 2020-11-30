package com.gabriel_codarcea.features.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabriel_codarcea.core.data.model.Breed
import com.gabriel_codarcea.core.data.model.LoadingState
import com.gabriel_codarcea.core.data.preferences.SharedPreferencesManager
import com.gabriel_codarcea.core.network.`interface`.ApiResponseInterface
import com.gabriel_codarcea.core.network.manager.BreedsManager
import org.koin.core.KoinComponent
import org.koin.core.inject

class BreedsListViewModel : ViewModel(), KoinComponent {

    private val breedsManager: BreedsManager by inject()
    private val sharedPrefs: SharedPreferencesManager by inject()

    val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    fun state(): LiveData<LoadingState> = loadingState

    val breedsList: MutableLiveData<List<Breed>> = MutableLiveData()

    private val breedsManagerCallback = object : ApiResponseInterface<List<Breed>> {
        override fun onResponse(breeds: List<Breed>) {
            breedsList.value = breeds
            loadingState.value = LoadingState.FINISHED
        }

        override fun onFailure(t: Throwable) {
            loadingState.value = LoadingState.ERROR
        }
    }

    fun setBreedsManagerCallback() {
        breedsManager.callback = breedsManagerCallback
        loadingState.value = breedsManager.downloadStatus.value
    }

    fun checkBreedsDownloaded() {
        if (sharedPrefs.didDownloadData()) {
            breedsManager.getBreedsFromDB()
        }
    }

    fun refreshBreedsList() {
        loadingState.value = LoadingState.IN_PROGRESS
        breedsManager.refreshBreeds()
    }
}
