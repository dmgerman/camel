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
name|Map
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
name|spi
operator|.
name|ClassResolver
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
name|Metadata
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
name|annotations
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
name|support
operator|.
name|DefaultComponent
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
name|ResourceHelper
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
name|Configuration
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

begin_comment
comment|/**  * Represents the component that manages {@link DefaultComponent}.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"ehcache"
argument_list|)
DECL|class|EhcacheComponent
specifier|public
class|class
name|EhcacheComponent
extends|extends
name|DefaultComponent
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
name|EhcacheComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|managers
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|Object
argument_list|,
name|EhcacheManager
argument_list|>
name|managers
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configuration
specifier|private
name|EhcacheConfiguration
name|configuration
init|=
operator|new
name|EhcacheConfiguration
argument_list|()
decl_stmt|;
DECL|method|EhcacheComponent ()
specifier|public
name|EhcacheComponent
parameter_list|()
block|{     }
DECL|method|EhcacheComponent (CamelContext context)
specifier|public
name|EhcacheComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|EhcacheConfiguration
name|configuration
init|=
name|this
operator|.
name|configuration
operator|.
name|copy
argument_list|()
decl_stmt|;
name|EhcacheEndpoint
name|endpoint
init|=
operator|new
name|EhcacheEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
comment|// ****************************
comment|// Helpers
comment|// ****************************
DECL|method|createCacheManager (EhcacheConfiguration configuration)
specifier|public
name|EhcacheManager
name|createCacheManager
parameter_list|(
name|EhcacheConfiguration
name|configuration
parameter_list|)
throws|throws
name|IOException
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
argument_list|,
literal|"Camel Ehcache configuration"
argument_list|)
expr_stmt|;
comment|// Check if a cache manager has been configured
if|if
condition|(
name|configuration
operator|.
name|hasCacheManager
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"EhcacheManager configured with supplied CacheManager"
argument_list|)
expr_stmt|;
return|return
name|managers
operator|.
name|computeIfAbsent
argument_list|(
name|configuration
operator|.
name|getCacheManager
argument_list|()
argument_list|,
name|m
lambda|->
operator|new
name|EhcacheManager
argument_list|(
name|CacheManager
operator|.
name|class
operator|.
name|cast
argument_list|(
name|m
argument_list|)
argument_list|,
literal|false
argument_list|,
name|configuration
argument_list|)
argument_list|)
return|;
block|}
comment|// Check if a cache manager configuration has been provided
if|if
condition|(
name|configuration
operator|.
name|hasCacheManagerConfiguration
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"EhcacheManager configured with supplied CacheManagerConfiguration"
argument_list|)
expr_stmt|;
return|return
name|managers
operator|.
name|computeIfAbsent
argument_list|(
name|configuration
operator|.
name|getCacheManagerConfiguration
argument_list|()
argument_list|,
name|c
lambda|->
operator|new
name|EhcacheManager
argument_list|(
name|CacheManagerBuilder
operator|.
name|newCacheManager
argument_list|(
name|Configuration
operator|.
name|class
operator|.
name|cast
argument_list|(
name|c
argument_list|)
argument_list|)
argument_list|,
literal|true
argument_list|,
name|configuration
argument_list|)
argument_list|)
return|;
block|}
comment|// Check if a configuration file has been provided
if|if
condition|(
name|configuration
operator|.
name|hasConfigurationUri
argument_list|()
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
name|ClassResolver
name|classResolver
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
decl_stmt|;
name|URL
name|url
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsUrl
argument_list|(
name|classResolver
argument_list|,
name|configurationUri
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|"EhcacheManager configured with supplied URI {}"
argument_list|,
name|url
argument_list|)
expr_stmt|;
return|return
name|managers
operator|.
name|computeIfAbsent
argument_list|(
name|url
argument_list|,
name|u
lambda|->
operator|new
name|EhcacheManager
argument_list|(
name|CacheManagerBuilder
operator|.
name|newCacheManager
argument_list|(
operator|new
name|XmlConfiguration
argument_list|(
name|URL
operator|.
name|class
operator|.
name|cast
argument_list|(
name|u
argument_list|)
argument_list|)
argument_list|)
argument_list|,
literal|true
argument_list|,
name|configuration
argument_list|)
argument_list|)
return|;
block|}
name|LOGGER
operator|.
name|info
argument_list|(
literal|"EhcacheManager configured with default builder"
argument_list|)
expr_stmt|;
return|return
operator|new
name|EhcacheManager
argument_list|(
name|CacheManagerBuilder
operator|.
name|newCacheManagerBuilder
argument_list|()
operator|.
name|build
argument_list|()
argument_list|,
literal|true
argument_list|,
name|configuration
argument_list|)
return|;
block|}
comment|// ****************************
comment|// Properties
comment|// ****************************
DECL|method|getConfiguration ()
specifier|public
name|EhcacheConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * Sets the global component configuration      */
DECL|method|setConfiguration (EhcacheConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|EhcacheConfiguration
name|configuration
parameter_list|)
block|{
comment|// The component configuration can't be null
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
argument_list|,
literal|"EhcacheConfiguration"
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getCacheManager ()
specifier|public
name|CacheManager
name|getCacheManager
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getCacheManager
argument_list|()
return|;
block|}
comment|/**      * The cache manager      */
DECL|method|setCacheManager (CacheManager cacheManager)
specifier|public
name|void
name|setCacheManager
parameter_list|(
name|CacheManager
name|cacheManager
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|.
name|setCacheManager
argument_list|(
name|cacheManager
argument_list|)
expr_stmt|;
block|}
DECL|method|getCacheManagerConfiguration ()
specifier|public
name|Configuration
name|getCacheManagerConfiguration
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getCacheManagerConfiguration
argument_list|()
return|;
block|}
comment|/**      * The cache manager configuration      */
DECL|method|setCacheManagerConfiguration (Configuration cacheManagerConfiguration)
specifier|public
name|void
name|setCacheManagerConfiguration
parameter_list|(
name|Configuration
name|cacheManagerConfiguration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|.
name|setCacheManagerConfiguration
argument_list|(
name|cacheManagerConfiguration
argument_list|)
expr_stmt|;
block|}
comment|/**      * The default cache configuration to be used to create caches.      */
DECL|method|setCacheConfiguration (CacheConfiguration cacheConfiguration)
specifier|public
name|void
name|setCacheConfiguration
parameter_list|(
name|CacheConfiguration
name|cacheConfiguration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|.
name|setConfiguration
argument_list|(
name|cacheConfiguration
argument_list|)
expr_stmt|;
block|}
DECL|method|getCacheConfiguration ()
specifier|public
name|CacheConfiguration
name|getCacheConfiguration
parameter_list|()
block|{
return|return
name|this
operator|.
name|configuration
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
DECL|method|getCachesConfigurations ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|CacheConfiguration
argument_list|>
name|getCachesConfigurations
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getConfigurations
argument_list|()
return|;
block|}
comment|/**      * A map of caches configurations to be used to create caches.      */
DECL|method|setCachesConfigurations (Map<String, CacheConfiguration> configurations)
specifier|public
name|void
name|setCachesConfigurations
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|CacheConfiguration
argument_list|>
name|configurations
parameter_list|)
block|{
name|configuration
operator|.
name|setConfigurations
argument_list|(
name|configurations
argument_list|)
expr_stmt|;
block|}
DECL|method|addCachesConfigurations (Map<String, CacheConfiguration> configurations)
specifier|public
name|void
name|addCachesConfigurations
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|CacheConfiguration
argument_list|>
name|configurations
parameter_list|)
block|{
name|configuration
operator|.
name|addConfigurations
argument_list|(
name|configurations
argument_list|)
expr_stmt|;
block|}
DECL|method|getCacheConfigurationUri ()
specifier|public
name|String
name|getCacheConfigurationUri
parameter_list|()
block|{
return|return
name|this
operator|.
name|configuration
operator|.
name|getConfigurationUri
argument_list|()
return|;
block|}
comment|/**      * URI pointing to the Ehcache XML configuration file's location      */
DECL|method|setCacheConfigurationUri (String configurationUri)
specifier|public
name|void
name|setCacheConfigurationUri
parameter_list|(
name|String
name|configurationUri
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|.
name|setConfigurationUri
argument_list|(
name|configurationUri
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

