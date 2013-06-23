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
name|Iterator
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

begin_comment
comment|/**  * {@link org.apache.camel.Expression} to walk a {@link org.apache.camel.Message} body  * using an {@link Iterator}, which grabs the content between a start and end token.  *<p/>  * The message body must be able to convert to {@link InputStream} type which is used as stream  * to access the message body.  *<p/>  * For splitting XML files use {@link org.apache.camel.support.TokenXMLExpressionIterator} instead.  */
end_comment

begin_class
DECL|class|TokenPairExpressionIterator
specifier|public
class|class
name|TokenPairExpressionIterator
extends|extends
name|ExpressionAdapter
block|{
DECL|field|startToken
specifier|protected
specifier|final
name|String
name|startToken
decl_stmt|;
DECL|field|endToken
specifier|protected
specifier|final
name|String
name|endToken
decl_stmt|;
DECL|field|includeTokens
specifier|protected
specifier|final
name|boolean
name|includeTokens
decl_stmt|;
DECL|method|TokenPairExpressionIterator (String startToken, String endToken, boolean includeTokens)
specifier|public
name|TokenPairExpressionIterator
parameter_list|(
name|String
name|startToken
parameter_list|,
name|String
name|endToken
parameter_list|,
name|boolean
name|includeTokens
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|startToken
argument_list|,
literal|"startToken"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|endToken
argument_list|,
literal|"endToken"
argument_list|)
expr_stmt|;
name|this
operator|.
name|startToken
operator|=
name|startToken
expr_stmt|;
name|this
operator|.
name|endToken
operator|=
name|endToken
expr_stmt|;
name|this
operator|.
name|includeTokens
operator|=
name|includeTokens
expr_stmt|;
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
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
name|createIterator
argument_list|(
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
comment|/**      * Strategy to create the iterator      *      * @param in input stream to iterate      * @param charset charset      * @return the iterator      */
DECL|method|createIterator (InputStream in, String charset)
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
name|String
name|charset
parameter_list|)
block|{
name|TokenPairIterator
name|iterator
init|=
operator|new
name|TokenPairIterator
argument_list|(
name|startToken
argument_list|,
name|endToken
argument_list|,
name|includeTokens
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
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"tokenize[body() using tokens: "
operator|+
name|startToken
operator|+
literal|"..."
operator|+
name|endToken
operator|+
literal|"]"
return|;
block|}
comment|/**      * Iterator to walk the input stream      */
DECL|class|TokenPairIterator
specifier|static
class|class
name|TokenPairIterator
implements|implements
name|Iterator
argument_list|<
name|Object
argument_list|>
implements|,
name|Closeable
block|{
DECL|field|startToken
specifier|final
name|String
name|startToken
decl_stmt|;
DECL|field|scanStartToken
name|String
name|scanStartToken
decl_stmt|;
DECL|field|endToken
specifier|final
name|String
name|endToken
decl_stmt|;
DECL|field|scanEndToken
name|String
name|scanEndToken
decl_stmt|;
DECL|field|includeTokens
specifier|final
name|boolean
name|includeTokens
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
DECL|method|TokenPairIterator (String startToken, String endToken, boolean includeTokens, InputStream in, String charset)
name|TokenPairIterator
parameter_list|(
name|String
name|startToken
parameter_list|,
name|String
name|endToken
parameter_list|,
name|boolean
name|includeTokens
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
name|startToken
operator|=
name|startToken
expr_stmt|;
name|this
operator|.
name|endToken
operator|=
name|endToken
expr_stmt|;
name|this
operator|.
name|includeTokens
operator|=
name|includeTokens
expr_stmt|;
name|this
operator|.
name|in
operator|=
name|in
expr_stmt|;
name|this
operator|.
name|charset
operator|=
name|charset
expr_stmt|;
comment|// make sure [ and ] is escaped as we use scanner which is reg exp based
comment|// where [ and ] have special meaning
name|scanStartToken
operator|=
name|startToken
expr_stmt|;
if|if
condition|(
name|scanStartToken
operator|.
name|startsWith
argument_list|(
literal|"["
argument_list|)
condition|)
block|{
name|scanStartToken
operator|=
literal|"\\"
operator|+
name|scanStartToken
expr_stmt|;
block|}
if|if
condition|(
name|scanStartToken
operator|.
name|endsWith
argument_list|(
literal|"]"
argument_list|)
condition|)
block|{
name|scanStartToken
operator|=
name|scanStartToken
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
literal|"\\]"
expr_stmt|;
block|}
name|scanEndToken
operator|=
name|endToken
expr_stmt|;
if|if
condition|(
name|scanEndToken
operator|.
name|startsWith
argument_list|(
literal|"["
argument_list|)
condition|)
block|{
name|scanEndToken
operator|=
literal|"\\"
operator|+
name|scanEndToken
expr_stmt|;
block|}
if|if
condition|(
name|scanEndToken
operator|.
name|endsWith
argument_list|(
literal|"]"
argument_list|)
condition|)
block|{
name|scanEndToken
operator|=
name|scanEndToken
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|scanEndToken
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|+
literal|"\\]"
expr_stmt|;
block|}
block|}
DECL|method|init ()
name|void
name|init
parameter_list|()
block|{
comment|// use end token as delimiter
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
DECL|method|getNext (boolean first)
name|Object
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
comment|// only grab text after the start token
if|if
condition|(
name|next
operator|!=
literal|null
operator|&&
name|next
operator|.
name|contains
argument_list|(
name|startToken
argument_list|)
condition|)
block|{
name|next
operator|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|next
argument_list|,
name|startToken
argument_list|)
expr_stmt|;
comment|// include tokens in answer
if|if
condition|(
name|next
operator|!=
literal|null
operator|&&
name|includeTokens
condition|)
block|{
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
name|startToken
argument_list|)
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
block|}
else|else
block|{
comment|// must have start token, otherwise we have reached beyond last tokens
comment|// and should not return more data
return|return
literal|null
return|;
block|}
return|return
name|next
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
block|}
end_class

end_unit

