begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|concurrent
operator|.
name|ThreadPoolRejectedPolicy
import|;
end_import

begin_comment
comment|/**  * Builder to build {@link org.apache.camel.spi.ThreadPoolProfile}.  *<p/>  * Use the {@link #build()} method when done setting up the profile.  */
end_comment

begin_class
DECL|class|ThreadPoolProfileBuilder
specifier|public
class|class
name|ThreadPoolProfileBuilder
block|{
DECL|field|profile
specifier|private
specifier|final
name|ThreadPoolProfile
name|profile
decl_stmt|;
DECL|method|ThreadPoolProfileBuilder (String id)
specifier|public
name|ThreadPoolProfileBuilder
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|profile
operator|=
operator|new
name|ThreadPoolProfile
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
DECL|method|ThreadPoolProfileBuilder (String id, ThreadPoolProfile origProfile)
specifier|public
name|ThreadPoolProfileBuilder
parameter_list|(
name|String
name|id
parameter_list|,
name|ThreadPoolProfile
name|origProfile
parameter_list|)
block|{
name|this
operator|.
name|profile
operator|=
name|origProfile
operator|.
name|clone
argument_list|()
expr_stmt|;
name|this
operator|.
name|profile
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
DECL|method|defaultProfile (Boolean defaultProfile)
specifier|public
name|ThreadPoolProfileBuilder
name|defaultProfile
parameter_list|(
name|Boolean
name|defaultProfile
parameter_list|)
block|{
name|this
operator|.
name|profile
operator|.
name|setDefaultProfile
argument_list|(
name|defaultProfile
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|poolSize (Integer poolSize)
specifier|public
name|ThreadPoolProfileBuilder
name|poolSize
parameter_list|(
name|Integer
name|poolSize
parameter_list|)
block|{
name|profile
operator|.
name|setPoolSize
argument_list|(
name|poolSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|maxPoolSize (Integer maxPoolSize)
specifier|public
name|ThreadPoolProfileBuilder
name|maxPoolSize
parameter_list|(
name|Integer
name|maxPoolSize
parameter_list|)
block|{
name|profile
operator|.
name|setMaxPoolSize
argument_list|(
name|maxPoolSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|keepAliveTime (Long keepAliveTime, TimeUnit timeUnit)
specifier|public
name|ThreadPoolProfileBuilder
name|keepAliveTime
parameter_list|(
name|Long
name|keepAliveTime
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|profile
operator|.
name|setKeepAliveTime
argument_list|(
name|keepAliveTime
argument_list|)
expr_stmt|;
name|profile
operator|.
name|setTimeUnit
argument_list|(
name|timeUnit
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|keepAliveTime (Long keepAliveTime)
specifier|public
name|ThreadPoolProfileBuilder
name|keepAliveTime
parameter_list|(
name|Long
name|keepAliveTime
parameter_list|)
block|{
name|profile
operator|.
name|setKeepAliveTime
argument_list|(
name|keepAliveTime
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|maxQueueSize (Integer maxQueueSize)
specifier|public
name|ThreadPoolProfileBuilder
name|maxQueueSize
parameter_list|(
name|Integer
name|maxQueueSize
parameter_list|)
block|{
if|if
condition|(
name|maxQueueSize
operator|!=
literal|null
condition|)
block|{
name|profile
operator|.
name|setMaxQueueSize
argument_list|(
name|maxQueueSize
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|allowCoreThreadTimeOut (Boolean allowCoreThreadTimeOut)
specifier|public
name|ThreadPoolProfileBuilder
name|allowCoreThreadTimeOut
parameter_list|(
name|Boolean
name|allowCoreThreadTimeOut
parameter_list|)
block|{
name|profile
operator|.
name|setAllowCoreThreadTimeOut
argument_list|(
name|allowCoreThreadTimeOut
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|rejectedPolicy (ThreadPoolRejectedPolicy rejectedPolicy)
specifier|public
name|ThreadPoolProfileBuilder
name|rejectedPolicy
parameter_list|(
name|ThreadPoolRejectedPolicy
name|rejectedPolicy
parameter_list|)
block|{
name|profile
operator|.
name|setRejectedPolicy
argument_list|(
name|rejectedPolicy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Builds the thread pool profile      *       * @return the thread pool profile      */
DECL|method|build ()
specifier|public
name|ThreadPoolProfile
name|build
parameter_list|()
block|{
return|return
name|profile
return|;
block|}
block|}
end_class

end_unit

