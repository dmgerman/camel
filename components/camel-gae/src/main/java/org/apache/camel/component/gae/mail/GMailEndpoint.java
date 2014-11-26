begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|mail
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|appengine
operator|.
name|api
operator|.
name|mail
operator|.
name|MailService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|appengine
operator|.
name|api
operator|.
name|mail
operator|.
name|MailService
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|appengine
operator|.
name|api
operator|.
name|mail
operator|.
name|MailServiceFactory
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
name|Component
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
name|gae
operator|.
name|bind
operator|.
name|OutboundBinding
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
name|gae
operator|.
name|bind
operator|.
name|OutboundBindingSupport
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
name|DefaultEndpoint
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
name|spi
operator|.
name|UriPath
import|;
end_import

begin_comment
comment|/**  * Represents a<a href="http://camel.apache.org/gmail.html">Google App Engine Mail endpoint</a>.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"gmail"
argument_list|,
name|label
operator|=
literal|"cloud,mail"
argument_list|)
DECL|class|GMailEndpoint
specifier|public
class|class
name|GMailEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|OutboundBindingSupport
argument_list|<
name|GMailEndpoint
argument_list|,
name|Message
argument_list|,
name|Void
argument_list|>
block|{
DECL|field|outboundBinding
specifier|private
name|OutboundBinding
argument_list|<
name|GMailEndpoint
argument_list|,
name|Message
argument_list|,
name|Void
argument_list|>
name|outboundBinding
decl_stmt|;
DECL|field|mailService
specifier|private
name|MailService
name|mailService
decl_stmt|;
annotation|@
name|UriPath
DECL|field|sender
specifier|private
name|String
name|sender
decl_stmt|;
annotation|@
name|UriParam
DECL|field|subject
specifier|private
name|String
name|subject
decl_stmt|;
annotation|@
name|UriParam
DECL|field|to
specifier|private
name|String
name|to
decl_stmt|;
annotation|@
name|UriParam
DECL|field|cc
specifier|private
name|String
name|cc
decl_stmt|;
annotation|@
name|UriParam
DECL|field|bcc
specifier|private
name|String
name|bcc
decl_stmt|;
DECL|method|GMailEndpoint (String endpointUri, Component component, String sender)
specifier|public
name|GMailEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|String
name|sender
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|sender
operator|=
name|sender
expr_stmt|;
name|this
operator|.
name|mailService
operator|=
name|MailServiceFactory
operator|.
name|getMailService
argument_list|()
expr_stmt|;
block|}
DECL|method|getOutboundBinding ()
specifier|public
name|OutboundBinding
argument_list|<
name|GMailEndpoint
argument_list|,
name|Message
argument_list|,
name|Void
argument_list|>
name|getOutboundBinding
parameter_list|()
block|{
return|return
name|outboundBinding
return|;
block|}
DECL|method|setOutboundBinding (OutboundBinding<GMailEndpoint, Message, Void> outboundBinding)
specifier|public
name|void
name|setOutboundBinding
parameter_list|(
name|OutboundBinding
argument_list|<
name|GMailEndpoint
argument_list|,
name|Message
argument_list|,
name|Void
argument_list|>
name|outboundBinding
parameter_list|)
block|{
name|this
operator|.
name|outboundBinding
operator|=
name|outboundBinding
expr_stmt|;
block|}
DECL|method|getMailService ()
specifier|public
name|MailService
name|getMailService
parameter_list|()
block|{
return|return
name|mailService
return|;
block|}
DECL|method|getSender ()
specifier|public
name|String
name|getSender
parameter_list|()
block|{
return|return
name|sender
return|;
block|}
DECL|method|getSubject ()
specifier|public
name|String
name|getSubject
parameter_list|()
block|{
return|return
name|subject
return|;
block|}
DECL|method|setSubject (String subject)
specifier|public
name|void
name|setSubject
parameter_list|(
name|String
name|subject
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
block|}
DECL|method|getTo ()
specifier|public
name|String
name|getTo
parameter_list|()
block|{
return|return
name|to
return|;
block|}
DECL|method|setTo (String to)
specifier|public
name|void
name|setTo
parameter_list|(
name|String
name|to
parameter_list|)
block|{
name|this
operator|.
name|to
operator|=
name|to
expr_stmt|;
block|}
DECL|method|getCc ()
specifier|public
name|String
name|getCc
parameter_list|()
block|{
return|return
name|cc
return|;
block|}
DECL|method|setCc (String cc)
specifier|public
name|void
name|setCc
parameter_list|(
name|String
name|cc
parameter_list|)
block|{
name|this
operator|.
name|cc
operator|=
name|cc
expr_stmt|;
block|}
DECL|method|getBcc ()
specifier|public
name|String
name|getBcc
parameter_list|()
block|{
return|return
name|bcc
return|;
block|}
DECL|method|setBcc (String bcc)
specifier|public
name|void
name|setBcc
parameter_list|(
name|String
name|bcc
parameter_list|)
block|{
name|this
operator|.
name|bcc
operator|=
name|bcc
expr_stmt|;
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"consumption from gmail endpoint not supported"
argument_list|)
throw|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|GMailProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
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
block|}
end_class

end_unit

