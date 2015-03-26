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
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLDataException
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
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|Component
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
name|Consumer
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
name|Producer
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
name|DefaultPollingEndpoint
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
name|Metadata
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
name|UriEndpoint
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
name|spi
operator|.
name|UriPath
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
name|UnsafeUriCharactersEncoder
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
name|BeanPropertyRowMapper
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
name|RowMapper
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
comment|/**  * SQL Endpoint. Endpoint URI should contain valid SQL statement, but instead of  * question marks (that are parameter placeholders), sharp signs should be used.  * This is because in camel question mark has other meaning.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"sql"
argument_list|,
name|title
operator|=
literal|"SQL"
argument_list|,
name|syntax
operator|=
literal|"sql:query"
argument_list|,
name|consumerClass
operator|=
name|SqlConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"database,sql"
argument_list|)
DECL|class|SqlEndpoint
specifier|public
class|class
name|SqlEndpoint
extends|extends
name|DefaultPollingEndpoint
block|{
DECL|field|jdbcTemplate
specifier|private
name|JdbcTemplate
name|jdbcTemplate
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Deprecated
DECL|field|dataSourceRef
specifier|private
name|String
name|dataSourceRef
decl_stmt|;
annotation|@
name|UriParam
DECL|field|dataSource
specifier|private
name|DataSource
name|dataSource
decl_stmt|;
annotation|@
name|UriParam
DECL|field|batch
specifier|private
name|boolean
name|batch
decl_stmt|;
annotation|@
name|UriParam
DECL|field|maxMessagesPerPoll
specifier|private
name|int
name|maxMessagesPerPoll
decl_stmt|;
annotation|@
name|UriParam
DECL|field|processingStrategy
specifier|private
name|SqlProcessingStrategy
name|processingStrategy
decl_stmt|;
annotation|@
name|UriParam
DECL|field|prepareStatementStrategy
specifier|private
name|SqlPrepareStatementStrategy
name|prepareStatementStrategy
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
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|allowNamedParameters
specifier|private
name|boolean
name|allowNamedParameters
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|alwaysPopulateStatement
specifier|private
name|boolean
name|alwaysPopulateStatement
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|","
argument_list|)
DECL|field|separator
specifier|private
name|char
name|separator
init|=
literal|','
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"SelectList"
argument_list|)
DECL|field|outputType
specifier|private
name|SqlOutputType
name|outputType
init|=
name|SqlOutputType
operator|.
name|SelectList
decl_stmt|;
annotation|@
name|UriParam
DECL|field|outputClass
specifier|private
name|String
name|outputClass
decl_stmt|;
annotation|@
name|UriParam
DECL|field|parametersCount
specifier|private
name|int
name|parametersCount
decl_stmt|;
annotation|@
name|UriParam
DECL|field|noop
specifier|private
name|boolean
name|noop
decl_stmt|;
annotation|@
name|UriParam
DECL|field|outputHeader
specifier|private
name|String
name|outputHeader
decl_stmt|;
DECL|method|SqlEndpoint ()
specifier|public
name|SqlEndpoint
parameter_list|()
block|{     }
DECL|method|SqlEndpoint (String uri, Component component, JdbcTemplate jdbcTemplate, String query)
specifier|public
name|SqlEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|JdbcTemplate
name|jdbcTemplate
parameter_list|,
name|String
name|query
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
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
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|SqlPrepareStatementStrategy
name|prepareStrategy
init|=
name|prepareStatementStrategy
operator|!=
literal|null
condition|?
name|prepareStatementStrategy
else|:
operator|new
name|DefaultSqlPrepareStatementStrategy
argument_list|(
name|separator
argument_list|)
decl_stmt|;
name|SqlProcessingStrategy
name|proStrategy
init|=
name|processingStrategy
operator|!=
literal|null
condition|?
name|processingStrategy
else|:
operator|new
name|DefaultSqlProcessingStrategy
argument_list|(
name|prepareStrategy
argument_list|)
decl_stmt|;
name|SqlConsumer
name|consumer
init|=
operator|new
name|SqlConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|jdbcTemplate
argument_list|,
name|query
argument_list|,
name|prepareStrategy
argument_list|,
name|proStrategy
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setMaxMessagesPerPoll
argument_list|(
name|getMaxMessagesPerPoll
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setOnConsume
argument_list|(
name|getOnConsume
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setOnConsumeFailed
argument_list|(
name|getOnConsumeFailed
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setOnConsumeBatchComplete
argument_list|(
name|getOnConsumeBatchComplete
argument_list|()
argument_list|)
expr_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|SqlPrepareStatementStrategy
name|prepareStrategy
init|=
name|prepareStatementStrategy
operator|!=
literal|null
condition|?
name|prepareStatementStrategy
else|:
operator|new
name|DefaultSqlPrepareStatementStrategy
argument_list|(
name|separator
argument_list|)
decl_stmt|;
name|SqlProducer
name|result
init|=
operator|new
name|SqlProducer
argument_list|(
name|this
argument_list|,
name|query
argument_list|,
name|jdbcTemplate
argument_list|,
name|prepareStrategy
argument_list|,
name|batch
argument_list|,
name|alwaysPopulateStatement
argument_list|)
decl_stmt|;
name|result
operator|.
name|setParametersCount
argument_list|(
name|parametersCount
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getJdbcTemplate ()
specifier|public
name|JdbcTemplate
name|getJdbcTemplate
parameter_list|()
block|{
return|return
name|jdbcTemplate
return|;
block|}
DECL|method|setJdbcTemplate (JdbcTemplate jdbcTemplate)
specifier|public
name|void
name|setJdbcTemplate
parameter_list|(
name|JdbcTemplate
name|jdbcTemplate
parameter_list|)
block|{
name|this
operator|.
name|jdbcTemplate
operator|=
name|jdbcTemplate
expr_stmt|;
block|}
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
comment|/**      * Sets the SQL query to perform      */
DECL|method|setQuery (String query)
specifier|public
name|void
name|setQuery
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
DECL|method|isBatch ()
specifier|public
name|boolean
name|isBatch
parameter_list|()
block|{
return|return
name|batch
return|;
block|}
comment|/**      * Enables or disables batch mode      */
DECL|method|setBatch (boolean batch)
specifier|public
name|void
name|setBatch
parameter_list|(
name|boolean
name|batch
parameter_list|)
block|{
name|this
operator|.
name|batch
operator|=
name|batch
expr_stmt|;
block|}
DECL|method|getMaxMessagesPerPoll ()
specifier|public
name|int
name|getMaxMessagesPerPoll
parameter_list|()
block|{
return|return
name|maxMessagesPerPoll
return|;
block|}
comment|/**      * Sets the maximum number of messages to poll      */
DECL|method|setMaxMessagesPerPoll (int maxMessagesPerPoll)
specifier|public
name|void
name|setMaxMessagesPerPoll
parameter_list|(
name|int
name|maxMessagesPerPoll
parameter_list|)
block|{
name|this
operator|.
name|maxMessagesPerPoll
operator|=
name|maxMessagesPerPoll
expr_stmt|;
block|}
DECL|method|getProcessingStrategy ()
specifier|public
name|SqlProcessingStrategy
name|getProcessingStrategy
parameter_list|()
block|{
return|return
name|processingStrategy
return|;
block|}
comment|/**      * Allows to plugin to use a custom org.apache.camel.component.sql.SqlProcessingStrategy to execute queries when the consumer has processed the rows/batch.      */
DECL|method|setProcessingStrategy (SqlProcessingStrategy processingStrategy)
specifier|public
name|void
name|setProcessingStrategy
parameter_list|(
name|SqlProcessingStrategy
name|processingStrategy
parameter_list|)
block|{
name|this
operator|.
name|processingStrategy
operator|=
name|processingStrategy
expr_stmt|;
block|}
DECL|method|getPrepareStatementStrategy ()
specifier|public
name|SqlPrepareStatementStrategy
name|getPrepareStatementStrategy
parameter_list|()
block|{
return|return
name|prepareStatementStrategy
return|;
block|}
comment|/**      * Allows to plugin to use a custom org.apache.camel.component.sql.SqlPrepareStatementStrategy to control preparation of the query and prepared statement.      */
DECL|method|setPrepareStatementStrategy (SqlPrepareStatementStrategy prepareStatementStrategy)
specifier|public
name|void
name|setPrepareStatementStrategy
parameter_list|(
name|SqlPrepareStatementStrategy
name|prepareStatementStrategy
parameter_list|)
block|{
name|this
operator|.
name|prepareStatementStrategy
operator|=
name|prepareStatementStrategy
expr_stmt|;
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
comment|/**      * After processing each row then this query can be executed, if the Exchange was processed successfully, for example to mark the row as processed. The query can have parameter.      */
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
comment|/**      * After processing each row then this query can be executed, if the Exchange failed, for example to mark the row as failed. The query can have parameter.      */
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
comment|/**      * After processing the entire batch, this query can be executed to bulk update rows etc. The query cannot have parameters.      */
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
DECL|method|isAllowNamedParameters ()
specifier|public
name|boolean
name|isAllowNamedParameters
parameter_list|()
block|{
return|return
name|allowNamedParameters
return|;
block|}
comment|/**      * Whether to allow using named parameters in the queries.      */
DECL|method|setAllowNamedParameters (boolean allowNamedParameters)
specifier|public
name|void
name|setAllowNamedParameters
parameter_list|(
name|boolean
name|allowNamedParameters
parameter_list|)
block|{
name|this
operator|.
name|allowNamedParameters
operator|=
name|allowNamedParameters
expr_stmt|;
block|}
DECL|method|isAlwaysPopulateStatement ()
specifier|public
name|boolean
name|isAlwaysPopulateStatement
parameter_list|()
block|{
return|return
name|alwaysPopulateStatement
return|;
block|}
comment|/**      * If enabled then the populateStatement method from org.apache.camel.component.sql.SqlPrepareStatementStrategy is always invoked,      * also if there is no expected parameters to be prepared. When this is false then the populateStatement is only invoked if there      * is 1 or more expected parameters to be set; for example this avoids reading the message body/headers for SQL queries with no parameters.      */
DECL|method|setAlwaysPopulateStatement (boolean alwaysPopulateStatement)
specifier|public
name|void
name|setAlwaysPopulateStatement
parameter_list|(
name|boolean
name|alwaysPopulateStatement
parameter_list|)
block|{
name|this
operator|.
name|alwaysPopulateStatement
operator|=
name|alwaysPopulateStatement
expr_stmt|;
block|}
DECL|method|getSeparator ()
specifier|public
name|char
name|getSeparator
parameter_list|()
block|{
return|return
name|separator
return|;
block|}
comment|/**      * The separator to use when parameter values is taken from message body (if the body is a String type), to be inserted at # placeholders.      * Notice if you use named parameters, then a Map type is used instead.      *<p/>      * The default value is ,      */
DECL|method|setSeparator (char separator)
specifier|public
name|void
name|setSeparator
parameter_list|(
name|char
name|separator
parameter_list|)
block|{
name|this
operator|.
name|separator
operator|=
name|separator
expr_stmt|;
block|}
DECL|method|getOutputType ()
specifier|public
name|SqlOutputType
name|getOutputType
parameter_list|()
block|{
return|return
name|outputType
return|;
block|}
comment|/**      * Make the output of consumer or producer to SelectList as List of Map, or SelectOne as single Java object in the following way:      * a) If the query has only single column, then that JDBC Column object is returned. (such as SELECT COUNT( * ) FROM PROJECT will return a Long object.      * b) If the query has more than one column, then it will return a Map of that result.      * c) If the outputClass is set, then it will convert the query result into an Java bean object by calling all the setters that match the column names.       * It will assume your class has a default constructor to create instance with.      * d) If the query resulted in more than one rows, it throws an non-unique result exception.      */
DECL|method|setOutputType (SqlOutputType outputType)
specifier|public
name|void
name|setOutputType
parameter_list|(
name|SqlOutputType
name|outputType
parameter_list|)
block|{
name|this
operator|.
name|outputType
operator|=
name|outputType
expr_stmt|;
block|}
DECL|method|getOutputClass ()
specifier|public
name|String
name|getOutputClass
parameter_list|()
block|{
return|return
name|outputClass
return|;
block|}
comment|/**      * Specify the full package and class name to use as conversion when outputType=SelectOne.      */
DECL|method|setOutputClass (String outputClass)
specifier|public
name|void
name|setOutputClass
parameter_list|(
name|String
name|outputClass
parameter_list|)
block|{
name|this
operator|.
name|outputClass
operator|=
name|outputClass
expr_stmt|;
block|}
DECL|method|getParametersCount ()
specifier|public
name|int
name|getParametersCount
parameter_list|()
block|{
return|return
name|parametersCount
return|;
block|}
comment|/**      * If set greater than zero, then Camel will use this count value of parameters to replace instead of querying via JDBC metadata API.      * This is useful if the JDBC vendor could not return correct parameters count, then user may override instead.      */
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
DECL|method|isNoop ()
specifier|public
name|boolean
name|isNoop
parameter_list|()
block|{
return|return
name|noop
return|;
block|}
comment|/**      * If set, will ignore the results of the SQL query and use the existing IN message as the OUT message for the continuation of processing      */
DECL|method|setNoop (boolean noop)
specifier|public
name|void
name|setNoop
parameter_list|(
name|boolean
name|noop
parameter_list|)
block|{
name|this
operator|.
name|noop
operator|=
name|noop
expr_stmt|;
block|}
DECL|method|getOutputHeader ()
specifier|public
name|String
name|getOutputHeader
parameter_list|()
block|{
return|return
name|outputHeader
return|;
block|}
comment|/**      * Store the query result in a header instead of the message body.      * By default, outputHeader == null and the query result is stored in the message body,      * any existing content in the message body is discarded.      * If outputHeader is set, the value is used as the name of the header to store the      * query result and the original message body is preserved.      */
DECL|method|setOutputHeader (String outputHeader)
specifier|public
name|void
name|setOutputHeader
parameter_list|(
name|String
name|outputHeader
parameter_list|)
block|{
name|this
operator|.
name|outputHeader
operator|=
name|outputHeader
expr_stmt|;
block|}
DECL|method|getDataSourceRef ()
specifier|public
name|String
name|getDataSourceRef
parameter_list|()
block|{
return|return
name|dataSourceRef
return|;
block|}
comment|/**      * Sets the reference to a DataSource to lookup from the registry, to use for communicating with the database.      */
DECL|method|setDataSourceRef (String dataSourceRef)
specifier|public
name|void
name|setDataSourceRef
parameter_list|(
name|String
name|dataSourceRef
parameter_list|)
block|{
name|this
operator|.
name|dataSourceRef
operator|=
name|dataSourceRef
expr_stmt|;
block|}
DECL|method|getDataSource ()
specifier|public
name|DataSource
name|getDataSource
parameter_list|()
block|{
return|return
name|dataSource
return|;
block|}
comment|/**      * Sets the DataSource to use to communicate with the database.      */
DECL|method|setDataSource (DataSource dataSource)
specifier|public
name|void
name|setDataSource
parameter_list|(
name|DataSource
name|dataSource
parameter_list|)
block|{
name|this
operator|.
name|dataSource
operator|=
name|dataSource
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
comment|// Make sure it's properly encoded
return|return
literal|"sql:"
operator|+
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|query
argument_list|)
return|;
block|}
DECL|method|queryForList (ResultSet rs, boolean allowMapToClass)
specifier|protected
name|List
argument_list|<
name|?
argument_list|>
name|queryForList
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|boolean
name|allowMapToClass
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|allowMapToClass
operator|&&
name|outputClass
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|outputClzz
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|outputClass
argument_list|)
decl_stmt|;
name|RowMapper
name|rowMapper
init|=
operator|new
name|BeanPropertyRowMapper
argument_list|(
name|outputClzz
argument_list|)
decl_stmt|;
name|RowMapperResultSetExtractor
argument_list|<
name|?
argument_list|>
name|mapper
init|=
operator|new
name|RowMapperResultSetExtractor
argument_list|(
name|rowMapper
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
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
return|return
name|data
return|;
block|}
else|else
block|{
name|ColumnMapRowMapper
name|rowMapper
init|=
operator|new
name|ColumnMapRowMapper
argument_list|()
decl_stmt|;
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
name|rowMapper
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
return|return
name|data
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|queryForObject (ResultSet rs)
specifier|protected
name|Object
name|queryForObject
parameter_list|(
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
block|{
name|Object
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|outputClass
operator|==
literal|null
condition|)
block|{
name|RowMapper
name|rowMapper
init|=
operator|new
name|ColumnMapRowMapper
argument_list|()
decl_stmt|;
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
name|rowMapper
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
if|if
condition|(
name|data
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|SQLDataException
argument_list|(
literal|"Query result not unique for outputType=SelectOne. Got "
operator|+
name|data
operator|.
name|size
argument_list|()
operator|+
literal|" count instead."
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|data
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// Set content depend on number of column from query result
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|row
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|result
operator|=
name|row
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|row
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|outputClzz
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|outputClass
argument_list|)
decl_stmt|;
name|RowMapper
name|rowMapper
init|=
operator|new
name|BeanPropertyRowMapper
argument_list|(
name|outputClzz
argument_list|)
decl_stmt|;
name|RowMapperResultSetExtractor
argument_list|<
name|?
argument_list|>
name|mapper
init|=
operator|new
name|RowMapperResultSetExtractor
argument_list|(
name|rowMapper
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
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
if|if
condition|(
name|data
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|SQLDataException
argument_list|(
literal|"Query result not unique for outputType=SelectOne. Got "
operator|+
name|data
operator|.
name|size
argument_list|()
operator|+
literal|" count instead."
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|data
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|result
operator|=
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
comment|// If data.size is zero, let result be null.
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

