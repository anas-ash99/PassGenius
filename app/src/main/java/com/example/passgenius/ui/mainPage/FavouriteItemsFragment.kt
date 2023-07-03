package com.example.passgenius.ui.mainPage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passgenius.R
import com.example.passgenius.common.Constants
import com.example.passgenius.ui.adapters.PassItemAdapter
import com.example.passgenius.databinding.FragmentFavouriteItemsBinding
import com.example.passgenius.domain.models.ItemListModel
import com.example.passgenius.domain.viewModels.MainViewModel
import com.example.passgenius.ui.ItemPage.AddNewItemActivity


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

    private fun initRecyclerView() {
        val byName = Comparator.comparing { obj: ItemListModel -> obj.name.lowercase() }
        viewModel.favouriteItems.value?.sortWith(byName)
        adapter = PassItemAdapter(requireContext(),viewModel::onUserAction, viewModel.favouriteItems.value!!, ::onItemClick, isSearchPage = false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun onItemClick(item: ItemListModel, position:Int){
        val intent = Intent(requireContext(), AddNewItemActivity::class.java)
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

        requireActivity().startActivityForResult(intent, Constants.ITEM_PAGE_REQUEST_CODE)
    }


}