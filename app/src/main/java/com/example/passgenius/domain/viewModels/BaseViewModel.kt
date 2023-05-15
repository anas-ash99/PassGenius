package com.example.passgenius.domain.viewModels

import androidx.lifecycle.ViewModel
import androidx.room.Ignore
import com.example.passgenius.data.repository.MyRepository
import com.example.passgenius.domain.models.UserModel
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
open class BaseViewModel
    @Inject constructor(
        @Ignore
        val repository: MyRepository
    )
    :ViewModel() {

    fun getLoggedInUser():UserModel{

        return UserModel()

    }




}