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
comment|/**      * Extracts the body for logging purpose.      *<p/>      * Will clip the body if its too big for logging.      *      * @see org.apache.camel.Exchange#LOG_DEBUG_BODY_MAX_CHARS      * @param message the message      * @return the logging message      */
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
literal|"Message: [Body is null]"
return|;
block|}
comment|// do not log streams by default
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
name|streams
operator|&&
name|obj
operator|instanceof
name|StreamSource
condition|)
block|{
return|return
literal|"Message: [Body is instance of java.xml.transform.StreamSource]"
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|streams
operator|&&
name|obj
operator|instanceof
name|InputStream
condition|)
block|{
return|return
literal|"Message: [Body is instance of java.io.InputStream]"
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|streams
operator|&&
name|obj
operator|instanceof
name|OutputStream
condition|)
block|{
return|return
literal|"Message: [Body is instance of java.io.OutputStream]"
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|streams
operator|&&
name|obj
operator|instanceof
name|Reader
condition|)
block|{
return|return
literal|"Message: [Body is instance of java.io.Reader]"
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|streams
operator|&&
name|obj
operator|instanceof
name|Writer
condition|)
block|{
return|return
literal|"Message: [Body is instance of java.io.Writer]"
return|;
block|}
comment|// default to 1000 chars
name|int
name|length
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
name|length
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
name|String
name|body
init|=
name|obj
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
return|return
literal|"Message: [Body is null]"
return|;
block|}
comment|// clip body if length enabled and the body is too big
if|if
condition|(
name|length
operator|>
literal|0
operator|&&
name|body
operator|.
name|length
argument_list|()
operator|>
name|length
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
name|length
argument_list|)
operator|+
literal|"... [Body clipped after "
operator|+
name|length
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
literal|"Message: "
operator|+
name|body
return|;
block|}
block|}
end_class

end_unit

