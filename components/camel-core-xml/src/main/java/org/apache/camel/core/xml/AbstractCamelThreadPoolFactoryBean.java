begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|xml
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
name|spi
operator|.
name|Metadata
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
name|CamelContextHelper
import|;
end_import

begin_comment
comment|/**  * A factory which instantiates {@link java.util.concurrent.ExecutorService} objects  */
end_comment

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|AbstractCamelThreadPoolFactoryBean
specifier|public
specifier|abstract
class|class
name|AbstractCamelThreadPoolFactoryBean
extends|extends
name|AbstractCamelFactoryBean
argument_list|<
name|ExecutorService
argument_list|>
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Sets the core pool size (threads to keep minimum in pool)"
argument_list|)
DECL|field|poolSize
specifier|private
name|String
name|poolSize
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Sets the maximum pool size"
argument_list|)
DECL|field|maxPoolSize
specifier|private
name|String
name|maxPoolSize
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Sets the keep alive time for inactive threads"
argument_list|)
DECL|field|keepAliveTime
specifier|private
name|String
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
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Sets the time unit used for keep alive time"
argument_list|,
name|defaultValue
operator|=
literal|"SECONDS"
argument_list|)
DECL|field|timeUnit
specifier|private
name|TimeUnit
name|timeUnit
init|=
name|TimeUnit
operator|.
name|SECONDS
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Sets the maximum number of tasks in the work queue. Use -1 for an unbounded queue"
argument_list|)
DECL|field|maxQueueSize
specifier|private
name|String
name|maxQueueSize
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Sets whether to allow core threads to timeout"
argument_list|)
DECL|field|allowCoreThreadTimeOut
specifier|private
name|String
name|allowCoreThreadTimeOut
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Sets the handler for tasks which cannot be executed by the thread pool."
argument_list|,
name|defaultValue
operator|=
literal|"CallerRuns"
argument_list|)
DECL|field|rejectedPolicy
specifier|private
name|ThreadPoolRejectedPolicy
name|rejectedPolicy
init|=
name|ThreadPoolRejectedPolicy
operator|.
name|CallerRuns
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"To use a custom thread name / pattern"
argument_list|)
DECL|field|threadName
specifier|private
name|String
name|threadName
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Whether to use a scheduled thread pool"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|scheduled
specifier|private
name|Boolean
name|scheduled
decl_stmt|;
DECL|method|getObject ()
specifier|public
name|ExecutorService
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|size
init|=
name|CamelContextHelper
operator|.
name|parseInteger
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|poolSize
argument_list|)
decl_stmt|;
if|if
condition|(
name|size
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"PoolSize must be a positive number"
argument_list|)
throw|;
block|}
name|int
name|max
init|=
name|size
decl_stmt|;
if|if
condition|(
name|maxPoolSize
operator|!=
literal|null
condition|)
block|{
name|max
operator|=
name|CamelContextHelper
operator|.
name|parseInteger
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|maxPoolSize
argument_list|)
expr_stmt|;
block|}
name|long
name|keepAlive
init|=
literal|60
decl_stmt|;
if|if
condition|(
name|keepAliveTime
operator|!=
literal|null
condition|)
block|{
name|keepAlive
operator|=
name|CamelContextHelper
operator|.
name|parseLong
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|keepAliveTime
argument_list|)
expr_stmt|;
block|}
name|int
name|queueSize
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|maxQueueSize
operator|!=
literal|null
condition|)
block|{
name|queueSize
operator|=
name|CamelContextHelper
operator|.
name|parseInteger
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|maxQueueSize
argument_list|)
expr_stmt|;
block|}
name|boolean
name|allow
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|allowCoreThreadTimeOut
operator|!=
literal|null
condition|)
block|{
name|allow
operator|=
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|allowCoreThreadTimeOut
argument_list|)
expr_stmt|;
block|}
name|ThreadPoolProfile
name|profile
init|=
operator|new
name|ThreadPoolProfileBuilder
argument_list|(
name|getId
argument_list|()
argument_list|)
operator|.
name|poolSize
argument_list|(
name|size
argument_list|)
operator|.
name|maxPoolSize
argument_list|(
name|max
argument_list|)
operator|.
name|keepAliveTime
argument_list|(
name|keepAlive
argument_list|,
name|timeUnit
argument_list|)
operator|.
name|maxQueueSize
argument_list|(
name|queueSize
argument_list|)
operator|.
name|allowCoreThreadTimeOut
argument_list|(
name|allow
argument_list|)
operator|.
name|rejectedPolicy
argument_list|(
name|rejectedPolicy
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|ExecutorService
name|answer
decl_stmt|;
if|if
condition|(
name|scheduled
operator|!=
literal|null
operator|&&
name|scheduled
condition|)
block|{
name|answer
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newScheduledThreadPool
argument_list|(
name|getId
argument_list|()
argument_list|,
name|getThreadName
argument_list|()
argument_list|,
name|profile
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|getId
argument_list|()
argument_list|,
name|getThreadName
argument_list|()
argument_list|,
name|profile
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getObjectType ()
specifier|public
name|Class
argument_list|<
name|ExecutorService
argument_list|>
name|getObjectType
parameter_list|()
block|{
return|return
name|ExecutorService
operator|.
name|class
return|;
block|}
DECL|method|getPoolSize ()
specifier|public
name|String
name|getPoolSize
parameter_list|()
block|{
return|return
name|poolSize
return|;
block|}
DECL|method|setPoolSize (String poolSize)
specifier|public
name|void
name|setPoolSize
parameter_list|(
name|String
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
name|String
name|getMaxPoolSize
parameter_list|()
block|{
return|return
name|maxPoolSize
return|;
block|}
DECL|method|setMaxPoolSize (String maxPoolSize)
specifier|public
name|void
name|setMaxPoolSize
parameter_list|(
name|String
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
name|String
name|getKeepAliveTime
parameter_list|()
block|{
return|return
name|keepAliveTime
return|;
block|}
DECL|method|setKeepAliveTime (String keepAliveTime)
specifier|public
name|void
name|setKeepAliveTime
parameter_list|(
name|String
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
name|String
name|getMaxQueueSize
parameter_list|()
block|{
return|return
name|maxQueueSize
return|;
block|}
DECL|method|setMaxQueueSize (String maxQueueSize)
specifier|public
name|void
name|setMaxQueueSize
parameter_list|(
name|String
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
DECL|method|getAllowCoreThreadTimeOut ()
specifier|public
name|String
name|getAllowCoreThreadTimeOut
parameter_list|()
block|{
return|return
name|allowCoreThreadTimeOut
return|;
block|}
DECL|method|setAllowCoreThreadTimeOut (String allowCoreThreadTimeOut)
specifier|public
name|void
name|setAllowCoreThreadTimeOut
parameter_list|(
name|String
name|allowCoreThreadTimeOut
parameter_list|)
block|{
name|this
operator|.
name|allowCoreThreadTimeOut
operator|=
name|allowCoreThreadTimeOut
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
DECL|method|getScheduled ()
specifier|public
name|Boolean
name|getScheduled
parameter_list|()
block|{
return|return
name|scheduled
return|;
block|}
DECL|method|setScheduled (Boolean scheduled)
specifier|public
name|void
name|setScheduled
parameter_list|(
name|Boolean
name|scheduled
parameter_list|)
block|{
name|this
operator|.
name|scheduled
operator|=
name|scheduled
expr_stmt|;
block|}
block|}
end_class

end_unit

