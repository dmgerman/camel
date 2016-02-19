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
name|UriParam
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
comment|/**  * Base class for SQL endpoints.  */
end_comment

begin_class
DECL|class|DefaultSqlEndpoint
specifier|public
specifier|abstract
class|class
name|DefaultSqlEndpoint
extends|extends
name|DefaultPollingEndpoint
block|{
DECL|field|jdbcTemplate
specifier|private
name|JdbcTemplate
name|jdbcTemplate
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Sets the reference to a DataSource to lookup from the registry, to use for communicating with the database."
argument_list|)
annotation|@
name|Deprecated
DECL|field|dataSourceRef
specifier|private
name|String
name|dataSourceRef
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Sets the DataSource to use to communicate with the database."
argument_list|)
DECL|field|dataSource
specifier|private
name|DataSource
name|dataSource
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Enables or disables transaction. If enabled then if processing an exchange failed then the consumer"
operator|+
literal|"break out processing any further exchanges to cause a rollback eager."
argument_list|)
DECL|field|transacted
specifier|private
name|boolean
name|transacted
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Enables or disables batch mode"
argument_list|)
DECL|field|batch
specifier|private
name|boolean
name|batch
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Sets the maximum number of messages to poll"
argument_list|)
DECL|field|maxMessagesPerPoll
specifier|private
name|int
name|maxMessagesPerPoll
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|description
operator|=
literal|"Allows to plugin to use a custom org.apache.camel.component.sql.SqlProcessingStrategy to execute queries when the consumer has processed the rows/batch."
argument_list|)
DECL|field|processingStrategy
specifier|private
name|SqlProcessingStrategy
name|processingStrategy
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Allows to plugin to use a custom org.apache.camel.component.sql.SqlPrepareStatementStrategy to control preparation of the query and prepared statement."
argument_list|)
DECL|field|prepareStatementStrategy
specifier|private
name|SqlPrepareStatementStrategy
name|prepareStatementStrategy
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"After processing each row then this query can be executed, if the Exchange was processed successfully, for example to mark the row as processed. The query can have"
operator|+
literal|" parameter."
argument_list|)
DECL|field|onConsume
specifier|private
name|String
name|onConsume
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"After processing each row then this query can be executed, if the Exchange failed, for example to mark the row as failed. The query can have parameter."
argument_list|)
DECL|field|onConsumeFailed
specifier|private
name|String
name|onConsumeFailed
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"After processing the entire batch, this query can be executed to bulk update rows etc. The query cannot have parameters."
argument_list|)
DECL|field|onConsumeBatchComplete
specifier|private
name|String
name|onConsumeBatchComplete
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|description
operator|=
literal|"Sets how resultset should be delivered to route. Indicates delivery as either a list or individual object. defaults to true."
argument_list|)
DECL|field|useIterator
specifier|private
name|boolean
name|useIterator
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Sets whether empty resultset should be allowed to be sent to the next hop. Defaults to false. So the empty resultset will be filtered out."
argument_list|)
DECL|field|routeEmptyResultSet
specifier|private
name|boolean
name|routeEmptyResultSet
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"-1"
argument_list|,
name|description
operator|=
literal|"Sets an expected update count to validate when using onConsume."
argument_list|)
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
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Sets whether to break batch if onConsume failed."
argument_list|)
DECL|field|breakBatchOnConsumeFail
specifier|private
name|boolean
name|breakBatchOnConsumeFail
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|description
operator|=
literal|"Whether to allow using named parameters in the queries."
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
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|,
name|description
operator|=
literal|"If enabled then the populateStatement method from org.apache.camel.component.sql.SqlPrepareStatementStrategy is always invoked, "
operator|+
literal|"also if there is no expected parameters to be prepared. When this is false then the populateStatement is only invoked if there is 1"
operator|+
literal|" or more expected parameters to be set; for example this avoids reading the message body/headers for SQL queries with no parameters."
argument_list|)
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
argument_list|,
name|description
operator|=
literal|"The separator to use when parameter values is taken from message body (if the body is a String type), to be inserted at # placeholders."
operator|+
literal|"Notice if you use named parameters, then a Map type is used instead. The default value is comma"
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
argument_list|,
name|description
operator|=
literal|"Make the output of consumer or producer to SelectList as List of Map, or SelectOne as single Java object in the following way:"
operator|+
literal|"a) If the query has only single column, then that JDBC Column object is returned. (such as SELECT COUNT( * ) FROM PROJECT will return a Long object."
operator|+
literal|"b) If the query has more than one column, then it will return a Map of that result."
operator|+
literal|"c) If the outputClass is set, then it will convert the query result into an Java bean object by calling all the setters that match the column names."
operator|+
literal|"It will assume your class has a default constructor to create instance with."
operator|+
literal|"d) If the query resulted in more than one rows, it throws an non-unique result exception."
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
argument_list|(
name|description
operator|=
literal|"Specify the full package and class name to use as conversion when outputType=SelectOne."
argument_list|)
DECL|field|outputClass
specifier|private
name|String
name|outputClass
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|,
name|description
operator|=
literal|"If set greater than zero, then Camel will use this count value of parameters to replace instead of"
operator|+
literal|" querying via JDBC metadata API. This is useful if the JDBC vendor could not return correct parameters count, then user may override instead."
argument_list|)
DECL|field|parametersCount
specifier|private
name|int
name|parametersCount
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"If set, will ignore the results of the SQL query and use the existing IN message as the OUT message for the continuation of processing"
argument_list|)
DECL|field|noop
specifier|private
name|boolean
name|noop
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Store the query result in a header instead of the message body. By default, outputHeader == null and the query result is stored"
operator|+
literal|" in the message body, any existing content in the message body is discarded. If outputHeader is set, the value is used as the name of the header"
operator|+
literal|" to store the query result and the original message body is preserved."
argument_list|)
DECL|field|outputHeader
specifier|private
name|String
name|outputHeader
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Whether to use the message body as the SQL and then headers for parameters. If this option is enabled then the SQL in the uri is not used."
argument_list|)
DECL|field|useMessageBodyForSql
specifier|private
name|boolean
name|useMessageBodyForSql
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"#"
argument_list|,
name|description
operator|=
literal|"Specifies a character that will be replaced to ? in SQL query."
operator|+
literal|" Notice, that it is simple String.replaceAll() operation and no SQL parsing is involved (quoted strings will also change)."
argument_list|)
DECL|field|placeholder
specifier|private
name|String
name|placeholder
init|=
literal|"#"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|description
operator|=
literal|"Sets whether to use placeholder and replace all placeholder characters with ? sign in the SQL queries."
argument_list|)
DECL|field|usePlaceholder
specifier|private
name|boolean
name|usePlaceholder
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|prefix
operator|=
literal|"template."
argument_list|,
name|multiValue
operator|=
literal|true
argument_list|,
name|description
operator|=
literal|"Configures the Spring JdbcTemplate with the key/values from the Map"
argument_list|)
DECL|field|templateOptions
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|templateOptions
decl_stmt|;
DECL|method|DefaultSqlEndpoint ()
specifier|public
name|DefaultSqlEndpoint
parameter_list|()
block|{     }
DECL|method|DefaultSqlEndpoint (String uri, Component component, JdbcTemplate jdbcTemplate)
specifier|public
name|DefaultSqlEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|JdbcTemplate
name|jdbcTemplate
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
DECL|method|isTransacted ()
specifier|public
name|boolean
name|isTransacted
parameter_list|()
block|{
return|return
name|transacted
return|;
block|}
comment|/**      * Enables or disables transaction. If enabled then if processing an exchange failed then the consumer      + break out processing any further exchanges to cause a rollback eager      */
DECL|method|setTransacted (boolean transacted)
specifier|public
name|void
name|setTransacted
parameter_list|(
name|boolean
name|transacted
parameter_list|)
block|{
name|this
operator|.
name|transacted
operator|=
name|transacted
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
comment|/**      * The separator to use when parameter values is taken from message body (if the body is a String type), to be inserted at # placeholders.      * Notice if you use named parameters, then a Map type is used instead.      *<p/>      * The default value is comma.      */
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
DECL|method|isUseMessageBodyForSql ()
specifier|public
name|boolean
name|isUseMessageBodyForSql
parameter_list|()
block|{
return|return
name|useMessageBodyForSql
return|;
block|}
comment|/**      * Whether to use the message body as the SQL and then headers for parameters.      *<p/>      * If this option is enabled then the SQL in the uri is not used.      */
DECL|method|setUseMessageBodyForSql (boolean useMessageBodyForSql)
specifier|public
name|void
name|setUseMessageBodyForSql
parameter_list|(
name|boolean
name|useMessageBodyForSql
parameter_list|)
block|{
name|this
operator|.
name|useMessageBodyForSql
operator|=
name|useMessageBodyForSql
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
comment|/**      * Sets how resultset should be delivered to route. Indicates delivery as either a list or individual object. defaults to true.      */
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
comment|/**      * Sets whether empty resultset should be allowed to be sent to the next hop.      * Defaults to false. So the empty resultset will be filtered out.      */
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
comment|/**      * Sets an expected update count to validate when using onConsume.      */
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
DECL|method|getPlaceholder ()
specifier|public
name|String
name|getPlaceholder
parameter_list|()
block|{
return|return
name|placeholder
return|;
block|}
comment|/**      * Specifies a character that will be replaced to ? in SQL query.      * Notice, that it is simple String.replaceAll() operation and no SQL parsing is involved (quoted strings will also change).      */
DECL|method|setPlaceholder (String placeholder)
specifier|public
name|void
name|setPlaceholder
parameter_list|(
name|String
name|placeholder
parameter_list|)
block|{
name|this
operator|.
name|placeholder
operator|=
name|placeholder
expr_stmt|;
block|}
DECL|method|isUsePlaceholder ()
specifier|public
name|boolean
name|isUsePlaceholder
parameter_list|()
block|{
return|return
name|usePlaceholder
return|;
block|}
comment|/**      * Sets whether to use placeholder and replace all placeholder characters with ? sign in the SQL queries.      *<p/>      * This option is default<tt>true</tt>      */
DECL|method|setUsePlaceholder (boolean usePlaceholder)
specifier|public
name|void
name|setUsePlaceholder
parameter_list|(
name|boolean
name|usePlaceholder
parameter_list|)
block|{
name|this
operator|.
name|usePlaceholder
operator|=
name|usePlaceholder
expr_stmt|;
block|}
DECL|method|getTemplateOptions ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getTemplateOptions
parameter_list|()
block|{
return|return
name|templateOptions
return|;
block|}
comment|/**      * Configures the Spring JdbcTemplate with the key/values from the Map      */
DECL|method|setTemplateOptions (Map<String, Object> templateOptions)
specifier|public
name|void
name|setTemplateOptions
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|templateOptions
parameter_list|)
block|{
name|this
operator|.
name|templateOptions
operator|=
name|templateOptions
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|queryForList (ResultSet rs, boolean allowMapToClass)
specifier|public
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
name|outputClazz
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
name|outputClazz
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
specifier|public
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

