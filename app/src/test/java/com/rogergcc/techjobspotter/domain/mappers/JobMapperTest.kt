package com.rogergcc.techjobspotter.domain.mappers

import com.rogergcc.techjobspotter.data.mappers.JobMapper
import com.rogergcc.techjobspotter.data.model.JobDto
import com.rogergcc.techjobspotter.domain.model.JobPosition
import com.rogergcc.techjobspotter.ui.presentation.model.JobPositionUi
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created on enero.
 * year 2025 .
 */
class JobMapperTest {

    private val jobMapper = JobMapper()

    @Test
    fun `test dtoToDomain`() {
        val jobDto = JobDto(
            title = "Software Engineer",
            companyName = "Tech Company",
            companyLogoUrl = "http://logo.url",
            description = "Job description",
            publicationDate = "2023-01-01",
            salary = "1000",
            url = "http://job.url",
            tags = listOf("Kotlin", "Android"),
            jobType = "Full-time",
            candidateRequiredLocation = "Remote",
            category = "Engineering",
            companyLogo = "http://logo.url"
        )

        val jobPosition = jobMapper.dtoToDomain(jobDto)

        assertEquals(jobDto.id, jobPosition.id)
        assertEquals(jobDto.title, jobPosition.title)
        assertEquals(jobDto.companyName, jobPosition.companyName)
        assertEquals(jobDto.companyLogoUrl, jobPosition.companyLogoUrl)
        assertEquals(jobDto.description, jobPosition.description)
        assertEquals(jobDto.publicationDate, jobPosition.publicationDate)
        assertEquals(jobDto.salary, jobPosition.salary)
        assertEquals(jobDto.url, jobPosition.url)
        assertEquals(jobDto.tags, jobPosition.tags)
        assertEquals(jobDto.jobType, jobPosition.jobType)
        assertEquals(jobDto.candidateRequiredLocation, jobPosition.candidateRequiredLocation)
        assertEquals(jobDto.category, jobPosition.category)
        assertEquals(jobDto.companyLogo, jobPosition.companyLogo)
    }

    @Test
    fun `test domainToDto`() {
        val jobPosition = JobPosition(
            title = "Software Engineer",
            companyName = "Tech Company",
            companyLogoUrl = "http://logo.url",
            description = "Job description",
            publicationDate = "2023-01-01",
            salary = "1000",
            url = "http://job.url",
            tags = listOf("Kotlin", "Android"),
            jobType = "Full-time",
            candidateRequiredLocation = "Remote",
            category = "Engineering",
            companyLogo = "http://logo.url"
        )

        val jobDto = jobMapper.domainToDto(jobPosition)

        assertEquals(jobPosition.id, jobDto.id)
        assertEquals(jobPosition.title, jobDto.title)
        assertEquals(jobPosition.companyName, jobDto.companyName)
        assertEquals(jobPosition.companyLogoUrl, jobDto.companyLogoUrl)
        assertEquals(jobPosition.description, jobDto.description)
        assertEquals(jobPosition.publicationDate, jobDto.publicationDate)
        assertEquals(jobPosition.salary, jobDto.salary)
        assertEquals(jobPosition.url, jobDto.url)
        assertEquals(jobPosition.tags, jobDto.tags)
        assertEquals(jobPosition.jobType, jobDto.jobType)
        assertEquals(jobPosition.candidateRequiredLocation, jobDto.candidateRequiredLocation)
        assertEquals(jobPosition.category, jobDto.category)
        assertEquals(jobPosition.companyLogo, jobDto.companyLogo)
    }

    @Test
    fun `test presentationToDomain`() {
        val jobPresentation = JobPositionUi(
            id = 1,
            title = "Software Engineer",
            companyName = "Tech Company",
            companyLogoUrl = "http://logo.url",
            description = "Job description",
            publicationDate = "2023-01-01",
            salary = "1000",
            url = "http://job.url",
            tags = listOf("Kotlin", "Android"),
            jobType = "Full-time",
            candidateRequiredLocation = "Remote",
            category = "Engineering",
            companyLogo = "http://logo.url"
        )

        val jobPosition = jobMapper.presentationToDomain(jobPresentation)

        assertEquals(jobPresentation.id, jobPosition.id)
        assertEquals(jobPresentation.title, jobPosition.title)
        assertEquals(jobPresentation.companyName, jobPosition.companyName)
        assertEquals(jobPresentation.companyLogoUrl, jobPosition.companyLogoUrl)
        assertEquals(jobPresentation.description, jobPosition.description)
        assertEquals(jobPresentation.publicationDate, jobPosition.publicationDate)
        assertEquals(jobPresentation.salary, jobPosition.salary)
        assertEquals(jobPresentation.url, jobPosition.url)
        assertEquals(jobPresentation.tags, jobPosition.tags)
        assertEquals(jobPresentation.jobType, jobPosition.jobType)
        assertEquals(
            jobPresentation.candidateRequiredLocation,
            jobPosition.candidateRequiredLocation
        )
        assertEquals(jobPresentation.category, jobPosition.category)
        assertEquals(jobPresentation.companyLogo, jobPosition.companyLogo)
    }

    @Test
    fun `test domainToPresentation`() {
        val jobPosition = JobPosition(
            id = 1,
            title = "Software Engineer",
            companyName = "Tech Company",
            companyLogoUrl = "http://logo.url",
            description = "Job description",
            publicationDate = "2023-01-01",
            salary = "1000",
            url = "http://job.url",
            tags = listOf("Kotlin", "Android"),
            jobType = "Full-time",
            candidateRequiredLocation = "Remote",
            category = "Engineering",
            companyLogo = "http://logo.url"
        )

        val jobPresentation = jobMapper.domainToPresentation(jobPosition)

        assertEquals(jobPosition.id, jobPresentation.id)
        assertEquals(jobPosition.title, jobPresentation.title)
        assertEquals(jobPosition.companyName, jobPresentation.companyName)
        assertEquals(jobPosition.companyLogoUrl, jobPresentation.companyLogoUrl)
        assertEquals(jobPosition.description, jobPresentation.description)
        assertEquals(jobPosition.publicationDate, jobPresentation.publicationDate)
        assertEquals(jobPosition.salary, jobPresentation.salary)
        assertEquals(jobPosition.url, jobPresentation.url)
        assertEquals(jobPosition.tags, jobPresentation.tags)
        assertEquals(jobPosition.jobType, jobPresentation.jobType)
        assertEquals(
            jobPosition.candidateRequiredLocation,
            jobPresentation.candidateRequiredLocation
        )
        assertEquals(jobPosition.category, jobPresentation.category)
        assertEquals(jobPosition.companyLogo, jobPresentation.companyLogo)
    }
}