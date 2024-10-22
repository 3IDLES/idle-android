package com.idle.designsystem.binding.component

import android.content.Context
import android.graphics.Point
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.idle.binding.ToastType
import com.idle.binding.dpToPx
import com.idle.designsystem.binding.R

private var currentToast: Toast? = null

fun showToast(
    context: Context,
    msg: String,
    toastType: ToastType,
    paddingBottom: Int
) {
    // 현재 표시 중인 토스트가 있으면 닫기
    currentToast?.cancel()

    val inflater = LayoutInflater.from(context)
    val customToastView = inflater.inflate(R.layout.view_care_toast, null)
    val toastBody = customToastView.findViewById<LinearLayout>(R.id.toast_body)
    val messageTextView = customToastView.findViewById<TextView>(R.id.toast_text)
    val iconImageView = customToastView.findViewById<ImageView>(R.id.toast_icon)

    when (toastType) {
        ToastType.SUCCESS -> {
            toastBody.setBackgroundResource(R.drawable.shape_snackbar_success)
            iconImageView.setImageResource(com.idle.designresource.R.drawable.ic_success)
        }

        ToastType.ERROR -> {
           toastBody.setBackgroundResource(R.drawable.shape_snackbar_error)
            iconImageView.setImageResource(com.idle.designresource.R.drawable.ic_error)
        }
    }
    messageTextView.text = msg

    val toast = Toast(context)
    toast.view = customToastView

    toast.setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, dpToPx(paddingBottom))
    toast.duration = Toast.LENGTH_SHORT

    val horizontalMarginInDp = 20
    val horizontalMarginPercentage = dpToPx(horizontalMarginInDp) / context.screenWidth()
    toast.setMargin(horizontalMarginPercentage.toFloat(), 0f)

    toast.show()
    currentToast = toast
}


fun dismissToast() {
    currentToast?.cancel()
    currentToast = null
}

private fun Context.screenDimension(): Point {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size
}

private fun Context.screenWidth(): Int = screenDimension().x