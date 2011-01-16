begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

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
name|junit
operator|.
name|framework
operator|.
name|AssertionFailedError
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
name|mock
operator|.
name|MockEndpoint
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
name|DefaultHeaderFilterStrategy
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

begin_import
import|import static
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
operator|.
name|jmsComponentAutoAcknowledge
import|;
end_import

begin_comment
comment|/**  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsHeaderFilteringTest
specifier|public
class|class
name|JmsHeaderFilteringTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|IN_FILTER_PATTERN
specifier|private
specifier|static
specifier|final
name|String
name|IN_FILTER_PATTERN
init|=
literal|"(org_apache_camel)[_|a-z|A-Z|0-9]*(test)[_|a-z|A-Z|0-9]*"
decl_stmt|;
DECL|field|componentName
specifier|private
specifier|final
name|String
name|componentName
init|=
literal|"jms"
decl_stmt|;
DECL|field|testQueueEndpointA
specifier|private
specifier|final
name|String
name|testQueueEndpointA
init|=
name|componentName
operator|+
literal|":queue:test.a"
decl_stmt|;
DECL|field|testQueueEndpointB
specifier|private
specifier|final
name|String
name|testQueueEndpointB
init|=
name|componentName
operator|+
literal|":queue:test.b"
decl_stmt|;
DECL|field|assertionReceiver
specifier|private
specifier|final
name|String
name|assertionReceiver
init|=
literal|"mock:errors"
decl_stmt|;
DECL|field|latch
specifier|private
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|2
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testHeaderFilters ()
specifier|public
name|void
name|testHeaderFilters
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|errors
init|=
name|this
operator|.
name|resolveMandatoryEndpoint
argument_list|(
name|assertionReceiver
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|errors
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
name|testQueueEndpointA
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"org.apache.camel.jms"
argument_list|,
literal|10000
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"org.apache.camel.test.jms"
argument_list|,
literal|20000
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"testheader"
argument_list|,
literal|1020
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"anotherheader"
argument_list|,
literal|1030
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|errors
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
name|ConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://localhost?broker.persistent=false&broker.useJmx=false"
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
name|componentName
argument_list|,
name|jmsComponentAutoAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
expr_stmt|;
comment|// add "testheader" to in filter set
name|JmsComponent
name|component
init|=
operator|(
name|JmsComponent
operator|)
name|camelContext
operator|.
name|getComponent
argument_list|(
name|componentName
argument_list|)
decl_stmt|;
operator|(
operator|(
name|DefaultHeaderFilterStrategy
operator|)
name|component
operator|.
name|getHeaderFilterStrategy
argument_list|()
operator|)
operator|.
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"testheader"
argument_list|)
expr_stmt|;
comment|// add "anotherheader" to out filter set
operator|(
operator|(
name|DefaultHeaderFilterStrategy
operator|)
name|component
operator|.
name|getHeaderFilterStrategy
argument_list|()
operator|)
operator|.
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"anotherheader"
argument_list|)
expr_stmt|;
comment|// add a regular expression pattern filter
comment|// notice that dots are encoded to underscores in jms headers
operator|(
operator|(
name|DefaultHeaderFilterStrategy
operator|)
name|component
operator|.
name|getHeaderFilterStrategy
argument_list|()
operator|)
operator|.
name|setInFilterPattern
argument_list|(
name|IN_FILTER_PATTERN
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|onException
argument_list|(
name|AssertionFailedError
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|1
argument_list|)
operator|.
name|to
argument_list|(
name|assertionReceiver
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|testQueueEndpointA
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|OutHeaderChecker
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
name|testQueueEndpointB
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|testQueueEndpointB
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|InHeaderChecker
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|OutHeaderChecker
class|class
name|OutHeaderChecker
implements|implements
name|Processor
block|{
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|JmsMessage
name|message
init|=
operator|(
name|JmsMessage
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// testheader not filtered out until it is copied back to camel
name|assertEquals
argument_list|(
literal|1020
argument_list|,
name|message
operator|.
name|getJmsMessage
argument_list|()
operator|.
name|getObjectProperty
argument_list|(
literal|"testheader"
argument_list|)
argument_list|)
expr_stmt|;
comment|// anotherheader has been filtered out
name|assertNull
argument_list|(
name|message
operator|.
name|getJmsMessage
argument_list|()
operator|.
name|getObjectProperty
argument_list|(
literal|"anotherheader"
argument_list|)
argument_list|)
expr_stmt|;
comment|// notice dots are replaced by underscores when it is copied to jms message properties
name|assertEquals
argument_list|(
literal|10000
argument_list|,
name|message
operator|.
name|getJmsMessage
argument_list|()
operator|.
name|getObjectProperty
argument_list|(
literal|"org_apache_camel_jms"
argument_list|)
argument_list|)
expr_stmt|;
comment|// like testheader, org.apache.camel.test.jms will be filtered "in" filter
name|assertEquals
argument_list|(
literal|20000
argument_list|,
name|message
operator|.
name|getJmsMessage
argument_list|()
operator|.
name|getObjectProperty
argument_list|(
literal|"org_apache_camel_test_jms"
argument_list|)
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|InHeaderChecker
class|class
name|InHeaderChecker
implements|implements
name|Processor
block|{
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// filtered out by "in" filter
name|assertNull
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"testheader"
argument_list|)
argument_list|)
expr_stmt|;
comment|// it has been filtered out by "out" filter
name|assertNull
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"anotherheader"
argument_list|)
argument_list|)
expr_stmt|;
comment|// it should not been filtered out
name|assertEquals
argument_list|(
literal|10000
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"org.apache.camel.jms"
argument_list|)
argument_list|)
expr_stmt|;
comment|// filtered out by "in" filter
name|assertNull
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"org.apache.camel.test.jms"
argument_list|)
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

