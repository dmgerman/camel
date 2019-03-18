begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
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
name|Collection
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
import|;
end_import

begin_comment
comment|/**  * Tests the WaitStrategy and ClaimStrategy configuration of the disruptor component  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|value
operator|=
name|Parameterized
operator|.
name|class
argument_list|)
DECL|class|DisruptorWaitClaimStrategyComponentTest
specifier|public
class|class
name|DisruptorWaitClaimStrategyComponentTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|VALUE
specifier|private
specifier|static
specifier|final
name|Integer
name|VALUE
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
literal|42
argument_list|)
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Produce
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|producerType
specifier|private
specifier|final
name|String
name|producerType
decl_stmt|;
DECL|field|waitStrategy
specifier|private
specifier|final
name|String
name|waitStrategy
decl_stmt|;
DECL|field|disruptorUri
specifier|private
name|String
name|disruptorUri
decl_stmt|;
DECL|method|DisruptorWaitClaimStrategyComponentTest (final String waitStrategy, final String producerType)
specifier|public
name|DisruptorWaitClaimStrategyComponentTest
parameter_list|(
specifier|final
name|String
name|waitStrategy
parameter_list|,
specifier|final
name|String
name|producerType
parameter_list|)
block|{
name|this
operator|.
name|waitStrategy
operator|=
name|waitStrategy
expr_stmt|;
name|this
operator|.
name|producerType
operator|=
name|producerType
expr_stmt|;
block|}
annotation|@
name|Parameters
DECL|method|strategies ()
specifier|public
specifier|static
name|Collection
argument_list|<
name|String
index|[]
argument_list|>
name|strategies
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|String
index|[]
argument_list|>
name|strategies
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|DisruptorWaitStrategy
name|waitStrategy
range|:
name|DisruptorWaitStrategy
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
specifier|final
name|DisruptorProducerType
name|producerType
range|:
name|DisruptorProducerType
operator|.
name|values
argument_list|()
control|)
block|{
name|strategies
operator|.
name|add
argument_list|(
operator|new
name|String
index|[]
block|{
name|waitStrategy
operator|.
name|name
argument_list|()
block|,
name|producerType
operator|.
name|name
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|strategies
return|;
block|}
annotation|@
name|Test
DECL|method|testProduce ()
specifier|public
name|void
name|testProduce
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|VALUE
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|asyncSendBody
argument_list|(
name|disruptorUri
argument_list|,
name|VALUE
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
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
name|disruptorUri
operator|=
literal|"disruptor:test?waitStrategy="
operator|+
name|waitStrategy
operator|+
literal|"&producerType="
operator|+
name|producerType
expr_stmt|;
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
name|disruptorUri
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

