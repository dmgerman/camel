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
name|util
operator|.
name|URISupport
import|;
end_import

begin_class
DECL|class|CacheConfiguration
specifier|public
class|class
name|CacheConfiguration
implements|implements
name|Cloneable
block|{
DECL|field|cacheName
specifier|private
name|String
name|cacheName
decl_stmt|;
DECL|field|maxElementsInMemory
specifier|private
name|int
name|maxElementsInMemory
init|=
literal|1000
decl_stmt|;
DECL|field|memoryStoreEvictionPolicy
specifier|private
name|MemoryStoreEvictionPolicy
name|memoryStoreEvictionPolicy
init|=
name|MemoryStoreEvictionPolicy
operator|.
name|LFU
decl_stmt|;
DECL|field|overflowToDisk
specifier|private
name|boolean
name|overflowToDisk
init|=
literal|true
decl_stmt|;
DECL|field|diskStorePath
specifier|private
name|String
name|diskStorePath
decl_stmt|;
DECL|field|eternal
specifier|private
name|boolean
name|eternal
decl_stmt|;
DECL|field|timeToLiveSeconds
specifier|private
name|long
name|timeToLiveSeconds
init|=
literal|300
decl_stmt|;
DECL|field|timeToIdleSeconds
specifier|private
name|long
name|timeToIdleSeconds
init|=
literal|300
decl_stmt|;
DECL|field|diskPersistent
specifier|private
name|boolean
name|diskPersistent
decl_stmt|;
DECL|field|diskExpiryThreadIntervalSeconds
specifier|private
name|long
name|diskExpiryThreadIntervalSeconds
decl_stmt|;
DECL|field|eventListenerRegistry
specifier|private
name|CacheEventListenerRegistry
name|eventListenerRegistry
init|=
operator|new
name|CacheEventListenerRegistry
argument_list|()
decl_stmt|;
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
DECL|method|CacheConfiguration (URI uri)
specifier|public
name|CacheConfiguration
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|parseURI
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
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
DECL|method|parseURI (URI uri)
specifier|public
name|void
name|parseURI
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|protocol
init|=
name|uri
operator|.
name|getScheme
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"cache"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unrecognized Cache protocol: "
operator|+
name|protocol
operator|+
literal|" for uri: "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|setCacheName
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cacheSettings
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|cacheSettings
operator|.
name|containsKey
argument_list|(
literal|"maxElementsInMemory"
argument_list|)
condition|)
block|{
name|setMaxElementsInMemory
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|cacheSettings
operator|.
name|get
argument_list|(
literal|"maxElementsInMemory"
argument_list|)
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cacheSettings
operator|.
name|containsKey
argument_list|(
literal|"overflowToDisk"
argument_list|)
condition|)
block|{
name|setOverflowToDisk
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|cacheSettings
operator|.
name|get
argument_list|(
literal|"overflowToDisk"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cacheSettings
operator|.
name|containsKey
argument_list|(
literal|"diskStorePath"
argument_list|)
condition|)
block|{
name|setDiskStorePath
argument_list|(
operator|(
name|String
operator|)
name|cacheSettings
operator|.
name|get
argument_list|(
literal|"diskStorePath"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cacheSettings
operator|.
name|containsKey
argument_list|(
literal|"eternal"
argument_list|)
condition|)
block|{
name|setEternal
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|cacheSettings
operator|.
name|get
argument_list|(
literal|"eternal"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cacheSettings
operator|.
name|containsKey
argument_list|(
literal|"timeToLiveSeconds"
argument_list|)
condition|)
block|{
name|setTimeToLiveSeconds
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|cacheSettings
operator|.
name|get
argument_list|(
literal|"timeToLiveSeconds"
argument_list|)
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cacheSettings
operator|.
name|containsKey
argument_list|(
literal|"timeToIdleSeconds"
argument_list|)
condition|)
block|{
name|setTimeToIdleSeconds
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|cacheSettings
operator|.
name|get
argument_list|(
literal|"timeToIdleSeconds"
argument_list|)
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cacheSettings
operator|.
name|containsKey
argument_list|(
literal|"diskPersistent"
argument_list|)
condition|)
block|{
name|setDiskPersistent
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|cacheSettings
operator|.
name|get
argument_list|(
literal|"diskPersistent"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cacheSettings
operator|.
name|containsKey
argument_list|(
literal|"diskExpiryThreadIntervalSeconds"
argument_list|)
condition|)
block|{
name|setDiskExpiryThreadIntervalSeconds
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|cacheSettings
operator|.
name|get
argument_list|(
literal|"diskExpiryThreadIntervalSeconds"
argument_list|)
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cacheSettings
operator|.
name|containsKey
argument_list|(
literal|"memoryStoreEvictionPolicy"
argument_list|)
condition|)
block|{
name|String
name|policy
init|=
operator|(
name|String
operator|)
name|cacheSettings
operator|.
name|get
argument_list|(
literal|"memoryStoreEvictionPolicy"
argument_list|)
decl_stmt|;
comment|// remove leading if any given as fromString uses LRU, LFU or FIFO
name|policy
operator|=
name|policy
operator|.
name|replace
argument_list|(
literal|"MemoryStoreEvictionPolicy."
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|setMemoryStoreEvictionPolicy
argument_list|(
name|MemoryStoreEvictionPolicy
operator|.
name|fromString
argument_list|(
name|policy
argument_list|)
argument_list|)
expr_stmt|;
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
block|}
end_class

end_unit

