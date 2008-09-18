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
name|junit
operator|.
name|framework
operator|.
name|Assert
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
name|Exchange
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
DECL|method|testExceptionOnPollGetsThrownOnShutdown ()
specifier|public
name|void
name|testExceptionOnPollGetsThrownOnShutdown
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
name|Exception
name|actualException
init|=
literal|null
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
comment|// exception is caught and saved
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
try|try
block|{
comment|// exception should be thrown
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|actualException
operator|=
name|e
expr_stmt|;
block|}
comment|// make sure its the right exception!
name|Assert
operator|.
name|assertEquals
argument_list|(
name|expectedException
argument_list|,
name|actualException
argument_list|)
expr_stmt|;
block|}
DECL|method|testNoExceptionOnPollAndNoneThrownOnShutdown ()
specifier|public
name|void
name|testNoExceptionOnPollAndNoneThrownOnShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|Exception
name|actualException
init|=
literal|null
decl_stmt|;
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
try|try
block|{
comment|// exception should not be thrown
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|actualException
operator|=
name|e
expr_stmt|;
block|}
comment|// make sure no exception was thrown
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|actualException
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

