package com.example.passgenius.domain.viewModels


import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.*
import com.example.passgenius.R
import com.example.passgenius.common.DataHolder
import com.example.passgenius.data.dataStates.DataState
import com.example.passgenius.data.repository.MyRepository
import com.example.passgenius.domain.models.*
import com.example.passgenius.ui.authenticationPage.AuthenticationActivity
import com.example.passgenius.ui.mainPage.HomeFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MyRepository
    ): ViewModel() {
    val _allItems: MutableLiveData<DataState<MutableList<ItemListModel>>> = MutableLiveData()
    val loginItems: MutableLiveData<MutableList<LoginItemModel>> = MutableLiveData()
        init {
            loginItems.value = mutableListOf()
        }

    var loggedInUser:UserModel? = null
    var lifecycleOwner:LifecycleOwner? = null

    var isLoadingDone:Boolean = false
    val noteItems: MutableLiveData<MutableList<SecureNoteModel>> by lazy {
        MutableLiveData<MutableList<SecureNoteModel>>()
    }
    val favouriteItems: MutableLiveData<MutableList<ItemListModel>> by lazy {
        MutableLiveData<MutableList<ItemListModel>>()
    }
    val itemsList: MutableLiveData<MutableList<ItemListModel>> by lazy {
        MutableLiveData<MutableList<ItemListModel>>()
    }

    val noteItemsRv: MutableLiveData<MutableList<ItemListModel>> by lazy {
        MutableLiveData<MutableList<ItemListModel>>()
    }
    val loginItemsRv: MutableLiveData<MutableList<ItemListModel>> by lazy {
        MutableLiveData<MutableList<ItemListModel>>()
    }
    val idsItemsRv: MutableLiveData<MutableList<ItemListModel>> by lazy {
        MutableLiveData<MutableList<ItemListModel>>()
    }
    val paymentsItemsRv: MutableLiveData<MutableList<ItemListModel>> by lazy {
        MutableLiveData<MutableList<ItemListModel>>()
    }

    val allItems: MutableLiveData<MutableList<ItemListModel>> = MutableLiveData()
         init {
             allItems.value = mutableListOf()
         }
    lateinit var sharedPreferences:SharedPreferences

    var isExitingTheApp: Boolean = true

    fun initViewModel(activity: Activity, manger:FragmentManager){
        this.sharedPreferences  = activity.getSharedPreferences("PROFILE", AppCompatActivity.MODE_PRIVATE)
        getLoggedInUser()
        initItemsValues()
        authOnCreate(activity,manger)
        DataHolder.loggedInUser = loggedInUser

    }


    fun observeAllItems(){

    }

    fun getLoggedInUser(){
        viewModelScope.launch {
            loggedInUser = repository.getLoggedInUser(sharedPreferences)
        }
    }

    fun updateLoggedInUser(){
        viewModelScope.launch {
            repository.updateLoggedInUser(sharedPreferences, loggedInUser!!)
        }

    }


    private fun initItemsValues(){
        viewModelScope.launch {
            repository.getAllItems().onEach {
                _allItems.value = it

            }.launchIn(viewModelScope)
        }
        viewModelScope.launch {
            repository.getLoginItemsRemote().onEach {
                loginItems.value = it
            }.launchIn(viewModelScope)
        }
        viewModelScope.launch {
            loginItemsRv.value = repository.getLoginItemsRV()
        }

        viewModelScope.launch {
            noteItemsRv.value = repository.getNoteItemsRV()
        }

    }





    fun getItemsFromLocalDB(){

        viewModelScope.launch {

            async {
                repository.convertItems().onEach {
                    _allItems.value = it
                }.launchIn(viewModelScope)
            }


            async {
               loginItemsRv.value = repository.getLoginItemsRV()
            }

            async {
               noteItemsRv.value = repository.getNoteItemsRV()
            }
        }

        }

    fun reinitItems() {
        allItems.value = DataHolder.allItems.value
        loginItemsRv.value = allItems.value?.filter {
            it.type == "LOGIN"
        } as MutableList<ItemListModel>?
        noteItemsRv.value = allItems.value?.filter {
            it.type == "NOTE"
        } as MutableList<ItemListModel>?
    }






    private fun authOnCreate(activity: Activity, manager: FragmentManager){
        if (loggedInUser?.lastSeen != "ACTIVE"){
            isExitingTheApp = false
            activity.startActivity(Intent(activity, AuthenticationActivity::class.java))
            activity.finish()

        }else{

            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.replace(R.id.fragment_layout, HomeFragment() , "home_fragment")
            transaction.commit()

        }


    }


}


//        val db = RoomInstance.getDB(context)
//        val emptyList1:MutableList<ItemListModel> = mutableListOf()
//        val emptyList3:MutableList<ItemListModel> = mutableListOf()
//        val emptyList4:MutableList<ItemListModel> = mutableListOf()
//        val emptyList5:MutableList<ItemListModel> = mutableListOf()
//        val emptyList2:MutableList<SecureNoteModel> = mutableListOf()
//        allItems.value = emptyList1
//        noteItems.value = emptyList2
//        noteItemsRv.value = emptyList3
//        loginItemsRv.value = emptyList4
//        favouriteItems.value = emptyList5

