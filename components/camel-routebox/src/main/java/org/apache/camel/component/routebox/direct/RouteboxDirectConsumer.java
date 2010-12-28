begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.routebox.direct
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|routebox
operator|.
name|direct
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
name|AsyncProcessor
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
name|ProducerTemplate
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
name|ShutdownRunningTask
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
name|SuspendableService
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
name|routebox
operator|.
name|RouteboxConsumer
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
name|routebox
operator|.
name|RouteboxEndpoint
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
name|routebox
operator|.
name|RouteboxServiceSupport
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
name|converter
operator|.
name|AsyncProcessorTypeConverter
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
name|ShutdownAware
import|;
end_import

begin_class
DECL|class|RouteboxDirectConsumer
specifier|public
class|class
name|RouteboxDirectConsumer
extends|extends
name|RouteboxServiceSupport
implements|implements
name|RouteboxConsumer
implements|,
name|ShutdownAware
implements|,
name|SuspendableService
block|{
DECL|field|producer
specifier|protected
name|ProducerTemplate
name|producer
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|asyncProcessor
specifier|private
specifier|volatile
name|AsyncProcessor
name|asyncProcessor
decl_stmt|;
DECL|method|RouteboxDirectConsumer (RouteboxDirectEndpoint endpoint, Processor processor)
specifier|public
name|RouteboxDirectConsumer
parameter_list|(
name|RouteboxDirectEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|producer
operator|=
name|endpoint
operator|.
name|getConfig
argument_list|()
operator|.
name|getInnerProducerTemplate
argument_list|()
expr_stmt|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// add consumer to endpoint
name|boolean
name|existing
init|=
name|this
operator|==
name|getEndpoint
argument_list|()
operator|.
name|getConsumer
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|existing
operator|&&
name|getEndpoint
argument_list|()
operator|.
name|hasConsumer
argument_list|(
name|this
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot add a 2nd consumer to the same endpoint. Endpoint "
operator|+
name|getEndpoint
argument_list|()
operator|+
literal|" only allows one consumer."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|existing
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|addConsumer
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
comment|// now start the inner context
if|if
condition|(
operator|!
name|isStartedInnerContext
argument_list|()
condition|)
block|{
name|doStartInnerContext
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|getEndpoint
argument_list|()
operator|.
name|removeConsumer
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// now stop the inner context
if|if
condition|(
name|isStartedInnerContext
argument_list|()
condition|)
block|{
name|doStopInnerContext
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doSuspend ()
specifier|protected
name|void
name|doSuspend
parameter_list|()
throws|throws
name|Exception
block|{
name|getEndpoint
argument_list|()
operator|.
name|removeConsumer
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|doResume ()
specifier|protected
name|void
name|doResume
parameter_list|()
throws|throws
name|Exception
block|{
comment|// resume by using the start logic
name|doStart
argument_list|()
expr_stmt|;
block|}
comment|/**      * Provides an {@link org.apache.camel.AsyncProcessor} interface to the configured      * processor on the consumer. If the processor does not implement the interface,      * it will be adapted so that it does.      */
DECL|method|getAsyncProcessor ()
specifier|public
specifier|synchronized
name|AsyncProcessor
name|getAsyncProcessor
parameter_list|()
block|{
if|if
condition|(
name|asyncProcessor
operator|==
literal|null
condition|)
block|{
name|asyncProcessor
operator|=
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
return|return
name|asyncProcessor
return|;
block|}
DECL|method|deferShutdown (ShutdownRunningTask shutdownRunningTask)
specifier|public
name|boolean
name|deferShutdown
parameter_list|(
name|ShutdownRunningTask
name|shutdownRunningTask
parameter_list|)
block|{
comment|// deny stopping on shutdown as we want direct consumers to run in case some other queues
comment|// depend on this consumer to run, so it can complete its exchanges
return|return
literal|true
return|;
block|}
DECL|method|getPendingExchangesSize ()
specifier|public
name|int
name|getPendingExchangesSize
parameter_list|()
block|{
comment|// return 0 as we do not have an internal memory queue with a variable size
comment|// of inflight messages.
return|return
literal|0
return|;
block|}
DECL|method|prepareShutdown ()
specifier|public
name|void
name|prepareShutdown
parameter_list|()
block|{
comment|// noop
block|}
DECL|method|getEndpoint ()
specifier|public
name|RouteboxDirectEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|RouteboxDirectEndpoint
operator|)
name|getRouteboxEndpoint
argument_list|()
return|;
block|}
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
block|}
end_class

end_unit

