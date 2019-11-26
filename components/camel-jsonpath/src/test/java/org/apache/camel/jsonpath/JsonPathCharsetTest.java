begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jsonpath
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jsonpath
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|test
operator|.
name|junit5
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertNotNull
import|;
end_import

begin_class
DECL|class|JsonPathCharsetTest
specifier|public
class|class
name|JsonPathCharsetTest
extends|extends
name|CamelTestSupport
block|{
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
literal|"direct:start"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|jsonpath
argument_list|(
literal|"$.store.book[*].title"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:authors"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testUTF16BEFile ()
specifier|public
name|void
name|testUTF16BEFile
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:authors"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/booksUTF16BE.json"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|check
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUTF16LEFile ()
specifier|public
name|void
name|testUTF16LEFile
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:authors"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/booksUTF16LE.json"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|check
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUTF16BEInputStream ()
specifier|public
name|void
name|testUTF16BEInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:authors"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|InputStream
name|input
init|=
name|JsonPathCharsetTest
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"booksUTF16BE.json"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|input
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|check
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUTF16BEURL ()
specifier|public
name|void
name|testUTF16BEURL
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:authors"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
literal|"file:src/test/resources/booksUTF16BE.json"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|check
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testISO8859WithJsonHeaderCamelJsonInputEncoding ()
specifier|public
name|void
name|testISO8859WithJsonHeaderCamelJsonInputEncoding
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:authors"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
literal|"file:src/test/resources/germanbooks-iso-8859-1.json"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|url
argument_list|,
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|Object
operator|>
name|singletonMap
argument_list|(
name|JsonPathConstants
operator|.
name|HEADER_JSON_ENCODING
argument_list|,
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
name|check
argument_list|(
literal|"Joseph und seine BrÃ¼der"
argument_list|,
literal|"GÃ¶tzendÃ¤mmerung"
argument_list|)
expr_stmt|;
block|}
DECL|method|check ()
specifier|private
name|void
name|check
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|check
argument_list|(
literal|"Sayings of the Century"
argument_list|,
literal|"Sword of Honour"
argument_list|)
expr_stmt|;
block|}
DECL|method|check (String title1, String title2)
specifier|private
name|void
name|check
parameter_list|(
name|String
name|title1
parameter_list|,
name|String
name|title2
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|authors
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:authors"
argument_list|)
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
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|title1
argument_list|,
name|authors
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|title2
argument_list|,
name|authors
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

