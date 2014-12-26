begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|XmlElementRef
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
name|XmlElements
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Expression
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
name|model
operator|.
name|loadbalancer
operator|.
name|CircuitBreakerLoadBalancerDefinition
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
name|loadbalancer
operator|.
name|CustomLoadBalancerDefinition
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
name|loadbalancer
operator|.
name|FailoverLoadBalancerDefinition
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
name|loadbalancer
operator|.
name|RandomLoadBalancerDefinition
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
name|loadbalancer
operator|.
name|RoundRobinLoadBalancerDefinition
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
name|loadbalancer
operator|.
name|StickyLoadBalancerDefinition
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
name|loadbalancer
operator|.
name|TopicLoadBalancerDefinition
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
name|loadbalancer
operator|.
name|WeightedLoadBalancerDefinition
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
name|FailOverLoadBalancer
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
name|processor
operator|.
name|loadbalancer
operator|.
name|RandomLoadBalancer
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
name|RoundRobinLoadBalancer
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
name|StickyLoadBalancer
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
name|TopicLoadBalancer
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
name|WeightedLoadBalancer
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
name|WeightedRandomLoadBalancer
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
name|WeightedRoundRobinLoadBalancer
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
name|CollectionStringBuffer
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;loadBalance/&gt; element  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"loadBalance"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|LoadBalanceDefinition
specifier|public
class|class
name|LoadBalanceDefinition
extends|extends
name|ProcessorDefinition
argument_list|<
name|LoadBalanceDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
annotation|@
name|Deprecated
DECL|field|ref
specifier|private
name|String
name|ref
decl_stmt|;
annotation|@
name|XmlElements
argument_list|(
block|{
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"failover"
argument_list|,
name|type
operator|=
name|FailoverLoadBalancerDefinition
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"random"
argument_list|,
name|type
operator|=
name|RandomLoadBalancerDefinition
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"custom"
argument_list|,
name|type
operator|=
name|CustomLoadBalancerDefinition
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"roundRobin"
argument_list|,
name|type
operator|=
name|RoundRobinLoadBalancerDefinition
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"sticky"
argument_list|,
name|type
operator|=
name|StickyLoadBalancerDefinition
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"topic"
argument_list|,
name|type
operator|=
name|TopicLoadBalancerDefinition
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"weighted"
argument_list|,
name|type
operator|=
name|WeightedLoadBalancerDefinition
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"circuitBreaker"
argument_list|,
name|type
operator|=
name|CircuitBreakerLoadBalancerDefinition
operator|.
name|class
argument_list|)
block|}
argument_list|)
DECL|field|loadBalancerType
specifier|private
name|LoadBalancerDefinition
name|loadBalancerType
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|outputs
specifier|private
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|LoadBalanceDefinition ()
specifier|public
name|LoadBalanceDefinition
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|outputs
return|;
block|}
DECL|method|setOutputs (List<ProcessorDefinition<?>> outputs)
specifier|public
name|void
name|setOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
parameter_list|)
block|{
name|this
operator|.
name|outputs
operator|=
name|outputs
expr_stmt|;
if|if
condition|(
name|outputs
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|output
range|:
name|outputs
control|)
block|{
name|configureChild
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|isOutputSupported ()
specifier|public
name|boolean
name|isOutputSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getRef ()
specifier|public
name|String
name|getRef
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
DECL|method|setRef (String ref)
specifier|public
name|void
name|setRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
DECL|method|getLoadBalancerType ()
specifier|public
name|LoadBalancerDefinition
name|getLoadBalancerType
parameter_list|()
block|{
return|return
name|loadBalancerType
return|;
block|}
DECL|method|setLoadBalancerType (LoadBalancerDefinition loadbalancer)
specifier|public
name|void
name|setLoadBalancerType
parameter_list|(
name|LoadBalancerDefinition
name|loadbalancer
parameter_list|)
block|{
if|if
condition|(
name|loadBalancerType
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Loadbalancer already configured to: "
operator|+
name|loadBalancerType
operator|+
literal|". Cannot set it to: "
operator|+
name|loadbalancer
argument_list|)
throw|;
block|}
name|loadBalancerType
operator|=
name|loadbalancer
expr_stmt|;
block|}
DECL|method|createOutputsProcessor (RouteContext routeContext, Collection<ProcessorDefinition<?>> outputs)
specifier|protected
name|Processor
name|createOutputsProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Collection
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
parameter_list|)
throws|throws
name|Exception
block|{
name|LoadBalancer
name|loadBalancer
init|=
name|LoadBalancerDefinition
operator|.
name|getLoadBalancer
argument_list|(
name|routeContext
argument_list|,
name|loadBalancerType
argument_list|,
name|ref
argument_list|)
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|processorType
range|:
name|outputs
control|)
block|{
name|Processor
name|processor
init|=
name|createProcessor
argument_list|(
name|routeContext
argument_list|,
name|processorType
argument_list|)
decl_stmt|;
name|loadBalancer
operator|.
name|addProcessor
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
return|return
name|loadBalancer
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|LoadBalancer
name|loadBalancer
init|=
name|LoadBalancerDefinition
operator|.
name|getLoadBalancer
argument_list|(
name|routeContext
argument_list|,
name|loadBalancerType
argument_list|,
name|ref
argument_list|)
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|processorType
range|:
name|getOutputs
argument_list|()
control|)
block|{
comment|// output must not be another load balancer
comment|// check for instanceof as the code below as there is compilation errors on earlier versions of JDK6
comment|// on Windows boxes or with IBM JDKs etc.
if|if
condition|(
name|LoadBalanceDefinition
operator|.
name|class
operator|.
name|isInstance
argument_list|(
name|processorType
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Loadbalancer already configured to: "
operator|+
name|loadBalancerType
operator|+
literal|". Cannot set it to: "
operator|+
name|processorType
argument_list|)
throw|;
block|}
name|Processor
name|processor
init|=
name|createProcessor
argument_list|(
name|routeContext
argument_list|,
name|processorType
argument_list|)
decl_stmt|;
name|processor
operator|=
name|wrapChannel
argument_list|(
name|routeContext
argument_list|,
name|processor
argument_list|,
name|processorType
argument_list|)
expr_stmt|;
name|loadBalancer
operator|.
name|addProcessor
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
return|return
name|loadBalancer
return|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Uses a custom load balancer      *      * @param loadBalancer  the load balancer      * @return the builder      */
DECL|method|loadBalance (LoadBalancer loadBalancer)
specifier|public
name|LoadBalanceDefinition
name|loadBalance
parameter_list|(
name|LoadBalancer
name|loadBalancer
parameter_list|)
block|{
name|setLoadBalancerType
argument_list|(
operator|new
name|LoadBalancerDefinition
argument_list|(
name|loadBalancer
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses fail over load balancer      *<p/>      * Will not round robin and inherit the error handler.      *      * @return the builder      */
DECL|method|failover ()
specifier|public
name|LoadBalanceDefinition
name|failover
parameter_list|()
block|{
return|return
name|failover
argument_list|(
operator|-
literal|1
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Uses fail over load balancer      *<p/>      * Will not round robin and inherit the error handler.      *      * @param exceptions exception classes which we want to failover if one of them was thrown      * @return the builder      */
DECL|method|failover (Class<?>.... exceptions)
specifier|public
name|LoadBalanceDefinition
name|failover
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|exceptions
parameter_list|)
block|{
return|return
name|failover
argument_list|(
operator|-
literal|1
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
name|exceptions
argument_list|)
return|;
block|}
comment|/**      * Uses fail over load balancer      *      * @param maximumFailoverAttempts  maximum number of failover attempts before exhausting.      *                                 Use -1 to newer exhaust when round robin is also enabled.      *                                 If round robin is disabled then it will exhaust when there are no more endpoints to failover      * @param inheritErrorHandler      whether or not to inherit error handler.      *                                 If<tt>false</tt> then it will failover immediately in case of an exception      * @param roundRobin               whether or not to use round robin (which keeps state)      * @param exceptions               exception classes which we want to failover if one of them was thrown      * @return the builder      */
DECL|method|failover (int maximumFailoverAttempts, boolean inheritErrorHandler, boolean roundRobin, Class<?>... exceptions)
specifier|public
name|LoadBalanceDefinition
name|failover
parameter_list|(
name|int
name|maximumFailoverAttempts
parameter_list|,
name|boolean
name|inheritErrorHandler
parameter_list|,
name|boolean
name|roundRobin
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|exceptions
parameter_list|)
block|{
name|FailOverLoadBalancer
name|failover
init|=
operator|new
name|FailOverLoadBalancer
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|exceptions
argument_list|)
argument_list|)
decl_stmt|;
name|failover
operator|.
name|setMaximumFailoverAttempts
argument_list|(
name|maximumFailoverAttempts
argument_list|)
expr_stmt|;
name|failover
operator|.
name|setRoundRobin
argument_list|(
name|roundRobin
argument_list|)
expr_stmt|;
name|setLoadBalancerType
argument_list|(
operator|new
name|LoadBalancerDefinition
argument_list|(
name|failover
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|setInheritErrorHandler
argument_list|(
name|inheritErrorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses weighted load balancer      *      * @param roundRobin                   used to set the processor selection algorithm.      * @param distributionRatio            String of weighted ratios for distribution of messages.      * @return the builder      */
DECL|method|weighted (boolean roundRobin, String distributionRatio)
specifier|public
name|LoadBalanceDefinition
name|weighted
parameter_list|(
name|boolean
name|roundRobin
parameter_list|,
name|String
name|distributionRatio
parameter_list|)
block|{
return|return
name|weighted
argument_list|(
name|roundRobin
argument_list|,
name|distributionRatio
argument_list|,
literal|","
argument_list|)
return|;
block|}
comment|/**      * Uses circuitBreaker load balancer      *      * @param threshold         number of errors before failure.      * @param halfOpenAfter     time interval in milliseconds for half open state.      * @param exceptions        exception classes which we want to break if one of them was thrown      * @return the builder      */
DECL|method|circuitBreaker (int threshold, long halfOpenAfter, Class<?>... exceptions)
specifier|public
name|LoadBalanceDefinition
name|circuitBreaker
parameter_list|(
name|int
name|threshold
parameter_list|,
name|long
name|halfOpenAfter
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|exceptions
parameter_list|)
block|{
name|CircuitBreakerLoadBalancer
name|breakerLoadBalancer
init|=
operator|new
name|CircuitBreakerLoadBalancer
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|exceptions
argument_list|)
argument_list|)
decl_stmt|;
name|breakerLoadBalancer
operator|.
name|setThreshold
argument_list|(
name|threshold
argument_list|)
expr_stmt|;
name|breakerLoadBalancer
operator|.
name|setHalfOpenAfter
argument_list|(
name|halfOpenAfter
argument_list|)
expr_stmt|;
name|setLoadBalancerType
argument_list|(
operator|new
name|LoadBalancerDefinition
argument_list|(
name|breakerLoadBalancer
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses weighted load balancer      *      * @param roundRobin                   used to set the processor selection algorithm.      * @param distributionRatio            String of weighted ratios for distribution of messages.      * @param distributionRatioDelimiter   String containing delimiter to be used for ratios      * @return the builder      */
DECL|method|weighted (boolean roundRobin, String distributionRatio, String distributionRatioDelimiter)
specifier|public
name|LoadBalanceDefinition
name|weighted
parameter_list|(
name|boolean
name|roundRobin
parameter_list|,
name|String
name|distributionRatio
parameter_list|,
name|String
name|distributionRatioDelimiter
parameter_list|)
block|{
name|WeightedLoadBalancer
name|weighted
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|distributionRatioList
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
name|String
index|[]
name|ratios
init|=
name|distributionRatio
operator|.
name|split
argument_list|(
name|distributionRatioDelimiter
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|ratio
range|:
name|ratios
control|)
block|{
name|distributionRatioList
operator|.
name|add
argument_list|(
operator|new
name|Integer
argument_list|(
name|ratio
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|roundRobin
condition|)
block|{
name|weighted
operator|=
operator|new
name|WeightedRandomLoadBalancer
argument_list|(
name|distributionRatioList
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|weighted
operator|=
operator|new
name|WeightedRoundRobinLoadBalancer
argument_list|(
name|distributionRatioList
argument_list|)
expr_stmt|;
block|}
name|setLoadBalancerType
argument_list|(
operator|new
name|LoadBalancerDefinition
argument_list|(
name|weighted
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses round robin load balancer      *      * @return the builder      */
DECL|method|roundRobin ()
specifier|public
name|LoadBalanceDefinition
name|roundRobin
parameter_list|()
block|{
name|setLoadBalancerType
argument_list|(
operator|new
name|LoadBalancerDefinition
argument_list|(
operator|new
name|RoundRobinLoadBalancer
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses random load balancer      *      * @return the builder      */
DECL|method|random ()
specifier|public
name|LoadBalanceDefinition
name|random
parameter_list|()
block|{
name|setLoadBalancerType
argument_list|(
operator|new
name|LoadBalancerDefinition
argument_list|(
operator|new
name|RandomLoadBalancer
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses the custom load balancer      *      * @param ref reference to lookup a custom load balancer from the {@link org.apache.camel.spi.Registry} to be used.      * @return the builder      */
DECL|method|custom (String ref)
specifier|public
name|LoadBalanceDefinition
name|custom
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|CustomLoadBalancerDefinition
name|balancer
init|=
operator|new
name|CustomLoadBalancerDefinition
argument_list|()
decl_stmt|;
name|balancer
operator|.
name|setRef
argument_list|(
name|ref
argument_list|)
expr_stmt|;
name|setLoadBalancerType
argument_list|(
name|balancer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses sticky load balancer      *      * @param correlationExpression  the expression for correlation      * @return  the builder      */
DECL|method|sticky (Expression correlationExpression)
specifier|public
name|LoadBalanceDefinition
name|sticky
parameter_list|(
name|Expression
name|correlationExpression
parameter_list|)
block|{
name|setLoadBalancerType
argument_list|(
operator|new
name|LoadBalancerDefinition
argument_list|(
operator|new
name|StickyLoadBalancer
argument_list|(
name|correlationExpression
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses topic load balancer      *       * @return the builder      */
DECL|method|topic ()
specifier|public
name|LoadBalanceDefinition
name|topic
parameter_list|()
block|{
name|setLoadBalancerType
argument_list|(
operator|new
name|LoadBalancerDefinition
argument_list|(
operator|new
name|TopicLoadBalancer
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
name|CollectionStringBuffer
name|buffer
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
literal|"loadBalance["
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|list
init|=
name|getOutputs
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|processorType
range|:
name|list
control|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|processorType
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|loadBalancerType
operator|!=
literal|null
condition|)
block|{
return|return
literal|"LoadBalanceType["
operator|+
name|loadBalancerType
operator|+
literal|", "
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
else|else
block|{
return|return
literal|"LoadBalanceType[ref:"
operator|+
name|ref
operator|+
literal|", "
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
block|}
end_class

end_unit

