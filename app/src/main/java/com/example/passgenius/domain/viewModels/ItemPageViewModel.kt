package com.example.passgenius.domain.viewModels

import android.text.InputType
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passgenius.common.AddItemPageType
import com.example.passgenius.common.DataHolder
import com.example.passgenius.common.enums.AddItemType
import com.example.passgenius.data.dataStates.DataState
import com.example.passgenius.data.repository.MyRepository
import com.example.passgenius.domain.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
@ExperimentalCoroutinesApi
class ItemPageViewModel @Inject constructor(
    private val repository: MyRepository,

    ): ViewModel() {
    var loginItem: LoginItemModel = LoginItemModel()
    var noteItem: SecureNoteModel = SecureNoteModel()
    var itemType:AddItemType? = null
    var items = mutableListOf<EditTextItemModel>()
    var isChangeHappened = false
    val pageType by lazy {
        MutableLiveData<AddItemPageType>()
    }

    val validationError by lazy {
        MutableLiveData<MutableList<Int>>()
    }
    val onBackClick by lazy {
        MutableLiveData(false)
    }
    var item:ItemListModel? = null
    val isDeleteDialogShown by lazy {
        MutableLiveData(false)
    }

    val isFavourite by lazy {
        MutableLiveData(false)
    }
    val saveLoginItemResponse by lazy {
        MutableLiveData<DataState<LoginItemModel>>()
    }

    val saveNoteItemResponse by lazy {
        MutableLiveData<DataState<SecureNoteModel>>()
    }


    fun onCheckMarkClick(){


        validationError.value = mutableListOf()
        isChangeHappened = true
        when(itemType){
            AddItemType.LOGIN-> {
                if (validateLoginInput()){
                    saveLoginItem()
                }
            }
            AddItemType.NOTE-> {
                if (validateNoteInput()){
                    saveNoteItem()
                }
            }
            else-> {}
        }
    }

     fun deleteIconClick(){

         isDeleteDialogShown.value = true
     }
    private fun validateLoginInput():Boolean{
        var isValid = true

        if (item?.loginItem?.itemName?.isBlank()!!){
            isValid = false
            validationError.value?.add(0)
            items[0].isValid = false
        }


        if (item?.loginItem?.email?.isBlank()!!){
            isValid = false
            validationError.value?.add(1)
            items[1].isValid = false
        }

        if (item?.loginItem?.password?.isBlank()!!){
            isValid = false
            validationError.value?.add(3)
            items[3].isValid = false
        }

        if (!isValid)
            validationError.value = validationError.value
        return isValid

    }



    private fun validateNoteInput():Boolean{
        var isValid = true


        if ( item?.noteItem?.title?.isBlank()!!){
            isValid = false
            validationError.value?.add(0)
            items[0].isValid = false
        }

        if ( item?.noteItem?.title?.isBlank()!!){
            isValid = false
            validationError.value?.add(1)
            items[1].isValid = false
        }


        if (!isValid)
            validationError.value = validationError.value


        return isValid



    }

    fun takeInput(s:CharSequence,name:String  ){
        if (itemType == AddItemType.LOGIN){
            when(name){
                "Password"-> item?.loginItem?.password = s.toString().trim()
                "Email"-> item?.loginItem?.email = s.toString().trim()
                "Username"-> item?.loginItem?.username = s.toString().trim()
                "Note"-> item?.loginItem?.note = s.toString().trim()
                "Website"-> item?.loginItem?.website = s.toString().trim()
                "Name"-> item?.loginItem?.itemName = s.toString().trim()
            }

        }else if (itemType == AddItemType.NOTE){
            when(name){
                "Title"-> item?.noteItem?.title = s.toString().trim()
                "Content"->  item?.noteItem?.content = s.toString().trim()
            }
        }

    }
    fun getItemsForList():MutableList<EditTextItemModel>{
        items.removeAll(items)
        if (itemType == AddItemType.LOGIN){
            items.add(EditTextItemModel("Name", InputType.TYPE_CLASS_TEXT, item?.loginItem?.itemName!!) )
            items.add( EditTextItemModel("Email", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, item?.loginItem?.email!!) )
            items.add( EditTextItemModel("Username", InputType.TYPE_CLASS_TEXT, item?.loginItem?.username!!) )
            items.add( EditTextItemModel("Password", InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD, item?.loginItem?.password!!) )
            items.add( EditTextItemModel("Website", InputType.TYPE_TEXT_VARIATION_URI, item?.loginItem?.website!!) )
            items.add( EditTextItemModel("Note", InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE, item?.loginItem?.note!!) )
        }else{
            items.add(EditTextItemModel("Title", InputType.TYPE_CLASS_TEXT, item?.noteItem?.title!!) )
            items.add(EditTextItemModel("Content", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, item?.noteItem?.content!!) )

        }


        if (pageType.value == AddItemPageType.VIEW_PAGE ){
            items = items.filter { it.value.isNotBlank()} as MutableList<EditTextItemModel>
        }
        return items

    }






    private fun saveLoginItem(){
       viewModelScope.launch {
           repository.saveLoginItemToRemoteDB(item?.loginItem!!).onEach {
               saveLoginItemResponse.value = it
           }.launchIn(viewModelScope)
       }
       item = ItemListModel(loginItem.itemName, loginItem.email, "LOGIN", loginItem = loginItem)

    }

    private fun saveNoteItem(){
        viewModelScope.launch {
            repository.saveNoteItemToRemoteDB(item?.noteItem!!).onEach {
                saveNoteItemResponse.value = it
            }.launchIn(viewModelScope)
        }
        item = ItemListModel(noteItem.title, noteItem.content, "NOTE", noteItem = noteItem)


    }



    fun onEditClick(){
        pageType.value = AddItemPageType.EDIT_PAGE
    }
    fun onBackPressed(){
        onBackClick.value = true
    }

    fun deleteNoteItem(){
        viewModelScope.launch {
            repository.deleteNoteItem(noteItem)
        }
        DataHolder.allItems.remove(item)
    }

    fun deleteLoginItem(){
        viewModelScope.launch {
            repository.deleteLoginItem(loginItem)
        }
        DataHolder.allItems.remove(item)
    }

    fun deleteItem() {
        viewModelScope.launch {
        when(itemType){
            AddItemType.LOGIN -> {
                repository.deleteNoteItem(item?.noteItem!!)
            }

            AddItemType.NOTE -> repository.deleteNoteItem(item?.noteItem!!)
            else->{}
        }


        }
         onBackPressed()
    }

}