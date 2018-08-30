begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|interceptor
package|;
end_package

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
name|CamelContext
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|processor
operator|.
name|SpringTestHelper
operator|.
name|createSpringCamelContext
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|MixedStreamCachingInterceptorTest
specifier|public
class|class
name|MixedStreamCachingInterceptorTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testStreamCaching ()
specifier|public
name|void
name|testStreamCaching
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
name|StreamSource
name|message
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|a
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|StreamCache
name|cache
init|=
name|assertIsInstanceOf
argument_list|(
name|StreamCache
operator|.
name|class
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|message
argument_list|,
name|cache
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoStreamCaching ()
specifier|public
name|void
name|testNoStreamCaching
parameter_list|()
throws|throws
name|Exception
block|{
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
name|StreamSource
name|message
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:b"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|b
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|StreamSource
name|stream
init|=
name|assertIsInstanceOf
argument_list|(
name|StreamSource
operator|.
name|class
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|createSpringCamelContext
argument_list|(
name|this
argument_list|,
literal|"org/apache/camel/spring/interceptor/mixedStreamCachingInterceptorTest.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

