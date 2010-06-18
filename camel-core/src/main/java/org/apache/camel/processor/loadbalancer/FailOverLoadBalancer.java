begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.loadbalancer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|loadbalancer
package|;
end_package

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
name|AsyncProcessor
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
name|impl
operator|.
name|converter
operator|.
name|AsyncProcessorTypeConverter
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
comment|/**  * This FailOverLoadBalancer will failover to use next processor when an exception occurred  */
end_comment

begin_class
DECL|class|FailOverLoadBalancer
specifier|public
class|class
name|FailOverLoadBalancer
extends|extends
name|LoadBalancerSupport
block|{
DECL|field|exceptions
specifier|private
specifier|final
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|exceptions
decl_stmt|;
DECL|field|roundRobin
specifier|private
name|boolean
name|roundRobin
decl_stmt|;
DECL|field|maximumFailoverAttempts
specifier|private
name|int
name|maximumFailoverAttempts
init|=
operator|-
literal|1
decl_stmt|;
comment|// stateful counter
DECL|field|counter
specifier|private
name|int
name|counter
init|=
operator|-
literal|1
decl_stmt|;
DECL|method|FailOverLoadBalancer ()
specifier|public
name|FailOverLoadBalancer
parameter_list|()
block|{
name|this
operator|.
name|exceptions
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|FailOverLoadBalancer (List<Class<?>> exceptions)
specifier|public
name|FailOverLoadBalancer
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|exceptions
parameter_list|)
block|{
name|this
operator|.
name|exceptions
operator|=
name|exceptions
expr_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
range|:
name|exceptions
control|)
block|{
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|isAssignableFrom
argument_list|(
name|Throwable
operator|.
name|class
argument_list|,
name|type
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Class is not an instance of Throwable: "
operator|+
name|type
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|getExceptions ()
specifier|public
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getExceptions
parameter_list|()
block|{
return|return
name|exceptions
return|;
block|}
DECL|method|isRoundRobin ()
specifier|public
name|boolean
name|isRoundRobin
parameter_list|()
block|{
return|return
name|roundRobin
return|;
block|}
DECL|method|setRoundRobin (boolean roundRobin)
specifier|public
name|void
name|setRoundRobin
parameter_list|(
name|boolean
name|roundRobin
parameter_list|)
block|{
name|this
operator|.
name|roundRobin
operator|=
name|roundRobin
expr_stmt|;
block|}
DECL|method|getMaximumFailoverAttempts ()
specifier|public
name|int
name|getMaximumFailoverAttempts
parameter_list|()
block|{
return|return
name|maximumFailoverAttempts
return|;
block|}
DECL|method|setMaximumFailoverAttempts (int maximumFailoverAttempts)
specifier|public
name|void
name|setMaximumFailoverAttempts
parameter_list|(
name|int
name|maximumFailoverAttempts
parameter_list|)
block|{
name|this
operator|.
name|maximumFailoverAttempts
operator|=
name|maximumFailoverAttempts
expr_stmt|;
block|}
comment|/**      * Should the given failed Exchange failover?      *      * @param exchange the exchange that failed      * @return<tt>true</tt> to failover      */
DECL|method|shouldFailOver (Exchange exchange)
specifier|protected
name|boolean
name|shouldFailOver
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|exceptions
operator|==
literal|null
operator|||
name|exceptions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// always failover if no exceptions defined
return|return
literal|true
return|;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|exception
range|:
name|exceptions
control|)
block|{
comment|// will look in exception hierarchy
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|(
name|exception
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
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
name|boolean
name|sync
decl_stmt|;
name|List
argument_list|<
name|Processor
argument_list|>
name|processors
init|=
name|getProcessors
argument_list|()
decl_stmt|;
if|if
condition|(
name|processors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No processors available to process "
operator|+
name|exchange
argument_list|)
throw|;
block|}
name|int
name|index
init|=
literal|0
decl_stmt|;
name|int
name|attempts
init|=
literal|0
decl_stmt|;
comment|// pick the first endpoint to use
if|if
condition|(
name|isRoundRobin
argument_list|()
condition|)
block|{
if|if
condition|(
operator|++
name|counter
operator|>=
name|processors
operator|.
name|size
argument_list|()
condition|)
block|{
name|counter
operator|=
literal|0
expr_stmt|;
block|}
name|index
operator|=
name|counter
expr_stmt|;
block|}
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Failover starting with endpoint index "
operator|+
name|index
argument_list|)
expr_stmt|;
block|}
name|Processor
name|processor
init|=
name|processors
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
comment|// process the first time, which indicate if we should continue synchronously or not
name|sync
operator|=
name|processExchange
argument_list|(
name|processor
argument_list|,
name|exchange
argument_list|,
name|attempts
argument_list|,
name|index
argument_list|,
name|callback
argument_list|,
name|processors
argument_list|)
expr_stmt|;
comment|// continue as long its being processed synchronously
if|if
condition|(
operator|!
name|sync
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Processing exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" is continued being processed asynchronously"
argument_list|)
expr_stmt|;
block|}
comment|// the remainder of the failover will be completed async
comment|// so we break out now, then the callback will be invoked which then continue routing from where we left here
return|return
literal|false
return|;
block|}
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Processing exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" is continued being processed synchronously"
argument_list|)
expr_stmt|;
block|}
comment|// loop while we should fail over
while|while
condition|(
name|shouldFailOver
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|attempts
operator|++
expr_stmt|;
comment|// are we exhausted by attempts?
if|if
condition|(
name|maximumFailoverAttempts
operator|>
operator|-
literal|1
operator|&&
name|attempts
operator|>
name|maximumFailoverAttempts
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Braking out of failover after "
operator|+
name|attempts
operator|+
literal|" failover attempts"
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
name|index
operator|++
expr_stmt|;
name|counter
operator|++
expr_stmt|;
if|if
condition|(
name|index
operator|>=
name|processors
operator|.
name|size
argument_list|()
condition|)
block|{
comment|// out of bounds
if|if
condition|(
name|isRoundRobin
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Failover is round robin enabled and therefore starting from the first endpoint"
argument_list|)
expr_stmt|;
name|index
operator|=
literal|0
expr_stmt|;
name|counter
operator|=
literal|0
expr_stmt|;
block|}
else|else
block|{
comment|// no more processors to try
name|log
operator|.
name|debug
argument_list|(
literal|"Braking out of failover as we reach the end of endpoints to use for failover"
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
comment|// try again but prepare exchange before we failover
name|prepareExchangeForFailover
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|processor
operator|=
name|processors
operator|.
name|get
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|sync
operator|=
name|processExchange
argument_list|(
name|processor
argument_list|,
name|exchange
argument_list|,
name|attempts
argument_list|,
name|index
argument_list|,
name|callback
argument_list|,
name|processors
argument_list|)
expr_stmt|;
comment|// continue as long its being processed synchronously
if|if
condition|(
operator|!
name|sync
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Processing exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" is continued being processed asynchronously"
argument_list|)
expr_stmt|;
block|}
comment|// the remainder of the failover will be completed async
comment|// so we break out now, then the callback will be invoked which then continue routing from where we left here
return|return
literal|false
return|;
block|}
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Processing exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" is continued being processed synchronously"
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|/**      * Prepares the exchange for failover      *      * @param exchange the exchange      */
DECL|method|prepareExchangeForFailover (Exchange exchange)
specifier|protected
name|void
name|prepareExchangeForFailover
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_HANDLED
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERED
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|)
expr_stmt|;
block|}
DECL|method|processExchange (final Processor processor, final Exchange exchange, final int attempts, final int index, final AsyncCallback callback, final List<Processor> processors)
specifier|private
name|boolean
name|processExchange
parameter_list|(
specifier|final
name|Processor
name|processor
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|int
name|attempts
parameter_list|,
specifier|final
name|int
name|index
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
specifier|final
name|List
argument_list|<
name|Processor
argument_list|>
name|processors
parameter_list|)
block|{
name|boolean
name|sync
decl_stmt|;
if|if
condition|(
name|processor
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No processors could be chosen to process "
operator|+
name|exchange
argument_list|)
throw|;
block|}
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Processing failover at attempt "
operator|+
name|attempts
operator|+
literal|" for exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|AsyncProcessor
name|albp
init|=
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|sync
operator|=
name|albp
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|FailOverAsyncCallback
argument_list|(
name|exchange
argument_list|,
name|attempts
argument_list|,
name|index
argument_list|,
name|callback
argument_list|,
name|processors
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|sync
return|;
block|}
comment|/**      * Failover logic to be executed asynchronously if one of the failover endpoints      * is a real {@link AsyncProcessor}.      */
DECL|class|FailOverAsyncCallback
specifier|private
specifier|final
class|class
name|FailOverAsyncCallback
implements|implements
name|AsyncCallback
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|attempts
specifier|private
name|int
name|attempts
decl_stmt|;
DECL|field|index
specifier|private
name|int
name|index
decl_stmt|;
DECL|field|callback
specifier|private
specifier|final
name|AsyncCallback
name|callback
decl_stmt|;
DECL|field|processors
specifier|private
specifier|final
name|List
argument_list|<
name|Processor
argument_list|>
name|processors
decl_stmt|;
DECL|method|FailOverAsyncCallback (Exchange exchange, int attempts, int index, AsyncCallback callback, List<Processor> processors)
specifier|private
name|FailOverAsyncCallback
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|int
name|attempts
parameter_list|,
name|int
name|index
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|List
argument_list|<
name|Processor
argument_list|>
name|processors
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|attempts
operator|=
name|attempts
expr_stmt|;
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
name|this
operator|.
name|processors
operator|=
name|processors
expr_stmt|;
block|}
DECL|method|done (boolean doneSync)
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// should we failover?
if|if
condition|(
name|shouldFailOver
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|attempts
operator|++
expr_stmt|;
comment|// are we exhausted by attempts?
if|if
condition|(
name|maximumFailoverAttempts
operator|>
operator|-
literal|1
operator|&&
name|attempts
operator|>
name|maximumFailoverAttempts
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Braking out of failover after "
operator|+
name|attempts
operator|+
literal|" failover attempts"
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|index
operator|++
expr_stmt|;
name|counter
operator|++
expr_stmt|;
if|if
condition|(
name|index
operator|>=
name|processors
operator|.
name|size
argument_list|()
condition|)
block|{
comment|// out of bounds
if|if
condition|(
name|isRoundRobin
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Failover is round robin enabled and therefore starting from the first endpoint"
argument_list|)
expr_stmt|;
name|index
operator|=
literal|0
expr_stmt|;
name|counter
operator|=
literal|0
expr_stmt|;
block|}
else|else
block|{
comment|// no more processors to try
name|log
operator|.
name|debug
argument_list|(
literal|"Braking out of failover as we reach the end of endpoints to use for failover"
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
comment|// try again but prepare exchange before we failover
name|prepareExchangeForFailover
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|processors
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
comment|// try to failover using the next processor
name|AsyncProcessor
name|albp
init|=
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|albp
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// we are done doing failover
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"FailoverLoadBalancer"
return|;
block|}
block|}
end_class

end_unit

