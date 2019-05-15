begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ehcache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ehcache
package|;
end_package

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
name|Service
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
name|ReferenceCount
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|UserManagedCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|config
operator|.
name|CacheConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|config
operator|.
name|builders
operator|.
name|CacheConfigurationBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|config
operator|.
name|builders
operator|.
name|UserManagedCacheBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|spi
operator|.
name|service
operator|.
name|ServiceConfiguration
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
DECL|class|EhcacheManager
specifier|public
class|class
name|EhcacheManager
implements|implements
name|Service
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|EhcacheManager
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|EhcacheConfiguration
name|configuration
decl_stmt|;
DECL|field|cacheManager
specifier|private
specifier|final
name|CacheManager
name|cacheManager
decl_stmt|;
DECL|field|userCaches
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|UserManagedCache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|userCaches
decl_stmt|;
DECL|field|refCount
specifier|private
specifier|final
name|ReferenceCount
name|refCount
decl_stmt|;
DECL|method|EhcacheManager (CacheManager cacheManager, boolean managed, EhcacheConfiguration configuration)
specifier|public
name|EhcacheManager
parameter_list|(
name|CacheManager
name|cacheManager
parameter_list|,
name|boolean
name|managed
parameter_list|,
name|EhcacheConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|cacheManager
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|cacheManager
argument_list|,
literal|"cacheManager"
argument_list|)
expr_stmt|;
name|this
operator|.
name|userCaches
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|refCount
operator|=
name|ReferenceCount
operator|.
name|on
argument_list|(
name|managed
condition|?
name|cacheManager
operator|::
name|init
else|:
parameter_list|()
lambda|->
block|{ }
argument_list|,
name|managed
condition|?
name|cacheManager
operator|::
name|close
else|:
parameter_list|()
lambda|->
block|{ }
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
specifier|synchronized
name|void
name|start
parameter_list|()
block|{
name|refCount
operator|.
name|retain
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
specifier|synchronized
name|void
name|stop
parameter_list|()
block|{
name|refCount
operator|.
name|release
argument_list|()
expr_stmt|;
name|userCaches
operator|.
name|values
argument_list|()
operator|.
name|forEach
argument_list|(
name|UserManagedCache
operator|::
name|close
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getCache (String name, Class<K> keyType, Class<V> valueType)
specifier|public
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getCache
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|K
argument_list|>
name|keyType
parameter_list|,
name|Class
argument_list|<
name|V
argument_list|>
name|valueType
parameter_list|)
throws|throws
name|Exception
block|{
name|CacheConfiguration
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cacheConfiguration
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|configuration
operator|.
name|hasConfiguration
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Using custom cache configuration for cache {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|cacheConfiguration
operator|=
name|CacheConfiguration
operator|.
name|class
operator|.
name|cast
argument_list|(
name|configuration
operator|.
name|getConfigurations
argument_list|()
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|configuration
operator|.
name|hasConfiguration
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Using global cache configuration for cache {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|cacheConfiguration
operator|=
name|CacheConfiguration
operator|.
name|class
operator|.
name|cast
argument_list|(
name|configuration
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|keyType
operator|==
literal|null
operator|&&
name|valueType
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|cacheConfiguration
operator|!=
literal|null
condition|)
block|{
name|keyType
operator|=
name|cacheConfiguration
operator|.
name|getKeyType
argument_list|()
expr_stmt|;
name|valueType
operator|=
name|cacheConfiguration
operator|.
name|getValueType
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|keyType
operator|=
operator|(
name|Class
argument_list|<
name|K
argument_list|>
operator|)
name|Object
operator|.
name|class
expr_stmt|;
name|valueType
operator|=
operator|(
name|Class
argument_list|<
name|V
argument_list|>
operator|)
name|Object
operator|.
name|class
expr_stmt|;
block|}
block|}
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cache
init|=
name|cacheManager
operator|.
name|getCache
argument_list|(
name|name
argument_list|,
name|keyType
argument_list|,
name|valueType
argument_list|)
decl_stmt|;
if|if
condition|(
name|cache
operator|==
literal|null
operator|&&
name|configuration
operator|!=
literal|null
operator|&&
name|configuration
operator|.
name|isCreateCacheIfNotExist
argument_list|()
condition|)
block|{
if|if
condition|(
name|cacheConfiguration
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|keyType
operator|!=
name|cacheConfiguration
operator|.
name|getKeyType
argument_list|()
operator|||
name|valueType
operator|!=
name|cacheConfiguration
operator|.
name|getValueType
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Mismatch keyType / valueType configuration for cache {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|CacheConfigurationBuilder
name|builder
init|=
name|CacheConfigurationBuilder
operator|.
name|newCacheConfigurationBuilder
argument_list|(
name|keyType
argument_list|,
name|valueType
argument_list|,
name|cacheConfiguration
operator|.
name|getResourcePools
argument_list|()
argument_list|)
operator|.
name|withClassLoader
argument_list|(
name|cacheConfiguration
operator|.
name|getClassLoader
argument_list|()
argument_list|)
operator|.
name|withEvictionAdvisor
argument_list|(
name|cacheConfiguration
operator|.
name|getEvictionAdvisor
argument_list|()
argument_list|)
operator|.
name|withExpiry
argument_list|(
name|cacheConfiguration
operator|.
name|getExpiryPolicy
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ServiceConfiguration
argument_list|<
name|?
argument_list|>
name|serviceConfig
range|:
name|cacheConfiguration
operator|.
name|getServiceConfigurations
argument_list|()
control|)
block|{
name|builder
operator|=
name|builder
operator|.
name|add
argument_list|(
name|serviceConfig
argument_list|)
expr_stmt|;
block|}
name|cacheConfiguration
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
name|cache
operator|=
name|cacheManager
operator|.
name|createCache
argument_list|(
name|name
argument_list|,
name|cacheConfiguration
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// If a cache configuration is not provided, create a User Managed
comment|// Cache
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Using a UserManagedCache for cache {} as no configuration has been found"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|K
argument_list|>
name|kt
init|=
name|keyType
decl_stmt|;
name|Class
argument_list|<
name|V
argument_list|>
name|vt
init|=
name|valueType
decl_stmt|;
name|cache
operator|=
name|Cache
operator|.
name|class
operator|.
name|cast
argument_list|(
name|userCaches
operator|.
name|computeIfAbsent
argument_list|(
name|name
argument_list|,
name|key
lambda|->
name|UserManagedCacheBuilder
operator|.
name|newUserManagedCacheBuilder
argument_list|(
name|kt
argument_list|,
name|vt
argument_list|)
operator|.
name|build
argument_list|(
literal|true
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cache
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Unable to retrieve the cache "
operator|+
name|name
operator|+
literal|" from cache manager "
operator|+
name|cacheManager
argument_list|)
throw|;
block|}
return|return
name|cache
return|;
block|}
DECL|method|getCacheManager ()
name|CacheManager
name|getCacheManager
parameter_list|()
block|{
return|return
name|this
operator|.
name|cacheManager
return|;
block|}
DECL|method|getReferenceCount ()
name|ReferenceCount
name|getReferenceCount
parameter_list|()
block|{
return|return
name|refCount
return|;
block|}
block|}
end_class

end_unit

