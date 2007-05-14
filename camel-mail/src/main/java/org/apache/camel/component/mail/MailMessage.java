begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Header
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
name|java
operator|.
name|util
operator|.
name|Enumeration
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

begin_comment
comment|/**  * Represents a {@link org.apache.camel.Message} for working with Mail  *  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|MailMessage
specifier|public
class|class
name|MailMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|mailMessage
specifier|private
name|Message
name|mailMessage
decl_stmt|;
DECL|method|MailMessage ()
specifier|public
name|MailMessage
parameter_list|()
block|{     }
DECL|method|MailMessage (Message message)
specifier|public
name|MailMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|mailMessage
operator|=
name|message
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
if|if
condition|(
name|mailMessage
operator|!=
literal|null
condition|)
block|{
return|return
literal|"MailMessage: "
operator|+
name|mailMessage
return|;
block|}
else|else
block|{
return|return
literal|"MailMessage: "
operator|+
name|getBody
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getExchange ()
specifier|public
name|MailExchange
name|getExchange
parameter_list|()
block|{
return|return
operator|(
name|MailExchange
operator|)
name|super
operator|.
name|getExchange
argument_list|()
return|;
block|}
comment|/**      * Returns the underlying Mail message      *      * @return the underlying Mail message      */
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
name|this
operator|.
name|mailMessage
operator|=
name|mailMessage
expr_stmt|;
block|}
DECL|method|getHeader (String name)
specifier|public
name|Object
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
index|[]
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|mailMessage
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|answer
operator|=
name|mailMessage
operator|.
name|getHeader
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MessagingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MessageHeaderAccessException
argument_list|(
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
return|return
name|super
operator|.
name|getHeader
argument_list|(
name|name
argument_list|)
return|;
block|}
if|if
condition|(
name|answer
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
name|answer
index|[
literal|0
index|]
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|MailMessage
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|MailMessage
argument_list|()
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
return|return
name|getExchange
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|extractBodyFromMail
argument_list|(
name|getExchange
argument_list|()
argument_list|,
name|mailMessage
argument_list|)
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
name|Enumeration
name|names
decl_stmt|;
try|try
block|{
name|names
operator|=
name|mailMessage
operator|.
name|getAllHeaders
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MessagingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MessageHeaderNamesAccessException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Copying...."
argument_list|)
expr_stmt|;
try|try
block|{
while|while
condition|(
name|names
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|Header
name|header
init|=
operator|(
name|Header
operator|)
name|names
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|,
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Set: "
operator|+
name|header
operator|.
name|getName
argument_list|()
operator|+
literal|"="
operator|+
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MessageHeaderNamesAccessException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

