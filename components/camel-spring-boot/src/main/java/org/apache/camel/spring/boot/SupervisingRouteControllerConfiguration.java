begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.supervising.controller"
argument_list|)
DECL|class|SupervisingRouteControllerConfiguration
specifier|public
class|class
name|SupervisingRouteControllerConfiguration
block|{
comment|/**      * Global option to enable/disable this ${@link org.apache.camel.spi.RouteController}, default is false.      */
DECL|field|enabled
specifier|private
name|boolean
name|enabled
decl_stmt|;
comment|/**      * The default back-off configuration, back-off configuration for routes inherits from this default.      */
DECL|field|backOff
specifier|private
name|BackOffConfiguration
name|backOff
init|=
operator|new
name|BackOffConfiguration
argument_list|()
decl_stmt|;
comment|/**      * Routes configuration.      */
DECL|field|routes
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|RouteConfiguration
argument_list|>
name|routes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
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
DECL|method|getBackOff ()
specifier|public
name|BackOffConfiguration
name|getBackOff
parameter_list|()
block|{
return|return
name|backOff
return|;
block|}
DECL|method|getRoutes ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|RouteConfiguration
argument_list|>
name|getRoutes
parameter_list|()
block|{
return|return
name|routes
return|;
block|}
comment|// *****************************************
comment|// Configuration Classes
comment|// *****************************************
DECL|class|RouteConfiguration
specifier|public
specifier|static
class|class
name|RouteConfiguration
block|{
comment|/**          * The back-off configuration from this route, inherits from default back-off          */
DECL|field|backOff
specifier|private
name|BackOffConfiguration
name|backOff
decl_stmt|;
DECL|method|getBackOff ()
specifier|public
name|BackOffConfiguration
name|getBackOff
parameter_list|()
block|{
return|return
name|backOff
return|;
block|}
DECL|method|setBackOff (BackOffConfiguration backOff)
specifier|public
name|void
name|setBackOff
parameter_list|(
name|BackOffConfiguration
name|backOff
parameter_list|)
block|{
name|this
operator|.
name|backOff
operator|=
name|backOff
expr_stmt|;
block|}
block|}
DECL|class|BackOffConfiguration
specifier|public
specifier|static
class|class
name|BackOffConfiguration
block|{
comment|/**          * The delay to wait before retry the operation.          *          * You can also specify time values using units, such as 60s (60 seconds),          * 5m30s (5 minutes and 30 seconds), and 1h (1 hour).          */
DECL|field|delay
specifier|private
name|String
name|delay
decl_stmt|;
comment|/**          * The maximum back-off time.          *          * You can also specify time values using units, such as 60s (60 seconds),          * 5m30s (5 minutes and 30 seconds), and 1h (1 hour).          */
DECL|field|maxDelay
specifier|private
name|String
name|maxDelay
decl_stmt|;
comment|/**          * The maximum elapsed time after which the back-off is exhausted.          *          * You can also specify time values using units, such as 60s (60 seconds),          * 5m30s (5 minutes and 30 seconds), and 1h (1 hour).          */
DECL|field|maxElapsedTime
specifier|private
name|String
name|maxElapsedTime
decl_stmt|;
comment|/**          * The maximum number of attempts after which the back-off is exhausted.          */
DECL|field|maxAttempts
specifier|private
name|Long
name|maxAttempts
decl_stmt|;
comment|/**          * The value to multiply the current interval by for each retry attempt.          */
DECL|field|multiplier
specifier|private
name|Double
name|multiplier
decl_stmt|;
DECL|method|getDelay ()
specifier|public
name|String
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
DECL|method|setDelay (String delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|String
name|delay
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
block|}
DECL|method|getMaxDelay ()
specifier|public
name|String
name|getMaxDelay
parameter_list|()
block|{
return|return
name|maxDelay
return|;
block|}
DECL|method|setMaxDelay (String maxDelay)
specifier|public
name|void
name|setMaxDelay
parameter_list|(
name|String
name|maxDelay
parameter_list|)
block|{
name|this
operator|.
name|maxDelay
operator|=
name|maxDelay
expr_stmt|;
block|}
DECL|method|getMaxElapsedTime ()
specifier|public
name|String
name|getMaxElapsedTime
parameter_list|()
block|{
return|return
name|maxElapsedTime
return|;
block|}
DECL|method|setMaxElapsedTime (String maxElapsedTime)
specifier|public
name|void
name|setMaxElapsedTime
parameter_list|(
name|String
name|maxElapsedTime
parameter_list|)
block|{
name|this
operator|.
name|maxElapsedTime
operator|=
name|maxElapsedTime
expr_stmt|;
block|}
DECL|method|getMaxAttempts ()
specifier|public
name|Long
name|getMaxAttempts
parameter_list|()
block|{
return|return
name|maxAttempts
return|;
block|}
DECL|method|setMaxAttempts (Long maxAttempts)
specifier|public
name|void
name|setMaxAttempts
parameter_list|(
name|Long
name|maxAttempts
parameter_list|)
block|{
name|this
operator|.
name|maxAttempts
operator|=
name|maxAttempts
expr_stmt|;
block|}
DECL|method|getMultiplier ()
specifier|public
name|Double
name|getMultiplier
parameter_list|()
block|{
return|return
name|multiplier
return|;
block|}
DECL|method|setMultiplier (Double multiplier)
specifier|public
name|void
name|setMultiplier
parameter_list|(
name|Double
name|multiplier
parameter_list|)
block|{
name|this
operator|.
name|multiplier
operator|=
name|multiplier
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

