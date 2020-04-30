package com.joshmlwood.crudadmindemo.admin

import com.joshmlwood.crudadmindemo.domain.Room
import com.joshmlwood.crudadmindemo.domain.Room_
import com.joshmlwood.crudadmindemo.repository.RoomRepository
import com.joshmlwood.crudadmindemo.repository.PagingAndSortingSpecificationRepository
import com.joshmlwood.crudadmindemo.service.dto.AdminAutocompleteDTO
import com.joshmlwood.crudadmindemo.service.dto.AdminRoomDTO
import com.joshmlwood.crudadmindemo.service.filter.LongFilter
import com.joshmlwood.crudadmindemo.service.filter.StringFilter
import com.joshmlwood.crudadmindemo.service.mapper.AdminRoomMapper
import com.joshmlwood.crudadmindemo.service.mapper.EntityMapper
import com.joshmlwood.crudadmindemo.service.mapper.RoomAutocompleteMapper
import org.springframework.data.jpa.domain.Specification
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.KClass

@Transactional
@RestController
@RequestMapping("/api/admin/rooms")
class AdminRoomController(
    private val mapper: AdminRoomMapper,
    private val repository: RoomRepository,
    private val autocompleteMapper: RoomAutocompleteMapper
) : AbstractAdminController<Room, Long, AdminRoomDTO>(mapper, repository) {
    override fun entityClass(): KClass<Room> {
        return Room::class
    }

    override fun dtoClass(): KClass<AdminRoomDTO> {
        return AdminRoomDTO::class
    }

    override fun buildCriteriaQuery(query: String): Specification<Room> {
        var spec = where()
        val split = query.split(' ')
        split.forEach { s ->
            when {
                s.toLongOrNull() != null -> {
                    spec = or(spec, buildSpecification(LongFilter(equals = s.toLong()), Room_.id))
                }
                else -> {
                    val filter = StringFilter(contains = s)
                    spec = or(spec, buildSpecification(filter, Room_.description))
                }
            }
        }
        return spec
    }

    override fun toAutocompleteDto(entity: Room): AdminAutocompleteDTO {
        return autocompleteMapper.toDto(entity)
    }
}
