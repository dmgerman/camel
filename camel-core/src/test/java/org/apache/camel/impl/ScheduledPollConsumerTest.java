begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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

begin_class
DECL|class|ScheduledPollConsumerTest
specifier|public
class|class
name|ScheduledPollConsumerTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testExceptionOnPollAndCanStartAgain ()
specifier|public
name|void
name|testExceptionOnPollAndCanStartAgain
parameter_list|()
throws|throws
name|Exception
block|{
name|Exception
name|expectedException
init|=
operator|new
name|Exception
argument_list|(
literal|"Hello, I should be thrown on shutdown only!"
argument_list|)
decl_stmt|;
name|MockScheduledPollConsumer
name|consumer
init|=
operator|new
name|MockScheduledPollConsumer
argument_list|(
name|expectedException
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// poll that throws an exception
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// prepare for 2nd run but this time it should not thrown an exception on poll
name|consumer
operator|.
name|setExceptionToThrowOnPoll
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// start it again and we should be able to run
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
comment|// should be able to stop with no problem
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testNoExceptionOnPoll ()
specifier|public
name|void
name|testNoExceptionOnPoll
parameter_list|()
throws|throws
name|Exception
block|{
name|MockScheduledPollConsumer
name|consumer
init|=
operator|new
name|MockScheduledPollConsumer
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

