begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|support
operator|.
name|SynchronizationAdapter
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|HttpAsyncCallbackTest
specifier|public
class|class
name|HttpAsyncCallbackTest
extends|extends
name|HttpAsyncTestSupport
block|{
DECL|field|LATCH
specifier|private
specifier|static
specifier|final
name|CountDownLatch
name|LATCH
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|3
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testAsyncAndSyncAtSameTimeWithHttp ()
specifier|public
name|void
name|testAsyncAndSyncAtSameTimeWithHttp
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
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"Hello Claus"
argument_list|,
literal|"Hello Hadrian"
argument_list|,
literal|"Hello Willem"
argument_list|)
expr_stmt|;
comment|// START SNIPPET: e3
name|MyCallback
name|callback
init|=
operator|new
name|MyCallback
argument_list|()
decl_stmt|;
comment|// Send 3 async request/reply message to the http endpoint
comment|// where we let the callback handle gathering the responses
name|String
name|url
init|=
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/myservice"
decl_stmt|;
name|template
operator|.
name|asyncCallbackRequestBody
argument_list|(
name|url
argument_list|,
literal|"Claus"
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|template
operator|.
name|asyncCallbackRequestBody
argument_list|(
name|url
argument_list|,
literal|"Hadrian"
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|template
operator|.
name|asyncCallbackRequestBody
argument_list|(
name|url
argument_list|,
literal|"Willem"
argument_list|,
name|callback
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e3
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should get 3 callbacks"
argument_list|,
name|LATCH
operator|.
name|await
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
comment|// assert that we got all the correct data in our callback
name|assertTrue
argument_list|(
literal|"Claus is missing"
argument_list|,
name|callback
operator|.
name|getData
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Hello Claus"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Hadrian is missing"
argument_list|,
name|callback
operator|.
name|getData
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Hello Hadrian"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Willem is missing"
argument_list|,
name|callback
operator|.
name|getData
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Hello Willem"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// START SNIPPET: e2
comment|/**      * Our own callback that will gather all the responses.      * We extend the SynchronizationAdapter class as we then only need to override the onComplete method.      */
DECL|class|MyCallback
specifier|private
specifier|static
class|class
name|MyCallback
extends|extends
name|SynchronizationAdapter
block|{
DECL|field|data
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|data
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|onComplete (Exchange exchange)
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// this method is invoked when the exchange was a success and we can get the response
name|String
name|body
init|=
name|exchange
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
decl_stmt|;
name|data
operator|.
name|add
argument_list|(
name|body
argument_list|)
expr_stmt|;
comment|// the latch is used for testing purposes
name|LATCH
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
DECL|method|getData ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getData
parameter_list|()
block|{
return|return
name|data
return|;
block|}
block|}
comment|// END SNIPPET: e2
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
comment|// The mocks are here for unit test
comment|// Simulate a slow http service (delaying a bit) we want to invoke async
name|from
argument_list|(
literal|"jetty:http://0.0.0.0:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/myservice"
argument_list|)
operator|.
name|delay
argument_list|(
literal|300
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"Hello "
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

