begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

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
name|NamedNode
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
name|Processor
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
name|model
operator|.
name|ProcessorDefinition
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
name|processor
operator|.
name|DelegateProcessor
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
name|spi
operator|.
name|InterceptStrategy
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
name|spi
operator|.
name|NodeIdFactory
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

begin_comment
comment|/**  * Demonstrates how you can use a custom id factory to assign ids to Camel Java routes  * and then attach your own debugger and be able to use the custom ids to know at what  * point you are debugging  *  * @version   */
end_comment

begin_class
DECL|class|CustomIdFactoryTest
specifier|public
class|class
name|CustomIdFactoryTest
extends|extends
name|ContextTestSupport
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
name|CustomIdFactoryTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|counter
specifier|private
specifier|static
name|int
name|counter
decl_stmt|;
DECL|field|ids
specifier|private
specifier|static
name|String
name|ids
decl_stmt|;
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
name|ids
operator|=
literal|""
expr_stmt|;
name|counter
operator|=
literal|0
expr_stmt|;
name|super
operator|.
name|setUp
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
comment|// use our own id factory so we can generate the keys we like to use
name|context
operator|.
name|setNodeIdFactory
argument_list|(
operator|new
name|NodeIdFactory
argument_list|()
block|{
specifier|public
name|String
name|createId
parameter_list|(
name|NamedNode
name|definition
parameter_list|)
block|{
return|return
literal|"#"
operator|+
name|definition
operator|.
name|getShortName
argument_list|()
operator|+
operator|++
name|counter
operator|+
literal|"#"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// add our debugger so we can debug camel routes when we send in messages
name|context
operator|.
name|addInterceptStrategy
argument_list|(
operator|new
name|MyDebuggerCheckingId
argument_list|()
argument_list|)
expr_stmt|;
comment|// a little content based router so we got 2 paths to route at runtime
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Hello"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:hello"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|log
argument_list|(
literal|"Hey"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:other"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**      * Test path 1      */
annotation|@
name|Test
DECL|method|testHello ()
specifier|public
name|void
name|testHello
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"#route1#"
argument_list|,
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:hello"
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
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// this should take the when path (first to)
name|assertEquals
argument_list|(
literal|"#choice7##to2#"
argument_list|,
name|ids
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test path 2      */
annotation|@
name|Test
DECL|method|testOther ()
specifier|public
name|void
name|testOther
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"#route1#"
argument_list|,
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:other"
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
literal|"direct:start"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// this should take the otherwise path
name|assertEquals
argument_list|(
literal|"#choice7##log4##to6#"
argument_list|,
name|ids
argument_list|)
expr_stmt|;
block|}
DECL|class|MyDebuggerCheckingId
specifier|private
specifier|static
class|class
name|MyDebuggerCheckingId
implements|implements
name|InterceptStrategy
block|{
DECL|method|wrapProcessorInInterceptors (final CamelContext context, final ProcessorDefinition<?> definition, Processor target, Processor nextTarget)
specifier|public
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
specifier|final
name|CamelContext
name|context
parameter_list|,
specifier|final
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|,
name|Processor
name|target
parameter_list|,
name|Processor
name|nextTarget
parameter_list|)
throws|throws
name|Exception
block|{
comment|// MUST DO THIS
comment|// force id creation as sub nodes have lazy assigned ids
name|definition
operator|.
name|idOrCreate
argument_list|(
name|context
operator|.
name|getNodeIdFactory
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|DelegateProcessor
argument_list|(
name|target
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|processNext
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Debugging at: {} with id: {} with exchange: {}"
argument_list|,
name|definition
operator|.
name|toString
argument_list|()
argument_list|,
name|definition
operator|.
name|getId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// record the path taken at runtime
name|ids
operator|+=
name|definition
operator|.
name|getId
argument_list|()
expr_stmt|;
comment|// continue to the real target by invoking super
name|super
operator|.
name|processNext
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
block|}
end_class

end_unit

