begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|XPathContentBasedRouterTest
specifier|public
class|class
name|XPathContentBasedRouterTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testXPathContentBasedRouter ()
specifier|public
name|void
name|testXPathContentBasedRouter
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:english"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<hello/>"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:dutch"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<hallo/>"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:german"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<hallo/>"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:french"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<hellos/>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"<hello/>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"<hallo/>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"<hellos/>"
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
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|xpath
argument_list|(
literal|"//hello"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:english"
argument_list|)
operator|.
name|when
argument_list|(
name|xpath
argument_list|(
literal|"//hallo"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:dutch"
argument_list|,
literal|"mock:german"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:french"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

