begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate.cassandra
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
operator|.
name|cassandra
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|CamelContext
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
name|Exchange
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
name|AggregationRepository
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
name|RecoverableAggregationRepository
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
name|service
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
name|eq
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
name|concat
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
comment|/**  * Implementation of {@link AggregationRepository} using Cassandra table to store  * exchanges.  * Advice: use LeveledCompaction for this table and tune read/write consistency levels.  * Warning: Cassandra is not the best tool for queuing use cases  * See: http://www.datastax.com/dev/blog/cassandra-anti-patterns-queues-and-queue-like-datasets  */
end_comment

begin_class
DECL|class|CassandraAggregationRepository
specifier|public
class|class
name|CassandraAggregationRepository
extends|extends
name|ServiceSupport
implements|implements
name|RecoverableAggregationRepository
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
name|CassandraAggregationRepository
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
literal|"CAMEL_AGGREGATION"
decl_stmt|;
comment|/**      * Exchange Id column name      */
DECL|field|exchangeIdColumn
specifier|private
name|String
name|exchangeIdColumn
init|=
literal|"EXCHANGE_ID"
decl_stmt|;
comment|/**      * Exchange column name      */
DECL|field|exchangeColumn
specifier|private
name|String
name|exchangeColumn
init|=
literal|"EXCHANGE"
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
comment|/**      * Exchange marshaller/unmarshaller      */
DECL|field|exchangeCodec
specifier|private
specifier|final
name|CassandraCamelCodec
name|exchangeCodec
init|=
operator|new
name|CassandraCamelCodec
argument_list|()
decl_stmt|;
comment|/**      * Time to live in seconds used for inserts      */
DECL|field|ttl
specifier|private
name|Integer
name|ttl
decl_stmt|;
comment|/**      * Writeconsistency level      */
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
comment|/**      * Prepared statement used to get exchangeIds and exchange ids      */
DECL|field|selectKeyIdStatement
specifier|private
name|PreparedStatement
name|selectKeyIdStatement
decl_stmt|;
comment|/**      * Prepared statement used to delete with key and exchange id      */
DECL|field|deleteIfIdStatement
specifier|private
name|PreparedStatement
name|deleteIfIdStatement
decl_stmt|;
DECL|field|recoveryIntervalInMillis
specifier|private
name|long
name|recoveryIntervalInMillis
init|=
literal|5000
decl_stmt|;
DECL|field|useRecovery
specifier|private
name|boolean
name|useRecovery
init|=
literal|true
decl_stmt|;
DECL|field|deadLetterUri
specifier|private
name|String
name|deadLetterUri
decl_stmt|;
DECL|field|maximumRedeliveries
specifier|private
name|int
name|maximumRedeliveries
decl_stmt|;
DECL|field|allowSerializedHeaders
specifier|private
name|boolean
name|allowSerializedHeaders
decl_stmt|;
DECL|method|CassandraAggregationRepository ()
specifier|public
name|CassandraAggregationRepository
parameter_list|()
block|{     }
DECL|method|CassandraAggregationRepository (Session session)
specifier|public
name|CassandraAggregationRepository
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
DECL|method|CassandraAggregationRepository (Cluster cluster, String keyspace)
specifier|public
name|CassandraAggregationRepository
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
comment|/**      * Generate primary key values from aggregation key.      */
DECL|method|getPKValues (String key)
specifier|protected
name|Object
index|[]
name|getPKValues
parameter_list|(
name|String
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
comment|/**      * Get aggregation key colum name.      */
DECL|method|getKeyColumn ()
specifier|private
name|String
name|getKeyColumn
parameter_list|()
block|{
return|return
name|pkColumns
index|[
name|pkColumns
operator|.
name|length
operator|-
literal|1
index|]
return|;
block|}
DECL|method|getAllColumns ()
specifier|private
name|String
index|[]
name|getAllColumns
parameter_list|()
block|{
return|return
name|append
argument_list|(
name|pkColumns
argument_list|,
name|exchangeIdColumn
argument_list|,
name|exchangeColumn
argument_list|)
return|;
block|}
comment|//--------------------------------------------------------------------------
comment|// Service support
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
name|initSelectKeyIdStatement
argument_list|()
expr_stmt|;
name|initDeleteIfIdStatement
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
comment|// Add exchange to repository
DECL|method|initInsertStatement ()
specifier|private
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
name|getAllColumns
argument_list|()
argument_list|,
literal|false
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
comment|/**      * Insert or update exchange in aggregation table.      */
annotation|@
name|Override
DECL|method|add (CamelContext camelContext, String key, Exchange exchange)
specifier|public
name|Exchange
name|add
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
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
literal|"Inserting key {} exchange {}"
argument_list|,
name|idValues
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
try|try
block|{
name|ByteBuffer
name|marshalledExchange
init|=
name|exchangeCodec
operator|.
name|marshallExchange
argument_list|(
name|camelContext
argument_list|,
name|exchange
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
name|Object
index|[]
name|cqlParams
init|=
name|concat
argument_list|(
name|idValues
argument_list|,
operator|new
name|Object
index|[]
block|{
name|exchange
operator|.
name|getExchangeId
argument_list|()
block|,
name|marshalledExchange
block|}
argument_list|)
decl_stmt|;
name|getSession
argument_list|()
operator|.
name|execute
argument_list|(
name|insertStatement
operator|.
name|bind
argument_list|(
name|cqlParams
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|iOException
parameter_list|)
block|{
throw|throw
operator|new
name|CassandraAggregationException
argument_list|(
literal|"Failed to write exchange"
argument_list|,
name|exchange
argument_list|,
name|iOException
argument_list|)
throw|;
block|}
block|}
comment|// -------------------------------------------------------------------------
comment|// Get exchange from repository
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
name|getAllColumns
argument_list|()
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
comment|/**      * Get exchange from aggregation table by aggregation key.      */
annotation|@
name|Override
DECL|method|get (CamelContext camelContext, String key)
specifier|public
name|Exchange
name|get
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|Object
index|[]
name|pkValues
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
literal|"Selecting key {}"
argument_list|,
name|pkValues
argument_list|)
expr_stmt|;
name|Row
name|row
init|=
name|getSession
argument_list|()
operator|.
name|execute
argument_list|(
name|selectStatement
operator|.
name|bind
argument_list|(
name|pkValues
argument_list|)
argument_list|)
operator|.
name|one
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|row
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|exchange
operator|=
name|exchangeCodec
operator|.
name|unmarshallExchange
argument_list|(
name|camelContext
argument_list|,
name|row
operator|.
name|getBytes
argument_list|(
name|exchangeColumn
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|iOException
parameter_list|)
block|{
throw|throw
operator|new
name|CassandraAggregationException
argument_list|(
literal|"Failed to read exchange"
argument_list|,
name|exchange
argument_list|,
name|iOException
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|classNotFoundException
parameter_list|)
block|{
throw|throw
operator|new
name|CassandraAggregationException
argument_list|(
literal|"Failed to read exchange"
argument_list|,
name|exchange
argument_list|,
name|classNotFoundException
argument_list|)
throw|;
block|}
block|}
return|return
name|exchange
return|;
block|}
comment|// -------------------------------------------------------------------------
comment|// Confirm exchange in repository
DECL|method|initDeleteIfIdStatement ()
specifier|private
name|void
name|initDeleteIfIdStatement
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
literal|false
argument_list|)
decl_stmt|;
name|Delete
operator|.
name|Conditions
name|deleteIf
init|=
name|delete
operator|.
name|onlyIf
argument_list|(
name|eq
argument_list|(
name|exchangeIdColumn
argument_list|,
name|bindMarker
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|deleteIf
operator|=
name|applyConsistencyLevel
argument_list|(
name|deleteIf
argument_list|,
name|writeConsistencyLevel
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Generated Delete If Id {}"
argument_list|,
name|deleteIf
argument_list|)
expr_stmt|;
name|deleteIfIdStatement
operator|=
name|getSession
argument_list|()
operator|.
name|prepare
argument_list|(
name|deleteIf
argument_list|)
expr_stmt|;
block|}
comment|/**      * Remove exchange by Id from aggregation table.      */
annotation|@
name|Override
DECL|method|confirm (CamelContext camelContext, String exchangeId)
specifier|public
name|void
name|confirm
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|exchangeId
parameter_list|)
block|{
name|String
name|keyColumn
init|=
name|getKeyColumn
argument_list|()
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Selecting Ids"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Row
argument_list|>
name|rows
init|=
name|selectKeyIds
argument_list|()
decl_stmt|;
for|for
control|(
name|Row
name|row
range|:
name|rows
control|)
block|{
if|if
condition|(
name|row
operator|.
name|getString
argument_list|(
name|exchangeIdColumn
argument_list|)
operator|.
name|equals
argument_list|(
name|exchangeId
argument_list|)
condition|)
block|{
name|String
name|key
init|=
name|row
operator|.
name|getString
argument_list|(
name|keyColumn
argument_list|)
decl_stmt|;
name|Object
index|[]
name|cqlParams
init|=
name|append
argument_list|(
name|getPKValues
argument_list|(
name|key
argument_list|)
argument_list|,
name|exchangeId
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Deleting If Id {}"
argument_list|,
name|cqlParams
argument_list|)
expr_stmt|;
name|getSession
argument_list|()
operator|.
name|execute
argument_list|(
name|deleteIfIdStatement
operator|.
name|bind
argument_list|(
name|cqlParams
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// -------------------------------------------------------------------------
comment|// Remove exchange from repository
DECL|method|initDeleteStatement ()
specifier|private
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
literal|false
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
comment|/**      * Remove exchange by aggregation key from aggregation table.      */
annotation|@
name|Override
DECL|method|remove (CamelContext camelContext, String key, Exchange exchange)
specifier|public
name|void
name|remove
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|,
name|Exchange
name|exchange
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
expr_stmt|;
block|}
comment|// -------------------------------------------------------------------------
DECL|method|initSelectKeyIdStatement ()
specifier|private
name|void
name|initSelectKeyIdStatement
parameter_list|()
block|{
name|Select
name|select
init|=
name|generateSelect
argument_list|(
name|table
argument_list|,
operator|new
name|String
index|[]
block|{
name|getKeyColumn
argument_list|()
block|,
name|exchangeIdColumn
block|}
argument_list|,
comment|// Key + Exchange Id columns
name|pkColumns
argument_list|,
name|pkColumns
operator|.
name|length
operator|-
literal|1
argument_list|)
decl_stmt|;
comment|// Where fixed PK columns
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
literal|"Generated Select keys {}"
argument_list|,
name|select
argument_list|)
expr_stmt|;
name|selectKeyIdStatement
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
DECL|method|selectKeyIds ()
specifier|protected
name|List
argument_list|<
name|Row
argument_list|>
name|selectKeyIds
parameter_list|()
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Selecting keys {}"
argument_list|,
name|getPrefixPKValues
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|getSession
argument_list|()
operator|.
name|execute
argument_list|(
name|selectKeyIdStatement
operator|.
name|bind
argument_list|(
name|getPrefixPKValues
argument_list|()
argument_list|)
argument_list|)
operator|.
name|all
argument_list|()
return|;
block|}
comment|/**      * Get aggregation exchangeIds from aggregation table.      */
annotation|@
name|Override
DECL|method|getKeys ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getKeys
parameter_list|()
block|{
name|List
argument_list|<
name|Row
argument_list|>
name|rows
init|=
name|selectKeyIds
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|rows
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|keyColumnName
init|=
name|getKeyColumn
argument_list|()
decl_stmt|;
for|for
control|(
name|Row
name|row
range|:
name|rows
control|)
block|{
name|keys
operator|.
name|add
argument_list|(
name|row
operator|.
name|getString
argument_list|(
name|keyColumnName
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|keys
return|;
block|}
comment|/**      * Get exchange IDs to be recovered      *      * @return Exchange IDs      */
annotation|@
name|Override
DECL|method|scan (CamelContext camelContext)
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|scan
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|List
argument_list|<
name|Row
argument_list|>
name|rows
init|=
name|selectKeyIds
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|exchangeIds
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|rows
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Row
name|row
range|:
name|rows
control|)
block|{
name|exchangeIds
operator|.
name|add
argument_list|(
name|row
operator|.
name|getString
argument_list|(
name|exchangeIdColumn
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|exchangeIds
return|;
block|}
comment|/**      * Get exchange by exchange ID.      * This is far from optimal.      */
annotation|@
name|Override
DECL|method|recover (CamelContext camelContext, String exchangeId)
specifier|public
name|Exchange
name|recover
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|exchangeId
parameter_list|)
block|{
name|List
argument_list|<
name|Row
argument_list|>
name|rows
init|=
name|selectKeyIds
argument_list|()
decl_stmt|;
name|String
name|keyColumnName
init|=
name|getKeyColumn
argument_list|()
decl_stmt|;
name|String
name|lKey
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Row
name|row
range|:
name|rows
control|)
block|{
name|String
name|lExchangeId
init|=
name|row
operator|.
name|getString
argument_list|(
name|exchangeIdColumn
argument_list|)
decl_stmt|;
if|if
condition|(
name|lExchangeId
operator|.
name|equals
argument_list|(
name|exchangeId
argument_list|)
condition|)
block|{
name|lKey
operator|=
name|row
operator|.
name|getString
argument_list|(
name|keyColumnName
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
return|return
name|lKey
operator|==
literal|null
condition|?
literal|null
else|:
name|get
argument_list|(
name|camelContext
argument_list|,
name|lKey
argument_list|)
return|;
block|}
comment|// -------------------------------------------------------------------------
comment|// Getters and Setters
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
DECL|method|setPrefixPKValues (Object... prefixPKValues)
specifier|public
name|void
name|setPrefixPKValues
parameter_list|(
name|Object
modifier|...
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
DECL|method|getExchangeIdColumn ()
specifier|public
name|String
name|getExchangeIdColumn
parameter_list|()
block|{
return|return
name|exchangeIdColumn
return|;
block|}
DECL|method|setExchangeIdColumn (String exchangeIdColumn)
specifier|public
name|void
name|setExchangeIdColumn
parameter_list|(
name|String
name|exchangeIdColumn
parameter_list|)
block|{
name|this
operator|.
name|exchangeIdColumn
operator|=
name|exchangeIdColumn
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
DECL|method|getExchangeColumn ()
specifier|public
name|String
name|getExchangeColumn
parameter_list|()
block|{
return|return
name|exchangeColumn
return|;
block|}
DECL|method|setExchangeColumn (String exchangeColumnName)
specifier|public
name|void
name|setExchangeColumn
parameter_list|(
name|String
name|exchangeColumnName
parameter_list|)
block|{
name|this
operator|.
name|exchangeColumn
operator|=
name|exchangeColumnName
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
annotation|@
name|Override
DECL|method|getRecoveryIntervalInMillis ()
specifier|public
name|long
name|getRecoveryIntervalInMillis
parameter_list|()
block|{
return|return
name|recoveryIntervalInMillis
return|;
block|}
DECL|method|setRecoveryIntervalInMillis (long recoveryIntervalInMillis)
specifier|public
name|void
name|setRecoveryIntervalInMillis
parameter_list|(
name|long
name|recoveryIntervalInMillis
parameter_list|)
block|{
name|this
operator|.
name|recoveryIntervalInMillis
operator|=
name|recoveryIntervalInMillis
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setRecoveryInterval (long interval, TimeUnit timeUnit)
specifier|public
name|void
name|setRecoveryInterval
parameter_list|(
name|long
name|interval
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|this
operator|.
name|recoveryIntervalInMillis
operator|=
name|timeUnit
operator|.
name|toMillis
argument_list|(
name|interval
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setRecoveryInterval (long recoveryIntervalInMillis)
specifier|public
name|void
name|setRecoveryInterval
parameter_list|(
name|long
name|recoveryIntervalInMillis
parameter_list|)
block|{
name|this
operator|.
name|recoveryIntervalInMillis
operator|=
name|recoveryIntervalInMillis
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUseRecovery ()
specifier|public
name|boolean
name|isUseRecovery
parameter_list|()
block|{
return|return
name|useRecovery
return|;
block|}
annotation|@
name|Override
DECL|method|setUseRecovery (boolean useRecovery)
specifier|public
name|void
name|setUseRecovery
parameter_list|(
name|boolean
name|useRecovery
parameter_list|)
block|{
name|this
operator|.
name|useRecovery
operator|=
name|useRecovery
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDeadLetterUri ()
specifier|public
name|String
name|getDeadLetterUri
parameter_list|()
block|{
return|return
name|deadLetterUri
return|;
block|}
annotation|@
name|Override
DECL|method|setDeadLetterUri (String deadLetterUri)
specifier|public
name|void
name|setDeadLetterUri
parameter_list|(
name|String
name|deadLetterUri
parameter_list|)
block|{
name|this
operator|.
name|deadLetterUri
operator|=
name|deadLetterUri
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMaximumRedeliveries ()
specifier|public
name|int
name|getMaximumRedeliveries
parameter_list|()
block|{
return|return
name|maximumRedeliveries
return|;
block|}
annotation|@
name|Override
DECL|method|setMaximumRedeliveries (int maximumRedeliveries)
specifier|public
name|void
name|setMaximumRedeliveries
parameter_list|(
name|int
name|maximumRedeliveries
parameter_list|)
block|{
name|this
operator|.
name|maximumRedeliveries
operator|=
name|maximumRedeliveries
expr_stmt|;
block|}
DECL|method|isAllowSerializedHeaders ()
specifier|public
name|boolean
name|isAllowSerializedHeaders
parameter_list|()
block|{
return|return
name|allowSerializedHeaders
return|;
block|}
DECL|method|setAllowSerializedHeaders (boolean allowSerializedHeaders)
specifier|public
name|void
name|setAllowSerializedHeaders
parameter_list|(
name|boolean
name|allowSerializedHeaders
parameter_list|)
block|{
name|this
operator|.
name|allowSerializedHeaders
operator|=
name|allowSerializedHeaders
expr_stmt|;
block|}
block|}
end_class

end_unit

