begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cache.springboot
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
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|store
operator|.
name|MemoryStoreEvictionPolicy
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
name|cache
operator|.
name|CacheEventListenerRegistry
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
name|cache
operator|.
name|CacheLoaderRegistry
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
comment|/**  * The cache component enables you to perform caching operations using EHCache  * as the Cache Implementation.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.cache"
argument_list|)
DECL|class|CacheComponentConfiguration
specifier|public
class|class
name|CacheComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the cache component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To use the given CacheManagerFactory for creating the CacheManager. By      * default the DefaultCacheManagerFactory is used. The option is a      * org.apache.camel.component.cache.CacheManagerFactory type.      */
DECL|field|cacheManagerFactory
specifier|private
name|String
name|cacheManagerFactory
decl_stmt|;
comment|/**      * Sets the Cache configuration      */
DECL|field|configuration
specifier|private
name|CacheConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Sets the location of the ehcache.xml file to load from classpath or file      * system. By default the file is loaded from classpath:ehcache.xml      */
DECL|field|configurationFile
specifier|private
name|String
name|configurationFile
init|=
literal|"classpath:ehcache.xml"
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getCacheManagerFactory ()
specifier|public
name|String
name|getCacheManagerFactory
parameter_list|()
block|{
return|return
name|cacheManagerFactory
return|;
block|}
DECL|method|setCacheManagerFactory (String cacheManagerFactory)
specifier|public
name|void
name|setCacheManagerFactory
parameter_list|(
name|String
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
DECL|method|getConfiguration ()
specifier|public
name|CacheConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( CacheConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|CacheConfigurationNestedConfiguration
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
DECL|method|getConfigurationFile ()
specifier|public
name|String
name|getConfigurationFile
parameter_list|()
block|{
return|return
name|configurationFile
return|;
block|}
DECL|method|setConfigurationFile (String configurationFile)
specifier|public
name|void
name|setConfigurationFile
parameter_list|(
name|String
name|configurationFile
parameter_list|)
block|{
name|this
operator|.
name|configurationFile
operator|=
name|configurationFile
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
DECL|class|CacheConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|CacheConfigurationNestedConfiguration
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
name|cache
operator|.
name|CacheConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * Name of the cache          */
DECL|field|cacheName
specifier|private
name|String
name|cacheName
decl_stmt|;
comment|/**          * The number of elements that may be stored in the defined cache in          * memory.          */
DECL|field|maxElementsInMemory
specifier|private
name|Integer
name|maxElementsInMemory
init|=
literal|1000
decl_stmt|;
comment|/**          * Which eviction strategy to use when maximum number of elements in          * memory is reached. The strategy defines which elements to be removed.          *<ul>          *<li>LRU - Lest Recently Used</li>          *<li>LFU - Lest Frequently Used</li>          *<li>FIFO - First In First Out</li>          *</ul>          */
DECL|field|memoryStoreEvictionPolicy
specifier|private
name|MemoryStoreEvictionPolicy
name|memoryStoreEvictionPolicy
decl_stmt|;
comment|/**          * Specifies whether cache may overflow to disk          */
DECL|field|overflowToDisk
specifier|private
name|Boolean
name|overflowToDisk
init|=
literal|true
decl_stmt|;
comment|/**          * This parameter is ignored. CacheManager sets it using setter          * injection.          */
annotation|@
name|Deprecated
DECL|field|diskStorePath
specifier|private
name|String
name|diskStorePath
decl_stmt|;
comment|/**          * Sets whether elements are eternal. If eternal, timeouts are ignored          * and the element never expires.          */
DECL|field|eternal
specifier|private
name|Boolean
name|eternal
init|=
literal|false
decl_stmt|;
comment|/**          * The maximum time between creation time and when an element expires.          * Is used only if the element is not eternal          */
DECL|field|timeToLiveSeconds
specifier|private
name|Long
name|timeToLiveSeconds
init|=
literal|300L
decl_stmt|;
comment|/**          * The maximum amount of time between accesses before an element expires          */
DECL|field|timeToIdleSeconds
specifier|private
name|Long
name|timeToIdleSeconds
init|=
literal|300L
decl_stmt|;
comment|/**          * Whether the disk store persists between restarts of the application.          */
DECL|field|diskPersistent
specifier|private
name|Boolean
name|diskPersistent
init|=
literal|false
decl_stmt|;
comment|/**          * The number of seconds between runs of the disk expiry thread.          */
DECL|field|diskExpiryThreadIntervalSeconds
specifier|private
name|Long
name|diskExpiryThreadIntervalSeconds
decl_stmt|;
comment|/**          * To configure event listeners using the CacheEventListenerRegistry          */
annotation|@
name|NestedConfigurationProperty
DECL|field|eventListenerRegistry
specifier|private
name|CacheEventListenerRegistry
name|eventListenerRegistry
decl_stmt|;
comment|/**          * To configure cache loader using the CacheLoaderRegistry          */
annotation|@
name|NestedConfigurationProperty
DECL|field|cacheLoaderRegistry
specifier|private
name|CacheLoaderRegistry
name|cacheLoaderRegistry
decl_stmt|;
comment|/**          * Whether to turn on allowing to store non serializable objects in the          * cache. If this option is enabled then overflow to disk cannot be          * enabled as well.          */
DECL|field|objectCache
specifier|private
name|Boolean
name|objectCache
init|=
literal|false
decl_stmt|;
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
DECL|method|getMaxElementsInMemory ()
specifier|public
name|Integer
name|getMaxElementsInMemory
parameter_list|()
block|{
return|return
name|maxElementsInMemory
return|;
block|}
DECL|method|setMaxElementsInMemory (Integer maxElementsInMemory)
specifier|public
name|void
name|setMaxElementsInMemory
parameter_list|(
name|Integer
name|maxElementsInMemory
parameter_list|)
block|{
name|this
operator|.
name|maxElementsInMemory
operator|=
name|maxElementsInMemory
expr_stmt|;
block|}
DECL|method|getMemoryStoreEvictionPolicy ()
specifier|public
name|MemoryStoreEvictionPolicy
name|getMemoryStoreEvictionPolicy
parameter_list|()
block|{
return|return
name|memoryStoreEvictionPolicy
return|;
block|}
DECL|method|setMemoryStoreEvictionPolicy ( MemoryStoreEvictionPolicy memoryStoreEvictionPolicy)
specifier|public
name|void
name|setMemoryStoreEvictionPolicy
parameter_list|(
name|MemoryStoreEvictionPolicy
name|memoryStoreEvictionPolicy
parameter_list|)
block|{
name|this
operator|.
name|memoryStoreEvictionPolicy
operator|=
name|memoryStoreEvictionPolicy
expr_stmt|;
block|}
DECL|method|getOverflowToDisk ()
specifier|public
name|Boolean
name|getOverflowToDisk
parameter_list|()
block|{
return|return
name|overflowToDisk
return|;
block|}
DECL|method|setOverflowToDisk (Boolean overflowToDisk)
specifier|public
name|void
name|setOverflowToDisk
parameter_list|(
name|Boolean
name|overflowToDisk
parameter_list|)
block|{
name|this
operator|.
name|overflowToDisk
operator|=
name|overflowToDisk
expr_stmt|;
block|}
annotation|@
name|Deprecated
annotation|@
name|DeprecatedConfigurationProperty
DECL|method|getDiskStorePath ()
specifier|public
name|String
name|getDiskStorePath
parameter_list|()
block|{
return|return
name|diskStorePath
return|;
block|}
annotation|@
name|Deprecated
DECL|method|setDiskStorePath (String diskStorePath)
specifier|public
name|void
name|setDiskStorePath
parameter_list|(
name|String
name|diskStorePath
parameter_list|)
block|{
name|this
operator|.
name|diskStorePath
operator|=
name|diskStorePath
expr_stmt|;
block|}
DECL|method|getEternal ()
specifier|public
name|Boolean
name|getEternal
parameter_list|()
block|{
return|return
name|eternal
return|;
block|}
DECL|method|setEternal (Boolean eternal)
specifier|public
name|void
name|setEternal
parameter_list|(
name|Boolean
name|eternal
parameter_list|)
block|{
name|this
operator|.
name|eternal
operator|=
name|eternal
expr_stmt|;
block|}
DECL|method|getTimeToLiveSeconds ()
specifier|public
name|Long
name|getTimeToLiveSeconds
parameter_list|()
block|{
return|return
name|timeToLiveSeconds
return|;
block|}
DECL|method|setTimeToLiveSeconds (Long timeToLiveSeconds)
specifier|public
name|void
name|setTimeToLiveSeconds
parameter_list|(
name|Long
name|timeToLiveSeconds
parameter_list|)
block|{
name|this
operator|.
name|timeToLiveSeconds
operator|=
name|timeToLiveSeconds
expr_stmt|;
block|}
DECL|method|getTimeToIdleSeconds ()
specifier|public
name|Long
name|getTimeToIdleSeconds
parameter_list|()
block|{
return|return
name|timeToIdleSeconds
return|;
block|}
DECL|method|setTimeToIdleSeconds (Long timeToIdleSeconds)
specifier|public
name|void
name|setTimeToIdleSeconds
parameter_list|(
name|Long
name|timeToIdleSeconds
parameter_list|)
block|{
name|this
operator|.
name|timeToIdleSeconds
operator|=
name|timeToIdleSeconds
expr_stmt|;
block|}
DECL|method|getDiskPersistent ()
specifier|public
name|Boolean
name|getDiskPersistent
parameter_list|()
block|{
return|return
name|diskPersistent
return|;
block|}
DECL|method|setDiskPersistent (Boolean diskPersistent)
specifier|public
name|void
name|setDiskPersistent
parameter_list|(
name|Boolean
name|diskPersistent
parameter_list|)
block|{
name|this
operator|.
name|diskPersistent
operator|=
name|diskPersistent
expr_stmt|;
block|}
DECL|method|getDiskExpiryThreadIntervalSeconds ()
specifier|public
name|Long
name|getDiskExpiryThreadIntervalSeconds
parameter_list|()
block|{
return|return
name|diskExpiryThreadIntervalSeconds
return|;
block|}
DECL|method|setDiskExpiryThreadIntervalSeconds ( Long diskExpiryThreadIntervalSeconds)
specifier|public
name|void
name|setDiskExpiryThreadIntervalSeconds
parameter_list|(
name|Long
name|diskExpiryThreadIntervalSeconds
parameter_list|)
block|{
name|this
operator|.
name|diskExpiryThreadIntervalSeconds
operator|=
name|diskExpiryThreadIntervalSeconds
expr_stmt|;
block|}
DECL|method|getEventListenerRegistry ()
specifier|public
name|CacheEventListenerRegistry
name|getEventListenerRegistry
parameter_list|()
block|{
return|return
name|eventListenerRegistry
return|;
block|}
DECL|method|setEventListenerRegistry ( CacheEventListenerRegistry eventListenerRegistry)
specifier|public
name|void
name|setEventListenerRegistry
parameter_list|(
name|CacheEventListenerRegistry
name|eventListenerRegistry
parameter_list|)
block|{
name|this
operator|.
name|eventListenerRegistry
operator|=
name|eventListenerRegistry
expr_stmt|;
block|}
DECL|method|getCacheLoaderRegistry ()
specifier|public
name|CacheLoaderRegistry
name|getCacheLoaderRegistry
parameter_list|()
block|{
return|return
name|cacheLoaderRegistry
return|;
block|}
DECL|method|setCacheLoaderRegistry ( CacheLoaderRegistry cacheLoaderRegistry)
specifier|public
name|void
name|setCacheLoaderRegistry
parameter_list|(
name|CacheLoaderRegistry
name|cacheLoaderRegistry
parameter_list|)
block|{
name|this
operator|.
name|cacheLoaderRegistry
operator|=
name|cacheLoaderRegistry
expr_stmt|;
block|}
DECL|method|getObjectCache ()
specifier|public
name|Boolean
name|getObjectCache
parameter_list|()
block|{
return|return
name|objectCache
return|;
block|}
DECL|method|setObjectCache (Boolean objectCache)
specifier|public
name|void
name|setObjectCache
parameter_list|(
name|Boolean
name|objectCache
parameter_list|)
block|{
name|this
operator|.
name|objectCache
operator|=
name|objectCache
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

