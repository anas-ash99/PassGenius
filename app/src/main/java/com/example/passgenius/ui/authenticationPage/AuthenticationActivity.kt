
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
    private var cancellationSignal:CancellationSignal? = null
    private var inputText:MutableLiveData<String> = MutableLiveData("")
    private var user:UserModel? = UserModel()

    private val viewModel:AuthenticationViewModel by viewModels()
    @Inject
    lateinit var repo:MyRepository
    private lateinit var authenticationCallback:BiometricPrompt.AuthenticationCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authntication)
        window.statusBarColor = ContextCompat.getColor(this, R.color.auth_page_statusBar)
//        startActivity(Intent(this@AuthenticationActivity, MainActivity::class.java))
//        finish()

        authenticationCallback =
            object : BiometricPrompt.AuthenticationCallback(){
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
//                    Toast.makeText(applicationContext, "Biometrics error", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    viewModel.loggedInUser?.lastSeen = "ACTIVE"
                    viewModel.updateLoggedInUser()
//                    Toast.makeText(applicationContext, "Logged in", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@AuthenticationActivity, MainActivity::class.java))
                    finish()
                }
            }
        viewModel.initViewModel(this )
        showBiometricDialog()
        observeEditText()
        onButtonUseDataClick()
    }


    private fun authUser() {


        try {
            lifecycleScope.launch {
                user = repo.getLoggedInUser(getSharedPreferences("PROFILE", MODE_PRIVATE))
                if (user == null){
                    Toast.makeText(applicationContext, "no user", Toast.LENGTH_SHORT).show()


                }else{
                    println(LocalDateTime.parse(user?.lastSeen))
                    val minutesSinceLastSeen:Long = LocalDateTime.parse(user?.lastSeen).until(LocalDateTime.now(), ChronoUnit.MINUTES)

                    if (minutesSinceLastSeen > 1){
                        showBiometricDialog()
                        observeEditText()
                        onButtonUseDataClick()
                    }else{
                        startActivity(Intent(this@AuthenticationActivity, MainActivity::class.java))
                        finish()
                    }
                    println(minutesSinceLastSeen)
                }

            }
        }catch (e:java.lang.Exception){
            Log.e("auth", e.toString())
        }


    }

    private fun onButtonUseDataClick() {
        binding.useDataButton.setOnClickListener {
            showBiometricDialog()
        }
    }

    private fun showBiometricDialog() {
        val biometricPrompt = BiometricPrompt.Builder(this)
            .setTitle("Unlock PassGenius")
            .setNegativeButton("Use app password", this.mainExecutor, DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
            }).build()
        biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
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



//
//            Toast.makeText(this, "clik", Toast.LENGTH_SHORT).show()
//            binding.loginButton.setOnClickListener {
//                if (inputText != ""){
//                     startActivity(Intent(this,MainActivity::class.java))
//                }
//            }

        inputText.observe(this, Observer {
            if (it != ""){
                binding.loginButton.setOnClickListener {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        })




    }

    private fun getCancellationSignal():CancellationSignal{
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            Toast.makeText(applicationContext, "Authentication was cancelled", Toast.LENGTH_SHORT).show()
        }
        return cancellationSignal as CancellationSignal
    }
    private fun checkBiometricSupport(): Boolean{
        val keyguardManager:KeyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isKeyguardSecure){
            Toast.makeText(applicationContext, "Fingerprint is not in enabled the setting", Toast.LENGTH_SHORT).show()
            return false
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(applicationContext, "Fingerprint authentication not enabled", Toast.LENGTH_SHORT).show()
            return false
        }

        return if(packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            true
        }else true
    }
}