package com.rogergcc.techjobspotter.domain.mappers

import com.rogergcc.techjobspotter.data.cache.database.JobEntity


/**
 * Created on enero.
 * year 2025 .
 */
interface Mapper<Dto, Domain, Presentation> {
    fun dtoToDomain(dto: Dto): Domain
    fun domainToDto(domain: Domain): Dto
    fun domainToPresentation(domain: Domain): Presentation
    fun presentationToDomain(presentation: Presentation): Domain
    fun domainToEntity(domain: Domain): JobEntity
    fun entityToDomain(entity: JobEntity): Domain

    fun listDtoToDomain(listDto: List<Dto>): List<Domain> {
        return listDto.map { dtoToDomain(it) }
    }
    fun listDomainToDto(listDomain: List<Domain>): List<Dto> {
        return listDomain.map { domainToDto(it) }
    }
    fun listDomainToPresentation(listDomain: List<Domain>): List<Presentation> {
        return listDomain.map { domainToPresentation(it) }
    }
    fun listPresentationToDomain(listPresentation: List<Presentation>): List<Domain> {
        return listPresentation.map { presentationToDomain(it) }
    }
    fun listDomainToEntity(listDomain: List<Domain>): List<JobEntity> {
        return listDomain.map { domainToEntity(it) }
    }
    fun listEntityToDomain(listEntity: List<JobEntity>): List<Domain> {
        return listEntity.map { entityToDomain(it) }
    }

}