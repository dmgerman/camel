begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.jmstofile
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|jmstofile
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
name|CamelContext
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
name|ProducerTemplate
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
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_comment
comment|/**  * An example class for demonstrating some of the basics behind Camel. This  * example sends some text messages on to a JMS Queue, consumes them and  * persists them to disk  */
end_comment

begin_class
DECL|class|CamelJmsToFileExample
specifier|public
specifier|final
class|class
name|CamelJmsToFileExample
block|{
DECL|method|CamelJmsToFileExample ()
specifier|private
name|CamelJmsToFileExample
parameter_list|()
block|{             }
DECL|method|main (String args[])
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e1
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
comment|// END SNIPPET: e1
comment|// Set up the ActiveMQ JMS Components
comment|// START SNIPPET: e2
name|ConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://localhost?broker.persistent=false"
argument_list|)
decl_stmt|;
comment|// Note we can explicit name the component
name|context
operator|.
name|addComponent
argument_list|(
literal|"test-jms"
argument_list|,
name|JmsComponent
operator|.
name|jmsComponentAutoAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e2
comment|// Add some configuration by hand ...
comment|// START SNIPPET: e3
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"test-jms:queue:test.queue"
argument_list|)
operator|.
name|to
argument_list|(
literal|"file://test"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e3
comment|// Camel template - a handy class for kicking off exchanges
comment|// START SNIPPET: e4
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
comment|// END SNIPPET: e4
comment|// Now everything is set up - lets start the context
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// Now send some test text to a component - for this case a JMS Queue
comment|// The text get converted to JMS messages - and sent to the Queue
comment|// test.queue
comment|// The file component is listening for messages from the Queue
comment|// test.queue, consumes
comment|// them and stores them to disk. The content of each file will be the
comment|// test we sent here.
comment|// The listener on the file component gets notified when new files are
comment|// found ... that's it!
comment|// START SNIPPET: e5
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"test-jms:queue:test.queue"
argument_list|,
literal|"Test Message: "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
comment|// END SNIPPET: e5
comment|// wait a bit and then stop
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

