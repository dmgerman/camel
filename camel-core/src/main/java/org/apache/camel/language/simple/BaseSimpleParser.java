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
name|ArrayDeque
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
name|Deque
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
name|ast
operator|.
name|Block
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
name|ast
operator|.
name|BlockEnd
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
name|ast
operator|.
name|BlockStart
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
name|ast
operator|.
name|SimpleNode
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
name|ast
operator|.
name|UnaryExpression
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
name|SimpleParserException
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

begin_comment
comment|/**  * Base class for Simple language parser.  *<p/>  * This parser is based on the principles of a  *<a href="http://en.wikipedia.org/wiki/Recursive_descent_parser">recursive descent parser</a>.  */
end_comment

begin_class
DECL|class|BaseSimpleParser
specifier|public
specifier|abstract
class|class
name|BaseSimpleParser
block|{
DECL|field|expression
specifier|protected
specifier|final
name|String
name|expression
decl_stmt|;
DECL|field|tokens
specifier|protected
specifier|final
name|List
argument_list|<
name|SimpleToken
argument_list|>
name|tokens
init|=
operator|new
name|ArrayList
argument_list|<
name|SimpleToken
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|nodes
specifier|protected
specifier|final
name|List
argument_list|<
name|SimpleNode
argument_list|>
name|nodes
init|=
operator|new
name|ArrayList
argument_list|<
name|SimpleNode
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|token
specifier|protected
name|SimpleToken
name|token
decl_stmt|;
DECL|field|previousIndex
specifier|protected
name|int
name|previousIndex
decl_stmt|;
DECL|field|index
specifier|protected
name|int
name|index
decl_stmt|;
DECL|field|allowEscape
specifier|protected
name|boolean
name|allowEscape
init|=
literal|true
decl_stmt|;
DECL|method|BaseSimpleParser (String expression, boolean allowEscape)
specifier|protected
name|BaseSimpleParser
parameter_list|(
name|String
name|expression
parameter_list|,
name|boolean
name|allowEscape
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
name|this
operator|.
name|allowEscape
operator|=
name|allowEscape
expr_stmt|;
block|}
comment|/**      * Advances the parser position to the next known {@link SimpleToken}      * in the input.      */
DECL|method|nextToken ()
specifier|protected
name|void
name|nextToken
parameter_list|()
block|{
if|if
condition|(
name|index
operator|<
name|expression
operator|.
name|length
argument_list|()
condition|)
block|{
name|SimpleToken
name|next
init|=
name|SimpleTokenizer
operator|.
name|nextToken
argument_list|(
name|expression
argument_list|,
name|index
argument_list|,
name|allowEscape
argument_list|)
decl_stmt|;
comment|// add token
name|tokens
operator|.
name|add
argument_list|(
name|next
argument_list|)
expr_stmt|;
name|token
operator|=
name|next
expr_stmt|;
comment|// position index after the token
name|previousIndex
operator|=
name|index
expr_stmt|;
name|index
operator|+=
name|next
operator|.
name|getLength
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// end of tokens
name|token
operator|=
operator|new
name|SimpleToken
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|eol
argument_list|,
literal|null
argument_list|)
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Advances the parser position to the next known {@link SimpleToken}      * in the input.      *      * @param filter filter for accepted token types      */
DECL|method|nextToken (TokenType... filter)
specifier|protected
name|void
name|nextToken
parameter_list|(
name|TokenType
modifier|...
name|filter
parameter_list|)
block|{
if|if
condition|(
name|index
operator|<
name|expression
operator|.
name|length
argument_list|()
condition|)
block|{
name|SimpleToken
name|next
init|=
name|SimpleTokenizer
operator|.
name|nextToken
argument_list|(
name|expression
argument_list|,
name|index
argument_list|,
name|allowEscape
argument_list|,
name|filter
argument_list|)
decl_stmt|;
comment|// add token
name|tokens
operator|.
name|add
argument_list|(
name|next
argument_list|)
expr_stmt|;
name|token
operator|=
name|next
expr_stmt|;
comment|// position index after the token
name|previousIndex
operator|=
name|index
expr_stmt|;
name|index
operator|+=
name|next
operator|.
name|getLength
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// end of tokens
name|token
operator|=
operator|new
name|SimpleToken
argument_list|(
operator|new
name|SimpleTokenType
argument_list|(
name|TokenType
operator|.
name|eol
argument_list|,
literal|null
argument_list|)
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Clears the parser state, which means it can be used for parsing a new input.      */
DECL|method|clear ()
specifier|protected
name|void
name|clear
parameter_list|()
block|{
name|token
operator|=
literal|null
expr_stmt|;
name|previousIndex
operator|=
literal|0
expr_stmt|;
name|index
operator|=
literal|0
expr_stmt|;
name|tokens
operator|.
name|clear
argument_list|()
expr_stmt|;
name|nodes
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Prepares blocks, such as functions, single or double quoted texts.      *<p/>      * This process prepares the {@link Block}s in the AST. This is done      * by linking child {@link SimpleNode nodes} which are within the start and end of the blocks,      * as child to the given block. This is done to have the AST graph updated and prepared properly.      *<p/>      * So when the AST node is later used to create the {@link org.apache.camel.Predicate}s      * or {@link org.apache.camel.Expression}s to be used by Camel then the AST graph      * has a linked and prepared graph of nodes which represent the input expression.      */
DECL|method|prepareBlocks ()
specifier|protected
name|void
name|prepareBlocks
parameter_list|()
block|{
name|List
argument_list|<
name|SimpleNode
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|SimpleNode
argument_list|>
argument_list|()
decl_stmt|;
name|Deque
argument_list|<
name|Block
argument_list|>
name|stack
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|SimpleNode
name|token
range|:
name|nodes
control|)
block|{
if|if
condition|(
name|token
operator|instanceof
name|BlockStart
condition|)
block|{
comment|// a new block is started, so push on the stack
name|stack
operator|.
name|push
argument_list|(
operator|(
name|Block
operator|)
name|token
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|BlockEnd
condition|)
block|{
comment|// end block is just an abstract mode, so we should not add it
if|if
condition|(
name|stack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
name|token
operator|.
name|getToken
argument_list|()
operator|.
name|getType
argument_list|()
operator|.
name|getType
argument_list|()
operator|+
literal|" has no matching start token"
argument_list|,
name|token
operator|.
name|getToken
argument_list|()
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
name|Block
name|top
init|=
name|stack
operator|.
name|pop
argument_list|()
decl_stmt|;
comment|// if there is a block on the stack then it should accept the child token
name|Block
name|block
init|=
name|stack
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|stack
operator|.
name|peek
argument_list|()
decl_stmt|;
if|if
condition|(
name|block
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|block
operator|.
name|acceptAndAddNode
argument_list|(
name|top
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
name|block
operator|.
name|getToken
argument_list|()
operator|.
name|getType
argument_list|()
operator|+
literal|" cannot accept "
operator|+
name|token
operator|.
name|getToken
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|,
name|token
operator|.
name|getToken
argument_list|()
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// no block, so add to answer
name|answer
operator|.
name|add
argument_list|(
name|top
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// if there is a block on the stack then it should accept the child token
name|Block
name|block
init|=
name|stack
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|stack
operator|.
name|peek
argument_list|()
decl_stmt|;
if|if
condition|(
name|block
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|block
operator|.
name|acceptAndAddNode
argument_list|(
name|token
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
name|block
operator|.
name|getToken
argument_list|()
operator|.
name|getType
argument_list|()
operator|+
literal|" cannot accept "
operator|+
name|token
operator|.
name|getToken
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|,
name|token
operator|.
name|getToken
argument_list|()
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// no block, so add to answer
name|answer
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// replace nodes from the stack
name|nodes
operator|.
name|clear
argument_list|()
expr_stmt|;
name|nodes
operator|.
name|addAll
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Prepares unary expressions.      *<p/>      * This process prepares the unary expressions in the AST. This is done      * by linking the unary operator with the left hand side node,      * to have the AST graph updated and prepared properly.      *<p/>      * So when the AST node is later used to create the {@link org.apache.camel.Predicate}s      * or {@link org.apache.camel.Expression}s to be used by Camel then the AST graph      * has a linked and prepared graph of nodes which represent the input expression.      */
DECL|method|prepareUnaryExpressions ()
specifier|protected
name|void
name|prepareUnaryExpressions
parameter_list|()
block|{
name|Deque
argument_list|<
name|SimpleNode
argument_list|>
name|stack
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|SimpleNode
name|node
range|:
name|nodes
control|)
block|{
if|if
condition|(
name|node
operator|instanceof
name|UnaryExpression
condition|)
block|{
name|UnaryExpression
name|token
init|=
operator|(
name|UnaryExpression
operator|)
name|node
decl_stmt|;
comment|// remember the logical operator
name|String
name|operator
init|=
name|token
operator|.
name|getOperator
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|SimpleNode
name|previous
init|=
name|stack
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|stack
operator|.
name|pop
argument_list|()
decl_stmt|;
if|if
condition|(
name|previous
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Unary operator "
operator|+
name|operator
operator|+
literal|" has no left hand side token"
argument_list|,
name|token
operator|.
name|getToken
argument_list|()
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
name|token
operator|.
name|acceptLeft
argument_list|(
name|previous
argument_list|)
expr_stmt|;
block|}
block|}
name|stack
operator|.
name|push
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
comment|// replace nodes from the stack
name|nodes
operator|.
name|clear
argument_list|()
expr_stmt|;
name|nodes
operator|.
name|addAll
argument_list|(
name|stack
argument_list|)
expr_stmt|;
block|}
comment|// --------------------------------------------------------------
comment|// grammar
comment|// --------------------------------------------------------------
comment|/**      * Accept the given token.      *<p/>      * This is to be used by the grammar to accept tokens and then continue parsing      * using the grammar, such as a function grammar.      *      * @param accept  the token      * @return<tt>true</tt> if accepted,<tt>false</tt> otherwise.      */
DECL|method|accept (TokenType accept)
specifier|protected
name|boolean
name|accept
parameter_list|(
name|TokenType
name|accept
parameter_list|)
block|{
return|return
name|token
operator|==
literal|null
operator|||
name|token
operator|.
name|getType
argument_list|()
operator|.
name|getType
argument_list|()
operator|==
name|accept
return|;
block|}
comment|/**      * Expect a given token      *      * @param expect the token to expect      * @throws SimpleParserException is thrown if the token is not as expected      */
DECL|method|expect (TokenType expect)
specifier|protected
name|void
name|expect
parameter_list|(
name|TokenType
name|expect
parameter_list|)
throws|throws
name|SimpleParserException
block|{
if|if
condition|(
name|token
operator|!=
literal|null
operator|&&
name|token
operator|.
name|getType
argument_list|()
operator|.
name|getType
argument_list|()
operator|==
name|expect
condition|)
block|{
return|return;
block|}
elseif|else
if|if
condition|(
name|token
operator|==
literal|null
condition|)
block|{
comment|// use the previous index as that is where the problem is
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"expected symbol "
operator|+
name|expect
operator|+
literal|" but reached eol"
argument_list|,
name|previousIndex
argument_list|)
throw|;
block|}
else|else
block|{
comment|// use the previous index as that is where the problem is
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"expected symbol "
operator|+
name|expect
operator|+
literal|" but was "
operator|+
name|token
operator|.
name|getType
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|,
name|previousIndex
argument_list|)
throw|;
block|}
block|}
comment|/**      * Expect and accept a given number of tokens in sequence.      *<p/>      * This is used to accept whitespace or string literals.      *      * @param expect the token to accept      */
DECL|method|expectAndAcceptMore (TokenType expect)
specifier|protected
name|void
name|expectAndAcceptMore
parameter_list|(
name|TokenType
name|expect
parameter_list|)
block|{
name|expect
argument_list|(
name|expect
argument_list|)
expr_stmt|;
while|while
condition|(
operator|!
name|token
operator|.
name|getType
argument_list|()
operator|.
name|isEol
argument_list|()
operator|&&
name|token
operator|.
name|getType
argument_list|()
operator|.
name|getType
argument_list|()
operator|==
name|expect
condition|)
block|{
name|nextToken
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

