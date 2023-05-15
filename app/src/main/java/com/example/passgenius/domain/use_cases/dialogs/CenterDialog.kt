package com.example.passgenius.domain.use_cases.dialogs

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Gravity
import androidx.cardview.widget.CardView
import com.example.passgenius.common.DeleteClickInterface
import com.example.passgenius.R
import com.example.passgenius.common.enums.AddItemType
import com.example.passgenius.common.instances.DialogInstance
import com.example.passgenius.domain.models.LoginItemModel
import com.example.passgenius.domain.models.SecureNoteModel

class CenterDialog (
    private val context: Context,
    var pageTye: AddItemType? = null,
    var loginItemModel: LoginItemModel = LoginItemModel(),
    var noteItem: SecureNoteModel = SecureNoteModel(),
    deleteInterface: DeleteClickInterface
) {

    var dialog: Dialog = DialogInstance.initDialog(context, Gravity.CENTER,R.layout.dialog_delete_alert, R.style.DialogCenterAnimation)!!
    private var deleteButton:CardView
    private var cancelButton:CardView
    var itemRVPosition:Int = -1
    init {
        dialog?.window?.setGravity(Gravity.CENTER)
        deleteButton = dialog?.findViewById(R.id.deleteButton)!!
        cancelButton = dialog?.findViewById(R.id.cancel_button)!!


        cancelButton.setOnClickListener {
            dialog.hide()
        }

        deleteButton.setOnClickListener {
            try {
                deleteInterface.onDeleteClick(itemRVPosition)
            }catch (e:java.lang.Exception){
                Log.e("dialog", e.toString())
            }
        }

    }

    fun showDialog(position: Int) {
        itemRVPosition = position
        dialog.show()
    }






}