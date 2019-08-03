begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter.deprecated
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twitter
operator|.
name|deprecated
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|component
operator|.
name|twitter
operator|.
name|CamelTwitterTestSupport
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|Status
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|Is
operator|.
name|is
import|;
end_import

begin_comment
comment|/**  * Tests posting a twitter update and getting the status update id from the Twitter API response  */
end_comment

begin_class
annotation|@
name|Deprecated
DECL|class|UserProducerInOutTest
specifier|public
class|class
name|UserProducerInOutTest
extends|extends
name|CamelTwitterTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|UserProducerInOutTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Test
DECL|method|testPostStatusUpdateRequestResponse ()
specifier|public
name|void
name|testPostStatusUpdateRequestResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|Date
name|now
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|String
name|tweet
init|=
literal|"UserProducerInOutTest: This is a tweet posted on "
operator|+
name|now
operator|.
name|toString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Tweet: "
operator|+
name|tweet
argument_list|)
expr_stmt|;
name|ProducerTemplate
name|producerTemplate
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
comment|// send tweet to the twitter endpoint
name|producerTemplate
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:tweets"
argument_list|,
name|tweet
argument_list|,
literal|"customHeader"
argument_list|,
literal|12312
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedBodyReceived
argument_list|()
operator|.
name|body
argument_list|(
name|Status
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Message headers should be preserved
name|resultEndpoint
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"customHeader"
argument_list|,
literal|12312
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|tweets
init|=
name|resultEndpoint
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|tweets
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|tweets
operator|.
name|size
argument_list|()
argument_list|,
name|is
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Status
name|receivedTweet
init|=
name|tweets
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Status
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|receivedTweet
argument_list|)
expr_stmt|;
comment|// The identifier for the published tweet should be there
name|assertNotNull
argument_list|(
name|receivedTweet
operator|.
name|getId
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
block|{
name|from
argument_list|(
literal|"direct:tweets"
argument_list|)
comment|//.to("log:org.apache.camel.component.twitter?level=INFO&showAll=true&multiline=true")
operator|.
name|inOut
argument_list|(
literal|"twitter://timeline/user?"
operator|+
name|getUriTokens
argument_list|()
argument_list|)
comment|//.to("log:org.apache.camel.component.twitter?level=INFO&showAll=true&multiline=true")
comment|//.transform().simple("The tweet '${body.text}' was published with the id '${body.id}'")
comment|//.to("log:org.apache.camel.component.twitter?level=INFO&showAll=true&multiline=true")
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

