begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.facebook
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|facebook
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
name|CamelContext
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

begin_comment
comment|/**  * Test preconfigured Facebook component.  */
end_comment

begin_class
DECL|class|FacebookEndpointTest
specifier|public
class|class
name|FacebookEndpointTest
extends|extends
name|CamelFacebookTestSupport
block|{
DECL|method|FacebookEndpointTest ()
specifier|public
name|FacebookEndpointTest
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|Test
DECL|method|testNoArgsEndpoint ()
specifier|public
name|void
name|testNoArgsEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock://testNoArgs"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
argument_list|()
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:testNoArgs"
argument_list|,
literal|null
argument_list|,
name|FacebookConstants
operator|.
name|FACEBOOK_PROPERTY_PREFIX
operator|+
literal|"userId"
argument_list|,
literal|"me"
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|FacebookComponent
name|component
init|=
operator|new
name|FacebookComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setConfiguration
argument_list|(
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"facebook"
argument_list|,
name|component
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
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
literal|"direct:testNoArgs"
argument_list|)
operator|.
name|to
argument_list|(
literal|"facebook:getuserlikes"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock://testNoArgs"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

