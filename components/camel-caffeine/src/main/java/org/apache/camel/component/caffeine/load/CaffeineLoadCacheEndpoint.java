begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.caffeine.load
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
name|TimeUnit
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
name|Caffeine
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
name|LoadingCache
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|CaffeineConfiguration
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
name|support
operator|.
name|DefaultEndpoint
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
name|UriEndpoint
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The caffeine-loadcache component is used for integration with Caffeine Load  * Cache.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.20.0"
argument_list|,
name|scheme
operator|=
literal|"caffeine-loadcache"
argument_list|,
name|title
operator|=
literal|"Caffeine LoadCache"
argument_list|,
name|syntax
operator|=
literal|"caffeine-loadcache:cacheName"
argument_list|,
name|label
operator|=
literal|"cache,datagrid,clustering"
argument_list|)
DECL|class|CaffeineLoadCacheEndpoint
specifier|public
class|class
name|CaffeineLoadCacheEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"the cache name"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|cacheName
specifier|private
specifier|final
name|String
name|cacheName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
specifier|final
name|CaffeineConfiguration
name|configuration
decl_stmt|;
DECL|field|cache
specifier|private
name|LoadingCache
name|cache
decl_stmt|;
DECL|method|CaffeineLoadCacheEndpoint (String uri, CaffeineLoadCacheComponent component, String cacheName, CaffeineConfiguration configuration)
name|CaffeineLoadCacheEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|CaffeineLoadCacheComponent
name|component
parameter_list|,
name|String
name|cacheName
parameter_list|,
name|CaffeineConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|cacheName
operator|=
name|cacheName
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
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|CaffeineLoadCacheProducer
argument_list|(
name|this
argument_list|,
name|this
operator|.
name|cacheName
argument_list|,
name|configuration
argument_list|,
name|cache
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
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
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getCache
argument_list|()
argument_list|)
condition|)
block|{
name|cache
operator|=
operator|(
name|LoadingCache
operator|)
name|configuration
operator|.
name|getCache
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Caffeine
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
name|Caffeine
operator|.
name|newBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getEvictionType
argument_list|()
operator|==
name|EvictionType
operator|.
name|SIZE_BASED
condition|)
block|{
name|builder
operator|.
name|initialCapacity
argument_list|(
name|configuration
operator|.
name|getInitialCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|maximumSize
argument_list|(
name|configuration
operator|.
name|getMaximumSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|configuration
operator|.
name|getEvictionType
argument_list|()
operator|==
name|EvictionType
operator|.
name|TIME_BASED
condition|)
block|{
name|builder
operator|.
name|expireAfterAccess
argument_list|(
name|configuration
operator|.
name|getExpireAfterAccessTime
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|builder
operator|.
name|expireAfterWrite
argument_list|(
name|configuration
operator|.
name|getExpireAfterWriteTime
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|isStatsEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getStatsCounter
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|recordStats
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|recordStats
argument_list|(
parameter_list|()
lambda|->
name|configuration
operator|.
name|getStatsCounter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getRemovalListener
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|removalListener
argument_list|(
name|configuration
operator|.
name|getRemovalListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|cache
operator|=
name|builder
operator|.
name|build
argument_list|(
name|configuration
operator|.
name|getCacheLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getConfiguration ()
name|CaffeineConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The caffeine-loadcache component doesn't support consumer"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

