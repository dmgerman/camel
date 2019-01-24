begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
operator|.
name|validator
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
name|CamelContext
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
name|impl
operator|.
name|validator
operator|.
name|ProcessorValidator
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
name|validator
operator|.
name|PredicateValidatorDefinition
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
name|validator
operator|.
name|ValidatorDefinition
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
name|processor
operator|.
name|validation
operator|.
name|PredicateValidatingProcessor
import|;
end_import

begin_class
DECL|class|PredicateValidatorReifier
class|class
name|PredicateValidatorReifier
extends|extends
name|ValidatorReifier
argument_list|<
name|PredicateValidatorDefinition
argument_list|>
block|{
DECL|method|PredicateValidatorReifier (ValidatorDefinition definition)
name|PredicateValidatorReifier
parameter_list|(
name|ValidatorDefinition
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|PredicateValidatorDefinition
operator|)
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doCreateValidator (CamelContext context)
specifier|protected
name|Validator
name|doCreateValidator
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|Predicate
name|pred
init|=
name|definition
operator|.
name|getExpression
argument_list|()
operator|.
name|createPredicate
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|PredicateValidatingProcessor
name|processor
init|=
operator|new
name|PredicateValidatingProcessor
argument_list|(
name|pred
argument_list|)
decl_stmt|;
return|return
operator|new
name|ProcessorValidator
argument_list|(
name|context
argument_list|)
operator|.
name|setProcessor
argument_list|(
name|processor
argument_list|)
operator|.
name|setType
argument_list|(
name|definition
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

