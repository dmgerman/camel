begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hipchat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hipchat
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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
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
name|InvalidPayloadException
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
name|Message
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
name|support
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
name|URISupport
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
name|StatusLine
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
name|CloseableHttpResponse
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
name|ContentType
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|UnsafeUriCharactersEncoder
operator|.
name|encodeHttpURI
import|;
end_import

begin_comment
comment|/**  * The Hipchat producer to send message to a user and/or a room.  */
end_comment

begin_class
DECL|class|HipchatProducer
specifier|public
class|class
name|HipchatProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|MAPPER
specifier|private
specifier|static
specifier|final
name|ObjectMapper
name|MAPPER
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
DECL|field|hipchatProducerToString
specifier|private
specifier|transient
name|String
name|hipchatProducerToString
decl_stmt|;
DECL|method|HipchatProducer (HipchatEndpoint endpoint)
specifier|public
name|HipchatProducer
parameter_list|(
name|HipchatEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
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
name|Message
name|message
init|=
name|getMessageForResponse
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|String
name|user
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_USER
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_USER_RESPONSE_STATUS
argument_list|,
name|sendUserMessage
argument_list|(
name|user
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|room
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_ROOM
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|room
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_ROOM_RESPONSE_STATUS
argument_list|,
name|sendRoomMessage
argument_list|(
name|room
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|sendRoomMessage (String room, Exchange exchange)
specifier|private
name|StatusLine
name|sendRoomMessage
parameter_list|(
name|String
name|room
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidPayloadException
block|{
name|String
name|urlPath
init|=
name|String
operator|.
name|format
argument_list|(
name|getConfig
argument_list|()
operator|.
name|withAuthToken
argument_list|(
name|HipchatApiConstants
operator|.
name|URI_PATH_ROOM_NOTIFY
argument_list|)
argument_list|,
name|room
argument_list|)
decl_stmt|;
name|String
name|backGroundColor
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|MESSAGE_BACKGROUND_COLOR
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|jsonParam
init|=
name|getCommonHttpPostParam
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|backGroundColor
operator|!=
literal|null
condition|)
block|{
name|jsonParam
operator|.
name|put
argument_list|(
name|HipchatApiConstants
operator|.
name|API_MESSAGE_COLOR
argument_list|,
name|backGroundColor
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Sending message to room: "
operator|+
name|room
operator|+
literal|", "
operator|+
name|MAPPER
operator|.
name|writeValueAsString
argument_list|(
name|jsonParam
argument_list|)
argument_list|)
expr_stmt|;
name|StatusLine
name|statusLine
init|=
name|post
argument_list|(
name|encodeHttpURI
argument_list|(
name|urlPath
argument_list|)
argument_list|,
name|jsonParam
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Response status for send room message: {}"
argument_list|,
name|statusLine
argument_list|)
expr_stmt|;
return|return
name|statusLine
return|;
block|}
DECL|method|sendUserMessage (String user, Exchange exchange)
specifier|private
name|StatusLine
name|sendUserMessage
parameter_list|(
name|String
name|user
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidPayloadException
block|{
name|String
name|urlPath
init|=
name|String
operator|.
name|format
argument_list|(
name|getConfig
argument_list|()
operator|.
name|withAuthToken
argument_list|(
name|HipchatApiConstants
operator|.
name|URI_PATH_USER_MESSAGE
argument_list|)
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|jsonParam
init|=
name|getCommonHttpPostParam
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Sending message to user: "
operator|+
name|user
operator|+
literal|", "
operator|+
name|MAPPER
operator|.
name|writeValueAsString
argument_list|(
name|jsonParam
argument_list|)
argument_list|)
expr_stmt|;
name|StatusLine
name|statusLine
init|=
name|post
argument_list|(
name|urlPath
argument_list|,
name|jsonParam
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Response status for send user message: {}"
argument_list|,
name|statusLine
argument_list|)
expr_stmt|;
return|return
name|statusLine
return|;
block|}
DECL|method|getCommonHttpPostParam (Exchange exchange)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getCommonHttpPostParam
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
name|String
name|format
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|MESSAGE_FORMAT
argument_list|,
literal|"text"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|notify
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TRIGGER_NOTIFY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
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
argument_list|<>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|jsonMap
operator|.
name|put
argument_list|(
name|HipchatApiConstants
operator|.
name|API_MESSAGE
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|notify
operator|!=
literal|null
condition|)
block|{
name|jsonMap
operator|.
name|put
argument_list|(
name|HipchatApiConstants
operator|.
name|API_MESSAGE_NOTIFY
argument_list|,
name|notify
argument_list|)
expr_stmt|;
block|}
name|jsonMap
operator|.
name|put
argument_list|(
name|HipchatApiConstants
operator|.
name|API_MESSAGE_FORMAT
argument_list|,
name|format
argument_list|)
expr_stmt|;
return|return
name|jsonMap
return|;
block|}
DECL|method|post (String urlPath, Map<String, String> postParam)
specifier|protected
name|StatusLine
name|post
parameter_list|(
name|String
name|urlPath
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|postParam
parameter_list|)
throws|throws
name|IOException
block|{
name|HttpPost
name|httpPost
init|=
operator|new
name|HttpPost
argument_list|(
name|getConfig
argument_list|()
operator|.
name|hipChatUrl
argument_list|()
operator|+
name|urlPath
argument_list|)
decl_stmt|;
name|httpPost
operator|.
name|setEntity
argument_list|(
operator|new
name|StringEntity
argument_list|(
name|MAPPER
operator|.
name|writeValueAsString
argument_list|(
name|postParam
argument_list|)
argument_list|,
name|ContentType
operator|.
name|APPLICATION_JSON
argument_list|)
argument_list|)
expr_stmt|;
name|CloseableHttpResponse
name|closeableHttpResponse
init|=
name|getConfig
argument_list|()
operator|.
name|getHttpClient
argument_list|()
operator|.
name|execute
argument_list|(
name|httpPost
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|closeableHttpResponse
operator|.
name|getStatusLine
argument_list|()
return|;
block|}
finally|finally
block|{
name|closeableHttpResponse
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getMessageForResponse (final Exchange exchange)
specifier|private
name|Message
name|getMessageForResponse
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|out
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|out
return|;
block|}
return|return
name|exchange
operator|.
name|getIn
argument_list|()
return|;
block|}
DECL|method|getConfig ()
specifier|private
name|HipchatConfiguration
name|getConfig
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|HipchatEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|HipchatEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|hipchatProducerToString
operator|==
literal|null
condition|)
block|{
name|hipchatProducerToString
operator|=
literal|"HipchatProducer["
operator|+
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|+
literal|"]"
expr_stmt|;
block|}
return|return
name|hipchatProducerToString
return|;
block|}
block|}
end_class

end_unit

