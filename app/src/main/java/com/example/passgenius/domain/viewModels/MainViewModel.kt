package com.example.passgenius.domain.viewModels



import androidx.lifecycle.*
import com.example.passgenius.common.states.HomePageState

import com.example.passgenius.common.Constants
import com.example.passgenius.common.CurrentCategory
import com.example.passgenius.common.DataHolder
import com.example.passgenius.common.UserActions
import com.example.passgenius.common.enums.MainScreenTypes
import com.example.passgenius.common.states.MainActivityState
import com.example.passgenius.data.dataStates.DataState
import com.example.passgenius.data.repository.MyRepository
import com.example.passgenius.domain.models.*
import com.example.passgenius.ui.adapters.AdapterCategories

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MyRepository
    ): ViewModel() {


    val homeState = MutableLiveData(HomePageState())
    val categoriesList = Constants.categoriesList
    val mainActivityState = MutableLiveData(MainActivityState())
    val currentCategory = MutableStateFlow<String?>(null)
    val loginItems: MutableLiveData<MutableList<LoginItemModel>> = MutableLiveData()
        init {
            loginItems.value = mutableListOf()
        }

    var loggedInUser:UserModel? = null
    var lifecycleOwner:LifecycleOwner? = null


    val favouriteItems: MutableLiveData<MutableList<ItemListModel>> by lazy {
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

    val allItems: MutableLiveData<MutableList<ItemListModel>> = MutableLiveData(mutableListOf())

    var isExitingTheApp: Boolean = true

    fun initViewModel(){
        getLoggedInUser()
        if (!mainActivityState.value?.isActivityCreated!!){
            initItemsValues()
            authOnCreate()

        }
        DataHolder.loggedInUser = loggedInUser

    }


    fun onUserAction(action: UserActions){
        when(action){
            is UserActions.ChoseItemsCategory -> {changeSelectedCategory(action.adapter,action.position)}
            is UserActions.NavigateScreens -> {}
            UserActions.PlusOrXClick -> {}
        }
    }


    private fun changeSelectedCategory(adapter:AdapterCategories, position:Int){
        categoriesList[position].isSelected = true
        adapter.notifyChang(position)
        categoriesList.onEachIndexed{ index,item ->
            if (item.isSelected && index != position){
                item.isSelected = false
                adapter.notifyChang(index)
            }
        }
        currentCategory.value = categoriesList[position].name
    }
    fun onPaymentsCategoryClick(){
        mainActivityState.value = mainActivityState.value?.copy(isPaymentLayoutShown = true)
    }
    fun onAddClick(){
        mainActivityState.value = mainActivityState.value?.copy(plusButton = mainActivityState.value?.plusButton == false, isPaymentLayoutShown = false)
    }
    fun getLoggedInUser(){
        viewModelScope.launch {
            loggedInUser = repository.getLoggedInUser()
        }
    }
    fun updateLoggedInUser(){
        viewModelScope.launch {
            repository.updateLoggedInUser(loggedInUser!!)
        }

    }

    private fun initItemsValues(){
        viewModelScope.launch {
            repository.getAllItems().onEach {
                when(it){
                    is DataState.Error -> getItemsFromLocalDB()
                    DataState.Loading -> homeState.value = homeState.value?.copy(isLoading = true)
                    is DataState.Success -> {
                        allItems.value = it.data!!
                        currentCategory.value = CurrentCategory.All.category
                        homeState.value = homeState.value?.copy(isLoading = false)
                        mainActivityState.value?.isActivityCreated = true
                    }
                }
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






    private fun authOnCreate(){
        if (loggedInUser?.lastSeen != "ACTIVE"){
            isExitingTheApp = false
            mainActivityState.value?.isAuthenticated = true
            mainActivityState.value?.currentScreen?.value = MainScreenTypes.HOME

        }else{
            mainActivityState.value?.isAuthenticated = false
        }

    }

    private fun getItemsFromLocalDB() {
        viewModelScope.launch {
            repository.getItemsFromLocalDB().onEach {
                when(it){
                    is DataState.Error -> homeState.value = homeState.value?.copy(isLoading = false, isError = true)
                    DataState.Loading -> {}
                    is DataState.Success -> {
                        allItems.value = it.data!!
                        currentCategory.value = CurrentCategory.All.category
                        homeState.value = homeState.value?.copy(isLoading = false)
                        mainActivityState.value?.isActivityCreated = true
                    }
                }
            }.launchIn(viewModelScope)
        }

    }


}



