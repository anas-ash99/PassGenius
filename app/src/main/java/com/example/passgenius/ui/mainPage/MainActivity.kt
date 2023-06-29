package com.example.passgenius.ui.mainPage

import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment

import com.example.passgenius.common.enums.MainScreenTypes
import com.example.passgenius.databinding.ActivityMainBinding
import com.example.passgenius.domain.models.LoginItemModel
import com.example.passgenius.domain.models.SecureNoteModel

import com.example.passgenius.domain.viewModels.MainViewModel
import com.example.passgenius.ui.ItemPage.AddNewItemActivity
import com.example.passgenius.ui.authenticationPage.AuthenticationActivity
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

import com.example.passgenius.R as wR


@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {





    lateinit var binding:ActivityMainBinding
    private var isClicked = false
    private lateinit var i:Intent
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = resources.getColor(wR.color.main_activity_background_color)
        viewModel.initViewModel()
        viewModel.lifecycleOwner = this
        binding = DataBindingUtil.setContentView(this, wR.layout.activity_main)
        handleAddButtonClick()
        observeState()
        binding.viewModel = viewModel
        i = Intent(this, AddNewItemActivity::class.java)
        onAddCategoriesClicks()
        onBottomNavClicks()
        checkAuthentication()

    }


    private fun observeState(){
        viewModel.mainActivityState.observe(this){
            binding.state = it
        }
    }




    private fun authUserOnStart() {


        try {

            viewModel.getLoggedInUser()

//            if(viewModel.loggedInUser?.lastSeen != "ACTIVE"){
//                val minutesSinceLastSeen:Long = LocalDateTime.parse(viewModel.loggedInUser?.lastSeen).until(LocalDateTime.now(), ChronoUnit.MINUTES)
//                if ( viewModel.loggedInUser?.settings?.lockAppTime != -1 && minutesSinceLastSeen >= viewModel.loggedInUser?.settings?.lockAppTime!!){
//                    viewModel.isExitingTheApp = false
//                    startActivity(Intent(applicationContext, AuthenticationActivity::class.java))
//                    finish()
//                }
//            }

        }catch (e:java.lang.Exception){
            Log.e("auth", e.toString())
        }


    }

    private fun observeCurrentScreen(){
        viewModel.mainActivityState.value?.currentScreen?.observe(this){

            when(it){
                MainScreenTypes.HOME -> {
                    switchFragment(HomeFragment())
                    if (binding.bottomNavigationView.selectedItemId != wR.id.home_icon){
                        binding.bottomNavigationView.selectedItemId = wR.id.home_icon

                    }
                }
                MainScreenTypes.FAVORITE -> {
                    switchFragment(FavouriteItemsFragment())
                    if (binding.bottomNavigationView.selectedItemId != wR.id.favorite_icon){
                        binding.bottomNavigationView.selectedItemId = wR.id.favorite_icon

                    }

                }
                MainScreenTypes.SEARCH -> {
                    switchFragment(SearchFragment())
                    if (binding.bottomNavigationView.selectedItemId != wR.id.search_icon){
                        binding.bottomNavigationView.selectedItemId = wR.id.search_icon

                    }

                }
                MainScreenTypes.SETTINGS -> {
                    switchFragment(SettingsFragment())
                    if (binding.bottomNavigationView.selectedItemId != wR.id.settings_icon){
                        binding.bottomNavigationView.selectedItemId = wR.id.settings_icon

                    }

                }
            }
        }
    }


    override fun onBackPressed() {

        if (viewModel.mainActivityState.value?.currentScreen?.value != MainScreenTypes.HOME){
            viewModel.mainActivityState.value?.currentScreen?.value = MainScreenTypes.HOME

        }else{
            super.onBackPressed()
        }
    }
    private fun switchFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(wR.id.fragment_layout, fragment, fragment.tag).commit()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.isExitingTheApp = true
        authUserOnStart()
    }

    override fun onPause() {
        if (viewModel.isExitingTheApp){
            viewModel.loggedInUser?.lastSeen = LocalDateTime.now().toString()
            viewModel.updateLoggedInUser()

        }
        super.onPause()
    }


    private fun handleAddButtonClick() {

    }



    private fun checkAuthentication(){
        if (!viewModel.mainActivityState.value?.isAuthenticated!!){
            startActivity(Intent(this, AuthenticationActivity::class.java))
            finish()
        }else{
            observeCurrentScreen()
        }
    }
    private fun onAddCategoriesClicks(){


        binding.addLoginCard.setOnClickListener {
            i.putExtra("item", LoginItemModel())

            beginActivity("LOGIN")
        }

        binding.secureNoteLayout.setOnClickListener {
            i.putExtra("item", SecureNoteModel())

           beginActivity("NOTE")
        }

        binding.addPaymentsCard.setOnClickListener {
            binding.allLayout.visibility = View.GONE
            binding.paymentsLayout.visibility = View.VISIBLE
        }

        binding.addBankCard.setOnClickListener {

        }

    }

    private fun beginActivity(name:String){
        viewModel.isExitingTheApp = false
        i.putExtra("pageType", name)
        i.putExtra("isEditPage", true)
        startActivity(i)
        overridePendingTransition(wR.anim.slide_in_up, wR.anim.slide_up_immediatley)
        binding.buttonIcon.setBackgroundResource(wR.drawable.icon_plus)
        binding.addCategoriesLayout.visibility = View.GONE
        isClicked= false
    }

    private fun onBottomNavClicks() {
     binding.bottomNavigationView.setOnItemSelectedListener {

         when(it.title?.toString()){
              "Home" -> viewModel.mainActivityState.value?.currentScreen?.value = MainScreenTypes.HOME
              "Favorite" -> viewModel.mainActivityState.value?.currentScreen?.value = MainScreenTypes.FAVORITE
              "Search" -> viewModel.mainActivityState.value?.currentScreen?.value = MainScreenTypes.SEARCH
              "Settings" -> viewModel.mainActivityState.value?.currentScreen?.value = MainScreenTypes.SETTINGS
         }
         true
     }

    }




}



