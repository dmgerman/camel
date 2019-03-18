begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.caffeine.load.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|caffeine
operator|.
name|load
operator|.
name|springboot
package|;
end_package

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
name|com
operator|.
name|github
operator|.
name|benmanes
operator|.
name|caffeine
operator|.
name|cache
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|benmanes
operator|.
name|caffeine
operator|.
name|cache
operator|.
name|CacheLoader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|benmanes
operator|.
name|caffeine
operator|.
name|cache
operator|.
name|RemovalListener
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|benmanes
operator|.
name|caffeine
operator|.
name|cache
operator|.
name|stats
operator|.
name|StatsCounter
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
name|caffeine
operator|.
name|EvictionType
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

begin_comment
comment|/**  * The caffeine-loadcache component is used for integration with Caffeine Load  * Cache.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.caffeine-loadcache"
argument_list|)
DECL|class|CaffeineLoadCacheComponentConfiguration
specifier|public
class|class
name|CaffeineLoadCacheComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the caffeine-loadcache component.      * This is enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * Sets the global component configuration      */
DECL|field|configuration
specifier|private
name|CaffeineConfigurationNestedConfiguration
name|configuration
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
name|CaffeineConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( CaffeineConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|CaffeineConfigurationNestedConfiguration
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
DECL|class|CaffeineConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|CaffeineConfigurationNestedConfiguration
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
name|caffeine
operator|.
name|CaffeineConfiguration
operator|.
name|class
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
comment|/**          * The cache key type, default "java.lang.Object"          */
DECL|field|keyType
specifier|private
name|Class
name|keyType
init|=
name|java
operator|.
name|lang
operator|.
name|Object
operator|.
name|class
decl_stmt|;
comment|/**          * The cache value type, default "java.lang.Object"          */
DECL|field|valueType
specifier|private
name|Class
name|valueType
init|=
name|java
operator|.
name|lang
operator|.
name|Object
operator|.
name|class
decl_stmt|;
comment|/**          * To configure an already instantiated cache to be used          */
DECL|field|cache
specifier|private
name|Cache
name|cache
decl_stmt|;
comment|/**          * To configure a CacheLoader in case of a LoadCache use          */
DECL|field|cacheLoader
specifier|private
name|CacheLoader
name|cacheLoader
decl_stmt|;
comment|/**          * To enable stats on the cache          */
DECL|field|statsEnabled
specifier|private
name|Boolean
name|statsEnabled
init|=
literal|false
decl_stmt|;
comment|/**          * Set the initial Capacity for the cache          */
DECL|field|initialCapacity
specifier|private
name|Integer
name|initialCapacity
init|=
literal|10000
decl_stmt|;
comment|/**          * Set the maximum size for the cache          */
DECL|field|maximumSize
specifier|private
name|Integer
name|maximumSize
init|=
literal|10000
decl_stmt|;
comment|/**          * Set the eviction Type for this cache          */
DECL|field|evictionType
specifier|private
name|EvictionType
name|evictionType
init|=
name|EvictionType
operator|.
name|SIZE_BASED
decl_stmt|;
comment|/**          * Set the expire After Access Time in case of time based Eviction (in          * seconds)          */
DECL|field|expireAfterAccessTime
specifier|private
name|Integer
name|expireAfterAccessTime
init|=
literal|300
decl_stmt|;
comment|/**          * Set the expire After Access Write in case of time based Eviction (in          * seconds)          */
DECL|field|expireAfterWriteTime
specifier|private
name|Integer
name|expireAfterWriteTime
init|=
literal|300
decl_stmt|;
comment|/**          * Set a specific removal Listener for the cache          */
DECL|field|removalListener
specifier|private
name|RemovalListener
name|removalListener
decl_stmt|;
comment|/**          * Set a specific Stats Counter for the cache stats          */
DECL|field|statsCounter
specifier|private
name|StatsCounter
name|statsCounter
decl_stmt|;
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
DECL|method|getKeyType ()
specifier|public
name|Class
name|getKeyType
parameter_list|()
block|{
return|return
name|keyType
return|;
block|}
DECL|method|setKeyType (Class keyType)
specifier|public
name|void
name|setKeyType
parameter_list|(
name|Class
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
name|Class
name|getValueType
parameter_list|()
block|{
return|return
name|valueType
return|;
block|}
DECL|method|setValueType (Class valueType)
specifier|public
name|void
name|setValueType
parameter_list|(
name|Class
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
DECL|method|getCache ()
specifier|public
name|Cache
name|getCache
parameter_list|()
block|{
return|return
name|cache
return|;
block|}
DECL|method|setCache (Cache cache)
specifier|public
name|void
name|setCache
parameter_list|(
name|Cache
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
DECL|method|getCacheLoader ()
specifier|public
name|CacheLoader
name|getCacheLoader
parameter_list|()
block|{
return|return
name|cacheLoader
return|;
block|}
DECL|method|setCacheLoader (CacheLoader cacheLoader)
specifier|public
name|void
name|setCacheLoader
parameter_list|(
name|CacheLoader
name|cacheLoader
parameter_list|)
block|{
name|this
operator|.
name|cacheLoader
operator|=
name|cacheLoader
expr_stmt|;
block|}
DECL|method|getStatsEnabled ()
specifier|public
name|Boolean
name|getStatsEnabled
parameter_list|()
block|{
return|return
name|statsEnabled
return|;
block|}
DECL|method|setStatsEnabled (Boolean statsEnabled)
specifier|public
name|void
name|setStatsEnabled
parameter_list|(
name|Boolean
name|statsEnabled
parameter_list|)
block|{
name|this
operator|.
name|statsEnabled
operator|=
name|statsEnabled
expr_stmt|;
block|}
DECL|method|getInitialCapacity ()
specifier|public
name|Integer
name|getInitialCapacity
parameter_list|()
block|{
return|return
name|initialCapacity
return|;
block|}
DECL|method|setInitialCapacity (Integer initialCapacity)
specifier|public
name|void
name|setInitialCapacity
parameter_list|(
name|Integer
name|initialCapacity
parameter_list|)
block|{
name|this
operator|.
name|initialCapacity
operator|=
name|initialCapacity
expr_stmt|;
block|}
DECL|method|getMaximumSize ()
specifier|public
name|Integer
name|getMaximumSize
parameter_list|()
block|{
return|return
name|maximumSize
return|;
block|}
DECL|method|setMaximumSize (Integer maximumSize)
specifier|public
name|void
name|setMaximumSize
parameter_list|(
name|Integer
name|maximumSize
parameter_list|)
block|{
name|this
operator|.
name|maximumSize
operator|=
name|maximumSize
expr_stmt|;
block|}
DECL|method|getEvictionType ()
specifier|public
name|EvictionType
name|getEvictionType
parameter_list|()
block|{
return|return
name|evictionType
return|;
block|}
DECL|method|setEvictionType (EvictionType evictionType)
specifier|public
name|void
name|setEvictionType
parameter_list|(
name|EvictionType
name|evictionType
parameter_list|)
block|{
name|this
operator|.
name|evictionType
operator|=
name|evictionType
expr_stmt|;
block|}
DECL|method|getExpireAfterAccessTime ()
specifier|public
name|Integer
name|getExpireAfterAccessTime
parameter_list|()
block|{
return|return
name|expireAfterAccessTime
return|;
block|}
DECL|method|setExpireAfterAccessTime (Integer expireAfterAccessTime)
specifier|public
name|void
name|setExpireAfterAccessTime
parameter_list|(
name|Integer
name|expireAfterAccessTime
parameter_list|)
block|{
name|this
operator|.
name|expireAfterAccessTime
operator|=
name|expireAfterAccessTime
expr_stmt|;
block|}
DECL|method|getExpireAfterWriteTime ()
specifier|public
name|Integer
name|getExpireAfterWriteTime
parameter_list|()
block|{
return|return
name|expireAfterWriteTime
return|;
block|}
DECL|method|setExpireAfterWriteTime (Integer expireAfterWriteTime)
specifier|public
name|void
name|setExpireAfterWriteTime
parameter_list|(
name|Integer
name|expireAfterWriteTime
parameter_list|)
block|{
name|this
operator|.
name|expireAfterWriteTime
operator|=
name|expireAfterWriteTime
expr_stmt|;
block|}
DECL|method|getRemovalListener ()
specifier|public
name|RemovalListener
name|getRemovalListener
parameter_list|()
block|{
return|return
name|removalListener
return|;
block|}
DECL|method|setRemovalListener (RemovalListener removalListener)
specifier|public
name|void
name|setRemovalListener
parameter_list|(
name|RemovalListener
name|removalListener
parameter_list|)
block|{
name|this
operator|.
name|removalListener
operator|=
name|removalListener
expr_stmt|;
block|}
DECL|method|getStatsCounter ()
specifier|public
name|StatsCounter
name|getStatsCounter
parameter_list|()
block|{
return|return
name|statsCounter
return|;
block|}
DECL|method|setStatsCounter (StatsCounter statsCounter)
specifier|public
name|void
name|setStatsCounter
parameter_list|(
name|StatsCounter
name|statsCounter
parameter_list|)
block|{
name|this
operator|.
name|statsCounter
operator|=
name|statsCounter
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

