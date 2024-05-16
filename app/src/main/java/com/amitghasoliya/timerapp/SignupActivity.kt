package com.amitghasoliya.timerapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.amitghasoliya.timerapp.databinding.ActivitySignupBinding
import com.amitghasoliya.timerapp.utils.SharedPref
import com.amitghasoliya.timerapp.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.Primary)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SharedPref.initialize(this)
        val userType = SharedPref.getUserType()!!

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.signup.setOnClickListener {
            val username = binding.name.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            authViewModel.signup(userType,username,email, password)
        }

        binding.profile.text = userType

        binding.goToLogin.setOnClickListener {
            finish()
        }

        authViewModel.authResult.observe(this, Observer { result ->
            result.onSuccess {
                if(userType == "User"){
                    startActivity(Intent(this, UserActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }.onFailure {
                Toast.makeText(this, "Signup Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }


}