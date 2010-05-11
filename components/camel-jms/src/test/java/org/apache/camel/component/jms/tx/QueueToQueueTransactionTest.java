begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Test case derived from:  * http://camel.apache.org/transactional-client.html and Martin  * Krasser's sample:  * http://www.nabble.com/JMS-Transactions---How-To-td15168958s22882.html#a15198803  * NOTE: had to split into separate test classes as I was unable to fully tear  * down and isolate the test cases, I'm not sure why, but as soon as we know the  * Transaction classes can be joined into one.  */
end_comment

begin_class
DECL|class|QueueToQueueTransactionTest
specifier|public
class|class
name|QueueToQueueTransactionTest
extends|extends
name|AbstractTransactionTest
block|{
DECL|method|getExpectedRouteCount ()
specifier|protected
name|int
name|getExpectedRouteCount
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Test
DECL|method|testRollbackUsingXmlQueueToQueue ()
specifier|public
name|void
name|testRollbackUsingXmlQueueToQueue
parameter_list|()
throws|throws
name|Exception
block|{
comment|// configure routes and add to camel context
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
literal|"activemq:queue:foo?transacted=true"
argument_list|)
operator|.
name|policy
argument_list|(
name|required
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|ConditionalExceptionProcessor
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:queue:bar?transacted=true"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertResult
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

