package com.rogergcc.techjobspotter.domain.usecase

import com.rogergcc.techjobspotter.domain.IJobsPositionsCacheRepository
import com.rogergcc.techjobspotter.domain.IJobsPositionsRemoteRepository
import com.rogergcc.techjobspotter.domain.model.JobPosition


/**
 * Created on agosto.
 * year 2023 .
 */
class JobsPositionUseCase(
    private val remoteJosPositionRepository: IJobsPositionsRemoteRepository,
    private val cacheJobsPositionCache: IJobsPositionsCacheRepository
) {
    suspend fun getJobsApi(): List<JobPosition> = remoteJosPositionRepository.geJobs()

    suspend fun getJobsPositionCache(): List<JobPosition> = cacheJobsPositionCache.getAllJobs()

    suspend fun insertJobCache(job: JobPosition) = cacheJobsPositionCache.insertJob(job)

    suspend fun getJobByIdCache(id: Int): JobPosition = cacheJobsPositionCache.getJobById(id)

    suspend fun deleteJobCache(job: JobPosition) = cacheJobsPositionCache.deleteJob(job)

}