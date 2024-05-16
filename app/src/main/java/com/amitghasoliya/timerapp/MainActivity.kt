package com.amitghasoliya.timerapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amitghasoliya.timerapp.databinding.ActivityMainBinding
import com.amitghasoliya.timerapp.utils.SharedPref
import com.amitghasoliya.timerapp.viewmodels.AdminViewModel
import com.amitghasoliya.timerapp.viewmodels.AuthViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthViewModel by viewModels()
    private val viewModel: AdminViewModel by viewModels()

    companion object {
        const val START_TIME_IN_MILLIS: Long = 300000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.Primary)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SharedPref.initialize(this)

        binding.logoutAdmin.setOnClickListener {
            authViewModel.logout()
            SharedPref.logoutUser(this)
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }

        binding.btnStartPause.setOnClickListener {
            if (viewModel.timerRunning) {
                binding.btnStartPause.text = "Resume"
                viewModel.startOrPauseTimer()
            } else {
                binding.btnStartPause.text = "Pause"
                viewModel.startOrPauseTimer()
                viewModel.timerRunning = true
            }
        }

        binding.btnReset.setOnClickListener {
            binding.btnStartPause.text = "Start"
            viewModel.resetTimer()
        }

        viewModel.countDownText.observe(this, { text ->
            binding.timer.text = text
            if (text== "00:00"){
                binding.btnStartPause.text = "Start"
                viewModel.resetTimer()
            }
        })
    }
}