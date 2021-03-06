begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Body
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
name|Header
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

begin_class
DECL|class|RequestReplyCorrelatedWithCustomHeaderTest
specifier|public
class|class
name|RequestReplyCorrelatedWithCustomHeaderTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|connectionFactory
specifier|private
name|ConnectionFactory
name|connectionFactory
decl_stmt|;
DECL|method|processRequest (@ody final String body, @Header(R) final String customCorrelation, @Header(R) final String jmsCorrelationId, final Exchange exchange)
specifier|public
specifier|static
name|void
name|processRequest
parameter_list|(
annotation|@
name|Body
specifier|final
name|String
name|body
parameter_list|,
annotation|@
name|Header
argument_list|(
literal|"CustomCorrelation"
argument_list|)
specifier|final
name|String
name|customCorrelation
parameter_list|,
annotation|@
name|Header
argument_list|(
literal|"JMSCorrelationId"
argument_list|)
specifier|final
name|String
name|jmsCorrelationId
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|customCorrelation
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|jmsCorrelationId
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hi, "
operator|+
name|body
operator|+
literal|", "
operator|+
name|customCorrelation
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldCorrelateRepliesWithCustomCorrelationProperty ()
specifier|public
name|void
name|shouldCorrelateRepliesWithCustomCorrelationProperty
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"activemq:queue:request"
argument_list|,
literal|"Bobby"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|reply
operator|.
name|matches
argument_list|(
literal|"Hi, Bobby, Camel-.*"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldCorrelateRepliesWithCustomCorrelationPropertyAndValue ()
specifier|public
name|void
name|shouldCorrelateRepliesWithCustomCorrelationPropertyAndValue
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|reply
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"activemq:queue:request"
argument_list|,
literal|"Bobby"
argument_list|,
literal|"CustomCorrelation"
argument_list|,
literal|"custom-id"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hi, Bobby, custom-id"
argument_list|,
name|reply
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
specifier|final
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|connectionFactory
operator|=
name|CamelJmsTestHelper
operator|.
name|createConnectionFactory
argument_list|()
expr_stmt|;
specifier|final
name|JmsComponent
name|activeMq
init|=
name|jmsComponentAutoAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
decl_stmt|;
name|activeMq
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setCorrelationProperty
argument_list|(
literal|"CustomCorrelation"
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"activemq"
argument_list|,
name|activeMq
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"activemq:queue:request"
argument_list|)
operator|.
name|bean
argument_list|(
name|RequestReplyCorrelatedWithCustomHeaderTest
operator|.
name|class
argument_list|,
literal|"processRequest"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

