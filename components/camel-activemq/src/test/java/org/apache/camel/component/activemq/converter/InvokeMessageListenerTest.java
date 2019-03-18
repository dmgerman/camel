begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.activemq.converter
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
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TextMessage
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
name|spring
operator|.
name|ConsumerBean
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
name|activemq
operator|.
name|ActiveMQComponent
operator|.
name|activeMQComponent
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
DECL|class|InvokeMessageListenerTest
specifier|public
class|class
name|InvokeMessageListenerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
DECL|field|startEndpointUri
specifier|protected
name|String
name|startEndpointUri
init|=
literal|"activemq:queue:test.a"
decl_stmt|;
DECL|field|listener
specifier|protected
name|ConsumerBean
name|listener
init|=
operator|new
name|ConsumerBean
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendTextMessage ()
specifier|public
name|void
name|testSendTextMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedBody
init|=
literal|"Hello there!"
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|startEndpointUri
argument_list|,
name|expectedBody
argument_list|,
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|listener
operator|.
name|assertMessagesArrived
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Message
argument_list|>
name|list
init|=
name|listener
operator|.
name|flushMessages
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should have received some messages!"
argument_list|,
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Received: "
operator|+
name|message
argument_list|)
expr_stmt|;
name|TextMessage
name|textMessage
init|=
name|assertIsInstanceOf
argument_list|(
name|TextMessage
operator|.
name|class
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Text mesage body: "
operator|+
name|textMessage
argument_list|,
name|expectedBody
argument_list|,
name|textMessage
operator|.
name|getText
argument_list|()
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
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"activemq"
argument_list|,
name|activeMQComponent
argument_list|(
literal|"vm://localhost?broker.persistent=false"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
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
name|from
argument_list|(
name|startEndpointUri
argument_list|)
operator|.
name|bean
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

