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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLClassLoader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|JsonNode
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
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
name|catalog
operator|.
name|CatalogHelper
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
name|catalog
operator|.
name|CollectionStringBuffer
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|CatalogHelper
operator|.
name|loadText
import|;
end_import

begin_comment
comment|/**  * Nexus repository that can scan for custom Camel connectors and add to the {@link ConnectorDataStore}.  */
end_comment

begin_class
DECL|class|ConnectorDataStoreNexusRepository
specifier|public
class|class
name|ConnectorDataStoreNexusRepository
extends|extends
name|BaseNexusRepository
block|{
DECL|field|connectorDataStore
specifier|private
name|ConnectorDataStore
name|connectorDataStore
decl_stmt|;
DECL|method|ConnectorDataStoreNexusRepository ()
specifier|public
name|ConnectorDataStoreNexusRepository
parameter_list|()
block|{
name|super
argument_list|(
literal|"connector"
argument_list|)
expr_stmt|;
block|}
DECL|method|getConnectorDataStore ()
specifier|public
name|ConnectorDataStore
name|getConnectorDataStore
parameter_list|()
block|{
return|return
name|connectorDataStore
return|;
block|}
DECL|method|setConnectorDataStore (ConnectorDataStore connectorDataStore)
specifier|public
name|void
name|setConnectorDataStore
parameter_list|(
name|ConnectorDataStore
name|connectorDataStore
parameter_list|)
block|{
name|this
operator|.
name|connectorDataStore
operator|=
name|connectorDataStore
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
if|if
condition|(
name|connectorDataStore
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ConnectorDataStore must be configured"
argument_list|)
throw|;
block|}
name|super
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onNewArtifacts (Set<NexusArtifactDto> newArtifacts)
specifier|public
name|void
name|onNewArtifacts
parameter_list|(
name|Set
argument_list|<
name|NexusArtifactDto
argument_list|>
name|newArtifacts
parameter_list|)
block|{
comment|// now download the new artifact JARs and look inside to find more details
for|for
control|(
name|NexusArtifactDto
name|dto
range|:
name|newArtifacts
control|)
block|{
try|try
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Processing new artifact: {}:{}:{}"
argument_list|,
name|dto
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|dto
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|dto
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|url
init|=
name|createArtifactURL
argument_list|(
name|dto
argument_list|)
decl_stmt|;
name|URL
name|jarUrl
init|=
operator|new
name|URL
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|addCustomCamelConnectorFromArtifact
argument_list|(
name|dto
argument_list|,
name|jarUrl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error downloading connector JAR "
operator|+
name|dto
operator|.
name|getArtifactLink
argument_list|()
operator|+
literal|". This exception is ignored. "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Adds the connector to the data store      *      * @param dto                 the artifact      * @param name                the name of connector      * @param description         the description of connector      * @param labels              the labels of connector      * @param connectorJson       camel-connector JSon      * @param connectorSchemaJson camel-connector-schema JSon      */
DECL|method|addConnector (NexusArtifactDto dto, String name, String description, String labels, String connectorJson, String connectorSchemaJson)
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
parameter_list|)
block|{
if|if
condition|(
name|connectorDataStore
operator|!=
literal|null
condition|)
block|{
name|ConnectorDto
name|connector
init|=
operator|new
name|ConnectorDto
argument_list|(
name|dto
argument_list|,
name|name
argument_list|,
name|description
argument_list|,
name|labels
argument_list|,
name|connectorJson
argument_list|,
name|connectorSchemaJson
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Added connector: {}:{}:{}"
argument_list|,
name|dto
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|dto
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|dto
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|connectorDataStore
operator|.
name|addConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Adds any discovered third party Camel connectors from the artifact.      */
DECL|method|addCustomCamelConnectorFromArtifact (NexusArtifactDto dto, URL jarUrl)
specifier|private
name|void
name|addCustomCamelConnectorFromArtifact
parameter_list|(
name|NexusArtifactDto
name|dto
parameter_list|,
name|URL
name|jarUrl
parameter_list|)
block|{
try|try
init|(
name|URLClassLoader
name|classLoader
init|=
operator|new
name|URLClassLoader
argument_list|(
operator|new
name|URL
index|[]
block|{
name|jarUrl
block|}
argument_list|)
init|;
init|)
block|{
name|String
index|[]
name|json
init|=
name|loadConnectorJSonSchema
argument_list|(
name|classLoader
argument_list|)
decl_stmt|;
if|if
condition|(
name|json
operator|!=
literal|null
condition|)
block|{
name|ObjectMapper
name|mapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
name|JsonNode
name|tree
init|=
name|mapper
operator|.
name|readTree
argument_list|(
name|json
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|tree
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
operator|.
name|textValue
argument_list|()
decl_stmt|;
name|String
name|description
init|=
name|tree
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
operator|.
name|textValue
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|JsonNode
argument_list|>
name|it
init|=
name|tree
operator|.
name|withArray
argument_list|(
literal|"labels"
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|CollectionStringBuffer
name|csb
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
literal|","
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|text
init|=
name|it
operator|.
name|next
argument_list|()
operator|.
name|textValue
argument_list|()
decl_stmt|;
name|csb
operator|.
name|append
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
name|addConnector
argument_list|(
name|dto
argument_list|,
name|name
argument_list|,
name|description
argument_list|,
name|csb
operator|.
name|toString
argument_list|()
argument_list|,
name|json
index|[
literal|0
index|]
argument_list|,
name|json
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error scanning JAR for custom Camel components"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|loadConnectorJSonSchema (URLClassLoader classLoader)
specifier|private
name|String
index|[]
name|loadConnectorJSonSchema
parameter_list|(
name|URLClassLoader
name|classLoader
parameter_list|)
block|{
name|String
index|[]
name|answer
init|=
operator|new
name|String
index|[
literal|2
index|]
decl_stmt|;
name|String
name|path
init|=
literal|"camel-connector.json"
decl_stmt|;
try|try
block|{
name|InputStream
name|is
init|=
name|classLoader
operator|.
name|getResourceAsStream
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|answer
index|[
literal|0
index|]
operator|=
name|loadText
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error loading "
operator|+
name|path
operator|+
literal|" file"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|path
operator|=
literal|"camel-connector-schema.json"
expr_stmt|;
try|try
block|{
name|InputStream
name|is
init|=
name|classLoader
operator|.
name|getResourceAsStream
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|answer
index|[
literal|1
index|]
operator|=
name|loadText
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error loading "
operator|+
name|path
operator|+
literal|" file"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

