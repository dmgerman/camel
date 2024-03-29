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

begin_comment
comment|/**  * Test that chained request/reply over JMS works in parallel mode with the splitter EIP.  */
end_comment

begin_class
DECL|class|JmsSplitterParallelChainedTest
specifier|public
class|class
name|JmsSplitterParallelChainedTest
extends|extends
name|CamelTestSupport
block|{
DECL|method|getUri ()
specifier|protected
name|String
name|getUri
parameter_list|()
block|{
return|return
literal|"activemq:queue:foo"
return|;
block|}
DECL|method|getUri2 ()
specifier|protected
name|String
name|getUri2
parameter_list|()
block|{
return|return
literal|"activemq:queue:bar"
return|;
block|}
annotation|@
name|Test
DECL|method|testSplitParallel ()
specifier|public
name|void
name|testSplitParallel
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A,B,C,D,E"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:reply"
argument_list|)
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"Hi A"
argument_list|,
literal|"Hi B"
argument_list|,
literal|"Hi C"
argument_list|,
literal|"Hi D"
argument_list|,
literal|"Hi E"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:reply2"
argument_list|)
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"Bye Hi A"
argument_list|,
literal|"Bye Hi B"
argument_list|,
literal|"Bye Hi C"
argument_list|,
literal|"Bye Hi D"
argument_list|,
literal|"Bye Hi E"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"Bye Hi A"
argument_list|,
literal|"Bye Hi B"
argument_list|,
literal|"Bye Hi C"
argument_list|,
literal|"Bye Hi D"
argument_list|,
literal|"Bye Hi E"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A,B,C,D,E"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
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
name|CamelJmsTestHelper
operator|.
name|createConnectionFactory
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"activemq"
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
literal|"direct:start"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
operator|.
name|tokenize
argument_list|(
literal|","
argument_list|)
argument_list|)
operator|.
name|parallelProcessing
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:before"
argument_list|)
operator|.
name|to
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|getUri
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:after"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:split"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|getUri
argument_list|()
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"Hi "
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:reply"
argument_list|)
operator|.
name|to
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|getUri2
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|getUri2
argument_list|()
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"Bye "
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:reply2"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

