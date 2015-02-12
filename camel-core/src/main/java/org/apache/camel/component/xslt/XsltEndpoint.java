begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|builder
operator|.
name|xml
operator|.
name|ResultHandlerFactory
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
name|builder
operator|.
name|xml
operator|.
name|XsltBuilder
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
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
name|util
operator|.
name|ObjectHelper
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
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

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
name|scheme
operator|=
literal|"xslt"
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
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|XsltEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|SAXON_TRANSFORMER_FACTORY_CLASS_NAME
specifier|private
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
literal|"true"
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
DECL|field|converter
specifier|private
name|XmlConverter
name|converter
decl_stmt|;
annotation|@
name|UriParam
DECL|field|transformerFactoryClass
specifier|private
name|String
name|transformerFactoryClass
decl_stmt|;
annotation|@
name|UriParam
DECL|field|transformerFactory
specifier|private
name|TransformerFactory
name|transformerFactory
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|saxon
specifier|private
name|boolean
name|saxon
decl_stmt|;
annotation|@
name|UriParam
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
DECL|field|errorListener
specifier|private
name|ErrorListener
name|errorListener
decl_stmt|;
annotation|@
name|UriParam
DECL|field|uriResolver
specifier|private
name|URIResolver
name|uriResolver
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|allowStAX
specifier|private
name|boolean
name|allowStAX
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|deleteOutputFile
specifier|private
name|boolean
name|deleteOutputFile
decl_stmt|;
annotation|@
name|Deprecated
DECL|method|XsltEndpoint (String endpointUri, Component component, XsltBuilder xslt, String resourceUri, boolean cacheStylesheet)
specifier|public
name|XsltEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|XsltBuilder
name|xslt
parameter_list|,
name|String
name|resourceUri
parameter_list|,
name|boolean
name|cacheStylesheet
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|xslt
argument_list|)
expr_stmt|;
name|this
operator|.
name|xslt
operator|=
name|xslt
expr_stmt|;
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
name|this
operator|.
name|contentCache
operator|=
name|cacheStylesheet
expr_stmt|;
block|}
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
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Endpoint State"
argument_list|)
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel ID"
argument_list|)
DECL|method|getCamelId ()
specifier|public
name|String
name|getCamelId
parameter_list|()
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel ManagementName"
argument_list|)
DECL|method|getCamelManagementName ()
specifier|public
name|String
name|getCamelManagementName
parameter_list|()
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getManagementName
argument_list|()
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
name|LOG
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
comment|/**      * The name of the template to load from classpath or file system      */
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
DECL|method|getConverter ()
specifier|public
name|XmlConverter
name|getConverter
parameter_list|()
block|{
return|return
name|converter
return|;
block|}
comment|/**      * To use a custom implementation of {@link org.apache.camel.converter.jaxp.XmlConverter}      */
DECL|method|setConverter (XmlConverter converter)
specifier|public
name|void
name|setConverter
parameter_list|(
name|XmlConverter
name|converter
parameter_list|)
block|{
name|this
operator|.
name|converter
operator|=
name|converter
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
comment|/**      * Whether to allow using StAX as the javax.xml.transform.Source.      */
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
DECL|method|loadResource (String resourceUri)
specifier|protected
name|void
name|loadResource
parameter_list|(
name|String
name|resourceUri
parameter_list|)
throws|throws
name|TransformerException
throws|,
name|IOException
block|{
name|LOG
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
name|LOG
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
name|this
operator|.
name|xslt
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|XsltBuilder
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|converter
operator|!=
literal|null
condition|)
block|{
name|xslt
operator|.
name|setConverter
argument_list|(
name|converter
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|transformerFactoryClass
operator|==
literal|null
operator|&&
name|saxon
condition|)
block|{
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
name|?
argument_list|>
name|factoryClass
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|transformerFactoryClass
argument_list|,
name|XsltComponent
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
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
operator|(
name|TransformerFactory
operator|)
name|getCamelContext
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|factoryClass
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
name|LOG
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
name|getConverter
argument_list|()
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
argument_list|)
expr_stmt|;
comment|// and then inject camel context and start service
name|xslt
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
comment|// the processor is the xslt builder
name|setProcessor
argument_list|(
name|xslt
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|xslt
argument_list|)
expr_stmt|;
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
name|xslt
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

