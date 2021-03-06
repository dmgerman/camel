begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
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
name|spi
operator|.
name|PollingConsumerPollStrategy
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
name|EventDrivenPollingConsumer
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
name|ScheduledBatchPollingConsumer
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
name|service
operator|.
name|ServiceHelper
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
name|StopWatch
import|;
end_import

begin_class
DECL|class|GenericFilePollingConsumer
specifier|public
class|class
name|GenericFilePollingConsumer
extends|extends
name|EventDrivenPollingConsumer
block|{
DECL|field|delay
specifier|private
specifier|final
name|long
name|delay
decl_stmt|;
DECL|method|GenericFilePollingConsumer (GenericFileEndpoint endpoint)
specifier|public
name|GenericFilePollingConsumer
parameter_list|(
name|GenericFileEndpoint
name|endpoint
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
name|delay
operator|=
name|endpoint
operator|.
name|getDelay
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createConsumer ()
specifier|protected
name|Consumer
name|createConsumer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets add ourselves as a consumer
name|GenericFileConsumer
name|consumer
init|=
operator|(
name|GenericFileConsumer
operator|)
name|super
operator|.
name|createConsumer
argument_list|()
decl_stmt|;
comment|// do not start scheduler as we poll manually
name|consumer
operator|.
name|setStartScheduler
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// when using polling consumer we poll only 1 file per poll so we can limit
name|consumer
operator|.
name|setMaxMessagesPerPoll
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// however do not limit eager as we may sort the files and thus need to do a full scan so we can sort afterwards
name|consumer
operator|.
name|setEagerLimitMaxMessagesPerPoll
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// we only want to poll once so disconnect by default
return|return
name|consumer
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
comment|// ensure consumer is started
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|getConsumer
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getConsumer ()
specifier|protected
name|GenericFileConsumer
name|getConsumer
parameter_list|()
block|{
return|return
operator|(
name|GenericFileConsumer
operator|)
name|super
operator|.
name|getConsumer
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|receiveNoWait ()
specifier|public
name|Exchange
name|receiveNoWait
parameter_list|()
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"receiveNoWait polling file: {}"
argument_list|,
name|getConsumer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|int
name|polled
init|=
name|doReceive
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|polled
operator|>
literal|0
condition|)
block|{
return|return
name|super
operator|.
name|receive
argument_list|(
literal|0
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|receive ()
specifier|public
name|Exchange
name|receive
parameter_list|()
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"receive polling file: {}"
argument_list|,
name|getConsumer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|int
name|polled
init|=
name|doReceive
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
decl_stmt|;
if|if
condition|(
name|polled
operator|>
literal|0
condition|)
block|{
return|return
name|super
operator|.
name|receive
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|receive (long timeout)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"receive({}) polling file: {}"
argument_list|,
name|timeout
argument_list|,
name|getConsumer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|int
name|polled
init|=
name|doReceive
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
if|if
condition|(
name|polled
operator|>
literal|0
condition|)
block|{
return|return
name|super
operator|.
name|receive
argument_list|(
name|timeout
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|doReceive (long timeout)
specifier|protected
name|int
name|doReceive
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|int
name|retryCounter
init|=
operator|-
literal|1
decl_stmt|;
name|boolean
name|done
init|=
literal|false
decl_stmt|;
name|Throwable
name|cause
init|=
literal|null
decl_stmt|;
name|int
name|polledMessages
init|=
literal|0
decl_stmt|;
name|PollingConsumerPollStrategy
name|pollStrategy
init|=
name|getConsumer
argument_list|()
operator|.
name|getPollStrategy
argument_list|()
decl_stmt|;
name|boolean
name|sendEmptyMessageWhenIdle
init|=
name|getConsumer
argument_list|()
operator|instanceof
name|ScheduledBatchPollingConsumer
operator|&&
name|getConsumer
argument_list|()
operator|.
name|isSendEmptyMessageWhenIdle
argument_list|()
decl_stmt|;
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|done
condition|)
block|{
try|try
block|{
name|cause
operator|=
literal|null
expr_stmt|;
comment|// eager assume we are done
name|done
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
if|if
condition|(
name|retryCounter
operator|==
operator|-
literal|1
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Starting to poll: {}"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Retrying attempt {} to poll: {}"
argument_list|,
name|retryCounter
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// mark we are polling which should also include the begin/poll/commit
name|boolean
name|begin
init|=
name|pollStrategy
operator|.
name|begin
argument_list|(
name|getConsumer
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|begin
condition|)
block|{
name|retryCounter
operator|++
expr_stmt|;
name|polledMessages
operator|=
name|getConsumer
argument_list|()
operator|.
name|poll
argument_list|()
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Polled {} messages"
argument_list|,
name|polledMessages
argument_list|)
expr_stmt|;
if|if
condition|(
name|polledMessages
operator|==
literal|0
operator|&&
name|sendEmptyMessageWhenIdle
condition|)
block|{
comment|// send an "empty" exchange
name|processEmptyMessage
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|polledMessages
operator|==
literal|0
operator|&&
name|timeout
operator|>
literal|0
condition|)
block|{
comment|// if we did not poll a file and we are using timeout then try to poll again
name|done
operator|=
literal|false
expr_stmt|;
block|}
name|pollStrategy
operator|.
name|commit
argument_list|(
name|getConsumer
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
argument_list|,
name|polledMessages
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Cannot begin polling as pollStrategy returned false: {}"
argument_list|,
name|pollStrategy
argument_list|)
expr_stmt|;
block|}
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Finished polling: {}"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
try|try
block|{
name|boolean
name|retry
init|=
name|pollStrategy
operator|.
name|rollback
argument_list|(
name|getConsumer
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
argument_list|,
name|retryCounter
argument_list|,
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
name|retry
condition|)
block|{
comment|// do not set cause as we retry
name|done
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|cause
operator|=
name|e
expr_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|cause
operator|=
name|t
expr_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|cause
operator|=
name|t
expr_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|done
operator|&&
name|timeout
operator|>
literal|0
condition|)
block|{
comment|// prepare for next attempt until we hit timeout
name|long
name|left
init|=
name|timeout
operator|-
name|watch
operator|.
name|taken
argument_list|()
decl_stmt|;
name|long
name|min
init|=
name|Math
operator|.
name|min
argument_list|(
name|left
argument_list|,
name|delay
argument_list|)
decl_stmt|;
if|if
condition|(
name|min
operator|>
literal|0
condition|)
block|{
try|try
block|{
comment|// sleep for next pool
name|sleep
argument_list|(
name|min
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
else|else
block|{
comment|// timeout hit
name|done
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|cause
operator|!=
literal|null
condition|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|cause
argument_list|)
throw|;
block|}
return|return
name|polledMessages
return|;
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
name|Object
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Received file: {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|/**      * No messages to poll so send an empty message instead.      *      * @throws Exception is thrown if error processing the empty message.      */
DECL|method|processEmptyMessage ()
specifier|protected
name|void
name|processEmptyMessage
parameter_list|()
throws|throws
name|Exception
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
name|log
operator|.
name|debug
argument_list|(
literal|"Sending empty message as there were no messages from polling: {}"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|sleep (long delay)
specifier|private
name|void
name|sleep
parameter_list|(
name|long
name|delay
parameter_list|)
throws|throws
name|InterruptedException
block|{
if|if
condition|(
name|delay
operator|<=
literal|0
condition|)
block|{
return|return;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Sleeping for: {} millis"
argument_list|,
name|delay
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

