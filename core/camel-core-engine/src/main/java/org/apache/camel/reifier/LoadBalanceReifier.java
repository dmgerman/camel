begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Channel
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
name|LoadBalanceDefinition
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
name|ProcessorDefinition
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
name|reifier
operator|.
name|loadbalancer
operator|.
name|LoadBalancerReifier
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

begin_class
DECL|class|LoadBalanceReifier
specifier|public
class|class
name|LoadBalanceReifier
extends|extends
name|ProcessorReifier
argument_list|<
name|LoadBalanceDefinition
argument_list|>
block|{
DECL|method|LoadBalanceReifier (ProcessorDefinition<?> definition)
specifier|public
name|LoadBalanceReifier
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|LoadBalanceDefinition
operator|)
name|definition
argument_list|)
expr_stmt|;
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
comment|// the load balancer is stateful so we should only create it once in
comment|// case its used from a context scoped error handler
name|LoadBalancer
name|loadBalancer
init|=
name|definition
operator|.
name|getLoadBalancerType
argument_list|()
operator|.
name|getLoadBalancer
argument_list|()
decl_stmt|;
if|if
condition|(
name|loadBalancer
operator|==
literal|null
condition|)
block|{
comment|// then create it and reuse it
name|loadBalancer
operator|=
name|LoadBalancerReifier
operator|.
name|reifier
argument_list|(
name|definition
operator|.
name|getLoadBalancerType
argument_list|()
argument_list|)
operator|.
name|createLoadBalancer
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
name|definition
operator|.
name|getLoadBalancerType
argument_list|()
operator|.
name|setLoadBalancer
argument_list|(
name|loadBalancer
argument_list|)
expr_stmt|;
comment|// some load balancer can only support a fixed number of outputs
name|int
name|max
init|=
name|definition
operator|.
name|getLoadBalancerType
argument_list|()
operator|.
name|getMaximumNumberOfOutputs
argument_list|()
decl_stmt|;
name|int
name|size
init|=
name|definition
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|>
name|max
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"To many outputs configured on "
operator|+
name|definition
operator|.
name|getLoadBalancerType
argument_list|()
operator|+
literal|": "
operator|+
name|size
operator|+
literal|"> "
operator|+
name|max
argument_list|)
throw|;
block|}
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|processorType
range|:
name|definition
operator|.
name|getOutputs
argument_list|()
control|)
block|{
comment|// output must not be another load balancer
comment|// check for instanceof as the code below as there is
comment|// compilation errors on earlier versions of JDK6
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
name|definition
operator|.
name|getLoadBalancerType
argument_list|()
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
name|Channel
name|channel
init|=
name|wrapChannel
argument_list|(
name|routeContext
argument_list|,
name|processor
argument_list|,
name|processorType
argument_list|)
decl_stmt|;
name|loadBalancer
operator|.
name|addProcessor
argument_list|(
name|channel
argument_list|)
expr_stmt|;
block|}
block|}
name|Boolean
name|inherit
init|=
name|definition
operator|.
name|isInheritErrorHandler
argument_list|()
decl_stmt|;
if|if
condition|(
name|definition
operator|.
name|getLoadBalancerType
argument_list|()
operator|instanceof
name|FailoverLoadBalancerDefinition
condition|)
block|{
comment|// special for failover load balancer where you can configure it to
comment|// not inherit error handler for its children
comment|// but the load balancer itself should inherit so Camels error
comment|// handler can react afterwards
name|inherit
operator|=
literal|true
expr_stmt|;
block|}
name|Processor
name|target
init|=
name|wrapChannel
argument_list|(
name|routeContext
argument_list|,
name|loadBalancer
argument_list|,
name|definition
argument_list|,
name|inherit
argument_list|)
decl_stmt|;
return|return
name|target
return|;
block|}
block|}
end_class

end_unit

