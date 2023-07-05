package com.example.passgenius.ui.ItemPage


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData

import com.example.passgenius.R
import com.example.passgenius.common.AddItemPageType

import com.example.passgenius.databinding.ActivityAddNewLoginBinding
import com.example.passgenius.ui.dialogs.DeleteItemDialog
import com.example.passgenius.common.enums.ItemType

import com.example.passgenius.domain.models.*

import com.example.passgenius.domain.viewModels.ItemPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Suppress("DEPRECATION")
@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class ItemActivity: AppCompatActivity(){

    private lateinit var binding: ActivityAddNewLoginBinding

    private val viewModel: ItemPageViewModel by viewModels()
    private var isEditPage:MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var deleteDialog: DeleteItemDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_login)
        isEditPage.value = intent.getBooleanExtra("isEditPage", true)
        viewModel.pageType.value = intent.getSerializableExtra("pageType") as AddItemPageType
        viewModel.itemType = intent.getSerializableExtra("itemType") as ItemType
        viewModel.item = intent.getSerializableExtra("item") as ItemListModel
        viewModel.checkIfFavourite()
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





    @Deprecated("Deprecated in Java")
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onBackPressed() {
        val intent = Intent()
        if (viewModel.isChangeHappened){
            setResult(RESULT_OK, intent)
        }
        super.onBackPressed()

    }



}


