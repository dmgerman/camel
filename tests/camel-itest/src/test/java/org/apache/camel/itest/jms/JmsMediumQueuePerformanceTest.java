begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|jms
package|;
end_package

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsMediumQueuePerformanceTest
specifier|public
class|class
name|JmsMediumQueuePerformanceTest
extends|extends
name|JmsPerformanceTest
block|{
DECL|field|mediumQueueCount
specifier|protected
name|int
name|mediumQueueCount
init|=
literal|1000
decl_stmt|;
annotation|@
name|Override
DECL|method|testSendingAndReceivingMessages ()
specifier|public
name|void
name|testSendingAndReceivingMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|expected
init|=
name|mediumQueueCount
operator|+
name|messageCount
decl_stmt|;
name|setExpectedMessageCount
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sending "
operator|+
name|mediumQueueCount
operator|+
literal|" messages first"
argument_list|)
expr_stmt|;
name|sendLoop
argument_list|(
literal|0
argument_list|,
name|mediumQueueCount
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sent!"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
comment|// TODO now start the route!
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Now testing!"
argument_list|)
expr_stmt|;
name|timedSendLoop
argument_list|(
name|mediumQueueCount
argument_list|,
name|expected
argument_list|)
expr_stmt|;
name|assertExpectedMessagesReceived
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

