package com.rogergcc.techjobspotter.domain


/**
 * Created on agosto.
 * year 2023 .
 */

interface IJobsAssetsDataSource {
    fun getJobsFromAssets(): List<Job>
}