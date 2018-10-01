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
DECL|class|FileContentBasedRouterTest
specifier|public
class|class
name|FileContentBasedRouterTest
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
literal|"target/cbr"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|sendFiles ()
specifier|private
name|void
name|sendFiles
parameter_list|()
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/cbr"
argument_list|,
literal|"Hello London"
argument_list|,
literal|"CamelFileName"
argument_list|,
literal|"london.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/cbr"
argument_list|,
literal|"Hello Paris"
argument_list|,
literal|"CamelFileName"
argument_list|,
literal|"paris.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/cbr"
argument_list|,
literal|"Hello Copenhagen"
argument_list|,
literal|"CamelFileName"
argument_list|,
literal|"copenhagen.txt"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRouteLondon ()
specifier|public
name|void
name|testRouteLondon
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:london"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// should not load the content of the body into memory unless demand for it
comment|// so the type received should be a GenericFile (holder for the file)
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|GenericFile
operator|.
name|class
argument_list|)
expr_stmt|;
name|sendFiles
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRouteParis ()
specifier|public
name|void
name|testRouteParis
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:paris"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// should not load the content of the body into memory unless demand for it
comment|// so the type received should be a GenericFile (holder for the file)
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|GenericFile
operator|.
name|class
argument_list|)
expr_stmt|;
name|sendFiles
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRouteOther ()
specifier|public
name|void
name|testRouteOther
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:other"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"CamelFileName"
argument_list|,
literal|"copenhagen.txt"
argument_list|)
expr_stmt|;
comment|// should not load the content of the body into memory unless demand for it
comment|// so the type received should be a GenericFile (holder for the file)
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|GenericFile
operator|.
name|class
argument_list|)
expr_stmt|;
name|sendFiles
argument_list|()
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
literal|"file://target/cbr?noop=true&initialDelay=0&delay=10"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"CamelFileName"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"london.txt"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:london"
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"CamelFileName"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"paris.txt"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:paris"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:other"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

