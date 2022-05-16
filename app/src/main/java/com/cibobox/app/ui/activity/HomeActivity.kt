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
import com.cibobox.app.data.modal.OrderCompleteModal
import com.cibobox.app.data.modal.OrderListData
import com.cibobox.app.data.modal.OrderListModal
import com.cibobox.app.data.modal.OrderListRequest
import com.cibobox.app.databinding.ActivityHomeBinding

import com.cibobox.app.ui.adapter.OrderListAdapter
import com.commonProject.interfaces.LoadMoreListener
import com.eisuchi.dialog.LogoutDialog
import com.eisuchi.eisuchi.data.modal.ComplateOrderModal
import com.eisuchi.eisuchi.data.modal.LogoutModal

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
class HomeActivity : BaseActivity<BaseViewModal, ActivityHomeBinding>(), OrderListAdapter.OnItemSelected {

    override val  mViewModel : BaseViewModal by viewModels()
    var adapter: OrderListAdapter? = null
    private val list: MutableList<OrderListData> = mutableListOf()

    var page: Int = 0
    var hasNextPage: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
       // setDummyData()
        setupRecyclerView()
        pullToRefresh()
        clickEvent()


    }





    override fun getViewBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)



    // SwipeLayout Refresh
    fun pullToRefresh() {
        binding.include.swipeRefreshLayout.setOnRefreshListener {
            // list.clear()
            list.clear()
            hasNextPage = true
            binding.include.recyclerView?.isLoading = true

             page=0
            getOrderList(page)
        }
    }

    // Recyclerview Init
    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.include.recyclerView.layoutManager = layoutManager
        adapter = OrderListAdapter(this, list, session, "", this)
        binding.include.recyclerView.adapter = adapter
        binding.include.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        binding.include.recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !binding.include.recyclerView.isLoading) {
                    binding.include.progressbar.visible()
                    getOrderList(++page)
                }
            }
        })

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





    // OrderList API calling
    fun getOrderList(pageno :Int) {


     //   val request = OrderListRequest(session.user.result.get(0).userid!!.toInt())

        //val jsonObject = JSONObject()
        //jsonObject.put("userid", 173)

        mViewModel.order(session.user.result.get(0).userid!!.toInt(), pageno).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                       // binding.include.swipeRefreshLayout.isRefreshing = false
                        resource.data?.let { response -> OrderResponse(response) }
                    }
                    Status.ERROR -> {
                      //  binding.include.swipeRefreshLayout.isRefreshing = false
                        //  logOutDialog()
                        // showAlert(it.message.toString())
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                      //  binding.include.swipeRefreshLayout.isRefreshing = true

                    }
                }
            }
        })
    }


    // Order API Response
    private fun OrderResponse(orderModal: Response<OrderListModal>) {
        val response = orderModal.body()
        binding.include.swipeRefreshLayout.isRefreshing = false
        try {
            if (response?.success == "1") {
                list.addAll(response.result)
                if (list.size>0){
                    binding.include.progressbar.hide()
                }
                 //   adapter?.notifyDataSetChanged()


                response.result?.let { list.size.minus(it?.size) }?.let {
                    adapter?.notifyItemRangeInserted(
                        it,
                        list.size
                    )
                }
                hasNextPage = list.size < response.total!!


            } else {
                // binding.toolbar.txtTitle.text = getString(R.string.dashboard)
            }
        } catch (e: Exception) {
            errorResponse(response.toString())

        }



        refreshData(getString(R.string.no_data_found), 1)
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




    override fun onItemSelect(position: Int, data: OrderListData, action: String) {
        val intent = Intent(this, OrderDetailActivity::class.java)
        intent.putExtra(Constant.DATA, data.orderId?.toString())
        intent.putExtra(Constant.ORDER_ID, data.id?.toInt())
        intent.putExtra(Constant.COMPLETE_ORDER, data.status)
        startActivity(intent)
        Animatoo.animateCard(this)
    }

    override fun onCompleteOrder(position: Int, data: OrderListData, action: String) {


        val dialog = LogoutDialog.newInstance(
            this,
            object : LogoutDialog.onItemClick {
                override fun onItemCLicked() {
                    data.id?.let { orderComplete(it) }
                }
            })
        val bundle = Bundle()
        bundle.putString(Constant.TITLE, this.getString(R.string.app_name))
        bundle.putString(Constant.TEXT, this.getString(R.string.msg_complate))
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "YesNO")
    }

    // complete Order Api Calling
    fun  orderComplete(id :Int) {




        mViewModel.orderComplete(id).observe(this , Observer {
            it?.let { resource ->   when (resource.status) {
                Status.SUCCESS -> {
                    // hideProgressbar()
                    resource.data?.let { response -> orderCompletedResponse(response) }
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

    private fun orderCompletedResponse(response: Response<OrderCompleteModal>) {
        val data = response.body()

        if (data?.success==1){
            Toast.makeText(this, data.msg, Toast.LENGTH_LONG).show()
            list.clear()
            getOrderList(page)
        }else{
            errorResponse(data?.msg.toString())
        }

    }


    override fun onResume() {
        super.onResume()
        list.clear()
        binding.include.swipeRefreshLayout.isRefreshing = true
        page=0
        getOrderList(page)

    }

    // Logout API Calling
    fun logOut() {

        var result = ""
        try {
            val jsonBody = JSONObject()
          //  jsonBody.put("sessionKey", session.user.data?.sessionKey)

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


