begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|model
operator|.
name|SetBodyDefinition
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
name|SplitDefinition
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
name|ToDefinition
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
name|language
operator|.
name|ConstantExpression
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
name|ProcessorFactory
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
name|RouteContext
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|CustomProcessorFactoryTest
specifier|public
class|class
name|CustomProcessorFactoryTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
comment|// START SNIPPET: e1
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
comment|// register our custom factory
name|context
operator|.
name|setProcessorFactory
argument_list|(
operator|new
name|MyFactory
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
comment|// END SNIPPET: e1
comment|// START SNIPPET: e2
DECL|method|testAlterDefinitionUsingProcessorFactory ()
specifier|public
name|void
name|testAlterDefinitionUsingProcessorFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"body was altered"
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
block|}
DECL|method|testAlterDefinitionUsingProcessorFactoryWithChild ()
specifier|public
name|void
name|testAlterDefinitionUsingProcessorFactoryWithChild
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"body was altered"
argument_list|,
literal|"body was altered"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:extra"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"body was altered"
argument_list|,
literal|"body was altered"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello,World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello,World"
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
literal|"direct:start"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"body not altered"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"body not altered"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:split"
argument_list|)
operator|.
name|end
argument_list|()
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
comment|// END SNIPPET: e2
comment|// START SNIPPET: e3
DECL|class|MyFactory
specifier|public
specifier|static
class|class
name|MyFactory
implements|implements
name|ProcessorFactory
block|{
DECL|method|createChildProcessor (RouteContext routeContext, ProcessorDefinition definition, boolean mandatory)
specifier|public
name|Processor
name|createChildProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|ProcessorDefinition
name|definition
parameter_list|,
name|boolean
name|mandatory
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
DECL|method|createProcessor (RouteContext routeContext, ProcessorDefinition definition)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|ProcessorDefinition
name|definition
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|definition
operator|instanceof
name|SplitDefinition
condition|)
block|{
comment|// add additional output to the splitter
name|SplitDefinition
name|split
init|=
operator|(
name|SplitDefinition
operator|)
name|definition
decl_stmt|;
name|split
operator|.
name|addOutput
argument_list|(
operator|new
name|ToDefinition
argument_list|(
literal|"mock:extra"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|instanceof
name|SetBodyDefinition
condition|)
block|{
name|SetBodyDefinition
name|set
init|=
operator|(
name|SetBodyDefinition
operator|)
name|definition
decl_stmt|;
name|set
operator|.
name|setExpression
argument_list|(
operator|new
name|ConstantExpression
argument_list|(
literal|"body was altered"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// return null to let the default implementation create the processor, we just wanted to alter the definition
comment|// before the processor was created
return|return
literal|null
return|;
block|}
block|}
comment|// END SNIPPET: e3
block|}
end_class

end_unit

