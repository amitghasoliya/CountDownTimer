package com.amitghasoliya.timerapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amitghasoliya.timerapp.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val database: DatabaseReference by lazy { FirebaseDatabase.getInstance().reference }

    private val _authResult = MutableLiveData<Result<String>>()
    val authResult: LiveData<Result<String>> = _authResult

    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authResult.value = Result.success("Login successful")
                } else {
                    _authResult.value = Result.failure(task.exception ?: Exception("Login failed"))
                }
            }
    }

    fun signup(userType: String, username:String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authResult.value = Result.success("Signup successful")
                    val user= Users(username,email,password)
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    database.child(userType).child(userId).setValue(user)
                } else {
                    _authResult.value = Result.failure(task.exception ?: Exception("Signup failed"))
                }
            }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

}