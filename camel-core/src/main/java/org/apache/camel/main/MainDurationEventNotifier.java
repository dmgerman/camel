begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.main
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
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
name|CountDownLatch
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
name|ScheduledExecutorService
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|CamelContext
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
name|CamelEvent
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
name|CamelEvent
operator|.
name|ExchangeCompletedEvent
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
name|CamelEvent
operator|.
name|ExchangeCreatedEvent
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
name|CamelEvent
operator|.
name|ExchangeFailedEvent
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
name|EventNotifierSupport
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
comment|/**  * A {@link org.apache.camel.spi.EventNotifier} to trigger shutdown of the Main JVM  * when maximum number of messages has been processed.  */
end_comment

begin_class
DECL|class|MainDurationEventNotifier
specifier|public
class|class
name|MainDurationEventNotifier
extends|extends
name|EventNotifierSupport
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
name|MainLifecycleStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|maxMessages
specifier|private
specifier|final
name|int
name|maxMessages
decl_stmt|;
DECL|field|maxIdleSeconds
specifier|private
specifier|final
name|long
name|maxIdleSeconds
decl_stmt|;
DECL|field|completed
specifier|private
specifier|final
name|AtomicBoolean
name|completed
decl_stmt|;
DECL|field|latch
specifier|private
specifier|final
name|CountDownLatch
name|latch
decl_stmt|;
DECL|field|stopCamelContext
specifier|private
specifier|final
name|boolean
name|stopCamelContext
decl_stmt|;
DECL|field|doneMessages
specifier|private
specifier|volatile
name|int
name|doneMessages
decl_stmt|;
DECL|field|watch
specifier|private
specifier|volatile
name|StopWatch
name|watch
decl_stmt|;
DECL|field|executorService
specifier|private
specifier|volatile
name|ScheduledExecutorService
name|executorService
decl_stmt|;
DECL|method|MainDurationEventNotifier (CamelContext camelContext, int maxMessages, long maxIdleSeconds, AtomicBoolean completed, CountDownLatch latch, boolean stopCamelContext)
specifier|public
name|MainDurationEventNotifier
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|int
name|maxMessages
parameter_list|,
name|long
name|maxIdleSeconds
parameter_list|,
name|AtomicBoolean
name|completed
parameter_list|,
name|CountDownLatch
name|latch
parameter_list|,
name|boolean
name|stopCamelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|maxMessages
operator|=
name|maxMessages
expr_stmt|;
name|this
operator|.
name|maxIdleSeconds
operator|=
name|maxIdleSeconds
expr_stmt|;
name|this
operator|.
name|completed
operator|=
name|completed
expr_stmt|;
name|this
operator|.
name|latch
operator|=
name|latch
expr_stmt|;
name|this
operator|.
name|stopCamelContext
operator|=
name|stopCamelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|notify (CamelEvent event)
specifier|public
name|void
name|notify
parameter_list|(
name|CamelEvent
name|event
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|begin
init|=
name|event
operator|instanceof
name|ExchangeCreatedEvent
decl_stmt|;
name|boolean
name|complete
init|=
name|event
operator|instanceof
name|ExchangeCompletedEvent
operator|||
name|event
operator|instanceof
name|ExchangeFailedEvent
decl_stmt|;
if|if
condition|(
name|maxMessages
operator|>
literal|0
operator|&&
name|complete
condition|)
block|{
name|doneMessages
operator|++
expr_stmt|;
name|boolean
name|result
init|=
name|doneMessages
operator|>=
name|maxMessages
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Duration max messages check {}>= {} -> {}"
argument_list|,
name|doneMessages
argument_list|,
name|maxMessages
argument_list|,
name|result
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
condition|)
block|{
if|if
condition|(
name|completed
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Duration max messages triggering shutdown of the JVM."
argument_list|)
expr_stmt|;
try|try
block|{
comment|// shutting down CamelContext
if|if
condition|(
name|stopCamelContext
condition|)
block|{
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error during stopping CamelContext. This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// trigger stopping the Main
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// idle reacts on both incoming and complete messages
if|if
condition|(
name|maxIdleSeconds
operator|>
literal|0
operator|&&
operator|(
name|begin
operator|||
name|complete
operator|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Message activity so restarting stop watch"
argument_list|)
expr_stmt|;
name|watch
operator|.
name|restart
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|isEnabled (CamelEvent event)
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|CamelEvent
name|event
parameter_list|)
block|{
return|return
name|event
operator|instanceof
name|ExchangeCompletedEvent
operator|||
name|event
operator|instanceof
name|ExchangeFailedEvent
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
literal|"MainDurationEventNotifier["
operator|+
name|maxMessages
operator|+
literal|" max messages]"
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
if|if
condition|(
name|maxIdleSeconds
operator|>
literal|0
condition|)
block|{
comment|// we only start watch when Camel is started
name|camelContext
operator|.
name|addStartupListener
argument_list|(
parameter_list|(
name|context
parameter_list|,
name|alreadyStarted
parameter_list|)
lambda|->
name|watch
operator|=
operator|new
name|StopWatch
argument_list|()
argument_list|)
expr_stmt|;
comment|// okay we need to trigger on idle after X period, and therefore we need a background task that checks this
name|executorService
operator|=
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadScheduledExecutor
argument_list|(
name|this
argument_list|,
literal|"MainDurationIdleChecker"
argument_list|)
expr_stmt|;
name|Runnable
name|task
init|=
parameter_list|()
lambda|->
block|{
if|if
condition|(
name|watch
operator|==
literal|null
condition|)
block|{
comment|// camel has not been started yet
return|return;
block|}
comment|// any inflight messages currently
name|int
name|inflight
init|=
name|camelContext
operator|.
name|getInflightRepository
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|inflight
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Duration max idle check is skipped due {} inflight messages"
argument_list|,
name|inflight
argument_list|)
expr_stmt|;
return|return;
block|}
name|long
name|seconds
init|=
name|watch
operator|.
name|taken
argument_list|()
operator|/
literal|1000
decl_stmt|;
name|boolean
name|result
init|=
name|seconds
operator|>=
name|maxIdleSeconds
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Duration max idle check {}>= {} -> {}"
argument_list|,
name|seconds
argument_list|,
name|maxIdleSeconds
argument_list|,
name|result
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
condition|)
block|{
if|if
condition|(
name|completed
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Duration max idle triggering shutdown of the JVM."
argument_list|)
expr_stmt|;
try|try
block|{
comment|// shutting down CamelContext
if|if
condition|(
name|stopCamelContext
condition|)
block|{
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error during stopping CamelContext. This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// trigger stopping the Main
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
decl_stmt|;
name|executorService
operator|.
name|scheduleAtFixedRate
argument_list|(
name|task
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

