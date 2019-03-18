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
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

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
name|RuntimeCamelException
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
DECL|class|MailProducerUnsupportedCharsetTest
specifier|public
class|class
name|MailProducerUnsupportedCharsetTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testSencUnsupportedCharset ()
specifier|public
name|void
name|testSencUnsupportedCharset
parameter_list|()
throws|throws
name|Exception
block|{
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"pop3://jones@localhost?password=secret&consumer.initialDelay=100&consumer.delay=100&ignoreUnsupportedCharset=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
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
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|allMessages
argument_list|()
operator|.
name|header
argument_list|(
literal|"Content-Type"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"text/plain"
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
literal|"To"
argument_list|,
literal|"jones@localhost"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"smtp://localhost?ignoreUnsupportedCharset=true"
argument_list|,
literal|"Hello World"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|headers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"To"
argument_list|,
literal|"jones@localhost"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"text/plain; charset=ansi_x3.110-1983"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"smtp://localhost?ignoreUnsupportedCharset=true"
argument_list|,
literal|"Bye World"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSencUnsupportedCharsetDisabledOption ()
specifier|public
name|void
name|testSencUnsupportedCharsetDisabledOption
parameter_list|()
throws|throws
name|Exception
block|{
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"pop3://jones@localhost?password=secret&consumer.initialDelay=100&consumer.delay=100&ignoreUnsupportedCharset=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
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
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|allMessages
argument_list|()
operator|.
name|header
argument_list|(
literal|"Content-Type"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"text/plain"
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
literal|"To"
argument_list|,
literal|"jones@localhost"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"smtp://localhost?ignoreUnsupportedCharset=false"
argument_list|,
literal|"Hello World"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|headers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"To"
argument_list|,
literal|"jones@localhost"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"text/plain; charset=XXX"
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"smtp://localhost?ignoreUnsupportedCharset=false"
argument_list|,
literal|"Bye World"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|UnsupportedEncodingException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

