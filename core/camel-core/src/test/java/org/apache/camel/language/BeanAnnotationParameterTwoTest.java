begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
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
DECL|class|BeanAnnotationParameterTwoTest
specifier|public
class|class
name|BeanAnnotationParameterTwoTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testBeanAnnotationOne ()
specifier|public
name|void
name|testBeanAnnotationOne
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
literal|"Hello/Bonjour World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:one"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanAnnotationTwo ()
specifier|public
name|void
name|testBeanAnnotationTwo
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
literal|"Hello/Bonjour World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:two"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanAnnotationThree ()
specifier|public
name|void
name|testBeanAnnotationThree
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
literal|"Hello/Bonjour World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:three"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanAnnotationFour ()
specifier|public
name|void
name|testBeanAnnotationFour
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:middle"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello/Bonjour World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:four"
argument_list|,
literal|"World"
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
literal|"GreetingService"
argument_list|,
operator|new
name|GreetingService
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
literal|"direct:one"
argument_list|)
operator|.
name|bean
argument_list|(
name|MyBean
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:two"
argument_list|)
operator|.
name|bean
argument_list|(
name|MyBean
operator|.
name|class
argument_list|,
literal|"callA"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:three"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|BEAN_METHOD_NAME
argument_list|,
name|constant
argument_list|(
literal|"callA"
argument_list|)
argument_list|)
operator|.
name|bean
argument_list|(
name|MyBean
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:four"
argument_list|)
operator|.
name|bean
argument_list|(
name|MyBean
operator|.
name|class
argument_list|,
literal|"callA"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:middle"
argument_list|)
operator|.
name|bean
argument_list|(
name|MyBean
operator|.
name|class
argument_list|,
literal|"callB"
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
specifier|final
class|class
name|MyBean
block|{
DECL|method|callA (@eanref = R, method = R) String greeting, @Bean(ref = R, method = R) String french, String body)
specifier|public
name|String
name|callA
parameter_list|(
annotation|@
name|Bean
argument_list|(
name|ref
operator|=
literal|"GreetingService"
argument_list|,
name|method
operator|=
literal|"english"
argument_list|)
name|String
name|greeting
parameter_list|,
annotation|@
name|Bean
argument_list|(
name|ref
operator|=
literal|"GreetingService"
argument_list|,
name|method
operator|=
literal|"french"
argument_list|)
name|String
name|french
parameter_list|,
name|String
name|body
parameter_list|)
block|{
return|return
name|greeting
operator|+
literal|"/"
operator|+
name|french
operator|+
literal|" "
operator|+
name|body
return|;
block|}
DECL|method|callB ()
specifier|public
name|String
name|callB
parameter_list|()
block|{
return|return
literal|"Bye World"
return|;
block|}
block|}
DECL|class|GreetingService
specifier|public
specifier|static
specifier|final
class|class
name|GreetingService
block|{
DECL|method|callA ()
specifier|public
name|String
name|callA
parameter_list|()
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Should not callA"
argument_list|)
throw|;
block|}
DECL|method|callB ()
specifier|public
name|String
name|callB
parameter_list|()
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Should not callB"
argument_list|)
throw|;
block|}
DECL|method|english ()
specifier|public
name|String
name|english
parameter_list|()
block|{
return|return
literal|"Hello"
return|;
block|}
DECL|method|french ()
specifier|public
name|String
name|french
parameter_list|()
block|{
return|return
literal|"Bonjour"
return|;
block|}
DECL|method|german ()
specifier|public
name|String
name|german
parameter_list|()
block|{
return|return
literal|"Hallo"
return|;
block|}
block|}
block|}
end_class

end_unit
