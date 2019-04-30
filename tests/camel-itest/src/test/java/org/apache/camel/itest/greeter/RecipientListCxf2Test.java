begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.greeter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|greeter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|component
operator|.
name|cxf
operator|.
name|common
operator|.
name|message
operator|.
name|CxfConstants
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
name|spring
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
name|AbstractApplicationContext
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

begin_class
DECL|class|RecipientListCxf2Test
specifier|public
class|class
name|RecipientListCxf2Test
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|port1
specifier|private
specifier|static
name|int
name|port1
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|20012
argument_list|)
decl_stmt|;
DECL|field|port2
specifier|private
specifier|static
name|int
name|port2
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|20023
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
literal|"RecipientListCxf2Test.port1"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|port1
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"RecipientListCxf2Test.port2"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|port2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:reply"
argument_list|)
DECL|field|replyEndpoint
specifier|protected
name|MockEndpoint
name|replyEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:reply2"
argument_list|)
DECL|field|reply2Endpoint
specifier|protected
name|MockEndpoint
name|reply2Endpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:output"
argument_list|)
DECL|field|outputEndpoint
specifier|protected
name|MockEndpoint
name|outputEndpoint
decl_stmt|;
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/itest/greeter/RecipientListCxf2Test-context.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testRecipientListCXF2 ()
specifier|public
name|void
name|testRecipientListCXF2
parameter_list|()
throws|throws
name|Exception
block|{
name|replyEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Willem"
argument_list|,
literal|"Hello Jonathan"
argument_list|,
literal|"Hello Freeman"
argument_list|)
expr_stmt|;
name|reply2Endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Claus"
argument_list|,
literal|"Bye Jonathan"
argument_list|,
literal|"Bye Freeman"
argument_list|)
expr_stmt|;
name|outputEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Willem"
argument_list|,
literal|"Bye Claus"
argument_list|,
literal|"Bye Jonathan"
argument_list|,
literal|"Hello Freeman"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
literal|"greetMe"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"cxf:bean:clientEndpoint?address=http://localhost:"
operator|+
name|port1
operator|+
literal|"/SoapContext/SoapPort"
argument_list|)
expr_stmt|;
comment|// returns the last message from the recipient list
name|Object
name|out
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Willem"
argument_list|,
name|headers
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Willem"
argument_list|,
name|out
argument_list|)
expr_stmt|;
comment|// change foo headers
name|headers
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"cxf:bean:clientEndpoint?address=http://localhost:"
operator|+
name|port2
operator|+
literal|"/SoapContext/SoapPort"
argument_list|)
expr_stmt|;
comment|// call again to ensure that works also
comment|// returns the last message from the recipient list
name|String
name|out2
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Claus"
argument_list|,
name|headers
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye Claus"
argument_list|,
name|out2
argument_list|)
expr_stmt|;
comment|// change foo headers again
name|headers
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"cxf:bean:clientEndpoint?address=http://localhost:"
operator|+
name|port1
operator|+
literal|"/SoapContext/SoapPort"
operator|+
literal|",cxf:bean:clientEndpoint?address=http://localhost:"
operator|+
name|port2
operator|+
literal|"/SoapContext/SoapPort"
argument_list|)
expr_stmt|;
comment|// and call again to ensure that it really works also
comment|// returns the last message from the recipient list
name|String
name|out3
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Jonathan"
argument_list|,
name|headers
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye Jonathan"
argument_list|,
name|out3
argument_list|)
expr_stmt|;
comment|// change foo headers again
name|headers
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"cxf:bean:clientEndpoint?address=http://localhost:"
operator|+
name|port2
operator|+
literal|"/SoapContext/SoapPort"
operator|+
literal|",cxf:bean:clientEndpoint?address=http://localhost:"
operator|+
name|port1
operator|+
literal|"/SoapContext/SoapPort"
argument_list|)
expr_stmt|;
comment|// and call again to ensure that it really works also
comment|// returns the last message from the recipient list
name|String
name|out4
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Freeman"
argument_list|,
name|headers
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Freeman"
argument_list|,
name|out4
argument_list|)
expr_stmt|;
name|replyEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|reply2Endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|outputEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

