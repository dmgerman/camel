begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.simple.types
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
operator|.
name|types
package|;
end_package

begin_comment
comment|/**  * Types of binary operators supported  */
end_comment

begin_enum
DECL|enum|BinaryOperatorType
specifier|public
enum|enum
name|BinaryOperatorType
block|{
DECL|enumConstant|EQ
DECL|enumConstant|EQ_IGNORE
DECL|enumConstant|GT
DECL|enumConstant|GTE
DECL|enumConstant|LT
DECL|enumConstant|LTE
DECL|enumConstant|NOT_EQ
DECL|enumConstant|CONTAINS
DECL|enumConstant|NOT_CONTAINS
DECL|enumConstant|REGEX
DECL|enumConstant|NOT_REGEX
name|EQ
block|,
name|EQ_IGNORE
block|,
name|GT
block|,
name|GTE
block|,
name|LT
block|,
name|LTE
block|,
name|NOT_EQ
block|,
name|CONTAINS
block|,
name|NOT_CONTAINS
block|,
name|REGEX
block|,
name|NOT_REGEX
block|,
DECL|enumConstant|IN
DECL|enumConstant|NOT_IN
DECL|enumConstant|IS
DECL|enumConstant|NOT_IS
DECL|enumConstant|RANGE
DECL|enumConstant|NOT_RANGE
DECL|enumConstant|STARTS_WITH
DECL|enumConstant|ENDS_WITH
name|IN
block|,
name|NOT_IN
block|,
name|IS
block|,
name|NOT_IS
block|,
name|RANGE
block|,
name|NOT_RANGE
block|,
name|STARTS_WITH
block|,
name|ENDS_WITH
block|;
DECL|method|asOperator (String text)
specifier|public
specifier|static
name|BinaryOperatorType
name|asOperator
parameter_list|(
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
literal|"=="
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|EQ
return|;
block|}
elseif|else
if|if
condition|(
literal|"=~"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|EQ_IGNORE
return|;
block|}
elseif|else
if|if
condition|(
literal|">"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|GT
return|;
block|}
elseif|else
if|if
condition|(
literal|">="
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|GTE
return|;
block|}
elseif|else
if|if
condition|(
literal|"<"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|LT
return|;
block|}
elseif|else
if|if
condition|(
literal|"<="
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|LTE
return|;
block|}
elseif|else
if|if
condition|(
literal|"!="
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|NOT_EQ
return|;
block|}
elseif|else
if|if
condition|(
literal|"contains"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|CONTAINS
return|;
block|}
elseif|else
if|if
condition|(
literal|"not contains"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|NOT_CONTAINS
return|;
block|}
elseif|else
if|if
condition|(
literal|"regex"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|REGEX
return|;
block|}
elseif|else
if|if
condition|(
literal|"not regex"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|NOT_REGEX
return|;
block|}
elseif|else
if|if
condition|(
literal|"in"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|IN
return|;
block|}
elseif|else
if|if
condition|(
literal|"not in"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|NOT_IN
return|;
block|}
elseif|else
if|if
condition|(
literal|"is"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|IS
return|;
block|}
elseif|else
if|if
condition|(
literal|"not is"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|NOT_IS
return|;
block|}
elseif|else
if|if
condition|(
literal|"range"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|RANGE
return|;
block|}
elseif|else
if|if
condition|(
literal|"not range"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|NOT_RANGE
return|;
block|}
elseif|else
if|if
condition|(
literal|"starts with"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|STARTS_WITH
return|;
block|}
elseif|else
if|if
condition|(
literal|"ends with"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
name|ENDS_WITH
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Operator not supported: "
operator|+
name|text
argument_list|)
throw|;
block|}
DECL|method|getOperatorText (BinaryOperatorType operator)
specifier|public
specifier|static
name|String
name|getOperatorText
parameter_list|(
name|BinaryOperatorType
name|operator
parameter_list|)
block|{
if|if
condition|(
name|operator
operator|==
name|EQ
condition|)
block|{
return|return
literal|"=="
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|EQ_IGNORE
condition|)
block|{
return|return
literal|"=~"
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|GT
condition|)
block|{
return|return
literal|">"
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|GTE
condition|)
block|{
return|return
literal|">="
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|LT
condition|)
block|{
return|return
literal|"<"
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|LTE
condition|)
block|{
return|return
literal|"<="
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|NOT_EQ
condition|)
block|{
return|return
literal|"!="
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|CONTAINS
condition|)
block|{
return|return
literal|"contains"
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|NOT_CONTAINS
condition|)
block|{
return|return
literal|"not contains"
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|REGEX
condition|)
block|{
return|return
literal|"regex"
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|NOT_REGEX
condition|)
block|{
return|return
literal|"not regex"
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|IN
condition|)
block|{
return|return
literal|"in"
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|NOT_IN
condition|)
block|{
return|return
literal|"not in"
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|IS
condition|)
block|{
return|return
literal|"is"
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|NOT_IS
condition|)
block|{
return|return
literal|"not is"
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|RANGE
condition|)
block|{
return|return
literal|"range"
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|NOT_RANGE
condition|)
block|{
return|return
literal|"not range"
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|STARTS_WITH
condition|)
block|{
return|return
literal|"starts with"
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|ENDS_WITH
condition|)
block|{
return|return
literal|"ends with"
return|;
block|}
return|return
literal|""
return|;
block|}
comment|/**      * Parameter types a binary operator supports on the right hand side.      *<ul>      *<li>Literal - Only literals enclosed by single quotes</li>      *<li>LiteralWithFunction - literals which may have embedded functions enclosed by single quotes</li>      *<li>Function - A function</li>      *<li>NumericValue - A numeric value</li>      *<li>BooleanValue - A boolean value</li>      *<li>NullValue - A null value</li>      *</ul>      */
DECL|enum|ParameterType
specifier|public
enum|enum
name|ParameterType
block|{
DECL|enumConstant|Literal
DECL|enumConstant|LiteralWithFunction
DECL|enumConstant|Function
DECL|enumConstant|NumericValue
DECL|enumConstant|BooleanValue
DECL|enumConstant|NullValue
name|Literal
block|,
name|LiteralWithFunction
block|,
name|Function
block|,
name|NumericValue
block|,
name|BooleanValue
block|,
name|NullValue
block|;
DECL|method|isLiteralSupported ()
specifier|public
name|boolean
name|isLiteralSupported
parameter_list|()
block|{
return|return
name|this
operator|==
name|Literal
return|;
block|}
DECL|method|isLiteralWithFunctionSupport ()
specifier|public
name|boolean
name|isLiteralWithFunctionSupport
parameter_list|()
block|{
return|return
name|this
operator|==
name|LiteralWithFunction
return|;
block|}
DECL|method|isFunctionSupport ()
specifier|public
name|boolean
name|isFunctionSupport
parameter_list|()
block|{
return|return
name|this
operator|==
name|Function
return|;
block|}
DECL|method|isNumericValueSupported ()
specifier|public
name|boolean
name|isNumericValueSupported
parameter_list|()
block|{
return|return
name|this
operator|==
name|NumericValue
return|;
block|}
DECL|method|isBooleanValueSupported ()
specifier|public
name|boolean
name|isBooleanValueSupported
parameter_list|()
block|{
return|return
name|this
operator|==
name|BooleanValue
return|;
block|}
DECL|method|isNullValueSupported ()
specifier|public
name|boolean
name|isNullValueSupported
parameter_list|()
block|{
return|return
name|this
operator|==
name|NullValue
return|;
block|}
block|}
comment|/**      * Returns the types of right hand side parameters this operator supports.      *      * @param operator the operator      * @return<tt>null</tt> if accepting all types, otherwise the array of accepted types      */
DECL|method|supportedParameterTypes (BinaryOperatorType operator)
specifier|public
specifier|static
name|ParameterType
index|[]
name|supportedParameterTypes
parameter_list|(
name|BinaryOperatorType
name|operator
parameter_list|)
block|{
if|if
condition|(
name|operator
operator|==
name|EQ
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|EQ_IGNORE
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|GT
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|GTE
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|LT
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|LTE
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|NOT_EQ
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|CONTAINS
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|NOT_CONTAINS
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|REGEX
condition|)
block|{
return|return
operator|new
name|ParameterType
index|[]
block|{
name|ParameterType
operator|.
name|Literal
block|,
name|ParameterType
operator|.
name|Function
block|}
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|NOT_REGEX
condition|)
block|{
return|return
operator|new
name|ParameterType
index|[]
block|{
name|ParameterType
operator|.
name|Literal
block|,
name|ParameterType
operator|.
name|Function
block|}
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|IN
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|NOT_IN
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|IS
condition|)
block|{
return|return
operator|new
name|ParameterType
index|[]
block|{
name|ParameterType
operator|.
name|LiteralWithFunction
block|,
name|ParameterType
operator|.
name|Function
block|}
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|NOT_IS
condition|)
block|{
return|return
operator|new
name|ParameterType
index|[]
block|{
name|ParameterType
operator|.
name|LiteralWithFunction
block|,
name|ParameterType
operator|.
name|Function
block|}
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|RANGE
condition|)
block|{
return|return
operator|new
name|ParameterType
index|[]
block|{
name|ParameterType
operator|.
name|LiteralWithFunction
block|,
name|ParameterType
operator|.
name|Function
block|}
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|NOT_RANGE
condition|)
block|{
return|return
operator|new
name|ParameterType
index|[]
block|{
name|ParameterType
operator|.
name|LiteralWithFunction
block|,
name|ParameterType
operator|.
name|Function
block|}
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|STARTS_WITH
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|ENDS_WITH
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
literal|null
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
name|getOperatorText
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_enum

end_unit

