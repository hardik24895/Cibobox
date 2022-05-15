package com.cibobox.app.ui.activity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cibobox.app.R
import com.cibobox.app.data.modal.OrderCompleteModal
import com.cibobox.app.data.modal.OrderDetailModal
import com.cibobox.app.data.modal.OrderIteams
import com.cibobox.app.data.modal.OrderListData

import com.cibobox.app.databinding.ActivityOrderDetailBinding
import com.cibobox.app.ui.adapter.OrderDetailIteamAdapter
import com.eisuchi.dialog.LogoutDialog
import com.eisuchi.eisuchi.ui.base.BaseActivity
import com.eisuchi.eisuchi.ui.base.BaseViewModal
import com.eisuchi.eisuchi.uitils.Constant
import com.eisuchi.eisuchi.uitils.RetrofitRequestBody
import com.eisuchi.eisuchi.uitils.Status
import com.eisuchi.extention.goToActivityAndClearTask
import com.eisuchi.extention.hide
import com.eisuchi.extention.visible
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
@AndroidEntryPoint
class OrderDetailActivity :BaseActivity<BaseViewModal, ActivityOrderDetailBinding>() , OrderDetailIteamAdapter.OnItemSelected{
    var adapter: OrderDetailIteamAdapter? = null
    override val  mViewModel : BaseViewModal by viewModels()
    private val list: MutableList<OrderIteams> = mutableListOf()
    var handler = Handler()
    var item_url=""
    lateinit var timedTask: Runnable
    val strList :MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        clickEvent()
        setupRecyclerView()
        getOrderDetail(false)
        //autoRefresh()
        binding.include.txtTitle.text ="#" + intent.getStringExtra(Constant.DATA)
        if (intent.getStringExtra(Constant.COMPLETE_ORDER)=="0"){
            binding.btnCompleteOrder.visible()
        }else{
            binding.btnCompleteOrder.hide()
        }

    }

    override fun getViewBinding(): ActivityOrderDetailBinding = ActivityOrderDetailBinding.inflate(layoutInflater)



    // Recyclerview Init
    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        adapter = OrderDetailIteamAdapter(this, list, session, "",this)
        binding.recyclerView.adapter = adapter

        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

    }

    // View Click Handle
    fun clickEvent(){
        binding.include.imgBack.setOnClickListener { finish() }
        binding.btnCompleteOrder.setOnClickListener {


            val dialog = LogoutDialog.newInstance(
                this,
                object : LogoutDialog.onItemClick {
                    override fun onItemCLicked() {
                        orderComplete()
                    }
                })
            val bundle = Bundle()
            bundle.putString(Constant.TITLE, this.getString(R.string.app_name))
            bundle.putString(Constant.TEXT, this.getString(R.string.msg_complate))
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, "YesNO")
        }


    }

    // OrderList API calling
    fun  getOrderDetail(b: Boolean) {
        list.clear()


        intent.getStringExtra(Constant.DATA)?.let {
            mViewModel.orderDetail(it).observe(this , Observer {
                it?.let { resource ->   when (resource.status) {
                    Status.SUCCESS -> {
                        // hideProgressbar()
                        resource.data?.let { response -> OrderDetailResponse(response,b) }
                    }
                    Status.ERROR -> {
                        // hideProgressbar()
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        // showProgressbar()
                    }
                }
                } })
        }
    }

    private fun OrderDetailResponse(orderDetailModal: Response<OrderDetailModal>, b: Boolean) {
        val response = orderDetailModal.body()


        if (response?.success == "1") {
            list.clear()
            val data = response.result
            data?.items.let { it?.let { it1 -> list.addAll(it1) } }
            adapter?.notifyDataSetChanged()

            binding.txtEmail.text = data?.email
            binding.txtPhone.text = data?.mobileNo
            binding.txtTotal.text ="$"+ data?.totalprice

        }else{
            errorResponse(response?.msg.toString())
        }
    }


    // complete Order Api Calling
    fun  orderComplete() {

            mViewModel.orderComplete(intent.getIntExtra(Constant.ORDER_ID,0)).observe(this , Observer {
                it?.let { resource ->   when (resource.status) {
                    Status.SUCCESS -> {
                         hideProgressbar()
                        resource.data?.let { response -> orderCompletedResponse(response) }
                    }
                    Status.ERROR -> {
                         hideProgressbar()
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                         showProgressbar()
                    }
                }
                } })

    }

    private fun orderCompletedResponse(response: Response<OrderCompleteModal>) {
        val data = response.body()

        if (data?.success==1){
            Toast.makeText(this, data.msg, Toast.LENGTH_LONG).show()
            goToActivityAndClearTask<HomeActivity>()
        }else{
            errorResponse(data?.msg.toString())
        }

    }

    override fun onResume() {
        super.onResume()
        //handler.post(timedTask)
    }











    override fun onPause() {
        super.onPause()
      //  if (timedTask!=null)
        //    handler.removeCallbacks(timedTask)
    }

    override fun onDestroy() {
        super.onDestroy()
      //  if (timedTask!=null)
         //   handler.removeCallbacks(timedTask)
    }




    override fun onItemSelect(position: Int, data: OrderListData, action: String) {

    }




}
