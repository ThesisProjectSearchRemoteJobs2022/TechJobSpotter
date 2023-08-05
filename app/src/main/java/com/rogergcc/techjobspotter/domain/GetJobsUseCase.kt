package com.rogergcc.techjobspotter.domain


/**
 * Created on agosto.
 * year 2023 .
 */
class GetJobsUseCase(
    private val jobMapper: JobsMapper,
    private val remote: JobRemoteDataSource
) {

    suspend operator fun invoke(): List<Job> = remote.geJobs().map(jobMapper::dtoToDomain)

}