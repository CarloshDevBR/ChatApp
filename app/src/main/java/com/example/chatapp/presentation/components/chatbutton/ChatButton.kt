package com.example.chatapp.presentation.components.chatbutton

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.chatapp.R
import com.example.chatapp.databinding.ComponentButtonBinding

class ChatButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var binding = ComponentButtonBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    private lateinit var label: String

    var loading: Boolean = false

    init {
        attrs?.let {
            initializeComponent(it)
        }
    }

    private fun initializeComponent(attrs: AttributeSet) {
        setupAttrs(attrs)
        setupComponent()
    }

    private fun setupAttrs(attrs: AttributeSet) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.Button)

        label = attributes.getString(R.styleable.Button_label) ?: ""

        loading = attributes.getBoolean(R.styleable.Button_loading, false)

        attributes.recycle()
    }

    private fun setupComponent() = with(binding) {
        txtBtnChat.text = label
        txtBtnChat.visibility = if (loading) View.INVISIBLE else View.VISIBLE

        progressbarBtnChat.visibility = if (loading.not()) View.INVISIBLE else View.VISIBLE

        btnChat.background = if (loading) {
            ContextCompat.getDrawable(context, R.drawable.shape_rounded_background_filled_disabled)
        } else {
            ContextCompat.getDrawable(context, R.drawable.shape_rounded_background_filled)
        }
    }

    fun setChatButtonClickListener(listener: OnClickListener) = with(binding) {
        btnChat.setOnClickListener(listener)
    }
}