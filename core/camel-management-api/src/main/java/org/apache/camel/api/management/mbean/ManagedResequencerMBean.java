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

begin_interface
DECL|interface|ManagedResequencerMBean
specifier|public
interface|interface
name|ManagedResequencerMBean
extends|extends
name|ManagedProcessorMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Expression to use for re-ordering the messages, such as a header with a sequence number"
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
literal|"The size of the batch to be re-ordered. The default size is 100."
argument_list|)
DECL|method|getBatchSize ()
name|Integer
name|getBatchSize
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Minimum time to wait for missing elements (messages)."
argument_list|)
DECL|method|getTimeout ()
name|Long
name|getTimeout
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to allow duplicates."
argument_list|)
DECL|method|isAllowDuplicates ()
name|Boolean
name|isAllowDuplicates
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to reverse the ordering."
argument_list|)
DECL|method|isReverse ()
name|Boolean
name|isReverse
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to ignore invalid exchanges"
argument_list|)
DECL|method|isIgnoreInvalidExchanges ()
name|Boolean
name|isIgnoreInvalidExchanges
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The capacity of the resequencer's inbound queue"
argument_list|)
DECL|method|getCapacity ()
name|Integer
name|getCapacity
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"If true, throws an exception when messages older than the last delivered message are processed"
argument_list|)
DECL|method|isRejectOld ()
name|Boolean
name|isRejectOld
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

