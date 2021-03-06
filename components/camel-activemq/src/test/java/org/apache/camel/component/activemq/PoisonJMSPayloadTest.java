begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.activemq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|activemq
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageProducer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ObjectMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
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
name|test
operator|.
name|spring
operator|.
name|CamelSpringTestSupport
import|;
end_import

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
name|AbstractApplicationContext
import|;
end_import

begin_class
DECL|class|PoisonJMSPayloadTest
specifier|public
class|class
name|PoisonJMSPayloadTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Test
DECL|method|testCreateBodyThrowException ()
specifier|public
name|void
name|testCreateBodyThrowException
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result-activemq"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Poison JMS message payload: Failed to extract body due to: javax.jms.JMSException: Failed to build body from content. Serializable class not available to broker."
argument_list|)
expr_stmt|;
name|ActiveMQConnectionFactory
name|factory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://localhost"
argument_list|)
decl_stmt|;
name|Connection
name|conn
init|=
name|factory
operator|.
name|createConnection
argument_list|()
decl_stmt|;
name|conn
operator|.
name|start
argument_list|()
expr_stmt|;
name|Session
name|sess
init|=
name|conn
operator|.
name|createSession
argument_list|(
literal|false
argument_list|,
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
decl_stmt|;
name|MessageProducer
name|producer
init|=
name|sess
operator|.
name|createProducer
argument_list|(
name|sess
operator|.
name|createTopic
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
decl_stmt|;
name|ObjectMessage
name|msg
init|=
name|sess
operator|.
name|createObjectMessage
argument_list|()
decl_stmt|;
name|ObjectPayload
name|payload
init|=
operator|new
name|ObjectPayload
argument_list|()
decl_stmt|;
name|payload
operator|.
name|payload
operator|=
literal|"test"
expr_stmt|;
name|msg
operator|.
name|setObject
argument_list|(
name|payload
argument_list|)
expr_stmt|;
name|producer
operator|.
name|send
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// bean should not be invoked
name|boolean
name|invoked
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
literal|"myBean"
argument_list|,
name|MyBean
operator|.
name|class
argument_list|)
operator|.
name|isInvoked
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Bean should not be invoked"
argument_list|,
name|invoked
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
name|AbstractApplicationContext
name|context
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/activemq/jms-createbody.xml"
argument_list|)
decl_stmt|;
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

