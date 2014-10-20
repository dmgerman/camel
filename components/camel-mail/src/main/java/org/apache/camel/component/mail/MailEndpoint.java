begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|search
operator|.
name|SearchTerm
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|mail
operator|.
name|imap
operator|.
name|SortTerm
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
name|impl
operator|.
name|DefaultExchange
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
name|DefaultHeaderFilterStrategy
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
name|HeaderFilterStrategy
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

begin_comment
comment|/**  * Endpoint for Camel Mail.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"mail"
argument_list|,
name|consumerClass
operator|=
name|MailConsumer
operator|.
name|class
argument_list|)
DECL|class|MailEndpoint
specifier|public
class|class
name|MailEndpoint
extends|extends
name|ScheduledPollEndpoint
block|{
DECL|field|binding
specifier|private
name|MailBinding
name|binding
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|MailConfiguration
name|configuration
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
decl_stmt|;
DECL|field|contentTypeResolver
specifier|private
name|ContentTypeResolver
name|contentTypeResolver
decl_stmt|;
annotation|@
name|UriParam
DECL|field|maxMessagesPerPoll
specifier|private
name|int
name|maxMessagesPerPoll
decl_stmt|;
DECL|field|searchTerm
specifier|private
name|SearchTerm
name|searchTerm
decl_stmt|;
DECL|field|sortTerm
specifier|private
name|SortTerm
index|[]
name|sortTerm
decl_stmt|;
DECL|field|postProcessAction
specifier|private
name|MailBoxPostProcessAction
name|postProcessAction
decl_stmt|;
DECL|method|MailEndpoint ()
specifier|public
name|MailEndpoint
parameter_list|()
block|{     }
DECL|method|MailEndpoint (String uri, MailComponent component, MailConfiguration configuration)
specifier|public
name|MailEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|MailComponent
name|component
parameter_list|,
name|MailConfiguration
name|configuration
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
name|configuration
expr_stmt|;
block|}
annotation|@
name|Deprecated
DECL|method|MailEndpoint (String endpointUri, MailConfiguration configuration)
specifier|public
name|MailEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|MailConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|MailEndpoint (String endpointUri)
specifier|public
name|MailEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|this
argument_list|(
name|endpointUri
argument_list|,
operator|new
name|MailConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|JavaMailSender
name|sender
init|=
name|configuration
operator|.
name|getJavaMailSender
argument_list|()
decl_stmt|;
if|if
condition|(
name|sender
operator|==
literal|null
condition|)
block|{
comment|// use default mail sender
name|sender
operator|=
name|configuration
operator|.
name|createJavaMailSender
argument_list|()
expr_stmt|;
block|}
return|return
name|createProducer
argument_list|(
name|sender
argument_list|)
return|;
block|}
comment|/**      * Creates a producer using the given sender      */
DECL|method|createProducer (JavaMailSender sender)
specifier|public
name|Producer
name|createProducer
parameter_list|(
name|JavaMailSender
name|sender
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|MailProducer
argument_list|(
name|this
argument_list|,
name|sender
argument_list|)
return|;
block|}
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
if|if
condition|(
name|configuration
operator|.
name|getProtocol
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"smtp"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Protocol "
operator|+
name|configuration
operator|.
name|getProtocol
argument_list|()
operator|+
literal|" cannot be used for a MailConsumer. Please use another protocol such as pop3 or imap."
argument_list|)
throw|;
block|}
comment|// must use java mail sender impl as we need to get hold of a mail session
name|JavaMailSender
name|sender
init|=
name|configuration
operator|.
name|createJavaMailSender
argument_list|()
decl_stmt|;
return|return
name|createConsumer
argument_list|(
name|processor
argument_list|,
name|sender
argument_list|)
return|;
block|}
comment|/**      * Creates a consumer using the given processor and sender      */
DECL|method|createConsumer (Processor processor, JavaMailSender sender)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|JavaMailSender
name|sender
parameter_list|)
throws|throws
name|Exception
block|{
name|MailConsumer
name|answer
init|=
operator|new
name|MailConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|sender
argument_list|)
decl_stmt|;
comment|// ScheduledPollConsumer default delay is 500 millis and that is too often for polling a mailbox,
comment|// so we override with a new default value. End user can override this value by providing a consumer.delay parameter
name|answer
operator|.
name|setDelay
argument_list|(
name|MailConsumer
operator|.
name|DEFAULT_CONSUMER_DELAY
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setMaxMessagesPerPoll
argument_list|(
name|getMaxMessagesPerPoll
argument_list|()
argument_list|)
expr_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|createExchange (ExchangePattern pattern)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
return|return
name|createExchange
argument_list|(
name|pattern
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|createExchange (Message message)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|createExchange
argument_list|(
name|getExchangePattern
argument_list|()
argument_list|,
name|message
argument_list|)
return|;
block|}
DECL|method|createExchange (ExchangePattern pattern, Message message)
specifier|private
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|this
argument_list|,
name|pattern
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BINDING
argument_list|,
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|MailMessage
argument_list|(
name|message
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|isMapMailMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getBinding ()
specifier|public
name|MailBinding
name|getBinding
parameter_list|()
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
operator|new
name|MailBinding
argument_list|(
name|headerFilterStrategy
argument_list|,
name|contentTypeResolver
argument_list|)
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
comment|/**      * Sets the binding used to convert from a Camel message to and from a Mail message      */
DECL|method|setBinding (MailBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|MailBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|MailConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (MailConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|MailConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
DECL|method|getContentTypeResolver ()
specifier|public
name|ContentTypeResolver
name|getContentTypeResolver
parameter_list|()
block|{
return|return
name|contentTypeResolver
return|;
block|}
DECL|method|setContentTypeResolver (ContentTypeResolver contentTypeResolver)
specifier|public
name|void
name|setContentTypeResolver
parameter_list|(
name|ContentTypeResolver
name|contentTypeResolver
parameter_list|)
block|{
name|this
operator|.
name|contentTypeResolver
operator|=
name|contentTypeResolver
expr_stmt|;
block|}
DECL|method|getMaxMessagesPerPoll ()
specifier|public
name|int
name|getMaxMessagesPerPoll
parameter_list|()
block|{
return|return
name|maxMessagesPerPoll
return|;
block|}
DECL|method|setMaxMessagesPerPoll (int maxMessagesPerPoll)
specifier|public
name|void
name|setMaxMessagesPerPoll
parameter_list|(
name|int
name|maxMessagesPerPoll
parameter_list|)
block|{
name|this
operator|.
name|maxMessagesPerPoll
operator|=
name|maxMessagesPerPoll
expr_stmt|;
block|}
DECL|method|getSearchTerm ()
specifier|public
name|SearchTerm
name|getSearchTerm
parameter_list|()
block|{
return|return
name|searchTerm
return|;
block|}
DECL|method|setSearchTerm (SearchTerm searchTerm)
specifier|public
name|void
name|setSearchTerm
parameter_list|(
name|SearchTerm
name|searchTerm
parameter_list|)
block|{
name|this
operator|.
name|searchTerm
operator|=
name|searchTerm
expr_stmt|;
block|}
comment|/**      * @return Sorting order for messages. Only natively supported for IMAP. Emulated to some degree when using POP3      * or when IMAP server does not have the SORT capability.      * @see com.sun.mail.imap.SortTerm      */
DECL|method|getSortTerm ()
specifier|public
name|SortTerm
index|[]
name|getSortTerm
parameter_list|()
block|{
return|return
name|sortTerm
operator|==
literal|null
condition|?
literal|null
else|:
name|sortTerm
operator|.
name|clone
argument_list|()
return|;
block|}
comment|/**      * @param sortTerm {@link #getSortTerm()}      */
DECL|method|setSortTerm (SortTerm[] sortTerm)
specifier|public
name|void
name|setSortTerm
parameter_list|(
name|SortTerm
index|[]
name|sortTerm
parameter_list|)
block|{
name|this
operator|.
name|sortTerm
operator|=
name|sortTerm
operator|==
literal|null
condition|?
literal|null
else|:
name|sortTerm
operator|.
name|clone
argument_list|()
expr_stmt|;
block|}
comment|/**      * @return Post processor that can e.g. delete old email. Gets called once the messages have been polled and      * processed.      */
DECL|method|getPostProcessAction ()
specifier|public
name|MailBoxPostProcessAction
name|getPostProcessAction
parameter_list|()
block|{
return|return
name|postProcessAction
return|;
block|}
comment|/**      * @param postProcessAction {@link #getPostProcessAction()}      */
DECL|method|setPostProcessAction (MailBoxPostProcessAction postProcessAction)
specifier|public
name|void
name|setPostProcessAction
parameter_list|(
name|MailBoxPostProcessAction
name|postProcessAction
parameter_list|)
block|{
name|this
operator|.
name|postProcessAction
operator|=
name|postProcessAction
expr_stmt|;
block|}
block|}
end_class

end_unit

