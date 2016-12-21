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
name|CamelContextHelper
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
name|IntrospectionSupport
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
DECL|class|ElsqlComponent
specifier|public
class|class
name|ElsqlComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|databaseVendor
specifier|private
name|ElSqlDatabaseVendor
name|databaseVendor
decl_stmt|;
DECL|field|dataSource
specifier|private
name|DataSource
name|dataSource
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|elSqlConfig
specifier|private
name|ElSqlConfig
name|elSqlConfig
decl_stmt|;
DECL|field|resourceUri
specifier|private
name|String
name|resourceUri
decl_stmt|;
DECL|method|ElsqlComponent ()
specifier|public
name|ElsqlComponent
parameter_list|()
block|{
name|super
argument_list|(
name|ElsqlEndpoint
operator|.
name|class
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
name|getDataSource
argument_list|()
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
name|NamedParameterJdbcTemplate
name|jdbcTemplate
init|=
operator|new
name|NamedParameterJdbcTemplate
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|jdbcTemplate
argument_list|,
name|parameters
argument_list|,
literal|"template."
argument_list|)
expr_stmt|;
name|String
name|elsqlName
init|=
name|remaining
decl_stmt|;
name|String
name|resUri
init|=
name|resourceUri
decl_stmt|;
name|String
index|[]
name|part
init|=
name|remaining
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|part
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|elsqlName
operator|=
name|part
index|[
literal|0
index|]
expr_stmt|;
name|resUri
operator|=
name|part
index|[
literal|1
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|part
operator|.
name|length
operator|>
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid uri. Must by elsql:elsqlName:resourceUri, was: "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|String
name|onConsume
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"consumer.onConsume"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|onConsume
operator|==
literal|null
condition|)
block|{
name|onConsume
operator|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"onConsume"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|String
name|onConsumeFailed
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"consumer.onConsumeFailed"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|onConsumeFailed
operator|==
literal|null
condition|)
block|{
name|onConsumeFailed
operator|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"onConsumeFailed"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|String
name|onConsumeBatchComplete
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"consumer.onConsumeBatchComplete"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|onConsumeBatchComplete
operator|==
literal|null
condition|)
block|{
name|onConsumeBatchComplete
operator|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"onConsumeBatchComplete"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|ElsqlEndpoint
name|endpoint
init|=
operator|new
name|ElsqlEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|jdbcTemplate
argument_list|,
name|target
argument_list|,
name|elsqlName
argument_list|,
name|resUri
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setElSqlConfig
argument_list|(
name|elSqlConfig
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setDatabaseVendor
argument_list|(
name|databaseVendor
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setDataSource
argument_list|(
name|ds
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setDataSourceRef
argument_list|(
name|dataSourceRef
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setOnConsume
argument_list|(
name|onConsume
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setOnConsumeFailed
argument_list|(
name|onConsumeFailed
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setOnConsumeBatchComplete
argument_list|(
name|onConsumeBatchComplete
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getDatabaseVendor ()
specifier|public
name|ElSqlDatabaseVendor
name|getDatabaseVendor
parameter_list|()
block|{
return|return
name|databaseVendor
return|;
block|}
comment|/**      * To use a vendor specific {@link com.opengamma.elsql.ElSqlConfig}      */
DECL|method|setDatabaseVendor (ElSqlDatabaseVendor databaseVendor)
specifier|public
name|void
name|setDatabaseVendor
parameter_list|(
name|ElSqlDatabaseVendor
name|databaseVendor
parameter_list|)
block|{
name|this
operator|.
name|databaseVendor
operator|=
name|databaseVendor
expr_stmt|;
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
comment|/**      * To use a specific configured ElSqlConfig. It may be better to use the<tt>databaseVendor</tt> option instead.      */
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
comment|/**      * The resource file which contains the elsql SQL statements to use. You can specify multiple resources separated by comma.      * The resources are loaded on the classpath by default, you can prefix with<tt>file:</tt> to load from file system.      * Notice you can set this option on the component and then you do not have to configure this on the endpoint.      */
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

