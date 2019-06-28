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
name|spi
operator|.
name|RouteContext
import|;
end_import

begin_class
DECL|class|RandomLoadBalancerReifier
specifier|public
class|class
name|RandomLoadBalancerReifier
extends|extends
name|LoadBalancerReifier
argument_list|<
name|RandomLoadBalancerDefinition
argument_list|>
block|{
DECL|method|RandomLoadBalancerReifier (LoadBalancerDefinition definition)
name|RandomLoadBalancerReifier
parameter_list|(
name|LoadBalancerDefinition
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|RandomLoadBalancerDefinition
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
return|return
operator|new
name|RandomLoadBalancer
argument_list|()
return|;
block|}
block|}
end_class

end_unit

