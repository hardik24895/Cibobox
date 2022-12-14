package com.eisuchi.eisuchi.uitils

import android.text.TextUtils
import java.lang.Math.abs
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


object TimeStamp {
    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS

    val FULL_DATE_FORMAT = "dd MMM yyyy, hh:mm:ss a"
    val DATE_FORMAT_MMM_YYYY = "MMM yyyy"
    val TIME_FORMAT_SHORT_24 = "HH:mm"
    val TIME_FORMAT_SHORT_12 = "hh:mm a"
    val TIME_FORMAT_FULL = "hh:mm:ss a"

    fun formatToSeconds(value: String, format: String): Long {
        val timeZone = TimeZone.getDefault()
        return formatToSeconds(value, format, timeZone)
    }

    fun formatToSeconds(value: String, format: String, timeZone: TimeZone): Long {
        try {
            val sdf = SimpleDateFormat(format, Locale.ENGLISH)
            sdf.timeZone = timeZone
            val mDate = sdf.parse(value)
            return TimeUnit.MILLISECONDS.toSeconds(mDate.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return 0
    }


    fun formatToMillis(value: String, format: String): Long {
        val timeZone = TimeZone.getDefault()
        return formatToMillis(value, format, timeZone)
    }


    fun formatToMillis(value: String, format: String, timeZone: TimeZone): Long {
        try {
            val sdf = SimpleDateFormat(format)
            sdf.timeZone = timeZone
            val mDate = sdf.parse(value)
            return mDate.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        /*val zone = ZoneId.systemDefault()
        val formatter = DateTimeFormatter.ofPattern(Constant.DATE_FORMAT)



        val dateTime = LocalDateTime.parse(value, formatter)
        return    dateTime.atZone(zone).toInstant().toEpochMilli()*/

        return  0
    }

    fun millisToFormat(millis: Long?): String {
        val timeZone = TimeZone.getDefault()
        return millisToFormat(millis!!, FULL_DATE_FORMAT, timeZone)
    }

    fun millisToFormat(millis: String): String {
        val timeZone = TimeZone.getDefault()
        return millisToFormat(java.lang.Long.parseLong(millis), FULL_DATE_FORMAT, timeZone)
    }

    fun millisToFormat(millis: Long, format: String): String {
        val timeZone = TimeZone.getDefault()
        return millisToFormat(millis, format, timeZone)
    }

    fun millisToFormat(millis: String, format: String): String {
        val timeZone = TimeZone.getDefault()
        return millisToFormat(java.lang.Long.parseLong(millis), format, timeZone)
    }

    fun millisToFormat(milis: Long, format: String, tz: TimeZone): String {
        var millis = milis
        if (millis < 1000000000000L) {
            millis *= 1000
        }
        val cal = Calendar.getInstance(tz)
        cal.timeInMillis = millis
        val sdf = SimpleDateFormat(format, Locale.ENGLISH)
        sdf.timeZone = tz
        return sdf.format(cal.time)
    }

    fun formatDateFromString(inputDate: String): String {
        var parsed: Date? = null
        var outputDate = ""

        val dfInput = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dfOutput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        try {
            parsed = dfInput.parse(inputDate)
            outputDate = dfOutput.format(parsed)

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return outputDate
    }

    fun formatServerDateToLocal(inputDate: String): String {
        var parsed: Date? = null
        var outputDate = ""

        val dfInput = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val dfOutput = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        try {
            parsed = dfInput.parse(inputDate)
            outputDate = dfOutput.format(parsed)

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return outputDate
    }

    fun getDateFromCheckInTime(Checkindate: String): String {


        if (!Checkindate.equals("")) {
            var parsed: Date? = null
            var outputDate = ""

            val dfInput = SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault())
            val dfOutput = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

            try {
                parsed = dfInput.parse(Checkindate)
                outputDate = dfOutput.format(parsed)

            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return outputDate
        } else
            return ""

    }

    fun getTimeFromCheckInOUtTime(Checkindate: String): String {


        var parsed: Date? = null
        var outputDate = ""

        val dfInput = SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault())
        val dfOutput = SimpleDateFormat("hh:mm:ss", Locale.getDefault())

        try {
            parsed = dfInput.parse(Checkindate)
            outputDate = dfOutput.format(parsed)

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return outputDate

    }


    fun getLocalfromUtc(utc_data: String, inputPattern: String, outputPattern: String): String {
        var formattedDate = ""
        if (!TextUtils.isEmpty(utc_data)) {
            try {
                val df = SimpleDateFormat(inputPattern, Locale.getDefault())
                df.timeZone = TimeZone.getTimeZone("UTC")
                var date: Date? = null
                try {
                    date = df.parse(utc_data)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

                //System.out.println(utc_data);
                df.timeZone = TimeZone.getDefault()
                val df1 = SimpleDateFormat(outputPattern, Locale.getDefault())
                formattedDate = df1.format(date)
                //System.out.println("finalLocalTimeDate -> "+formattedDate);
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return formattedDate
    }

    fun getServerDate(date: String): String {
        var convertDate = ""
        try {
            val f: DateFormat = SimpleDateFormat(Constant.DATE_FORMAT)
            val d: Date = f.parse(date)
            val date: DateFormat = SimpleDateFormat("dd MMM yyyy")
            val time: DateFormat = SimpleDateFormat("HH:mm")
            System.out.println("Date: " + date.format(d))
            System.out.println("Time: " + time.format(d))
            convertDate = date.format(d).toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return convertDate

    }

    fun getServerTime(date: String): String {
        var convertTime = ""
        var format = ""
        var format2 = ""
        try {
            val f: DateFormat = SimpleDateFormat(Constant.DATE_FORMAT)
            val d: Date = f.parse(date)
            f.setTimeZone(TimeZone.getDefault());
            val time: DateFormat = SimpleDateFormat("HH:mm")
            time.setTimeZone(TimeZone.getDefault());
            convertTime = time.format(d).toString()


            // Parsing hours, minutes and seconds in array

            // Parsing hours, minutes and seconds in array
            val arr: List<String> = convertTime.split(":")

            // Converting hours into integer

            // Converting hours into integer
            var hh = arr[0].toInt()

            if (hh > 12) {
                hh = hh - 12
                format = "PM"
            } else if (hh == 0) {
                hh = 12
                format = "AM"
            } else if (hh == 12) {
                hh = 12
                format = "PM"
            } else {
                format = "AM"
            }

            // Converting hh to String and
            // padding it with 0 on left side

            // Converting hh to String and
            // padding it with 0 on left side
            val hour = hh
            val minute = arr[1]
            format2 = hh.toString() + ":" + minute + " " + format
            // format =
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return format2

    }


    fun getServerTimeMinSec(date: String): String {
        var convertTime = ""
        try {
            val f: DateFormat = SimpleDateFormat(Constant.DATE_FORMAT)
            val d: Date = f.parse(date)
            val date: DateFormat = SimpleDateFormat("dd-MM-yyyy")
            val time: DateFormat = SimpleDateFormat("hh:mm:ss")
            convertTime = time.format(d).toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return convertTime

    }

    fun getAge(dobString: String): Int {
        val f: DateFormat = SimpleDateFormat(Constant.DATE_FORMAT)
        val d: Date = f.parse(dobString)
        val date: DateFormat = SimpleDateFormat("dd")
        val moth: DateFormat = SimpleDateFormat("MM")
        val year: DateFormat = SimpleDateFormat("yyyy")
        val dobDate = date.format(d).toInt()
        val dobMonth = moth.format(d).toInt()
        val dobYear = year.format(d).toInt()

        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()

        dob.set(dobYear, dobMonth, dobDate)

        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]

        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }

        val ageInt = age


        return ageInt + 1
    }

    fun getStartDateRange(): String {
        var begining: Date

        run {
            val calendar = getCalendarForNow()
            calendar[Calendar.DAY_OF_MONTH] = calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
            setTimeToBeginningOfDay(calendar)
            begining = calendar.time
        }
        val simpleDate = SimpleDateFormat("dd-MM-yyyy")
        val strDt = simpleDate.format(begining)
        return strDt
    }

    fun getSpesificStartDateRange(): String {
        val calendar = getCalendarForNow()
        if (calendar.get(Calendar.MONTH) > 10)
            return "25-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR)
        else
            return "25-0" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR)
    }

    fun getEndDateRange(): String {
        var end: Date
        run {
            val calendar = getCalendarForNow()
            calendar[Calendar.DAY_OF_MONTH] = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            setTimeToEndofDay(calendar)
            end = calendar.time
        }
        val simpleDate = SimpleDateFormat("dd/MM/yyyy")
        val strDt = simpleDate.format(end)
        return strDt
    }


    private fun getCalendarForNow(): Calendar {
        val calendar = GregorianCalendar.getInstance()
        calendar.time = Date()
        return calendar
    }

    private fun setTimeToBeginningOfDay(calendar: Calendar) {
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
    }

    private fun setTimeToEndofDay(calendar: Calendar) {
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.SECOND] = 59
        calendar[Calendar.MILLISECOND] = 999
    }

    fun getCountOfDays(
        createdDateString: String?,
        expireDateString: String?
    ): Int? {

        val date1: Date
        val date2: Date
        val dates = SimpleDateFormat("dd/MM/yyyy")
        date1 = dates.parse(createdDateString)
        date2 = dates.parse(expireDateString)
        val difference: Long = abs(date1.time - date2.time)
        val differenceDates = difference / (24 * 60 * 60 * 1000)
        val dayDifference = differenceDates.toString()
        return dayDifference.toInt()
    }
}
