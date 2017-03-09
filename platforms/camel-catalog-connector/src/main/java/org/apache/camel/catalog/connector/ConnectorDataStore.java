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

begin_comment
comment|/**  * Data store for connector details to be used by the {@link CamelConnectorCatalog}.  */
end_comment

begin_interface
DECL|interface|ConnectorDataStore
specifier|public
interface|interface
name|ConnectorDataStore
block|{
comment|/**      * Adds or updates the connector to the catalog      *      * @param dto                   the connector dto      * @param connectorJson         the<tt>camel-connector</tt> json file      * @param connectorSchemaJson   the<tt>camel-connector-schema</tt> json file      * @param componentSchemaJson   the<tt>camel-component-schema</tt> json file      */
DECL|method|addConnector (ConnectorDto dto, String connectorJson, String connectorSchemaJson, String componentSchemaJson)
name|void
name|addConnector
parameter_list|(
name|ConnectorDto
name|dto
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
function_decl|;
comment|/**      * Is the connector already registered in the catalog      *      * @param dto  the connector dto      */
DECL|method|hasConnector (ConnectorDto dto)
name|boolean
name|hasConnector
parameter_list|(
name|ConnectorDto
name|dto
parameter_list|)
function_decl|;
comment|/**      * Removes the connector from the catalog      *      * @param dto  the connector dto      */
DECL|method|removeConnector (ConnectorDto dto)
name|void
name|removeConnector
parameter_list|(
name|ConnectorDto
name|dto
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
comment|/**      * Returns the<tt>camel-connector</tt> json file for the given connector with the Maven coordinate      *      * @param dto  the connector dto      */
DECL|method|connectorJSon (ConnectorDto dto)
name|String
name|connectorJSon
parameter_list|(
name|ConnectorDto
name|dto
parameter_list|)
function_decl|;
comment|/**      * Returns the<tt>camel-connector-schema</tt> json file for the given connector with the Maven coordinate      *      * @param dto  the connector dto      */
DECL|method|connectorSchemaJSon (ConnectorDto dto)
name|String
name|connectorSchemaJSon
parameter_list|(
name|ConnectorDto
name|dto
parameter_list|)
function_decl|;
comment|/**      * Returns the<tt>camel-component-schema</tt> json file for the given connector with the Maven coordinate      *      * @param dto  the connector dto      */
DECL|method|componentSchemaJSon (ConnectorDto dto)
name|String
name|componentSchemaJSon
parameter_list|(
name|ConnectorDto
name|dto
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

