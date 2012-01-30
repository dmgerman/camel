begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.routebox.seda
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
name|seda
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
name|TimeUnit
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
name|component
operator|.
name|routebox
operator|.
name|strategy
operator|.
name|RouteboxDispatcher
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
name|AsyncProcessorConverterHelper
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
name|AsyncProcessorHelper
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
DECL|class|RouteboxSedaConsumer
specifier|public
class|class
name|RouteboxSedaConsumer
extends|extends
name|RouteboxServiceSupport
implements|implements
name|RouteboxConsumer
implements|,
name|Runnable
implements|,
name|ShutdownAware
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RouteboxSedaConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|processor
specifier|protected
name|AsyncProcessor
name|processor
decl_stmt|;
DECL|field|producer
specifier|protected
name|ProducerTemplate
name|producer
decl_stmt|;
DECL|method|RouteboxSedaConsumer (RouteboxSedaEndpoint endpoint, Processor processor)
specifier|public
name|RouteboxSedaConsumer
parameter_list|(
name|RouteboxSedaEndpoint
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
name|setProcessor
argument_list|(
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
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
operator|(
operator|(
name|RouteboxSedaEndpoint
operator|)
name|getRouteboxEndpoint
argument_list|()
operator|)
operator|.
name|onStarted
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|doStartInnerContext
argument_list|()
expr_stmt|;
comment|// Create a URI link from the primary context to routes in the new inner context
name|int
name|poolSize
init|=
name|getRouteboxEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getThreads
argument_list|()
decl_stmt|;
name|setExecutor
argument_list|(
name|getRouteboxEndpoint
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
name|getRouteboxEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|poolSize
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|poolSize
condition|;
name|i
operator|++
control|)
block|{
name|getExecutor
argument_list|()
operator|.
name|execute
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
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
operator|(
operator|(
name|RouteboxSedaEndpoint
operator|)
name|getRouteboxEndpoint
argument_list|()
operator|)
operator|.
name|onStopped
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// Shutdown the executor
name|getRouteboxEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdown
argument_list|(
name|getExecutor
argument_list|()
argument_list|)
expr_stmt|;
name|setExecutor
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|doStopInnerContext
argument_list|()
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
init|=
operator|(
operator|(
name|RouteboxSedaEndpoint
operator|)
name|getRouteboxEndpoint
argument_list|()
operator|)
operator|.
name|getQueue
argument_list|()
decl_stmt|;
while|while
condition|(
name|queue
operator|!=
literal|null
operator|&&
name|isRunAllowed
argument_list|()
condition|)
block|{
try|try
block|{
specifier|final
name|Exchange
name|exchange
init|=
name|queue
operator|.
name|poll
argument_list|(
name|getRouteboxEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getPollInterval
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
name|dispatchToInnerRoute
argument_list|(
name|queue
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sleep interrupted, are we stopping? {}"
argument_list|,
name|isStopping
argument_list|()
operator|||
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
block|}
block|}
DECL|method|dispatchToInnerRoute (BlockingQueue<Exchange> queue, final Exchange exchange)
specifier|private
name|void
name|dispatchToInnerRoute
parameter_list|(
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|Exchange
name|result
decl_stmt|;
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Dispatching to inner route: {}"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|RouteboxDispatcher
name|dispatcher
init|=
operator|new
name|RouteboxDispatcher
argument_list|(
name|producer
argument_list|)
decl_stmt|;
name|result
operator|=
name|dispatcher
operator|.
name|dispatchAsync
argument_list|(
name|getRouteboxEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|processor
argument_list|,
name|result
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// noop
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
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isWarnEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"This consumer is stopped during polling an exchange, so putting it back on the seda queue: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|queue
operator|.
name|put
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|getRouteboxEndpoint
argument_list|()
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
return|return
literal|false
return|;
block|}
DECL|method|getPendingExchangesSize ()
specifier|public
name|int
name|getPendingExchangesSize
parameter_list|()
block|{
comment|// TODO: Get size of queue
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|prepareShutdown (boolean forced)
specifier|public
name|void
name|prepareShutdown
parameter_list|(
name|boolean
name|forced
parameter_list|)
block|{     }
DECL|method|setProcessor (AsyncProcessor processor)
specifier|public
name|void
name|setProcessor
parameter_list|(
name|AsyncProcessor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
DECL|method|getProcessor ()
specifier|public
name|AsyncProcessor
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

