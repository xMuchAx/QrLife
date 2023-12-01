package com.example.qrlife.model

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

class QrCode(val dataQrCode: String, var qrCodeBitmap: Bitmap? = null) {

    companion object {
        private const val QR_CODE_SIZE = 300

        fun generateQRCode(data: String): Bitmap? {
            try {
                val bitMatrix: BitMatrix = MultiFormatWriter().encode(
                    data,
                    BarcodeFormat.QR_CODE,
                    QR_CODE_SIZE,
                    QR_CODE_SIZE,
                    null
                )

                val width = bitMatrix.width
                val height = bitMatrix.height
                val pixels = IntArray(width * height)

                for (y in 0 until height) {
                    for (x in 0 until width) {
                        if (bitMatrix[x, y]) {
                            pixels[y * width + x] = 0xff000000.toInt()
                        } else {
                            pixels[y * width + x] = 0xffffffff.toInt()
                        }
                    }
                }

                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
                return bitmap
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}
