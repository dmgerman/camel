begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.routebox
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|routebox
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
name|ArrayList
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
name|ProducerTemplate
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
name|RouteBuilder
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
name|routebox
operator|.
name|strategy
operator|.
name|RouteboxDispatchStrategy
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
name|DefaultCamelContext
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
name|Registry
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
DECL|class|RouteboxConfiguration
specifier|public
class|class
name|RouteboxConfiguration
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RouteboxConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|uri
specifier|private
name|URI
name|uri
decl_stmt|;
DECL|field|authority
specifier|private
name|String
name|authority
decl_stmt|;
DECL|field|endpointName
specifier|private
name|String
name|endpointName
decl_stmt|;
DECL|field|consumerUri
specifier|private
name|URI
name|consumerUri
decl_stmt|;
DECL|field|producerUri
specifier|private
name|URI
name|producerUri
decl_stmt|;
DECL|field|dispatchStrategy
specifier|private
name|RouteboxDispatchStrategy
name|dispatchStrategy
decl_stmt|;
DECL|field|dispatchMap
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dispatchMap
decl_stmt|;
DECL|field|innerContext
specifier|private
name|CamelContext
name|innerContext
decl_stmt|;
DECL|field|routeBuilders
specifier|private
name|List
argument_list|<
name|RouteBuilder
argument_list|>
name|routeBuilders
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteBuilder
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|innerRegistry
specifier|private
name|Registry
name|innerRegistry
decl_stmt|;
DECL|field|forkContext
specifier|private
name|boolean
name|forkContext
init|=
literal|true
decl_stmt|;
DECL|field|local
specifier|private
name|boolean
name|local
init|=
literal|true
decl_stmt|;
DECL|field|connectionTimeout
specifier|private
name|long
name|connectionTimeout
init|=
literal|20000
decl_stmt|;
DECL|field|pollInterval
specifier|private
name|long
name|pollInterval
init|=
literal|1000
decl_stmt|;
DECL|field|innerProtocol
specifier|private
name|String
name|innerProtocol
decl_stmt|;
DECL|field|threads
specifier|private
name|int
name|threads
init|=
literal|20
decl_stmt|;
DECL|field|queueSize
specifier|private
name|int
name|queueSize
decl_stmt|;
DECL|field|innerProducerTemplate
specifier|private
name|ProducerTemplate
name|innerProducerTemplate
decl_stmt|;
DECL|field|sendToConsumer
specifier|private
name|boolean
name|sendToConsumer
init|=
literal|true
decl_stmt|;
DECL|method|RouteboxConfiguration ()
specifier|public
name|RouteboxConfiguration
parameter_list|()
block|{     }
DECL|method|RouteboxConfiguration (URI uri)
specifier|public
name|RouteboxConfiguration
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|parseURI (URI uri, Map<String, Object> parameters, RouteboxComponent component)
specifier|public
name|void
name|parseURI
parameter_list|(
name|URI
name|uri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|RouteboxComponent
name|component
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|protocol
init|=
name|uri
operator|.
name|getScheme
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"routebox"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unrecognized protocol: "
operator|+
name|protocol
operator|+
literal|" for uri: "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|setUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|setAuthority
argument_list|(
name|uri
operator|.
name|getAuthority
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Authority: "
operator|+
name|uri
operator|.
name|getAuthority
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|setEndpointName
argument_list|(
name|getAuthority
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"threads"
argument_list|)
condition|)
block|{
name|setThreads
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"threads"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"forkContext"
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
operator|(
name|Boolean
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"forkContext"
argument_list|)
argument_list|)
operator|)
condition|)
block|{
name|setForkContext
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"innerProtocol"
argument_list|)
condition|)
block|{
name|setInnerProtocol
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"innerProtocol"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
operator|!
name|innerProtocol
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"seda"
argument_list|)
operator|)
operator|&&
operator|(
operator|!
name|innerProtocol
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"direct"
argument_list|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unrecognized inner protocol: "
operator|+
name|innerProtocol
operator|+
literal|" for uri: "
operator|+
name|uri
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|setInnerProtocol
argument_list|(
literal|"direct"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"sendToConsumer"
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|Boolean
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"sendToConsumer"
argument_list|)
argument_list|)
condition|)
block|{
name|setSendToConsumer
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"connectionTimeout"
argument_list|)
condition|)
block|{
name|setConnectionTimeout
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"connectionTimeout"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"pollInterval"
argument_list|)
condition|)
block|{
name|setConnectionTimeout
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"pollInterval"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"routeBuilders"
argument_list|)
condition|)
block|{
name|routeBuilders
operator|=
operator|(
name|List
argument_list|<
name|RouteBuilder
argument_list|>
operator|)
name|component
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"routeBuilders"
argument_list|,
name|List
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"innerRegistry"
argument_list|)
condition|)
block|{
name|innerRegistry
operator|=
name|component
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"innerRegistry"
argument_list|,
name|Registry
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isForkContext
argument_list|()
condition|)
block|{
if|if
condition|(
name|innerRegistry
operator|!=
literal|null
condition|)
block|{
name|innerContext
operator|=
name|component
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"innerContext"
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|,
operator|new
name|DefaultCamelContext
argument_list|(
name|innerRegistry
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|innerContext
operator|=
name|component
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"innerContext"
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|,
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|innerContext
operator|=
name|component
operator|.
name|getCamelContext
argument_list|()
expr_stmt|;
block|}
name|innerProducerTemplate
operator|=
name|innerContext
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|setQueueSize
argument_list|(
name|component
operator|.
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"size"
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|consumerUri
operator|=
name|component
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"consumerUri"
argument_list|,
name|URI
operator|.
name|class
argument_list|,
operator|new
name|URI
argument_list|(
literal|"routebox:"
operator|+
name|getEndpointName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|producerUri
operator|=
name|component
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"producerUri"
argument_list|,
name|URI
operator|.
name|class
argument_list|,
operator|new
name|URI
argument_list|(
literal|"routebox:"
operator|+
name|getEndpointName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|dispatchStrategy
operator|=
name|component
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"dispatchStrategy"
argument_list|,
name|RouteboxDispatchStrategy
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|dispatchMap
operator|=
operator|(
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|component
operator|.
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"dispatchMap"
argument_list|,
name|HashMap
operator|.
name|class
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|dispatchStrategy
operator|==
literal|null
operator|&&
name|dispatchMap
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No Routebox Dispatch Map or Strategy has been set. Routebox may not have more than one inner route."
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getUri ()
specifier|public
name|URI
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|setUri (URI uri)
specifier|public
name|void
name|setUri
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|getAuthority ()
specifier|public
name|String
name|getAuthority
parameter_list|()
block|{
return|return
name|authority
return|;
block|}
DECL|method|setAuthority (String authority)
specifier|public
name|void
name|setAuthority
parameter_list|(
name|String
name|authority
parameter_list|)
block|{
name|this
operator|.
name|authority
operator|=
name|authority
expr_stmt|;
block|}
DECL|method|getInnerContext ()
specifier|public
name|CamelContext
name|getInnerContext
parameter_list|()
block|{
return|return
name|innerContext
return|;
block|}
DECL|method|setInnerContext (CamelContext innerContext)
specifier|public
name|void
name|setInnerContext
parameter_list|(
name|CamelContext
name|innerContext
parameter_list|)
block|{
name|this
operator|.
name|innerContext
operator|=
name|innerContext
expr_stmt|;
block|}
DECL|method|setRouteBuilders (List<RouteBuilder> routeBuilders)
specifier|public
name|void
name|setRouteBuilders
parameter_list|(
name|List
argument_list|<
name|RouteBuilder
argument_list|>
name|routeBuilders
parameter_list|)
block|{
name|this
operator|.
name|routeBuilders
operator|=
name|routeBuilders
expr_stmt|;
block|}
DECL|method|getRouteBuilders ()
specifier|public
name|List
argument_list|<
name|RouteBuilder
argument_list|>
name|getRouteBuilders
parameter_list|()
block|{
return|return
name|routeBuilders
return|;
block|}
DECL|method|setForkContext (boolean forkContext)
specifier|public
name|void
name|setForkContext
parameter_list|(
name|boolean
name|forkContext
parameter_list|)
block|{
name|this
operator|.
name|forkContext
operator|=
name|forkContext
expr_stmt|;
block|}
DECL|method|isForkContext ()
specifier|public
name|boolean
name|isForkContext
parameter_list|()
block|{
return|return
name|forkContext
return|;
block|}
DECL|method|setThreads (int threads)
specifier|public
name|void
name|setThreads
parameter_list|(
name|int
name|threads
parameter_list|)
block|{
name|this
operator|.
name|threads
operator|=
name|threads
expr_stmt|;
block|}
DECL|method|getThreads ()
specifier|public
name|int
name|getThreads
parameter_list|()
block|{
return|return
name|threads
return|;
block|}
DECL|method|setEndpointName (String endpointName)
specifier|public
name|void
name|setEndpointName
parameter_list|(
name|String
name|endpointName
parameter_list|)
block|{
name|this
operator|.
name|endpointName
operator|=
name|endpointName
expr_stmt|;
block|}
DECL|method|getEndpointName ()
specifier|public
name|String
name|getEndpointName
parameter_list|()
block|{
return|return
name|endpointName
return|;
block|}
DECL|method|setLocal (boolean local)
specifier|public
name|void
name|setLocal
parameter_list|(
name|boolean
name|local
parameter_list|)
block|{
name|this
operator|.
name|local
operator|=
name|local
expr_stmt|;
block|}
DECL|method|isLocal ()
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
name|local
return|;
block|}
DECL|method|setProducerUri (URI producerUri)
specifier|public
name|void
name|setProducerUri
parameter_list|(
name|URI
name|producerUri
parameter_list|)
block|{
name|this
operator|.
name|producerUri
operator|=
name|producerUri
expr_stmt|;
block|}
DECL|method|getProducerUri ()
specifier|public
name|URI
name|getProducerUri
parameter_list|()
block|{
return|return
name|producerUri
return|;
block|}
DECL|method|setConsumerUri (URI consumerUri)
specifier|public
name|void
name|setConsumerUri
parameter_list|(
name|URI
name|consumerUri
parameter_list|)
block|{
name|this
operator|.
name|consumerUri
operator|=
name|consumerUri
expr_stmt|;
block|}
DECL|method|getConsumerUri ()
specifier|public
name|URI
name|getConsumerUri
parameter_list|()
block|{
return|return
name|consumerUri
return|;
block|}
DECL|method|setDispatchStrategy (RouteboxDispatchStrategy dispatchStrategy)
specifier|public
name|void
name|setDispatchStrategy
parameter_list|(
name|RouteboxDispatchStrategy
name|dispatchStrategy
parameter_list|)
block|{
name|this
operator|.
name|dispatchStrategy
operator|=
name|dispatchStrategy
expr_stmt|;
block|}
DECL|method|getDispatchStrategy ()
specifier|public
name|RouteboxDispatchStrategy
name|getDispatchStrategy
parameter_list|()
block|{
return|return
name|dispatchStrategy
return|;
block|}
DECL|method|setConnectionTimeout (long connectionTimeout)
specifier|public
name|void
name|setConnectionTimeout
parameter_list|(
name|long
name|connectionTimeout
parameter_list|)
block|{
name|this
operator|.
name|connectionTimeout
operator|=
name|connectionTimeout
expr_stmt|;
block|}
DECL|method|getConnectionTimeout ()
specifier|public
name|long
name|getConnectionTimeout
parameter_list|()
block|{
return|return
name|connectionTimeout
return|;
block|}
DECL|method|getPollInterval ()
specifier|public
name|long
name|getPollInterval
parameter_list|()
block|{
return|return
name|pollInterval
return|;
block|}
DECL|method|setPollInterval (long pollInterval)
specifier|public
name|void
name|setPollInterval
parameter_list|(
name|long
name|pollInterval
parameter_list|)
block|{
name|this
operator|.
name|pollInterval
operator|=
name|pollInterval
expr_stmt|;
block|}
DECL|method|setQueueSize (int queueSize)
specifier|public
name|void
name|setQueueSize
parameter_list|(
name|int
name|queueSize
parameter_list|)
block|{
name|this
operator|.
name|queueSize
operator|=
name|queueSize
expr_stmt|;
block|}
DECL|method|getQueueSize ()
specifier|public
name|int
name|getQueueSize
parameter_list|()
block|{
return|return
name|queueSize
return|;
block|}
DECL|method|setInnerProducerTemplate (ProducerTemplate innerProducerTemplate)
specifier|public
name|void
name|setInnerProducerTemplate
parameter_list|(
name|ProducerTemplate
name|innerProducerTemplate
parameter_list|)
block|{
name|this
operator|.
name|innerProducerTemplate
operator|=
name|innerProducerTemplate
expr_stmt|;
block|}
DECL|method|getInnerProducerTemplate ()
specifier|public
name|ProducerTemplate
name|getInnerProducerTemplate
parameter_list|()
block|{
return|return
name|innerProducerTemplate
return|;
block|}
DECL|method|setInnerProtocol (String innerProtocol)
specifier|public
name|void
name|setInnerProtocol
parameter_list|(
name|String
name|innerProtocol
parameter_list|)
block|{
name|this
operator|.
name|innerProtocol
operator|=
name|innerProtocol
expr_stmt|;
block|}
DECL|method|getInnerProtocol ()
specifier|public
name|String
name|getInnerProtocol
parameter_list|()
block|{
return|return
name|innerProtocol
return|;
block|}
DECL|method|setInnerRegistry (Registry innerRegistry)
specifier|public
name|void
name|setInnerRegistry
parameter_list|(
name|Registry
name|innerRegistry
parameter_list|)
block|{
name|this
operator|.
name|innerRegistry
operator|=
name|innerRegistry
expr_stmt|;
block|}
DECL|method|getInnerRegistry ()
specifier|public
name|Registry
name|getInnerRegistry
parameter_list|()
block|{
return|return
name|innerRegistry
return|;
block|}
DECL|method|setSendToConsumer (boolean sendToConsumer)
specifier|public
name|void
name|setSendToConsumer
parameter_list|(
name|boolean
name|sendToConsumer
parameter_list|)
block|{
name|this
operator|.
name|sendToConsumer
operator|=
name|sendToConsumer
expr_stmt|;
block|}
DECL|method|isSendToConsumer ()
specifier|public
name|boolean
name|isSendToConsumer
parameter_list|()
block|{
return|return
name|sendToConsumer
return|;
block|}
DECL|method|setDispatchMap (Map<String, String> dispatchMap)
specifier|public
name|void
name|setDispatchMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dispatchMap
parameter_list|)
block|{
name|this
operator|.
name|dispatchMap
operator|=
name|dispatchMap
expr_stmt|;
block|}
DECL|method|getDispatchMap ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getDispatchMap
parameter_list|()
block|{
return|return
name|dispatchMap
return|;
block|}
block|}
end_class

end_unit

