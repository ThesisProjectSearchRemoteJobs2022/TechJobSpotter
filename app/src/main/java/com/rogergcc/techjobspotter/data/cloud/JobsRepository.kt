package com.rogergcc.techjobspotter.data.cloud

import android.content.Context
import android.util.Log
import com.rogergcc.techjobspotter.data.cloud.model.JobDto
import com.rogergcc.techjobspotter.data.cloud.model.RemoteJobsResponse
import com.rogergcc.techjobspotter.domain.IJobsAssetsDataSource
import com.rogergcc.techjobspotter.domain.IJobsPositionRepository
import com.rogergcc.techjobspotter.domain.Job
import com.rogergcc.techjobspotter.domain.JobsMapper
import com.rogergcc.techjobspotter.ui.utils.loadJSONFromAsset


/**
 * Created on agosto.
 * year 2023 .
 */
interface ContextProvider {
    fun getContext(): Context
}

interface JobsMapperProvider {
    fun getJobsMapper(): JobsMapper
}

class JobsRepository( private val contextProvider: ContextProvider,
                      private val jobsMapperProvider: JobsMapperProvider) :
    IJobsPositionRepository, IJobsAssetsDataSource {
    // Implementa la lógica para obtener los datos remotos
    override suspend fun geJobs(): List<JobDto> {
        // Código para obtener los datos remotos
        // ...

        return listOf(JobDto(), JobDto())
//        return listOf(/* lista de JobDto obtenidos desde la fuente remota */)
//        return emptyList()
    }

    override fun getJobsFromAssets(): List<Job> {
        return try {
            val remoteJobsResponse: RemoteJobsResponse? = contextProvider.getContext() .loadJSONFromAsset("mock_response.json")
            val jobsDto = remoteJobsResponse?.jobDtos
            jobsDto?.mapNotNull { jobDto ->
                jobDto?.let { jobsMapperProvider.getJobsMapper().dtoToDomain(it) }
            } ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "getJobsFromAssets: ${e.message}")
            emptyList<Job>()
        }
    }

    companion object {
        private const val TAG = "JobsRepository"
    }
}