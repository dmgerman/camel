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
name|language
operator|.
name|ExpressionType
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

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"sticky"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|StickyLoadBalanceStrategy
specifier|public
class|class
name|StickyLoadBalanceStrategy
extends|extends
name|LoadBalancerType
block|{
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|,
name|name
operator|=
literal|"expression"
argument_list|,
name|type
operator|=
name|ExpressionType
operator|.
name|class
argument_list|)
DECL|field|expressionType
specifier|private
name|ExpressionType
name|expressionType
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"loadBalancer"
argument_list|,
name|type
operator|=
name|ExpressionType
operator|.
name|class
argument_list|)
DECL|field|loadBalancerType
specifier|private
name|LoadBalancerType
name|loadBalancerType
decl_stmt|;
DECL|method|StickyLoadBalanceStrategy ()
specifier|public
name|StickyLoadBalanceStrategy
parameter_list|()
block|{
name|super
argument_list|(
literal|"org.apache.camel.processor.loadbalancer.StickyLoadBalancer"
argument_list|)
expr_stmt|;
block|}
DECL|method|StickyLoadBalanceStrategy (ExpressionType expressionType)
specifier|public
name|StickyLoadBalanceStrategy
parameter_list|(
name|ExpressionType
name|expressionType
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|expressionType
operator|=
name|expressionType
expr_stmt|;
block|}
DECL|method|StickyLoadBalanceStrategy (ExpressionType expressionType, LoadBalancerType loadBalancerType)
specifier|public
name|StickyLoadBalanceStrategy
parameter_list|(
name|ExpressionType
name|expressionType
parameter_list|,
name|LoadBalancerType
name|loadBalancerType
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|expressionType
operator|=
name|expressionType
expr_stmt|;
name|this
operator|.
name|loadBalancerType
operator|=
name|loadBalancerType
expr_stmt|;
block|}
DECL|method|setExpressionType (ExpressionType expressionType)
specifier|public
name|void
name|setExpressionType
parameter_list|(
name|ExpressionType
name|expressionType
parameter_list|)
block|{
name|this
operator|.
name|expressionType
operator|=
name|expressionType
expr_stmt|;
block|}
DECL|method|getExpressionType ()
specifier|public
name|ExpressionType
name|getExpressionType
parameter_list|()
block|{
return|return
name|expressionType
return|;
block|}
DECL|method|setLoadBalancerType (LoadBalancerType loadBalancerType)
specifier|public
name|void
name|setLoadBalancerType
parameter_list|(
name|LoadBalancerType
name|loadBalancerType
parameter_list|)
block|{
name|this
operator|.
name|loadBalancerType
operator|=
name|loadBalancerType
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
annotation|@
name|Override
DECL|method|configureLoadBalancer (LoadBalancer loadBalancer)
specifier|protected
name|void
name|configureLoadBalancer
parameter_list|(
name|LoadBalancer
name|loadBalancer
parameter_list|)
block|{
name|ExpressionType
name|expression
init|=
name|getExpressionType
argument_list|()
decl_stmt|;
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|loadBalancer
argument_list|,
literal|"correlationExpression"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
name|LoadBalancerType
name|type
init|=
name|getLoadBalancerType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|loadBalancer
argument_list|,
literal|"loadBalancer"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
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
literal|"StickyLoadBalanceStrategy["
operator|+
name|expressionType
operator|+
literal|", "
operator|+
name|loadBalancerType
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

