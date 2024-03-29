begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.parser.java
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
operator|.
name|java
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
name|test
operator|.
name|junit4
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
name|Test
import|;
end_import

begin_class
DECL|class|SplitTokenizeTest
specifier|public
class|class
name|SplitTokenizeTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testSplitTokenizerA ()
specifier|public
name|void
name|testSplitTokenizerA
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Claus"
argument_list|,
literal|"James"
argument_list|,
literal|"Willem"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Claus,James,Willem"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplitTokenizerB ()
specifier|public
name|void
name|testSplitTokenizerB
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Claus"
argument_list|,
literal|"James"
argument_list|,
literal|"Willem"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:b"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"myHeader"
argument_list|,
literal|"Claus,James,Willem"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplitTokenizerC ()
specifier|public
name|void
name|testSplitTokenizerC
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Claus"
argument_list|,
literal|"James"
argument_list|,
literal|"Willem"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:c"
argument_list|,
literal|"Claus James Willem"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplitTokenizerD ()
specifier|public
name|void
name|testSplitTokenizerD
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"[Claus]"
argument_list|,
literal|"[James]"
argument_list|,
literal|"[Willem]"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:d"
argument_list|,
literal|"[Claus][James][Willem]"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplitTokenizerE ()
specifier|public
name|void
name|testSplitTokenizerE
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<person>Claus</person>"
argument_list|,
literal|"<person>James</person>"
argument_list|,
literal|"<person>Willem</person>"
argument_list|)
expr_stmt|;
name|String
name|xml
init|=
literal|"<persons><person>Claus</person><person>James</person><person>Willem</person></persons>"
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:e"
argument_list|,
name|xml
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplitTokenizerEWithSlash ()
specifier|public
name|void
name|testSplitTokenizerEWithSlash
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
name|String
name|xml
init|=
literal|"<persons><person attr='/' /></persons>"
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<person attr='/' />"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:e"
argument_list|,
name|xml
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplitTokenizerF ()
specifier|public
name|void
name|testSplitTokenizerF
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<person name=\"Claus\"/>"
argument_list|,
literal|"<person>James</person>"
argument_list|,
literal|"<person>Willem</person>"
argument_list|)
expr_stmt|;
name|String
name|xml
init|=
literal|"<persons><person/><person name=\"Claus\"/><person>James</person><person>Willem</person></persons>"
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:f"
argument_list|,
name|xml
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
literal|"direct:a"
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|tokenize
argument_list|(
literal|","
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:split"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|tokenize
argument_list|(
literal|","
argument_list|,
literal|"myHeader"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:split"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:c"
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|tokenize
argument_list|(
literal|"(\\W+)\\s*"
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:split"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:d"
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|tokenizePair
argument_list|(
literal|"["
argument_list|,
literal|"]"
argument_list|,
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:split"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:e"
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|tokenizeXML
argument_list|(
literal|"person"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:split"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:f"
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"//person"
argument_list|)
comment|// To test the body is not empty
comment|// it will call the ObjectHelper.evaluateValuePredicate()
operator|.
name|filter
argument_list|()
operator|.
name|simple
argument_list|(
literal|"${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:split"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

