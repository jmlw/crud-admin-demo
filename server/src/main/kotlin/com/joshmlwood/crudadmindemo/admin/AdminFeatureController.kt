package com.joshmlwood.crudadmindemo.admin

import com.joshmlwood.crudadmindemo.domain.Feature
import com.joshmlwood.crudadmindemo.domain.Feature_
import com.joshmlwood.crudadmindemo.repository.FeatureRepository
import com.joshmlwood.crudadmindemo.repository.PagingAndSortingSpecificationRepository
import com.joshmlwood.crudadmindemo.service.dto.AdminAutocompleteDTO
import com.joshmlwood.crudadmindemo.service.dto.AdminFeatureDTO
import com.joshmlwood.crudadmindemo.service.filter.LongFilter
import com.joshmlwood.crudadmindemo.service.filter.StringFilter
import com.joshmlwood.crudadmindemo.service.mapper.AdminFeatureMapper
import com.joshmlwood.crudadmindemo.service.mapper.EntityMapper
import com.joshmlwood.crudadmindemo.service.mapper.FeatureAutocompleteMapper
import org.springframework.data.jpa.domain.Specification
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.KClass

@Transactional
@RestController
@RequestMapping("/api/admin/features")
class AdminFeatureController(
    private val mapper: AdminFeatureMapper,
    private val repository: FeatureRepository,
    private val autocompleteMapper: FeatureAutocompleteMapper
) : AbstractAdminController<Feature, Long, AdminFeatureDTO>(mapper, repository) {
    override fun entityClass(): KClass<Feature> {
        return Feature::class
    }

    override fun dtoClass(): KClass<AdminFeatureDTO> {
        return AdminFeatureDTO::class
    }

    override fun buildCriteriaQuery(query: String): Specification<Feature> {
        var spec = where()
        val split = query.split(' ')
        split.forEach { s ->
            when {
                s.toLongOrNull() != null -> {
                    spec = or(spec, buildSpecification(LongFilter(equals = s.toLong()), Feature_.id))
                }
                else -> {
                    val filter: StringFilter = StringFilter(contains = s)
                    spec = or(spec, buildSpecification(filter, Feature_.name))
                }
            }
        }
        return spec
    }

    override fun toAutocompleteDto(entity: Feature): AdminAutocompleteDTO {
        return autocompleteMapper.toDto(entity);
    }
}
