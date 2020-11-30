package com.gabriel_codarcea.kittycats.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabriel_codarcea.core.data.preferences.SharedPreferencesManager
import com.gabriel_codarcea.core.network.manager.BreedsManager
import com.gabriel_codarcea.kittycats.model.ActivityType
import org.koin.core.KoinComponent
import org.koin.core.inject

class LaunchViewModel : ViewModel(), KoinComponent {

    private val sharedPrefs: SharedPreferencesManager by inject()
    private val breedsManager: BreedsManager by inject()

    val activityType: MutableLiveData<ActivityType> = MutableLiveData()

    fun startBreedsDownload() {
        if (!sharedPrefs.didDownloadData()) breedsManager.getBreedsFromAPI()
    }

    fun startApp() {
        if (sharedPrefs.isLoggedIn()) {
            activityType.postValue(ActivityType.BREEDS_LIST)
        } else {
            activityType.postValue(ActivityType.LOGIN)
        }
    }

    fun cancelBreedsDownload() {
        breedsManager.cancelDownload()
    }
}
