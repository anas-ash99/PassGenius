package com.example.passgenius.domain.use_cases

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView

class TestUseCase {

    fun initPage(context: Context){
        Toast.makeText(context, "been init", Toast.LENGTH_SHORT).show()

    }


    fun observeIsEditPage(
        isEditPage:Boolean,
        editPageView:LinearLayout,
        showPageView:LinearLayout,
        dateLayout:LinearLayout,
        deleteButton:CardView,
        starIconCard:CardView,
        doneButton:CardView,
        editButton:CardView
    ) {
        if (isEditPage) {
            editPageView.visibility = View.VISIBLE
            showPageView.visibility = View.GONE
            dateLayout.visibility = View.GONE
            deleteButton.visibility = View.GONE
            starIconCard.visibility = View.GONE
            doneButton.visibility = View.VISIBLE
            editButton.visibility = View.GONE

        } else {


            dateLayout.visibility = View.VISIBLE
            deleteButton.visibility = View.VISIBLE
            starIconCard.visibility = View.VISIBLE
            editPageView.visibility = View.GONE
            showPageView.visibility = View.VISIBLE
            doneButton.visibility = View.GONE
            editButton.visibility = View.VISIBLE

        }

    }





}