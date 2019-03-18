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
name|Endpoint
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
name|EventDrivenConsumerRoute
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
name|interceptor
operator|.
name|DefaultChannel
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

begin_class
DECL|class|ResequencerTest
specifier|public
class|class
name|ResequencerTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|startEndpoint
specifier|protected
name|Endpoint
name|startEndpoint
decl_stmt|;
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendMessagesInWrongOrderButReceiveThemInCorrectOrder ()
specifier|public
name|void
name|testSendMessagesInWrongOrderButReceiveThemInCorrectOrder
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Guillaume"
argument_list|,
literal|"Hiram"
argument_list|,
literal|"James"
argument_list|,
literal|"Rob"
argument_list|)
expr_stmt|;
name|sendBodies
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Rob"
argument_list|,
literal|"Hiram"
argument_list|,
literal|"Guillaume"
argument_list|,
literal|"James"
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
name|resultEndpoint
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
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
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
comment|// use jmx only when running the following test(s)
return|return
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"testBatchResequencerTypeWithJmx"
argument_list|)
return|;
block|}
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
comment|// START SNIPPET: example
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|resequence
argument_list|()
operator|.
name|body
argument_list|()
operator|.
name|timeout
argument_list|(
literal|50
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testBatchResequencerTypeWithJmx ()
specifier|public
name|void
name|testBatchResequencerTypeWithJmx
parameter_list|()
throws|throws
name|Exception
block|{
name|testBatchResequencerTypeWithoutJmx
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBatchResequencerTypeWithoutJmx ()
specifier|public
name|void
name|testBatchResequencerTypeWithoutJmx
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|list
init|=
name|getRouteList
argument_list|(
name|createRouteBuilder
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number of routes created: "
operator|+
name|list
argument_list|,
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Route
name|route
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|EventDrivenConsumerRoute
name|consumerRoute
init|=
name|assertIsInstanceOf
argument_list|(
name|EventDrivenConsumerRoute
operator|.
name|class
argument_list|,
name|route
argument_list|)
decl_stmt|;
name|DefaultChannel
name|channel
init|=
name|assertIsInstanceOf
argument_list|(
name|DefaultChannel
operator|.
name|class
argument_list|,
name|unwrapChannel
argument_list|(
name|consumerRoute
operator|.
name|getProcessor
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|DefaultErrorHandler
operator|.
name|class
argument_list|,
name|channel
operator|.
name|getErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|Resequencer
operator|.
name|class
argument_list|,
name|channel
operator|.
name|getNextProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

