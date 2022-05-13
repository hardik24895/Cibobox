package com.cibobox.app.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.cibobox.app.R
import com.cibobox.app.databinding.ActivityLoginBinding
import com.eisuchi.extention.*

import com.eisuchi.eisuchi.data.modal.LoginModal

import com.eisuchi.eisuchi.ui.base.BaseActivity
import com.eisuchi.eisuchi.ui.base.BaseViewModal

import com.eisuchi.eisuchi.uitils.Constant
import com.eisuchi.eisuchi.uitils.Logger
import com.eisuchi.eisuchi.uitils.RetrofitRequestBody
import com.eisuchi.eisuchi.uitils.Status
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

@AndroidEntryPoint
class LoginActivity : BaseActivity<BaseViewModal, ActivityLoginBinding>() {

    override val mViewModel: BaseViewModal by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        clickEvent()

    }


    override fun getViewBinding(): ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)


    // view click handle
    fun clickEvent(){
        binding.btnLogin.setOnClickListener {
            goToActivityAndClearTask<HomeActivity>()
           // validation()
        }

        binding.edtPassword.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                validation()
            }
            false
        })

        binding.txtForgotpwd.setOnClickListener {
           // openWebPage(Constant.FORGPWD_URL)
        }
    }

    // check input validation
    fun validation() {
        when {
            binding.edtEmail.isEmpty() -> {
                binding.mainView.showSnackBar(getString(R.string.enter_email))
            }
            !isValidEmail(binding.edtEmail.getValue())-> {
                binding.mainView.showSnackBar(getString(R.string.enter_valid_email))
            }
            binding.edtPassword.getValue().length < 6 -> {
                binding.mainView.showSnackBar(getString(R.string.enter_valid_password))
            }

            else -> {
              //  login("sdfgsdgdfgdxf")


/*
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("", "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result
                   // val token = "jcgbdzihfgsdihfsdfs"
                    login(token.toString())
                    // Log and toast
                    // val msg = getString(R.string.msg_token_fmt, token)
                    Log.d("token", token.toString())
                    //  Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                })*/

            }


        }
    }



    // call login API
    private fun login(token:String) {

        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("email", binding.edtEmail.getValue())
            jsonBody.put("password", binding.edtPassword.getValue())
            jsonBody.put("deviceToken", token)
            jsonBody.put("deviceType", 0)

            result = RetrofitRequestBody.setParentJsonData(
                Constant.LOGIN,
                jsonBody
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        mViewModel.login(RetrofitRequestBody.wrapParams(result)).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                       hideProgressbar()
                        resource.data?.let { response -> handleResponse(response) }
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

    fun handleResponse(loginModal : Response<LoginModal>){
        val response =loginModal.body()
        if (response?.status == 1) {
            session.user = response
            session.storeDataByKey(Constant.IS_SOUND, true)
            Logger.d("Session Key=== ${response.data?.sessionKey}" )
            goToActivityAndClearTask<HomeActivity>()
        } else {
            showAlert(response?.data?.msg.toString())
        }
    }

    /**
     * Open a web page of a specified URL
     *
     * @param url URL to open
     */
    fun openWebPage(url: String?) {
        val webpage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }



}