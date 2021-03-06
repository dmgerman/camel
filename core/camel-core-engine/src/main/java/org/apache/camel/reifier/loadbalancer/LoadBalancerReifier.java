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
name|HashMap
import|;
end_import

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
name|function
operator|.
name|Function
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
name|AbstractReifier
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
name|StringHelper
import|;
end_import

begin_class
DECL|class|LoadBalancerReifier
specifier|public
class|class
name|LoadBalancerReifier
parameter_list|<
name|T
extends|extends
name|LoadBalancerDefinition
parameter_list|>
extends|extends
name|AbstractReifier
block|{
DECL|field|LOAD_BALANCERS
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Function
argument_list|<
name|LoadBalancerDefinition
argument_list|,
name|LoadBalancerReifier
argument_list|<
name|?
extends|extends
name|LoadBalancerDefinition
argument_list|>
argument_list|>
argument_list|>
name|LOAD_BALANCERS
decl_stmt|;
static|static
block|{
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Function
argument_list|<
name|LoadBalancerDefinition
argument_list|,
name|LoadBalancerReifier
argument_list|<
name|?
extends|extends
name|LoadBalancerDefinition
argument_list|>
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|LoadBalancerDefinition
operator|.
name|class
argument_list|,
name|LoadBalancerReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|CustomLoadBalancerDefinition
operator|.
name|class
argument_list|,
name|CustomLoadBalancerReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|FailoverLoadBalancerDefinition
operator|.
name|class
argument_list|,
name|FailoverLoadBalancerReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|RandomLoadBalancerDefinition
operator|.
name|class
argument_list|,
name|RandomLoadBalancerReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|RoundRobinLoadBalancerDefinition
operator|.
name|class
argument_list|,
name|RoundRobinLoadBalancerReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|StickyLoadBalancerDefinition
operator|.
name|class
argument_list|,
name|StickyLoadBalancerReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|TopicLoadBalancerDefinition
operator|.
name|class
argument_list|,
name|TopicLoadBalancerReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|WeightedLoadBalancerDefinition
operator|.
name|class
argument_list|,
name|WeightedLoadBalancerReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|LOAD_BALANCERS
operator|=
name|map
expr_stmt|;
block|}
DECL|field|definition
specifier|protected
specifier|final
name|T
name|definition
decl_stmt|;
DECL|method|LoadBalancerReifier (T definition)
specifier|public
name|LoadBalancerReifier
parameter_list|(
name|T
name|definition
parameter_list|)
block|{
name|this
operator|.
name|definition
operator|=
name|definition
expr_stmt|;
block|}
DECL|method|reifier (LoadBalancerDefinition definition)
specifier|public
specifier|static
name|LoadBalancerReifier
argument_list|<
name|?
extends|extends
name|LoadBalancerDefinition
argument_list|>
name|reifier
parameter_list|(
name|LoadBalancerDefinition
name|definition
parameter_list|)
block|{
name|Function
argument_list|<
name|LoadBalancerDefinition
argument_list|,
name|LoadBalancerReifier
argument_list|<
name|?
extends|extends
name|LoadBalancerDefinition
argument_list|>
argument_list|>
name|reifier
init|=
name|LOAD_BALANCERS
operator|.
name|get
argument_list|(
name|definition
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|reifier
operator|!=
literal|null
condition|)
block|{
return|return
name|reifier
operator|.
name|apply
argument_list|(
name|definition
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unsupported definition: "
operator|+
name|definition
argument_list|)
throw|;
block|}
comment|/**      * Factory method to create the load balancer from the loadBalancerTypeName      */
DECL|method|createLoadBalancer (RouteContext routeContext)
specifier|public
name|LoadBalancer
name|createLoadBalancer
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|String
name|loadBalancerTypeName
init|=
name|definition
operator|.
name|getLoadBalancerTypeName
argument_list|()
decl_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|loadBalancerTypeName
argument_list|,
literal|"loadBalancerTypeName"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|LoadBalancer
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|loadBalancerTypeName
operator|!=
literal|null
condition|)
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
name|loadBalancerTypeName
argument_list|,
name|LoadBalancer
operator|.
name|class
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
name|loadBalancerTypeName
operator|+
literal|" in the classpath"
argument_list|)
throw|;
block|}
name|answer
operator|=
operator|(
name|LoadBalancer
operator|)
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|type
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|definition
operator|.
name|configureLoadBalancer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

