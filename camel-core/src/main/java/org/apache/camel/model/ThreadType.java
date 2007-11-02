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
name|Collections
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
name|ThreadPoolExecutor
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
name|XmlElementRef
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
name|impl
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
name|ThreadProcessor
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;thread/&gt; element  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"thread"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ThreadType
specifier|public
class|class
name|ThreadType
extends|extends
name|ProcessorType
argument_list|<
name|ProcessorType
argument_list|>
block|{
annotation|@
name|XmlAttribute
DECL|field|coreSize
specifier|private
name|int
name|coreSize
init|=
literal|1
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|daemon
specifier|private
name|boolean
name|daemon
init|=
literal|true
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|keepAliveTime
specifier|private
name|long
name|keepAliveTime
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|maxSize
specifier|private
name|int
name|maxSize
init|=
literal|1
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|name
specifier|private
name|String
name|name
init|=
literal|"Thread Processor"
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|priority
specifier|private
name|int
name|priority
init|=
name|Thread
operator|.
name|NORM_PRIORITY
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|stackSize
specifier|private
name|long
name|stackSize
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|outputs
specifier|private
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|taskQueue
specifier|private
name|BlockingQueue
argument_list|<
name|Runnable
argument_list|>
name|taskQueue
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|threadGroup
specifier|private
name|ThreadGroup
name|threadGroup
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|executor
specifier|private
name|ThreadPoolExecutor
name|executor
decl_stmt|;
DECL|method|ThreadType ()
specifier|public
name|ThreadType
parameter_list|()
block|{     }
DECL|method|ThreadType (int coreSize)
specifier|public
name|ThreadType
parameter_list|(
name|int
name|coreSize
parameter_list|)
block|{
name|this
operator|.
name|coreSize
operator|=
name|coreSize
expr_stmt|;
name|this
operator|.
name|maxSize
operator|=
name|coreSize
expr_stmt|;
block|}
DECL|method|ThreadType (ThreadPoolExecutor executor)
specifier|public
name|ThreadType
parameter_list|(
name|ThreadPoolExecutor
name|executor
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
comment|/*     @Override     public List getInterceptors() {         return Collections.EMPTY_LIST;     } */
annotation|@
name|Override
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|outputs
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
literal|"Thread["
operator|+
name|getLabel
argument_list|()
operator|+
literal|"]"
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
literal|"coreSize="
operator|+
name|coreSize
return|;
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
name|ThreadProcessor
name|thread
init|=
operator|new
name|ThreadProcessor
argument_list|()
decl_stmt|;
name|thread
operator|.
name|setExecutor
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|thread
operator|.
name|setCoreSize
argument_list|(
name|coreSize
argument_list|)
expr_stmt|;
name|thread
operator|.
name|setDaemon
argument_list|(
name|daemon
argument_list|)
expr_stmt|;
name|thread
operator|.
name|setKeepAliveTime
argument_list|(
name|keepAliveTime
argument_list|)
expr_stmt|;
name|thread
operator|.
name|setMaxSize
argument_list|(
name|maxSize
argument_list|)
expr_stmt|;
name|thread
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|thread
operator|.
name|setPriority
argument_list|(
name|priority
argument_list|)
expr_stmt|;
name|thread
operator|.
name|setStackSize
argument_list|(
name|stackSize
argument_list|)
expr_stmt|;
name|thread
operator|.
name|setTaskQueue
argument_list|(
name|taskQueue
argument_list|)
expr_stmt|;
name|thread
operator|.
name|setThreadGroup
argument_list|(
name|threadGroup
argument_list|)
expr_stmt|;
comment|// TODO: see if we can avoid creating so many nested pipelines
name|ArrayList
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
name|createOutputsProcessor
argument_list|(
name|routeContext
argument_list|,
name|outputs
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|Pipeline
argument_list|(
name|pipe
argument_list|)
return|;
block|}
comment|///////////////////////////////////////////////////////////////////
comment|//
comment|// Fluent Methods
comment|//
comment|///////////////////////////////////////////////////////////////////
DECL|method|coreSize (int coreSize)
specifier|public
name|ThreadType
name|coreSize
parameter_list|(
name|int
name|coreSize
parameter_list|)
block|{
name|setCoreSize
argument_list|(
name|coreSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|daemon (boolean daemon)
specifier|public
name|ThreadType
name|daemon
parameter_list|(
name|boolean
name|daemon
parameter_list|)
block|{
name|setDaemon
argument_list|(
name|daemon
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|keepAliveTime (long keepAliveTime)
specifier|public
name|ThreadType
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
DECL|method|maxSize (int maxSize)
specifier|public
name|ThreadType
name|maxSize
parameter_list|(
name|int
name|maxSize
parameter_list|)
block|{
name|setMaxSize
argument_list|(
name|maxSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|name (String name)
specifier|public
name|ThreadType
name|name
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|priority (int priority)
specifier|public
name|ThreadType
name|priority
parameter_list|(
name|int
name|priority
parameter_list|)
block|{
name|setPriority
argument_list|(
name|priority
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|stackSize (long stackSize)
specifier|public
name|ThreadType
name|stackSize
parameter_list|(
name|long
name|stackSize
parameter_list|)
block|{
name|setStackSize
argument_list|(
name|stackSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|taskQueue (BlockingQueue<Runnable> taskQueue)
specifier|public
name|ThreadType
name|taskQueue
parameter_list|(
name|BlockingQueue
argument_list|<
name|Runnable
argument_list|>
name|taskQueue
parameter_list|)
block|{
name|setTaskQueue
argument_list|(
name|taskQueue
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|threadGroup (ThreadGroup threadGroup)
specifier|public
name|ThreadType
name|threadGroup
parameter_list|(
name|ThreadGroup
name|threadGroup
parameter_list|)
block|{
name|setThreadGroup
argument_list|(
name|threadGroup
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|executor (ThreadPoolExecutor executor)
specifier|public
name|ThreadType
name|executor
parameter_list|(
name|ThreadPoolExecutor
name|executor
parameter_list|)
block|{
name|setExecutor
argument_list|(
name|executor
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|///////////////////////////////////////////////////////////////////
comment|//
comment|// Property Accessors
comment|//
comment|///////////////////////////////////////////////////////////////////
DECL|method|setCoreSize (int coreSize)
specifier|public
name|void
name|setCoreSize
parameter_list|(
name|int
name|coreSize
parameter_list|)
block|{
name|this
operator|.
name|coreSize
operator|=
name|coreSize
expr_stmt|;
block|}
DECL|method|setDaemon (boolean daemon)
specifier|public
name|void
name|setDaemon
parameter_list|(
name|boolean
name|daemon
parameter_list|)
block|{
name|this
operator|.
name|daemon
operator|=
name|daemon
expr_stmt|;
block|}
DECL|method|setKeepAliveTime (long keepAliveTime)
specifier|public
name|void
name|setKeepAliveTime
parameter_list|(
name|long
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
DECL|method|setMaxSize (int maxSize)
specifier|public
name|void
name|setMaxSize
parameter_list|(
name|int
name|maxSize
parameter_list|)
block|{
name|this
operator|.
name|maxSize
operator|=
name|maxSize
expr_stmt|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|setPriority (int priority)
specifier|public
name|void
name|setPriority
parameter_list|(
name|int
name|priority
parameter_list|)
block|{
name|this
operator|.
name|priority
operator|=
name|priority
expr_stmt|;
block|}
DECL|method|setStackSize (long stackSize)
specifier|public
name|void
name|setStackSize
parameter_list|(
name|long
name|stackSize
parameter_list|)
block|{
name|this
operator|.
name|stackSize
operator|=
name|stackSize
expr_stmt|;
block|}
DECL|method|setTaskQueue (BlockingQueue<Runnable> taskQueue)
specifier|public
name|void
name|setTaskQueue
parameter_list|(
name|BlockingQueue
argument_list|<
name|Runnable
argument_list|>
name|taskQueue
parameter_list|)
block|{
name|this
operator|.
name|taskQueue
operator|=
name|taskQueue
expr_stmt|;
block|}
DECL|method|setThreadGroup (ThreadGroup threadGroup)
specifier|public
name|void
name|setThreadGroup
parameter_list|(
name|ThreadGroup
name|threadGroup
parameter_list|)
block|{
name|this
operator|.
name|threadGroup
operator|=
name|threadGroup
expr_stmt|;
block|}
DECL|method|getExecutor ()
specifier|public
name|ThreadPoolExecutor
name|getExecutor
parameter_list|()
block|{
return|return
name|executor
return|;
block|}
DECL|method|setExecutor (ThreadPoolExecutor executor)
specifier|public
name|void
name|setExecutor
parameter_list|(
name|ThreadPoolExecutor
name|executor
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
block|}
end_class

end_unit

