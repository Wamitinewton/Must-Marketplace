package com.example.mustmarket.core.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Constants {
    val EMAIL_REGEX = "^[a-z0-9_.]{5,48}@[a-z0-9]{2,}(\\.[a-z0-9]{2,}){1,5}$".toRegex()
    val PHONE_REGEX = "^[0-9]{10}$".toRegex()
    val PASSWORD_REGEX =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^_&+=])(?=\\S+$).{8,}$".toRegex()

    fun formatPrice(price: Double): String {
        val nf = NumberFormat.getInstance(Locale.getDefault())
        nf.minimumFractionDigits = 0
        nf.maximumFractionDigits = 2
        nf.isGroupingUsed = true
        return nf.format(price)
    }

    fun formatDate(timeStamp: Long): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyy", Locale.getDefault())
        val date = Date(timeStamp)
        return dateFormat.format(date)
    }


}