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
name|WaitForTaskToComplete
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
name|processor
operator|.
name|UnitOfWorkProcessor
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
name|util
operator|.
name|concurrent
operator|.
name|ExecutorServiceHelper
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;threads/&gt; element  *  * @version $Revision$  */
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
name|ProcessorDefinition
argument_list|>
implements|implements
name|ExecutorServiceAwareDefinition
argument_list|<
name|ThreadsDefinition
argument_list|>
block|{
annotation|@
name|XmlTransient
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|executorServiceRef
specifier|private
name|String
name|executorServiceRef
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|poolSize
specifier|private
name|Integer
name|poolSize
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|maxPoolSize
specifier|private
name|Integer
name|maxPoolSize
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|keepAliveTime
specifier|private
name|Integer
name|keepAliveTime
init|=
literal|60
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
annotation|@
name|XmlJavaTypeAdapter
argument_list|(
name|TimeUnitAdapter
operator|.
name|class
argument_list|)
DECL|field|units
specifier|private
name|TimeUnit
name|units
init|=
name|TimeUnit
operator|.
name|SECONDS
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|threadName
specifier|private
name|String
name|threadName
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|waitForTaskToComplete
specifier|private
name|WaitForTaskToComplete
name|waitForTaskToComplete
init|=
name|WaitForTaskToComplete
operator|.
name|IfReplyExpected
decl_stmt|;
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
comment|// prefer any explicit configured executor service
name|executorService
operator|=
name|ExecutorServiceHelper
operator|.
name|getConfiguredExecutorService
argument_list|(
name|routeContext
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
comment|// none was configured so create an executor based on the other parameters
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
if|if
condition|(
name|poolSize
operator|==
literal|null
operator|||
name|poolSize
operator|<=
literal|0
condition|)
block|{
comment|// use the cached thread pool
name|executorService
operator|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newCachedThreadPool
argument_list|(
name|this
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// use a custom pool based on the settings
name|int
name|max
init|=
name|getMaxPoolSize
argument_list|()
operator|!=
literal|null
condition|?
name|getMaxPoolSize
argument_list|()
else|:
name|poolSize
decl_stmt|;
name|executorService
operator|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
name|name
argument_list|,
name|poolSize
argument_list|,
name|max
argument_list|,
name|getKeepAliveTime
argument_list|()
argument_list|,
name|getUnits
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|Processor
name|childProcessor
init|=
name|routeContext
operator|.
name|createProcessor
argument_list|(
name|this
argument_list|)
decl_stmt|;
comment|// wrap it in a unit of work so the route that comes next is also done in a unit of work
name|UnitOfWorkProcessor
name|uow
init|=
operator|new
name|UnitOfWorkProcessor
argument_list|(
name|routeContext
argument_list|,
name|childProcessor
argument_list|)
decl_stmt|;
return|return
operator|new
name|ThreadsProcessor
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|uow
argument_list|,
name|executorService
argument_list|,
name|waitForTaskToComplete
argument_list|)
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
DECL|method|keepAliveTime (int keepAliveTime)
specifier|public
name|ThreadsDefinition
name|keepAliveTime
parameter_list|(
name|int
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
DECL|method|units (TimeUnit keepAliveTimeUnits)
specifier|public
name|ThreadsDefinition
name|units
parameter_list|(
name|TimeUnit
name|keepAliveTimeUnits
parameter_list|)
block|{
name|setUnits
argument_list|(
name|keepAliveTimeUnits
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
comment|/**      * Setting to whether to wait for async tasks to be complete before continuing original route.      *<p/>      * Is default<tt>IfReplyExpected</tt>      *      * @param wait the wait option      * @return the builder      */
DECL|method|waitForTaskToComplete (WaitForTaskToComplete wait)
specifier|public
name|ThreadsDefinition
name|waitForTaskToComplete
parameter_list|(
name|WaitForTaskToComplete
name|wait
parameter_list|)
block|{
name|setWaitForTaskToComplete
argument_list|(
name|wait
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
DECL|method|getWaitForTaskToComplete ()
specifier|public
name|WaitForTaskToComplete
name|getWaitForTaskToComplete
parameter_list|()
block|{
return|return
name|waitForTaskToComplete
return|;
block|}
DECL|method|setWaitForTaskToComplete (WaitForTaskToComplete waitForTaskToComplete)
specifier|public
name|void
name|setWaitForTaskToComplete
parameter_list|(
name|WaitForTaskToComplete
name|waitForTaskToComplete
parameter_list|)
block|{
name|this
operator|.
name|waitForTaskToComplete
operator|=
name|waitForTaskToComplete
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
name|Integer
name|getKeepAliveTime
parameter_list|()
block|{
return|return
name|keepAliveTime
return|;
block|}
DECL|method|setKeepAliveTime (Integer keepAliveTime)
specifier|public
name|void
name|setKeepAliveTime
parameter_list|(
name|Integer
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
DECL|method|getUnits ()
specifier|public
name|TimeUnit
name|getUnits
parameter_list|()
block|{
return|return
name|units
return|;
block|}
DECL|method|setUnits (TimeUnit units)
specifier|public
name|void
name|setUnits
parameter_list|(
name|TimeUnit
name|units
parameter_list|)
block|{
name|this
operator|.
name|units
operator|=
name|units
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
block|}
end_class

end_unit

