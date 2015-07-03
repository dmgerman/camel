begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.utils.cassandra
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|utils
operator|.
name|cassandra
package|;
end_package

begin_import
import|import
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|ConsistencyLevel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|RegularStatement
import|;
end_import

begin_import
import|import
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|querybuilder
operator|.
name|Delete
import|;
end_import

begin_import
import|import
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|querybuilder
operator|.
name|Insert
import|;
end_import

begin_import
import|import
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|querybuilder
operator|.
name|QueryBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|querybuilder
operator|.
name|Select
import|;
end_import

begin_import
import|import
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|querybuilder
operator|.
name|Truncate
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|querybuilder
operator|.
name|QueryBuilder
operator|.
name|bindMarker
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|querybuilder
operator|.
name|QueryBuilder
operator|.
name|delete
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|querybuilder
operator|.
name|QueryBuilder
operator|.
name|eq
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|querybuilder
operator|.
name|QueryBuilder
operator|.
name|insertInto
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|querybuilder
operator|.
name|QueryBuilder
operator|.
name|select
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|querybuilder
operator|.
name|QueryBuilder
operator|.
name|ttl
import|;
end_import

begin_class
DECL|class|CassandraUtils
specifier|public
specifier|final
class|class
name|CassandraUtils
block|{
DECL|method|CassandraUtils ()
specifier|private
name|CassandraUtils
parameter_list|()
block|{     }
comment|/**      * Test if the array is null or empty.      */
DECL|method|isEmpty (Object[] array)
specifier|public
specifier|static
name|boolean
name|isEmpty
parameter_list|(
name|Object
index|[]
name|array
parameter_list|)
block|{
return|return
name|array
operator|==
literal|null
operator|||
name|array
operator|.
name|length
operator|==
literal|0
return|;
block|}
comment|/**      * Concatenate 2 arrays.      */
DECL|method|concat (Object[] array1, Object[] array2)
specifier|public
specifier|static
name|Object
index|[]
name|concat
parameter_list|(
name|Object
index|[]
name|array1
parameter_list|,
name|Object
index|[]
name|array2
parameter_list|)
block|{
if|if
condition|(
name|isEmpty
argument_list|(
name|array1
argument_list|)
condition|)
block|{
return|return
name|array2
return|;
block|}
if|if
condition|(
name|isEmpty
argument_list|(
name|array2
argument_list|)
condition|)
block|{
return|return
name|array1
return|;
block|}
name|Object
index|[]
name|array
init|=
operator|new
name|Object
index|[
name|array1
operator|.
name|length
operator|+
name|array2
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|array1
argument_list|,
literal|0
argument_list|,
name|array
argument_list|,
literal|0
argument_list|,
name|array1
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|array2
argument_list|,
literal|0
argument_list|,
name|array
argument_list|,
name|array1
operator|.
name|length
argument_list|,
name|array2
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|array
return|;
block|}
DECL|method|size (String[] array)
specifier|private
specifier|static
name|int
name|size
parameter_list|(
name|String
index|[]
name|array
parameter_list|)
block|{
return|return
name|array
operator|==
literal|null
condition|?
literal|0
else|:
name|array
operator|.
name|length
return|;
block|}
DECL|method|isEmpty (String[] array)
specifier|private
specifier|static
name|boolean
name|isEmpty
parameter_list|(
name|String
index|[]
name|array
parameter_list|)
block|{
return|return
name|size
argument_list|(
name|array
argument_list|)
operator|==
literal|0
return|;
block|}
comment|/**      * Concatenate 2 arrays.      */
DECL|method|concat (String[] array1, String[] array2)
specifier|public
specifier|static
name|String
index|[]
name|concat
parameter_list|(
name|String
index|[]
name|array1
parameter_list|,
name|String
index|[]
name|array2
parameter_list|)
block|{
if|if
condition|(
name|isEmpty
argument_list|(
name|array1
argument_list|)
condition|)
block|{
return|return
name|array2
return|;
block|}
if|if
condition|(
name|isEmpty
argument_list|(
name|array2
argument_list|)
condition|)
block|{
return|return
name|array1
return|;
block|}
name|String
index|[]
name|array
init|=
operator|new
name|String
index|[
name|array1
operator|.
name|length
operator|+
name|array2
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|array1
argument_list|,
literal|0
argument_list|,
name|array
argument_list|,
literal|0
argument_list|,
name|array1
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|array2
argument_list|,
literal|0
argument_list|,
name|array
argument_list|,
name|array1
operator|.
name|length
argument_list|,
name|array2
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|array
return|;
block|}
comment|/**      * Append values to given array.      */
DECL|method|append (Object[] array1, Object... array2)
specifier|public
specifier|static
name|Object
index|[]
name|append
parameter_list|(
name|Object
index|[]
name|array1
parameter_list|,
name|Object
modifier|...
name|array2
parameter_list|)
block|{
return|return
name|concat
argument_list|(
name|array1
argument_list|,
name|array2
argument_list|)
return|;
block|}
comment|/**      * Append values to given array.      */
DECL|method|append (String[] array1, String... array2)
specifier|public
specifier|static
name|String
index|[]
name|append
parameter_list|(
name|String
index|[]
name|array1
parameter_list|,
name|String
modifier|...
name|array2
parameter_list|)
block|{
return|return
name|concat
argument_list|(
name|array1
argument_list|,
name|array2
argument_list|)
return|;
block|}
comment|/**      * Generate Insert CQL.      */
DECL|method|generateInsert (String table, String[] columns, boolean ifNotExists, Integer ttl)
specifier|public
specifier|static
name|Insert
name|generateInsert
parameter_list|(
name|String
name|table
parameter_list|,
name|String
index|[]
name|columns
parameter_list|,
name|boolean
name|ifNotExists
parameter_list|,
name|Integer
name|ttl
parameter_list|)
block|{
name|Insert
name|insert
init|=
name|insertInto
argument_list|(
name|table
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|column
range|:
name|columns
control|)
block|{
name|insert
operator|=
name|insert
operator|.
name|value
argument_list|(
name|column
argument_list|,
name|bindMarker
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ifNotExists
condition|)
block|{
name|insert
operator|=
name|insert
operator|.
name|ifNotExists
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ttl
operator|!=
literal|null
condition|)
block|{
name|insert
operator|.
name|using
argument_list|(
name|ttl
argument_list|(
name|ttl
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|insert
return|;
block|}
comment|/**      * Generate select where columns = ? CQL.      */
DECL|method|generateSelect (String table, String[] selectColumns, String[] whereColumns)
specifier|public
specifier|static
name|Select
name|generateSelect
parameter_list|(
name|String
name|table
parameter_list|,
name|String
index|[]
name|selectColumns
parameter_list|,
name|String
index|[]
name|whereColumns
parameter_list|)
block|{
return|return
name|generateSelect
argument_list|(
name|table
argument_list|,
name|selectColumns
argument_list|,
name|whereColumns
argument_list|,
name|size
argument_list|(
name|whereColumns
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Generate select where columns = ? CQL.      */
DECL|method|generateSelect (String table, String[] selectColumns, String[] whereColumns, int whereColumnsMaxIndex)
specifier|public
specifier|static
name|Select
name|generateSelect
parameter_list|(
name|String
name|table
parameter_list|,
name|String
index|[]
name|selectColumns
parameter_list|,
name|String
index|[]
name|whereColumns
parameter_list|,
name|int
name|whereColumnsMaxIndex
parameter_list|)
block|{
name|Select
name|select
init|=
name|select
argument_list|(
name|selectColumns
argument_list|)
operator|.
name|from
argument_list|(
name|table
argument_list|)
decl_stmt|;
if|if
condition|(
name|isWhereClause
argument_list|(
name|whereColumns
argument_list|,
name|whereColumnsMaxIndex
argument_list|)
condition|)
block|{
name|Select
operator|.
name|Where
name|where
init|=
name|select
operator|.
name|where
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|whereColumns
operator|.
name|length
operator|&&
name|i
operator|<
name|whereColumnsMaxIndex
condition|;
name|i
operator|++
control|)
block|{
name|where
operator|.
name|and
argument_list|(
name|eq
argument_list|(
name|whereColumns
index|[
name|i
index|]
argument_list|,
name|bindMarker
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|select
return|;
block|}
comment|/**      * Generate delete where columns = ? CQL.      */
DECL|method|generateDelete (String table, String[] whereColumns, boolean ifExists)
specifier|public
specifier|static
name|Delete
name|generateDelete
parameter_list|(
name|String
name|table
parameter_list|,
name|String
index|[]
name|whereColumns
parameter_list|,
name|boolean
name|ifExists
parameter_list|)
block|{
return|return
name|generateDelete
argument_list|(
name|table
argument_list|,
name|whereColumns
argument_list|,
name|size
argument_list|(
name|whereColumns
argument_list|)
argument_list|,
name|ifExists
argument_list|)
return|;
block|}
comment|/**      * Generate delete where columns = ? CQL.      */
DECL|method|generateDelete (String table, String[] whereColumns, int whereColumnsMaxIndex, boolean ifExists)
specifier|public
specifier|static
name|Delete
name|generateDelete
parameter_list|(
name|String
name|table
parameter_list|,
name|String
index|[]
name|whereColumns
parameter_list|,
name|int
name|whereColumnsMaxIndex
parameter_list|,
name|boolean
name|ifExists
parameter_list|)
block|{
name|Delete
name|delete
init|=
name|delete
argument_list|()
operator|.
name|from
argument_list|(
name|table
argument_list|)
decl_stmt|;
if|if
condition|(
name|isWhereClause
argument_list|(
name|whereColumns
argument_list|,
name|whereColumnsMaxIndex
argument_list|)
condition|)
block|{
name|Delete
operator|.
name|Where
name|where
init|=
name|delete
operator|.
name|where
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|whereColumns
operator|.
name|length
operator|&&
name|i
operator|<
name|whereColumnsMaxIndex
condition|;
name|i
operator|++
control|)
block|{
name|where
operator|.
name|and
argument_list|(
name|eq
argument_list|(
name|whereColumns
index|[
name|i
index|]
argument_list|,
name|bindMarker
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ifExists
condition|)
block|{
name|delete
operator|=
name|delete
operator|.
name|ifExists
argument_list|()
expr_stmt|;
block|}
return|return
name|delete
return|;
block|}
DECL|method|isWhereClause (String[] whereColumns, int whereColumnsMaxIndex)
specifier|private
specifier|static
name|boolean
name|isWhereClause
parameter_list|(
name|String
index|[]
name|whereColumns
parameter_list|,
name|int
name|whereColumnsMaxIndex
parameter_list|)
block|{
return|return
operator|!
name|isEmpty
argument_list|(
name|whereColumns
argument_list|)
operator|&&
name|whereColumnsMaxIndex
operator|>
literal|0
return|;
block|}
comment|/**      * Generate delete where columns = ? CQL.      */
DECL|method|generateTruncate (String table)
specifier|public
specifier|static
name|Truncate
name|generateTruncate
parameter_list|(
name|String
name|table
parameter_list|)
block|{
name|Truncate
name|truncate
init|=
name|QueryBuilder
operator|.
name|truncate
argument_list|(
name|table
argument_list|)
decl_stmt|;
return|return
name|truncate
return|;
block|}
comment|/**      * Apply consistency level if provided, else leave default.      */
DECL|method|applyConsistencyLevel (T statement, ConsistencyLevel consistencyLevel)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|RegularStatement
parameter_list|>
name|T
name|applyConsistencyLevel
parameter_list|(
name|T
name|statement
parameter_list|,
name|ConsistencyLevel
name|consistencyLevel
parameter_list|)
block|{
if|if
condition|(
name|consistencyLevel
operator|!=
literal|null
condition|)
block|{
name|statement
operator|.
name|setConsistencyLevel
argument_list|(
name|consistencyLevel
argument_list|)
expr_stmt|;
block|}
return|return
name|statement
return|;
block|}
block|}
end_class

end_unit

