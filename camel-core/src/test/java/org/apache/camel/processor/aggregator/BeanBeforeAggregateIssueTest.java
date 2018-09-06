begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregator
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
name|TimeUnit
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
name|builder
operator|.
name|NotifyBuilder
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
name|processor
operator|.
name|BodyInAggregatingStrategy
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
name|aggregate
operator|.
name|MemoryAggregationRepository
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
DECL|class|BeanBeforeAggregateIssueTest
specifier|public
class|class
name|BeanBeforeAggregateIssueTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|myRepo
specifier|private
name|MyAggRepo
name|myRepo
init|=
operator|new
name|MyAggRepo
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testBeanBeforeAggregation ()
specifier|public
name|void
name|testBeanBeforeAggregation
parameter_list|()
throws|throws
name|Exception
block|{
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|3
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A+B"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:start"
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:start"
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// wait for all exchanges to be done (2 input + 1 aggregated)
name|notify
operator|.
name|matches
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
comment|// should have confirmed
name|assertTrue
argument_list|(
literal|"Should have confirmed"
argument_list|,
name|myRepo
operator|.
name|isConfirm
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
name|from
argument_list|(
literal|"seda:start"
argument_list|)
operator|.
name|bean
argument_list|(
name|TestBean
operator|.
name|class
argument_list|)
operator|.
name|aggregate
argument_list|(
name|constant
argument_list|(
literal|"true"
argument_list|)
argument_list|,
operator|new
name|BodyInAggregatingStrategy
argument_list|()
argument_list|)
operator|.
name|aggregationRepository
argument_list|(
name|myRepo
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|2
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
DECL|class|TestBean
specifier|public
specifier|static
specifier|final
class|class
name|TestBean
block|{
DECL|method|doNothing (String foo)
specifier|public
name|String
name|doNothing
parameter_list|(
name|String
name|foo
parameter_list|)
block|{
return|return
name|foo
return|;
block|}
block|}
DECL|class|MyAggRepo
specifier|private
specifier|static
specifier|final
class|class
name|MyAggRepo
extends|extends
name|MemoryAggregationRepository
block|{
DECL|field|confirm
specifier|private
specifier|volatile
name|boolean
name|confirm
decl_stmt|;
annotation|@
name|Override
DECL|method|confirm (CamelContext camelContext, String exchangeId)
specifier|public
name|void
name|confirm
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|exchangeId
parameter_list|)
block|{
comment|// test that confirm is invoked
name|super
operator|.
name|confirm
argument_list|(
name|camelContext
argument_list|,
name|exchangeId
argument_list|)
expr_stmt|;
name|confirm
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|isConfirm ()
specifier|public
name|boolean
name|isConfirm
parameter_list|()
block|{
return|return
name|confirm
return|;
block|}
block|}
block|}
end_class

end_unit

