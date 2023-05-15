package com.example.passgenius.ui.mainPage

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.passgenius.data.repository.MyRepository
import com.example.passgenius.databinding.ActivityMainBinding
import com.example.passgenius.domain.models.LoginItemModel
import com.example.passgenius.domain.models.SecureNoteModel
import com.example.passgenius.domain.models.UserModel
import com.example.passgenius.domain.viewModels.MainViewModel
import com.example.passgenius.ui.ItemPage.AddNewItemActivity
import com.example.passgenius.ui.authenticationPage.AuthenticationActivity
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Objects
import javax.inject.Inject
import com.example.passgenius.R as wR


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {





    lateinit var binding:ActivityMainBinding
    private var isClicked = false
    private lateinit var i:Intent
    private lateinit var  viewModel: MainViewModel
    private var user:UserModel? = UserModel()
    private val manager: FragmentManager = supportFragmentManager
    val transaction: FragmentTransaction = manager.beginTransaction()

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var repository: MyRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        window.statusBarColor = resources.getColor(wR.color.main_activity_background_color)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.initViewModel(this, manager)
        viewModel.lifecycleOwner = this
        viewModel.observeAllItems()
        binding = DataBindingUtil.setContentView(this, wR.layout.activity_main)
//        authOnCreate()
        handleAddButtonClick()
        i = Intent(this, AddNewItemActivity::class.java)
        onAddCategoriesClicks()
        onBottomNavClicks()
//        viewModel.loggedInUser?.favourites = arrayListOf("644c61a7ea1c970a42384a2c", "644c7a9fb0277a4a0675584a")
//        viewModel.updateLoggedInUser()
//        println(viewModel.loggedInUser)

    }



    fun test(list:MutableList<Objects>){

        println(list[0])

    }

    private fun authOnCreate(){
        if (viewModel.loggedInUser?.lastSeen != "ACTIVE"){
            viewModel.isExitingTheApp = false
            startActivity(Intent(applicationContext, AuthenticationActivity::class.java))
            finish()

        }else{
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.replace(wR.id.fragment_layout, HomeFragment() , "home_fragment")
            transaction.commit()
            handleAddButtonClick()
            i = Intent(this, AddNewItemActivity::class.java)
            onAddCategoriesClicks()
            onBottomNavClicks()

        }


    }

    private fun authUserOnStart() {


        try {

            viewModel.getLoggedInUser()

            if(viewModel.loggedInUser?.lastSeen != "ACTIVE"){
                val minutesSinceLastSeen:Long = LocalDateTime.parse(viewModel.loggedInUser?.lastSeen).until(LocalDateTime.now(), ChronoUnit.MINUTES)
                if ( viewModel.loggedInUser?.settings?.lockAppTime != -1 && minutesSinceLastSeen >= viewModel.loggedInUser?.settings?.lockAppTime!!){
                    viewModel.isExitingTheApp = false
                    startActivity(Intent(applicationContext, AuthenticationActivity::class.java))
                    finish()
                }
            }

        }catch (e:java.lang.Exception){
            Log.e("auth", e.toString())
        }


    }


    override fun onStart() {
        super.onStart()
        viewModel.isExitingTheApp = true
        authUserOnStart()
        mainViewModel.reinitItems()

    }

    override fun onPause() {
        if (viewModel.isExitingTheApp){
            viewModel.loggedInUser?.lastSeen = LocalDateTime.now().toString()
            viewModel.updateLoggedInUser()

        }
        super.onPause()
    }


    private fun handleAddButtonClick() {
        binding.addButton2.animate().rotation(45f).duration = 400

        binding.addButton.setOnClickListener {
            if (!isClicked){

                binding.addCategoriesLayout.visibility = View.VISIBLE
                binding.allLayout.visibility = View.VISIBLE
                binding.paymentsLayout.visibility = View.GONE
                isClicked = true


            }
        }
        binding.addButton2.setOnClickListener {
//            binding.addButton.animate().rotation(45f).duration = 400
            binding.addCategoriesLayout.visibility = View.GONE
            isClicked=false
            binding.allLayout.visibility = View.VISIBLE
            binding.paymentsLayout.visibility = View.GONE
        }
        binding.addCategoriesLayout.setOnClickListener {
//            binding.addButton.animate().rotation(-90f).duration = 400
            binding.addCategoriesLayout.visibility = View.GONE
            isClicked= false
            binding.allLayout.visibility = View.VISIBLE
            binding.paymentsLayout.visibility = View.GONE
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

        try {


//        binding.homeIcon.layoutParams.height  = 75
//        binding.homeIcon.layoutParams.width  = 75
//        binding.starIcon.layoutParams.height  = 75
//        binding.starIcon.layoutParams.width  = 75
//        binding.profileIcon.layoutParams.height  = 65
//        binding.profileIcon.layoutParams.width  = 65
//        binding.searchImage.layoutParams.width = 75
//        binding.searchImage.layoutParams.height = 75
        binding.starIcon.setBackgroundResource(wR.drawable.start_icon_unselected)
        binding.homeIcon.setBackgroundResource(wR.drawable.ic_baseline_home_24)
        binding.profileIcon.setBackgroundResource(wR.drawable.ic_outline_settings_unselected)



        binding.favoritesCard.setOnClickListener {

            binding.starIcon.setBackgroundResource(wR.drawable.star_selected_icon)
            binding.homeIcon.setBackgroundResource(wR.drawable.ic_outline_home_24)
            binding.profileIcon.setBackgroundResource(wR.drawable.ic_outline_settings_unselected)
            binding.searchImage.setBackgroundResource(wR.drawable.ic_baseline_search_icon)

//            val transaction: FragmentTransaction = manager.beginTransaction()
//            transaction.replace(wR.id.fragment_layout, FavouriteItemsFragment() , "fav_items_fragment").addToBackStack(null)
//            transaction.commit()
            if (supportFragmentManager.findFragmentByTag("fav_items_fragment") == null || !supportFragmentManager.findFragmentByTag("fav_items_fragment")?.isVisible!!) {
                supportFragmentManager.beginTransaction().apply {
                    replace(wR.id.fragment_layout, FavouriteItemsFragment(), "fav_items_fragment")
                    commit()
                }
            }
        }
        binding.homeCard.setOnClickListener {

            binding.starIcon.setBackgroundResource(wR.drawable.start_icon_unselected)
            binding.homeIcon.setBackgroundResource(wR.drawable.ic_baseline_home_24)
            binding.profileIcon.setBackgroundResource(wR.drawable.ic_outline_settings_unselected)
            binding.searchImage.setBackgroundResource(wR.drawable.ic_baseline_search_icon)

////            if (supportFragmentManager.findFragmentByTag("home_fragment") == null && !supportFragmentManager.findFragmentByTag("home_fragment")?.isVisible!!){
//                val transaction: FragmentTransaction = manager.beginTransaction()
//                transaction.replace(wR.id.fragment_layout, HomeFragment() , "home_fragment")
//                transaction.commit()
////            }
            if (supportFragmentManager.findFragmentByTag("home_fragment") == null || !supportFragmentManager.findFragmentByTag("home_fragment")?.isVisible!!) {
                supportFragmentManager.beginTransaction().apply {
                    replace(wR.id.fragment_layout, HomeFragment(), "home_fragment")
                    commit()
                }
            }

        }

        binding.searchCard.setOnClickListener {

            binding.starIcon.setBackgroundResource(wR.drawable.start_icon_unselected)
            binding.homeIcon.setBackgroundResource(wR.drawable.ic_outline_home_24)
            binding.searchImage.setBackgroundResource(wR.drawable.ic_baseline_search_icon_selected)
            binding.profileIcon.setBackgroundResource(wR.drawable.ic_outline_settings_unselected)
//            val transaction: FragmentTransaction = manager.beginTransaction()
//            transaction.replace(wR.id.fragment_layout, SearchFragment() , "search_fragment")
//            transaction.commit()
            if (supportFragmentManager.findFragmentByTag("search_fragment") == null || !supportFragmentManager.findFragmentByTag("search_fragment")?.isVisible!! ) {
                supportFragmentManager.beginTransaction().apply {
                    replace(wR.id.fragment_layout, SearchFragment(), "search_fragment")
                    commit()
                }
            }
        }

         binding.profileCard.setOnClickListener {
             binding.starIcon.setBackgroundResource(wR.drawable.start_icon_unselected)
             binding.homeIcon.setBackgroundResource(wR.drawable.ic_outline_home_24)
             binding.profileIcon.setBackgroundResource(wR.drawable.ic_baseline_settings_selected)
             binding.searchImage.setBackgroundResource(wR.drawable.ic_baseline_search_icon)
//             val transaction: FragmentTransaction = manager.beginTransaction()
//             transaction.replace(wR.id.fragment_layout, SettingsFragment() , "settings_fragment").addToBackStack(null)
//             transaction.commit()
             if (supportFragmentManager.findFragmentByTag("settings_fragment") == null || !supportFragmentManager.findFragmentByTag("settings_fragment")?.isVisible!!) {
                 supportFragmentManager.beginTransaction().apply {
                     replace(wR.id.fragment_layout, SettingsFragment(), "settings_fragment")
                     commit()

                 }

             }
        }

        }
        catch (e:Exception){
            Log.e("TAG", e.toString())
        }
    }




}



