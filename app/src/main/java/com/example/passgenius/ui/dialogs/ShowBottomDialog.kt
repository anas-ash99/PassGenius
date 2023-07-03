package com.example.passgenius.ui.dialogs

import android.annotation.SuppressLint
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
import com.example.passgenius.R
import com.example.passgenius.common.UserActions
import com.example.passgenius.common.extension_methods.StringMethods.shortName
import com.example.passgenius.domain.models.ItemListModel
import com.example.passgenius.domain.models.LoginItemModel
import com.example.passgenius.domain.models.SecureNoteModel
import de.hdodenhof.circleimageview.CircleImageView

open class ShowBottomDialog(
    var context: Context,
    val userActions: (UserActions)->Unit,
){

    var loginItem: LoginItemModel = LoginItemModel()
    var noteItem: SecureNoteModel = SecureNoteModel()
    var dialog:Dialog = Dialog(context)
    private var  itemName:TextView
    private var  itemImage:CircleImageView
    private var  logoText:TextView
    private var  text1:TextView
    private var  text2:TextView
    private var  text3:TextView
    private var deleteCard:LinearLayout
    private var layout1:LinearLayout
    private var layout2:LinearLayout
    private var layout3:LinearLayout
    private var logoTextCard: CardView
    private var clipBoard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_pass_item)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        itemName= dialog.findViewById(R.id.itemName)
        itemImage = dialog.findViewById(R.id.itemImage)
        logoText = dialog.findViewById(R.id.LogoText)
        deleteCard = dialog.findViewById(R.id.deleteCard)
        layout1 = dialog.findViewById(R.id.layout1)
        layout2= dialog.findViewById(R.id.layout2)
        layout3 = dialog.findViewById(R.id.layout3)
        text1 = dialog.findViewById(R.id.text1)
        text2 = dialog.findViewById(R.id.text2)
        text3 = dialog.findViewById(R.id.text3)
        logoTextCard = dialog.findViewById(R.id.LogoTextCard)

        onDialogDismiss()

    }





    @SuppressLint("SetTextI18n")
    fun showDialog(item:ItemListModel){

        setHeaderValues(item.name)
        when(item.type){
            "LOGIN" ->{
                loginItem = item.loginItem
                layout3.visibility = View.VISIBLE

                layout1.setOnClickListener {

                    copyToClipboard("email", item.loginItem.email)

                }

                layout2.setOnClickListener {
                    copyToClipboard("name", item.loginItem.username)

                }
                layout3.setOnClickListener {
                    copyToClipboard("password", item.loginItem.password)
                }
            }
            "NOTE"->{
                layout3.visibility = View.GONE
                text1.text = "Copy Title"
                text2.text = "Copy content"
                layout1.setOnClickListener {
                    copyToClipboard("title", item.noteItem.title)
                }

                layout2.setOnClickListener {
                    copyToClipboard("content",item.noteItem.content)
                }
            }

        }
        onDeleteItemClick()
        dialog.show()
    }


    private fun onDeleteItemClick(){
        deleteCard.setOnClickListener {
            userActions(UserActions.DeleteItem)
            userActions(UserActions.DismissDialog)
            dialog.hide()
        }
    }


    private fun onDialogDismiss(){
        dialog.setOnDismissListener {
            userActions(UserActions.DismissDialog)
            loginItem = LoginItemModel()
            noteItem = SecureNoteModel()
        }
    }


    private fun setHeaderValues(name:String) {
        itemImage.visibility = View.GONE
        logoTextCard.visibility = View.VISIBLE
        logoText.text = name.shortName()
        itemName.text = name
    }

    private fun copyToClipboard(label:String, toCopy:String){
        val clip: ClipData = ClipData.newPlainText(label, toCopy )
        clipBoard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to clipboard.", Toast.LENGTH_SHORT).show()
    }




}