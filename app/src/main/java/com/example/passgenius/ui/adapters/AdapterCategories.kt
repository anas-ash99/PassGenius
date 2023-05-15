package com.example.passgenius.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.passgenius.R
import com.example.passgenius.domain.interfaces.OnCategoryRVClicks


class AdapterCategories (
    private val context: Context,
    private val onCategoryRVClicks: OnCategoryRVClicks,

        ): RecyclerView.Adapter<AdapterCategories.MyViewHolder>() {

    private var selectedPosition:Int = 0
    private var list = arrayListOf<String>("All", "Logins", "Notes", "Identities", "Payments" )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category_rv, parent, false)
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (selectedPosition == position){
            holder.iconCard.setCardBackgroundColor(context.getColor(R.color.ring_color))
            holder.ring.setCardBackgroundColor(context.getColor(R.color.ring_color))
        }else{
            holder.iconCard.setCardBackgroundColor(context.getColor(R.color.unselected_color))
            holder.ring.setCardBackgroundColor(context.getColor(R.color.unselected_color))

        }

        when(position){
            0 -> {
                var params:MarginLayoutParams = holder.itemView.layoutParams as MarginLayoutParams
                params.leftMargin = 40

            }
            1 -> holder.imageView.setBackgroundResource(R.drawable.ic_baseline_login_24)
            2-> holder.imageView.setBackgroundResource(R.drawable.icon_notes_black)
            3 -> holder.imageView.setBackgroundResource(R.drawable.id_icon)
            4 -> holder.imageView.setBackgroundResource(R.drawable.icon_businss_invest_money)
        }
        holder.title.text = list[position]

        holder.itemView.setOnClickListener {
            selectedPosition = position
            onCategoryRVClicks.onCategoryClick(list[position])
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        var title: TextView = itemView.findViewById(R.id.textView)
        val ring: CardView = itemView.findViewById(R.id.ring)
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
        var iconCard: CardView = itemView.findViewById(R.id.iconCard)



    }


}
