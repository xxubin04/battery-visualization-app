package com.xxubin04.batteryvisualizationapp.ui.graph

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GraphViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "전류-전압 그래프 시각화"
    }
    val text: LiveData<String> = _text
}