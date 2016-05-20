begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
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

begin_comment
comment|/**  * Bean holding the configuration of the telegram component.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|TelegramConfiguration
specifier|public
class|class
name|TelegramConfiguration
block|{
DECL|field|ENDPOINT_TYPE_BOTS
specifier|public
specifier|static
specifier|final
name|String
name|ENDPOINT_TYPE_BOTS
init|=
literal|"bots"
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The endpoint type. Currently, only the 'bots' type is supported."
argument_list|,
name|enums
operator|=
name|ENDPOINT_TYPE_BOTS
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The authorization token for using the bot (ask the BotFather), eg. 654321531:HGF_dTra456323dHuOedsE343211fqr3t-H."
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|authorizationToken
specifier|private
name|String
name|authorizationToken
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"The identifier of the chat that will receive the produced messages. Chat ids can be first obtained from incoming messages "
operator|+
literal|"(eg. when a telegram user starts a conversation with a bot, its client sends automatically a '/start' message containing the chat id). "
operator|+
literal|"It is an optional parameter, as the chat id can be set dynamically for each outgoing message (using body or headers)."
argument_list|,
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|chatId
specifier|private
name|String
name|chatId
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Timeout in seconds for long polling. Put 0 for short polling or a bigger number for long polling. Long polling produces shorter response time."
argument_list|,
name|optionalPrefix
operator|=
literal|"consumer."
argument_list|,
name|defaultValue
operator|=
literal|"30"
argument_list|,
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|timeout
specifier|private
name|Integer
name|timeout
init|=
literal|30
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Limit on the number of updates that can be received in a single polling request."
argument_list|,
name|optionalPrefix
operator|=
literal|"consumer."
argument_list|,
name|defaultValue
operator|=
literal|"100"
argument_list|,
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|limit
specifier|private
name|Integer
name|limit
init|=
literal|100
decl_stmt|;
DECL|method|TelegramConfiguration ()
specifier|public
name|TelegramConfiguration
parameter_list|()
block|{     }
comment|/**      * Sets the remaining configuration parameters available in the URI.      *      * @param remaining the URI part after the scheme      */
DECL|method|updatePathConfig (String remaining)
specifier|public
name|void
name|updatePathConfig
parameter_list|(
name|String
name|remaining
parameter_list|)
block|{
name|String
index|[]
name|parts
init|=
name|remaining
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unexpected URI format. Expected 'bots/<authorizationToken>', found '"
operator|+
name|remaining
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|String
name|type
init|=
name|parts
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|type
operator|.
name|equals
argument_list|(
name|ENDPOINT_TYPE_BOTS
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unexpected endpoint type. Expected 'bots', found '"
operator|+
name|parts
index|[
literal|0
index|]
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|String
name|authorizationToken
init|=
name|parts
index|[
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|authorizationToken
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Authorization token is required"
argument_list|)
throw|;
block|}
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|authorizationToken
operator|=
name|authorizationToken
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (String type)
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getAuthorizationToken ()
specifier|public
name|String
name|getAuthorizationToken
parameter_list|()
block|{
return|return
name|authorizationToken
return|;
block|}
DECL|method|setAuthorizationToken (String authorizationToken)
specifier|public
name|void
name|setAuthorizationToken
parameter_list|(
name|String
name|authorizationToken
parameter_list|)
block|{
name|this
operator|.
name|authorizationToken
operator|=
name|authorizationToken
expr_stmt|;
block|}
DECL|method|getChatId ()
specifier|public
name|String
name|getChatId
parameter_list|()
block|{
return|return
name|chatId
return|;
block|}
DECL|method|setChatId (String chatId)
specifier|public
name|void
name|setChatId
parameter_list|(
name|String
name|chatId
parameter_list|)
block|{
name|this
operator|.
name|chatId
operator|=
name|chatId
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|Integer
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (Integer timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|Integer
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getLimit ()
specifier|public
name|Integer
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
DECL|method|setLimit (Integer limit)
specifier|public
name|void
name|setLimit
parameter_list|(
name|Integer
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"TelegramConfiguration{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"type='"
argument_list|)
operator|.
name|append
argument_list|(
name|type
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", authorizationToken='"
argument_list|)
operator|.
name|append
argument_list|(
name|authorizationToken
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", chatId='"
argument_list|)
operator|.
name|append
argument_list|(
name|chatId
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", timeout="
argument_list|)
operator|.
name|append
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", limit="
argument_list|)
operator|.
name|append
argument_list|(
name|limit
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

