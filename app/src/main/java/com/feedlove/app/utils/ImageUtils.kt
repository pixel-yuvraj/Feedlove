package com.feedlove.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

object ImageUtils {

    fun compressAndResize(context: Context, uri: Uri,
                          maxWidth: Int = 800, maxHeight: Int = 800,
                          quality: Int = 75): ByteArray {
        val bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        } else {
            val src = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(src)
        }
        val (w, h) = bitmap.width to bitmap.height
        val scale = minOf(maxWidth.toFloat() / w, maxHeight.toFloat() / h, 1f)
        val scaled = if (scale < 1f) {
            Bitmap.createScaledBitmap(bitmap, (w * scale).toInt(), (h * scale).toInt(), true)
        } else bitmap
        val baos = ByteArrayOutputStream()
        scaled.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        return baos.toByteArray()
    }
}

