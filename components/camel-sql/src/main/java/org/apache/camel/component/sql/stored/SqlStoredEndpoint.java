begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql.stored
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
operator|.
name|stored
package|;
end_package

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
name|stored
operator|.
name|template
operator|.
name|TemplateParser
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
name|JdbcTemplate
import|;
end_import

begin_comment
comment|/**  * The sql component allows you to work with databases using JDBC Stored Procedure queries.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.17.0"
argument_list|,
name|scheme
operator|=
literal|"sql-stored"
argument_list|,
name|title
operator|=
literal|"SQL Stored Procedure"
argument_list|,
name|syntax
operator|=
literal|"sql-stored:template"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"database,sql"
argument_list|)
DECL|class|SqlStoredEndpoint
specifier|public
class|class
name|SqlStoredEndpoint
extends|extends
name|DefaultPollingEndpoint
block|{
DECL|field|wrapperFactory
specifier|private
name|CallableStatementWrapperFactory
name|wrapperFactory
decl_stmt|;
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
literal|"Sets the DataSource to use to communicate with the database."
argument_list|)
DECL|field|dataSource
specifier|private
name|DataSource
name|dataSource
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Sets the StoredProcedure template to perform"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|template
specifier|private
name|String
name|template
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
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
name|description
operator|=
literal|"Whether to use the message body as the template and then headers for parameters. If this option is enabled then the template in the uri is not used."
argument_list|)
DECL|field|useMessageBodyForTemplate
specifier|private
name|boolean
name|useMessageBodyForTemplate
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"If set, will ignore the results of the template and use the existing IN message as the OUT message for the continuation of processing"
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
literal|"Store the template result in a header instead of the message body. By default, outputHeader == null and the template result is stored"
operator|+
literal|" in the message body, any existing content in the message body is discarded. If outputHeader is set, the value is used as the name of the header"
operator|+
literal|" to store the template result and the original message body is preserved."
argument_list|)
DECL|field|outputHeader
specifier|private
name|String
name|outputHeader
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Whether this call is for a function."
argument_list|)
DECL|field|function
specifier|private
name|boolean
name|function
decl_stmt|;
DECL|method|SqlStoredEndpoint (String uri, SqlStoredComponent component, JdbcTemplate jdbcTemplate)
specifier|public
name|SqlStoredEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|SqlStoredComponent
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
name|setJdbcTemplate
argument_list|(
name|jdbcTemplate
argument_list|)
expr_stmt|;
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
name|SqlStoredProducer
argument_list|(
name|this
argument_list|)
return|;
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
literal|"sql-stored:"
operator|+
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|this
operator|.
name|template
argument_list|)
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
name|this
operator|.
name|wrapperFactory
operator|=
operator|new
name|CallableStatementWrapperFactory
argument_list|(
name|jdbcTemplate
argument_list|,
operator|new
name|TemplateParser
argument_list|()
argument_list|,
name|isFunction
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|wrapperFactory
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|wrapperFactory
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
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
DECL|method|isUseMessageBodyForTemplate ()
specifier|public
name|boolean
name|isUseMessageBodyForTemplate
parameter_list|()
block|{
return|return
name|useMessageBodyForTemplate
return|;
block|}
DECL|method|setUseMessageBodyForTemplate (boolean useMessageBodyForTemplate)
specifier|public
name|void
name|setUseMessageBodyForTemplate
parameter_list|(
name|boolean
name|useMessageBodyForTemplate
parameter_list|)
block|{
name|this
operator|.
name|useMessageBodyForTemplate
operator|=
name|useMessageBodyForTemplate
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
DECL|method|getTemplate ()
specifier|public
name|String
name|getTemplate
parameter_list|()
block|{
return|return
name|template
return|;
block|}
DECL|method|setTemplate (String template)
specifier|public
name|void
name|setTemplate
parameter_list|(
name|String
name|template
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
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
DECL|method|isFunction ()
specifier|public
name|boolean
name|isFunction
parameter_list|()
block|{
return|return
name|function
return|;
block|}
DECL|method|setFunction (boolean function)
specifier|public
name|void
name|setFunction
parameter_list|(
name|boolean
name|function
parameter_list|)
block|{
name|this
operator|.
name|function
operator|=
name|function
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|getWrapperFactory ()
specifier|public
name|CallableStatementWrapperFactory
name|getWrapperFactory
parameter_list|()
block|{
return|return
name|wrapperFactory
return|;
block|}
block|}
end_class

end_unit

