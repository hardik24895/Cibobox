package com.cibobox.app.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.cibobox.app.R
import com.cibobox.app.databinding.ActivityHomeBinding

import com.cibobox.app.ui.adapter.OrderListAdapter
import com.cibobox.app.ui.adapter.TempOrderListAdapter
import com.eisuchi.dialog.LogoutDialog
import com.eisuchi.eisuchi.data.modal.ComplateOrderModal
import com.eisuchi.eisuchi.data.modal.LogoutModal
import com.eisuchi.eisuchi.data.modal.OrderDataItem

import com.eisuchi.eisuchi.data.modal.OrderListModal
import com.eisuchi.eisuchi.ui.base.BaseActivity
import com.eisuchi.eisuchi.ui.base.BaseViewModal
import com.eisuchi.eisuchi.uitils.Constant
import com.eisuchi.eisuchi.uitils.RetrofitRequestBody
import com.eisuchi.eisuchi.uitils.Status
import com.eisuchi.extention.goToActivityAndClearTask
import com.eisuchi.extention.hide
import com.eisuchi.extention.visible
import com.google.gson.Gson
import com.google.gson.JsonElement
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
@AndroidEntryPoint
class HomeActivity : BaseActivity<BaseViewModal, ActivityHomeBinding>(), TempOrderListAdapter.OnItemSelected {

    override val  mViewModel : BaseViewModal by viewModels()
    var adapter: TempOrderListAdapter? = null
    var handler = Handler()
    private val list: MutableList<OrderDataItem> = mutableListOf()
    val strList :MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setDummyData()
        setupRecyclerView()
       // pullToRefresh()
        clickEvent()
    }

    fun setDummyData(){

       for (i in 0 until 10){
           strList.add("a")
       }



    }


    override fun getViewBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)



    // SwipeLayout Refresh
    fun pullToRefresh() {
        binding.include.swipeRefreshLayout.setOnRefreshListener {
            // list.clear()
            adapter?.notifyDataSetChanged()
            list.clear()
            getOrderList()
        }
    }

    // Recyclerview Init
    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.include.recyclerView.layoutManager = layoutManager
        adapter = TempOrderListAdapter(this, strList, session, "", this)
        binding.include.recyclerView.adapter = adapter
        binding.include.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    // View Click Handle
    fun clickEvent() {
       binding.include2.txtLogout.setOnClickListener {
           val dialog = LogoutDialog.newInstance(
               this,
               object : LogoutDialog.onItemClick {
                   override fun onItemCLicked() {
                       logOut()
                   }
               })
           val bundle = Bundle()
           bundle.putString(Constant.TITLE, this.getString(R.string.app_name))
           bundle.putString(Constant.TEXT, this.getString(R.string.msg_logout))
           dialog.arguments = bundle
           dialog.show(supportFragmentManager, "YesNO")
       }


    }




    // Auto Refresh 1 Min
    fun autoRefresh(){
        val timedTask: Runnable = object : Runnable {
            override fun run() {
                adapter?.notifyDataSetChanged()
                handler.postDelayed(this, 60000)
            }
        }

        handler.post(timedTask)
    }

    // OrderList API calling
    fun getOrderList() {
        list.clear()
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("sessionKey", session.user.data?.sessionKey)

            result = RetrofitRequestBody.setParentJsonData(
                Constant.ORDERS,
                jsonBody
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        mViewModel.order(RetrofitRequestBody.wrapParams(result)).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.include.swipeRefreshLayout.isRefreshing = false
                        resource.data?.let { response -> OrderResponse(response) }
                    }
                    Status.ERROR -> {
                        binding.include.swipeRefreshLayout.isRefreshing = false
                        //  logOutDialog()
                        // showAlert(it.message.toString())
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.include.swipeRefreshLayout.isRefreshing = true

                    }
                }
            }
        })
    }


    // Order API Response
    private fun OrderResponse(orderModal: Response<JsonElement>) {
        val response = orderModal.body()
        try {
            val gson = Gson()
            val orderModal = gson.fromJson(response.toString(), OrderListModal::class.java)
            if (orderModal?.status == 1) {
                list.clear()
                list.addAll(orderModal.data)
                if (list.size>0)
                //   binding.toolbar.txtTitle.text = list.get(0).restaurantName
                    adapter?.notifyDataSetChanged()


            } else {
                // binding.toolbar.txtTitle.text = getString(R.string.dashboard)
            }
        } catch (e: Exception) {
            errorResponse(response.toString())

        }



        refreshData(getString(R.string.no_data_found), 1)
    }


    // Order API Response
    private fun complateOrderResponse(orderModal: Response<JsonElement>) {
        val response = orderModal.body()
        try {
            val gson = Gson()
            val orderModal = gson.fromJson(response.toString(), ComplateOrderModal::class.java)
            if (orderModal?.result == "success") {
                // list.clear()
                getOrderList()

            } else {
                // binding.toolbar.txtTitle.text = getString(R.string.dashboard)
            }
        } catch (e: Exception) {
            errorResponse(response.toString())

        }



        // refreshData(getString(R.string.no_data_found), 1)
    }

    // OrderList API calling
    fun completOrder(orderid:String) {
        list.clear()
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("sessionKey", session.user.data?.sessionKey)
            jsonBody.put("user_id", session.user.data?.id)
            jsonBody.put("order_id", orderid)

            result = RetrofitRequestBody.setParentJsonData(
                Constant.COMPLETE_ORDER,
                jsonBody
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        mViewModel.complateOrder(RetrofitRequestBody.wrapParams(result)).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.include.swipeRefreshLayout.isRefreshing = false
                        resource.data?.let { response -> complateOrderResponse(response) }
                    }
                    Status.ERROR -> {
                        binding.include.swipeRefreshLayout.isRefreshing = false
                        //  logOutDialog()
                        // showAlert(it.message.toString())
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.include.swipeRefreshLayout.isRefreshing = true

                    }
                }
            }
        })
    }


    // Show Hide Place Holder
    private fun refreshData(msg: String?, code: Int) {
        binding.include.recyclerView.setLoadedCompleted()
        adapter?.notifyDataSetChanged()
        if (list.size > 0) {
            binding.include.imgNodata.hide()
            binding.include.tvInfo.hide()
            binding.include.recyclerView.visible()
        } else {
            binding.include.imgNodata.visible()
            if (code == 0)
                binding.include.imgNodata.setImageResource(R.drawable.no_internet_bg)
            else
                binding.include.tvInfo.visible()
            binding.include.tvInfo.text="You have no orders"
            // binding.include.imgNodata.setImageResource(R.drawable.nodata)
            binding.include.recyclerView.hide()
        }
    }


    // Order Item Click
    override fun onItemSelect(position: Int, data: String, action: String) {
        val intent = Intent(this, OrderDetailActivity::class.java)
        intent.putExtra(Constant.DATA, data.toString())
        startActivity(intent)
        Animatoo.animateCard(this)
    }





    override fun onCompleteOrder(position: Int, data: OrderDataItem, action: String) {
        val dialog = LogoutDialog.newInstance(
            this,
            object : LogoutDialog.onItemClick {
                override fun onItemCLicked() {
                    completOrder(data.id.toString())
                }
            })
        val bundle = Bundle()
        bundle.putString(Constant.TITLE, this.getString(R.string.app_name))
        bundle.putString(Constant.TEXT, this.getString(R.string.msg_complate))
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "YesNO")
    }




    override fun onResume() {
        super.onResume()
        list.clear()
        //getOrderList()

    }

    // Logout API Calling
    fun logOut() {

        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("sessionKey", session.user.data?.sessionKey)

            result = RetrofitRequestBody.setParentJsonData(
                Constant.LOGOUT,
                jsonBody
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        mViewModel.logout(RetrofitRequestBody.wrapParams(result)).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        hideProgressbar()
                        resource.data?.let { response -> logOutResponse(response) }
                    }
                    Status.ERROR -> {
                        hideProgressbar()
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        showProgressbar()
                    }
                }
            }
        })
    }

    // LogOut API response
    private fun logOutResponse(loginModal: Response<LogoutModal>) {
        val response = loginModal.body()
        if (response?.status == 1) {
            session.clearSession()
            goToActivityAndClearTask<LoginActivity>()
        } else {
            logOutDialog(this)
        }

    }
}


