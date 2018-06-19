begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|infinispan
operator|.
name|cache
operator|.
name|impl
operator|.
name|DecoratedCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|RemoteCacheManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|configuration
operator|.
name|ConfigurationBuilder
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
name|commons
operator|.
name|api
operator|.
name|BasicCacheContainer
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
DECL|class|InfinispanManager
specifier|public
class|class
name|InfinispanManager
implements|implements
name|Service
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|InfinispanManager
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|InfinispanConfiguration
name|configuration
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|cacheContainer
specifier|private
name|BasicCacheContainer
name|cacheContainer
decl_stmt|;
DECL|field|isManagedCacheContainer
specifier|private
name|boolean
name|isManagedCacheContainer
decl_stmt|;
DECL|method|InfinispanManager ()
specifier|public
name|InfinispanManager
parameter_list|()
block|{
name|this
operator|.
name|camelContext
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
operator|new
name|InfinispanConfiguration
argument_list|()
expr_stmt|;
name|this
operator|.
name|configuration
operator|.
name|setCacheContainer
argument_list|(
operator|new
name|DefaultCacheManager
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|InfinispanManager (InfinispanConfiguration configuration)
specifier|public
name|InfinispanManager
parameter_list|(
name|InfinispanConfiguration
name|configuration
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
DECL|method|InfinispanManager (CamelContext camelContext, InfinispanConfiguration configuration)
specifier|public
name|InfinispanManager
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|InfinispanConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|cacheContainer
operator|=
name|configuration
operator|.
name|getCacheContainer
argument_list|()
expr_stmt|;
if|if
condition|(
name|cacheContainer
operator|==
literal|null
condition|)
block|{
specifier|final
name|Object
name|containerConf
init|=
name|configuration
operator|.
name|getCacheContainerConfiguration
argument_list|()
decl_stmt|;
comment|// Check if a container configuration object has been provided so use
comment|// it and discard any other additional configuration.
if|if
condition|(
name|containerConf
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|containerConf
operator|instanceof
name|org
operator|.
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|configuration
operator|.
name|Configuration
condition|)
block|{
name|cacheContainer
operator|=
operator|new
name|RemoteCacheManager
argument_list|(
operator|(
name|org
operator|.
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|configuration
operator|.
name|Configuration
operator|)
name|containerConf
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|containerConf
operator|instanceof
name|org
operator|.
name|infinispan
operator|.
name|configuration
operator|.
name|cache
operator|.
name|Configuration
condition|)
block|{
name|cacheContainer
operator|=
operator|new
name|DefaultCacheManager
argument_list|(
operator|(
name|org
operator|.
name|infinispan
operator|.
name|configuration
operator|.
name|cache
operator|.
name|Configuration
operator|)
name|containerConf
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported CacheContainer Configuration type: "
operator|+
name|containerConf
operator|.
name|getClass
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|// If the hosts properties has been configured, it means we want to
comment|// connect to a remote cache so set-up a RemoteCacheManager
if|if
condition|(
name|cacheContainer
operator|==
literal|null
operator|&&
name|configuration
operator|.
name|getHosts
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ConfigurationBuilder
name|builder
init|=
operator|new
name|ConfigurationBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|addServers
argument_list|(
name|configuration
operator|.
name|getHosts
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|camelContext
operator|!=
literal|null
operator|&&
name|camelContext
operator|.
name|getApplicationContextClassLoader
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|classLoader
argument_list|(
name|camelContext
operator|.
name|getApplicationContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|classLoader
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
comment|// Properties can be set either via a properties file or via
comment|// properties on configuration, if you set both they are merged
comment|// with properties defined on configuration overriding those from
comment|// file.
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getConfigurationUri
argument_list|()
argument_list|)
condition|)
block|{
name|properties
operator|.
name|putAll
argument_list|(
name|InfinispanUtil
operator|.
name|loadProperties
argument_list|(
name|camelContext
argument_list|,
name|configuration
operator|.
name|getConfigurationUri
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getConfigurationProperties
argument_list|()
argument_list|)
condition|)
block|{
name|properties
operator|.
name|putAll
argument_list|(
name|configuration
operator|.
name|getConfigurationProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|properties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|builder
operator|.
name|withProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
name|cacheContainer
operator|=
operator|new
name|RemoteCacheManager
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// Finally we can set-up a DefaultCacheManager if none of the methods
comment|// above was triggered. You can configure the cache using a configuration
comment|// file.
if|if
condition|(
name|cacheContainer
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getConfigurationUri
argument_list|()
argument_list|)
condition|)
block|{
try|try
init|(
name|InputStream
name|is
init|=
name|InfinispanUtil
operator|.
name|openInputStream
argument_list|(
name|camelContext
argument_list|,
name|configuration
operator|.
name|getConfigurationUri
argument_list|()
argument_list|)
init|)
block|{
name|cacheContainer
operator|=
operator|new
name|DefaultCacheManager
argument_list|(
name|is
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|cacheContainer
operator|=
operator|new
name|DefaultCacheManager
argument_list|(
operator|new
name|org
operator|.
name|infinispan
operator|.
name|configuration
operator|.
name|cache
operator|.
name|ConfigurationBuilder
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|isManagedCacheContainer
operator|=
literal|true
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isManagedCacheContainer
condition|)
block|{
name|cacheContainer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getCacheContainer ()
specifier|public
name|BasicCacheContainer
name|getCacheContainer
parameter_list|()
block|{
return|return
name|cacheContainer
return|;
block|}
DECL|method|isCacheContainerEmbedded ()
specifier|public
name|boolean
name|isCacheContainerEmbedded
parameter_list|()
block|{
return|return
name|InfinispanUtil
operator|.
name|isEmbedded
argument_list|(
name|cacheContainer
argument_list|)
return|;
block|}
DECL|method|isCacheContainerRemote ()
specifier|public
name|boolean
name|isCacheContainerRemote
parameter_list|()
block|{
return|return
name|InfinispanUtil
operator|.
name|isRemote
argument_list|(
name|cacheContainer
argument_list|)
return|;
block|}
DECL|method|getCache (String cacheName)
specifier|public
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|BasicCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getCache
parameter_list|(
name|String
name|cacheName
parameter_list|)
block|{
name|BasicCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cache
decl_stmt|;
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
name|cacheContainer
operator|.
name|getCache
argument_list|()
expr_stmt|;
name|cacheName
operator|=
name|cache
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|cache
operator|=
name|cacheContainer
operator|.
name|getCache
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
block|}
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Cache[{}]"
argument_list|,
name|cacheName
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|hasFlags
argument_list|()
operator|&&
name|InfinispanUtil
operator|.
name|isEmbedded
argument_list|(
name|cache
argument_list|)
condition|)
block|{
name|cache
operator|=
operator|new
name|DecoratedCache
argument_list|(
name|InfinispanUtil
operator|.
name|asAdvanced
argument_list|(
name|cache
argument_list|)
argument_list|,
name|configuration
operator|.
name|getFlags
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|cache
return|;
block|}
DECL|method|getCache (String cacheName, boolean forceReturnValue)
specifier|public
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|BasicCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getCache
parameter_list|(
name|String
name|cacheName
parameter_list|,
name|boolean
name|forceReturnValue
parameter_list|)
block|{
if|if
condition|(
name|isCacheContainerRemote
argument_list|()
condition|)
block|{
name|BasicCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cache
decl_stmt|;
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
name|InfinispanUtil
operator|.
name|asRemote
argument_list|(
name|cacheContainer
argument_list|)
operator|.
name|getCache
argument_list|(
name|forceReturnValue
argument_list|)
expr_stmt|;
name|cacheName
operator|=
name|cache
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|cache
operator|=
name|InfinispanUtil
operator|.
name|asRemote
argument_list|(
name|cacheContainer
argument_list|)
operator|.
name|getCache
argument_list|(
name|cacheName
argument_list|,
name|forceReturnValue
argument_list|)
expr_stmt|;
block|}
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Cache[{}]"
argument_list|,
name|cacheName
argument_list|)
expr_stmt|;
return|return
name|cache
return|;
block|}
else|else
block|{
return|return
name|getCache
argument_list|(
name|cacheName
argument_list|)
return|;
block|}
block|}
DECL|method|getCache (Exchange exchange, String defaultCache)
specifier|public
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|BasicCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getCache
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|defaultCache
parameter_list|)
block|{
return|return
name|getCache
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|defaultCache
argument_list|)
return|;
block|}
DECL|method|getCache (Message message, String defaultCache)
specifier|public
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|BasicCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getCache
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|defaultCache
parameter_list|)
block|{
name|BasicCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cache
init|=
name|getCache
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|InfinispanConstants
operator|.
name|CACHE_NAME
argument_list|,
name|defaultCache
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|message
operator|.
name|getHeader
argument_list|(
name|InfinispanConstants
operator|.
name|IGNORE_RETURN_VALUES
argument_list|)
operator|!=
literal|null
condition|?
name|cache
else|:
name|InfinispanUtil
operator|.
name|ignoreReturnValuesCache
argument_list|(
name|cache
argument_list|)
return|;
block|}
block|}
end_class

end_unit

