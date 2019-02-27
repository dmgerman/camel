begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.sqs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|sqs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|model
operator|.
name|Message
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
name|impl
operator|.
name|JndiRegistry
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

begin_class
DECL|class|SqsConcurrentConsumerTest
specifier|public
class|class
name|SqsConcurrentConsumerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|NUM_CONCURRENT
specifier|private
specifier|static
specifier|final
name|int
name|NUM_CONCURRENT
init|=
literal|10
decl_stmt|;
DECL|field|NUM_MESSAGES
specifier|private
specifier|static
specifier|final
name|int
name|NUM_MESSAGES
init|=
literal|100
decl_stmt|;
DECL|field|threadNumbers
specifier|final
name|Set
argument_list|<
name|Long
argument_list|>
name|threadNumbers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|consumeMessagesFromQueue ()
specifier|public
name|void
name|consumeMessagesFromQueue
parameter_list|()
throws|throws
name|Exception
block|{
comment|// simple test to make sure that concurrent consumers were used in the test
name|NotifyBuilder
name|notifier
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenCompleted
argument_list|(
name|NUM_MESSAGES
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"We didn't process "
operator|+
name|NUM_MESSAGES
operator|+
literal|" messages as we expected!"
argument_list|,
name|notifier
operator|.
name|matches
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|isPlatform
argument_list|(
literal|"windows"
argument_list|)
condition|)
block|{
comment|// threading is different on windows
block|}
else|else
block|{
comment|// usually we use all threads evenly but sometimes threads are reused so just test that 50%+ was used
if|if
condition|(
name|threadNumbers
operator|.
name|size
argument_list|()
operator|<
operator|(
name|NUM_CONCURRENT
operator|/
literal|2
operator|)
condition|)
block|{
name|fail
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"We were expecting to have about half of %d numbers of concurrent consumers, but only found %d"
argument_list|,
name|NUM_CONCURRENT
argument_list|,
name|threadNumbers
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|reg
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|AmazonSQSClientMock
name|client
init|=
operator|new
name|AmazonSQSClientMock
argument_list|()
decl_stmt|;
name|createDummyMessages
argument_list|(
name|client
argument_list|,
name|NUM_MESSAGES
argument_list|)
expr_stmt|;
name|reg
operator|.
name|bind
argument_list|(
literal|"client"
argument_list|,
name|client
argument_list|)
expr_stmt|;
return|return
name|reg
return|;
block|}
DECL|method|createDummyMessages (AmazonSQSClientMock client, int numMessages)
specifier|private
name|void
name|createDummyMessages
parameter_list|(
name|AmazonSQSClientMock
name|client
parameter_list|,
name|int
name|numMessages
parameter_list|)
block|{
for|for
control|(
name|int
name|counter
init|=
literal|0
init|;
name|counter
operator|<
name|numMessages
condition|;
name|counter
operator|++
control|)
block|{
name|Message
name|message
init|=
operator|new
name|Message
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
literal|"Message "
operator|+
name|counter
argument_list|)
expr_stmt|;
name|message
operator|.
name|setMD5OfBody
argument_list|(
literal|"6a1559560f67c5e7a7d5d838bf0272ee"
argument_list|)
expr_stmt|;
name|message
operator|.
name|setMessageId
argument_list|(
literal|"f6fb6f99-5eb2-4be4-9b15-144774141458"
argument_list|)
expr_stmt|;
name|message
operator|.
name|setReceiptHandle
argument_list|(
literal|"0NNAq8PwvXsyZkR6yu4nQ07FGxNmOBWi5"
argument_list|)
expr_stmt|;
name|client
operator|.
name|messages
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
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
literal|"aws-sqs://demo?concurrentConsumers="
operator|+
name|NUM_CONCURRENT
operator|+
literal|"&maxMessagesPerPoll=10&amazonSQSClient=#client"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|threadNumbers
operator|.
name|add
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|log
argument_list|(
literal|"processed a new message!"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit
