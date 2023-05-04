package com.uniplat.uniplatapi.component.dsl

abstract class Condition {

    fun and(initializer: Condition.() -> Unit) {
        addCondition(And().apply(initializer))
    }

    fun or(initializer: Condition.() -> Unit) {
        addCondition(Or().apply(initializer))
    }

    infix fun String.eq(value: Any?) {
        addCondition(Eq(this, value))
    }

    infix fun String.like(value: String) {
        addCondition(Like(this, value))
    }

    infix fun String.ilike(value: String) {
        addCondition(ILike(this, value))
    }

    protected abstract fun addCondition(condition: Condition)
}

open class CompositeCondition(private val sqlOperator: String) : Condition() {

    private val conditions = mutableListOf<Condition>()

    override fun addCondition(condition: Condition) {
        conditions += condition
    }

    override fun toString(): String {
        return if (conditions.size == 1) {
            conditions.first().toString()
        } else {
            conditions.joinToString(prefix = "(", postfix = ")", separator = " $sqlOperator ")
        }
    }
}

class And : CompositeCondition("and")

class Or : CompositeCondition("or")

class Eq(private val column: String, private val value: Any?) : Condition() {

    init {
        if (value != null && value !is Number && value !is String) {
            throw IllegalArgumentException("Only <null>, numbers and strings values can be used in the 'where' clause")
        }
    }

    override fun addCondition(condition: Condition) {
        throw IllegalStateException("Can't add a nested condition to the sql 'eq'")
    }

    override fun toString(): String {
        return when (value) {
            null -> "$column is null"
            is String -> "$column = '$value'"
            else -> "$column = $value"
        }
    }
}

class OrderBy : CompositeCondition("order by") {

    private val orderConditions = mutableListOf<String>()

    fun asc(column: String) {
        orderConditions.add("$column asc")
    }

    fun desc(column: String) {
        orderConditions.add("$column desc")
    }

    override fun toString(): String {
        return orderConditions.joinToString(", ")
    }
}

class Like(private val column: String, private val value: String) : Condition() {

    override fun addCondition(condition: Condition) {
        throw IllegalStateException("Can't add a nested condition to the sql 'like'")
    }

    override fun toString(): String {
        return "$column like '$value'"
    }
}

class ILike(private val column: String, private val value: String) : Condition() {

    override fun addCondition(condition: Condition) {
        throw IllegalStateException("Can't add a nested condition to the sql 'ilike'")
    }

    override fun toString(): String {
        return "$column ilike '$value'"
    }
}

class SqlSelectBuilder {

    private val columns = mutableListOf<String>()
    private lateinit var table: String
    private var condition: Condition? = null
    private var limit: Int? = null
    private var offset: Int? = null
    private val orderConditions = OrderBy()

    fun select(vararg columns: String) {
        if (columns.isEmpty()) {
            throw IllegalArgumentException("At least one column should be defined")
        }
        if (this.columns.isNotEmpty()) {
            throw IllegalStateException("Detected an attempt to re-define columns to fetch. Current columns list: ${this.columns}, new columns list: $columns")
        }
        this.columns.addAll(columns)
    }

    fun from(table: String) {
        this.table = table
    }

    fun where(initializer: Condition.() -> Unit) {
        condition = And().apply(initializer)
    }

    fun limit(limit: Int) {
        this.limit = limit
    }

    fun offset(offset: Int) {
        this.offset = offset
    }

    fun orderBy(initializer: OrderBy.() -> Unit) {
        orderConditions.initializer()
    }

    override fun toString(): String {
        val columnsToFetch = if (columns.isEmpty()) "*" else columns.joinToString(", ")
        val conditionString = if (condition == null) "" else " where $condition"
        val orderByString = if (orderConditions.toString().isEmpty()) "" else " order by $orderConditions"
        val limitString = if (limit == null) "" else " limit $limit"
        val offsetString = if (offset == null) "" else " offset $offset"
        return "select $columnsToFetch from $table$conditionString$orderByString$limitString$offsetString"
    }

    fun toCountQuery(): String {
        val conditionString = if (condition == null) "" else " where $condition"
        return "select count(*) from $table$conditionString"
    }
}

fun query(initializer: SqlSelectBuilder.() -> Unit): SqlSelectBuilder {
    return SqlSelectBuilder().apply(initializer)
}
