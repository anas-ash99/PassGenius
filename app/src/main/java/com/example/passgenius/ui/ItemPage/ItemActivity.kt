package com.example.passgenius.ui.ItemPage


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType

import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData

import com.example.passgenius.common.DeleteClickInterface
import com.example.passgenius.R
import com.example.passgenius.common.AddItemPageType

import com.example.passgenius.ui.adapters.ItemEditTextAdapter
import com.example.passgenius.databinding.ActivityAddNewLoginBinding
import com.example.passgenius.ui.dialogs.DeleteItemDialog
import com.example.passgenius.common.enums.AddItemType

import com.example.passgenius.domain.models.*

import com.example.passgenius.domain.viewModels.ItemPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Suppress("DEPRECATION")
@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class ItemActivity: AppCompatActivity(), DeleteClickInterface {

    private lateinit var binding: ActivityAddNewLoginBinding

    private val viewModel: ItemPageViewModel by viewModels()
    private var isEditPage:MutableLiveData<Boolean> = MutableLiveData()
    private var pageType:String = ""
    private lateinit var deleteDialog: DeleteItemDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_login)
        isEditPage.value = intent.getBooleanExtra("isEditPage", true)
        viewModel.pageType.value = intent.getSerializableExtra("pageType") as AddItemPageType
        viewModel.itemType = intent.getSerializableExtra("itemType") as AddItemType
        viewModel.item = intent.getSerializableExtra("item") as ItemListModel
        deleteDialog = DeleteItemDialog(this, viewModel)
        switchFragment(ItemFragment())
        observeDialog()
        observeOnBackPressed()

    }

    private fun observeDialog() {
        viewModel.isDeleteDialogShown.observe(this){
            if (it){
                deleteDialog.showDialog()
            }
        }
    }


    private fun switchFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_layout, fragment, fragment.tag).commit()
        }
    }
    private fun observeOnBackPressed() {
        viewModel.onBackClick.observe(this){
            if (it){
                onBackPressed()
            }
        }
    }













    private fun onstarClick() {
//        binding.starIconCard.setOnClickListener {
//
//            if (!starClick.value!!){
//                binding.startImage.setBackgroundResource(R.drawable.star_fav_icon)
//
//                Toast.makeText(applicationContext, "Added to favorites", Toast.LENGTH_SHORT).show()
//                starClick.value = true
//            }else{
//                binding.startImage.setBackgroundResource(R.drawable.ic_baseline_star_outline_24)
//                starClick.value = false
//            }
//
//            when(pageType){
//                "LOGIN"->{
//                    Log.v("tag",starClick.value?.toString()?.trim()?.lowercase()!! )
////                    viewModel.loginItem.isFavourite = starClick.value?.toString()?.trim()?.lowercase()!!
////                    db.loginItemsDao.addItem(viewModel.loginItem)
//                }
//                "NOTE"->{
////                    viewModel.noteItem.isFavourite = starClick.value?.toString()?.trim()?.lowercase()!!
////                    db.noteItemsDao.addItem(viewModel.noteItem)
//                }
//            }
//        }
    }
    


    @Deprecated("Deprecated in Java")
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onBackPressed() {
        val intent = Intent()
        if (viewModel.isChangeHappened){
            setResult(RESULT_OK, intent)
        }
        super.onBackPressed()

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



        Toast.makeText(this, "Item has been deleted", Toast.LENGTH_SHORT).show()
        onBackPressed()
    }

}


