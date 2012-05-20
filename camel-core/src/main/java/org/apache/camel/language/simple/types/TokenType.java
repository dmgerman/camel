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
comment|/**  * Classifications of known token types.  */
end_comment

begin_enum
DECL|enum|TokenType
specifier|public
enum|enum
name|TokenType
block|{
DECL|enumConstant|whiteSpace
DECL|enumConstant|character
name|whiteSpace
block|,
name|character
block|,
DECL|enumConstant|booleanValue
DECL|enumConstant|numericValue
DECL|enumConstant|nullValue
name|booleanValue
block|,
name|numericValue
block|,
name|nullValue
block|,
DECL|enumConstant|singleQuote
DECL|enumConstant|doubleQuote
name|singleQuote
block|,
name|doubleQuote
block|,
DECL|enumConstant|escape
name|escape
block|,
DECL|enumConstant|functionStart
DECL|enumConstant|functionEnd
name|functionStart
block|,
name|functionEnd
block|,
DECL|enumConstant|binaryOperator
DECL|enumConstant|unaryOperator
DECL|enumConstant|logicalOperator
name|binaryOperator
block|,
name|unaryOperator
block|,
name|logicalOperator
block|,
DECL|enumConstant|eol
name|eol
block|}
end_enum

end_unit

