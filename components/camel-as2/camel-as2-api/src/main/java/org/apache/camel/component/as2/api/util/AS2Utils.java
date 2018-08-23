begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|KeyEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|InvalidAS2NameException
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
name|Header
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
name|HeaderIterator
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
name|HttpEntity
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
name|HttpEntityEnclosingRequest
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
name|HttpMessage
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
name|HttpRequest
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
name|HttpResponse
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
name|RequestLine
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

begin_comment
comment|/**  * Utility Methods used in AS2 Component  */
end_comment

begin_class
DECL|class|AS2Utils
specifier|public
specifier|final
class|class
name|AS2Utils
block|{
DECL|field|DQUOTE
specifier|public
specifier|static
specifier|final
name|String
name|DQUOTE
init|=
literal|"\""
decl_stmt|;
DECL|field|BACKSLASH
specifier|public
specifier|static
specifier|final
name|String
name|BACKSLASH
init|=
literal|"\\\\"
decl_stmt|;
DECL|field|AS2_TEXT_CHAR_SET
specifier|public
specifier|static
specifier|final
name|String
name|AS2_TEXT_CHAR_SET
init|=
literal|"[\u0021\u0023-\\\u005B\\\u005D-\u007E]"
decl_stmt|;
DECL|field|AS2_QUOTED_TEXT_CHAR_SET
specifier|public
specifier|static
specifier|final
name|String
name|AS2_QUOTED_TEXT_CHAR_SET
init|=
literal|"[\u0020\u0021\u0023-\\\u005B\\\u005D-\u007E]"
decl_stmt|;
DECL|field|AS2_QUOTED_PAIR
specifier|public
specifier|static
specifier|final
name|String
name|AS2_QUOTED_PAIR
init|=
name|BACKSLASH
operator|+
name|DQUOTE
operator|+
literal|"|"
operator|+
name|BACKSLASH
operator|+
name|BACKSLASH
decl_stmt|;
DECL|field|AS2_QUOTED_NAME
specifier|public
specifier|static
specifier|final
name|String
name|AS2_QUOTED_NAME
init|=
name|DQUOTE
operator|+
literal|"("
operator|+
name|AS2_QUOTED_TEXT_CHAR_SET
operator|+
literal|"|"
operator|+
name|AS2_QUOTED_PAIR
operator|+
literal|"){1,128}"
operator|+
name|DQUOTE
decl_stmt|;
DECL|field|AS2_ATOMIC_NAME
specifier|public
specifier|static
specifier|final
name|String
name|AS2_ATOMIC_NAME
init|=
literal|"("
operator|+
name|AS2_TEXT_CHAR_SET
operator|+
literal|"){1,128}"
decl_stmt|;
DECL|field|AS2_NAME
specifier|public
specifier|static
specifier|final
name|String
name|AS2_NAME
init|=
name|AS2_ATOMIC_NAME
operator|+
literal|"|"
operator|+
name|AS2_QUOTED_NAME
decl_stmt|;
DECL|field|AS_NAME_PATTERN
specifier|public
specifier|static
specifier|final
name|Pattern
name|AS_NAME_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|AS2_NAME
argument_list|)
decl_stmt|;
DECL|field|generator
specifier|private
specifier|static
name|Random
name|generator
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
DECL|method|AS2Utils ()
specifier|private
name|AS2Utils
parameter_list|()
block|{     }
comment|/**      * Validates if the given<code>name</code> is a valid AS2 Name      *      * @param name - the name to validate.      * @throws InvalidAS2NameException - If<code>name</code> is invalid.      */
DECL|method|validateAS2Name (String name)
specifier|public
specifier|static
name|void
name|validateAS2Name
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|InvalidAS2NameException
block|{
name|Matcher
name|matcher
init|=
name|AS_NAME_PATTERN
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
comment|// if name does not match, determine where it fails to match.
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|i
operator|=
name|name
operator|.
name|length
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|Matcher
name|region
init|=
name|matcher
operator|.
name|region
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|region
operator|.
name|matches
argument_list|()
operator|||
name|region
operator|.
name|hitEnd
argument_list|()
condition|)
block|{
break|break;
block|}
block|}
throw|throw
operator|new
name|InvalidAS2NameException
argument_list|(
name|name
argument_list|,
name|i
argument_list|)
throw|;
block|}
block|}
comment|/**      * Generates a globally unique message ID which includes<code>fqdn</code>: a fully qualified domain name (FQDN)      * @param fqdn - the fully qualified domain name to use in message id.      * @return The generated message id.      */
DECL|method|createMessageId (String fqdn)
specifier|public
specifier|static
name|String
name|createMessageId
parameter_list|(
name|String
name|fqdn
parameter_list|)
block|{
comment|/* Wall Clock Time in Nanoseconds */
comment|/* 64 Bit Random Number */
comment|/* Fully Qualified Domain Name */
return|return
literal|"<"
operator|+
name|Long
operator|.
name|toString
argument_list|(
name|System
operator|.
name|nanoTime
argument_list|()
argument_list|,
literal|36
argument_list|)
operator|+
literal|"."
operator|+
name|Long
operator|.
name|toString
argument_list|(
name|generator
operator|.
name|nextLong
argument_list|()
argument_list|,
literal|36
argument_list|)
operator|+
literal|"@"
operator|+
name|fqdn
operator|+
literal|">"
return|;
block|}
comment|/**      * Determines if<code>c</code> is a printable character.      * @param c - the character to test      * @return<code>true</code> if<code>c</code> is a printable character;<code>false</code> otherwise.      */
DECL|method|isPrintableChar (char c)
specifier|public
specifier|static
name|boolean
name|isPrintableChar
parameter_list|(
name|char
name|c
parameter_list|)
block|{
name|Character
operator|.
name|UnicodeBlock
name|block
init|=
name|Character
operator|.
name|UnicodeBlock
operator|.
name|of
argument_list|(
name|c
argument_list|)
decl_stmt|;
return|return
operator|(
operator|!
name|Character
operator|.
name|isISOControl
argument_list|(
name|c
argument_list|)
operator|)
operator|&&
name|c
operator|!=
name|KeyEvent
operator|.
name|CHAR_UNDEFINED
operator|&&
name|block
operator|!=
literal|null
operator|&&
name|block
operator|!=
name|Character
operator|.
name|UnicodeBlock
operator|.
name|SPECIALS
return|;
block|}
DECL|method|printRequest (HttpRequest request)
specifier|public
specifier|static
name|String
name|printRequest
parameter_list|(
name|HttpRequest
name|request
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|;
name|PrintStream
name|ps
operator|=
operator|new
name|PrintStream
argument_list|(
name|baos
argument_list|,
literal|true
argument_list|,
literal|"utf-8"
argument_list|)
init|)
block|{
name|printRequest
argument_list|(
name|ps
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|String
name|content
init|=
operator|new
name|String
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
return|return
name|content
return|;
block|}
block|}
DECL|method|printMessage (HttpMessage message)
specifier|public
specifier|static
name|String
name|printMessage
parameter_list|(
name|HttpMessage
name|message
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|;
name|PrintStream
name|ps
operator|=
operator|new
name|PrintStream
argument_list|(
name|baos
argument_list|,
literal|true
argument_list|,
literal|"utf-8"
argument_list|)
init|)
block|{
name|printMessage
argument_list|(
name|ps
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|String
name|content
init|=
operator|new
name|String
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
return|return
name|content
return|;
block|}
block|}
comment|/**      * Prints the contents of request to given print stream.      *      * @param out      *            - the stream printed to.      * @param request      *            - the request printed.      * @throws IOException - If failed to print request.      */
DECL|method|printRequest (PrintStream out, HttpRequest request)
specifier|public
specifier|static
name|void
name|printRequest
parameter_list|(
name|PrintStream
name|out
parameter_list|,
name|HttpRequest
name|request
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Print request line
name|RequestLine
name|requestLine
init|=
name|request
operator|.
name|getRequestLine
argument_list|()
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
name|requestLine
operator|.
name|getMethod
argument_list|()
operator|+
literal|' '
operator|+
name|requestLine
operator|.
name|getUri
argument_list|()
operator|+
literal|' '
operator|+
name|requestLine
operator|.
name|getProtocolVersion
argument_list|()
argument_list|)
expr_stmt|;
comment|// Write headers
for|for
control|(
specifier|final
name|HeaderIterator
name|it
init|=
name|request
operator|.
name|headerIterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Header
name|header
init|=
name|it
operator|.
name|nextHeader
argument_list|()
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
name|header
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
operator|(
name|header
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
name|header
operator|.
name|getValue
argument_list|()
operator|)
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
comment|// write empty line separating header from body.
if|if
condition|(
name|request
operator|instanceof
name|HttpEntityEnclosingRequest
condition|)
block|{
comment|// Write entity
name|HttpEntity
name|entity
init|=
operator|(
operator|(
name|HttpEntityEnclosingRequest
operator|)
name|request
operator|)
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|entity
operator|.
name|writeTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Prints the contents of an Http Message to given print stream.      *      * @param out - the stream printed to.      * @param message - the request printed.      * @throws IOException - If failed to print message.      */
DECL|method|printMessage (PrintStream out, HttpMessage message)
specifier|public
specifier|static
name|void
name|printMessage
parameter_list|(
name|PrintStream
name|out
parameter_list|,
name|HttpMessage
name|message
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Print request line
if|if
condition|(
name|message
operator|instanceof
name|HttpRequest
condition|)
block|{
name|RequestLine
name|requestLine
init|=
operator|(
operator|(
name|HttpRequest
operator|)
name|message
operator|)
operator|.
name|getRequestLine
argument_list|()
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
name|requestLine
operator|.
name|getMethod
argument_list|()
operator|+
literal|' '
operator|+
name|requestLine
operator|.
name|getUri
argument_list|()
operator|+
literal|' '
operator|+
name|requestLine
operator|.
name|getProtocolVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// HttpResponse
name|StatusLine
name|statusLine
init|=
operator|(
operator|(
name|HttpResponse
operator|)
name|message
operator|)
operator|.
name|getStatusLine
argument_list|()
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
name|statusLine
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Write headers
for|for
control|(
specifier|final
name|HeaderIterator
name|it
init|=
name|message
operator|.
name|headerIterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Header
name|header
init|=
name|it
operator|.
name|nextHeader
argument_list|()
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
name|header
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
operator|(
name|header
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
name|header
operator|.
name|getValue
argument_list|()
operator|)
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
comment|// write empty line separating header from body.
if|if
condition|(
name|message
operator|instanceof
name|HttpEntityEnclosingRequest
condition|)
block|{
comment|// Write entity
name|HttpEntity
name|entity
init|=
operator|(
operator|(
name|HttpEntityEnclosingRequest
operator|)
name|message
operator|)
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
name|entity
operator|.
name|writeTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|HttpResponse
condition|)
block|{
comment|// Write entity
name|HttpEntity
name|entity
init|=
operator|(
operator|(
name|HttpResponse
operator|)
name|message
operator|)
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
name|entity
operator|.
name|writeTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

