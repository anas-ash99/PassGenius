package com.example.passgenius.ui.ItemPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passgenius.R
import com.example.passgenius.common.AddItemPageType
import com.example.passgenius.common.DataHolder.allItems
import com.example.passgenius.common.DataHolder.favouriteItemsIds
import com.example.passgenius.common.KeyboardManger.hideSoftKeyboard
import com.example.passgenius.common.extension_methods.ItemListMethods.updateOrAddLogInItem
import com.example.passgenius.common.extension_methods.ItemListMethods.updateOrAddNoteItem
import com.example.passgenius.data.dataStates.DataState

import com.example.passgenius.databinding.FragmentItemBinding
import com.example.passgenius.domain.viewModels.ItemPageViewModel
import com.example.passgenius.ui.adapters.ItemEditTextAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class ItemFragment : Fragment() {


    private lateinit var binding:FragmentItemBinding
    private lateinit var viewModel: ItemPageViewModel
    private lateinit var adapter: ItemEditTextAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item, container, false)
        viewModel = ViewModelProvider(requireActivity())[ItemPageViewModel::class.java]
        binding.viewModel = viewModel
        observePageType()
        observeSaveItem()
        observeValidationError()
        observeIsFavourite()
        println(favouriteItemsIds)
        return binding.root

    }

    private fun observeIsFavourite() {
        viewModel.isFavourite.observe(requireActivity()){
            if (it){
                binding.startImage.setBackgroundResource(R.drawable.star_fav_icon)
            }else{
                binding.startImage.setBackgroundResource(R.drawable.ic_baseline_star_outline_24)
            }
        }
    }


    private fun observeValidationError() {
        viewModel.validationError.observe(requireActivity()){
            if (it.isNotEmpty()){
                it.onEach { pos->
                    adapter.notifyItemChanged(pos)
                }
            }


        }
    }

    private fun observeSaveItem() {
           viewModel.saveLoginItemResponse.observe(requireActivity()){
               when(it){
                   is DataState.Error -> {
                       binding.progressBarLayout.visibility = View.GONE
                       Toast.makeText(requireContext(), "Couldn't save item", Toast.LENGTH_SHORT).show()
                   }
                   DataState.Loading -> {
                       hideSoftKeyboard(binding.editPageView, requireContext())
                       binding.progressBarLayout.visibility = View.VISIBLE
                   }
                   is DataState.Success -> {
                       binding.progressBarLayout.visibility = View.GONE
                       viewModel.item?.loginItem = it.data
                       viewModel.pageType.value = AddItemPageType.VIEW_PAGE
                       allItems.updateOrAddLogInItem(it.data)
                   }
               }
           }

        viewModel.saveNoteItemResponse.observe(requireActivity()){
            when(it){
                is DataState.Error -> {
                    binding.progressBarLayout.visibility = View.GONE
                    Toast.makeText(requireContext(), "Couldn't save item", Toast.LENGTH_SHORT).show()
                }
                DataState.Loading -> {
                    hideSoftKeyboard(binding.editPageView, requireContext())
                    binding.progressBarLayout.visibility = View.VISIBLE
                }
                is DataState.Success -> {

                    binding.progressBarLayout.visibility = View.GONE
                    viewModel.item?.noteItem = it.data
                    viewModel.pageType.value = AddItemPageType.VIEW_PAGE
                    allItems.updateOrAddNoteItem(it.data)

                }
            }
        }
    }
    private fun observePageType() {
        viewModel.pageType.observe(requireActivity()){
            binding.pageType = it
            initRecyclerView()
        }
    }

    private fun initRecyclerView(){
        adapter = ItemEditTextAdapter(requireContext(),viewModel.getItemsForList(),viewModel.pageType.value!!, viewModel)
        binding.editTextRv.adapter = adapter
        binding.editTextRv.layoutManager = LinearLayoutManager(requireContext())
    }

}