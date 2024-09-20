package com.rogergcc.techjobspotter.domain

import com.rogergcc.techjobspotter.data.cloud.model.JobDto


/**
 * Created on agosto.
 * year 2023 .
 */

interface IJobRemoteDataSource {
    suspend fun geJobs(): List<JobDto>
}