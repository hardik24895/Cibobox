package com.eisuchi.extention

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.eisuchi.eisuchi.uitils.Constant

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun AppCompatActivity.isRunning() = lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)

inline fun <reified T : Activity> AppCompatActivity.goToActivityAndClearTask() {
    val intent = Intent(this, T::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    Animatoo.animateCard(this)
    finish()
}

inline fun <reified T : Activity> Fragment.goToActivityAndClearTask() {
    val intent = Intent(context, T::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    activity?.finish()
}

inline fun <reified T : Activity> AppCompatActivity.goToActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
    Animatoo.animateCard(this)
}

inline fun <reified T : Activity> Fragment.goToActivity() {
    startActivity(Intent(activity, T::class.java))
    Animatoo.animateCard(activity)
}

inline fun <reified T : Activity> Fragment.goToActivityBundle(bundle: Bundle) {
    val intent = Intent(activity, T::class.java)
    intent.putExtra(Constant.DATA, bundle)
    startActivity(intent)
    Animatoo.animateCard(context)
}

fun AppCompatActivity.addFragments(fragments: List<Fragment>, containerId: Int) {
    fragments.forEach {
        val ft = supportFragmentManager.beginTransaction()
        ft.add(containerId, it)
        ft.commitAllowingStateLoss()
    }
}

inline fun <reified T : Activity> AppCompatActivity.onBackPress() {
    Animatoo.animateSlideLeft(this)
    finish()
}

fun AppCompatActivity.replaceFragments(fragments: List<Fragment>, containerId: Int) {
    fragments.forEach {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(containerId, it)
        ft.commitAllowingStateLoss()
    }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, containerId: Int) {
    val ft = supportFragmentManager.beginTransaction()
    ft.replace(containerId, fragment)
    ft.commitAllowingStateLoss()
}

fun Fragment.replaceFragment(fragment: Fragment, containerId: Int) {
    val ft = childFragmentManager.beginTransaction()
    ft.replace(containerId, fragment)
    ft.commitAllowingStateLoss()
}

fun AppCompatActivity.addFragment(
    fragment: Fragment,
    containerId: Int,
    addToStack: Boolean = true
) {
    val ft = supportFragmentManager.beginTransaction()
    ft.add(containerId, fragment)
    if (addToStack) ft.addToBackStack(fragment.javaClass.name)
    ft.commitAllowingStateLoss()
}

fun Fragment.addFragment(fragment: Fragment, containerId: Int, addToStack: Boolean = true) {
    val ft = childFragmentManager.beginTransaction()
    ft.add(containerId, fragment)
    if (addToStack) ft.addToBackStack(fragment.javaClass.name)
    ft.commitAllowingStateLoss()
}

fun AppCompatActivity.showFragment(fragment: Fragment) {
    val ft = supportFragmentManager.beginTransaction()
    ft.show(fragment)
    ft.commitAllowingStateLoss()
}

fun AppCompatActivity.hideFragment(fragment: Fragment) {
    val ft = supportFragmentManager.beginTransaction()
    ft.hide(fragment)
    ft.commitAllowingStateLoss()
}





/*
fun setHomeScreenTitle(requireActivity: Activity, title: String) {
    val toolbar = requireActivity.findViewById<View>(R.id.toolbar)
    val tvTitle: TextviewBold = toolbar.findViewById(R.id.tvTitle)
    tvTitle.setText(title)
}
*/


fun getCurrentDate(): String {

    val date: String
    val selectedMonth: String
    val selectedDay: String

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)


    if (day < 10) {
        selectedDay = "0" + day
    } else
        selectedDay = day.toString()


    if (month < 10) {
        selectedMonth = "0" + (month + 1)
    } else
        selectedMonth = month.toString()

    date = "" + selectedDay + "-" + selectedMonth + "-" + year

    return date
}


fun getYesterdayDate(): String {

    val date: String
    val selectedMonth: String
    val selectedDay: String

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)


    if (day < 10) {
        selectedDay = "0" + (day - 1)
    } else
        selectedDay = (day - 1).toString()


    if (month < 10) {
        selectedMonth = "0" + (month + 1)
    } else
        selectedMonth = month.toString()

    date = "" + selectedDay + "-" + selectedMonth + "-" + year

    return date.toString()
}


fun getCurrentDateTime(): String {

    val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    val currentDate = sdf.format(Date())

    return currentDate.toString()
}

fun getCurentTime(date: String): String {
    try {
        val f: DateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val d: Date = f.parse(date) as Date
      //  val date: DateFormat = SimpleDateFormat("MM/dd/yyyy")
        val time: DateFormat = SimpleDateFormat("HH:mm")
        System.out.println("Time: " + time.format(d))
        return time.format(d).toString()
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun showDateTimePicker(requireActivity: Activity, edittext: EditText) {

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(
        requireActivity,
        {
                _, year, monthOfYear, dayOfMonth ->

            val selectedMonth: String
            val selectedDay: String
            if (dayOfMonth < 10) {
                selectedDay = "0" + dayOfMonth
            } else
                selectedDay = dayOfMonth.toString()


            if (monthOfYear < 10) {
                selectedMonth = "0" + (monthOfYear + 1)
            } else
                selectedMonth = monthOfYear.toString()

            edittext.setText("" + selectedDay + "/" + selectedMonth + "/" + year)
        },
        year,
        month,
        day
    )
    dpd.show()
}


fun showPastDateTimePicker(requireActivity: Activity, edittext: EditText) {

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(
        requireActivity,
        { _, year, monthOfYear, dayOfMonth ->

            val selectedMonth: String
            val selectedDay: String
            if (dayOfMonth < 10) {
                selectedDay = "0" + dayOfMonth
            } else
                selectedDay = dayOfMonth.toString()


            if (monthOfYear < 10) {
                selectedMonth = "0" + (monthOfYear + 1)
            } else
                selectedMonth = monthOfYear.toString()

            edittext.setText("" + selectedDay + "/" + selectedMonth + "/" + year)
        },
        year,
        month,
        day
    )

    val c1 = Calendar.getInstance()
    val year1 = c.get(Calendar.YEAR)
    val month1 = c.get(Calendar.MONTH)
    val day1 = c.get(Calendar.DAY_OF_MONTH)

    c1.set(Calendar.YEAR, year1)
    c1.set(Calendar.MONTH, month1)
    c1.set(Calendar.DAY_OF_MONTH, day1 - 1)

    /* val f = SimpleDateFormat("dd/MM/yyyy")
     val d = f.parse((day - 1).toString() + "/" + month + "/" + year)*/
    dpd.datePicker.maxDate = c1.timeInMillis
    //  dpd.getDatePicker().setMinDate(c.getTimeInMillis())

    /* val dateParts: List<String> = edittext.getValue().split("/")
     val day11 = dateParts[0]
     val month11 = dateParts[1]
     val year11 = dateParts[2]

     val c2 = Calendar.getInstance()
     c2.set(Calendar.YEAR, year11.toInt())
     c2.set(Calendar.MONTH, month11.toInt())
     c2.set(Calendar.DAY_OF_MONTH, day11.toInt())
     dpd.getDatePicker().setMinDate(c2.timeInMillis)*/
    dpd.show()
}


fun showNextFromStartDateTimePicker(
    requireActivity: Activity,
    edittext: EditText,
    startDate: String
) {

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(
        requireActivity,
        { _, year, monthOfYear, dayOfMonth ->

            val selectedMonth: String
            val selectedDay: String
            if (dayOfMonth < 10) {
                selectedDay = "0" + dayOfMonth
            } else
                selectedDay = dayOfMonth.toString()


            if (monthOfYear < 10) {
                selectedMonth = "0" + (monthOfYear + 1)
            } else
                selectedMonth = monthOfYear.toString()

            edittext.setText("" + selectedDay + "/" + selectedMonth + "/" + year)
        },
        year,
        month,
        day
    )

    val f = SimpleDateFormat("dd/MM/yyyy")
    val d = f.parse(startDate)

    if (d != null) {
        dpd.datePicker.minDate = d.time
    }

    dpd.show()
}

fun Context.getColorCompat(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)

