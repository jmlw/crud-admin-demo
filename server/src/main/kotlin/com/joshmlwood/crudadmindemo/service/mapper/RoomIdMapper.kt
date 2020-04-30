package com.joshmlwood.crudadmindemo.service.mapper

import com.joshmlwood.crudadmindemo.domain.Feature
import com.joshmlwood.crudadmindemo.domain.Room
import com.joshmlwood.crudadmindemo.repository.FeatureRepository
import com.joshmlwood.crudadmindemo.repository.RoomRepository
import com.joshmlwood.crudadmindemo.service.dto.AdminAutocompleteDTO
import org.mapstruct.Mapper
import org.springframework.beans.factory.annotation.Autowired

@Mapper(componentModel = "spring", uses = [])
abstract class RoomIdMapper:
    EntityMapper<Long?, Room?> {
    @Autowired
    private lateinit var repository: RoomRepository

    override fun toEntity(dto: Long?): Room? {
        return dto?.let { repository.findById(it).orElse(null) }
    }

    override fun toDto(entity: Room?): Long? {
        return entity?.id
    }
}

@Mapper(componentModel = "spring", uses = [])
abstract class RoomAutocompleteMapper: EntityMapper<AdminAutocompleteDTO, Room> {
    @Autowired
    private lateinit var repository: RoomRepository

    override fun toEntity(dto: AdminAutocompleteDTO): Room {
        return repository.findById(dto.id.toLong()).orElse(null)
    }

    override fun toDto(entity: Room): AdminAutocompleteDTO {
        return AdminAutocompleteDTO(
            entity.id.toString(),
            "${entity.id} floor:${entity.floor} occupancy:${entity.occupancy} - ${entity.description}",
            entity::class.simpleName?.toLowerCase() ?: ""
        )
    }
}
