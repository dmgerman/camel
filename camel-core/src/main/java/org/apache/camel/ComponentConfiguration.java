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
name|SortedMap
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
name|ParameterConfiguration
import|;
end_import

begin_comment
comment|/**  * Represents a set of configuration values for an endpoint URI which can be created from a URI string  * or a base URI string and a set of parameter names and values.  *  * The configuration values can then be introspected, modified and converted back into a URI string  * or Endpoint.  *  * For @{link UriEndpointComponent} implementations created for Endpoints annotated with {@link org.apache.camel.spi.UriEndpoint} and the  * associated annotations then all the parameter values can be introspected and the parameter values are converted to their  * correct type.  *  * Other implementations keep all the types as String and there is no validation until you try to create  * an Endpoint from the values.  */
end_comment

begin_interface
DECL|interface|ComponentConfiguration
specifier|public
interface|interface
name|ComponentConfiguration
block|{
comment|/**      * Returns the base URI without any query parameters added      */
DECL|method|getBaseUri ()
name|String
name|getBaseUri
parameter_list|()
function_decl|;
comment|/**      * Sets the base URI without any query parameters added      */
DECL|method|setBaseUri (String baseUri)
name|void
name|setBaseUri
parameter_list|(
name|String
name|baseUri
parameter_list|)
function_decl|;
comment|/**      * Returns the current parameters of the configuration (usually encoded as ?foo=bar&whatnot=something URI query parameters)      */
DECL|method|getParameters ()
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParameters
parameter_list|()
function_decl|;
comment|/**      * Sets the parameter values of this configuration      */
DECL|method|setParameters (Map<String, Object> propertyValues)
name|void
name|setParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|propertyValues
parameter_list|)
function_decl|;
comment|/**      * Returns the parameter value for the given name      *      * @param name the name of the URI query parameter to get      * @return the value of the parameter      */
DECL|method|getParameter (String name)
name|Object
name|getParameter
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Sets the parameter value of the given name      *      * @param name  the name of the URI query parameter      * @param value the new value of the parameter      */
DECL|method|setParameter (String name, Object value)
name|void
name|setParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
comment|/**      * Returns the URI string (without schema) with query parameters for the current      * configuration which can then be used to create an {@link org.apache.camel.Endpoint}      */
DECL|method|getUriString ()
name|String
name|getUriString
parameter_list|()
function_decl|;
comment|/**      * Sets the URI string (without schema but with optional query parameters)      * which will update the {@link #getBaseUri()} and the {@link #getParameters()} values      *      * @param newValue the new URI string with query arguments      */
DECL|method|setUriString (String newValue)
name|void
name|setUriString
parameter_list|(
name|String
name|newValue
parameter_list|)
throws|throws
name|URISyntaxException
function_decl|;
comment|/**      * Returns the URI query parameter configuration for the given parameter name or null if it does not exist      */
DECL|method|getParameterConfiguration (String name)
name|ParameterConfiguration
name|getParameterConfiguration
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the sorted map of all the parameter names to their {@link ParameterConfiguration} objects      */
DECL|method|getParameterConfigurationMap ()
name|SortedMap
argument_list|<
name|String
argument_list|,
name|ParameterConfiguration
argument_list|>
name|getParameterConfigurationMap
parameter_list|()
function_decl|;
comment|/**      * Converts the configuration into a URI and then looks up the endpoint in the {@link CamelContext}      * which typically results in a new {@link Endpoint} instance being created.      */
DECL|method|createEndpoint ()
name|Endpoint
name|createEndpoint
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Applies the current set of parameters to the given endpoint instance.      *<p/>      * Note that typically parts of the URI are not injected into the Endpoint; this method purely      *      * @param endpoint      */
DECL|method|configureEndpoint (Endpoint endpoint)
name|void
name|configureEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * Gets the named URI parameter value on the given endpoint      *      * @param endpoint the endpoint instance      * @param name     the name of the URI query parameter      * @return the value of the parameter      * @throws RuntimeCamelException if the parameter name does not exist on the endpoint      */
DECL|method|getEndpointParameter (Endpoint endpoint, String name)
name|Object
name|getEndpointParameter
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|RuntimeCamelException
function_decl|;
comment|/**      * Sets the named URI query parameter value on the given endpoint      *      * @param endpoint the endpoint instance      * @param name     the name of the URI query parameter      * @param value    the new value of the URI query parameter      * @throws RuntimeCamelException      */
DECL|method|setEndpointParameter (Endpoint endpoint, String name, Object value)
name|void
name|setEndpointParameter
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|RuntimeCamelException
function_decl|;
comment|/**      * A helper method for tools such as CLIs, IDEs or web tools that provides a completion list for Endpoint Paths      * rather like bash tab completion or Karaf attribute or option completion handers.      *      * So given the current configuration data, return a list of completions given the specified text.      *      * e.g. return the files in a directory, the matching queues in a message broker, the database tables in a database component etc      *      * @param completionText the prefix text used to complete on (usually a matching bit of text)      *      * @return a list of matches      * @return a list of matches      */
DECL|method|completeEndpointPath (String completionText)
name|List
argument_list|<
name|String
argument_list|>
name|completeEndpointPath
parameter_list|(
name|String
name|completionText
parameter_list|)
function_decl|;
comment|/**      * Creates a<a href="http://json-schema.org/">JSON schema</a> representation of the      * configuration parameters for this endpoint and the types and validation rules.      *      * @return a JSON string which represents the JSON schema for this endpoints configuration parameters.      */
DECL|method|createParameterJsonSchema ()
name|String
name|createParameterJsonSchema
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

