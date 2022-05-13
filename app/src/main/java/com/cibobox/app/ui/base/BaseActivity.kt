package com.eisuchi.eisuchi.ui.base

import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.cibobox.app.R
import com.cibobox.app.ui.activity.LoginActivity
import com.commonProject.ui.dialog.ProgressDialog
import com.eisuchi.dialog.LogoutDialog

import com.eisuchi.eisuchi.data.modal.BaseModal

import com.eisuchi.eisuchi.uitils.Constant
import com.eisuchi.extention.dismissAlertDialog
import com.eisuchi.extention.goToActivityAndClearTask
import com.eisuchi.extention.showAlert
import com.eisuchi.utils.SessionManager
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.gson.Gson


/**
 * Created by Hardik
 */

/**
 * Abstract Activity which binds [ViewModel] [VM] and [ViewBinding] [VB]
 */
abstract class BaseActivity<VM : ViewModel, VB : ViewBinding> : AppCompatActivity() {


    protected abstract val mViewModel: VM

    protected lateinit var binding: VB

    var title: TextView? = null
    var toolbar: Toolbar? = null
    lateinit var session: SessionManager

    private var appUpdateManager: AppUpdateManager? = null
    private val IMMEDIATE_APP_UPDATE_REQ_CODE = 124

    var shouldPerformDispatchTouch = true
    var progressDialog: ProgressDialog? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = SessionManager(this)
        disableAutoFill()
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        checkUpdate();
        binding = getViewBinding()
    }
    /**
     * It returns [VB] which is assigned to [mViewBinding] and used in [onCreate]
     */
    abstract fun getViewBinding(): VB

    open fun checkUpdate() {
        val appUpdateInfoTask: com.google.android.gms.tasks.Task<AppUpdateInfo> = appUpdateManager!!.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() === UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                startUpdateFlow(appUpdateInfo)
            } else if (appUpdateInfo.updateAvailability() === UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdateFlow(appUpdateInfo)
            }
        }
    }

     open fun startUpdateFlow(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager!!.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.IMMEDIATE,
                this,
                IMMEDIATE_APP_UPDATE_REQ_CODE
            )
        } catch (e: SendIntentException) {
            e.printStackTrace()
        }
    }

    /*fun isThisMe(userId: String?): Boolean {
        return userId == session.user?._id
    }*/

    fun errorResponse(response:String){
        val gson = Gson()
        val baseModal=gson.fromJson(response, BaseModal::class.java)
        if (baseModal?.error?.code==6){
            logOutDialog(this)
        }else{
            showAlert(baseModal.error?.msg ?: "Error Occurred!")
        }
    }

    // LogOut Dialog Show
    fun logOutDialog(context: Context){

        val dialog = LogoutDialog.newInstance(
            context,
            object : LogoutDialog.onItemClick {
                override fun onItemCLicked() {
                    session.clearSession()
                    goToActivityAndClearTask<LoginActivity>()
                }
            })
        val bundle = Bundle()
        bundle.putString(Constant.TITLE, getString(R.string.app_name))
        bundle.putString(Constant.TEXT, "Invalid session key")
        bundle.putBoolean(Constant.INFO, true)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "YesNO")
    }

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard()
    }

    override fun onDestroy() {
        dismissAlertDialog()
        hideSoftKeyboard()
        super.onDestroy()
    }

    fun showProgressbar() {
        showProgressbar(null)
    }

    fun showProgressbar(message: String? = getString(R.string.please_wait)) {

        hideProgressbar()
        progressDialog = ProgressDialog(this, message)
        progressDialog?.show()
    }

    fun hideProgressbar() {
        if (progressDialog != null && progressDialog?.isShowing!!) progressDialog!!.dismiss()
    }

    /* fun setUpToolbar(strTitle: String) {
         setUpToolbarWithBackArrow(strTitle, false)
     }*/

/*    @JvmOverloads
    fun setUpToolbarWithBackArrow(strTitle: String? = null, isBackArrow: Boolean = true) {
        toolbar = findViewById(R.id.toolbar)
        title = toolbar?.findViewById(R.id.tvTitle)

        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(isBackArrow)
            actionBar.setHomeAsUpIndicator(R.drawable.v_ic_arrow_back)
            if (strTitle != null) title?.text = strTitle
        }
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                hideSoftKeyboard()
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showSoftKeyboard(view: EditText) {
        view.requestFocus(view.text.length)
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideSoftKeyboard(): Boolean {
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            return false
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        var ret = false
        try {
            val view = currentFocus
            ret = super.dispatchTouchEvent(event)
            if (shouldPerformDispatchTouch) {
                if (view is EditText) {
                    val w = currentFocus
                    val scrCords = IntArray(2)
                    if (w != null) {
                        w.getLocationOnScreen(scrCords)
                        val x = event.rawX + w.left - scrCords[0]
                        val y = event.rawY + w.top - scrCords[1]

                        if (event.action == MotionEvent.ACTION_UP && (x < w.left || x >= w.right || y < w.top || y > w.bottom)) {
                            val imm =
                                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                        }
                    }
                }
            }
            return ret
        } catch (e: Exception) {
            e.printStackTrace()
            return ret
        }

    }


    fun disableAutoFill() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.importantForAutofill =
                View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
        }
    }

    companion object {

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        }
    }

    open fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(
                    applicationContext,
                    "Update canceled by user! Result Code: $resultCode", Toast.LENGTH_LONG
                ).show()
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(
                    applicationContext,
                    "Update success! Result Code: $resultCode", Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Update Failed! Result Code: $resultCode",
                    Toast.LENGTH_LONG
                ).show()
                checkUpdate()
            }
        }
    }
}
