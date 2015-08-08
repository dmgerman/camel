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
name|CamelExchangeException
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
name|component
operator|.
name|slack
operator|.
name|helper
operator|.
name|SlackMessage
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
name|DefaultProducer
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
name|util
operator|.
name|IOHelper
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
name|HttpClientBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|json
operator|.
name|simple
operator|.
name|JSONObject
import|;
end_import

begin_class
DECL|class|SlackProducer
specifier|public
class|class
name|SlackProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|slackEndpoint
specifier|private
name|SlackEndpoint
name|slackEndpoint
decl_stmt|;
DECL|method|SlackProducer (SlackEndpoint endpoint)
specifier|public
name|SlackProducer
parameter_list|(
name|SlackEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|slackEndpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Create an HttpClient and Post object
name|HttpClient
name|client
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|HttpPost
name|httpPost
init|=
operator|new
name|HttpPost
argument_list|(
name|slackEndpoint
operator|.
name|getWebhookUrl
argument_list|()
argument_list|)
decl_stmt|;
comment|// Build Helper object
name|SlackMessage
name|slackMessage
init|=
operator|new
name|SlackMessage
argument_list|()
decl_stmt|;
name|slackMessage
operator|.
name|setText
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|slackMessage
operator|.
name|setChannel
argument_list|(
name|slackEndpoint
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
name|slackMessage
operator|.
name|setUsername
argument_list|(
name|slackEndpoint
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|slackMessage
operator|.
name|setIconUrl
argument_list|(
name|slackEndpoint
operator|.
name|getIconUrl
argument_list|()
argument_list|)
expr_stmt|;
name|slackMessage
operator|.
name|setIconEmoji
argument_list|(
name|slackEndpoint
operator|.
name|getIconEmoji
argument_list|()
argument_list|)
expr_stmt|;
comment|// use charset from exchange or fallback to the default charset
name|String
name|charset
init|=
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// Set the post body
name|String
name|json
init|=
name|asJson
argument_list|(
name|slackMessage
argument_list|)
decl_stmt|;
name|StringEntity
name|body
init|=
operator|new
name|StringEntity
argument_list|(
name|json
argument_list|,
name|charset
argument_list|)
decl_stmt|;
comment|// Do the post
name|httpPost
operator|.
name|setEntity
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|client
operator|.
name|execute
argument_list|(
name|httpPost
argument_list|)
decl_stmt|;
comment|// 2xx is OK, anything else we regard as failure
if|if
condition|(
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
operator|<
literal|200
operator|||
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
operator|>
literal|299
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Error POSTing to Slack API: "
operator|+
name|response
operator|.
name|toString
argument_list|()
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
block|}
comment|/**      * Returns a JSON string to be posted to the Slack API      *      * @return JSON string      */
DECL|method|asJson (SlackMessage message)
specifier|public
name|String
name|asJson
parameter_list|(
name|SlackMessage
name|message
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|jsonMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// Put the values in a map
name|jsonMap
operator|.
name|put
argument_list|(
literal|"text"
argument_list|,
name|message
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"channel"
argument_list|,
name|message
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"username"
argument_list|,
name|message
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"icon_url"
argument_list|,
name|message
operator|.
name|getIconUrl
argument_list|()
argument_list|)
expr_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
literal|"icon_emoji"
argument_list|,
name|message
operator|.
name|getIconEmoji
argument_list|()
argument_list|)
expr_stmt|;
comment|// Generate a JSONObject
name|JSONObject
name|jsonObject
init|=
operator|new
name|JSONObject
argument_list|(
name|jsonMap
argument_list|)
decl_stmt|;
comment|// Return the string based on the JSON Object
return|return
name|JSONObject
operator|.
name|toJSONString
argument_list|(
name|jsonObject
argument_list|)
return|;
block|}
block|}
end_class

end_unit

