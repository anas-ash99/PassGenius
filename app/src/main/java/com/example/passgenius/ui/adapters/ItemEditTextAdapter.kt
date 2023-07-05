package com.example.passgenius.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.passgenius.common.AddItemPageType
import com.example.passgenius.common.PasswordGenerator
import com.example.passgenius.common.extension_methods.ExtinctionFunctions.copyText
import com.example.passgenius.databinding.ItemEditTextBinding
import com.example.passgenius.domain.models.EditTextItemModel
import com.example.passgenius.domain.viewModels.ItemPageViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ItemEditTextAdapter(
  private val context: Context,
  private var itemsList: MutableList<EditTextItemModel>,
  private val pageType: AddItemPageType,
  private val viewModel: ItemPageViewModel

): RecyclerView.Adapter<ItemEditTextAdapter.MyViewHolder>() {

//  private lateinit var viewModel: AddItemViewModel
  private var  isDoneClicked:MutableLiveData<Boolean> = MutableLiveData()


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
     val inflater = LayoutInflater.from(context)
    return MyViewHolder (
      ItemEditTextBinding.inflate(inflater, parent, false)
    )

  }

  fun onDoneClick(value:Boolean){
    isDoneClicked.value = value
  }



//  private fun initViewItem(holder: MyViewHolder, position:Int) {
//      val item: EditTextItemModel = itemsList[position]
//      takeInput(holder.editText,item.name )
//      holder.editText.inputType = item.type
//      holder.textInputLayout.hint = item.name
//      holder.editText.setText(item.value)
//      changeUnderLineOnFocus(holder.editText,holder.underline)
//      if (isEditPage){
//        if (item.name == "Password"){
//          holder.showPasswordIcon.visibility =View.VISIBLE
//          holder.generateTV.visibility = View.VISIBLE
//          onEyeIconClick(holder.showPasswordIcon, holder.editText)
//        }
//
//        holder.underline.visibility= View.VISIBLE
//        holder.copyText.visibility = View.GONE
//
//      }
//      else if (!isEditPage){
//        if (item.name == "Password"){
//          holder.showPasswordIcon.visibility =View.VISIBLE
//          onEyeIconClick(holder.showPasswordIcon, holder.editText)
//        }
//        holder.underline.visibility= View.GONE
//        holder.copyText.visibility = View.VISIBLE
//        if (holder.editText.text?.toString()?.trim()?.isEmpty()!!){
//          holder.textInputLayout.visibility =View.GONE
//          holder.copyText.visibility = View.GONE
//        }
//        holder.editText.focusable = View.NOT_FOCUSABLE
//
//
//
//      }
//
//
//
//
//  }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      val item: EditTextItemModel = itemsList[position]
      holder.bind(item, position)
//      initViewItem(holder, position)


//      isDoneClicked.observe(lifecycleOwner){
//
//            if (it) {
//              when (itemType) {
//                AddItemType.LOGIN -> {
//                  if (viewModel.loginItem.email == "" || viewModel.loginItem.email == "" || viewModel.loginItem.password == "") {
//                    if (holder.editText.text?.toString()
//                        ?.trim()!! == "" && (item.name == "Name" || item.name == "Email" || item.name == "Password")
//                    ) {
//                      holder.editText.error = "filed is required"
//                    }
//                  }
//                }
//                AddItemType.SECURE_NOTE->{
//                  if (holder.editText.text?.toString()?.trim()?.isEmpty()!!){
//                    holder.editText.error = "Filed is required"
//
//                  }
//
//                }
//
//                else -> {}
//              }
//            }
//
//          }
//      holder.copyText.setOnClickListener {
//        val clipBoard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//        val clip: ClipData = ClipData.newPlainText("value",holder.editText.text?.toString()?.trim()!!)
//        clipBoard.setPrimaryClip(clip)
//        Toast.makeText(context, "Copied to clipboard.", Toast.LENGTH_SHORT).show()
//      }





    }



  override fun getItemCount(): Int {
    return itemsList.size
  }





  inner class MyViewHolder(val binding:ItemEditTextBinding): RecyclerView.ViewHolder(binding.root){
     @OptIn(ExperimentalCoroutinesApi::class)
     fun bind(item:EditTextItemModel, position: Int){
       binding.item = item
       binding.pageType = pageType
       binding.viewModel = viewModel

       binding.generateTV.setOnClickListener {
         item.value = PasswordGenerator.generatePassword()
         notifyItemChanged(position)
       }
       binding.copyText.setOnClickListener {
           item.value.copyText(context)
       }
       if (!item.isValid){
         binding.editText.error = "error"
       }
       binding.textInputLayout.isPasswordVisibilityToggleEnabled = item.name == "Password"
     }

  }
}
