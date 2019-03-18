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
name|jms
operator|.
name|JmsEndpoint
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
name|Before
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
name|jms
operator|.
name|connection
operator|.
name|JmsTransactionManager
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
comment|/**  *  */
end_comment

begin_class
DECL|class|ActiveMQRouteTest
specifier|public
class|class
name|ActiveMQRouteTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|EXPECTED_BODY
specifier|private
specifier|static
specifier|final
name|String
name|EXPECTED_BODY
init|=
literal|"Hello there!"
decl_stmt|;
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
annotation|@
name|Test
DECL|method|testJmsRouteWithTextMessage ()
specifier|public
name|void
name|testJmsRouteWithTextMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|EXPECTED_BODY
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"cheese"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|sendExchange
argument_list|(
name|EXPECTED_BODY
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|sendExchange (final Object expectedBody)
specifier|protected
name|void
name|sendExchange
parameter_list|(
specifier|final
name|Object
name|expectedBody
parameter_list|)
block|{
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
block|}
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|=
operator|(
name|MockEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
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
comment|// START SNIPPET: example
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
comment|// END SNIPPET: example
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
name|to
argument_list|(
literal|"activemq:queue:test.b"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:test.b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|JmsEndpoint
name|endpoint1
init|=
operator|(
name|JmsEndpoint
operator|)
name|endpoint
argument_list|(
literal|"activemq:topic:quote.IONA"
argument_list|)
decl_stmt|;
name|endpoint1
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setTransactionManager
argument_list|(
operator|new
name|JmsTransactionManager
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint1
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setTransacted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|endpoint1
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:transactedClient"
argument_list|)
expr_stmt|;
name|JmsEndpoint
name|endpoint2
init|=
operator|(
name|JmsEndpoint
operator|)
name|endpoint
argument_list|(
literal|"activemq:topic:quote.IONA"
argument_list|)
decl_stmt|;
name|endpoint2
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setTransacted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|endpoint2
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:nonTrasnactedClient"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

