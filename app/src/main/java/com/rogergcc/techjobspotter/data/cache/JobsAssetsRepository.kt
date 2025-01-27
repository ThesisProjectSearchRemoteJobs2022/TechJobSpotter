package com.rogergcc.techjobspotter.data.cache

import com.rogergcc.techjobspotter.data.model.RemoteJobsResponse
import com.rogergcc.techjobspotter.domain.IJobsPositionsRemoteRepository
import com.rogergcc.techjobspotter.domain.mappers.JobsMapperProvider
import com.rogergcc.techjobspotter.domain.model.JobPosition
import com.rogergcc.techjobspotter.ui.provider.ContextProvider
import com.rogergcc.techjobspotter.ui.utils.loadJSONFromAsset


/**
 * Created on agosto.
 * year 2023 .
 */

class JobsAssetsRepository(
    private val contextProvider: ContextProvider,
    private val jobsMapperProvider: JobsMapperProvider,
) :
//    IJobsPositionsRemoteRepository, IJobsAssetsDataSource {
    IJobsPositionsRemoteRepository {

    override suspend fun geJobs(): List<JobPosition> {
        return try {
            val remoteJobsResponse: RemoteJobsResponse? = contextProvider.getContext() .loadJSONFromAsset("mock_response.json")
            val jobsDto = remoteJobsResponse?.jobsData
            jobsDto?.mapNotNull { jobDto ->
                jobDto?.let { jobsMapperProvider.provider().dtoToDomain(it) }
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList<JobPosition>()
        }
    }

}