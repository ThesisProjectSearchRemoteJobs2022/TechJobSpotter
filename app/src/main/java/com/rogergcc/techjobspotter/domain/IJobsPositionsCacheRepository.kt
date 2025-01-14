package com.rogergcc.techjobspotter.domain

import com.rogergcc.techjobspotter.domain.model.JobPosition


/**
 * Created on agosto.
 * year 2023 .
 */

interface IJobsPositionsCacheRepository {
    suspend fun getAllJobs(): List<JobPosition>
    suspend fun insertJob(job: JobPosition)
    suspend fun getJobById(id: Int): JobPosition
    suspend fun deleteJob(job: JobPosition)
}