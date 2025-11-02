package com.example.chatapp.presentation.viewutils

import android.text.InputType
import androidx.core.content.ContextCompat
import com.example.chatapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputEditText.togglePasswordVisibility(layout: TextInputLayout, isVisible: Boolean) {
    val defaultValue = 0

    val newInputType = if (isVisible) {
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    } else {
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    val iconRes = if (isVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility

    layout.endIconDrawable = ContextCompat.getDrawable(
        context,
        iconRes
    )

    inputType = newInputType
    setSelection(this.text?.length ?: defaultValue)
}