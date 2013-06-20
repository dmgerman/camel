begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mustache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mustache
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
name|junit4
operator|.
name|CamelTestSupport
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
name|util
operator|.
name|StopWatch
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
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Unit test for {@link MustacheComponent} and {@link MustacheEndpoint}  */
end_comment

begin_class
DECL|class|MustacheComponentTest
specifier|public
class|class
name|MustacheComponentTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:endSimple"
argument_list|)
DECL|field|endSimpleMock
specifier|protected
name|MockEndpoint
name|endSimpleMock
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:startSimple"
argument_list|)
DECL|field|startSimpleProducerTemplate
specifier|protected
name|ProducerTemplate
name|startSimpleProducerTemplate
decl_stmt|;
comment|/**      * Main test      */
annotation|@
name|Test
DECL|method|testMustache ()
specifier|public
name|void
name|testMustache
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Prepare
name|endSimpleMock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|endSimpleMock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Message with body 'The Body' and some header 'Some Header'"
argument_list|)
expr_stmt|;
comment|// Act
name|startSimpleProducerTemplate
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"The Body"
argument_list|,
literal|"someHeader"
argument_list|,
literal|"Some Header"
argument_list|)
expr_stmt|;
comment|// Verify
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test using code Template header      */
annotation|@
name|Test
DECL|method|testMustacheWithTemplateHeader ()
specifier|public
name|void
name|testMustacheWithTemplateHeader
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Prepare
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|"The Body"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"someHeader"
argument_list|,
literal|"Some Header"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|MustacheConstants
operator|.
name|MUSTACHE_TEMPLATE
argument_list|,
literal|"Body='{{body}}'|SomeHeader='{{headers.someHeader}}'"
argument_list|)
expr_stmt|;
name|endSimpleMock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|endSimpleMock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Body='The Body'|SomeHeader='Some Header'"
argument_list|)
expr_stmt|;
comment|// Act
name|startSimpleProducerTemplate
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// Verify
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test using Resource URI header      */
annotation|@
name|Test
DECL|method|testMustacheWithResourceUriHeader ()
specifier|public
name|void
name|testMustacheWithResourceUriHeader
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Prepare
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|"The Body"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"someHeader"
argument_list|,
literal|"Some Header"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|MustacheConstants
operator|.
name|MUSTACHE_RESOURCE_URI
argument_list|,
literal|"/another.mustache"
argument_list|)
expr_stmt|;
name|endSimpleMock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|endSimpleMock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Yet another Mustache with body:\n"
operator|+
literal|"    'The Body'\n"
operator|+
literal|"and some header:\n"
operator|+
literal|"    'Some Header'\n"
argument_list|)
expr_stmt|;
comment|// Act
name|startSimpleProducerTemplate
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// Verify
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMustacheWithInheritance ()
specifier|public
name|void
name|testMustacheWithInheritance
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Prepare
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|"The Body"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|MustacheConstants
operator|.
name|MUSTACHE_RESOURCE_URI
argument_list|,
literal|"/child.mustache"
argument_list|)
expr_stmt|;
name|endSimpleMock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|endSimpleMock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Start\n"
operator|+
literal|"Content 1: Child 1\n"
operator|+
literal|"Middle\n"
operator|+
literal|"Content 2: Child 2\n"
operator|+
literal|"End"
argument_list|)
expr_stmt|;
comment|// Act
name|startSimpleProducerTemplate
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// Verify
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMustacheWithPartials ()
specifier|public
name|void
name|testMustacheWithPartials
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Prepare
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|"The Body"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|MustacheConstants
operator|.
name|MUSTACHE_RESOURCE_URI
argument_list|,
literal|"/includer.mustache"
argument_list|)
expr_stmt|;
name|endSimpleMock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|endSimpleMock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Start\n"
operator|+
literal|"Included\n"
operator|+
literal|"End"
argument_list|)
expr_stmt|;
comment|// Act
name|startSimpleProducerTemplate
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// Verify
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|/**      * Main test      */
annotation|@
name|Test
DECL|method|testMustachePerformance ()
specifier|public
name|void
name|testMustachePerformance
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|messageCount
init|=
literal|10000
decl_stmt|;
name|endSimpleMock
operator|.
name|expectedMessageCount
argument_list|(
name|messageCount
argument_list|)
expr_stmt|;
name|StopWatch
name|stopwatch
init|=
operator|new
name|StopWatch
argument_list|(
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|messageCount
condition|;
name|i
operator|++
control|)
block|{
name|startSimpleProducerTemplate
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"The Body"
argument_list|,
literal|"someHeader"
argument_list|,
literal|"Some Header"
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
operator|.
name|info
argument_list|(
literal|"Mustache performance: "
operator|+
name|stopwatch
operator|.
name|stop
argument_list|()
operator|+
literal|"ms for "
operator|+
name|messageCount
operator|+
literal|" messages"
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
block|{
name|from
argument_list|(
literal|"direct:startSimple"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mustache://simple.mustache"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endSimple"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

