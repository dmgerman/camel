begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.jxpath
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|jxpath
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
name|Message
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
name|ExpressionEvaluationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|jxpath
operator|.
name|CompiledExpression
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|jxpath
operator|.
name|JXPathContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|jxpath
operator|.
name|JXPathException
import|;
end_import

begin_comment
comment|/**  *<a href="http://commons.apache.org/jxpath/">JXPath</a> {@link Expression} support   */
end_comment

begin_class
DECL|class|JXPathExpression
specifier|public
class|class
name|JXPathExpression
extends|extends
name|ExpressionSupport
argument_list|<
name|Exchange
argument_list|>
block|{
DECL|field|expression
specifier|private
name|String
name|expression
decl_stmt|;
DECL|field|compiledExpression
specifier|private
name|CompiledExpression
name|compiledExpression
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
comment|/** 	 * Creates a new JXPathExpression instance 	 *  	 * @param expression the JXPath expression to be evaluated 	 * @param type the expected result type 	 */
DECL|method|JXPathExpression (String expression, Class<?> type)
specifier|public
name|JXPathExpression
parameter_list|(
name|String
name|expression
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
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
try|try
block|{
name|JXPathContext
name|context
init|=
name|JXPathContext
operator|.
name|newContext
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|getJXPathExpression
argument_list|()
operator|.
name|getValue
argument_list|(
name|context
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertResultType
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
catch|catch
parameter_list|(
name|JXPathException
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
comment|/* 	 * Check if the result is of the specified type 	 */
DECL|method|assertResultType (Exchange exchange, Object result)
specifier|private
name|void
name|assertResultType
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|result
parameter_list|)
block|{
if|if
condition|(
operator|!
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|result
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|JXPathException
argument_list|(
literal|"JXPath result type is "
operator|+
name|result
operator|.
name|getClass
argument_list|()
operator|+
literal|" instead of required type "
operator|+
name|type
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
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
name|expression
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/* 	 * Get a compiled expression instance for better performance 	 */
DECL|method|getJXPathExpression ()
specifier|private
specifier|synchronized
name|CompiledExpression
name|getJXPathExpression
parameter_list|()
block|{
if|if
condition|(
name|compiledExpression
operator|==
literal|null
condition|)
block|{
name|compiledExpression
operator|=
name|JXPathContext
operator|.
name|compile
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
return|return
name|compiledExpression
return|;
block|}
block|}
end_class

end_unit

