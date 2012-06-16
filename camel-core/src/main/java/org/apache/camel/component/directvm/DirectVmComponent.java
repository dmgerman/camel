begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.directvm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|directvm
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|impl
operator|.
name|DefaultComponent
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link DirectVmEndpoint}. It holds the  * list of named direct-vm endpoints.  */
end_comment

begin_class
DECL|class|DirectVmComponent
specifier|public
class|class
name|DirectVmComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|START_COUNTER
specifier|private
specifier|static
specifier|final
name|AtomicInteger
name|START_COUNTER
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
comment|// must keep a map of consumers on the component to ensure endpoints can lookup old consumers
comment|// later in case the DirectVmEndpoint was re-created due the old was evicted from the endpoints LRUCache
comment|// on DefaultCamelContext
DECL|field|CONSUMERS
specifier|private
specifier|static
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|DirectVmConsumer
argument_list|>
name|CONSUMERS
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|DirectVmConsumer
argument_list|>
argument_list|()
decl_stmt|;
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
name|DirectVmEndpoint
name|answer
init|=
operator|new
name|DirectVmEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|answer
operator|.
name|configureProperties
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getConsumer (DirectVmEndpoint endpoint)
specifier|public
name|DirectVmConsumer
name|getConsumer
parameter_list|(
name|DirectVmEndpoint
name|endpoint
parameter_list|)
block|{
name|String
name|key
init|=
name|getConsumerKey
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|CONSUMERS
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|addConsumer (DirectVmEndpoint endpoint, DirectVmConsumer consumer)
specifier|public
name|void
name|addConsumer
parameter_list|(
name|DirectVmEndpoint
name|endpoint
parameter_list|,
name|DirectVmConsumer
name|consumer
parameter_list|)
block|{
name|String
name|key
init|=
name|getConsumerKey
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|DirectVmConsumer
name|existing
init|=
name|CONSUMERS
operator|.
name|putIfAbsent
argument_list|(
name|key
argument_list|,
name|consumer
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
operator|!=
literal|null
condition|)
block|{
name|String
name|contextId
init|=
name|existing
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"A consumer "
operator|+
name|existing
operator|+
literal|" already exists from CamelContext: "
operator|+
name|contextId
operator|+
literal|". Multiple consumers not supported"
argument_list|)
throw|;
block|}
block|}
DECL|method|removeConsumer (DirectVmEndpoint endpoint, DirectVmConsumer consumer)
specifier|public
name|void
name|removeConsumer
parameter_list|(
name|DirectVmEndpoint
name|endpoint
parameter_list|,
name|DirectVmConsumer
name|consumer
parameter_list|)
block|{
name|String
name|key
init|=
name|getConsumerKey
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|CONSUMERS
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
DECL|method|getConsumerKey (String uri)
specifier|private
specifier|static
name|String
name|getConsumerKey
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
name|uri
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
comment|// strip parameters
name|uri
operator|=
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|uri
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|uri
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|START_COUNTER
operator|.
name|incrementAndGet
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
name|START_COUNTER
operator|.
name|decrementAndGet
argument_list|()
operator|<=
literal|0
condition|)
block|{
comment|// clear queues when no more direct-vm components in use
name|CONSUMERS
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

