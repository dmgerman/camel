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
name|io
operator|.
name|InputStream
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
name|Cache
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
name|CacheException
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
name|CacheManager
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
name|Ehcache
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
name|Element
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
name|component
operator|.
name|cache
operator|.
name|factory
operator|.
name|CacheManagerFactory
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
name|impl
operator|.
name|DefaultProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_class
DECL|class|CacheProducer
specifier|public
class|class
name|CacheProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CacheProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|config
name|CacheConfiguration
name|config
decl_stmt|;
DECL|field|cacheManager
name|CacheManager
name|cacheManager
decl_stmt|;
DECL|field|cache
name|Ehcache
name|cache
decl_stmt|;
DECL|method|CacheProducer (Endpoint endpoint, CacheConfiguration config)
specifier|public
name|CacheProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|CacheConfiguration
name|config
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"In CacheProducer.start()"
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"In CacheProducer.stop()"
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"In CacheProducer.process()"
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|body
argument_list|)
decl_stmt|;
comment|// Read InputStream into a byte[] buffer
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|is
operator|.
name|available
argument_list|()
index|]
decl_stmt|;
name|int
name|n
init|=
name|is
operator|.
name|available
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|n
condition|;
name|j
operator|++
control|)
block|{
name|buffer
index|[
name|j
index|]
operator|=
operator|(
name|byte
operator|)
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
comment|// Cache the buffer to the specified Cache against the specified key
name|cacheManager
operator|=
operator|new
name|CacheManagerFactory
argument_list|()
operator|.
name|instantiateCacheManager
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Cache Name: "
operator|+
name|config
operator|.
name|getCacheName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cacheManager
operator|.
name|cacheExists
argument_list|(
name|config
operator|.
name|getCacheName
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Found an existing cache: "
operator|+
name|config
operator|.
name|getCacheName
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Cache "
operator|+
name|config
operator|.
name|getCacheName
argument_list|()
operator|+
literal|" currently contains "
operator|+
name|cacheManager
operator|.
name|getCache
argument_list|(
name|config
operator|.
name|getCacheName
argument_list|()
argument_list|)
operator|.
name|getSize
argument_list|()
operator|+
literal|" elements"
argument_list|)
expr_stmt|;
name|cache
operator|=
name|cacheManager
operator|.
name|getCache
argument_list|(
name|config
operator|.
name|getCacheName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cache
operator|=
operator|new
name|Cache
argument_list|(
name|config
operator|.
name|getCacheName
argument_list|()
argument_list|,
name|config
operator|.
name|getMaxElementsInMemory
argument_list|()
argument_list|,
name|config
operator|.
name|getMemoryStoreEvictionPolicy
argument_list|()
argument_list|,
name|config
operator|.
name|isOverflowToDisk
argument_list|()
argument_list|,
name|config
operator|.
name|getDiskStorePath
argument_list|()
argument_list|,
name|config
operator|.
name|isEternal
argument_list|()
argument_list|,
name|config
operator|.
name|getTimeToLiveSeconds
argument_list|()
argument_list|,
name|config
operator|.
name|getTimeToIdleSeconds
argument_list|()
argument_list|,
name|config
operator|.
name|isDiskPersistent
argument_list|()
argument_list|,
name|config
operator|.
name|getDiskExpiryThreadIntervalSeconds
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|cacheManager
operator|.
name|addCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Added a new cache: "
operator|+
name|cache
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|key
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CACHE_KEY"
argument_list|)
decl_stmt|;
name|String
name|operation
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CACHE_OPERATION"
argument_list|)
decl_stmt|;
if|if
condition|(
name|operation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CacheException
argument_list|(
literal|"Operation property is not specified in the incoming exchange header."
operator|+
literal|"A valid Operation property must be set to ADD, UPDATE, DELETE, DELETEALL"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|(
name|key
operator|==
literal|null
operator|)
operator|&&
operator|(
operator|!
name|operation
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DELETEALL"
argument_list|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|CacheException
argument_list|(
literal|"Cache Key is not specified in exchange either header or URL. Unable to add objects to the cache without a Key"
argument_list|)
throw|;
block|}
name|performCacheOperation
argument_list|(
name|operation
argument_list|,
name|key
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
DECL|method|performCacheOperation (String operation, String key, byte[] buffer)
specifier|private
name|void
name|performCacheOperation
parameter_list|(
name|String
name|operation
parameter_list|,
name|String
name|key
parameter_list|,
name|byte
index|[]
name|buffer
parameter_list|)
block|{
if|if
condition|(
name|operation
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DELETEALL"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Deleting All elements from the Cache"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|removeAll
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operation
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"ADD"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Adding an element with key "
operator|+
name|key
operator|+
literal|" into the Cache"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
operator|new
name|Element
argument_list|(
name|key
argument_list|,
name|buffer
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operation
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"UPDATE"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Updating an element with key "
operator|+
name|key
operator|+
literal|" into the Cache"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
operator|new
name|Element
argument_list|(
name|key
argument_list|,
name|buffer
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operation
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DELETE"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Deleting an element with key "
operator|+
name|key
operator|+
literal|" into the Cache"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|remove
argument_list|(
name|key
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

