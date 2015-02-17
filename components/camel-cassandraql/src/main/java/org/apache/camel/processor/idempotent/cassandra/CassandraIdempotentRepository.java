begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.idempotent.cassandra
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|idempotent
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
name|Cluster
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
name|PreparedStatement
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
name|ResultSet
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
name|Row
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
name|Session
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
name|Select
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|IdempotentRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ServiceSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|utils
operator|.
name|cassandra
operator|.
name|CassandraSessionHolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|utils
operator|.
name|cassandra
operator|.
name|CassandraUtils
operator|.
name|append
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|utils
operator|.
name|cassandra
operator|.
name|CassandraUtils
operator|.
name|applyConsistencyLevel
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|utils
operator|.
name|cassandra
operator|.
name|CassandraUtils
operator|.
name|generateDelete
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|utils
operator|.
name|cassandra
operator|.
name|CassandraUtils
operator|.
name|generateInsert
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|utils
operator|.
name|cassandra
operator|.
name|CassandraUtils
operator|.
name|generateSelect
import|;
end_import

begin_comment
comment|/**  * Implementation of {@link IdempotentRepository} using Cassandra table to store  * message ids.  * Advice: use LeveledCompaction for this table and tune read/write consistency levels.  * Warning: Cassandra is not the best tool for queuing use cases  * See http://www.datastax.com/dev/blog/cassandra-anti-patterns-queues-and-queue-like-datasets  *  * @param<K> Message Id  */
end_comment

begin_class
DECL|class|CassandraIdempotentRepository
specifier|public
class|class
name|CassandraIdempotentRepository
parameter_list|<
name|K
parameter_list|>
extends|extends
name|ServiceSupport
implements|implements
name|IdempotentRepository
argument_list|<
name|K
argument_list|>
block|{
comment|/**      * Logger      */
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CassandraIdempotentRepository
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Session holder      */
DECL|field|sessionHolder
specifier|private
name|CassandraSessionHolder
name|sessionHolder
decl_stmt|;
comment|/**      * Table name      */
DECL|field|table
specifier|private
name|String
name|table
init|=
literal|"CAMEL_IDEMPOTENT"
decl_stmt|;
comment|/**      * Values used as primary key prefix      */
DECL|field|prefixPKValues
specifier|private
name|Object
index|[]
name|prefixPKValues
init|=
operator|new
name|Object
index|[
literal|0
index|]
decl_stmt|;
comment|/**      * Primary key columns      */
DECL|field|pkColumns
specifier|private
name|String
index|[]
name|pkColumns
init|=
block|{
literal|"KEY"
block|}
decl_stmt|;
comment|/**      * Time to live in seconds used for inserts      */
DECL|field|ttl
specifier|private
name|Integer
name|ttl
decl_stmt|;
comment|/**      * Write consistency level      */
DECL|field|writeConsistencyLevel
specifier|private
name|ConsistencyLevel
name|writeConsistencyLevel
decl_stmt|;
comment|/**      * Read consistency level      */
DECL|field|readConsistencyLevel
specifier|private
name|ConsistencyLevel
name|readConsistencyLevel
decl_stmt|;
DECL|field|insertStatement
specifier|private
name|PreparedStatement
name|insertStatement
decl_stmt|;
DECL|field|selectStatement
specifier|private
name|PreparedStatement
name|selectStatement
decl_stmt|;
DECL|field|deleteStatement
specifier|private
name|PreparedStatement
name|deleteStatement
decl_stmt|;
DECL|method|CassandraIdempotentRepository ()
specifier|public
name|CassandraIdempotentRepository
parameter_list|()
block|{     }
DECL|method|CassandraIdempotentRepository (Session session)
specifier|public
name|CassandraIdempotentRepository
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
name|this
operator|.
name|sessionHolder
operator|=
operator|new
name|CassandraSessionHolder
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
DECL|method|CassandraIdempotentRepository (Cluster cluster, String keyspace)
specifier|public
name|CassandraIdempotentRepository
parameter_list|(
name|Cluster
name|cluster
parameter_list|,
name|String
name|keyspace
parameter_list|)
block|{
name|this
operator|.
name|sessionHolder
operator|=
operator|new
name|CassandraSessionHolder
argument_list|(
name|cluster
argument_list|,
name|keyspace
argument_list|)
expr_stmt|;
block|}
DECL|method|isKey (ResultSet resultSet)
specifier|private
name|boolean
name|isKey
parameter_list|(
name|ResultSet
name|resultSet
parameter_list|)
block|{
name|Row
name|row
init|=
name|resultSet
operator|.
name|one
argument_list|()
decl_stmt|;
if|if
condition|(
name|row
operator|==
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"No row to check key"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Row with {} columns to check key"
argument_list|,
name|row
operator|.
name|getColumnDefinitions
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|row
operator|.
name|getColumnDefinitions
argument_list|()
operator|.
name|size
argument_list|()
operator|>=
name|pkColumns
operator|.
name|length
return|;
block|}
block|}
DECL|method|isApplied (ResultSet resultSet)
specifier|protected
specifier|final
name|boolean
name|isApplied
parameter_list|(
name|ResultSet
name|resultSet
parameter_list|)
block|{
name|Row
name|row
init|=
name|resultSet
operator|.
name|one
argument_list|()
decl_stmt|;
return|return
name|row
operator|==
literal|null
operator|||
name|row
operator|.
name|getBool
argument_list|(
literal|"[applied]"
argument_list|)
return|;
block|}
DECL|method|getPKValues (K key)
specifier|protected
name|Object
index|[]
name|getPKValues
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|append
argument_list|(
name|prefixPKValues
argument_list|,
name|key
argument_list|)
return|;
block|}
comment|// -------------------------------------------------------------------------
comment|// Lifecycle methods
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|sessionHolder
operator|.
name|start
argument_list|()
expr_stmt|;
name|initInsertStatement
argument_list|()
expr_stmt|;
name|initSelectStatement
argument_list|()
expr_stmt|;
name|initDeleteStatement
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|sessionHolder
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
comment|// -------------------------------------------------------------------------
comment|// Add key to repository
DECL|method|initInsertStatement ()
specifier|protected
name|void
name|initInsertStatement
parameter_list|()
block|{
name|Insert
name|insert
init|=
name|generateInsert
argument_list|(
name|table
argument_list|,
name|pkColumns
argument_list|,
literal|true
argument_list|,
name|ttl
argument_list|)
decl_stmt|;
name|insert
operator|=
name|applyConsistencyLevel
argument_list|(
name|insert
argument_list|,
name|writeConsistencyLevel
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Generated Insert {}"
argument_list|,
name|insert
argument_list|)
expr_stmt|;
name|insertStatement
operator|=
name|getSession
argument_list|()
operator|.
name|prepare
argument_list|(
name|insert
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|add (K key)
specifier|public
name|boolean
name|add
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|Object
index|[]
name|idValues
init|=
name|getPKValues
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Inserting key {}"
argument_list|,
operator|(
name|Object
operator|)
name|idValues
argument_list|)
expr_stmt|;
return|return
name|isApplied
argument_list|(
name|getSession
argument_list|()
operator|.
name|execute
argument_list|(
name|insertStatement
operator|.
name|bind
argument_list|(
name|idValues
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
comment|// -------------------------------------------------------------------------
comment|// Check if key is in repository
DECL|method|initSelectStatement ()
specifier|protected
name|void
name|initSelectStatement
parameter_list|()
block|{
name|Select
name|select
init|=
name|generateSelect
argument_list|(
name|table
argument_list|,
name|pkColumns
argument_list|,
name|pkColumns
argument_list|)
decl_stmt|;
name|select
operator|=
name|applyConsistencyLevel
argument_list|(
name|select
argument_list|,
name|readConsistencyLevel
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Generated Select {}"
argument_list|,
name|select
argument_list|)
expr_stmt|;
name|selectStatement
operator|=
name|getSession
argument_list|()
operator|.
name|prepare
argument_list|(
name|select
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|contains (K key)
specifier|public
name|boolean
name|contains
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|Object
index|[]
name|idValues
init|=
name|getPKValues
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Checking key {}"
argument_list|,
operator|(
name|Object
operator|)
name|idValues
argument_list|)
expr_stmt|;
return|return
name|isKey
argument_list|(
name|getSession
argument_list|()
operator|.
name|execute
argument_list|(
name|selectStatement
operator|.
name|bind
argument_list|(
name|idValues
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|confirm (K key)
specifier|public
name|boolean
name|confirm
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
comment|// -------------------------------------------------------------------------
comment|// Remove key from repository
DECL|method|initDeleteStatement ()
specifier|protected
name|void
name|initDeleteStatement
parameter_list|()
block|{
name|Delete
name|delete
init|=
name|generateDelete
argument_list|(
name|table
argument_list|,
name|pkColumns
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|delete
operator|=
name|applyConsistencyLevel
argument_list|(
name|delete
argument_list|,
name|writeConsistencyLevel
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Generated Delete {}"
argument_list|,
name|delete
argument_list|)
expr_stmt|;
name|deleteStatement
operator|=
name|getSession
argument_list|()
operator|.
name|prepare
argument_list|(
name|delete
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|remove (K key)
specifier|public
name|boolean
name|remove
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|Object
index|[]
name|idValues
init|=
name|getPKValues
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Deleting key {}"
argument_list|,
operator|(
name|Object
operator|)
name|idValues
argument_list|)
expr_stmt|;
return|return
name|isApplied
argument_list|(
name|getSession
argument_list|()
operator|.
name|execute
argument_list|(
name|deleteStatement
operator|.
name|bind
argument_list|(
name|idValues
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
comment|// -------------------------------------------------------------------------
comment|// Getters& Setters
DECL|method|getSession ()
specifier|public
name|Session
name|getSession
parameter_list|()
block|{
return|return
name|sessionHolder
operator|.
name|getSession
argument_list|()
return|;
block|}
DECL|method|setSession (Session session)
specifier|public
name|void
name|setSession
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
name|this
operator|.
name|sessionHolder
operator|=
operator|new
name|CassandraSessionHolder
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
DECL|method|getTable ()
specifier|public
name|String
name|getTable
parameter_list|()
block|{
return|return
name|table
return|;
block|}
DECL|method|setTable (String table)
specifier|public
name|void
name|setTable
parameter_list|(
name|String
name|table
parameter_list|)
block|{
name|this
operator|.
name|table
operator|=
name|table
expr_stmt|;
block|}
DECL|method|getPKColumns ()
specifier|public
name|String
index|[]
name|getPKColumns
parameter_list|()
block|{
return|return
name|pkColumns
return|;
block|}
DECL|method|setPKColumns (String... pkColumns)
specifier|public
name|void
name|setPKColumns
parameter_list|(
name|String
modifier|...
name|pkColumns
parameter_list|)
block|{
name|this
operator|.
name|pkColumns
operator|=
name|pkColumns
expr_stmt|;
block|}
DECL|method|getTtl ()
specifier|public
name|Integer
name|getTtl
parameter_list|()
block|{
return|return
name|ttl
return|;
block|}
DECL|method|setTtl (Integer ttl)
specifier|public
name|void
name|setTtl
parameter_list|(
name|Integer
name|ttl
parameter_list|)
block|{
name|this
operator|.
name|ttl
operator|=
name|ttl
expr_stmt|;
block|}
DECL|method|getWriteConsistencyLevel ()
specifier|public
name|ConsistencyLevel
name|getWriteConsistencyLevel
parameter_list|()
block|{
return|return
name|writeConsistencyLevel
return|;
block|}
DECL|method|setWriteConsistencyLevel (ConsistencyLevel writeConsistencyLevel)
specifier|public
name|void
name|setWriteConsistencyLevel
parameter_list|(
name|ConsistencyLevel
name|writeConsistencyLevel
parameter_list|)
block|{
name|this
operator|.
name|writeConsistencyLevel
operator|=
name|writeConsistencyLevel
expr_stmt|;
block|}
DECL|method|getReadConsistencyLevel ()
specifier|public
name|ConsistencyLevel
name|getReadConsistencyLevel
parameter_list|()
block|{
return|return
name|readConsistencyLevel
return|;
block|}
DECL|method|setReadConsistencyLevel (ConsistencyLevel readConsistencyLevel)
specifier|public
name|void
name|setReadConsistencyLevel
parameter_list|(
name|ConsistencyLevel
name|readConsistencyLevel
parameter_list|)
block|{
name|this
operator|.
name|readConsistencyLevel
operator|=
name|readConsistencyLevel
expr_stmt|;
block|}
DECL|method|getPrefixPKValues ()
specifier|public
name|Object
index|[]
name|getPrefixPKValues
parameter_list|()
block|{
return|return
name|prefixPKValues
return|;
block|}
DECL|method|setPrefixPKValues (Object[] prefixPKValues)
specifier|public
name|void
name|setPrefixPKValues
parameter_list|(
name|Object
index|[]
name|prefixPKValues
parameter_list|)
block|{
name|this
operator|.
name|prefixPKValues
operator|=
name|prefixPKValues
expr_stmt|;
block|}
block|}
end_class

end_unit

