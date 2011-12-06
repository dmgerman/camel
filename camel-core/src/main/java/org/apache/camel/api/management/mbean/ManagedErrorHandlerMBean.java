begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.api.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
operator|.
name|management
operator|.
name|mbean
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
import|;
end_import

begin_interface
DECL|interface|ManagedErrorHandlerMBean
specifier|public
interface|interface
name|ManagedErrorHandlerMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel id"
argument_list|)
DECL|method|getCamelId ()
name|String
name|getCamelId
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Does the error handler support redelivery"
argument_list|)
DECL|method|isSupportRedelivery ()
name|boolean
name|isSupportRedelivery
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Is this error handler a dead letter channel"
argument_list|)
DECL|method|isDeadLetterChannel ()
name|boolean
name|isDeadLetterChannel
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"When a message is moved to dead letter channel is it the original message or recent message"
argument_list|)
DECL|method|isDeadLetterUseOriginalMessage ()
name|boolean
name|isDeadLetterUseOriginalMessage
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Does this error handler support transactions"
argument_list|)
DECL|method|isSupportTransactions ()
name|boolean
name|isSupportTransactions
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Endpoint Uri for the dead letter channel where dead message is move to"
argument_list|)
DECL|method|getDeadLetterChannelEndpointUri ()
name|String
name|getDeadLetterChannelEndpointUri
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for maximum redeliveries"
argument_list|)
DECL|method|getMaximumRedeliveries ()
name|Integer
name|getMaximumRedeliveries
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for maximum redeliveries"
argument_list|)
DECL|method|setMaximumRedeliveries (Integer maximum)
name|void
name|setMaximumRedeliveries
parameter_list|(
name|Integer
name|maximum
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for maximum redelivery delay"
argument_list|)
DECL|method|getMaximumRedeliveryDelay ()
name|Long
name|getMaximumRedeliveryDelay
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for maximum redelivery delay"
argument_list|)
DECL|method|setMaximumRedeliveryDelay (Long delay)
name|void
name|setMaximumRedeliveryDelay
parameter_list|(
name|Long
name|delay
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for redelivery delay"
argument_list|)
DECL|method|getRedeliveryDelay ()
name|Long
name|getRedeliveryDelay
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for redelivery delay"
argument_list|)
DECL|method|setRedeliveryDelay (Long delay)
name|void
name|setRedeliveryDelay
parameter_list|(
name|Long
name|delay
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for backoff multiplier"
argument_list|)
DECL|method|getBackOffMultiplier ()
name|Double
name|getBackOffMultiplier
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for backoff multiplier"
argument_list|)
DECL|method|setBackOffMultiplier (Double multiplier)
name|void
name|setBackOffMultiplier
parameter_list|(
name|Double
name|multiplier
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for collision avoidance factor"
argument_list|)
DECL|method|getCollisionAvoidanceFactor ()
name|Double
name|getCollisionAvoidanceFactor
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for collision avoidance factor"
argument_list|)
DECL|method|setCollisionAvoidanceFactor (Double factor)
name|void
name|setCollisionAvoidanceFactor
parameter_list|(
name|Double
name|factor
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for collision avoidance percent"
argument_list|)
DECL|method|getCollisionAvoidancePercent ()
name|Double
name|getCollisionAvoidancePercent
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for collision avoidance percent"
argument_list|)
DECL|method|setCollisionAvoidancePercent (Double percent)
name|void
name|setCollisionAvoidancePercent
parameter_list|(
name|Double
name|percent
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for delay pattern"
argument_list|)
DECL|method|getDelayPattern ()
name|String
name|getDelayPattern
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for delay pattern"
argument_list|)
DECL|method|setDelayPattern (String pattern)
name|void
name|setDelayPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for logging level when retries exhausted"
argument_list|)
DECL|method|getRetriesExhaustedLogLevel ()
name|String
name|getRetriesExhaustedLogLevel
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for logging level when retries exhausted"
argument_list|)
DECL|method|setRetriesExhaustedLogLevel (String level)
name|void
name|setRetriesExhaustedLogLevel
parameter_list|(
name|String
name|level
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for logging level when attempting retry"
argument_list|)
DECL|method|getRetryAttemptedLogLevel ()
name|String
name|getRetryAttemptedLogLevel
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for logging level when attempting retry"
argument_list|)
DECL|method|setRetryAttemptedLogLevel (String level)
name|void
name|setRetryAttemptedLogLevel
parameter_list|(
name|String
name|level
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for logging stack traces"
argument_list|)
DECL|method|getLogStackTrace ()
name|Boolean
name|getLogStackTrace
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for logging stack traces"
argument_list|)
DECL|method|setLogStackTrace (Boolean log)
name|void
name|setLogStackTrace
parameter_list|(
name|Boolean
name|log
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for logging redelivery stack traces"
argument_list|)
DECL|method|getLogRetryStackTrace ()
name|Boolean
name|getLogRetryStackTrace
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for logging redelivery stack traces"
argument_list|)
DECL|method|setLogRetryStackTrace (Boolean log)
name|void
name|setLogRetryStackTrace
parameter_list|(
name|Boolean
name|log
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for logging handled exceptions"
argument_list|)
DECL|method|getLogHandled ()
name|Boolean
name|getLogHandled
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for logging handled exceptions"
argument_list|)
DECL|method|setLogHandled (Boolean log)
name|void
name|setLogHandled
parameter_list|(
name|Boolean
name|log
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for logging handled and continued exceptions"
argument_list|)
DECL|method|getLogContinued ()
name|Boolean
name|getLogContinued
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for logging handled and continued exceptions"
argument_list|)
DECL|method|setLogContinued (Boolean log)
name|void
name|setLogContinued
parameter_list|(
name|Boolean
name|log
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for logging exhausted exceptions"
argument_list|)
DECL|method|getLogExhausted ()
name|Boolean
name|getLogExhausted
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for logging exhausted exceptions"
argument_list|)
DECL|method|setLogExhausted (Boolean log)
name|void
name|setLogExhausted
parameter_list|(
name|Boolean
name|log
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for using collision avoidance"
argument_list|)
DECL|method|getUseCollisionAvoidance ()
name|Boolean
name|getUseCollisionAvoidance
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for using collision avoidance"
argument_list|)
DECL|method|setUseCollisionAvoidance (Boolean avoidance)
name|void
name|setUseCollisionAvoidance
parameter_list|(
name|Boolean
name|avoidance
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for using exponential backoff"
argument_list|)
DECL|method|getUseExponentialBackOff ()
name|Boolean
name|getUseExponentialBackOff
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"RedeliveryPolicy for using exponential backoff"
argument_list|)
DECL|method|setUseExponentialBackOff (Boolean backoff)
name|void
name|setUseExponentialBackOff
parameter_list|(
name|Boolean
name|backoff
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

