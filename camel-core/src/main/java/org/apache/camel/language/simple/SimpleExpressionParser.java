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
name|ArrayList
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
name|Expression
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
name|builder
operator|.
name|ExpressionBuilder
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
name|LiteralExpression
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
name|LiteralNode
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
name|SimpleFunctionEnd
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
name|SimpleFunctionStart
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

begin_comment
comment|/**  * A parser to parse simple language as a Camel {@link Expression}  */
end_comment

begin_class
DECL|class|SimpleExpressionParser
specifier|public
class|class
name|SimpleExpressionParser
extends|extends
name|BaseSimpleParser
block|{
DECL|method|SimpleExpressionParser (String expression)
specifier|public
name|SimpleExpressionParser
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|super
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
DECL|method|parseExpression ()
specifier|public
name|Expression
name|parseExpression
parameter_list|()
block|{
name|clear
argument_list|()
expr_stmt|;
try|try
block|{
return|return
name|doParseExpression
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|SimpleParserException
name|e
parameter_list|)
block|{
comment|// catch parser exception and turn that into a syntax exceptions
throw|throw
operator|new
name|SimpleIllegalSyntaxException
argument_list|(
name|expression
argument_list|,
name|e
operator|.
name|getIndex
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// include exception in rethrown exception
throw|throw
operator|new
name|SimpleIllegalSyntaxException
argument_list|(
name|expression
argument_list|,
operator|-
literal|1
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|doParseExpression ()
specifier|protected
name|Expression
name|doParseExpression
parameter_list|()
block|{
comment|// parse the expression using the following grammar
name|nextToken
argument_list|()
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
condition|)
block|{
comment|// an expression supports just template (eg text), functions, or unary operator
name|templateText
argument_list|()
expr_stmt|;
name|functionText
argument_list|()
expr_stmt|;
name|unaryOperator
argument_list|()
expr_stmt|;
name|nextToken
argument_list|()
expr_stmt|;
block|}
comment|// now after parsing we need a bit of work to do, to make it easier to turn the tokens
comment|// into and ast, and then from the ast, to Camel expression(s).
comment|// hence why there is a number of tasks going on below to accomplish this
comment|// turn the tokens into the ast model
name|parseAndCreateAstModel
argument_list|()
expr_stmt|;
comment|// compact and stack blocks (eg function start/end)
name|prepareBlocks
argument_list|()
expr_stmt|;
comment|// compact and stack unary operators
name|prepareUnaryExpressions
argument_list|()
expr_stmt|;
comment|// create and return as a Camel expression
name|List
argument_list|<
name|Expression
argument_list|>
name|expressions
init|=
name|createExpressions
argument_list|()
decl_stmt|;
if|if
condition|(
name|expressions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|expressions
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|expressions
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
else|else
block|{
comment|// concat expressions as evaluating an expression is like a template language
return|return
name|ExpressionBuilder
operator|.
name|concatExpression
argument_list|(
name|expressions
argument_list|,
name|expression
argument_list|)
return|;
block|}
block|}
DECL|method|parseAndCreateAstModel ()
specifier|protected
name|void
name|parseAndCreateAstModel
parameter_list|()
block|{
comment|// we loop the tokens and create a sequence of ast nodes
name|LiteralNode
name|imageToken
init|=
literal|null
decl_stmt|;
for|for
control|(
name|SimpleToken
name|token
range|:
name|tokens
control|)
block|{
comment|// break if eol
if|if
condition|(
name|token
operator|.
name|getType
argument_list|()
operator|.
name|isEol
argument_list|()
condition|)
block|{
break|break;
block|}
comment|// create a node from the token
name|SimpleNode
name|node
init|=
name|createNode
argument_list|(
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
comment|// a new token was created so the current image token need to be added first
if|if
condition|(
name|imageToken
operator|!=
literal|null
condition|)
block|{
name|nodes
operator|.
name|add
argument_list|(
name|imageToken
argument_list|)
expr_stmt|;
name|imageToken
operator|=
literal|null
expr_stmt|;
block|}
comment|// and then add the created node
name|nodes
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
comment|// continue to next
continue|continue;
block|}
comment|// if no token was created then its a character/whitespace/escaped symbol
comment|// which we need to add together in the same image
if|if
condition|(
name|imageToken
operator|==
literal|null
condition|)
block|{
name|imageToken
operator|=
operator|new
name|LiteralExpression
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
name|imageToken
operator|.
name|addText
argument_list|(
name|token
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// append any leftover image tokens (when we reached eol)
if|if
condition|(
name|imageToken
operator|!=
literal|null
condition|)
block|{
name|nodes
operator|.
name|add
argument_list|(
name|imageToken
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createNode (SimpleToken token)
specifier|private
name|SimpleNode
name|createNode
parameter_list|(
name|SimpleToken
name|token
parameter_list|)
block|{
comment|// expression only support functions and unary operators
if|if
condition|(
name|token
operator|.
name|getType
argument_list|()
operator|.
name|isFunctionStart
argument_list|()
condition|)
block|{
return|return
operator|new
name|SimpleFunctionStart
argument_list|(
name|token
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|token
operator|.
name|getType
argument_list|()
operator|.
name|isFunctionEnd
argument_list|()
condition|)
block|{
return|return
operator|new
name|SimpleFunctionEnd
argument_list|(
name|token
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|token
operator|.
name|getType
argument_list|()
operator|.
name|isUnary
argument_list|()
condition|)
block|{
return|return
operator|new
name|UnaryExpression
argument_list|(
name|token
argument_list|)
return|;
block|}
comment|// by returning null, we will let the parser determine what to do
return|return
literal|null
return|;
block|}
DECL|method|createExpressions ()
specifier|private
name|List
argument_list|<
name|Expression
argument_list|>
name|createExpressions
parameter_list|()
block|{
name|List
argument_list|<
name|Expression
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Expression
argument_list|>
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
name|Expression
name|exp
init|=
name|token
operator|.
name|createExpression
argument_list|(
name|expression
argument_list|)
decl_stmt|;
if|if
condition|(
name|exp
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|exp
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|// --------------------------------------------------------------
comment|// grammar
comment|// --------------------------------------------------------------
comment|// the expression parser only understands
comment|// - template = literal texts with can contain embedded functions
comment|// - function = simple functions such as ${body} etc
comment|// - unary operator = operator attached to the left hand side node
DECL|method|templateText ()
specifier|protected
name|void
name|templateText
parameter_list|()
block|{
comment|// for template we accept anything but functions
while|while
condition|(
operator|!
name|token
operator|.
name|getType
argument_list|()
operator|.
name|isFunctionStart
argument_list|()
operator|&&
operator|!
name|token
operator|.
name|getType
argument_list|()
operator|.
name|isFunctionEnd
argument_list|()
operator|&&
operator|!
name|token
operator|.
name|getType
argument_list|()
operator|.
name|isEol
argument_list|()
condition|)
block|{
name|nextToken
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|functionText ()
specifier|protected
name|boolean
name|functionText
parameter_list|()
block|{
if|if
condition|(
name|accept
argument_list|(
name|TokenType
operator|.
name|functionStart
argument_list|)
condition|)
block|{
name|nextToken
argument_list|()
expr_stmt|;
while|while
condition|(
operator|!
name|token
operator|.
name|getType
argument_list|()
operator|.
name|isFunctionEnd
argument_list|()
operator|&&
operator|!
name|token
operator|.
name|getType
argument_list|()
operator|.
name|isEol
argument_list|()
condition|)
block|{
comment|// we need to loop until we find the ending function quote, or the eol
name|nextToken
argument_list|()
expr_stmt|;
block|}
name|expect
argument_list|(
name|TokenType
operator|.
name|functionEnd
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|unaryOperator ()
specifier|protected
name|boolean
name|unaryOperator
parameter_list|()
block|{
if|if
condition|(
name|accept
argument_list|(
name|TokenType
operator|.
name|unaryOperator
argument_list|)
condition|)
block|{
name|nextToken
argument_list|()
expr_stmt|;
comment|// there should be a whitespace after the operator
name|expect
argument_list|(
name|TokenType
operator|.
name|whiteSpace
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

