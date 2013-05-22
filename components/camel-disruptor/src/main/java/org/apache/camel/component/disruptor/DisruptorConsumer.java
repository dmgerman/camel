begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|impl
operator|.
name|LoggingExceptionHandler
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
name|ExceptionHandler
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
name|support
operator|.
name|ServiceSupport
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ExchangeHelper
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
comment|/**  * A Consumer for the Disruptor component.  */
end_comment

begin_class
DECL|class|DisruptorConsumer
specifier|public
class|class
name|DisruptorConsumer
extends|extends
name|ServiceSupport
implements|implements
name|Consumer
implements|,
name|SuspendableService
implements|,
name|ShutdownAware
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
name|DisruptorConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|DisruptorEndpoint
name|endpoint
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|AsyncProcessor
name|processor
decl_stmt|;
DECL|field|exceptionHandler
specifier|private
name|ExceptionHandler
name|exceptionHandler
decl_stmt|;
DECL|method|DisruptorConsumer (final DisruptorEndpoint endpoint, final Processor processor)
specifier|public
name|DisruptorConsumer
parameter_list|(
specifier|final
name|DisruptorEndpoint
name|endpoint
parameter_list|,
specifier|final
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|getExceptionHandler ()
specifier|public
name|ExceptionHandler
name|getExceptionHandler
parameter_list|()
block|{
if|if
condition|(
name|exceptionHandler
operator|==
literal|null
condition|)
block|{
name|exceptionHandler
operator|=
operator|new
name|LoggingExceptionHandler
argument_list|(
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|exceptionHandler
return|;
block|}
DECL|method|setExceptionHandler (final ExceptionHandler exceptionHandler)
specifier|public
name|void
name|setExceptionHandler
parameter_list|(
specifier|final
name|ExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|this
operator|.
name|exceptionHandler
operator|=
name|exceptionHandler
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|DisruptorEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
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
name|getEndpoint
argument_list|()
operator|.
name|onStarted
argument_list|(
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
name|getEndpoint
argument_list|()
operator|.
name|onStopped
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|onStopped
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doResume ()
specifier|protected
name|void
name|doResume
parameter_list|()
throws|throws
name|Exception
block|{
name|getEndpoint
argument_list|()
operator|.
name|onStarted
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|createEventHandlers (final int concurrentConsumers)
name|Set
argument_list|<
name|LifecycleAwareExchangeEventHandler
argument_list|>
name|createEventHandlers
parameter_list|(
specifier|final
name|int
name|concurrentConsumers
parameter_list|)
block|{
specifier|final
name|Set
argument_list|<
name|LifecycleAwareExchangeEventHandler
argument_list|>
name|eventHandlers
init|=
operator|new
name|HashSet
argument_list|<
name|LifecycleAwareExchangeEventHandler
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|concurrentConsumers
condition|;
operator|++
name|i
control|)
block|{
name|eventHandlers
operator|.
name|add
argument_list|(
operator|new
name|ConsumerEventHandler
argument_list|(
name|i
argument_list|,
name|concurrentConsumers
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|eventHandlers
return|;
block|}
annotation|@
name|Override
DECL|method|deferShutdown (final ShutdownRunningTask shutdownRunningTask)
specifier|public
name|boolean
name|deferShutdown
parameter_list|(
specifier|final
name|ShutdownRunningTask
name|shutdownRunningTask
parameter_list|)
block|{
comment|// deny stopping on shutdown as we want disruptor consumers to run in case some other queues
comment|// depend on this consumer to run, so it can complete its exchanges
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|prepareShutdown (final boolean forced)
specifier|public
name|void
name|prepareShutdown
parameter_list|(
specifier|final
name|boolean
name|forced
parameter_list|)
block|{
comment|// nothing
block|}
annotation|@
name|Override
DECL|method|getPendingExchangesSize ()
specifier|public
name|int
name|getPendingExchangesSize
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getDisruptor
argument_list|()
operator|.
name|getPendingExchangeCount
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"DisruptorConsumer["
operator|+
name|endpoint
operator|+
literal|"]"
return|;
block|}
DECL|method|prepareExchange (final Exchange exchange)
specifier|private
name|Exchange
name|prepareExchange
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// send a new copied exchange with new camel context
comment|// don't copy handovers as they are handled by the Disruptor Event Handlers
specifier|final
name|Exchange
name|newExchange
init|=
name|ExchangeHelper
operator|.
name|copyExchangeAndSetCamelContext
argument_list|(
name|exchange
argument_list|,
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// set the from endpoint
name|newExchange
operator|.
name|setFromEndpoint
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|newExchange
return|;
block|}
DECL|method|process (final ExchangeEvent exchangeEvent)
specifier|private
name|void
name|process
parameter_list|(
specifier|final
name|ExchangeEvent
name|exchangeEvent
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|exchangeEvent
operator|.
name|getExchange
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|ignore
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|DisruptorEndpoint
operator|.
name|DISRUPTOR_IGNORE_EXCHANGE
argument_list|,
literal|false
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ignore
condition|)
block|{
comment|// Property was set and it was set to true, so don't process Exchange.
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Ignoring exchange {}"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// send a new copied exchange with new camel context
specifier|final
name|Exchange
name|result
init|=
name|prepareExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// use the regular processor and use the asynchronous routing engine to support it
name|AsyncCallback
name|callback
init|=
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
name|exchangeEvent
operator|.
name|consumed
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|processor
argument_list|,
name|result
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
comment|/**      * Implementation of the {@link LifecycleAwareExchangeEventHandler} interface that passes all Exchanges to the      * {@link Processor} registered at this {@link DisruptorConsumer}.      */
DECL|class|ConsumerEventHandler
specifier|private
class|class
name|ConsumerEventHandler
extends|extends
name|AbstractLifecycleAwareExchangeEventHandler
block|{
DECL|field|ordinal
specifier|private
specifier|final
name|int
name|ordinal
decl_stmt|;
DECL|field|concurrentConsumers
specifier|private
specifier|final
name|int
name|concurrentConsumers
decl_stmt|;
DECL|method|ConsumerEventHandler (final int ordinal, final int concurrentConsumers)
specifier|public
name|ConsumerEventHandler
parameter_list|(
specifier|final
name|int
name|ordinal
parameter_list|,
specifier|final
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|ordinal
operator|=
name|ordinal
expr_stmt|;
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onEvent (final ExchangeEvent event, final long sequence, final boolean endOfBatch)
specifier|public
name|void
name|onEvent
parameter_list|(
specifier|final
name|ExchangeEvent
name|event
parameter_list|,
specifier|final
name|long
name|sequence
parameter_list|,
specifier|final
name|boolean
name|endOfBatch
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Consumer threads are managed at the endpoint to achieve the optimal performance.
comment|// However, both multiple consumers (pub-sub style multicasting) as well as 'worker-pool' consumers dividing
comment|// exchanges amongst them are scheduled on their own threads and are provided with all exchanges.
comment|// To prevent duplicate exchange processing by worker-pool event handlers, they are all given an ordinal,
comment|// which can be used to determine whether he should process the exchange, or leave it for his brethren.
comment|//see http://code.google.com/p/disruptor/wiki/FrequentlyAskedQuestions#How_do_you_arrange_a_Disruptor_with_multiple_consumers_so_that_e
if|if
condition|(
name|sequence
operator|%
name|concurrentConsumers
operator|==
name|ordinal
condition|)
block|{
try|try
block|{
name|process
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
specifier|final
name|Exchange
name|exchange
init|=
name|event
operator|.
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
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
name|e
argument_list|)
expr_stmt|;
block|}
else|else
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
block|}
block|}
block|}
end_class

end_unit

