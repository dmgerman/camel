begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|model
operator|.
name|ExpressionNodeHelper
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
name|ExpressionSubElementDefinition
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
name|language
operator|.
name|ExpressionDefinition
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

begin_comment
comment|/**  * Sticky load balancer Sticky load balancing using an Expression to calculate a  * correlation key to perform the sticky load balancing; rather like jsessionid  * in the web or JMSXGroupID in JMS.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,routing,loadbalance"
argument_list|)
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
DECL|class|StickyLoadBalancerDefinition
specifier|public
class|class
name|StickyLoadBalancerDefinition
extends|extends
name|LoadBalancerDefinition
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"correlationExpression"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|field|correlationExpression
specifier|private
name|ExpressionSubElementDefinition
name|correlationExpression
decl_stmt|;
DECL|method|StickyLoadBalancerDefinition ()
specifier|public
name|StickyLoadBalancerDefinition
parameter_list|()
block|{     }
DECL|method|getCorrelationExpression ()
specifier|public
name|ExpressionSubElementDefinition
name|getCorrelationExpression
parameter_list|()
block|{
return|return
name|correlationExpression
return|;
block|}
comment|/**      * The correlation expression to use to calculate the correlation key      */
DECL|method|setCorrelationExpression (ExpressionSubElementDefinition correlationExpression)
specifier|public
name|void
name|setCorrelationExpression
parameter_list|(
name|ExpressionSubElementDefinition
name|correlationExpression
parameter_list|)
block|{
name|this
operator|.
name|correlationExpression
operator|=
name|correlationExpression
expr_stmt|;
block|}
DECL|method|setCorrelationExpression (Expression expression)
specifier|public
name|void
name|setCorrelationExpression
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|ExpressionDefinition
name|def
init|=
name|ExpressionNodeHelper
operator|.
name|toExpressionDefinition
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|this
operator|.
name|correlationExpression
operator|=
operator|new
name|ExpressionSubElementDefinition
argument_list|()
expr_stmt|;
name|this
operator|.
name|correlationExpression
operator|.
name|setExpressionType
argument_list|(
name|def
argument_list|)
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
literal|"StickyLoadBalancer["
operator|+
name|correlationExpression
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

