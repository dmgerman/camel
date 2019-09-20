begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xslt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xslt
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
name|HashMap
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|ErrorListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|URIResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|EntityResolver
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
name|CamelContext
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
name|Exchange
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|ClassResolver
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
name|Injector
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
name|Metadata
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
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
name|support
operator|.
name|EndpointHelper
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
name|support
operator|.
name|ProcessorEndpoint
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
name|support
operator|.
name|service
operator|.
name|ServiceHelper
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Transforms the message using a XSLT template.  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed XsltEndpoint"
argument_list|)
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"1.3.0"
argument_list|,
name|scheme
operator|=
literal|"xslt"
argument_list|,
name|title
operator|=
literal|"XSLT"
argument_list|,
name|syntax
operator|=
literal|"xslt:resourceUri"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"core,transformation"
argument_list|)
DECL|class|XsltEndpoint
specifier|public
class|class
name|XsltEndpoint
extends|extends
name|ProcessorEndpoint
block|{
DECL|field|SAXON_TRANSFORMER_FACTORY_CLASS_NAME
specifier|public
specifier|static
specifier|final
name|String
name|SAXON_TRANSFORMER_FACTORY_CLASS_NAME
init|=
literal|"net.sf.saxon.TransformerFactoryImpl"
decl_stmt|;
DECL|field|cacheCleared
specifier|private
specifier|volatile
name|boolean
name|cacheCleared
decl_stmt|;
DECL|field|xslt
specifier|private
specifier|volatile
name|XsltBuilder
name|xslt
decl_stmt|;
DECL|field|parameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|resourceUri
specifier|private
name|String
name|resourceUri
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|contentCache
specifier|private
name|boolean
name|contentCache
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|transformerFactoryClass
specifier|private
name|String
name|transformerFactoryClass
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|transformerFactory
specifier|private
name|TransformerFactory
name|transformerFactory
decl_stmt|;
annotation|@
name|UriParam
DECL|field|saxon
specifier|private
name|boolean
name|saxon
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|saxonConfiguration
specifier|private
name|Object
name|saxonConfiguration
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|saxonConfigurationProperties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|saxonConfigurationProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|javaType
operator|=
literal|"java.lang.String"
argument_list|)
DECL|field|saxonExtensionFunctions
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|saxonExtensionFunctions
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|resultHandlerFactory
specifier|private
name|ResultHandlerFactory
name|resultHandlerFactory
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|failOnNullBody
specifier|private
name|boolean
name|failOnNullBody
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"string"
argument_list|)
DECL|field|output
specifier|private
name|XsltOutput
name|output
init|=
name|XsltOutput
operator|.
name|string
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"0"
argument_list|)
DECL|field|transformerCacheSize
specifier|private
name|int
name|transformerCacheSize
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|errorListener
specifier|private
name|ErrorListener
name|errorListener
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|uriResolver
specifier|private
name|URIResolver
name|uriResolver
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|displayName
operator|=
literal|"Allow StAX"
argument_list|)
DECL|field|allowStAX
specifier|private
name|boolean
name|allowStAX
decl_stmt|;
annotation|@
name|UriParam
DECL|field|deleteOutputFile
specifier|private
name|boolean
name|deleteOutputFile
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|entityResolver
specifier|private
name|EntityResolver
name|entityResolver
decl_stmt|;
DECL|method|XsltEndpoint (String endpointUri, Component component)
specifier|public
name|XsltEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Clears the cached XSLT stylesheet, forcing to re-load the stylesheet on next request"
argument_list|)
DECL|method|clearCachedStylesheet ()
specifier|public
name|void
name|clearCachedStylesheet
parameter_list|()
block|{
name|this
operator|.
name|cacheCleared
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether the XSLT stylesheet is cached"
argument_list|)
DECL|method|isCacheStylesheet ()
specifier|public
name|boolean
name|isCacheStylesheet
parameter_list|()
block|{
return|return
name|contentCache
return|;
block|}
DECL|method|findOrCreateEndpoint (String uri, String newResourceUri)
specifier|public
name|XsltEndpoint
name|findOrCreateEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|newResourceUri
parameter_list|)
block|{
name|String
name|newUri
init|=
name|uri
operator|.
name|replace
argument_list|(
name|resourceUri
argument_list|,
name|newResourceUri
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Getting endpoint with URI: {}"
argument_list|,
name|newUri
argument_list|)
expr_stmt|;
return|return
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|newUri
argument_list|,
name|XsltEndpoint
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|onExchange (Exchange exchange)
specifier|protected
name|void
name|onExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|contentCache
operator|||
name|cacheCleared
condition|)
block|{
name|loadResource
argument_list|(
name|resourceUri
argument_list|,
name|xslt
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|onExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|isCacheCleared ()
specifier|public
name|boolean
name|isCacheCleared
parameter_list|()
block|{
return|return
name|cacheCleared
return|;
block|}
DECL|method|setCacheCleared (boolean cacheCleared)
specifier|public
name|void
name|setCacheCleared
parameter_list|(
name|boolean
name|cacheCleared
parameter_list|)
block|{
name|this
operator|.
name|cacheCleared
operator|=
name|cacheCleared
expr_stmt|;
block|}
DECL|method|getXslt ()
specifier|public
name|XsltBuilder
name|getXslt
parameter_list|()
block|{
return|return
name|xslt
return|;
block|}
DECL|method|setXslt (XsltBuilder xslt)
specifier|public
name|void
name|setXslt
parameter_list|(
name|XsltBuilder
name|xslt
parameter_list|)
block|{
name|this
operator|.
name|xslt
operator|=
name|xslt
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Path to the template"
argument_list|)
DECL|method|getResourceUri ()
specifier|public
name|String
name|getResourceUri
parameter_list|()
block|{
return|return
name|resourceUri
return|;
block|}
comment|/**      * Path to the template.      *<p/>      * The following is supported by the default URIResolver.      * You can prefix with: classpath, file, http, ref, or bean.      * classpath, file and http loads the resource using these protocols (classpath is default).      * ref will lookup the resource in the registry.      * bean will call a method on a bean to be used as the resource.      * For bean you can specify the method name after dot, eg bean:myBean.myMethod      *      * @param resourceUri  the resource path      */
DECL|method|setResourceUri (String resourceUri)
specifier|public
name|void
name|setResourceUri
parameter_list|(
name|String
name|resourceUri
parameter_list|)
block|{
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
block|}
DECL|method|getTransformerFactoryClass ()
specifier|public
name|String
name|getTransformerFactoryClass
parameter_list|()
block|{
return|return
name|transformerFactoryClass
return|;
block|}
comment|/**      * To use a custom XSLT transformer factory, specified as a FQN class name      */
DECL|method|setTransformerFactoryClass (String transformerFactoryClass)
specifier|public
name|void
name|setTransformerFactoryClass
parameter_list|(
name|String
name|transformerFactoryClass
parameter_list|)
block|{
name|this
operator|.
name|transformerFactoryClass
operator|=
name|transformerFactoryClass
expr_stmt|;
block|}
DECL|method|getTransformerFactory ()
specifier|public
name|TransformerFactory
name|getTransformerFactory
parameter_list|()
block|{
return|return
name|transformerFactory
return|;
block|}
comment|/**      * To use a custom XSLT transformer factory      */
DECL|method|setTransformerFactory (TransformerFactory transformerFactory)
specifier|public
name|void
name|setTransformerFactory
parameter_list|(
name|TransformerFactory
name|transformerFactory
parameter_list|)
block|{
name|this
operator|.
name|transformerFactory
operator|=
name|transformerFactory
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to use Saxon as the transformerFactoryClass"
argument_list|)
DECL|method|isSaxon ()
specifier|public
name|boolean
name|isSaxon
parameter_list|()
block|{
return|return
name|saxon
return|;
block|}
comment|/**      * Whether to use Saxon as the transformerFactoryClass.      * If enabled then the class net.sf.saxon.TransformerFactoryImpl. You would need to add Saxon to the classpath.      */
DECL|method|setSaxon (boolean saxon)
specifier|public
name|void
name|setSaxon
parameter_list|(
name|boolean
name|saxon
parameter_list|)
block|{
name|this
operator|.
name|saxon
operator|=
name|saxon
expr_stmt|;
block|}
DECL|method|getSaxonExtensionFunctions ()
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getSaxonExtensionFunctions
parameter_list|()
block|{
return|return
name|saxonExtensionFunctions
return|;
block|}
comment|/**      * Allows you to use a custom net.sf.saxon.lib.ExtensionFunctionDefinition.      * You would need to add camel-saxon to the classpath.      * The function is looked up in the registry, where you can comma to separate multiple values to lookup.      */
DECL|method|setSaxonExtensionFunctions (List<Object> extensionFunctions)
specifier|public
name|void
name|setSaxonExtensionFunctions
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|extensionFunctions
parameter_list|)
block|{
name|this
operator|.
name|saxonExtensionFunctions
operator|=
name|extensionFunctions
expr_stmt|;
block|}
comment|/**      * Allows you to use a custom net.sf.saxon.lib.ExtensionFunctionDefinition.      * You would need to add camel-saxon to the classpath.      * The function is looked up in the registry, where you can comma to separate multiple values to lookup.      */
DECL|method|setSaxonExtensionFunctions (String extensionFunctions)
specifier|public
name|void
name|setSaxonExtensionFunctions
parameter_list|(
name|String
name|extensionFunctions
parameter_list|)
block|{
name|this
operator|.
name|saxonExtensionFunctions
operator|=
name|EndpointHelper
operator|.
name|resolveReferenceListParameter
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|extensionFunctions
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|getSaxonConfiguration ()
specifier|public
name|Object
name|getSaxonConfiguration
parameter_list|()
block|{
return|return
name|saxonConfiguration
return|;
block|}
comment|/**      * To use a custom Saxon configuration      */
DECL|method|setSaxonConfiguration (Object saxonConfiguration)
specifier|public
name|void
name|setSaxonConfiguration
parameter_list|(
name|Object
name|saxonConfiguration
parameter_list|)
block|{
name|this
operator|.
name|saxonConfiguration
operator|=
name|saxonConfiguration
expr_stmt|;
block|}
DECL|method|getSaxonConfigurationProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getSaxonConfigurationProperties
parameter_list|()
block|{
return|return
name|saxonConfigurationProperties
return|;
block|}
comment|/**      * To set custom Saxon configuration properties      */
DECL|method|setSaxonConfigurationProperties (Map<String, Object> configurationProperties)
specifier|public
name|void
name|setSaxonConfigurationProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|configurationProperties
parameter_list|)
block|{
name|this
operator|.
name|saxonConfigurationProperties
operator|=
name|configurationProperties
expr_stmt|;
block|}
DECL|method|getResultHandlerFactory ()
specifier|public
name|ResultHandlerFactory
name|getResultHandlerFactory
parameter_list|()
block|{
return|return
name|resultHandlerFactory
return|;
block|}
comment|/**      * Allows you to use a custom org.apache.camel.builder.xml.ResultHandlerFactory which is capable of      * using custom org.apache.camel.builder.xml.ResultHandler types.      */
DECL|method|setResultHandlerFactory (ResultHandlerFactory resultHandlerFactory)
specifier|public
name|void
name|setResultHandlerFactory
parameter_list|(
name|ResultHandlerFactory
name|resultHandlerFactory
parameter_list|)
block|{
name|this
operator|.
name|resultHandlerFactory
operator|=
name|resultHandlerFactory
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether or not to throw an exception if the input body is null"
argument_list|)
DECL|method|isFailOnNullBody ()
specifier|public
name|boolean
name|isFailOnNullBody
parameter_list|()
block|{
return|return
name|failOnNullBody
return|;
block|}
comment|/**      * Whether or not to throw an exception if the input body is null.      */
DECL|method|setFailOnNullBody (boolean failOnNullBody)
specifier|public
name|void
name|setFailOnNullBody
parameter_list|(
name|boolean
name|failOnNullBody
parameter_list|)
block|{
name|this
operator|.
name|failOnNullBody
operator|=
name|failOnNullBody
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"What kind of option to use."
argument_list|)
DECL|method|getOutput ()
specifier|public
name|XsltOutput
name|getOutput
parameter_list|()
block|{
return|return
name|output
return|;
block|}
comment|/**      * Option to specify which output type to use.      * Possible values are: string, bytes, DOM, file. The first three options are all in memory based, where as file is streamed directly to a java.io.File.      * For file you must specify the filename in the IN header with the key Exchange.XSLT_FILE_NAME which is also CamelXsltFileName.      * Also any paths leading to the filename must be created beforehand, otherwise an exception is thrown at runtime.      */
DECL|method|setOutput (XsltOutput output)
specifier|public
name|void
name|setOutput
parameter_list|(
name|XsltOutput
name|output
parameter_list|)
block|{
name|this
operator|.
name|output
operator|=
name|output
expr_stmt|;
block|}
DECL|method|getTransformerCacheSize ()
specifier|public
name|int
name|getTransformerCacheSize
parameter_list|()
block|{
return|return
name|transformerCacheSize
return|;
block|}
comment|/**      * The number of javax.xml.transform.Transformer object that are cached for reuse to avoid calls to Template.newTransformer().      */
DECL|method|setTransformerCacheSize (int transformerCacheSize)
specifier|public
name|void
name|setTransformerCacheSize
parameter_list|(
name|int
name|transformerCacheSize
parameter_list|)
block|{
name|this
operator|.
name|transformerCacheSize
operator|=
name|transformerCacheSize
expr_stmt|;
block|}
DECL|method|getErrorListener ()
specifier|public
name|ErrorListener
name|getErrorListener
parameter_list|()
block|{
return|return
name|errorListener
return|;
block|}
comment|/**      *  Allows to configure to use a custom javax.xml.transform.ErrorListener. Beware when doing this then the default error      *  listener which captures any errors or fatal errors and store information on the Exchange as properties is not in use.      *  So only use this option for special use-cases.      */
DECL|method|setErrorListener (ErrorListener errorListener)
specifier|public
name|void
name|setErrorListener
parameter_list|(
name|ErrorListener
name|errorListener
parameter_list|)
block|{
name|this
operator|.
name|errorListener
operator|=
name|errorListener
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Cache for the resource content (the stylesheet file) when it is loaded."
argument_list|)
DECL|method|isContentCache ()
specifier|public
name|boolean
name|isContentCache
parameter_list|()
block|{
return|return
name|contentCache
return|;
block|}
comment|/**      * Cache for the resource content (the stylesheet file) when it is loaded.      * If set to false Camel will reload the stylesheet file on each message processing. This is good for development.      * A cached stylesheet can be forced to reload at runtime via JMX using the clearCachedStylesheet operation.      */
DECL|method|setContentCache (boolean contentCache)
specifier|public
name|void
name|setContentCache
parameter_list|(
name|boolean
name|contentCache
parameter_list|)
block|{
name|this
operator|.
name|contentCache
operator|=
name|contentCache
expr_stmt|;
block|}
DECL|method|getUriResolver ()
specifier|public
name|URIResolver
name|getUriResolver
parameter_list|()
block|{
return|return
name|uriResolver
return|;
block|}
comment|/**      * To use a custom javax.xml.transform.URIResolver      */
DECL|method|setUriResolver (URIResolver uriResolver)
specifier|public
name|void
name|setUriResolver
parameter_list|(
name|URIResolver
name|uriResolver
parameter_list|)
block|{
name|this
operator|.
name|uriResolver
operator|=
name|uriResolver
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to allow using StAX as the javax.xml.transform.Source"
argument_list|)
DECL|method|isAllowStAX ()
specifier|public
name|boolean
name|isAllowStAX
parameter_list|()
block|{
return|return
name|allowStAX
return|;
block|}
comment|/**      * Whether to allow using StAX as the javax.xml.transform.Source.      * You can enable this if the XSLT library supports StAX such as the Saxon library (camel-saxon).      * The Xalan library (default in JVM) does not support StAXSource.      */
DECL|method|setAllowStAX (boolean allowStAX)
specifier|public
name|void
name|setAllowStAX
parameter_list|(
name|boolean
name|allowStAX
parameter_list|)
block|{
name|this
operator|.
name|allowStAX
operator|=
name|allowStAX
expr_stmt|;
block|}
DECL|method|isDeleteOutputFile ()
specifier|public
name|boolean
name|isDeleteOutputFile
parameter_list|()
block|{
return|return
name|deleteOutputFile
return|;
block|}
comment|/**      * If you have output=file then this option dictates whether or not the output file should be deleted when the Exchange      * is done processing. For example suppose the output file is a temporary file, then it can be a good idea to delete it after use.      */
DECL|method|setDeleteOutputFile (boolean deleteOutputFile)
specifier|public
name|void
name|setDeleteOutputFile
parameter_list|(
name|boolean
name|deleteOutputFile
parameter_list|)
block|{
name|this
operator|.
name|deleteOutputFile
operator|=
name|deleteOutputFile
expr_stmt|;
block|}
DECL|method|getEntityResolver ()
specifier|public
name|EntityResolver
name|getEntityResolver
parameter_list|()
block|{
return|return
name|entityResolver
return|;
block|}
comment|/**      * To use a custom org.xml.sax.EntityResolver with javax.xml.transform.sax.SAXSource.      */
DECL|method|setEntityResolver (EntityResolver entityResolver)
specifier|public
name|void
name|setEntityResolver
parameter_list|(
name|EntityResolver
name|entityResolver
parameter_list|)
block|{
name|this
operator|.
name|entityResolver
operator|=
name|entityResolver
expr_stmt|;
block|}
DECL|method|getParameters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
comment|/**      * Additional parameters to configure on the javax.xml.transform.Transformer.      */
DECL|method|setParameters (Map<String, Object> parameters)
specifier|public
name|void
name|setParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
block|}
comment|/**      * Loads the resource.      *      * @param resourceUri  the resource to load      * @throws TransformerException is thrown if error loading resource      * @throws IOException is thrown if error loading resource      */
DECL|method|loadResource (String resourceUri, XsltBuilder xslt)
specifier|protected
name|void
name|loadResource
parameter_list|(
name|String
name|resourceUri
parameter_list|,
name|XsltBuilder
name|xslt
parameter_list|)
throws|throws
name|TransformerException
throws|,
name|IOException
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"{} loading schema resource: {}"
argument_list|,
name|this
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
name|Source
name|source
init|=
name|xslt
operator|.
name|getUriResolver
argument_list|()
operator|.
name|resolve
argument_list|(
name|resourceUri
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot load schema resource "
operator|+
name|resourceUri
argument_list|)
throw|;
block|}
else|else
block|{
name|xslt
operator|.
name|setTransformerSource
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
comment|// now loaded so clear flag
name|cacheCleared
operator|=
literal|false
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
comment|// the processor is the xslt builder
name|setXslt
argument_list|(
name|createXsltBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|setProcessor
argument_list|(
name|getXslt
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createXsltBuilder ()
specifier|protected
name|XsltBuilder
name|createXsltBuilder
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|ctx
init|=
name|getCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|ClassResolver
name|resolver
init|=
name|ctx
operator|.
name|getClassResolver
argument_list|()
decl_stmt|;
specifier|final
name|Injector
name|injector
init|=
name|ctx
operator|.
name|getInjector
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"{} using schema resource: {}"
argument_list|,
name|this
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
specifier|final
name|XsltBuilder
name|xslt
init|=
name|injector
operator|.
name|newInstance
argument_list|(
name|XsltBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|useSaxon
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|transformerFactoryClass
operator|==
literal|null
operator|&&
operator|(
name|saxon
operator|||
name|saxonExtensionFunctions
operator|!=
literal|null
operator|)
condition|)
block|{
name|useSaxon
operator|=
literal|true
expr_stmt|;
name|transformerFactoryClass
operator|=
name|SAXON_TRANSFORMER_FACTORY_CLASS_NAME
expr_stmt|;
block|}
name|TransformerFactory
name|factory
init|=
name|transformerFactory
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
operator|&&
name|transformerFactoryClass
operator|!=
literal|null
condition|)
block|{
comment|// provide the class loader of this component to work in OSGi environments
name|Class
argument_list|<
name|TransformerFactory
argument_list|>
name|factoryClass
init|=
name|resolver
operator|.
name|resolveMandatoryClass
argument_list|(
name|transformerFactoryClass
argument_list|,
name|TransformerFactory
operator|.
name|class
argument_list|,
name|XsltComponent
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Using TransformerFactoryClass {}"
argument_list|,
name|factoryClass
argument_list|)
expr_stmt|;
name|factory
operator|=
name|injector
operator|.
name|newInstance
argument_list|(
name|factoryClass
argument_list|)
expr_stmt|;
if|if
condition|(
name|useSaxon
condition|)
block|{
name|XsltHelper
operator|.
name|registerSaxonConfiguration
argument_list|(
name|ctx
argument_list|,
name|factoryClass
argument_list|,
name|factory
argument_list|,
name|saxonConfiguration
argument_list|)
expr_stmt|;
name|XsltHelper
operator|.
name|registerSaxonConfigurationProperties
argument_list|(
name|ctx
argument_list|,
name|factoryClass
argument_list|,
name|factory
argument_list|,
name|saxonConfigurationProperties
argument_list|)
expr_stmt|;
name|XsltHelper
operator|.
name|registerSaxonExtensionFunctions
argument_list|(
name|ctx
argument_list|,
name|factoryClass
argument_list|,
name|factory
argument_list|,
name|saxonExtensionFunctions
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using TransformerFactory {}"
argument_list|,
name|factory
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setTransformerFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resultHandlerFactory
operator|!=
literal|null
condition|)
block|{
name|xslt
operator|.
name|setResultHandlerFactory
argument_list|(
name|resultHandlerFactory
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|errorListener
operator|!=
literal|null
condition|)
block|{
name|xslt
operator|.
name|errorListener
argument_list|(
name|errorListener
argument_list|)
expr_stmt|;
block|}
name|xslt
operator|.
name|setFailOnNullBody
argument_list|(
name|failOnNullBody
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|transformerCacheSize
argument_list|(
name|transformerCacheSize
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setUriResolver
argument_list|(
name|uriResolver
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setEntityResolver
argument_list|(
name|entityResolver
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setAllowStAX
argument_list|(
name|allowStAX
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setDeleteOutputFile
argument_list|(
name|deleteOutputFile
argument_list|)
expr_stmt|;
name|configureOutput
argument_list|(
name|xslt
argument_list|,
name|output
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
comment|// any additional transformer parameters then make a copy to avoid side-effects
if|if
condition|(
name|parameters
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|copy
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
name|xslt
operator|.
name|setParameters
argument_list|(
name|copy
argument_list|)
expr_stmt|;
block|}
comment|// must load resource first which sets a template and do a stylesheet compilation to catch errors early
name|loadResource
argument_list|(
name|resourceUri
argument_list|,
name|xslt
argument_list|)
expr_stmt|;
return|return
name|xslt
return|;
block|}
DECL|method|configureOutput (XsltBuilder xslt, String output)
specifier|protected
name|void
name|configureOutput
parameter_list|(
name|XsltBuilder
name|xslt
parameter_list|,
name|String
name|output
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|output
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
literal|"string"
operator|.
name|equalsIgnoreCase
argument_list|(
name|output
argument_list|)
condition|)
block|{
name|xslt
operator|.
name|outputString
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"bytes"
operator|.
name|equalsIgnoreCase
argument_list|(
name|output
argument_list|)
condition|)
block|{
name|xslt
operator|.
name|outputBytes
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"DOM"
operator|.
name|equalsIgnoreCase
argument_list|(
name|output
argument_list|)
condition|)
block|{
name|xslt
operator|.
name|outputDOM
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"file"
operator|.
name|equalsIgnoreCase
argument_list|(
name|output
argument_list|)
condition|)
block|{
name|xslt
operator|.
name|outputFile
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown output type: "
operator|+
name|output
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|getXslt
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

