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
name|xbean
operator|.
name|spring
operator|.
name|context
operator|.
name|ClassPathXmlApplicationContext
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * Test case derived from:  * http://camel.apache.org/transactional-client.html and Martin  * Krasser's sample:  * http://www.nabble.com/JMS-Transactions---How-To-td15168958s22882.html#a15198803  * NOTE: had to split into separate test classes as I was unable to fully tear  * down and isolate the test cases, I'm not sure why, but as soon as we know the  * Transaction classes can be joined into one.  */
end_comment

begin_class
DECL|class|XMLQueueToQueueTransactionTest
specifier|public
class|class
name|XMLQueueToQueueTransactionTest
extends|extends
name|AbstractTransactionTest
block|{
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/jms/tx/XMLQueueToQueueTransactionTest.xml"
argument_list|)
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
comment|// routes should have been configured via xml and added to the camel context
name|assertResult
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

