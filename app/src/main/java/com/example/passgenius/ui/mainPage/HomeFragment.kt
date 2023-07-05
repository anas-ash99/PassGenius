package com.example.passgenius.ui.mainPage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passgenius.R
import com.example.passgenius.common.CurrentCategory

import com.example.passgenius.ui.adapters.AdapterCategories
import com.example.passgenius.ui.adapters.PassItemAdapter

import com.example.passgenius.databinding.FragmentHomeBinding

import com.example.passgenius.domain.models.ItemListModel

import com.example.passgenius.domain.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

import kotlin.Comparator

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var categoriesAdapter: AdapterCategories
    private lateinit var binding: FragmentHomeBinding
    private lateinit var itemsAdapter: PassItemAdapter
    private lateinit var viewModel :MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        initCategoriesRecyclerView()
        observeCurrentCategory()
        observeSate()
        observeDeleteItem()


        return binding.root
    }



    private fun observeDeleteItem() {
        viewModel.deleteItem.observe(requireActivity()){

            if (it){
                viewModel.mainActivityState.value?.currentShownDialogItemPosition?.let { pos->

                    itemsAdapter.notifyDeleteItem(pos)
                }
                viewModel.deleteItem.value = false
                viewModel.mainActivityState.value = viewModel.mainActivityState.value?.copy(
                    isBottomDialogShown = false, currentShownDialogItemPosition = null, currentShownDialogItem = null)

            }
        }
    }

    private fun observeSate() {
        viewModel.homeState.observe(requireActivity()){
            binding.state = it
        }
    }


    private fun observeCurrentCategory(){
        viewModel.currentCategory.observe(requireActivity()) {
            viewModel.homeState.value = viewModel.homeState.value?.copy(currentCategory = it)
           when(it){
               CurrentCategory.All.category -> {

                   if (viewModel.allItems.value?.isEmpty()!!){
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = true )
                   }else{
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = false)
                       initRecyclerView(viewModel.allItems.value!!)
                   }

               }
               CurrentCategory.Logins.category-> {

                   if (viewModel.loginItems.value == null || viewModel.loginItems.value?.isEmpty()!!){
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = true)
                   }else{
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = false)
                       initRecyclerView(viewModel.loginItems.value!!)
                   }


               }
               CurrentCategory.Notes.category-> {

                   if (viewModel.noteItems.value == null || viewModel.noteItems.value?.isEmpty()!!){
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = true)
                   }else{
                       initRecyclerView(viewModel.noteItems.value!!)
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = false)

                   }

               }
               CurrentCategory.Identities.category ->{
                   if (viewModel.idsItems.value == null || viewModel.idsItems.value?.isEmpty()!!){
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = true)
                   }else{
                       initRecyclerView(viewModel.idsItems.value!!)
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = false)

                   }


               }
               CurrentCategory.Payments.category->{
                   if (viewModel.paymentsItems.value == null || viewModel.paymentsItems.value?.isEmpty()!!){
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = true)
                   }else{
                       initRecyclerView(viewModel.paymentsItems.value!!)
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = false)

                   }

               }
           }

        }
    }


   private fun initRecyclerView(list: MutableList<ItemListModel>){

       try {
           val byName = Comparator.comparing { obj: ItemListModel -> obj.name.lowercase() }
           list.sortWith(byName)
           itemsAdapter = PassItemAdapter(requireContext(),list, isSearchPage = false, viewModel )
           binding.itemsRecyclerView.adapter = itemsAdapter
           binding.itemsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

       }catch (e:Exception){
           Log.e("error", e.message,e)
       }

   }
    private fun initCategoriesRecyclerView() {
        categoriesAdapter = AdapterCategories(requireContext(), viewModel::onUserAction,viewModel.categoriesList)
        binding.categoriesRecyclerView.adapter = categoriesAdapter
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false )
    }

}