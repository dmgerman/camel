begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.async
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|async
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
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
name|jms
operator|.
name|JmsComponent
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
name|itest
operator|.
name|CamelJmsTestHelper
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
name|Registry
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
name|support
operator|.
name|SimpleRegistry
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
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|JmsComponent
operator|.
name|jmsComponentAutoAcknowledge
import|;
end_import

begin_class
DECL|class|HttpAsyncDslTest
specifier|public
class|class
name|HttpAsyncDslTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|order
specifier|private
specifier|static
specifier|volatile
name|String
name|order
init|=
literal|""
decl_stmt|;
annotation|@
name|Test
DECL|method|testRequestOnly ()
specifier|public
name|void
name|testRequestOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:validate"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// even though its request only the message is still continued being processed
name|getMockEndpoint
argument_list|(
literal|"mock:order"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"jms:queue:order"
argument_list|,
literal|"Order: Camel in Action"
argument_list|)
expr_stmt|;
name|order
operator|+=
literal|"C"
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// B should be last (either ABC or BAC depending on threading)
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|order
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|order
operator|.
name|endsWith
argument_list|(
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRequestReply ()
specifier|public
name|void
name|testRequestReply
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:validate"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// even though its request only the message is still continued being processed
name|getMockEndpoint
argument_list|(
literal|"mock:order"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|String
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"jms:queue:order"
argument_list|,
literal|"Order: Camel in Action"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|order
operator|+=
literal|"C"
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// should be in strict ABC order as we do request/reply
name|assertEquals
argument_list|(
literal|"ABC"
argument_list|,
name|order
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Order OK"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|order
operator|=
literal|""
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelRegistry ()
specifier|protected
name|Registry
name|createCamelRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|Registry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"validateOrder"
argument_list|,
operator|new
name|MyValidateOrderBean
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"handleOrder"
argument_list|,
operator|new
name|MyHandleOrderBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Override
DECL|method|bindToRegistry (Registry registry)
specifier|protected
name|void
name|bindToRegistry
parameter_list|(
name|Registry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
comment|// add ActiveMQ with embedded broker
name|ConnectionFactory
name|connectionFactory
init|=
name|CamelJmsTestHelper
operator|.
name|createConnectionFactory
argument_list|()
decl_stmt|;
name|JmsComponent
name|amq
init|=
name|jmsComponentAutoAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
decl_stmt|;
name|amq
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"jms"
argument_list|,
name|amq
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
comment|// START SNIPPET: e1
comment|// list on the JMS queue for new orders
name|from
argument_list|(
literal|"jms:queue:order"
argument_list|)
comment|// do some sanity check validation
operator|.
name|to
argument_list|(
literal|"bean:validateOrder"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:validate"
argument_list|)
comment|// use multi threading with a pool size of 20
comment|// turn the route async as some others do not expect a reply
comment|// and a few does then we can use the threads DSL as a turning point
comment|// if the JMS ReplyTo was set then we expect a reply, otherwise not
comment|// use a pool of 20 threads for the point forward
operator|.
name|threads
argument_list|(
literal|20
argument_list|)
comment|// do some CPU heavy processing of the message (we simulate and delay just 500 ms)
operator|.
name|delay
argument_list|(
literal|500
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:handleOrder"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:order"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
DECL|class|MyValidateOrderBean
specifier|public
specifier|static
class|class
name|MyValidateOrderBean
block|{
DECL|method|validateOrder (byte[] payload)
specifier|public
name|void
name|validateOrder
parameter_list|(
name|byte
index|[]
name|payload
parameter_list|)
block|{
name|order
operator|+=
literal|"A"
expr_stmt|;
comment|// noop
block|}
block|}
DECL|class|MyHandleOrderBean
specifier|public
specifier|static
class|class
name|MyHandleOrderBean
block|{
DECL|method|handleOrder (String message)
specifier|public
name|String
name|handleOrder
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|order
operator|+=
literal|"B"
expr_stmt|;
return|return
literal|"Order OK"
return|;
comment|// noop
block|}
block|}
block|}
end_class

end_unit

