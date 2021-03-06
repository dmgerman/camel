begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client
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
package|;
end_package

begin_class
DECL|class|AtomixClientConstants
specifier|public
specifier|final
class|class
name|AtomixClientConstants
block|{
DECL|field|RESOURCE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_NAME
init|=
literal|"CamelAtomixResourceName"
decl_stmt|;
DECL|field|RESOURCE_ACTION
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_ACTION
init|=
literal|"CamelAtomixResourceAction"
decl_stmt|;
DECL|field|RESOURCE_KEY
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_KEY
init|=
literal|"CamelAtomixResourceKey"
decl_stmt|;
DECL|field|RESOURCE_VALUE
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_VALUE
init|=
literal|"CamelAtomixResourceValue"
decl_stmt|;
DECL|field|RESOURCE_DEFAULT_VALUE
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_DEFAULT_VALUE
init|=
literal|"CamelAtomixResourceDefaultValue"
decl_stmt|;
DECL|field|RESOURCE_OLD_VALUE
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_OLD_VALUE
init|=
literal|"CamelAtomixResourceOldValue"
decl_stmt|;
DECL|field|RESOURCE_ACTION_HAS_RESULT
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_ACTION_HAS_RESULT
init|=
literal|"CamelAtomixResourceActionHasResult"
decl_stmt|;
DECL|field|RESOURCE_TTL
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_TTL
init|=
literal|"CamelAtomixResourceTTL"
decl_stmt|;
DECL|field|RESOURCE_READ_CONSISTENCY
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_READ_CONSISTENCY
init|=
literal|"CamelAtomixResourceReadConsistency"
decl_stmt|;
DECL|field|EVENT_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|EVENT_TYPE
init|=
literal|"CamelAtomixEventType"
decl_stmt|;
DECL|field|MESSAGE_ID
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE_ID
init|=
literal|"CamelAtomixEventType"
decl_stmt|;
DECL|field|MEMBER_NAME
specifier|public
specifier|static
specifier|final
name|String
name|MEMBER_NAME
init|=
literal|"CamelAtomixMemberName"
decl_stmt|;
DECL|field|CHANNEL_NAME
specifier|public
specifier|static
specifier|final
name|String
name|CHANNEL_NAME
init|=
literal|"CamelAtomixChannelName"
decl_stmt|;
DECL|field|BROADCAST_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|BROADCAST_TYPE
init|=
literal|"CamelAtomixBroadcastType"
decl_stmt|;
DECL|method|AtomixClientConstants ()
specifier|private
name|AtomixClientConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

