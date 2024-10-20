package com.idle.designsystem.binding.component

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.idle.binding.base.dpToPx
import com.idle.designsystem.binding.R

@SuppressLint("RestrictedApi")
fun showSnackbar(rootView: View, data: String, paddingBottom: Int) {
    val inflater = LayoutInflater.from(rootView.context)
    val careSnackbarView = inflater.inflate(R.layout.view_care_snackbar, null)
    val messageTextView = careSnackbarView.findViewById<TextView>(R.id.snackbar_text)
    val iconImageView = careSnackbarView.findViewById<ImageView>(R.id.snackbar_icon)

    val (msg, type) = try {
        data.split("|").let {
            if (it.size >= 2) {
                it[0] to it[1]
            } else {
                data to SnackBarType.ERROR.name
            }
        }
    } catch (e: Exception) {
        data to SnackBarType.ERROR.name
    }

    val snackBarType = SnackBarType.create(type)
    when (snackBarType) {
        SnackBarType.SUCCESS -> {
            careSnackbarView.setBackgroundResource(R.drawable.shape_snackbar_success)
            iconImageView.setImageResource(com.idle.designresource.R.drawable.ic_success)
        }

        SnackBarType.ERROR -> {
            careSnackbarView.setBackgroundResource(R.drawable.shape_snackbar_error)
            iconImageView.setImageResource(com.idle.designresource.R.drawable.ic_error)
        }
    }
    messageTextView.text = msg

    // Snackbar 생성 및 설정
    val snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG)
    val snackbarLayout = snackbar.view as SnackbarLayout
    snackbarLayout.setBackgroundColor(
        ContextCompat.getColor(
            rootView.context,
            android.R.color.transparent
        )
    )
    snackbarLayout.setPadding(0, 0, 0, 0)
    snackbarLayout.addView(careSnackbarView, 0)

    // LayoutParams에서 마진을 설정할 때 dp를 px로 변환
    val params = snackbarLayout.layoutParams
    if (params is FrameLayout.LayoutParams) {
        params.setMargins(dpToPx(20), 0, dpToPx(20), dpToPx(paddingBottom))
        // 왼쪽, 위, 오른쪽, 아래 마진 설정
        snackbarLayout.layoutParams = params
    }

    snackbar.show()
}

private enum class SnackBarType {
    ERROR, SUCCESS;

    companion object {
        fun create(type: String): SnackBarType {
            return SnackBarType.entries.firstOrNull { it.name == type } ?: ERROR
        }
    }
}
