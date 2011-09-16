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
comment|/**  * The different token types used by the simple parser.  */
end_comment

begin_class
DECL|class|SimpleTokenType
specifier|public
class|class
name|SimpleTokenType
block|{
DECL|field|type
specifier|private
specifier|final
name|TokenType
name|type
decl_stmt|;
DECL|field|value
specifier|private
specifier|final
name|String
name|value
decl_stmt|;
DECL|method|SimpleTokenType (TokenType type, String value)
specifier|public
name|SimpleTokenType
parameter_list|(
name|TokenType
name|type
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the type of this token      *      * @return the type      */
DECL|method|getType ()
specifier|public
name|TokenType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * Gets the input value in this token      *      * @return the value      */
DECL|method|getValue ()
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**      * Whether the type is whitespace      */
DECL|method|isWhitespace ()
specifier|public
name|boolean
name|isWhitespace
parameter_list|()
block|{
return|return
name|type
operator|==
name|TokenType
operator|.
name|whiteSpace
return|;
block|}
comment|/**      * Whether the type is eol      */
DECL|method|isEol ()
specifier|public
name|boolean
name|isEol
parameter_list|()
block|{
return|return
name|type
operator|==
name|TokenType
operator|.
name|eol
return|;
block|}
comment|/**      * Whether the type is single quote      */
DECL|method|isSingleQuote ()
specifier|public
name|boolean
name|isSingleQuote
parameter_list|()
block|{
return|return
name|type
operator|==
name|TokenType
operator|.
name|singleQuote
return|;
block|}
comment|/**      * Whether the type is double quote      */
DECL|method|isDoubleQuote ()
specifier|public
name|boolean
name|isDoubleQuote
parameter_list|()
block|{
return|return
name|type
operator|==
name|TokenType
operator|.
name|doubleQuote
return|;
block|}
comment|/**      * Whether the type is a function start      */
DECL|method|isFunctionStart ()
specifier|public
name|boolean
name|isFunctionStart
parameter_list|()
block|{
return|return
name|type
operator|==
name|TokenType
operator|.
name|functionStart
return|;
block|}
comment|/**      * Whether the type is a function end      */
DECL|method|isFunctionEnd ()
specifier|public
name|boolean
name|isFunctionEnd
parameter_list|()
block|{
return|return
name|type
operator|==
name|TokenType
operator|.
name|functionEnd
return|;
block|}
comment|/**      * Whether the type is binary operator      */
DECL|method|isBinary ()
specifier|public
name|boolean
name|isBinary
parameter_list|()
block|{
return|return
name|type
operator|==
name|TokenType
operator|.
name|binaryOperator
return|;
block|}
comment|/**      * Whether the type is unary operator      */
DECL|method|isUnary ()
specifier|public
name|boolean
name|isUnary
parameter_list|()
block|{
return|return
name|type
operator|==
name|TokenType
operator|.
name|unaryOperator
return|;
block|}
comment|/**      * Whether the type is logical operator      */
DECL|method|isLogical ()
specifier|public
name|boolean
name|isLogical
parameter_list|()
block|{
return|return
name|type
operator|==
name|TokenType
operator|.
name|logicalOperator
return|;
block|}
comment|/**      * Whether the type is a null value      */
DECL|method|isNullValue ()
specifier|public
name|boolean
name|isNullValue
parameter_list|()
block|{
return|return
name|type
operator|==
name|TokenType
operator|.
name|nullValue
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
name|value
return|;
block|}
block|}
end_class

end_unit

