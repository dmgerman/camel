begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.elsql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|elsql
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opengamma
operator|.
name|elsql
operator|.
name|ElSql
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opengamma
operator|.
name|elsql
operator|.
name|ElSqlConfig
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
name|component
operator|.
name|sql
operator|.
name|SqlEndpoint
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
name|component
operator|.
name|sql
operator|.
name|SqlPrepareStatementStrategy
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
name|component
operator|.
name|sql
operator|.
name|SqlProcessingStrategy
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
name|ObjectHelper
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
name|ResourceHelper
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
name|namedparam
operator|.
name|NamedParameterJdbcTemplate
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"elsql"
argument_list|,
name|title
operator|=
literal|"SQL"
argument_list|,
name|syntax
operator|=
literal|"elsql:elsqlName:resourceUri"
argument_list|,
name|consumerClass
operator|=
name|ElsqlConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"database,sql"
argument_list|)
DECL|class|ElsqlEndpoint
specifier|public
class|class
name|ElsqlEndpoint
extends|extends
name|SqlEndpoint
block|{
DECL|field|elSql
specifier|private
specifier|volatile
name|ElSql
name|elSql
decl_stmt|;
DECL|field|namedJdbcTemplate
specifier|private
name|NamedParameterJdbcTemplate
name|namedJdbcTemplate
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
DECL|field|elsqlName
specifier|private
name|String
name|elsqlName
decl_stmt|;
annotation|@
name|UriPath
DECL|field|resourceUri
specifier|private
name|String
name|resourceUri
decl_stmt|;
annotation|@
name|UriParam
DECL|field|elSqlConfig
specifier|private
name|ElSqlConfig
name|elSqlConfig
decl_stmt|;
DECL|method|ElsqlEndpoint (String uri, Component component, NamedParameterJdbcTemplate namedJdbcTemplate, String elsqlName, String resourceUri)
specifier|public
name|ElsqlEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|NamedParameterJdbcTemplate
name|namedJdbcTemplate
parameter_list|,
name|String
name|elsqlName
parameter_list|,
name|String
name|resourceUri
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|elsqlName
operator|=
name|elsqlName
expr_stmt|;
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
name|this
operator|.
name|namedJdbcTemplate
operator|=
name|namedJdbcTemplate
expr_stmt|;
block|}
annotation|@
name|Override
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
name|SqlProcessingStrategy
name|proStrategy
init|=
operator|new
name|ElsqlSqlProcessingStrategy
argument_list|(
name|elsqlName
argument_list|,
name|elSql
argument_list|)
decl_stmt|;
name|SqlPrepareStatementStrategy
name|preStategy
init|=
operator|new
name|ElsqlSqlPrepareStatementStrategy
argument_list|()
decl_stmt|;
name|JdbcTemplate
name|template
init|=
operator|new
name|JdbcTemplate
argument_list|(
name|getDataSource
argument_list|()
argument_list|)
decl_stmt|;
name|ElsqlConsumer
name|consumer
init|=
operator|new
name|ElsqlConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|template
argument_list|,
name|elsqlName
argument_list|,
name|preStategy
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
name|consumer
operator|.
name|setBreakBatchOnConsumeFail
argument_list|(
name|isBreakBatchOnConsumeFail
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setExpectedUpdateCount
argument_list|(
name|getExpectedUpdateCount
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setUseIterator
argument_list|(
name|isUseIterator
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setRouteEmptyResultSet
argument_list|(
name|isRouteEmptyResultSet
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
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|ElsqlProducer
name|result
init|=
operator|new
name|ElsqlProducer
argument_list|(
name|this
argument_list|,
name|elSql
argument_list|,
name|elsqlName
argument_list|,
name|namedJdbcTemplate
argument_list|)
decl_stmt|;
return|return
name|result
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|elSqlConfig
argument_list|,
literal|"elSqlConfig"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|resourceUri
argument_list|,
literal|"resourceUri"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsUrl
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|resourceUri
argument_list|)
decl_stmt|;
name|elSql
operator|=
name|ElSql
operator|.
name|parse
argument_list|(
name|elSqlConfig
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
comment|/**      * The name of the elsql to use (is @NAMED in the elsql file)      */
DECL|method|getElsqlName ()
specifier|public
name|String
name|getElsqlName
parameter_list|()
block|{
return|return
name|elsqlName
return|;
block|}
DECL|method|getElSqlConfig ()
specifier|public
name|ElSqlConfig
name|getElSqlConfig
parameter_list|()
block|{
return|return
name|elSqlConfig
return|;
block|}
comment|/**      * The elsql configuration to use      */
DECL|method|setElSqlConfig (ElSqlConfig elSqlConfig)
specifier|public
name|void
name|setElSqlConfig
parameter_list|(
name|ElSqlConfig
name|elSqlConfig
parameter_list|)
block|{
name|this
operator|.
name|elSqlConfig
operator|=
name|elSqlConfig
expr_stmt|;
block|}
DECL|method|getResourceUri ()
specifier|public
name|String
name|getResourceUri
parameter_list|()
block|{
return|return
name|resourceUri
return|;
block|}
comment|/**      * The eqlsql resource tile which contains the elsql SQL statements to use      */
DECL|method|setResourceUri (String resourceUri)
specifier|public
name|void
name|setResourceUri
parameter_list|(
name|String
name|resourceUri
parameter_list|)
block|{
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
block|}
block|}
end_class

end_unit

