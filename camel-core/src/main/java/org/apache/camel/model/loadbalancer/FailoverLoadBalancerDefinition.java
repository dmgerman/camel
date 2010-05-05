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

begin_comment
comment|/**  * Represents an XML&lt;failover/&gt; element  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"failover"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|FailoverLoadBalancerDefinition
specifier|public
class|class
name|FailoverLoadBalancerDefinition
extends|extends
name|LoadBalancerDefinition
block|{
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
DECL|field|roundRobin
specifier|private
name|Boolean
name|roundRobin
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|maximumFailoverAttempts
specifier|private
name|Integer
name|maximumFailoverAttempts
decl_stmt|;
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
name|FailOverLoadBalancer
name|answer
decl_stmt|;
if|if
condition|(
operator|!
name|exceptions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
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
name|classes
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
operator|new
name|FailOverLoadBalancer
argument_list|(
name|classes
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|FailOverLoadBalancer
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
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
name|getMaximumFailoverAttempts
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isRoundRobin
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setRoundRobin
argument_list|(
name|isRoundRobin
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
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
DECL|method|isRoundRobin ()
specifier|public
name|Boolean
name|isRoundRobin
parameter_list|()
block|{
return|return
name|roundRobin
return|;
block|}
DECL|method|setRoundRobin (Boolean roundRobin)
specifier|public
name|void
name|setRoundRobin
parameter_list|(
name|Boolean
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
name|Integer
name|getMaximumFailoverAttempts
parameter_list|()
block|{
return|return
name|maximumFailoverAttempts
return|;
block|}
DECL|method|setMaximumFailoverAttempts (Integer maximumFailoverAttempts)
specifier|public
name|void
name|setMaximumFailoverAttempts
parameter_list|(
name|Integer
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
annotation|@
name|Override
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

