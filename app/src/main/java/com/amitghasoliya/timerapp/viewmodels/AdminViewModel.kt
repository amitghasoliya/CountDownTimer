package com.amitghasoliya.timerapp.viewmodels

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amitghasoliya.timerapp.MainActivity
import com.google.firebase.database.FirebaseDatabase

class AdminViewModel : ViewModel() {

    private var timer: CountDownTimer? = null
    private var timeLeftInMillis: Long = MainActivity.START_TIME_IN_MILLIS
    var timerRunning: Boolean = false

    private val _countDownText = MutableLiveData<String>()
    val countDownText: LiveData<String>
        get() = _countDownText

    init {
        updateCountDownText()
    }

    fun startOrPauseTimer() {
        if (timerRunning) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    fun resetTimer() {
        timer?.cancel()
        timeLeftInMillis = MainActivity.START_TIME_IN_MILLIS
        updateCountDownText()
        saveCounterData()
        timerRunning = false
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownText()
                saveCounterData()
            }

            override fun onFinish() {
                timerRunning = false
                _countDownText.value = "00:00"
            }
        }.start()

        timerRunning = true
    }

    private fun pauseTimer() {
        timer?.cancel()
        saveCounterData()
        timerRunning = false
    }

    private fun saveCounterData() {
        val database = FirebaseDatabase.getInstance().getReference()
        database.child("counter").setValue(timeLeftInMillis)
    }

    private fun updateCountDownText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeFormatted = String.format("%02d:%02d", minutes, seconds)
        _countDownText.value = timeFormatted
    }
}