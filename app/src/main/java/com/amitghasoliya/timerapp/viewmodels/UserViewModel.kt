package com.amitghasoliya.timerapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserViewModel : ViewModel() {

    private val _countDownText = MutableLiveData<String>()
    val countDownText: LiveData<String>
        get() = _countDownText

    init {
        fetchCounterData()
    }

    private fun fetchCounterData() {
        val database = FirebaseDatabase.getInstance().getReference("counter")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val timeLeftInMillis = snapshot.getValue(Long::class.java) ?: 0L
                updateCountDownText(timeLeftInMillis)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun updateCountDownText(timeLeftInMillis: Long) {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60

        val timeFormatted = String.format("%02d:%02d", minutes, seconds)
        _countDownText.value = timeFormatted
    }
}