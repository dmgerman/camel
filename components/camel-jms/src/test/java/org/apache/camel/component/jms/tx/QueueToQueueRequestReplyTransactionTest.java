begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.tx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|tx
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
name|spi
operator|.
name|Policy
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
name|spring
operator|.
name|SpringRouteBuilder
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
name|spring
operator|.
name|spi
operator|.
name|SpringTransactionPolicy
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
comment|/**  * Test case derived from:  * http://camel.apache.org/transactional-client.html and Martin  * Krasser's sample:  * http://www.nabble.com/JMS-Transactions---How-To-td15168958s22882.html#a15198803  *<p/>  * NOTE: had to split into separate test classes as I was unable to fully tear  * down and isolate the test cases, I'm not sure why, but as soon as we know the  * Transaction classes can be joined into one.  */
end_comment

begin_class
DECL|class|QueueToQueueRequestReplyTransactionTest
specifier|public
class|class
name|QueueToQueueRequestReplyTransactionTest
extends|extends
name|AbstractTransactionTest
block|{
annotation|@
name|Test
DECL|method|testRollbackUsingXmlQueueToQueueRequestReplyUsingDynamicMessageSelector ()
specifier|public
name|void
name|testRollbackUsingXmlQueueToQueueRequestReplyUsingDynamicMessageSelector
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|ConditionalExceptionProcessor
name|cp
init|=
operator|new
name|ConditionalExceptionProcessor
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|SpringRouteBuilder
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
name|Policy
name|required
init|=
name|lookup
argument_list|(
literal|"PROPAGATION_REQUIRED_POLICY"
argument_list|,
name|SpringTransactionPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"activemq:queue:foo"
argument_list|)
operator|.
name|policy
argument_list|(
name|required
argument_list|)
operator|.
name|process
argument_list|(
name|cp
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq-1:queue:bar?replyTo=queue:bar.reply"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq-1:queue:bar"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|e
parameter_list|)
block|{
name|String
name|request
init|=
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|out
init|=
name|e
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|String
name|selectorValue
init|=
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"camelProvider"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|selectorValue
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|setHeader
argument_list|(
literal|"camelProvider"
argument_list|,
name|selectorValue
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|setBody
argument_list|(
literal|"Re: "
operator|+
name|request
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|5
condition|;
operator|++
name|i
control|)
block|{
name|Object
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"activemq:queue:foo"
argument_list|,
literal|"blah"
operator|+
name|i
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Received unexpeced reply"
argument_list|,
name|reply
operator|.
name|equals
argument_list|(
literal|"Re: blah"
operator|+
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cp
operator|.
name|getErrorMessage
argument_list|()
argument_list|,
name|cp
operator|.
name|getErrorMessage
argument_list|()
operator|==
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

