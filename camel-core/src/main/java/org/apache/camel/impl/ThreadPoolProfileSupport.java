begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ThreadPoolProfileSupport
specifier|public
class|class
name|ThreadPoolProfileSupport
implements|implements
name|ThreadPoolProfile
block|{
DECL|field|defaultProfile
specifier|private
name|Boolean
name|defaultProfile
decl_stmt|;
DECL|field|poolSize
specifier|private
name|Integer
name|poolSize
init|=
literal|10
decl_stmt|;
DECL|field|maxPoolSize
specifier|private
name|Integer
name|maxPoolSize
init|=
literal|20
decl_stmt|;
DECL|field|keepAliveTime
specifier|private
name|Long
name|keepAliveTime
init|=
literal|60L
decl_stmt|;
DECL|field|timeUnit
specifier|private
name|TimeUnit
name|timeUnit
init|=
name|TimeUnit
operator|.
name|SECONDS
decl_stmt|;
DECL|field|maxQueueSize
specifier|private
name|Integer
name|maxQueueSize
init|=
operator|-
literal|1
decl_stmt|;
DECL|method|isDefaultProfile ()
specifier|public
name|Boolean
name|isDefaultProfile
parameter_list|()
block|{
return|return
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
block|}
end_class

end_unit

