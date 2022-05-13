package com.cibobox.app.ui.adapter


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cibobox.app.databinding.RowOrderListBinding

import com.eisuchi.eisuchi.data.modal.OrderDataItem
import com.eisuchi.eisuchi.uitils.Constant
import com.eisuchi.eisuchi.uitils.TimeStamp
import com.eisuchi.extention.invisible
import com.eisuchi.extention.visible
import com.eisuchi.utils.SessionManager
import java.util.*
import java.util.concurrent.TimeUnit


class OrderListAdapter(
    private val mContext: Context,
    var list: MutableList<OrderDataItem> = mutableListOf(),
    var session: SessionManager,
    var status: String,
    private val listener: OnItemSelected,
) : RecyclerView.Adapter<OrderListAdapter.ItemHolder>() {

    lateinit var binding: RowOrderListBinding

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        binding = RowOrderListBinding.inflate(
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
        fun onItemSelect(position: Int, data: OrderDataItem, action: String)
        fun onCompleteOrder(position: Int, data: OrderDataItem, action: String)
    }

    class ItemHolder(containerView: RowOrderListBinding) : RecyclerView.ViewHolder(containerView.root) {
        val binding = containerView

        fun bindData(
            context: Context,
            data: OrderDataItem,
            listener: OnItemSelected, session: SessionManager
        ) {


           // binding.txtMin.text = elapseTime
            //binding.btnComplate.setOnClickListener { listener.onCompleteOrder(adapterPosition, data, "MainView") }
            binding.mainView.setOnClickListener { listener.onItemSelect(adapterPosition, data, "MainView") }


        }
    }


}