begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.slack
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|slack
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
name|apache
operator|.
name|http
operator|.
name|HttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|HttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpPost
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|StringEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|HttpClients
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assume
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

begin_class
DECL|class|SlackConsumerTest
specifier|public
class|class
name|SlackConsumerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|token
specifier|private
name|String
name|token
decl_stmt|;
DECL|field|hook
specifier|private
name|String
name|hook
decl_stmt|;
annotation|@
name|Override
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
name|token
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"SLACK_TOKEN"
argument_list|)
expr_stmt|;
name|hook
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"SLACK_HOOK"
argument_list|,
literal|"https://hooks.slack.com/services/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"
argument_list|)
expr_stmt|;
name|assumeCredentials
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumePrefixedMessages ()
specifier|public
name|void
name|testConsumePrefixedMessages
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|message
init|=
literal|"Hi camel"
decl_stmt|;
name|sendMessage
argument_list|(
name|message
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
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|simple
argument_list|(
literal|"${body.getText()}"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|assumeCredentials ()
specifier|private
name|void
name|assumeCredentials
parameter_list|()
block|{
name|Assume
operator|.
name|assumeThat
argument_list|(
literal|"Please specify a Slack access token"
argument_list|,
name|token
argument_list|,
name|CoreMatchers
operator|.
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
name|Assume
operator|.
name|assumeThat
argument_list|(
literal|"Please specify a Slack application webhook URL"
argument_list|,
name|hook
argument_list|,
name|CoreMatchers
operator|.
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|sendMessage (String message)
specifier|private
name|void
name|sendMessage
parameter_list|(
name|String
name|message
parameter_list|)
throws|throws
name|IOException
block|{
name|HttpClient
name|client
init|=
name|HttpClients
operator|.
name|createDefault
argument_list|()
decl_stmt|;
name|HttpPost
name|post
init|=
operator|new
name|HttpPost
argument_list|(
name|hook
argument_list|)
decl_stmt|;
name|post
operator|.
name|setHeader
argument_list|(
literal|"Content-type"
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|post
operator|.
name|setEntity
argument_list|(
operator|new
name|StringEntity
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"{ 'text': '%s'}"
argument_list|,
name|message
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|client
operator|.
name|execute
argument_list|(
name|post
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_OK
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
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
name|String
operator|.
name|format
argument_list|(
literal|"slack://general?token=RAW(%s)&maxResults=1"
argument_list|,
name|token
argument_list|)
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

