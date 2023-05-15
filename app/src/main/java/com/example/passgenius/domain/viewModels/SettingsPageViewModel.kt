package com.example.passgenius.domain.viewModels

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passgenius.R
import com.example.passgenius.common.enums.SettingsPageType
import com.example.passgenius.data.repository.MyRepository
import com.example.passgenius.domain.models.UserModel
import com.example.passgenius.ui.profilePage.SecurityFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsPageViewModel
     @Inject constructor(
        private val repository: MyRepository
     )
    :ViewModel() {

    val pageType: MutableLiveData<SettingsPageType?> by lazy {
        MutableLiveData<SettingsPageType?>()
    }
    var loggedInUser:UserModel? = null
    lateinit var sharedPreferences:SharedPreferences



    fun initPage(intent: Intent, transaction: FragmentTransaction, sharedPreferences: SharedPreferences){
        this.sharedPreferences = sharedPreferences
        getLoggedInUser()
        setPageType(intent)
        startFragment(transaction)

    }

    private fun getLoggedInUser(){
        viewModelScope.launch {
            loggedInUser = repository.getLoggedInUser(sharedPreferences)!!
        }
    }

    fun updateLoggedInUser(){
        viewModelScope.launch {
            repository.updateLoggedInUser(sharedPreferences, loggedInUser!!)
        }

    }

    fun setPageType(intent:Intent){
        pageType.value =  intent.getSerializableExtra("PAGE_TYPE")!! as SettingsPageType

    }



    private fun startFragment(transaction: FragmentTransaction) {

        when(pageType.value){
            SettingsPageType.SECURITY->{
                transaction.add(R.id.fragment_layout, SecurityFragment() , "")
                transaction.commit()

            }

            else -> {}
        }
    }

    fun handleArrowBack(activity:Activity, arrowIcon:CardView){
        arrowIcon.setOnClickListener {
            activity.onBackPressed()
        }



    }










}