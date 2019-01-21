begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcache.processor.aggregate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcache
operator|.
name|processor
operator|.
name|aggregate
package|;
end_package

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
name|HashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|Cache
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
name|component
operator|.
name|jcache
operator|.
name|JCacheConfiguration
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
name|jcache
operator|.
name|JCacheHelper
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
name|jcache
operator|.
name|JCacheManager
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
name|DefaultExchangeHolder
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
name|OptimisticLockingAggregationRepository
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
name|DefaultExchange
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
name|ServiceSupport
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

begin_class
DECL|class|JCacheAggregationRepository
specifier|public
class|class
name|JCacheAggregationRepository
extends|extends
name|ServiceSupport
implements|implements
name|OptimisticLockingAggregationRepository
block|{
DECL|field|configuration
specifier|private
name|JCacheConfiguration
name|configuration
decl_stmt|;
DECL|field|cache
specifier|private
name|Cache
argument_list|<
name|String
argument_list|,
name|DefaultExchangeHolder
argument_list|>
name|cache
decl_stmt|;
DECL|field|optimistic
specifier|private
name|boolean
name|optimistic
decl_stmt|;
DECL|field|allowSerializedHeaders
specifier|private
name|boolean
name|allowSerializedHeaders
decl_stmt|;
DECL|field|cacheManager
specifier|private
name|JCacheManager
argument_list|<
name|String
argument_list|,
name|DefaultExchangeHolder
argument_list|>
name|cacheManager
decl_stmt|;
DECL|method|JCacheAggregationRepository ()
specifier|public
name|JCacheAggregationRepository
parameter_list|()
block|{
name|this
operator|.
name|configuration
operator|=
operator|new
name|JCacheConfiguration
argument_list|()
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|JCacheConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (JCacheConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|JCacheConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getCacheName ()
specifier|public
name|String
name|getCacheName
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getCacheName
argument_list|()
return|;
block|}
DECL|method|setCacheName (String cacheName)
specifier|public
name|void
name|setCacheName
parameter_list|(
name|String
name|cacheName
parameter_list|)
block|{
name|configuration
operator|.
name|setCacheName
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
block|}
DECL|method|getCache ()
specifier|public
name|Cache
argument_list|<
name|String
argument_list|,
name|DefaultExchangeHolder
argument_list|>
name|getCache
parameter_list|()
block|{
return|return
name|cache
return|;
block|}
DECL|method|setCache (Cache<String, DefaultExchangeHolder> cache)
specifier|public
name|void
name|setCache
parameter_list|(
name|Cache
argument_list|<
name|String
argument_list|,
name|DefaultExchangeHolder
argument_list|>
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
DECL|method|isOptimistic ()
specifier|public
name|boolean
name|isOptimistic
parameter_list|()
block|{
return|return
name|optimistic
return|;
block|}
DECL|method|setOptimistic (boolean optimistic)
specifier|public
name|void
name|setOptimistic
parameter_list|(
name|boolean
name|optimistic
parameter_list|)
block|{
name|this
operator|.
name|optimistic
operator|=
name|optimistic
expr_stmt|;
block|}
DECL|method|isAllowSerializedHeaders ()
specifier|public
name|boolean
name|isAllowSerializedHeaders
parameter_list|()
block|{
return|return
name|allowSerializedHeaders
return|;
block|}
DECL|method|setAllowSerializedHeaders (boolean allowSerializedHeaders)
specifier|public
name|void
name|setAllowSerializedHeaders
parameter_list|(
name|boolean
name|allowSerializedHeaders
parameter_list|)
block|{
name|this
operator|.
name|allowSerializedHeaders
operator|=
name|allowSerializedHeaders
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|add (CamelContext camelContext, String key, Exchange oldExchange, Exchange newExchange)
specifier|public
name|Exchange
name|add
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|,
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
throws|throws
name|OptimisticLockingException
block|{
if|if
condition|(
operator|!
name|optimistic
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Adding an Exchange with ID {} for key {} in an optimistic manner."
argument_list|,
name|newExchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
name|DefaultExchangeHolder
name|newHolder
init|=
name|DefaultExchangeHolder
operator|.
name|marshal
argument_list|(
name|newExchange
argument_list|,
literal|true
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
name|DefaultExchangeHolder
name|oldHolder
init|=
name|cache
operator|.
name|getAndPut
argument_list|(
name|key
argument_list|,
name|newHolder
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldHolder
operator|!=
literal|null
condition|)
block|{
name|Exchange
name|exchange
init|=
name|unmarshallExchange
argument_list|(
name|camelContext
argument_list|,
name|oldHolder
argument_list|)
decl_stmt|;
name|log
operator|.
name|error
argument_list|(
literal|"Optimistic locking failed for exchange with key {}: IMap#putIfAbsend returned Exchange with ID {}, while it's expected no exchanges to be returned"
argument_list|,
name|key
argument_list|,
name|exchange
operator|!=
literal|null
condition|?
name|exchange
operator|.
name|getExchangeId
argument_list|()
else|:
literal|"<null>"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|OptimisticLockingException
argument_list|()
throw|;
block|}
block|}
else|else
block|{
name|DefaultExchangeHolder
name|oldHolder
init|=
name|DefaultExchangeHolder
operator|.
name|marshal
argument_list|(
name|oldExchange
argument_list|,
literal|true
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
name|DefaultExchangeHolder
name|newHolder
init|=
name|DefaultExchangeHolder
operator|.
name|marshal
argument_list|(
name|newExchange
argument_list|,
literal|true
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|cache
operator|.
name|replace
argument_list|(
name|key
argument_list|,
name|oldHolder
argument_list|,
name|newHolder
argument_list|)
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Optimistic locking failed for exchange with key {}: IMap#replace returned no Exchanges, while it's expected to replace one"
argument_list|,
name|key
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|OptimisticLockingException
argument_list|()
throw|;
block|}
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Added an Exchange with ID {} for key {} in optimistic manner."
argument_list|,
name|newExchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
return|return
name|oldExchange
return|;
block|}
annotation|@
name|Override
DECL|method|add (CamelContext camelContext, String key, Exchange exchange)
specifier|public
name|Exchange
name|add
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|optimistic
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Adding an Exchange with ID {} for key {} in a thread-safe manner."
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|DefaultExchangeHolder
name|newHolder
init|=
name|DefaultExchangeHolder
operator|.
name|marshal
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
name|DefaultExchangeHolder
name|oldHolder
init|=
name|cache
operator|.
name|getAndPut
argument_list|(
name|key
argument_list|,
name|newHolder
argument_list|)
decl_stmt|;
return|return
name|unmarshallExchange
argument_list|(
name|camelContext
argument_list|,
name|oldHolder
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|get (CamelContext camelContext, String key)
specifier|public
name|Exchange
name|get
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|)
block|{
return|return
name|unmarshallExchange
argument_list|(
name|camelContext
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|remove (CamelContext camelContext, String key, Exchange exchange)
specifier|public
name|void
name|remove
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|DefaultExchangeHolder
name|holder
init|=
name|DefaultExchangeHolder
operator|.
name|marshal
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
if|if
condition|(
name|optimistic
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Removing an exchange with ID {} for key {} in an optimistic manner."
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|cache
operator|.
name|remove
argument_list|(
name|key
argument_list|,
name|holder
argument_list|)
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Optimistic locking failed for exchange with key {}: IMap#remove removed no Exchanges, while it's expected to remove one."
argument_list|,
name|key
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|OptimisticLockingException
argument_list|()
throw|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Removed an exchange with ID {} for key {} in an optimistic manner."
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cache
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|confirm (CamelContext camelContext, String exchangeId)
specifier|public
name|void
name|confirm
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|exchangeId
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Confirming an exchange with ID {}."
argument_list|,
name|exchangeId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getKeys ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getKeys
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|Cache
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|DefaultExchangeHolder
argument_list|>
argument_list|>
name|entries
init|=
name|cache
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|entries
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|keys
operator|.
name|add
argument_list|(
name|entries
operator|.
name|next
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|keys
argument_list|)
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
if|if
condition|(
name|cache
operator|!=
literal|null
condition|)
block|{
name|cacheManager
operator|=
operator|new
name|JCacheManager
argument_list|<>
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cacheManager
operator|=
name|JCacheHelper
operator|.
name|createManager
argument_list|(
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
argument_list|,
literal|"configuration"
argument_list|)
argument_list|)
expr_stmt|;
name|cache
operator|=
name|cacheManager
operator|.
name|getCache
argument_list|()
expr_stmt|;
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
name|cacheManager
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|unmarshallExchange (CamelContext camelContext, DefaultExchangeHolder holder)
specifier|protected
name|Exchange
name|unmarshallExchange
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|DefaultExchangeHolder
name|holder
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|holder
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|DefaultExchangeHolder
operator|.
name|unmarshal
argument_list|(
name|exchange
argument_list|,
name|holder
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

