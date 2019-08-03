begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.issues
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
name|issues
package|;
end_package

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
DECL|class|JmsTransactedDeadLetterChannelNotHandlerRollbackOnExceptionTest
specifier|public
class|class
name|JmsTransactedDeadLetterChannelNotHandlerRollbackOnExceptionTest
extends|extends
name|JmsTransactedDeadLetterChannelHandlerRollbackOnExceptionTest
block|{
annotation|@
name|Override
DECL|method|isHandleNew ()
specifier|protected
name|boolean
name|isHandleNew
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|shouldNotLoseMessagesOnExceptionInErrorHandler ()
specifier|public
name|void
name|shouldNotLoseMessagesOnExceptionInErrorHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|testingEndpoint
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
comment|// as we do not handle new exception, then the exception propagates back
comment|// and causes the transaction to rollback, and we can find the message in the ActiveMQ DLQ
name|Object
name|dlqBody
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"activemq:ActiveMQ.DLQ"
argument_list|,
literal|2000
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|dlqBody
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

