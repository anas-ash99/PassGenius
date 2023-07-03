package com.example.passgenius.ui.mainPage

import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.passgenius.common.Constants
import com.example.passgenius.common.Constants.ITEM_PAGE_REQUEST_CODE
import com.example.passgenius.common.CurrentCategory
import com.example.passgenius.common.DataHolder

import com.example.passgenius.common.enums.MainScreenTypes
import com.example.passgenius.databinding.ActivityMainBinding
import com.example.passgenius.domain.models.ItemListModel
import com.example.passgenius.domain.models.LoginItemModel
import com.example.passgenius.domain.models.SecureNoteModel

import com.example.passgenius.domain.viewModels.MainViewModel
import com.example.passgenius.ui.ItemPage.AddNewItemActivity
import com.example.passgenius.ui.authenticationPage.AuthenticationActivity
import com.example.passgenius.ui.dialogs.ShowBottomDialog
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

import com.example.passgenius.R as wR


@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA", "DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {





    lateinit var binding:ActivityMainBinding
    private var isClicked = false
    private lateinit var i:Intent
    private lateinit var  viewModel: MainViewModel
    private lateinit var itemDialog: ShowBottomDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = resources.getColor(wR.color.main_activity_background_color)
        binding = DataBindingUtil.setContentView(this, wR.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.initViewModel()
        observeState()
        binding.viewModel = viewModel
        i = Intent(this, AddNewItemActivity::class.java)
        onAddCategoriesClicks()
        onBottomNavClicks()
        checkAuthentication()
        itemDialog = ShowBottomDialog(this,viewModel::onUserAction)


    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
          if (requestCode == ITEM_PAGE_REQUEST_CODE ){
              if (data?.getStringExtra("action") == "update"){

                  val item = data.getSerializableExtra("item") as ItemListModel
                  viewModel.allItems.value?.removeAt(viewModel.mainActivityState.value?.currentShownDialogItemPosition!!)
                  viewModel.allItems.value!!.add(viewModel.mainActivityState.value?.currentShownDialogItemPosition!!, item)
                  viewModel.allItems.value?.onEach {
                      println("name: " + it.name)
                  }

                  viewModel.currentCategory.value = CurrentCategory.All.category
                  viewModel.allItems.value = DataHolder.allItems
                  viewModel.setOtherItems()



              }

          }

    }
    private fun observeState(){
        viewModel.mainActivityState.observe(this){
            binding.state = it
            if (it.isBottomDialogShown){
                itemDialog.showDialog(viewModel.mainActivityState.value?.currentShownDialogItem!! )
            }
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


    override fun onPause() {
        if (viewModel.isExitingTheApp){
            viewModel.loggedInUser?.lastSeen = LocalDateTime.now().toString()
            viewModel.updateLoggedInUser()

        }
        super.onPause()
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
        i.putExtra("pageAction","addNew")
//        viewModel.mainActivityState.value = viewModel.mainActivityState.value?.currentShownDialogItemPosition
        startActivityForResult(i,ITEM_PAGE_REQUEST_CODE )
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


    private fun onItemClick(item: ItemListModel, position:Int){
        val intent = Intent(this, AddNewItemActivity::class.java)
        when(item.type) {

            "LOGIN" -> {
                intent.putExtra("pageType", "LOGIN")
                intent.putExtra ("isEditPage", false)
                intent.putExtra("item", item.loginItem)
                intent.putExtra("itemList", item)
            }

            "NOTE"->{
                intent.putExtra("pageType", "NOTE")
                intent.putExtra("item", item.noteItem)
                intent.putExtra("itemList", item)

            }
        }


        viewModel.mainActivityState.value = viewModel.mainActivityState.value?.copy(currentShownDialogItemPosition = position)
        intent.putExtra ("isEditPage", false)
        intent.putExtra("pageAction","update")

        startActivityForResult(intent,ITEM_PAGE_REQUEST_CODE)
    }


}



