package com.example.passgenius.domain.viewModels



import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.example.passgenius.common.states.HomePageState

import com.example.passgenius.common.Constants
import com.example.passgenius.common.CurrentCategory
import com.example.passgenius.common.DataHolder
import com.example.passgenius.common.UserActions
import com.example.passgenius.common.enums.ItemType
import com.example.passgenius.common.enums.MainScreenTypes
import com.example.passgenius.common.states.MainActivityState
import com.example.passgenius.data.dataStates.DataState
import com.example.passgenius.data.repository.MyRepository
import com.example.passgenius.domain.models.*
import com.example.passgenius.ui.adapters.AdapterCategories

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    val deleteItem by lazy {MutableLiveData(false)}
    val mainActivityState = MutableLiveData(MainActivityState())
    val currentCategory = MutableLiveData<String?>(null)
    var loggedInUser:UserModel? = null
    var isViewCreated = false
    val itemClick by lazy {
        MutableLiveData<ItemListModel>()
    }
    val favouriteItems by lazy {
        MutableLiveData<MutableList<ItemListModel>>(mutableListOf())
    }

    val noteItems by lazy {
        MutableLiveData<MutableList<ItemListModel>>()
    }
    val loginItems by lazy {
        MutableLiveData<MutableList<ItemListModel>>()
    }
    val idsItems by lazy {
        MutableLiveData<MutableList<ItemListModel>>()
    }
    val paymentsItems by lazy {
        MutableLiveData<MutableList<ItemListModel>>()
    }

    val allItems: MutableLiveData<MutableList<ItemListModel>> = MutableLiveData(mutableListOf())

    var isExitingTheApp: Boolean = true

    fun initViewModel(){


        if (!mainActivityState.value?.isActivityCreated!!){
            getItemsFromLocalDB()
            authOnCreate()
            getLoggedInUser()
        }
        DataHolder.loggedInUser = loggedInUser

    }



    fun getFavoriteItems(){
        repository.getFavouriteItems()?.let { items->
            DataHolder.favouriteItemsIds = items.list
            favouriteItems.value?.removeAll(favouriteItems.value!!)
            allItems.value?.onEach { item->
                if (item.type == ItemType.LOGIN.name){
                    if (items.list.contains(item.loginItem._id)){
                        favouriteItems.value?.add(item)
                    }
                }else if (item.type == ItemType.NOTE.name){
                    if (items.list.contains(item.noteItem._id)){
                        favouriteItems.value?.add(item)
                    }
                }
            }

        }
    }
    fun onUserAction(action: UserActions){
        when(action){
            is UserActions.ChoseItemsCategory -> {changeSelectedCategory(action.adapter,action.position)}
            is UserActions.NavigateScreens -> {}
            UserActions.PlusOrXClick -> {}
            is UserActions.CopyDetail -> {}
            UserActions.DeleteItem -> {
                deleteItem()
            }
            is UserActions.ItemMenuClick -> {
                mainActivityState.value = mainActivityState.value?.copy(
                    isBottomDialogShown = true,
                    currentShownDialogItemPosition = action.position,
                    currentShownDialogItem = action.item
                )
            }
            UserActions.DismissDialog -> mainActivityState.value = mainActivityState.value?.copy(isBottomDialogShown = false, currentShownDialogItemPosition = null)
        }
    }


    @SuppressLint("SuspiciousIndentation")
    private fun deleteItem() {

     val item = mainActivityState.value?.currentShownDialogItem

        when(item?.type){
            "LOGIN"->{
                loginItems.value?.remove(item)

                viewModelScope.launch(Dispatchers.IO) {
                    repository.deleteLoginItem(item.loginItem)
                }
            }
            "NOTE"->{
                noteItems.value?.remove(item)
                viewModelScope.launch {
                    repository.deleteNoteItem(item.noteItem)
                }
            }
        }
        allItems.value?.remove(item)
        deleteItem.value = true

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

    private fun getItemsFromRemoteDB(){
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
                        DataHolder.allItems = allItems.value!!
                        setOtherItems()
                    }
                }
            }.launchIn(viewModelScope)
        }

    }

    fun setOtherItems() {
        loginItems.value = allItems.value?.filter { it.type == "LOGIN"} as MutableList<ItemListModel>?
        noteItems.value = allItems.value?.filter { it.type =="NOTE" } as MutableList<ItemListModel>?
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
                        DataHolder.allItems = it.data
                        currentCategory.value = CurrentCategory.All.category
                        homeState.value = homeState.value?.copy(isLoading = false)
                        mainActivityState.value?.isActivityCreated = true
                        setOtherItems()

                    }
                }
            }.launchIn(viewModelScope)
        }

    }

    fun onPassItemClick(item: ItemListModel) {
        itemClick.value = item
    }


}



