begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
package|;
end_package

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
name|Endpoint
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
name|ExchangePattern
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
name|ResolveEndpointFailedException
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
DECL|class|SjmsEndpointTest
specifier|public
class|class
name|SjmsEndpointTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Test
DECL|method|testDefaults ()
specifier|public
name|void
name|testDefaults
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:test"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|SjmsEndpoint
argument_list|)
expr_stmt|;
name|SjmsEndpoint
name|sjms
init|=
operator|(
name|SjmsEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertEquals
argument_list|(
name|sjms
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
literal|"sjms://test"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|sjms
operator|.
name|createExchange
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testQueueEndpoint ()
specifier|public
name|void
name|testQueueEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|sjms
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|sjms
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|sjms
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
literal|"sjms://queue:test"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sjms
operator|instanceof
name|SjmsEndpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJndiStyleEndpointName ()
specifier|public
name|void
name|testJndiStyleEndpointName
parameter_list|()
throws|throws
name|Exception
block|{
name|SjmsEndpoint
name|sjms
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:/jms/test/hov.t1.dev:topic"
argument_list|,
name|SjmsEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|sjms
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sjms
operator|.
name|isTopic
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/jms/test/hov.t1.dev:topic"
argument_list|,
name|sjms
operator|.
name|getDestinationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSetTransacted ()
specifier|public
name|void
name|testSetTransacted
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test?transacted=true"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|SjmsEndpoint
argument_list|)
expr_stmt|;
name|SjmsEndpoint
name|qe
init|=
operator|(
name|SjmsEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertTrue
argument_list|(
name|qe
operator|.
name|isTransacted
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsyncProducer ()
specifier|public
name|void
name|testAsyncProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test?synchronous=true"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|SjmsEndpoint
argument_list|)
expr_stmt|;
name|SjmsEndpoint
name|qe
init|=
operator|(
name|SjmsEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertTrue
argument_list|(
name|qe
operator|.
name|isSynchronous
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNamedReplyTo ()
specifier|public
name|void
name|testNamedReplyTo
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|namedReplyTo
init|=
literal|"reply.to.queue"
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test?namedReplyTo="
operator|+
name|namedReplyTo
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|SjmsEndpoint
argument_list|)
expr_stmt|;
name|SjmsEndpoint
name|qe
init|=
operator|(
name|SjmsEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertEquals
argument_list|(
name|qe
operator|.
name|getNamedReplyTo
argument_list|()
argument_list|,
name|namedReplyTo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|qe
operator|.
name|createExchange
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultExchangePattern ()
specifier|public
name|void
name|testDefaultExchangePattern
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|SjmsEndpoint
name|sjms
init|=
operator|(
name|SjmsEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|sjms
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
name|sjms
operator|.
name|getExchangePattern
argument_list|()
argument_list|)
expr_stmt|;
comment|// assertTrue(sjms.createExchange().getPattern().equals(ExchangePattern.InOnly));
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Exception thrown: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testInOnlyExchangePattern ()
specifier|public
name|void
name|testInOnlyExchangePattern
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|Endpoint
name|sjms
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test?exchangePattern="
operator|+
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|sjms
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sjms
operator|.
name|createExchange
argument_list|()
operator|.
name|getPattern
argument_list|()
operator|.
name|equals
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Exception thrown: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testInOutExchangePattern ()
specifier|public
name|void
name|testInOutExchangePattern
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|Endpoint
name|sjms
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test?exchangePattern="
operator|+
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|sjms
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sjms
operator|.
name|createExchange
argument_list|()
operator|.
name|getPattern
argument_list|()
operator|.
name|equals
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Exception thrown: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ResolveEndpointFailedException
operator|.
name|class
argument_list|)
DECL|method|testUnsupportedMessageExchangePattern ()
specifier|public
name|void
name|testUnsupportedMessageExchangePattern
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test2?messageExchangePattern="
operator|+
name|ExchangePattern
operator|.
name|OutOnly
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNamedReplyToAndMEPMatch ()
specifier|public
name|void
name|testNamedReplyToAndMEPMatch
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|namedReplyTo
init|=
literal|"reply.to.queue"
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test?namedReplyTo="
operator|+
name|namedReplyTo
operator|+
literal|"&exchangePattern="
operator|+
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|SjmsEndpoint
argument_list|)
expr_stmt|;
name|SjmsEndpoint
name|qe
init|=
operator|(
name|SjmsEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertEquals
argument_list|(
name|qe
operator|.
name|getNamedReplyTo
argument_list|()
argument_list|,
name|namedReplyTo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|qe
operator|.
name|createExchange
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|Exception
operator|.
name|class
argument_list|)
DECL|method|testNamedReplyToAndMEPMismatch ()
specifier|public
name|void
name|testNamedReplyToAndMEPMismatch
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test?namedReplyTo=reply.to.queue&exchangePattern="
operator|+
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDestinationName ()
specifier|public
name|void
name|testDestinationName
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test?synchronous=true"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|SjmsEndpoint
argument_list|)
expr_stmt|;
name|SjmsEndpoint
name|qe
init|=
operator|(
name|SjmsEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertTrue
argument_list|(
name|qe
operator|.
name|isSynchronous
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTransactedBatchCountDefault ()
specifier|public
name|void
name|testTransactedBatchCountDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test?transacted=true"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|SjmsEndpoint
argument_list|)
expr_stmt|;
name|SjmsEndpoint
name|qe
init|=
operator|(
name|SjmsEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertTrue
argument_list|(
name|qe
operator|.
name|getTransactionBatchCount
argument_list|()
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTransactedBatchCountModified ()
specifier|public
name|void
name|testTransactedBatchCountModified
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test?transacted=true&transactionBatchCount=10"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|SjmsEndpoint
argument_list|)
expr_stmt|;
name|SjmsEndpoint
name|qe
init|=
operator|(
name|SjmsEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertTrue
argument_list|(
name|qe
operator|.
name|getTransactionBatchCount
argument_list|()
operator|==
literal|10
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTransactedBatchTimeoutDefault ()
specifier|public
name|void
name|testTransactedBatchTimeoutDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test?transacted=true"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|SjmsEndpoint
argument_list|)
expr_stmt|;
name|SjmsEndpoint
name|qe
init|=
operator|(
name|SjmsEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertTrue
argument_list|(
name|qe
operator|.
name|getTransactionBatchTimeout
argument_list|()
operator|==
literal|5000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTransactedBatchTimeoutModified ()
specifier|public
name|void
name|testTransactedBatchTimeoutModified
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test?transacted=true&transactionBatchTimeout=3000"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|SjmsEndpoint
argument_list|)
expr_stmt|;
name|SjmsEndpoint
name|qe
init|=
operator|(
name|SjmsEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertTrue
argument_list|(
name|qe
operator|.
name|getTransactionBatchTimeout
argument_list|()
operator|==
literal|3000
argument_list|)
expr_stmt|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|ActiveMQConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://broker?broker.persistent=false&broker.useJmx=false"
argument_list|)
decl_stmt|;
name|SjmsComponent
name|component
init|=
operator|new
name|SjmsComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setConnectionCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|component
operator|.
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"sjms"
argument_list|,
name|component
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
block|}
end_class

end_unit

