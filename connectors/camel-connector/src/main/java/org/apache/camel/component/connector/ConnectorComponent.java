begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.connector
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|connector
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Component
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
name|CamelCatalog
import|;
end_import

begin_comment
comment|/**  * A component which is based from a Camel Connector.  */
end_comment

begin_interface
DECL|interface|ConnectorComponent
specifier|public
interface|interface
name|ConnectorComponent
extends|extends
name|Component
block|{
comment|/**      * Adds a new option to the existing map of options      *      * @param options  the existing options      * @param name     the name of the option      * @param value    the value of the option      */
DECL|method|addConnectorOption (Map<String, String> options, String name, String value)
name|void
name|addConnectorOption
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|options
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
function_decl|;
comment|/**      * Creates the endpoint uri based on the options from the connector.      *      * @param scheme  the component name      * @param options the options to use for creating the endpoint      * @return the endpoint uri      * @throws URISyntaxException is thrown if error creating the endpoint uri.      */
DECL|method|createEndpointUri (String scheme, Map<String, String> options)
name|String
name|createEndpointUri
parameter_list|(
name|String
name|scheme
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|options
parameter_list|)
throws|throws
name|URISyntaxException
function_decl|;
comment|/**      * Gets the {@link CamelCatalog} which can be used by the connector to help create the component.      */
DECL|method|getCamelCatalog ()
name|CamelCatalog
name|getCamelCatalog
parameter_list|()
function_decl|;
comment|/**      * Gets the connector name (title)      */
DECL|method|getConnectorName ()
name|String
name|getConnectorName
parameter_list|()
function_decl|;
comment|/**      * Gets the connector component name (component scheme)      */
DECL|method|getComponentName ()
name|String
name|getComponentName
parameter_list|()
function_decl|;
comment|/**      * Gets the camel-connector JSon file.      */
DECL|method|getCamelConnectorJSon ()
name|String
name|getCamelConnectorJSon
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

