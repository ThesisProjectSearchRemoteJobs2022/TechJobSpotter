package com.rogergcc.techjobspotter.data.cache.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created on enero.
 * year 2025 .
 */
@Entity(tableName = "jobs_table")
data class JobEntity(
    @PrimaryKey val idKey: String,
    val id: Int,
    val title: String,
    val description: String,

    val companyName: String,
    val companyLogoUrl: String,
    val publicationDate: String,
    val salary: String,
    val url: String,
    val tags: List<String>,
    val jobType: String,
    val candidateRequiredLocation: String,
    val category: String,
)