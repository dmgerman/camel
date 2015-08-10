begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Ehcache
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|event
operator|.
name|CacheEventListener
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
name|Producer
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
name|DefaultEndpoint
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
name|impl
operator|.
name|DefaultMessage
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
name|ObjectHelper
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
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"cache"
argument_list|,
name|title
operator|=
literal|"EHCache"
argument_list|,
name|syntax
operator|=
literal|"cache:cacheName"
argument_list|,
name|consumerClass
operator|=
name|CacheConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"cache"
argument_list|)
DECL|class|CacheEndpoint
specifier|public
class|class
name|CacheEndpoint
extends|extends
name|DefaultEndpoint
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
name|CacheEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|UriParam
DECL|field|config
specifier|private
name|CacheConfiguration
name|config
decl_stmt|;
annotation|@
name|UriParam
DECL|field|cacheManagerFactory
specifier|private
name|CacheManagerFactory
name|cacheManagerFactory
decl_stmt|;
annotation|@
name|UriParam
DECL|field|key
specifier|private
name|String
name|key
decl_stmt|;
annotation|@
name|UriParam
DECL|field|operation
specifier|private
name|String
name|operation
decl_stmt|;
DECL|method|CacheEndpoint ()
specifier|public
name|CacheEndpoint
parameter_list|()
block|{     }
DECL|method|CacheEndpoint (String endpointUri, Component component, CacheConfiguration config, CacheManagerFactory cacheManagerFactory)
specifier|public
name|CacheEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|CacheConfiguration
name|config
parameter_list|,
name|CacheManagerFactory
name|cacheManagerFactory
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|cacheManagerFactory
operator|=
name|cacheManagerFactory
expr_stmt|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|config
argument_list|,
literal|"config"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|cacheManagerFactory
argument_list|,
literal|"cacheManagerFactory"
argument_list|)
expr_stmt|;
name|CacheConsumer
name|answer
init|=
operator|new
name|CacheConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|config
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|config
argument_list|,
literal|"config"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|cacheManagerFactory
argument_list|,
literal|"cacheManagerFactory"
argument_list|)
expr_stmt|;
return|return
operator|new
name|CacheProducer
argument_list|(
name|this
argument_list|,
name|config
argument_list|)
return|;
block|}
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
DECL|method|getConfig ()
specifier|public
name|CacheConfiguration
name|getConfig
parameter_list|()
block|{
return|return
name|config
return|;
block|}
DECL|method|setConfig (CacheConfiguration config)
specifier|public
name|void
name|setConfig
parameter_list|(
name|CacheConfiguration
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
DECL|method|getCacheManagerFactory ()
specifier|public
name|CacheManagerFactory
name|getCacheManagerFactory
parameter_list|()
block|{
return|return
name|cacheManagerFactory
return|;
block|}
comment|/**      * To use a custom CacheManagerFactory for creating the CacheManager to be used by this endpoint.      *<p/>      * By default the CacheManagerFactory configured on the component is used.      */
DECL|method|setCacheManagerFactory (CacheManagerFactory cacheManagerFactory)
specifier|public
name|void
name|setCacheManagerFactory
parameter_list|(
name|CacheManagerFactory
name|cacheManagerFactory
parameter_list|)
block|{
name|this
operator|.
name|cacheManagerFactory
operator|=
name|cacheManagerFactory
expr_stmt|;
block|}
DECL|method|createCacheExchange (String operation, String key, Object value)
specifier|public
name|Exchange
name|createCacheExchange
parameter_list|(
name|String
name|operation
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|super
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_OPERATION
argument_list|,
name|operation
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_KEY
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
comment|/**      * Returns {@link Cache} instance or create new one if not exists.      *       * @return {@link Cache}      */
DECL|method|initializeCache ()
specifier|public
name|Ehcache
name|initializeCache
parameter_list|()
block|{
name|CacheManager
name|cacheManager
init|=
name|getCacheManagerFactory
argument_list|()
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|Ehcache
name|cache
decl_stmt|;
if|if
condition|(
name|cacheManager
operator|.
name|cacheExists
argument_list|(
name|config
operator|.
name|getCacheName
argument_list|()
argument_list|)
condition|)
block|{
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
literal|"Found an existing cache: {}"
argument_list|,
name|config
operator|.
name|getCacheName
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Cache {} currently contains {} elements"
argument_list|,
name|config
operator|.
name|getCacheName
argument_list|()
argument_list|,
name|cacheManager
operator|.
name|getEhcache
argument_list|(
name|config
operator|.
name|getCacheName
argument_list|()
argument_list|)
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|cache
operator|=
name|cacheManager
operator|.
name|getEhcache
argument_list|(
name|config
operator|.
name|getCacheName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cache
operator|=
operator|new
name|Cache
argument_list|(
name|config
operator|.
name|getCacheName
argument_list|()
argument_list|,
name|config
operator|.
name|getMaxElementsInMemory
argument_list|()
argument_list|,
name|config
operator|.
name|getMemoryStoreEvictionPolicy
argument_list|()
argument_list|,
name|config
operator|.
name|isOverflowToDisk
argument_list|()
argument_list|,
name|config
operator|.
name|getDiskStorePath
argument_list|()
argument_list|,
name|config
operator|.
name|isEternal
argument_list|()
argument_list|,
name|config
operator|.
name|getTimeToLiveSeconds
argument_list|()
argument_list|,
name|config
operator|.
name|getTimeToIdleSeconds
argument_list|()
argument_list|,
name|config
operator|.
name|isDiskPersistent
argument_list|()
argument_list|,
name|config
operator|.
name|getDiskExpiryThreadIntervalSeconds
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
for|for
control|(
name|CacheEventListener
name|listener
range|:
name|config
operator|.
name|getEventListenerRegistry
argument_list|()
operator|.
name|getEventListeners
argument_list|()
control|)
block|{
name|cache
operator|.
name|getCacheEventNotificationService
argument_list|()
operator|.
name|registerListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|CacheLoaderWrapper
name|loader
range|:
name|config
operator|.
name|getCacheLoaderRegistry
argument_list|()
operator|.
name|getCacheLoaders
argument_list|()
control|)
block|{
name|loader
operator|.
name|init
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|cache
operator|.
name|registerCacheLoader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
name|cacheManager
operator|.
name|addCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Added a new cache: "
operator|+
name|cache
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|cache
return|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|CacheManager
name|cacheManager
init|=
name|getCacheManagerFactory
argument_list|()
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cacheManager
operator|.
name|removeCache
argument_list|(
name|config
operator|.
name|getCacheName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * The default cache operation to use.      * If an operation in the message header, then the operation from the header takes precedence.      */
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
comment|/**      * The default key to use.      * If a key is provided in the message header, then the key from the header takes precedence.      */
DECL|method|setKey (String key)
specifier|public
name|void
name|setKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
block|}
end_class

end_unit

