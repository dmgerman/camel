begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.vm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|vm
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|BlockingQueue
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
name|component
operator|.
name|seda
operator|.
name|QueueReference
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
name|seda
operator|.
name|SedaComponent
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
name|seda
operator|.
name|SedaEndpoint
import|;
end_import

begin_comment
comment|/**  * An implementation of the<a href="http://camel.apache.org/vm.html">VM components</a>  * for asynchronous SEDA exchanges on a {@link BlockingQueue} within the classloader tree containing  * the camel-core.jar. i.e. to handle communicating across CamelContext instances and possibly across  * web application contexts, providing that camel-core.jar is on the system classpath.  *  * @version   */
end_comment

begin_class
DECL|class|VmComponent
specifier|public
class|class
name|VmComponent
extends|extends
name|SedaComponent
block|{
DECL|field|QUEUES
specifier|protected
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|QueueReference
argument_list|>
name|QUEUES
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|QueueReference
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|ENDPOINTS
specifier|protected
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|SedaEndpoint
argument_list|>
name|ENDPOINTS
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|SedaEndpoint
argument_list|>
argument_list|()
decl_stmt|;
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
annotation|@
name|Override
DECL|method|getQueues ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|QueueReference
argument_list|>
name|getQueues
parameter_list|()
block|{
return|return
name|QUEUES
return|;
block|}
annotation|@
name|Override
DECL|method|getQueueReference (String key)
specifier|public
name|QueueReference
name|getQueueReference
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|QUEUES
operator|.
name|get
argument_list|(
name|key
argument_list|)
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
comment|// clear queues when no more vm components in use
name|getQueues
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// also clear endpoints
name|ENDPOINTS
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
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
if|if
condition|(
name|ENDPOINTS
operator|.
name|containsKey
argument_list|(
name|uri
argument_list|)
condition|)
block|{
return|return
name|ENDPOINTS
operator|.
name|get
argument_list|(
name|uri
argument_list|)
return|;
block|}
name|SedaEndpoint
name|answer
init|=
operator|(
name|SedaEndpoint
operator|)
name|super
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|ENDPOINTS
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

