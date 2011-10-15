begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
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
name|bean
operator|.
name|issues
operator|.
name|PrivateClasses
operator|.
name|HelloCamel
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
operator|.
name|issues
operator|.
name|PrivateClasses
operator|.
name|EXPECTED_OUTPUT
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
name|component
operator|.
name|bean
operator|.
name|issues
operator|.
name|PrivateClasses
operator|.
name|METHOD_NAME
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
name|component
operator|.
name|bean
operator|.
name|issues
operator|.
name|PrivateClasses
operator|.
name|newPackagePrivateHelloCamel
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
name|component
operator|.
name|bean
operator|.
name|issues
operator|.
name|PrivateClasses
operator|.
name|newPrivateHelloCamel
import|;
end_import

begin_comment
comment|/**  * Tests Bean binding for private& package-private classes where the target method is accessible through an interface.  */
end_comment

begin_class
DECL|class|BeanPrivateClassWithInterfaceMethodTest
specifier|public
specifier|final
class|class
name|BeanPrivateClassWithInterfaceMethodTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|INPUT_BODY
specifier|private
specifier|static
specifier|final
name|String
name|INPUT_BODY
init|=
literal|"Whatever"
decl_stmt|;
DECL|field|packagePrivateImpl
specifier|private
specifier|final
name|HelloCamel
name|packagePrivateImpl
init|=
name|newPackagePrivateHelloCamel
argument_list|()
decl_stmt|;
DECL|field|privateImpl
specifier|private
specifier|final
name|HelloCamel
name|privateImpl
init|=
name|newPrivateHelloCamel
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testPackagePrivateClassBinding ()
specifier|public
name|void
name|testPackagePrivateClassBinding
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|mockResult
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:packagePrivateClassResult"
argument_list|)
decl_stmt|;
name|mockResult
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockResult
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|equals
argument_list|(
name|EXPECTED_OUTPUT
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:testPackagePrivateClass"
argument_list|,
name|INPUT_BODY
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPrivateClassBinding ()
specifier|public
name|void
name|testPrivateClassBinding
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|mockResult
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:privateClassResult"
argument_list|)
decl_stmt|;
name|mockResult
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockResult
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|equals
argument_list|(
name|EXPECTED_OUTPUT
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:testPrivateClass"
argument_list|,
name|INPUT_BODY
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
literal|"direct:testPackagePrivateClass"
argument_list|)
operator|.
name|bean
argument_list|(
name|packagePrivateImpl
argument_list|,
name|METHOD_NAME
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:packagePrivateClassResult"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:testPrivateClass"
argument_list|)
operator|.
name|bean
argument_list|(
name|privateImpl
argument_list|,
name|METHOD_NAME
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:privateClassResult"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

