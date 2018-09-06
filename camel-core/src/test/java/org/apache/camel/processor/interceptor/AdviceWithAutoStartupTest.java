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
name|AdviceWithRouteBuilder
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|AdviceWithAutoStartupTest
specifier|public
class|class
name|AdviceWithAutoStartupTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testAdvised ()
specifier|public
name|void
name|testAdvised
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFalse
argument_list|(
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteDefinition
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|adviceWith
argument_list|(
name|context
argument_list|,
operator|new
name|AdviceWithRouteBuilder
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
name|replaceFromWith
argument_list|(
literal|"seda:newBar"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|startRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|context
operator|.
name|startRoute
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:newBar"
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
name|context
operator|.
name|setAutoStartup
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:newBar"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:bar"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:newBar"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

