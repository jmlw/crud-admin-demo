package com.joshmlwood.crudadmindemo.service.mapper

interface EntityMapper<DTO, ENTITY> : EntityToDtoMapper<DTO, ENTITY>, DtoToEntityMapper<DTO, ENTITY>

interface EntityToDtoMapper<DTO, ENTITY> {
    fun toDto(entity: ENTITY): DTO
    fun toDto(entityList: MutableList<ENTITY>): MutableList<DTO>
}

interface DtoToEntityMapper<DTO, ENTITY> {
    fun toEntity(dto: DTO): ENTITY
    fun toEntity(dtoList: MutableList<DTO>): MutableList<ENTITY>
}
