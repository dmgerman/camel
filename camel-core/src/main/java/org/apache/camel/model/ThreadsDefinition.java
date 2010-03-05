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
name|ExecutorServiceAware
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
if|if
condition|(
name|executorServiceRef
operator|!=
literal|null
condition|)
block|{
name|executorService
operator|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|executorServiceRef
argument_list|,
name|ExecutorService
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|executorService
operator|==
literal|null
operator|&&
name|poolSize
operator|!=
literal|null
condition|)
block|{
name|executorService
operator|=
name|ExecutorServiceHelper
operator|.
name|newScheduledThreadPool
argument_list|(
name|poolSize
argument_list|,
literal|"Threads"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
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
comment|/**      * Setting the core pool size for the underlying {@link java.util.concurrent.ExecutorService}.      *      * @return the builder      */
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
block|}
end_class

end_unit

