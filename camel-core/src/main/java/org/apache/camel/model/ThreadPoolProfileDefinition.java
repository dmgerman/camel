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
name|ThreadPoolProfile
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;threadPoolProfile/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
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
implements|implements
name|ThreadPoolProfile
block|{
annotation|@
name|XmlAttribute
argument_list|()
DECL|field|defaultProfile
specifier|private
name|Boolean
name|defaultProfile
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|()
DECL|field|poolSize
specifier|private
name|Integer
name|poolSize
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|()
DECL|field|maxPoolSize
specifier|private
name|Integer
name|maxPoolSize
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|()
DECL|field|keepAliveTime
specifier|private
name|Long
name|keepAliveTime
decl_stmt|;
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
argument_list|()
DECL|field|maxQueueSize
specifier|private
name|Integer
name|maxQueueSize
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|()
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
DECL|method|ThreadPoolProfileDefinition (ThreadPoolProfile threadPoolProfile)
specifier|public
name|ThreadPoolProfileDefinition
parameter_list|(
name|ThreadPoolProfile
name|threadPoolProfile
parameter_list|)
block|{
name|setDefaultProfile
argument_list|(
name|threadPoolProfile
operator|.
name|isDefaultProfile
argument_list|()
argument_list|)
expr_stmt|;
name|setPoolSize
argument_list|(
name|threadPoolProfile
operator|.
name|getPoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|setMaxPoolSize
argument_list|(
name|threadPoolProfile
operator|.
name|getMaxPoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|setKeepAliveTime
argument_list|(
name|threadPoolProfile
operator|.
name|getKeepAliveTime
argument_list|()
argument_list|)
expr_stmt|;
name|setTimeUnit
argument_list|(
name|threadPoolProfile
operator|.
name|getTimeUnit
argument_list|()
argument_list|)
expr_stmt|;
name|setMaxQueueSize
argument_list|(
name|threadPoolProfile
operator|.
name|getMaxQueueSize
argument_list|()
argument_list|)
expr_stmt|;
name|setRejectedPolicy
argument_list|(
name|threadPoolProfile
operator|.
name|getRejectedPolicy
argument_list|()
argument_list|)
expr_stmt|;
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
name|setMaxPoolSize
argument_list|(
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
name|setKeepAliveTime
argument_list|(
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
name|setMaxQueueSize
argument_list|(
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

