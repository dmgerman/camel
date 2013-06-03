begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
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
name|util
operator|.
name|LinkedList
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
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
name|ExchangePattern
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
name|impl
operator|.
name|ScheduledBatchPollingConsumer
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
name|UriParam
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
name|util
operator|.
name|CastUtils
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|dao
operator|.
name|DataAccessException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|core
operator|.
name|ColumnMapRowMapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|core
operator|.
name|JdbcTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|core
operator|.
name|PreparedStatementCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|core
operator|.
name|RowMapperResultSetExtractor
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|SqlConsumer
specifier|public
class|class
name|SqlConsumer
extends|extends
name|ScheduledBatchPollingConsumer
block|{
DECL|field|query
specifier|private
specifier|final
name|String
name|query
decl_stmt|;
DECL|field|jdbcTemplate
specifier|private
specifier|final
name|JdbcTemplate
name|jdbcTemplate
decl_stmt|;
DECL|field|sqlPrepareStatementStrategy
specifier|private
specifier|final
name|SqlPrepareStatementStrategy
name|sqlPrepareStatementStrategy
decl_stmt|;
DECL|field|sqlProcessingStrategy
specifier|private
specifier|final
name|SqlProcessingStrategy
name|sqlProcessingStrategy
decl_stmt|;
annotation|@
name|UriParam
DECL|field|onConsume
specifier|private
name|String
name|onConsume
decl_stmt|;
annotation|@
name|UriParam
DECL|field|onConsumeFailed
specifier|private
name|String
name|onConsumeFailed
decl_stmt|;
annotation|@
name|UriParam
DECL|field|onConsumeBatchComplete
specifier|private
name|String
name|onConsumeBatchComplete
decl_stmt|;
annotation|@
name|UriParam
DECL|field|useIterator
specifier|private
name|boolean
name|useIterator
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|routeEmptyResultSet
specifier|private
name|boolean
name|routeEmptyResultSet
decl_stmt|;
annotation|@
name|UriParam
DECL|field|expectedUpdateCount
specifier|private
name|int
name|expectedUpdateCount
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|UriParam
DECL|field|breakBatchOnConsumeFail
specifier|private
name|boolean
name|breakBatchOnConsumeFail
decl_stmt|;
DECL|class|DataHolder
specifier|private
specifier|static
specifier|final
class|class
name|DataHolder
block|{
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
DECL|field|data
specifier|private
name|Object
name|data
decl_stmt|;
DECL|method|DataHolder ()
specifier|private
name|DataHolder
parameter_list|()
block|{         }
block|}
DECL|method|SqlConsumer (SqlEndpoint endpoint, Processor processor, JdbcTemplate jdbcTemplate, String query, SqlPrepareStatementStrategy sqlPrepareStatementStrategy, SqlProcessingStrategy sqlProcessingStrategy)
specifier|public
name|SqlConsumer
parameter_list|(
name|SqlEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|JdbcTemplate
name|jdbcTemplate
parameter_list|,
name|String
name|query
parameter_list|,
name|SqlPrepareStatementStrategy
name|sqlPrepareStatementStrategy
parameter_list|,
name|SqlProcessingStrategy
name|sqlProcessingStrategy
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
name|jdbcTemplate
operator|=
name|jdbcTemplate
expr_stmt|;
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
name|this
operator|.
name|sqlPrepareStatementStrategy
operator|=
name|sqlPrepareStatementStrategy
expr_stmt|;
name|this
operator|.
name|sqlProcessingStrategy
operator|=
name|sqlProcessingStrategy
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SqlEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SqlEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
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
comment|// must reset for each poll
name|shutdownRunningTask
operator|=
literal|null
expr_stmt|;
name|pendingExchanges
operator|=
literal|0
expr_stmt|;
specifier|final
name|String
name|preparedQuery
init|=
name|sqlPrepareStatementStrategy
operator|.
name|prepareQuery
argument_list|(
name|query
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|isAllowNamedParameters
argument_list|()
argument_list|)
decl_stmt|;
name|Integer
name|messagePolled
init|=
name|jdbcTemplate
operator|.
name|execute
argument_list|(
name|preparedQuery
argument_list|,
operator|new
name|PreparedStatementCallback
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|doInPreparedStatement
parameter_list|(
name|PreparedStatement
name|preparedStatement
parameter_list|)
throws|throws
name|SQLException
throws|,
name|DataAccessException
block|{
name|Queue
argument_list|<
name|DataHolder
argument_list|>
name|answer
init|=
operator|new
name|LinkedList
argument_list|<
name|DataHolder
argument_list|>
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Executing query: {}"
argument_list|,
name|preparedQuery
argument_list|)
expr_stmt|;
name|ResultSet
name|rs
init|=
name|preparedStatement
operator|.
name|executeQuery
argument_list|()
decl_stmt|;
try|try
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Got result list from query: {}"
argument_list|,
name|rs
argument_list|)
expr_stmt|;
name|RowMapperResultSetExtractor
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|mapper
init|=
operator|new
name|RowMapperResultSetExtractor
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|(
operator|new
name|ColumnMapRowMapper
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|data
init|=
name|mapper
operator|.
name|extractData
argument_list|(
name|rs
argument_list|)
decl_stmt|;
comment|// create a list of exchange objects with the data
if|if
condition|(
name|useIterator
condition|)
block|{
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|item
range|:
name|data
control|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|item
argument_list|)
decl_stmt|;
name|DataHolder
name|holder
init|=
operator|new
name|DataHolder
argument_list|()
decl_stmt|;
name|holder
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|holder
operator|.
name|data
operator|=
name|item
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|holder
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|data
operator|.
name|isEmpty
argument_list|()
operator|||
name|routeEmptyResultSet
condition|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|DataHolder
name|holder
init|=
operator|new
name|DataHolder
argument_list|()
decl_stmt|;
name|holder
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|holder
operator|.
name|data
operator|=
name|data
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|holder
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|rs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// process all the exchanges in this batch
try|try
block|{
name|int
name|rows
init|=
name|processBatch
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|answer
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
name|rows
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|messagePolled
return|;
block|}
DECL|method|createExchange (Object data)
specifier|protected
name|Exchange
name|createExchange
parameter_list|(
name|Object
name|data
parameter_list|)
block|{
specifier|final
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
decl_stmt|;
name|Message
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|data
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
DECL|method|processBatch (Queue<Object> exchanges)
specifier|public
name|int
name|processBatch
parameter_list|(
name|Queue
argument_list|<
name|Object
argument_list|>
name|exchanges
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|total
init|=
name|exchanges
operator|.
name|size
argument_list|()
decl_stmt|;
comment|// limit if needed
if|if
condition|(
name|maxMessagesPerPoll
operator|>
literal|0
operator|&&
name|total
operator|>
name|maxMessagesPerPoll
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Limiting to maximum messages to poll "
operator|+
name|maxMessagesPerPoll
operator|+
literal|" as there was "
operator|+
name|total
operator|+
literal|" messages in this poll."
argument_list|)
expr_stmt|;
name|total
operator|=
name|maxMessagesPerPoll
expr_stmt|;
block|}
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|total
operator|&&
name|isBatchAllowed
argument_list|()
condition|;
name|index
operator|++
control|)
block|{
comment|// only loop if we are started (allowed to run)
name|DataHolder
name|holder
init|=
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|DataHolder
operator|.
name|class
argument_list|,
name|exchanges
operator|.
name|poll
argument_list|()
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|holder
operator|.
name|exchange
decl_stmt|;
name|Object
name|data
init|=
name|holder
operator|.
name|data
decl_stmt|;
comment|// add current index and total as properties
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_SIZE
argument_list|,
name|total
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|,
name|index
operator|==
name|total
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|// update pending number of exchanges
name|pendingExchanges
operator|=
name|total
operator|-
name|index
operator|-
literal|1
expr_stmt|;
comment|// process the current exchange
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// pick the on consume to use
name|String
name|sql
init|=
name|exchange
operator|.
name|isFailed
argument_list|()
condition|?
name|onConsumeFailed
else|:
name|onConsume
decl_stmt|;
try|try
block|{
comment|// we can only run on consume if there was data
if|if
condition|(
name|data
operator|!=
literal|null
operator|&&
name|sql
operator|!=
literal|null
condition|)
block|{
name|int
name|updateCount
init|=
name|sqlProcessingStrategy
operator|.
name|commit
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|data
argument_list|,
name|jdbcTemplate
argument_list|,
name|sql
argument_list|)
decl_stmt|;
if|if
condition|(
name|expectedUpdateCount
operator|>
operator|-
literal|1
operator|&&
name|updateCount
operator|!=
name|expectedUpdateCount
condition|)
block|{
name|String
name|msg
init|=
literal|"Expected update count "
operator|+
name|expectedUpdateCount
operator|+
literal|" but was "
operator|+
name|updateCount
operator|+
literal|" executing query: "
operator|+
name|sql
decl_stmt|;
throw|throw
operator|new
name|SQLException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|breakBatchOnConsumeFail
condition|)
block|{
throw|throw
name|e
throw|;
block|}
else|else
block|{
name|handleException
argument_list|(
literal|"Error executing onConsume/onConsumeFailed query "
operator|+
name|sql
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
try|try
block|{
if|if
condition|(
name|onConsumeBatchComplete
operator|!=
literal|null
condition|)
block|{
name|int
name|updateCount
init|=
name|sqlProcessingStrategy
operator|.
name|commitBatchComplete
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|jdbcTemplate
argument_list|,
name|onConsumeBatchComplete
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"onConsumeBatchComplete update count {}"
argument_list|,
name|updateCount
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|breakBatchOnConsumeFail
condition|)
block|{
throw|throw
name|e
throw|;
block|}
else|else
block|{
name|handleException
argument_list|(
literal|"Error executing onConsumeBatchComplete query "
operator|+
name|onConsumeBatchComplete
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|total
return|;
block|}
DECL|method|getOnConsume ()
specifier|public
name|String
name|getOnConsume
parameter_list|()
block|{
return|return
name|onConsume
return|;
block|}
comment|/**      * Sets a SQL to execute when the row has been successfully processed.      */
DECL|method|setOnConsume (String onConsume)
specifier|public
name|void
name|setOnConsume
parameter_list|(
name|String
name|onConsume
parameter_list|)
block|{
name|this
operator|.
name|onConsume
operator|=
name|onConsume
expr_stmt|;
block|}
DECL|method|getOnConsumeFailed ()
specifier|public
name|String
name|getOnConsumeFailed
parameter_list|()
block|{
return|return
name|onConsumeFailed
return|;
block|}
comment|/**      * Sets a SQL to execute when the row failed being processed.      */
DECL|method|setOnConsumeFailed (String onConsumeFailed)
specifier|public
name|void
name|setOnConsumeFailed
parameter_list|(
name|String
name|onConsumeFailed
parameter_list|)
block|{
name|this
operator|.
name|onConsumeFailed
operator|=
name|onConsumeFailed
expr_stmt|;
block|}
DECL|method|getOnConsumeBatchComplete ()
specifier|public
name|String
name|getOnConsumeBatchComplete
parameter_list|()
block|{
return|return
name|onConsumeBatchComplete
return|;
block|}
DECL|method|setOnConsumeBatchComplete (String onConsumeBatchComplete)
specifier|public
name|void
name|setOnConsumeBatchComplete
parameter_list|(
name|String
name|onConsumeBatchComplete
parameter_list|)
block|{
name|this
operator|.
name|onConsumeBatchComplete
operator|=
name|onConsumeBatchComplete
expr_stmt|;
block|}
comment|/**      * Indicates how resultset should be delivered to the route      */
DECL|method|isUseIterator ()
specifier|public
name|boolean
name|isUseIterator
parameter_list|()
block|{
return|return
name|useIterator
return|;
block|}
comment|/**      * Sets how resultset should be delivered to route.      * Indicates delivery as either a list or individual object.      * defaults to true.      */
DECL|method|setUseIterator (boolean useIterator)
specifier|public
name|void
name|setUseIterator
parameter_list|(
name|boolean
name|useIterator
parameter_list|)
block|{
name|this
operator|.
name|useIterator
operator|=
name|useIterator
expr_stmt|;
block|}
comment|/**      * Indicates whether empty resultset should be allowed to be sent to the next hop or not      */
DECL|method|isRouteEmptyResultSet ()
specifier|public
name|boolean
name|isRouteEmptyResultSet
parameter_list|()
block|{
return|return
name|routeEmptyResultSet
return|;
block|}
comment|/**      * Sets whether empty resultset should be allowed to be sent to the next hop.      * defaults to false. So the empty resultset will be filtered out.      */
DECL|method|setRouteEmptyResultSet (boolean routeEmptyResultSet)
specifier|public
name|void
name|setRouteEmptyResultSet
parameter_list|(
name|boolean
name|routeEmptyResultSet
parameter_list|)
block|{
name|this
operator|.
name|routeEmptyResultSet
operator|=
name|routeEmptyResultSet
expr_stmt|;
block|}
DECL|method|getExpectedUpdateCount ()
specifier|public
name|int
name|getExpectedUpdateCount
parameter_list|()
block|{
return|return
name|expectedUpdateCount
return|;
block|}
comment|/**      * Sets an expected update count to validate when using onConsume.      *      * @param expectedUpdateCount typically set this value to<tt>1</tt> to expect 1 row updated.      */
DECL|method|setExpectedUpdateCount (int expectedUpdateCount)
specifier|public
name|void
name|setExpectedUpdateCount
parameter_list|(
name|int
name|expectedUpdateCount
parameter_list|)
block|{
name|this
operator|.
name|expectedUpdateCount
operator|=
name|expectedUpdateCount
expr_stmt|;
block|}
DECL|method|isBreakBatchOnConsumeFail ()
specifier|public
name|boolean
name|isBreakBatchOnConsumeFail
parameter_list|()
block|{
return|return
name|breakBatchOnConsumeFail
return|;
block|}
comment|/**      * Sets whether to break batch if onConsume failed.      */
DECL|method|setBreakBatchOnConsumeFail (boolean breakBatchOnConsumeFail)
specifier|public
name|void
name|setBreakBatchOnConsumeFail
parameter_list|(
name|boolean
name|breakBatchOnConsumeFail
parameter_list|)
block|{
name|this
operator|.
name|breakBatchOnConsumeFail
operator|=
name|breakBatchOnConsumeFail
expr_stmt|;
block|}
block|}
end_class

end_unit

