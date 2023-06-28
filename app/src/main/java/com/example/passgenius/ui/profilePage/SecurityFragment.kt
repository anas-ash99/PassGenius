package com.example.passgenius.ui.profilePage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.passgenius.R
import com.example.passgenius.common.Constants
import com.example.passgenius.common.DataHolder
import com.example.passgenius.databinding.FragmentSecurityBinding
import com.example.passgenius.domain.interfaces.AppLockTimeInterface
import com.example.passgenius.ui.dialogs.AppLockTimeDialog
import com.example.passgenius.domain.viewModels.SettingsPageViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SecurityFragment : Fragment(), AppLockTimeInterface {


    private lateinit var binding:FragmentSecurityBinding
    private lateinit var viewModel: SettingsPageViewModel
    private lateinit var appLockTimeDialog: AppLockTimeDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_security,container, false )
        viewModel = ViewModelProvider(requireActivity())[SettingsPageViewModel::class.java]
        binding.topNavBar.textView5.text ="Security"

        viewModel.handleArrowBack(requireActivity(), binding.topNavBar.arrowBack)
        appLockTimeDialog = AppLockTimeDialog(requireContext(), this, viewModel.loggedInUser?.settings?.lockAppTime!!)

        binding.lockTimeLayout.setOnClickListener {
            appLockTimeDialog.dialog.show()
        }
        binding.user = viewModel.loggedInUser
       initLockTimeText()
        onLockOnExitCheck()
        println(viewModel.loggedInUser)
        return binding.root

    }

    private fun onLockOnExitCheck() {
       binding.lockOnExitCheckBx.setOnCheckedChangeListener { buttonView, isChecked ->
           if (isChecked){
               viewModel.loggedInUser?.settings?.lockAppTime = 0

           }else{
               viewModel.loggedInUser?.settings?.lockAppTime = 1

           }
           viewModel.updateLoggedInUser()
           println(viewModel.loggedInUser)
       }
    }

    override fun onChoseTime(time: Int) {


        viewModel.loggedInUser?.settings?.lockAppTime = time
        initLockTimeText()
        viewModel.updateLoggedInUser()
        appLockTimeDialog.dialog.hide()
        DataHolder.loggedInUser = viewModel.loggedInUser
    }

    private fun initLockTimeText() {
        when(val time:Int = viewModel.loggedInUser?.settings?.lockAppTime!!){
            -1 -> binding.lockTimeText.text  = "Never lock app when open and unused"
            1 ->  binding.lockTimeText.text  = "After $time minute of being open and unused"
            else ->  binding.lockTimeText.text  = "After $time minutes of being open and unused"
        }
    }


}