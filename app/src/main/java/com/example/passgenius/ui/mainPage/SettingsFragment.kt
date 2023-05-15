package com.example.passgenius.ui.mainPage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.passgenius.R
import com.example.passgenius.common.enums.SettingsPageType
import com.example.passgenius.databinding.FragmentSecurityBinding
import com.example.passgenius.databinding.FragmentSettingsBinding
import com.example.passgenius.domain.use_cases.SettingsFragmentUseCases
import com.example.passgenius.domain.viewModels.MainViewModel
import com.example.passgenius.ui.profilePage.SettingsCategoriesActivity


class SettingsFragment : Fragment() {


    private lateinit var viewModel:MainViewModel
    private lateinit var binding: FragmentSettingsBinding
    private val useCase = SettingsFragmentUseCases()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
//        useCase.handleCategoriesClicks(requireContext(),binding.securityLayout, binding.accountLayout)
         handleCategoriesClicks()
        return binding.root
    }

    private fun handleCategoriesClicks() {
        binding.securityLayout.setOnClickListener {
            viewModel.isExitingTheApp = false
            val intent = Intent(requireContext(), SettingsCategoriesActivity::class.java )
            intent.putExtra("PAGE_TYPE", SettingsPageType.SECURITY)
            startActivity(intent)
        }

        binding.accountLayout.setOnClickListener {

        }






    }


}