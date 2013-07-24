begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|ScheduledFuture
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
name|CamelContextAware
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
name|StaticService
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
name|TimerListener
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
name|ObjectHelper
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
comment|/**  * A {@link TimerListener} manager which triggers the  * {@link org.apache.camel.TimerListener} listeners once every second.  *<p/>  * Also ensure when adding and remove listeners, that they are correctly removed to avoid  * leaking memory.  *  * @see TimerListener  */
end_comment

begin_class
DECL|class|TimerListenerManager
specifier|public
class|class
name|TimerListenerManager
extends|extends
name|ServiceSupport
implements|implements
name|Runnable
implements|,
name|CamelContextAware
implements|,
name|StaticService
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
name|TimerListenerManager
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|listeners
specifier|private
specifier|final
name|Set
argument_list|<
name|TimerListener
argument_list|>
name|listeners
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|TimerListener
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|executorService
specifier|private
name|ScheduledExecutorService
name|executorService
decl_stmt|;
DECL|field|task
specifier|private
specifier|volatile
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|task
decl_stmt|;
DECL|field|interval
specifier|private
name|long
name|interval
init|=
literal|1000L
decl_stmt|;
DECL|method|TimerListenerManager ()
specifier|public
name|TimerListenerManager
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
comment|/**      * Gets the interval in millis.      *<p/>      * The default interval is 1000 millis.      *      * @return interval in millis.      */
DECL|method|getInterval ()
specifier|public
name|long
name|getInterval
parameter_list|()
block|{
return|return
name|interval
return|;
block|}
comment|/**      * Sets the interval in millis.      *      * @param interval interval in millis.      */
DECL|method|setInterval (long interval)
specifier|public
name|void
name|setInterval
parameter_list|(
name|long
name|interval
parameter_list|)
block|{
name|this
operator|.
name|interval
operator|=
name|interval
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Running scheduled TimerListener task"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"TimerListener task cannot run as its not allowed"
argument_list|)
expr_stmt|;
return|return;
block|}
for|for
control|(
name|TimerListener
name|listener
range|:
name|listeners
control|)
block|{
try|try
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Invoking onTimer on {}"
argument_list|,
name|listener
argument_list|)
expr_stmt|;
name|listener
operator|.
name|onTimer
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// ignore
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error occurred during onTimer for TimerListener: "
operator|+
name|listener
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Adds the listener.      *<p/>      * It may be important to implement {@link #equals(Object)} and {@link #hashCode()} for the listener      * to ensure that we can remove the same listener again, when invoking remove.      *       * @param listener listener      */
DECL|method|addTimerListener (TimerListener listener)
specifier|public
name|void
name|addTimerListener
parameter_list|(
name|TimerListener
name|listener
parameter_list|)
block|{
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Added TimerListener: {}"
argument_list|,
name|listener
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes the listener.      *<p/>      * It may be important to implement {@link #equals(Object)} and {@link #hashCode()} for the listener      * to ensure that we can remove the same listener again, when invoking remove.      *      * @param listener listener.      */
DECL|method|removeTimerListener (TimerListener listener)
specifier|public
name|void
name|removeTimerListener
parameter_list|(
name|TimerListener
name|listener
parameter_list|)
block|{
name|listeners
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Removed TimerListener: {}"
argument_list|,
name|listener
argument_list|)
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// create scheduled thread pool to trigger the task to run every interval
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
literal|"ManagementLoadTask"
argument_list|)
expr_stmt|;
name|task
operator|=
name|executorService
operator|.
name|scheduleAtFixedRate
argument_list|(
name|this
argument_list|,
name|interval
argument_list|,
name|interval
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Started scheduled TimerListener task to run with interval {} ms"
argument_list|,
name|interval
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
comment|// executor service will be shutdown by CamelContext
if|if
condition|(
name|task
operator|!=
literal|null
condition|)
block|{
name|task
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|task
operator|=
literal|null
expr_stmt|;
block|}
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
comment|// shutdown thread pool when we are shutting down
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|executorService
operator|=
literal|null
expr_stmt|;
name|listeners
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

