begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog.nexus
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|nexus
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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_class
DECL|class|LocalFileConnectorNexusRepository
specifier|public
class|class
name|LocalFileConnectorNexusRepository
extends|extends
name|ConnectorCatalogNexusRepository
block|{
DECL|field|onAddConnector
specifier|private
name|Runnable
name|onAddConnector
decl_stmt|;
DECL|method|getOnAddConnector ()
specifier|public
name|Runnable
name|getOnAddConnector
parameter_list|()
block|{
return|return
name|onAddConnector
return|;
block|}
DECL|method|setOnAddConnector (Runnable onAddConnector)
specifier|public
name|void
name|setOnAddConnector
parameter_list|(
name|Runnable
name|onAddConnector
parameter_list|)
block|{
name|this
operator|.
name|onAddConnector
operator|=
name|onAddConnector
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createNexusUrl ()
specifier|protected
name|URL
name|createNexusUrl
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/test-classes/nexus-sample-connector-result.xml"
argument_list|)
decl_stmt|;
return|return
operator|new
name|URL
argument_list|(
literal|"file:"
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createArtifactURL (NexusArtifactDto dto)
specifier|protected
name|String
name|createArtifactURL
parameter_list|(
name|NexusArtifactDto
name|dto
parameter_list|)
block|{
comment|// load from local file instead
return|return
literal|"file:target/localrepo/"
operator|+
name|dto
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"-"
operator|+
name|dto
operator|.
name|getVersion
argument_list|()
operator|+
literal|".jar"
return|;
block|}
annotation|@
name|Override
DECL|method|addConnector (NexusArtifactDto dto, String name, String scheme, String javaType, String description, String labels, String connectorJson, String connectorSchemaJson, String componentSchemaJson)
specifier|protected
name|void
name|addConnector
parameter_list|(
name|NexusArtifactDto
name|dto
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|scheme
parameter_list|,
name|String
name|javaType
parameter_list|,
name|String
name|description
parameter_list|,
name|String
name|labels
parameter_list|,
name|String
name|connectorJson
parameter_list|,
name|String
name|connectorSchemaJson
parameter_list|,
name|String
name|componentSchemaJson
parameter_list|)
block|{
name|super
operator|.
name|addConnector
argument_list|(
name|dto
argument_list|,
name|name
argument_list|,
name|scheme
argument_list|,
name|javaType
argument_list|,
name|description
argument_list|,
name|labels
argument_list|,
name|connectorJson
argument_list|,
name|connectorSchemaJson
argument_list|,
name|componentSchemaJson
argument_list|)
expr_stmt|;
if|if
condition|(
name|onAddConnector
operator|!=
literal|null
condition|)
block|{
name|onAddConnector
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

