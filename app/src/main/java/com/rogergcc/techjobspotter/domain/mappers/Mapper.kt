package com.rogergcc.techjobspotter.domain.mappers


/**
 * Created on agosto.
 * year 2023 .
 */
abstract class Mapper<Domain, Dto, Entity> {

    // dto-domain mappings, mandatory to implement
    abstract fun dtoToDomain(dto: Dto): Domain
    abstract fun domainToDto(domain: Domain): Dto

    // dto-domain list mapping support
    fun dtoToDomain(dtoList: List<Dto>?): List<Domain> = (dtoList ?: emptyList()).map(::dtoToDomain)
    fun domainToDto(domainList: List<Domain>?): List<Dto> =
        (domainList ?: emptyList()).map(::domainToDto)

    // entity-domain mappings, mandatory to implement
    open fun entityToDomain(entity: Entity): Domain =
        throw NotImplementedError("override and implement this method")

    open fun domainToEntity(domain: Domain): Entity =
        throw NotImplementedError("override and implement this method")

    // entity-domain list mapping support
    fun entityToDomain(domainList: List<Entity>): List<Domain> = domainList.map(::entityToDomain)
    fun domainToEntity(domainList: List<Domain>): List<Entity> = domainList.map(::domainToEntity)

    // entity-dto mappings, mandatory to implement
    open fun entityToDto(entity: Entity): Dto =
        throw NotImplementedError("override and implement this method")

    open fun dtoToEntity(dto: Dto): Entity =
        throw NotImplementedError("override and implement this method")

    // entity-dto list mapping support
    fun entityToDto(dtoList: List<Entity>): List<Dto> = dtoList.map(::entityToDto)
    fun dtoToEntity(dtoList: List<Dto>): List<Entity> = dtoList.map(::dtoToEntity)

}