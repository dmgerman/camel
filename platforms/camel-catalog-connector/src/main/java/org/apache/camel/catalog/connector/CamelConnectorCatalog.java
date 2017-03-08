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

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MXBean
import|;
end_import

begin_comment
comment|/**  * Catalog of connectors.  */
end_comment

begin_interface
annotation|@
name|MXBean
DECL|interface|CamelConnectorCatalog
specifier|public
interface|interface
name|CamelConnectorCatalog
block|{
comment|/**      * To configure which {@link ConnectorDataStore} to use      */
DECL|method|setConnectorDataStore (ConnectorDataStore dataStore)
name|void
name|setConnectorDataStore
parameter_list|(
name|ConnectorDataStore
name|dataStore
parameter_list|)
function_decl|;
comment|/**      * Is the connector already registered in the catalog      *      * @param groupId               maven group id      * @param artifactId            maven artifact id      * @param version               maven version      * @return whether the catalog has the connector or not      */
DECL|method|hasConnector (String groupId, String artifactId, String version)
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
function_decl|;
comment|/**      * Adds or updates the connector to the catalog      *      * @param groupId               maven group id      * @param artifactId            maven artifact id      * @param version               maven version      * @param name                  name of connector      * @param scheme                scheme of connector      * @param description           description of connector      * @param labels                labels (separated by comma) of connector      * @param connectorJson         the<tt>camel-connector</tt> json file      * @param connectorSchemaJson   the<tt>camel-connector-schema</tt> json file      */
DECL|method|addConnector (String groupId, String artifactId, String version, String name, String scheme, String description, String labels, String connectorJson, String connectorSchemaJson)
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
name|scheme
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
function_decl|;
comment|/**      * Removes the connector from the catalog      *      * @param groupId               maven group id      * @param artifactId            maven artifact id      * @param version               maven version      */
DECL|method|removeConnector (String groupId, String artifactId, String version)
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
function_decl|;
comment|/**      * Finds all the connectors from the catalog      *      * @param latestVersionOnly  whether to include only latest version of the connectors      */
DECL|method|findConnector (boolean latestVersionOnly)
name|List
argument_list|<
name|ConnectorDto
argument_list|>
name|findConnector
parameter_list|(
name|boolean
name|latestVersionOnly
parameter_list|)
function_decl|;
comment|/**      * Find all the connectors that matches the maven coordinate, name, label or description from the catalog      *      * @param filter             filter text      * @param latestVersionOnly  whether to include only latest version of the connectors      */
DECL|method|findConnector (String filter, boolean latestVersionOnly)
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
function_decl|;
comment|/**      * Returns the<tt>camel-connector</tt> json file for the given connector with the Maven coordinate      *      * @param groupId     maven group id      * @param artifactId  maven artifact id      * @param version     maven version      */
DECL|method|connectorJSon (String groupId, String artifactId, String version)
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
function_decl|;
comment|/**      * Returns the<tt>camel-connector-schema</tt> json file for the given connector with the Maven coordinate      *      * @param groupId     maven group id      * @param artifactId  maven artifact id      * @param version     maven version      */
DECL|method|connectorSchemaJSon (String groupId, String artifactId, String version)
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
function_decl|;
block|}
end_interface

end_unit

