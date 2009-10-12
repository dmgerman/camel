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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
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
name|jaxp
operator|.
name|BytesSource
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
name|jaxp
operator|.
name|StringSource
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
name|jaxp
operator|.
name|XmlConverter
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
DECL|field|MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|MESSAGE
init|=
literal|"<hello>world!</hello>"
decl_stmt|;
DECL|field|BODY_TYPE
specifier|private
specifier|static
specifier|final
name|String
name|BODY_TYPE
init|=
literal|"body.type"
decl_stmt|;
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
DECL|field|converter
specifier|private
specifier|final
name|XmlConverter
name|converter
init|=
operator|new
name|XmlConverter
argument_list|()
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
name|MESSAGE
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
DECL|method|testNoConversionForOtherXmlSourceTypes ()
specifier|public
name|void
name|testNoConversionForOtherXmlSourceTypes
parameter_list|()
throws|throws
name|Exception
block|{
name|a
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|send
argument_list|(
name|converter
operator|.
name|toDOMSource
argument_list|(
name|MESSAGE
argument_list|)
argument_list|)
expr_stmt|;
name|send
argument_list|(
operator|new
name|StringSource
argument_list|(
name|MESSAGE
argument_list|)
argument_list|)
expr_stmt|;
name|send
argument_list|(
operator|new
name|BytesSource
argument_list|(
name|MESSAGE
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|a
operator|.
name|getExchanges
argument_list|()
control|)
block|{
name|assertFalse
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|BODY_TYPE
argument_list|,
name|Class
operator|.
name|class
argument_list|)
operator|.
name|toString
argument_list|()
operator|+
literal|" shouldn't have been converted to StreamCache"
argument_list|,
name|exchange
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
block|}
DECL|method|send (Source source)
specifier|private
name|void
name|send
parameter_list|(
name|Source
name|source
parameter_list|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:a"
argument_list|,
name|source
argument_list|,
name|BODY_TYPE
argument_list|,
name|source
operator|.
name|getClass
argument_list|()
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
name|MESSAGE
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
name|MESSAGE
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
name|MESSAGE
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
name|assertMockEndpointsSatisfied
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
name|MESSAGE
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
name|MESSAGE
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
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
DECL|method|testStreamCachingInterceptorToString ()
specifier|public
name|void
name|testStreamCachingInterceptorToString
parameter_list|()
block|{
name|StreamCachingInterceptor
name|cache
init|=
operator|new
name|StreamCachingInterceptor
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|cache
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|StreamCaching
name|caching
init|=
operator|new
name|StreamCaching
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|caching
operator|.
name|toString
argument_list|()
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
comment|//START SNIPPET: route
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
comment|//END SNIPPET: route
comment|//... or for all the following routes in this builder
comment|//START SNIPPET: routebuilder
name|context
operator|.
name|setStreamCaching
argument_list|(
literal|true
argument_list|)
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
comment|//END SNIPPET: routebuilder
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

