package com.example.hardwaresensors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainViewModel: ViewModel(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private var _time: MutableLiveData<Int> = MutableLiveData()
    var time: LiveData<Int> = _time

    var pass = false

    override fun onCleared() {
        job.cancel()
    }

    fun restart() {
        launch(Dispatchers.IO) {
            for(i in 5 downTo 0) {
               delay(1000L)
                _time.postValue(i)
            }
        }
    }
}
