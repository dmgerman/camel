begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
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
name|util
operator|.
name|concurrent
operator|.
name|CamelThreadFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|task
operator|.
name|SimpleAsyncTaskExecutor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|task
operator|.
name|TaskExecutor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|JmsException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|listener
operator|.
name|DefaultMessageListenerContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|scheduling
operator|.
name|concurrent
operator|.
name|ThreadPoolTaskExecutor
import|;
end_import

begin_comment
comment|/**  * The default {@link DefaultMessageListenerContainer container} which listen for messages  * on the JMS destination.  *<p/>  * This implementation extends Springs {@link DefaultMessageListenerContainer} supporting  * automatic recovery and throttling.  */
end_comment

begin_class
DECL|class|DefaultJmsMessageListenerContainer
specifier|public
class|class
name|DefaultJmsMessageListenerContainer
extends|extends
name|DefaultMessageListenerContainer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|JmsEndpoint
name|endpoint
decl_stmt|;
DECL|field|allowQuickStop
specifier|private
specifier|final
name|boolean
name|allowQuickStop
decl_stmt|;
DECL|field|taskExecutor
specifier|private
specifier|volatile
name|TaskExecutor
name|taskExecutor
decl_stmt|;
DECL|method|DefaultJmsMessageListenerContainer (JmsEndpoint endpoint)
specifier|public
name|DefaultJmsMessageListenerContainer
parameter_list|(
name|JmsEndpoint
name|endpoint
parameter_list|)
block|{
name|this
argument_list|(
name|endpoint
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultJmsMessageListenerContainer (JmsEndpoint endpoint, boolean allowQuickStop)
specifier|public
name|DefaultJmsMessageListenerContainer
parameter_list|(
name|JmsEndpoint
name|endpoint
parameter_list|,
name|boolean
name|allowQuickStop
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
name|allowQuickStop
operator|=
name|allowQuickStop
expr_stmt|;
block|}
comment|/**      * Whether this {@link DefaultMessageListenerContainer} allows the {@link #runningAllowed()} to quick stop      * in case {@link JmsConfiguration#isAcceptMessagesWhileStopping()} is enabled, and {@link org.apache.camel.CamelContext}      * is currently being stopped.      */
DECL|method|isAllowQuickStop ()
specifier|protected
name|boolean
name|isAllowQuickStop
parameter_list|()
block|{
return|return
name|allowQuickStop
return|;
block|}
annotation|@
name|Override
DECL|method|runningAllowed ()
specifier|protected
name|boolean
name|runningAllowed
parameter_list|()
block|{
comment|// we can stop quickly if CamelContext is being stopped, and we do not accept messages while stopping
comment|// this allows a more cleanly shutdown of the message listener
name|boolean
name|quickStop
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|isAllowQuickStop
argument_list|()
operator|&&
operator|!
name|endpoint
operator|.
name|isAcceptMessagesWhileStopping
argument_list|()
condition|)
block|{
name|quickStop
operator|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|.
name|isStopping
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|quickStop
condition|)
block|{
comment|// log at debug level so its quicker to see we are stopping quicker from the logs
name|logger
operator|.
name|debug
argument_list|(
literal|"runningAllowed() -> false due CamelContext is stopping and endpoint configured to not accept messages while stopping"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
else|else
block|{
comment|// otherwise we only run if the endpoint is running
name|boolean
name|answer
init|=
name|endpoint
operator|.
name|isRunning
argument_list|()
decl_stmt|;
comment|// log at trace level as otherwise this can be noisy during normal operation
name|logger
operator|.
name|trace
argument_list|(
literal|"runningAllowed() -> "
operator|+
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
comment|/**      * Create a default TaskExecutor. Called if no explicit TaskExecutor has been specified.      *<p />      * The type of {@link TaskExecutor} will depend on the value of      * {@link JmsConfiguration#getDefaultTaskExecutorType()}. For more details, refer to the Javadoc of      * {@link DefaultTaskExecutorType}.      *<p />      * In all cases, it uses the specified bean name and Camel's {@link org.apache.camel.spi.ExecutorServiceManager}      * to resolve the thread name.      * @see JmsConfiguration#setDefaultTaskExecutorType(DefaultTaskExecutorType)      * @see ThreadPoolTaskExecutor#setBeanName(String)      */
annotation|@
name|Override
DECL|method|createDefaultTaskExecutor ()
specifier|protected
name|TaskExecutor
name|createDefaultTaskExecutor
parameter_list|()
block|{
name|String
name|pattern
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|getThreadNamePattern
argument_list|()
decl_stmt|;
name|String
name|beanName
init|=
name|getBeanName
argument_list|()
operator|==
literal|null
condition|?
name|endpoint
operator|.
name|getThreadName
argument_list|()
else|:
name|getBeanName
argument_list|()
decl_stmt|;
name|TaskExecutor
name|answer
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getDefaultTaskExecutorType
argument_list|()
operator|==
name|DefaultTaskExecutorType
operator|.
name|ThreadPool
condition|)
block|{
name|ThreadPoolTaskExecutor
name|executor
init|=
operator|new
name|ThreadPoolTaskExecutor
argument_list|()
decl_stmt|;
name|executor
operator|.
name|setBeanName
argument_list|(
name|beanName
argument_list|)
expr_stmt|;
name|executor
operator|.
name|setThreadFactory
argument_list|(
operator|new
name|CamelThreadFactory
argument_list|(
name|pattern
argument_list|,
name|beanName
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|executor
operator|.
name|setCorePoolSize
argument_list|(
name|endpoint
operator|.
name|getConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
comment|// Direct hand-off mode. Do not queue up tasks: assign it to a thread immediately.
comment|// We set no upper-bound on the thread pool (no maxPoolSize) as it's already implicitly constrained by
comment|// maxConcurrentConsumers on the DMLC itself (i.e. DMLC will only grow up to a level of concurrency as
comment|// defined by maxConcurrentConsumers).
name|executor
operator|.
name|setQueueCapacity
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|executor
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|answer
operator|=
name|executor
expr_stmt|;
block|}
else|else
block|{
name|SimpleAsyncTaskExecutor
name|executor
init|=
operator|new
name|SimpleAsyncTaskExecutor
argument_list|(
name|beanName
argument_list|)
decl_stmt|;
name|executor
operator|.
name|setThreadFactory
argument_list|(
operator|new
name|CamelThreadFactory
argument_list|(
name|pattern
argument_list|,
name|beanName
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|=
name|executor
expr_stmt|;
block|}
name|taskExecutor
operator|=
name|answer
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|JmsException
block|{
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Stopping listenerContainer: "
operator|+
name|this
operator|+
literal|" with cacheLevel: "
operator|+
name|getCacheLevel
argument_list|()
operator|+
literal|" and sharedConnectionEnabled: "
operator|+
name|sharedConnectionEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|stop
argument_list|()
expr_stmt|;
if|if
condition|(
name|taskExecutor
operator|instanceof
name|ThreadPoolTaskExecutor
condition|)
block|{
name|ThreadPoolTaskExecutor
name|executor
init|=
operator|(
name|ThreadPoolTaskExecutor
operator|)
name|taskExecutor
decl_stmt|;
name|executor
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Destroying listenerContainer: "
operator|+
name|this
operator|+
literal|" with cacheLevel: "
operator|+
name|getCacheLevel
argument_list|()
operator|+
literal|" and sharedConnectionEnabled: "
operator|+
name|sharedConnectionEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|destroy
argument_list|()
expr_stmt|;
if|if
condition|(
name|taskExecutor
operator|instanceof
name|ThreadPoolTaskExecutor
condition|)
block|{
name|ThreadPoolTaskExecutor
name|executor
init|=
operator|(
name|ThreadPoolTaskExecutor
operator|)
name|taskExecutor
decl_stmt|;
name|executor
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|stopSharedConnection ()
specifier|protected
name|void
name|stopSharedConnection
parameter_list|()
block|{
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|sharedConnectionEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Stopping shared connection on listenerContainer: "
operator|+
name|this
argument_list|)
expr_stmt|;
block|}
block|}
name|super
operator|.
name|stopSharedConnection
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

