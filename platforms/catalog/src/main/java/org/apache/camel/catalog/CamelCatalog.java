begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
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
name|Set
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
comment|/**  * Catalog of components, data formats, models (EIPs), languages, and more  from this Apache Camel release.  */
end_comment

begin_interface
annotation|@
name|MXBean
DECL|interface|CamelCatalog
specifier|public
interface|interface
name|CamelCatalog
block|{
comment|/**      * Enables caching of the resources which makes the catalog faster, but keeps data in memory during caching.      *<p/>      * The catalog does not cache by default.      */
DECL|method|enableCache ()
name|void
name|enableCache
parameter_list|()
function_decl|;
comment|/**      * To plugin a custom {@link SuggestionStrategy} to provide suggestion for unknown options      */
DECL|method|setSuggestion (SuggestionStrategy suggestionStrategy)
name|void
name|setSuggestion
parameter_list|(
name|SuggestionStrategy
name|suggestionStrategy
parameter_list|)
function_decl|;
comment|/**      * The version of this Camel Catalog      */
DECL|method|getCatalogVersion ()
name|String
name|getCatalogVersion
parameter_list|()
function_decl|;
comment|/**      * Find all the component names from the Camel catalog      */
DECL|method|findComponentNames ()
name|List
argument_list|<
name|String
argument_list|>
name|findComponentNames
parameter_list|()
function_decl|;
comment|/**      * Find all the data format names from the Camel catalog      */
DECL|method|findDataFormatNames ()
name|List
argument_list|<
name|String
argument_list|>
name|findDataFormatNames
parameter_list|()
function_decl|;
comment|/**      * Find all the language names from the Camel catalog      */
DECL|method|findLanguageNames ()
name|List
argument_list|<
name|String
argument_list|>
name|findLanguageNames
parameter_list|()
function_decl|;
comment|/**      * Find all the model names from the Camel catalog      */
DECL|method|findModelNames ()
name|List
argument_list|<
name|String
argument_list|>
name|findModelNames
parameter_list|()
function_decl|;
comment|/**      * Find all the component names from the Camel catalog that matches the label      */
DECL|method|findComponentNames (String filter)
name|List
argument_list|<
name|String
argument_list|>
name|findComponentNames
parameter_list|(
name|String
name|filter
parameter_list|)
function_decl|;
comment|/**      * Find all the data format names from the Camel catalog that matches the label      */
DECL|method|findDataFormatNames (String filter)
name|List
argument_list|<
name|String
argument_list|>
name|findDataFormatNames
parameter_list|(
name|String
name|filter
parameter_list|)
function_decl|;
comment|/**      * Find all the language names from the Camel catalog that matches the label      */
DECL|method|findLanguageNames (String filter)
name|List
argument_list|<
name|String
argument_list|>
name|findLanguageNames
parameter_list|(
name|String
name|filter
parameter_list|)
function_decl|;
comment|/**      * Find all the model names from the Camel catalog that matches the label      */
DECL|method|findModelNames (String filter)
name|List
argument_list|<
name|String
argument_list|>
name|findModelNames
parameter_list|(
name|String
name|filter
parameter_list|)
function_decl|;
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
comment|/**      * Find all the unique label names all the components are using.      *      * @return a set of all the labels.      */
DECL|method|findComponentLabels ()
name|Set
argument_list|<
name|String
argument_list|>
name|findComponentLabels
parameter_list|()
function_decl|;
comment|/**      * Find all the unique label names all the data formats are using.      *      * @return a set of all the labels.      */
DECL|method|findDataFormatLabels ()
name|Set
argument_list|<
name|String
argument_list|>
name|findDataFormatLabels
parameter_list|()
function_decl|;
comment|/**      * Find all the unique label names all the data formats are using.      *      * @return a set of all the labels.      */
DECL|method|findLanguageLabels ()
name|Set
argument_list|<
name|String
argument_list|>
name|findLanguageLabels
parameter_list|()
function_decl|;
comment|/**      * Find all the unique label names all the models are using.      *      * @return a set of all the labels.      */
DECL|method|findModelLabels ()
name|Set
argument_list|<
name|String
argument_list|>
name|findModelLabels
parameter_list|()
function_decl|;
comment|/**      * Returns the Apache Camel Maven Archetype catalog in XML format.      *      * @return the catalog in XML      */
DECL|method|archetypeCatalogAsXml ()
name|String
name|archetypeCatalogAsXml
parameter_list|()
function_decl|;
comment|/**      * Returns the Camel Spring XML schema      *      * @return the spring XML schema      */
DECL|method|springSchemaAsXml ()
name|String
name|springSchemaAsXml
parameter_list|()
function_decl|;
comment|/**      * Returns the Camel Blueprint XML schema      *      * @return the blueprint XML schema      */
DECL|method|blueprintSchemaAsXml ()
name|String
name|blueprintSchemaAsXml
parameter_list|()
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
comment|/**      * Parses and validates the endpoint uri and constructs a key/value properties of each option      *      * @param uri  the endpoint uri      * @return validation result      */
DECL|method|validateEndpointProperties (String uri)
name|EndpointValidationResult
name|validateEndpointProperties
parameter_list|(
name|String
name|uri
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
comment|/**      * Creates an endpoint uri in Java style from the information in the json schema      *      * @param scheme the endpoint schema      * @param json the json schema with the endpoint properties      * @return the constructed endpoint uri      * @throws java.net.URISyntaxException is thrown if there is encoding error      */
DECL|method|asEndpointUri (String scheme, String json, boolean encode)
name|String
name|asEndpointUri
parameter_list|(
name|String
name|scheme
parameter_list|,
name|String
name|json
parameter_list|,
name|boolean
name|encode
parameter_list|)
throws|throws
name|URISyntaxException
function_decl|;
comment|/**      * Creates an endpoint uri in XML style (eg escape& as&ampl;) from the information in the json schema      *      * @param scheme the endpoint schema      * @param json the json schema with the endpoint properties      * @return the constructed endpoint uri      * @throws java.net.URISyntaxException is thrown if there is encoding error      */
DECL|method|asEndpointUriXml (String scheme, String json, boolean encode)
name|String
name|asEndpointUriXml
parameter_list|(
name|String
name|scheme
parameter_list|,
name|String
name|json
parameter_list|,
name|boolean
name|encode
parameter_list|)
throws|throws
name|URISyntaxException
function_decl|;
comment|/**      * Creates an endpoint uri in Java style from the information from the properties      *      * @param scheme the endpoint schema      * @param properties the properties as key value pairs      * @return the constructed endpoint uri      * @throws java.net.URISyntaxException is thrown if there is encoding error      */
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
comment|/**      * Creates an endpoint uri in XML style (eg escape& as&ampl;) from the information from the properties      *      * @param scheme the endpoint schema      * @param properties the properties as key value pairs      * @return the constructed endpoint uri      * @throws java.net.URISyntaxException is thrown if there is encoding error      */
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
comment|/**      * Lists all the components summary details in JSon      */
DECL|method|listComponentsAsJson ()
name|String
name|listComponentsAsJson
parameter_list|()
function_decl|;
comment|/**      * Lists all the data formats summary details in JSon      */
DECL|method|listDataFormatsAsJson ()
name|String
name|listDataFormatsAsJson
parameter_list|()
function_decl|;
comment|/**      * Lists all the languages summary details in JSon      */
DECL|method|listLanguagesAsJson ()
name|String
name|listLanguagesAsJson
parameter_list|()
function_decl|;
comment|/**      * Lists all the models (EIPs) summary details in JSon      */
DECL|method|listModelsAsJson ()
name|String
name|listModelsAsJson
parameter_list|()
function_decl|;
comment|/**      * Reports a summary what the catalog contains in JSon      */
DECL|method|summaryAsJson ()
name|String
name|summaryAsJson
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

