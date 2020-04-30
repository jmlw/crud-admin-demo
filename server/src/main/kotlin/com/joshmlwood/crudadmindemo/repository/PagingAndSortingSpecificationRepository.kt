package com.joshmlwood.crudadmindemo.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.PagingAndSortingRepository
import java.io.Serializable

@NoRepositoryBean
interface PagingAndSortingSpecificationRepository<ENTITY, SERIALIZABLE : Serializable>:
    PagingAndSortingRepository<ENTITY, SERIALIZABLE>,
    JpaSpecificationExecutor<ENTITY>,
    JpaRepository<ENTITY, SERIALIZABLE>
