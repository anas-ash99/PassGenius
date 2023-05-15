package com.example.passgenius.ui.ItemPage


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View

import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passgenius.common.DeleteClickInterface
import com.example.passgenius.R
import com.example.passgenius.common.KeyboardManger
import com.example.passgenius.common.KeyboardManger.hideSoftKeyboard
import com.example.passgenius.ui.adapters.ItemEditTextAdapter
import com.example.passgenius.databinding.ActivityAddNewLoginBinding
import com.example.passgenius.domain.use_cases.dialogs.CenterDialog
import com.example.passgenius.common.enums.AddItemType
import com.example.passgenius.data.dataStates.DataState
import com.example.passgenius.data.repository.MyRepository

import com.example.passgenius.data.room.daos.LoginItemDao
import com.example.passgenius.data.room.daos.NoteItemDao
import com.example.passgenius.domain.models.*
import com.example.passgenius.domain.use_cases.TestUseCase
import com.example.passgenius.domain.viewModels.AddItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject


@AndroidEntryPoint
class AddNewItemActivity(): AppCompatActivity(), DeleteClickInterface {

    private lateinit var binding: ActivityAddNewLoginBinding
    private lateinit var viewModel: AddItemViewModel
    private var items:MutableList<EditTextItemModel> = mutableListOf()
    private lateinit var adapter: ItemEditTextAdapter
    private var isEditPage:MutableLiveData<Boolean> = MutableLiveData()
    private var onDoneClick:MutableLiveData<Boolean> = MutableLiveData()
    private var pageType:String = ""
    private var starClick:MutableLiveData<Boolean> = MutableLiveData(false)
    private lateinit var deleteDialogClass: CenterDialog
    @Inject
    lateinit var noteDao: NoteItemDao
    @Inject
    lateinit var loginDao: LoginItemDao
    @Inject
    lateinit var repo:MyRepository

    @Inject
    lateinit var testUseCase: TestUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_login)
        viewModel = ViewModelProvider(this)[AddItemViewModel::class.java]
        isEditPage.value = intent.getBooleanExtra("isEditPage", true)
        pageType = intent.getStringExtra("pageType")!!
        deleteDialogClass = CenterDialog(this, deleteInterface = this )
        initPage()
        observeSaveNoteItem()
        observeSaveItem()
        handleEditButtonClick()
        onDoneButtonClick()
        observeIsEditPage()
        onArrowClick()
        onDeleteIconClick()
//        lifecycleScope.launch {
//
//            repo.updateLoggedInUser(getSharedPreferences("PROFILE", MODE_PRIVATE), UserModel(lastSeen = "ACTIVE"))
//        }



    }

    private fun handleEditButtonClick() {
        binding.editButton.setOnClickListener {
            isEditPage.value = true
            when(pageType){
                "LOGIN"-> initRecyclerView(AddItemType.LOGIN)
                "NOTE"-> initRecyclerView(AddItemType.SECURE_NOTE)
            }


        }
    }






    private fun observeSaveItem(){
        viewModel.saveLoginItemResponse.observe(this, Observer{
            when(it){
                is DataState.Success ->{
                    isEditPage.value = false
                    binding.progressBarLayout.visibility =View.GONE
                    initRecyclerView(AddItemType.LOGIN)
                }
                is DataState.Loading ->{

                    binding.progressBarLayout.visibility =View.VISIBLE
                }

                is DataState.Error->{
                    Toast.makeText(this, "couldn't save item", Toast.LENGTH_SHORT).show()
                    binding.progressBarLayout.visibility =View.GONE
                }


                else -> {}
            }
        })

    }

    private fun observeSaveNoteItem(){
        viewModel.saveNoteItemResponse.observe(this, Observer{
            when(it){
                is DataState.Success ->{
                    isEditPage.value = false
                    refreshNotesItems()
                    binding.progressBarLayout.visibility =View.GONE

                    initRecyclerView(AddItemType.SECURE_NOTE)

                }
                is DataState.Loading ->{
                    binding.progressBarLayout.visibility =View.VISIBLE
                }

                is DataState.Error->{
                    Toast.makeText(this, "couldn't save item", Toast.LENGTH_SHORT).show()
                    binding.progressBarLayout.visibility =View.GONE
                }


                else -> {}
            }
        })

    }




    private fun onDoneButtonClick(){
        binding.doneButton.setOnClickListener {
            when(pageType){
                "LOGIN"-> {
                    if (viewModel.loginItem.itemName == "" || viewModel.loginItem.email == "" || viewModel.loginItem.password == "") {
                        adapter.onDoneClick(true)
                    } else {
                        refreshLoginItems()
                        viewModel.saveLoginItem()

                    }
                }
                "NOTE"->{
                    if (viewModel.noteItem.title == "" || viewModel.noteItem.content == "") {
                        adapter.onDoneClick(true)
                    } else {
                        viewModel.noteItem.createdSAt = LocalDateTime.now().toString()
                        viewModel.saveNoteItem()
                    }
                }
            }



            KeyboardManger.hideSoftKeyboard(binding.editPageView, this)
        }


    }




    private fun observeIsEditPage(){

        isEditPage.observe(this, Observer {
            testUseCase.observeIsEditPage(it!!,
                binding.editPageView,
                binding.showPageView,
                binding.dateLayout,
                binding.deleteButton,
                binding.starIconCard,
                binding.doneButton,
                binding.editButton

            )
        })

//      isEditPage.observe(this, Observer {
//          if (it){
//              binding.editPageView.visibility = View.VISIBLE
//              binding.showPageView.visibility = View.GONE
//              binding.dateLayout.visibility = View.GONE
//              binding.deleteButton.visibility =View.GONE
//              binding.starIconCard.visibility = View.GONE
//              binding.doneButton.visibility = View.VISIBLE
//              binding.editButton.visibility = View.GONE
//
//          }else{
//
//
//              binding.dateLayout.visibility = View.VISIBLE
//              binding.deleteButton.visibility =View.VISIBLE
//              binding.starIconCard.visibility = View.VISIBLE
//              binding.editPageView.visibility = View.GONE
//              binding.showPageView.visibility = View.VISIBLE
//              binding.doneButton.visibility = View.GONE
//              binding.editButton.visibility = View.VISIBLE
//              onstarClick()
//
//          }
//      })



  }

    private fun onstarClick() {
        binding.starIconCard.setOnClickListener {

            if (!starClick.value!!){
                binding.startImage.setBackgroundResource(R.drawable.star_fav_icon)

                Toast.makeText(applicationContext, "Added to favorites", Toast.LENGTH_SHORT).show()
                starClick.value = true
            }else{
                binding.startImage.setBackgroundResource(R.drawable.ic_baseline_star_outline_24)
                starClick.value = false
            }

            when(pageType){
                "LOGIN"->{
                    Log.v("tag",starClick.value?.toString()?.trim()?.lowercase()!! )
//                    viewModel.loginItem.isFavourite = starClick.value?.toString()?.trim()?.lowercase()!!
//                    db.loginItemsDao.addItem(viewModel.loginItem)
                }
                "NOTE"->{
//                    viewModel.noteItem.isFavourite = starClick.value?.toString()?.trim()?.lowercase()!!
//                    db.noteItemsDao.addItem(viewModel.noteItem)
                }
            }
        }
    }
    


    private fun onDeleteIconClick(){
        binding.deleteButton.setOnClickListener {
            deleteDialogClass?.dialog?.show()
        }
        
    }
    private fun onArrowClick() {
        binding.arrowBack.setOnClickListener {
            onBackPressed()
        }
    }




    private fun initPage() {
        val intent:Intent = intent
        viewModel.item = intent.getSerializableExtra("itemList") as ItemListModel?

        when(intent.getStringExtra("pageType")){
            "LOGIN" -> {
                viewModel.loginItem =  intent.getSerializableExtra("item" ) as LoginItemModel
                initLoginItems()
                initRecyclerView(AddItemType.LOGIN)
                if (viewModel.loginItem.itemName.length > 2){
                    binding.LogoText2.text = viewModel.loginItem.itemName.substring(0,2).uppercase()
                }
                deleteDialogClass.loginItemModel = viewModel.loginItem
                deleteDialogClass.pageTye = AddItemType.LOGIN
            }
            "NOTE"->{
                viewModel.noteItem = intent.getSerializableExtra("item" ) as SecureNoteModel
                
                initNotePage()
                initRecyclerView(AddItemType.SECURE_NOTE)
                if (viewModel.noteItem.title.length > 2){
                    binding.LogoText2.text = viewModel.noteItem.title.substring(0,2).uppercase()
                }
                
                deleteDialogClass.noteItem = viewModel.noteItem
                deleteDialogClass.pageTye = AddItemType.SECURE_NOTE

            }

        }

    }





    private fun initNotePage() {
        items.add(0, EditTextItemModel("Title", InputType.TYPE_CLASS_TEXT, viewModel.noteItem.title) )
        items.add(1, EditTextItemModel("Content", InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE, viewModel.noteItem.content) )

    }
    private fun refreshNotesItems(){
        items[0].value = viewModel.noteItem.title
        items[1].value = viewModel.noteItem.content
        binding.LogoText2.text = viewModel.noteItem.title.substring(0,2).uppercase()

    }

    private fun initLoginItems() {

        items.add(0, EditTextItemModel("Name", InputType.TYPE_CLASS_TEXT, viewModel.loginItem.itemName) )
        items.add(1, EditTextItemModel("Email", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, viewModel.loginItem.email) )
        items.add(2, EditTextItemModel("Username", InputType.TYPE_CLASS_TEXT, viewModel.loginItem.username) )
        items.add(3, EditTextItemModel("Password", InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD, viewModel.loginItem.password) )
        items.add(4, EditTextItemModel("Website", InputType.TYPE_TEXT_VARIATION_URI, viewModel.loginItem.website) )
        items.add(5, EditTextItemModel("Note", InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE, viewModel.loginItem.note) )

    }

    private fun refreshLoginItems(){
        items[0].value = viewModel.loginItem.itemName
        items[1].value = viewModel.loginItem.email
        items[2].value = viewModel.loginItem.username
        items[3].value = viewModel.loginItem.password
        items[4].value = viewModel.loginItem.website
        items[5].value = viewModel.loginItem.note
        binding.LogoText2.text = viewModel.loginItem.itemName.substring(0,2).uppercase()


    }

    private fun initRecyclerView(type: AddItemType){

        adapter = ItemEditTextAdapter(this, this, this, items, type, isEditPage.value!! )
        binding.editTextRv.adapter = adapter
        binding.editTextRv.layoutManager = LinearLayoutManager(this)

    }


    override fun onDeleteClick(position: Int) {
//        lifecycleScope.launch {
//            loginDao.deleteItem(viewModel.loginItem)
//
//        }

        when(pageType){
            "LOGIN" ->viewModel.deleteLoginItem()
            "NOTE" ->viewModel.deleteNoteItem()
        }

        deleteDialogClass.dialog.dismiss()

        Toast.makeText(this, "Item has been deleted", Toast.LENGTH_SHORT).show()
        onBackPressed()
    }

}


