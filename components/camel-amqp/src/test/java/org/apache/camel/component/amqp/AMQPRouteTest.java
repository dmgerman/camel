begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.amqp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|amqp
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
name|CamelContext
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
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|SerializationUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|qpid
operator|.
name|client
operator|.
name|transport
operator|.
name|TransportConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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
name|Ignore
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
name|amqp
operator|.
name|AMQPComponent
operator|.
name|amqpComponent
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_comment
comment|//@Ignore("AMQP testing is a bit unstable")
end_comment

begin_class
DECL|class|AMQPRouteTest
specifier|public
class|class
name|AMQPRouteTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|startBroker ()
specifier|public
specifier|static
name|void
name|startBroker
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create an in-JVM broker
try|try
block|{
name|TransportConnection
operator|.
name|createVMBroker
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// fails the first time, so create it again
name|TransportConnection
operator|.
name|createVMBroker
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|AfterClass
DECL|method|shutdownBroker ()
specifier|public
specifier|static
name|void
name|shutdownBroker
parameter_list|()
block|{
name|TransportConnection
operator|.
name|killVMBroker
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJmsRouteWithTextMessage ()
specifier|public
name|void
name|testJmsRouteWithTextMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedBody
init|=
literal|"Hello there!"
decl_stmt|;
name|boolean
name|windows
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Windows"
argument_list|)
decl_stmt|;
if|if
condition|(
name|windows
condition|)
block|{
comment|// it could sometimes send it twice so we expect at least 1 msg
name|resultEndpoint
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
block|}
name|resultEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"cheese"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|sendExchange
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
if|if
condition|(
name|windows
condition|)
block|{
comment|// send the message twice to walk around the AMQP's drop first message issue on Windows box
name|sendExchange
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
block|}
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJmsRouteWithObjectMessage ()
specifier|public
name|void
name|testJmsRouteWithObjectMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|PurchaseOrder
name|expectedBody
init|=
operator|new
name|PurchaseOrder
argument_list|(
literal|"Beer"
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"cheese"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|sendExchange
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJmsRouteWithByteArrayMessage ()
specifier|public
name|void
name|testJmsRouteWithByteArrayMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|PurchaseOrder
name|aPO
init|=
operator|new
name|PurchaseOrder
argument_list|(
literal|"Beer"
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|byte
index|[]
name|expectedBody
init|=
name|SerializationUtils
operator|.
name|serialize
argument_list|(
name|aPO
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"cheese"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|sendExchange
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|sendExchange (final Object expectedBody)
specifier|protected
name|void
name|sendExchange
parameter_list|(
specifier|final
name|Object
name|expectedBody
parameter_list|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"amqp:queue:test.a"
argument_list|,
name|expectedBody
argument_list|,
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
block|}
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"amqp"
argument_list|,
name|amqpComponent
argument_list|(
literal|"amqp://guest:guest@/test?brokerlist='vm://:1'"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"amqp:test.a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"amqp:test.b"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"amqp:test.b"
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

