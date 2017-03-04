begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sip
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sip
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|sip
operator|.
name|message
operator|.
name|Request
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
name|Produce
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
name|ProducerTemplate
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
name|mock
operator|.
name|MockEndpoint
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
name|test
operator|.
name|AvailablePortFinder
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
DECL|class|PublishSubscribeTest
specifier|public
class|class
name|PublishSubscribeTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|port1
specifier|private
name|int
name|port1
decl_stmt|;
DECL|field|port2
specifier|private
name|int
name|port2
decl_stmt|;
DECL|field|port3
specifier|private
name|int
name|port3
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:neverland"
argument_list|)
DECL|field|unreachableEndpoint
specifier|private
name|MockEndpoint
name|unreachableEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:notification"
argument_list|)
DECL|field|resultEndpoint
specifier|private
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|producerTemplate
specifier|private
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|port1
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|17189
argument_list|)
expr_stmt|;
name|port2
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
name|port1
operator|+
literal|1
argument_list|)
expr_stmt|;
name|port3
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
name|port2
operator|+
literal|1
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPresenceAgentBasedPubSub ()
specifier|public
name|void
name|testPresenceAgentBasedPubSub
parameter_list|()
throws|throws
name|Exception
block|{
name|unreachableEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|producerTemplate
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"sip://agent@localhost:"
operator|+
name|port1
operator|+
literal|"?stackName=client&eventHeaderName=evtHdrName&eventId=evtid&fromUser=user2&fromHost=localhost&fromPort="
operator|+
name|port3
argument_list|,
literal|"EVENT_A"
argument_list|,
literal|"REQUEST_METHOD"
argument_list|,
name|Request
operator|.
name|PUBLISH
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create PresenceAgent
name|fromF
argument_list|(
literal|"sip://agent@localhost:%s?stackName=PresenceAgent&presenceAgent=true&eventHeaderName=evtHdrName&eventId=evtid"
argument_list|,
name|port1
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:neverland"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:neverland"
argument_list|)
expr_stmt|;
name|fromF
argument_list|(
literal|"sip://johndoe@localhost:%s?stackName=Subscriber&toUser=agent&toHost=localhost&toPort=%s&eventHeaderName=evtHdrName&eventId=evtid"
argument_list|,
name|port2
argument_list|,
name|port1
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:notification"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:notification"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

