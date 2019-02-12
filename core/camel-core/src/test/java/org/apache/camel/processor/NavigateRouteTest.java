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
name|Navigate
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
comment|/**  * Unit test for navigating a route (runtime processors, not the model).  */
end_comment

begin_class
DECL|class|NavigateRouteTest
specifier|public
class|class
name|NavigateRouteTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|processors
specifier|private
specifier|static
name|List
argument_list|<
name|Processor
argument_list|>
name|processors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testNavigateRoute ()
specifier|public
name|void
name|testNavigateRoute
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
name|expectedMessageCount
argument_list|(
literal|2
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
name|Navigate
argument_list|<
name|Processor
argument_list|>
name|nav
init|=
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|navigate
argument_list|()
decl_stmt|;
name|navigateRoute
argument_list|(
name|nav
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"There should be 6 processors to navigate"
argument_list|,
literal|6
argument_list|,
name|processors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|navigateRoute (Navigate<Processor> nav)
specifier|private
name|void
name|navigateRoute
parameter_list|(
name|Navigate
argument_list|<
name|Processor
argument_list|>
name|nav
parameter_list|)
block|{
if|if
condition|(
operator|!
name|nav
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|nav
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"ProcessorToReactiveProcessorBridge"
argument_list|)
condition|)
block|{
name|nav
operator|=
call|(
name|Navigate
call|)
argument_list|(
operator|(
name|Navigate
operator|)
name|nav
argument_list|)
operator|.
name|next
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Processor
name|child
range|:
name|nav
operator|.
name|next
argument_list|()
control|)
block|{
name|processors
operator|.
name|add
argument_list|(
name|child
argument_list|)
expr_stmt|;
if|if
condition|(
name|child
operator|instanceof
name|SendProcessor
condition|)
block|{
name|SendProcessor
name|send
init|=
operator|(
name|SendProcessor
operator|)
name|child
decl_stmt|;
name|assertEquals
argument_list|(
literal|"mock://result"
argument_list|,
name|send
operator|.
name|getDestination
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|child
operator|instanceof
name|ConvertBodyProcessor
condition|)
block|{
name|ConvertBodyProcessor
name|convert
init|=
operator|(
name|ConvertBodyProcessor
operator|)
name|child
decl_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|convert
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// navigate children
if|if
condition|(
name|child
operator|instanceof
name|Navigate
condition|)
block|{
name|navigateRoute
argument_list|(
operator|(
name|Navigate
argument_list|<
name|Processor
argument_list|>
operator|)
name|child
argument_list|)
expr_stmt|;
block|}
block|}
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
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
operator|.
name|tokenize
argument_list|(
literal|" "
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
