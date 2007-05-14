begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|util
package|;
end_package

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
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|aopalliance
operator|.
name|intercept
operator|.
name|MethodInvocation
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
name|Endpoint
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
name|Message
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
name|builder
operator|.
name|ExpressionBuilder
import|;
end_import

begin_comment
comment|/**  * Represents the strategy used to figure out how to map a message exchange to a POJO method invocation  *  * @version $Revision:$  */
end_comment

begin_class
DECL|class|DefaultMethodInvocationStrategy
specifier|public
class|class
name|DefaultMethodInvocationStrategy
implements|implements
name|MethodInvocationStrategy
block|{
DECL|field|parameterTypeToExpressionMap
specifier|private
name|Map
argument_list|<
name|Class
argument_list|,
name|Expression
argument_list|>
name|parameterTypeToExpressionMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Class
argument_list|,
name|Expression
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|DefaultMethodInvocationStrategy ()
specifier|public
name|DefaultMethodInvocationStrategy
parameter_list|()
block|{     }
DECL|method|getDefaultParameterTypeExpression (Class parameterType)
specifier|public
specifier|synchronized
name|Expression
name|getDefaultParameterTypeExpression
parameter_list|(
name|Class
name|parameterType
parameter_list|)
block|{
return|return
name|parameterTypeToExpressionMap
operator|.
name|get
argument_list|(
name|parameterType
argument_list|)
return|;
block|}
comment|/**      * Adds a default parameter type mapping to an expression      */
DECL|method|addParameterMapping (Class parameterType, Expression expression)
specifier|public
specifier|synchronized
name|void
name|addParameterMapping
parameter_list|(
name|Class
name|parameterType
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|parameterTypeToExpressionMap
operator|.
name|put
argument_list|(
name|parameterType
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates an invocation on the given POJO using annotations to decide which method to invoke      * and to figure out which parameters to use      */
comment|/*     public MethodInvocation createInvocation(Object pojo,                                              BeanInfo beanInfo,                                               Exchange messageExchange,                                              Endpoint pojoEndpoint) throws RuntimeCamelException {         return beanInfo.createInvocation(pojo, messageExchange);     } */
DECL|method|loadDefaultRegistry ()
specifier|public
name|void
name|loadDefaultRegistry
parameter_list|()
block|{
name|addParameterMapping
argument_list|(
name|Exchange
operator|.
name|class
argument_list|,
name|ExpressionBuilder
operator|.
name|exchangeExpression
argument_list|()
argument_list|)
expr_stmt|;
name|addParameterMapping
argument_list|(
name|Message
operator|.
name|class
argument_list|,
name|ExpressionBuilder
operator|.
name|inMessageExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

