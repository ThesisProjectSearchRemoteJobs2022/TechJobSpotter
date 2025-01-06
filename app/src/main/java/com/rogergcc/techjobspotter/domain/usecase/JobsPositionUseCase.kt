package com.rogergcc.techjobspotter.domain.usecase

import com.rogergcc.techjobspotter.domain.IJobsPositions
import com.rogergcc.techjobspotter.domain.model.JobPosition


/**
 * Created on agosto.
 * year 2023 .
 */
class JobsPositionUseCase(
    private val repository: IJobsPositions,
) {
    suspend fun execute(): List<JobPosition> = repository.geJobs()

}