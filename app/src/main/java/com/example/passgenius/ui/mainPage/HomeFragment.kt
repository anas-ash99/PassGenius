package com.example.passgenius.ui.mainPage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passgenius.R
import com.example.passgenius.common.DataHolder
import com.example.passgenius.ui.adapters.AdapterCategories
import com.example.passgenius.ui.adapters.PassItemAdapter
import com.example.passgenius.data.dataStates.DataState
import com.example.passgenius.databinding.FragmentHomeBinding
import com.example.passgenius.domain.interfaces.OnCategoryRVClicks
import com.example.passgenius.domain.models.ItemListModel
import com.example.passgenius.data.room.daos.LoginItemDao
import com.example.passgenius.data.room.daos.NoteItemDao
import com.example.passgenius.domain.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.Comparator

@AndroidEntryPoint
class HomeFragment : Fragment(), OnCategoryRVClicks {

    private lateinit var categoriesAdapter: AdapterCategories
    private lateinit var binding: FragmentHomeBinding
    private lateinit var itemsAdapter: PassItemAdapter
    private var currentCategory:String = "All"
    private lateinit var viewModel: MainViewModel
    @Inject
    lateinit var loginDao: LoginItemDao
    @Inject
    lateinit var noteDao: NoteItemDao
    private var selectedPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        initCategoriesRecyclerView()
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        observeItems()
        observeAllItems2()

        return binding.root
    }


    private fun observeItems(){

        viewModel._allItems.observe(requireActivity(), Observer {
            when(it){
                is DataState.Success<MutableList<ItemListModel>> ->{
                    viewModel.allItems.value = it.data
                    DataHolder.allItems.value = it.data
                    when (selectedPosition) {
                        0 -> { onCategoryClick("All") }
                        1 -> { onCategoryClick("Logins")}
                        2 -> { onCategoryClick("Notes") }
                        3 -> onCategoryClick("Identities")
                        4 -> onCategoryClick("Payments")
                    }
                    viewModel.isLoadingDone = true
                    binding.progressBar.visibility = View.GONE
                    binding.mainLayout.visibility =View.VISIBLE

                }
                is DataState.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE

                }

                is DataState.Error ->{
                    Toast.makeText(requireContext(), "Something went wrong" , Toast.LENGTH_SHORT).show()
                    viewModel.isLoadingDone = true
                    viewModel.getItemsFromLocalDB()
                    binding.progressBar.visibility = View.GONE
                }
                else -> {}
            }
        })


    }

    override fun onStart() {
        super.onStart()

    }


    private fun observeAllItems2(){

        viewModel.allItems.observe(requireActivity(), Observer {
            if (viewModel.isLoadingDone && it.isNotEmpty()){
                when (selectedPosition) {
                    0 -> { onCategoryClick("All") }
                    1 -> { onCategoryClick("Logins")}
                    2 -> { onCategoryClick("Notes") }
                    3 -> onCategoryClick("Identities")
                    4 -> onCategoryClick("Payments")
                }
            }
        })


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


        categoriesAdapter = AdapterCategories(requireContext(),this)
        binding.categoriesRecyclerView.adapter = categoriesAdapter
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false )
//        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onCategoryClick(category:String) {
        binding.headerText.text = category
        currentCategory = category
        when(category){
            "All"-> {
                if (viewModel.allItems.value == null || viewModel.allItems.value?.isEmpty()!!){
                    binding.emptyListLayout.visibility = View.VISIBLE
                    binding.itemsRecyclerView.visibility = View.GONE
                }else{
                    initRecyclerView(DataHolder.allItems.value!!)

                    binding.emptyListLayout.visibility = View.GONE
                    binding.itemsRecyclerView.visibility = View.VISIBLE
                }
                selectedPosition = 0

            }
            "Logins"-> {

                if (viewModel.loginItemsRv.value == null || viewModel.loginItemsRv.value?.isEmpty()!!){
                    binding.emptyListLayout.visibility = View.VISIBLE
                    binding.itemsRecyclerView.visibility = View.GONE
                }else{
                    initRecyclerView(viewModel.loginItemsRv.value!!)

                    binding.emptyListLayout.visibility = View.GONE
                    binding.itemsRecyclerView.visibility = View.VISIBLE
                }
                selectedPosition =1

            }
            "Notes"-> {

                if (viewModel.noteItemsRv.value == null || viewModel.noteItemsRv.value?.isEmpty()!!){
                    binding.emptyListLayout.visibility = View.VISIBLE
                    binding.itemsRecyclerView.visibility = View.GONE
                }else{
                    initRecyclerView(viewModel.noteItemsRv.value!!)

                    binding.emptyListLayout.visibility = View.GONE
                    binding.itemsRecyclerView.visibility = View.VISIBLE
                }
                selectedPosition = 2
            }

            "Identities" ->{
                if (viewModel.idsItemsRv.value == null || viewModel.idsItemsRv.value?.isEmpty()!!){
                    binding.emptyListLayout.visibility = View.VISIBLE
                    binding.itemsRecyclerView.visibility = View.GONE
                }else{
                    initRecyclerView(viewModel.idsItemsRv.value!!)

                    binding.emptyListLayout.visibility = View.GONE
                    binding.itemsRecyclerView.visibility = View.VISIBLE
                }
                selectedPosition = 3

            }

            "Payments" ->{
                if (viewModel.paymentsItemsRv.value == null || viewModel.paymentsItemsRv.value?.isEmpty()!!){
                    binding.emptyListLayout.visibility = View.VISIBLE
                    binding.itemsRecyclerView.visibility = View.GONE
                }else{
                    initRecyclerView(viewModel.paymentsItemsRv.value!!)

                    binding.emptyListLayout.visibility = View.GONE
                    binding.itemsRecyclerView.visibility = View.VISIBLE
                }
                selectedPosition = 3
            }



        }
    }


}