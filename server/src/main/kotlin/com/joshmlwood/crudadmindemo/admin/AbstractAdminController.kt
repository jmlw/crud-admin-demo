package com.joshmlwood.crudadmindemo.admin

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.joshmlwood.crudadmindemo.service.dto.AdminAutocompleteDTO
import com.joshmlwood.crudadmindemo.service.mapper.EntityMapper
import com.joshmlwood.crudadmindemo.admin.errors.EntityNotFoundException
import com.joshmlwood.crudadmindemo.repository.PagingAndSortingSpecificationRepository
import com.joshmlwood.crudadmindemo.service.QueryService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.Serializable
import java.time.Instant
import java.util.ArrayList
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
annotation class ReadOnly

@Target(AnnotationTarget.FIELD)
annotation class PrimaryKey(
    val writeOnCreate: Boolean = false
)

enum class Cardinality {
    TO_ONE,
    TO_MANY
}

@Target(AnnotationTarget.FIELD)
annotation class Relation(val relationEntity: String, val cardinality: Cardinality = Cardinality.TO_ONE)

enum class ReactFieldType {
    STRING,
    NUMBER,
    BOOLEAN,
    DATE,
    TIME,
    DATE_TIME,
    TO_ONE,
    TO_MANY
}

val FieldTypeMap = mapOf(
    Long::class to ReactFieldType.NUMBER,
    String::class to ReactFieldType.STRING,
    Instant::class to ReactFieldType.DATE_TIME,
    Boolean::class to ReactFieldType.BOOLEAN
)

data class AdminEntityConfig(
    var isReadOnly: Boolean = false,
    var fields: List<AdminFieldConfig> = emptyList()
)

data class AdminFieldConfig(
    var isPrimaryKey: Boolean = false,
    var isReadOnly: Boolean = false,
    var isWriteOnCreate: Boolean = false,
    var type: ReactFieldType = ReactFieldType.STRING,
    var relationEntity: String? = null,
    var name: String
)

@RestController
@Transactional
abstract class AbstractAdminController<ENTITY : Any, ID : Serializable, DTO : Any>(
    private val mapper: EntityMapper<DTO, ENTITY>,
    private val repository: PagingAndSortingSpecificationRepository<ENTITY, ID>
): QueryService<ENTITY>() {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    fun entityClass(): KClass<ENTITY> {
        throw NotImplementedError("This method must be implemented")
    }

    fun dtoClass(): KClass<DTO> {
        throw NotImplementedError("This method must be implemented")
    }

    fun buildCriteriaQuery(query: String): Specification<ENTITY> {
        throw NotImplementedError("This method must be implemented")
    }

    protected fun toAutocompleteDto(entity: ENTITY): AdminAutocompleteDTO {
        throw NotImplementedError("This method must be implemented")
    }

    @GetMapping("")
    fun list(page: Pageable, @RequestParam(required = false, name = "q") query: String?): Page<DTO> {
        if (query != null) {
            try {
                val specification = buildCriteriaQuery(query)
                return repository.findAll(specification, page).map { mapper.toDto(it) }
            } catch (e: NotImplementedError) {
                log.warn("Criteria query failed or is not supported with [$query] supplied")
            }
        }
        return repository.findAll(page).map { mapper.toDto(it) }
    }

    @GetMapping("/autocomplete")
    fun autocomplete(page: Pageable, @RequestParam(required = false, name = "q") query: String?): Page<AdminAutocompleteDTO> {
        if (query != null) {
            try {
                val specification = buildCriteriaQuery(query)
                return repository.findAll(specification, page).map { toAutocompleteDto(it) }
            } catch (e: NotImplementedError) {
                log.warn("Criteria query failed or is not supported with [$query] supplied")
            }
        }
        return repository.findAll(page).map { toAutocompleteDto(it) }
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: ID): DTO? {
        return repository.findById(id).map { mapper.toDto(it) }.orElseThrow {
            EntityNotFoundException(
                entityClass(),
                id
            )
        }
    }

    @GetMapping("/config")
    fun config(): AdminEntityConfig {
        val serializedProperties = mutableListOf<String>()
        objectMapper.readTree(objectMapper.writeValueAsString(dtoClass().createInstance())).fieldNames().forEachRemaining { serializedProperties.add(it) }
        return AdminEntityConfig(
            // when the class is an insert only entity
            dtoClass().java.isAnnotationPresent(ReadOnly::class.java),
            dtoClass().memberProperties
                .map {
                    getAdminFieldConfig(it)
                }.sortedBy { serializedProperties.indexOf(it.name) }
        )
    }

    @PostMapping("")
    fun save(@RequestBody dto: DTO): DTO {
        return mapper.toDto(repository.save(mapper.toEntity(dto)))
    }

    @PutMapping("")
    fun saveAll(@RequestBody dtos: String): MutableList<DTO> {
        val entities = this.mapJsonToObjectList<ENTITY>(dtos, entityClass().java)
        return if (entities != null) {
            mapper.toDto(repository.saveAll(entities).toMutableList())
        } else {
            mutableListOf()
        }
    }

    @Throws(Exception::class)
    private fun <T> mapJsonToObjectList(
        json: String?,
        clazz: Class<*>?
    ): List<T>? {
        val list: List<T>
        val t: TypeFactory = TypeFactory.defaultInstance()
        list = objectMapper.readValue(json, t.constructCollectionType(ArrayList::class.java, clazz))
        return list
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: ID, @RequestBody dto: DTO): DTO {
        return mapper.toDto(repository.save(mapper.toEntity(dto)))
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable("id") id: ID) {
        if (repository.existsById(id)) {
            repository.deleteById(id)
        } else {
            throw EntityNotFoundException(
                entityClass(),
                id
            )
        }
    }

    private fun getAdminFieldConfig(it: KProperty1<DTO, *>): AdminFieldConfig {
        val config = AdminFieldConfig(name = it.name)
        config.type = FieldTypeMap.getOrDefault(it.returnType.classifier, ReactFieldType.STRING)
        val javaField = it.javaField!!
        if (javaField.isAnnotationPresent(PrimaryKey::class.java)) {
            val annotation = javaField.getAnnotation(PrimaryKey::class.java)
            config.isPrimaryKey = true
            config.isWriteOnCreate = annotation.writeOnCreate
        }
        if (javaField.isAnnotationPresent(Relation::class.java)) {
            val annotation = javaField.getAnnotation(Relation::class.java)
            config.relationEntity = annotation.relationEntity
            config.type = ReactFieldType.valueOf(annotation.cardinality.name)
        }
        if (javaField.isAnnotationPresent(ReadOnly::class.java)) {
            config.isReadOnly = true
        }
        return config
    }
}
