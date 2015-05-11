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
name|Map
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
name|spi
operator|.
name|UriParams
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
name|UriPath
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
name|URISupport
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|CacheConfiguration
specifier|public
class|class
name|CacheConfiguration
implements|implements
name|Cloneable
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|cacheName
specifier|private
name|String
name|cacheName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1000"
argument_list|)
DECL|field|maxElementsInMemory
specifier|private
name|int
name|maxElementsInMemory
init|=
literal|1000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"LFU"
argument_list|,
name|enums
operator|=
literal|"LRU,LFU,FIFO"
argument_list|)
DECL|field|memoryStoreEvictionPolicy
specifier|private
name|MemoryStoreEvictionPolicy
name|memoryStoreEvictionPolicy
init|=
name|MemoryStoreEvictionPolicy
operator|.
name|LFU
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|overflowToDisk
specifier|private
name|boolean
name|overflowToDisk
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Deprecated
DECL|field|diskStorePath
specifier|private
name|String
name|diskStorePath
decl_stmt|;
annotation|@
name|UriParam
DECL|field|eternal
specifier|private
name|boolean
name|eternal
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"300"
argument_list|)
DECL|field|timeToLiveSeconds
specifier|private
name|long
name|timeToLiveSeconds
init|=
literal|300
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"300"
argument_list|)
DECL|field|timeToIdleSeconds
specifier|private
name|long
name|timeToIdleSeconds
init|=
literal|300
decl_stmt|;
annotation|@
name|UriParam
DECL|field|diskPersistent
specifier|private
name|boolean
name|diskPersistent
decl_stmt|;
annotation|@
name|UriParam
DECL|field|diskExpiryThreadIntervalSeconds
specifier|private
name|long
name|diskExpiryThreadIntervalSeconds
decl_stmt|;
annotation|@
name|UriParam
DECL|field|objectCache
specifier|private
name|boolean
name|objectCache
decl_stmt|;
annotation|@
name|UriParam
DECL|field|eventListenerRegistry
specifier|private
name|CacheEventListenerRegistry
name|eventListenerRegistry
init|=
operator|new
name|CacheEventListenerRegistry
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
DECL|field|cacheLoaderRegistry
specifier|private
name|CacheLoaderRegistry
name|cacheLoaderRegistry
init|=
operator|new
name|CacheLoaderRegistry
argument_list|()
decl_stmt|;
DECL|method|CacheConfiguration ()
specifier|public
name|CacheConfiguration
parameter_list|()
block|{     }
DECL|method|copy ()
specifier|public
name|CacheConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
name|CacheConfiguration
name|copy
init|=
operator|(
name|CacheConfiguration
operator|)
name|clone
argument_list|()
decl_stmt|;
comment|// override any properties where a reference copy isn't what we want
return|return
name|copy
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
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
comment|/**      * Name of the cache      */
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
name|int
name|getMaxElementsInMemory
parameter_list|()
block|{
return|return
name|maxElementsInMemory
return|;
block|}
comment|/**      * The number of elements that may be stored in the defined cache in memory.      */
DECL|method|setMaxElementsInMemory (int maxElementsInMemory)
specifier|public
name|void
name|setMaxElementsInMemory
parameter_list|(
name|int
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
comment|/**      * Which eviction strategy to use when maximum number of elements in memory is reached. The strategy defines      * which elements to be removed.      *<ul>      *<li>LRU - Lest Recently Used</li>      *<li>LFU - Lest Frequently Used</li>      *<li>FIFO - First In First Out</li>      *</ul>      */
DECL|method|setMemoryStoreEvictionPolicy (MemoryStoreEvictionPolicy memoryStoreEvictionPolicy)
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
DECL|method|isOverflowToDisk ()
specifier|public
name|boolean
name|isOverflowToDisk
parameter_list|()
block|{
return|return
name|overflowToDisk
return|;
block|}
comment|/**      * Specifies whether cache may overflow to disk      */
DECL|method|setOverflowToDisk (boolean overflowToDisk)
specifier|public
name|void
name|setOverflowToDisk
parameter_list|(
name|boolean
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
comment|/**      * This parameter is ignored. CacheManager sets it using setter injection.      */
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
DECL|method|isEternal ()
specifier|public
name|boolean
name|isEternal
parameter_list|()
block|{
return|return
name|eternal
return|;
block|}
comment|/**      * Sets whether elements are eternal. If eternal, timeouts are ignored and the element never expires.      */
DECL|method|setEternal (boolean eternal)
specifier|public
name|void
name|setEternal
parameter_list|(
name|boolean
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
name|long
name|getTimeToLiveSeconds
parameter_list|()
block|{
return|return
name|timeToLiveSeconds
return|;
block|}
comment|/**      * The maximum time between creation time and when an element expires. Is used only if the element is not eternal      */
DECL|method|setTimeToLiveSeconds (long timeToLiveSeconds)
specifier|public
name|void
name|setTimeToLiveSeconds
parameter_list|(
name|long
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
name|long
name|getTimeToIdleSeconds
parameter_list|()
block|{
return|return
name|timeToIdleSeconds
return|;
block|}
comment|/**      * The maximum amount of time between accesses before an element expires      */
DECL|method|setTimeToIdleSeconds (long timeToIdleSeconds)
specifier|public
name|void
name|setTimeToIdleSeconds
parameter_list|(
name|long
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
DECL|method|isDiskPersistent ()
specifier|public
name|boolean
name|isDiskPersistent
parameter_list|()
block|{
return|return
name|diskPersistent
return|;
block|}
comment|/**      * Whether the disk store persists between restarts of the application.      */
DECL|method|setDiskPersistent (boolean diskPersistent)
specifier|public
name|void
name|setDiskPersistent
parameter_list|(
name|boolean
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
name|long
name|getDiskExpiryThreadIntervalSeconds
parameter_list|()
block|{
return|return
name|diskExpiryThreadIntervalSeconds
return|;
block|}
comment|/**      * The number of seconds between runs of the disk expiry thread.      */
DECL|method|setDiskExpiryThreadIntervalSeconds (long diskExpiryThreadIntervalSeconds)
specifier|public
name|void
name|setDiskExpiryThreadIntervalSeconds
parameter_list|(
name|long
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
comment|/**      * To configure event listeners using the CacheEventListenerRegistry     */
DECL|method|setEventListenerRegistry (CacheEventListenerRegistry eventListenerRegistry)
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
comment|/**      * To configure cache loader using the CacheLoaderRegistry      */
DECL|method|setCacheLoaderRegistry (CacheLoaderRegistry cacheLoaderRegistry)
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
DECL|method|isObjectCache ()
specifier|public
name|boolean
name|isObjectCache
parameter_list|()
block|{
return|return
name|objectCache
return|;
block|}
comment|/**      * Whether to turn on allowing to store non serializable objects in the cache.      * If this option is enabled then overflow to disk cannot be enabled as well.      */
DECL|method|setObjectCache (boolean objectCache)
specifier|public
name|void
name|setObjectCache
parameter_list|(
name|boolean
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
end_class

end_unit

