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
name|concurrent
operator|.
name|Future
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
name|Test
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|HttpAsyncTest
specifier|public
class|class
name|HttpAsyncTest
extends|extends
name|HttpAsyncTestSupport
block|{
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
comment|// START SNIPPET: e2
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
comment|// We expect the name job to be faster than the async job even though the async job
comment|// was started first
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Claus"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
comment|// Send a async request/reply message to the http endpoint
name|Future
argument_list|<
name|Object
argument_list|>
name|future
init|=
name|template
operator|.
name|asyncRequestBody
argument_list|(
literal|"http://0.0.0.0:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/myservice"
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
comment|// We got the future so in the meantime we can do other stuff, as this is Camel
comment|// so lets invoke another request/reply route but this time is synchronous
name|String
name|name
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:name"
argument_list|,
literal|"Give me a name"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|name
argument_list|)
expr_stmt|;
comment|// Okay we got a name and we have done some other work at the same time
comment|// the async route is running, but now its about time to wait and get
comment|// get the response from the async task
comment|// We use the extract future body to get the response from the future
comment|// (waiting if needed) and then return a string body response.
comment|// This allows us to do this in a single code line instead of using the
comment|// JDK Future API to get hold of it, but you can also use that if you want
comment|// Adding the (String) To make the CS happy
name|String
name|response
init|=
name|template
operator|.
name|extractFutureBody
argument_list|(
name|future
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// END SNIPPET: e2
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
comment|// The mocks are here for unit test
comment|// Some other service to return a name, this is invoked synchronously
name|from
argument_list|(
literal|"direct:name"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Claus"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// Simulate a slow http service (delaying 1 sec) we want to invoke async
name|fromF
argument_list|(
literal|"jetty:http://0.0.0.0:%s/myservice"
argument_list|,
name|getPort
argument_list|()
argument_list|)
operator|.
name|delay
argument_list|(
literal|1000
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Bye World"
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

