begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ContextTestSupport
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
name|bean
operator|.
name|BeanProcessor
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
name|jmsComponentClientAcknowledge
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
name|JndiRegistry
import|;
end_import

begin_comment
comment|/**  * Unit test for sending the bean method name as a key over the JMS wire, that we now support this.  */
end_comment

begin_class
DECL|class|JmsBeanMethodHeaderTest
specifier|public
class|class
name|JmsBeanMethodHeaderTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testPlainHeader ()
specifier|public
name|void
name|testPlainHeader
parameter_list|()
throws|throws
name|Exception
block|{
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
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"foo"
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:in"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"foo"
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testUnderscoreHeader ()
specifier|public
name|void
name|testUnderscoreHeader
parameter_list|()
throws|throws
name|Exception
block|{
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
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"foo_bar"
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:in"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"foo_bar"
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testUsingBeanNoJMS ()
specifier|public
name|void
name|testUsingBeanNoJMS
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:approve"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Yes"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:approve"
argument_list|,
literal|"James"
argument_list|,
name|BeanProcessor
operator|.
name|METHOD_NAME
argument_list|,
literal|"approveLoan"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testUsingBeanAndJMS ()
specifier|public
name|void
name|testUsingBeanAndJMS
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:approve"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Yes"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:approve"
argument_list|,
literal|"James"
argument_list|,
name|BeanProcessor
operator|.
name|METHOD_NAME
argument_list|,
literal|"approveLoan"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testUsingJMStoJMStoBean ()
specifier|public
name|void
name|testUsingJMStoJMStoBean
parameter_list|()
throws|throws
name|Exception
block|{
comment|// the big one from jms to jms to test that we do not lost the bean method name
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:approve"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"No"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:queue"
argument_list|,
literal|"James"
argument_list|,
name|BeanProcessor
operator|.
name|METHOD_NAME
argument_list|,
literal|"approveSuperLoan"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
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
name|ConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://localhost?broker.persistent=false"
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"activemq"
argument_list|,
name|jmsComponentClientAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|reg
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|reg
operator|.
name|bind
argument_list|(
literal|"approveService"
argument_list|,
operator|new
name|ApproveService
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|reg
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
literal|"direct:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:test.a"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:test.a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:approve"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:approve"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:approve"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:approve"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:approveService"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:approve"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|ApproveService
specifier|public
specifier|static
class|class
name|ApproveService
block|{
DECL|method|doSomeStuff (String input)
specifier|public
name|void
name|doSomeStuff
parameter_list|(
name|String
name|input
parameter_list|)
block|{
comment|// just to confuse Camel with more public methods to choose among
block|}
DECL|method|approveLoan (@ody String body)
specifier|public
name|String
name|approveLoan
parameter_list|(
annotation|@
name|Body
name|String
name|body
parameter_list|)
block|{
return|return
literal|"Yes"
return|;
block|}
DECL|method|approveSuperLoan (@ody String body)
specifier|public
name|String
name|approveSuperLoan
parameter_list|(
annotation|@
name|Body
name|String
name|body
parameter_list|)
block|{
return|return
literal|"No"
return|;
block|}
block|}
block|}
end_class

end_unit

