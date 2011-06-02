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
name|cxf
operator|.
name|transport
operator|.
name|CamelDestination
operator|.
name|ConsumerProcessor
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
name|camel
operator|.
name|impl
operator|.
name|DefaultExchange
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
name|Exchange
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
name|ExchangeImpl
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
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|transport
operator|.
name|Conduit
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
name|transport
operator|.
name|ConduitInitiator
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
name|transport
operator|.
name|ConduitInitiatorManager
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
name|transport
operator|.
name|MessageObserver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
DECL|class|CamelDestinationTest
specifier|public
class|class
name|CamelDestinationTest
extends|extends
name|CamelTransportTestSupport
block|{
DECL|field|destMessage
specifier|private
name|Message
name|destMessage
decl_stmt|;
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
name|onException
argument_list|(
name|RuntimeCamelException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:error"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:Producer"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:EndpointA"
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
DECL|method|testCamelDestinationConfiguration ()
specifier|public
name|void
name|testCamelDestinationConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|testEndpointQName
init|=
operator|new
name|QName
argument_list|(
literal|"http://camel.apache.org/camel-test"
argument_list|,
literal|"port"
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
literal|"/org/apache/camel/component/cxf/transport/CamelDestination.xml"
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
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
name|testEndpointQName
argument_list|)
expr_stmt|;
name|CamelDestination
name|destination
init|=
operator|new
name|CamelDestination
argument_list|(
literal|null
argument_list|,
name|bus
argument_list|,
literal|null
argument_list|,
name|endpointInfo
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"{http://camel.apache.org/camel-test}port.camel-destination"
argument_list|,
name|destination
operator|.
name|getBeanName
argument_list|()
argument_list|)
expr_stmt|;
name|CamelContext
name|context
init|=
name|destination
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The camel context which get from camel destination is not null"
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong camel context"
argument_list|,
name|context
operator|.
name|getName
argument_list|()
argument_list|,
literal|"dest_context"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The camel context should has two routers"
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|2
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
DECL|method|setupCamelDestination (EndpointInfo endpointInfo, boolean send)
specifier|public
name|CamelDestination
name|setupCamelDestination
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|,
name|boolean
name|send
parameter_list|)
throws|throws
name|IOException
block|{
name|ConduitInitiator
name|conduitInitiator
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ConduitInitiator
operator|.
name|class
argument_list|)
decl_stmt|;
name|CamelDestination
name|camelDestination
init|=
operator|new
name|CamelDestination
argument_list|(
name|context
argument_list|,
name|bus
argument_list|,
name|conduitInitiator
argument_list|,
name|endpointInfo
argument_list|)
decl_stmt|;
if|if
condition|(
name|send
condition|)
block|{
comment|// setMessageObserver
name|observer
operator|=
operator|new
name|MessageObserver
argument_list|()
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|destMessage
operator|=
name|m
expr_stmt|;
block|}
block|}
expr_stmt|;
name|camelDestination
operator|.
name|setMessageObserver
argument_list|(
name|observer
argument_list|)
expr_stmt|;
block|}
return|return
name|camelDestination
return|;
block|}
annotation|@
name|Test
DECL|method|testGetTransportFactoryFromBus ()
specifier|public
name|void
name|testGetTransportFactoryFromBus
parameter_list|()
throws|throws
name|Exception
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
operator|.
name|getConduitInitiator
argument_list|(
name|CamelTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOneWayDestination ()
specifier|public
name|void
name|testOneWayDestination
parameter_list|()
throws|throws
name|Exception
block|{
name|destMessage
operator|=
literal|null
expr_stmt|;
name|inMessage
operator|=
literal|null
expr_stmt|;
name|EndpointInfo
name|conduitEpInfo
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|conduitEpInfo
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
name|conduitEpInfo
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Message
name|outMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|CamelDestination
name|destination
init|=
literal|null
decl_stmt|;
try|try
block|{
name|endpointInfo
operator|.
name|setAddress
argument_list|(
literal|"camel://direct:EndpointA"
argument_list|)
expr_stmt|;
name|destination
operator|=
name|setupCamelDestination
argument_list|(
name|endpointInfo
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// destination.activate();
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|assertFalse
argument_list|(
literal|"The CamelDestination activate should not through exception "
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|sendoutMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
argument_list|,
literal|true
argument_list|,
literal|"HelloWorld"
argument_list|)
expr_stmt|;
comment|// just verify the Destination inMessage
name|assertTrue
argument_list|(
literal|"The destiantion should have got the message "
argument_list|,
name|destMessage
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|verifyReceivedMessage
argument_list|(
name|destMessage
argument_list|,
literal|"HelloWorld"
argument_list|)
expr_stmt|;
name|destination
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
DECL|method|verifyReceivedMessage (Message inMessage, String content)
specifier|private
name|void
name|verifyReceivedMessage
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|String
name|content
parameter_list|)
throws|throws
name|IOException
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
name|bis
operator|.
name|read
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
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
annotation|@
name|Test
DECL|method|testRoundTripDestination ()
specifier|public
name|void
name|testRoundTripDestination
parameter_list|()
throws|throws
name|Exception
block|{
name|inMessage
operator|=
literal|null
expr_stmt|;
name|EndpointInfo
name|conduitEpInfo
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|conduitEpInfo
operator|.
name|setAddress
argument_list|(
literal|"camel://direct:Producer"
argument_list|)
expr_stmt|;
comment|// set up the conduit send to be true
name|CamelConduit
name|conduit
init|=
name|setupCamelConduit
argument_list|(
name|conduitEpInfo
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
specifier|final
name|Message
name|outMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|endpointInfo
operator|.
name|setAddress
argument_list|(
literal|"camel://direct:EndpointA"
argument_list|)
expr_stmt|;
specifier|final
name|CamelDestination
name|destination
init|=
name|setupCamelDestination
argument_list|(
name|endpointInfo
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// set up MessageObserver for handlering the conduit message
name|MessageObserver
name|observer
init|=
operator|new
name|MessageObserver
argument_list|()
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
try|try
block|{
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verifyReceivedMessage
argument_list|(
name|m
argument_list|,
literal|"HelloWorld"
argument_list|)
expr_stmt|;
comment|//verifyHeaders(m, outMessage);
comment|// setup the message for
name|Conduit
name|backConduit
decl_stmt|;
name|backConduit
operator|=
name|destination
operator|.
name|getBackChannel
argument_list|(
name|m
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// wait for the message to be got from the conduit
name|Message
name|replyMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|sendoutMessage
argument_list|(
name|backConduit
argument_list|,
name|replyMessage
argument_list|,
literal|true
argument_list|,
literal|"HelloWorld Response"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
decl_stmt|;
name|MockEndpoint
name|error
init|=
operator|(
name|MockEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
decl_stmt|;
name|error
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|//this call will active the camelDestination
name|destination
operator|.
name|setMessageObserver
argument_list|(
name|observer
argument_list|)
expr_stmt|;
comment|// set is oneway false for get response from destination
comment|// need to use another thread to send the request message
name|sendoutMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
argument_list|,
literal|false
argument_list|,
literal|"HelloWorld"
argument_list|)
expr_stmt|;
comment|// wait for the message to be got from the destination,
comment|// create the thread to handler the Destination incomming message
name|verifyReceivedMessage
argument_list|(
name|inMessage
argument_list|,
literal|"HelloWorld Response"
argument_list|)
expr_stmt|;
name|error
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|destination
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRoundTripDestinationWithFault ()
specifier|public
name|void
name|testRoundTripDestinationWithFault
parameter_list|()
throws|throws
name|Exception
block|{
name|inMessage
operator|=
literal|null
expr_stmt|;
name|EndpointInfo
name|conduitEpInfo
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|conduitEpInfo
operator|.
name|setAddress
argument_list|(
literal|"camel://direct:Producer"
argument_list|)
expr_stmt|;
comment|// set up the conduit send to be true
name|CamelConduit
name|conduit
init|=
name|setupCamelConduit
argument_list|(
name|conduitEpInfo
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
specifier|final
name|Message
name|outMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|endpointInfo
operator|.
name|setAddress
argument_list|(
literal|"camel://direct:EndpointA"
argument_list|)
expr_stmt|;
specifier|final
name|CamelDestination
name|destination
init|=
name|setupCamelDestination
argument_list|(
name|endpointInfo
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|destination
operator|.
name|setCheckException
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// set up MessageObserver for handlering the conduit message
name|MessageObserver
name|observer
init|=
operator|new
name|MessageObserver
argument_list|()
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
try|try
block|{
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verifyReceivedMessage
argument_list|(
name|m
argument_list|,
literal|"HelloWorld"
argument_list|)
expr_stmt|;
comment|//verifyHeaders(m, outMessage);
comment|// setup the message for
name|Conduit
name|backConduit
decl_stmt|;
name|backConduit
operator|=
name|destination
operator|.
name|getBackChannel
argument_list|(
name|m
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// wait for the message to be got from the conduit
name|Message
name|replyMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|replyMessage
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
operator|new
name|RuntimeCamelException
argument_list|()
argument_list|)
expr_stmt|;
name|sendoutMessage
argument_list|(
name|backConduit
argument_list|,
name|replyMessage
argument_list|,
literal|true
argument_list|,
literal|"HelloWorld Fault"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
decl_stmt|;
name|MockEndpoint
name|error
init|=
operator|(
name|MockEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
decl_stmt|;
name|error
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|//this call will active the camelDestination
name|destination
operator|.
name|setMessageObserver
argument_list|(
name|observer
argument_list|)
expr_stmt|;
comment|// set is oneway false for get response from destination
comment|// need to use another thread to send the request message
name|sendoutMessage
argument_list|(
name|conduit
argument_list|,
name|outMessage
argument_list|,
literal|false
argument_list|,
literal|"HelloWorld"
argument_list|)
expr_stmt|;
comment|// wait for the message to be got from the destination,
comment|// create the thread to handler the Destination incomming message
name|verifyReceivedMessage
argument_list|(
name|inMessage
argument_list|,
literal|"HelloWorld Fault"
argument_list|)
expr_stmt|;
name|error
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|destination
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExceptionForwardedToExchange ()
specifier|public
name|void
name|testExceptionForwardedToExchange
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|RuntimeException
name|expectedException
init|=
operator|new
name|RuntimeException
argument_list|(
literal|"We simulate an exception in CXF processing"
argument_list|)
decl_stmt|;
name|DefaultCamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|CamelDestination
name|dest
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|CamelDestination
operator|.
name|class
argument_list|)
decl_stmt|;
name|dest
operator|.
name|incoming
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andThrow
argument_list|(
name|expectedException
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|dest
argument_list|)
expr_stmt|;
name|ConsumerProcessor
name|consumerProcessor
init|=
name|dest
operator|.
expr|new
name|ConsumerProcessor
argument_list|()
decl_stmt|;
comment|// Send our dummy exchange and check that the exception that occured on incoming is set
name|DefaultExchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|consumerProcessor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Exception
name|exc
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|exc
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|expectedException
argument_list|,
name|exc
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|dest
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

