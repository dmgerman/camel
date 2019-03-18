begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.health
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|health
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
name|health
operator|.
name|HealthCheckConfiguration
import|;
end_import

begin_class
DECL|class|AbstractHealthCheckConfiguration
specifier|public
specifier|abstract
class|class
name|AbstractHealthCheckConfiguration
block|{
comment|/**      * Set if the check associated to this configuration is enabled or not.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * Set the check interval.      */
DECL|field|interval
specifier|private
name|String
name|interval
decl_stmt|;
comment|/**      * Set the number of failure before reporting the service as un-healthy.      */
DECL|field|failureThreshold
specifier|private
name|Integer
name|failureThreshold
decl_stmt|;
comment|/**      * Set if the check associated to this configuration is enabled or not.      */
DECL|method|isEnabled ()
specifier|public
name|Boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
comment|/**      * Set if the check associated to this configuration is enabled or not.      */
DECL|method|setEnabled (Boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|Boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
DECL|method|getInterval ()
specifier|public
name|String
name|getInterval
parameter_list|()
block|{
return|return
name|interval
return|;
block|}
comment|/**      * Set the check interval.      */
DECL|method|setInterval (String interval)
specifier|public
name|void
name|setInterval
parameter_list|(
name|String
name|interval
parameter_list|)
block|{
name|this
operator|.
name|interval
operator|=
name|interval
expr_stmt|;
block|}
DECL|method|getFailureThreshold ()
specifier|public
name|Integer
name|getFailureThreshold
parameter_list|()
block|{
return|return
name|failureThreshold
return|;
block|}
comment|/**      * Set the number of failure before reporting the service as un-healthy.      */
DECL|method|setFailureThreshold (Integer failureThreshold)
specifier|public
name|void
name|setFailureThreshold
parameter_list|(
name|Integer
name|failureThreshold
parameter_list|)
block|{
name|this
operator|.
name|failureThreshold
operator|=
name|failureThreshold
expr_stmt|;
block|}
comment|/**      * Convert this configuration to a {@link HealthCheckConfiguration} using default values.      */
DECL|method|asHealthCheckConfiguration ()
specifier|public
name|HealthCheckConfiguration
name|asHealthCheckConfiguration
parameter_list|()
block|{
return|return
name|HealthCheckConfiguration
operator|.
name|builder
argument_list|()
operator|.
name|enabled
argument_list|(
name|this
operator|.
name|isEnabled
argument_list|()
argument_list|)
operator|.
name|interval
argument_list|(
name|this
operator|.
name|getInterval
argument_list|()
argument_list|)
operator|.
name|failureThreshold
argument_list|(
name|this
operator|.
name|getFailureThreshold
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

