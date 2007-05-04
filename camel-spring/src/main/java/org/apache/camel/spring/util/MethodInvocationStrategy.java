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

begin_comment
comment|/**  * A strategy for invoking a method on a pojo from a message exchange  *  * @version $Revision: $  */
end_comment

begin_interface
DECL|interface|MethodInvocationStrategy
specifier|public
interface|interface
name|MethodInvocationStrategy
block|{
comment|/**      * Creates an invocation on the given POJO using annotations to decide which method to invoke      * and to figure out which parameters to use      */
DECL|method|createInvocation (Object pojo, BeanInfo beanInfo, Exchange messageExchange, Endpoint pojoEndpoint)
name|MethodInvocation
name|createInvocation
parameter_list|(
name|Object
name|pojo
parameter_list|,
name|BeanInfo
name|beanInfo
parameter_list|,
name|Exchange
name|messageExchange
parameter_list|,
name|Endpoint
name|pojoEndpoint
parameter_list|)
throws|throws
name|RuntimeCamelException
function_decl|;
DECL|method|getDefaultParameterTypeExpression (Class parameterType)
name|Expression
name|getDefaultParameterTypeExpression
parameter_list|(
name|Class
name|parameterType
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

