begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ehcache.springboot
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
operator|.
name|springboot
package|;
end_package

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
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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
name|event
operator|.
name|EventFiring
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|event
operator|.
name|EventOrdering
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|DeprecatedConfigurationProperty
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * The ehcache component enables you to perform caching operations using Ehcache  * as cache implementation.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.ehcache"
argument_list|)
DECL|class|EhcacheComponentConfiguration
specifier|public
class|class
name|EhcacheComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Sets the global component configuration      */
DECL|field|configuration
specifier|private
name|EhcacheConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * The cache manager      */
annotation|@
name|NestedConfigurationProperty
DECL|field|cacheManager
specifier|private
name|CacheManager
name|cacheManager
decl_stmt|;
comment|/**      * The cache manager configuration      */
annotation|@
name|NestedConfigurationProperty
DECL|field|cacheManagerConfiguration
specifier|private
name|Configuration
name|cacheManagerConfiguration
decl_stmt|;
comment|/**      * The default cache configuration to be used to create caches.      */
annotation|@
name|NestedConfigurationProperty
DECL|field|cacheConfiguration
specifier|private
name|CacheConfiguration
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|cacheConfiguration
decl_stmt|;
comment|/**      * A map of caches configurations to be used to create caches.      */
DECL|field|cachesConfigurations
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|CacheConfiguration
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|cachesConfigurations
decl_stmt|;
comment|/**      * URI pointing to the Ehcache XML configuration file's location      */
DECL|field|cacheConfigurationUri
specifier|private
name|String
name|cacheConfigurationUri
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|EhcacheConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( EhcacheConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|EhcacheConfigurationNestedConfiguration
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
DECL|method|getCacheManager ()
specifier|public
name|CacheManager
name|getCacheManager
parameter_list|()
block|{
return|return
name|cacheManager
return|;
block|}
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
name|cacheManager
operator|=
name|cacheManager
expr_stmt|;
block|}
DECL|method|getCacheManagerConfiguration ()
specifier|public
name|Configuration
name|getCacheManagerConfiguration
parameter_list|()
block|{
return|return
name|cacheManagerConfiguration
return|;
block|}
DECL|method|setCacheManagerConfiguration ( Configuration cacheManagerConfiguration)
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
name|cacheManagerConfiguration
operator|=
name|cacheManagerConfiguration
expr_stmt|;
block|}
DECL|method|getCacheConfiguration ()
specifier|public
name|CacheConfiguration
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|getCacheConfiguration
parameter_list|()
block|{
return|return
name|cacheConfiguration
return|;
block|}
DECL|method|setCacheConfiguration ( CacheConfiguration<?, ?> cacheConfiguration)
specifier|public
name|void
name|setCacheConfiguration
parameter_list|(
name|CacheConfiguration
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|cacheConfiguration
parameter_list|)
block|{
name|this
operator|.
name|cacheConfiguration
operator|=
name|cacheConfiguration
expr_stmt|;
block|}
DECL|method|getCachesConfigurations ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|CacheConfiguration
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|getCachesConfigurations
parameter_list|()
block|{
return|return
name|cachesConfigurations
return|;
block|}
DECL|method|setCachesConfigurations ( Map<String, CacheConfiguration<?, ?>> cachesConfigurations)
specifier|public
name|void
name|setCachesConfigurations
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|CacheConfiguration
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|cachesConfigurations
parameter_list|)
block|{
name|this
operator|.
name|cachesConfigurations
operator|=
name|cachesConfigurations
expr_stmt|;
block|}
DECL|method|getCacheConfigurationUri ()
specifier|public
name|String
name|getCacheConfigurationUri
parameter_list|()
block|{
return|return
name|cacheConfigurationUri
return|;
block|}
DECL|method|setCacheConfigurationUri (String cacheConfigurationUri)
specifier|public
name|void
name|setCacheConfigurationUri
parameter_list|(
name|String
name|cacheConfigurationUri
parameter_list|)
block|{
name|this
operator|.
name|cacheConfigurationUri
operator|=
name|cacheConfigurationUri
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|class|EhcacheConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|EhcacheConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ehcache
operator|.
name|EhcacheConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * URI pointing to the Ehcache XML configuration file's location          */
DECL|field|configurationUri
specifier|private
name|String
name|configurationUri
decl_stmt|;
comment|/**          * URI pointing to the Ehcache XML configuration file's location          *           * @deprecated use {@link #setConfigurationUri(String)} instead          */
annotation|@
name|Deprecated
DECL|field|configUri
specifier|private
name|String
name|configUri
decl_stmt|;
comment|/**          * Configure if a cache need to be created if it does exist or can't be          * pre-configured.          */
DECL|field|createCacheIfNotExist
specifier|private
name|Boolean
name|createCacheIfNotExist
init|=
literal|true
decl_stmt|;
comment|/**          * To configure the default cache action. If an action is set in the          * message header, then the operation from the header takes precedence.          */
DECL|field|action
specifier|private
name|String
name|action
decl_stmt|;
comment|/**          * To configure the default action key. If a key is set in the message          * header, then the key from the header takes precedence.          */
DECL|field|key
specifier|private
name|Object
name|key
decl_stmt|;
comment|/**          * The cache manager          */
DECL|field|cacheManager
specifier|private
name|CacheManager
name|cacheManager
decl_stmt|;
comment|/**          * The cache manager configuration          */
DECL|field|cacheManagerConfiguration
specifier|private
name|Configuration
name|cacheManagerConfiguration
decl_stmt|;
DECL|field|eventOrdering
specifier|private
name|EventOrdering
name|eventOrdering
init|=
name|EventOrdering
operator|.
name|ORDERED
decl_stmt|;
DECL|field|eventFiring
specifier|private
name|EventFiring
name|eventFiring
init|=
name|EventFiring
operator|.
name|ASYNCHRONOUS
decl_stmt|;
DECL|field|eventTypes
specifier|private
name|Set
name|eventTypes
decl_stmt|;
comment|/**          * The default cache configuration to be used to create caches.          */
DECL|field|configuration
specifier|private
name|CacheConfiguration
name|configuration
decl_stmt|;
DECL|field|configurations
specifier|private
name|Map
name|configurations
decl_stmt|;
comment|/**          * The cache key type, default "java.lang.Object"          */
DECL|field|keyType
specifier|private
name|String
name|keyType
init|=
literal|"java.lang.Object"
decl_stmt|;
comment|/**          * The cache value type, default "java.lang.Object"          */
DECL|field|valueType
specifier|private
name|String
name|valueType
init|=
literal|"java.lang.Object"
decl_stmt|;
DECL|method|getConfigurationUri ()
specifier|public
name|String
name|getConfigurationUri
parameter_list|()
block|{
return|return
name|configurationUri
return|;
block|}
DECL|method|setConfigurationUri (String configurationUri)
specifier|public
name|void
name|setConfigurationUri
parameter_list|(
name|String
name|configurationUri
parameter_list|)
block|{
name|this
operator|.
name|configurationUri
operator|=
name|configurationUri
expr_stmt|;
block|}
annotation|@
name|Deprecated
annotation|@
name|DeprecatedConfigurationProperty
DECL|method|getConfigUri ()
specifier|public
name|String
name|getConfigUri
parameter_list|()
block|{
return|return
name|configUri
return|;
block|}
annotation|@
name|Deprecated
DECL|method|setConfigUri (String configUri)
specifier|public
name|void
name|setConfigUri
parameter_list|(
name|String
name|configUri
parameter_list|)
block|{
name|this
operator|.
name|configUri
operator|=
name|configUri
expr_stmt|;
block|}
DECL|method|getCreateCacheIfNotExist ()
specifier|public
name|Boolean
name|getCreateCacheIfNotExist
parameter_list|()
block|{
return|return
name|createCacheIfNotExist
return|;
block|}
DECL|method|setCreateCacheIfNotExist (Boolean createCacheIfNotExist)
specifier|public
name|void
name|setCreateCacheIfNotExist
parameter_list|(
name|Boolean
name|createCacheIfNotExist
parameter_list|)
block|{
name|this
operator|.
name|createCacheIfNotExist
operator|=
name|createCacheIfNotExist
expr_stmt|;
block|}
DECL|method|getAction ()
specifier|public
name|String
name|getAction
parameter_list|()
block|{
return|return
name|action
return|;
block|}
DECL|method|setAction (String action)
specifier|public
name|void
name|setAction
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|Object
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|setKey (Object key)
specifier|public
name|void
name|setKey
parameter_list|(
name|Object
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
DECL|method|getCacheManager ()
specifier|public
name|CacheManager
name|getCacheManager
parameter_list|()
block|{
return|return
name|cacheManager
return|;
block|}
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
name|cacheManager
operator|=
name|cacheManager
expr_stmt|;
block|}
DECL|method|getCacheManagerConfiguration ()
specifier|public
name|Configuration
name|getCacheManagerConfiguration
parameter_list|()
block|{
return|return
name|cacheManagerConfiguration
return|;
block|}
DECL|method|setCacheManagerConfiguration ( Configuration cacheManagerConfiguration)
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
name|cacheManagerConfiguration
operator|=
name|cacheManagerConfiguration
expr_stmt|;
block|}
DECL|method|getEventOrdering ()
specifier|public
name|EventOrdering
name|getEventOrdering
parameter_list|()
block|{
return|return
name|eventOrdering
return|;
block|}
DECL|method|setEventOrdering (EventOrdering eventOrdering)
specifier|public
name|void
name|setEventOrdering
parameter_list|(
name|EventOrdering
name|eventOrdering
parameter_list|)
block|{
name|this
operator|.
name|eventOrdering
operator|=
name|eventOrdering
expr_stmt|;
block|}
DECL|method|getEventFiring ()
specifier|public
name|EventFiring
name|getEventFiring
parameter_list|()
block|{
return|return
name|eventFiring
return|;
block|}
DECL|method|setEventFiring (EventFiring eventFiring)
specifier|public
name|void
name|setEventFiring
parameter_list|(
name|EventFiring
name|eventFiring
parameter_list|)
block|{
name|this
operator|.
name|eventFiring
operator|=
name|eventFiring
expr_stmt|;
block|}
DECL|method|getEventTypes ()
specifier|public
name|Set
name|getEventTypes
parameter_list|()
block|{
return|return
name|eventTypes
return|;
block|}
DECL|method|setEventTypes (Set eventTypes)
specifier|public
name|void
name|setEventTypes
parameter_list|(
name|Set
name|eventTypes
parameter_list|)
block|{
name|this
operator|.
name|eventTypes
operator|=
name|eventTypes
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|CacheConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (CacheConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|CacheConfiguration
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
DECL|method|getConfigurations ()
specifier|public
name|Map
name|getConfigurations
parameter_list|()
block|{
return|return
name|configurations
return|;
block|}
DECL|method|setConfigurations (Map configurations)
specifier|public
name|void
name|setConfigurations
parameter_list|(
name|Map
name|configurations
parameter_list|)
block|{
name|this
operator|.
name|configurations
operator|=
name|configurations
expr_stmt|;
block|}
DECL|method|getKeyType ()
specifier|public
name|String
name|getKeyType
parameter_list|()
block|{
return|return
name|keyType
return|;
block|}
DECL|method|setKeyType (String keyType)
specifier|public
name|void
name|setKeyType
parameter_list|(
name|String
name|keyType
parameter_list|)
block|{
name|this
operator|.
name|keyType
operator|=
name|keyType
expr_stmt|;
block|}
DECL|method|getValueType ()
specifier|public
name|String
name|getValueType
parameter_list|()
block|{
return|return
name|valueType
return|;
block|}
DECL|method|setValueType (String valueType)
specifier|public
name|void
name|setValueType
parameter_list|(
name|String
name|valueType
parameter_list|)
block|{
name|this
operator|.
name|valueType
operator|=
name|valueType
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

