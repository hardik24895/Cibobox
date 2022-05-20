package com.cibobox.app.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.NotificationManagerCompat

import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
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
import com.eisuchi.dialog.NotificationPermissionDialog
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.JsonElement
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
@AndroidEntryPoint
class HomeActivity : BaseActivity<BaseViewModal, ActivityHomeBinding>() {

    override val  mViewModel : BaseViewModal by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        clickEvent()
        handleNotificationClick(intent)
        binding.toolbar.imgSound.visible()
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        navView.setupWithNavController(navController)
        if (session.getDataByKeyBoolean(Constant.IS_SOUND, true))
            binding.toolbar.imgSound.setImageResource(R.drawable.sound_on)
        else
            binding.toolbar.imgSound.setImageResource(R.drawable.sound_off)
    }





    override fun getViewBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)


    // View Click Handle
    fun clickEvent() {

        binding.toolbar.imgSound.setOnClickListener {
            if (session.getDataByKeyBoolean(Constant.IS_SOUND, true)){
                binding.toolbar.imgSound.setImageResource(R.drawable.sound_off)
                session.storeDataByKey(Constant.IS_SOUND, false)
            }else{
                binding.toolbar.imgSound.setImageResource(R.drawable.sound_on)
                session.storeDataByKey(Constant.IS_SOUND, true)
            }
        }
       binding.toolbar.txtLogout.setOnClickListener {
           val dialog = LogoutDialog.newInstance(
               this,
               object : LogoutDialog.onItemClick {
                   override fun onItemCLicked() {
                       //logOut()
                       session.clearSession()
                       goToActivityAndClearTask<LoginActivity>()
                   }
               })
           val bundle = Bundle()
           bundle.putString(Constant.TITLE, this.getString(R.string.app_name))
           bundle.putString(Constant.TEXT, this.getString(R.string.msg_logout))
           dialog.arguments = bundle
           dialog.show(supportFragmentManager, "YesNO")
       }


    }







    override fun onResume() {
        super.onResume()
        if(!isNotificationListenerEnabled(this)){
            permissonDialog(this)
        }

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
    fun handleNotificationClick(intent: Intent?){
        val extras = intent?.extras
        if (extras != null) {
            val bundle = intent.getBundleExtra(Constant.DATA)

           // val type = bundle?.getString(Constant.ORDER)
            val orderID = bundle?.getString(Constant.ORDER_ID)


                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, OrderDetailActivity::class.java)
                    intent.putExtra(Constant.DATA, orderID)
                    startActivity(intent)
                    Animatoo.animateCard(this)
                }, 1000)


        }
    }

    fun isNotificationListenerEnabled(context: Context): Boolean {
        val packageNames = NotificationManagerCompat.getEnabledListenerPackages(this)
        return packageNames.contains(context.getPackageName())
    }

    // LogOut Dialog Show
    fun permissonDialog(context: Context){

        val dialog = NotificationPermissionDialog.newInstance(
            context,
            object : NotificationPermissionDialog.onItemClick {
                override fun onItemCLicked() {
                    openNotificationListenSettings()
                }
            })
        val bundle = Bundle()
        bundle.putString(Constant.TITLE, getString(R.string.app_name))
        bundle.putString(Constant.TEXT, "Allow access to notifications in Settings manually, otherwise some features may not work properly")
        bundle.putBoolean(Constant.INFO, true)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "YesNO")
    }

    fun openNotificationListenSettings() {
        try {
            val intent: Intent
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            } else {
                intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            }
            startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

}


