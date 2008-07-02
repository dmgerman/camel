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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
operator|.
name|UseLatestAggregationStrategy
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
name|Main
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * The LoanBroker is a RouteBuilder which builds the whole loan message routing rules  *  * @version $  */
end_comment

begin_class
DECL|class|LoanBroker
specifier|public
class|class
name|LoanBroker
extends|extends
name|RouteBuilder
block|{
comment|/**      * A main() so we can easily run these routing rules in our IDE      * @throws Exception      */
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
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
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
comment|// Set up the ActiveMQ JMS Components
name|ConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"tcp://localhost:61616"
argument_list|)
decl_stmt|;
comment|// Note we can explicitly name the component
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
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|LoanBroker
argument_list|()
argument_list|)
expr_stmt|;
comment|// Start the loan broker
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
comment|/**      * Lets configure the Camel routing rules using Java code...      */
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// START SNIPPET: dsl
comment|// Put the message from loanRequestQueue to the creditRequestQueue
name|from
argument_list|(
literal|"test-jms:queue:loanRequestQueue"
argument_list|)
operator|.
name|to
argument_list|(
literal|"test-jms:queue:creditRequestQueue"
argument_list|)
expr_stmt|;
comment|// Now we can let the CreditAgency process the request, then the message will be put into creditResponseQueue
name|from
argument_list|(
literal|"test-jms:queue:creditRequestQueue"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|CreditAgency
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"test-jms:queue:creditResponseQueue"
argument_list|)
expr_stmt|;
comment|// Here we use the multicast pattern to send the message to three different bank queue
name|from
argument_list|(
literal|"test-jms:queue:creditResponseQueue"
argument_list|)
operator|.
name|multicast
argument_list|()
operator|.
name|to
argument_list|(
literal|"test-jms:queue:bank1"
argument_list|,
literal|"test-jms:queue:bank2"
argument_list|,
literal|"test-jms:queue:bank3"
argument_list|)
expr_stmt|;
comment|// Each bank processor will process the message and put the response message into the bankReplyQueue
name|from
argument_list|(
literal|"test-jms:queue:bank1"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Bank
argument_list|(
literal|"bank1"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"test-jms:queue:bankReplyQueue"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"test-jms:queue:bank2"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Bank
argument_list|(
literal|"bank2"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"test-jms:queue:bankReplyQueue"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"test-jms:queue:bank3"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Bank
argument_list|(
literal|"bank3"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"test-jms:queue:bankReplyQueue"
argument_list|)
expr_stmt|;
comment|// Now we aggregating the response message by using the Constants.PROPERTY_SSN header
comment|// The aggregation will completed when all the three bank responses are received
name|from
argument_list|(
literal|"test-jms:queue:bankReplyQueue"
argument_list|)
operator|.
name|aggregator
argument_list|(
name|header
argument_list|(
name|Constants
operator|.
name|PROPERTY_SSN
argument_list|)
argument_list|,
operator|new
name|BankResponseAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|completedPredicate
argument_list|(
name|header
argument_list|(
literal|"aggregated"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|3
argument_list|)
argument_list|)
comment|// Here we do some translation and put the message back to loanReplyQueue
operator|.
name|process
argument_list|(
operator|new
name|Translator
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"test-jms:queue:loanReplyQueue"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: dsl
block|}
block|}
end_class

end_unit

