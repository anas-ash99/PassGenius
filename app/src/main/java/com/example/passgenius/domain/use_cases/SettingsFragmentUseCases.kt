package com.example.passgenius.domain.use_cases

import android.content.Context
import android.content.Intent
import android.widget.LinearLayout
import com.example.passgenius.common.enums.SettingsPageType
import com.example.passgenius.ui.profilePage.SettingsCategoriesActivity

class SettingsFragmentUseCases {


    fun handleCategoriesClicks(
        context: Context,
        securityLayout:LinearLayout,
        accountLayout: LinearLayout
        ) {
       securityLayout.setOnClickListener {
            val intent = Intent(context, SettingsCategoriesActivity::class.java )
            intent.putExtra("PAGE_TYPE", SettingsPageType.SECURITY)
            context.startActivity(intent)
        }

        accountLayout.setOnClickListener {

        }






    }

}