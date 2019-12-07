begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pg.replication.slot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pg
operator|.
name|replication
operator|.
name|slot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketException
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
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|ScheduledExecutorService
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
name|ScheduledFuture
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
name|Message
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
name|Processor
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
name|Synchronization
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
name|ScheduledPollConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|postgresql
operator|.
name|PGConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|postgresql
operator|.
name|replication
operator|.
name|PGReplicationStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|postgresql
operator|.
name|replication
operator|.
name|fluent
operator|.
name|logical
operator|.
name|ChainedLogicalStreamBuilder
import|;
end_import

begin_comment
comment|/**  * The pg-replication-slot consumer.  */
end_comment

begin_class
DECL|class|PgReplicationSlotConsumer
specifier|public
class|class
name|PgReplicationSlotConsumer
extends|extends
name|ScheduledPollConsumer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|PgReplicationSlotEndpoint
name|endpoint
decl_stmt|;
DECL|field|connection
specifier|private
name|Connection
name|connection
decl_stmt|;
DECL|field|pgConnection
specifier|private
name|PGConnection
name|pgConnection
decl_stmt|;
DECL|field|replicationStream
specifier|private
name|PGReplicationStream
name|replicationStream
decl_stmt|;
DECL|field|scheduledExecutor
specifier|private
name|ScheduledExecutorService
name|scheduledExecutor
decl_stmt|;
DECL|field|payload
specifier|private
name|byte
index|[]
name|payload
decl_stmt|;
DECL|method|PgReplicationSlotConsumer (PgReplicationSlotEndpoint endpoint, Processor processor)
name|PgReplicationSlotConsumer
parameter_list|(
name|PgReplicationSlotEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|this
operator|.
name|connect
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|scheduledExecutor
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|scheduledExecutor
operator|=
name|this
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadScheduledExecutor
argument_list|(
name|this
argument_list|,
literal|"PgReplicationStatusUpdateSender"
argument_list|)
expr_stmt|;
block|}
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|connection
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
name|this
operator|.
name|connection
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|scheduledExecutor
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|this
operator|.
name|scheduledExecutor
argument_list|)
expr_stmt|;
name|this
operator|.
name|scheduledExecutor
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|PGReplicationStream
name|stream
init|=
name|getStream
argument_list|()
decl_stmt|;
comment|// If the stream is null, this means the slot is active, i.e. used by another connection. We'll try
comment|// again on the next poll.
if|if
condition|(
name|stream
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
try|try
block|{
comment|// The same payload will be sent again and again until the processing is completed successfully.
comment|// We should not read another payload before that to guarantee the order of processing.
if|if
condition|(
name|this
operator|.
name|payload
operator|==
literal|null
condition|)
block|{
name|ByteBuffer
name|msg
init|=
name|stream
operator|.
name|readPending
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
name|int
name|offset
init|=
name|msg
operator|.
name|arrayOffset
argument_list|()
decl_stmt|;
name|byte
index|[]
name|source
init|=
name|msg
operator|.
name|array
argument_list|()
decl_stmt|;
name|int
name|length
init|=
name|source
operator|.
name|length
operator|-
name|offset
decl_stmt|;
name|this
operator|.
name|payload
operator|=
operator|new
name|byte
index|[
name|length
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|source
argument_list|,
name|offset
argument_list|,
name|this
operator|.
name|payload
argument_list|,
literal|0
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
comment|// If the cause of the exception is that connection is lost, we'll try to reconnect so in the next poll a
comment|// new connection will be available.
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|SocketException
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Connection to PosgreSQL server has been lost, trying to reconnect."
argument_list|)
expr_stmt|;
name|this
operator|.
name|connect
argument_list|()
expr_stmt|;
block|}
throw|throw
name|e
throw|;
block|}
name|Exchange
name|exchange
init|=
name|this
operator|.
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setExchangeId
argument_list|(
name|stream
operator|.
name|getLastReceiveLSN
argument_list|()
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|this
operator|.
name|payload
argument_list|)
expr_stmt|;
specifier|final
name|long
name|delay
init|=
name|this
operator|.
name|endpoint
operator|.
name|getStatusInterval
argument_list|()
decl_stmt|;
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|scheduledFuture
init|=
name|this
operator|.
name|scheduledExecutor
operator|.
name|scheduleAtFixedRate
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Processing took too long. Sending status update to avoid disconnect."
argument_list|)
expr_stmt|;
name|stream
operator|.
name|forceUpdateStatus
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
name|delay
argument_list|,
name|delay
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|Synchronization
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|processCommit
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|scheduledFuture
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|processRollback
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|scheduledFuture
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
block|}
DECL|method|processCommit (Exchange exchange)
specifier|private
name|void
name|processCommit
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
comment|// Reset the `payload` buffer first because it's already processed, and in case of losing the connection
comment|// while updating the status, the next poll will try to reconnect again instead of processing the stale payload.
name|this
operator|.
name|payload
operator|=
literal|null
expr_stmt|;
name|PGReplicationStream
name|stream
init|=
name|getStream
argument_list|()
decl_stmt|;
if|if
condition|(
name|stream
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|stream
operator|.
name|setAppliedLSN
argument_list|(
name|stream
operator|.
name|getLastReceiveLSN
argument_list|()
argument_list|)
expr_stmt|;
name|stream
operator|.
name|setFlushedLSN
argument_list|(
name|stream
operator|.
name|getLastReceiveLSN
argument_list|()
argument_list|)
expr_stmt|;
name|stream
operator|.
name|forceUpdateStatus
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Exception while sending feedback to PostgreSQL."
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processRollback (Exchange exchange)
specifier|private
name|void
name|processRollback
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Exception
name|cause
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|cause
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error during processing exchange. Will attempt to process the message on next poll."
argument_list|,
name|exchange
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createSlot ()
specifier|private
name|void
name|createSlot
parameter_list|()
throws|throws
name|SQLException
block|{
name|this
operator|.
name|pgConnection
operator|.
name|getReplicationAPI
argument_list|()
operator|.
name|createReplicationSlot
argument_list|()
operator|.
name|logical
argument_list|()
operator|.
name|withSlotName
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getSlot
argument_list|()
argument_list|)
operator|.
name|withOutputPlugin
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getOutputPlugin
argument_list|()
argument_list|)
operator|.
name|make
argument_list|()
expr_stmt|;
block|}
DECL|method|isSlotCreated ()
specifier|private
name|boolean
name|isSlotCreated
parameter_list|()
throws|throws
name|SQLException
block|{
name|String
name|sql
init|=
name|String
operator|.
name|format
argument_list|(
literal|"SELECT count(*) FROM pg_replication_slots WHERE slot_name = '%s';"
argument_list|,
name|this
operator|.
name|endpoint
operator|.
name|getSlot
argument_list|()
argument_list|)
decl_stmt|;
try|try
init|(
name|Statement
name|statement
init|=
name|this
operator|.
name|connection
operator|.
name|createStatement
argument_list|()
init|;
name|ResultSet
name|resultSet
operator|=
name|statement
operator|.
name|executeQuery
argument_list|(
name|sql
argument_list|)
init|)
block|{
name|resultSet
operator|.
name|next
argument_list|()
expr_stmt|;
return|return
name|resultSet
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
operator|>
literal|0
return|;
block|}
block|}
DECL|method|getStream ()
specifier|private
name|PGReplicationStream
name|getStream
parameter_list|()
throws|throws
name|SQLException
block|{
if|if
condition|(
name|this
operator|.
name|replicationStream
operator|!=
literal|null
operator|&&
operator|!
name|this
operator|.
name|replicationStream
operator|.
name|isClosed
argument_list|()
condition|)
block|{
return|return
name|this
operator|.
name|replicationStream
return|;
block|}
if|if
condition|(
name|isSlotActive
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Slot: %s is active. Waiting for it to be available."
argument_list|,
name|this
operator|.
name|endpoint
operator|.
name|getSlot
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|ChainedLogicalStreamBuilder
name|streamBuilder
init|=
name|this
operator|.
name|pgConnection
operator|.
name|getReplicationAPI
argument_list|()
operator|.
name|replicationStream
argument_list|()
operator|.
name|logical
argument_list|()
operator|.
name|withSlotName
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getSlot
argument_list|()
argument_list|)
operator|.
name|withStatusInterval
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getStatusInterval
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|Properties
name|slotOptions
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|slotOptions
operator|.
name|putAll
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getSlotOptions
argument_list|()
argument_list|)
expr_stmt|;
name|streamBuilder
operator|.
name|withSlotOptions
argument_list|(
name|slotOptions
argument_list|)
expr_stmt|;
name|this
operator|.
name|replicationStream
operator|=
name|streamBuilder
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|this
operator|.
name|replicationStream
return|;
block|}
DECL|method|isSlotActive ()
specifier|private
name|boolean
name|isSlotActive
parameter_list|()
throws|throws
name|SQLException
block|{
name|String
name|sql
init|=
name|String
operator|.
name|format
argument_list|(
literal|"SELECT count(*) FROM pg_replication_slots where slot_name = '%s' AND active = true;"
argument_list|,
name|this
operator|.
name|endpoint
operator|.
name|getSlot
argument_list|()
argument_list|)
decl_stmt|;
try|try
init|(
name|Statement
name|statement
init|=
name|this
operator|.
name|connection
operator|.
name|createStatement
argument_list|()
init|;
name|ResultSet
name|resultSet
operator|=
name|statement
operator|.
name|executeQuery
argument_list|(
name|sql
argument_list|)
init|)
block|{
name|resultSet
operator|.
name|next
argument_list|()
expr_stmt|;
return|return
name|resultSet
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
operator|>
literal|0
return|;
block|}
block|}
DECL|method|connect ()
specifier|private
name|void
name|connect
parameter_list|()
throws|throws
name|SQLException
block|{
if|if
condition|(
name|this
operator|.
name|connection
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|connection
operator|=
name|this
operator|.
name|endpoint
operator|.
name|newDbConnection
argument_list|()
expr_stmt|;
name|this
operator|.
name|pgConnection
operator|=
name|this
operator|.
name|connection
operator|.
name|unwrap
argument_list|(
name|PGConnection
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|replicationStream
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|endpoint
operator|.
name|getAutoCreateSlot
argument_list|()
operator|&&
operator|!
name|this
operator|.
name|isSlotCreated
argument_list|()
condition|)
block|{
name|this
operator|.
name|createSlot
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

