package com.rogergcc.techjobspotter.domain.mappers

import com.rogergcc.techjobspotter.data.mappers.JobMapper


/**
 * Created on diciembre.
 * year 2024 .
 */
interface JobsMapperProvider {
//    fun getJobsMapper(): JobsMapperAbstract
    fun getJobsMapper(): JobMapper
}