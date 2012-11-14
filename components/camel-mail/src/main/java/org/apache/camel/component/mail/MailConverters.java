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
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Converter
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
name|NoTypeConversionAvailableException
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
name|TypeConverter
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
name|converter
operator|.
name|IOConverter
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
name|TypeConverterRegistry
import|;
end_import

begin_comment
comment|/**  * JavaMail specific converters.  *  * @version   */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|MailConverters
specifier|public
specifier|final
class|class
name|MailConverters
block|{
DECL|field|NOW_DATE_FORMAT
specifier|private
specifier|static
specifier|final
name|String
name|NOW_DATE_FORMAT
init|=
literal|"yyyy-MM-dd HH:mm:SS"
decl_stmt|;
comment|// the now syntax: "now-24h" or "now - 24h" = the last 24 hours etc.
DECL|field|NOW_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|NOW_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"now\\s?(\\+|\\-)\\s?(.*)"
argument_list|)
decl_stmt|;
DECL|method|MailConverters ()
specifier|private
name|MailConverters
parameter_list|()
block|{
comment|//Utility Class
block|}
comment|/**      * Converts the given JavaMail message to a String body.      * Can return null.      */
annotation|@
name|Converter
DECL|method|toString (Message message)
specifier|public
specifier|static
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
comment|/**      * Converts the given JavaMail multipart to a String body, where the content-type of the multipart      * must be text based (ie start with text). Can return null.      */
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
comment|/**      * Converts the given JavaMail message to an InputStream.      */
annotation|@
name|Converter
DECL|method|toInputStream (Message message)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
throws|,
name|MessagingException
block|{
return|return
name|message
operator|.
name|getInputStream
argument_list|()
return|;
block|}
comment|/**      * Converts the given JavaMail multipart to a InputStream body, where the contenttype of the multipart      * must be text based (ie start with text). Can return null.      */
annotation|@
name|Converter
DECL|method|toInputStream (Multipart multipart)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|Multipart
name|multipart
parameter_list|)
throws|throws
name|IOException
throws|,
name|MessagingException
block|{
name|String
name|s
init|=
name|toString
argument_list|(
name|multipart
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|IOConverter
operator|.
name|toInputStream
argument_list|(
name|s
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toSearchTerm (SimpleSearchTerm simple, Exchange exchange)
specifier|public
specifier|static
name|SearchTerm
name|toSearchTerm
parameter_list|(
name|SimpleSearchTerm
name|simple
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|ParseException
throws|,
name|NoTypeConversionAvailableException
block|{
return|return
name|toSearchTerm
argument_list|(
name|simple
argument_list|,
name|exchange
operator|!=
literal|null
condition|?
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
else|:
literal|null
argument_list|)
return|;
block|}
DECL|method|toSearchTerm (SimpleSearchTerm simple, TypeConverter typeConverter)
specifier|public
specifier|static
name|SearchTerm
name|toSearchTerm
parameter_list|(
name|SimpleSearchTerm
name|simple
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|)
throws|throws
name|ParseException
throws|,
name|NoTypeConversionAvailableException
block|{
name|SearchTermBuilder
name|builder
init|=
operator|new
name|SearchTermBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|simple
operator|.
name|isUnseen
argument_list|()
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|unseen
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|simple
operator|.
name|getSubjectOrBody
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|text
init|=
name|simple
operator|.
name|getSubjectOrBody
argument_list|()
decl_stmt|;
name|builder
operator|=
name|builder
operator|.
name|subject
argument_list|(
name|text
argument_list|)
operator|.
name|body
argument_list|(
name|SearchTermBuilder
operator|.
name|Op
operator|.
name|or
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|simple
operator|.
name|getSubject
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|subject
argument_list|(
name|simple
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|simple
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|body
argument_list|(
name|simple
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|simple
operator|.
name|getFrom
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|from
argument_list|(
name|simple
operator|.
name|getFrom
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|simple
operator|.
name|getTo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|recipient
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|TO
argument_list|,
name|simple
operator|.
name|getTo
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|simple
operator|.
name|getFromSentDate
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|s
init|=
name|simple
operator|.
name|getFromSentDate
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
literal|"now"
argument_list|)
condition|)
block|{
name|long
name|offset
init|=
name|extractOffset
argument_list|(
name|s
argument_list|,
name|typeConverter
argument_list|)
decl_stmt|;
name|builder
operator|=
name|builder
operator|.
name|and
argument_list|(
operator|new
name|NowSearchTerm
argument_list|(
name|SearchTermBuilder
operator|.
name|Comparison
operator|.
name|GE
operator|.
name|asNum
argument_list|()
argument_list|,
literal|true
argument_list|,
name|offset
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|SimpleDateFormat
name|sdf
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|NOW_DATE_FORMAT
argument_list|)
decl_stmt|;
name|Date
name|date
init|=
name|sdf
operator|.
name|parse
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|builder
operator|=
name|builder
operator|.
name|sent
argument_list|(
name|SearchTermBuilder
operator|.
name|Comparison
operator|.
name|GE
argument_list|,
name|date
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|simple
operator|.
name|getToSentDate
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|s
init|=
name|simple
operator|.
name|getFromSentDate
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
literal|"now"
argument_list|)
condition|)
block|{
name|long
name|offset
init|=
name|extractOffset
argument_list|(
name|s
argument_list|,
name|typeConverter
argument_list|)
decl_stmt|;
name|builder
operator|=
name|builder
operator|.
name|and
argument_list|(
operator|new
name|NowSearchTerm
argument_list|(
name|SearchTermBuilder
operator|.
name|Comparison
operator|.
name|LE
operator|.
name|asNum
argument_list|()
argument_list|,
literal|true
argument_list|,
name|offset
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|SimpleDateFormat
name|sdf
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|NOW_DATE_FORMAT
argument_list|)
decl_stmt|;
name|Date
name|date
init|=
name|sdf
operator|.
name|parse
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|builder
operator|=
name|builder
operator|.
name|sent
argument_list|(
name|SearchTermBuilder
operator|.
name|Comparison
operator|.
name|LE
argument_list|,
name|date
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|simple
operator|.
name|getFromReceivedDate
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|s
init|=
name|simple
operator|.
name|getFromSentDate
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
literal|"now"
argument_list|)
condition|)
block|{
name|long
name|offset
init|=
name|extractOffset
argument_list|(
name|s
argument_list|,
name|typeConverter
argument_list|)
decl_stmt|;
name|builder
operator|=
name|builder
operator|.
name|and
argument_list|(
operator|new
name|NowSearchTerm
argument_list|(
name|SearchTermBuilder
operator|.
name|Comparison
operator|.
name|GE
operator|.
name|asNum
argument_list|()
argument_list|,
literal|false
argument_list|,
name|offset
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|SimpleDateFormat
name|sdf
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|NOW_DATE_FORMAT
argument_list|)
decl_stmt|;
name|Date
name|date
init|=
name|sdf
operator|.
name|parse
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|builder
operator|=
name|builder
operator|.
name|received
argument_list|(
name|SearchTermBuilder
operator|.
name|Comparison
operator|.
name|GE
argument_list|,
name|date
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|simple
operator|.
name|getToReceivedDate
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|s
init|=
name|simple
operator|.
name|getFromSentDate
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
literal|"now"
argument_list|)
condition|)
block|{
name|long
name|offset
init|=
name|extractOffset
argument_list|(
name|s
argument_list|,
name|typeConverter
argument_list|)
decl_stmt|;
name|builder
operator|=
name|builder
operator|.
name|and
argument_list|(
operator|new
name|NowSearchTerm
argument_list|(
name|SearchTermBuilder
operator|.
name|Comparison
operator|.
name|LE
operator|.
name|asNum
argument_list|()
argument_list|,
literal|false
argument_list|,
name|offset
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|SimpleDateFormat
name|sdf
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|NOW_DATE_FORMAT
argument_list|)
decl_stmt|;
name|Date
name|date
init|=
name|sdf
operator|.
name|parse
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|builder
operator|=
name|builder
operator|.
name|received
argument_list|(
name|SearchTermBuilder
operator|.
name|Comparison
operator|.
name|LE
argument_list|,
name|date
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|extractOffset (String now, TypeConverter typeConverter)
specifier|private
specifier|static
name|long
name|extractOffset
parameter_list|(
name|String
name|now
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|)
throws|throws
name|NoTypeConversionAvailableException
block|{
name|Matcher
name|matcher
init|=
name|NOW_PATTERN
operator|.
name|matcher
argument_list|(
name|now
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
name|String
name|op
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|remainder
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|// convert remainder to a time millis (eg we have a String -> long converter that supports
comment|// syntax with hours, days, minutes: eg 5h30m for 5 hours and 30 minutes).
name|long
name|offset
init|=
name|typeConverter
operator|.
name|mandatoryConvertTo
argument_list|(
name|long
operator|.
name|class
argument_list|,
name|remainder
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"+"
operator|.
name|equals
argument_list|(
name|op
argument_list|)
condition|)
block|{
return|return
name|offset
return|;
block|}
else|else
block|{
return|return
operator|-
literal|1
operator|*
name|offset
return|;
block|}
block|}
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

