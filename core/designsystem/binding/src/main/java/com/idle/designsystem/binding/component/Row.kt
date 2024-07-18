package com.idle.designsystem.binding.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.idle.designsystem.binding.R

class CareRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var careRow: ConstraintLayout
    private var titleTextView: TextView
    private var arrowImageView: ImageView
    private var switchView: SwitchCompat

    init {
        LayoutInflater.from(context).inflate(R.layout.view_care_row, this, true)
        careRow = findViewById(R.id.row_CL)
        arrowImageView = findViewById(R.id.row_arrow_IV)
        titleTextView = findViewById(R.id.row_title_TV)
        switchView = findViewById(R.id.row_switch_SC)

        context.obtainStyledAttributes(attrs, R.styleable.CareRow).apply {
            val titleText = getString(R.styleable.CareRow_title) ?: ""
            titleTextView.text = titleText

            val showArrow = getBoolean(R.styleable.CareRow_showArrow, true)
            arrowImageView.isVisible = showArrow

            val showSwitch = getBoolean(R.styleable.CareRow_showSwitch, false)
            switchView.isVisible = showSwitch
            if (showSwitch) arrowImageView.visibility = View.GONE

            val switchState = getBoolean(R.styleable.CareRow_switchState, false)
            switchView.isChecked = switchState

            val onClickMethodName = getString(R.styleable.CareRow_onClick)
            if (!onClickMethodName.isNullOrEmpty()) {
                setOnClickListener {
                    invokeMethodByName(onClickMethodName)
                }
            }

            careRow.setOnClickListener { onClick?.invoke() }
            recycle()
        }
    }

    private var onClick: (() -> Unit)? = null

    fun setTitle(title: String) {
        titleTextView.text = title
    }

    fun showArrow(show: Boolean) {
        arrowImageView.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showSwitch(show: Boolean) {
        switchView.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setSwitchState(state: Boolean) {
        switchView.isChecked = state
    }

    fun getSwitchState(): Boolean {
        return switchView.isChecked
    }

    fun setOnClickListener(listener: () -> Unit) {
        onClick = listener
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