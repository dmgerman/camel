begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ses
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|ses
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

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
name|Enumeration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Address
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Flags
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
name|javax
operator|.
name|mail
operator|.
name|Multipart
import|;
end_import

begin_class
DECL|class|MockMessage
specifier|public
class|class
name|MockMessage
extends|extends
name|Message
block|{
annotation|@
name|Override
DECL|method|getSize ()
specifier|public
name|int
name|getSize
parameter_list|()
throws|throws
name|MessagingException
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|getLineCount ()
specifier|public
name|int
name|getLineCount
parameter_list|()
throws|throws
name|MessagingException
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|getContentType ()
specifier|public
name|String
name|getContentType
parameter_list|()
throws|throws
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|isMimeType (String mimeType)
specifier|public
name|boolean
name|isMimeType
parameter_list|(
name|String
name|mimeType
parameter_list|)
throws|throws
name|MessagingException
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|getDisposition ()
specifier|public
name|String
name|getDisposition
parameter_list|()
throws|throws
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setDisposition (String disposition)
specifier|public
name|void
name|setDisposition
parameter_list|(
name|String
name|disposition
parameter_list|)
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
throws|throws
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setDescription (String description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|getFileName ()
specifier|public
name|String
name|getFileName
parameter_list|()
throws|throws
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setFileName (String filename)
specifier|public
name|void
name|setFileName
parameter_list|(
name|String
name|filename
parameter_list|)
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|getInputStream ()
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
throws|,
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getDataHandler ()
specifier|public
name|DataHandler
name|getDataHandler
parameter_list|()
throws|throws
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getContent ()
specifier|public
name|Object
name|getContent
parameter_list|()
throws|throws
name|IOException
throws|,
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setDataHandler (DataHandler dh)
specifier|public
name|void
name|setDataHandler
parameter_list|(
name|DataHandler
name|dh
parameter_list|)
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|setContent (Object obj, String type)
specifier|public
name|void
name|setContent
parameter_list|(
name|Object
name|obj
parameter_list|,
name|String
name|type
parameter_list|)
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|setText (String text)
specifier|public
name|void
name|setText
parameter_list|(
name|String
name|text
parameter_list|)
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|setContent (Multipart mp)
specifier|public
name|void
name|setContent
parameter_list|(
name|Multipart
name|mp
parameter_list|)
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|writeTo (OutputStream os)
specifier|public
name|void
name|writeTo
parameter_list|(
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
throws|,
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|getHeader (String headername)
specifier|public
name|String
index|[]
name|getHeader
parameter_list|(
name|String
name|headername
parameter_list|)
throws|throws
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setHeader (String headername, String headervalue)
specifier|public
name|void
name|setHeader
parameter_list|(
name|String
name|headername
parameter_list|,
name|String
name|headervalue
parameter_list|)
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|addHeader (String headername, String headervalue)
specifier|public
name|void
name|addHeader
parameter_list|(
name|String
name|headername
parameter_list|,
name|String
name|headervalue
parameter_list|)
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|removeHeader (String headername)
specifier|public
name|void
name|removeHeader
parameter_list|(
name|String
name|headername
parameter_list|)
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|getAllHeaders ()
specifier|public
name|Enumeration
argument_list|<
name|Header
argument_list|>
name|getAllHeaders
parameter_list|()
throws|throws
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getMatchingHeaders (String[] headernames)
specifier|public
name|Enumeration
argument_list|<
name|Header
argument_list|>
name|getMatchingHeaders
parameter_list|(
name|String
index|[]
name|headernames
parameter_list|)
throws|throws
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getNonMatchingHeaders (String[] headernames)
specifier|public
name|Enumeration
argument_list|<
name|Header
argument_list|>
name|getNonMatchingHeaders
parameter_list|(
name|String
index|[]
name|headernames
parameter_list|)
throws|throws
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getFrom ()
specifier|public
name|Address
index|[]
name|getFrom
parameter_list|()
throws|throws
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setFrom ()
specifier|public
name|void
name|setFrom
parameter_list|()
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|setFrom (Address address)
specifier|public
name|void
name|setFrom
parameter_list|(
name|Address
name|address
parameter_list|)
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|addFrom (Address[] addresses)
specifier|public
name|void
name|addFrom
parameter_list|(
name|Address
index|[]
name|addresses
parameter_list|)
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|getRecipients (RecipientType type)
specifier|public
name|Address
index|[]
name|getRecipients
parameter_list|(
name|RecipientType
name|type
parameter_list|)
throws|throws
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setRecipients (RecipientType type, Address[] addresses)
specifier|public
name|void
name|setRecipients
parameter_list|(
name|RecipientType
name|type
parameter_list|,
name|Address
index|[]
name|addresses
parameter_list|)
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|addRecipients (RecipientType type, Address[] addresses)
specifier|public
name|void
name|addRecipients
parameter_list|(
name|RecipientType
name|type
parameter_list|,
name|Address
index|[]
name|addresses
parameter_list|)
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|getSubject ()
specifier|public
name|String
name|getSubject
parameter_list|()
throws|throws
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setSubject (String subject)
specifier|public
name|void
name|setSubject
parameter_list|(
name|String
name|subject
parameter_list|)
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|getSentDate ()
specifier|public
name|Date
name|getSentDate
parameter_list|()
throws|throws
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setSentDate (Date date)
specifier|public
name|void
name|setSentDate
parameter_list|(
name|Date
name|date
parameter_list|)
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|getReceivedDate ()
specifier|public
name|Date
name|getReceivedDate
parameter_list|()
throws|throws
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getFlags ()
specifier|public
name|Flags
name|getFlags
parameter_list|()
throws|throws
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setFlags (Flags flag, boolean set)
specifier|public
name|void
name|setFlags
parameter_list|(
name|Flags
name|flag
parameter_list|,
name|boolean
name|set
parameter_list|)
throws|throws
name|MessagingException
block|{     }
annotation|@
name|Override
DECL|method|reply (boolean replyToAll)
specifier|public
name|Message
name|reply
parameter_list|(
name|boolean
name|replyToAll
parameter_list|)
throws|throws
name|MessagingException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|saveChanges ()
specifier|public
name|void
name|saveChanges
parameter_list|()
throws|throws
name|MessagingException
block|{     }
block|}
end_class

end_unit

