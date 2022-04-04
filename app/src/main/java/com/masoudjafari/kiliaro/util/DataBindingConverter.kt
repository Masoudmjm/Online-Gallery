package com.masoudjafari.kiliaro.util

import java.text.CharacterIterator
import java.text.SimpleDateFormat
import java.text.StringCharacterIterator
import java.time.Year
import java.time.YearMonth
import java.util.*
import kotlin.Int
import kotlin.String
import kotlin.math.min
import java.lang.String as String1

class DataBindingConverter {
    companion object {

        @JvmStatic
        fun humanReadableByteCount(value: Int): String {
            var bytes = value
            if (-1000 < bytes && bytes < 1000) {
                return "$bytes B"
            }
            val ci: CharacterIterator = StringCharacterIterator("kMGTPE")
            while (bytes <= -999950 || bytes >= 999950) {
                bytes /= 1000
                ci.next()
            }
            return String1.format("%.1f %cB", bytes / 1000.0, ci.current())
        }

        @JvmStatic
        fun getDateTimeFromString(s : String): String {
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(s)
            val calendar = Calendar.getInstance()
            calendar.time = date

            val year = calendar.get(Calendar.YEAR)
            var month: String = calendar.get(Calendar.MONTH).toString()
            var day: String = calendar.get(Calendar.DAY_OF_MONTH).toString()
            var hour: String = calendar.get(Calendar.HOUR_OF_DAY).toString()
            var minute: String = calendar.get(Calendar.MINUTE).toString()

            if (month.length == 1)
                month = "0$month"

            if (day.length == 1)
                day = "0$day"

            if (hour.length == 1)
                hour = "0$hour"

            if (minute.length == 1)
                minute = "0$minute"

            return "$year/$month/$day  $hour:$minute"
        }
    }
}