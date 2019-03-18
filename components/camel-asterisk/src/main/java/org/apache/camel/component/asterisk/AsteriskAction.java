begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.asterisk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|asterisk
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
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
name|asteriskjava
operator|.
name|manager
operator|.
name|action
operator|.
name|ExtensionStateAction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|action
operator|.
name|ManagerAction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|action
operator|.
name|QueueStatusAction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|action
operator|.
name|SipPeersAction
import|;
end_import

begin_enum
DECL|enum|AsteriskAction
specifier|public
enum|enum
name|AsteriskAction
implements|implements
name|Function
argument_list|<
name|Exchange
argument_list|,
name|ManagerAction
argument_list|>
block|{
DECL|enumConstant|QUEUE_STATUS
name|QUEUE_STATUS
block|{
annotation|@
name|Override
specifier|public
name|ManagerAction
name|apply
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|new
name|QueueStatusAction
argument_list|()
return|;
block|}
block|}
block|,
DECL|enumConstant|SIP_PEERS
name|SIP_PEERS
block|{
annotation|@
name|Override
specifier|public
name|ManagerAction
name|apply
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|new
name|SipPeersAction
argument_list|()
return|;
block|}
block|}
block|,
DECL|enumConstant|EXTENSION_STATE
name|EXTENSION_STATE
block|{
annotation|@
name|Override
specifier|public
name|ManagerAction
name|apply
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|new
name|ExtensionStateAction
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|AsteriskConstants
operator|.
name|EXTENSION
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|AsteriskConstants
operator|.
name|CONTEXT
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_enum

end_unit

