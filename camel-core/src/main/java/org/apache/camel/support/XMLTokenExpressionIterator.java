begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|List
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
name|Set
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
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|InvalidPayloadException
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
name|StaxConverter
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
name|NamespaceAware
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
name|IOHelper
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|XMLTokenExpressionIterator
specifier|public
class|class
name|XMLTokenExpressionIterator
extends|extends
name|ExpressionAdapter
implements|implements
name|NamespaceAware
block|{
DECL|field|path
specifier|protected
specifier|final
name|String
name|path
decl_stmt|;
DECL|field|wrap
specifier|protected
name|boolean
name|wrap
decl_stmt|;
DECL|field|nsmap
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsmap
decl_stmt|;
DECL|method|XMLTokenExpressionIterator (String path, boolean wrap)
specifier|public
name|XMLTokenExpressionIterator
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|wrap
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|path
argument_list|,
literal|"path"
argument_list|)
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|wrap
operator|=
name|wrap
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setNamespaces (Map<String, String> nsmap)
specifier|public
name|void
name|setNamespaces
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsmap
parameter_list|)
block|{
name|this
operator|.
name|nsmap
operator|=
name|nsmap
expr_stmt|;
block|}
DECL|method|setWrap (boolean wrap)
specifier|public
name|void
name|setWrap
parameter_list|(
name|boolean
name|wrap
parameter_list|)
block|{
name|this
operator|.
name|wrap
operator|=
name|wrap
expr_stmt|;
block|}
DECL|method|createIterator (InputStream in, Exchange exchange)
specifier|protected
name|Iterator
argument_list|<
name|?
argument_list|>
name|createIterator
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|XMLTokenIterator
name|iterator
init|=
operator|new
name|XMLTokenIterator
argument_list|(
name|path
argument_list|,
name|nsmap
argument_list|,
name|wrap
argument_list|,
name|in
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
return|return
name|iterator
return|;
block|}
annotation|@
name|Override
DECL|method|matches (Exchange exchange)
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// as a predicate we must close the stream, as we do not return an iterator that can be used
comment|// afterwards to iterate the input stream
name|Object
name|value
init|=
name|doEvaluate
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
decl_stmt|;
return|return
name|ObjectHelper
operator|.
name|evaluateValuePredicate
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|evaluate (Exchange exchange)
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// as we return an iterator to access the input stream, we should not close it
return|return
name|doEvaluate
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Strategy to evaluate the exchange      *      * @param exchange   the exchange      * @param closeStream whether to close the stream before returning from this method.      * @return the evaluated value      */
DECL|method|doEvaluate (Exchange exchange, boolean closeStream)
specifier|protected
name|Object
name|doEvaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|closeStream
parameter_list|)
block|{
name|InputStream
name|in
init|=
literal|null
decl_stmt|;
try|try
block|{
name|in
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|createIterator
argument_list|(
name|in
argument_list|,
name|exchange
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InvalidPayloadException
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|// must close input stream
name|IOHelper
operator|.
name|close
argument_list|(
name|in
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|// must close input stream
name|IOHelper
operator|.
name|close
argument_list|(
name|in
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|closeStream
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|XMLTokenIterator
specifier|static
class|class
name|XMLTokenIterator
implements|implements
name|Iterator
argument_list|<
name|Object
argument_list|>
implements|,
name|Closeable
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|XMLTokenIterator
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|NAMESPACE_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|NAMESPACE_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"xmlns(:\\w+|)\\s*=\\s*('[^']+'|\"[^\"]+\")"
argument_list|)
decl_stmt|;
DECL|field|splitpath
specifier|private
name|AttributedQName
index|[]
name|splitpath
decl_stmt|;
DECL|field|index
specifier|private
name|int
name|index
decl_stmt|;
DECL|field|wrap
specifier|private
name|boolean
name|wrap
decl_stmt|;
DECL|field|in
specifier|private
name|RecordableInputStream
name|in
decl_stmt|;
DECL|field|reader
specifier|private
name|XMLStreamReader
name|reader
decl_stmt|;
DECL|field|path
specifier|private
name|List
argument_list|<
name|QName
argument_list|>
name|path
decl_stmt|;
DECL|field|namespaces
specifier|private
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|namespaces
decl_stmt|;
DECL|field|segments
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|segments
decl_stmt|;
DECL|field|segmentlog
specifier|private
name|List
argument_list|<
name|QName
argument_list|>
name|segmentlog
decl_stmt|;
DECL|field|code
specifier|private
name|int
name|code
decl_stmt|;
DECL|field|consumed
specifier|private
name|int
name|consumed
decl_stmt|;
DECL|field|backtrack
specifier|private
name|boolean
name|backtrack
decl_stmt|;
DECL|field|trackdepth
specifier|private
name|int
name|trackdepth
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|depth
specifier|private
name|int
name|depth
decl_stmt|;
DECL|field|nextToken
specifier|private
name|Object
name|nextToken
decl_stmt|;
DECL|method|XMLTokenIterator (String path, Map<String, String> nsmap, boolean wrap, InputStream in, Exchange exchange)
specifier|public
name|XMLTokenIterator
parameter_list|(
name|String
name|path
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsmap
parameter_list|,
name|boolean
name|wrap
parameter_list|,
name|InputStream
name|in
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|XMLStreamException
block|{
specifier|final
name|String
index|[]
name|sl
init|=
name|path
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|this
operator|.
name|splitpath
operator|=
operator|new
name|AttributedQName
index|[
name|sl
operator|.
name|length
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|sl
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|s
init|=
name|sl
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|int
name|d
init|=
name|s
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|String
name|pfx
init|=
name|d
operator|>
literal|0
condition|?
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|d
argument_list|)
else|:
literal|""
decl_stmt|;
name|this
operator|.
name|splitpath
index|[
name|i
index|]
operator|=
operator|new
name|AttributedQName
argument_list|(
literal|"*"
operator|.
name|equals
argument_list|(
name|pfx
argument_list|)
condition|?
literal|"*"
else|:
name|nsmap
operator|.
name|get
argument_list|(
name|pfx
argument_list|)
argument_list|,
name|d
operator|>
literal|0
condition|?
name|s
operator|.
name|substring
argument_list|(
name|d
operator|+
literal|1
argument_list|)
else|:
name|s
argument_list|,
name|pfx
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|wrap
operator|=
name|wrap
expr_stmt|;
name|String
name|charset
init|=
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|this
operator|.
name|in
operator|=
operator|new
name|RecordableInputStream
argument_list|(
name|in
argument_list|,
name|charset
argument_list|)
expr_stmt|;
name|this
operator|.
name|reader
operator|=
operator|new
name|StaxConverter
argument_list|()
operator|.
name|createXMLStreamReader
argument_list|(
name|this
operator|.
name|in
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"reader.class: {}"
argument_list|,
name|reader
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|coff
init|=
name|reader
operator|.
name|getLocation
argument_list|()
operator|.
name|getCharacterOffset
argument_list|()
decl_stmt|;
if|if
condition|(
name|coff
operator|!=
literal|0
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"XMLStreamReader {} not supporting Location"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|XMLStreamException
argument_list|(
literal|"reader not supporting Location"
argument_list|)
throw|;
block|}
name|this
operator|.
name|path
operator|=
operator|new
name|ArrayList
argument_list|<
name|QName
argument_list|>
argument_list|()
expr_stmt|;
comment|// wrapped mode needs the segments and the injected mode needs the namespaces
if|if
condition|(
name|wrap
condition|)
block|{
name|this
operator|.
name|segments
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|segmentlog
operator|=
operator|new
name|ArrayList
argument_list|<
name|QName
argument_list|>
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|namespaces
operator|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|nextToken
operator|=
name|getNextToken
argument_list|()
expr_stmt|;
block|}
DECL|method|isDoS ()
specifier|private
name|boolean
name|isDoS
parameter_list|()
block|{
return|return
name|splitpath
index|[
name|index
index|]
operator|==
literal|null
return|;
block|}
DECL|method|current ()
specifier|private
name|AttributedQName
name|current
parameter_list|()
block|{
return|return
name|splitpath
index|[
name|index
operator|+
operator|(
name|isDoS
argument_list|()
condition|?
literal|1
else|:
literal|0
operator|)
index|]
return|;
block|}
DECL|method|ancestor ()
specifier|private
name|AttributedQName
name|ancestor
parameter_list|()
block|{
return|return
name|index
operator|==
literal|0
condition|?
literal|null
else|:
name|splitpath
index|[
name|index
operator|-
literal|1
index|]
return|;
block|}
DECL|method|down ()
specifier|private
name|void
name|down
parameter_list|()
block|{
if|if
condition|(
name|isDoS
argument_list|()
condition|)
block|{
name|index
operator|++
expr_stmt|;
block|}
name|index
operator|++
expr_stmt|;
block|}
DECL|method|up ()
specifier|private
name|void
name|up
parameter_list|()
block|{
name|index
operator|--
expr_stmt|;
block|}
DECL|method|isBottom ()
specifier|private
name|boolean
name|isBottom
parameter_list|()
block|{
return|return
name|index
operator|==
name|splitpath
operator|.
name|length
operator|-
operator|(
name|isDoS
argument_list|()
condition|?
literal|2
else|:
literal|1
operator|)
return|;
block|}
DECL|method|isTop ()
specifier|private
name|boolean
name|isTop
parameter_list|()
block|{
return|return
name|index
operator|==
literal|0
return|;
block|}
DECL|method|readNext ()
specifier|private
name|int
name|readNext
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|int
name|c
init|=
name|code
decl_stmt|;
if|if
condition|(
name|c
operator|>
literal|0
condition|)
block|{
name|code
operator|=
literal|0
expr_stmt|;
block|}
else|else
block|{
name|c
operator|=
name|reader
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
DECL|method|getCurrenText ()
specifier|private
name|String
name|getCurrenText
parameter_list|()
block|{
name|int
name|pos
init|=
name|reader
operator|.
name|getLocation
argument_list|()
operator|.
name|getCharacterOffset
argument_list|()
decl_stmt|;
name|String
name|txt
init|=
name|in
operator|.
name|getText
argument_list|(
name|pos
operator|-
name|consumed
argument_list|)
decl_stmt|;
name|consumed
operator|=
name|pos
expr_stmt|;
comment|// keep recording
name|in
operator|.
name|record
argument_list|()
expr_stmt|;
return|return
name|txt
return|;
block|}
DECL|method|pushName (QName name)
specifier|private
name|void
name|pushName
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|path
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|popName ()
specifier|private
name|QName
name|popName
parameter_list|()
block|{
return|return
name|path
operator|.
name|remove
argument_list|(
name|path
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
DECL|method|pushSegment (QName qname, String token)
specifier|private
name|void
name|pushSegment
parameter_list|(
name|QName
name|qname
parameter_list|,
name|String
name|token
parameter_list|)
block|{
name|segments
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|segmentlog
operator|.
name|add
argument_list|(
name|qname
argument_list|)
expr_stmt|;
block|}
DECL|method|popSegment ()
specifier|private
name|String
name|popSegment
parameter_list|()
block|{
return|return
name|segments
operator|.
name|remove
argument_list|(
name|segments
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
DECL|method|peekLog ()
specifier|private
name|QName
name|peekLog
parameter_list|()
block|{
return|return
name|segmentlog
operator|.
name|get
argument_list|(
name|segmentlog
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
DECL|method|popLog ()
specifier|private
name|QName
name|popLog
parameter_list|()
block|{
return|return
name|segmentlog
operator|.
name|remove
argument_list|(
name|segmentlog
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
DECL|method|pushNamespaces (XMLStreamReader reader)
specifier|private
name|void
name|pushNamespaces
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|m
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|namespaces
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|m
operator|.
name|putAll
argument_list|(
name|namespaces
operator|.
name|get
argument_list|(
name|namespaces
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|reader
operator|.
name|getNamespaceCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|m
operator|.
name|put
argument_list|(
name|reader
operator|.
name|getNamespacePrefix
argument_list|(
name|i
argument_list|)
argument_list|,
name|reader
operator|.
name|getNamespaceURI
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|namespaces
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
DECL|method|popNamespaces ()
specifier|private
name|void
name|popNamespaces
parameter_list|()
block|{
name|namespaces
operator|.
name|remove
argument_list|(
name|namespaces
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|getCurrentNamespaceBindings ()
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getCurrentNamespaceBindings
parameter_list|()
block|{
return|return
name|namespaces
operator|.
name|get
argument_list|(
name|namespaces
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
DECL|method|readCurrent (boolean incl)
specifier|private
name|void
name|readCurrent
parameter_list|(
name|boolean
name|incl
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|int
name|d
init|=
name|depth
decl_stmt|;
while|while
condition|(
name|d
operator|<=
name|depth
condition|)
block|{
name|int
name|code
init|=
name|reader
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|code
operator|==
name|XMLStreamReader
operator|.
name|START_ELEMENT
condition|)
block|{
name|depth
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|code
operator|==
name|XMLStreamReader
operator|.
name|END_ELEMENT
condition|)
block|{
name|depth
operator|--
expr_stmt|;
block|}
block|}
comment|// either look ahead to the next token or stay at the end element token
if|if
condition|(
name|incl
condition|)
block|{
name|code
operator|=
name|reader
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|code
operator|=
name|reader
operator|.
name|getEventType
argument_list|()
expr_stmt|;
if|if
condition|(
name|code
operator|==
name|XMLStreamReader
operator|.
name|END_ELEMENT
condition|)
block|{
comment|// revert the depth count to avoid double counting the up event
name|depth
operator|++
expr_stmt|;
block|}
block|}
block|}
DECL|method|getCurrentToken ()
specifier|private
name|String
name|getCurrentToken
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|readCurrent
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|popName
argument_list|()
expr_stmt|;
name|String
name|token
init|=
name|createContextualToken
argument_list|(
name|getCurrenText
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|wrap
condition|)
block|{
name|popNamespaces
argument_list|()
expr_stmt|;
block|}
return|return
name|token
return|;
block|}
DECL|method|createContextualToken (String token)
specifier|private
name|String
name|createContextualToken
parameter_list|(
name|String
name|token
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|wrap
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|segments
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|segments
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|token
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|path
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|QName
name|q
init|=
name|path
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"</"
argument_list|)
operator|.
name|append
argument_list|(
name|makeName
argument_list|(
name|q
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
specifier|final
name|String
name|stag
init|=
name|token
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|token
operator|.
name|indexOf
argument_list|(
literal|'>'
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|skip
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Matcher
name|matcher
init|=
name|NAMESPACE_PATTERN
operator|.
name|matcher
argument_list|(
name|stag
argument_list|)
decl_stmt|;
name|char
name|quote
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|prefix
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefix
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|prefix
operator|=
name|prefix
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|skip
operator|.
name|add
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
if|if
condition|(
name|quote
operator|==
literal|0
condition|)
block|{
name|quote
operator|=
name|matcher
operator|.
name|group
argument_list|(
literal|2
argument_list|)
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|quote
operator|==
literal|0
condition|)
block|{
name|quote
operator|=
literal|'"'
expr_stmt|;
block|}
name|boolean
name|empty
init|=
name|stag
operator|.
name|endsWith
argument_list|(
literal|"/>"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|token
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|stag
operator|.
name|length
argument_list|()
operator|-
operator|(
name|empty
condition|?
literal|2
else|:
literal|1
operator|)
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|e
range|:
name|getCurrentNamespaceBindings
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|skip
operator|.
name|contains
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|?
literal|" xmlns"
else|:
literal|" xmlns:"
argument_list|)
operator|.
name|append
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|quote
argument_list|)
operator|.
name|append
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|quote
argument_list|)
expr_stmt|;
block|}
block|}
name|sb
operator|.
name|append
argument_list|(
name|token
operator|.
name|substring
argument_list|(
name|stag
operator|.
name|length
argument_list|()
operator|-
operator|(
name|empty
condition|?
literal|2
else|:
literal|1
operator|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getNextToken ()
specifier|private
name|String
name|getNextToken
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|int
name|code
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|code
operator|!=
name|XMLStreamReader
operator|.
name|END_DOCUMENT
condition|)
block|{
name|code
operator|=
name|readNext
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|code
condition|)
block|{
case|case
name|XMLStreamReader
operator|.
name|START_ELEMENT
case|:
name|depth
operator|++
expr_stmt|;
name|QName
name|name
init|=
name|reader
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"se={}; depth={}; trackdepth={}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|name
block|,
name|depth
block|,
name|trackdepth
block|}
argument_list|)
expr_stmt|;
block|}
name|String
name|token
init|=
name|getCurrenText
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"token={}"
argument_list|,
name|token
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|backtrack
operator|&&
name|wrap
condition|)
block|{
name|pushSegment
argument_list|(
name|name
argument_list|,
name|token
argument_list|)
expr_stmt|;
block|}
name|pushName
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|wrap
condition|)
block|{
name|pushNamespaces
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
name|backtrack
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|current
argument_list|()
operator|.
name|matches
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// mark the position of the match in the segments list
if|if
condition|(
name|isBottom
argument_list|()
condition|)
block|{
comment|// final match
name|token
operator|=
name|getCurrentToken
argument_list|()
expr_stmt|;
name|backtrack
operator|=
literal|true
expr_stmt|;
name|trackdepth
operator|=
name|depth
expr_stmt|;
return|return
name|token
return|;
block|}
else|else
block|{
comment|// intermediary match
name|down
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|isDoS
argument_list|()
condition|)
block|{
comment|// continue
block|}
else|else
block|{
comment|// skip
name|readCurrent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|XMLStreamReader
operator|.
name|END_ELEMENT
case|:
name|depth
operator|--
expr_stmt|;
name|QName
name|endname
init|=
name|reader
operator|.
name|getName
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"ee={}"
argument_list|,
name|endname
argument_list|)
expr_stmt|;
name|popName
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|wrap
condition|)
block|{
name|popNamespaces
argument_list|()
expr_stmt|;
block|}
name|int
name|pc
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|backtrack
operator|||
operator|(
name|trackdepth
operator|>
literal|0
operator|&&
name|depth
operator|==
name|trackdepth
operator|-
literal|1
operator|)
condition|)
block|{
comment|// reactive backtrack if not backtracking and update the track depth
name|backtrack
operator|=
literal|true
expr_stmt|;
name|trackdepth
operator|--
expr_stmt|;
if|if
condition|(
name|wrap
condition|)
block|{
while|while
condition|(
operator|!
name|endname
operator|.
name|equals
argument_list|(
name|peekLog
argument_list|()
argument_list|)
condition|)
block|{
name|pc
operator|++
expr_stmt|;
name|popLog
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|backtrack
condition|)
block|{
if|if
condition|(
name|wrap
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|pc
condition|;
name|i
operator|++
control|)
block|{
name|popSegment
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|(
name|ancestor
argument_list|()
operator|==
literal|null
operator|&&
operator|!
name|isTop
argument_list|()
operator|)
operator|||
operator|(
name|ancestor
argument_list|()
operator|!=
literal|null
operator|&&
name|ancestor
argument_list|()
operator|.
name|matches
argument_list|(
name|endname
argument_list|)
operator|)
condition|)
block|{
name|up
argument_list|()
expr_stmt|;
block|}
block|}
break|break;
case|case
name|XMLStreamReader
operator|.
name|END_DOCUMENT
case|:
name|LOG
operator|.
name|trace
argument_list|(
literal|"depth={}"
argument_list|,
name|depth
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|makeName (QName qname)
specifier|private
specifier|static
name|String
name|makeName
parameter_list|(
name|QName
name|qname
parameter_list|)
block|{
name|String
name|pfx
init|=
name|qname
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
return|return
name|pfx
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|?
name|qname
operator|.
name|getLocalPart
argument_list|()
else|:
name|qname
operator|.
name|getPrefix
argument_list|()
operator|+
literal|":"
operator|+
name|qname
operator|.
name|getLocalPart
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|nextToken
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|Object
name|next
parameter_list|()
block|{
name|Object
name|o
init|=
name|nextToken
decl_stmt|;
try|try
block|{
name|nextToken
operator|=
name|getNextToken
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
comment|//
block|}
return|return
name|o
return|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
comment|// noop
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
try|try
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|class|AttributedQName
specifier|static
class|class
name|AttributedQName
extends|extends
name|QName
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|9878370226894144L
decl_stmt|;
DECL|field|lcpattern
specifier|private
name|Pattern
name|lcpattern
decl_stmt|;
DECL|field|nsany
specifier|private
name|boolean
name|nsany
decl_stmt|;
DECL|method|AttributedQName (String localPart)
specifier|public
name|AttributedQName
parameter_list|(
name|String
name|localPart
parameter_list|)
block|{
name|super
argument_list|(
name|localPart
argument_list|)
expr_stmt|;
name|checkWildcard
argument_list|(
literal|""
argument_list|,
name|localPart
argument_list|)
expr_stmt|;
block|}
DECL|method|AttributedQName (String namespaceURI, String localPart, String prefix)
specifier|public
name|AttributedQName
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|localPart
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|super
argument_list|(
name|namespaceURI
argument_list|,
name|localPart
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
name|checkWildcard
argument_list|(
name|namespaceURI
argument_list|,
name|localPart
argument_list|)
expr_stmt|;
block|}
DECL|method|AttributedQName (String namespaceURI, String localPart)
specifier|public
name|AttributedQName
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|localPart
parameter_list|)
block|{
name|super
argument_list|(
name|namespaceURI
argument_list|,
name|localPart
argument_list|)
expr_stmt|;
name|checkWildcard
argument_list|(
name|namespaceURI
argument_list|,
name|localPart
argument_list|)
expr_stmt|;
block|}
DECL|method|matches (QName qname)
specifier|public
name|boolean
name|matches
parameter_list|(
name|QName
name|qname
parameter_list|)
block|{
return|return
operator|(
name|nsany
operator|||
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|)
operator|&&
operator|(
name|lcpattern
operator|!=
literal|null
condition|?
name|lcpattern
operator|.
name|matcher
argument_list|(
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|()
else|:
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|)
return|;
block|}
DECL|method|checkWildcard (String nsa, String lcp)
specifier|private
name|void
name|checkWildcard
parameter_list|(
name|String
name|nsa
parameter_list|,
name|String
name|lcp
parameter_list|)
block|{
name|nsany
operator|=
literal|"*"
operator|.
name|equals
argument_list|(
name|nsa
argument_list|)
expr_stmt|;
name|boolean
name|wc
init|=
literal|false
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
name|lcp
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|lcp
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|'?'
operator|||
name|c
operator|==
literal|'*'
condition|)
block|{
name|wc
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|wc
condition|)
block|{
name|lcpattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|lcp
operator|.
name|replace
argument_list|(
literal|"."
argument_list|,
literal|"\\."
argument_list|)
operator|.
name|replace
argument_list|(
literal|"*"
argument_list|,
literal|".*"
argument_list|)
operator|.
name|replace
argument_list|(
literal|"?"
argument_list|,
literal|"."
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

