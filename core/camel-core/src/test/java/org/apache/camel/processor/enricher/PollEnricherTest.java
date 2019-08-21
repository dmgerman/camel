begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.enricher
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|enricher
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
name|ContextTestSupport
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
name|ExchangePattern
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

begin_class
DECL|class|PollEnricherTest
specifier|public
class|class
name|PollEnricherTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|aggregationStrategy
specifier|private
specifier|static
name|SampleAggregator
name|aggregationStrategy
init|=
operator|new
name|SampleAggregator
argument_list|()
decl_stmt|;
DECL|field|mock
specifier|protected
name|MockEndpoint
name|mock
decl_stmt|;
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|mock
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:mock"
argument_list|)
expr_stmt|;
block|}
comment|// -------------------------------------------------------------
comment|// InOnly routes
comment|// -------------------------------------------------------------
annotation|@
name|Test
DECL|method|testPollEnrichInOnly ()
specifier|public
name|void
name|testPollEnrichInOnly
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo1"
argument_list|,
literal|"blah"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"test:blah"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
literal|"seda://foo1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:enricher-test-1"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPollEnrichInOnlyWaitWithTimeout ()
specifier|public
name|void
name|testPollEnrichInOnlyWaitWithTimeout
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// this first try there is no data so we timeout
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"test:blah"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
literal|"seda://foo2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:enricher-test-2"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
comment|// not expected data so we are not happy
name|mock
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
comment|// now send it and try again
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo2"
argument_list|,
literal|"blah"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:enricher-test-2"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPollEnrichInOnlyWaitNoTimeout ()
specifier|public
name|void
name|testPollEnrichInOnlyWaitNoTimeout
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// use another thread to send it a bit later
name|Thread
name|t
init|=
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|250
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo3"
argument_list|,
literal|"blah"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"test:blah"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
literal|"seda://foo3"
argument_list|)
expr_stmt|;
name|t
operator|.
name|start
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:enricher-test-3"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
comment|// should take approx 1 sec to complete as the other thread is sending a
comment|// bit later and we wait
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|long
name|delta
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should take approx 0.25 sec: was "
operator|+
name|delta
argument_list|,
name|delta
operator|>
literal|150
argument_list|)
expr_stmt|;
block|}
comment|// -------------------------------------------------------------
comment|// InOut routes
comment|// -------------------------------------------------------------
annotation|@
name|Test
DECL|method|testPollEnrichInOut ()
specifier|public
name|void
name|testPollEnrichInOut
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo4"
argument_list|,
literal|"blah"
argument_list|)
expr_stmt|;
name|String
name|result
init|=
operator|(
name|String
operator|)
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:enricher-test-4"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test:blah"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPollEnrichInOutPlusHeader ()
specifier|public
name|void
name|testPollEnrichInOutPlusHeader
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo4"
argument_list|,
literal|"blah"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:enricher-test-4"
argument_list|,
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
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test:blah"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"seda://foo4"
argument_list|,
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
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
comment|// -------------------------------------------------------------
comment|// InOnly routes
comment|// -------------------------------------------------------------
name|from
argument_list|(
literal|"direct:enricher-test-1"
argument_list|)
operator|.
name|pollEnrich
argument_list|(
literal|"seda:foo1"
argument_list|,
name|aggregationStrategy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:mock"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:enricher-test-2"
argument_list|)
operator|.
name|pollEnrich
argument_list|(
literal|"seda:foo2"
argument_list|,
literal|1000
argument_list|,
name|aggregationStrategy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:mock"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:enricher-test-3"
argument_list|)
operator|.
name|pollEnrich
argument_list|(
literal|"seda:foo3"
argument_list|,
operator|-
literal|1
argument_list|,
name|aggregationStrategy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:mock"
argument_list|)
expr_stmt|;
comment|// -------------------------------------------------------------
comment|// InOut routes
comment|// -------------------------------------------------------------
name|from
argument_list|(
literal|"direct:enricher-test-4"
argument_list|)
operator|.
name|pollEnrich
argument_list|(
literal|"seda:foo4"
argument_list|,
name|aggregationStrategy
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

