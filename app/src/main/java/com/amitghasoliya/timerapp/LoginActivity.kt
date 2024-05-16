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
import com.amitghasoliya.timerapp.databinding.ActivityLoginBinding
import com.amitghasoliya.timerapp.utils.SharedPref
import com.amitghasoliya.timerapp.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.Primary)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SharedPref.initialize(this)
        SharedPref.setUserType("User")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        binding.login.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            authViewModel.login(email, password)
        }

        binding.continueWithAdmin.setOnClickListener {
            if (SharedPref.getUserType() == "User"){
                binding.continueWithAdmin.text = "Switch to User >>"
                SharedPref.setUserType("Admin")
            }else{
                binding.continueWithAdmin.text = "Switch to Admin >>"
                SharedPref.setUserType("User")
            }
        }

        binding.goToSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            intent.putExtra("userType", SharedPref.getUserType())
            startActivity(intent)
        }

        authViewModel.authResult.observe(this, Observer { result ->
            result.onSuccess {
                if(SharedPref.getUserType() == "User"){
                    startActivity(Intent(this, UserActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }.onFailure {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            if (SharedPref.getUserType() == "User"){
                startActivity(Intent(this,UserActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        }
    }
}