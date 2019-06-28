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

begin_class
DECL|class|FailoverLoadBalancerReifier
specifier|public
class|class
name|FailoverLoadBalancerReifier
extends|extends
name|LoadBalancerReifier
argument_list|<
name|FailoverLoadBalancerDefinition
argument_list|>
block|{
DECL|method|FailoverLoadBalancerReifier (LoadBalancerDefinition definition)
name|FailoverLoadBalancerReifier
parameter_list|(
name|LoadBalancerDefinition
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|FailoverLoadBalancerDefinition
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
name|FailOverLoadBalancer
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
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|definition
operator|.
name|getExceptionTypes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|classes
operator|.
name|addAll
argument_list|(
name|definition
operator|.
name|getExceptionTypes
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|definition
operator|.
name|getExceptions
argument_list|()
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
name|definition
operator|.
name|getExceptions
argument_list|()
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
name|FailOverLoadBalancer
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|FailOverLoadBalancer
argument_list|(
name|classes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getMaximumFailoverAttempts
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setMaximumFailoverAttempts
argument_list|(
name|definition
operator|.
name|getMaximumFailoverAttempts
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getRoundRobin
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setRoundRobin
argument_list|(
name|definition
operator|.
name|getRoundRobin
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getSticky
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setSticky
argument_list|(
name|definition
operator|.
name|getSticky
argument_list|()
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

