begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina2
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|Endpoint
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
name|mina
operator|.
name|transport
operator|.
name|socket
operator|.
name|SocketConnector
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
name|easymock
operator|.
name|classextension
operator|.
name|EasyMock
operator|.
name|createMock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|EasyMock
operator|.
name|replay
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|EasyMock
operator|.
name|verify
import|;
end_import

begin_comment
comment|/**  * Unit testing for using a MinaProducer that it can shutdown properly (CAMEL-395)  */
end_comment

begin_class
DECL|class|Mina2ProducerShutdownMockTest
specifier|public
class|class
name|Mina2ProducerShutdownMockTest
extends|extends
name|BaseMina2Test
block|{
annotation|@
name|Test
DECL|method|testProducerShutdownTestingWithMock ()
specifier|public
name|void
name|testProducerShutdownTestingWithMock
parameter_list|()
throws|throws
name|Exception
block|{
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
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
comment|// create our mock and record expected behavior = that worker timeout should be set to 0
name|SocketConnector
name|mockConnector
init|=
name|createMock
argument_list|(
name|SocketConnector
operator|.
name|class
argument_list|)
decl_stmt|;
name|mockConnector
operator|.
name|dispose
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|mockConnector
argument_list|)
expr_stmt|;
comment|// normal camel code to get a producer
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"mina2:tcp://localhost:%1$s?textline=true&sync=false"
argument_list|,
name|getPort
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// set input and execute it
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// insert our mock instead of real MINA IoConnector
name|Field
name|field
init|=
name|producer
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"connector"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|field
operator|.
name|set
argument_list|(
name|producer
argument_list|,
name|mockConnector
argument_list|)
expr_stmt|;
comment|//
comment|// Everything is asynchronous.
comment|// We need to wait a second to make sure we get the message.
comment|//
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// stop using our mock
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mockConnector
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
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
name|String
operator|.
name|format
argument_list|(
literal|"mina2:tcp://localhost:%1$s?textline=true&sync=false"
argument_list|,
name|getPort
argument_list|()
argument_list|)
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

