begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|StreamCache
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
name|file
operator|.
name|GenericFile
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
name|jaxp
operator|.
name|BytesSource
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
name|jaxp
operator|.
name|StringSource
import|;
end_import

begin_comment
comment|/**  * Some helper methods when working with {@link org.apache.camel.Message}.  *  * @version   */
end_comment

begin_class
DECL|class|MessageHelper
specifier|public
specifier|final
class|class
name|MessageHelper
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|MessageHelper ()
specifier|private
name|MessageHelper
parameter_list|()
block|{     }
comment|/**      * Extracts the given body and returns it as a String, that      * can be used for logging etc.      *<p/>      * Will handle stream based bodies wrapped in StreamCache.      *      * @param message  the message with the body      * @return the body as String, can return<tt>null</null> if no body      */
DECL|method|extractBodyAsString (Message message)
specifier|public
specifier|static
name|String
name|extractBodyAsString
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|StreamCache
name|newBody
init|=
name|message
operator|.
name|getBody
argument_list|(
name|StreamCache
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|newBody
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setBody
argument_list|(
name|newBody
argument_list|)
expr_stmt|;
block|}
name|Object
name|answer
init|=
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|message
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|newBody
operator|!=
literal|null
condition|)
block|{
comment|// Reset the InputStreamCache
name|newBody
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
operator|!=
literal|null
condition|?
name|answer
operator|.
name|toString
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**      * Gets the given body class type name as a String.      *<p/>      * Will skip java.lang. for the build in Java types.      *      * @param message  the message with the body      * @return the body typename as String, can return<tt>null</null> if no body      */
DECL|method|getBodyTypeName (Message message)
specifier|public
specifier|static
name|String
name|getBodyTypeName
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|answer
init|=
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|message
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
operator|&&
name|answer
operator|.
name|startsWith
argument_list|(
literal|"java.lang."
argument_list|)
condition|)
block|{
return|return
name|answer
operator|.
name|substring
argument_list|(
literal|10
argument_list|)
return|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * If the message body contains a {@link StreamCache} instance, reset the cache to       * enable reading from it again.      *       * @param message the message for which to reset the body      */
DECL|method|resetStreamCache (Message message)
specifier|public
specifier|static
name|void
name|resetStreamCache
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|message
operator|.
name|getBody
argument_list|()
operator|instanceof
name|StreamCache
condition|)
block|{
operator|(
operator|(
name|StreamCache
operator|)
name|message
operator|.
name|getBody
argument_list|()
operator|)
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns the MIME content type on the message or<tt>null</tt> if none defined      */
DECL|method|getContentType (Message message)
specifier|public
specifier|static
name|String
name|getContentType
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|message
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Returns the MIME content encoding on the message or<tt>null</tt> if none defined      */
DECL|method|getContentEncoding (Message message)
specifier|public
specifier|static
name|String
name|getContentEncoding
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|message
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Extracts the body for logging purpose.      *<p/>      * Will clip the body if its too big for logging.      * Will prepend the message with<tt>Message:</tt>      *      * @see org.apache.camel.Exchange#LOG_DEBUG_BODY_STREAMS      * @see org.apache.camel.Exchange#LOG_DEBUG_BODY_MAX_CHARS      * @param message the message      * @return the logging message      */
DECL|method|extractBodyForLogging (Message message)
specifier|public
specifier|static
name|String
name|extractBodyForLogging
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|extractBodyForLogging
argument_list|(
name|message
argument_list|,
literal|"Message: "
argument_list|)
return|;
block|}
comment|/**      * Extracts the body for logging purpose.      *<p/>      * Will clip the body if its too big for logging.      *      * @see org.apache.camel.Exchange#LOG_DEBUG_BODY_STREAMS      * @see org.apache.camel.Exchange#LOG_DEBUG_BODY_MAX_CHARS      * @param message the message      * @param prepend a message to prepend      * @return the logging message      */
DECL|method|extractBodyForLogging (Message message, String prepend)
specifier|public
specifier|static
name|String
name|extractBodyForLogging
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|prepend
parameter_list|)
block|{
name|boolean
name|streams
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|message
operator|.
name|getExchange
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|property
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getContext
argument_list|()
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|Exchange
operator|.
name|LOG_DEBUG_BODY_STREAMS
argument_list|)
decl_stmt|;
if|if
condition|(
name|property
operator|!=
literal|null
condition|)
block|{
name|streams
operator|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Boolean
operator|.
name|class
argument_list|,
name|property
argument_list|)
expr_stmt|;
block|}
block|}
comment|// default to 1000 chars
name|int
name|maxChars
init|=
literal|1000
decl_stmt|;
if|if
condition|(
name|message
operator|.
name|getExchange
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|property
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getContext
argument_list|()
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|Exchange
operator|.
name|LOG_DEBUG_BODY_MAX_CHARS
argument_list|)
decl_stmt|;
if|if
condition|(
name|property
operator|!=
literal|null
condition|)
block|{
name|maxChars
operator|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|property
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|extractBodyForLogging
argument_list|(
name|message
argument_list|,
name|prepend
argument_list|,
name|streams
argument_list|,
literal|false
argument_list|,
name|maxChars
argument_list|)
return|;
block|}
comment|/**      * Extracts the body for logging purpose.      *<p/>      * Will clip the body if its too big for logging.      *      * @see org.apache.camel.Exchange#LOG_DEBUG_BODY_MAX_CHARS      * @param message the message      * @param prepend a message to prepend      * @param allowStreams whether or not streams is allowed      * @param allowFiles whether or not files is allowed      * @param maxChars limit to maximum number of chars. Use 0 or negative value to not limit at all.      * @return the logging message      */
DECL|method|extractBodyForLogging (Message message, String prepend, boolean allowStreams, boolean allowFiles, int maxChars)
specifier|public
specifier|static
name|String
name|extractBodyForLogging
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|prepend
parameter_list|,
name|boolean
name|allowStreams
parameter_list|,
name|boolean
name|allowFiles
parameter_list|,
name|int
name|maxChars
parameter_list|)
block|{
name|Object
name|obj
init|=
name|message
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
name|prepend
operator|+
literal|"[Body is null]"
return|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|StringSource
operator|||
name|obj
operator|instanceof
name|BytesSource
condition|)
block|{
comment|// these two are okay
block|}
elseif|else
if|if
condition|(
operator|!
name|allowStreams
operator|&&
name|obj
operator|instanceof
name|StreamCache
condition|)
block|{
return|return
name|prepend
operator|+
literal|"[Body is instance of org.apache.camel.StreamCache]"
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|allowStreams
operator|&&
name|obj
operator|instanceof
name|StreamSource
condition|)
block|{
return|return
name|prepend
operator|+
literal|"[Body is instance of java.xml.transform.StreamSource]"
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|allowStreams
operator|&&
name|obj
operator|instanceof
name|InputStream
condition|)
block|{
return|return
name|prepend
operator|+
literal|"[Body is instance of java.io.InputStream]"
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|allowStreams
operator|&&
name|obj
operator|instanceof
name|OutputStream
condition|)
block|{
return|return
name|prepend
operator|+
literal|"[Body is instance of java.io.OutputStream]"
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|allowStreams
operator|&&
name|obj
operator|instanceof
name|Reader
condition|)
block|{
return|return
name|prepend
operator|+
literal|"[Body is instance of java.io.Reader]"
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|allowStreams
operator|&&
name|obj
operator|instanceof
name|Writer
condition|)
block|{
return|return
name|prepend
operator|+
literal|"[Body is instance of java.io.Writer]"
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|allowFiles
operator|&&
operator|(
name|obj
operator|instanceof
name|GenericFile
operator|||
name|obj
operator|instanceof
name|File
operator|)
condition|)
block|{
return|return
name|prepend
operator|+
literal|"[Body is file based: "
operator|+
name|obj
operator|+
literal|"]"
return|;
block|}
comment|// is the body a stream cache
name|StreamCache
name|cache
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|StreamCache
condition|)
block|{
name|cache
operator|=
operator|(
name|StreamCache
operator|)
name|obj
expr_stmt|;
block|}
else|else
block|{
name|cache
operator|=
literal|null
expr_stmt|;
block|}
comment|// grab the message body as a string
name|String
name|body
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|message
operator|.
name|getExchange
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|body
operator|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore as the body is for logging purpose
block|}
block|}
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
name|body
operator|=
name|obj
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
comment|// reset stream cache after use
if|if
condition|(
name|cache
operator|!=
literal|null
condition|)
block|{
name|cache
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
return|return
name|prepend
operator|+
literal|"[Body is null]"
return|;
block|}
comment|// clip body if length enabled and the body is too big
if|if
condition|(
name|maxChars
operator|>
literal|0
operator|&&
name|body
operator|.
name|length
argument_list|()
operator|>
name|maxChars
condition|)
block|{
name|body
operator|=
name|body
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|maxChars
argument_list|)
operator|+
literal|"... [Body clipped after "
operator|+
name|maxChars
operator|+
literal|" chars, total length is "
operator|+
name|body
operator|.
name|length
argument_list|()
operator|+
literal|"]"
expr_stmt|;
block|}
return|return
name|prepend
operator|+
name|body
return|;
block|}
comment|/**      * Dumps the message as a generic XML structure.      *      * @param message  the message      * @return the XML      */
DECL|method|dumpAsXml (Message message)
specifier|public
specifier|static
name|String
name|dumpAsXml
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|dumpAsXml
argument_list|(
name|message
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Dumps the message as a generic XML structure.      *      * @param message  the message      * @param includeBody whether or not to include the message body      * @return the XML      */
DECL|method|dumpAsXml (Message message, boolean includeBody)
specifier|public
specifier|static
name|String
name|dumpAsXml
parameter_list|(
name|Message
name|message
parameter_list|,
name|boolean
name|includeBody
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// include exchangeId as attribute on the<message> tag
name|sb
operator|.
name|append
argument_list|(
literal|"<message exchangeId=\""
argument_list|)
operator|.
name|append
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getExchangeId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\">\n"
argument_list|)
expr_stmt|;
comment|// headers
if|if
condition|(
name|message
operator|.
name|hasHeaders
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"<headers>\n"
argument_list|)
expr_stmt|;
comment|// sort the headers so they are listed A..Z
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|message
operator|.
name|getHeaders
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|String
name|type
init|=
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<header key=\""
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" type=\""
operator|+
name|type
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
comment|// dump header value as XML, use Camel type converter to convert to String
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|String
name|xml
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|xml
operator|!=
literal|null
condition|)
block|{
comment|// must always xml encode
name|sb
operator|.
name|append
argument_list|(
name|StringHelper
operator|.
name|xmlEncode
argument_list|(
name|xml
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore as the body is for logging purpose
block|}
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"</header>\n"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"</headers>\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|includeBody
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"<body"
argument_list|)
expr_stmt|;
name|String
name|type
init|=
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|message
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" type=\""
operator|+
name|type
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
comment|// dump body value as XML, use Camel type converter to convert to String
comment|// do not allow streams, but allow files, and clip very big message bodies (128kb)
name|String
name|xml
init|=
name|extractBodyForLogging
argument_list|(
name|message
argument_list|,
literal|""
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|128
operator|*
literal|1024
argument_list|)
decl_stmt|;
if|if
condition|(
name|xml
operator|!=
literal|null
condition|)
block|{
comment|// must always xml encode
name|sb
operator|.
name|append
argument_list|(
name|StringHelper
operator|.
name|xmlEncode
argument_list|(
name|xml
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"</body>\n"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"</message>"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Copies the headers from the source to the target message.      *      * @param source the source message      * @param target the target message      * @param override whether to override existing headers      */
DECL|method|copyHeaders (Message source, Message target, boolean override)
specifier|public
specifier|static
name|void
name|copyHeaders
parameter_list|(
name|Message
name|source
parameter_list|,
name|Message
name|target
parameter_list|,
name|boolean
name|override
parameter_list|)
block|{
if|if
condition|(
operator|!
name|source
operator|.
name|hasHeaders
argument_list|()
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|source
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|target
operator|.
name|getHeader
argument_list|(
name|key
argument_list|)
operator|==
literal|null
operator|||
name|override
condition|)
block|{
name|target
operator|.
name|setHeader
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

