package com.idle.designsystem.binding.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.idle.designsystem.binding.R

class CareSubtitleTopAppBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var backIconImageView: ImageView
    private var titleTextView: TextView
    private var leftComponentPlaceholder: FrameLayout
    private var onNavigationClick: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_care_subtitle_top_app_bar, this, true)
        backIconImageView = findViewById(R.id.bar_back_IV)
        titleTextView = findViewById(R.id.bar_title_TV)
        leftComponentPlaceholder = findViewById(R.id.left_component_placeholder_FL)

        context.obtainStyledAttributes(attrs, R.styleable.CareSubtitleTopAppBar).apply {
            val titleText = getString(R.styleable.CareSubtitleTopAppBar_title) ?: ""
            titleTextView.text = titleText

            val showBackButton = getBoolean(R.styleable.CareSubtitleTopAppBar_showBackIcon, true)
            backIconImageView.isVisible = showBackButton

            val onBackClickMethodName = getString(R.styleable.CareSubtitleTopAppBar_onBackClick)
            if (!onBackClickMethodName.isNullOrEmpty()) {
                setOnNavigationClickListener {
                    invokeMethodByName(onBackClickMethodName)
                }
            }

            backIconImageView.setOnClickListener { onNavigationClick?.invoke() }
            recycle()
        }
    }

    fun setTitle(title: String) {
        titleTextView.text = title
    }

    fun setOnNavigationClickListener(listener: () -> Unit) {
        onNavigationClick = listener
    }

    fun setLeftComponent(view: View) {
        leftComponentPlaceholder.removeAllViews()
        leftComponentPlaceholder.addView(view)
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