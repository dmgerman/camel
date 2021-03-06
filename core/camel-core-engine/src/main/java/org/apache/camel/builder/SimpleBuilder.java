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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelExecutionException
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
name|Predicate
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
name|Simple
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
name|ExpressionResultTypeAware
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
name|Language
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
name|PredicateToExpressionAdapter
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
name|ScriptHelper
import|;
end_import

begin_comment
comment|/**  * Creates an {@link Simple} language builder.  *<p/>  * This builder is available in the Java DSL from the {@link RouteBuilder} which  * means that using simple language for {@link Expression}s or  * {@link Predicate}s is very easy with the help of this builder.  */
end_comment

begin_class
DECL|class|SimpleBuilder
specifier|public
class|class
name|SimpleBuilder
implements|implements
name|Predicate
implements|,
name|Expression
implements|,
name|ExpressionResultTypeAware
block|{
DECL|field|text
specifier|private
specifier|final
name|String
name|text
decl_stmt|;
DECL|field|resultType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
decl_stmt|;
comment|// cache the expression/predicate
DECL|field|expression
specifier|private
specifier|volatile
name|Expression
name|expression
decl_stmt|;
DECL|field|predicate
specifier|private
specifier|volatile
name|Predicate
name|predicate
decl_stmt|;
DECL|method|SimpleBuilder (String text)
specifier|public
name|SimpleBuilder
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
block|}
DECL|method|simple (String text)
specifier|public
specifier|static
name|SimpleBuilder
name|simple
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
operator|new
name|SimpleBuilder
argument_list|(
name|text
argument_list|)
return|;
block|}
DECL|method|simple (String text, Class<?> resultType)
specifier|public
specifier|static
name|SimpleBuilder
name|simple
parameter_list|(
name|String
name|text
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
name|SimpleBuilder
name|answer
init|=
name|simple
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setResultType
argument_list|(
name|resultType
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|simpleF (String formatText, Object... values)
specifier|public
specifier|static
name|SimpleBuilder
name|simpleF
parameter_list|(
name|String
name|formatText
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
return|return
name|simple
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|formatText
argument_list|,
name|values
argument_list|)
argument_list|)
return|;
block|}
DECL|method|simpleF (String formatText, Class<?> resultType, Object... values)
specifier|public
specifier|static
name|SimpleBuilder
name|simpleF
parameter_list|(
name|String
name|formatText
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
return|return
name|simple
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|formatText
argument_list|,
name|values
argument_list|)
argument_list|,
name|resultType
argument_list|)
return|;
block|}
DECL|method|getText ()
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|text
return|;
block|}
annotation|@
name|Override
DECL|method|getExpressionText ()
specifier|public
name|String
name|getExpressionText
parameter_list|()
block|{
return|return
name|getText
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getResultType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getResultType
parameter_list|()
block|{
return|return
name|resultType
return|;
block|}
DECL|method|setResultType (Class<?> resultType)
specifier|public
name|void
name|setResultType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
name|this
operator|.
name|resultType
operator|=
name|resultType
expr_stmt|;
block|}
DECL|method|resultType (Class<?> resultType)
specifier|public
name|SimpleBuilder
name|resultType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
name|setResultType
argument_list|(
name|resultType
argument_list|)
expr_stmt|;
return|return
name|this
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
if|if
condition|(
name|predicate
operator|==
literal|null
condition|)
block|{
name|predicate
operator|=
name|createPredicate
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
name|predicate
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|evaluate (Exchange exchange, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|expression
operator|==
literal|null
condition|)
block|{
name|expression
operator|=
name|createExpression
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|createPredicate (Exchange exchange)
specifier|private
name|Predicate
name|createPredicate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
comment|// resolve property placeholders
name|String
name|resolve
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|text
argument_list|)
decl_stmt|;
comment|// and optional it be refer to an external script on the
comment|// file/classpath
name|resolve
operator|=
name|ScriptHelper
operator|.
name|resolveOptionalExternalScript
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|resolve
argument_list|)
expr_stmt|;
name|Language
name|simple
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
return|return
name|simple
operator|.
name|createPredicate
argument_list|(
name|resolve
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|CamelExecutionException
operator|.
name|wrapCamelExecutionException
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|createExpression (Exchange exchange)
specifier|private
name|Expression
name|createExpression
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
comment|// resolve property placeholders
name|String
name|resolve
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|text
argument_list|)
decl_stmt|;
comment|// and optional it be refer to an external script on the
comment|// file/classpath
name|resolve
operator|=
name|ScriptHelper
operator|.
name|resolveOptionalExternalScript
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|resolve
argument_list|)
expr_stmt|;
name|Language
name|simple
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
return|return
name|createSimpleExpression
argument_list|(
name|simple
argument_list|,
name|resolve
argument_list|,
name|resultType
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|CamelExecutionException
operator|.
name|wrapCamelExecutionException
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|createSimpleExpression (Language simple, String expression, Class<?> resultType)
specifier|private
specifier|static
name|Expression
name|createSimpleExpression
parameter_list|(
name|Language
name|simple
parameter_list|,
name|String
name|expression
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
if|if
condition|(
name|resultType
operator|==
name|Boolean
operator|.
name|class
operator|||
name|resultType
operator|==
name|boolean
operator|.
name|class
condition|)
block|{
comment|// if its a boolean as result then its a predicate
name|Predicate
name|predicate
init|=
name|simple
operator|.
name|createPredicate
argument_list|(
name|expression
argument_list|)
decl_stmt|;
return|return
name|PredicateToExpressionAdapter
operator|.
name|toExpression
argument_list|(
name|predicate
argument_list|)
return|;
block|}
else|else
block|{
name|Expression
name|exp
init|=
name|simple
operator|.
name|createExpression
argument_list|(
name|expression
argument_list|)
decl_stmt|;
if|if
condition|(
name|resultType
operator|!=
literal|null
condition|)
block|{
name|exp
operator|=
name|ExpressionBuilder
operator|.
name|convertToExpression
argument_list|(
name|exp
argument_list|,
name|resultType
argument_list|)
expr_stmt|;
block|}
return|return
name|exp
return|;
block|}
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
literal|"Simple: "
operator|+
name|text
return|;
block|}
block|}
end_class

end_unit

