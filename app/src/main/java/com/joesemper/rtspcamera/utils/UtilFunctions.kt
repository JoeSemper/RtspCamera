package com.joesemper.rtspcamera.utils

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun unixTimeToHoursMinutesSeconds(time: Long): String {
    val sdf = SimpleDateFormat("HH:mm:ss")
    return sdf.format(time)
}