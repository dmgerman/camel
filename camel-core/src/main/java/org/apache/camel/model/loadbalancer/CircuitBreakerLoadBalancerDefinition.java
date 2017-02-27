begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.loadbalancer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|LoadBalancerDefinition
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
name|loadbalancer
operator|.
name|CircuitBreakerLoadBalancer
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
name|loadbalancer
operator|.
name|LoadBalancer
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
name|Metadata
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
name|RouteContext
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
comment|/**  * Circuit break load balancer  *<p/>  * The Circuit Breaker load balancer is a stateful pattern that monitors all calls for certain exceptions.  * Initially the Circuit Breaker is in closed state and passes all messages.  * If there are failures and the threshold is reached, it moves to open state and rejects all calls until halfOpenAfter  * timeout is reached. After this timeout is reached, if there is a new call, it will pass and if the result is  * success the Circuit Breaker will move to closed state, or to open state if there was an error.  *  * @deprecated use Hystrix EIP instead which is the popular Netflix implementation of circuit breaker  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,routing,loadbalance,circuitbreaker"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"circuitBreaker"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|Deprecated
DECL|class|CircuitBreakerLoadBalancerDefinition
specifier|public
class|class
name|CircuitBreakerLoadBalancerDefinition
extends|extends
name|LoadBalancerDefinition
block|{
annotation|@
name|XmlTransient
DECL|field|exceptionTypes
specifier|private
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|exceptionTypes
init|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"exception"
argument_list|)
DECL|field|exceptions
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|exceptions
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|halfOpenAfter
specifier|private
name|Long
name|halfOpenAfter
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|threshold
specifier|private
name|Integer
name|threshold
decl_stmt|;
DECL|method|CircuitBreakerLoadBalancerDefinition ()
specifier|public
name|CircuitBreakerLoadBalancerDefinition
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|getMaximumNumberOfOutputs ()
specifier|protected
name|int
name|getMaximumNumberOfOutputs
parameter_list|()
block|{
comment|// we can only support 1 output
return|return
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|createLoadBalancer (RouteContext routeContext)
specifier|protected
name|LoadBalancer
name|createLoadBalancer
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|CircuitBreakerLoadBalancer
name|answer
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|exceptionTypes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|classes
operator|.
name|addAll
argument_list|(
name|exceptionTypes
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|exceptions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|String
name|name
range|:
name|exceptions
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find class: "
operator|+
name|name
operator|+
literal|" in the classpath"
argument_list|)
throw|;
block|}
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
name|classes
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|classes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|answer
operator|=
operator|new
name|CircuitBreakerLoadBalancer
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|CircuitBreakerLoadBalancer
argument_list|(
name|classes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getHalfOpenAfter
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setHalfOpenAfter
argument_list|(
name|getHalfOpenAfter
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getThreshold
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setThreshold
argument_list|(
name|getThreshold
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getHalfOpenAfter ()
specifier|public
name|Long
name|getHalfOpenAfter
parameter_list|()
block|{
return|return
name|halfOpenAfter
return|;
block|}
comment|/**      * The timeout in millis to use as threshold to move state from closed to half-open or open state      */
DECL|method|setHalfOpenAfter (Long halfOpenAfter)
specifier|public
name|void
name|setHalfOpenAfter
parameter_list|(
name|Long
name|halfOpenAfter
parameter_list|)
block|{
name|this
operator|.
name|halfOpenAfter
operator|=
name|halfOpenAfter
expr_stmt|;
block|}
DECL|method|getThreshold ()
specifier|public
name|Integer
name|getThreshold
parameter_list|()
block|{
return|return
name|threshold
return|;
block|}
comment|/**      * Number of previous failed messages to use as threshold to move state from closed to half-open or open state      */
DECL|method|setThreshold (Integer threshold)
specifier|public
name|void
name|setThreshold
parameter_list|(
name|Integer
name|threshold
parameter_list|)
block|{
name|this
operator|.
name|threshold
operator|=
name|threshold
expr_stmt|;
block|}
DECL|method|getExceptions ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExceptions
parameter_list|()
block|{
return|return
name|exceptions
return|;
block|}
comment|/**      * A list of class names for specific exceptions to monitor.      * If no exceptions is configured then all exceptions is monitored      */
DECL|method|setExceptions (List<String> exceptions)
specifier|public
name|void
name|setExceptions
parameter_list|(
name|List
argument_list|<
name|String
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
block|}
DECL|method|getExceptionTypes ()
specifier|public
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getExceptionTypes
parameter_list|()
block|{
return|return
name|exceptionTypes
return|;
block|}
comment|/**      * A list of specific exceptions to monitor.      * If no exceptions is configured then all exceptions is monitored      */
DECL|method|setExceptionTypes (List<Class<?>> exceptionTypes)
specifier|public
name|void
name|setExceptionTypes
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|exceptionTypes
parameter_list|)
block|{
name|this
operator|.
name|exceptionTypes
operator|=
name|exceptionTypes
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"CircuitBreakerLoadBalancer"
return|;
block|}
block|}
end_class

end_unit

