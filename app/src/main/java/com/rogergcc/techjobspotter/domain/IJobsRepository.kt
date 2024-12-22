package com.rogergcc.techjobspotter.domain

import com.rogergcc.techjobspotter.data.cloud.model.JobDto
import com.rogergcc.techjobspotter.domain.model.JobPosition


/**
 * Created on diciembre.
 * year 2024 .
 */
interface IJobsRepository {
 suspend fun getRemoteJobs(): List<JobDto>
 fun getJobsFromAssets(): List<JobPosition>
}