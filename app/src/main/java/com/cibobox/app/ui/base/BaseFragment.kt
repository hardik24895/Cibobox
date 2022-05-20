package com.sniffspace.app.ui.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.cibobox.app.R
import com.cibobox.app.ui.activity.LoginActivity
import com.commonProject.interfaces.SnackbarActionListener
import com.commonProject.ui.dialog.ProgressDialog
import com.eisuchi.dialog.LogoutDialog

import com.eisuchi.eisuchi.data.modal.BaseModal
import com.eisuchi.eisuchi.uitils.Constant
import com.eisuchi.extention.goToActivityAndClearTask
import com.eisuchi.extention.showAlert
import com.eisuchi.utils.SessionManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by Hardik
 */

/**
 * Abstract Fragment which binds [ViewModel] [VM] and [ViewBinding] [VB]
 */
abstract class BaseFragment<VM : ViewModel, VB : ViewBinding> : Fragment() {
    protected abstract val mViewModel: VM

    protected lateinit var binding: VB

    var mContext: Context? = null
    var mActivity: Activity? = null
    private var lastClickTime: Long = 0
    lateinit var session: SessionManager
    var progressDialog: ProgressDialog? = null
    var snackbar: Snackbar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mActivity = activity
        session = SessionManager(context)
        binding = getViewBinding()
    }
    /**
     * It returns [VB] which is assigned to [mViewBinding] and used in [onCreate]
     */
    abstract fun getViewBinding(): VB
    /* fun isThisMe(userId: String?): Boolean {
         return session.user != null && userId == session.user?._id
     }*/

    fun showProgressbar() {
        showProgressbar(null)
    }

    fun showProgressbar(message: String? = getString(R.string.please_wait)) {
        hideProgressbar()
        progressDialog = ProgressDialog(mContext!!, message)
        progressDialog?.show()
    }

    fun hideProgressbar() {
        if (progressDialog != null && progressDialog?.isShowing!!) progressDialog!!.dismiss()
    }

    fun showSoftKeyboard(view: EditText) {
        view.requestFocus(view.text.length)
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideSoftKeyboard(): Boolean {
        try {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        } catch (e: Exception) {
            return false
        }
    }
    fun errorResponse(response:String){
        val gson = Gson()
        val baseModal=gson.fromJson(response, BaseModal::class.java)
        if (baseModal?.error?.code==6){
            logOutDialog(requireContext())
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
        dialog.show(childFragmentManager, "YesNO")
    }
    /* fun setupToolbar(view: View, title: String? = null) {
         (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.toolbar))
         val actionBar = (activity as AppCompatActivity).supportActionBar
         if (actionBar != null) {
             actionBar.setDisplayShowTitleEnabled(false)
             actionBar.setDisplayHomeAsUpEnabled(false)
             if (title != null) tvTitle.text = title
         }
     }

     fun setupToolBarWithMenu(view: View, title: String, icon: Int = R.drawable.v_ic_menu) {
         (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.toolbar))
         val actionBar = (activity as AppCompatActivity).supportActionBar
         if (actionBar != null) {
             actionBar.setDisplayShowTitleEnabled(false)
             actionBar.setDisplayHomeAsUpEnabled(true)
             actionBar.setHomeAsUpIndicator(icon)
             tvTitle.text = title
         }
     }*/

    fun showSnackbar(view: View?, msg: String, LENGTH: Int) {
        if (view == null) return
        snackbar = Snackbar.make(view, msg, LENGTH)
        val sbView = snackbar?.view
        val textView =
            sbView?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView?.setTextColor(ContextCompat.getColor(mContext!!, R.color.colorAccent))
        snackbar?.show()
    }

    fun showSnackbar(
        view: View?,
        msg: String,
        LENGTH: Int,
        action: String,
        actionListener: SnackbarActionListener?
    ) {
        if (view == null) return
        snackbar = Snackbar.make(view, msg, LENGTH)
        snackbar?.setActionTextColor(ContextCompat.getColor(mContext!!, R.color.colorAccent))
        if (actionListener != null) {
            snackbar?.setAction(action) { view1 ->
                snackbar?.dismiss()
                actionListener.onAction()
            }
        }
        val sbView = snackbar?.view
        val textView =
            sbView?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView?.setTextColor(ContextCompat.getColor(mContext!!, R.color.colorAccent))
        snackbar?.show()
    }

    fun preventDoubleClick(view: View) {
        // preventing double, using threshold of 1000 ms
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()
    }

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onDestroy() {
        snackbar?.dismiss()
        hideProgressbar()
        super.onDestroy()
    }
}