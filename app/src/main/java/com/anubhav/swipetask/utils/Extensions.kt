package com.anubhav.swipetask.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import java.text.DecimalFormat

fun String.parseStringToPrice(): Double? {
    val parsedValue = this.toDoubleOrNull() ?: return null
    val formattedNumber = DecimalFormat("#.00").format(parsedValue)
    return formattedNumber.toDoubleOrNull()
}

fun ContentResolver.getFileName(uri: Uri): String {
    var name = ""
    val cursor = query(uri, null, null, null, null)
    cursor?.use {
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
    return name
}