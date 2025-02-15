package com.rogergcc.techjobspotter.ui.utils.extensions


/**
 * Created on febrero.
 * year 2025 .
 */
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val datePatternYMDH = "yyyy-MM-dd'T'HH:mm:ss"

private const val dateFormatUi = "dd MMM, yyyy"

fun String.toFormattedDateUi(): String {
    val formatter = DateTimeFormatter.ofPattern(datePatternYMDH)
    val dateTime = LocalDateTime.parse(this, formatter)
    val outputFormatter = DateTimeFormatter.ofPattern(dateFormatUi)
    return dateTime.format(outputFormatter)
}

fun String.toFormattedDateDomain(): String {
    val formatter = DateTimeFormatter.ofPattern(datePatternYMDH)
    val dateTime = LocalDateTime.parse(this, formatter)
    val outputFormatter = DateTimeFormatter.ofPattern(dateFormatUi)
    return dateTime.format(outputFormatter)
}

fun String.toFormattedDateSpanish(): String {
//    val sdf = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.getDefault())
    val sdf = SimpleDateFormat("EEE, d MMM yyyy hh:mm aa", Locale.getDefault())
    return sdf.format(this)
}