begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.event
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|event
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
name|spi
operator|.
name|CamelEvent
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
DECL|class|EventNotifierExchangeSentParallelTest
specifier|public
class|class
name|EventNotifierExchangeSentParallelTest
extends|extends
name|EventNotifierExchangeSentTest
block|{
annotation|@
name|Test
DECL|method|testExchangeSentRecipient ()
specifier|public
name|void
name|testExchangeSentRecipient
parameter_list|()
throws|throws
name|Exception
block|{
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
name|sendBodyAndHeader
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"foo"
argument_list|,
literal|"direct:cool,direct:start"
argument_list|)
expr_stmt|;
comment|// wait for the message to be fully done using oneExchangeDone
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|oneExchangeDone
operator|.
name|matchesMockWaitTime
argument_list|()
argument_list|)
expr_stmt|;
comment|// stop Camel to let all the events complete
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be 11 or more, was: "
operator|+
name|events
operator|.
name|size
argument_list|()
argument_list|,
name|events
operator|.
name|size
argument_list|()
operator|>=
literal|11
argument_list|)
expr_stmt|;
comment|// we run parallel so just assert we got 6 sending and 6 sent events
name|int
name|sent
init|=
literal|0
decl_stmt|;
name|int
name|sending
init|=
literal|0
decl_stmt|;
for|for
control|(
name|CamelEvent
name|event
range|:
name|events
control|)
block|{
if|if
condition|(
name|event
operator|instanceof
name|ExchangeSendingEvent
condition|)
block|{
name|sending
operator|++
expr_stmt|;
block|}
else|else
block|{
name|sent
operator|++
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"There should be 5 or more, was "
operator|+
name|sending
argument_list|,
name|sending
operator|>=
literal|5
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"There should be 5 or more, was "
operator|+
name|sent
argument_list|,
name|sent
operator|>=
literal|5
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
name|to
argument_list|(
literal|"log:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bar"
argument_list|)
operator|.
name|delay
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|recipientList
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|parallelProcessing
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:cool"
argument_list|)
operator|.
name|delay
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:tap"
argument_list|)
operator|.
name|wireTap
argument_list|(
literal|"log:foo"
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
