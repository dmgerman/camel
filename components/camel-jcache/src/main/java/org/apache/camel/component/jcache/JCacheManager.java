begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcache
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|List
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
name|javax
operator|.
name|cache
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|Caching
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|configuration
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|configuration
operator|.
name|MutableConfiguration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|event
operator|.
name|CacheEntryEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|event
operator|.
name|CacheEntryEventFilter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|event
operator|.
name|CacheEntryListenerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|event
operator|.
name|EventType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|spi
operator|.
name|CachingProvider
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|JCacheManager
specifier|public
class|class
name|JCacheManager
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Closeable
block|{
DECL|field|configuration
specifier|private
specifier|final
name|JCacheConfiguration
name|configuration
decl_stmt|;
DECL|field|classLoader
specifier|private
specifier|final
name|ClassLoader
name|classLoader
decl_stmt|;
DECL|field|cacheName
specifier|private
specifier|final
name|String
name|cacheName
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|provider
specifier|private
name|CachingProvider
name|provider
decl_stmt|;
DECL|field|manager
specifier|private
name|CacheManager
name|manager
decl_stmt|;
DECL|field|cache
specifier|private
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cache
decl_stmt|;
DECL|method|JCacheManager (JCacheConfiguration configuration)
specifier|public
name|JCacheManager
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
name|this
operator|.
name|camelContext
operator|=
name|configuration
operator|.
name|getCamelContext
argument_list|()
expr_stmt|;
name|this
operator|.
name|classLoader
operator|=
name|camelContext
operator|!=
literal|null
condition|?
name|camelContext
operator|.
name|getApplicationContextClassLoader
argument_list|()
else|:
literal|null
expr_stmt|;
name|this
operator|.
name|cacheName
operator|=
name|configuration
operator|.
name|getCacheName
argument_list|()
expr_stmt|;
name|this
operator|.
name|provider
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|manager
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|cache
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|JCacheManager (Cache<K, V> cache)
specifier|public
name|JCacheManager
parameter_list|(
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cache
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|classLoader
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|cacheName
operator|=
name|cache
operator|.
name|getName
argument_list|()
expr_stmt|;
name|this
operator|.
name|provider
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|manager
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
DECL|method|getCacheName ()
specifier|public
name|String
name|getCacheName
parameter_list|()
block|{
return|return
name|this
operator|.
name|cacheName
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|JCacheConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|this
operator|.
name|configuration
return|;
block|}
DECL|method|getCache ()
specifier|public
specifier|synchronized
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getCache
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|cache
operator|==
literal|null
condition|)
block|{
name|JCacheProvider
name|provider
init|=
name|JCacheProviders
operator|.
name|lookup
argument_list|(
name|configuration
operator|.
name|getCachingProvider
argument_list|()
argument_list|)
decl_stmt|;
name|cache
operator|=
name|doGetCache
argument_list|(
name|provider
argument_list|)
expr_stmt|;
block|}
return|return
name|cache
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
specifier|synchronized
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|cache
operator|!=
literal|null
condition|)
block|{
name|cache
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|manager
operator|!=
literal|null
condition|)
block|{
name|manager
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|provider
operator|!=
literal|null
condition|)
block|{
name|provider
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|getEventFilter ()
specifier|protected
name|CacheEntryEventFilter
name|getEventFilter
parameter_list|()
block|{
if|if
condition|(
name|configuration
operator|.
name|getEventFilters
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|JCacheEntryEventFilters
operator|.
name|Chained
argument_list|(
name|configuration
operator|.
name|getEventFilters
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getFilteredEvents
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|EventType
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|configuration
operator|.
name|getFilteredEvents
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|EventType
name|et
init|=
name|EventType
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|et
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|JCacheEntryEventFilters
operator|.
name|Named
argument_list|(
name|list
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|cacheEntryEvent
lambda|->
literal|true
return|;
block|}
block|}
DECL|method|doGetCache (JCacheProvider jcacheProvider)
specifier|protected
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|doGetCache
parameter_list|(
name|JCacheProvider
name|jcacheProvider
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|cache
operator|==
literal|null
condition|)
block|{
name|String
name|uri
init|=
name|configuration
operator|.
name|getConfigurationUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
operator|&&
name|camelContext
operator|!=
literal|null
condition|)
block|{
name|uri
operator|=
name|camelContext
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
name|provider
operator|=
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|jcacheProvider
operator|.
name|className
argument_list|()
argument_list|)
condition|?
name|Caching
operator|.
name|getCachingProvider
argument_list|(
name|jcacheProvider
operator|.
name|className
argument_list|()
argument_list|)
else|:
name|Caching
operator|.
name|getCachingProvider
argument_list|()
expr_stmt|;
name|manager
operator|=
name|provider
operator|.
name|getCacheManager
argument_list|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|uri
argument_list|)
condition|?
name|URI
operator|.
name|create
argument_list|(
name|uri
argument_list|)
else|:
literal|null
argument_list|,
literal|null
argument_list|,
name|configuration
operator|.
name|getCacheConfigurationProperties
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|=
name|manager
operator|.
name|getCache
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
if|if
condition|(
name|cache
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|configuration
operator|.
name|isCreateCacheIfNotExists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cache "
operator|+
name|cacheName
operator|+
literal|" does not exist and should not be created (createCacheIfNotExists=false)"
argument_list|)
throw|;
block|}
name|cache
operator|=
name|manager
operator|.
name|createCache
argument_list|(
name|cacheName
argument_list|,
name|getOrCreateCacheConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|cache
return|;
block|}
DECL|method|getOrCreateCacheConfiguration ()
specifier|private
name|Configuration
name|getOrCreateCacheConfiguration
parameter_list|()
block|{
if|if
condition|(
name|configuration
operator|.
name|getCacheConfiguration
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|configuration
operator|.
name|getCacheConfiguration
argument_list|()
return|;
block|}
name|MutableConfiguration
name|mutableConfiguration
init|=
operator|new
name|MutableConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getCacheLoaderFactory
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|mutableConfiguration
operator|.
name|setCacheLoaderFactory
argument_list|(
name|configuration
operator|.
name|getCacheLoaderFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getCacheWriterFactory
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|mutableConfiguration
operator|.
name|setCacheWriterFactory
argument_list|(
name|configuration
operator|.
name|getCacheWriterFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getExpiryPolicyFactory
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|mutableConfiguration
operator|.
name|setExpiryPolicyFactory
argument_list|(
name|configuration
operator|.
name|getExpiryPolicyFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|mutableConfiguration
operator|.
name|setManagementEnabled
argument_list|(
name|configuration
operator|.
name|isManagementEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|mutableConfiguration
operator|.
name|setStatisticsEnabled
argument_list|(
name|configuration
operator|.
name|isStatisticsEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|mutableConfiguration
operator|.
name|setReadThrough
argument_list|(
name|configuration
operator|.
name|isReadThrough
argument_list|()
argument_list|)
expr_stmt|;
name|mutableConfiguration
operator|.
name|setStoreByValue
argument_list|(
name|configuration
operator|.
name|isStoreByValue
argument_list|()
argument_list|)
expr_stmt|;
name|mutableConfiguration
operator|.
name|setWriteThrough
argument_list|(
name|configuration
operator|.
name|isWriteThrough
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|mutableConfiguration
return|;
block|}
block|}
end_class

end_unit

