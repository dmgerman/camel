begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|util
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
name|builder
operator|.
name|ExpressionClause
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
name|model
operator|.
name|language
operator|.
name|ExpressionDefinition
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
name|model
operator|.
name|language
operator|.
name|MethodCallExpression
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
name|model
operator|.
name|language
operator|.
name|XPathExpression
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|ExpressionRenderer
specifier|public
specifier|final
class|class
name|ExpressionRenderer
block|{
DECL|method|ExpressionRenderer ()
specifier|private
name|ExpressionRenderer
parameter_list|()
block|{
comment|// Utility class, no public or protected default constructor
block|}
comment|/**      * a common render method to process the expressionDefinition      *       * @param buffer      * @param expression      */
DECL|method|render (StringBuilder buffer, ExpressionDefinition expression)
specifier|public
specifier|static
name|void
name|render
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
if|if
condition|(
name|buffer
operator|.
name|toString
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|")"
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|expression
operator|instanceof
name|ExpressionClause
condition|)
block|{
name|renderExpressionClause
argument_list|(
name|buffer
argument_list|,
operator|(
name|ExpressionClause
operator|)
name|expression
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expression
operator|.
name|getExpressionValue
argument_list|()
operator|instanceof
name|ExpressionClause
condition|)
block|{
name|render
argument_list|(
name|buffer
argument_list|,
operator|(
name|ExpressionClause
operator|)
name|expression
operator|.
name|getExpressionValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|expression
operator|.
name|getExpressionValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|renderExpression
argument_list|(
name|buffer
argument_list|,
name|expression
operator|.
name|getExpressionValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expression
operator|.
name|getLanguage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|renderLanguageExpression
argument_list|(
name|buffer
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Render a constant: constant("")      *       * @param expression      * @param buffer      */
DECL|method|renderConstant (StringBuilder buffer, ExpressionDefinition expression)
specifier|public
specifier|static
name|void
name|renderConstant
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|".constant(\""
argument_list|)
operator|.
name|append
argument_list|(
name|expression
operator|.
name|getExpressionValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\")"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Render an expression clause      *       * @param buffer      * @param expression      */
DECL|method|renderExpressionClause (StringBuilder buffer, ExpressionClause expression)
specifier|private
specifier|static
name|void
name|renderExpressionClause
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|ExpressionClause
name|expression
parameter_list|)
block|{
if|if
condition|(
name|expression
operator|.
name|getLanguage
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// render a language expression
name|renderLanguageExpression
argument_list|(
name|buffer
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expression
operator|.
name|getExpressionType
argument_list|()
operator|instanceof
name|MethodCallExpression
condition|)
block|{
comment|// render a methodCall expression
name|String
name|exp
init|=
name|expression
operator|.
name|getExpressionType
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|bean
init|=
name|exp
operator|.
name|substring
argument_list|(
name|exp
operator|.
name|indexOf
argument_list|(
literal|'{'
argument_list|)
operator|+
literal|1
argument_list|,
name|exp
operator|.
name|indexOf
argument_list|(
literal|','
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|method
init|=
name|exp
operator|.
name|substring
argument_list|(
name|exp
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
operator|+
literal|1
argument_list|,
name|exp
operator|.
name|indexOf
argument_list|(
literal|'}'
argument_list|)
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"method(\""
argument_list|)
operator|.
name|append
argument_list|(
name|bean
argument_list|)
operator|.
name|append
argument_list|(
literal|"\", \""
argument_list|)
operator|.
name|append
argument_list|(
name|method
argument_list|)
operator|.
name|append
argument_list|(
literal|"\")"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expression
operator|.
name|getExpressionType
argument_list|()
operator|instanceof
name|XPathExpression
condition|)
block|{
name|XPathExpression
name|xpath
init|=
operator|(
name|XPathExpression
operator|)
name|expression
operator|.
name|getExpressionType
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"xpath(\""
argument_list|)
operator|.
name|append
argument_list|(
name|xpath
operator|.
name|getExpression
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\", "
argument_list|)
operator|.
name|append
argument_list|(
name|xpath
operator|.
name|getResultType
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|".class)"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|renderExpression
argument_list|(
name|buffer
argument_list|,
name|expression
operator|.
name|getExpressionValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Render a language expression      *       * @param buffer      * @param expression      */
DECL|method|renderLanguageExpression (StringBuilder buffer, ExpressionDefinition expression)
specifier|public
specifier|static
name|void
name|renderLanguageExpression
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
comment|// render a language expression
name|buffer
operator|.
name|append
argument_list|(
name|expression
operator|.
name|getLanguage
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"(\""
argument_list|)
expr_stmt|;
if|if
condition|(
name|expression
operator|.
name|getExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|expression
operator|.
name|getExpression
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\")"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expression
operator|.
name|getExpressionValue
argument_list|()
operator|instanceof
name|ExpressionClause
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
operator|(
operator|(
name|ExpressionClause
operator|)
name|expression
operator|.
name|getExpressionValue
argument_list|()
operator|)
operator|.
name|getExpression
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\")"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Render a simple expression: header(foo) -> header("foo")      * tokenize(header(foo), ,) -> header("foo").tokenize(",")      *       * @param buffer      * @param expression      */
DECL|method|renderExpression (StringBuilder buffer, String expression)
specifier|public
specifier|static
name|void
name|renderExpression
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|String
name|expression
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
operator|.
name|contains
argument_list|(
literal|","
argument_list|)
condition|)
block|{
if|if
condition|(
name|expression
operator|.
name|contains
argument_list|(
literal|"("
argument_list|)
condition|)
block|{
comment|// header(foo) -> header("foo")
name|expression
operator|=
name|expression
operator|.
name|replaceAll
argument_list|(
literal|"\\("
argument_list|,
literal|"(\""
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\)"
argument_list|,
literal|"\")"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// body -> body()
name|buffer
operator|.
name|append
argument_list|(
name|expression
argument_list|)
operator|.
name|append
argument_list|(
literal|"()"
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|expression
operator|.
name|startsWith
argument_list|(
literal|"tokenize"
argument_list|)
condition|)
block|{
name|String
name|words
index|[]
init|=
name|expression
operator|.
name|split
argument_list|(
literal|"\\("
argument_list|)
decl_stmt|;
if|if
condition|(
name|words
operator|.
name|length
operator|==
literal|2
condition|)
block|{
comment|// tokenize(body, ,) -> body().tokenize(",")
name|String
name|tokenize
init|=
name|words
index|[
literal|1
index|]
operator|.
name|substring
argument_list|(
name|words
index|[
literal|1
index|]
operator|.
name|indexOf
argument_list|(
literal|" "
argument_list|)
operator|+
literal|1
argument_list|,
name|words
index|[
literal|1
index|]
operator|.
name|lastIndexOf
argument_list|(
literal|")"
argument_list|)
argument_list|)
decl_stmt|;
name|words
index|[
literal|1
index|]
operator|=
name|words
index|[
literal|1
index|]
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|words
index|[
literal|1
index|]
operator|.
name|indexOf
argument_list|(
literal|","
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|words
index|[
literal|1
index|]
operator|.
name|contains
argument_list|(
literal|"["
argument_list|)
condition|)
block|{
comment|// body
name|buffer
operator|.
name|append
argument_list|(
name|words
index|[
literal|1
index|]
argument_list|)
operator|.
name|append
argument_list|(
literal|"()"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// bodyAs[clazz]
name|String
name|word
init|=
name|words
index|[
literal|1
index|]
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|words
index|[
literal|1
index|]
operator|.
name|indexOf
argument_list|(
literal|"As"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|clazz
init|=
name|words
index|[
literal|1
index|]
operator|.
name|substring
argument_list|(
name|words
index|[
literal|1
index|]
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
operator|+
literal|1
argument_list|,
name|words
index|[
literal|1
index|]
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|word
argument_list|)
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
operator|.
name|append
argument_list|(
name|clazz
argument_list|)
operator|.
name|append
argument_list|(
literal|".class)"
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|words
index|[
literal|0
index|]
argument_list|)
operator|.
name|append
argument_list|(
literal|"(\""
argument_list|)
operator|.
name|append
argument_list|(
name|tokenize
argument_list|)
operator|.
name|append
argument_list|(
literal|"\")"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|words
operator|.
name|length
operator|==
literal|3
condition|)
block|{
comment|// tokenize(header(foo), ,) -> header("foo").tokenize(",")
name|String
name|symbolName
init|=
name|words
index|[
literal|2
index|]
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|words
index|[
literal|2
index|]
operator|.
name|indexOf
argument_list|(
literal|")"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|tokenize
init|=
name|words
index|[
literal|2
index|]
operator|.
name|substring
argument_list|(
name|words
index|[
literal|2
index|]
operator|.
name|indexOf
argument_list|(
literal|" "
argument_list|)
operator|+
literal|1
argument_list|,
name|words
index|[
literal|2
index|]
operator|.
name|lastIndexOf
argument_list|(
literal|")"
argument_list|)
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|words
index|[
literal|1
index|]
argument_list|)
operator|.
name|append
argument_list|(
literal|"(\""
argument_list|)
operator|.
name|append
argument_list|(
name|symbolName
argument_list|)
operator|.
name|append
argument_list|(
literal|"\")."
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|words
index|[
literal|0
index|]
argument_list|)
operator|.
name|append
argument_list|(
literal|"(\""
argument_list|)
operator|.
name|append
argument_list|(
name|tokenize
argument_list|)
operator|.
name|append
argument_list|(
literal|"\")"
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|expression
operator|.
name|startsWith
argument_list|(
literal|"append"
argument_list|)
condition|)
block|{
comment|// append(body, World!) -> body().append(" World!")
name|String
name|words
index|[]
init|=
name|expression
operator|.
name|split
argument_list|(
literal|"\\(|, |\\)"
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|words
index|[
literal|1
index|]
argument_list|)
operator|.
name|append
argument_list|(
literal|"()."
argument_list|)
operator|.
name|append
argument_list|(
literal|"append(\""
argument_list|)
operator|.
name|append
argument_list|(
name|words
index|[
literal|2
index|]
argument_list|)
operator|.
name|append
argument_list|(
literal|"\")"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expression
operator|.
name|startsWith
argument_list|(
literal|"prepend"
argument_list|)
condition|)
block|{
comment|// prepend(body, World!) -> body().prepend(" World!")
name|String
name|words
index|[]
init|=
name|expression
operator|.
name|split
argument_list|(
literal|"\\(|, |\\)"
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|words
index|[
literal|1
index|]
argument_list|)
operator|.
name|append
argument_list|(
literal|"()."
argument_list|)
operator|.
name|append
argument_list|(
literal|"prepend(\""
argument_list|)
operator|.
name|append
argument_list|(
name|words
index|[
literal|2
index|]
argument_list|)
operator|.
name|append
argument_list|(
literal|"\")"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

