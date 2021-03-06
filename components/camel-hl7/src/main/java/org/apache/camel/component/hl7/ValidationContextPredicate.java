begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hl7
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hl7
package|;
end_package

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|hl7v2
operator|.
name|HL7Exception
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|hl7v2
operator|.
name|HapiContext
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|hl7v2
operator|.
name|model
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|hl7v2
operator|.
name|validation
operator|.
name|MessageValidator
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|hl7v2
operator|.
name|validation
operator|.
name|ValidationContext
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
name|RuntimeCamelException
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

begin_class
DECL|class|ValidationContextPredicate
specifier|public
class|class
name|ValidationContextPredicate
implements|implements
name|Predicate
block|{
DECL|field|validatorExpression
specifier|private
name|Expression
name|validatorExpression
decl_stmt|;
DECL|method|ValidationContextPredicate ()
specifier|public
name|ValidationContextPredicate
parameter_list|()
block|{
name|this
argument_list|(
operator|(
name|Expression
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|ValidationContextPredicate (HapiContext hapiContext)
specifier|public
name|ValidationContextPredicate
parameter_list|(
name|HapiContext
name|hapiContext
parameter_list|)
block|{
name|this
argument_list|(
name|hapiContext
operator|.
name|getValidationContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|ValidationContextPredicate (ValidationContext validationContext)
specifier|public
name|ValidationContextPredicate
parameter_list|(
name|ValidationContext
name|validationContext
parameter_list|)
block|{
name|this
argument_list|(
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|validationContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|ValidationContextPredicate (Expression expression)
specifier|public
name|ValidationContextPredicate
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|validatorExpression
operator|=
name|expression
expr_stmt|;
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
try|try
block|{
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|ValidationContext
name|context
init|=
name|validatorExpression
operator|!=
literal|null
condition|?
name|validatorExpression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|ValidationContext
operator|.
name|class
argument_list|)
else|:
name|dynamicValidationContext
argument_list|(
name|message
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HL7Constants
operator|.
name|HL7_CONTEXT
argument_list|,
name|HapiContext
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|MessageValidator
name|validator
init|=
operator|new
name|MessageValidator
argument_list|(
name|context
argument_list|,
literal|false
argument_list|)
decl_stmt|;
return|return
name|validator
operator|.
name|validate
argument_list|(
name|message
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|HL7Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|dynamicValidationContext (Message message, HapiContext hapiContext)
specifier|private
name|ValidationContext
name|dynamicValidationContext
parameter_list|(
name|Message
name|message
parameter_list|,
name|HapiContext
name|hapiContext
parameter_list|)
block|{
return|return
name|hapiContext
operator|!=
literal|null
condition|?
name|hapiContext
operator|.
name|getValidationContext
argument_list|()
else|:
name|message
operator|.
name|getParser
argument_list|()
operator|.
name|getHapiContext
argument_list|()
operator|.
name|getValidationContext
argument_list|()
return|;
block|}
block|}
end_class

end_unit

