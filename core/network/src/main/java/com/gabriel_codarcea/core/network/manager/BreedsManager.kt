package com.gabriel_codarcea.core.network.manager

import androidx.lifecycle.MutableLiveData
import com.gabriel_codarcea.core.data.model.Breed
import com.gabriel_codarcea.core.data.model.LoadingState
import com.gabriel_codarcea.core.data.preferences.SharedPreferencesManager
import com.gabriel_codarcea.core.data.repository.BreedsDatabaseRepository
import com.gabriel_codarcea.core.network.BuildConfig.API_KEY
import com.gabriel_codarcea.core.network.`interface`.ApiResponseInterface
import com.gabriel_codarcea.core.network.client.ApiClient
import com.gabriel_codarcea.core.network.service.ApiService
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BreedsManager : KoinComponent {

    private val apiClient: ApiClient by inject()
    private val breedsDatabaseRepository: BreedsDatabaseRepository by inject()
    private val sharedPrefs: SharedPreferencesManager by inject()
    private val apiService: ApiService? = apiClient.getClient()

    val downloadStatus: MutableLiveData<LoadingState> = MutableLiveData(LoadingState.EMPTY)
    val downloadLiveStatus: MutableLiveData<String> = MutableLiveData("")

    private var downloadJob: Job? = null

    var callback: ApiResponseInterface<List<Breed>>? = null

    fun getBreedsFromAPI() {
        downloadStatus.value = LoadingState.IN_PROGRESS
        apiService?.getBreeds(API_KEY)?.enqueue(object : Callback<List<Breed>> {

            override fun onResponse(
                call: Call<List<Breed>>,
                response: Response<List<Breed>>
            ) {
                val breedsList = response.body()
                breedsList?.let {
                    updateBreedsWithImages(it)
                } ?: callback?.onFailure(Throwable("NULL_BREEDS_LIST_RETURNED"))
            }

            override fun onFailure(call: Call<List<Breed>>, t: Throwable) {
                callback?.onFailure(t)
            }
        })
    }

    private fun updateBreedsWithImages(breeds: List<Breed>) {
        downloadJob = CoroutineScope(IO).launch {
            val breedsWithImages: List<Breed>? = getImagesForBreedsFromAPI(breeds, downloadJob)
            if (breedsWithImages != null) {
                saveBreedsIntoLocalDB(breedsWithImages)
                val breedsFromDB = breedsDatabaseRepository.getBreeds()
                withContext(Main) {
                    sharedPrefs.setDataDownloaded(true)
                    callback?.let { cb ->
                        cb.onResponse(breedsFromDB)
                        downloadStatus.value = LoadingState.FINISHED
                    }
                }
            } else {
                withContext(Main) {
                    sharedPrefs.setDataDownloaded(false)
                    callback?.let { cb ->
                        cb.onFailure(Throwable("FAILED_TO_GET_IMAGES_FOR_BREEDS"))
                        downloadStatus.value = LoadingState.ERROR
                    }
                }
            }
        }
    }

    private fun getImagesForBreedsFromAPI(breeds: List<Breed>, job: Job?): List<Breed>? {
        val totalBreeds = breeds.size
        for (i in breeds.indices) {
            if (job != null && job.isActive) {
                val breedURLResponse =
                    apiService?.getImageForBreed(API_KEY, breeds[i].id)?.execute()
                breedURLResponse?.body()?.let { breedURLList ->
                    if (!breedURLList.isNullOrEmpty() && !breedURLList[0].url.isNullOrEmpty()) {
                        breeds[i].image = breedURLList[0].url
                        downloadLiveStatus.postValue("Updating breeds $i/$totalBreeds...")
                    } else return null
                } ?: return null
            } else {
                return null
            }
        }
        return breeds
    }

    private fun saveBreedsIntoLocalDB(breeds: List<Breed>) {
        breedsDatabaseRepository.insertBreeds(breeds)
    }


    fun refreshBreeds() {
        sharedPrefs.setDataDownloaded(false)
        CoroutineScope(IO).launch {
            breedsDatabaseRepository.emptyBreedsDB()
            withContext(Main){
                getBreedsFromAPI()
            }
        }
    }

    fun getBreedsFromDB() {
        CoroutineScope(IO).launch {
            val breedsFromDB = breedsDatabaseRepository.getBreeds()
            withContext(Main) {
                callback?.onResponse(breedsFromDB)
                downloadStatus.value = LoadingState.FINISHED
            }
        }
    }

    fun getSingleBreedFromDBWithID(breedID: Int) = breedsDatabaseRepository.getBreedById(breedID)

    fun cancelDownload() {
        downloadJob?.cancel("", Throwable("DOWNLOAD_CANCELLED"))
    }
}