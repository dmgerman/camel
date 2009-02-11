begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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

begin_comment
comment|/**  * A collection of helper methods for working with expressions.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ExpressionHelper
specifier|public
specifier|final
class|class
name|ExpressionHelper
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|ExpressionHelper ()
specifier|private
name|ExpressionHelper
parameter_list|()
block|{     }
comment|/**      * Evaluates the given expression on the exchange as a String value      *      * @param expression the expression to evaluate      * @param exchange the exchange to use to evaluate the expression      * @return the result of the evaluation as a string.      */
DECL|method|evaluateAsString (Expression expression, Exchange exchange)
specifier|public
specifier|static
name|String
name|evaluateAsString
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|evaluateAsType
argument_list|(
name|expression
argument_list|,
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Evaluates the given expression on the exchange, converting the result to      * the given type      *      * @param expression the expression to evaluate      * @param exchange the exchange to use to evaluate the expression      * @param resultType the type of the result that is required      * @return the result of the evaluation as the specified type.      */
DECL|method|evaluateAsType (Expression expression, Exchange exchange, Class<T> resultType)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|evaluateAsType
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|resultType
parameter_list|)
block|{
return|return
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|resultType
argument_list|)
return|;
block|}
block|}
end_class

end_unit

