package com.rogergcc.techjobspotter.ui.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * Created on enero.
 * year 2025 .
 */
@Parcelize
data class JobPositionUi(
    val candidateRequiredLocation: String? = "",
    val category: String? = "",
    val companyLogo: String? = "",
    val companyLogoUrl: String? = "",
    val companyName: String? = "",
    val description: String? = "",
    val id: Int? = 0,
    val jobType: String? = "",
    val publicationDate: String? = "",
    val salary: String? = "",
    val tags: List<String>? = listOf(),
    val title: String? = "",
    val url: String? = "",
    var isMarked: Boolean = false,
) : Parcelable