package com.joshmlwood.crudadmindemo.repository

import com.joshmlwood.crudadmindemo.domain.Feature
import org.springframework.data.repository.PagingAndSortingRepository

interface FeatureRepository : PagingAndSortingSpecificationRepository<Feature, Long>
