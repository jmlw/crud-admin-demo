package com.joshmlwood.crudadmindemo.repository

import com.joshmlwood.crudadmindemo.domain.Hotel
import org.springframework.data.repository.PagingAndSortingRepository

interface HotelRepository: PagingAndSortingSpecificationRepository<Hotel, Long>
