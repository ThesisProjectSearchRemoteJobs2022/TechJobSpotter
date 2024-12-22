package com.rogergcc.techjobspotter.domain.usecase

import com.rogergcc.techjobspotter.domain.IJobsAssetsDataSource
import com.rogergcc.techjobspotter.domain.model.JobPosition


/**
 * Created on agosto.
 * year 2023 .
 */
class JobsPositionUseCase(
    private val repository: IJobsAssetsDataSource
) {
    fun execute(): List<JobPosition> = repository.getJobsFromAssets()

}