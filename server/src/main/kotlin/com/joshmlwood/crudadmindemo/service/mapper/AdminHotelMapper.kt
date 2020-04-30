package com.joshmlwood.crudadmindemo.service.mapper

import com.joshmlwood.crudadmindemo.domain.Feature
import com.joshmlwood.crudadmindemo.domain.Hotel
import com.joshmlwood.crudadmindemo.repository.FeatureRepository
import com.joshmlwood.crudadmindemo.repository.HotelRepository
import com.joshmlwood.crudadmindemo.service.dto.AdminAutocompleteDTO
import com.joshmlwood.crudadmindemo.service.dto.AdminHotelDTO
import org.mapstruct.Mapper
import org.springframework.beans.factory.annotation.Autowired

@Mapper(componentModel = "spring", uses = [RoomAutocompleteMapper::class])
interface AdminHotelMapper:
    EntityMapper<AdminHotelDTO, Hotel>

@Mapper(componentModel = "spring", uses = [])
abstract class HotelAutocompleteMapper: EntityMapper<AdminAutocompleteDTO, Hotel> {
    @Autowired
    private lateinit var repository: HotelRepository

    override fun toEntity(dto: AdminAutocompleteDTO): Hotel {
        return repository.findById(dto.id.toLong()).orElse(null)
    }

    override fun toDto(entity: Hotel): AdminAutocompleteDTO {
        return AdminAutocompleteDTO(
            entity.id.toString(),
            "${entity.name} ${entity.address}",
            entity::class.simpleName?.toLowerCase() ?: ""
        )
    }
}
