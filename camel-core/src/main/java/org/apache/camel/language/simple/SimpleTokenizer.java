begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.simple
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|simple
package|;
end_package

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
name|concurrent
operator|.
name|CopyOnWriteArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|language
operator|.
name|simple
operator|.
name|types
operator|.
name|SimpleToken
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
name|language
operator|.
name|simple
operator|.
name|types
operator|.
name|SimpleTokenType
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
name|language
operator|.
name|simple
operator|.
name|types
operator|.
name|TokenType
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
comment|/**  * Tokenizer to create {@link SimpleToken} from the input.  */
end_comment

begin_class
DECL|class|SimpleTokenizer
specifier|public
specifier|final
class|class
name|SimpleTokenizer
block|{
comment|// use CopyOnWriteArrayList so we can modify it in the for loop when changing function start/end tokens
DECL|field|KNOWN_TOKENS
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|SimpleTokenType
argument_list|>
name|KNOWN_TOKENS
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|SimpleTokenType
argument_list|>
argument_list|()
decl_stmt|;
comment|// optimise to be able to quick check for start functions
DECL|field|FUNCTION_START
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|FUNCTION_START
init|=
operator|new
name|String
index|[]
block|{
literal|"${"
block|,
literal|"$simple{"
block|}
decl_stmt|;
static|static
block|{
comment|// add known tokens
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|functionStart
argument_list|,
literal|"${"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|functionStart
argument_list|,
literal|"$simple{"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|functionEnd
argument_list|,
literal|"}"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|whiteSpace
argument_list|,
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|whiteSpace
argument_list|,
literal|"\t"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|whiteSpace
argument_list|,
literal|"\n"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|whiteSpace
argument_list|,
literal|"\r"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|singleQuote
argument_list|,
literal|"'"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|doubleQuote
argument_list|,
literal|"\""
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|booleanValue
argument_list|,
literal|"true"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|booleanValue
argument_list|,
literal|"false"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|nullValue
argument_list|,
literal|"null"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|escape
argument_list|,
literal|"\\"
argument_list|)
argument_list|)
expr_stmt|;
comment|// binary operators
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|"=="
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|"=~"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|">="
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|"<="
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|">"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|"<"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|"!="
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|"not is"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|"is"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|"not contains"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|"contains"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|"not regex"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|"regex"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|"not in"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|"in"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|"range"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|"not range"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|"starts with"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|binaryOperator
argument_list|,
literal|"ends with"
argument_list|)
argument_list|)
expr_stmt|;
comment|// unary operators
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|unaryOperator
argument_list|,
literal|"++"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|unaryOperator
argument_list|,
literal|"--"
argument_list|)
argument_list|)
expr_stmt|;
comment|// logical operators
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|logicalOperator
argument_list|,
literal|"&&"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|logicalOperator
argument_list|,
literal|"||"
argument_list|)
argument_list|)
expr_stmt|;
comment|// TODO: @deprecated logical operators, to be removed in Camel 3.0
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|logicalOperator
argument_list|,
literal|"and"
argument_list|)
argument_list|)
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|logicalOperator
argument_list|,
literal|"or"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|SimpleTokenizer ()
specifier|private
name|SimpleTokenizer
parameter_list|()
block|{
comment|// static methods
block|}
comment|/**      * Does the expression include a simple function.      *      * @param expression the expression      * @return<tt>true</tt> if one or more simple function is included in the expression      */
DECL|method|hasFunctionStartToken (String expression)
specifier|public
specifier|static
name|boolean
name|hasFunctionStartToken
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
return|return
name|expression
operator|.
name|contains
argument_list|(
name|FUNCTION_START
index|[
literal|0
index|]
argument_list|)
operator|||
name|expression
operator|.
name|contains
argument_list|(
name|FUNCTION_START
index|[
literal|1
index|]
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * @see SimpleLanguage#changeFunctionStartToken(String...)      */
DECL|method|changeFunctionStartToken (String... startToken)
specifier|public
specifier|static
name|void
name|changeFunctionStartToken
parameter_list|(
name|String
modifier|...
name|startToken
parameter_list|)
block|{
for|for
control|(
name|SimpleTokenType
name|type
range|:
name|KNOWN_TOKENS
control|)
block|{
if|if
condition|(
name|type
operator|.
name|getType
argument_list|()
operator|==
name|TokenType
operator|.
name|functionStart
condition|)
block|{
name|KNOWN_TOKENS
operator|.
name|remove
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|startToken
operator|.
name|length
operator|>
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"At most 2 start tokens is allowed"
argument_list|)
throw|;
block|}
comment|// reset
name|FUNCTION_START
index|[
literal|0
index|]
operator|=
literal|""
expr_stmt|;
name|FUNCTION_START
index|[
literal|1
index|]
operator|=
literal|""
expr_stmt|;
comment|// add in start of list as its a more common token to be used
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|startToken
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|token
init|=
name|startToken
index|[
name|i
index|]
decl_stmt|;
name|FUNCTION_START
index|[
name|i
index|]
operator|=
name|token
expr_stmt|;
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
literal|0
argument_list|,
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|functionStart
argument_list|,
name|token
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @see SimpleLanguage#changeFunctionEndToken(String...)      */
DECL|method|changeFunctionEndToken (String... endToken)
specifier|public
specifier|static
name|void
name|changeFunctionEndToken
parameter_list|(
name|String
modifier|...
name|endToken
parameter_list|)
block|{
for|for
control|(
name|SimpleTokenType
name|type
range|:
name|KNOWN_TOKENS
control|)
block|{
if|if
condition|(
name|type
operator|.
name|getType
argument_list|()
operator|==
name|TokenType
operator|.
name|functionEnd
condition|)
block|{
name|KNOWN_TOKENS
operator|.
name|remove
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
block|}
comment|// add after the start tokens
name|int
name|pos
init|=
literal|0
decl_stmt|;
for|for
control|(
name|SimpleTokenType
name|type
range|:
name|KNOWN_TOKENS
control|)
block|{
if|if
condition|(
name|type
operator|.
name|getType
argument_list|()
operator|==
name|TokenType
operator|.
name|functionStart
condition|)
block|{
name|pos
operator|++
expr_stmt|;
block|}
block|}
comment|// add after function start of list as its a more common token to be used
for|for
control|(
name|String
name|token
range|:
name|endToken
control|)
block|{
name|KNOWN_TOKENS
operator|.
name|add
argument_list|(
name|pos
argument_list|,
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|functionEnd
argument_list|,
name|token
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Create the next token      *      * @param expression  the input expression      * @param index       the current index      * @param allowEscape whether to allow escapes      * @param filter      defines the accepted token types to be returned (character is always used as fallback)      * @return the created token, will always return a token      */
DECL|method|nextToken (String expression, int index, boolean allowEscape, TokenType... filter)
specifier|public
specifier|static
name|SimpleToken
name|nextToken
parameter_list|(
name|String
name|expression
parameter_list|,
name|int
name|index
parameter_list|,
name|boolean
name|allowEscape
parameter_list|,
name|TokenType
modifier|...
name|filter
parameter_list|)
block|{
return|return
name|doNextToken
argument_list|(
name|expression
argument_list|,
name|index
argument_list|,
name|allowEscape
argument_list|,
name|filter
argument_list|)
return|;
block|}
comment|/**      * Create the next token      *      * @param expression  the input expression      * @param index       the current index      * @param allowEscape whether to allow escapes      * @return the created token, will always return a token      */
DECL|method|nextToken (String expression, int index, boolean allowEscape)
specifier|public
specifier|static
name|SimpleToken
name|nextToken
parameter_list|(
name|String
name|expression
parameter_list|,
name|int
name|index
parameter_list|,
name|boolean
name|allowEscape
parameter_list|)
block|{
return|return
name|doNextToken
argument_list|(
name|expression
argument_list|,
name|index
argument_list|,
name|allowEscape
argument_list|)
return|;
block|}
DECL|method|doNextToken (String expression, int index, boolean allowEscape, TokenType... filters)
specifier|private
specifier|static
name|SimpleToken
name|doNextToken
parameter_list|(
name|String
name|expression
parameter_list|,
name|int
name|index
parameter_list|,
name|boolean
name|allowEscape
parameter_list|,
name|TokenType
modifier|...
name|filters
parameter_list|)
block|{
name|boolean
name|numericAllowed
init|=
name|acceptType
argument_list|(
name|TokenType
operator|.
name|numericValue
argument_list|,
name|filters
argument_list|)
decl_stmt|;
if|if
condition|(
name|numericAllowed
condition|)
block|{
comment|// is it a numeric value
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|boolean
name|digit
init|=
literal|true
decl_stmt|;
while|while
condition|(
name|digit
operator|&&
name|index
operator|<
name|expression
operator|.
name|length
argument_list|()
condition|)
block|{
name|digit
operator|=
name|Character
operator|.
name|isDigit
argument_list|(
name|expression
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|digit
condition|)
block|{
name|char
name|ch
init|=
name|expression
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
name|index
operator|++
expr_stmt|;
continue|continue;
block|}
comment|// is it a dot or comma as part of a floating point number
name|boolean
name|decimalSeparator
init|=
literal|'.'
operator|==
name|expression
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
operator|||
literal|','
operator|==
name|expression
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|decimalSeparator
operator|&&
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|char
name|ch
init|=
name|expression
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
name|index
operator|++
expr_stmt|;
comment|// assume its still a digit
name|digit
operator|=
literal|true
expr_stmt|;
continue|continue;
block|}
block|}
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
operator|new
name|SimpleToken
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|numericValue
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|index
argument_list|)
return|;
block|}
block|}
name|boolean
name|escapeAllowed
init|=
name|allowEscape
operator|&&
name|acceptType
argument_list|(
name|TokenType
operator|.
name|escape
argument_list|,
name|filters
argument_list|)
decl_stmt|;
if|if
condition|(
name|escapeAllowed
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|char
name|ch
init|=
name|expression
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|boolean
name|escaped
init|=
literal|'\\'
operator|==
name|ch
decl_stmt|;
if|if
condition|(
name|escaped
operator|&&
name|index
operator|<
name|expression
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
comment|// grab next character to escape
name|char
name|next
init|=
name|expression
operator|.
name|charAt
argument_list|(
operator|++
name|index
argument_list|)
decl_stmt|;
comment|// special for new line, tabs and carriage return
name|boolean
name|special
init|=
literal|false
decl_stmt|;
if|if
condition|(
literal|'n'
operator|==
name|next
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|special
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|'t'
operator|==
name|next
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\t"
argument_list|)
expr_stmt|;
name|special
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|'r'
operator|==
name|next
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\r"
argument_list|)
expr_stmt|;
name|special
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|'}'
operator|==
name|next
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
name|special
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
comment|// not special just a regular character
name|sb
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
comment|// force 2 as length if special
return|return
operator|new
name|SimpleToken
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|character
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|index
argument_list|,
name|special
condition|?
literal|2
else|:
literal|1
argument_list|)
return|;
block|}
block|}
comment|// it could be any of the known tokens
name|String
name|text
init|=
name|expression
operator|.
name|substring
argument_list|(
name|index
argument_list|)
decl_stmt|;
for|for
control|(
name|SimpleTokenType
name|token
range|:
name|KNOWN_TOKENS
control|)
block|{
if|if
condition|(
name|acceptType
argument_list|(
name|token
operator|.
name|getType
argument_list|()
argument_list|,
name|filters
argument_list|)
condition|)
block|{
if|if
condition|(
name|acceptToken
argument_list|(
name|token
argument_list|,
name|text
argument_list|,
name|expression
argument_list|,
name|index
argument_list|)
condition|)
block|{
return|return
operator|new
name|SimpleToken
argument_list|(
name|token
argument_list|,
name|index
argument_list|)
return|;
block|}
block|}
block|}
comment|// fallback and create a character token
name|char
name|ch
init|=
name|expression
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|SimpleToken
name|token
init|=
operator|new
name|SimpleToken
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|character
argument_list|,
literal|""
operator|+
name|ch
argument_list|)
argument_list|,
name|index
argument_list|)
decl_stmt|;
return|return
name|token
return|;
block|}
DECL|method|acceptType (TokenType type, TokenType... filters)
specifier|private
specifier|static
name|boolean
name|acceptType
parameter_list|(
name|TokenType
name|type
parameter_list|,
name|TokenType
modifier|...
name|filters
parameter_list|)
block|{
if|if
condition|(
name|filters
operator|==
literal|null
operator|||
name|filters
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
for|for
control|(
name|TokenType
name|filter
range|:
name|filters
control|)
block|{
if|if
condition|(
name|type
operator|==
name|filter
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|acceptToken (SimpleTokenType token, String text, String expression, int index)
specifier|private
specifier|static
name|boolean
name|acceptToken
parameter_list|(
name|SimpleTokenType
name|token
parameter_list|,
name|String
name|text
parameter_list|,
name|String
name|expression
parameter_list|,
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|token
operator|.
name|isUnary
argument_list|()
operator|&&
name|text
operator|.
name|startsWith
argument_list|(
name|token
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|SimpleTokenType
name|functionEndToken
init|=
name|getFunctionEndToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|functionEndToken
operator|!=
literal|null
condition|)
block|{
name|int
name|endLen
init|=
name|functionEndToken
operator|.
name|getValue
argument_list|()
operator|.
name|length
argument_list|()
decl_stmt|;
comment|// special check for unary as the previous must be a function end, and the next a whitespace
comment|// to ensure unary operators is only applied on functions as intended
name|int
name|len
init|=
name|token
operator|.
name|getValue
argument_list|()
operator|.
name|length
argument_list|()
decl_stmt|;
name|String
name|previous
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|index
operator|-
name|endLen
operator|>=
literal|0
condition|)
block|{
name|previous
operator|=
name|expression
operator|.
name|substring
argument_list|(
name|index
operator|-
name|endLen
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
name|String
name|after
init|=
name|text
operator|.
name|substring
argument_list|(
name|len
argument_list|)
decl_stmt|;
name|boolean
name|whiteSpace
init|=
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|after
argument_list|)
operator|||
name|after
operator|.
name|startsWith
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
name|boolean
name|functionEnd
init|=
name|previous
operator|.
name|equals
argument_list|(
name|functionEndToken
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|functionEnd
operator|&&
name|whiteSpace
return|;
block|}
block|}
return|return
name|text
operator|.
name|startsWith
argument_list|(
name|token
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getFunctionEndToken ()
specifier|private
specifier|static
name|SimpleTokenType
name|getFunctionEndToken
parameter_list|()
block|{
for|for
control|(
name|SimpleTokenType
name|token
range|:
name|KNOWN_TOKENS
control|)
block|{
if|if
condition|(
name|token
operator|.
name|isFunctionEnd
argument_list|()
condition|)
block|{
return|return
name|token
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

