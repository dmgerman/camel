begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|bean
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|RuntimeCamelException
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
name|spring
operator|.
name|ws
operator|.
name|SpringWebserviceConsumer
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
name|spring
operator|.
name|ws
operator|.
name|SpringWebserviceEndpoint
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
name|spring
operator|.
name|ws
operator|.
name|type
operator|.
name|EndpointMappingKey
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|server
operator|.
name|endpoint
operator|.
name|MessageEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|addressing
operator|.
name|core
operator|.
name|MessageAddressingProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|addressing
operator|.
name|messageid
operator|.
name|MessageIdStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|addressing
operator|.
name|server
operator|.
name|AbstractAddressingEndpointMapping
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|addressing
operator|.
name|server
operator|.
name|AnnotationActionEndpointMapping
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|transport
operator|.
name|WebServiceMessageSender
import|;
end_import

begin_comment
comment|/**  * Provides support for full WS-Addressing. Supported are faultAction and  * response action. For more details look at @see  * {@link AbstractAddressingEndpointMapping}. Implementation of the  * {@link org.springframework.ws.server.EndpointMapping} consumer interface that  * uses the camel uri to map to a WS-Addressing {@code Action} header.  *<p/>  */
end_comment

begin_class
DECL|class|WSACamelEndpointMapping
specifier|public
class|class
name|WSACamelEndpointMapping
extends|extends
name|AbstractAddressingEndpointMapping
implements|implements
name|CamelSpringWSEndpointMapping
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
name|WSACamelEndpointMapping
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoints
specifier|private
name|Map
argument_list|<
name|EndpointMappingKey
argument_list|,
name|MessageEndpoint
argument_list|>
name|endpoints
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|outputActionSuffix
specifier|private
name|String
name|outputActionSuffix
init|=
name|AnnotationActionEndpointMapping
operator|.
name|DEFAULT_OUTPUT_ACTION_SUFFIX
decl_stmt|;
DECL|field|faultActionSuffix
specifier|private
name|String
name|faultActionSuffix
init|=
name|AnnotationActionEndpointMapping
operator|.
name|DEFAULT_OUTPUT_ACTION_SUFFIX
decl_stmt|;
annotation|@
name|Override
DECL|method|getEndpointInternal (MessageAddressingProperties map)
specifier|protected
name|Object
name|getEndpointInternal
parameter_list|(
name|MessageAddressingProperties
name|map
parameter_list|)
block|{
comment|// search the endpoint with compositeKeyFirst
for|for
control|(
name|EndpointMappingKey
name|key
range|:
name|endpoints
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|compositeOrSimpleKey
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|key
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|ACTION
case|:
name|compositeOrSimpleKey
operator|=
name|getActionCompositeLookupKey
argument_list|(
name|map
argument_list|)
expr_stmt|;
break|break;
case|case
name|TO
case|:
name|compositeOrSimpleKey
operator|=
name|getToCompositeLookupKey
argument_list|(
name|map
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Invalid mapping type specified. Supported types are: spring-ws:action:<WS-Addressing Action>(optional:<WS-Addressing To>?<params...>\n)"
operator|+
literal|"spring-ws:to:<WS-Addressing To>(optional:<WS-Addressing Action>?<params...>)"
argument_list|)
throw|;
block|}
comment|// lookup for specific endpoint
if|if
condition|(
name|compositeOrSimpleKey
operator|!=
literal|null
operator|&&
name|key
operator|.
name|getLookupKey
argument_list|()
operator|.
name|equals
argument_list|(
name|compositeOrSimpleKey
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found mapping for key {}"
argument_list|,
name|key
argument_list|)
expr_stmt|;
return|return
name|endpoints
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
comment|// look up for the simple key
for|for
control|(
name|EndpointMappingKey
name|key
range|:
name|endpoints
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|simpleKey
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|key
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|ACTION
case|:
if|if
condition|(
name|map
operator|.
name|getAction
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|simpleKey
operator|=
name|map
operator|.
name|getAction
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
break|break;
case|case
name|TO
case|:
if|if
condition|(
name|map
operator|.
name|getTo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|simpleKey
operator|=
name|map
operator|.
name|getTo
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
break|break;
default|default:
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Invalid mapping type specified. Supported types are: spring-ws:action:<WS-Addressing Action>(optional:<WS-Addressing To>?<params...>\n)"
operator|+
literal|"spring-ws:to:<WS-Addressing To>(optional:<WS-Addressing Action>?<params...>)"
argument_list|)
throw|;
block|}
comment|// look up for less specific endpoint
if|if
condition|(
name|simpleKey
operator|!=
literal|null
operator|&&
name|key
operator|.
name|getLookupKey
argument_list|()
operator|.
name|equals
argument_list|(
name|simpleKey
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found mapping for key {}"
argument_list|,
name|key
argument_list|)
expr_stmt|;
return|return
name|endpoints
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Generate a lookupKey for a given WS-Addressing message using action      * property. The possible combination are:      *<ul>      *<li>wsaAction</li>      *<li>wsaAction:wsaGetTo</li>      *</ul>      *       * @param map      * @return      */
DECL|method|getActionCompositeLookupKey (MessageAddressingProperties map)
specifier|protected
name|String
name|getActionCompositeLookupKey
parameter_list|(
name|MessageAddressingProperties
name|map
parameter_list|)
block|{
name|String
name|key
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|getAction
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|key
operator|=
name|map
operator|.
name|getAction
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|map
operator|.
name|getTo
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|key
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|key
operator|+=
literal|":"
expr_stmt|;
block|}
name|key
operator|+=
name|map
operator|.
name|getTo
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|key
return|;
block|}
comment|/**      * Generate a lookupKey for a given WS-Addressing message using getTo      * property. The possible combination are:      *<ul>      *<li>wsaGetTo</li>      *<li>wsaGetTo:wsaAction</li>      *</ul>      *       * @param map      * @return      */
DECL|method|getToCompositeLookupKey (MessageAddressingProperties map)
specifier|protected
name|String
name|getToCompositeLookupKey
parameter_list|(
name|MessageAddressingProperties
name|map
parameter_list|)
block|{
name|String
name|key
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|getTo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|key
operator|=
name|map
operator|.
name|getTo
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|map
operator|.
name|getAction
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|key
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|key
operator|+=
literal|":"
expr_stmt|;
block|}
name|key
operator|+=
name|map
operator|.
name|getAction
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|key
return|;
block|}
comment|/**      * Return output camel uri param or default action or null      */
annotation|@
name|Override
DECL|method|getResponseAction (Object endpoint, MessageAddressingProperties requestMap)
specifier|protected
name|URI
name|getResponseAction
parameter_list|(
name|Object
name|endpoint
parameter_list|,
name|MessageAddressingProperties
name|requestMap
parameter_list|)
block|{
name|SpringWebserviceEndpoint
name|camelEndpoint
init|=
name|getSpringWebserviceEndpoint
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|URI
name|actionUri
init|=
name|camelEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getOutputAction
argument_list|()
decl_stmt|;
if|if
condition|(
name|actionUri
operator|==
literal|null
condition|)
block|{
name|actionUri
operator|=
name|getDefaultResponseAction
argument_list|(
name|camelEndpoint
argument_list|,
name|requestMap
argument_list|)
expr_stmt|;
block|}
return|return
name|actionUri
return|;
block|}
comment|/**      * Configure message sender for wsa:replyTo from a camel route definition.      * The route definition has priority over this endpoint.      */
annotation|@
name|Override
DECL|method|getMessageSenders (Object endpoint)
specifier|protected
name|WebServiceMessageSender
index|[]
name|getMessageSenders
parameter_list|(
name|Object
name|endpoint
parameter_list|)
block|{
name|SpringWebserviceEndpoint
name|camelEndpoint
init|=
name|getSpringWebserviceEndpoint
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|camelEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMessageSender
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|WebServiceMessageSender
index|[]
block|{
name|camelEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMessageSender
argument_list|()
block|}
return|;
block|}
return|return
name|super
operator|.
name|getMessageSenders
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
comment|/**      * Configure message id strategy for wsa:replyTo The route definition has      * priority over this endpoint.      */
annotation|@
name|Override
DECL|method|getMessageIdStrategy (Object endpoint)
specifier|protected
name|MessageIdStrategy
name|getMessageIdStrategy
parameter_list|(
name|Object
name|endpoint
parameter_list|)
block|{
name|SpringWebserviceEndpoint
name|camelEndpoint
init|=
name|getSpringWebserviceEndpoint
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|camelEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMessageIdStrategy
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|camelEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMessageIdStrategy
argument_list|()
return|;
block|}
return|return
name|super
operator|.
name|getMessageIdStrategy
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
comment|/**      * return fault came uri param or default fault action or null      */
annotation|@
name|Override
DECL|method|getFaultAction (Object endpoint, MessageAddressingProperties requestMap)
specifier|protected
name|URI
name|getFaultAction
parameter_list|(
name|Object
name|endpoint
parameter_list|,
name|MessageAddressingProperties
name|requestMap
parameter_list|)
block|{
name|SpringWebserviceEndpoint
name|camelEndpoint
init|=
name|getSpringWebserviceEndpoint
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|URI
name|actionUri
init|=
name|camelEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFaultAction
argument_list|()
decl_stmt|;
if|if
condition|(
name|actionUri
operator|==
literal|null
condition|)
block|{
name|actionUri
operator|=
name|getDefaultFaultAction
argument_list|(
name|camelEndpoint
argument_list|,
name|requestMap
argument_list|)
expr_stmt|;
block|}
return|return
name|actionUri
return|;
block|}
DECL|method|getSpringWebserviceEndpoint (Object endpoint)
specifier|private
name|SpringWebserviceEndpoint
name|getSpringWebserviceEndpoint
parameter_list|(
name|Object
name|endpoint
parameter_list|)
block|{
name|Assert
operator|.
name|isInstanceOf
argument_list|(
name|SpringWebserviceConsumer
operator|.
name|class
argument_list|,
name|endpoint
argument_list|,
literal|"Endpoint needs to be an instance of SpringWebserviceConsumer"
argument_list|)
expr_stmt|;
name|SpringWebserviceConsumer
name|springWebserviceConsumer
init|=
operator|(
name|SpringWebserviceConsumer
operator|)
name|endpoint
decl_stmt|;
return|return
operator|(
name|SpringWebserviceEndpoint
operator|)
name|springWebserviceConsumer
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|getDefaultResponseAction (Object endpoint, MessageAddressingProperties requestMap)
specifier|protected
name|URI
name|getDefaultResponseAction
parameter_list|(
name|Object
name|endpoint
parameter_list|,
name|MessageAddressingProperties
name|requestMap
parameter_list|)
block|{
name|URI
name|requestAction
init|=
name|requestMap
operator|.
name|getAction
argument_list|()
decl_stmt|;
if|if
condition|(
name|requestAction
operator|!=
literal|null
condition|)
block|{
return|return
name|URI
operator|.
name|create
argument_list|(
name|requestAction
operator|.
name|toString
argument_list|()
operator|+
name|getOutputActionSuffix
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|getDefaultFaultAction (Object endpoint, MessageAddressingProperties requestMap)
specifier|protected
name|URI
name|getDefaultFaultAction
parameter_list|(
name|Object
name|endpoint
parameter_list|,
name|MessageAddressingProperties
name|requestMap
parameter_list|)
block|{
name|URI
name|requestAction
init|=
name|requestMap
operator|.
name|getAction
argument_list|()
decl_stmt|;
if|if
condition|(
name|requestAction
operator|!=
literal|null
condition|)
block|{
return|return
name|URI
operator|.
name|create
argument_list|(
name|requestAction
operator|.
name|toString
argument_list|()
operator|+
name|getFaultActionSuffix
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|addConsumer (EndpointMappingKey key, MessageEndpoint endpoint)
specifier|public
name|void
name|addConsumer
parameter_list|(
name|EndpointMappingKey
name|key
parameter_list|,
name|MessageEndpoint
name|endpoint
parameter_list|)
block|{
name|endpoints
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|removeConsumer (Object key)
specifier|public
name|void
name|removeConsumer
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|endpoints
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the suffix to add to request<code>Action</code>s for reply      * messages.      */
DECL|method|getOutputActionSuffix ()
specifier|public
name|String
name|getOutputActionSuffix
parameter_list|()
block|{
return|return
name|outputActionSuffix
return|;
block|}
comment|/**      * Sets the suffix to add to request<code>Action</code>s for reply      * messages.      *       * @see #DEFAULT_OUTPUT_ACTION_SUFFIX      */
DECL|method|setOutputActionSuffix (String outputActionSuffix)
specifier|public
name|void
name|setOutputActionSuffix
parameter_list|(
name|String
name|outputActionSuffix
parameter_list|)
block|{
name|Assert
operator|.
name|hasText
argument_list|(
name|outputActionSuffix
argument_list|,
literal|"'outputActionSuffix' must not be empty"
argument_list|)
expr_stmt|;
name|this
operator|.
name|outputActionSuffix
operator|=
name|outputActionSuffix
expr_stmt|;
block|}
comment|/**      * Returns the suffix to add to request<code>Action</code>s for reply fault      * messages.      */
DECL|method|getFaultActionSuffix ()
specifier|public
name|String
name|getFaultActionSuffix
parameter_list|()
block|{
return|return
name|faultActionSuffix
return|;
block|}
comment|/**      * Sets the suffix to add to request<code>Action</code>s for reply fault      * messages.      *       * @see #DEFAULT_FAULT_ACTION_SUFFIX      */
DECL|method|setFaultActionSuffix (String faultActionSuffix)
specifier|public
name|void
name|setFaultActionSuffix
parameter_list|(
name|String
name|faultActionSuffix
parameter_list|)
block|{
name|Assert
operator|.
name|hasText
argument_list|(
name|faultActionSuffix
argument_list|,
literal|"'faultActionSuffix' must not be empty"
argument_list|)
expr_stmt|;
name|this
operator|.
name|faultActionSuffix
operator|=
name|faultActionSuffix
expr_stmt|;
block|}
block|}
end_class

end_unit

