begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servlet
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|Locale
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
name|Consumer
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
name|Endpoint
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
name|Processor
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
name|http
operator|.
name|common
operator|.
name|HttpBinding
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
name|http
operator|.
name|common
operator|.
name|HttpCommonComponent
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
name|http
operator|.
name|common
operator|.
name|HttpConsumer
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
name|HeaderFilterStrategy
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
name|RestApiConsumerFactory
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
name|RestConfiguration
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
name|RestConsumerFactory
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
name|FileUtil
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
name|StringHelper
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
name|URISupport
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
name|UnsafeUriCharactersEncoder
import|;
end_import

begin_class
DECL|class|ServletComponent
specifier|public
class|class
name|ServletComponent
extends|extends
name|HttpCommonComponent
implements|implements
name|RestConsumerFactory
implements|,
name|RestApiConsumerFactory
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"CamelServlet"
argument_list|,
name|description
operator|=
literal|"Default name of servlet to use. The default name is CamelServlet."
argument_list|)
DECL|field|servletName
specifier|private
name|String
name|servletName
init|=
literal|"CamelServlet"
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|description
operator|=
literal|"To use a custom org.apache.camel.component.servlet.HttpRegistry."
argument_list|)
DECL|field|httpRegistry
specifier|private
name|HttpRegistry
name|httpRegistry
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|description
operator|=
literal|"Whether to automatic bind multipart/form-data as attachments on the Camel Exchange}."
operator|+
literal|" The options attachmentMultipartBinding=true and disableStreamCache=false cannot work together."
operator|+
literal|" Remove disableStreamCache to use AttachmentMultipartBinding."
operator|+
literal|" This is turn off by default as this may require servlet specific configuration to enable this when using Servlet's."
argument_list|)
DECL|field|attachmentMultipartBinding
specifier|private
name|boolean
name|attachmentMultipartBinding
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|description
operator|=
literal|"Whitelist of accepted filename extensions for accepting uploaded files."
operator|+
literal|" Multiple extensions can be separated by comma, such as txt,xml."
argument_list|)
DECL|field|fileNameExtWhitelist
specifier|private
name|String
name|fileNameExtWhitelist
decl_stmt|;
DECL|method|ServletComponent ()
specifier|public
name|ServletComponent
parameter_list|()
block|{     }
DECL|method|ServletComponent (Class<? extends ServletEndpoint> endpointClass)
specifier|public
name|ServletComponent
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|ServletEndpoint
argument_list|>
name|endpointClass
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// must extract well known parameters before we create the endpoint
name|Boolean
name|throwExceptionOnFailure
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"throwExceptionOnFailure"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Boolean
name|transferException
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"transferException"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Boolean
name|bridgeEndpoint
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"bridgeEndpoint"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpBinding
name|binding
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"httpBinding"
argument_list|,
name|HttpBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|Boolean
name|matchOnUriPrefix
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"matchOnUriPrefix"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|servletName
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"servletName"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|getServletName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|httpMethodRestrict
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"httpMethodRestrict"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"headerFilterStrategy"
argument_list|,
name|HeaderFilterStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
name|Boolean
name|async
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"async"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Boolean
name|attachmentMultipartBinding
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"attachmentMultipartBinding"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Boolean
name|disableStreamCache
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"disableStreamCache"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|lenientContextPath
argument_list|()
condition|)
block|{
comment|// the uri must have a leading slash for the context-path matching to work with servlet, and it can be something people
comment|// forget to add and then the servlet consumer cannot match the context-path as would have been expected
name|String
name|scheme
init|=
name|StringHelper
operator|.
name|before
argument_list|(
name|uri
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|String
name|after
init|=
name|StringHelper
operator|.
name|after
argument_list|(
name|uri
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
comment|// rebuild uri to have exactly one leading slash
while|while
condition|(
name|after
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|after
operator|=
name|after
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|after
operator|=
literal|"/"
operator|+
name|after
expr_stmt|;
name|uri
operator|=
name|scheme
operator|+
literal|":"
operator|+
name|after
expr_stmt|;
block|}
comment|// restructure uri to be based on the parameters left as we dont want to include the Camel internal options
name|URI
name|httpUri
init|=
name|URISupport
operator|.
name|createRemainingURI
argument_list|(
operator|new
name|URI
argument_list|(
name|UnsafeUriCharactersEncoder
operator|.
name|encodeHttpURI
argument_list|(
name|uri
argument_list|)
argument_list|)
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|ServletEndpoint
name|endpoint
init|=
name|createServletEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|httpUri
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setServletName
argument_list|(
name|servletName
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setFileNameExtWhitelist
argument_list|(
name|fileNameExtWhitelist
argument_list|)
expr_stmt|;
if|if
condition|(
name|async
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setAsync
argument_list|(
name|async
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|headerFilterStrategy
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|headerFilterStrategy
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setEndpointHeaderFilterStrategy
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
comment|// prefer to use endpoint configured over component configured
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
comment|// fallback to component configured
name|binding
operator|=
name|getHttpBinding
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|binding
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
comment|// should we use an exception for failed error codes?
if|if
condition|(
name|throwExceptionOnFailure
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setThrowExceptionOnFailure
argument_list|(
name|throwExceptionOnFailure
argument_list|)
expr_stmt|;
block|}
comment|// should we transfer exception as serialized object
if|if
condition|(
name|transferException
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setTransferException
argument_list|(
name|transferException
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bridgeEndpoint
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setBridgeEndpoint
argument_list|(
name|bridgeEndpoint
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|matchOnUriPrefix
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setMatchOnUriPrefix
argument_list|(
name|matchOnUriPrefix
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpMethodRestrict
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setHttpMethodRestrict
argument_list|(
name|httpMethodRestrict
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|attachmentMultipartBinding
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setAttachmentMultipartBinding
argument_list|(
name|attachmentMultipartBinding
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|.
name|setAttachmentMultipartBinding
argument_list|(
name|isAttachmentMultipartBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|disableStreamCache
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setDisableStreamCache
argument_list|(
name|disableStreamCache
argument_list|)
expr_stmt|;
block|}
comment|// turn off stream caching if in attachment mode
if|if
condition|(
name|endpoint
operator|.
name|isAttachmentMultipartBinding
argument_list|()
condition|)
block|{
if|if
condition|(
name|disableStreamCache
operator|==
literal|null
condition|)
block|{
comment|// disableStreamCache not explicit configured so we can automatic change it
name|log
operator|.
name|info
argument_list|(
literal|"Disabling stream caching as attachmentMultipartBinding is enabled"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setDisableStreamCache
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|disableStreamCache
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The options attachmentMultipartBinding=true and disableStreamCache=false cannot work together."
operator|+
literal|" Remove disableStreamCache to use AttachmentMultipartBinding"
argument_list|)
throw|;
block|}
block|}
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
comment|/**      * Whether defining the context-path is lenient and do not require an exact leading slash.      */
DECL|method|lenientContextPath ()
specifier|protected
name|boolean
name|lenientContextPath
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Strategy to create the servlet endpoint.      */
DECL|method|createServletEndpoint (String endpointUri, ServletComponent component, URI httpUri)
specifier|protected
name|ServletEndpoint
name|createServletEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|ServletComponent
name|component
parameter_list|,
name|URI
name|httpUri
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|ServletEndpoint
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|httpUri
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|connect (HttpConsumer consumer)
specifier|public
name|void
name|connect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|ServletConsumer
name|sc
init|=
operator|(
name|ServletConsumer
operator|)
name|consumer
decl_stmt|;
name|String
name|name
init|=
name|sc
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getServletName
argument_list|()
decl_stmt|;
name|HttpRegistry
name|registry
init|=
name|httpRegistry
decl_stmt|;
if|if
condition|(
name|registry
operator|==
literal|null
condition|)
block|{
name|registry
operator|=
name|DefaultHttpRegistry
operator|.
name|getHttpRegistry
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
name|registry
operator|.
name|register
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|disconnect (HttpConsumer consumer)
specifier|public
name|void
name|disconnect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|ServletConsumer
name|sc
init|=
operator|(
name|ServletConsumer
operator|)
name|consumer
decl_stmt|;
name|String
name|name
init|=
name|sc
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getServletName
argument_list|()
decl_stmt|;
name|HttpRegistry
name|registry
init|=
name|httpRegistry
decl_stmt|;
if|if
condition|(
name|registry
operator|==
literal|null
condition|)
block|{
name|registry
operator|=
name|DefaultHttpRegistry
operator|.
name|getHttpRegistry
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
name|registry
operator|.
name|unregister
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|getServletName ()
specifier|public
name|String
name|getServletName
parameter_list|()
block|{
return|return
name|servletName
return|;
block|}
comment|/**      * Default name of servlet to use. The default name is CamelServlet.      */
DECL|method|setServletName (String servletName)
specifier|public
name|void
name|setServletName
parameter_list|(
name|String
name|servletName
parameter_list|)
block|{
name|this
operator|.
name|servletName
operator|=
name|servletName
expr_stmt|;
block|}
DECL|method|getHttpRegistry ()
specifier|public
name|HttpRegistry
name|getHttpRegistry
parameter_list|()
block|{
return|return
name|httpRegistry
return|;
block|}
comment|/**      * To use a custom org.apache.camel.component.servlet.HttpRegistry.      */
DECL|method|setHttpRegistry (HttpRegistry httpRegistry)
specifier|public
name|void
name|setHttpRegistry
parameter_list|(
name|HttpRegistry
name|httpRegistry
parameter_list|)
block|{
name|this
operator|.
name|httpRegistry
operator|=
name|httpRegistry
expr_stmt|;
block|}
DECL|method|isAttachmentMultipartBinding ()
specifier|public
name|boolean
name|isAttachmentMultipartBinding
parameter_list|()
block|{
return|return
name|attachmentMultipartBinding
return|;
block|}
comment|/**      * Whether to automatic bind multipart/form-data as attachments on the Camel {@link Exchange}.      *<p/>      * The options attachmentMultipartBinding=true and disableStreamCache=false cannot work together.      * Remove disableStreamCache to use AttachmentMultipartBinding.      *<p/>      * This is turn off by default as this may require servlet specific configuration to enable this when using Servlet's.      */
DECL|method|setAttachmentMultipartBinding (boolean attachmentMultipartBinding)
specifier|public
name|void
name|setAttachmentMultipartBinding
parameter_list|(
name|boolean
name|attachmentMultipartBinding
parameter_list|)
block|{
name|this
operator|.
name|attachmentMultipartBinding
operator|=
name|attachmentMultipartBinding
expr_stmt|;
block|}
DECL|method|getFileNameExtWhitelist ()
specifier|public
name|String
name|getFileNameExtWhitelist
parameter_list|()
block|{
return|return
name|fileNameExtWhitelist
return|;
block|}
comment|/**      * Whitelist of accepted filename extensions for accepting uploaded files.      *<p/>      * Multiple extensions can be separated by comma, such as txt,xml.      */
DECL|method|setFileNameExtWhitelist (String fileNameExtWhitelist)
specifier|public
name|void
name|setFileNameExtWhitelist
parameter_list|(
name|String
name|fileNameExtWhitelist
parameter_list|)
block|{
name|this
operator|.
name|fileNameExtWhitelist
operator|=
name|fileNameExtWhitelist
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (CamelContext camelContext, Processor processor, String verb, String basePath, String uriTemplate, String consumes, String produces, RestConfiguration configuration, Map<String, Object> parameters)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|verb
parameter_list|,
name|String
name|basePath
parameter_list|,
name|String
name|uriTemplate
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|,
name|RestConfiguration
name|configuration
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|doCreateConsumer
argument_list|(
name|camelContext
argument_list|,
name|processor
argument_list|,
name|verb
argument_list|,
name|basePath
argument_list|,
name|uriTemplate
argument_list|,
name|consumes
argument_list|,
name|produces
argument_list|,
name|configuration
argument_list|,
name|parameters
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createApiConsumer (CamelContext camelContext, Processor processor, String contextPath, RestConfiguration configuration, Map<String, Object> parameters)
specifier|public
name|Consumer
name|createApiConsumer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|contextPath
parameter_list|,
name|RestConfiguration
name|configuration
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// reuse the createConsumer method we already have. The api need to use GET and match on uri prefix
return|return
name|doCreateConsumer
argument_list|(
name|camelContext
argument_list|,
name|processor
argument_list|,
literal|"GET"
argument_list|,
name|contextPath
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|configuration
argument_list|,
name|parameters
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|doCreateConsumer (CamelContext camelContext, Processor processor, String verb, String basePath, String uriTemplate, String consumes, String produces, RestConfiguration configuration, Map<String, Object> parameters, boolean api)
name|Consumer
name|doCreateConsumer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|verb
parameter_list|,
name|String
name|basePath
parameter_list|,
name|String
name|uriTemplate
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|,
name|RestConfiguration
name|configuration
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|boolean
name|api
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|path
init|=
name|basePath
decl_stmt|;
if|if
condition|(
name|uriTemplate
operator|!=
literal|null
condition|)
block|{
comment|// make sure to avoid double slashes
if|if
condition|(
name|uriTemplate
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|+
name|uriTemplate
expr_stmt|;
block|}
else|else
block|{
name|path
operator|=
name|path
operator|+
literal|"/"
operator|+
name|uriTemplate
expr_stmt|;
block|}
block|}
name|path
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|path
argument_list|)
expr_stmt|;
comment|// if no explicit port/host configured, then use port from rest configuration
name|RestConfiguration
name|config
init|=
name|configuration
decl_stmt|;
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
name|config
operator|=
name|camelContext
operator|.
name|getRestConfiguration
argument_list|(
literal|"servlet"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// build query string, and append any endpoint configuration properties
if|if
condition|(
name|config
operator|.
name|getComponent
argument_list|()
operator|==
literal|null
operator|||
name|config
operator|.
name|getComponent
argument_list|()
operator|.
name|equals
argument_list|(
literal|"servlet"
argument_list|)
condition|)
block|{
comment|// setup endpoint options
if|if
condition|(
name|config
operator|.
name|getEndpointProperties
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|config
operator|.
name|getEndpointProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|map
operator|.
name|putAll
argument_list|(
name|config
operator|.
name|getEndpointProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|boolean
name|cors
init|=
name|config
operator|.
name|isEnableCORS
argument_list|()
decl_stmt|;
if|if
condition|(
name|cors
condition|)
block|{
comment|// allow HTTP Options as we want to handle CORS in rest-dsl
name|map
operator|.
name|put
argument_list|(
literal|"optionsEnabled"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
comment|// do not append with context-path as the servlet path should be without context-path
name|String
name|query
init|=
name|URISupport
operator|.
name|createQueryString
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|String
name|url
decl_stmt|;
if|if
condition|(
name|api
condition|)
block|{
name|url
operator|=
literal|"servlet:///%s?matchOnUriPrefix=true&httpMethodRestrict=%s"
expr_stmt|;
block|}
else|else
block|{
name|url
operator|=
literal|"servlet:///%s?httpMethodRestrict=%s"
expr_stmt|;
block|}
comment|// must use upper case for restrict
name|String
name|restrict
init|=
name|verb
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
if|if
condition|(
name|cors
condition|)
block|{
name|restrict
operator|+=
literal|",OPTIONS"
expr_stmt|;
block|}
comment|// get the endpoint
name|url
operator|=
name|String
operator|.
name|format
argument_list|(
name|url
argument_list|,
name|path
argument_list|,
name|restrict
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|query
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|url
operator|=
name|url
operator|+
literal|"&"
operator|+
name|query
expr_stmt|;
block|}
name|ServletEndpoint
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|url
argument_list|,
name|ServletEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|camelContext
argument_list|,
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|map
operator|.
name|containsKey
argument_list|(
literal|"httpBinding"
argument_list|)
condition|)
block|{
comment|// use the rest binding, if not using a custom http binding
name|HttpBinding
name|binding
init|=
operator|new
name|ServletRestHttpBinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|endpoint
operator|.
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setTransferException
argument_list|(
name|endpoint
operator|.
name|isTransferException
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setEagerCheckContentAvailable
argument_list|(
name|endpoint
operator|.
name|isEagerCheckContentAvailable
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setHttpBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
comment|// configure consumer properties
name|Consumer
name|consumer
init|=
name|endpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getConsumerProperties
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|config
operator|.
name|getConsumerProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setProperties
argument_list|(
name|camelContext
argument_list|,
name|consumer
argument_list|,
name|config
operator|.
name|getConsumerProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|consumer
return|;
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
name|RestConfiguration
name|config
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRestConfiguration
argument_list|(
literal|"servlet"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// configure additional options on jetty configuration
if|if
condition|(
name|config
operator|.
name|getComponentProperties
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|config
operator|.
name|getComponentProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setProperties
argument_list|(
name|this
argument_list|,
name|config
operator|.
name|getComponentProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

