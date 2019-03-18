begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client.messaging
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|messaging
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
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|AtomixClientConfiguration
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
name|UriParam
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
name|UriParams
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|AtomixMessagingConfiguration
specifier|public
class|class
name|AtomixMessagingConfiguration
extends|extends
name|AtomixClientConfiguration
block|{
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"DIRECT"
argument_list|)
DECL|field|defaultAction
specifier|private
name|AtomixMessaging
operator|.
name|Action
name|defaultAction
init|=
name|AtomixMessaging
operator|.
name|Action
operator|.
name|DIRECT
decl_stmt|;
annotation|@
name|UriParam
DECL|field|memberName
specifier|private
name|String
name|memberName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|channelName
specifier|private
name|String
name|channelName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"ALL"
argument_list|)
DECL|field|broadcastType
specifier|private
name|AtomixMessaging
operator|.
name|BroadcastType
name|broadcastType
init|=
name|AtomixMessaging
operator|.
name|BroadcastType
operator|.
name|ALL
decl_stmt|;
comment|// ****************************************
comment|// Properties
comment|// ****************************************
DECL|method|getDefaultAction ()
specifier|public
name|AtomixMessaging
operator|.
name|Action
name|getDefaultAction
parameter_list|()
block|{
return|return
name|defaultAction
return|;
block|}
comment|/**      * The default action.      */
DECL|method|setDefaultAction (AtomixMessaging.Action defaultAction)
specifier|public
name|void
name|setDefaultAction
parameter_list|(
name|AtomixMessaging
operator|.
name|Action
name|defaultAction
parameter_list|)
block|{
name|this
operator|.
name|defaultAction
operator|=
name|defaultAction
expr_stmt|;
block|}
DECL|method|getMemberName ()
specifier|public
name|String
name|getMemberName
parameter_list|()
block|{
return|return
name|memberName
return|;
block|}
comment|/**      * The Atomix Group member name      */
DECL|method|setMemberName (String memberName)
specifier|public
name|void
name|setMemberName
parameter_list|(
name|String
name|memberName
parameter_list|)
block|{
name|this
operator|.
name|memberName
operator|=
name|memberName
expr_stmt|;
block|}
DECL|method|getChannelName ()
specifier|public
name|String
name|getChannelName
parameter_list|()
block|{
return|return
name|channelName
return|;
block|}
comment|/**      * The messaging channel name      */
DECL|method|setChannelName (String channelName)
specifier|public
name|void
name|setChannelName
parameter_list|(
name|String
name|channelName
parameter_list|)
block|{
name|this
operator|.
name|channelName
operator|=
name|channelName
expr_stmt|;
block|}
DECL|method|getBroadcastType ()
specifier|public
name|AtomixMessaging
operator|.
name|BroadcastType
name|getBroadcastType
parameter_list|()
block|{
return|return
name|broadcastType
return|;
block|}
comment|/**      * The broadcast type.      */
DECL|method|setBroadcastType (AtomixMessaging.BroadcastType broadcastType)
specifier|public
name|void
name|setBroadcastType
parameter_list|(
name|AtomixMessaging
operator|.
name|BroadcastType
name|broadcastType
parameter_list|)
block|{
name|this
operator|.
name|broadcastType
operator|=
name|broadcastType
expr_stmt|;
block|}
comment|// ****************************************
comment|// Copy
comment|// ****************************************
DECL|method|copy ()
specifier|public
name|AtomixMessagingConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|AtomixMessagingConfiguration
operator|)
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

