begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Types of logical operators supported  */
end_comment

begin_enum
DECL|enum|LogicalOperatorType
specifier|public
enum|enum
name|LogicalOperatorType
block|{
DECL|enumConstant|AND
DECL|enumConstant|OR
name|AND
block|,
name|OR
block|;
DECL|method|asOperator (String text)
specifier|public
specifier|static
name|LogicalOperatorType
name|asOperator
parameter_list|(
name|String
name|text
parameter_list|)
block|{
switch|switch
condition|(
name|text
condition|)
block|{
case|case
literal|"&&"
case|:
return|return
name|AND
return|;
case|case
literal|"||"
case|:
return|return
name|OR
return|;
default|default:
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
block|}
DECL|method|getOperatorText (LogicalOperatorType operator)
specifier|public
name|String
name|getOperatorText
parameter_list|(
name|LogicalOperatorType
name|operator
parameter_list|)
block|{
switch|switch
condition|(
name|operator
condition|)
block|{
case|case
name|AND
case|:
return|return
literal|"&&"
return|;
case|case
name|OR
case|:
return|return
literal|"||"
return|;
default|default:
return|return
literal|""
return|;
block|}
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

