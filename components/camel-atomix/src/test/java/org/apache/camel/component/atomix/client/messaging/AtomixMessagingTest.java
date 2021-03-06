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
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

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
name|UUID
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
name|Component
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
name|EndpointInject
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
name|FluentProducerTemplate
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
name|RoutesBuilder
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
name|RouteBuilder
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
name|AtomixClientConstants
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
name|AtomixClientTestSupport
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
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|AtomixMessagingTest
specifier|public
class|class
name|AtomixMessagingTest
extends|extends
name|AtomixClientTestSupport
block|{
DECL|field|NODE_NAME
specifier|private
specifier|static
specifier|final
name|String
name|NODE_NAME
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"direct:start"
argument_list|)
DECL|field|template
specifier|private
name|FluentProducerTemplate
name|template
decl_stmt|;
comment|// ************************************
comment|// Setup
comment|// ************************************
annotation|@
name|Override
DECL|method|createComponents ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Component
argument_list|>
name|createComponents
parameter_list|()
block|{
name|AtomixMessagingComponent
name|component
init|=
operator|new
name|AtomixMessagingComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setNodes
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|getReplicaAddress
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"atomix-messaging"
argument_list|,
name|component
argument_list|)
return|;
block|}
comment|// ************************************
comment|// Test
comment|// ************************************
annotation|@
name|Test
DECL|method|testMessaging ()
specifier|public
name|void
name|testMessaging
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock1
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:member-1"
argument_list|)
decl_stmt|;
name|mock1
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mock1
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"direct-message"
argument_list|,
literal|"broadcast-message"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock2
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:member-2"
argument_list|)
decl_stmt|;
name|mock2
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock2
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"broadcast-message"
argument_list|)
expr_stmt|;
name|template
operator|.
name|clearAll
argument_list|()
operator|.
name|withHeader
argument_list|(
name|AtomixClientConstants
operator|.
name|RESOURCE_ACTION
argument_list|,
name|AtomixMessaging
operator|.
name|Action
operator|.
name|DIRECT
argument_list|)
operator|.
name|withHeader
argument_list|(
name|AtomixClientConstants
operator|.
name|MEMBER_NAME
argument_list|,
literal|"member-1"
argument_list|)
operator|.
name|withHeader
argument_list|(
name|AtomixClientConstants
operator|.
name|CHANNEL_NAME
argument_list|,
literal|"channel"
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"direct-message"
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
name|template
operator|.
name|clearAll
argument_list|()
operator|.
name|withHeader
argument_list|(
name|AtomixClientConstants
operator|.
name|RESOURCE_ACTION
argument_list|,
name|AtomixMessaging
operator|.
name|Action
operator|.
name|BROADCAST
argument_list|)
operator|.
name|withHeader
argument_list|(
name|AtomixClientConstants
operator|.
name|CHANNEL_NAME
argument_list|,
literal|"channel"
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"direct-message"
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
block|}
comment|// ************************************
comment|// Routes
comment|// ************************************
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"atomix-messaging:group"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"atomix-messaging:group?memberName=member-1&channelName=channel"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:member-1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"atomix-messaging:group?memberName=member-2&channelName=channel"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:member-2"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

