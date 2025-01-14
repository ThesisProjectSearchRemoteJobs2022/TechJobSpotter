package com.rogergcc.techjobspotter.data.cache.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


/**
 * Created on enero.
 * year 2025 .
 */
@Dao
interface JobDao {
    @Query("SELECT * FROM jobs_table")
    suspend fun getAllJobs(): List<JobEntity>

    @Insert
    suspend fun insertJob(job: JobEntity)

    @Query("SELECT * FROM jobs_table WHERE id = :id")
    suspend fun getJobById(id: Int): JobEntity

    @Delete
    suspend fun deleteJob(job: JobEntity)

}