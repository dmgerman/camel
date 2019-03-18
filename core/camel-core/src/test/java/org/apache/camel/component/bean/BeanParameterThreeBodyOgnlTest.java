begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|impl
operator|.
name|JndiRegistry
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
DECL|class|BeanParameterThreeBodyOgnlTest
specifier|public
class|class
name|BeanParameterThreeBodyOgnlTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testBeanParameterValue ()
specifier|public
name|void
name|testBeanParameterValue
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"3"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|body
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|body
operator|.
name|add
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
literal|"C"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
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
literal|"foo"
argument_list|,
operator|new
name|MyBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:foo?method=bar(${body[0]},${body[1]},${body[2]})"
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
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|method|bar (String order1, String order2, String order3)
specifier|public
name|String
name|bar
parameter_list|(
name|String
name|order1
parameter_list|,
name|String
name|order2
parameter_list|,
name|String
name|order3
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|order1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|order2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"C"
argument_list|,
name|order3
argument_list|)
expr_stmt|;
return|return
literal|"3"
return|;
block|}
DECL|method|bar (String order1, String order2)
specifier|public
name|String
name|bar
parameter_list|(
name|String
name|order1
parameter_list|,
name|String
name|order2
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|order1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|order2
argument_list|)
expr_stmt|;
return|return
literal|"2"
return|;
block|}
DECL|method|bar (String order1)
specifier|public
name|String
name|bar
parameter_list|(
name|String
name|order1
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|order1
argument_list|)
expr_stmt|;
return|return
literal|"1"
return|;
block|}
block|}
block|}
end_class

end_unit

