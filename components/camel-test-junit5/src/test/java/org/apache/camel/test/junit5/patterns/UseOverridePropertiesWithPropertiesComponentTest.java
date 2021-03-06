begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.junit5.patterns
package|package
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
name|patterns
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|AdviceWithRouteBuilder
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
name|reifier
operator|.
name|RouteReifier
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
name|BeforeEach
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

begin_class
DECL|class|UseOverridePropertiesWithPropertiesComponentTest
specifier|public
class|class
name|UseOverridePropertiesWithPropertiesComponentTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseAdviceWith ()
specifier|public
name|boolean
name|isUseAdviceWith
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|BeforeEach
DECL|method|doSomethingBefore ()
specifier|public
name|void
name|doSomethingBefore
parameter_list|()
throws|throws
name|Exception
block|{
name|AdviceWithRouteBuilder
name|mocker
init|=
operator|new
name|AdviceWithRouteBuilder
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
name|replaceFromWith
argument_list|(
literal|"direct:sftp"
argument_list|)
expr_stmt|;
name|interceptSendToEndpoint
argument_list|(
literal|"file:*"
argument_list|)
operator|.
name|skipSendToOriginalEndpoint
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:file"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|RouteReifier
operator|.
name|adviceWith
argument_list|(
name|context
operator|.
name|getRouteDefinition
argument_list|(
literal|"myRoute"
argument_list|)
argument_list|,
name|context
argument_list|,
name|mocker
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|useOverridePropertiesWithPropertiesComponent ()
specifier|protected
name|Properties
name|useOverridePropertiesWithPropertiesComponent
parameter_list|()
block|{
name|Properties
name|pc
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|pc
operator|.
name|put
argument_list|(
literal|"ftp.username"
argument_list|,
literal|"scott"
argument_list|)
expr_stmt|;
name|pc
operator|.
name|put
argument_list|(
literal|"ftp.password"
argument_list|,
literal|"tiger"
argument_list|)
expr_stmt|;
return|return
name|pc
return|;
block|}
annotation|@
name|Test
DECL|method|testOverride ()
specifier|public
name|void
name|testOverride
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:file"
argument_list|)
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
literal|"direct:sftp"
argument_list|,
literal|"Hello World"
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
literal|"ftp:somepath?username={{ftp.username}}&password={{ftp.password}}"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"myRoute"
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:target/out"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

