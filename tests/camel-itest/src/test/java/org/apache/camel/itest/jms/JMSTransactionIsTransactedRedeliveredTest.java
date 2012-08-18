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
name|AdviceWithRouteBuilder
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
name|test
operator|.
name|AvailablePortFinder
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
name|CamelSpringTestSupport
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
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * Test that exchange.isExternalRedelivered() is kept around even when  * Message implementation changes from JmsMessage to DefaultMessage, when routing  * from JMS over Jetty.  */
end_comment

begin_class
DECL|class|JMSTransactionIsTransactedRedeliveredTest
specifier|public
class|class
name|JMSTransactionIsTransactedRedeliveredTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|port
specifier|private
specifier|static
name|int
name|port
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|20039
argument_list|)
decl_stmt|;
static|static
block|{
comment|//set them as system properties so Spring can use the property placeholder
comment|//things to set them into the URL's in the spring contexts
name|System
operator|.
name|setProperty
argument_list|(
literal|"Jetty.port"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createApplicationContext ()
specifier|protected
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"/org/apache/camel/itest/jms/JMSTransactionIsTransactedRedeliveredTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isUseAdviceWith ()
specifier|public
name|boolean
name|isUseAdviceWith
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Test
DECL|method|testTransactionSuccess ()
specifier|public
name|void
name|testTransactionSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|adviceWith
argument_list|(
name|context
argument_list|,
operator|new
name|AdviceWithRouteBuilder
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
name|onException
argument_list|(
name|AssertionError
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:error"
argument_list|,
literal|"mock:error"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// there should be no assertion errors
name|MockEndpoint
name|error
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
decl_stmt|;
name|error
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
comment|// success at 3rd attempt
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"count"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|MockEndpoint
name|jetty
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:jetty"
argument_list|)
decl_stmt|;
name|jetty
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"activemq:queue:okay"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|jetty
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|error
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|class|MyBeforeProcessor
specifier|public
specifier|static
class|class
name|MyBeforeProcessor
implements|implements
name|Processor
block|{
DECL|field|count
specifier|private
name|int
name|count
decl_stmt|;
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
operator|++
name|count
expr_stmt|;
comment|// the first is not redelivered
if|if
condition|(
name|count
operator|==
literal|1
condition|)
block|{
name|assertFalse
argument_list|(
literal|"Should not be external redelivered"
argument_list|,
name|exchange
operator|.
name|isExternalRedelivered
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertTrue
argument_list|(
literal|"Should be external redelivered"
argument_list|,
name|exchange
operator|.
name|isExternalRedelivered
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|count
operator|<
literal|3
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Forced exception"
argument_list|)
throw|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"count"
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|MyAfterProcessor
specifier|public
specifier|static
class|class
name|MyAfterProcessor
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
comment|// origin message should be a external redelivered
name|assertTrue
argument_list|(
literal|"Should be external redelivered"
argument_list|,
name|exchange
operator|.
name|isExternalRedelivered
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

