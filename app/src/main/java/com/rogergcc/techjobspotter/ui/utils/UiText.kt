package com.rogergcc.techjobspotter.ui.utils

import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.rogergcc.techjobspotter.ui.utils.AppUtils.html


/**
 * Created on noviembre.
 * year 2023 .
 */
sealed class UiText {
    companion object {
        const val TAG = "UiText"
    }

    data class DynamicString(val value: String) : UiText() {
        override fun toString(): String = value
    }

    class StringResource(
        @StringRes val resId: Int,
        val args: List<Any>
    ) : UiText() {
        override fun toString(): String =
            "resId = $resId\nargs = ${args.toList().map { "(${it::class} = $it)" }}"
    }

    fun asStringNull(context: Context?): String? {
        try {
            return asString(context ?: return null)
        } catch (e: Exception) {
            Log.e(TAG, "Got invalid data from $this")
            return null
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> {
                val str = context.getString(resId)
                if (args.isEmpty()) {
                    str
                } else {
                    str.format(*args.map {
                        when (it) {
                            is UiText -> it.asString(context)
                            else -> it
                        }
                    }.toTypedArray())
                }
            }
        }
    }
}


fun txt(value: String): UiText {
    return UiText.DynamicString(value)
}

@JvmName("txtNull")
fun txt(value: String?): UiText? {
    return UiText.DynamicString(value ?: return null)
}

fun txt(@StringRes resId: Int, vararg args: Any): UiText {
    return UiText.StringResource(resId, args.toList())
}

@JvmName("txtNull")
fun txt(@StringRes resId: Int?, vararg args: Any?): UiText? {
    if (resId == null || args.any { it == null }) {
        return null
    }
    return UiText.StringResource(resId, args.filterNotNull().toList())
}

fun TextView?.setText(text: UiText?) {
    if (this == null) return
    if (text == null) {
        this.isVisible = false
    } else {
        val str = text.asStringNull(context)?.let {
            if (this.maxLines == 1) {
                it.replace("\n", " ")
            } else {
                it
            }
        }

        this.isGone = str.isNullOrBlank()
        this.text = str
    }
}

fun TextView?.setTextHtml(text: UiText?) {
    if (this == null) return
    if (text == null) {
        this.isVisible = false
    } else {
        val str = text.asStringNull(context)
        this.isGone = str.isNullOrBlank()
        this.text = str.html()
    }
}