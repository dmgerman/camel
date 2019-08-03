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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
DECL|class|JettySimulateFailoverRoundRobinTest
specifier|public
class|class
name|JettySimulateFailoverRoundRobinTest
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
argument_list|(
literal|23041
argument_list|)
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
argument_list|(
literal|23042
argument_list|)
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
argument_list|(
literal|23043
argument_list|)
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
argument_list|(
literal|23044
argument_list|)
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
DECL|method|testJettySimulateFailoverRoundRobin ()
specifier|public
name|void
name|testJettySimulateFailoverRoundRobin
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyFailoverLoadBalancer
argument_list|(
name|template
argument_list|,
name|hbad
argument_list|,
name|hbad2
argument_list|,
name|hgood
argument_list|,
name|hgood2
argument_list|)
argument_list|)
expr_stmt|;
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
comment|/**      * A custom failover processor      */
DECL|class|MyFailoverLoadBalancer
specifier|public
specifier|static
class|class
name|MyFailoverLoadBalancer
implements|implements
name|Processor
block|{
DECL|field|template
specifier|private
specifier|final
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|endpoints
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|endpoints
decl_stmt|;
DECL|field|counter
specifier|private
name|int
name|counter
init|=
operator|-
literal|1
decl_stmt|;
DECL|method|MyFailoverLoadBalancer (ProducerTemplate template, String... endpoints)
specifier|public
name|MyFailoverLoadBalancer
parameter_list|(
name|ProducerTemplate
name|template
parameter_list|,
name|String
modifier|...
name|endpoints
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
name|this
operator|.
name|endpoints
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|endpoints
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
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
name|boolean
name|done
init|=
literal|false
decl_stmt|;
while|while
condition|(
operator|!
name|done
condition|)
block|{
comment|// pick endpoint
if|if
condition|(
operator|++
name|counter
operator|>=
name|endpoints
operator|.
name|size
argument_list|()
condition|)
block|{
name|counter
operator|=
literal|0
expr_stmt|;
block|}
name|String
name|endpoint
init|=
name|endpoints
operator|.
name|get
argument_list|(
name|counter
argument_list|)
decl_stmt|;
comment|// process exchange
try|try
block|{
name|template
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// check whether we are done or prepare for failover
name|done
operator|=
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
expr_stmt|;
if|if
condition|(
operator|!
name|done
condition|)
block|{
name|prepareExchangeForFailover
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|prepareExchangeForFailover (Exchange exchange)
specifier|private
name|void
name|prepareExchangeForFailover
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_HANDLED
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERED
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

