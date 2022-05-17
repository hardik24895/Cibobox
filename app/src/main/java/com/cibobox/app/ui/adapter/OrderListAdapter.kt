package com.cibobox.app.ui.adapter


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cibobox.app.data.modal.OrderListData
import com.cibobox.app.databinding.RowOrderListBinding


import com.eisuchi.eisuchi.uitils.Constant
import com.eisuchi.eisuchi.uitils.TimeStamp
import com.eisuchi.extention.hide
import com.eisuchi.extention.invisible
import com.eisuchi.extention.visible
import com.eisuchi.utils.SessionManager
import java.util.*
import java.util.concurrent.TimeUnit


class OrderListAdapter(
    private val mContext: Context,
    var list: MutableList<OrderListData> = mutableListOf(),
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
        fun onItemSelect(position: Int, data: OrderListData, action: String)
        fun onCompleteOrder(position: Int, data: OrderListData, action: String)
    }

    class ItemHolder(containerView: RowOrderListBinding) : RecyclerView.ViewHolder(containerView.root) {
        val binding = containerView

        fun bindData(
            context: Context,
            data: OrderListData,
            listener: OnItemSelected, session: SessionManager
        ) {

            // Order Pending
            if (data.status=="0"){
                binding.btnCompleOrder.visible()
                binding.mainView.setBackgroundColor(Color.parseColor("#80FFD580"))
            }
            // Order Complete
            else if (data.status =="1"){
                binding.btnCompleOrder.hide()
                binding.mainView.setBackgroundColor(Color.parseColor("#80DAF7A6"))
            }
            // Order Reject
            else{
                binding.btnCompleOrder.hide()
                binding.mainView.setBackgroundColor(Color.parseColor("#80E03744"))
            }

         // val amount=String.format("$ %.2f", data.totalAmount?.toDouble())

           binding.txtOrderNo.text ="#" + data.orderId
            binding.txtAmount .text =  data.totalAmount
            binding.txtDate.text = TimeStamp.getServerDate(data.createdAt.toString())
           // binding.txtMin.text = elapseTime
            binding.btnCompleOrder.setOnClickListener { listener.onCompleteOrder(adapterPosition, data, "MainView") }
            binding.mainView.setOnClickListener { listener.onItemSelect(adapterPosition, data, "MainView") }


        }
    }


}