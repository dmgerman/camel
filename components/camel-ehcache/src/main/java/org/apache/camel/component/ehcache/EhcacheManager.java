begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|URL
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
name|ResourceHelper
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
name|CacheManagerBuilder
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
name|xml
operator|.
name|XmlConfiguration
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
DECL|field|managed
specifier|private
specifier|final
name|boolean
name|managed
decl_stmt|;
DECL|method|EhcacheManager (String cacheName, EhcacheConfiguration configuration)
specifier|public
name|EhcacheManager
parameter_list|(
name|String
name|cacheName
parameter_list|,
name|EhcacheConfiguration
name|configuration
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|cacheName
argument_list|,
name|configuration
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|EhcacheManager (String cacheName, EhcacheConfiguration configuration, CamelContext camelContext)
specifier|public
name|EhcacheManager
parameter_list|(
name|String
name|cacheName
parameter_list|,
name|EhcacheConfiguration
name|configuration
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|createCacheManager
argument_list|(
name|cacheName
argument_list|,
name|configuration
argument_list|,
name|camelContext
argument_list|)
argument_list|,
operator|!
name|configuration
operator|.
name|hasCacheManager
argument_list|()
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
DECL|method|EhcacheManager (CacheManager cacheManager)
specifier|public
name|EhcacheManager
parameter_list|(
name|CacheManager
name|cacheManager
parameter_list|)
block|{
name|this
argument_list|(
name|cacheManager
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|EhcacheManager (CacheManager cacheManager, boolean managed)
specifier|public
name|EhcacheManager
parameter_list|(
name|CacheManager
name|cacheManager
parameter_list|,
name|boolean
name|managed
parameter_list|)
block|{
name|this
argument_list|(
name|cacheManager
argument_list|,
name|managed
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|EhcacheManager (CacheManager cacheManager, boolean managed, EhcacheConfiguration configuration)
specifier|private
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
name|cacheManager
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
name|managed
operator|=
name|managed
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|cacheManager
argument_list|,
literal|"cacheManager"
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
throws|throws
name|Exception
block|{
if|if
condition|(
name|managed
condition|)
block|{
name|cacheManager
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
specifier|synchronized
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|managed
condition|)
block|{
name|cacheManager
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// Clean up any User managed cache
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
name|CacheConfiguration
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cacheConfiguration
init|=
name|configuration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|cacheConfiguration
operator|!=
literal|null
condition|)
block|{
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
name|cache
operator|=
operator|(
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
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
name|keyType
argument_list|,
name|valueType
argument_list|)
operator|.
name|build
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
comment|// *************************************************
comment|//
comment|// *************************************************
DECL|method|createCacheManager (String cacheName, EhcacheConfiguration configuration)
specifier|private
specifier|static
name|CacheManager
name|createCacheManager
parameter_list|(
name|String
name|cacheName
parameter_list|,
name|EhcacheConfiguration
name|configuration
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|createCacheManager
argument_list|(
name|cacheName
argument_list|,
name|configuration
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|createCacheManager (String cacheName, EhcacheConfiguration configuration, CamelContext camelContext)
specifier|private
specifier|static
name|CacheManager
name|createCacheManager
parameter_list|(
name|String
name|cacheName
parameter_list|,
name|EhcacheConfiguration
name|configuration
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|IOException
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|cacheName
argument_list|,
literal|"Ehcache cacheName"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
argument_list|,
literal|"Camel Ehcache configuration"
argument_list|)
expr_stmt|;
name|CacheManager
name|manager
init|=
name|configuration
operator|.
name|getCacheManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|manager
operator|==
literal|null
condition|)
block|{
name|String
name|configurationUri
init|=
name|configuration
operator|.
name|getConfigurationUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|configurationUri
operator|!=
literal|null
condition|)
block|{
name|URL
name|url
init|=
name|camelContext
operator|!=
literal|null
condition|?
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsUrl
argument_list|(
name|camelContext
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|configurationUri
argument_list|)
else|:
operator|new
name|URL
argument_list|(
name|configurationUri
argument_list|)
decl_stmt|;
name|manager
operator|=
name|CacheManagerBuilder
operator|.
name|newCacheManager
argument_list|(
operator|new
name|XmlConfiguration
argument_list|(
name|url
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|CacheManagerBuilder
name|builder
init|=
name|CacheManagerBuilder
operator|.
name|newCacheManagerBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getConfiguration
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|withCache
argument_list|(
name|cacheName
argument_list|,
name|configuration
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|manager
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|manager
return|;
block|}
block|}
end_class

end_unit

