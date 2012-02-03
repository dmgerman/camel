begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|issues
package|;
end_package

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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|RemoveEndpointsTest
specifier|public
class|class
name|RemoveEndpointsTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testRemoveAllEndpoints ()
specifier|public
name|void
name|testRemoveAllEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:jms-queue"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|context
operator|.
name|getEndpoints
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|list
init|=
name|context
operator|.
name|removeEndpoints
argument_list|(
literal|"*"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// all have been removed
comment|// TODO: [CAMEL-4976] the following fails, but it should pass
comment|// assertEquals(0, context.getEndpoints().size());
comment|// assertNull(context.hasEndpoint("jms://topic:bar"));
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
name|ConnectionFactory
name|connectionFactory
init|=
name|CamelJmsTestHelper
operator|.
name|createConnectionFactory
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"jms"
argument_list|,
name|jmsComponentAutoAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
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
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jms:queue:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jms:queue:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:jms-queue"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jms:topic:bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:jms-topic"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:mem-queue"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:seda-queue"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

