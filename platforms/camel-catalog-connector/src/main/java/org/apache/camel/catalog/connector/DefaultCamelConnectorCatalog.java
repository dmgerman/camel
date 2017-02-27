begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog.connector
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|connector
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|DefaultCamelConnectorCatalog
specifier|public
class|class
name|DefaultCamelConnectorCatalog
implements|implements
name|CamelConnectorCatalog
block|{
DECL|field|dataStore
specifier|private
name|ConnectorDataStore
name|dataStore
init|=
operator|new
name|MemoryConnectorDataStore
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|setConnectorDataStore (ConnectorDataStore dataStore)
specifier|public
name|void
name|setConnectorDataStore
parameter_list|(
name|ConnectorDataStore
name|dataStore
parameter_list|)
block|{
name|this
operator|.
name|dataStore
operator|=
name|dataStore
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hasConnector (String groupId, String artifactId, String version)
specifier|public
name|boolean
name|hasConnector
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|ConnectorDto
name|dto
init|=
operator|new
name|ConnectorDto
argument_list|()
decl_stmt|;
name|dto
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|dto
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|dto
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
return|return
name|dataStore
operator|.
name|hasConnector
argument_list|(
name|dto
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addConnector (String groupId, String artifactId, String version, String name, String description, String labels, String connectorJson, String connectorSchemaJson)
specifier|public
name|void
name|addConnector
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
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
name|ConnectorDto
name|dto
init|=
operator|new
name|ConnectorDto
argument_list|()
decl_stmt|;
name|dto
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|dto
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|dto
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|dto
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|dto
operator|.
name|setDescription
argument_list|(
name|description
argument_list|)
expr_stmt|;
name|dto
operator|.
name|setLabels
argument_list|(
name|labels
argument_list|)
expr_stmt|;
name|dataStore
operator|.
name|addConnector
argument_list|(
name|dto
argument_list|,
name|connectorJson
argument_list|,
name|connectorSchemaJson
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|removeConnector (String groupId, String artifactId, String version)
specifier|public
name|void
name|removeConnector
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|ConnectorDto
name|dto
init|=
operator|new
name|ConnectorDto
argument_list|()
decl_stmt|;
name|dto
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|dto
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|dto
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|dataStore
operator|.
name|removeConnector
argument_list|(
name|dto
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|findConnector (boolean latestVersionOnly)
specifier|public
name|List
argument_list|<
name|ConnectorDto
argument_list|>
name|findConnector
parameter_list|(
name|boolean
name|latestVersionOnly
parameter_list|)
block|{
return|return
name|findConnector
argument_list|(
literal|null
argument_list|,
name|latestVersionOnly
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|findConnector (String filter, boolean latestVersionOnly)
specifier|public
name|List
argument_list|<
name|ConnectorDto
argument_list|>
name|findConnector
parameter_list|(
name|String
name|filter
parameter_list|,
name|boolean
name|latestVersionOnly
parameter_list|)
block|{
return|return
name|dataStore
operator|.
name|findConnector
argument_list|(
name|filter
argument_list|,
name|latestVersionOnly
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|connectorJSon (String groupId, String artifactId, String version)
specifier|public
name|String
name|connectorJSon
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|connectorSchemaJSon (String groupId, String artifactId, String version)
specifier|public
name|String
name|connectorSchemaJSon
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

