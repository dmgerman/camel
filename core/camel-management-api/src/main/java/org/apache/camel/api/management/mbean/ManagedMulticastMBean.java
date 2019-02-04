begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
DECL|interface|ManagedMulticastMBean
specifier|public
interface|interface
name|ManagedMulticastMBean
extends|extends
name|ManagedProcessorMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"If enabled then the aggregate method on AggregationStrategy can be called concurrently."
argument_list|)
DECL|method|isParallelAggregate ()
name|Boolean
name|isParallelAggregate
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"If enabled then sending messages to the multicasts occurs concurrently."
argument_list|)
DECL|method|isParallelProcessing ()
name|Boolean
name|isParallelProcessing
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"If enabled then Camel will process replies out-of-order, eg in the order they come back."
argument_list|)
DECL|method|isStreaming ()
name|Boolean
name|isStreaming
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Will now stop further processing if an exception or failure occurred during processing."
argument_list|)
DECL|method|isStopOnException ()
name|Boolean
name|isStopOnException
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Shares the UnitOfWork with the parent and the resource exchange"
argument_list|)
DECL|method|isShareUnitOfWork ()
name|Boolean
name|isShareUnitOfWork
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The total timeout specified in millis, when using parallel processing."
argument_list|)
DECL|method|getTimeout ()
name|Long
name|getTimeout
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

