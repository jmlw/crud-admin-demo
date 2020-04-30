package com.joshmlwood.crudadmindemo.admin

import com.joshmlwood.crudadmindemo.domain.Hotel
import com.joshmlwood.crudadmindemo.domain.Hotel_
import com.joshmlwood.crudadmindemo.domain.Room
import com.joshmlwood.crudadmindemo.repository.HotelRepository
import com.joshmlwood.crudadmindemo.repository.PagingAndSortingSpecificationRepository
import com.joshmlwood.crudadmindemo.service.dto.AdminAutocompleteDTO
import com.joshmlwood.crudadmindemo.service.dto.AdminHotelDTO
import com.joshmlwood.crudadmindemo.service.filter.LongFilter
import com.joshmlwood.crudadmindemo.service.filter.StringFilter
import com.joshmlwood.crudadmindemo.service.mapper.AdminHotelMapper
import com.joshmlwood.crudadmindemo.service.mapper.EntityMapper
import com.joshmlwood.crudadmindemo.service.mapper.HotelAutocompleteMapper
import org.springframework.data.jpa.domain.Specification
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.KClass

@Transactional
@RestController
@RequestMapping("/api/admin/hotels")
class AdminHotelController(
    private val mapper: AdminHotelMapper,
    private val repository: HotelRepository,
    private val autocompleteMapper: HotelAutocompleteMapper
) : AbstractAdminController<Hotel, Long, AdminHotelDTO>(mapper, repository) {
    override fun entityClass(): KClass<Hotel> {
        return Hotel::class
    }

    override fun dtoClass(): KClass<AdminHotelDTO> {
        return AdminHotelDTO::class
    }

    override fun buildCriteriaQuery(query: String): Specification<Hotel> {
        var spec = where()
        val split = query.split(' ')
        split.forEach { s ->
            when {
                s.toLongOrNull() != null -> {
                    spec = or(spec, buildSpecification(LongFilter(equals = s.toLong()), Hotel_.id))
                }
                else -> {
                    val filter = StringFilter(contains = s)
                    spec = or(spec, buildSpecification(filter, Hotel_.address))
                }
            }
        }
        return spec
    }

    override fun toAutocompleteDto(entity: Hotel): AdminAutocompleteDTO {
        return autocompleteMapper.toDto(entity)
    }
}
