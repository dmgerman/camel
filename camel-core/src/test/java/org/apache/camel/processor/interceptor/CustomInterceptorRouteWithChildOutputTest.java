begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|interceptor
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
name|LogDefinition
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
name|RouteDefinition
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
name|spi
operator|.
name|InterceptStrategy
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
DECL|class|CustomInterceptorRouteWithChildOutputTest
specifier|public
class|class
name|CustomInterceptorRouteWithChildOutputTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|myInterceptor
specifier|private
name|MyInterceptor
name|myInterceptor
init|=
operator|new
name|MyInterceptor
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testCustomInterceptor ()
specifier|public
name|void
name|testCustomInterceptor
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:child"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
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
literal|"A,B,C"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|myInterceptor
operator|.
name|getDefs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|LogDefinition
operator|.
name|class
argument_list|,
name|myInterceptor
operator|.
name|getDefs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|ToDefinition
operator|.
name|class
argument_list|,
name|myInterceptor
operator|.
name|getDefs
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mock:child"
argument_list|,
name|myInterceptor
operator|.
name|getDefs
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|SplitDefinition
operator|.
name|class
argument_list|,
name|myInterceptor
operator|.
name|getDefs
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|ToDefinition
operator|.
name|class
argument_list|,
name|myInterceptor
operator|.
name|getDefs
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mock:result"
argument_list|,
name|myInterceptor
operator|.
name|getDefs
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getLabel
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
comment|// add our custom interceptor
name|context
operator|.
name|addInterceptStrategy
argument_list|(
name|myInterceptor
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
operator|.
name|tokenize
argument_list|(
literal|","
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"Spltted ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:child"
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|class|MyInterceptor
specifier|private
specifier|static
class|class
name|MyInterceptor
implements|implements
name|InterceptStrategy
block|{
DECL|field|defs
specifier|private
specifier|final
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|defs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|wrapProcessorInInterceptors (CamelContext context, NamedNode definition, Processor target, Processor nextTarget)
specifier|public
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|NamedNode
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
name|defs
operator|.
name|add
argument_list|(
operator|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
operator|)
name|definition
argument_list|)
expr_stmt|;
return|return
name|target
return|;
block|}
DECL|method|getDefs ()
specifier|public
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|getDefs
parameter_list|()
block|{
return|return
name|defs
return|;
block|}
block|}
block|}
end_class

end_unit

