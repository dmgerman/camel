begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.api.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
operator|.
name|management
operator|.
name|mbean
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
import|;
end_import

begin_interface
DECL|interface|ManagedIdempotentConsumerMBean
specifier|public
interface|interface
name|ManagedIdempotentConsumerMBean
extends|extends
name|ManagedProcessorMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The language for the expression"
argument_list|)
DECL|method|getExpressionLanguage ()
name|String
name|getExpressionLanguage
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Expression used to calculate the correlation key to use for duplicate check"
argument_list|)
DECL|method|getExpression ()
name|String
name|getExpression
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to eagerly add the key to the idempotent repository or wait until the exchange is complete"
argument_list|)
DECL|method|isEager ()
name|Boolean
name|isEager
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to complete the idempotent consumer eager or when the exchange is done"
argument_list|)
DECL|method|isCompletionEager ()
name|Boolean
name|isCompletionEager
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"whether to skip duplicates or not"
argument_list|)
DECL|method|isSkipDuplicate ()
name|Boolean
name|isSkipDuplicate
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"whether to remove or keep the key on failure"
argument_list|)
DECL|method|isRemoveOnFailure ()
name|Boolean
name|isRemoveOnFailure
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Current count of duplicate Messages"
argument_list|)
DECL|method|getDuplicateMessageCount ()
name|long
name|getDuplicateMessageCount
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Reset the current count of duplicate Messages"
argument_list|)
DECL|method|resetDuplicateMessageCount ()
name|void
name|resetDuplicateMessageCount
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Clear the repository containing Messages"
argument_list|)
DECL|method|clear ()
name|void
name|clear
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

