begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
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
name|ExecutorService
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
name|AsyncCallback
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreams
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreamsService
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
name|DefaultConsumer
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

begin_comment
comment|/**  * The Camel reactive-streams consumer.  */
end_comment

begin_class
DECL|class|ReactiveStreamsConsumer
specifier|public
class|class
name|ReactiveStreamsConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ReactiveStreamsConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|ReactiveStreamsEndpoint
name|endpoint
decl_stmt|;
DECL|field|executor
specifier|private
name|ExecutorService
name|executor
decl_stmt|;
DECL|field|service
specifier|private
name|CamelReactiveStreamsService
name|service
decl_stmt|;
DECL|method|ReactiveStreamsConsumer (ReactiveStreamsEndpoint endpoint, Processor processor)
specifier|public
name|ReactiveStreamsConsumer
parameter_list|(
name|ReactiveStreamsEndpoint
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
name|this
operator|.
name|endpoint
operator|=
name|endpoint
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
name|service
operator|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|poolSize
init|=
name|endpoint
operator|.
name|getConcurrentConsumers
argument_list|()
decl_stmt|;
if|if
condition|(
name|executor
operator|==
literal|null
condition|)
block|{
name|executor
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newFixedThreadPool
argument_list|(
name|this
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|poolSize
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|service
operator|.
name|attachCamelConsumer
argument_list|(
name|endpoint
operator|.
name|getStream
argument_list|()
argument_list|,
name|this
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|this
operator|.
name|service
operator|.
name|detachCamelConsumer
argument_list|(
name|endpoint
operator|.
name|getStream
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|executor
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|executor
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ReactiveStreamsConstants
operator|.
name|REACTIVE_STREAMS_EVENT_TYPE
argument_list|,
literal|"onNext"
argument_list|)
expr_stmt|;
return|return
name|doSend
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
DECL|method|onComplete ()
specifier|public
name|void
name|onComplete
parameter_list|()
block|{
if|if
condition|(
name|endpoint
operator|.
name|isForwardOnComplete
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
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
name|ReactiveStreamsConstants
operator|.
name|REACTIVE_STREAMS_EVENT_TYPE
argument_list|,
literal|"onComplete"
argument_list|)
expr_stmt|;
name|doSend
argument_list|(
name|exchange
argument_list|,
name|done
lambda|->
block|{             }
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onError (Throwable error)
specifier|public
name|void
name|onError
parameter_list|(
name|Throwable
name|error
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isForwardOnError
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
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
name|ReactiveStreamsConstants
operator|.
name|REACTIVE_STREAMS_EVENT_TYPE
argument_list|,
literal|"onError"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|error
argument_list|)
expr_stmt|;
name|doSend
argument_list|(
name|exchange
argument_list|,
name|done
lambda|->
block|{             }
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doSend (Exchange exchange, AsyncCallback callback)
specifier|private
name|boolean
name|doSend
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|ExecutorService
name|executorService
init|=
name|this
operator|.
name|executor
decl_stmt|;
if|if
condition|(
name|executorService
operator|!=
literal|null
operator|&&
name|this
operator|.
name|isRunAllowed
argument_list|()
condition|)
block|{
name|executorService
operator|.
name|execute
argument_list|(
parameter_list|()
lambda|->
name|this
operator|.
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|doneSync
lambda|->
block|{
block|if (exchange.getException(
argument_list|)
operator|!=
literal|null
block|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
block|)
end_class

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_return
return|return
literal|false
return|;
end_return

begin_block
unit|} else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Consumer not ready to process exchanges. The exchange {} will be discarded"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
end_block

begin_function
unit|}      @
name|Override
DECL|method|getEndpoint ()
specifier|public
name|ReactiveStreamsEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
end_function

unit|}
end_unit

