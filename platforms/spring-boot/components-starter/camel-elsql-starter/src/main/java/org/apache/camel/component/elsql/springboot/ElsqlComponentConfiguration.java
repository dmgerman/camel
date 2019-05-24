begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.elsql.springboot
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
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|elsql
operator|.
name|ElSqlDatabaseVendor
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * The elsql component is an extension to the existing SQL Component that uses  * ElSql to define the SQL queries.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.elsql"
argument_list|)
DECL|class|ElsqlComponentConfiguration
specifier|public
class|class
name|ElsqlComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the elsql component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To use a vendor specific com.opengamma.elsql.ElSqlConfig      */
DECL|field|databaseVendor
specifier|private
name|ElSqlDatabaseVendor
name|databaseVendor
decl_stmt|;
comment|/**      * Sets the DataSource to use to communicate with the database. The option      * is a javax.sql.DataSource type.      */
DECL|field|dataSource
specifier|private
name|String
name|dataSource
decl_stmt|;
comment|/**      * To use a specific configured ElSqlConfig. It may be better to use the      * databaseVendor option instead. The option is a      * com.opengamma.elsql.ElSqlConfig type.      */
DECL|field|elSqlConfig
specifier|private
name|String
name|elSqlConfig
decl_stmt|;
comment|/**      * The resource file which contains the elsql SQL statements to use. You can      * specify multiple resources separated by comma. The resources are loaded      * on the classpath by default, you can prefix with file: to load from file      * system. Notice you can set this option on the component and then you do      * not have to configure this on the endpoint.      */
DECL|field|resourceUri
specifier|private
name|String
name|resourceUri
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
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
DECL|method|getDataSource ()
specifier|public
name|String
name|getDataSource
parameter_list|()
block|{
return|return
name|dataSource
return|;
block|}
DECL|method|setDataSource (String dataSource)
specifier|public
name|void
name|setDataSource
parameter_list|(
name|String
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
DECL|method|getElSqlConfig ()
specifier|public
name|String
name|getElSqlConfig
parameter_list|()
block|{
return|return
name|elSqlConfig
return|;
block|}
DECL|method|setElSqlConfig (String elSqlConfig)
specifier|public
name|void
name|setElSqlConfig
parameter_list|(
name|String
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
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
block|}
end_class

end_unit

