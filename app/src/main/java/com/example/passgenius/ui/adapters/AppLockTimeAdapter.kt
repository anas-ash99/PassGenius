package com.example.passgenius.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.passgenius.R
import com.example.passgenius.domain.interfaces.AppLockTimeInterface

class AppLockTimeAdapter(
    private val context: Context,
    private val appLockTimeInterface: AppLockTimeInterface,
    private val time:Int
) : RecyclerView.Adapter<AppLockTimeAdapter.MyViewHolder>() {
    private var items= mutableListOf(1,2,3,4,5,10,15,30,60,-1)
    private var selectedItem:Int = items.indexOf(time)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return AppLockTimeAdapter.MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_app_lock_time, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val item = items[position]
        if (position == 0){
            holder.checkBox.text = "$item minute"
        }else if(position == items.size - 1){
            holder.checkBox.text = "Never"
        }else{
            holder.checkBox.text = "$item minutes"
        }


        holder.checkBox.setOnClickListener {
            selectedItem = position
            notifyDataSetChanged()
            appLockTimeInterface.onChoseTime(item)

        }

        holder.checkBox.isChecked = position != -1 && position == selectedItem






    }

    override fun getItemCount(): Int {
        return items.size
    }


    class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

       val checkBox:CheckBox = itemView.findViewById(R.id.checkbox)

    }
}