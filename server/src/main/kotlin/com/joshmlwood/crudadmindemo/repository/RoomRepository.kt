package com.joshmlwood.crudadmindemo.repository

import com.joshmlwood.crudadmindemo.domain.Room
import org.springframework.data.repository.PagingAndSortingRepository

interface RoomRepository: PagingAndSortingSpecificationRepository<Room, Long>
