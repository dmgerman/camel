begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.transport
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|transport
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|Processor
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
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageImpl
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
DECL|class|CamelConduitTest
specifier|public
class|class
name|CamelConduitTest
extends|extends
name|CamelTransportTestSupport
block|{
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
literal|"direct:Producer"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:EndpointA"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|Object
name|result
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|DefaultCamelContext
argument_list|()
return|;
block|}
annotation|@
name|Test
DECL|method|testCamelConduitConfiguration ()
specifier|public
name|void
name|testCamelConduitConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|testEndpointQNameA
init|=
operator|new
name|QName
argument_list|(
literal|"http://camel.apache.org/camel-test"
argument_list|,
literal|"portA"
argument_list|)
decl_stmt|;
name|QName
name|testEndpointQNameB
init|=
operator|new
name|QName
argument_list|(
literal|"http://camel.apache.org/camel-test"
argument_list|,
literal|"portB"
argument_list|)
decl_stmt|;
comment|// set up the bus with configure file
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
literal|"/org/apache/camel/component/cxf/transport/CamelConduit.xml"
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
comment|// create the conduit and set the configuration with it
name|endpointInfo
operator|.
name|setAddress
argument_list|(
literal|"camel://direct:EndpointA"
argument_list|)
expr_stmt|;
name|endpointInfo
operator|.
name|setName
argument_list|(
name|testEndpointQNameA
argument_list|)
expr_stmt|;
name|CamelConduit
name|conduit
init|=
operator|new
name|CamelConduit
argument_list|(
literal|null
argument_list|,
name|bus
argument_list|,
name|endpointInfo
argument_list|)
decl_stmt|;
name|CamelContext
name|context
init|=
name|conduit
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"the camel context which get from camel conduit is not null"
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"get the wrong camel context"
argument_list|,
name|context
operator|.
name|getName
argument_list|()
argument_list|,
literal|"conduit_context"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
literal|"direct:EndpointA"
argument_list|)
expr_stmt|;
name|endpointInfo
operator|.
name|setAddress
argument_list|(
literal|"camel://direct:EndpointC"
argument_list|)
expr_stmt|;
name|endpointInfo
operator|.
name|setName
argument_list|(
name|testEndpointQNameB
argument_list|)
expr_stmt|;
name|conduit
operator|=
operator|new
name|CamelConduit
argument_list|(
literal|null
argument_list|,
name|bus
argument_list|,
name|endpointInfo
argument_list|)
expr_stmt|;
name|context
operator|=
name|conduit
operator|.
name|getCamelContext
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"the camel context which get from camel conduit is not null"
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"get the wrong camel context"
argument_list|,
name|context
operator|.
name|getName
argument_list|()
argument_list|,
literal|"context"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
literal|"direct:EndpointC"
argument_list|)
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPrepareSend ()
specifier|public
name|void
name|testPrepareSend
parameter_list|()
throws|throws
name|Exception
block|{
name|endpointInfo
operator|.
name|setAddress
argument_list|(
literal|"camel://direct:Producer"
argument_list|)
expr_stmt|;
name|CamelConduit
name|conduit
init|=
name|setupCamelConduit
argument_list|(
name|endpointInfo
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
try|try
block|{
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|verifyMessageContent
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|verifyMessageContent (Message message)
specifier|public
name|void
name|verifyMessageContent
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|OutputStream
name|os
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"OutputStream should not be null"
argument_list|,
name|os
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendOut ()
specifier|public
name|void
name|testSendOut
parameter_list|()
throws|throws
name|Exception
block|{
name|endpointInfo
operator|.
name|setAddress
argument_list|(
literal|"camel://direct:Producer"
argument_list|)
expr_stmt|;
name|CamelConduit
name|conduit
init|=
name|setupCamelConduit
argument_list|(
name|endpointInfo
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:EndpointA"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
comment|// set the isOneWay to be true
name|sendoutMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
literal|true
argument_list|,
literal|"HelloWorld"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// verify the endpoint get the response
block|}
annotation|@
name|Test
DECL|method|testSendOutRunTrip ()
specifier|public
name|void
name|testSendOutRunTrip
parameter_list|()
throws|throws
name|Exception
block|{
name|endpointInfo
operator|.
name|setAddress
argument_list|(
literal|"camel://direct:Producer"
argument_list|)
expr_stmt|;
name|CamelConduit
name|conduit
init|=
name|setupCamelConduit
argument_list|(
name|endpointInfo
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:EndpointA"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
comment|// set the isOneWay to be false
name|sendoutMessage
argument_list|(
name|conduit
argument_list|,
name|message
argument_list|,
literal|false
argument_list|,
literal|"HelloWorld"
argument_list|)
expr_stmt|;
comment|// verify the endpoint get the response
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|verifyReceivedMessage
argument_list|(
literal|"HelloWorld"
argument_list|)
expr_stmt|;
block|}
DECL|method|verifyReceivedMessage (String content)
specifier|public
name|void
name|verifyReceivedMessage
parameter_list|(
name|String
name|content
parameter_list|)
block|{
name|ByteArrayInputStream
name|bis
init|=
operator|(
name|ByteArrayInputStream
operator|)
name|inMessage
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|byte
name|bytes
index|[]
init|=
operator|new
name|byte
index|[
name|bis
operator|.
name|available
argument_list|()
index|]
decl_stmt|;
try|try
block|{
name|bis
operator|.
name|read
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|String
name|reponse
init|=
operator|new
name|String
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The reponse date should be equals"
argument_list|,
name|content
argument_list|,
name|reponse
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

