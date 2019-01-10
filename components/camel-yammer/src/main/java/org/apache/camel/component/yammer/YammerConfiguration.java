begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yammer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yammer
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriParams
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
name|spi
operator|.
name|UriPath
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|YammerConfiguration
specifier|public
class|class
name|YammerConfiguration
block|{
annotation|@
name|UriPath
argument_list|(
name|name
operator|=
literal|"function"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|functionType
specifier|private
name|YammerFunctionType
name|functionType
decl_stmt|;
DECL|field|function
specifier|private
name|String
name|function
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|consumerKey
specifier|private
name|String
name|consumerKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|consumerSecret
specifier|private
name|String
name|consumerSecret
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|accessToken
specifier|private
name|String
name|accessToken
decl_stmt|;
annotation|@
name|UriParam
DECL|field|useJson
specifier|private
name|boolean
name|useJson
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"5000"
argument_list|)
DECL|field|delay
specifier|private
name|long
name|delay
init|=
literal|3000
operator|+
literal|2000
decl_stmt|;
comment|// 3 sec per poll is enforced by yammer; add 2 sec for safety
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"-1"
argument_list|)
DECL|field|limit
specifier|private
name|int
name|limit
init|=
operator|-
literal|1
decl_stmt|;
comment|// default is unlimited
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"-1"
argument_list|)
DECL|field|olderThan
specifier|private
name|int
name|olderThan
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"-1"
argument_list|)
DECL|field|newerThan
specifier|private
name|int
name|newerThan
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|enums
operator|=
literal|"true,extended"
argument_list|)
DECL|field|threaded
specifier|private
name|String
name|threaded
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|userId
specifier|private
name|String
name|userId
decl_stmt|;
DECL|field|requestor
specifier|private
name|ApiRequestor
name|requestor
decl_stmt|;
DECL|method|getConsumerKey ()
specifier|public
name|String
name|getConsumerKey
parameter_list|()
block|{
return|return
name|consumerKey
return|;
block|}
comment|/**      * The consumer key      */
DECL|method|setConsumerKey (String consumerKey)
specifier|public
name|void
name|setConsumerKey
parameter_list|(
name|String
name|consumerKey
parameter_list|)
block|{
name|this
operator|.
name|consumerKey
operator|=
name|consumerKey
expr_stmt|;
block|}
DECL|method|getConsumerSecret ()
specifier|public
name|String
name|getConsumerSecret
parameter_list|()
block|{
return|return
name|consumerSecret
return|;
block|}
comment|/**      * The consumer secret      */
DECL|method|setConsumerSecret (String consumerSecret)
specifier|public
name|void
name|setConsumerSecret
parameter_list|(
name|String
name|consumerSecret
parameter_list|)
block|{
name|this
operator|.
name|consumerSecret
operator|=
name|consumerSecret
expr_stmt|;
block|}
DECL|method|getDelay ()
specifier|public
name|long
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
comment|/**      * Delay between polling in millis      */
DECL|method|setDelay (long delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
block|}
DECL|method|getAccessToken ()
specifier|public
name|String
name|getAccessToken
parameter_list|()
block|{
return|return
name|accessToken
return|;
block|}
comment|/**      * The access token      */
DECL|method|setAccessToken (String accessToken)
specifier|public
name|void
name|setAccessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
block|{
name|this
operator|.
name|accessToken
operator|=
name|accessToken
expr_stmt|;
block|}
DECL|method|getFunctionType ()
specifier|public
name|YammerFunctionType
name|getFunctionType
parameter_list|()
block|{
return|return
name|functionType
return|;
block|}
comment|/**      * The function to use      */
DECL|method|setFunctionType (YammerFunctionType functionType)
specifier|public
name|void
name|setFunctionType
parameter_list|(
name|YammerFunctionType
name|functionType
parameter_list|)
block|{
name|this
operator|.
name|functionType
operator|=
name|functionType
expr_stmt|;
block|}
DECL|method|getFunction ()
specifier|public
name|String
name|getFunction
parameter_list|()
block|{
return|return
name|function
return|;
block|}
comment|/**      * The function to use      */
DECL|method|setFunction (String function)
specifier|public
name|void
name|setFunction
parameter_list|(
name|String
name|function
parameter_list|)
block|{
name|this
operator|.
name|function
operator|=
name|function
expr_stmt|;
block|}
DECL|method|isUseJson ()
specifier|public
name|boolean
name|isUseJson
parameter_list|()
block|{
return|return
name|useJson
return|;
block|}
comment|/**      * Set to true if you want to use raw JSON rather than converting to POJOs.      */
DECL|method|setUseJson (boolean useJson)
specifier|public
name|void
name|setUseJson
parameter_list|(
name|boolean
name|useJson
parameter_list|)
block|{
name|this
operator|.
name|useJson
operator|=
name|useJson
expr_stmt|;
block|}
DECL|method|getRequestor (String apiUrl)
specifier|public
name|ApiRequestor
name|getRequestor
parameter_list|(
name|String
name|apiUrl
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|requestor
operator|==
literal|null
condition|)
block|{
name|requestor
operator|=
operator|new
name|ScribeApiRequestor
argument_list|(
name|apiUrl
argument_list|,
name|getAccessToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|requestor
return|;
block|}
DECL|method|setRequestor (ApiRequestor requestor)
specifier|public
name|void
name|setRequestor
parameter_list|(
name|ApiRequestor
name|requestor
parameter_list|)
block|{
name|this
operator|.
name|requestor
operator|=
name|requestor
expr_stmt|;
block|}
DECL|method|getLimit ()
specifier|public
name|int
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
comment|/**      * Return only the specified number of messages. Works for threaded=true and threaded=extended.      */
DECL|method|setLimit (int limit)
specifier|public
name|void
name|setLimit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
block|}
DECL|method|getOlderThan ()
specifier|public
name|int
name|getOlderThan
parameter_list|()
block|{
return|return
name|olderThan
return|;
block|}
comment|/**      * Returns messages older than the message ID specified as a numeric string.      * This is useful for paginating messages. For example, if you're currently viewing 20 messages and the oldest is number 2912,      * you could append "?olderThan=2912â³ to your request to get the 20 messages prior to those you're seeing.      */
DECL|method|setOlderThan (int olderThan)
specifier|public
name|void
name|setOlderThan
parameter_list|(
name|int
name|olderThan
parameter_list|)
block|{
name|this
operator|.
name|olderThan
operator|=
name|olderThan
expr_stmt|;
block|}
DECL|method|getNewerThan ()
specifier|public
name|int
name|getNewerThan
parameter_list|()
block|{
return|return
name|newerThan
return|;
block|}
comment|/**      * Returns messages newer than the message ID specified as a numeric string. This should be used when polling for new messages.      * If you're looking at messages, and the most recent message returned is 3516, you can make a request with the parameter "?newerThan=3516â³      * to ensure that you do not get duplicate copies of messages already on your page.      */
DECL|method|setNewerThan (int newerThan)
specifier|public
name|void
name|setNewerThan
parameter_list|(
name|int
name|newerThan
parameter_list|)
block|{
name|this
operator|.
name|newerThan
operator|=
name|newerThan
expr_stmt|;
block|}
DECL|method|getThreaded ()
specifier|public
name|String
name|getThreaded
parameter_list|()
block|{
return|return
name|threaded
return|;
block|}
comment|/**      * threaded=true will only return the first message in each thread.      * This parameter is intended for apps which display message threads collapsed.      * threaded=extended will return the thread starter messages in order of most recently active as well as the      * two most recent messages, as they are viewed in the default view on the Yammer web interface.      */
DECL|method|setThreaded (String threaded)
specifier|public
name|void
name|setThreaded
parameter_list|(
name|String
name|threaded
parameter_list|)
block|{
name|this
operator|.
name|threaded
operator|=
name|threaded
expr_stmt|;
block|}
DECL|method|getUserId ()
specifier|public
name|String
name|getUserId
parameter_list|()
block|{
return|return
name|userId
return|;
block|}
comment|/**      * The user id      */
DECL|method|setUserId (String userId)
specifier|public
name|void
name|setUserId
parameter_list|(
name|String
name|userId
parameter_list|)
block|{
name|this
operator|.
name|userId
operator|=
name|userId
expr_stmt|;
block|}
block|}
end_class

end_unit

