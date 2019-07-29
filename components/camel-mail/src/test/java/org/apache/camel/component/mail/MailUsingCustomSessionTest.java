begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Session
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
name|BindToRegistry
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
name|RoutesBuilder
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
name|jvnet
operator|.
name|mock_javamail
operator|.
name|Mailbox
import|;
end_import

begin_class
DECL|class|MailUsingCustomSessionTest
specifier|public
class|class
name|MailUsingCustomSessionTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"myCustomMailSession"
argument_list|)
DECL|field|mailSession
specifier|private
name|Session
name|mailSession
init|=
name|Session
operator|.
name|getInstance
argument_list|(
operator|new
name|Properties
argument_list|()
argument_list|)
decl_stmt|;
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
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointConfigurationWithCustomSession ()
specifier|public
name|void
name|testEndpointConfigurationWithCustomSession
parameter_list|()
block|{
comment|// Verify that the mail session bound to the bean registry is identical
comment|// to the session tied to the endpoint configuration
name|assertSame
argument_list|(
name|mailSession
argument_list|,
name|getEndpointMailSession
argument_list|(
literal|"smtp://james@localhost?session=#myCustomMailSession"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendAndReceiveMailsWithCustomSession ()
specifier|public
name|void
name|testSendAndReceiveMailsWithCustomSession
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"hello camel!"
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
literal|"subject"
argument_list|,
literal|"Hello Camel"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"smtp://james@localhost?session=#myCustomMailSession"
argument_list|,
literal|"hello camel!"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Mailbox
name|mailbox
init|=
name|Mailbox
operator|.
name|get
argument_list|(
literal|"james@localhost"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Expected one mail for james@localhost"
argument_list|,
literal|1
argument_list|,
name|mailbox
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|mailbox
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"hello camel!"
argument_list|,
name|message
operator|.
name|getContent
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"camel@localhost"
argument_list|,
name|message
operator|.
name|getFrom
argument_list|()
index|[
literal|0
index|]
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getEndpointMailSession (String uri)
specifier|private
name|Session
name|getEndpointMailSession
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|MailEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|,
name|MailEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSession
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
literal|"pop3://james@localhost?session=#myCustomMailSession&consumer.initialDelay=100&consumer.delay=100"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

