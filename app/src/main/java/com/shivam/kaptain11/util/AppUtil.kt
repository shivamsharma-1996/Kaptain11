package com.shivam.kaptain11.util

import android.app.Activity
import android.content.Intent
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object AppUtil {

    @JvmStatic
    fun pickImageFromGallery(activity: Activity, IMAGE_PICK_CODE: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(
            intent,
            IMAGE_PICK_CODE
        )
    }

    @JvmStatic
    @Throws(ParseException::class)
    fun convertIsoDateTime(timestamp: String?): Date? {
        val format = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US
        )
        format.timeZone = TimeZone.getTimeZone("UTC")
        return format.parse(timestamp)
    }
}