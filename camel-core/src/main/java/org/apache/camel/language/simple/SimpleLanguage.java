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
name|IllegalSyntaxException
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
comment|/**  * A<a href="http://activemq.apache.org/camel/simple.html">simple language</a>  * which maps simple property style notations to access headers and bodies.  * Examples of supported expressions are:  *<ul>  *<li>id to access the inbound message id</li>  *<li>in.body or body to access the inbound body</li>  *<li>out.body to access the inbound body</li>  *<li>in.header.foo or header.foo to access an inbound header called 'foo'</li>  *<li>out.header.foo to access an outbound header called 'foo'</li>  *<li>property.foo to access the exchange property called 'foo'</li>  *<li>sys.foo to access the system property called 'foo'</li>  *<li>exception.messsage to access the exception message</li>  *<li>date:&lt;command&gt;:&lt;pattern&gt; for date formatting using the {@link java.text.SimpleDateFormat} patterns.  *     Supported commands are:<tt>now</tt> for current timestamp,  *<tt>in.header.xxx</tt> or<tt>header.xxx</tt> to use the Date object in the in header.  *<tt>out.header.xxx</tt> to use the Date object in the out header.  *</li>  *<li>bean:&lt;bean expression&gt; to invoke a bean using the  * {@link org.apache.camel.language.bean.BeanLanguage BeanLanguage}</li>  *</ul>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|SimpleLanguage
specifier|public
class|class
name|SimpleLanguage
extends|extends
name|AbstractSimpleLanguage
block|{
DECL|method|simple (String expression)
specifier|public
specifier|static
name|Expression
name|simple
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|SimpleLanguage
name|language
init|=
operator|new
name|SimpleLanguage
argument_list|()
decl_stmt|;
return|return
name|language
operator|.
name|createExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
DECL|method|createSimpleExpression (String expression)
specifier|protected
name|Expression
name|createSimpleExpression
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
comment|// in header expression
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
operator|==
literal|null
condition|)
block|{
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"headers."
argument_list|,
name|expression
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
literal|"in.headers."
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
comment|// out header expression
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
name|outHeaderExpression
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
comment|// system property
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
name|systemProperty
argument_list|(
name|remainder
argument_list|)
return|;
block|}
comment|// date: prefix
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"date:"
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
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalSyntaxException
argument_list|(
name|this
argument_list|,
name|expression
operator|+
literal|" ${date:command:pattern} is the correct syntax."
argument_list|)
throw|;
block|}
name|String
name|command
init|=
name|parts
index|[
literal|0
index|]
decl_stmt|;
name|String
name|pattern
init|=
name|parts
index|[
literal|1
index|]
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
name|beanExpression
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
block|}
end_class

end_unit

