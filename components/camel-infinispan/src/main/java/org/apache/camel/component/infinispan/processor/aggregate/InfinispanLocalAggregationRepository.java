begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan.processor.aggregate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
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
name|Set
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
name|TimeUnit
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
name|spi
operator|.
name|RecoverableAggregationRepository
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

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|commons
operator|.
name|api
operator|.
name|BasicCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|configuration
operator|.
name|cache
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|configuration
operator|.
name|global
operator|.
name|GlobalConfigurationBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|manager
operator|.
name|DefaultCacheManager
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
DECL|class|InfinispanLocalAggregationRepository
specifier|public
class|class
name|InfinispanLocalAggregationRepository
extends|extends
name|ServiceSupport
implements|implements
name|RecoverableAggregationRepository
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
name|InfinispanLocalAggregationRepository
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|useRecovery
specifier|private
name|boolean
name|useRecovery
init|=
literal|true
decl_stmt|;
DECL|field|manager
specifier|private
name|DefaultCacheManager
name|manager
decl_stmt|;
DECL|field|cacheName
specifier|private
name|String
name|cacheName
decl_stmt|;
DECL|field|deadLetterChannel
specifier|private
name|String
name|deadLetterChannel
decl_stmt|;
DECL|field|recoveryInterval
specifier|private
name|long
name|recoveryInterval
init|=
literal|5000
decl_stmt|;
DECL|field|maximumRedeliveries
specifier|private
name|int
name|maximumRedeliveries
init|=
literal|3
decl_stmt|;
DECL|field|allowSerializedHeaders
specifier|private
name|boolean
name|allowSerializedHeaders
decl_stmt|;
DECL|field|cache
specifier|private
name|BasicCache
argument_list|<
name|String
argument_list|,
name|DefaultExchangeHolder
argument_list|>
name|cache
decl_stmt|;
DECL|field|configuration
specifier|private
name|Configuration
name|configuration
decl_stmt|;
comment|/**      * Creates new {@link InfinispanLocalAggregationRepository} that defaults to non-optimistic locking      * with recoverable behavior and a local Infinispan cache.       */
DECL|method|InfinispanLocalAggregationRepository ()
specifier|public
name|InfinispanLocalAggregationRepository
parameter_list|()
block|{     }
comment|/**      * Creates new {@link InfinispanLocalAggregationRepository} that defaults to non-optimistic locking      * with recoverable behavior and a local Infinispan cache.       * @param cacheName cache name      */
DECL|method|InfinispanLocalAggregationRepository (final String cacheName)
specifier|public
name|InfinispanLocalAggregationRepository
parameter_list|(
specifier|final
name|String
name|cacheName
parameter_list|)
block|{
name|this
operator|.
name|cacheName
operator|=
name|cacheName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|add (final CamelContext camelContext, final String key, final Exchange exchange)
specifier|public
name|Exchange
name|add
parameter_list|(
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|String
name|key
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|LOG
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
name|put
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"Removing an exchange with ID {} for key {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|cache
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"Confirming an exchange with ID {}."
argument_list|,
name|exchangeId
argument_list|)
expr_stmt|;
name|cache
operator|.
name|remove
argument_list|(
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
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|cache
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|scan (CamelContext camelContext)
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|scan
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Scanning for exchanges to recover in {} context"
argument_list|,
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|scanned
init|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|cache
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Found {} keys for exchanges to recover in {} context"
argument_list|,
name|scanned
operator|.
name|size
argument_list|()
argument_list|,
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|scanned
return|;
block|}
annotation|@
name|Override
DECL|method|recover (CamelContext camelContext, String exchangeId)
specifier|public
name|Exchange
name|recover
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|exchangeId
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Recovering an Exchange with ID {}."
argument_list|,
name|exchangeId
argument_list|)
expr_stmt|;
return|return
name|useRecovery
condition|?
name|unmarshallExchange
argument_list|(
name|camelContext
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|exchangeId
argument_list|)
argument_list|)
else|:
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setRecoveryInterval (long interval, TimeUnit timeUnit)
specifier|public
name|void
name|setRecoveryInterval
parameter_list|(
name|long
name|interval
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|this
operator|.
name|recoveryInterval
operator|=
name|timeUnit
operator|.
name|toMillis
argument_list|(
name|interval
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setRecoveryInterval (long interval)
specifier|public
name|void
name|setRecoveryInterval
parameter_list|(
name|long
name|interval
parameter_list|)
block|{
name|this
operator|.
name|recoveryInterval
operator|=
name|interval
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRecoveryIntervalInMillis ()
specifier|public
name|long
name|getRecoveryIntervalInMillis
parameter_list|()
block|{
return|return
name|recoveryInterval
return|;
block|}
annotation|@
name|Override
DECL|method|setUseRecovery (boolean useRecovery)
specifier|public
name|void
name|setUseRecovery
parameter_list|(
name|boolean
name|useRecovery
parameter_list|)
block|{
name|this
operator|.
name|useRecovery
operator|=
name|useRecovery
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUseRecovery ()
specifier|public
name|boolean
name|isUseRecovery
parameter_list|()
block|{
return|return
name|useRecovery
return|;
block|}
annotation|@
name|Override
DECL|method|setDeadLetterUri (String deadLetterUri)
specifier|public
name|void
name|setDeadLetterUri
parameter_list|(
name|String
name|deadLetterUri
parameter_list|)
block|{
name|this
operator|.
name|deadLetterChannel
operator|=
name|deadLetterUri
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDeadLetterUri ()
specifier|public
name|String
name|getDeadLetterUri
parameter_list|()
block|{
return|return
name|deadLetterChannel
return|;
block|}
annotation|@
name|Override
DECL|method|setMaximumRedeliveries (int maximumRedeliveries)
specifier|public
name|void
name|setMaximumRedeliveries
parameter_list|(
name|int
name|maximumRedeliveries
parameter_list|)
block|{
name|this
operator|.
name|maximumRedeliveries
operator|=
name|maximumRedeliveries
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMaximumRedeliveries ()
specifier|public
name|int
name|getMaximumRedeliveries
parameter_list|()
block|{
return|return
name|maximumRedeliveries
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
name|maximumRedeliveries
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Maximum redelivery retries must be zero or a positive integer."
argument_list|)
throw|;
block|}
if|if
condition|(
name|recoveryInterval
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Recovery interval must be zero or a positive integer."
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
argument_list|)
condition|)
block|{
name|manager
operator|=
operator|new
name|DefaultCacheManager
argument_list|(
operator|new
name|GlobalConfigurationBuilder
argument_list|()
operator|.
name|defaultCacheName
argument_list|(
literal|"default"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|manager
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|manager
operator|=
operator|new
name|DefaultCacheManager
argument_list|(
operator|new
name|GlobalConfigurationBuilder
argument_list|()
operator|.
name|defaultCacheName
argument_list|(
literal|"default"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|manager
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|cacheName
argument_list|)
condition|)
block|{
name|cache
operator|=
name|manager
operator|.
name|getCache
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|cache
operator|=
name|manager
operator|.
name|getCache
argument_list|(
name|cacheName
argument_list|)
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
name|manager
operator|.
name|stop
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
DECL|method|getManager ()
specifier|public
name|DefaultCacheManager
name|getManager
parameter_list|()
block|{
return|return
name|manager
return|;
block|}
DECL|method|setManager (DefaultCacheManager manager)
specifier|public
name|void
name|setManager
parameter_list|(
name|DefaultCacheManager
name|manager
parameter_list|)
block|{
name|this
operator|.
name|manager
operator|=
name|manager
expr_stmt|;
block|}
DECL|method|getCacheName ()
specifier|public
name|String
name|getCacheName
parameter_list|()
block|{
return|return
name|cacheName
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
name|this
operator|.
name|cacheName
operator|=
name|cacheName
expr_stmt|;
block|}
DECL|method|getDeadLetterChannel ()
specifier|public
name|String
name|getDeadLetterChannel
parameter_list|()
block|{
return|return
name|deadLetterChannel
return|;
block|}
DECL|method|setDeadLetterChannel (String deadLetterChannel)
specifier|public
name|void
name|setDeadLetterChannel
parameter_list|(
name|String
name|deadLetterChannel
parameter_list|)
block|{
name|this
operator|.
name|deadLetterChannel
operator|=
name|deadLetterChannel
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
DECL|method|getCache ()
specifier|public
name|BasicCache
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
DECL|method|setCache (BasicCache<String, DefaultExchangeHolder> cache)
specifier|public
name|void
name|setCache
parameter_list|(
name|BasicCache
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
DECL|method|getConfiguration ()
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (Configuration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|Configuration
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
block|}
end_class

end_unit

