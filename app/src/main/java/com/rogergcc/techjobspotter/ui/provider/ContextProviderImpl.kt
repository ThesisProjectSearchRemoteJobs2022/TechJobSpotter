package com.rogergcc.techjobspotter.ui.provider

import android.content.Context


/**
 * Created on enero.
 * year 2025 .
 */
class ContextProviderImpl(private val context: Context) : ContextProvider {
    override fun getContext(): Context = context
}