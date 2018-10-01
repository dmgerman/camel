begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|StreamCache
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

begin_class
DECL|class|StreamCachingPerRouteTest
specifier|public
class|class
name|StreamCachingPerRouteTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testStreamCachingPerRoute ()
specifier|public
name|void
name|testStreamCachingPerRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|a
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
decl_stmt|;
name|a
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|MockEndpoint
name|b
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
decl_stmt|;
name|b
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|MockEndpoint
name|c
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
decl_stmt|;
name|c
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"A"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|sendB
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"B"
argument_list|)
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:b"
argument_list|,
name|sendB
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:c"
argument_list|,
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"C"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Object
name|bodyA
init|=
name|a
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|StreamCache
operator|.
name|class
argument_list|,
name|bodyA
argument_list|)
expr_stmt|;
name|Object
name|bodyC
init|=
name|c
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|StreamCache
operator|.
name|class
argument_list|,
name|bodyC
argument_list|)
expr_stmt|;
comment|// should not be stream cache but the pure body
name|Object
name|bodyB
init|=
name|b
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|StreamSource
operator|.
name|class
argument_list|,
name|bodyB
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Should be same body as we send"
argument_list|,
name|sendB
argument_list|,
name|bodyB
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
name|context
operator|.
name|setStreamCaching
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|noStreamCaching
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:c"
argument_list|)
operator|.
name|streamCaching
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

