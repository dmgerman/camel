begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.health
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|health
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Optional
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
name|Builder
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * A builder helper to create a result.  */
end_comment

begin_class
DECL|class|HealthCheckResultBuilder
specifier|public
specifier|final
class|class
name|HealthCheckResultBuilder
implements|implements
name|Builder
argument_list|<
name|HealthCheck
operator|.
name|Result
argument_list|>
block|{
DECL|field|check
specifier|private
name|HealthCheck
name|check
decl_stmt|;
DECL|field|message
specifier|private
name|String
name|message
decl_stmt|;
DECL|field|error
specifier|private
name|Throwable
name|error
decl_stmt|;
DECL|field|details
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|details
decl_stmt|;
DECL|field|state
specifier|private
name|HealthCheck
operator|.
name|State
name|state
decl_stmt|;
DECL|method|HealthCheckResultBuilder (HealthCheck check)
specifier|private
name|HealthCheckResultBuilder
parameter_list|(
name|HealthCheck
name|check
parameter_list|)
block|{
name|this
operator|.
name|check
operator|=
name|check
expr_stmt|;
block|}
DECL|method|message ()
specifier|public
name|String
name|message
parameter_list|()
block|{
return|return
name|this
operator|.
name|message
return|;
block|}
DECL|method|message (String message)
specifier|public
name|HealthCheckResultBuilder
name|message
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|error ()
specifier|public
name|Throwable
name|error
parameter_list|()
block|{
return|return
name|this
operator|.
name|error
return|;
block|}
DECL|method|error (Throwable error)
specifier|public
name|HealthCheckResultBuilder
name|error
parameter_list|(
name|Throwable
name|error
parameter_list|)
block|{
name|this
operator|.
name|error
operator|=
name|error
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|detail (String key)
specifier|public
name|Object
name|detail
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|this
operator|.
name|details
operator|!=
literal|null
condition|?
name|this
operator|.
name|details
operator|.
name|get
argument_list|(
name|key
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|detail (String key, Object value)
specifier|public
name|HealthCheckResultBuilder
name|detail
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|details
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|details
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|details
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|details (Map<String, Object> details)
specifier|public
name|HealthCheckResultBuilder
name|details
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|details
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|details
argument_list|)
condition|)
block|{
name|details
operator|.
name|forEach
argument_list|(
name|this
operator|::
name|detail
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|state ()
specifier|public
name|HealthCheck
operator|.
name|State
name|state
parameter_list|()
block|{
return|return
name|this
operator|.
name|state
return|;
block|}
DECL|method|state (HealthCheck.State state)
specifier|public
name|HealthCheckResultBuilder
name|state
parameter_list|(
name|HealthCheck
operator|.
name|State
name|state
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|up ()
specifier|public
name|HealthCheckResultBuilder
name|up
parameter_list|()
block|{
return|return
name|state
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|UP
argument_list|)
return|;
block|}
DECL|method|down ()
specifier|public
name|HealthCheckResultBuilder
name|down
parameter_list|()
block|{
return|return
name|state
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|DOWN
argument_list|)
return|;
block|}
DECL|method|unknown ()
specifier|public
name|HealthCheckResultBuilder
name|unknown
parameter_list|()
block|{
return|return
name|state
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|UNKNOWN
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|build ()
specifier|public
name|HealthCheck
operator|.
name|Result
name|build
parameter_list|()
block|{
comment|// Validation
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|this
operator|.
name|state
argument_list|,
literal|"Response State"
argument_list|)
expr_stmt|;
specifier|final
name|HealthCheck
operator|.
name|State
name|responseState
init|=
name|this
operator|.
name|state
decl_stmt|;
specifier|final
name|Optional
argument_list|<
name|String
argument_list|>
name|responseMessage
init|=
name|Optional
operator|.
name|ofNullable
argument_list|(
name|this
operator|.
name|message
argument_list|)
decl_stmt|;
specifier|final
name|Optional
argument_list|<
name|Throwable
argument_list|>
name|responseError
init|=
name|Optional
operator|.
name|ofNullable
argument_list|(
name|this
operator|.
name|error
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseDetails
init|=
name|HealthCheckResultBuilder
operator|.
name|this
operator|.
name|details
operator|!=
literal|null
condition|?
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|HealthCheckResultBuilder
operator|.
name|this
operator|.
name|details
argument_list|)
argument_list|)
else|:
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
return|return
operator|new
name|HealthCheck
operator|.
name|Result
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|HealthCheck
name|getCheck
parameter_list|()
block|{
return|return
name|check
return|;
block|}
annotation|@
name|Override
specifier|public
name|HealthCheck
operator|.
name|State
name|getState
parameter_list|()
block|{
return|return
name|responseState
return|;
block|}
annotation|@
name|Override
specifier|public
name|Optional
argument_list|<
name|String
argument_list|>
name|getMessage
parameter_list|()
block|{
return|return
name|responseMessage
return|;
block|}
annotation|@
name|Override
specifier|public
name|Optional
argument_list|<
name|Throwable
argument_list|>
name|getError
parameter_list|()
block|{
return|return
name|responseError
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getDetails
parameter_list|()
block|{
return|return
name|responseDetails
return|;
block|}
block|}
return|;
block|}
DECL|method|on (HealthCheck check)
specifier|public
specifier|static
name|HealthCheckResultBuilder
name|on
parameter_list|(
name|HealthCheck
name|check
parameter_list|)
block|{
return|return
operator|new
name|HealthCheckResultBuilder
argument_list|(
name|check
argument_list|)
return|;
block|}
block|}
end_class

end_unit

