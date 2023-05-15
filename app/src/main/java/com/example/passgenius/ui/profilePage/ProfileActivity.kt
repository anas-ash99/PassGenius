package com.example.passgenius.ui.profilePage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.transition.Slide
import com.example.passgenius.R
import com.example.passgenius.common.enums.SettingsPageType
import com.example.passgenius.databinding.ActivityProfileBinding
import com.example.passgenius.ui.mainPage.HomeFragment

class ProfileActivity : AppCompatActivity() {

     private lateinit var binding:ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        binding.securityLayout.setOnClickListener {
            val intent =Intent(this@ProfileActivity,SettingsCategoriesActivity::class.java )
            intent.putExtra("PAGE_TYPE", SettingsPageType.SECURITY)
            startActivity(intent)

        }

    }
}