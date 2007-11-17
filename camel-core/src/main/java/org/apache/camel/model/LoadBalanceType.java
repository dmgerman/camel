begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Collection
import|;
end_import

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
name|Predicate
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
name|model
operator|.
name|loadbalancer
operator|.
name|LoadBalancerType
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
name|RandomLoadBalanceStrategy
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
name|RoundRobinLoadBalanceStrategy
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
name|StickyLoadBalanceStrategy
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
name|TopicLoadBalanceStrategy
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
name|ChoiceProcessor
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
name|FilterProcessor
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
name|SendProcessor
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
name|util
operator|.
name|CollectionStringBuffer
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
DECL|class|LoadBalanceType
specifier|public
class|class
name|LoadBalanceType
extends|extends
name|OutputType
argument_list|<
name|LoadBalanceType
argument_list|>
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
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
literal|"roundRobin"
argument_list|,
name|type
operator|=
name|RoundRobinLoadBalanceStrategy
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
name|RandomLoadBalanceStrategy
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
name|StickyLoadBalanceStrategy
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
name|TopicLoadBalanceStrategy
operator|.
name|class
argument_list|)
block|}
argument_list|)
DECL|field|loadBalancerType
specifier|private
name|LoadBalancerType
name|loadBalancerType
decl_stmt|;
DECL|method|LoadBalanceType ()
specifier|public
name|LoadBalanceType
parameter_list|()
block|{              }
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
name|LoadBalancerType
name|getLoadBalancerType
parameter_list|()
block|{
return|return
name|loadBalancerType
return|;
block|}
DECL|method|setLoadBalancerType (LoadBalancerType loadbalancer)
specifier|public
name|void
name|setLoadBalancerType
parameter_list|(
name|LoadBalancerType
name|loadbalancer
parameter_list|)
block|{
name|loadBalancerType
operator|=
name|loadbalancer
expr_stmt|;
block|}
DECL|method|createOutputsProcessor (RouteContext routeContext, Collection<ProcessorType<?>> outputs)
specifier|protected
name|Processor
name|createOutputsProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Collection
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|loadBalancerType
argument_list|,
literal|"loadBalancerType"
argument_list|)
expr_stmt|;
name|LoadBalancer
name|loadBalancer
init|=
name|loadBalancerType
operator|.
name|getLoadBalancer
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
for|for
control|(
name|ProcessorType
name|processorType
range|:
name|outputs
control|)
block|{
comment|//The outputs should be the SendProcessor
name|SendProcessor
name|processor
init|=
operator|(
name|SendProcessor
operator|)
name|processorType
operator|.
name|createProcessor
argument_list|(
name|routeContext
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
comment|// when this method will be called
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|loadBalancerType
argument_list|,
literal|"loadBalancerType"
argument_list|)
expr_stmt|;
name|LoadBalancer
name|loadBalancer
init|=
name|loadBalancerType
operator|.
name|getLoadBalancer
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
for|for
control|(
name|ProcessorType
name|processorType
range|:
name|getOutputs
argument_list|()
control|)
block|{
comment|//The outputs should be the SendProcessor
name|SendProcessor
name|processor
init|=
operator|(
name|SendProcessor
operator|)
name|processorType
operator|.
name|createProcessor
argument_list|(
name|routeContext
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
comment|// Fluent API
comment|// -------------------------------------------------------------------------
DECL|method|setLoadBalancer (LoadBalancer loadBalancer)
specifier|public
name|LoadBalanceType
name|setLoadBalancer
parameter_list|(
name|LoadBalancer
name|loadBalancer
parameter_list|)
block|{
name|loadBalancerType
operator|=
operator|new
name|LoadBalancerType
argument_list|(
name|loadBalancer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|roundRobin ()
specifier|public
name|LoadBalanceType
name|roundRobin
parameter_list|()
block|{
name|loadBalancerType
operator|=
operator|new
name|LoadBalancerType
argument_list|(
operator|new
name|RoundRobinLoadBalancer
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|random ()
specifier|public
name|LoadBalanceType
name|random
parameter_list|()
block|{
name|loadBalancerType
operator|=
operator|new
name|LoadBalancerType
argument_list|(
operator|new
name|RandomLoadBalancer
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|sticky (Expression<Exchange> correlationExpression)
specifier|public
name|LoadBalanceType
name|sticky
parameter_list|(
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|correlationExpression
parameter_list|)
block|{
name|loadBalancerType
operator|=
operator|new
name|LoadBalancerType
argument_list|(
operator|new
name|StickyLoadBalancer
argument_list|(
name|correlationExpression
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|topic ()
specifier|public
name|LoadBalanceType
name|topic
parameter_list|()
block|{
name|loadBalancerType
operator|=
operator|new
name|LoadBalancerType
argument_list|(
operator|new
name|TopicLoadBalancer
argument_list|()
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
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProcessorType
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
name|ProcessorType
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
name|String
name|result
decl_stmt|;
if|if
condition|(
name|loadBalancerType
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
literal|"LoadBalanceType["
operator|+
name|loadBalancerType
operator|+
literal|", "
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
literal|"LoadBalanceType["
operator|+
name|ref
operator|+
literal|", "
expr_stmt|;
block|}
name|result
operator|=
name|result
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

