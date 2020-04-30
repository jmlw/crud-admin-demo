package com.joshmlwood.crudadmindemo.service

import com.joshmlwood.crudadmindemo.service.filter.Filter
import com.joshmlwood.crudadmindemo.service.filter.RangeFilter
import com.joshmlwood.crudadmindemo.service.filter.StringFilter
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Root
import javax.persistence.criteria.SetJoin
import javax.persistence.metamodel.SetAttribute
import javax.persistence.metamodel.SingularAttribute
import org.springframework.data.jpa.domain.Specification
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
abstract class QueryService<ENTITY> {

    protected fun <X> buildSpecification(
        filter: Filter<X>,
        field: SingularAttribute<in ENTITY, X>
    ): Specification<ENTITY> {
        return when {
            filter is StringFilter -> buildStringSpecification(filter, field as SingularAttribute<in ENTITY, String>)
            else -> buildSpecification(filter) { root -> root.get(field) }
        }
//      TODO: this does not behave as intended. Overloaded methods fall back to supertype for Filter
//        return buildSpecification(filter) { root -> root.get(field) }
    }

    protected fun and(first: Specification<ENTITY>, second: Specification<ENTITY>): Specification<ENTITY> =
        first.and(second)!!

    protected fun or(first: Specification<ENTITY>, second: Specification<ENTITY>): Specification<ENTITY> =
        first.or(second)!!

    protected fun where(): Specification<ENTITY> = Specification.where<ENTITY>(null)!!

    protected fun noOp(): Specification<ENTITY> =
        Specification { _, _, criteriaBuilder -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)) }

    protected fun buildStringSpecification(
        filter: StringFilter,
        field: SingularAttribute<in ENTITY, String>
    ): Specification<ENTITY> {
        return buildSpecification(filter) { root -> root.get(field) }
    }

    protected fun <JSON_TYPE> buildJsonbStringSpecification(
        filter: StringFilter,
        field: SingularAttribute<in ENTITY, JSON_TYPE>,
        jsonPath: List<String>
    ): Specification<ENTITY> {
        val jsonExtractor = { root: Root<ENTITY>, builder: CriteriaBuilder ->
            builder.function(
                "jsonb_extract_path_text",
                String::class.java,
                root.get(field),
                builder.literal(jsonPath)
            )
        }

        return when {
            filter.equals != null -> Specification { root, _, builder ->
                builder.equal(
                    jsonExtractor(root, builder),
                    filter.equals
                )
            }
            filter.`in` != null -> Specification { root, _, builder ->
                var `in` = builder.`in`(jsonExtractor(root, builder))
                for (value in filter.`in`) {
                    `in` = `in`.value(value)
                }
                `in`
            }
            filter.contains != null -> Specification { root, _, builder ->
                builder.like(
                    builder.upper(jsonExtractor(root, builder)),
                    wrapLikeQuery(filter.contains)
                )
            }
            filter.specified != null -> return if (filter.specified)
                Specification { root, _, builder -> builder.isNotNull(jsonExtractor(root, builder)) }
            else
                Specification { root, _, builder -> builder.isNull(jsonExtractor(root, builder)) }
            else -> noOp()
        }
    }

    protected fun buildJsonSpecification(
        filter: StringFilter,
        metaclassFunction: (root: Root<ENTITY>) -> Expression<String>
    ): Specification<ENTITY> {
        return when {
            filter.equals != null -> equalsSpecification(metaclassFunction, filter.equals)
            filter.`in` != null -> valueIn(metaclassFunction, filter.`in`)
            filter.contains != null -> likeUpperSpecification(metaclassFunction, filter.contains)
            filter.specified != null -> byFieldSpecified(metaclassFunction, filter.specified)
            else -> noOp()
        }
    }

    protected fun buildSpecification(
        filter: StringFilter,
        metaclassFunction: (root: Root<ENTITY>) -> Expression<String>
    ): Specification<ENTITY> {
        return when {
            filter.equals != null -> equalsSpecification(metaclassFunction, filter.equals)
            filter.`in` != null -> valueIn(metaclassFunction, filter.`in`)
            filter.contains != null -> likeUpperSpecification(metaclassFunction, filter.contains)
            filter.specified != null -> byFieldSpecified(metaclassFunction, filter.specified)
            else -> noOp()
        }
    }

    protected fun <X : Comparable<X>> buildRangeSpecification(
        filter: RangeFilter<X>,
        field: SingularAttribute<in ENTITY, X>
    ): Specification<ENTITY> {
        return buildSpecification(filter) { root -> root.get(field) }
    }

    protected fun <X : Comparable<X>> buildSpecification(
        filter: RangeFilter<X>,
        metaclassFunction: (root: Root<ENTITY>) -> Expression<X>
    ): Specification<ENTITY> {
        if (filter.equals != null) {
            return equalsSpecification(metaclassFunction, filter.equals)
        } else if (filter.`in` != null) {
            return valueIn(metaclassFunction, filter.`in`)
        }

        var result: Specification<ENTITY> = noOp()
        if (filter.specified != null) {
            result = result.and(byFieldSpecified(metaclassFunction, filter.specified))!!
        }
        if (filter.greaterThan != null) {
            result = result.and(greaterThan(metaclassFunction, filter.greaterThan))!!
        }
        if (filter.greaterThanOrEqualTo != null) {
            result = result.and(greaterThanOrEqualTo(metaclassFunction, filter.greaterThanOrEqualTo))!!
        }
        if (filter.lessThan != null) {
            result = result.and(lessThan(metaclassFunction, filter.lessThan))!!
        }
        if (filter.lessThanOrEqualTo != null) {
            result = result.and(lessThanOrEqualTo(metaclassFunction, filter.lessThanOrEqualTo))!!
        }
        return result
    }

    protected fun <X> buildSpecification(
        filter: Filter<X>,
        metaclassFunction: (root: Root<ENTITY>) -> Expression<X>
    ): Specification<ENTITY> {
        return when {
            filter.equals != null -> equalsSpecification(metaclassFunction, filter.equals)
            filter.`in` != null -> valueIn(metaclassFunction, filter.`in`)
            filter.specified != null -> byFieldSpecified(metaclassFunction, filter.specified)
            else -> noOp()
        }
    }

    protected fun <OTHER, X> buildReferringEntitySpecification(
        filter: Filter<X>,
        reference: SingularAttribute<in ENTITY, OTHER>,
        valueField: SingularAttribute<in OTHER, X>
    ): Specification<ENTITY>? {
        return buildSpecification(filter) { root -> root.get(reference).get(valueField) }
    }

    protected fun <OTHER, X> buildReferringEntitySpecification(
        filter: Filter<X>,
        reference: SetAttribute<ENTITY, OTHER>,
        valueField: SingularAttribute<OTHER, X>
    ): Specification<ENTITY>? {
        return buildReferringEntitySpecification(
            filter,
            { root -> root.join(reference) },
            { entity -> entity.get(valueField) })
    }

    protected fun <OTHER, MISC, X> buildReferringEntitySpecification(
        filter: Filter<X>,
        functionToEntity: (root: Root<ENTITY>) -> SetJoin<MISC, OTHER>,
        entityToColumn: (SetJoin<MISC, OTHER>) -> Expression<X>
    ): Specification<ENTITY>? {
        return when {
            filter.equals != null -> equalsSpecification(
                { x -> entityToColumn(functionToEntity(x)) },
                filter.equals
            )
            filter.specified != null -> byFieldSpecified(
                { root -> functionToEntity(root) },
                filter.specified
            )
            else -> null
        }
    }

    protected fun <OTHER, X : Comparable<X>> buildReferringEntitySpecification(
        filter: RangeFilter<X>,
        reference: SetAttribute<ENTITY, OTHER>,
        valueField: SingularAttribute<OTHER, X>
    ): Specification<ENTITY> {
        return buildReferringEntitySpecification(
            filter,
            { root -> root.join(reference) },
            { entity -> entity.get(valueField) })
    }

    protected fun <OTHER, MISC, X : Comparable<X>> buildReferringEntitySpecification(
        filter: RangeFilter<X>,
        functionToEntity: (root: Root<ENTITY>) -> SetJoin<MISC, OTHER>,
        entityToColumn: (SetJoin<MISC, OTHER>) -> Expression<X>
    ): Specification<ENTITY> {

        val fused: (Root<ENTITY>) -> Expression<X> = { x: Root<ENTITY> -> entityToColumn(functionToEntity(x)) }
        if (filter.equals != null) {
            return equalsSpecification(fused, filter.equals)
        } else if (filter.`in` != null) {
            return valueIn(fused, filter.`in`)
        }
        var result = noOp()
        if (filter.specified != null) {
            // 'functionToEntity' doesn't work, must use lambda formula
            result = result.and(byFieldSpecified({ root -> functionToEntity(root) }, filter.specified))!!
        }
        if (filter.greaterThan != null) {
            result = result.and(greaterThan(fused, filter.greaterThan))!!
        }
        if (filter.greaterThanOrEqualTo != null) {
            result = result.and(greaterThanOrEqualTo(fused, filter.greaterThanOrEqualTo))!!
        }
        if (filter.lessThan != null) {
            result = result.and(lessThan(fused, filter.lessThan))!!
        }
        if (filter.lessThanOrEqualTo != null) {
            result = result.and(lessThanOrEqualTo(fused, filter.lessThanOrEqualTo))!!
        }
        return result
    }

    protected fun <X> equalsSpecification(
        metaclassFunction: (root: Root<ENTITY>) -> Expression<X>,
        value: X?
    ): Specification<ENTITY> {
        return Specification { root, _, builder -> builder.equal(metaclassFunction(root), value) }
    }

    protected fun likeUpperSpecification(
        metaclassFunction: (root: Root<ENTITY>) -> Expression<String>,
        value: String
    ): Specification<ENTITY> {
        return Specification { root, _, builder ->
            builder.like(
                builder.upper(metaclassFunction(root)),
                wrapLikeQuery(value)
            )
        }
    }

    protected fun <X> byFieldSpecified(
        metaclassFunction: (root: Root<ENTITY>) -> Expression<X>,
        specified: Boolean
    ): Specification<ENTITY> {
        return if (specified)
            Specification { root, _, builder -> builder.isNotNull(metaclassFunction(root)) }
        else
            Specification { root, _, builder -> builder.isNull(metaclassFunction(root)) }
    }

    protected fun <X> byFieldEmptiness(
        metaclassFunction: (root: Root<ENTITY>) -> Expression<Set<X>>,
        specified: Boolean
    ): Specification<ENTITY> {
        return if (specified)
            Specification { root, _, builder -> builder.isNotEmpty(metaclassFunction(root)) }
        else
            Specification { root, _, builder -> builder.isEmpty(metaclassFunction(root)) }
    }

    protected fun <X> valueIn(
        metaclassFunction: (root: Root<ENTITY>) -> Expression<X>,
        values: Collection<X>
    ): Specification<ENTITY> {
        return Specification { root, _, builder ->
            var `in` = builder.`in`(metaclassFunction(root))
            for (value in values) {
                `in` = `in`.value(value)
            }
            `in`
        }
    }

    protected fun <X : Comparable<X>> greaterThanOrEqualTo(
        metaclassFunction: (root: Root<ENTITY>) -> Expression<X>,
        value: X
    ): Specification<ENTITY> {
        return Specification { root, _, builder -> builder.greaterThanOrEqualTo(metaclassFunction(root), value) }
    }

    protected fun <X : Comparable<X>> greaterThan(
        metaclassFunction: (root: Root<ENTITY>) -> Expression<X>,
        value: X
    ): Specification<ENTITY> {
        return Specification { root, _, builder -> builder.greaterThan(metaclassFunction(root), value) }
    }

    protected fun <X : Comparable<X>> lessThanOrEqualTo(
        metaclassFunction: (root: Root<ENTITY>) -> Expression<X>,
        value: X
    ): Specification<ENTITY> {
        return Specification { root, _, builder -> builder.lessThanOrEqualTo(metaclassFunction(root), value) }
    }

    protected fun <X : Comparable<X>> lessThan(
        metaclassFunction: (root: Root<ENTITY>) -> Expression<X>,
        value: X
    ): Specification<ENTITY> {
        return Specification { root, _, builder -> builder.lessThan(metaclassFunction(root), value) }
    }

    protected fun wrapLikeQuery(txt: String): String {
        return "%" + txt.toUpperCase() + "%"
    }
}
