begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.soroushbot.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|soroushbot
operator|.
name|component
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
name|soroushbot
operator|.
name|models
operator|.
name|SoroushMessage
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
name|soroushbot
operator|.
name|utils
operator|.
name|CongestionException
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
name|soroushbot
operator|.
name|utils
operator|.
name|MultiQueueWithTopicThreadPool
import|;
end_import

begin_comment
comment|/**  * create a thread pool and process each message using one of threads,  * it is guaranteed that all message from a same person will processed by the same thread.  * thread pool size could be configured using {@link SoroushBotEndpoint#getConcurrentConsumers()}  * this consumer support both Sync and Async processors.  */
end_comment

begin_class
DECL|class|SoroushBotMultiThreadConsumer
specifier|public
class|class
name|SoroushBotMultiThreadConsumer
extends|extends
name|SoroushBotAbstractConsumer
block|{
comment|/**      * Since we want that every message from the same user to be processed one by one,      * i.e. no 2 message from the same user execute concurrently,      * we create a new simple thread pool that let us select a thread by a topic.      * It guarantees that all tasks with the same topic execute in the same thread.      * We use userIds as the topic of each task.      */
DECL|field|threadPool
name|MultiQueueWithTopicThreadPool
name|threadPool
decl_stmt|;
DECL|method|SoroushBotMultiThreadConsumer (SoroushBotEndpoint endpoint, Processor processor)
specifier|public
name|SoroushBotMultiThreadConsumer
parameter_list|(
name|SoroushBotEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|threadPool
operator|=
operator|new
name|MultiQueueWithTopicThreadPool
argument_list|(
name|endpoint
operator|.
name|getConcurrentConsumers
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getQueueCapacityPerThread
argument_list|()
argument_list|,
literal|"Soroush Thread"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|sendExchange (Exchange exchange)
specifier|protected
name|void
name|sendExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|threadPool
operator|.
name|execute
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|SoroushMessage
operator|.
name|class
argument_list|)
operator|.
name|getFrom
argument_list|()
argument_list|,
parameter_list|()
lambda|->
block|{
try|try
block|{
if|if
condition|(
name|endpoint
operator|.
name|isSynchronous
argument_list|()
condition|)
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
else|else
block|{
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|e
lambda|->
block|{                         }
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"internal error occurs"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CongestionException
argument_list|(
name|ex
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|SoroushMessage
operator|.
name|class
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

