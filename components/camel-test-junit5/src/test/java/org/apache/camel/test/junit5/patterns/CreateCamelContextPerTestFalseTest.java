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
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|EndpointInject
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
name|Produce
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
name|ProducerTemplate
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
name|AfterAll
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|CreateCamelContextPerTestFalseTest
specifier|public
class|class
name|CreateCamelContextPerTestFalseTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CreateCamelContextPerTestFalseTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|CREATED_CONTEXTS
specifier|private
specifier|static
specifier|final
name|AtomicInteger
name|CREATED_CONTEXTS
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|POST_TEAR_DOWN
specifier|private
specifier|static
specifier|final
name|AtomicInteger
name|POST_TEAR_DOWN
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Produce
argument_list|(
literal|"direct:start"
argument_list|)
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Override
DECL|method|isCreateCamelContextPerClass ()
specifier|public
name|boolean
name|isCreateCamelContextPerClass
parameter_list|()
block|{
return|return
literal|false
return|;
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
name|LOG
operator|.
name|info
argument_list|(
literal|"createCamelContext()"
argument_list|)
expr_stmt|;
name|CREATED_CONTEXTS
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|createCamelContext
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|doPostTearDown ()
specifier|protected
name|void
name|doPostTearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"doPostTearDown()"
argument_list|)
expr_stmt|;
name|POST_TEAR_DOWN
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|super
operator|.
name|doPostTearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendMatchingMessage ()
specifier|public
name|void
name|testSendMatchingMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedBody
init|=
literal|"<matched/>"
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|expectedBody
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|CREATED_CONTEXTS
operator|.
name|get
argument_list|()
operator|>=
literal|1
argument_list|,
literal|"Should create 1 or more CamelContext per test class"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendAnotherMatchingMessage ()
specifier|public
name|void
name|testSendAnotherMatchingMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedBody
init|=
literal|"<matched/>"
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|expectedBody
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|CREATED_CONTEXTS
operator|.
name|get
argument_list|()
operator|>=
literal|1
argument_list|,
literal|"Should create 1 or more CamelContext per test class"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendNotMatchingMessage ()
specifier|public
name|void
name|testSendNotMatchingMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"<notMatched/>"
argument_list|,
literal|"foo"
argument_list|,
literal|"notMatchedHeaderValue"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|CREATED_CONTEXTS
operator|.
name|get
argument_list|()
operator|>=
literal|1
argument_list|,
literal|"Should create 1 or more CamelContext per test class"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterAll
DECL|method|validateTearDown ()
specifier|public
specifier|static
name|void
name|validateTearDown
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|CREATED_CONTEXTS
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|POST_TEAR_DOWN
operator|.
name|get
argument_list|()
argument_list|)
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
literal|"direct:start"
argument_list|)
operator|.
name|filter
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
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
block|}
end_class

end_unit

