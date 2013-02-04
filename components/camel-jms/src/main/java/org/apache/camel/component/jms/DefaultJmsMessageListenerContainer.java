begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * The default {@link DefaultMessageListenerContainer container} which listen for messages  * on the JMS destination.  *<p/>  * This implementation extends Springs {@link DefaultMessageListenerContainer} supporting  * automatic recovery and throttling.  *  * @version   */
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
DECL|method|DefaultJmsMessageListenerContainer (JmsEndpoint endpoint)
specifier|public
name|DefaultJmsMessageListenerContainer
parameter_list|(
name|JmsEndpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|runningAllowed ()
specifier|protected
name|boolean
name|runningAllowed
parameter_list|()
block|{
comment|// do not run if we have been stopped
return|return
name|endpoint
operator|.
name|isRunning
argument_list|()
return|;
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
name|answer
init|=
operator|new
name|ThreadPoolTaskExecutor
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setBeanName
argument_list|(
name|beanName
argument_list|)
expr_stmt|;
name|answer
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
name|answer
operator|.
name|setQueueCapacity
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|answer
operator|.
name|initialize
argument_list|()
expr_stmt|;
return|return
name|answer
return|;
block|}
else|else
block|{
name|SimpleAsyncTaskExecutor
name|answer
init|=
operator|new
name|SimpleAsyncTaskExecutor
argument_list|(
name|beanName
argument_list|)
decl_stmt|;
name|answer
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
return|return
name|answer
return|;
block|}
block|}
block|}
end_class

end_unit

