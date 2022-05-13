package com.cibobox.app.ui.activity

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.cibobox.app.databinding.ActivitySplashBinding
import com.eisuchi.extention.goToActivityAndClearTask
import com.eisuchi.eisuchi.ui.base.BaseActivity
import com.eisuchi.eisuchi.ui.base.BaseViewModal

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<BaseViewModal, ActivitySplashBinding>() {

    override val mViewModel: BaseViewModal by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            validateRedirection()
        }, 1000)



       // binding.txtHello.text ="Got it"

    }
    private fun validateRedirection() {

        if (session.isLoggedIn) {
            goToActivityAndClearTask<HomeActivity>()
        } else{
            goToActivityAndClearTask<LoginActivity>()
        }
    }
    override fun getViewBinding(): ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)


}