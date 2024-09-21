package com.rogergcc.techjobspotter.domain


/**
 * Created on agosto.
 * year 2023 .
 */
class JobsPositionUseCase(
    private val repository: IJobsAssetsDataSource
) {
    fun execute(): List<Job> = repository.getJobsFromAssets()

}