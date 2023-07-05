package com.example.passgenius.domain.viewModels

import android.text.InputType
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passgenius.common.AddItemPageType
import com.example.passgenius.common.DataHolder.allItems
import com.example.passgenius.common.DataHolder.favouriteItemsIds
import com.example.passgenius.common.enums.ItemType
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
    var itemType:ItemType? = null
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
        MutableLiveData<Boolean>()
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
            ItemType.LOGIN-> {
                if (validateLoginInput()){
                    saveLoginItem()
                }
            }
            ItemType.NOTE-> {
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
        if (itemType == ItemType.LOGIN){
            when(name){
                "Password"-> item?.loginItem?.password = s.toString().trim()
                "Email"-> item?.loginItem?.email = s.toString().trim()
                "Username"-> item?.loginItem?.username = s.toString().trim()
                "Note"-> item?.loginItem?.note = s.toString().trim()
                "Website"-> item?.loginItem?.website = s.toString().trim()
                "Name"-> item?.loginItem?.itemName = s.toString().trim()
            }

        }else if (itemType == ItemType.NOTE){
            when(name){
                "Title"-> item?.noteItem?.title = s.toString().trim()
                "Content"->  item?.noteItem?.content = s.toString().trim()
            }
        }

    }
    fun getItemsForList():MutableList<EditTextItemModel>{
        items.removeAll(items)
        if (itemType == ItemType.LOGIN){
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


           repository.saveLoginItemToLocalDB(item?.loginItem!!).onEach {
               saveLoginItemResponse.value = it
           }.launchIn(viewModelScope)
       }
       item = ItemListModel(loginItem.itemName, loginItem.email, "LOGIN", loginItem = loginItem)

    }

    private fun saveNoteItem(){
        viewModelScope.launch {
            repository.saveNoteItemToLocalDB(item?.noteItem!!).onEach {
                saveNoteItemResponse.value = it
            }.launchIn(viewModelScope)
//            repository.saveNoteItemToRemoteDB(item?.noteItem!!).onEach {
//                saveNoteItemResponse.value = it
//            }.launchIn(viewModelScope)
        }
        item = ItemListModel(noteItem.title, noteItem.content, "NOTE", noteItem = noteItem)


    }



    fun onEditClick(){
        pageType.value = AddItemPageType.EDIT_PAGE
    }
    fun onBackPressed(){
        onBackClick.value = true
    }
    fun checkIfFavourite(){

        when(itemType){
            ItemType.LOGIN -> {
                isFavourite.value = favouriteItemsIds.contains(item?.loginItem?._id)
            }
            ItemType.NOTE -> {
                isFavourite.value = favouriteItemsIds.contains(item?.noteItem?._id)
            }
           else->{}
        }
    }
    fun addItemToFavorite(){

        if (!isFavourite.value!!){
            when(itemType){
                ItemType.LOGIN-> {
                    favouriteItemsIds.add(item?.loginItem?._id!!)
                }
                ItemType.NOTE -> favouriteItemsIds.add(item?.noteItem?._id!!)
                else-> {}
            }
        }else{
            when(itemType){
                ItemType.LOGIN-> {
                    favouriteItemsIds.remove(item?.loginItem?._id!!)
                }
                ItemType.NOTE -> favouriteItemsIds.remove(item?.noteItem?._id!!)
                else-> {}
            }
        }

        repository.updateFavouriteItems(FavouriteItems(favouriteItemsIds))
        checkIfFavourite()
    }
    fun deleteItem() {

        isChangeHappened = true
        when(itemType){
            ItemType.LOGIN -> {
                viewModelScope.launch {
                    repository.deleteLoginItem(item?.loginItem!!)
                }
                allItems.remove(item)
            }

            ItemType.NOTE -> {
                viewModelScope.launch {
                    repository.deleteNoteItem(item?.noteItem!!)
                }
                allItems.remove(item)

            }
            else->{}
        }


        onBackPressed()

    }

}