begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.runtimecatalog
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|runtimecatalog
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
name|StaticService
import|;
end_import

begin_comment
comment|/**  * Runtime based CamelCatalog which are included in camel-core that can provided limit CamelCatalog capabilities  */
end_comment

begin_interface
DECL|interface|RuntimeCamelCatalog
specifier|public
interface|interface
name|RuntimeCamelCatalog
extends|extends
name|StaticService
block|{
comment|// configuration
comment|/**      * Gets the {@link JSonSchemaResolver}.      */
DECL|method|getJSonSchemaResolver ()
name|JSonSchemaResolver
name|getJSonSchemaResolver
parameter_list|()
function_decl|;
comment|/**      * To use a custom {@link JSonSchemaResolver}      */
DECL|method|setJSonSchemaResolver (JSonSchemaResolver resolver)
name|void
name|setJSonSchemaResolver
parameter_list|(
name|JSonSchemaResolver
name|resolver
parameter_list|)
function_decl|;
comment|// functions
comment|/**      * Returns the component information as JSon format.      *      * @param name the component name      * @return component details in JSon      */
DECL|method|componentJSonSchema (String name)
name|String
name|componentJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the data format information as JSon format.      *      * @param name the data format name      * @return data format details in JSon      */
DECL|method|dataFormatJSonSchema (String name)
name|String
name|dataFormatJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the language information as JSon format.      *      * @param name the language name      * @return language details in JSon      */
DECL|method|languageJSonSchema (String name)
name|String
name|languageJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the model information as JSon format.      *      * @param name the model name      * @return model details in JSon      */
DECL|method|modelJSonSchema (String name)
name|String
name|modelJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Parses the endpoint uri and constructs a key/value properties of each option      *      * @param uri  the endpoint uri      * @return properties as key value pairs of each endpoint option      */
DECL|method|endpointProperties (String uri)
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|endpointProperties
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|URISyntaxException
function_decl|;
comment|/**      * Parses the endpoint uri and constructs a key/value properties of only the lenient properties (eg custom options)      *<p/>      * For example using the HTTP components to provide query parameters in the endpoint uri.      *      * @param uri  the endpoint uri      * @return properties as key value pairs of each lenient properties      */
DECL|method|endpointLenientProperties (String uri)
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|endpointLenientProperties
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|URISyntaxException
function_decl|;
comment|/**      * Validates the pattern whether its a valid time pattern.      *      * @param pattern  the pattern such as 5000, 5s, 5sec, 4min, 4m30s, 1h, etc.      * @return<tt>true</tt> if valid,<tt>false</tt> if invalid      */
DECL|method|validateTimePattern (String pattern)
name|boolean
name|validateTimePattern
parameter_list|(
name|String
name|pattern
parameter_list|)
function_decl|;
comment|/**      * Validates the properties for the given scheme against component and endpoint      *      * @param scheme  the endpoint scheme      * @param properties  the endpoint properties      * @return validation result      */
DECL|method|validateProperties (String scheme, Map<String, String> properties)
name|EndpointValidationResult
name|validateProperties
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
name|properties
parameter_list|)
function_decl|;
comment|/**      * Parses and validates the endpoint uri and constructs a key/value properties of each option.      *      * @param uri  the endpoint uri      * @return validation result      */
DECL|method|validateEndpointProperties (String uri)
name|EndpointValidationResult
name|validateEndpointProperties
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Parses and validates the endpoint uri and constructs a key/value properties of each option.      *<p/>      * The option ignoreLenientProperties can be used to ignore components that uses lenient properties.      * When this is true, then the uri validation is stricter but would fail on properties that are not part of the component      * but in the uri because of using lenient properties.      * For example using the HTTP components to provide query parameters in the endpoint uri.      *      * @param uri  the endpoint uri      * @param ignoreLenientProperties  whether to ignore components that uses lenient properties.      * @return validation result      */
DECL|method|validateEndpointProperties (String uri, boolean ignoreLenientProperties)
name|EndpointValidationResult
name|validateEndpointProperties
parameter_list|(
name|String
name|uri
parameter_list|,
name|boolean
name|ignoreLenientProperties
parameter_list|)
function_decl|;
comment|/**      * Parses and validates the endpoint uri and constructs a key/value properties of each option.      *<p/>      * The option ignoreLenientProperties can be used to ignore components that uses lenient properties.      * When this is true, then the uri validation is stricter but would fail on properties that are not part of the component      * but in the uri because of using lenient properties.      * For example using the HTTP components to provide query parameters in the endpoint uri.      *      * @param uri  the endpoint uri      * @param ignoreLenientProperties  whether to ignore components that uses lenient properties.      * @param consumerOnly whether the endpoint is only used as a consumer      * @param producerOnly whether the endpoint is only used as a producer      * @return validation result      */
DECL|method|validateEndpointProperties (String uri, boolean ignoreLenientProperties, boolean consumerOnly, boolean producerOnly)
name|EndpointValidationResult
name|validateEndpointProperties
parameter_list|(
name|String
name|uri
parameter_list|,
name|boolean
name|ignoreLenientProperties
parameter_list|,
name|boolean
name|consumerOnly
parameter_list|,
name|boolean
name|producerOnly
parameter_list|)
function_decl|;
comment|/**      * Parses and validates the language as a predicate      *<p/>      *<b>Important:</b> This requires having<tt>camel-core</tt> and the language dependencies on the classpath      *      * @param classLoader a custom classloader to use for loading the language from the classpath, or<tt>null</tt> for using default classloader      * @param language the name of the language      * @param text  the predicate text      * @return validation result      */
DECL|method|validateLanguagePredicate (ClassLoader classLoader, String language, String text)
name|LanguageValidationResult
name|validateLanguagePredicate
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|,
name|String
name|language
parameter_list|,
name|String
name|text
parameter_list|)
function_decl|;
comment|/**      * Parses and validates the language as an expression      *<p/>      *<b>Important:</b> This requires having<tt>camel-core</tt> and the language dependencies on the classpath      *      * @param classLoader a custom classloader to use for loading the language from the classpath, or<tt>null</tt> for using default classloader      * @param language the name of the language      * @param text  the expression text      * @return validation result      */
DECL|method|validateLanguageExpression (ClassLoader classLoader, String language, String text)
name|LanguageValidationResult
name|validateLanguageExpression
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|,
name|String
name|language
parameter_list|,
name|String
name|text
parameter_list|)
function_decl|;
comment|/**      * Returns the component name from the given endpoint uri      *      * @param uri  the endpoint uri      * @return the component name (aka scheme), or<tt>null</tt> if not possible to determine      */
DECL|method|endpointComponentName (String uri)
name|String
name|endpointComponentName
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Creates an endpoint uri in Java style from the information from the properties      *      * @param scheme the endpoint schema      * @param properties the properties as key value pairs      * @param encode whether to URL encode the returned uri or not      * @return the constructed endpoint uri      * @throws java.net.URISyntaxException is thrown if there is encoding error      */
DECL|method|asEndpointUri (String scheme, Map<String, String> properties, boolean encode)
name|String
name|asEndpointUri
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
name|properties
parameter_list|,
name|boolean
name|encode
parameter_list|)
throws|throws
name|URISyntaxException
function_decl|;
comment|/**      * Creates an endpoint uri in XML style (eg escape& as&ampl;) from the information from the properties      *      * @param scheme the endpoint schema      * @param properties the properties as key value pairs      * @param encode whether to URL encode the returned uri or not      * @return the constructed endpoint uri      * @throws java.net.URISyntaxException is thrown if there is encoding error      */
DECL|method|asEndpointUriXml (String scheme, Map<String, String> properties, boolean encode)
name|String
name|asEndpointUriXml
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
name|properties
parameter_list|,
name|boolean
name|encode
parameter_list|)
throws|throws
name|URISyntaxException
function_decl|;
block|}
end_interface

end_unit

