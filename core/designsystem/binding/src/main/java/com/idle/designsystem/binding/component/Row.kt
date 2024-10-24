package com.idle.designsystem.binding.component

import android.annotation.SuppressLint
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

    private var titleTextView: TextView
    private var arrowImageView: ImageView
    private var switchView: SwitchCompat

    init {
        LayoutInflater.from(context).inflate(R.layout.view_care_row, this, true)
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

            recycle()
        }
    }

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

    @SuppressLint("ClickableViewAccessibility")
    fun setOnSwitchClickListener(listener: () -> Unit) {
        switchView.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                listener()
            }
            true  // 터치 이벤트를 소비하여 상태 변화 막기
        }
    }
}