package com.rogergcc.techjobspotter.data.cache

import android.util.Log
import com.rogergcc.techjobspotter.data.cache.database.JobDao
import com.rogergcc.techjobspotter.domain.IJobsPositionsCacheRepository
import com.rogergcc.techjobspotter.domain.mappers.JobsMapperProvider
import com.rogergcc.techjobspotter.domain.model.JobPosition


/**
 * Created on enero.
 * year 2025 .
 */
class JobsPositionCache(
    private val jobDao: JobDao,
    private val jobsMapperProvider: JobsMapperProvider
): IJobsPositionsCacheRepository {
    override suspend fun getAllJobs(): List<JobPosition> {
        try {
            val jobsCache = jobDao.getAllJobs()
            return  jobsMapperProvider.provider().listEntityToDomain(jobsCache)
        } catch (e: Exception) {
            Log.e(TAG, "geJobs: exception: ${e.message}" )
            return emptyList()
        }
    }

    override suspend fun insertJob(job: JobPosition) {
        try {
            jobDao.insertJob(jobsMapperProvider.provider().domainToEntity(job))
        } catch (e: Exception) {
            Log.e(TAG, "insertJob: exception: ${e.message}" )
        }
    }

    override suspend fun getJobById(id: Int): JobPosition {
        try {
            val job = jobDao.getJobById(id)
            job?.let {
                return jobsMapperProvider.provider().entityToDomain(it)
            } ?: run {
                Log.e(TAG, "getJobById: Job not found for id: $id")
                return JobPosition()
            }
        } catch (e: Exception) {
            Log.e(TAG, "getJobById: exception: ${e.message}" )
            return JobPosition()
        }
    }

    override suspend fun deleteJob(job: JobPosition) {
        try {
            jobDao.deleteJob(jobsMapperProvider.provider().domainToEntity(job))
        } catch (e: Exception) {
            Log.e(TAG, "deleteJob: exception: ${e.message}" )
        }
    }

    companion object {
        private const val TAG = "JobsPositionCache"
    }
}