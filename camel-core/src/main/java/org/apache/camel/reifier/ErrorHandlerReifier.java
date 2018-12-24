begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
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
name|RuntimeCamelException
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
name|model
operator|.
name|OnExceptionDefinition
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
name|model
operator|.
name|RedeliveryPolicyDefinition
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
name|RedeliveryPolicy
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
name|CamelContextHelper
import|;
end_import

begin_class
DECL|class|ErrorHandlerReifier
specifier|public
specifier|final
class|class
name|ErrorHandlerReifier
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|ErrorHandlerReifier ()
specifier|private
name|ErrorHandlerReifier
parameter_list|()
block|{     }
DECL|method|createRedeliveryPolicy (RedeliveryPolicyDefinition definition, CamelContext context, RedeliveryPolicy parentPolicy)
specifier|public
specifier|static
name|RedeliveryPolicy
name|createRedeliveryPolicy
parameter_list|(
name|RedeliveryPolicyDefinition
name|definition
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|RedeliveryPolicy
name|parentPolicy
parameter_list|)
block|{
name|RedeliveryPolicy
name|answer
decl_stmt|;
if|if
condition|(
name|parentPolicy
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|parentPolicy
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|RedeliveryPolicy
argument_list|()
expr_stmt|;
block|}
try|try
block|{
comment|// copy across the properties - if they are set
if|if
condition|(
name|definition
operator|.
name|getMaximumRedeliveries
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setMaximumRedeliveries
argument_list|(
name|CamelContextHelper
operator|.
name|parseInteger
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getMaximumRedeliveries
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getRedeliveryDelay
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setRedeliveryDelay
argument_list|(
name|CamelContextHelper
operator|.
name|parseLong
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getRedeliveryDelay
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getAsyncDelayedRedelivery
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setAsyncDelayedRedelivery
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getAsyncDelayedRedelivery
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getRetriesExhaustedLogLevel
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setRetriesExhaustedLogLevel
argument_list|(
name|definition
operator|.
name|getRetriesExhaustedLogLevel
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getRetryAttemptedLogLevel
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setRetryAttemptedLogLevel
argument_list|(
name|definition
operator|.
name|getRetryAttemptedLogLevel
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getRetryAttemptedLogInterval
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setRetryAttemptedLogInterval
argument_list|(
name|CamelContextHelper
operator|.
name|parseInteger
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getRetryAttemptedLogInterval
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getBackOffMultiplier
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setBackOffMultiplier
argument_list|(
name|CamelContextHelper
operator|.
name|parseDouble
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getBackOffMultiplier
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getUseExponentialBackOff
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setUseExponentialBackOff
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getUseExponentialBackOff
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getCollisionAvoidanceFactor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setCollisionAvoidanceFactor
argument_list|(
name|CamelContextHelper
operator|.
name|parseDouble
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getCollisionAvoidanceFactor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getUseCollisionAvoidance
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setUseCollisionAvoidance
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getUseCollisionAvoidance
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getMaximumRedeliveryDelay
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setMaximumRedeliveryDelay
argument_list|(
name|CamelContextHelper
operator|.
name|parseLong
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getMaximumRedeliveryDelay
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogStackTrace
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogStackTrace
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogStackTrace
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogRetryStackTrace
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogRetryStackTrace
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogRetryStackTrace
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogHandled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogHandled
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogHandled
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogNewException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogNewException
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogNewException
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogContinued
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogContinued
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogContinued
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogRetryAttempted
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogRetryAttempted
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogRetryAttempted
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogExhausted
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogExhausted
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogExhausted
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogExhaustedMessageHistory
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogExhaustedMessageHistory
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogExhaustedMessageHistory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogExhaustedMessageBody
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogExhaustedMessageBody
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogExhaustedMessageBody
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getDisableRedelivery
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getDisableRedelivery
argument_list|()
argument_list|)
condition|)
block|{
name|answer
operator|.
name|setMaximumRedeliveries
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|definition
operator|.
name|getDelayPattern
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setDelayPattern
argument_list|(
name|CamelContextHelper
operator|.
name|parseText
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getDelayPattern
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getAllowRedeliveryWhileStopping
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setAllowRedeliveryWhileStopping
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getAllowRedeliveryWhileStopping
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getExchangeFormatterRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setExchangeFormatterRef
argument_list|(
name|CamelContextHelper
operator|.
name|parseText
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getExchangeFormatterRef
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Allows an exception handler to create a new redelivery policy for this exception type      *      * @param definition      * @param context      the camel context      * @param parentPolicy the current redelivery policy, is newer<tt>null</tt>      * @return a newly created redelivery policy, or return the original policy if no customization is required      *         for this exception handler.      */
DECL|method|createRedeliveryPolicy (OnExceptionDefinition definition, CamelContext context, RedeliveryPolicy parentPolicy)
specifier|public
specifier|static
name|RedeliveryPolicy
name|createRedeliveryPolicy
parameter_list|(
name|OnExceptionDefinition
name|definition
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|RedeliveryPolicy
name|parentPolicy
parameter_list|)
block|{
if|if
condition|(
name|definition
operator|.
name|getRedeliveryPolicyRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getRedeliveryPolicyRef
argument_list|()
argument_list|,
name|RedeliveryPolicy
operator|.
name|class
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|definition
operator|.
name|getRedeliveryPolicyType
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|createRedeliveryPolicy
argument_list|(
name|definition
operator|.
name|getRedeliveryPolicyType
argument_list|()
argument_list|,
name|context
argument_list|,
name|parentPolicy
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|definition
operator|.
name|getOutputs
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|parentPolicy
operator|.
name|getMaximumRedeliveries
argument_list|()
operator|!=
literal|0
condition|)
block|{
comment|// if we have outputs, then do not inherit parent maximumRedeliveries
comment|// as you would have to explicit configure maximumRedeliveries on this onException to use it
comment|// this is the behavior Camel has always had
name|RedeliveryPolicy
name|answer
init|=
name|parentPolicy
operator|.
name|copy
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setMaximumRedeliveries
argument_list|(
literal|0
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
else|else
block|{
return|return
name|parentPolicy
return|;
block|}
block|}
block|}
end_class

end_unit

