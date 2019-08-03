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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|Body
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
name|Headers
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
name|support
operator|.
name|jndi
operator|.
name|JndiContext
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
comment|/**  * Unit test of bean can propagate headers in a pipeline  */
end_comment

begin_class
DECL|class|BeanPipelineTest
specifier|public
class|class
name|BeanPipelineTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testBeanInPipeline ()
specifier|public
name|void
name|testBeanInPipeline
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World from James"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"from"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:input"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"from"
argument_list|,
literal|"Claus"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
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
literal|"direct:input"
argument_list|)
operator|.
name|pipeline
argument_list|(
literal|"bean:foo"
argument_list|,
literal|"bean:bar?method=usingExchange"
argument_list|,
literal|"bean:baz"
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
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiContext
name|answer
init|=
operator|new
name|JndiContext
argument_list|()
decl_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"foo"
argument_list|,
operator|new
name|FooBean
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"bar"
argument_list|,
operator|new
name|BarBean
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"baz"
argument_list|,
operator|new
name|BazBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|class|FooBean
specifier|public
specifier|static
class|class
name|FooBean
block|{
DECL|method|onlyPlainBody (Object body)
specifier|public
name|void
name|onlyPlainBody
parameter_list|(
name|Object
name|body
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|BarBean
specifier|public
specifier|static
class|class
name|BarBean
block|{
DECL|method|doNotUseMe (String body)
specifier|public
name|void
name|doNotUseMe
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Should not invoce me"
argument_list|)
expr_stmt|;
block|}
DECL|method|usingExchange (Exchange exchange)
specifier|public
name|void
name|usingExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"from"
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"from"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World from James"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|BazBean
specifier|public
specifier|static
class|class
name|BazBean
block|{
DECL|method|doNotUseMe (String body)
specifier|public
name|void
name|doNotUseMe
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Should not invoce me"
argument_list|)
expr_stmt|;
block|}
DECL|method|withAnnotations (@eaders Map<String, Object> headers, @Body String body)
specifier|public
name|void
name|withAnnotations
parameter_list|(
annotation|@
name|Headers
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
annotation|@
name|Body
name|String
name|body
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Hello World from James"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"James"
argument_list|,
name|headers
operator|.
name|get
argument_list|(
literal|"from"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

