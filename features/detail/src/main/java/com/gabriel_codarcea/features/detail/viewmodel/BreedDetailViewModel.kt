package com.gabriel_codarcea.features.detail.viewmodel

import androidx.lifecycle.*
import com.gabriel_codarcea.core.data.model.Breed
import com.gabriel_codarcea.core.data.model.LoadingState
import com.gabriel_codarcea.core.network.manager.BreedsManager
import org.koin.core.KoinComponent
import org.koin.core.inject

class BreedDetailViewModel : ViewModel(), KoinComponent {

    private val breedsManager: BreedsManager by inject()

    private val loadingState: MutableLiveData<LoadingState> =
        MutableLiveData(LoadingState.IN_PROGRESS)

    fun state(): LiveData<LoadingState> = loadingState

    private val breedIDLiveData: MutableLiveData<Int> = MutableLiveData()

    val breed: LiveData<Breed> = Transformations.switchMap(breedIDLiveData) { id ->
        breedsManager.getSingleBreedFromDBWithID(id)
    }

    fun getBreedDetails(breedID: Int) {
        breedIDLiveData.value = breedID
        loadingState.value = LoadingState.FINISHED
    }
}
