begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|jetty
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
DECL|class|JettyBridgeHostHeaderIssueTest
specifier|public
class|class
name|JettyBridgeHostHeaderIssueTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|port
specifier|private
name|int
name|port
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
DECL|field|port4
specifier|private
name|int
name|port4
decl_stmt|;
DECL|field|port5
specifier|private
name|int
name|port5
decl_stmt|;
DECL|field|receivedHostHeaderEndpoint1
specifier|private
name|String
name|receivedHostHeaderEndpoint1
decl_stmt|;
DECL|field|receivedHostHeaderEndpoint2
specifier|private
name|String
name|receivedHostHeaderEndpoint2
decl_stmt|;
DECL|field|receivedHostHeaderEndpoint3
specifier|private
name|String
name|receivedHostHeaderEndpoint3
decl_stmt|;
DECL|field|receivedHostHeaderEndpoint4
specifier|private
name|String
name|receivedHostHeaderEndpoint4
decl_stmt|;
annotation|@
name|Test
DECL|method|testHostHeader ()
specifier|public
name|void
name|testHostHeader
parameter_list|()
throws|throws
name|Exception
block|{
comment|//The first two calls will test http4 producers
comment|//The first call to our service will hit the first destination in the round robin load balancer
comment|//this destination has the preserveProxyHeader parameter set to true, so we verify the Host header
comment|//received by our downstream instance matches the address and port of the proxied service
name|Exchange
name|reply
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http:localhost:"
operator|+
name|port
operator|+
literal|"/myapp"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
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
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|reply
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|reply
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|//assert the received Host header is localhost:port (where port matches the /myapp port)
name|assertEquals
argument_list|(
literal|"localhost:"
operator|+
name|port
argument_list|,
name|receivedHostHeaderEndpoint1
argument_list|)
expr_stmt|;
comment|//The second call to our service will hit the second destination in the round robin load balancer
comment|//this destination does not have the preserveProxyHeader, so we expect the Host header received by the destination
comment|//to match the url of the destination service itself
name|Exchange
name|reply2
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http:localhost:"
operator|+
name|port
operator|+
literal|"/myapp"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|reply2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|reply2
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|//assert the received Host header is localhost:port3 (where port3 matches the /bar destination server)
name|assertEquals
argument_list|(
literal|"localhost:"
operator|+
name|port3
argument_list|,
name|receivedHostHeaderEndpoint2
argument_list|)
expr_stmt|;
comment|//The next two calls will use/test the jetty producers in the round robin load balancer
comment|//The first has the preserveHostHeader option set to true, so we would expect to receive a Host header matching the /myapp proxied service
name|Exchange
name|reply3
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http:localhost:"
operator|+
name|port
operator|+
literal|"/myapp"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye JWorld"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|reply3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jbar"
argument_list|,
name|reply3
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|//assert the received Host header is localhost:port (where port matches the /myapp destination server)
name|assertEquals
argument_list|(
literal|"localhost:"
operator|+
name|port
argument_list|,
name|receivedHostHeaderEndpoint3
argument_list|)
expr_stmt|;
comment|//The second does not have a preserveHostHeader (preserveHostHeader=false), we would expect to see a Host header matching the destination service
name|Exchange
name|reply4
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http:localhost:"
operator|+
name|port
operator|+
literal|"/myapp"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"JAVA!!!!"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|reply4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java???"
argument_list|,
name|reply4
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|//assert the received Host header is localhost:port5 (where port3 matches the /jbarf destination server)
name|assertEquals
argument_list|(
literal|"localhost:"
operator|+
name|port5
argument_list|,
name|receivedHostHeaderEndpoint4
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
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|12000
argument_list|)
expr_stmt|;
name|port2
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|12100
argument_list|)
expr_stmt|;
name|port3
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|12200
argument_list|)
expr_stmt|;
name|port4
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|12300
argument_list|)
expr_stmt|;
name|port5
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|12400
argument_list|)
expr_stmt|;
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
name|from
argument_list|(
literal|"jetty:http://localhost:"
operator|+
name|port
operator|+
literal|"/myapp?matchOnUriPrefix=true"
argument_list|)
operator|.
name|loadBalance
argument_list|()
operator|.
name|roundRobin
argument_list|()
operator|.
name|to
argument_list|(
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/foo?bridgeEndpoint=true&throwExceptionOnFailure=false&preserveHostHeader=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:"
operator|+
name|port3
operator|+
literal|"/bar?bridgeEndpoint=true&throwExceptionOnFailure=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:"
operator|+
name|port4
operator|+
literal|"/jbar?bridgeEndpoint=true&throwExceptionOnFailure=false&preserveHostHeader=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:"
operator|+
name|port5
operator|+
literal|"/jbarf?bridgeEndpoint=true&throwExceptionOnFailure=false"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:"
operator|+
name|port2
operator|+
literal|"/foo"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|receivedHostHeaderEndpoint1
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Host"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:"
operator|+
name|port3
operator|+
literal|"/bar"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|receivedHostHeaderEndpoint2
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Host"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:"
operator|+
name|port4
operator|+
literal|"/jbar"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|receivedHostHeaderEndpoint3
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Host"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"jbar"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:"
operator|+
name|port5
operator|+
literal|"/jbarf"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|receivedHostHeaderEndpoint4
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Host"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"java???"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

