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
DECL|class|JettyFailoverRoundRobinTest
specifier|public
class|class
name|JettyFailoverRoundRobinTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|port1
specifier|private
specifier|static
name|int
name|port1
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|port2
specifier|private
specifier|static
name|int
name|port2
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|port3
specifier|private
specifier|static
name|int
name|port3
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|port4
specifier|private
specifier|static
name|int
name|port4
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|bad
specifier|private
name|String
name|bad
init|=
literal|"jetty:http://localhost:"
operator|+
name|port1
operator|+
literal|"/bad"
decl_stmt|;
DECL|field|bad2
specifier|private
name|String
name|bad2
init|=
literal|"jetty:http://localhost:"
operator|+
name|port2
operator|+
literal|"/bad2"
decl_stmt|;
DECL|field|good
specifier|private
name|String
name|good
init|=
literal|"jetty:http://localhost:"
operator|+
name|port3
operator|+
literal|"/good"
decl_stmt|;
DECL|field|good2
specifier|private
name|String
name|good2
init|=
literal|"jetty:http://localhost:"
operator|+
name|port4
operator|+
literal|"/good2"
decl_stmt|;
DECL|field|hbad
specifier|private
name|String
name|hbad
init|=
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/bad"
decl_stmt|;
DECL|field|hbad2
specifier|private
name|String
name|hbad2
init|=
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/bad2"
decl_stmt|;
DECL|field|hgood
specifier|private
name|String
name|hgood
init|=
literal|"http://localhost:"
operator|+
name|port3
operator|+
literal|"/good"
decl_stmt|;
DECL|field|hgood2
specifier|private
name|String
name|hgood2
init|=
literal|"http://localhost:"
operator|+
name|port4
operator|+
literal|"/good2"
decl_stmt|;
annotation|@
name|Test
DECL|method|testJettyFailoverRoundRobin ()
specifier|public
name|void
name|testJettyFailoverRoundRobin
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:bad"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bad2"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:good"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:good2"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|String
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Good"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// reset mocks and send a message again to see that round robin
comment|// continue where it should
name|resetMocks
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bad"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bad2"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:good"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:good2"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|reply
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Also good"
argument_list|,
name|reply
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
comment|// load balance using failover in round robin mode.
comment|// Also do not inherit error handler which means the failover LB will not fallback
comment|// and use error handler but trigger failover to next endpoint immediately.
comment|// -1 is to indicate that failover LB should newer exhaust and keep trying
operator|.
name|loadBalance
argument_list|()
operator|.
name|failover
argument_list|(
operator|-
literal|1
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
comment|// this is the four endpoints we will load balance with failover
operator|.
name|to
argument_list|(
name|hbad
argument_list|,
name|hbad2
argument_list|,
name|hgood
argument_list|,
name|hgood2
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
name|from
argument_list|(
name|bad
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bad"
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
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
literal|500
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Something bad happened"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|bad2
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bad2"
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
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
literal|404
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Not found"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|good
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:good"
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
literal|"Good"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|good2
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:good2"
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
literal|"Also good"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

