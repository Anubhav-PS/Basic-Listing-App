package com.anubhav.swipetask.utils

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun validatePriceFormat(
    input: String,
    view: TextInputEditText,
    view2: TextInputLayout,
    canBeZero: Boolean
) {
    if (!canBeZero && input.startsWith("0")) {
        view.setText("")
        view2.error = "Price cannot be 0"
        return
    }
    view.error = null
    if (input.startsWith(".")) {
        view.setText("")
        return
    }
    if (input.startsWith(",")) {
        view.setText("")
        return
    }
    if (input.endsWith(",")) {
        val inputLength = input.length
        val subText = input.subSequence(
            0, inputLength - 2
        )
        view.setText(subText)
    }
    if (input.contains(".") && input.split(".")[1].length > 2) {
        val inputLength = input.length
        val subText = input.subSequence(
            0, inputLength - 2
        )
        view.setText(subText)
    }
}