begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.velocity
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|velocity
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|ExchangePattern
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
name|Message
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
name|component
operator|.
name|ResourceEndpoint
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
name|util
operator|.
name|ExchangeHelper
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
name|IOHelper
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
name|ResourceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|VelocityContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|app
operator|.
name|VelocityEngine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|context
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|runtime
operator|.
name|RuntimeConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|runtime
operator|.
name|log
operator|.
name|CommonsLogLogChute
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"velocity"
argument_list|,
name|title
operator|=
literal|"Velocity"
argument_list|,
name|syntax
operator|=
literal|"velocity:resourceUri"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"transformation"
argument_list|)
DECL|class|VelocityEndpoint
specifier|public
class|class
name|VelocityEndpoint
extends|extends
name|ResourceEndpoint
block|{
DECL|field|velocityEngine
specifier|private
name|VelocityEngine
name|velocityEngine
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|loaderCache
specifier|private
name|boolean
name|loaderCache
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
annotation|@
name|UriParam
DECL|field|propertiesFile
specifier|private
name|String
name|propertiesFile
decl_stmt|;
DECL|method|VelocityEndpoint ()
specifier|public
name|VelocityEndpoint
parameter_list|()
block|{     }
DECL|method|VelocityEndpoint (String uri, VelocityComponent component, String resourceUri)
specifier|public
name|VelocityEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|VelocityComponent
name|component
parameter_list|,
name|String
name|resourceUri
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getExchangePattern ()
specifier|public
name|ExchangePattern
name|getExchangePattern
parameter_list|()
block|{
return|return
name|ExchangePattern
operator|.
name|InOut
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|"velocity:"
operator|+
name|getResourceUri
argument_list|()
return|;
block|}
DECL|method|getVelocityEngine ()
specifier|private
specifier|synchronized
name|VelocityEngine
name|getVelocityEngine
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|velocityEngine
operator|==
literal|null
condition|)
block|{
name|velocityEngine
operator|=
operator|new
name|VelocityEngine
argument_list|()
expr_stmt|;
comment|// set the class resolver as a property so we can access it from CamelVelocityClasspathResourceLoader
name|velocityEngine
operator|.
name|addProperty
argument_list|(
literal|"CamelClassResolver"
argument_list|,
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|)
expr_stmt|;
comment|// set default properties
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
name|RuntimeConstants
operator|.
name|FILE_RESOURCE_LOADER_CACHE
argument_list|,
name|isLoaderCache
argument_list|()
condition|?
literal|"true"
else|:
literal|"false"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
name|RuntimeConstants
operator|.
name|RESOURCE_LOADER
argument_list|,
literal|"file, class"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"class.resource.loader.description"
argument_list|,
literal|"Camel Velocity Classpath Resource Loader"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"class.resource.loader.class"
argument_list|,
name|CamelVelocityClasspathResourceLoader
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
name|RuntimeConstants
operator|.
name|RUNTIME_LOG_LOGSYSTEM_CLASS
argument_list|,
name|CommonsLogLogChute
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
name|CommonsLogLogChute
operator|.
name|LOGCHUTE_COMMONS_LOG_NAME
argument_list|,
name|VelocityEndpoint
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// load the velocity properties from property file which may overrides the default ones
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getPropertiesFile
argument_list|()
argument_list|)
condition|)
block|{
name|InputStream
name|reader
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|getPropertiesFile
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|properties
operator|.
name|load
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Loaded the velocity configuration file "
operator|+
name|getPropertiesFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|reader
argument_list|,
name|getPropertiesFile
argument_list|()
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Initializing VelocityEngine with properties {}"
argument_list|,
name|properties
argument_list|)
expr_stmt|;
comment|// help the velocityEngine to load the CamelVelocityClasspathResourceLoader
name|ClassLoader
name|old
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
name|ClassLoader
name|delegate
init|=
operator|new
name|CamelVelocityDelegateClassLoader
argument_list|(
name|old
argument_list|)
decl_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|velocityEngine
operator|.
name|init
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|old
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|velocityEngine
return|;
block|}
DECL|method|setVelocityEngine (VelocityEngine velocityEngine)
specifier|public
name|void
name|setVelocityEngine
parameter_list|(
name|VelocityEngine
name|velocityEngine
parameter_list|)
block|{
name|this
operator|.
name|velocityEngine
operator|=
name|velocityEngine
expr_stmt|;
block|}
DECL|method|isLoaderCache ()
specifier|public
name|boolean
name|isLoaderCache
parameter_list|()
block|{
return|return
name|loaderCache
return|;
block|}
comment|/**      * Enables / disables the velocity resource loader cache which is enabled by default      */
DECL|method|setLoaderCache (boolean loaderCache)
specifier|public
name|void
name|setLoaderCache
parameter_list|(
name|boolean
name|loaderCache
parameter_list|)
block|{
name|this
operator|.
name|loaderCache
operator|=
name|loaderCache
expr_stmt|;
block|}
comment|/**      * Character encoding of the resource content.      */
DECL|method|setEncoding (String encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
DECL|method|getEncoding ()
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
comment|/**      * The URI of the properties file which is used for VelocityEngine initialization.      */
DECL|method|setPropertiesFile (String file)
specifier|public
name|void
name|setPropertiesFile
parameter_list|(
name|String
name|file
parameter_list|)
block|{
name|propertiesFile
operator|=
name|file
expr_stmt|;
block|}
DECL|method|getPropertiesFile ()
specifier|public
name|String
name|getPropertiesFile
parameter_list|()
block|{
return|return
name|propertiesFile
return|;
block|}
DECL|method|findOrCreateEndpoint (String uri, String newResourceUri)
specifier|public
name|VelocityEndpoint
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
name|getResourceUri
argument_list|()
argument_list|,
name|newResourceUri
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
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
name|VelocityEndpoint
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
name|String
name|path
init|=
name|getResourceUri
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|path
argument_list|,
literal|"resourceUri"
argument_list|)
expr_stmt|;
name|String
name|newResourceUri
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|VelocityConstants
operator|.
name|VELOCITY_RESOURCE_URI
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|newResourceUri
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|VelocityConstants
operator|.
name|VELOCITY_RESOURCE_URI
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"{} set to {} creating new endpoint to handle exchange"
argument_list|,
name|VelocityConstants
operator|.
name|VELOCITY_RESOURCE_URI
argument_list|,
name|newResourceUri
argument_list|)
expr_stmt|;
name|VelocityEndpoint
name|newEndpoint
init|=
name|findOrCreateEndpoint
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|,
name|newResourceUri
argument_list|)
decl_stmt|;
name|newEndpoint
operator|.
name|onExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return;
block|}
name|Reader
name|reader
decl_stmt|;
name|String
name|content
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|VelocityConstants
operator|.
name|VELOCITY_TEMPLATE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|content
operator|!=
literal|null
condition|)
block|{
comment|// use content from header
name|reader
operator|=
operator|new
name|StringReader
argument_list|(
name|content
argument_list|)
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Velocity content read from header {} for endpoint {}"
argument_list|,
name|VelocityConstants
operator|.
name|VELOCITY_TEMPLATE
argument_list|,
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// remove the header to avoid it being propagated in the routing
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|VelocityConstants
operator|.
name|VELOCITY_TEMPLATE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Velocity content read from resource {} with resourceUri: {} for endpoint {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|getResourceUri
argument_list|()
block|,
name|path
block|,
name|getEndpointUri
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
name|reader
operator|=
name|getEncoding
argument_list|()
operator|!=
literal|null
condition|?
operator|new
name|InputStreamReader
argument_list|(
name|getResourceAsInputStream
argument_list|()
argument_list|,
name|getEncoding
argument_list|()
argument_list|)
else|:
operator|new
name|InputStreamReader
argument_list|(
name|getResourceAsInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// getResourceAsInputStream also considers the content cache
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|String
name|logTag
init|=
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Context
name|velocityContext
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|VelocityConstants
operator|.
name|VELOCITY_CONTEXT
argument_list|,
name|Context
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|velocityContext
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|variableMap
init|=
name|ExchangeHelper
operator|.
name|createVariableMap
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|supplementalMap
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|VelocityConstants
operator|.
name|VELOCITY_SUPPLEMENTAL_CONTEXT
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|supplementalMap
operator|!=
literal|null
condition|)
block|{
name|variableMap
operator|.
name|putAll
argument_list|(
name|supplementalMap
argument_list|)
expr_stmt|;
block|}
name|velocityContext
operator|=
operator|new
name|VelocityContext
argument_list|(
name|variableMap
argument_list|)
expr_stmt|;
block|}
comment|// let velocity parse and generate the result in buffer
name|VelocityEngine
name|engine
init|=
name|getVelocityEngine
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Velocity is evaluating using velocity context: {}"
argument_list|,
name|velocityContext
argument_list|)
expr_stmt|;
name|engine
operator|.
name|evaluate
argument_list|(
name|velocityContext
argument_list|,
name|buffer
argument_list|,
name|logTag
argument_list|,
name|reader
argument_list|)
expr_stmt|;
comment|// now lets output the results to the exchange
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|out
operator|.
name|setBody
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|setAttachments
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getAttachments
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

