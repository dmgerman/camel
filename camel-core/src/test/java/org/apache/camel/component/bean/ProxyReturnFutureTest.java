begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ProxyReturnFutureTest
specifier|public
class|class
name|ProxyReturnFutureTest
extends|extends
name|ContextTestSupport
block|{
comment|// START SNIPPET: e2
annotation|@
name|Test
DECL|method|testFutureEcho ()
specifier|public
name|void
name|testFutureEcho
parameter_list|()
throws|throws
name|Exception
block|{
name|Echo
name|service
init|=
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:echo"
argument_list|)
argument_list|,
name|Echo
operator|.
name|class
argument_list|)
decl_stmt|;
name|Future
argument_list|<
name|String
argument_list|>
name|future
init|=
name|service
operator|.
name|asText
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Got future"
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Waiting for future to be done ..."
argument_list|)
expr_stmt|;
name|String
name|reply
init|=
name|future
operator|.
name|get
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Four"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
comment|// END SNIPPET: e2
annotation|@
name|Test
DECL|method|testFutureEchoCallTwoTimes ()
specifier|public
name|void
name|testFutureEchoCallTwoTimes
parameter_list|()
throws|throws
name|Exception
block|{
name|Echo
name|service
init|=
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:echo"
argument_list|)
argument_list|,
name|Echo
operator|.
name|class
argument_list|)
decl_stmt|;
name|Future
argument_list|<
name|String
argument_list|>
name|future
init|=
name|service
operator|.
name|asText
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Got future"
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Waiting for future to be done ..."
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Four"
argument_list|,
name|future
operator|.
name|get
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|future
operator|=
name|service
operator|.
name|asText
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Got future"
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Waiting for future to be done ..."
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Four"
argument_list|,
name|future
operator|.
name|get
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
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
literal|"direct:echo"
argument_list|)
operator|.
name|delay
argument_list|(
literal|250
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Four"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// START SNIPPET: e1
DECL|interface|Echo
specifier|public
interface|interface
name|Echo
block|{
comment|// returning a Future indicate asynchronous invocation
DECL|method|asText (int number)
name|Future
argument_list|<
name|String
argument_list|>
name|asText
parameter_list|(
name|int
name|number
parameter_list|)
function_decl|;
block|}
comment|// END SNIPPET: e1
block|}
end_class

end_unit

