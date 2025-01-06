package com.rogergcc.techjobspotter.domain.mappers

import com.rogergcc.techjobspotter.data.cloud.model.JobDto
import com.rogergcc.techjobspotter.domain.model.JobPosition


/**
 * Created on agosto.
 * year 2023 .
 */
class JobsMapper : Mapper<JobPosition, JobDto, Unit>() {

    override fun dtoToDomain(dto: JobDto) =
        JobPosition(
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
}