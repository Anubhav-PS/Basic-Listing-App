package com.anubhav.swipetask.services.models

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class ProductUploadRequest(
    val file: File,
    val contentType: String
) : RequestBody() {

    companion object {

        private const val DEFAULT_BUFFER_SIZE = 1048
    }

    override fun contentType(): MediaType? = "$contentType/*".toMediaTypeOrNull()

    override fun contentLength() = file.length()

    override fun writeTo(sink: BufferedSink) {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val fileInputStream = FileInputStream(file)
        var uploaded = 0L
        fileInputStream.use { inputStream ->
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                uploaded += read
                sink.write(buffer, 0, read)
            }
        }
    }

}