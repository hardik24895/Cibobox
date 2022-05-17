package com.eisuchi.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import com.cibobox.app.R
import com.cibobox.app.databinding.DialogNotificationPermissionBinding


import com.eisuchi.eisuchi.uitils.Constant
import com.eisuchi.extention.visible
import com.lightmicrofinance.app.utils.BlurDialogFragment


class NotificationPermissionDialog(context: Context) : BlurDialogFragment(), LifecycleOwner {

    private var _binding: DialogNotificationPermissionBinding? = null

    private val binding get() = _binding!!

    companion object {
        private lateinit var listener: onItemClick
        fun newInstance(
            context: Context,
            listeners: onItemClick
        ): NotificationPermissionDialog {
            this.listener = listeners
            return NotificationPermissionDialog(context)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog_Custom)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogNotificationPermissionBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateData()
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)

        _binding?.linYseNo?.visible()


        _binding?.tvYes?.setOnClickListener {
            listener.onItemCLicked()
            dismissAllowingStateLoss()
        }
        _binding?.tvNo?.setOnClickListener {
            dismissAllowingStateLoss()
        }


    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    private fun populateData() {
        val bundle = arguments
        if (bundle != null) {
            val title = bundle.getString(Constant.TITLE)
            val text = bundle.getString(Constant.TEXT)
            _binding?.txtTitle?.text = title
            _binding?.tvText?.text = text
        }
    }

    interface onItemClick {
        fun onItemCLicked()
    }

    interface onDissmiss {
        fun onDismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}








