package com.joshmlwood.crudadmindemo.service.mapper

import com.joshmlwood.crudadmindemo.domain.Feature
import com.joshmlwood.crudadmindemo.repository.FeatureRepository
import com.joshmlwood.crudadmindemo.service.dto.AdminAutocompleteDTO
import org.mapstruct.Mapper
import org.springframework.beans.factory.annotation.Autowired

/**
 * TODO
 *  Unsafe handling of non-existant IDs
 *  Should fail noisily or gracefully reject objects?
 */
@Mapper(componentModel = "spring", uses = [])
abstract class FeatureIdMapper:
    EntityMapper<Long?, Feature?> {
    @Autowired
    private lateinit var repository: FeatureRepository

    override fun toEntity(dto: Long?): Feature? {
        return dto?.let { repository.findById(it).orElse(null) }
    }

    override fun toDto(entity: Feature?): Long? {
        return entity?.id
    }
}

@Mapper(componentModel = "spring", uses = [])
abstract class FeatureAutocompleteMapper: EntityMapper<AdminAutocompleteDTO, Feature> {
    @Autowired
    private lateinit var repository: FeatureRepository

    override fun toEntity(dto: AdminAutocompleteDTO): Feature {
        return repository.findById(dto.id.toLong()).orElse(null)
    }

    override fun toDto(entity: Feature): AdminAutocompleteDTO {
        return AdminAutocompleteDTO(
            entity.id.toString(),
            entity.name,
            entity::class.simpleName?.toLowerCase() ?: ""
        )
    }
}
