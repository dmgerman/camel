begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|Properties
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
name|DataFormat
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
name|Language
import|;
end_import

begin_comment
comment|/**  * Catalog level interface for the {@link CamelContext}  */
end_comment

begin_interface
DECL|interface|CatalogCamelContext
specifier|public
interface|interface
name|CatalogCamelContext
extends|extends
name|CamelContext
block|{
comment|/**      * Resolves a component's default name from its java type.      *<p/>      * A component may be used with a non default name such as<tt>activemq</tt>,<tt>wmq</tt> for the JMS component.      * This method can resolve the default component name by its java type.      *      * @param javaType the FQN name of the java type      * @return the default component name.      */
DECL|method|resolveComponentDefaultName (String javaType)
name|String
name|resolveComponentDefaultName
parameter_list|(
name|String
name|javaType
parameter_list|)
function_decl|;
comment|/**      * Find information about all the Camel components available in the classpath and {@link org.apache.camel.spi.Registry}.      *      * @return a map with the component name, and value with component details.      * @throws LoadPropertiesException is thrown if error during classpath discovery of the components      * @throws IOException is thrown if error during classpath discovery of the components      */
DECL|method|findComponents ()
name|Map
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|findComponents
parameter_list|()
throws|throws
name|LoadPropertiesException
throws|,
name|IOException
function_decl|;
comment|/**      * Find information about all the EIPs from camel-core.      *      * @return a map with node id, and value with EIP details.      * @throws LoadPropertiesException is thrown if error during classpath discovery of the EIPs      * @throws IOException is thrown if error during classpath discovery of the EIPs      */
DECL|method|findEips ()
name|Map
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|findEips
parameter_list|()
throws|throws
name|LoadPropertiesException
throws|,
name|IOException
function_decl|;
comment|/**      * Returns the JSON schema representation of the component and endpoint parameters for the given component name.      *      * @return the json or<tt>null</tt> if the component is<b>not</b> built with JSon schema support      */
DECL|method|getComponentParameterJsonSchema (String componentName)
name|String
name|getComponentParameterJsonSchema
parameter_list|(
name|String
name|componentName
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the JSON schema representation of the {@link DataFormat} parameters for the given data format name.      *      * @return the json or<tt>null</tt> if the data format does not exist      */
DECL|method|getDataFormatParameterJsonSchema (String dataFormatName)
name|String
name|getDataFormatParameterJsonSchema
parameter_list|(
name|String
name|dataFormatName
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the JSON schema representation of the {@link Language} parameters for the given language name.      *      * @return the json or<tt>null</tt> if the language does not exist      */
DECL|method|getLanguageParameterJsonSchema (String languageName)
name|String
name|getLanguageParameterJsonSchema
parameter_list|(
name|String
name|languageName
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the JSON schema representation of the EIP parameters for the given EIP name.      *      * @return the json or<tt>null</tt> if the EIP does not exist      */
DECL|method|getEipParameterJsonSchema (String eipName)
name|String
name|getEipParameterJsonSchema
parameter_list|(
name|String
name|eipName
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns a JSON schema representation of the EIP parameters for the given EIP by its id.      *      * @param nameOrId the name of the EIP ({@link NamedNode#getShortName()} or a node id to refer to a specific node from the routes.      * @param includeAllOptions whether to include non configured options also (eg default options)      * @return the json or<tt>null</tt> if the eipName or the id was not found      */
DECL|method|explainEipJson (String nameOrId, boolean includeAllOptions)
name|String
name|explainEipJson
parameter_list|(
name|String
name|nameOrId
parameter_list|,
name|boolean
name|includeAllOptions
parameter_list|)
function_decl|;
comment|/**      * Returns a JSON schema representation of the component parameters (not endpoint parameters) for the given component by its id.      *      * @param componentName the name of the component.      * @param includeAllOptions whether to include non configured options also (eg default options)      * @return the json or<tt>null</tt> if the component was not found      */
DECL|method|explainComponentJson (String componentName, boolean includeAllOptions)
name|String
name|explainComponentJson
parameter_list|(
name|String
name|componentName
parameter_list|,
name|boolean
name|includeAllOptions
parameter_list|)
function_decl|;
comment|/**      * Returns a JSON schema representation of the component parameters (not endpoint parameters) for the given component by its id.      *      * @param dataFormat the data format instance.      * @param includeAllOptions whether to include non configured options also (eg default options)      * @return the json      */
DECL|method|explainDataFormatJson (String dataFormatName, DataFormat dataFormat, boolean includeAllOptions)
name|String
name|explainDataFormatJson
parameter_list|(
name|String
name|dataFormatName
parameter_list|,
name|DataFormat
name|dataFormat
parameter_list|,
name|boolean
name|includeAllOptions
parameter_list|)
function_decl|;
comment|/**      * Returns a JSON schema representation of the endpoint parameters for the given endpoint uri.      *      * @param uri the endpoint uri      * @param includeAllOptions whether to include non configured options also (eg default options)      * @return the json or<tt>null</tt> if uri parameters is invalid, or the component is<b>not</b> built with JSon schema support      */
DECL|method|explainEndpointJson (String uri, boolean includeAllOptions)
name|String
name|explainEndpointJson
parameter_list|(
name|String
name|uri
parameter_list|,
name|boolean
name|includeAllOptions
parameter_list|)
function_decl|;
comment|/**      * Creates a JSON representation of all the<b>static</b> and<b>dynamic</b> configured endpoints defined in the given route(s).      *      * @param routeId for a particular route, or<tt>null</tt> for all routes      * @return a JSON string      */
DECL|method|createRouteStaticEndpointJson (String routeId)
name|String
name|createRouteStaticEndpointJson
parameter_list|(
name|String
name|routeId
parameter_list|)
function_decl|;
comment|/**      * Creates a JSON representation of all the<b>static</b> (and possible<b>dynamic</b>) configured endpoints defined in the given route(s).      *      * @param routeId for a particular route, or<tt>null</tt> for all routes      * @param includeDynamic whether to include dynamic endpoints      * @return a JSON string      */
DECL|method|createRouteStaticEndpointJson (String routeId, boolean includeDynamic)
name|String
name|createRouteStaticEndpointJson
parameter_list|(
name|String
name|routeId
parameter_list|,
name|boolean
name|includeDynamic
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

