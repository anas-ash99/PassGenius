package com.example.passgenius.ui.adapters

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.passgenius.R
import com.example.passgenius.common.enums.AddItemType
import com.example.passgenius.common.enums.EditTextType
import com.example.passgenius.domain.models.EditTextItemModel
import com.example.passgenius.domain.viewModels.AddItemViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import okhttp3.internal.notifyAll

class ItemEditTextAdapter(
    private val context: Context,
    private val activity: ViewModelStoreOwner,
    private val lifecycleOwner: LifecycleOwner,
    private var itemsList: MutableList<EditTextItemModel>,
    private val pageType: AddItemType,
    private var isEditPage:Boolean = true

): RecyclerView.Adapter<ItemEditTextAdapter.MyViewHolder>() {

  private lateinit var viewModel: AddItemViewModel
  private var  isDoneClicked:MutableLiveData<Boolean> = MutableLiveData()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
    viewModel = ViewModelProvider(activity)[AddItemViewModel::class.java]


    return MyViewHolder (
      LayoutInflater.from(context).inflate(R.layout.item_edit_text, parent, false)
    )

  }

  fun onDoneClick(value:Boolean){
    isDoneClicked.value = value
  }

  private fun initViewItem(holder: MyViewHolder, position:Int) {
    val item: EditTextItemModel = itemsList[position]
    takeInput(holder.editText,item.name )
    holder.editText.inputType = item.type
    holder.textInputLayout.hint = item.name
    holder.editText.setText(item.value)
    changeUnderLineOnFocus(holder.editText,holder.underline)
    if (isEditPage){
      if (item.name == "Password"){
        holder.showPasswordIcon.visibility =View.VISIBLE
        holder.generateTV.visibility = View.VISIBLE
        onEyeIconClick(holder.showPasswordIcon, holder.editText)
      }

      holder.underline.visibility= View.VISIBLE
      holder.copyText.visibility = View.GONE

    }
    else if (!isEditPage){
      if (item.name == "Password"){
        holder.showPasswordIcon.visibility =View.VISIBLE
        onEyeIconClick(holder.showPasswordIcon, holder.editText)
      }
      holder.underline.visibility= View.GONE
      holder.copyText.visibility = View.VISIBLE
      if (holder.editText.text?.toString()?.trim()?.isEmpty()!!){
        holder.textInputLayout.visibility =View.GONE
        holder.copyText.visibility = View.GONE
      }
      holder.editText.focusable = View.NOT_FOCUSABLE



    }




  }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      val item: EditTextItemModel = itemsList[position]
      initViewItem(holder, position)

      isDoneClicked.observe(lifecycleOwner,Observer{

            if (it) {
              when (pageType) {
                AddItemType.LOGIN -> {
                  if (viewModel.loginItem.email == "" || viewModel.loginItem.email == "" || viewModel.loginItem.password == "") {
                    if (holder.editText.text?.toString()
                        ?.trim()!! == "" && (item.name == "Name" || item.name == "Email" || item.name == "Password")
                    ) {
                      holder.editText.error = "filed is required"
                    }
                  }
                }
                AddItemType.SECURE_NOTE->{
                  if (holder.editText.text?.toString()?.trim()?.isEmpty()!!){
                    holder.editText.error = "Filed is required"

                  }

                }

                else -> {}
              }
            }

          } )
      holder.copyText.setOnClickListener {
        var clipBoard: ClipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var clip: ClipData = ClipData.newPlainText("value",holder.editText.text?.toString()?.trim()!!)
        clipBoard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to clipboard.", Toast.LENGTH_SHORT).show()
      }





    }

  private fun takeInput(editText: EditText,name:String) {

    editText.addTextChangedListener {
      when(pageType){
        AddItemType.LOGIN -> {
          when(name){
            "Email" -> {
              viewModel.loginItem.email = it?.toString()?.trim()!!
            }
            "Password" -> {
              viewModel.loginItem.password = it?.toString()?.trim()!!
            }
            "Name" -> {
              viewModel.loginItem.itemName = it?.toString()?.trim()!!
            }
            "Note" -> {
              viewModel.loginItem.note = it?.toString()?.trim()!!
            }
          }
        }
        AddItemType.SECURE_NOTE ->{
            when(name){
              "Title" -> viewModel.noteItem.title = it?.toString()?.trim()!!
              "Content" -> viewModel.noteItem.content = it?.toString()?.trim()!!
            }

          }


        else -> {}
      }
    }


  }

  private fun onEyeIconClick(showPasswordIcon: ImageView, password:EditText) {
    var isClicked:Boolean = false
    showPasswordIcon.setOnClickListener {
      if (isClicked){
        password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        showPasswordIcon.setBackgroundResource(R.drawable.icon_show_eye)
        password.setSelection(password.text.length)
        isClicked = false
      }else{
        password.inputType = InputType.TYPE_CLASS_TEXT
        showPasswordIcon.setBackgroundResource(R.drawable.icon_hide_eye)
        password.setSelection(password.text.length)
        isClicked =true
      }
    }
  }

  private fun changeUnderLineOnFocus(et:EditText,line:View) {
    et.setOnFocusChangeListener { v, hasFocus ->
      if (hasFocus){
        line.setBackgroundResource(R.color.et_with_focus)
      }else{
        line.setBackgroundResource(R.color.et_no_focus)
      }
    }
  }

  override fun getItemCount(): Int {
    return itemsList.size
  }





  inner class MyViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
    val editText:TextInputEditText= itemView.findViewById(R.id.editText)
    val textInputLayout:TextInputLayout= itemView.findViewById(R.id.textInputLayout)
    val generateTV:TextView = itemView.findViewById(R.id.generateTV)
    val copyText:CardView = itemView.findViewById(R.id.copyText)
    val underline:View = itemView.findViewById(R.id.et_underline)
    val showPasswordIcon:ImageView = itemView.findViewById(R.id.showPasswordIcon)
  }
}
