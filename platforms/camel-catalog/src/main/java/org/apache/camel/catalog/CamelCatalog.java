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
comment|/**  * Catalog of components, data formats, models (EIPs), languages, and more from this Apache Camel release.  */
end_comment

begin_interface
annotation|@
name|MXBean
DECL|interface|CamelCatalog
specifier|public
interface|interface
name|CamelCatalog
block|{
comment|/**      * Returns the {@link JSonSchemaResolver} used by this catalog.      *      * @return the resolver      */
DECL|method|getJSonSchemaResolver ()
name|JSonSchemaResolver
name|getJSonSchemaResolver
parameter_list|()
function_decl|;
comment|/**      * To use a custom {@link JSonSchemaResolver} with this catalog.      *      * @param resolver  the custom resolver      */
DECL|method|setJSonSchemaResolver (JSonSchemaResolver resolver)
name|void
name|setJSonSchemaResolver
parameter_list|(
name|JSonSchemaResolver
name|resolver
parameter_list|)
function_decl|;
comment|/**      * To plugin a custom {@link RuntimeProvider} that amends the catalog to only include information that is supported on the runtime.      */
DECL|method|setRuntimeProvider (RuntimeProvider provider)
name|void
name|setRuntimeProvider
parameter_list|(
name|RuntimeProvider
name|provider
parameter_list|)
function_decl|;
comment|/**      * Gets the {@link RuntimeProvider} in use.      */
DECL|method|getRuntimeProvider ()
name|RuntimeProvider
name|getRuntimeProvider
parameter_list|()
function_decl|;
comment|/**      * Enables caching of the resources which makes the catalog faster, but keeps data in memory during caching.      *<p/>      * The catalog does not cache by default.      */
DECL|method|enableCache ()
name|void
name|enableCache
parameter_list|()
function_decl|;
comment|/**      * Whether caching has been enabled.      */
DECL|method|isCaching ()
name|boolean
name|isCaching
parameter_list|()
function_decl|;
comment|/**      * To plugin a custom {@link SuggestionStrategy} to provide suggestion for unknown options      */
DECL|method|setSuggestionStrategy (SuggestionStrategy suggestionStrategy)
name|void
name|setSuggestionStrategy
parameter_list|(
name|SuggestionStrategy
name|suggestionStrategy
parameter_list|)
function_decl|;
comment|/**      * Gets the {@link SuggestionStrategy} in use      */
DECL|method|getSuggestionStrategy ()
name|SuggestionStrategy
name|getSuggestionStrategy
parameter_list|()
function_decl|;
comment|/**      * To plugin a custom {@link VersionManager} to load other versions of Camel the catalog should use.      */
DECL|method|setVersionManager (VersionManager versionManager)
name|void
name|setVersionManager
parameter_list|(
name|VersionManager
name|versionManager
parameter_list|)
function_decl|;
comment|/**      * Gets the {@link VersionManager} in use      */
DECL|method|getVersionManager ()
name|VersionManager
name|getVersionManager
parameter_list|()
function_decl|;
comment|/**      * Adds a 3rd party component to this catalog.      *      * @param name      the component name      * @param className the fully qualified class name for the component class      */
DECL|method|addComponent (String name, String className)
name|void
name|addComponent
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|className
parameter_list|)
function_decl|;
comment|/**      * Adds a 3rd party component to this catalog.      *      * @param name       the component name      * @param className  the fully qualified class name for the component class      * @param jsonSchema the component JSon schema      */
DECL|method|addComponent (String name, String className, String jsonSchema)
name|void
name|addComponent
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|className
parameter_list|,
name|String
name|jsonSchema
parameter_list|)
function_decl|;
comment|/**      * Adds a 3rd party data format to this catalog.      *      * @param name      the data format name      * @param className the fully qualified class name for the data format class      */
DECL|method|addDataFormat (String name, String className)
name|void
name|addDataFormat
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|className
parameter_list|)
function_decl|;
comment|/**      * Adds a 3rd party data format to this catalog.      *      * @param name      the data format name      * @param className the fully qualified class name for the data format class      * @param jsonSchema the data format JSon schema      */
DECL|method|addDataFormat (String name, String className, String jsonSchema)
name|void
name|addDataFormat
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|className
parameter_list|,
name|String
name|jsonSchema
parameter_list|)
function_decl|;
comment|/**      * The version of this Camel Catalog      */
DECL|method|getCatalogVersion ()
name|String
name|getCatalogVersion
parameter_list|()
function_decl|;
comment|/**      * Attempt to load the Camel version to be used by the catalog.      *<p/>      * Loading the camel-catalog JAR of the given version of choice may require internet access      * to download the JAR from Maven central. You can pre download the JAR and install in a local      * Maven repository to avoid internet access for offline environments.      *<p/>      * When loading a new version the cache will be invalidated.      *<p/>      *<b>Important:</b> When loading a new runtime provider version, then its strongly advised to      * load the same/corresponding version first using {@link #loadVersion(String)}.      *      * @param version  the Camel version such as<tt>2.17.1</tt>      * @return<tt>true</tt> if the version was loaded,<tt>false</tt> if not.      */
DECL|method|loadVersion (String version)
name|boolean
name|loadVersion
parameter_list|(
name|String
name|version
parameter_list|)
function_decl|;
comment|/**      * Gets the current loaded Camel version used by the catalog.      */
DECL|method|getLoadedVersion ()
name|String
name|getLoadedVersion
parameter_list|()
function_decl|;
comment|/**      * Gets the current loaded runtime provider version used by the catalog.      */
DECL|method|getRuntimeProviderLoadedVersion ()
name|String
name|getRuntimeProviderLoadedVersion
parameter_list|()
function_decl|;
comment|/**      * Attempt to load the runtime provider version to be used by the catalog.      *<p/>      * Loading the runtime provider JAR of the given version of choice may require internet access      * to download the JAR from Maven central. You can pre download the JAR and install in a local      * Maven repository to avoid internet access for offline environments.      *<p/>      *<b>Important:</b> When loading a new runtime provider version, then its strongly advised to      * load the same/corresponding version first using {@link #loadVersion(String)}.      *      * @param groupId  the runtime provider Maven groupId      * @param artifactId  the runtime provider Maven artifactId      * @param version  the runtime provider Maven version      * @return<tt>true</tt> if the version was loaded,<tt>false</tt> if not.      */
DECL|method|loadRuntimeProviderVersion (String groupId, String artifactId, String version)
name|boolean
name|loadRuntimeProviderVersion
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
comment|/**      * Find all the other (miscellaneous) names from the Camel catalog      */
DECL|method|findOtherNames ()
name|List
argument_list|<
name|String
argument_list|>
name|findOtherNames
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
comment|/**      * Find all the other (miscellaneous) names from the Camel catalog that matches the label      */
DECL|method|findOtherNames (String filter)
name|List
argument_list|<
name|String
argument_list|>
name|findOtherNames
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
comment|/**      * Returns the other (miscellaneous) information as JSon format.      *      * @param name the other (miscellaneous) name      * @return other (miscellaneous) details in JSon      */
DECL|method|otherJSonSchema (String name)
name|String
name|otherJSonSchema
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
comment|/**      * Returns the component documentation as Ascii doc format.      *      * @param name the component name      * @return component documentation in ascii doc format.      */
DECL|method|componentAsciiDoc (String name)
name|String
name|componentAsciiDoc
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the component documentation as HTML format.      *      * @param name the component name      * @return component documentation in html format.      */
DECL|method|componentHtmlDoc (String name)
name|String
name|componentHtmlDoc
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the data format documentation as Ascii doc format.      *      * @param name the data format name      * @return data format documentation in ascii doc format.      */
DECL|method|dataFormatAsciiDoc (String name)
name|String
name|dataFormatAsciiDoc
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the data format documentation as HTML format.      *      * @param name the data format name      * @return data format documentation in HTML format.      */
DECL|method|dataFormatHtmlDoc (String name)
name|String
name|dataFormatHtmlDoc
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the language documentation as Ascii doc format.      *      * @param name the language name      * @return language documentation in ascii doc format.      */
DECL|method|languageAsciiDoc (String name)
name|String
name|languageAsciiDoc
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the language documentation as HTML format.      *      * @param name the language name      * @return language documentation in HTML format.      */
DECL|method|languageHtmlDoc (String name)
name|String
name|languageHtmlDoc
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the other (miscellaneous) documentation as Ascii doc format.      *      * @param name the other (miscellaneous) name      * @return other (miscellaneous) documentation in ascii doc format.      */
DECL|method|otherAsciiDoc (String name)
name|String
name|otherAsciiDoc
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the other (miscellaneous) documentation as HTML format.      *      * @param name the other (miscellaneous) name      * @return other (miscellaneous) documentation in HTML format.      */
DECL|method|otherHtmlDoc (String name)
name|String
name|otherHtmlDoc
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
comment|/**      * Find all the unique label names all the languages are using.      *      * @return a set of all the labels.      */
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
comment|/**      * Find all the unique label names all the other (miscellaneous) are using.      *      * @return a set of all the labels.      */
DECL|method|findOtherLabels ()
name|Set
argument_list|<
name|String
argument_list|>
name|findOtherLabels
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
comment|/**      * Creates an endpoint uri in XML style from the information from the properties      *      * @param scheme the endpoint schema      * @param properties the properties as key value pairs      * @param encode whether to URL encode the returned uri or not      * @return the constructed endpoint uri      * @throws java.net.URISyntaxException is thrown if there is encoding error      */
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
comment|/**      * Lists all the others (miscellaneous) summary details in JSon      */
DECL|method|listOthersAsJson ()
name|String
name|listOthersAsJson
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

