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
name|spi
operator|.
name|*
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
name|JdbcTemplate
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
name|consumerClass
operator|=
name|SqlConsumer
operator|.
name|class
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
DECL|field|query
specifier|private
name|String
name|query
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
init|=
operator|new
name|DefaultSqlProcessingStrategy
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
DECL|field|prepareStatementStrategy
specifier|private
name|SqlPrepareStatementStrategy
name|prepareStatementStrategy
init|=
operator|new
name|DefaultSqlPrepareStatementStrategy
argument_list|()
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
return|return
operator|new
name|SqlProducer
argument_list|(
name|this
argument_list|,
name|query
argument_list|,
name|jdbcTemplate
argument_list|,
name|batch
argument_list|,
name|alwaysPopulateStatement
argument_list|)
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
block|}
end_class

end_unit

