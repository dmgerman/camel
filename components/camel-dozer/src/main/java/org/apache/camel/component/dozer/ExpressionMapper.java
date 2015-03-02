begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dozer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dozer
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
name|spi
operator|.
name|Language
import|;
end_import

begin_comment
comment|/**  * Provides support for mapping a Camel expression to a target field in a   * mapping.  Expressions have the following format:  *<br><br>  * [language]:[expression]  *<br><br>  */
end_comment

begin_class
DECL|class|ExpressionMapper
specifier|public
class|class
name|ExpressionMapper
extends|extends
name|BaseConverter
block|{
DECL|field|currentExchange
specifier|private
name|ThreadLocal
argument_list|<
name|Exchange
argument_list|>
name|currentExchange
init|=
operator|new
name|ThreadLocal
argument_list|<
name|Exchange
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|convert (Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass)
specifier|public
name|Object
name|convert
parameter_list|(
name|Object
name|existingDestinationFieldValue
parameter_list|,
name|Object
name|sourceFieldValue
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|destinationClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|sourceClass
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|currentExchange
operator|.
name|get
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Current exchange has not been set for ExpressionMapper"
argument_list|)
throw|;
block|}
comment|// Resolve the language being used for this expression and evaluate
name|Exchange
name|exchange
init|=
name|currentExchange
operator|.
name|get
argument_list|()
decl_stmt|;
name|Language
name|expLang
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
name|getLanguagePart
argument_list|()
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|expLang
operator|.
name|createExpression
argument_list|(
name|getExpressionPart
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|destinationClass
argument_list|)
return|;
block|}
finally|finally
block|{
name|done
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Used as the source field for Dozer mappings.       */
DECL|method|getExpression ()
specifier|public
name|String
name|getExpression
parameter_list|()
block|{
return|return
name|getParameter
argument_list|()
return|;
block|}
comment|/**      * The actual expression, without the language prefix.      */
DECL|method|getExpressionPart ()
specifier|public
name|String
name|getExpressionPart
parameter_list|()
block|{
return|return
name|getParameter
argument_list|()
operator|.
name|substring
argument_list|(
name|getParameter
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
operator|+
literal|1
argument_list|)
return|;
block|}
comment|/**      * The expression language used for this mapping.      */
DECL|method|getLanguagePart ()
specifier|public
name|String
name|getLanguagePart
parameter_list|()
block|{
return|return
name|getParameter
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|getParameter
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Sets the Camel exchange reference for this mapping.  The exchange       * reference is stored in a thread-local which is cleaned up after the       * mapping has been performed via the done() method.      * @param exchange      */
DECL|method|setCurrentExchange (Exchange exchange)
specifier|public
name|void
name|setCurrentExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|currentExchange
operator|.
name|set
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|done ()
specifier|public
name|void
name|done
parameter_list|()
block|{
name|super
operator|.
name|done
argument_list|()
expr_stmt|;
name|currentExchange
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

