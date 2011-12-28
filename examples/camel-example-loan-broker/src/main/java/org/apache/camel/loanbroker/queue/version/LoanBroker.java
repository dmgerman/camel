begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.loanbroker.queue.version
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|loanbroker
operator|.
name|queue
operator|.
name|version
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
comment|/**  * Main class to start the loan broker server  */
end_comment

begin_class
DECL|class|LoanBroker
specifier|public
specifier|final
class|class
name|LoanBroker
block|{
DECL|method|LoanBroker ()
specifier|private
name|LoanBroker
parameter_list|()
block|{     }
comment|// START SNIPPET: starting
DECL|method|main (String... args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
modifier|...
name|args
parameter_list|)
throws|throws
name|Exception
block|{
comment|// setup an embedded JMS broker
name|JmsBroker
name|broker
init|=
operator|new
name|JmsBroker
argument_list|()
decl_stmt|;
name|broker
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// create a camel context
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
comment|// Set up the ActiveMQ JMS Components
name|ConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"tcp://localhost:51616"
argument_list|)
decl_stmt|;
comment|// Note we can explicitly name the component
name|context
operator|.
name|addComponent
argument_list|(
literal|"jms"
argument_list|,
name|JmsComponent
operator|.
name|jmsComponentAutoAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
expr_stmt|;
comment|// add the route
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|LoanBrokerRoute
argument_list|()
argument_list|)
expr_stmt|;
comment|// start Camel
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Server is ready"
argument_list|)
expr_stmt|;
comment|// let it run for 5 minutes before shutting down
name|Thread
operator|.
name|sleep
argument_list|(
literal|5
operator|*
literal|60
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|broker
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
comment|// END SNIPPET: starting
block|}
end_class

end_unit

