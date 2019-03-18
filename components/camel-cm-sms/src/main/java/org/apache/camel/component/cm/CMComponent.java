begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cm
package|;
end_package

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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ConstraintViolation
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|Validation
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|Validator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ValidatorFactory
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
name|CamelContext
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
name|Endpoint
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
name|ResolveEndpointFailedException
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
name|annotations
operator|.
name|Component
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
name|support
operator|.
name|DefaultComponent
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link CMEndpoint}s.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"cm-sms"
argument_list|)
DECL|class|CMComponent
specifier|public
class|class
name|CMComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|validator
specifier|private
name|Validator
name|validator
decl_stmt|;
DECL|method|CMComponent ()
specifier|public
name|CMComponent
parameter_list|()
block|{     }
DECL|method|CMComponent (final CamelContext context)
specifier|public
name|CMComponent
parameter_list|(
specifier|final
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (final String uri, final String remaining, final Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|remaining
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Set configuration based on uri parameters
specifier|final
name|CMConfiguration
name|config
init|=
operator|new
name|CMConfiguration
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|config
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// Validate configuration
name|log
operator|.
name|debug
argument_list|(
literal|"Validating uri based configuration"
argument_list|)
expr_stmt|;
specifier|final
name|Set
argument_list|<
name|ConstraintViolation
argument_list|<
name|CMConfiguration
argument_list|>
argument_list|>
name|constraintViolations
init|=
name|getValidator
argument_list|()
operator|.
name|validate
argument_list|(
name|config
argument_list|)
decl_stmt|;
if|if
condition|(
name|constraintViolations
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
specifier|final
name|StringBuffer
name|msg
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|ConstraintViolation
argument_list|<
name|CMConfiguration
argument_list|>
name|cv
range|:
name|constraintViolations
control|)
block|{
name|msg
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"- Invalid value for %s: %s"
argument_list|,
name|cv
operator|.
name|getPropertyPath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|cv
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|ResolveEndpointFailedException
argument_list|(
name|uri
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
comment|// Component is an Endpoint factory. So far, just one Endpoint type.
comment|// Endpoint construction and configuration.
specifier|final
name|CMEndpoint
name|endpoint
init|=
operator|new
name|CMEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setConfiguration
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setHost
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getValidator ()
specifier|public
name|Validator
name|getValidator
parameter_list|()
block|{
if|if
condition|(
name|validator
operator|==
literal|null
condition|)
block|{
name|ValidatorFactory
name|factory
init|=
name|Validation
operator|.
name|buildDefaultValidatorFactory
argument_list|()
decl_stmt|;
name|validator
operator|=
name|factory
operator|.
name|getValidator
argument_list|()
expr_stmt|;
block|}
return|return
name|validator
return|;
block|}
block|}
end_class

end_unit

