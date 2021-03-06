begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|HashSet
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
name|Set
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
name|maven
operator|.
name|config
operator|.
name|ConnectorConfigGenerator
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
name|kafka
operator|.
name|connect
operator|.
name|source
operator|.
name|SourceConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|AbstractMojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoFailureException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|LifecyclePhase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Mojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Parameter
import|;
end_import

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"generate-connector-config"
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|GENERATE_SOURCES
argument_list|)
DECL|class|GenerateConnectorConfigMojo
specifier|public
class|class
name|GenerateConnectorConfigMojo
extends|extends
name|AbstractMojo
block|{
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/generated-sources/connector-configurations"
argument_list|)
DECL|field|generatedSrcDir
specifier|private
name|File
name|generatedSrcDir
decl_stmt|;
comment|/**      * Debezium connector's class name, this has to be fully name with the package, e.g:      * 'io.debezium.connector.mysql.MySqlConnector'      */
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
literal|"camel.debezium.connector.class"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|field|connectorClassName
specifier|private
name|String
name|connectorClassName
decl_stmt|;
comment|/**      * Debezium connector's config class name, this has to be fully name with the package, e.g:      * 'io.debezium.connector.mysql.MySqlConnectorConfig'      */
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
literal|"camel.debezium.connector.config.class"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|field|connectorConfigClassName
specifier|private
name|String
name|connectorConfigClassName
decl_stmt|;
comment|/**      * Fields to override their default value      */
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
literal|"camel.debezium.fields"
argument_list|)
DECL|field|fields
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|fields
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
comment|/**      * Fields that are required      */
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
literal|"camel.debezium.required.fields"
argument_list|)
DECL|field|requiredFields
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|requiredFields
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoFailureException
block|{
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|requiredFields
init|=
name|getRequiredFields
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|overrideFields
init|=
name|getFields
argument_list|()
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|configClazz
init|=
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
name|connectorConfigClassName
argument_list|)
decl_stmt|;
if|if
condition|(
name|configClazz
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"connectorConfigClassName not found."
argument_list|)
throw|;
block|}
try|try
block|{
specifier|final
name|ConnectorConfigGenerator
name|connectorConfigGenerator
init|=
name|ConnectorConfigGenerator
operator|.
name|create
argument_list|(
name|getConnector
argument_list|()
argument_list|,
name|configClazz
argument_list|,
name|requiredFields
argument_list|,
name|overrideFields
argument_list|)
decl_stmt|;
specifier|final
name|File
name|parentPath
init|=
operator|new
name|File
argument_list|(
name|generatedSrcDir
argument_list|,
name|connectorConfigGenerator
operator|.
name|getPackageName
argument_list|()
operator|.
name|replace
argument_list|(
literal|"."
argument_list|,
literal|"/"
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|File
name|connectorConfigClassFile
init|=
operator|new
name|File
argument_list|(
name|parentPath
argument_list|,
name|connectorConfigGenerator
operator|.
name|getClassName
argument_list|()
operator|+
literal|".java"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|connectorConfigClassFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|connectorConfigClassFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|connectorConfigClassFile
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
block|}
name|connectorConfigGenerator
operator|.
name|printGeneratedClass
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|connectorConfigClassFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoFailureException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getConnector ()
specifier|private
name|SourceConnector
name|getConnector
parameter_list|()
block|{
return|return
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ObjectHelper
operator|.
name|newInstance
argument_list|(
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
name|connectorClassName
argument_list|)
argument_list|,
name|SourceConnector
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|setGeneratedSrcDir (final File generatedSrcDir)
specifier|public
name|void
name|setGeneratedSrcDir
parameter_list|(
specifier|final
name|File
name|generatedSrcDir
parameter_list|)
block|{
name|this
operator|.
name|generatedSrcDir
operator|=
name|generatedSrcDir
expr_stmt|;
block|}
DECL|method|setConnectorClassName (String connectorClassName)
specifier|public
name|void
name|setConnectorClassName
parameter_list|(
name|String
name|connectorClassName
parameter_list|)
block|{
name|this
operator|.
name|connectorClassName
operator|=
name|connectorClassName
expr_stmt|;
block|}
DECL|method|setConnectorConfigClassName (String connectorConfigClassName)
specifier|public
name|void
name|setConnectorConfigClassName
parameter_list|(
name|String
name|connectorConfigClassName
parameter_list|)
block|{
name|this
operator|.
name|connectorConfigClassName
operator|=
name|connectorConfigClassName
expr_stmt|;
block|}
DECL|method|setFields (Map<String, Object> fields)
specifier|public
name|void
name|setFields
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|fields
parameter_list|)
block|{
name|this
operator|.
name|fields
operator|=
name|fields
expr_stmt|;
block|}
DECL|method|setRequiredFields (List<String> requiredFields)
specifier|public
name|void
name|setRequiredFields
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|requiredFields
parameter_list|)
block|{
name|this
operator|.
name|requiredFields
operator|=
name|requiredFields
expr_stmt|;
block|}
DECL|method|getRequiredFields ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getRequiredFields
parameter_list|()
block|{
return|return
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|requiredFields
argument_list|)
return|;
block|}
DECL|method|getFields ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getFields
parameter_list|()
block|{
return|return
name|fields
return|;
block|}
block|}
end_class

end_unit

