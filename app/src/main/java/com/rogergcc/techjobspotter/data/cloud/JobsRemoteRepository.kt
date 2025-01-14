package com.rogergcc.techjobspotter.data.cloud

import com.rogergcc.techjobspotter.data.cloud.api.RemoteJobsPositionService
import com.rogergcc.techjobspotter.domain.IJobsPositionsRemoteRepository
import com.rogergcc.techjobspotter.domain.mappers.JobsMapperProvider
import com.rogergcc.techjobspotter.domain.model.JobPosition


/**
 * Created on agosto.
 * year 2023 .
 */


class JobsRemoteRepository(
    private val jobsApiService: RemoteJobsPositionService,
    private val jobsMapperProvider: JobsMapperProvider,
) :
    IJobsPositionsRemoteRepository {
    override suspend fun geJobs(): List<JobPosition> {
        // Código para obtener los datos remotos

        try {
            val responseJobsPosition = jobsApiService.getResponseJobsPosition(20)
            val jobsList = responseJobsPosition.jobsData

            val jobsPositionDomain = jobsList?.mapNotNull { jobDto ->
                jobDto?.let { jobsMapperProvider.getJobsMapper().dtoToDomain(it) }
            } ?: emptyList()

            return jobsPositionDomain
        } catch (e: Exception) {
            return emptyList()
        }

    }

}