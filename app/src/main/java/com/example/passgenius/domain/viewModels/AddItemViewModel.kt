package com.example.passgenius.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passgenius.common.DataHolder
import com.example.passgenius.data.dataStates.DataState
import com.example.passgenius.data.repository.MyRepository
import com.example.passgenius.domain.models.ItemListModel
import com.example.passgenius.domain.models.LoginItemModel
import com.example.passgenius.domain.models.ResModel
import com.example.passgenius.domain.models.SecureNoteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.math.log


@HiltViewModel
@ExperimentalCoroutinesApi
class AddItemViewModel @Inject constructor  (
    private val repository: MyRepository
        ): ViewModel() {
    var loginItem: LoginItemModel = LoginItemModel()
    var noteItem: SecureNoteModel = SecureNoteModel()
    var item:ItemListModel? = null
    val saveLoginItemResponse:MutableLiveData<DataState<LoginItemModel>> by lazy {
        MutableLiveData<DataState<LoginItemModel>>()
    }

    val saveNoteItemResponse:MutableLiveData<DataState<SecureNoteModel>> by lazy {
        MutableLiveData<DataState<SecureNoteModel>>()
    }



    fun saveLoginItem(){
       viewModelScope.launch {
           repository.saveLoginItemToRemoteDB(loginItem).onEach {
               saveLoginItemResponse.value = it
           }.launchIn(viewModelScope)
       }
       item = ItemListModel(loginItem.itemName, loginItem.email, "LOGIN", loginItem = loginItem)
       DataHolder.allItems.value?.add(item!!)


    }

    fun saveNoteItem(){
        viewModelScope.launch {
            repository.saveNoteItemToRemoteDB(noteItem).onEach {
                saveNoteItemResponse.value = it
            }.launchIn(viewModelScope)
        }
        item = ItemListModel(noteItem.title, noteItem.content, "NOTE", noteItem = noteItem)
        DataHolder.allItems.value?.add(item!!)

    }

    fun deleteNoteItem(){
        GlobalScope.launch {
            repository.deleteNoteItem(noteItem)
        }
        DataHolder.allItems.value?.remove(item)
    }

    fun deleteLoginItem(){
        GlobalScope.launch {
            repository.deleteLoginItem(loginItem)
        }
        DataHolder.allItems.value?.remove(item)
    }

}