begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nagios
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nagios
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Producer
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
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|MessagePayload
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|NagiosPassiveCheckSender
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|PassiveCheckSender
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|times
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|NagiosTest
specifier|public
class|class
name|NagiosTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Mock
DECL|field|nagiosPassiveCheckSender
specifier|protected
specifier|static
name|PassiveCheckSender
name|nagiosPassiveCheckSender
decl_stmt|;
DECL|field|canRun
specifier|protected
name|boolean
name|canRun
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setSender ()
specifier|public
specifier|static
name|void
name|setSender
parameter_list|()
block|{
name|nagiosPassiveCheckSender
operator|=
name|Mockito
operator|.
name|mock
argument_list|(
name|NagiosPassiveCheckSender
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
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
name|canRun
operator|=
literal|true
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendToNagios ()
specifier|public
name|void
name|testSendToNagios
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canRun
condition|)
block|{
return|return;
block|}
name|MessagePayload
name|expectedPayload
init|=
operator|new
name|MessagePayload
argument_list|(
literal|"localhost"
argument_list|,
name|Level
operator|.
name|OK
argument_list|,
name|context
operator|.
name|getName
argument_list|()
argument_list|,
literal|"Hello Nagios"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|allMessages
argument_list|()
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Nagios"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello Nagios"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|nagiosPassiveCheckSender
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|send
argument_list|(
name|expectedPayload
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendTwoToNagios ()
specifier|public
name|void
name|testSendTwoToNagios
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canRun
condition|)
block|{
return|return;
block|}
name|MessagePayload
name|expectedPayload1
init|=
operator|new
name|MessagePayload
argument_list|(
literal|"localhost"
argument_list|,
name|Level
operator|.
name|OK
argument_list|,
name|context
operator|.
name|getName
argument_list|()
argument_list|,
literal|"Hello Nagios"
argument_list|)
decl_stmt|;
name|MessagePayload
name|expectedPayload2
init|=
operator|new
name|MessagePayload
argument_list|(
literal|"localhost"
argument_list|,
name|Level
operator|.
name|OK
argument_list|,
name|context
operator|.
name|getName
argument_list|()
argument_list|,
literal|"Bye Nagios"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mock
operator|.
name|allMessages
argument_list|()
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Nagios"
argument_list|,
literal|"Bye Nagios"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello Nagios"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Bye Nagios"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|nagiosPassiveCheckSender
argument_list|)
operator|.
name|send
argument_list|(
name|expectedPayload1
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|nagiosPassiveCheckSender
argument_list|)
operator|.
name|send
argument_list|(
name|expectedPayload2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendToNagiosWarn ()
specifier|public
name|void
name|testSendToNagiosWarn
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canRun
condition|)
block|{
return|return;
block|}
name|MessagePayload
name|expectedPayload1
init|=
operator|new
name|MessagePayload
argument_list|(
literal|"localhost"
argument_list|,
name|Level
operator|.
name|WARNING
argument_list|,
name|context
operator|.
name|getName
argument_list|()
argument_list|,
literal|"Hello Nagios"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Nagios"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello Nagios"
argument_list|,
name|NagiosConstants
operator|.
name|LEVEL
argument_list|,
name|Level
operator|.
name|WARNING
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|nagiosPassiveCheckSender
argument_list|)
operator|.
name|send
argument_list|(
name|expectedPayload1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendToNagiosWarnAsText ()
specifier|public
name|void
name|testSendToNagiosWarnAsText
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canRun
condition|)
block|{
return|return;
block|}
name|MessagePayload
name|expectedPayload1
init|=
operator|new
name|MessagePayload
argument_list|(
literal|"localhost"
argument_list|,
name|Level
operator|.
name|WARNING
argument_list|,
name|context
operator|.
name|getName
argument_list|()
argument_list|,
literal|"Hello Nagios"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Nagios"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello Nagios"
argument_list|,
name|NagiosConstants
operator|.
name|LEVEL
argument_list|,
literal|"WARNING"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|nagiosPassiveCheckSender
argument_list|)
operator|.
name|send
argument_list|(
name|expectedPayload1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendToNagiosMultiHeaders ()
specifier|public
name|void
name|testSendToNagiosMultiHeaders
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canRun
condition|)
block|{
return|return;
block|}
name|MessagePayload
name|expectedPayload1
init|=
operator|new
name|MessagePayload
argument_list|(
literal|"myHost"
argument_list|,
name|Level
operator|.
name|CRITICAL
argument_list|,
literal|"myService"
argument_list|,
literal|"Hello Nagios"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Nagios"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|NagiosConstants
operator|.
name|LEVEL
argument_list|,
literal|"CRITICAL"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|NagiosConstants
operator|.
name|HOST_NAME
argument_list|,
literal|"myHost"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|NagiosConstants
operator|.
name|SERVICE_NAME
argument_list|,
literal|"myService"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello Nagios"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|nagiosPassiveCheckSender
argument_list|)
operator|.
name|send
argument_list|(
name|expectedPayload1
argument_list|)
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
name|String
name|uri
init|=
literal|"nagios:127.0.0.1:25664?password=secret"
decl_stmt|;
name|NagiosComponent
name|nagiosComponent
init|=
operator|new
name|NagiosComponent
argument_list|()
decl_stmt|;
name|nagiosComponent
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|NagiosEndpoint
name|nagiousEndpoint
init|=
operator|(
name|NagiosEndpoint
operator|)
name|nagiosComponent
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|nagiousEndpoint
operator|.
name|setSender
argument_list|(
name|nagiosPassiveCheckSender
argument_list|)
expr_stmt|;
name|Producer
name|nagiosProducer
init|=
name|nagiousEndpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
name|nagiousEndpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

