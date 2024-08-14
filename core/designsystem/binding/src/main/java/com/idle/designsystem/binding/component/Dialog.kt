package com.idle.designsystem.binding.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import com.idle.designsystem.binding.R

class CareDialog @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var titleTextView: TextView
    private var leftButton: Button
    private var rightButton: Button
    private var onLeftButtonClick: (() -> Unit)? = null
    private var onRightButtonClick: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_care_dialog, this, true)
        titleTextView = findViewById(R.id.dialog_title_TV)
        leftButton = findViewById(R.id.dialog_left_BTN)
        rightButton = findViewById(R.id.dialog_right_BTN)

        context.obtainStyledAttributes(attrs, R.styleable.CareDialog).apply {
            val titleText = getString(R.styleable.CareDialog_title) ?: ""
            titleTextView.text = titleText

            val leftButtonText = getString(R.styleable.CareDialog_leftButtonText) ?: ""
            leftButton.text = leftButtonText

            val rightButtonText = getString(R.styleable.CareDialog_rightButtonText) ?: ""
            rightButton.text = rightButtonText

            val onLeftButtonClickMethodName = getString(R.styleable.CareDialog_onLeftButtonClick)
            if (!onLeftButtonClickMethodName.isNullOrEmpty()) {
                setOnLeftButtonClickListener {
                    invokeMethodByName(onLeftButtonClickMethodName)
                }
            }

            val onRightButtonClickMethodName = getString(R.styleable.CareDialog_onRightButtonClick)
            if (!onRightButtonClickMethodName.isNullOrEmpty()) {
                setOnRightButtonClickListener {
                    invokeMethodByName(onRightButtonClickMethodName)
                }
            }

            leftButton.setOnClickListener { onLeftButtonClick?.invoke() }
            rightButton.setOnClickListener { onRightButtonClick?.invoke() }
            recycle()
        }
    }

    fun setOnLeftButtonClickListener(listener: () -> Unit) {
        onLeftButtonClick = listener
    }

    fun setOnRightButtonClickListener(listener: () -> Unit) {
        onRightButtonClick = listener
    }

    private fun invokeMethodByName(methodName: String) {
        try {
            val method = context.javaClass.getMethod(methodName)
            method.invoke(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}