begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.tx
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
name|tx
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
name|EndpointInject
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
name|Produce
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
name|ProducerTemplate
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
name|spring
operator|.
name|SpringRouteBuilder
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
name|AbstractXmlApplicationContext
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|JMXTXUseOriginalBodyWithTXErrorHandlerTest
specifier|public
class|class
name|JMXTXUseOriginalBodyWithTXErrorHandlerTest
extends|extends
name|JMXTXUseOriginalBodyTest
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:end"
argument_list|)
DECL|field|endpoint
specifier|protected
name|MockEndpoint
name|endpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:error"
argument_list|)
DECL|field|error
specifier|protected
name|MockEndpoint
name|error
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:checkpoint1"
argument_list|)
DECL|field|checkpoint1
specifier|protected
name|MockEndpoint
name|checkpoint1
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:checkpoint2"
argument_list|)
DECL|field|checkpoint2
specifier|protected
name|MockEndpoint
name|checkpoint2
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"activemq:start"
argument_list|)
DECL|field|start
specifier|protected
name|ProducerTemplate
name|start
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"activemq:broken"
argument_list|)
DECL|field|broken
specifier|protected
name|ProducerTemplate
name|broken
decl_stmt|;
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"/org/apache/camel/component/jms/tx/JMXTXUseOriginalBodyWithTXErrorHandlerTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testWithConstant ()
specifier|public
name|void
name|testWithConstant
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|error
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|checkpoint1
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|checkpoint2
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"oh no"
argument_list|)
expr_stmt|;
name|start
operator|.
name|sendBody
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWithBean ()
specifier|public
name|void
name|testWithBean
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|error
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|checkpoint1
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|checkpoint2
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"oh no"
argument_list|)
expr_stmt|;
name|broken
operator|.
name|sendBody
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|class|FooBean
specifier|public
specifier|static
class|class
name|FooBean
block|{
annotation|@
name|Handler
DECL|method|process (@ody String body)
specifier|public
name|String
name|process
parameter_list|(
annotation|@
name|Body
name|String
name|body
parameter_list|)
block|{
return|return
literal|"oh no"
return|;
block|}
block|}
DECL|class|TestRoutes
specifier|public
specifier|static
class|class
name|TestRoutes
extends|extends
name|SpringRouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|errorHandler
argument_list|(
name|transactionErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
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
name|useOriginalMessage
argument_list|()
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:error"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:broken"
argument_list|)
operator|.
name|transacted
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:checkpoint1"
argument_list|)
operator|.
name|setBody
argument_list|(
name|bean
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:checkpoint2"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|Exception
argument_list|(
literal|"boo"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:start"
argument_list|)
operator|.
name|transacted
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:checkpoint1"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"oh no"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:checkpoint2"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|Exception
argument_list|(
literal|"boo"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

