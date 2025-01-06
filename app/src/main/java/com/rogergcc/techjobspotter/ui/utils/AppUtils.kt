package com.rogergcc.techjobspotter.ui.utils

import android.text.Spanned
import androidx.core.text.HtmlCompat
import androidx.core.text.toSpanned


/**
 * Created on noviembre.
 * year 2023 .
 */
object AppUtils {

    fun String?.html(): Spanned {
        return getHtmlText(this ?: return "".toSpanned())
    }

    private fun getHtmlText(text: String): Spanned {
        return try {
            // I have no idea if this can throw any error, but I dont want to try
            HtmlCompat.fromHtml(
                text, HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        } catch (e: Exception) {
            text.toSpanned()
        }
    }
}