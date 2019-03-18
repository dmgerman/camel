begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|UnsupportedCharsetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|InvalidPayloadException
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
name|RuntimeCamelException
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
name|ExchangeBuilder
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
DECL|class|ConvertBodyTest
specifier|public
class|class
name|ConvertBodyTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testConvertBodyTo ()
specifier|public
name|void
name|testConvertBodyTo
parameter_list|()
block|{
try|try
block|{
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// set an invalid charset
name|from
argument_list|(
literal|"direct:invalid"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|"ASSI"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpoint"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
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
name|UnsupportedCharsetException
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
DECL|method|testConvertBodyCharset ()
specifier|public
name|void
name|testConvertBodyCharset
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
literal|"iso-8859-1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// do not propagate charset to avoid side effects with double conversion etc
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertBodyCharsetWithExistingCharsetName ()
specifier|public
name|void
name|testConvertBodyCharsetWithExistingCharsetName
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
literal|"iso-8859-1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// do not propagate charset to avoid side effects with double conversion etc
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|Exchange
name|srcExchange
init|=
name|ExchangeBuilder
operator|.
name|anExchange
argument_list|(
name|context
argument_list|)
operator|.
name|withProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"UTF-8"
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"Hello World"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:foo"
argument_list|,
name|srcExchange
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertToInteger ()
specifier|public
name|void
name|testConvertToInteger
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|11
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"11"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertNullBody ()
specifier|public
name|void
name|testConvertNullBody
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertFailed ()
specifier|public
name|void
name|testConvertFailed
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:invalid"
argument_list|,
literal|"11"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|InvalidPayloadException
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertToBytesCharset ()
specifier|public
name|void
name|testConvertToBytesCharset
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|body
init|=
literal|"Hello World"
operator|.
name|getBytes
argument_list|(
literal|"iso-8859-1"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:charset"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertToStringCharset ()
specifier|public
name|void
name|testConvertToStringCharset
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|body
init|=
literal|"Hello World"
decl_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:charset3"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|body
operator|.
name|getBytes
argument_list|(
literal|"utf-16"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertToBytesCharsetFail ()
specifier|public
name|void
name|testConvertToBytesCharsetFail
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|body
init|=
literal|"Hello World"
operator|.
name|getBytes
argument_list|(
literal|"utf-8"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:charset2"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
comment|// should NOT be okay as we expected utf-8 but got it in utf-16
name|result
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertToStringCharsetFail ()
specifier|public
name|void
name|testConvertToStringCharsetFail
parameter_list|()
throws|throws
name|Exception
block|{
comment|// does not work on AIX
name|String
name|osName
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
decl_stmt|;
name|boolean
name|aix
init|=
name|osName
operator|.
name|indexOf
argument_list|(
literal|"aix"
argument_list|)
operator|>
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|aix
condition|)
block|{
return|return;
block|}
name|String
name|body
init|=
literal|"Hell\u00F6 W\u00F6rld"
decl_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:charset3"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|body
operator|.
name|getBytes
argument_list|(
literal|"utf-8"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// should NOT be okay as we expected utf-8 but got it in utf-16
name|result
operator|.
name|assertIsNotSatisfied
argument_list|()
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:invalid"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|Date
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:charset"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
literal|"iso-8859-1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:charset2"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
literal|"utf-16"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:charset3"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|"utf-16"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

