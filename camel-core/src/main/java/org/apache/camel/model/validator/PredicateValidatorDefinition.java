begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|validator
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElementRef
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
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
name|model
operator|.
name|ExpressionNodeHelper
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
name|spi
operator|.
name|Metadata
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
name|Validator
import|;
end_import

begin_comment
comment|/**  * Represents a predicate {@link Validator} which leverages expression or predicates to  * perform content validation. A {@link org.apache.camel.impl.validator.ProcessorValidator}  * will be created internally with a {@link org.apache.camel.processor.validation.PredicateValidatingProcessor}  * which validates the message according to specified expression/predicates.  *   * {@see ValidatorDefinition}  * {@see Validator}  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"validation"
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"predicateValidator"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|PredicateValidatorDefinition
specifier|public
class|class
name|PredicateValidatorDefinition
extends|extends
name|ValidatorDefinition
block|{
annotation|@
name|XmlElementRef
DECL|field|expression
specifier|private
name|ExpressionDefinition
name|expression
decl_stmt|;
DECL|method|getExpression ()
specifier|public
name|ExpressionDefinition
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
DECL|method|setExpression (ExpressionDefinition expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
comment|// favour using the helper to set the expression as it can unwrap some unwanted builders when using Java DSL
if|if
condition|(
name|expression
operator|instanceof
name|Expression
condition|)
block|{
name|this
operator|.
name|expression
operator|=
name|ExpressionNodeHelper
operator|.
name|toExpressionDefinition
argument_list|(
operator|(
name|Expression
operator|)
name|expression
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expression
operator|instanceof
name|Predicate
condition|)
block|{
name|this
operator|.
name|expression
operator|=
name|ExpressionNodeHelper
operator|.
name|toExpressionDefinition
argument_list|(
operator|(
name|Predicate
operator|)
name|expression
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

