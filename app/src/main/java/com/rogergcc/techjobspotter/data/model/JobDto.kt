package com.rogergcc.techjobspotter.data.model

import com.google.gson.annotations.SerializedName

data class JobDto(
    @SerializedName("candidate_required_location")
    val candidateRequiredLocation: String? = "",
    @SerializedName("category")
    val category: String? = "",
    @SerializedName("company_logo") // data ok
    val companyLogo: String? = "",
//    @SerializedName("company_logo_url") // sometimes no data
//    val companyLogoUrl: String? = "",
    @SerializedName("company_name")
    val companyName: String? = "",
    @SerializedName("description")
    val description: String? = "",
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("job_type")
    val jobType: String? = "",
    @SerializedName("publication_date")
    val publicationDate: String? = "",
    @SerializedName("salary")
    val salary: String? = "",
    @SerializedName("tags")
    val tags: List<String>? = listOf(),
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("url")
    val url: String? = "",
)