package com.cibobox.app.ui.adapter


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cibobox.app.R
import com.cibobox.app.data.modal.OrderIteams
import com.cibobox.app.data.modal.OrderListData
import com.cibobox.app.databinding.RowOrderIteamListBinding
import com.cibobox.app.databinding.RowOrderListBinding



import com.eisuchi.eisuchi.uitils.Constant
import com.eisuchi.eisuchi.uitils.TimeStamp
import com.eisuchi.extention.invisible
import com.eisuchi.extention.visible
import com.eisuchi.utils.SessionManager
import java.util.*
import java.util.concurrent.TimeUnit


class OrderDetailIteamAdapter(
    private val mContext: Context,
    var list: MutableList<OrderIteams> = mutableListOf(),
    var session: SessionManager,
    var status: String,
    private val listener: OnItemSelected,
) : RecyclerView.Adapter<OrderDetailIteamAdapter.ItemHolder>() {

    lateinit var binding: RowOrderIteamListBinding

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        binding = RowOrderIteamListBinding.inflate(
            LayoutInflater
                .from(parent.getContext()), parent, false
        )
        return ItemHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]
        holder.bindData(mContext, data, listener, session)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: OrderListData, action: String)
    }

    class ItemHolder(containerView: RowOrderIteamListBinding) : RecyclerView.ViewHolder(containerView.root) {
        val binding = containerView

        fun bindData(
            context: Context,
            data: OrderIteams,
            listener: OnItemSelected, session: SessionManager
        ) {

            binding.texPrice.text = "$" + data.price
            binding.txtProductName.text = data.name
            binding.txtQty.text = "Qty: " +data.qty
            binding.txtInstra.text = data.instra
            Glide.with(context).load(data.image).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(binding.img)
           // binding.txtMin.text = elapseTime
            //binding.btnComplate.setOnClickListener { listener.onCompleteOrder(adapterPosition, data, "MainView") }


        }
    }


}