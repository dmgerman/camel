begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Map
import|;
end_import

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
name|MessagingException
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
name|RuntimeCamelException
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
name|DefaultMessage
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
name|ExchangeHelper
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
comment|/**  * Represents a {@link org.apache.camel.Message} for working with Mail  */
end_comment

begin_class
DECL|class|MailMessage
specifier|public
class|class
name|MailMessage
extends|extends
name|DefaultMessage
block|{
comment|// we need a copy of the original message in case we need to workaround a charset issue when extracting
comment|// mail content, see more in MailBinding
DECL|field|originalMailMessage
specifier|private
name|Message
name|originalMailMessage
decl_stmt|;
DECL|field|mailMessage
specifier|private
name|Message
name|mailMessage
decl_stmt|;
DECL|field|mapMailMessage
specifier|private
name|boolean
name|mapMailMessage
decl_stmt|;
DECL|method|MailMessage (Exchange exchange, Message message, boolean mapMailMessage)
specifier|public
name|MailMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Message
name|message
parameter_list|,
name|boolean
name|mapMailMessage
parameter_list|)
block|{
name|super
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|this
operator|.
name|originalMailMessage
operator|=
name|this
operator|.
name|mailMessage
operator|=
name|message
expr_stmt|;
name|this
operator|.
name|mapMailMessage
operator|=
name|mapMailMessage
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
comment|// do not dump the mail content, as it requires live connection to the mail server
return|return
literal|"MailMessage@"
operator|+
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|copy ()
specifier|public
name|MailMessage
name|copy
parameter_list|()
block|{
name|MailMessage
name|answer
init|=
operator|(
name|MailMessage
operator|)
name|super
operator|.
name|copy
argument_list|()
decl_stmt|;
name|answer
operator|.
name|originalMailMessage
operator|=
name|originalMailMessage
expr_stmt|;
name|answer
operator|.
name|mailMessage
operator|=
name|mailMessage
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Returns the original underlying Mail message      */
DECL|method|getOriginalMessage ()
specifier|public
name|Message
name|getOriginalMessage
parameter_list|()
block|{
return|return
name|originalMailMessage
return|;
block|}
comment|/**      * Returns the underlying Mail message      */
DECL|method|getMessage ()
specifier|public
name|Message
name|getMessage
parameter_list|()
block|{
return|return
name|mailMessage
return|;
block|}
DECL|method|setMessage (Message mailMessage)
specifier|public
name|void
name|setMessage
parameter_list|(
name|Message
name|mailMessage
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|originalMailMessage
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|originalMailMessage
operator|=
name|mailMessage
expr_stmt|;
block|}
name|this
operator|.
name|mailMessage
operator|=
name|mailMessage
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|MailMessage
name|newInstance
parameter_list|()
block|{
name|MailMessage
name|answer
init|=
operator|new
name|MailMessage
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|this
operator|.
name|mapMailMessage
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|createBody ()
specifier|protected
name|Object
name|createBody
parameter_list|()
block|{
if|if
condition|(
name|mailMessage
operator|!=
literal|null
condition|)
block|{
name|MailBinding
name|binding
init|=
name|ExchangeHelper
operator|.
name|getBinding
argument_list|(
name|getExchange
argument_list|()
argument_list|,
name|MailBinding
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|binding
operator|!=
literal|null
condition|?
name|binding
operator|.
name|extractBodyFromMail
argument_list|(
name|getExchange
argument_list|()
argument_list|,
name|this
argument_list|)
else|:
literal|null
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|populateInitialHeaders (Map<String, Object> map)
specifier|protected
name|void
name|populateInitialHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
name|mailMessage
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|MailBinding
name|binding
init|=
name|ExchangeHelper
operator|.
name|getBinding
argument_list|(
name|getExchange
argument_list|()
argument_list|,
name|MailBinding
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|binding
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|putAll
argument_list|(
name|binding
operator|.
name|extractHeadersFromMail
argument_list|(
name|mailMessage
argument_list|,
name|getExchange
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MessagingException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Error accessing headers due to: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|copyFrom (org.apache.camel.Message that)
specifier|public
name|void
name|copyFrom
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|that
parameter_list|)
block|{
comment|// only do a deep copy if we need to (yes when that is not a mail message, or if the mapMailMessage is true)
name|boolean
name|needCopy
init|=
operator|!
operator|(
name|that
operator|instanceof
name|MailMessage
operator|)
operator|||
operator|(
operator|(
operator|(
name|MailMessage
operator|)
name|that
operator|)
operator|.
name|mapMailMessage
operator|)
decl_stmt|;
if|if
condition|(
name|needCopy
condition|)
block|{
name|super
operator|.
name|copyFrom
argument_list|(
name|that
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// no deep copy needed, but copy message id
name|setMessageId
argument_list|(
name|that
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|that
operator|instanceof
name|MailMessage
condition|)
block|{
name|MailMessage
name|mailMessage
init|=
operator|(
name|MailMessage
operator|)
name|that
decl_stmt|;
name|this
operator|.
name|originalMailMessage
operator|=
name|mailMessage
operator|.
name|originalMailMessage
expr_stmt|;
name|this
operator|.
name|mailMessage
operator|=
name|mailMessage
operator|.
name|mailMessage
expr_stmt|;
name|this
operator|.
name|mapMailMessage
operator|=
name|mailMessage
operator|.
name|mapMailMessage
expr_stmt|;
block|}
comment|// cover over exchange if none has been assigned
if|if
condition|(
name|getExchange
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setExchange
argument_list|(
name|that
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

