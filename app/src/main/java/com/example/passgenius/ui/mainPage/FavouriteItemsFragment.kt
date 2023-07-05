package com.example.passgenius.ui.mainPage


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
import com.example.passgenius.ui.adapters.PassItemAdapter
import com.example.passgenius.databinding.FragmentFavouriteItemsBinding
import com.example.passgenius.domain.models.ItemListModel
import com.example.passgenius.domain.viewModels.MainViewModel



class FavouriteItemsFragment : Fragment() {

    private lateinit var binding:FragmentFavouriteItemsBinding
    private lateinit var adapter: PassItemAdapter
    private lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favourite_items, container, false)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        return binding.root
    }

    override fun onStart() {
        viewModel.getFavoriteItems()
        if (viewModel.favouriteItems.value?.isNotEmpty()!!){
            binding.emptyListLayout.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            initRecyclerView()
        }else{
            binding.emptyListLayout.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        }
        super.onStart()
    }
    private fun initRecyclerView() {
        val byName = Comparator.comparing { obj: ItemListModel -> obj.name.lowercase() }
        viewModel.favouriteItems.value?.sortWith(byName)
        adapter = PassItemAdapter(requireContext(), viewModel.favouriteItems.value!!, isSearchPage = false, viewModel)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

}