package com.example.passgenius.ui.profilePage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.passgenius.R
import com.example.passgenius.common.enums.SettingsPageType
import com.example.passgenius.databinding.ActivitySettingsCategoriesBinding
import com.example.passgenius.domain.viewModels.SettingsPageViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsCategoriesActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySettingsCategoriesBinding
    private val viewModel: SettingsPageViewModel by viewModels()
    private val manager: FragmentManager = supportFragmentManager
    private val transaction: FragmentTransaction = manager.beginTransaction()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings_categories)
        viewModel.initPage(intent, transaction,getSharedPreferences("PROFILE", MODE_PRIVATE))

    }


}