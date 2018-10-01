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
name|Collection
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
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|ConcurrentMap
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
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|MessageHistory
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
name|InflightRepository
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
name|ServiceSupport
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

begin_comment
comment|/**  * Default {@link org.apache.camel.spi.InflightRepository}.  */
end_comment

begin_class
DECL|class|DefaultInflightRepository
specifier|public
class|class
name|DefaultInflightRepository
extends|extends
name|ServiceSupport
implements|implements
name|InflightRepository
block|{
DECL|field|inflight
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|Exchange
argument_list|>
name|inflight
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|routeCount
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|AtomicInteger
argument_list|>
name|routeCount
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|add (Exchange exchange)
specifier|public
name|void
name|add
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|inflight
operator|.
name|put
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|remove (Exchange exchange)
specifier|public
name|void
name|remove
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|inflight
operator|.
name|remove
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|add (Exchange exchange, String routeId)
specifier|public
name|void
name|add
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|routeId
parameter_list|)
block|{
name|AtomicInteger
name|existing
init|=
name|routeCount
operator|.
name|get
argument_list|(
name|routeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
operator|!=
literal|null
condition|)
block|{
name|existing
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|remove (Exchange exchange, String routeId)
specifier|public
name|void
name|remove
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|routeId
parameter_list|)
block|{
name|AtomicInteger
name|existing
init|=
name|routeCount
operator|.
name|get
argument_list|(
name|routeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
operator|!=
literal|null
condition|)
block|{
name|existing
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|inflight
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|addRoute (String routeId)
specifier|public
name|void
name|addRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
block|{
name|routeCount
operator|.
name|putIfAbsent
argument_list|(
name|routeId
argument_list|,
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|removeRoute (String routeId)
specifier|public
name|void
name|removeRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
block|{
name|routeCount
operator|.
name|remove
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|size (String routeId)
specifier|public
name|int
name|size
parameter_list|(
name|String
name|routeId
parameter_list|)
block|{
name|AtomicInteger
name|existing
init|=
name|routeCount
operator|.
name|get
argument_list|(
name|routeId
argument_list|)
decl_stmt|;
return|return
name|existing
operator|!=
literal|null
condition|?
name|existing
operator|.
name|get
argument_list|()
else|:
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|browse ()
specifier|public
name|Collection
argument_list|<
name|InflightExchange
argument_list|>
name|browse
parameter_list|()
block|{
return|return
name|browse
argument_list|(
literal|null
argument_list|,
operator|-
literal|1
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|browse (String fromRouteId)
specifier|public
name|Collection
argument_list|<
name|InflightExchange
argument_list|>
name|browse
parameter_list|(
name|String
name|fromRouteId
parameter_list|)
block|{
return|return
name|browse
argument_list|(
name|fromRouteId
argument_list|,
operator|-
literal|1
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|browse (int limit, boolean sortByLongestDuration)
specifier|public
name|Collection
argument_list|<
name|InflightExchange
argument_list|>
name|browse
parameter_list|(
name|int
name|limit
parameter_list|,
name|boolean
name|sortByLongestDuration
parameter_list|)
block|{
return|return
name|browse
argument_list|(
literal|null
argument_list|,
name|limit
argument_list|,
name|sortByLongestDuration
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|browse (String fromRouteId, int limit, boolean sortByLongestDuration)
specifier|public
name|Collection
argument_list|<
name|InflightExchange
argument_list|>
name|browse
parameter_list|(
name|String
name|fromRouteId
parameter_list|,
name|int
name|limit
parameter_list|,
name|boolean
name|sortByLongestDuration
parameter_list|)
block|{
name|Stream
argument_list|<
name|Exchange
argument_list|>
name|values
decl_stmt|;
if|if
condition|(
name|fromRouteId
operator|==
literal|null
condition|)
block|{
comment|// all values
name|values
operator|=
name|inflight
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// only if route match
name|values
operator|=
name|inflight
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|e
lambda|->
name|fromRouteId
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getFromRouteId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sortByLongestDuration
condition|)
block|{
comment|// sort by duration and grab the first
name|values
operator|=
name|values
operator|.
name|sorted
argument_list|(
parameter_list|(
name|e1
parameter_list|,
name|e2
parameter_list|)
lambda|->
block|{
name|long
name|d1
init|=
name|getExchangeDuration
argument_list|(
name|e1
argument_list|)
decl_stmt|;
name|long
name|d2
init|=
name|getExchangeDuration
argument_list|(
name|e2
argument_list|)
decl_stmt|;
comment|// need the biggest number first
return|return
operator|-
literal|1
operator|*
name|Long
operator|.
name|compare
argument_list|(
name|d1
argument_list|,
name|d2
argument_list|)
return|;
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// else sort by exchange id
name|values
operator|=
name|values
operator|.
name|sorted
argument_list|(
name|Comparator
operator|.
name|comparing
argument_list|(
name|Exchange
operator|::
name|getExchangeId
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|limit
operator|>
literal|0
condition|)
block|{
name|values
operator|=
name|values
operator|.
name|limit
argument_list|(
name|limit
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|InflightExchange
argument_list|>
name|answer
init|=
name|values
operator|.
name|map
argument_list|(
name|InflightExchangeEntry
operator|::
operator|new
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|answer
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|oldest (String fromRouteId)
specifier|public
name|InflightExchange
name|oldest
parameter_list|(
name|String
name|fromRouteId
parameter_list|)
block|{
name|Stream
argument_list|<
name|Exchange
argument_list|>
name|values
decl_stmt|;
if|if
condition|(
name|fromRouteId
operator|==
literal|null
condition|)
block|{
comment|// all values
name|values
operator|=
name|inflight
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// only if route match
name|values
operator|=
name|inflight
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|e
lambda|->
name|fromRouteId
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getFromRouteId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// sort by duration and grab the first
name|Exchange
name|first
init|=
name|values
operator|.
name|sorted
argument_list|(
parameter_list|(
name|e1
parameter_list|,
name|e2
parameter_list|)
lambda|->
block|{
name|long
name|d1
init|=
name|getExchangeDuration
argument_list|(
name|e1
argument_list|)
decl_stmt|;
name|long
name|d2
init|=
name|getExchangeDuration
argument_list|(
name|e2
argument_list|)
decl_stmt|;
comment|// need the biggest number first
return|return
operator|-
literal|1
operator|*
name|Long
operator|.
name|compare
argument_list|(
name|d1
argument_list|,
name|d2
argument_list|)
return|;
block|}
argument_list|)
operator|.
name|findFirst
argument_list|()
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|first
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|InflightExchangeEntry
argument_list|(
name|first
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
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{     }
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
name|int
name|count
init|=
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|count
operator|>
literal|0
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Shutting down while there are still {} inflight exchanges."
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Shutting down with no inflight exchanges."
argument_list|)
expr_stmt|;
block|}
name|routeCount
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|getExchangeDuration (Exchange exchange)
specifier|private
specifier|static
name|long
name|getExchangeDuration
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|long
name|duration
init|=
literal|0
decl_stmt|;
name|Date
name|created
init|=
name|exchange
operator|.
name|getCreated
argument_list|()
decl_stmt|;
if|if
condition|(
name|created
operator|!=
literal|null
condition|)
block|{
name|duration
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|created
operator|.
name|getTime
argument_list|()
expr_stmt|;
block|}
return|return
name|duration
return|;
block|}
DECL|class|InflightExchangeEntry
specifier|private
specifier|static
specifier|final
class|class
name|InflightExchangeEntry
implements|implements
name|InflightExchange
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|method|InflightExchangeEntry (Exchange exchange)
specifier|private
name|InflightExchangeEntry
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getExchange ()
specifier|public
name|Exchange
name|getExchange
parameter_list|()
block|{
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
DECL|method|getDuration ()
specifier|public
name|long
name|getDuration
parameter_list|()
block|{
return|return
name|DefaultInflightRepository
operator|.
name|getExchangeDuration
argument_list|(
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getElapsed ()
specifier|public
name|long
name|getElapsed
parameter_list|()
block|{
name|LinkedList
argument_list|<
name|MessageHistory
argument_list|>
name|list
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|MESSAGE_HISTORY
argument_list|,
name|LinkedList
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
operator|||
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|0
return|;
block|}
comment|// get latest entry
name|MessageHistory
name|history
init|=
name|list
operator|.
name|getLast
argument_list|()
decl_stmt|;
if|if
condition|(
name|history
operator|!=
literal|null
condition|)
block|{
return|return
name|history
operator|.
name|getElapsed
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getNodeId ()
specifier|public
name|String
name|getNodeId
parameter_list|()
block|{
name|LinkedList
argument_list|<
name|MessageHistory
argument_list|>
name|list
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|MESSAGE_HISTORY
argument_list|,
name|LinkedList
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
operator|||
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// get latest entry
name|MessageHistory
name|history
init|=
name|list
operator|.
name|getLast
argument_list|()
decl_stmt|;
if|if
condition|(
name|history
operator|!=
literal|null
condition|)
block|{
return|return
name|history
operator|.
name|getNode
argument_list|()
operator|.
name|getId
argument_list|()
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
DECL|method|getFromRouteId ()
specifier|public
name|String
name|getFromRouteId
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getFromRouteId
argument_list|()
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getAtRouteId ()
specifier|public
name|String
name|getAtRouteId
parameter_list|()
block|{
name|LinkedList
argument_list|<
name|MessageHistory
argument_list|>
name|list
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|MESSAGE_HISTORY
argument_list|,
name|LinkedList
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
operator|||
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// get latest entry
name|MessageHistory
name|history
init|=
name|list
operator|.
name|getLast
argument_list|()
decl_stmt|;
if|if
condition|(
name|history
operator|!=
literal|null
condition|)
block|{
return|return
name|history
operator|.
name|getRouteId
argument_list|()
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
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"InflightExchangeEntry[exchangeId="
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
block|}
end_class

end_unit

