begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.etcd.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|etcd
operator|.
name|cloud
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|TimeUnit
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
name|TimeoutException
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
name|atomic
operator|.
name|AtomicLong
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
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|mousio
operator|.
name|client
operator|.
name|promises
operator|.
name|ResponsePromise
import|;
end_import

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|responses
operator|.
name|EtcdException
import|;
end_import

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|responses
operator|.
name|EtcdKeysResponse
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
name|cloud
operator|.
name|ServiceDefinition
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
name|etcd
operator|.
name|EtcdConfiguration
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
name|etcd
operator|.
name|EtcdHelper
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
DECL|class|EtcdWatchServiceDiscovery
specifier|public
class|class
name|EtcdWatchServiceDiscovery
extends|extends
name|EtcdServiceDiscovery
implements|implements
name|ResponsePromise
operator|.
name|IsSimplePromiseResponseHandler
argument_list|<
name|EtcdKeysResponse
argument_list|>
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
name|EtcdWatchServiceDiscovery
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|serversRef
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
argument_list|>
name|serversRef
decl_stmt|;
DECL|field|index
specifier|private
specifier|final
name|AtomicLong
name|index
decl_stmt|;
DECL|field|servicePath
specifier|private
specifier|final
name|String
name|servicePath
decl_stmt|;
DECL|method|EtcdWatchServiceDiscovery (EtcdConfiguration configuration)
specifier|public
name|EtcdWatchServiceDiscovery
parameter_list|(
name|EtcdConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|this
operator|.
name|serversRef
operator|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|index
operator|=
operator|new
name|AtomicLong
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|this
operator|.
name|servicePath
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
operator|.
name|getServicePath
argument_list|()
argument_list|,
literal|"servicePath"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getServices (String name)
specifier|public
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|getServices
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|servers
init|=
name|serversRef
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|servers
operator|==
literal|null
condition|)
block|{
name|serversRef
operator|.
name|set
argument_list|(
name|getServices
argument_list|()
argument_list|)
expr_stmt|;
name|watch
argument_list|()
expr_stmt|;
block|}
return|return
name|serversRef
operator|.
name|get
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|s
lambda|->
name|name
operator|.
name|equalsIgnoreCase
argument_list|(
name|s
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
comment|// *************************************************************************
comment|// Watch
comment|// *************************************************************************
annotation|@
name|Override
DECL|method|onResponse (ResponsePromise<EtcdKeysResponse> promise)
specifier|public
name|void
name|onResponse
parameter_list|(
name|ResponsePromise
argument_list|<
name|EtcdKeysResponse
argument_list|>
name|promise
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
return|return;
block|}
name|Throwable
name|throwable
init|=
name|promise
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|throwable
operator|instanceof
name|EtcdException
condition|)
block|{
name|EtcdException
name|exception
init|=
operator|(
name|EtcdException
operator|)
name|throwable
decl_stmt|;
if|if
condition|(
name|EtcdHelper
operator|.
name|isOutdatedIndexException
argument_list|(
name|exception
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Outdated index, key={}, cause={}"
argument_list|,
name|servicePath
argument_list|,
name|exception
operator|.
name|etcdCause
argument_list|)
expr_stmt|;
name|index
operator|.
name|set
argument_list|(
name|exception
operator|.
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
try|try
block|{
name|EtcdKeysResponse
name|response
init|=
name|promise
operator|.
name|get
argument_list|()
decl_stmt|;
name|EtcdHelper
operator|.
name|setIndex
argument_list|(
name|index
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|serversRef
operator|.
name|set
argument_list|(
name|getServices
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Timeout watching for {}"
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getServicePath
argument_list|()
argument_list|)
expr_stmt|;
name|throwable
operator|=
literal|null
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|throwable
operator|=
name|e
expr_stmt|;
block|}
block|}
if|if
condition|(
name|throwable
operator|==
literal|null
condition|)
block|{
name|watch
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|throwable
argument_list|)
throw|;
block|}
block|}
DECL|method|watch ()
specifier|private
name|void
name|watch
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
return|return;
block|}
try|try
block|{
name|getClient
argument_list|()
operator|.
name|get
argument_list|(
name|servicePath
argument_list|)
operator|.
name|recursive
argument_list|()
operator|.
name|waitForChange
argument_list|(
name|index
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|timeout
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|addListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
block|}
end_class

end_unit

