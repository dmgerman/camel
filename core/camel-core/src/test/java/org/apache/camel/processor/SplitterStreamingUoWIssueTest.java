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
name|Before
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
DECL|class|SplitterStreamingUoWIssueTest
specifier|public
class|class
name|SplitterStreamingUoWIssueTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/data/splitter"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplitterStreamingUoWIssue ()
specifier|public
name|void
name|testSplitterStreamingUoWIssue
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|"C"
argument_list|,
literal|"D"
argument_list|,
literal|"E"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A,B,C,D,E"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/splitter"
argument_list|,
literal|"A,B,C,D,E"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"splitme.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplitterTwoFilesStreamingUoWIssue ()
specifier|public
name|void
name|testSplitterTwoFilesStreamingUoWIssue
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|"C"
argument_list|,
literal|"D"
argument_list|,
literal|"E"
argument_list|,
literal|"F"
argument_list|,
literal|"G"
argument_list|,
literal|"H"
argument_list|,
literal|"I"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A,B,C,D,E"
argument_list|,
literal|"F,G,H,I"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/splitter"
argument_list|,
literal|"A,B,C,D,E"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"a.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/splitter"
argument_list|,
literal|"F,G,H,I"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"b.txt"
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
literal|"file:target/data/splitter?initialDelay=0&delay=10&delete=true&sortBy=file:name"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
operator|.
name|tokenize
argument_list|(
literal|","
argument_list|)
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|to
argument_list|(
literal|"seda:queue"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|log
argument_list|(
literal|"End of file ${file:name}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:queue"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Token: ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

