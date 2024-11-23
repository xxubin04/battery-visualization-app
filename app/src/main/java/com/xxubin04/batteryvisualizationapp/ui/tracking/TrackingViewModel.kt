package com.xxubin04.batteryvisualizationapp.ui.tracking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TrackingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "현재 충전 방식(정전류, 정전압)"
    }
    val text: LiveData<String> = _text
}