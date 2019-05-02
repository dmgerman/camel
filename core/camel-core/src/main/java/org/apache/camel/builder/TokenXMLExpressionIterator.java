begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|text
operator|.
name|MessageFormat
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
name|Scanner
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
name|MatchResult
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
name|support
operator|.
name|ExpressionAdapter
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
name|LanguageSupport
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|StringHelper
import|;
end_import

begin_comment
comment|/**  * {@link org.apache.camel.Expression} to walk a {@link org.apache.camel.Message} XML body  * using an {@link java.util.Iterator}, which grabs the content between a XML start and end token,  * where the end token corresponds implicitly to either the end tag or the self-closing start tag.  *<p/>  * The message body must be able to convert to {@link java.io.InputStream} type which is used as stream  * to access the message body.  *<p/>  * Can be used to split big XML files.  *<p/>  * This implementation supports inheriting namespaces from a parent/root tag.  */
end_comment

begin_class
DECL|class|TokenXMLExpressionIterator
specifier|public
class|class
name|TokenXMLExpressionIterator
extends|extends
name|ExpressionAdapter
block|{
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
DECL|field|SCAN_TOKEN_NS_PREFIX_REGEX
specifier|private
specifier|static
specifier|final
name|String
name|SCAN_TOKEN_NS_PREFIX_REGEX
init|=
literal|"([^:<>]{1,15}?:|)"
decl_stmt|;
DECL|field|SCAN_BLOCK_TOKEN_REGEX_TEMPLATE
specifier|private
specifier|static
specifier|final
name|String
name|SCAN_BLOCK_TOKEN_REGEX_TEMPLATE
init|=
literal|"<{0}(\\s+[^>]*)?/>|<{0}(\\s+[^>]*)?>(?:(?!(</{0}\\s*>)).)*</{0}\\s*>"
decl_stmt|;
DECL|field|SCAN_PARENT_TOKEN_REGEX_TEMPLATE
specifier|private
specifier|static
specifier|final
name|String
name|SCAN_PARENT_TOKEN_REGEX_TEMPLATE
init|=
literal|"<{0}(\\s+[^>]*\\s*)?>"
decl_stmt|;
DECL|field|OPTION_WRAP_TOKEN
specifier|private
specifier|static
specifier|final
name|String
name|OPTION_WRAP_TOKEN
init|=
literal|"<*>"
decl_stmt|;
DECL|field|tagToken
specifier|protected
specifier|final
name|String
name|tagToken
decl_stmt|;
DECL|field|inheritNamespaceToken
specifier|protected
specifier|final
name|String
name|inheritNamespaceToken
decl_stmt|;
DECL|method|TokenXMLExpressionIterator (String tagToken, String inheritNamespaceToken)
specifier|public
name|TokenXMLExpressionIterator
parameter_list|(
name|String
name|tagToken
parameter_list|,
name|String
name|inheritNamespaceToken
parameter_list|)
block|{
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|tagToken
argument_list|,
literal|"tagToken"
argument_list|)
expr_stmt|;
name|this
operator|.
name|tagToken
operator|=
name|tagToken
expr_stmt|;
comment|// namespace token is optional
name|this
operator|.
name|inheritNamespaceToken
operator|=
name|inheritNamespaceToken
expr_stmt|;
block|}
DECL|method|createIterator (Exchange exchange, InputStream in, String charset)
specifier|protected
name|Iterator
argument_list|<
name|?
argument_list|>
name|createIterator
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|in
parameter_list|,
name|String
name|charset
parameter_list|)
block|{
name|String
name|tag
init|=
name|tagToken
decl_stmt|;
if|if
condition|(
name|LanguageSupport
operator|.
name|hasSimpleFunction
argument_list|(
name|tag
argument_list|)
condition|)
block|{
name|tag
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
literal|"simple"
argument_list|)
operator|.
name|createExpression
argument_list|(
name|tag
argument_list|)
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|String
name|inherit
init|=
name|inheritNamespaceToken
decl_stmt|;
if|if
condition|(
name|inherit
operator|!=
literal|null
operator|&&
name|LanguageSupport
operator|.
name|hasSimpleFunction
argument_list|(
name|tag
argument_list|)
condition|)
block|{
name|inherit
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
literal|"simple"
argument_list|)
operator|.
name|createExpression
argument_list|(
name|inherit
argument_list|)
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|// must be XML tokens
if|if
condition|(
operator|!
name|tag
operator|.
name|startsWith
argument_list|(
literal|"<"
argument_list|)
condition|)
block|{
name|tag
operator|=
literal|"<"
operator|+
name|tag
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|tag
operator|.
name|endsWith
argument_list|(
literal|">"
argument_list|)
condition|)
block|{
name|tag
operator|=
name|tag
operator|+
literal|">"
expr_stmt|;
block|}
if|if
condition|(
name|inherit
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|inherit
operator|.
name|startsWith
argument_list|(
literal|"<"
argument_list|)
condition|)
block|{
name|inherit
operator|=
literal|"<"
operator|+
name|inherit
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|inherit
operator|.
name|endsWith
argument_list|(
literal|">"
argument_list|)
condition|)
block|{
name|inherit
operator|=
name|inherit
operator|+
literal|">"
expr_stmt|;
block|}
block|}
comment|// must be XML tokens
if|if
condition|(
operator|!
name|tag
operator|.
name|startsWith
argument_list|(
literal|"<"
argument_list|)
operator|||
operator|!
name|tag
operator|.
name|endsWith
argument_list|(
literal|">"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"XML Tag token must be a valid XML tag, was: "
operator|+
name|tag
argument_list|)
throw|;
block|}
if|if
condition|(
name|inherit
operator|!=
literal|null
operator|&&
operator|(
operator|!
name|inherit
operator|.
name|startsWith
argument_list|(
literal|"<"
argument_list|)
operator|||
operator|!
name|inherit
operator|.
name|endsWith
argument_list|(
literal|">"
argument_list|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Namespace token must be a valid XML token, was: "
operator|+
name|inherit
argument_list|)
throw|;
block|}
name|XMLTokenIterator
name|iterator
init|=
operator|new
name|XMLTokenIterator
argument_list|(
name|tag
argument_list|,
name|inherit
argument_list|,
name|in
argument_list|,
name|charset
argument_list|)
decl_stmt|;
name|iterator
operator|.
name|init
argument_list|()
expr_stmt|;
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
comment|// we may read from a file, and want to support custom charset defined on the exchange
name|String
name|charset
init|=
name|ExchangeHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
name|createIterator
argument_list|(
name|exchange
argument_list|,
name|in
argument_list|,
name|charset
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
comment|/**      * Iterator to walk the input stream      */
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
DECL|field|tagToken
specifier|final
name|String
name|tagToken
decl_stmt|;
DECL|field|in
specifier|final
name|InputStream
name|in
decl_stmt|;
DECL|field|charset
specifier|final
name|String
name|charset
decl_stmt|;
DECL|field|scanner
name|Scanner
name|scanner
decl_stmt|;
DECL|field|image
name|Object
name|image
decl_stmt|;
DECL|field|tagTokenPattern
specifier|private
specifier|final
name|Pattern
name|tagTokenPattern
decl_stmt|;
DECL|field|inheritNamespaceToken
specifier|private
specifier|final
name|String
name|inheritNamespaceToken
decl_stmt|;
DECL|field|wrapToken
specifier|private
specifier|final
name|boolean
name|wrapToken
decl_stmt|;
DECL|field|inheritNamespaceTokenPattern
specifier|private
name|Pattern
name|inheritNamespaceTokenPattern
decl_stmt|;
DECL|field|rootTokenNamespaces
specifier|private
name|String
name|rootTokenNamespaces
decl_stmt|;
DECL|field|wrapHead
specifier|private
name|String
name|wrapHead
decl_stmt|;
DECL|field|wrapTail
specifier|private
name|String
name|wrapTail
decl_stmt|;
DECL|method|XMLTokenIterator (String tagToken, String inheritNamespaceToken, InputStream in, String charset)
name|XMLTokenIterator
parameter_list|(
name|String
name|tagToken
parameter_list|,
name|String
name|inheritNamespaceToken
parameter_list|,
name|InputStream
name|in
parameter_list|,
name|String
name|charset
parameter_list|)
block|{
name|this
operator|.
name|tagToken
operator|=
name|tagToken
expr_stmt|;
name|this
operator|.
name|charset
operator|=
name|charset
expr_stmt|;
comment|// remove any beginning< and ending> as we need to support ns prefixes and attributes, so we use a reg exp patterns
name|this
operator|.
name|tagTokenPattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|MessageFormat
operator|.
name|format
argument_list|(
name|SCAN_BLOCK_TOKEN_REGEX_TEMPLATE
argument_list|,
name|SCAN_TOKEN_NS_PREFIX_REGEX
operator|+
name|tagToken
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|tagToken
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
argument_list|,
name|Pattern
operator|.
name|MULTILINE
operator||
name|Pattern
operator|.
name|DOTALL
argument_list|)
expr_stmt|;
name|this
operator|.
name|inheritNamespaceToken
operator|=
name|inheritNamespaceToken
expr_stmt|;
if|if
condition|(
name|inheritNamespaceToken
operator|!=
literal|null
operator|&&
name|OPTION_WRAP_TOKEN
operator|.
name|equals
argument_list|(
name|inheritNamespaceToken
argument_list|)
condition|)
block|{
name|this
operator|.
name|wrapToken
operator|=
literal|true
expr_stmt|;
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
block|}
else|else
block|{
name|this
operator|.
name|wrapToken
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|in
operator|=
name|in
expr_stmt|;
if|if
condition|(
name|inheritNamespaceToken
operator|!=
literal|null
condition|)
block|{
comment|// the inherit namespace token may itself have a namespace prefix
comment|// the namespaces on the parent tag can be in multi line, so we need to instruct the dot to support multilines
name|this
operator|.
name|inheritNamespaceTokenPattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|MessageFormat
operator|.
name|format
argument_list|(
name|SCAN_PARENT_TOKEN_REGEX_TEMPLATE
argument_list|,
name|SCAN_TOKEN_NS_PREFIX_REGEX
operator|+
name|inheritNamespaceToken
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|inheritNamespaceToken
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
argument_list|,
name|Pattern
operator|.
name|MULTILINE
operator||
name|Pattern
operator|.
name|DOTALL
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|init ()
name|void
name|init
parameter_list|()
block|{
comment|// use a scanner with the default delimiter
name|this
operator|.
name|scanner
operator|=
operator|new
name|Scanner
argument_list|(
name|in
argument_list|,
name|charset
argument_list|)
expr_stmt|;
name|this
operator|.
name|image
operator|=
name|scanner
operator|.
name|hasNext
argument_list|()
condition|?
operator|(
name|String
operator|)
name|next
argument_list|(
literal|true
argument_list|)
else|:
literal|null
expr_stmt|;
block|}
DECL|method|getNext (boolean first)
name|String
name|getNext
parameter_list|(
name|boolean
name|first
parameter_list|)
block|{
comment|// initialize inherited namespaces on first
if|if
condition|(
name|first
operator|&&
name|inheritNamespaceToken
operator|!=
literal|null
operator|&&
operator|!
name|wrapToken
condition|)
block|{
name|rootTokenNamespaces
operator|=
name|getNamespacesFromNamespaceToken
argument_list|(
name|scanner
operator|.
name|findWithinHorizon
argument_list|(
name|inheritNamespaceTokenPattern
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|next
init|=
name|scanner
operator|.
name|findWithinHorizon
argument_list|(
name|tagTokenPattern
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|first
operator|&&
name|wrapToken
condition|)
block|{
name|MatchResult
name|mres
init|=
name|scanner
operator|.
name|match
argument_list|()
decl_stmt|;
name|wrapHead
operator|=
operator|(
operator|(
name|RecordableInputStream
operator|)
name|in
operator|)
operator|.
name|getText
argument_list|(
name|mres
operator|.
name|start
argument_list|()
argument_list|)
expr_stmt|;
name|wrapTail
operator|=
name|buildXMLTail
argument_list|(
name|wrapHead
argument_list|)
expr_stmt|;
block|}
comment|// build answer accordingly to whether namespaces should be inherited or not
if|if
condition|(
name|inheritNamespaceToken
operator|!=
literal|null
operator|&&
name|rootTokenNamespaces
operator|!=
literal|null
condition|)
block|{
comment|// REVISIT should skip the prefixes that are declared within the child itself.
name|String
name|head
init|=
name|StringHelper
operator|.
name|before
argument_list|(
name|next
argument_list|,
literal|">"
argument_list|)
decl_stmt|;
name|boolean
name|empty
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|head
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|head
operator|=
name|head
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|head
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|empty
operator|=
literal|true
expr_stmt|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// append root namespaces to local start token
comment|// grab the text
name|String
name|tail
init|=
name|StringHelper
operator|.
name|after
argument_list|(
name|next
argument_list|,
literal|">"
argument_list|)
decl_stmt|;
comment|// build result with inherited namespaces
name|next
operator|=
name|sb
operator|.
name|append
argument_list|(
name|head
argument_list|)
operator|.
name|append
argument_list|(
name|rootTokenNamespaces
argument_list|)
operator|.
name|append
argument_list|(
name|empty
condition|?
literal|"/>"
else|:
literal|">"
argument_list|)
operator|.
name|append
argument_list|(
name|tail
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|wrapToken
condition|)
block|{
comment|// wrap the token
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|next
operator|=
name|sb
operator|.
name|append
argument_list|(
name|wrapHead
argument_list|)
operator|.
name|append
argument_list|(
name|next
argument_list|)
operator|.
name|append
argument_list|(
name|wrapTail
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|next
return|;
block|}
DECL|method|getNamespacesFromNamespaceToken (String text)
specifier|private
name|String
name|getNamespacesFromNamespaceToken
parameter_list|(
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|text
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// find namespaces (there can be attributes mixed, so we should only grab the namespaces)
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Matcher
name|matcher
init|=
name|NAMESPACE_PATTERN
operator|.
name|matcher
argument_list|(
name|text
argument_list|)
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
name|String
name|url
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|prefix
operator|=
literal|"_DEFAULT_"
expr_stmt|;
block|}
else|else
block|{
comment|// skip leading :
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
name|namespaces
operator|.
name|put
argument_list|(
name|prefix
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
comment|// did we find any namespaces
if|if
condition|(
name|namespaces
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// build namespace String
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|namespaces
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
comment|// note the value is already quoted
name|String
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"_DEFAULT_"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" xmlns="
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" xmlns:"
argument_list|)
operator|.
name|append
argument_list|(
name|key
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
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
name|image
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
return|return
name|next
argument_list|(
literal|false
argument_list|)
return|;
block|}
DECL|method|next (boolean first)
name|Object
name|next
parameter_list|(
name|boolean
name|first
parameter_list|)
block|{
name|Object
name|answer
init|=
name|image
decl_stmt|;
comment|// calculate next
if|if
condition|(
name|scanner
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|image
operator|=
name|getNext
argument_list|(
name|first
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|image
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// first time the image may be null
name|answer
operator|=
name|image
expr_stmt|;
block|}
return|return
name|answer
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
name|scanner
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|buildXMLTail (String xmlhead)
specifier|private
specifier|static
name|String
name|buildXMLTail
parameter_list|(
name|String
name|xmlhead
parameter_list|)
block|{
comment|// assume the input text is a portion of a well-formed xml
name|List
argument_list|<
name|String
argument_list|>
name|tags
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|int
name|p
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|p
operator|<
name|xmlhead
operator|.
name|length
argument_list|()
condition|)
block|{
name|p
operator|=
name|xmlhead
operator|.
name|indexOf
argument_list|(
literal|'<'
argument_list|,
name|p
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|<
literal|0
condition|)
block|{
break|break;
block|}
name|int
name|nc
init|=
name|xmlhead
operator|.
name|charAt
argument_list|(
name|p
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|nc
operator|==
literal|'?'
condition|)
block|{
name|p
operator|++
expr_stmt|;
continue|continue;
block|}
elseif|else
if|if
condition|(
name|nc
operator|==
literal|'/'
condition|)
block|{
name|p
operator|++
expr_stmt|;
name|tags
operator|.
name|remove
argument_list|(
name|tags
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|int
name|ep
init|=
name|xmlhead
operator|.
name|indexOf
argument_list|(
literal|'>'
argument_list|,
name|p
argument_list|)
decl_stmt|;
if|if
condition|(
name|xmlhead
operator|.
name|charAt
argument_list|(
name|ep
operator|-
literal|1
argument_list|)
operator|==
literal|'/'
condition|)
block|{
name|p
operator|++
expr_stmt|;
continue|continue;
block|}
specifier|final
name|int
name|sp
init|=
name|xmlhead
operator|.
name|substring
argument_list|(
name|p
argument_list|,
name|ep
argument_list|)
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
name|tags
operator|.
name|add
argument_list|(
name|xmlhead
operator|.
name|substring
argument_list|(
name|p
operator|+
literal|1
argument_list|,
name|sp
operator|>
literal|0
condition|?
name|p
operator|+
name|sp
else|:
name|ep
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|=
name|ep
expr_stmt|;
block|}
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|tags
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
name|sb
operator|.
name|append
argument_list|(
literal|"</"
argument_list|)
operator|.
name|append
argument_list|(
name|tags
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|">"
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
block|}
end_class

end_unit

