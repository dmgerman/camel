begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.docker.consumer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|docker
operator|.
name|consumer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|ExecutorService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|EventCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|command
operator|.
name|EventsCmd
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|model
operator|.
name|Event
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
name|Message
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
name|docker
operator|.
name|DockerClientFactory
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
name|docker
operator|.
name|DockerComponent
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
name|docker
operator|.
name|DockerConstants
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
name|docker
operator|.
name|DockerEndpoint
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
name|docker
operator|.
name|DockerHelper
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
comment|/**  * Docker Consumer for streaming events  */
end_comment

begin_class
DECL|class|DockerEventsConsumer
specifier|public
class|class
name|DockerEventsConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|EventCallback
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
name|DockerEventsConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|DockerEndpoint
name|endpoint
decl_stmt|;
DECL|field|component
specifier|private
name|DockerComponent
name|component
decl_stmt|;
DECL|field|eventsCmd
specifier|private
name|EventsCmd
name|eventsCmd
decl_stmt|;
DECL|field|eventsExecutorService
specifier|private
name|ExecutorService
name|eventsExecutorService
decl_stmt|;
DECL|method|DockerEventsConsumer (DockerEndpoint endpoint, Processor processor)
specifier|public
name|DockerEventsConsumer
parameter_list|(
name|DockerEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
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
name|this
operator|.
name|component
operator|=
operator|(
name|DockerComponent
operator|)
name|endpoint
operator|.
name|getComponent
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|DockerEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|DockerEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
comment|/**      * Determine the point in time to begin streaming events      */
DECL|method|processInitialEvent ()
specifier|private
name|long
name|processInitialEvent
parameter_list|()
block|{
name|long
name|currentTime
init|=
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|Long
name|initialRange
init|=
name|DockerHelper
operator|.
name|getProperty
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_INITIAL_RANGE
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|,
literal|null
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|initialRange
operator|!=
literal|null
condition|)
block|{
name|currentTime
operator|=
name|currentTime
operator|-
name|initialRange
expr_stmt|;
block|}
return|return
name|currentTime
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
name|eventsCmd
operator|=
name|DockerClientFactory
operator|.
name|getDockerClient
argument_list|(
name|component
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|,
literal|null
argument_list|)
operator|.
name|eventsCmd
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|eventsCmd
operator|.
name|withSince
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|processInitialEvent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|eventsExecutorService
operator|=
name|eventsCmd
operator|.
name|exec
argument_list|()
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
if|if
condition|(
name|eventsExecutorService
operator|!=
literal|null
operator|&&
operator|!
name|eventsExecutorService
operator|.
name|isTerminated
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Stopping Docker events Executor Service"
argument_list|)
expr_stmt|;
name|eventsExecutorService
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onEvent (Event event)
specifier|public
name|void
name|onEvent
parameter_list|(
name|Event
name|event
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Received Docker Event: "
operator|+
name|event
argument_list|)
expr_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|event
argument_list|)
expr_stmt|;
try|try
block|{
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Processing exchange [{}]..."
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Done processing exchange [{}]..."
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
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
block|}
annotation|@
name|Override
DECL|method|onException (Throwable throwable)
specifier|public
name|void
name|onException
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Error Consuming from Docker Events: {}"
argument_list|,
name|throwable
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onCompletion (int numEvents)
specifier|public
name|void
name|onCompletion
parameter_list|(
name|int
name|numEvents
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Docker events connection completed. Events processed : {}"
argument_list|,
name|numEvents
argument_list|)
expr_stmt|;
name|eventsCmd
operator|.
name|withSince
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Reestablishing connection with Docker"
argument_list|)
expr_stmt|;
name|eventsCmd
operator|.
name|exec
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isReceiving ()
specifier|public
name|boolean
name|isReceiving
parameter_list|()
block|{
return|return
name|isRunAllowed
argument_list|()
return|;
block|}
block|}
end_class

end_unit

