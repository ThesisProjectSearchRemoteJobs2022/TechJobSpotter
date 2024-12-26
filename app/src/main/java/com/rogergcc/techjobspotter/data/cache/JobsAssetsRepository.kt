package com.rogergcc.techjobspotter.data.cache

import com.rogergcc.techjobspotter.data.cloud.model.RemoteJobsResponse
import com.rogergcc.techjobspotter.domain.IJobsPositions
import com.rogergcc.techjobspotter.domain.mappers.JobsMapperProvider
import com.rogergcc.techjobspotter.domain.model.JobPosition
import com.rogergcc.techjobspotter.ui.utils.loadJSONFromAsset
import com.rogergcc.techjobspotter.ui.utils.provider.ContextProvider


/**
 * Created on agosto.
 * year 2023 .
 */

class JobsAssetsRepository(
    private val contextProvider: ContextProvider,
    private val jobsMapperProvider: JobsMapperProvider,
) :
//    IJobsPositions, IJobsAssetsDataSource {
    IJobsPositions {

    override suspend fun geJobs(): List<JobPosition> {
        return try {
            val remoteJobsResponse: RemoteJobsResponse? = contextProvider.getContext() .loadJSONFromAsset("mock_response.json")
            val jobsDto = remoteJobsResponse?.jobDtos
            jobsDto?.mapNotNull { jobDto ->
                jobDto?.let { jobsMapperProvider.getJobsMapper().dtoToDomain(it) }
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList<JobPosition>()
        }
    }

}