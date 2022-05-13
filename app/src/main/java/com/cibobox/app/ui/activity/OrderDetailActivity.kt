package com.cibobox.app.ui.activity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.cibobox.app.databinding.ActivityOrderDetailBinding
import com.cibobox.app.ui.adapter.OrderDetailIteamAdapter
import com.cibobox.app.ui.adapter.TempOrderDetailIteamAdapter
import com.eisuchi.eisuchi.data.modal.OrderDataItem
import com.eisuchi.eisuchi.data.modal.OrderDetailModal
import com.eisuchi.eisuchi.data.modal.OrderItems
import com.eisuchi.eisuchi.ui.base.BaseActivity
import com.eisuchi.eisuchi.ui.base.BaseViewModal
import com.eisuchi.eisuchi.uitils.Constant
import com.eisuchi.eisuchi.uitils.RetrofitRequestBody
import com.eisuchi.eisuchi.uitils.Status
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
@AndroidEntryPoint
class OrderDetailActivity :BaseActivity<BaseViewModal, ActivityOrderDetailBinding>() , TempOrderDetailIteamAdapter.OnItemSelected{
    var adapter: TempOrderDetailIteamAdapter? = null
    override val  mViewModel : BaseViewModal by viewModels()
    private val list: MutableList<OrderItems> = mutableListOf()
    var handler = Handler()
    var item_url=""
    lateinit var timedTask: Runnable
    val strList :MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        clickEvent()
        setDummyData()
        setupRecyclerView()
       // getOrderDetail(false)
        //autoRefresh()
        binding.include.txtTitle.text ="#123456"

    }

    override fun getViewBinding(): ActivityOrderDetailBinding = ActivityOrderDetailBinding.inflate(layoutInflater)

    fun setDummyData(){

        for (i in 0 until 5){
            strList.add("a")
        }

    }

    // Recyclerview Init
    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        adapter = TempOrderDetailIteamAdapter(this, strList, session, "",this)
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


    }

    // OrderList API calling
    fun  getOrderDetail(b: Boolean) {
        list.clear()
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("order_id", intent.getStringExtra(Constant.DATA))
            jsonBody.put("sessionKey", session.user.data?.sessionKey)

            result = RetrofitRequestBody.setParentJsonData(
                Constant.ORDER,
                jsonBody
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        mViewModel.orderDetail(RetrofitRequestBody.wrapParams(result)).observe(this , Observer {
            it?.let {
                    resource ->   when (resource.status) {
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

    private fun OrderDetailResponse(orderDetailModal: Response<OrderDetailModal>, b: Boolean) {
        val response = orderDetailModal.body()
        if (response?.data?.code == 6) {
            logOutDialog(this)
            return
        }

        if (response?.status == 1) {
            list.clear()
            val data = response.data
            data?.items.let { it?.let { it1 -> list.addAll(it1) } }
            adapter?.notifyDataSetChanged()

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



    // Auto Refresh 1 Min
    fun autoRefresh(){
        timedTask = object : Runnable {
            override fun run() {
                list.clear()
                getOrderDetail(false)
                handler.postDelayed(this, 60000)
            }
        }

        handler.post(timedTask)
    }

    override fun onItemSelect(position: Int, data: OrderDataItem, action: String) {

    }

    override fun onCompleteOrder(position: Int, data: OrderDataItem, action: String) {

    }


}
