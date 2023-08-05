package com.rogergcc.techjobspotter.domain


/**
 * Created on agosto.
 * year 2023 .
 */
data class Job(
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
    val tags: List<String?>? = listOf(),
    val title: String? = "",
    val url: String? = "",
)