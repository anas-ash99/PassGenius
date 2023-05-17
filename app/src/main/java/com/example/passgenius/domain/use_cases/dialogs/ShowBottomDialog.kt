package com.example.passgenius.domain.use_cases.dialogs

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.passgenius.common.DeleteClickInterface
import com.example.passgenius.R
import com.example.passgenius.common.enums.AddItemType
import com.example.passgenius.common.enums.BottomDialogActionType
import com.example.passgenius.common.extension_methods.StringMethods.shortName
import com.example.passgenius.domain.models.LoginItemModel
import com.example.passgenius.domain.models.SecureNoteModel
import de.hdodenhof.circleimageview.CircleImageView

open class ShowBottomDialog(
    var context: Context,
    var itemType: AddItemType,
    var lifecycleOwner: LifecycleOwner? = null,
    var loginItem: LoginItemModel = LoginItemModel(),
    var noteItem: SecureNoteModel = SecureNoteModel(),
    deleteInterface: DeleteClickInterface
){
    var dialog:Dialog = Dialog(context)
    val actionType:MutableLiveData<BottomDialogActionType>  =MutableLiveData()
//    private val deleteDialogClass:CenterDialog = CenterDialog(context,itemType, deleteInterface = deleteInterface)
    private var  itemName:TextView
    private var  itemImage:CircleImageView
    private var  logoText:TextView
    private var deleteCard:LinearLayout
    private var layout1:LinearLayout
    private var layout2:LinearLayout
    private var layout3:LinearLayout
    private var logoTextCard: CardView
    private var clipBoard: ClipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    init {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_pass_item)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.window?.setGravity(Gravity.BOTTOM)
        itemName= dialog.findViewById(R.id.itemName)
        itemImage = dialog.findViewById(R.id.itemImage)
        logoText = dialog.findViewById(R.id.LogoText)
        deleteCard = dialog.findViewById(R.id.deleteCard)
        layout1 = dialog.findViewById(R.id.layout1)
        layout2= dialog.findViewById(R.id.layout2)
        layout3 = dialog.findViewById(R.id.layout3)
        logoTextCard = dialog.findViewById(R.id.LogoTextCard)


    }


    fun showDialog(position: Int, deleteInterface: DeleteClickInterface){
        when(itemType){
            AddItemType.LOGIN->{

//                deleteDialogClass.loginItemModel = loginItem!!
                layout3.visibility = View.VISIBLE

                setHeaderValues(loginItem)
                deleteCard.setOnClickListener {
//                    actionType?.value = BottomDialogActionType("DELETE",position)
                    dialog.hide()
//                    deleteDialogClass.itemRVPosition = position
//                    deleteDialogClass.dialog.show()
                    observeDialogDeleteClick(position, deleteInterface)
                }

                layout3.setOnClickListener {
                    var clip: ClipData = ClipData.newPlainText("username",loginItem.password )
                    clipBoard.setPrimaryClip(clip)
                    Toast.makeText(context, "Copied to clipboard.", Toast.LENGTH_SHORT).show()
                }

                layout1.setOnClickListener {
                    var clip: ClipData = ClipData.newPlainText("email",loginItem.email )
                    clipBoard.setPrimaryClip(clip)
                    Toast.makeText(context, "Copied to clipboard.", Toast.LENGTH_SHORT).show()
                }
                layout2.setOnClickListener {
                    var clip: ClipData = ClipData.newPlainText("name",loginItem.username )
                    clipBoard.setPrimaryClip(clip)
                    Toast.makeText(context, "Copied to clipboard.", Toast.LENGTH_SHORT).show()
                }
            }
            AddItemType.SECURE_NOTE ->{



            }
            else->{

            }
        }

        dialog.show()
    }

    private fun setHeaderValues(item: LoginItemModel) {

//            itemImage.visibility = View.VISIBLE
//            logoTextCard.visibility = View.GONE
////            Glide.with(context).load(item.imageUrl).into(itemImage)
            itemImage.visibility = View.GONE
            logoTextCard.visibility = View.VISIBLE
            logoText.text = item.itemName.shortName()
            itemName.text = item.itemName

    }

    private fun observeDialogDeleteClick(position: Int, clickInterface: DeleteClickInterface) {
//        deleteDialogClass?.deleteClick?.observe(lifecycleOwner!!, Observer {
//            if (it){
////                actionType?.value = BottomDialogActionType("DELETE",position)
////                clickInterface.onDeleteClick(position)
//            }
//        })
    }

    }