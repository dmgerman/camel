begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|seda
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
name|builder
operator|.
name|RouteBuilder
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

begin_comment
comment|/**  * Tests that a Seda producer supports the blockWhenFull option by blocking when  * a message is sent while the queue is full.  */
end_comment

begin_class
DECL|class|SedaBlockWhenFullTest
specifier|public
class|class
name|SedaBlockWhenFullTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|QUEUE_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|QUEUE_SIZE
init|=
literal|1
decl_stmt|;
DECL|field|DELAY
specifier|private
specifier|static
specifier|final
name|int
name|DELAY
init|=
literal|10
decl_stmt|;
DECL|field|DELAY_LONG
specifier|private
specifier|static
specifier|final
name|int
name|DELAY_LONG
init|=
literal|130
decl_stmt|;
DECL|field|MOCK_URI
specifier|private
specifier|static
specifier|final
name|String
name|MOCK_URI
init|=
literal|"mock:blockWhenFullOutput"
decl_stmt|;
DECL|field|SIZE_PARAM
specifier|private
specifier|static
specifier|final
name|String
name|SIZE_PARAM
init|=
literal|"?size=%d"
decl_stmt|;
DECL|field|SEDA_WITH_OFFER_TIMEOUT_URI
specifier|private
specifier|static
specifier|final
name|String
name|SEDA_WITH_OFFER_TIMEOUT_URI
init|=
literal|"seda:blockingFoo"
operator|+
name|String
operator|.
name|format
argument_list|(
name|SIZE_PARAM
argument_list|,
name|QUEUE_SIZE
argument_list|)
operator|+
literal|"&blockWhenFull=true&offerTimeout=100"
decl_stmt|;
DECL|field|BLOCK_WHEN_FULL_URI
specifier|private
specifier|static
specifier|final
name|String
name|BLOCK_WHEN_FULL_URI
init|=
literal|"seda:blockingBar"
operator|+
name|String
operator|.
name|format
argument_list|(
name|SIZE_PARAM
argument_list|,
name|QUEUE_SIZE
argument_list|)
operator|+
literal|"&blockWhenFull=true&timeout=0&offerTimeout=200"
decl_stmt|;
DECL|field|DEFAULT_URI
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_URI
init|=
literal|"seda:foo"
operator|+
name|String
operator|.
name|format
argument_list|(
name|SIZE_PARAM
argument_list|,
name|QUEUE_SIZE
argument_list|)
decl_stmt|;
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
name|BLOCK_WHEN_FULL_URI
argument_list|)
operator|.
name|delay
argument_list|(
name|DELAY_LONG
argument_list|)
operator|.
name|syncDelayed
argument_list|()
operator|.
name|to
argument_list|(
name|MOCK_URI
argument_list|)
expr_stmt|;
comment|// use same delay as above on purpose
name|from
argument_list|(
name|DEFAULT_URI
argument_list|)
operator|.
name|delay
argument_list|(
name|DELAY
argument_list|)
operator|.
name|syncDelayed
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:whatever"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testSedaOfferTimeoutWhenFull ()
specifier|public
name|void
name|testSedaOfferTimeoutWhenFull
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|SedaEndpoint
name|seda
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|SEDA_WITH_OFFER_TIMEOUT_URI
argument_list|,
name|SedaEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|QUEUE_SIZE
argument_list|,
name|seda
operator|.
name|getQueue
argument_list|()
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|sendTwoOverCapacity
argument_list|(
name|SEDA_WITH_OFFER_TIMEOUT_URI
argument_list|,
name|QUEUE_SIZE
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failed to insert element into queue, "
operator|+
literal|"after timeout of "
operator|+
name|seda
operator|.
name|getOfferTimeout
argument_list|()
operator|+
literal|" milliseconds"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|IllegalStateException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testSedaDefaultWhenFull ()
specifier|public
name|void
name|testSedaDefaultWhenFull
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|SedaEndpoint
name|seda
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|DEFAULT_URI
argument_list|,
name|SedaEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Seda Endpoint is not setting the correct default (should be false) for \"blockWhenFull\""
argument_list|,
name|seda
operator|.
name|isBlockWhenFull
argument_list|()
argument_list|)
expr_stmt|;
name|sendTwoOverCapacity
argument_list|(
name|DEFAULT_URI
argument_list|,
name|QUEUE_SIZE
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"The route didn't fill the queue beyond capacity: test class isn't working as intended"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|IllegalStateException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testSedaBlockingWhenFull ()
specifier|public
name|void
name|testSedaBlockingWhenFull
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
name|MOCK_URI
argument_list|)
operator|.
name|setExpectedMessageCount
argument_list|(
name|QUEUE_SIZE
operator|+
literal|2
argument_list|)
expr_stmt|;
name|SedaEndpoint
name|seda
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|BLOCK_WHEN_FULL_URI
argument_list|,
name|SedaEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|QUEUE_SIZE
argument_list|,
name|seda
operator|.
name|getQueue
argument_list|()
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|sendTwoOverCapacity
argument_list|(
name|BLOCK_WHEN_FULL_URI
argument_list|,
name|QUEUE_SIZE
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsyncSedaBlockingWhenFull ()
specifier|public
name|void
name|testAsyncSedaBlockingWhenFull
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
name|MOCK_URI
argument_list|)
operator|.
name|setExpectedMessageCount
argument_list|(
name|QUEUE_SIZE
operator|+
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
name|MOCK_URI
argument_list|)
operator|.
name|setResultWaitTime
argument_list|(
name|DELAY_LONG
operator|*
literal|3
argument_list|)
expr_stmt|;
name|SedaEndpoint
name|seda
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|BLOCK_WHEN_FULL_URI
argument_list|,
name|SedaEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|QUEUE_SIZE
argument_list|,
name|seda
operator|.
name|getQueue
argument_list|()
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|asyncSendTwoOverCapacity
argument_list|(
name|BLOCK_WHEN_FULL_URI
argument_list|,
name|QUEUE_SIZE
operator|+
literal|4
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|/**      * This method make sure that we hit the limit by sending two msg over the      * given capacity which allows the delayer to kick in, leaving the 2nd msg      * in the queue, blocking/throwing on the third one.      */
DECL|method|sendTwoOverCapacity (String uri, int capacity)
specifier|private
name|void
name|sendTwoOverCapacity
parameter_list|(
name|String
name|uri
parameter_list|,
name|int
name|capacity
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
operator|(
name|capacity
operator|+
literal|2
operator|)
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|uri
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|asyncSendTwoOverCapacity (String uri, int capacity)
specifier|private
name|void
name|asyncSendTwoOverCapacity
parameter_list|(
name|String
name|uri
parameter_list|,
name|int
name|capacity
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
operator|(
name|capacity
operator|+
literal|2
operator|)
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|asyncSendBody
argument_list|(
name|uri
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

