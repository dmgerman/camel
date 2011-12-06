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
name|ManagedOperation
import|;
end_import

begin_interface
DECL|interface|ManagedBrowsableEndpointMBean
specifier|public
interface|interface
name|ManagedBrowsableEndpointMBean
block|{
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Current number of Exchanges in Queue"
argument_list|)
DECL|method|queueSize ()
name|long
name|queueSize
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Get Exchange from queue by index"
argument_list|)
DECL|method|browseExchange (Integer index)
name|String
name|browseExchange
parameter_list|(
name|Integer
name|index
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Get message body from queue by index"
argument_list|)
DECL|method|browseMessageBody (Integer index)
name|String
name|browseMessageBody
parameter_list|(
name|Integer
name|index
parameter_list|)
function_decl|;
comment|/**      * @deprecated use {@link #browseAllMessagesAsXml(Boolean)} instead      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Get message as XML from queue by index"
argument_list|)
annotation|@
name|Deprecated
DECL|method|browseMessageAsXml (Integer index)
name|String
name|browseMessageAsXml
parameter_list|(
name|Integer
name|index
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Get message as XML from queue by index"
argument_list|)
DECL|method|browseMessageAsXml (Integer index, Boolean includeBody)
name|String
name|browseMessageAsXml
parameter_list|(
name|Integer
name|index
parameter_list|,
name|Boolean
name|includeBody
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Gets all the messages as XML from the queue"
argument_list|)
DECL|method|browseAllMessagesAsXml (Boolean includeBody)
name|String
name|browseAllMessagesAsXml
parameter_list|(
name|Boolean
name|includeBody
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Gets the range of messages as XML from the queue"
argument_list|)
DECL|method|browseRangeMessagesAsXml (Integer fromIndex, Integer toIndex, Boolean includeBody)
name|String
name|browseRangeMessagesAsXml
parameter_list|(
name|Integer
name|fromIndex
parameter_list|,
name|Integer
name|toIndex
parameter_list|,
name|Boolean
name|includeBody
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

