begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client.queue
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|queue
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|io
operator|.
name|atomix
operator|.
name|catalyst
operator|.
name|concurrent
operator|.
name|Listener
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|collections
operator|.
name|DistributedQueue
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
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|AbstractAtomixClientConsumer
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
name|atomix
operator|.
name|client
operator|.
name|AtomixClientConstants
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
DECL|class|AtomixQueueConsumer
specifier|final
class|class
name|AtomixQueueConsumer
extends|extends
name|AbstractAtomixClientConsumer
argument_list|<
name|AtomixQueueEndpoint
argument_list|>
block|{
DECL|field|listeners
specifier|private
specifier|final
name|List
argument_list|<
name|Listener
argument_list|<
name|DistributedQueue
operator|.
name|ValueEvent
argument_list|<
name|Object
argument_list|>
argument_list|>
argument_list|>
name|listeners
decl_stmt|;
DECL|field|resourceName
specifier|private
specifier|final
name|String
name|resourceName
decl_stmt|;
DECL|field|resultHeader
specifier|private
specifier|final
name|String
name|resultHeader
decl_stmt|;
DECL|field|queue
specifier|private
name|DistributedQueue
argument_list|<
name|Object
argument_list|>
name|queue
decl_stmt|;
DECL|method|AtomixQueueConsumer (AtomixQueueEndpoint endpoint, Processor processor, String resourceName)
specifier|public
name|AtomixQueueConsumer
parameter_list|(
name|AtomixQueueEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|resourceName
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|listeners
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|resourceName
operator|=
name|resourceName
expr_stmt|;
name|this
operator|.
name|resultHeader
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getResultHeader
argument_list|()
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|this
operator|.
name|queue
operator|=
name|getAtomixEndpoint
argument_list|()
operator|.
name|getAtomix
argument_list|()
operator|.
name|getQueue
argument_list|(
name|resourceName
argument_list|,
operator|new
name|DistributedQueue
operator|.
name|Config
argument_list|(
name|getAtomixEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getResourceOptions
argument_list|(
name|resourceName
argument_list|)
argument_list|)
argument_list|,
operator|new
name|DistributedQueue
operator|.
name|Options
argument_list|(
name|getAtomixEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getResourceConfig
argument_list|(
name|resourceName
argument_list|)
argument_list|)
argument_list|)
operator|.
name|join
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Subscribe to events for queue: {}"
argument_list|,
name|resourceName
argument_list|)
expr_stmt|;
name|this
operator|.
name|listeners
operator|.
name|add
argument_list|(
name|this
operator|.
name|queue
operator|.
name|onAdd
argument_list|(
name|this
operator|::
name|onEvent
argument_list|)
operator|.
name|join
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|listeners
operator|.
name|add
argument_list|(
name|this
operator|.
name|queue
operator|.
name|onRemove
argument_list|(
name|this
operator|::
name|onEvent
argument_list|)
operator|.
name|join
argument_list|()
argument_list|)
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
comment|// close listeners
name|listeners
operator|.
name|forEach
argument_list|(
name|Listener
operator|::
name|close
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
comment|// ********************************************
comment|// Event handler
comment|// ********************************************
DECL|method|onEvent (DistributedQueue.ValueEvent<Object> event)
specifier|private
name|void
name|onEvent
parameter_list|(
name|DistributedQueue
operator|.
name|ValueEvent
argument_list|<
name|Object
argument_list|>
name|event
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|AtomixClientConstants
operator|.
name|EVENT_TYPE
argument_list|,
name|event
operator|.
name|type
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|resultHeader
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|event
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|resultHeader
argument_list|,
name|event
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

