package com.example.passgenius.ui.adapters

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.passgenius.common.DeleteClickInterface
import com.example.passgenius.R
import com.example.passgenius.ui.ItemPage.AddNewItemActivity
import com.example.passgenius.ui.dialogs.ShowBottomDialog
import com.example.passgenius.common.enums.AddItemType
import com.example.passgenius.domain.models.ItemListModel
import com.example.passgenius.domain.viewModels.MainViewModel
import de.hdodenhof.circleimageview.CircleImageView


class PassItemAdapter
        (

    private val context: Context,
    private val items: MutableList<ItemListModel>,
    lifecycleOwner: LifecycleOwner,
    viewModelStoreOwner: ViewModelStoreOwner,
    private val isSearchPage:Boolean = false
        ) : RecyclerView.Adapter<PassItemAdapter.MyViewHolder>(), DeleteClickInterface {
    private lateinit var  itemName:TextView
    private lateinit var  itemImage:CircleImageView
    private lateinit var  logoText:TextView
    private lateinit var deleteCard:LinearLayout
    private lateinit var layout1:LinearLayout
    private lateinit var layout2:LinearLayout
    private lateinit var layout3:LinearLayout
    private lateinit var logoTextCard: CardView
    private var viewModel: MainViewModel = ViewModelProvider(viewModelStoreOwner)[MainViewModel::class.java]

    private val dialogClass: ShowBottomDialog = ShowBottomDialog(context, AddItemType.LOGIN, lifecycleOwner =  lifecycleOwner, deleteInterface = this)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item__pass_rv, parent,false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item : ItemListModel = items.get(position)
         if (position ==  items.size -1){
             val param = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
             param.bottomMargin = 200
         }

//        if (item.imageUrl == ""){
            holder.logoText.text = (item.name.substring(0,2).toUpperCase())
            holder.itemImage.visibility =View.GONE
            holder.logoText.visibility = View.VISIBLE
//        }else{
//            Glide.with(context).load(item.imageUrl).into(holder.itemImage)
//            holder.itemImage.visibility =View.VISIBLE
//            holder.logoText.visibility = View.GONE
//        }

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

        val intent = Intent(context, AddNewItemActivity::class.java)


        when(item.type){
            "LOGIN"-> {
                holder.itemCard.setOnClickListener {

                    intent.putExtra("pageType", "LOGIN")
                    intent.putExtra("isEditPage", false)
                    intent.putExtra("item", item.loginItem)
                    intent.putExtra("itemList", item)
                    viewModel.isExitingTheApp = false
                    context.startActivity(intent)

                }
//                holder.menuIcon.setOnClickListener { showDialogLoginItem(item.loginItem, position)}
                holder.menuIcon.setOnClickListener {
                    dialogClass.loginItem = item.loginItem
                    dialogClass.itemType = AddItemType.LOGIN
                    dialogClass.showDialog(position, this)

                }
            }
            "NOTE" -> {
                holder.itemCard.setOnClickListener {
                    intent.putExtra("pageType", "NOTE")
                    intent.putExtra("isEditPage", false)
                    intent.putExtra("item", item.noteItem)
                    intent.putExtra("itemList", item)
                    viewModel.isExitingTheApp = false
                    context.startActivity(intent)
                }

                holder.menuIcon.setOnClickListener {
                    dialogClass.noteItem = item.noteItem
                    dialogClass.itemType = AddItemType.SECURE_NOTE
                    dialogClass.showDialog(position, this)

                }

            }
            else->{
            }
        }




    }




    override fun getItemCount(): Int {
        return items.size!!
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