begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Closeable
import|;
end_import

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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|CharBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|Channels
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|ReadableByteChannel
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
name|Charset
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
name|CharsetDecoder
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
name|IllegalCharsetNameException
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
name|UnsupportedCharsetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|InputMismatchException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
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

begin_class
DECL|class|Scanner
specifier|public
specifier|final
class|class
name|Scanner
implements|implements
name|Iterator
argument_list|<
name|String
argument_list|>
implements|,
name|Closeable
block|{
DECL|field|CACHE
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Pattern
argument_list|>
name|CACHE
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Pattern
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|boolean
name|removeEldestEntry
parameter_list|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Pattern
argument_list|>
name|eldest
parameter_list|)
block|{
return|return
name|size
argument_list|()
operator|>=
literal|7
return|;
block|}
block|}
decl_stmt|;
DECL|field|WHITESPACE_PATTERN
specifier|private
specifier|static
specifier|final
name|String
name|WHITESPACE_PATTERN
init|=
literal|"\\s+"
decl_stmt|;
DECL|field|FIND_ANY_PATTERN
specifier|private
specifier|static
specifier|final
name|String
name|FIND_ANY_PATTERN
init|=
literal|"(?s).*"
decl_stmt|;
DECL|field|BUFFER_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|BUFFER_SIZE
init|=
literal|1024
decl_stmt|;
DECL|field|source
specifier|private
name|Readable
name|source
decl_stmt|;
DECL|field|delimPattern
specifier|private
name|Pattern
name|delimPattern
decl_stmt|;
DECL|field|matcher
specifier|private
name|Matcher
name|matcher
decl_stmt|;
DECL|field|buf
specifier|private
name|CharBuffer
name|buf
decl_stmt|;
DECL|field|position
specifier|private
name|int
name|position
decl_stmt|;
DECL|field|inputExhausted
specifier|private
name|boolean
name|inputExhausted
decl_stmt|;
DECL|field|needInput
specifier|private
name|boolean
name|needInput
decl_stmt|;
DECL|field|skipped
specifier|private
name|boolean
name|skipped
decl_stmt|;
DECL|field|savedPosition
specifier|private
name|int
name|savedPosition
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|closed
specifier|private
name|boolean
name|closed
decl_stmt|;
DECL|field|lastIOException
specifier|private
name|IOException
name|lastIOException
decl_stmt|;
DECL|method|Scanner (InputStream source, String charsetName, String pattern)
specifier|public
name|Scanner
parameter_list|(
name|InputStream
name|source
parameter_list|,
name|String
name|charsetName
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|source
argument_list|,
literal|"source"
argument_list|)
argument_list|,
name|toDecoder
argument_list|(
name|charsetName
argument_list|)
argument_list|)
argument_list|,
name|cachePattern
argument_list|(
name|pattern
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|Scanner (File source, String charsetName, String pattern)
specifier|public
name|Scanner
parameter_list|(
name|File
name|source
parameter_list|,
name|String
name|charsetName
parameter_list|,
name|String
name|pattern
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
name|this
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|source
argument_list|,
literal|"source"
argument_list|)
argument_list|)
operator|.
name|getChannel
argument_list|()
argument_list|,
name|charsetName
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
block|}
DECL|method|Scanner (String source, String pattern)
specifier|public
name|Scanner
parameter_list|(
name|String
name|source
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|StringReader
argument_list|(
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|source
argument_list|,
literal|"source"
argument_list|)
argument_list|)
argument_list|,
name|cachePattern
argument_list|(
name|pattern
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|Scanner (ReadableByteChannel source, String charsetName, String pattern)
specifier|public
name|Scanner
parameter_list|(
name|ReadableByteChannel
name|source
parameter_list|,
name|String
name|charsetName
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
name|this
argument_list|(
name|Channels
operator|.
name|newReader
argument_list|(
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|source
argument_list|,
literal|"source"
argument_list|)
argument_list|,
name|toDecoder
argument_list|(
name|charsetName
argument_list|)
argument_list|,
operator|-
literal|1
argument_list|)
argument_list|,
name|cachePattern
argument_list|(
name|pattern
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|Scanner (Readable source, String pattern)
specifier|public
name|Scanner
parameter_list|(
name|Readable
name|source
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
name|this
argument_list|(
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|source
argument_list|,
literal|"source"
argument_list|)
argument_list|,
name|cachePattern
argument_list|(
name|pattern
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|Scanner (Readable source, Pattern pattern)
specifier|private
name|Scanner
parameter_list|(
name|Readable
name|source
parameter_list|,
name|Pattern
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
name|delimPattern
operator|=
name|pattern
operator|!=
literal|null
condition|?
name|pattern
else|:
name|cachePattern
argument_list|(
name|WHITESPACE_PATTERN
argument_list|)
expr_stmt|;
name|buf
operator|=
name|CharBuffer
operator|.
name|allocate
argument_list|(
name|BUFFER_SIZE
argument_list|)
expr_stmt|;
name|buf
operator|.
name|limit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|matcher
operator|=
name|delimPattern
operator|.
name|matcher
argument_list|(
name|buf
argument_list|)
expr_stmt|;
name|matcher
operator|.
name|useTransparentBounds
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|matcher
operator|.
name|useAnchoringBounds
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|toDecoder (String charsetName)
specifier|private
specifier|static
name|CharsetDecoder
name|toDecoder
parameter_list|(
name|String
name|charsetName
parameter_list|)
block|{
try|try
block|{
name|Charset
name|cs
init|=
name|charsetName
operator|!=
literal|null
condition|?
name|Charset
operator|.
name|forName
argument_list|(
name|charsetName
argument_list|)
else|:
name|Charset
operator|.
name|defaultCharset
argument_list|()
decl_stmt|;
return|return
name|cs
operator|.
name|newDecoder
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IllegalCharsetNameException
decl||
name|UnsupportedCharsetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|saveState
argument_list|()
expr_stmt|;
while|while
condition|(
operator|!
name|inputExhausted
condition|)
block|{
if|if
condition|(
name|hasTokenInBuffer
argument_list|()
condition|)
block|{
name|revertState
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
name|readMore
argument_list|()
expr_stmt|;
block|}
name|boolean
name|result
init|=
name|hasTokenInBuffer
argument_list|()
decl_stmt|;
name|revertState
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|String
name|next
parameter_list|()
block|{
name|checkClosed
argument_list|()
expr_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|String
name|token
init|=
name|getCompleteTokenInBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|token
operator|!=
literal|null
condition|)
block|{
name|skipped
operator|=
literal|false
expr_stmt|;
return|return
name|token
return|;
block|}
if|if
condition|(
name|needInput
condition|)
block|{
name|readMore
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|throwFor
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|saveState ()
specifier|private
name|void
name|saveState
parameter_list|()
block|{
name|savedPosition
operator|=
name|position
expr_stmt|;
block|}
DECL|method|revertState ()
specifier|private
name|void
name|revertState
parameter_list|()
block|{
name|position
operator|=
name|savedPosition
expr_stmt|;
name|savedPosition
operator|=
operator|-
literal|1
expr_stmt|;
name|skipped
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|readMore ()
specifier|private
name|void
name|readMore
parameter_list|()
block|{
if|if
condition|(
name|buf
operator|.
name|limit
argument_list|()
operator|==
name|buf
operator|.
name|capacity
argument_list|()
condition|)
block|{
name|expandBuffer
argument_list|()
expr_stmt|;
block|}
name|int
name|p
init|=
name|buf
operator|.
name|position
argument_list|()
decl_stmt|;
name|buf
operator|.
name|position
argument_list|(
name|buf
operator|.
name|limit
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|limit
argument_list|(
name|buf
operator|.
name|capacity
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|n
decl_stmt|;
try|try
block|{
name|n
operator|=
name|source
operator|.
name|read
argument_list|(
name|buf
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|lastIOException
operator|=
name|ioe
expr_stmt|;
name|n
operator|=
operator|-
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|n
operator|==
operator|-
literal|1
condition|)
block|{
name|inputExhausted
operator|=
literal|true
expr_stmt|;
name|needInput
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|n
operator|>
literal|0
condition|)
block|{
name|needInput
operator|=
literal|false
expr_stmt|;
block|}
name|buf
operator|.
name|limit
argument_list|(
name|buf
operator|.
name|position
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|position
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
DECL|method|expandBuffer ()
specifier|private
name|void
name|expandBuffer
parameter_list|()
block|{
name|int
name|offset
init|=
name|savedPosition
operator|==
operator|-
literal|1
condition|?
name|position
else|:
name|savedPosition
decl_stmt|;
name|buf
operator|.
name|position
argument_list|(
name|offset
argument_list|)
expr_stmt|;
if|if
condition|(
name|offset
operator|>
literal|0
condition|)
block|{
name|buf
operator|.
name|compact
argument_list|()
expr_stmt|;
name|translateSavedIndexes
argument_list|(
name|offset
argument_list|)
expr_stmt|;
name|position
operator|-=
name|offset
expr_stmt|;
name|buf
operator|.
name|flip
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|int
name|newSize
init|=
name|buf
operator|.
name|capacity
argument_list|()
operator|*
literal|2
decl_stmt|;
name|CharBuffer
name|newBuf
init|=
name|CharBuffer
operator|.
name|allocate
argument_list|(
name|newSize
argument_list|)
decl_stmt|;
name|newBuf
operator|.
name|put
argument_list|(
name|buf
argument_list|)
expr_stmt|;
name|newBuf
operator|.
name|flip
argument_list|()
expr_stmt|;
name|translateSavedIndexes
argument_list|(
name|offset
argument_list|)
expr_stmt|;
name|position
operator|-=
name|offset
expr_stmt|;
name|buf
operator|=
name|newBuf
expr_stmt|;
name|matcher
operator|.
name|reset
argument_list|(
name|buf
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|translateSavedIndexes (int offset)
specifier|private
name|void
name|translateSavedIndexes
parameter_list|(
name|int
name|offset
parameter_list|)
block|{
if|if
condition|(
name|savedPosition
operator|!=
operator|-
literal|1
condition|)
block|{
name|savedPosition
operator|-=
name|offset
expr_stmt|;
block|}
block|}
DECL|method|throwFor ()
specifier|private
name|void
name|throwFor
parameter_list|()
block|{
name|skipped
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|inputExhausted
operator|&&
name|position
operator|==
name|buf
operator|.
name|limit
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|InputMismatchException
argument_list|()
throw|;
block|}
block|}
DECL|method|hasTokenInBuffer ()
specifier|private
name|boolean
name|hasTokenInBuffer
parameter_list|()
block|{
name|matcher
operator|.
name|usePattern
argument_list|(
name|delimPattern
argument_list|)
expr_stmt|;
name|matcher
operator|.
name|region
argument_list|(
name|position
argument_list|,
name|buf
operator|.
name|limit
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|matcher
operator|.
name|lookingAt
argument_list|()
condition|)
block|{
name|position
operator|=
name|matcher
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
return|return
name|position
operator|!=
name|buf
operator|.
name|limit
argument_list|()
return|;
block|}
DECL|method|getCompleteTokenInBuffer ()
specifier|private
name|String
name|getCompleteTokenInBuffer
parameter_list|()
block|{
name|matcher
operator|.
name|usePattern
argument_list|(
name|delimPattern
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|skipped
condition|)
block|{
name|matcher
operator|.
name|region
argument_list|(
name|position
argument_list|,
name|buf
operator|.
name|limit
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|matcher
operator|.
name|lookingAt
argument_list|()
condition|)
block|{
if|if
condition|(
name|matcher
operator|.
name|hitEnd
argument_list|()
operator|&&
operator|!
name|inputExhausted
condition|)
block|{
name|needInput
operator|=
literal|true
expr_stmt|;
return|return
literal|null
return|;
block|}
name|skipped
operator|=
literal|true
expr_stmt|;
name|position
operator|=
name|matcher
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|position
operator|==
name|buf
operator|.
name|limit
argument_list|()
condition|)
block|{
if|if
condition|(
name|inputExhausted
condition|)
block|{
return|return
literal|null
return|;
block|}
name|needInput
operator|=
literal|true
expr_stmt|;
return|return
literal|null
return|;
block|}
name|matcher
operator|.
name|region
argument_list|(
name|position
argument_list|,
name|buf
operator|.
name|limit
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|foundNextDelim
init|=
name|matcher
operator|.
name|find
argument_list|()
decl_stmt|;
if|if
condition|(
name|foundNextDelim
operator|&&
operator|(
name|matcher
operator|.
name|end
argument_list|()
operator|==
name|position
operator|)
condition|)
block|{
name|foundNextDelim
operator|=
name|matcher
operator|.
name|find
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|foundNextDelim
condition|)
block|{
if|if
condition|(
name|matcher
operator|.
name|requireEnd
argument_list|()
operator|&&
operator|!
name|inputExhausted
condition|)
block|{
name|needInput
operator|=
literal|true
expr_stmt|;
return|return
literal|null
return|;
block|}
name|int
name|tokenEnd
init|=
name|matcher
operator|.
name|start
argument_list|()
decl_stmt|;
name|matcher
operator|.
name|usePattern
argument_list|(
name|cachePattern
argument_list|(
name|FIND_ANY_PATTERN
argument_list|)
argument_list|)
expr_stmt|;
name|matcher
operator|.
name|region
argument_list|(
name|position
argument_list|,
name|tokenEnd
argument_list|)
expr_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
name|String
name|s
init|=
name|matcher
operator|.
name|group
argument_list|()
decl_stmt|;
name|position
operator|=
name|matcher
operator|.
name|end
argument_list|()
expr_stmt|;
return|return
name|s
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
if|if
condition|(
name|inputExhausted
condition|)
block|{
name|matcher
operator|.
name|usePattern
argument_list|(
name|cachePattern
argument_list|(
name|FIND_ANY_PATTERN
argument_list|)
argument_list|)
expr_stmt|;
name|matcher
operator|.
name|region
argument_list|(
name|position
argument_list|,
name|buf
operator|.
name|limit
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
name|String
name|s
init|=
name|matcher
operator|.
name|group
argument_list|()
decl_stmt|;
name|position
operator|=
name|matcher
operator|.
name|end
argument_list|()
expr_stmt|;
return|return
name|s
return|;
block|}
return|return
literal|null
return|;
block|}
name|needInput
operator|=
literal|true
expr_stmt|;
return|return
literal|null
return|;
block|}
DECL|method|checkClosed ()
specifier|private
name|void
name|checkClosed
parameter_list|()
block|{
if|if
condition|(
name|closed
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|closed
condition|)
block|{
name|closed
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|source
operator|instanceof
name|Closeable
condition|)
block|{
try|try
block|{
operator|(
operator|(
name|Closeable
operator|)
name|source
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|lastIOException
operator|=
name|e
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|lastIOException
operator|!=
literal|null
condition|)
block|{
throw|throw
name|lastIOException
throw|;
block|}
block|}
DECL|method|cachePattern (String pattern)
specifier|private
specifier|static
name|Pattern
name|cachePattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
if|if
condition|(
name|pattern
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|CACHE
operator|.
name|computeIfAbsent
argument_list|(
name|pattern
argument_list|,
name|Pattern
operator|::
name|compile
argument_list|)
return|;
block|}
block|}
end_class

end_unit

