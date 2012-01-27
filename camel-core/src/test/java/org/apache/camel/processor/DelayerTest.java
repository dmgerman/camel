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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|DelayerTest
specifier|public
class|class
name|DelayerTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|bean
specifier|private
name|MyDelayCalcBean
name|bean
init|=
operator|new
name|MyDelayCalcBean
argument_list|()
decl_stmt|;
DECL|method|testSendingMessageGetsDelayed ()
specifier|public
name|void
name|testSendingMessageGetsDelayed
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// do not wait for the first message
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|setResultWaitTime
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"seda:a"
argument_list|,
literal|"<hello>world!</hello>"
argument_list|,
literal|"MyDelay"
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
comment|// we should not receive it as we wait at most 0.5 sec and it take 1 sec to send
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// now if we wait a bit longer we should receive the message!
name|resultEndpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testDelayConstant ()
specifier|public
name|void
name|testDelayConstant
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// should at least take 1 sec to complete
name|resultEndpoint
operator|.
name|setMinimumResultWaitTime
argument_list|(
literal|900
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:b"
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testDelayBean ()
specifier|public
name|void
name|testDelayBean
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// should at least take 1 sec to complete
name|resultEndpoint
operator|.
name|setMinimumResultWaitTime
argument_list|(
literal|900
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:c"
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
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
comment|// START SNIPPET: ex
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|delay
argument_list|()
operator|.
name|header
argument_list|(
literal|"MyDelay"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: ex
comment|// START SNIPPET: ex2
name|from
argument_list|(
literal|"seda:b"
argument_list|)
operator|.
name|delay
argument_list|(
literal|1000
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: ex2
comment|// START SNIPPET: ex3
name|from
argument_list|(
literal|"seda:c"
argument_list|)
operator|.
name|delay
argument_list|()
operator|.
name|method
argument_list|(
name|bean
argument_list|,
literal|"delayMe"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: ex3
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

