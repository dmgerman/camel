begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rest
package|;
end_package

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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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
name|ComponentVerifier
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
name|VerifiableComponent
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
name|extension
operator|.
name|ComponentVerifierExtension
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
name|DefaultComponent
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
name|model
operator|.
name|rest
operator|.
name|RestConstants
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
name|util
operator|.
name|CamelContextHelper
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
name|IntrospectionSupport
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
name|URISupport
import|;
end_import

begin_comment
comment|/**  * Rest component.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"verifiers"
argument_list|,
name|enums
operator|=
literal|"parameters,connectivity"
argument_list|)
DECL|class|RestComponent
specifier|public
class|class
name|RestComponent
extends|extends
name|DefaultComponent
implements|implements
name|VerifiableComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"common"
argument_list|)
DECL|field|componentName
specifier|private
name|String
name|componentName
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|apiDoc
specifier|private
name|String
name|apiDoc
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
DECL|method|RestComponent ()
specifier|public
name|RestComponent
parameter_list|()
block|{
name|registerExtension
argument_list|(
name|RestComponentVerifierExtension
operator|::
operator|new
argument_list|)
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
name|String
name|restConfigurationName
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"componentName"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|componentName
argument_list|)
decl_stmt|;
name|RestEndpoint
name|answer
init|=
operator|new
name|RestEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setComponentName
argument_list|(
name|restConfigurationName
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setApiDoc
argument_list|(
name|apiDoc
argument_list|)
expr_stmt|;
name|RestConfiguration
name|config
init|=
operator|new
name|RestConfiguration
argument_list|()
decl_stmt|;
name|mergeConfigurations
argument_list|(
name|config
argument_list|,
name|findGlobalRestConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|mergeConfigurations
argument_list|(
name|config
argument_list|,
name|getCamelContext
argument_list|()
operator|.
name|getRestConfiguration
argument_list|(
name|restConfigurationName
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// if no explicit host was given, then fallback and use default configured host
name|String
name|h
init|=
name|getAndRemoveOrResolveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"host"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|host
argument_list|)
decl_stmt|;
if|if
condition|(
name|h
operator|==
literal|null
condition|)
block|{
name|h
operator|=
name|config
operator|.
name|getHost
argument_list|()
expr_stmt|;
name|int
name|port
init|=
name|config
operator|.
name|getPort
argument_list|()
decl_stmt|;
comment|// is there a custom port number
if|if
condition|(
name|port
operator|>
literal|0
operator|&&
name|port
operator|!=
literal|80
operator|&&
name|port
operator|!=
literal|443
condition|)
block|{
name|h
operator|+=
literal|":"
operator|+
name|port
expr_stmt|;
block|}
block|}
comment|// host must start with http:// or https://
if|if
condition|(
name|h
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|h
operator|.
name|startsWith
argument_list|(
literal|"http://"
argument_list|)
operator|||
name|h
operator|.
name|startsWith
argument_list|(
literal|"https://"
argument_list|)
operator|)
condition|)
block|{
name|h
operator|=
literal|"http://"
operator|+
name|h
expr_stmt|;
block|}
name|answer
operator|.
name|setHost
argument_list|(
name|h
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|answer
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// use only what remains and at this point parameters that have been used have been removed
comment|// without overwriting any query parameters set via queryParameters endpoint option
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|queryParameters
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|existingQueryParameters
init|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
name|answer
operator|.
name|getQueryParameters
argument_list|()
argument_list|)
decl_stmt|;
name|queryParameters
operator|.
name|putAll
argument_list|(
name|existingQueryParameters
argument_list|)
expr_stmt|;
specifier|final
name|String
name|remainingParameters
init|=
name|URISupport
operator|.
name|createQueryString
argument_list|(
name|queryParameters
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setQueryParameters
argument_list|(
name|remainingParameters
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|setParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|remaining
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid syntax. Must be rest:method:path[:uriTemplate] where uriTemplate is optional"
argument_list|)
throw|;
block|}
name|String
name|method
init|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|remaining
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|remaining
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|String
name|path
decl_stmt|;
name|String
name|uriTemplate
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
operator|&&
name|s
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|path
operator|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|s
argument_list|,
literal|":"
argument_list|)
expr_stmt|;
name|uriTemplate
operator|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|s
argument_list|,
literal|":"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|path
operator|=
name|s
expr_stmt|;
name|uriTemplate
operator|=
literal|null
expr_stmt|;
block|}
comment|// remove trailing slashes
name|path
operator|=
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|uriTemplate
operator|=
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
name|uriTemplate
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setUriTemplate
argument_list|(
name|uriTemplate
argument_list|)
expr_stmt|;
comment|// if no explicit component name was given, then fallback and use default configured component name
if|if
condition|(
name|answer
operator|.
name|getComponentName
argument_list|()
operator|==
literal|null
condition|)
block|{
name|String
name|name
init|=
name|config
operator|.
name|getProducerComponent
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
comment|// fallback and use the consumer name
name|name
operator|=
name|config
operator|.
name|getComponent
argument_list|()
expr_stmt|;
block|}
name|answer
operator|.
name|setComponentName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|// if no explicit producer api was given, then fallback and use default configured
if|if
condition|(
name|answer
operator|.
name|getApiDoc
argument_list|()
operator|==
literal|null
condition|)
block|{
name|answer
operator|.
name|setApiDoc
argument_list|(
name|config
operator|.
name|getProducerApiDoc
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getComponentName ()
specifier|public
name|String
name|getComponentName
parameter_list|()
block|{
return|return
name|componentName
return|;
block|}
comment|/**      * The Camel Rest component to use for the REST transport, such as restlet, spark-rest.      * If no component has been explicit configured, then Camel will lookup if there is a Camel component      * that integrates with the Rest DSL, or if a org.apache.camel.spi.RestConsumerFactory (consumer)      * or org.apache.camel.spi.RestProducerFactory (producer) is registered in the registry.      * If either one is found, then that is being used.      */
DECL|method|setComponentName (String componentName)
specifier|public
name|void
name|setComponentName
parameter_list|(
name|String
name|componentName
parameter_list|)
block|{
name|this
operator|.
name|componentName
operator|=
name|componentName
expr_stmt|;
block|}
DECL|method|getApiDoc ()
specifier|public
name|String
name|getApiDoc
parameter_list|()
block|{
return|return
name|apiDoc
return|;
block|}
comment|/**      * The swagger api doc resource to use.      * The resource is loaded from classpath by default and must be in JSon format.      */
DECL|method|setApiDoc (String apiDoc)
specifier|public
name|void
name|setApiDoc
parameter_list|(
name|String
name|apiDoc
parameter_list|)
block|{
name|this
operator|.
name|apiDoc
operator|=
name|apiDoc
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
comment|/**      * Host and port of HTTP service to use (override host in swagger schema)      */
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
comment|// ****************************************
comment|// Helpers
comment|// ****************************************
DECL|method|findGlobalRestConfiguration ()
specifier|private
name|RestConfiguration
name|findGlobalRestConfiguration
parameter_list|()
block|{
name|CamelContext
name|context
init|=
name|getCamelContext
argument_list|()
decl_stmt|;
name|RestConfiguration
name|conf
init|=
name|CamelContextHelper
operator|.
name|lookup
argument_list|(
name|context
argument_list|,
name|RestConstants
operator|.
name|DEFAULT_REST_CONFIGURATION_ID
argument_list|,
name|RestConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|conf
operator|==
literal|null
condition|)
block|{
name|conf
operator|=
name|CamelContextHelper
operator|.
name|findByType
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|RestConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|conf
return|;
block|}
DECL|method|mergeConfigurations (RestConfiguration conf, RestConfiguration from)
specifier|private
name|RestConfiguration
name|mergeConfigurations
parameter_list|(
name|RestConfiguration
name|conf
parameter_list|,
name|RestConfiguration
name|from
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|conf
operator|==
name|from
condition|)
block|{
return|return
name|conf
return|;
block|}
if|if
condition|(
name|from
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
name|map
init|=
name|IntrospectionSupport
operator|.
name|getNonNullProperties
argument_list|(
name|from
argument_list|)
decl_stmt|;
comment|// Remove properties as they need to be manually managed
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|it
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|instanceof
name|Map
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
comment|// Copy common options, will override those in conf
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|conf
argument_list|,
name|map
argument_list|)
expr_stmt|;
comment|// Merge properties
name|mergeProperties
argument_list|(
name|conf
operator|::
name|getComponentProperties
argument_list|,
name|from
operator|::
name|getComponentProperties
argument_list|,
name|conf
operator|::
name|setComponentProperties
argument_list|)
expr_stmt|;
name|mergeProperties
argument_list|(
name|conf
operator|::
name|getEndpointProperties
argument_list|,
name|from
operator|::
name|getEndpointProperties
argument_list|,
name|conf
operator|::
name|setEndpointProperties
argument_list|)
expr_stmt|;
name|mergeProperties
argument_list|(
name|conf
operator|::
name|getConsumerProperties
argument_list|,
name|from
operator|::
name|getConsumerProperties
argument_list|,
name|conf
operator|::
name|setConsumerProperties
argument_list|)
expr_stmt|;
name|mergeProperties
argument_list|(
name|conf
operator|::
name|getDataFormatProperties
argument_list|,
name|from
operator|::
name|getDataFormatProperties
argument_list|,
name|conf
operator|::
name|setDataFormatProperties
argument_list|)
expr_stmt|;
name|mergeProperties
argument_list|(
name|conf
operator|::
name|getApiProperties
argument_list|,
name|from
operator|::
name|getApiProperties
argument_list|,
name|conf
operator|::
name|setApiProperties
argument_list|)
expr_stmt|;
name|mergeProperties
argument_list|(
name|conf
operator|::
name|getCorsHeaders
argument_list|,
name|from
operator|::
name|getCorsHeaders
argument_list|,
name|conf
operator|::
name|setCorsHeaders
argument_list|)
expr_stmt|;
block|}
return|return
name|conf
return|;
block|}
DECL|method|mergeProperties (Supplier<Map<String, T>> base, Supplier<Map<String, T>> addons, Consumer<Map<String, T>> consumer)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|void
name|mergeProperties
parameter_list|(
name|Supplier
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
argument_list|>
name|base
parameter_list|,
name|Supplier
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
argument_list|>
name|addons
parameter_list|,
name|Consumer
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
argument_list|>
name|consumer
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|baseMap
init|=
name|base
operator|.
name|get
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|addonsMap
init|=
name|addons
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|baseMap
operator|!=
literal|null
operator|||
name|addonsMap
operator|!=
literal|null
condition|)
block|{
name|HashMap
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|baseMap
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|putAll
argument_list|(
name|baseMap
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|addonsMap
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|putAll
argument_list|(
name|addonsMap
argument_list|)
expr_stmt|;
block|}
name|consumer
operator|.
name|accept
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getVerifier ()
specifier|public
name|ComponentVerifier
name|getVerifier
parameter_list|()
block|{
return|return
parameter_list|(
name|scope
parameter_list|,
name|parameters
parameter_list|)
lambda|->
name|getExtension
argument_list|(
name|ComponentVerifierExtension
operator|.
name|class
argument_list|)
operator|.
name|orElseThrow
argument_list|(
name|UnsupportedOperationException
operator|::
operator|new
argument_list|)
operator|.
name|verify
argument_list|(
name|scope
argument_list|,
name|parameters
argument_list|)
return|;
block|}
block|}
end_class

end_unit

