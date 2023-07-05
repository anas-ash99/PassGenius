package com.example.passgenius.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.passgenius.common.DeleteClickInterface
import com.example.passgenius.R
import com.example.passgenius.common.UserActions
import com.example.passgenius.domain.models.ItemListModel
import com.example.passgenius.domain.viewModels.MainViewModel


class PassItemAdapter
        (
    private val context: Context,
    private val items: MutableList<ItemListModel>,
    private val isSearchPage:Boolean = false,
    private val viewModel: MainViewModel
        ) : RecyclerView.Adapter<PassItemAdapter.MyViewHolder>(), DeleteClickInterface {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item__pass_rv, parent,false)
        )
    }



    fun notifyDeleteItem(position: Int){
        notifyItemRemoved(position)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item : ItemListModel = items[position]
         if (position ==  items.size -1){
             val param = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
             param.bottomMargin = 200
         }


        try {
            holder.logoText.text = (item.name.substring(0,2).uppercase())
            holder.itemImage.visibility =View.GONE
            holder.logoText.visibility = View.VISIBLE

            if (position > 0 && !isSearchPage ){
                val prevItem = items[position-1]
                if (item.name.substring(0,1).lowercase() == prevItem.name.substring(0,1).lowercase()){
                    holder.sortLetter.visibility= View.GONE

                }
            }
            if (isSearchPage){
                holder.sortLetter.visibility= View.GONE
            }

            holder.itemName.text = item.name

            if (item.secondaryName.length >37){
                holder.email.text = "${item.secondaryName.substring(0, 38)}..."
            }else{
                holder.email.text = item.secondaryName
            }
            holder.sortLetter.text = item.name.substring(0,1).uppercase()
        }catch (e:StringIndexOutOfBoundsException){
            Log.e("substring",e.message,e )
        }

        holder.itemCard.setOnClickListener {
           viewModel.onPassItemClick(item)
        }

        holder.menuIcon.setOnClickListener {
            viewModel.onUserAction(UserActions.ItemMenuClick(position, item))
        }




    }




    override fun getItemCount(): Int {
        return items.size
    }

    class MyViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val sortLetter: TextView = itemView.findViewById(R.id.sortLetter)
        val email: TextView = itemView.findViewById(R.id.email)
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val logoText:TextView = itemView.findViewById(R.id.LogoText)
        var menuIcon: CardView = itemView.findViewById(R.id.saveIcon)
        val itemCard:CardView = itemView.findViewById(R.id.cardLayout)
    }

    override fun onDeleteClick(position: Int) {
        if (position != -1){
            notifyItemRemoved(position)
            items.removeAt(position)
        }
    }


}