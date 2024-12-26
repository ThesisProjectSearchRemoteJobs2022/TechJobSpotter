package com.rogergcc.techjobspotter.data.cloud

import com.rogergcc.techjobspotter.data.cloud.model.JobDto
import com.rogergcc.techjobspotter.domain.IJobsPositions
import com.rogergcc.techjobspotter.domain.mappers.JobsMapperProvider
import com.rogergcc.techjobspotter.domain.model.JobPosition
import com.rogergcc.techjobspotter.ui.utils.provider.ContextProvider


/**
 * Created on agosto.
 * year 2023 .
 */


class JobsRemoteRepository(private val contextProvider: ContextProvider,
                           private val jobsMapperProvider: JobsMapperProvider
) :
//    IJobsPositions, IJobsAssetsDataSource {
    IJobsPositions {
    // Implementa la lógica para obtener los datos remotos
    override suspend fun geJobs(): List<JobPosition> {
        // Código para obtener los datos remotos

        return try {
            val jobDomain = jobsMapperProvider.getJobsMapper().dtoToDomain(JobDto())
            return listOf(jobDomain,jobDomain, jobDomain)
        } catch (e: Exception) {
            emptyList()
        }

    }


    companion object {
        private const val TAG = "JobsRemoteRepository"
    }
}