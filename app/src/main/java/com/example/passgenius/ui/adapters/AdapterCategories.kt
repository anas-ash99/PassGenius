package com.example.passgenius.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.passgenius.R
import com.example.passgenius.common.UserActions
import com.example.passgenius.domain.models.CategoryItem


class AdapterCategories (
    private val context: Context,
    private var actions:(UserActions)->Unit,

    private val list:List<CategoryItem>
    ): RecyclerView.Adapter<AdapterCategories.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category_rv, parent, false)
        )
    }

    fun notifyChang(position: Int){
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val item = list[position]
        if (item.isSelected){
            holder.iconCard.setCardBackgroundColor(context.getColor(R.color.ring_color))
            holder.ring.setCardBackgroundColor(context.getColor(R.color.ring_color))
        }else{
            holder.iconCard.setCardBackgroundColor(context.getColor(R.color.unselected_color))
            holder.ring.setCardBackgroundColor(context.getColor(R.color.unselected_color))
        }

        holder.imageView.setBackgroundResource(item.icon)
//        if (item.name == "All"){
//            val params:MarginLayoutParams = holder.itemView.layoutParams as MarginLayoutParams
//            params.leftMargin = 40
//        }
        holder.title.text = item.name
        holder.itemView.setOnClickListener {
            actions(UserActions.ChoseItemsCategory(position, this))
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
