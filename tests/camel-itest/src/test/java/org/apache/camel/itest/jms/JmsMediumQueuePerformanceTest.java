begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQConnectionFactory
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
name|jms
operator|.
name|JmsComponent
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
name|util
operator|.
name|jndi
operator|.
name|JndiContext
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|JmsMediumQueuePerformanceTest
specifier|public
class|class
name|JmsMediumQueuePerformanceTest
extends|extends
name|JmsPerformanceTest
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JmsMediumQueuePerformanceTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|mediumQueueCount
specifier|protected
name|int
name|mediumQueueCount
init|=
literal|1000
decl_stmt|;
annotation|@
name|Override
DECL|method|getActiveMQFileName ()
specifier|protected
name|String
name|getActiveMQFileName
parameter_list|()
block|{
comment|// using different port number to avoid clash
return|return
literal|"activemq8.xml"
return|;
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiContext
name|answer
init|=
operator|new
name|JndiContext
argument_list|()
decl_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"myBean"
argument_list|,
name|myBean
argument_list|)
expr_stmt|;
comment|// add ActiveMQ client
name|ConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://broker8"
argument_list|)
decl_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"activemq"
argument_list|,
name|JmsComponent
operator|.
name|jmsComponentAutoAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
annotation|@
name|Test
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Sending {} messages first"
argument_list|,
name|mediumQueueCount
argument_list|)
expr_stmt|;
name|sendLoop
argument_list|(
literal|0
argument_list|,
name|mediumQueueCount
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Sent..."
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Now testing"
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

