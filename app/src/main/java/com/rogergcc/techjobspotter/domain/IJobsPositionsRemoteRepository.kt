package com.rogergcc.techjobspotter.domain

import com.rogergcc.techjobspotter.domain.model.JobPosition


/**
 * Created on agosto.
 * year 2023 .
 */

interface IJobsPositionsRemoteRepository {
    suspend fun geJobs(): List<JobPosition>
    suspend fun searchJobs(query: String): List<JobPosition>

}