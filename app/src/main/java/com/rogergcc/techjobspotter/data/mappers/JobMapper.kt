package com.rogergcc.techjobspotter.data.mappers


/**
 * Created on enero.
 * year 2025 .
 */
import com.rogergcc.techjobspotter.data.cache.database.JobEntity
import com.rogergcc.techjobspotter.data.model.JobDto
//import com.rogergcc.techjobspotter.data.model.JobDto
import com.rogergcc.techjobspotter.domain.mappers.Mapper
import com.rogergcc.techjobspotter.domain.model.JobPosition
import com.rogergcc.techjobspotter.ui.presentation.model.JobPositionUi

class JobMapper : Mapper<JobPosition, JobDto, JobPositionUi> {
    override fun dtoToDomain(dto: JobDto): JobPosition {
        return JobPosition(
            id = dto.id,
            title = dto.title,
            companyName = dto.companyName,
            companyLogoUrl = dto.companyLogoUrl,
            description = dto.description,
            publicationDate = dto.publicationDate,
            salary = dto.salary,
            url = dto.url,
            tags = dto.tags,
            jobType = dto.jobType,
            candidateRequiredLocation = dto.candidateRequiredLocation,
            category = dto.category,
            companyLogo = dto.companyLogo
        )
    }

    override fun domainToDto(domain: JobPosition): JobDto {
        return JobDto(
            id = domain.id,
            title = domain.title,
            companyName = domain.companyName,
            companyLogoUrl = domain.companyLogoUrl,
            description = domain.description,
            publicationDate = domain.publicationDate,
            salary = domain.salary,
            url = domain.url,
            tags = domain.tags,
            jobType = domain.jobType,
            candidateRequiredLocation = domain.candidateRequiredLocation,
            category = domain.category,
            companyLogo = domain.companyLogo
        )
    }

    override fun presentationToDomain(presentation: JobPositionUi): JobPosition {
        return JobPosition(
            id = presentation.id,
            title = presentation.title,
            companyName = presentation.companyName,
            companyLogoUrl = presentation.companyLogoUrl,
            description = presentation.description,
            publicationDate = presentation.publicationDate,
            salary = presentation.salary,
            url = presentation.url,
            tags = presentation.tags,
            jobType = presentation.jobType,
            candidateRequiredLocation = presentation.candidateRequiredLocation,
            category = presentation.category,
            companyLogo = presentation.companyLogo
        )
    }

    override fun domainToPresentation(domain: JobPosition): JobPositionUi {
        return JobPositionUi(
            id = domain.id,
            title = domain.title,
            companyName = domain.companyName,
            companyLogoUrl = domain.companyLogoUrl,
            description = domain.description,
            publicationDate = domain.publicationDate,
            salary = domain.salary,
            url = domain.url,
            tags = domain.tags,
            jobType = domain.jobType,
            candidateRequiredLocation = domain.candidateRequiredLocation,
            category = domain.category,
            companyLogo = domain.companyLogo
        )
    }

    override fun domainToEntity(domain: JobPosition): JobEntity {
        return JobEntity(
            idKey = domain.id.toString(),
            id = domain.id ?: 0,
            title = domain.title ?: "",
            companyName = domain.companyName ?: "",
            companyLogoUrl = domain.companyLogoUrl ?: "",
            description = domain.description ?: "",
            publicationDate = domain.publicationDate ?: "",
            salary = domain.salary ?: "",
            url = domain.url ?: "",
            tags = domain.tags ?: listOf(),
            jobType = domain.jobType ?: "",
            candidateRequiredLocation = domain.candidateRequiredLocation ?: "",
            category = domain.category ?: ""
        )
    }
}