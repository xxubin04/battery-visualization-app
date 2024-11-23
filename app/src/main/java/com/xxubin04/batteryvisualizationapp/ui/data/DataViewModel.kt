package com.xxubin04.batteryvisualizationapp.ui.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "온도, 전류, 전압, 충전량(%)"
    }
    val text: LiveData<String> = _text
}