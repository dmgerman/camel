begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.simple.ast
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
operator|.
name|ast
package|;
end_package

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
name|OgnlHelper
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
comment|/**  * Represents one of built-in functions of the  *<a href="http://camel.apache.org/simple.html">simple language</a>  */
end_comment

begin_class
DECL|class|SimpleFunctionExpression
specifier|public
class|class
name|SimpleFunctionExpression
extends|extends
name|LiteralExpression
block|{
DECL|method|SimpleFunctionExpression (SimpleToken token)
specifier|public
name|SimpleFunctionExpression
parameter_list|(
name|SimpleToken
name|token
parameter_list|)
block|{
name|super
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createExpression (String expression)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|String
name|function
init|=
name|text
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|createSimpleExpression
argument_list|(
name|function
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Creates a Camel {@link Expression} based on this model.      *      * @param expression the input string      * @param strict whether to throw exception if the expression was not a function,      *          otherwise<tt>null</tt> is returned      * @return the created {@link Expression}      * @throws org.apache.camel.language.simple.types.SimpleParserException      *          should be thrown if error parsing the model      */
DECL|method|createExpression (String expression, boolean strict)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|String
name|expression
parameter_list|,
name|boolean
name|strict
parameter_list|)
block|{
name|String
name|function
init|=
name|text
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|createSimpleExpression
argument_list|(
name|function
argument_list|,
name|strict
argument_list|)
return|;
block|}
DECL|method|createSimpleExpression (String function, boolean strict)
specifier|private
name|Expression
name|createSimpleExpression
parameter_list|(
name|String
name|function
parameter_list|,
name|boolean
name|strict
parameter_list|)
block|{
comment|// return the function directly if we can create function without analyzing the prefix
name|Expression
name|answer
init|=
name|createSimpleExpressionDirectly
argument_list|(
name|function
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
comment|// body and headers first
name|answer
operator|=
name|createSimpleExpressionBodyOrHeader
argument_list|(
name|function
argument_list|,
name|strict
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
comment|// camelContext OGNL
name|String
name|remainder
init|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"camelContext"
argument_list|,
name|function
argument_list|)
decl_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|boolean
name|invalid
init|=
name|OgnlHelper
operator|.
name|isInvalidValidOgnlExpression
argument_list|(
name|remainder
argument_list|)
decl_stmt|;
if|if
condition|(
name|invalid
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Valid syntax: ${camelContext.OGNL} was: "
operator|+
name|function
argument_list|,
name|token
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|ExpressionBuilder
operator|.
name|camelContextOgnlExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
comment|// Exception OGNL
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"exception"
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|boolean
name|invalid
init|=
name|OgnlHelper
operator|.
name|isInvalidValidOgnlExpression
argument_list|(
name|remainder
argument_list|)
decl_stmt|;
if|if
condition|(
name|invalid
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Valid syntax: ${exception.OGNL} was: "
operator|+
name|function
argument_list|,
name|token
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|ExpressionBuilder
operator|.
name|exchangeExceptionOgnlExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
comment|// property
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"property"
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|==
literal|null
condition|)
block|{
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"exchangeProperty"
argument_list|,
name|function
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
comment|// remove leading character (dot or ?)
if|if
condition|(
name|remainder
operator|.
name|startsWith
argument_list|(
literal|"."
argument_list|)
operator|||
name|remainder
operator|.
name|startsWith
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
name|remainder
operator|=
name|remainder
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// remove starting and ending brackets
if|if
condition|(
name|remainder
operator|.
name|startsWith
argument_list|(
literal|"["
argument_list|)
operator|&&
name|remainder
operator|.
name|endsWith
argument_list|(
literal|"]"
argument_list|)
condition|)
block|{
name|remainder
operator|=
name|remainder
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|remainder
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// validate syntax
name|boolean
name|invalid
init|=
name|OgnlHelper
operator|.
name|isInvalidValidOgnlExpression
argument_list|(
name|remainder
argument_list|)
decl_stmt|;
if|if
condition|(
name|invalid
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Valid syntax: ${exchangeProperty.OGNL} was: "
operator|+
name|function
argument_list|,
name|token
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|OgnlHelper
operator|.
name|isValidOgnlExpression
argument_list|(
name|remainder
argument_list|)
condition|)
block|{
comment|// ognl based property
return|return
name|ExpressionBuilder
operator|.
name|propertyOgnlExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
else|else
block|{
comment|// regular property
return|return
name|ExpressionBuilder
operator|.
name|exchangePropertyExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
block|}
comment|// system property
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"sys."
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|systemPropertyExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"sysenv."
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|systemEnvironmentExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
comment|// exchange OGNL
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"exchange"
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|boolean
name|invalid
init|=
name|OgnlHelper
operator|.
name|isInvalidValidOgnlExpression
argument_list|(
name|remainder
argument_list|)
decl_stmt|;
if|if
condition|(
name|invalid
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Valid syntax: ${exchange.OGNL} was: "
operator|+
name|function
argument_list|,
name|token
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|ExpressionBuilder
operator|.
name|exchangeOgnlExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
comment|// file: prefix
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"file:"
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|Expression
name|fileExpression
init|=
name|createSimpleFileExpression
argument_list|(
name|remainder
argument_list|)
decl_stmt|;
if|if
condition|(
name|function
operator|!=
literal|null
condition|)
block|{
return|return
name|fileExpression
return|;
block|}
block|}
comment|// date: prefix
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"date:"
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|remainder
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Valid syntax: ${date:command:pattern} was: "
operator|+
name|function
argument_list|,
name|token
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
name|String
name|command
init|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|remainder
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|String
name|pattern
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|remainder
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
return|return
name|ExpressionBuilder
operator|.
name|dateExpression
argument_list|(
name|command
argument_list|,
name|pattern
argument_list|)
return|;
block|}
comment|// bean: prefix
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"bean:"
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|beanExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
comment|// properties: prefix
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"properties:"
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|remainder
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>
literal|2
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Valid syntax: ${properties:key[:default]} was: "
operator|+
name|function
argument_list|,
name|token
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|ExpressionBuilder
operator|.
name|propertiesComponentExpression
argument_list|(
name|remainder
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|// properties-location: prefix
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"properties-location:"
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|remainder
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>
literal|3
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Valid syntax: ${properties-location:location:key[:default]} was: "
operator|+
name|function
argument_list|,
name|token
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
name|String
name|locations
init|=
literal|null
decl_stmt|;
name|String
name|key
init|=
name|remainder
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>=
literal|2
condition|)
block|{
name|locations
operator|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|remainder
argument_list|,
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|remainder
argument_list|,
literal|":"
argument_list|)
expr_stmt|;
block|}
return|return
name|ExpressionBuilder
operator|.
name|propertiesComponentExpression
argument_list|(
name|key
argument_list|,
name|locations
argument_list|)
return|;
block|}
comment|// ref: prefix
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"ref:"
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|refExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
comment|// const: prefix
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"type:"
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|Expression
name|exp
init|=
name|ExpressionBuilder
operator|.
name|typeExpression
argument_list|(
name|remainder
argument_list|)
decl_stmt|;
comment|// we want to cache this expression so we wont re-evaluate it as the type/constant wont change
return|return
name|ExpressionBuilder
operator|.
name|cacheExpression
argument_list|(
name|exp
argument_list|)
return|;
block|}
if|if
condition|(
name|strict
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Unknown function: "
operator|+
name|function
argument_list|,
name|token
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|createSimpleExpressionBodyOrHeader (String function, boolean strict)
specifier|private
name|Expression
name|createSimpleExpressionBodyOrHeader
parameter_list|(
name|String
name|function
parameter_list|,
name|boolean
name|strict
parameter_list|)
block|{
comment|// bodyAs
name|String
name|remainder
init|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"bodyAs"
argument_list|,
name|function
argument_list|)
decl_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|String
name|type
init|=
name|ObjectHelper
operator|.
name|between
argument_list|(
name|remainder
argument_list|,
literal|"("
argument_list|,
literal|")"
argument_list|)
decl_stmt|;
name|remainder
operator|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|remainder
argument_list|,
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
operator|||
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|remainder
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Valid syntax: ${bodyAs(type)} was: "
operator|+
name|function
argument_list|,
name|token
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
name|type
operator|=
name|StringHelper
operator|.
name|removeQuotes
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|// mandatoryBodyAs
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"mandatoryBodyAs"
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|String
name|type
init|=
name|ObjectHelper
operator|.
name|between
argument_list|(
name|remainder
argument_list|,
literal|"("
argument_list|,
literal|")"
argument_list|)
decl_stmt|;
name|remainder
operator|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|remainder
argument_list|,
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
operator|||
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|remainder
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Valid syntax: ${mandatoryBodyAs(type)} was: "
operator|+
name|function
argument_list|,
name|token
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
name|type
operator|=
name|StringHelper
operator|.
name|removeQuotes
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|ExpressionBuilder
operator|.
name|mandatoryBodyExpression
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|// body OGNL
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"body"
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|==
literal|null
condition|)
block|{
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"in.body"
argument_list|,
name|function
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|boolean
name|invalid
init|=
name|OgnlHelper
operator|.
name|isInvalidValidOgnlExpression
argument_list|(
name|remainder
argument_list|)
decl_stmt|;
if|if
condition|(
name|invalid
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Valid syntax: ${body.OGNL} was: "
operator|+
name|function
argument_list|,
name|token
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|ExpressionBuilder
operator|.
name|bodyOgnlExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
comment|// headerAs
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"headerAs"
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|String
name|keyAndType
init|=
name|ObjectHelper
operator|.
name|between
argument_list|(
name|remainder
argument_list|,
literal|"("
argument_list|,
literal|")"
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyAndType
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Valid syntax: ${headerAs(key, type)} was: "
operator|+
name|function
argument_list|,
name|token
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
name|String
name|key
init|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|keyAndType
argument_list|,
literal|","
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|keyAndType
argument_list|,
literal|","
argument_list|)
decl_stmt|;
name|remainder
operator|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|remainder
argument_list|,
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|key
argument_list|)
operator|||
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|type
argument_list|)
operator|||
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|remainder
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Valid syntax: ${headerAs(key, type)} was: "
operator|+
name|function
argument_list|,
name|token
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
name|key
operator|=
name|StringHelper
operator|.
name|removeQuotes
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|type
operator|=
name|StringHelper
operator|.
name|removeQuotes
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|ExpressionBuilder
operator|.
name|headerExpression
argument_list|(
name|key
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|// headers function
if|if
condition|(
literal|"in.headers"
operator|.
name|equals
argument_list|(
name|function
argument_list|)
operator|||
literal|"headers"
operator|.
name|equals
argument_list|(
name|function
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|headersExpression
argument_list|()
return|;
block|}
comment|// in header function
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"in.headers"
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|==
literal|null
condition|)
block|{
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"in.header"
argument_list|,
name|function
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remainder
operator|==
literal|null
condition|)
block|{
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"headers"
argument_list|,
name|function
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remainder
operator|==
literal|null
condition|)
block|{
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"header"
argument_list|,
name|function
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
comment|// remove leading character (dot or ?)
if|if
condition|(
name|remainder
operator|.
name|startsWith
argument_list|(
literal|"."
argument_list|)
operator|||
name|remainder
operator|.
name|startsWith
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
name|remainder
operator|=
name|remainder
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// remove starting and ending brackets
if|if
condition|(
name|remainder
operator|.
name|startsWith
argument_list|(
literal|"["
argument_list|)
operator|&&
name|remainder
operator|.
name|endsWith
argument_list|(
literal|"]"
argument_list|)
condition|)
block|{
name|remainder
operator|=
name|remainder
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|remainder
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// remove quotes from key
name|String
name|key
init|=
name|StringHelper
operator|.
name|removeLeadingAndEndingQuotes
argument_list|(
name|remainder
argument_list|)
decl_stmt|;
comment|// validate syntax
name|boolean
name|invalid
init|=
name|OgnlHelper
operator|.
name|isInvalidValidOgnlExpression
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|invalid
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Valid syntax: ${header.name[key]} was: "
operator|+
name|function
argument_list|,
name|token
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|OgnlHelper
operator|.
name|isValidOgnlExpression
argument_list|(
name|key
argument_list|)
condition|)
block|{
comment|// ognl based header
return|return
name|ExpressionBuilder
operator|.
name|headersOgnlExpression
argument_list|(
name|key
argument_list|)
return|;
block|}
else|else
block|{
comment|// regular header
return|return
name|ExpressionBuilder
operator|.
name|headerExpression
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
comment|// out header function
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"out.header."
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|==
literal|null
condition|)
block|{
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"out.headers."
argument_list|,
name|function
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|outHeaderExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
comment|// random
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"random"
argument_list|,
name|function
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|String
name|values
init|=
name|ObjectHelper
operator|.
name|between
argument_list|(
name|remainder
argument_list|,
literal|"("
argument_list|,
literal|")"
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|==
literal|null
operator|||
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|values
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Valid syntax: ${random(min,max)} or ${random(max)} was: "
operator|+
name|function
argument_list|,
name|token
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|values
operator|.
name|contains
argument_list|(
literal|","
argument_list|)
condition|)
block|{
name|String
index|[]
name|tokens
init|=
name|values
operator|.
name|split
argument_list|(
literal|","
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokens
operator|.
name|length
operator|>
literal|2
condition|)
block|{
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Valid syntax: ${random(min,max)} or ${random(max)} was: "
operator|+
name|function
argument_list|,
name|token
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
name|int
name|min
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|tokens
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|int
name|max
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|tokens
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
return|return
name|ExpressionBuilder
operator|.
name|randomExpression
argument_list|(
name|min
argument_list|,
name|max
argument_list|)
return|;
block|}
else|else
block|{
name|int
name|max
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|values
argument_list|)
decl_stmt|;
return|return
name|ExpressionBuilder
operator|.
name|randomExpression
argument_list|(
name|max
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|createSimpleExpressionDirectly (String expression)
specifier|private
name|Expression
name|createSimpleExpressionDirectly
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEqualToAny
argument_list|(
name|expression
argument_list|,
literal|"body"
argument_list|,
literal|"in.body"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|expression
argument_list|,
literal|"out.body"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|outBodyExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|expression
argument_list|,
literal|"id"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|messageIdExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|expression
argument_list|,
literal|"exchangeId"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|exchangeIdExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|expression
argument_list|,
literal|"exchange"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|exchangeExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|expression
argument_list|,
literal|"exception"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|exchangeExceptionExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|expression
argument_list|,
literal|"exception.message"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|exchangeExceptionMessageExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|expression
argument_list|,
literal|"exception.stacktrace"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|exchangeExceptionStackTraceExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|expression
argument_list|,
literal|"threadName"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|threadNameExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|expression
argument_list|,
literal|"camelId"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|camelContextNameExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|expression
argument_list|,
literal|"routeId"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|routeIdExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|expression
argument_list|,
literal|"null"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|nullExpression
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|createSimpleFileExpression (String remainder)
specifier|private
name|Expression
name|createSimpleFileExpression
parameter_list|(
name|String
name|remainder
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"name"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileNameExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"name.noext"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileNameNoExtensionExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"name.ext"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileExtensionExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"onlyname"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileOnlyNameExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"onlyname.noext"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileOnlyNameNoExtensionExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"ext"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileExtensionExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"parent"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileParentExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"path"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|filePathExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"absolute"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileAbsoluteExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"absolute.path"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileAbsolutePathExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"length"
argument_list|)
operator|||
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"size"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileSizeExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"modified"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileLastModifiedExpression
argument_list|()
return|;
block|}
throw|throw
operator|new
name|SimpleParserException
argument_list|(
literal|"Unknown file language syntax: "
operator|+
name|remainder
argument_list|,
name|token
operator|.
name|getIndex
argument_list|()
argument_list|)
throw|;
block|}
DECL|method|ifStartsWithReturnRemainder (String prefix, String text)
specifier|private
name|String
name|ifStartsWithReturnRemainder
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|text
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|String
name|remainder
init|=
name|text
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|remainder
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
name|remainder
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

