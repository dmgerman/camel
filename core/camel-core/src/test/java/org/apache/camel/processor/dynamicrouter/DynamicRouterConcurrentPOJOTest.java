begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.dynamicrouter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|dynamicrouter
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
name|DynamicRouter
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
name|Header
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

begin_class
DECL|class|DynamicRouterConcurrentPOJOTest
specifier|public
class|class
name|DynamicRouterConcurrentPOJOTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|COUNT
specifier|private
specifier|static
specifier|final
name|int
name|COUNT
init|=
literal|100
decl_stmt|;
annotation|@
name|Test
DECL|method|testConcurrentDynamicRouter ()
specifier|public
name|void
name|testConcurrentDynamicRouter
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|COUNT
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|COUNT
argument_list|)
expr_stmt|;
name|Thread
name|sendToSedaA
init|=
name|createSedaSenderThread
argument_list|(
literal|"seda:a"
argument_list|)
decl_stmt|;
name|Thread
name|sendToSedaB
init|=
name|createSedaSenderThread
argument_list|(
literal|"seda:b"
argument_list|)
decl_stmt|;
name|sendToSedaA
operator|.
name|start
argument_list|()
expr_stmt|;
name|sendToSedaB
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|createSedaSenderThread (final String seda)
specifier|private
name|Thread
name|createSedaSenderThread
parameter_list|(
specifier|final
name|String
name|seda
parameter_list|)
block|{
return|return
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|COUNT
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|seda
argument_list|,
literal|"Message from "
operator|+
name|seda
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
return|;
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
literal|"seda:a"
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|MyDynamicRouterPojo
argument_list|(
literal|"mock:a"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:b"
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|MyDynamicRouterPojo
argument_list|(
literal|"mock:b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyDynamicRouterPojo
specifier|public
class|class
name|MyDynamicRouterPojo
block|{
DECL|field|target
specifier|private
specifier|final
name|String
name|target
decl_stmt|;
DECL|method|MyDynamicRouterPojo (String target)
specifier|public
name|MyDynamicRouterPojo
parameter_list|(
name|String
name|target
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
block|}
annotation|@
name|DynamicRouter
DECL|method|route (@eaderExchange.SLIP_ENDPOINT) String previous)
specifier|public
name|String
name|route
parameter_list|(
annotation|@
name|Header
argument_list|(
name|Exchange
operator|.
name|SLIP_ENDPOINT
argument_list|)
name|String
name|previous
parameter_list|)
block|{
if|if
condition|(
name|previous
operator|==
literal|null
condition|)
block|{
return|return
name|target
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

