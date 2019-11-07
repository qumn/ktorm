/*
 * Copyright 2018-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.liuwj.ktorm.dsl

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.expression.JoinExpression
import me.liuwj.ktorm.expression.JoinType
import me.liuwj.ktorm.expression.QuerySourceExpression
import me.liuwj.ktorm.schema.BaseTable
import me.liuwj.ktorm.schema.ColumnDeclaring

/**
 * Represents a query source, used in the `from` clause of a query.
 *
 * @property database the [Database] instance that the query is running on.
 * @property sourceTable the origin source table.
 * @property joinings the joining list of this query source.
 */
data class QuerySource(val database: Database, val sourceTable: BaseTable<*>, val joinings: List<Joining> = emptyList())

/**
 * Represents a table joining.
 *
 * @property type the join type.
 * @property rightTable the right table to be joined.
 * @property condition the joining condition.
 */
data class Joining(val type: JoinType, val rightTable: BaseTable<*>, val condition: ColumnDeclaring<Boolean>? = null)

/**
 * Wrap the specific table as a [QuerySource].
 */
fun Database.from(table: BaseTable<*>): QuerySource {
    return QuerySource(this, table)
}

/**
 * Convert this query source to a [QuerySourceExpression].
 */
fun QuerySource.asExpression(): QuerySourceExpression {
    var expr: QuerySourceExpression = sourceTable.asExpression()

    for ((type, rightTable, condition) in joinings) {
        expr = JoinExpression(
            type = type,
            left = expr,
            right = rightTable.asExpression(),
            condition = condition?.asExpression()
        )
    }

    return expr
}

/**
 * Join the right table and return a new [QuerySource], translated to `cross join` in SQL.
 */
fun QuerySource.crossJoin(right: BaseTable<*>, on: ColumnDeclaring<Boolean>? = null): QuerySource {
    return this.copy(joinings = joinings + Joining(JoinType.CROSS_JOIN, right, on))
}

/**
 * Join the right table and return a new [QuerySource], translated to `inner join` in SQL.
 */
fun QuerySource.innerJoin(right: BaseTable<*>, on: ColumnDeclaring<Boolean>? = null): QuerySource {
    return this.copy(joinings = joinings + Joining(JoinType.INNER_JOIN, right, on))
}

/**
 * Join the right table and return a new [QuerySource], translated to `left join` in SQL.
 */
fun QuerySource.leftJoin(right: BaseTable<*>, on: ColumnDeclaring<Boolean>? = null): QuerySource {
    return this.copy(joinings = joinings + Joining(JoinType.LEFT_JOIN, right, on))
}

/**
 * Join the right table and return a new [QuerySource], translated to `right join` in SQL.
 */
fun QuerySource.rightJoin(right: BaseTable<*>, on: ColumnDeclaring<Boolean>? = null): QuerySource {
    return this.copy(joinings = joinings + Joining(JoinType.RIGHT_JOIN, right, on))
}

/**
 * Join the right table and return a [JoinExpression], translated to `cross join` in SQL.
 */
@Deprecated(
    message = "This function will be removed in the future. Please use db.from(...).crossJoin(...) instead.", 
    replaceWith = ReplaceWith("db.from(this).crossJoin(right, on)")
)
fun QuerySourceExpression.crossJoin(
    right: QuerySourceExpression,
    on: ColumnDeclaring<Boolean>? = null
): JoinExpression {
    return JoinExpression(type = JoinType.CROSS_JOIN, left = this, right = right, condition = on?.asExpression())
}

/**
 * Join the right table and return a [JoinExpression], translated to `cross join` in SQL.
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "This function will be removed in the future. Please use db.from(...).crossJoin(...) instead.",
    replaceWith = ReplaceWith("db.from(this).crossJoin(right, on)")
)
fun QuerySourceExpression.crossJoin(
    right: BaseTable<*>,
    on: ColumnDeclaring<Boolean>? = null
): JoinExpression {
    return crossJoin(right.asExpression(), on)
}

/**
 * Join the right table and return a [JoinExpression], translated to `cross join` in SQL.
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "This function will be removed in the future. Please use db.from(...).crossJoin(...) instead.",
    replaceWith = ReplaceWith("db.from(this).crossJoin(right, on)")
)
fun BaseTable<*>.crossJoin(
    right: QuerySourceExpression,
    on: ColumnDeclaring<Boolean>? = null
): JoinExpression {
    return asExpression().crossJoin(right, on)
}

/**
 * Join the right table and return a [JoinExpression], translated to `cross join` in SQL.
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "This function will be removed in the future. Please use db.from(...).crossJoin(...) instead.",
    replaceWith = ReplaceWith("db.from(this).crossJoin(right, on)")
)
fun BaseTable<*>.crossJoin(
    right: BaseTable<*>,
    on: ColumnDeclaring<Boolean>? = null
): JoinExpression {
    return crossJoin(right.asExpression(), on)
}

/**
 * Join the right table and return a [JoinExpression], translated to `inner join` in SQL.
 */
@Deprecated(
    message = "This function will be removed in the future. Please use db.from(...).innerJoin(...) instead.",
    replaceWith = ReplaceWith("db.from(this).innerJoin(right, on)")
)
fun QuerySourceExpression.innerJoin(
    right: QuerySourceExpression,
    on: ColumnDeclaring<Boolean>? = null
): JoinExpression {
    return JoinExpression(type = JoinType.INNER_JOIN, left = this, right = right, condition = on?.asExpression())
}

/**
 * Join the right table and return a [JoinExpression], translated to `inner join` in SQL.
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "This function will be removed in the future. Please use db.from(...).innerJoin(...) instead.",
    replaceWith = ReplaceWith("db.from(this).innerJoin(right, on)")
)
fun QuerySourceExpression.innerJoin(
    right: BaseTable<*>,
    on: ColumnDeclaring<Boolean>? = null
): JoinExpression {
    return innerJoin(right.asExpression(), on)
}

/**
 * Join the right table and return a [JoinExpression], translated to `inner join` in SQL.
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "This function will be removed in the future. Please use db.from(...).innerJoin(...) instead.",
    replaceWith = ReplaceWith("db.from(this).innerJoin(right, on)")
)
fun BaseTable<*>.innerJoin(
    right: QuerySourceExpression,
    on: ColumnDeclaring<Boolean>? = null
): JoinExpression {
    return asExpression().innerJoin(right, on)
}

/**
 * Join the right table and return a [JoinExpression], translated to `inner join` in SQL.
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "This function will be removed in the future. Please use db.from(...).innerJoin(...) instead.",
    replaceWith = ReplaceWith("db.from(this).innerJoin(right, on)")
)
fun BaseTable<*>.innerJoin(
    right: BaseTable<*>,
    on: ColumnDeclaring<Boolean>? = null
): JoinExpression {
    return innerJoin(right.asExpression(), on)
}

/**
 * Join the right table and return a [JoinExpression], translated to `left join` in SQL.
 */
@Deprecated(
    message = "This function will be removed in the future. Please use db.from(...).leftJoin(...) instead.",
    replaceWith = ReplaceWith("db.from(this).leftJoin(right, on)")
)
fun QuerySourceExpression.leftJoin(
    right: QuerySourceExpression,
    on: ColumnDeclaring<Boolean>? = null
): JoinExpression {
    return JoinExpression(type = JoinType.LEFT_JOIN, left = this, right = right, condition = on?.asExpression())
}

/**
 * Join the right table and return a [JoinExpression], translated to `left join` in SQL.
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "This function will be removed in the future. Please use db.from(...).leftJoin(...) instead.",
    replaceWith = ReplaceWith("db.from(this).leftJoin(right, on)")
)
fun QuerySourceExpression.leftJoin(
    right: BaseTable<*>,
    on: ColumnDeclaring<Boolean>? = null
): JoinExpression {
    return leftJoin(right.asExpression(), on)
}

/**
 * Join the right table and return a [JoinExpression], translated to `left join` in SQL.
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "This function will be removed in the future. Please use db.from(...).leftJoin(...) instead.",
    replaceWith = ReplaceWith("db.from(this).leftJoin(right, on)")
)
fun BaseTable<*>.leftJoin(
    right: QuerySourceExpression,
    on: ColumnDeclaring<Boolean>? = null
): JoinExpression {
    return asExpression().leftJoin(right, on)
}

/**
 * Join the right table and return a [JoinExpression], translated to `left join` in SQL.
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "This function will be removed in the future. Please use db.from(...).leftJoin(...) instead.",
    replaceWith = ReplaceWith("db.from(this).leftJoin(right, on)")
)
fun BaseTable<*>.leftJoin(
    right: BaseTable<*>,
    on: ColumnDeclaring<Boolean>? = null
): JoinExpression {
    return leftJoin(right.asExpression(), on)
}

/**
 * Join the right table and return a [JoinExpression], translated to `right join` in SQL.
 */
@Deprecated(
    message = "This function will be removed in the future. Please use db.from(...).rightJoin(...) instead.",
    replaceWith = ReplaceWith("db.from(this).rightJoin(right, on)")
)
fun QuerySourceExpression.rightJoin(
    right: QuerySourceExpression,
    on: ColumnDeclaring<Boolean>? = null
): JoinExpression {
    return JoinExpression(type = JoinType.RIGHT_JOIN, left = this, right = right, condition = on?.asExpression())
}

/**
 * Join the right table and return a [JoinExpression], translated to `right join` in SQL.
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "This function will be removed in the future. Please use db.from(...).rightJoin(...) instead.",
    replaceWith = ReplaceWith("db.from(this).rightJoin(right, on)")
)
fun QuerySourceExpression.rightJoin(
    right: BaseTable<*>,
    on: ColumnDeclaring<Boolean>? = null
): JoinExpression {
    return rightJoin(right.asExpression(), on)
}

/**
 * Join the right table and return a [JoinExpression], translated to `right join` in SQL.
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "This function will be removed in the future. Please use db.from(...).rightJoin(...) instead.",
    replaceWith = ReplaceWith("db.from(this).rightJoin(right, on)")
)
fun BaseTable<*>.rightJoin(
    right: QuerySourceExpression,
    on: ColumnDeclaring<Boolean>? = null
): JoinExpression {
    return asExpression().rightJoin(right, on)
}

/**
 * Join the right table and return a [JoinExpression], translated to `right join` in SQL.
 */
@Suppress("DEPRECATION")
@Deprecated(
    message = "This function will be removed in the future. Please use db.from(...).rightJoin(...) instead.",
    replaceWith = ReplaceWith("db.from(this).rightJoin(right, on)")
)
fun BaseTable<*>.rightJoin(
    right: BaseTable<*>,
    on: ColumnDeclaring<Boolean>? = null
): JoinExpression {
    return rightJoin(right.asExpression(), on)
}
