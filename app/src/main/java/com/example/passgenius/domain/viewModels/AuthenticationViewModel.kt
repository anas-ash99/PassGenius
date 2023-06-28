package com.example.passgenius.domain.viewModels

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passgenius.data.repository.MyRepository
import com.example.passgenius.domain.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthenticationViewModel
    @Inject constructor(
       private val repository: MyRepository
    ): ViewModel() {

    var loggedInUser: UserModel? = null
    private lateinit var sharedPreferences:SharedPreferences
    fun initViewModel(activity: Activity){
        this.sharedPreferences  = activity.getSharedPreferences("PROFILE", AppCompatActivity.MODE_PRIVATE)
        getLoggedInUser()


    }



    private fun getLoggedInUser(){
        viewModelScope.launch {
            loggedInUser = repository.getLoggedInUser(sharedPreferences)
        }
    }

    fun updateLoggedInUser(){
        viewModelScope.launch {
            if (loggedInUser == null){
                loggedInUser = UserModel("username11", lastSeen = "ACTIVE")
                repository.updateLoggedInUser(sharedPreferences, loggedInUser!!)

            }else{
                repository.updateLoggedInUser(sharedPreferences, loggedInUser!!)
            }
        }

    }





}