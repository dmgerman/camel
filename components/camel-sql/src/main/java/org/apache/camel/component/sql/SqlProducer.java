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
name|Connection
import|;
end_import

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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|impl
operator|.
name|DefaultProducer
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
name|PreparedStatementCreator
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|support
operator|.
name|JdbcUtils
operator|.
name|closeResultSet
import|;
end_import

begin_class
DECL|class|SqlProducer
specifier|public
class|class
name|SqlProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|query
specifier|private
specifier|final
name|String
name|query
decl_stmt|;
DECL|field|resolvedQuery
specifier|private
name|String
name|resolvedQuery
decl_stmt|;
DECL|field|jdbcTemplate
specifier|private
specifier|final
name|JdbcTemplate
name|jdbcTemplate
decl_stmt|;
DECL|field|batch
specifier|private
specifier|final
name|boolean
name|batch
decl_stmt|;
DECL|field|alwaysPopulateStatement
specifier|private
specifier|final
name|boolean
name|alwaysPopulateStatement
decl_stmt|;
DECL|field|sqlPrepareStatementStrategy
specifier|private
specifier|final
name|SqlPrepareStatementStrategy
name|sqlPrepareStatementStrategy
decl_stmt|;
DECL|field|useMessageBodyForSql
specifier|private
specifier|final
name|boolean
name|useMessageBodyForSql
decl_stmt|;
DECL|field|parametersCount
specifier|private
name|int
name|parametersCount
decl_stmt|;
DECL|method|SqlProducer (SqlEndpoint endpoint, String query, JdbcTemplate jdbcTemplate, SqlPrepareStatementStrategy sqlPrepareStatementStrategy, boolean batch, boolean alwaysPopulateStatement, boolean useMessageBodyForSql)
specifier|public
name|SqlProducer
parameter_list|(
name|SqlEndpoint
name|endpoint
parameter_list|,
name|String
name|query
parameter_list|,
name|JdbcTemplate
name|jdbcTemplate
parameter_list|,
name|SqlPrepareStatementStrategy
name|sqlPrepareStatementStrategy
parameter_list|,
name|boolean
name|batch
parameter_list|,
name|boolean
name|alwaysPopulateStatement
parameter_list|,
name|boolean
name|useMessageBodyForSql
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
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
name|sqlPrepareStatementStrategy
operator|=
name|sqlPrepareStatementStrategy
expr_stmt|;
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
name|this
operator|.
name|batch
operator|=
name|batch
expr_stmt|;
name|this
operator|.
name|alwaysPopulateStatement
operator|=
name|alwaysPopulateStatement
expr_stmt|;
name|this
operator|.
name|useMessageBodyForSql
operator|=
name|useMessageBodyForSql
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
name|String
name|placeholder
init|=
name|getEndpoint
argument_list|()
operator|.
name|isUsePlaceholder
argument_list|()
condition|?
name|getEndpoint
argument_list|()
operator|.
name|getPlaceholder
argument_list|()
else|:
literal|null
decl_stmt|;
name|resolvedQuery
operator|=
name|SqlHelper
operator|.
name|resolveQuery
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|query
argument_list|,
name|placeholder
argument_list|)
expr_stmt|;
block|}
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|String
name|sql
decl_stmt|;
if|if
condition|(
name|useMessageBodyForSql
condition|)
block|{
name|sql
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|queryHeader
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_QUERY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|sql
operator|=
name|queryHeader
operator|!=
literal|null
condition|?
name|queryHeader
else|:
name|resolvedQuery
expr_stmt|;
block|}
specifier|final
name|String
name|preparedQuery
init|=
name|sqlPrepareStatementStrategy
operator|.
name|prepareQuery
argument_list|(
name|sql
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|isAllowNamedParameters
argument_list|()
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
comment|// CAMEL-7313 - check whether to return generated keys
specifier|final
name|Boolean
name|shouldRetrieveGeneratedKeys
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_RETRIEVE_GENERATED_KEYS
argument_list|,
literal|false
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|PreparedStatementCreator
name|statementCreator
init|=
operator|new
name|PreparedStatementCreator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|PreparedStatement
name|createPreparedStatement
parameter_list|(
name|Connection
name|con
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
operator|!
name|shouldRetrieveGeneratedKeys
condition|)
block|{
return|return
name|con
operator|.
name|prepareStatement
argument_list|(
name|preparedQuery
argument_list|)
return|;
block|}
else|else
block|{
name|Object
name|expectedGeneratedColumns
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_GENERATED_COLUMNS
argument_list|)
decl_stmt|;
if|if
condition|(
name|expectedGeneratedColumns
operator|==
literal|null
condition|)
block|{
return|return
name|con
operator|.
name|prepareStatement
argument_list|(
name|preparedQuery
argument_list|,
name|Statement
operator|.
name|RETURN_GENERATED_KEYS
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|expectedGeneratedColumns
operator|instanceof
name|String
index|[]
condition|)
block|{
return|return
name|con
operator|.
name|prepareStatement
argument_list|(
name|preparedQuery
argument_list|,
operator|(
name|String
index|[]
operator|)
name|expectedGeneratedColumns
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|expectedGeneratedColumns
operator|instanceof
name|int
index|[]
condition|)
block|{
return|return
name|con
operator|.
name|prepareStatement
argument_list|(
name|preparedQuery
argument_list|,
operator|(
name|int
index|[]
operator|)
name|expectedGeneratedColumns
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Header specifying expected returning columns isn't an instance of String[] or int[] but "
operator|+
name|expectedGeneratedColumns
operator|.
name|getClass
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
decl_stmt|;
name|jdbcTemplate
operator|.
name|execute
argument_list|(
name|statementCreator
argument_list|,
operator|new
name|PreparedStatementCallback
argument_list|<
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|doInPreparedStatement
parameter_list|(
name|PreparedStatement
name|ps
parameter_list|)
throws|throws
name|SQLException
block|{
name|ResultSet
name|rs
init|=
literal|null
decl_stmt|;
try|try
block|{
name|int
name|expected
init|=
name|parametersCount
operator|>
literal|0
condition|?
name|parametersCount
else|:
name|ps
operator|.
name|getParameterMetaData
argument_list|()
operator|.
name|getParameterCount
argument_list|()
decl_stmt|;
comment|// only populate if really needed
if|if
condition|(
name|alwaysPopulateStatement
operator|||
name|expected
operator|>
literal|0
condition|)
block|{
comment|// transfer incoming message body data to prepared statement parameters, if necessary
if|if
condition|(
name|batch
condition|)
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|iterator
decl_stmt|;
if|if
condition|(
name|useMessageBodyForSql
condition|)
block|{
name|iterator
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_PARAMETERS
argument_list|,
name|Iterator
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|iterator
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Iterator
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|iterator
operator|!=
literal|null
operator|&&
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|value
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|i
init|=
name|sqlPrepareStatementStrategy
operator|.
name|createPopulateIterator
argument_list|(
name|sql
argument_list|,
name|preparedQuery
argument_list|,
name|expected
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|sqlPrepareStatementStrategy
operator|.
name|populateStatement
argument_list|(
name|ps
argument_list|,
name|i
argument_list|,
name|expected
argument_list|)
expr_stmt|;
name|ps
operator|.
name|addBatch
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|Object
name|value
decl_stmt|;
if|if
condition|(
name|useMessageBodyForSql
condition|)
block|{
name|value
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_PARAMETERS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|value
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|?
argument_list|>
name|i
init|=
name|sqlPrepareStatementStrategy
operator|.
name|createPopulateIterator
argument_list|(
name|sql
argument_list|,
name|preparedQuery
argument_list|,
name|expected
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|sqlPrepareStatementStrategy
operator|.
name|populateStatement
argument_list|(
name|ps
argument_list|,
name|i
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
name|boolean
name|isResultSet
init|=
literal|false
decl_stmt|;
comment|// execute the prepared statement and populate the outgoing message
if|if
condition|(
name|batch
condition|)
block|{
name|int
index|[]
name|updateCounts
init|=
name|ps
operator|.
name|executeBatch
argument_list|()
decl_stmt|;
name|int
name|total
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|count
range|:
name|updateCounts
control|)
block|{
name|total
operator|+=
name|count
expr_stmt|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_UPDATE_COUNT
argument_list|,
name|total
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|isResultSet
operator|=
name|ps
operator|.
name|execute
argument_list|()
expr_stmt|;
if|if
condition|(
name|isResultSet
condition|)
block|{
comment|// preserve headers first, so we can override the SQL_ROW_COUNT header
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|rs
operator|=
name|ps
operator|.
name|getResultSet
argument_list|()
expr_stmt|;
name|SqlOutputType
name|outputType
init|=
name|getEndpoint
argument_list|()
operator|.
name|getOutputType
argument_list|()
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Got result list from query: {}, outputType={}"
argument_list|,
name|rs
argument_list|,
name|outputType
argument_list|)
expr_stmt|;
if|if
condition|(
name|outputType
operator|==
name|SqlOutputType
operator|.
name|SelectList
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|data
init|=
name|getEndpoint
argument_list|()
operator|.
name|queryForList
argument_list|(
name|rs
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// for noop=true we still want to enrich with the row count header
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isNoop
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getOutputHeader
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getOutputHeader
argument_list|()
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_ROW_COUNT
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|outputType
operator|==
name|SqlOutputType
operator|.
name|SelectOne
condition|)
block|{
name|Object
name|data
init|=
name|getEndpoint
argument_list|()
operator|.
name|queryForObject
argument_list|(
name|rs
argument_list|)
decl_stmt|;
if|if
condition|(
name|data
operator|!=
literal|null
condition|)
block|{
comment|// for noop=true we still want to enrich with the row count header
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isNoop
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getOutputHeader
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getOutputHeader
argument_list|()
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_ROW_COUNT
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid outputType="
operator|+
name|outputType
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_UPDATE_COUNT
argument_list|,
name|ps
operator|.
name|getUpdateCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|shouldRetrieveGeneratedKeys
condition|)
block|{
comment|// if no OUT message yet then create one and propagate headers
if|if
condition|(
operator|!
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isResultSet
condition|)
block|{
comment|// we won't return generated keys for SELECT statements
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_GENERATED_KEYS_DATA
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_GENERATED_KEYS_ROW_COUNT
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|List
argument_list|<
name|?
argument_list|>
name|generatedKeys
init|=
name|getEndpoint
argument_list|()
operator|.
name|queryForList
argument_list|(
name|ps
operator|.
name|getGeneratedKeys
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_GENERATED_KEYS_DATA
argument_list|,
name|generatedKeys
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_GENERATED_KEYS_ROW_COUNT
argument_list|,
name|generatedKeys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// data is set on exchange so return null
return|return
literal|null
return|;
block|}
finally|finally
block|{
name|closeResultSet
argument_list|(
name|rs
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|)
function|;
block|}
end_class

begin_function
DECL|method|setParametersCount (int parametersCount)
specifier|public
name|void
name|setParametersCount
parameter_list|(
name|int
name|parametersCount
parameter_list|)
block|{
name|this
operator|.
name|parametersCount
operator|=
name|parametersCount
expr_stmt|;
block|}
end_function

unit|}
end_unit

