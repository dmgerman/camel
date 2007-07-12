begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|builder
operator|.
name|PredicateBuilder
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * A<a href="http://activemq.apache.org/camel/simple.html>simple language</a>  * which maps simple property style notations  * to acces headers and bodies. Examples of supported expressions are  *<p/>  *<ul>  *<li>in.header.foo or header.foo to access an inbound header called 'foo'</li>  *<li>in.body or body to access the inbound body</li>  *<li>out.header.foo to access an outbound header called 'foo'</li>  *<li>out.body to access the inbound body</li>  *<li>property.foo to access the exchange property called 'foo'</li>  *<li>sys.foo to access the system property called 'foo'</li>  *</ul>  *  * @version $Revision: $  */
end_comment

begin_class
DECL|class|SimpleLanguage
specifier|public
class|class
name|SimpleLanguage
implements|implements
name|Language
block|{
DECL|method|createPredicate (String expression)
specifier|public
name|Predicate
argument_list|<
name|Exchange
argument_list|>
name|createPredicate
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
name|PredicateBuilder
operator|.
name|toPredicate
argument_list|(
name|createExpression
argument_list|(
name|expression
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createExpression (String expression)
specifier|public
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|createExpression
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
name|equals
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
name|String
name|remainder
init|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"in.header."
argument_list|,
name|expression
argument_list|)
decl_stmt|;
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
literal|"header."
argument_list|,
name|expression
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
name|headerExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"out.header."
argument_list|,
name|expression
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
name|outHeaderExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"property."
argument_list|,
name|expression
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
name|propertyExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"sys."
argument_list|,
name|expression
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
name|propertyExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalSyntaxException
argument_list|(
name|this
argument_list|,
name|expression
argument_list|)
throw|;
block|}
DECL|method|ifStartsWithReturnRemainder (String prefix, String text)
specifier|protected
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

