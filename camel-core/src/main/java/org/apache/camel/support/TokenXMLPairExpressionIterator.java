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
name|InputStream
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * {@link org.apache.camel.Expression} to walk a {@link org.apache.camel.Message} XML body  * using an {@link java.util.Iterator}, which grabs the content between a XML start and end token.  *<p/>  * The message body must be able to convert to {@link java.io.InputStream} type which is used as stream  * to access the message body.  *<p/>  * Can be used to split big XML files.  *<p/>  * This implementation supports inheriting namespaces from a parent/root tag.  */
end_comment

begin_class
DECL|class|TokenXMLPairExpressionIterator
specifier|public
class|class
name|TokenXMLPairExpressionIterator
extends|extends
name|TokenPairExpressionIterator
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
literal|"xmlns(:\\w+|)=\\\"(.*?)\\\""
argument_list|)
decl_stmt|;
DECL|field|SCAN_TOKEN_REGEX
specifier|private
specifier|static
specifier|final
name|String
name|SCAN_TOKEN_REGEX
init|=
literal|"(\\s+.*?|)>"
decl_stmt|;
DECL|field|inheritNamespaceToken
specifier|protected
specifier|final
name|String
name|inheritNamespaceToken
decl_stmt|;
DECL|method|TokenXMLPairExpressionIterator (String startToken, String endToken, String inheritNamespaceToken)
specifier|public
name|TokenXMLPairExpressionIterator
parameter_list|(
name|String
name|startToken
parameter_list|,
name|String
name|endToken
parameter_list|,
name|String
name|inheritNamespaceToken
parameter_list|)
block|{
name|super
argument_list|(
name|startToken
argument_list|,
name|endToken
argument_list|)
expr_stmt|;
comment|// namespace token is optional
name|this
operator|.
name|inheritNamespaceToken
operator|=
name|inheritNamespaceToken
expr_stmt|;
comment|// must be XML tokens
if|if
condition|(
operator|!
name|startToken
operator|.
name|startsWith
argument_list|(
literal|"<"
argument_list|)
operator|||
operator|!
name|startToken
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
literal|"Start token must be a valid XML token, was: "
operator|+
name|startToken
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|endToken
operator|.
name|startsWith
argument_list|(
literal|"<"
argument_list|)
operator|||
operator|!
name|endToken
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
literal|"End token must be a valid XML token, was: "
operator|+
name|endToken
argument_list|)
throw|;
block|}
if|if
condition|(
name|inheritNamespaceToken
operator|!=
literal|null
operator|&&
operator|(
operator|!
name|inheritNamespaceToken
operator|.
name|startsWith
argument_list|(
literal|"<"
argument_list|)
operator|||
operator|!
name|inheritNamespaceToken
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
name|inheritNamespaceToken
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|createIterator (InputStream in, String charset)
specifier|protected
name|Iterator
name|createIterator
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|String
name|charset
parameter_list|)
block|{
name|XMLTokenPairIterator
name|iterator
init|=
operator|new
name|XMLTokenPairIterator
argument_list|(
name|startToken
argument_list|,
name|endToken
argument_list|,
name|inheritNamespaceToken
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
comment|/**      * Iterator to walk the input stream      */
DECL|class|XMLTokenPairIterator
specifier|static
class|class
name|XMLTokenPairIterator
extends|extends
name|TokenPairIterator
block|{
DECL|field|startTokenPattern
specifier|private
specifier|final
name|Pattern
name|startTokenPattern
decl_stmt|;
DECL|field|scanEndToken
specifier|private
specifier|final
name|String
name|scanEndToken
decl_stmt|;
DECL|field|inheritNamespaceToken
specifier|private
specifier|final
name|String
name|inheritNamespaceToken
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
DECL|method|XMLTokenPairIterator (String startToken, String endToken, String inheritNamespaceToken, InputStream in, String charset)
name|XMLTokenPairIterator
parameter_list|(
name|String
name|startToken
parameter_list|,
name|String
name|endToken
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
name|super
argument_list|(
name|startToken
argument_list|,
name|endToken
argument_list|,
name|in
argument_list|,
name|charset
argument_list|)
expr_stmt|;
comment|// remove any ending> as we need to support attributes on the tags, so we need to use a reg exp pattern
name|String
name|token
init|=
name|startToken
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|startToken
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|+
name|SCAN_TOKEN_REGEX
decl_stmt|;
name|this
operator|.
name|startTokenPattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|this
operator|.
name|scanEndToken
operator|=
name|endToken
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|endToken
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|+
name|SCAN_TOKEN_REGEX
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
condition|)
block|{
name|token
operator|=
name|inheritNamespaceToken
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|inheritNamespaceToken
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|+
name|SCAN_TOKEN_REGEX
expr_stmt|;
name|this
operator|.
name|inheritNamespaceTokenPattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|init ()
name|void
name|init
parameter_list|()
block|{
comment|// use scan end token as delimiter which supports attributes/namespaces
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
operator|.
name|useDelimiter
argument_list|(
name|scanEndToken
argument_list|)
expr_stmt|;
comment|// this iterator will do look ahead as we may have data
comment|// after the last end token, which the scanner would find
comment|// so we need to be one step ahead of the scanner
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
annotation|@
name|Override
DECL|method|getNext (boolean first)
name|String
name|getNext
parameter_list|(
name|boolean
name|first
parameter_list|)
block|{
name|String
name|next
init|=
name|scanner
operator|.
name|next
argument_list|()
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
comment|// initialize inherited namespaces on first
if|if
condition|(
name|first
operator|&&
name|inheritNamespaceToken
operator|!=
literal|null
condition|)
block|{
name|rootTokenNamespaces
operator|=
name|getNamespacesFromNamespaceToken
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
comment|// make sure next is positioned at start token as we can have leading data
comment|// or we reached EOL and there is no more start tags
name|Matcher
name|matcher
init|=
name|startTokenPattern
operator|.
name|matcher
argument_list|(
name|next
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
name|int
name|index
init|=
name|matcher
operator|.
name|start
argument_list|()
decl_stmt|;
name|next
operator|=
name|next
operator|.
name|substring
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
comment|// build answer accordingly to whether namespaces should be inherited or not
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
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
comment|// append root namespaces to local start token
name|String
name|tag
init|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|next
argument_list|,
literal|">"
argument_list|)
decl_stmt|;
comment|// grab the text
name|String
name|text
init|=
name|ObjectHelper
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
name|tag
argument_list|)
operator|.
name|append
argument_list|(
name|rootTokenNamespaces
argument_list|)
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
operator|.
name|append
argument_list|(
name|text
argument_list|)
operator|.
name|append
argument_list|(
name|endToken
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|next
operator|=
name|sb
operator|.
name|append
argument_list|(
name|next
argument_list|)
operator|.
name|append
argument_list|(
name|endToken
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
comment|// grab the namespace tag
name|Matcher
name|mat
init|=
name|inheritNamespaceTokenPattern
operator|.
name|matcher
argument_list|(
name|text
argument_list|)
decl_stmt|;
if|if
condition|(
name|mat
operator|.
name|find
argument_list|()
condition|)
block|{
name|text
operator|=
name|mat
operator|.
name|group
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// cannot find namespace tag
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
argument_list|<
name|String
argument_list|,
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
literal|" xmlns=\""
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
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
literal|"=\""
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
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
block|}
block|}
end_class

end_unit

