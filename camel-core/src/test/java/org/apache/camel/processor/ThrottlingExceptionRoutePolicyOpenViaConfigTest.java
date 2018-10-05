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
name|throttling
operator|.
name|ThrottlingExceptionRoutePolicy
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
DECL|class|ThrottlingExceptionRoutePolicyOpenViaConfigTest
specifier|public
class|class
name|ThrottlingExceptionRoutePolicyOpenViaConfigTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|url
specifier|private
name|String
name|url
init|=
literal|"seda:foo?concurrentConsumers=20"
decl_stmt|;
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
DECL|field|size
specifier|private
name|int
name|size
init|=
literal|5
decl_stmt|;
DECL|field|policy
specifier|private
name|ThrottlingExceptionRoutePolicy
name|policy
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
name|this
operator|.
name|createPolicy
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|this
operator|.
name|setUseRouteBuilder
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|result
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeout
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|createPolicy ()
specifier|protected
name|void
name|createPolicy
parameter_list|()
block|{
name|int
name|threshold
init|=
literal|2
decl_stmt|;
name|long
name|failureWindow
init|=
literal|30
decl_stmt|;
name|long
name|halfOpenAfter
init|=
literal|100
decl_stmt|;
name|boolean
name|keepOpen
init|=
literal|false
decl_stmt|;
name|policy
operator|=
operator|new
name|ThrottlingExceptionRoutePolicy
argument_list|(
name|threshold
argument_list|,
name|failureWindow
argument_list|,
name|halfOpenAfter
argument_list|,
literal|null
argument_list|,
name|keepOpen
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testThrottlingRoutePolicyStartWithAlwaysOpenOffThenToggle ()
specifier|public
name|void
name|testThrottlingRoutePolicyStartWithAlwaysOpenOffThenToggle
parameter_list|()
throws|throws
name|Exception
block|{
comment|// send first set of messages
comment|// should go through b/c circuit is closed
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|url
argument_list|,
literal|"MessageRound1 "
operator|+
name|i
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|expectedMessageCount
argument_list|(
name|size
argument_list|)
expr_stmt|;
name|result
operator|.
name|setResultWaitTime
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// set keepOpen to true
name|policy
operator|.
name|setKeepOpen
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// trigger opening circuit
comment|// by sending another message
name|template
operator|.
name|sendBody
argument_list|(
name|url
argument_list|,
literal|"MessageTrigger"
argument_list|)
expr_stmt|;
comment|// give time for circuit to open
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
comment|// send next set of messages
comment|// should NOT go through b/c circuit is open
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|url
argument_list|,
literal|"MessageRound2 "
operator|+
name|i
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
comment|// gives time for policy half open check to run every second
comment|// and should not close b/c keepOpen is true
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
name|size
operator|+
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|setResultWaitTime
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// set keepOpen to false
name|policy
operator|.
name|setKeepOpen
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// gives time for policy half open check to run every second
comment|// and it should close b/c keepOpen is false
name|result
operator|.
name|expectedMessageCount
argument_list|(
name|size
operator|*
literal|2
operator|+
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|setResultWaitTime
argument_list|(
literal|1000
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
name|url
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|log
argument_list|(
literal|"${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo?groupSize=10"
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

