package com.joshmlwood.crudadmindemo.service.mapper

import com.joshmlwood.crudadmindemo.domain.Feature
import com.joshmlwood.crudadmindemo.service.dto.AdminFeatureDTO
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface AdminFeatureMapper: EntityMapper<AdminFeatureDTO, Feature>
