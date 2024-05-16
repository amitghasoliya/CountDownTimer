package com.amitghasoliya.timerapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amitghasoliya.timerapp.databinding.ActivityUserBinding
import com.amitghasoliya.timerapp.utils.SharedPref
import com.amitghasoliya.timerapp.viewmodels.AuthViewModel
import com.amitghasoliya.timerapp.viewmodels.UserViewModel

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private val authViewModel: AuthViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.Primary)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        SharedPref.initialize(this)
        binding.logout.setOnClickListener {
            authViewModel.logout()
            SharedPref.logoutUser(this)
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
        userViewModel.countDownText.observe(this, { countDownText ->
            binding.count.text = countDownText
        })
    }
}