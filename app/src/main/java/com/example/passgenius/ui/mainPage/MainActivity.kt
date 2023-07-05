package com.example.passgenius.ui.mainPage

import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.passgenius.common.AddItemPageType
import com.example.passgenius.common.Constants.ITEM_PAGE_REQUEST_CODE
import com.example.passgenius.common.DataHolder.allItems
import com.example.passgenius.common.PasswordGenerator
import com.example.passgenius.common.enums.AddItemType
import com.example.passgenius.common.enums.MainScreenTypes
import com.example.passgenius.databinding.ActivityMainBinding
import com.example.passgenius.domain.models.ItemListModel
import com.example.passgenius.domain.models.LoginItemModel
import com.example.passgenius.domain.models.SecureNoteModel

import com.example.passgenius.domain.viewModels.MainViewModel
import com.example.passgenius.ui.ItemPage.ItemActivity
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
        i = Intent(this, ItemActivity::class.java)
        onAddCategoriesClicks()
        onBottomNavClicks()
        checkAuthentication()
        itemDialog = ShowBottomDialog(this,viewModel::onUserAction)
        observeItemClick()


    }

    private fun observeItemClick() {
        viewModel.itemClick.observe(this){
            it?.let { item-> onItemClick(item) }
        }
    }



    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
          if (requestCode == ITEM_PAGE_REQUEST_CODE ){
              viewModel.allItems.value = allItems
              viewModel.currentCategory.value = viewModel.currentCategory.value

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


    @Deprecated("Deprecated in Java")
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

            beginActivity(AddItemType.LOGIN)
        }

        binding.secureNoteLayout.setOnClickListener {
            i.putExtra("item", SecureNoteModel())

           beginActivity(AddItemType.NOTE)
        }

        binding.addPaymentsCard.setOnClickListener {
            binding.allLayout.visibility = View.GONE
            binding.paymentsLayout.visibility = View.VISIBLE
        }

        binding.addBankCard.setOnClickListener {

        }

    }

    private fun beginActivity(itemType: AddItemType){
        viewModel.isExitingTheApp = false
        i.putExtra("itemType", itemType)
        i.putExtra("pageType", AddItemPageType.EDIT_PAGE)
        i.putExtra("item", ItemListModel("", ""))
        viewModel.mainActivityState.value = viewModel.mainActivityState.value?.copy(plusButton = false)
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



    private fun onItemClick(item: ItemListModel){
        val intent = Intent(this, ItemActivity::class.java)

        when(item.type) {

            "LOGIN" -> {
                intent.putExtra("itemType", AddItemType.LOGIN)
            }

            "NOTE"->{
                intent.putExtra("itemType", AddItemType.NOTE)
            }
        }
        viewModel.mainActivityState.value = viewModel.mainActivityState.value?.copy(plusButton = false)
        intent.putExtra("item", item)
        intent.putExtra("pageType", AddItemPageType.VIEW_PAGE)
        intent.putExtra("pageAction","update")
        startActivityForResult(intent, ITEM_PAGE_REQUEST_CODE)
        viewModel.itemClick.value = null
    }

}



