begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|converter
operator|.
name|stream
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
name|model
operator|.
name|InterceptorRef
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
name|model
operator|.
name|InterceptorType
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
name|processor
operator|.
name|DelegateProcessor
import|;
end_import

begin_class
DECL|class|StreamCachingInterceptorTest
specifier|public
class|class
name|StreamCachingInterceptorTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|a
specifier|private
name|MockEndpoint
name|a
decl_stmt|;
DECL|field|b
specifier|private
name|MockEndpoint
name|b
decl_stmt|;
DECL|method|testConvertStreamSourceWithRouteBuilderStreamCaching ()
specifier|public
name|void
name|testConvertStreamSourceWithRouteBuilderStreamCaching
parameter_list|()
throws|throws
name|Exception
block|{
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
name|assertMockEndpointsSatisifed
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|a
operator|.
name|assertExchangeReceived
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|StreamCache
argument_list|)
expr_stmt|;
block|}
DECL|method|testConvertStreamSourceWithRouteOnlyStreamCaching ()
specifier|public
name|void
name|testConvertStreamSourceWithRouteOnlyStreamCaching
parameter_list|()
throws|throws
name|Exception
block|{
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
name|assertMockEndpointsSatisifed
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|b
operator|.
name|assertExchangeReceived
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|StreamCache
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|b
operator|.
name|assertExchangeReceived
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
block|}
DECL|method|testConvertInputStreamWithRouteBuilderStreamCaching ()
specifier|public
name|void
name|testConvertInputStreamWithRouteBuilderStreamCaching
parameter_list|()
throws|throws
name|Exception
block|{
name|a
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|InputStream
name|message
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<hello>world!</hello>"
operator|.
name|getBytes
argument_list|()
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
name|assertMockEndpointsSatisifed
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|a
operator|.
name|assertExchangeReceived
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|StreamCache
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
operator|.
name|assertExchangeReceived
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
block|}
DECL|method|testIgnoreAlreadyRereadable ()
specifier|public
name|void
name|testIgnoreAlreadyRereadable
parameter_list|()
throws|throws
name|Exception
block|{
name|a
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisifed
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|a
operator|.
name|assertExchangeReceived
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|String
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
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
name|a
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
name|b
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
block|}
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
comment|//Stream caching for a single route...
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|streamCaching
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
comment|//... or for all the following routes in this builder
name|streamCaching
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|testNoStreamCaching ()
specifier|public
name|void
name|testNoStreamCaching
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|InterceptorType
argument_list|>
name|interceptors
init|=
operator|new
name|LinkedList
argument_list|<
name|InterceptorType
argument_list|>
argument_list|()
decl_stmt|;
name|InterceptorRef
name|streamCache
init|=
operator|new
name|InterceptorRef
argument_list|(
operator|new
name|StreamCachingInterceptor
argument_list|()
argument_list|)
decl_stmt|;
name|interceptors
operator|.
name|add
argument_list|(
name|streamCache
argument_list|)
expr_stmt|;
name|interceptors
operator|.
name|add
argument_list|(
operator|new
name|InterceptorRef
argument_list|(
operator|new
name|DelegateProcessor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|StreamCachingInterceptor
operator|.
name|noStreamCaching
argument_list|(
name|interceptors
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|interceptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|interceptors
operator|.
name|contains
argument_list|(
name|streamCache
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

