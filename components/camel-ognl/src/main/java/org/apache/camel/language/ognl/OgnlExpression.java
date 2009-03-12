begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.ognl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|ognl
package|;
end_package

begin_import
import|import
name|ognl
operator|.
name|Ognl
import|;
end_import

begin_import
import|import
name|ognl
operator|.
name|OgnlContext
import|;
end_import

begin_import
import|import
name|ognl
operator|.
name|OgnlException
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
name|ExpressionEvaluationException
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
name|ExpressionIllegalSyntaxException
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
name|impl
operator|.
name|ExpressionSupport
import|;
end_import

begin_comment
comment|/**  * An<a href="http://www.ognl.org/">OGNL</a> {@link Expression}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|OgnlExpression
specifier|public
class|class
name|OgnlExpression
extends|extends
name|ExpressionSupport
block|{
DECL|field|expressionString
specifier|private
specifier|final
name|String
name|expressionString
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
DECL|field|expression
specifier|private
name|Object
name|expression
decl_stmt|;
DECL|method|OgnlExpression (OgnlLanguage language, String expressionString, Class<?> type)
specifier|public
name|OgnlExpression
parameter_list|(
name|OgnlLanguage
name|language
parameter_list|,
name|String
name|expressionString
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|expressionString
operator|=
name|expressionString
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
try|try
block|{
name|this
operator|.
name|expression
operator|=
name|Ognl
operator|.
name|parseExpression
argument_list|(
name|expressionString
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OgnlException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ExpressionIllegalSyntaxException
argument_list|(
name|expressionString
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|ognl (String expression)
specifier|public
specifier|static
name|OgnlExpression
name|ognl
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
operator|new
name|OgnlExpression
argument_list|(
operator|new
name|OgnlLanguage
argument_list|()
argument_list|,
name|expression
argument_list|,
name|Object
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|evaluate (Exchange exchange)
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// TODO we could use caching here but then we'd have possible
comment|// concurrency issues so lets assume that the provider caches
name|OgnlContext
name|oglContext
init|=
operator|new
name|OgnlContext
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|Ognl
operator|.
name|getValue
argument_list|(
name|expression
argument_list|,
name|oglContext
argument_list|,
operator|new
name|RootObject
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OgnlException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ExpressionEvaluationException
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|assertionFailureMessage (Exchange exchange)
specifier|protected
name|String
name|assertionFailureMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|expressionString
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
literal|"OGNL["
operator|+
name|expressionString
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

