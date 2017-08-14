begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventObject
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
name|HashSet
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
name|CamelContextAware
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
name|management
operator|.
name|event
operator|.
name|ExchangeCreatedEvent
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
name|management
operator|.
name|event
operator|.
name|ExchangeSendingEvent
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
name|management
operator|.
name|event
operator|.
name|RouteAddedEvent
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
name|management
operator|.
name|event
operator|.
name|RouteRemovedEvent
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
name|EndpointUtilizationStatistics
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
name|RouteContext
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
name|RuntimeEndpointRegistry
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
name|UnitOfWork
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
name|EventNotifierSupport
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
name|LRUCacheFactory
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

begin_class
DECL|class|DefaultRuntimeEndpointRegistry
specifier|public
class|class
name|DefaultRuntimeEndpointRegistry
extends|extends
name|EventNotifierSupport
implements|implements
name|CamelContextAware
implements|,
name|RuntimeEndpointRegistry
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
comment|// route id -> endpoint urls
DECL|field|inputs
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|inputs
decl_stmt|;
DECL|field|outputs
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|outputs
decl_stmt|;
DECL|field|limit
specifier|private
name|int
name|limit
init|=
literal|1000
decl_stmt|;
DECL|field|enabled
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
DECL|field|extended
specifier|private
specifier|volatile
name|boolean
name|extended
decl_stmt|;
DECL|field|inputUtilization
specifier|private
name|EndpointUtilizationStatistics
name|inputUtilization
decl_stmt|;
DECL|field|outputUtilization
specifier|private
name|EndpointUtilizationStatistics
name|outputUtilization
decl_stmt|;
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getAllEndpoints (boolean includeInputs)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getAllEndpoints
parameter_list|(
name|boolean
name|includeInputs
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|includeInputs
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|inputs
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|answer
operator|.
name|addAll
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|outputs
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|answer
operator|.
name|addAll
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|answer
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpointsPerRoute (String routeId, boolean includeInputs)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getEndpointsPerRoute
parameter_list|(
name|String
name|routeId
parameter_list|,
name|boolean
name|includeInputs
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|includeInputs
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|uris
init|=
name|inputs
operator|.
name|get
argument_list|(
name|routeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|uris
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|addAll
argument_list|(
name|uris
argument_list|)
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|uris
init|=
name|outputs
operator|.
name|get
argument_list|(
name|routeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|uris
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|addAll
argument_list|(
name|uris
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|answer
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpointStatistics ()
specifier|public
name|List
argument_list|<
name|Statistic
argument_list|>
name|getEndpointStatistics
parameter_list|()
block|{
name|List
argument_list|<
name|Statistic
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Statistic
argument_list|>
argument_list|()
decl_stmt|;
comment|// inputs
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|inputs
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|routeId
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|uri
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|Long
name|hits
init|=
literal|0L
decl_stmt|;
if|if
condition|(
name|extended
condition|)
block|{
name|String
name|key
init|=
name|asUtilizationKey
argument_list|(
name|routeId
argument_list|,
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|hits
operator|=
name|inputUtilization
operator|.
name|getStatistics
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|hits
operator|==
literal|null
condition|)
block|{
name|hits
operator|=
literal|0L
expr_stmt|;
block|}
block|}
block|}
name|answer
operator|.
name|add
argument_list|(
operator|new
name|EndpointRuntimeStatistics
argument_list|(
name|uri
argument_list|,
name|routeId
argument_list|,
literal|"in"
argument_list|,
name|hits
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// outputs
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|outputs
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|routeId
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|uri
range|:
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Long
name|hits
init|=
literal|0L
decl_stmt|;
if|if
condition|(
name|extended
condition|)
block|{
name|String
name|key
init|=
name|asUtilizationKey
argument_list|(
name|routeId
argument_list|,
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|hits
operator|=
name|outputUtilization
operator|.
name|getStatistics
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|hits
operator|==
literal|null
condition|)
block|{
name|hits
operator|=
literal|0L
expr_stmt|;
block|}
block|}
block|}
name|answer
operator|.
name|add
argument_list|(
operator|new
name|EndpointRuntimeStatistics
argument_list|(
name|uri
argument_list|,
name|routeId
argument_list|,
literal|"out"
argument_list|,
name|hits
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|getLimit ()
specifier|public
name|int
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
annotation|@
name|Override
DECL|method|setLimit (int limit)
specifier|public
name|void
name|setLimit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|inputs
operator|.
name|clear
argument_list|()
expr_stmt|;
name|outputs
operator|.
name|clear
argument_list|()
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
comment|// its safe to call clear as reset
if|if
condition|(
name|inputUtilization
operator|!=
literal|null
condition|)
block|{
name|inputUtilization
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|outputUtilization
operator|!=
literal|null
condition|)
block|{
name|outputUtilization
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
name|int
name|total
init|=
name|inputs
operator|.
name|values
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|total
operator|+=
name|outputs
operator|.
name|values
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
return|return
name|total
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|inputs
operator|==
literal|null
condition|)
block|{
name|inputs
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|outputs
operator|==
literal|null
condition|)
block|{
name|outputs
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|getCamelContext
argument_list|()
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|extended
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getStatisticsLevel
argument_list|()
operator|.
name|isExtended
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|extended
condition|)
block|{
name|inputUtilization
operator|=
operator|new
name|DefaultEndpointUtilizationStatistics
argument_list|(
name|limit
argument_list|)
expr_stmt|;
name|outputUtilization
operator|=
operator|new
name|DefaultEndpointUtilizationStatistics
argument_list|(
name|limit
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|extended
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Runtime endpoint registry is in extended mode gathering usage statistics of all incoming and outgoing endpoints (cache limit: {})"
argument_list|,
name|limit
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Runtime endpoint registry is in normal mode gathering information of all incoming and outgoing endpoints (cache limit: {})"
argument_list|,
name|limit
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|inputUtilization
argument_list|,
name|outputUtilization
argument_list|)
expr_stmt|;
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
name|clear
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|inputUtilization
argument_list|,
name|outputUtilization
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|notify (EventObject event)
specifier|public
name|void
name|notify
parameter_list|(
name|EventObject
name|event
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|event
operator|instanceof
name|RouteAddedEvent
condition|)
block|{
name|RouteAddedEvent
name|rse
init|=
operator|(
name|RouteAddedEvent
operator|)
name|event
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|rse
operator|.
name|getRoute
argument_list|()
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|routeId
init|=
name|rse
operator|.
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
comment|// a HashSet is fine for inputs as we only have a limited number of those
name|Set
argument_list|<
name|String
argument_list|>
name|uris
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|uris
operator|.
name|add
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|inputs
operator|.
name|put
argument_list|(
name|routeId
argument_list|,
name|uris
argument_list|)
expr_stmt|;
comment|// use a LRUCache for outputs as we could potential have unlimited uris if dynamic routing is in use
comment|// and therefore need to have the limit in use
name|outputs
operator|.
name|put
argument_list|(
name|routeId
argument_list|,
name|LRUCacheFactory
operator|.
name|newLRUCache
argument_list|(
name|limit
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|instanceof
name|RouteRemovedEvent
condition|)
block|{
name|RouteRemovedEvent
name|rse
init|=
operator|(
name|RouteRemovedEvent
operator|)
name|event
decl_stmt|;
name|String
name|routeId
init|=
name|rse
operator|.
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|inputs
operator|.
name|remove
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
name|outputs
operator|.
name|remove
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
if|if
condition|(
name|extended
condition|)
block|{
name|String
name|uri
init|=
name|rse
operator|.
name|getRoute
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|asUtilizationKey
argument_list|(
name|routeId
argument_list|,
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|inputUtilization
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|extended
operator|&&
name|event
operator|instanceof
name|ExchangeCreatedEvent
condition|)
block|{
comment|// we only capture details in extended mode
name|ExchangeCreatedEvent
name|ece
init|=
operator|(
name|ExchangeCreatedEvent
operator|)
name|event
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|ece
operator|.
name|getExchange
argument_list|()
operator|.
name|getFromEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|String
name|routeId
init|=
name|ece
operator|.
name|getExchange
argument_list|()
operator|.
name|getFromRouteId
argument_list|()
decl_stmt|;
name|String
name|uri
init|=
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|asUtilizationKey
argument_list|(
name|routeId
argument_list|,
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|inputUtilization
operator|.
name|onHit
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|event
operator|instanceof
name|ExchangeSendingEvent
condition|)
block|{
name|ExchangeSendingEvent
name|ese
init|=
operator|(
name|ExchangeSendingEvent
operator|)
name|event
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|ese
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|routeId
init|=
name|getRouteId
argument_list|(
name|ese
operator|.
name|getExchange
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|uri
init|=
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|uris
init|=
name|outputs
operator|.
name|get
argument_list|(
name|routeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|uris
operator|!=
literal|null
operator|&&
operator|!
name|uris
operator|.
name|containsKey
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|uris
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|extended
condition|)
block|{
name|String
name|key
init|=
name|asUtilizationKey
argument_list|(
name|routeId
argument_list|,
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|outputUtilization
operator|.
name|onHit
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|getRouteId (Exchange exchange)
specifier|private
name|String
name|getRouteId
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|answer
init|=
literal|null
decl_stmt|;
name|UnitOfWork
name|uow
init|=
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
decl_stmt|;
name|RouteContext
name|rc
init|=
name|uow
operator|!=
literal|null
condition|?
name|uow
operator|.
name|getRouteContext
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|rc
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|rc
operator|.
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// fallback and get from route id on the exchange
name|answer
operator|=
name|exchange
operator|.
name|getFromRouteId
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|isDisabled ()
specifier|public
name|boolean
name|isDisabled
parameter_list|()
block|{
return|return
operator|!
name|enabled
return|;
block|}
annotation|@
name|Override
DECL|method|isEnabled (EventObject event)
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|EventObject
name|event
parameter_list|)
block|{
return|return
name|enabled
operator|&&
name|event
operator|instanceof
name|ExchangeCreatedEvent
operator|||
name|event
operator|instanceof
name|ExchangeSendingEvent
operator|||
name|event
operator|instanceof
name|RouteAddedEvent
operator|||
name|event
operator|instanceof
name|RouteRemovedEvent
return|;
block|}
DECL|method|asUtilizationKey (String routeId, String uri)
specifier|private
specifier|static
name|String
name|asUtilizationKey
parameter_list|(
name|String
name|routeId
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
name|routeId
operator|==
literal|null
operator|||
name|uri
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|routeId
operator|+
literal|"|"
operator|+
name|uri
return|;
block|}
block|}
DECL|class|EndpointRuntimeStatistics
specifier|private
specifier|static
specifier|final
class|class
name|EndpointRuntimeStatistics
implements|implements
name|Statistic
block|{
DECL|field|uri
specifier|private
specifier|final
name|String
name|uri
decl_stmt|;
DECL|field|routeId
specifier|private
specifier|final
name|String
name|routeId
decl_stmt|;
DECL|field|direction
specifier|private
specifier|final
name|String
name|direction
decl_stmt|;
DECL|field|hits
specifier|private
specifier|final
name|long
name|hits
decl_stmt|;
DECL|method|EndpointRuntimeStatistics (String uri, String routeId, String direction, long hits)
specifier|private
name|EndpointRuntimeStatistics
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|routeId
parameter_list|,
name|String
name|direction
parameter_list|,
name|long
name|hits
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
name|this
operator|.
name|routeId
operator|=
name|routeId
expr_stmt|;
name|this
operator|.
name|direction
operator|=
name|direction
expr_stmt|;
name|this
operator|.
name|hits
operator|=
name|hits
expr_stmt|;
block|}
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|getRouteId ()
specifier|public
name|String
name|getRouteId
parameter_list|()
block|{
return|return
name|routeId
return|;
block|}
DECL|method|getDirection ()
specifier|public
name|String
name|getDirection
parameter_list|()
block|{
return|return
name|direction
return|;
block|}
DECL|method|getHits ()
specifier|public
name|long
name|getHits
parameter_list|()
block|{
return|return
name|hits
return|;
block|}
block|}
block|}
end_class

end_unit

