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
name|RejectedExecutionHandler
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

begin_comment
comment|/**  * To configure thread pools  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"configuration"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"threadPoolProfile"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ThreadPoolProfileDefinition
specifier|public
class|class
name|ThreadPoolProfileDefinition
extends|extends
name|OptionalIdentifiedDefinition
argument_list|<
name|ThreadPoolProfileDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
DECL|field|defaultProfile
specifier|private
name|Boolean
name|defaultProfile
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|poolSize
specifier|private
name|String
name|poolSize
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|maxPoolSize
specifier|private
name|String
name|maxPoolSize
decl_stmt|;
annotation|@
name|XmlAttribute
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
DECL|field|timeUnit
specifier|private
name|TimeUnit
name|timeUnit
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|maxQueueSize
specifier|private
name|String
name|maxQueueSize
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|allowCoreThreadTimeOut
specifier|private
name|String
name|allowCoreThreadTimeOut
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|rejectedPolicy
specifier|private
name|ThreadPoolRejectedPolicy
name|rejectedPolicy
decl_stmt|;
DECL|method|ThreadPoolProfileDefinition ()
specifier|public
name|ThreadPoolProfileDefinition
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"threadPoolProfile"
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
literal|"ThreadPoolProfile "
operator|+
name|getId
argument_list|()
return|;
block|}
DECL|method|poolSize (int poolSize)
specifier|public
name|ThreadPoolProfileDefinition
name|poolSize
parameter_list|(
name|int
name|poolSize
parameter_list|)
block|{
return|return
name|poolSize
argument_list|(
literal|""
operator|+
name|poolSize
argument_list|)
return|;
block|}
DECL|method|poolSize (String poolSize)
specifier|public
name|ThreadPoolProfileDefinition
name|poolSize
parameter_list|(
name|String
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
DECL|method|maxPoolSize (int maxPoolSize)
specifier|public
name|ThreadPoolProfileDefinition
name|maxPoolSize
parameter_list|(
name|int
name|maxPoolSize
parameter_list|)
block|{
return|return
name|maxPoolSize
argument_list|(
literal|""
operator|+
name|maxQueueSize
argument_list|)
return|;
block|}
DECL|method|maxPoolSize (String maxPoolSize)
specifier|public
name|ThreadPoolProfileDefinition
name|maxPoolSize
parameter_list|(
name|String
name|maxPoolSize
parameter_list|)
block|{
name|setMaxPoolSize
argument_list|(
literal|""
operator|+
name|maxPoolSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|keepAliveTime (long keepAliveTime)
specifier|public
name|ThreadPoolProfileDefinition
name|keepAliveTime
parameter_list|(
name|long
name|keepAliveTime
parameter_list|)
block|{
return|return
name|keepAliveTime
argument_list|(
literal|""
operator|+
name|keepAliveTime
argument_list|)
return|;
block|}
DECL|method|keepAliveTime (String keepAliveTime)
specifier|public
name|ThreadPoolProfileDefinition
name|keepAliveTime
parameter_list|(
name|String
name|keepAliveTime
parameter_list|)
block|{
name|setKeepAliveTime
argument_list|(
literal|""
operator|+
name|keepAliveTime
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|timeUnit (TimeUnit timeUnit)
specifier|public
name|ThreadPoolProfileDefinition
name|timeUnit
parameter_list|(
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|setTimeUnit
argument_list|(
name|timeUnit
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|maxQueueSize (int maxQueueSize)
specifier|public
name|ThreadPoolProfileDefinition
name|maxQueueSize
parameter_list|(
name|int
name|maxQueueSize
parameter_list|)
block|{
return|return
name|maxQueueSize
argument_list|(
literal|""
operator|+
name|maxQueueSize
argument_list|)
return|;
block|}
DECL|method|maxQueueSize (String maxQueueSize)
specifier|public
name|ThreadPoolProfileDefinition
name|maxQueueSize
parameter_list|(
name|String
name|maxQueueSize
parameter_list|)
block|{
name|setMaxQueueSize
argument_list|(
literal|""
operator|+
name|maxQueueSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|rejectedPolicy (ThreadPoolRejectedPolicy rejectedPolicy)
specifier|public
name|ThreadPoolProfileDefinition
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
DECL|method|allowCoreThreadTimeOut (boolean allowCoreThreadTimeOut)
specifier|public
name|ThreadPoolProfileDefinition
name|allowCoreThreadTimeOut
parameter_list|(
name|boolean
name|allowCoreThreadTimeOut
parameter_list|)
block|{
name|setAllowCoreThreadTimeOut
argument_list|(
literal|""
operator|+
name|allowCoreThreadTimeOut
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getDefaultProfile ()
specifier|public
name|Boolean
name|getDefaultProfile
parameter_list|()
block|{
return|return
name|defaultProfile
return|;
block|}
comment|/**      * Whether this profile is the default thread pool profile      */
DECL|method|setDefaultProfile (Boolean defaultProfile)
specifier|public
name|void
name|setDefaultProfile
parameter_list|(
name|Boolean
name|defaultProfile
parameter_list|)
block|{
name|this
operator|.
name|defaultProfile
operator|=
name|defaultProfile
expr_stmt|;
block|}
DECL|method|isDefaultProfile ()
specifier|public
name|Boolean
name|isDefaultProfile
parameter_list|()
block|{
return|return
name|defaultProfile
operator|!=
literal|null
operator|&&
name|defaultProfile
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
comment|/**      * Sets the core pool size      */
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
comment|/**      * Sets the maximum pool size      */
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
comment|/**      * Sets the keep alive time for idle threads in the pool      */
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
comment|/**      * Sets the maximum number of tasks in the work queue.      *<p/>      * Use<tt>-1</tt> or<tt>Integer.MAX_VALUE</tt> for an unbounded queue      */
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
comment|/**      * Whether idle core threads is allowed to timeout and therefore can shrink the pool size below the core pool size      *<p/>      * Is by default<tt>false</tt>      */
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
comment|/**      * Sets the time unit to use for keep alive time      * By default SECONDS is used.      */
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
DECL|method|getRejectedExecutionHandler ()
specifier|public
name|RejectedExecutionHandler
name|getRejectedExecutionHandler
parameter_list|()
block|{
if|if
condition|(
name|rejectedPolicy
operator|!=
literal|null
condition|)
block|{
return|return
name|rejectedPolicy
operator|.
name|asRejectedExecutionHandler
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Sets the handler for tasks which cannot be executed by the thread pool.      */
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
block|}
end_class

end_unit

