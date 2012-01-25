begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|adapters
operator|.
name|XmlJavaTypeAdapter
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
name|ThreadPoolRejectedPolicy
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
name|builder
operator|.
name|ThreadPoolProfileBuilder
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
name|builder
operator|.
name|xml
operator|.
name|TimeUnitAdapter
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
name|processor
operator|.
name|Pipeline
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
name|processor
operator|.
name|ThreadsProcessor
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
name|ExecutorServiceManager
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
name|RouteContext
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
name|ThreadPoolProfile
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;threads/&gt; element  *  * @version   */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"threads"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ThreadsDefinition
specifier|public
class|class
name|ThreadsDefinition
extends|extends
name|OutputDefinition
argument_list|<
name|ThreadsDefinition
argument_list|>
implements|implements
name|ExecutorServiceAwareDefinition
argument_list|<
name|ThreadsDefinition
argument_list|>
block|{
comment|// TODO: Camel 3.0 Should extend NoOutputDefinition
annotation|@
name|XmlTransient
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|executorServiceRef
specifier|private
name|String
name|executorServiceRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|poolSize
specifier|private
name|Integer
name|poolSize
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|maxPoolSize
specifier|private
name|Integer
name|maxPoolSize
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|keepAliveTime
specifier|private
name|Long
name|keepAliveTime
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|XmlJavaTypeAdapter
argument_list|(
name|TimeUnitAdapter
operator|.
name|class
argument_list|)
DECL|field|timeUnit
specifier|private
name|TimeUnit
name|timeUnit
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|maxQueueSize
specifier|private
name|Integer
name|maxQueueSize
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|threadName
specifier|private
name|String
name|threadName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|rejectedPolicy
specifier|private
name|ThreadPoolRejectedPolicy
name|rejectedPolicy
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|callerRunsWhenRejected
specifier|private
name|Boolean
name|callerRunsWhenRejected
decl_stmt|;
DECL|method|ThreadsDefinition ()
specifier|public
name|ThreadsDefinition
parameter_list|()
block|{
name|this
operator|.
name|threadName
operator|=
literal|"Threads"
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
comment|// the threads name
name|String
name|name
init|=
name|getThreadName
argument_list|()
operator|!=
literal|null
condition|?
name|getThreadName
argument_list|()
else|:
literal|"Threads"
decl_stmt|;
comment|// prefer any explicit configured executor service
name|executorService
operator|=
name|ProcessorDefinitionHelper
operator|.
name|getConfiguredExecutorService
argument_list|(
name|routeContext
argument_list|,
name|name
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// if no explicit then create from the options
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
name|ExecutorServiceManager
name|manager
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
decl_stmt|;
comment|// create the thread pool using a builder
name|ThreadPoolProfile
name|profile
init|=
operator|new
name|ThreadPoolProfileBuilder
argument_list|(
name|name
argument_list|)
operator|.
name|poolSize
argument_list|(
name|getPoolSize
argument_list|()
argument_list|)
operator|.
name|maxPoolSize
argument_list|(
name|getMaxPoolSize
argument_list|()
argument_list|)
operator|.
name|keepAliveTime
argument_list|(
name|getKeepAliveTime
argument_list|()
argument_list|,
name|getTimeUnit
argument_list|()
argument_list|)
operator|.
name|maxQueueSize
argument_list|(
name|getMaxQueueSize
argument_list|()
argument_list|)
operator|.
name|rejectedPolicy
argument_list|(
name|getRejectedPolicy
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|executorService
operator|=
name|manager
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
name|name
argument_list|,
name|profile
argument_list|)
expr_stmt|;
block|}
name|ThreadsProcessor
name|thread
init|=
operator|new
name|ThreadsProcessor
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|executorService
argument_list|)
decl_stmt|;
if|if
condition|(
name|getCallerRunsWhenRejected
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// should be true by default
name|thread
operator|.
name|setCallerRunsWhenRejected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|thread
operator|.
name|setCallerRunsWhenRejected
argument_list|(
name|getCallerRunsWhenRejected
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|thread
operator|.
name|setRejectedPolicy
argument_list|(
name|getRejectedPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Processor
argument_list|>
name|pipe
init|=
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|pipe
operator|.
name|add
argument_list|(
name|thread
argument_list|)
expr_stmt|;
name|pipe
operator|.
name|add
argument_list|(
name|createChildProcessor
argument_list|(
name|routeContext
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// wrap in nested pipeline so this appears as one processor
comment|// (recipient list definition does this as well)
return|return
operator|new
name|Pipeline
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|pipe
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Threads["
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
literal|"threads"
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"threads"
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
literal|"Threads["
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|executorService (ExecutorService executorService)
specifier|public
name|ThreadsDefinition
name|executorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|setExecutorService
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|executorServiceRef (String executorServiceRef)
specifier|public
name|ThreadsDefinition
name|executorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
block|{
name|setExecutorServiceRef
argument_list|(
name|executorServiceRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the core pool size for the underlying {@link java.util.concurrent.ExecutorService}.      *      * @param poolSize the core pool size to keep minimum in the pool      * @return the builder      */
DECL|method|poolSize (int poolSize)
specifier|public
name|ThreadsDefinition
name|poolSize
parameter_list|(
name|int
name|poolSize
parameter_list|)
block|{
name|setPoolSize
argument_list|(
name|poolSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the maximum pool size for the underlying {@link java.util.concurrent.ExecutorService}.      *      * @param maxPoolSize the maximum pool size      * @return the builder      */
DECL|method|maxPoolSize (int maxPoolSize)
specifier|public
name|ThreadsDefinition
name|maxPoolSize
parameter_list|(
name|int
name|maxPoolSize
parameter_list|)
block|{
name|setMaxPoolSize
argument_list|(
name|maxPoolSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the keep alive time for idle threads      *      * @param keepAliveTime keep alive time      * @return the builder      */
DECL|method|keepAliveTime (long keepAliveTime)
specifier|public
name|ThreadsDefinition
name|keepAliveTime
parameter_list|(
name|long
name|keepAliveTime
parameter_list|)
block|{
name|setKeepAliveTime
argument_list|(
name|keepAliveTime
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the keep alive time unit.      * By default SECONDS is used.      *      * @param keepAliveTimeUnits time unit      * @return the builder      */
DECL|method|timeUnit (TimeUnit keepAliveTimeUnits)
specifier|public
name|ThreadsDefinition
name|timeUnit
parameter_list|(
name|TimeUnit
name|keepAliveTimeUnits
parameter_list|)
block|{
name|setTimeUnit
argument_list|(
name|keepAliveTimeUnits
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the maximum number of tasks in the work queue.      *<p/>      * Use<tt>-1</tt> or<tt>Integer.MAX_VALUE</tt> for an unbounded queue      *      * @param maxQueueSize the max queue size      * @return the builder      */
DECL|method|maxQueueSize (int maxQueueSize)
specifier|public
name|ThreadsDefinition
name|maxQueueSize
parameter_list|(
name|int
name|maxQueueSize
parameter_list|)
block|{
name|setMaxQueueSize
argument_list|(
name|maxQueueSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the handler for tasks which cannot be executed by the thread pool.      *      * @param rejectedPolicy  the policy for the handler      * @return the builder      */
DECL|method|rejectedPolicy (ThreadPoolRejectedPolicy rejectedPolicy)
specifier|public
name|ThreadsDefinition
name|rejectedPolicy
parameter_list|(
name|ThreadPoolRejectedPolicy
name|rejectedPolicy
parameter_list|)
block|{
name|setRejectedPolicy
argument_list|(
name|rejectedPolicy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the thread name to use.      *      * @param threadName the thread name      * @return the builder      */
DECL|method|threadName (String threadName)
specifier|public
name|ThreadsDefinition
name|threadName
parameter_list|(
name|String
name|threadName
parameter_list|)
block|{
name|setThreadName
argument_list|(
name|threadName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether or not the caller should run the task when it was rejected by the thread pool.      *<p/>      * Is by default<tt>true</tt>      *      * @param callerRunsWhenRejected whether or not the caller should run      * @return the builder      */
DECL|method|callerRunsWhenRejected (boolean callerRunsWhenRejected)
specifier|public
name|ThreadsDefinition
name|callerRunsWhenRejected
parameter_list|(
name|boolean
name|callerRunsWhenRejected
parameter_list|)
block|{
name|setCallerRunsWhenRejected
argument_list|(
name|callerRunsWhenRejected
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getExecutorService ()
specifier|public
name|ExecutorService
name|getExecutorService
parameter_list|()
block|{
return|return
name|executorService
return|;
block|}
DECL|method|setExecutorService (ExecutorService executorService)
specifier|public
name|void
name|setExecutorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
block|}
DECL|method|getExecutorServiceRef ()
specifier|public
name|String
name|getExecutorServiceRef
parameter_list|()
block|{
return|return
name|executorServiceRef
return|;
block|}
DECL|method|setExecutorServiceRef (String executorServiceRef)
specifier|public
name|void
name|setExecutorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
block|{
name|this
operator|.
name|executorServiceRef
operator|=
name|executorServiceRef
expr_stmt|;
block|}
DECL|method|getPoolSize ()
specifier|public
name|Integer
name|getPoolSize
parameter_list|()
block|{
return|return
name|poolSize
return|;
block|}
DECL|method|setPoolSize (Integer poolSize)
specifier|public
name|void
name|setPoolSize
parameter_list|(
name|Integer
name|poolSize
parameter_list|)
block|{
name|this
operator|.
name|poolSize
operator|=
name|poolSize
expr_stmt|;
block|}
DECL|method|getMaxPoolSize ()
specifier|public
name|Integer
name|getMaxPoolSize
parameter_list|()
block|{
return|return
name|maxPoolSize
return|;
block|}
DECL|method|setMaxPoolSize (Integer maxPoolSize)
specifier|public
name|void
name|setMaxPoolSize
parameter_list|(
name|Integer
name|maxPoolSize
parameter_list|)
block|{
name|this
operator|.
name|maxPoolSize
operator|=
name|maxPoolSize
expr_stmt|;
block|}
DECL|method|getKeepAliveTime ()
specifier|public
name|Long
name|getKeepAliveTime
parameter_list|()
block|{
return|return
name|keepAliveTime
return|;
block|}
DECL|method|setKeepAliveTime (Long keepAliveTime)
specifier|public
name|void
name|setKeepAliveTime
parameter_list|(
name|Long
name|keepAliveTime
parameter_list|)
block|{
name|this
operator|.
name|keepAliveTime
operator|=
name|keepAliveTime
expr_stmt|;
block|}
DECL|method|getTimeUnit ()
specifier|public
name|TimeUnit
name|getTimeUnit
parameter_list|()
block|{
return|return
name|timeUnit
return|;
block|}
DECL|method|setTimeUnit (TimeUnit timeUnit)
specifier|public
name|void
name|setTimeUnit
parameter_list|(
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|this
operator|.
name|timeUnit
operator|=
name|timeUnit
expr_stmt|;
block|}
DECL|method|getMaxQueueSize ()
specifier|public
name|Integer
name|getMaxQueueSize
parameter_list|()
block|{
return|return
name|maxQueueSize
return|;
block|}
DECL|method|setMaxQueueSize (Integer maxQueueSize)
specifier|public
name|void
name|setMaxQueueSize
parameter_list|(
name|Integer
name|maxQueueSize
parameter_list|)
block|{
name|this
operator|.
name|maxQueueSize
operator|=
name|maxQueueSize
expr_stmt|;
block|}
DECL|method|getThreadName ()
specifier|public
name|String
name|getThreadName
parameter_list|()
block|{
return|return
name|threadName
return|;
block|}
DECL|method|setThreadName (String threadName)
specifier|public
name|void
name|setThreadName
parameter_list|(
name|String
name|threadName
parameter_list|)
block|{
name|this
operator|.
name|threadName
operator|=
name|threadName
expr_stmt|;
block|}
DECL|method|getRejectedPolicy ()
specifier|public
name|ThreadPoolRejectedPolicy
name|getRejectedPolicy
parameter_list|()
block|{
return|return
name|rejectedPolicy
return|;
block|}
DECL|method|setRejectedPolicy (ThreadPoolRejectedPolicy rejectedPolicy)
specifier|public
name|void
name|setRejectedPolicy
parameter_list|(
name|ThreadPoolRejectedPolicy
name|rejectedPolicy
parameter_list|)
block|{
name|this
operator|.
name|rejectedPolicy
operator|=
name|rejectedPolicy
expr_stmt|;
block|}
DECL|method|getCallerRunsWhenRejected ()
specifier|public
name|Boolean
name|getCallerRunsWhenRejected
parameter_list|()
block|{
return|return
name|callerRunsWhenRejected
return|;
block|}
DECL|method|setCallerRunsWhenRejected (Boolean callerRunsWhenRejected)
specifier|public
name|void
name|setCallerRunsWhenRejected
parameter_list|(
name|Boolean
name|callerRunsWhenRejected
parameter_list|)
block|{
name|this
operator|.
name|callerRunsWhenRejected
operator|=
name|callerRunsWhenRejected
expr_stmt|;
block|}
block|}
end_class

end_unit

