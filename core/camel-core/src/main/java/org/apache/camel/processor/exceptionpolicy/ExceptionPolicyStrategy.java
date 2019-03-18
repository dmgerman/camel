begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.exceptionpolicy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|exceptionpolicy
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
name|model
operator|.
name|OnExceptionDefinition
import|;
end_import

begin_comment
comment|/**  * A strategy to determine which {@link org.apache.camel.model.OnExceptionDefinition} should handle the thrown  * exception.  *  * @see org.apache.camel.processor.exceptionpolicy.DefaultExceptionPolicyStrategy DefaultExceptionPolicy  */
end_comment

begin_interface
DECL|interface|ExceptionPolicyStrategy
specifier|public
interface|interface
name|ExceptionPolicyStrategy
block|{
comment|/**      * Resolves the {@link org.apache.camel.model.OnExceptionDefinition} that should handle the thrown exception.      *      * @param exceptionPolicies the configured exception policies to resolve from      * @param exchange           the exchange      * @param exception          the exception that was thrown      * @return the resolved exception type to handle this exception,<tt>null</tt> if none found.      */
DECL|method|getExceptionPolicy (Map<ExceptionPolicyKey, OnExceptionDefinition> exceptionPolicies, Exchange exchange, Throwable exception)
name|OnExceptionDefinition
name|getExceptionPolicy
parameter_list|(
name|Map
argument_list|<
name|ExceptionPolicyKey
argument_list|,
name|OnExceptionDefinition
argument_list|>
name|exceptionPolicies
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|exception
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

