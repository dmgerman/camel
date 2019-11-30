begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier.loadbalancer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
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

begin_class
DECL|class|WeightedLoadBalancerReifier
specifier|public
class|class
name|WeightedLoadBalancerReifier
extends|extends
name|LoadBalancerReifier
argument_list|<
name|WeightedLoadBalancerDefinition
argument_list|>
block|{
DECL|method|WeightedLoadBalancerReifier (LoadBalancerDefinition definition)
specifier|public
name|WeightedLoadBalancerReifier
parameter_list|(
name|LoadBalancerDefinition
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|WeightedLoadBalancerDefinition
operator|)
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createLoadBalancer (RouteContext routeContext)
specifier|public
name|LoadBalancer
name|createLoadBalancer
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|WeightedLoadBalancer
name|loadBalancer
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|distributionRatioList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
try|try
block|{
name|String
index|[]
name|ratios
init|=
name|definition
operator|.
name|getDistributionRatio
argument_list|()
operator|.
name|split
argument_list|(
name|definition
operator|.
name|getDistributionRatioDelimiter
argument_list|()
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
name|parseInt
argument_list|(
name|routeContext
argument_list|,
name|ratio
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|boolean
name|isRoundRobin
init|=
name|definition
operator|.
name|getRoundRobin
argument_list|()
operator|!=
literal|null
operator|&&
name|parseBoolean
argument_list|(
name|routeContext
argument_list|,
name|definition
operator|.
name|getRoundRobin
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|isRoundRobin
condition|)
block|{
name|loadBalancer
operator|=
operator|new
name|WeightedRoundRobinLoadBalancer
argument_list|(
name|distributionRatioList
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|loadBalancer
operator|=
operator|new
name|WeightedRandomLoadBalancer
argument_list|(
name|distributionRatioList
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
name|loadBalancer
return|;
block|}
block|}
end_class

end_unit

