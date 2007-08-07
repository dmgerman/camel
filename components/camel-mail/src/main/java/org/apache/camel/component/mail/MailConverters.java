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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|BodyPart
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

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeMultipart
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
name|Converter
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|MailConverters
specifier|public
class|class
name|MailConverters
block|{
comment|/**      * Converts the given JavaMail message to a String body      *      * @param message the message      * @return the String content      * @throws MessagingException      * @throws IOException      */
annotation|@
name|Converter
DECL|method|toString (Message message)
specifier|public
name|String
name|toString
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|MessagingException
throws|,
name|IOException
block|{
name|Object
name|content
init|=
name|message
operator|.
name|getContent
argument_list|()
decl_stmt|;
if|if
condition|(
name|content
operator|instanceof
name|MimeMultipart
condition|)
block|{
name|MimeMultipart
name|multipart
init|=
operator|(
name|MimeMultipart
operator|)
name|content
decl_stmt|;
if|if
condition|(
name|multipart
operator|.
name|getCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|BodyPart
name|part
init|=
name|multipart
operator|.
name|getBodyPart
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|content
operator|=
name|part
operator|.
name|getContent
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|content
operator|!=
literal|null
condition|)
block|{
return|return
name|content
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (Multipart multipart)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Multipart
name|multipart
parameter_list|)
throws|throws
name|MessagingException
throws|,
name|IOException
block|{
name|int
name|size
init|=
name|multipart
operator|.
name|getCount
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|BodyPart
name|part
init|=
name|multipart
operator|.
name|getBodyPart
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|part
operator|.
name|getContentType
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"text"
argument_list|)
condition|)
block|{
return|return
name|part
operator|.
name|getContent
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

