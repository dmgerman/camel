begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Route
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
name|LoadBalanceDefinition
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
name|SendDefinition
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
name|channel
operator|.
name|DefaultChannel
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
name|loadbalancer
operator|.
name|LoadBalancer
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
name|loadbalancer
operator|.
name|RandomLoadBalancer
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
comment|/**  * A crude unit test to navigate the route and build a Java DSL from the route  * definition  */
end_comment

begin_class
DECL|class|RandomLoadBalanceJavaDSLBuilderTest
specifier|public
class|class
name|RandomLoadBalanceJavaDSLBuilderTest
extends|extends
name|RandomLoadBalanceTest
block|{
annotation|@
name|Test
DECL|method|testNavigateRouteAsJavaDSLWithNavigate ()
specifier|public
name|void
name|testNavigateRouteAsJavaDSLWithNavigate
parameter_list|()
throws|throws
name|Exception
block|{
comment|// this one navigate using the runtime route using the
comment|// Navigate<Processor>
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Route
name|route
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
decl_stmt|;
comment|// the start of the route
name|sb
operator|.
name|append
argument_list|(
literal|"from(\""
operator|+
name|route
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
operator|+
literal|"\")"
argument_list|)
expr_stmt|;
comment|// navigate the route and add Java DSL to the sb
name|Navigate
argument_list|<
name|Processor
argument_list|>
name|nav
init|=
name|route
operator|.
name|navigate
argument_list|()
decl_stmt|;
name|navigateRoute
argument_list|(
name|nav
argument_list|,
name|sb
argument_list|)
expr_stmt|;
comment|// output the Java DSL
name|assertEquals
argument_list|(
literal|"from(\"direct://start\").loadBalance().random().to(\"mock://x\").to(\"mock://y\").to(\"mock://z\")"
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNavigateRouteAsJavaDSL ()
specifier|public
name|void
name|testNavigateRouteAsJavaDSL
parameter_list|()
throws|throws
name|Exception
block|{
comment|// this one navigate using the route definition
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|RouteDefinition
name|route
init|=
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// the start of the route
name|sb
operator|.
name|append
argument_list|(
literal|"from(\""
operator|+
name|route
operator|.
name|getInput
argument_list|()
operator|.
name|getUri
argument_list|()
operator|+
literal|"\")"
argument_list|)
expr_stmt|;
comment|// navigate the route and add Java DSL to the sb
name|navigateDefinition
argument_list|(
name|route
argument_list|,
name|sb
argument_list|)
expr_stmt|;
comment|// output the Java DSL
name|assertEquals
argument_list|(
literal|"from(\"direct://start\").loadBalance().random().to(\"mock://x\").to(\"mock://y\").to(\"mock://z\")"
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|navigateRoute (Navigate<Processor> nav, StringBuilder sb)
specifier|private
name|void
name|navigateRoute
parameter_list|(
name|Navigate
argument_list|<
name|Processor
argument_list|>
name|nav
parameter_list|,
name|StringBuilder
name|sb
parameter_list|)
block|{
if|if
condition|(
name|nav
operator|instanceof
name|Pipeline
condition|)
block|{
name|nav
operator|=
call|(
name|Navigate
argument_list|<
name|Processor
argument_list|>
call|)
argument_list|(
operator|(
name|Pipeline
operator|)
name|nav
argument_list|)
operator|.
name|getProcessors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
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
operator|instanceof
name|DefaultChannel
condition|)
block|{
name|DefaultChannel
name|channel
init|=
operator|(
name|DefaultChannel
operator|)
name|nav
decl_stmt|;
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|def
init|=
operator|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
operator|)
name|channel
operator|.
name|getProcessorDefinition
argument_list|()
decl_stmt|;
name|navigateDefinition
argument_list|(
name|def
argument_list|,
name|sb
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|navigateDefinition (ProcessorDefinition<?> def, StringBuilder sb)
specifier|private
name|void
name|navigateDefinition
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|def
parameter_list|,
name|StringBuilder
name|sb
parameter_list|)
block|{
comment|// must do this ugly cast to avoid compiler error on HP-UX
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|defn
init|=
operator|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
operator|)
name|def
decl_stmt|;
if|if
condition|(
name|defn
operator|instanceof
name|LoadBalanceDefinition
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|".loadBalance()"
argument_list|)
expr_stmt|;
name|LoadBalanceDefinition
name|lbd
init|=
operator|(
name|LoadBalanceDefinition
operator|)
name|defn
decl_stmt|;
name|LoadBalancer
name|balancer
init|=
name|lbd
operator|.
name|getLoadBalancerType
argument_list|()
operator|.
name|getLoadBalancer
argument_list|()
decl_stmt|;
if|if
condition|(
name|balancer
operator|instanceof
name|RandomLoadBalancer
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|".random()"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|defn
operator|instanceof
name|SendDefinition
condition|)
block|{
name|SendDefinition
argument_list|<
name|?
argument_list|>
name|send
init|=
operator|(
name|SendDefinition
argument_list|<
name|?
argument_list|>
operator|)
name|defn
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|".to(\""
operator|+
name|send
operator|.
name|getUri
argument_list|()
operator|+
literal|"\")"
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|children
init|=
name|defn
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
if|if
condition|(
name|children
operator|==
literal|null
operator|||
name|children
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|child
range|:
name|children
control|)
block|{
name|navigateDefinition
argument_list|(
name|child
argument_list|,
name|sb
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

