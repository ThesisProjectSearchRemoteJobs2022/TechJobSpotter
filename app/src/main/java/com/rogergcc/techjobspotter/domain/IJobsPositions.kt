package com.rogergcc.techjobspotter.domain

import com.rogergcc.techjobspotter.domain.model.JobPosition


/**
 * Created on agosto.
 * year 2023 .
 */

interface IJobsPositions {
//    suspend fun geJobs(): List<JobDto>
    suspend fun geJobs(): List<JobPosition>
}