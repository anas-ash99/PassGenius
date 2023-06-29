package com.example.passgenius.ui.mainPage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passgenius.R
import com.example.passgenius.common.CurrentCategory

import com.example.passgenius.ui.adapters.AdapterCategories
import com.example.passgenius.ui.adapters.PassItemAdapter

import com.example.passgenius.databinding.FragmentHomeBinding

import com.example.passgenius.domain.models.ItemListModel

import com.example.passgenius.domain.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
        observeItems()
        observeAllItems2()
        observeCurrentCategory()
        observeSate()
        return binding.root
    }

    private fun observeSate() {
        viewModel.homeState.observe(requireActivity()){
            binding.state = it
        }
    }


    private fun observeCurrentCategory(){
        viewModel.currentCategory.onEach {
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

                   if (viewModel.loginItemsRv.value == null || viewModel.loginItemsRv.value?.isEmpty()!!){
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = true)
                   }else{
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = false)
                       initRecyclerView(viewModel.loginItemsRv.value!!)

                   }


               }
               CurrentCategory.Notes.category-> {

                   if (viewModel.noteItemsRv.value == null || viewModel.noteItemsRv.value?.isEmpty()!!){
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = true)
                   }else{
                       initRecyclerView(viewModel.noteItemsRv.value!!)
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = false)

                   }

               }
               CurrentCategory.Identities.category ->{
                   if (viewModel.idsItemsRv.value == null || viewModel.idsItemsRv.value?.isEmpty()!!){
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = true)
                   }else{
                       initRecyclerView(viewModel.idsItemsRv.value!!)
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = false)

                   }


               }
               CurrentCategory.Payments.category->{
                   if (viewModel.paymentsItemsRv.value == null || viewModel.paymentsItemsRv.value?.isEmpty()!!){
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = true)
                   }else{
                       initRecyclerView(viewModel.paymentsItemsRv.value!!)
                       viewModel.homeState.value = viewModel.homeState.value?.copy(isListEmpty = false)

                   }

               }
           }

        }.launchIn(lifecycleScope)
    }

    private fun observeItems(){


    }


    private fun observeAllItems2(){


    }
    

   private fun initRecyclerView(list: MutableList<ItemListModel>){

       try {
           val byName = Comparator.comparing { obj: ItemListModel -> obj.name.lowercase() }
           list.sortWith(byName)
           itemsAdapter = PassItemAdapter(requireContext(), list, requireActivity(), isSearchPage = false, viewModelStoreOwner = requireActivity() )
           binding.itemsRecyclerView.adapter = itemsAdapter
           binding.itemsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

       }catch (e:Exception){
           Log.e("error", e.toString())
       }

   }
    private fun initCategoriesRecyclerView() {
        categoriesAdapter = AdapterCategories(requireContext(), viewModel::onUserAction,viewModel.categoriesList)
        binding.categoriesRecyclerView.adapter = categoriesAdapter
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false )

    }




}