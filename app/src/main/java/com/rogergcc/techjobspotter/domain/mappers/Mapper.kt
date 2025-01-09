package com.rogergcc.techjobspotter.domain.mappers

import com.rogergcc.techjobspotter.data.cache.database.JobEntity


/**
 * Created on enero.
 * year 2025 .
 */
interface Mapper<Domain, Dto, Presentation> {
    fun dtoToDomain(dto: Dto): Domain
    fun domainToDto(domain: Domain): Dto
    fun presentationToDomain(presentation: Presentation): Domain
    fun domainToPresentation(domain: Domain): Presentation
    fun domainToEntity(domain: Domain): JobEntity
}