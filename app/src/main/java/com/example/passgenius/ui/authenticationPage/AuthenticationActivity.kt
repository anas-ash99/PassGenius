
package com.example.passgenius.ui.authenticationPage

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.passgenius.R
import com.example.passgenius.common.enums.LassSeen
import com.example.passgenius.data.repository.MyRepository
import com.example.passgenius.databinding.ActivityAuthnticationBinding
import com.example.passgenius.domain.models.UserModel
import com.example.passgenius.domain.use_cases.BiometricAuth
import com.example.passgenius.domain.viewModels.AuthenticationViewModel
import com.example.passgenius.ui.mainPage.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthnticationBinding
    private var inputText:MutableLiveData<String> = MutableLiveData("")
    private lateinit var biometricAuth: BiometricAuth
    private val viewModel:AuthenticationViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authntication)
        window.statusBarColor = ContextCompat.getColor(this, R.color.auth_page_statusBar)
        biometricAuth = BiometricAuth(this, viewModel)

        viewModel.initViewModel(this )
        biometricAuth.showBiometricDialog()
        observeEditText()
        onButtonUseDataClick()
    }


    private fun onButtonUseDataClick() {
        binding.useDataButton.setOnClickListener {
            biometricAuth.showBiometricDialog()
        }
    }


    private fun observeEditText() {
        binding.editText.addTextChangedListener {
            if (it?.toString()?.length!! > 0) {
                binding.loginButton.setCardBackgroundColor(getColor(R.color.ring_color))
                binding.loginButton.isClickable = true
                binding.loginTV.setTextColor(getColor(R.color.white))
            }else{
                binding.loginButton.setCardBackgroundColor(getColor(R.color.login_button_color))
                binding.loginTV.setTextColor(getColor(android.R.color.secondary_text_light))
                binding.loginButton.isClickable = false
            }
            inputText.value = it?.toString()?.trim()!!
        }

        binding.editText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.hintBackground.visibility = View.VISIBLE
            }else{
                binding.hintBackground.visibility = View.GONE
            }
        }

        inputText.observe(this, Observer {
            if (it != ""){
                binding.loginButton.setOnClickListener {
                    viewModel.loggedInUser?.lastSeen = "ACTIVE"
                    viewModel.updateLoggedInUser()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        })




    }

}