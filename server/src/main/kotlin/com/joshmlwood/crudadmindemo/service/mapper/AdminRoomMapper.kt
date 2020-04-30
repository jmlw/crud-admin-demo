package com.joshmlwood.crudadmindemo.service.mapper

import com.joshmlwood.crudadmindemo.domain.Room
import com.joshmlwood.crudadmindemo.service.dto.AdminRoomDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring", uses = [FeatureAutocompleteMapper::class])
interface AdminRoomMapper:
    EntityMapper<AdminRoomDTO, Room>
