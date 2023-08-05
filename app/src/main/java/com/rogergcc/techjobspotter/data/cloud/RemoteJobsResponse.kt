package com.rogergcc.techjobspotter.data.cloud
import com.google.gson.annotations.SerializedName
import com.rogergcc.techjobspotter.data.cloud.model.JobDto


/**
 * Created on agosto.
 * year 2023 .
 */
data class RemoteJobsResponse(
    @SerializedName("job-count")
    val jobCount: Int? = 0,
    @SerializedName("jobs")
    val jobDtos: List<JobDto?>? = listOf(),
    @SerializedName("0-legal-notice")
    val legalNotice: String? = "",
    @SerializedName("00-warning")
    val warning: String? = ""
)

