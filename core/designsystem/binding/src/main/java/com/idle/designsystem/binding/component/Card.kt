package com.idle.designsystem.binding.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import com.idle.designsystem.binding.R

class CareCenterInfoCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var centerNameTextView: TextView
    private var centerAddressTextView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_care_center_info_card, this, true)
        centerNameTextView = findViewById(R.id.card_center_name_TV)
        centerAddressTextView = findViewById(R.id.card_center_address_TV)

        context.obtainStyledAttributes(attrs, R.styleable.CareCenterInfoCard).apply {
            val centerName = getString(R.styleable.CareCenterInfoCard_centerName) ?: ""
            centerNameTextView.text = centerName

            val centerAddress = getString(R.styleable.CareCenterInfoCard_centerAddress) ?: ""
            centerAddressTextView.text = centerAddress

            recycle()
        }
    }

    fun setCenterName(name: String) {
        centerNameTextView.text = name
    }

    fun setCenterAddress(address: String) {
        centerAddressTextView.text = address
    }
}