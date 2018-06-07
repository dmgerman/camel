begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.mail.stream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|mail
operator|.
name|stream
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
name|ArrayList
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
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|AddressException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|util
operator|.
name|Base64
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|gmail
operator|.
name|Gmail
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|gmail
operator|.
name|model
operator|.
name|Label
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|gmail
operator|.
name|model
operator|.
name|ListLabelsResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|gmail
operator|.
name|model
operator|.
name|MessagePartHeader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Splitter
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
name|Consumer
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
name|ExchangePattern
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
name|Processor
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
name|Producer
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
name|google
operator|.
name|mail
operator|.
name|GoogleMailClientFactory
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
name|ScheduledPollEndpoint
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
name|UriEndpoint
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The google-mail component provides access to Google Mail.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.22.0"
argument_list|,
name|scheme
operator|=
literal|"google-mail-stream"
argument_list|,
name|title
operator|=
literal|"Google Mail Stream"
argument_list|,
name|syntax
operator|=
literal|"google-mail-stream:index"
argument_list|,
name|consumerClass
operator|=
name|GoogleMailStreamConsumer
operator|.
name|class
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"api,cloud,mail"
argument_list|)
DECL|class|GoogleMailStreamEndpoint
specifier|public
class|class
name|GoogleMailStreamEndpoint
extends|extends
name|ScheduledPollEndpoint
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|GoogleMailStreamConfiguration
name|configuration
decl_stmt|;
DECL|method|GoogleMailStreamEndpoint (String uri, GoogleMailStreamComponent component, GoogleMailStreamConfiguration endpointConfiguration)
specifier|public
name|GoogleMailStreamEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|GoogleMailStreamComponent
name|component
parameter_list|,
name|GoogleMailStreamConfiguration
name|endpointConfiguration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|endpointConfiguration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The camel google mail stream component doesn't support producer"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|unreadLabelId
init|=
literal|null
decl_stmt|;
name|List
name|labelsIds
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ListLabelsResponse
name|listResponse
init|=
name|getClient
argument_list|()
operator|.
name|users
argument_list|()
operator|.
name|labels
argument_list|()
operator|.
name|list
argument_list|(
literal|"me"
argument_list|)
operator|.
name|execute
argument_list|()
decl_stmt|;
for|for
control|(
name|Label
name|label
range|:
name|listResponse
operator|.
name|getLabels
argument_list|()
control|)
block|{
name|Label
name|countLabel
init|=
name|getClient
argument_list|()
operator|.
name|users
argument_list|()
operator|.
name|labels
argument_list|()
operator|.
name|get
argument_list|(
literal|"me"
argument_list|,
name|label
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|execute
argument_list|()
decl_stmt|;
if|if
condition|(
name|countLabel
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"UNREAD"
argument_list|)
condition|)
block|{
name|unreadLabelId
operator|=
name|countLabel
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getLabels
argument_list|()
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|plainLabels
init|=
name|splitLabels
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getLabels
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Label
name|label
range|:
name|listResponse
operator|.
name|getLabels
argument_list|()
control|)
block|{
name|Label
name|countLabel
init|=
name|getClient
argument_list|()
operator|.
name|users
argument_list|()
operator|.
name|labels
argument_list|()
operator|.
name|get
argument_list|(
literal|"me"
argument_list|,
name|label
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|execute
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|plainLabel
range|:
name|plainLabels
control|)
block|{
if|if
condition|(
name|countLabel
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|plainLabel
argument_list|)
condition|)
block|{
name|labelsIds
operator|.
name|add
argument_list|(
name|countLabel
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|final
name|GoogleMailStreamConsumer
name|consumer
init|=
operator|new
name|GoogleMailStreamConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|unreadLabelId
argument_list|,
name|labelsIds
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|getClient ()
specifier|public
name|Gmail
name|getClient
parameter_list|()
block|{
return|return
operator|(
operator|(
name|GoogleMailStreamComponent
operator|)
name|getComponent
argument_list|()
operator|)
operator|.
name|getClient
argument_list|(
name|configuration
argument_list|)
return|;
block|}
DECL|method|getClientFactory ()
specifier|public
name|GoogleMailClientFactory
name|getClientFactory
parameter_list|()
block|{
return|return
operator|(
operator|(
name|GoogleMailStreamComponent
operator|)
name|getComponent
argument_list|()
operator|)
operator|.
name|getClientFactory
argument_list|()
return|;
block|}
DECL|method|setClientFactory (GoogleMailClientFactory clientFactory)
specifier|public
name|void
name|setClientFactory
parameter_list|(
name|GoogleMailClientFactory
name|clientFactory
parameter_list|)
block|{
operator|(
operator|(
name|GoogleMailStreamComponent
operator|)
name|getComponent
argument_list|()
operator|)
operator|.
name|setClientFactory
argument_list|(
name|clientFactory
argument_list|)
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|GoogleMailStreamConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|createExchange (ExchangePattern pattern, com.google.api.services.gmail.model.Message mail)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|,
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|gmail
operator|.
name|model
operator|.
name|Message
name|mail
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
name|Exchange
name|exchange
init|=
name|super
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|GoogleMailStreamConstants
operator|.
name|MAIL_ID
argument_list|,
name|mail
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|mail
operator|.
name|getPayload
argument_list|()
operator|.
name|getParts
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|mail
operator|.
name|getPayload
argument_list|()
operator|.
name|getParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getBody
argument_list|()
operator|.
name|getData
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|byte
index|[]
name|bodyBytes
init|=
name|Base64
operator|.
name|decodeBase64
argument_list|(
name|mail
operator|.
name|getPayload
argument_list|()
operator|.
name|getParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getBody
argument_list|()
operator|.
name|getData
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
comment|// get
comment|// body
name|String
name|body
init|=
operator|new
name|String
argument_list|(
name|bodyBytes
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
name|setHeaders
argument_list|(
name|message
argument_list|,
name|mail
operator|.
name|getPayload
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|setHeaders (Message message, List<MessagePartHeader> headers)
specifier|private
name|void
name|setHeaders
parameter_list|(
name|Message
name|message
parameter_list|,
name|List
argument_list|<
name|MessagePartHeader
argument_list|>
name|headers
parameter_list|)
block|{
for|for
control|(
name|MessagePartHeader
name|header
range|:
name|headers
control|)
block|{
if|if
condition|(
name|header
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"SUBJECT"
argument_list|)
operator|||
name|header
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"subject"
argument_list|)
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|GoogleMailStreamConstants
operator|.
name|MAIL_SUBJECT
argument_list|,
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|header
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TO"
argument_list|)
operator|||
name|header
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"to"
argument_list|)
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|GoogleMailStreamConstants
operator|.
name|MAIL_TO
argument_list|,
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|header
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"FROM"
argument_list|)
operator|||
name|header
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"from"
argument_list|)
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|GoogleMailStreamConstants
operator|.
name|MAIL_FROM
argument_list|,
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|header
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"CC"
argument_list|)
operator|||
name|header
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"cc"
argument_list|)
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|GoogleMailStreamConstants
operator|.
name|MAIL_CC
argument_list|,
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|header
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"BCC"
argument_list|)
operator|||
name|header
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"bcc"
argument_list|)
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|GoogleMailStreamConstants
operator|.
name|MAIL_BCC
argument_list|,
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|splitLabels (String labels)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|splitLabels
parameter_list|(
name|String
name|labels
parameter_list|)
throws|throws
name|AddressException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|labelsList
init|=
name|Splitter
operator|.
name|on
argument_list|(
literal|','
argument_list|)
operator|.
name|splitToList
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getLabels
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|labelsList
return|;
block|}
block|}
end_class

end_unit

