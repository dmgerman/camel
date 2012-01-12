begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
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
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_comment
comment|/**  * Unit tests for {@link AntPathMatcherGenericFileFilter}.  */
end_comment

begin_class
DECL|class|AntPathMatcherGenericFileFilterTest
specifier|public
class|class
name|AntPathMatcherGenericFileFilterTest
extends|extends
name|ContextTestSupport
block|{
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
name|deleteDirectory
argument_list|(
literal|"target/files"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"filter"
argument_list|,
operator|new
name|AntPathMatcherGenericFileFilter
argument_list|<
name|File
argument_list|>
argument_list|(
literal|"**/c*"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
DECL|method|testInclude ()
specifier|public
name|void
name|testInclude
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/files/ant-path-1/x/y/z"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"report.txt"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result1"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|oneExchangeDone
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
block|}
DECL|method|testExclude ()
specifier|public
name|void
name|testExclude
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/files/ant-path-2/x/y/z"
argument_list|,
literal|"Hello World 1"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"report.bak"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/files/ant-path-2/x/y/z"
argument_list|,
literal|"Hello World 2"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"report.txt"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result2"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"Hello World 2"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|oneExchangeDone
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
block|}
DECL|method|testIncludesAndExcludes ()
specifier|public
name|void
name|testIncludesAndExcludes
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/files/ant-path-3/x/y/z"
argument_list|,
literal|"Hello World 1"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"a.pdf"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/files/ant-path-3/x/y/z"
argument_list|,
literal|"Hello World 2"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"m.pdf"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/files/ant-path-3/x/y/z"
argument_list|,
literal|"Hello World 3"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"b.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/files/ant-path-3/x/y/z"
argument_list|,
literal|"Hello World 4"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"m.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/files/ant-path-3/x/y/z"
argument_list|,
literal|"Hello World 5"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"b.bak"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/files/ant-path-3/x/y/z"
argument_list|,
literal|"Hello World 6"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"m.bak"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result3"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"Hello World 2"
argument_list|,
literal|"Hello World 4"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|oneExchangeDone
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
block|}
DECL|method|testIncludesAndExcludesAndFilter ()
specifier|public
name|void
name|testIncludesAndExcludesAndFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/files/ant-path-4/x/y/z"
argument_list|,
literal|"Hello World 1"
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
literal|"file://target/files/ant-path-4/x/y/z"
argument_list|,
literal|"Hello World 2"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"b.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/files/ant-path-4/x/y/z"
argument_list|,
literal|"Hello World 3"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"c.txt"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result4"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"Hello World 3"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|oneExchangeDone
operator|.
name|matchesMockWaitTime
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"file://target/files/ant-path-1?recursive=true&antInclude=**/*.txt"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"file://target/files/ant-path-2?recursive=true&antExclude=**/*.bak"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"file://target/files/ant-path-3?recursive=true&antInclude=**/*.pdf,**/*.txt&antExclude=**/a*,**/b*"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result3"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"file://target/files/ant-path-4?recursive=true&antInclude=**/*.txt&antExclude=**/a*&filter=#filter"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result4"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

