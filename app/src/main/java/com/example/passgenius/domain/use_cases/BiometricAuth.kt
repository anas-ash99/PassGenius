package com.example.passgenius.domain.use_cases

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
import android.os.CancellationSignal
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.passgenius.domain.viewModels.AuthenticationViewModel
import com.example.passgenius.ui.authenticationPage.AuthenticationActivity
import com.example.passgenius.ui.mainPage.MainActivity

class BiometricAuth (
    private val activity: AuthenticationActivity,
    private val viewModel: AuthenticationViewModel
        ){

    private var cancellationSignal:CancellationSignal? = null
    var authenticationCallback: BiometricPrompt.AuthenticationCallback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
            super.onAuthenticationError(errorCode, errString)
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
            super.onAuthenticationSucceeded(result)
            viewModel.loggedInUser?.lastSeen = "ACTIVE"
            viewModel.updateLoggedInUser()
            println(viewModel.loggedInUser)
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finish()

        }
    }


  fun showBiometricDialog(){

      if (checkBiometricSupport()){

          val biometricPrompt = BiometricPrompt.Builder(activity)
              .setTitle("Unlock PassGenius")
              .setNegativeButton("Use app password", activity.mainExecutor, DialogInterface.OnClickListener { dialog, which ->
                  Toast.makeText(activity, "Cancel", Toast.LENGTH_SHORT).show()
              }).build()
          biometricPrompt.authenticate(getCancellationSignal(), activity.mainExecutor, authenticationCallback)
      }
    }


    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            Toast.makeText(activity, "Authentication was cancelled", Toast.LENGTH_SHORT).show()
        }
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean{
        val keyguardManager: KeyguardManager = activity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isKeyguardSecure){
            Toast.makeText(activity, "Fingerprint is not in enabled the setting", Toast.LENGTH_SHORT).show()
            return false
        }

        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(activity, "Fingerprint authentication not enabled", Toast.LENGTH_SHORT).show()
            return false
        }

        return if(activity.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            true
        }else true
    }
}