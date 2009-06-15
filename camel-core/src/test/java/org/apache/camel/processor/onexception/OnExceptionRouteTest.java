begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.onexception
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|onexception
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|JndiRegistry
import|;
end_import

begin_comment
comment|/**  * Unit test inspired by user forum.  */
end_comment

begin_class
DECL|class|OnExceptionRouteTest
specifier|public
class|class
name|OnExceptionRouteTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|myOwnHandlerBean
specifier|private
name|MyOwnHandlerBean
name|myOwnHandlerBean
decl_stmt|;
DECL|field|myServiceBean
specifier|private
name|MyServiceBean
name|myServiceBean
decl_stmt|;
DECL|method|testNoError ()
specifier|public
name|void
name|testNoError
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<order><type>myType</type><user>James</user></order>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testFunctionalError ()
specifier|public
name|void
name|testFunctionalError
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
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
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<order><type>myType</type><user>Func</user></order>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<order><type>myType</type><user>Func</user></order>"
argument_list|,
name|myOwnHandlerBean
operator|.
name|getPayload
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTechnicalError ()
specifier|public
name|void
name|testTechnicalError
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
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
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<order><type>myType</type><user>Tech</user></order>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// should not handle it
name|assertNull
argument_list|(
name|myOwnHandlerBean
operator|.
name|getPayload
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testErrorWhileHandlingException ()
specifier|public
name|void
name|testErrorWhileHandlingException
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|1
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
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<order><type>myType</type><user>FuncError</user></order>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// should not handle it
name|assertNull
argument_list|(
name|myOwnHandlerBean
operator|.
name|getPayload
argument_list|()
argument_list|)
expr_stmt|;
comment|// and check that we have the caused exception stored
name|Exception
name|cause
init|=
name|error
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|IOException
operator|.
name|class
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Damn something did not work"
argument_list|,
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|myOwnHandlerBean
operator|=
operator|new
name|MyOwnHandlerBean
argument_list|()
expr_stmt|;
name|myServiceBean
operator|=
operator|new
name|MyServiceBean
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myOwnHandler"
argument_list|,
name|myOwnHandlerBean
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myServiceBean"
argument_list|,
name|myServiceBean
argument_list|)
expr_stmt|;
return|return
name|jndi
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
comment|// START SNIPPET: e1
comment|// default should errors go to mock:error
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliverDelay
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// if a MyTechnicalException is thrown we will not try to redeliver and we mark it as handled
comment|// so the caller does not get a failure
comment|// since we have no to then the exchange will continue to be routed to the normal error handler
comment|// destination that is mock:error as defined above
name|onException
argument_list|(
name|MyTechnicalException
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|0
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// if a MyFunctionalException is thrown we do not want Camel to redelivery but handle it our self using
comment|// our bean myOwnHandler, then the exchange is not routed to the default error (mock:error)
name|onException
argument_list|(
name|MyFunctionalException
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|0
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:myOwnHandler"
argument_list|)
expr_stmt|;
comment|// here we route message to our service bean
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"//type = 'myType'"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:myServiceBean"
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
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

