package com.idle.setting.dialog

import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.idle.setting.databinding.FragmentLogoutDialogBinding

class LogoutDialogFragment() : DialogFragment() {
    private var _binding: FragmentLogoutDialogBinding? = null
    private val binding get() = _binding!!
    var onDismissCallback: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogoutDialogBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logoutDialog.apply {
            setOnLeftButtonClickListener { dismiss() }
            setOnRightButtonClickListener {
                findNavController().currentBackStackEntry?.savedStateHandle?.set(
                    key = "logout",
                    value = true,
                )

                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            val displayMetrics = Resources.getSystem().displayMetrics
            val paddingPx = (20 * displayMetrics.density).toInt()
            val screenWidth = displayMetrics.widthPixels

            attributes = attributes?.apply {
                width = screenWidth - (paddingPx * 2)
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }

            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismissCallback?.invoke()
        super.onDismiss(dialog)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}