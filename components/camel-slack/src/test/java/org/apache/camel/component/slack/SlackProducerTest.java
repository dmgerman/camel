begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|direct
operator|.
name|DirectEndpoint
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

begin_class
DECL|class|SlackProducerTest
specifier|public
class|class
name|SlackProducerTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:errors"
argument_list|)
DECL|field|errors
name|MockEndpoint
name|errors
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"direct:test"
argument_list|)
DECL|field|test
name|DirectEndpoint
name|test
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"direct:error"
argument_list|)
DECL|field|error
name|DirectEndpoint
name|error
decl_stmt|;
annotation|@
name|Test
DECL|method|testSlackMessage ()
specifier|public
name|void
name|testSlackMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|errors
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
name|test
argument_list|,
literal|"Hello from Camel!"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSlackError ()
specifier|public
name|void
name|testSlackError
parameter_list|()
throws|throws
name|Exception
block|{
name|errors
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
name|error
argument_list|,
literal|"Error from Camel!"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
block|{
name|SlackComponent
name|slack
init|=
operator|new
name|SlackComponent
argument_list|()
decl_stmt|;
name|slack
operator|.
name|setWebhookUrl
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"SLACK_HOOK"
argument_list|,
literal|"https://hooks.slack.com/services/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"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"slack"
argument_list|,
name|slack
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
name|to
argument_list|(
name|errors
argument_list|)
expr_stmt|;
specifier|final
name|String
name|slacUser
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"SLACK_USER"
argument_list|,
literal|"CamelTest"
argument_list|)
decl_stmt|;
name|from
argument_list|(
name|test
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"slack:#general?iconEmoji=:camel:&username=%s"
argument_list|,
name|slacUser
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|error
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"slack:#badchannel?iconEmoji=:camel:&username=%s"
argument_list|,
name|slack
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

