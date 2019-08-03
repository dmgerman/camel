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
name|Handler
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
name|CamelJmsTestHelper
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
name|jmsComponentTransacted
import|;
end_import

begin_class
DECL|class|JmsTransactedOnExceptionRollbackOnExceptionTest
specifier|public
class|class
name|JmsTransactedOnExceptionRollbackOnExceptionTest
extends|extends
name|CamelTestSupport
block|{
DECL|class|BadErrorHandler
specifier|public
specifier|static
class|class
name|BadErrorHandler
block|{
annotation|@
name|Handler
DECL|method|onException (Exchange exchange, Exception exception)
specifier|public
name|void
name|onException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Exception
name|exception
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"error in errorhandler"
argument_list|)
throw|;
block|}
block|}
DECL|field|testingEndpoint
specifier|protected
specifier|final
name|String
name|testingEndpoint
init|=
literal|"activemq:test."
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// we attempt to handle the exception but if it throw a new exception
comment|// then it causes the JMS transaction to rollback
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|bean
argument_list|(
name|BadErrorHandler
operator|.
name|class
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|testingEndpoint
argument_list|)
operator|.
name|log
argument_list|(
literal|"Incoming JMS message ${body}"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|RuntimeException
argument_list|(
literal|"bad error"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
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
comment|// no redeliveries
name|ConnectionFactory
name|connectionFactory
init|=
name|CamelJmsTestHelper
operator|.
name|createConnectionFactory
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|JmsComponent
name|component
init|=
name|jmsComponentTransacted
argument_list|(
name|connectionFactory
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"activemq"
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

