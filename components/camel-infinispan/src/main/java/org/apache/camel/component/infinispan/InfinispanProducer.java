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
name|impl
operator|.
name|DefaultProducer
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
name|Configuration
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
DECL|class|InfinispanProducer
specifier|public
class|class
name|InfinispanProducer
extends|extends
name|DefaultProducer
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
name|InfinispanProducer
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
DECL|method|InfinispanProducer (InfinispanEndpoint endpoint, InfinispanConfiguration configuration)
specifier|public
name|InfinispanProducer
parameter_list|(
name|InfinispanEndpoint
name|endpoint
parameter_list|,
name|InfinispanConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
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
operator|new
name|InfinispanOperation
argument_list|(
name|getCache
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|configuration
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
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
name|Configuration
name|config
init|=
operator|new
name|ConfigurationBuilder
argument_list|()
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
operator|.
name|addServers
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|cacheContainer
operator|=
operator|new
name|RemoteCacheManager
argument_list|(
name|config
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|isManagedCacheContainer
operator|=
literal|true
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getCache (Exchange exchange)
specifier|private
name|BasicCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|getCache
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|cacheName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|InfinispanConstants
operator|.
name|CACHE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cacheName
operator|==
literal|null
condition|)
block|{
name|cacheName
operator|=
name|configuration
operator|.
name|getCacheName
argument_list|()
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
name|cacheName
operator|!=
literal|null
condition|?
name|cacheContainer
operator|.
name|getCache
argument_list|(
name|cacheName
argument_list|)
else|:
name|cacheContainer
operator|.
name|getCache
argument_list|()
return|;
block|}
block|}
end_class

end_unit

