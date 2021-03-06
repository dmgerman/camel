begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.directvm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|directvm
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
name|impl
operator|.
name|DefaultCamelContext
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
name|service
operator|.
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|DirectVmConsumerExpressionTest
specifier|public
class|class
name|DirectVmConsumerExpressionTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|context2
specifier|private
name|CamelContext
name|context2
decl_stmt|;
DECL|field|context3
specifier|private
name|CamelContext
name|context3
decl_stmt|;
DECL|field|context4
specifier|private
name|CamelContext
name|context4
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|context2
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context3
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context4
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|context2
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|context3
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|context4
argument_list|)
expr_stmt|;
comment|// add routes after CamelContext has been started
name|RouteBuilder
name|routeBuilder
init|=
name|createRouteBuilderCamelContext2
argument_list|()
decl_stmt|;
if|if
condition|(
name|routeBuilder
operator|!=
literal|null
condition|)
block|{
name|context2
operator|.
name|addRoutes
argument_list|(
name|routeBuilder
argument_list|)
expr_stmt|;
block|}
name|routeBuilder
operator|=
name|createRouteBuilderCamelContext3
argument_list|()
expr_stmt|;
if|if
condition|(
name|routeBuilder
operator|!=
literal|null
condition|)
block|{
name|context3
operator|.
name|addRoutes
argument_list|(
name|routeBuilder
argument_list|)
expr_stmt|;
block|}
name|routeBuilder
operator|=
name|createRouteBuilderCamelContext4
argument_list|()
expr_stmt|;
if|if
condition|(
name|routeBuilder
operator|!=
literal|null
condition|)
block|{
name|context4
operator|.
name|addRoutes
argument_list|(
name|routeBuilder
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|context2
argument_list|,
name|context3
argument_list|,
name|context4
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSelectEndpoint ()
specifier|public
name|void
name|testSelectEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|result2
init|=
name|context2
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result2"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|result2
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|result3
init|=
name|context3
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result3"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|result3
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|result4
init|=
name|context4
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result4"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|result4
operator|.
name|expectedMessageCount
argument_list|(
literal|0
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
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|context2
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|context3
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|context4
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|recipientList
argument_list|(
operator|new
name|DirectVmConsumerExpression
argument_list|(
literal|"direct-vm://parent/**/context*"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createRouteBuilderCamelContext2 ()
specifier|private
name|RouteBuilder
name|createRouteBuilderCamelContext2
parameter_list|()
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
literal|"direct-vm:parent/child/context2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result2"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createRouteBuilderCamelContext3 ()
specifier|private
name|RouteBuilder
name|createRouteBuilderCamelContext3
parameter_list|()
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
literal|"direct-vm:parent/child/grandchild/context3"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result3"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createRouteBuilderCamelContext4 ()
specifier|private
name|RouteBuilder
name|createRouteBuilderCamelContext4
parameter_list|()
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
literal|"direct-vm:parent/child/ctx4"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result4"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

