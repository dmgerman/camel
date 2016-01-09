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
name|Endpoint
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
name|TemplateStoredProcedureFactory
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
name|UriEndpointComponent
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
name|CamelContextHelper
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

begin_class
DECL|class|SqlStoredComponent
specifier|public
class|class
name|SqlStoredComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|dataSource
specifier|private
name|DataSource
name|dataSource
decl_stmt|;
DECL|method|SqlStoredComponent ()
specifier|public
name|SqlStoredComponent
parameter_list|()
block|{
name|super
argument_list|(
name|SqlStoredEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|SqlStoredComponent (CamelContext context, Class<? extends Endpoint> endpointClass)
specifier|public
name|SqlStoredComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Endpoint
argument_list|>
name|endpointClass
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|endpointClass
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|DataSource
name|target
init|=
literal|null
decl_stmt|;
comment|// endpoint options overrule component configured datasource
name|DataSource
name|ds
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"dataSource"
argument_list|,
name|DataSource
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ds
operator|!=
literal|null
condition|)
block|{
name|target
operator|=
name|ds
expr_stmt|;
block|}
name|String
name|dataSourceRef
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"dataSourceRef"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|target
operator|==
literal|null
operator|&&
name|dataSourceRef
operator|!=
literal|null
condition|)
block|{
name|target
operator|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|dataSourceRef
argument_list|,
name|DataSource
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
comment|// fallback and use component
name|target
operator|=
name|dataSource
expr_stmt|;
block|}
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"DataSource must be configured"
argument_list|)
throw|;
block|}
name|JdbcTemplate
name|template
init|=
operator|new
name|JdbcTemplate
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|TemplateStoredProcedureFactory
name|factory
init|=
operator|new
name|TemplateStoredProcedureFactory
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|SqlStoredEndpoint
name|answer
init|=
operator|new
name|SqlStoredEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setJdbcTemplate
argument_list|(
name|template
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setTemplate
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setTemplateStoredProcedureFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
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
block|}
end_class

end_unit

