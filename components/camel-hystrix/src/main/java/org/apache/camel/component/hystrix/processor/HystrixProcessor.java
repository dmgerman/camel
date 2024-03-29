begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hystrix.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hystrix
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|HystrixCircuitBreaker
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|HystrixCommand
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|HystrixCommandGroupKey
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|HystrixCommandKey
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|HystrixCommandMetrics
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
name|AsyncCallback
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
name|Exchange
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
name|Navigate
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
name|Processor
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|CircuitBreakerConstants
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
name|IdAware
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
name|AsyncProcessorSupport
import|;
end_import

begin_comment
comment|/**  * Implementation of the Hystrix EIP.  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Hystrix Processor"
argument_list|)
DECL|class|HystrixProcessor
specifier|public
class|class
name|HystrixProcessor
extends|extends
name|AsyncProcessorSupport
implements|implements
name|Navigate
argument_list|<
name|Processor
argument_list|>
implements|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Traceable
implements|,
name|IdAware
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|groupKey
specifier|private
specifier|final
name|HystrixCommandGroupKey
name|groupKey
decl_stmt|;
DECL|field|commandKey
specifier|private
specifier|final
name|HystrixCommandKey
name|commandKey
decl_stmt|;
DECL|field|fallbackCommandKey
specifier|private
specifier|final
name|HystrixCommandKey
name|fallbackCommandKey
decl_stmt|;
DECL|field|setter
specifier|private
specifier|final
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|HystrixCommand
operator|.
name|Setter
name|setter
decl_stmt|;
DECL|field|fallbackSetter
specifier|private
specifier|final
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|HystrixCommand
operator|.
name|Setter
name|fallbackSetter
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|fallback
specifier|private
specifier|final
name|Processor
name|fallback
decl_stmt|;
DECL|field|fallbackViaNetwork
specifier|private
specifier|final
name|boolean
name|fallbackViaNetwork
decl_stmt|;
DECL|method|HystrixProcessor (HystrixCommandGroupKey groupKey, HystrixCommandKey commandKey, HystrixCommandKey fallbackCommandKey, HystrixCommand.Setter setter, HystrixCommand.Setter fallbackSetter, Processor processor, Processor fallback, boolean fallbackViaNetwork)
specifier|public
name|HystrixProcessor
parameter_list|(
name|HystrixCommandGroupKey
name|groupKey
parameter_list|,
name|HystrixCommandKey
name|commandKey
parameter_list|,
name|HystrixCommandKey
name|fallbackCommandKey
parameter_list|,
name|HystrixCommand
operator|.
name|Setter
name|setter
parameter_list|,
name|HystrixCommand
operator|.
name|Setter
name|fallbackSetter
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Processor
name|fallback
parameter_list|,
name|boolean
name|fallbackViaNetwork
parameter_list|)
block|{
name|this
operator|.
name|groupKey
operator|=
name|groupKey
expr_stmt|;
name|this
operator|.
name|commandKey
operator|=
name|commandKey
expr_stmt|;
name|this
operator|.
name|fallbackCommandKey
operator|=
name|fallbackCommandKey
expr_stmt|;
name|this
operator|.
name|setter
operator|=
name|setter
expr_stmt|;
name|this
operator|.
name|fallbackSetter
operator|=
name|fallbackSetter
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|this
operator|.
name|fallback
operator|=
name|fallback
expr_stmt|;
name|this
operator|.
name|fallbackViaNetwork
operator|=
name|fallbackViaNetwork
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getHystrixCommandKey ()
specifier|public
name|String
name|getHystrixCommandKey
parameter_list|()
block|{
return|return
name|commandKey
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getHystrixFallbackCommandKey ()
specifier|public
name|String
name|getHystrixFallbackCommandKey
parameter_list|()
block|{
if|if
condition|(
name|fallbackCommandKey
operator|!=
literal|null
condition|)
block|{
return|return
name|fallbackCommandKey
operator|.
name|name
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|ManagedAttribute
DECL|method|getHystrixGroupKey ()
specifier|public
name|String
name|getHystrixGroupKey
parameter_list|()
block|{
return|return
name|groupKey
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|isFallbackViaNetwork ()
specifier|public
name|boolean
name|isFallbackViaNetwork
parameter_list|()
block|{
return|return
name|isFallbackViaNetwork
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getHystrixTotalTimeMean ()
specifier|public
name|int
name|getHystrixTotalTimeMean
parameter_list|()
block|{
name|HystrixCommandMetrics
name|metrics
init|=
name|HystrixCommandMetrics
operator|.
name|getInstance
argument_list|(
name|commandKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|metrics
operator|!=
literal|null
condition|)
block|{
return|return
name|metrics
operator|.
name|getTotalTimeMean
argument_list|()
return|;
block|}
return|return
literal|0
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getHystrixExecutionTimeMean ()
specifier|public
name|int
name|getHystrixExecutionTimeMean
parameter_list|()
block|{
name|HystrixCommandMetrics
name|metrics
init|=
name|HystrixCommandMetrics
operator|.
name|getInstance
argument_list|(
name|commandKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|metrics
operator|!=
literal|null
condition|)
block|{
return|return
name|metrics
operator|.
name|getExecutionTimeMean
argument_list|()
return|;
block|}
return|return
literal|0
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getHystrixCurrentConcurrentExecutionCount ()
specifier|public
name|int
name|getHystrixCurrentConcurrentExecutionCount
parameter_list|()
block|{
name|HystrixCommandMetrics
name|metrics
init|=
name|HystrixCommandMetrics
operator|.
name|getInstance
argument_list|(
name|commandKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|metrics
operator|!=
literal|null
condition|)
block|{
return|return
name|metrics
operator|.
name|getCurrentConcurrentExecutionCount
argument_list|()
return|;
block|}
return|return
literal|0
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getHystrixTotalRequests ()
specifier|public
name|long
name|getHystrixTotalRequests
parameter_list|()
block|{
name|HystrixCommandMetrics
name|metrics
init|=
name|HystrixCommandMetrics
operator|.
name|getInstance
argument_list|(
name|commandKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|metrics
operator|!=
literal|null
condition|)
block|{
return|return
name|metrics
operator|.
name|getHealthCounts
argument_list|()
operator|.
name|getTotalRequests
argument_list|()
return|;
block|}
return|return
literal|0
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getHystrixErrorCount ()
specifier|public
name|long
name|getHystrixErrorCount
parameter_list|()
block|{
name|HystrixCommandMetrics
name|metrics
init|=
name|HystrixCommandMetrics
operator|.
name|getInstance
argument_list|(
name|commandKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|metrics
operator|!=
literal|null
condition|)
block|{
return|return
name|metrics
operator|.
name|getHealthCounts
argument_list|()
operator|.
name|getErrorCount
argument_list|()
return|;
block|}
return|return
literal|0
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getHystrixErrorPercentage ()
specifier|public
name|int
name|getHystrixErrorPercentage
parameter_list|()
block|{
name|HystrixCommandMetrics
name|metrics
init|=
name|HystrixCommandMetrics
operator|.
name|getInstance
argument_list|(
name|commandKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|metrics
operator|!=
literal|null
condition|)
block|{
return|return
name|metrics
operator|.
name|getHealthCounts
argument_list|()
operator|.
name|getErrorPercentage
argument_list|()
return|;
block|}
return|return
literal|0
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|isCircuitBreakerOpen ()
specifier|public
name|boolean
name|isCircuitBreakerOpen
parameter_list|()
block|{
name|HystrixCircuitBreaker
name|cb
init|=
name|HystrixCircuitBreaker
operator|.
name|Factory
operator|.
name|getInstance
argument_list|(
name|commandKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|cb
operator|!=
literal|null
condition|)
block|{
return|return
name|cb
operator|.
name|isOpen
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|Override
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"hystrix"
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|Processor
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|processor
argument_list|)
expr_stmt|;
if|if
condition|(
name|fallback
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|fallback
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
comment|// run this as if we run inside try .. catch so there is no regular Camel error handler
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|TRY_ROUTE_BLOCK
argument_list|,
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|HystrixProcessorCommandFallbackViaNetwork
name|fallbackCommand
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|fallbackViaNetwork
condition|)
block|{
name|fallbackCommand
operator|=
operator|new
name|HystrixProcessorCommandFallbackViaNetwork
argument_list|(
name|fallbackSetter
argument_list|,
name|exchange
argument_list|,
name|fallback
argument_list|)
expr_stmt|;
block|}
name|HystrixProcessorCommand
name|command
init|=
operator|new
name|HystrixProcessorCommand
argument_list|(
name|setter
argument_list|,
name|exchange
argument_list|,
name|processor
argument_list|,
name|fallback
argument_list|,
name|fallbackCommand
argument_list|)
decl_stmt|;
name|command
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// enrich exchange with details from hystrix about the command execution
name|commandResponse
argument_list|(
name|exchange
argument_list|,
name|command
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|TRY_ROUTE_BLOCK
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|commandResponse (Exchange exchange, HystrixCommand command)
specifier|private
name|void
name|commandResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|HystrixCommand
name|command
parameter_list|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|CircuitBreakerConstants
operator|.
name|RESPONSE_SUCCESSFUL_EXECUTION
argument_list|,
name|command
operator|.
name|isSuccessfulExecution
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|CircuitBreakerConstants
operator|.
name|RESPONSE_FROM_FALLBACK
argument_list|,
name|command
operator|.
name|isResponseFromFallback
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|CircuitBreakerConstants
operator|.
name|RESPONSE_SHORT_CIRCUITED
argument_list|,
name|command
operator|.
name|isResponseShortCircuited
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|CircuitBreakerConstants
operator|.
name|RESPONSE_TIMED_OUT
argument_list|,
name|command
operator|.
name|isResponseTimedOut
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|CircuitBreakerConstants
operator|.
name|RESPONSE_REJECTED
argument_list|,
name|command
operator|.
name|isResponseRejected
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

