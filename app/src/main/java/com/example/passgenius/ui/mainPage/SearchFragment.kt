package com.example.passgenius.ui.mainPage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passgenius.R
import com.example.passgenius.common.Constants
import com.example.passgenius.ui.adapters.PassItemAdapter
import com.example.passgenius.databinding.FragmentSearchItemsBinding
import com.example.passgenius.domain.models.ItemListModel
import com.example.passgenius.domain.viewModels.MainViewModel
import com.example.passgenius.ui.ItemPage.ItemActivity


class SearchFragment : Fragment() {

     private lateinit var binding: FragmentSearchItemsBinding
     private lateinit var viewModel: MainViewModel
     private var  items:MutableList<ItemListModel> = mutableListOf()
     private lateinit var adapter: PassItemAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search_items,container, false)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        adapter = PassItemAdapter(requireContext(),  items, isSearchPage = true, viewModel)
        observeEditText()
        onArrowBackClick()
        return binding.root
    }

    private fun onArrowBackClick() {
        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

      private fun initRecyclerView(){
          adapter = PassItemAdapter(requireContext(), items,isSearchPage = true, viewModel)
          binding.recyclerView.adapter = adapter
          binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
      }

    private fun observeEditText() {
        binding.editText.addTextChangedListener {
            if (it?.toString()?.trim()?.length!! > 0){
                binding.XButton.visibility = View.VISIBLE

                handleSearch(it.toString()?.trim()!!)
                binding.recyclerView.visibility = View.VISIBLE
                binding.startLayout.visibility = View.GONE
            }else{
                binding.XButton.visibility = View.GONE
                items = mutableListOf()
                initRecyclerView()

                binding.recyclerView.visibility = View.GONE
                binding.startLayout.visibility = View.VISIBLE
            }

        }
        binding.XButton.setOnClickListener {
            binding.editText.setText("")
        }
    }


    private fun handleSearch(word:String){

        items = viewModel.allItems.value?.filter { item->
            val itemName = item.name.lowercase().filter { !it.isWhitespace() }
            itemName.contains(word.lowercase().filter { !it.isWhitespace() })
        } as MutableList<ItemListModel>
        initRecyclerView()


    }


}