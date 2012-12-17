begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.format
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|format
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_class
DECL|class|FloatFormat
specifier|public
class|class
name|FloatFormat
extends|extends
name|AbstractNumberFormat
argument_list|<
name|Float
argument_list|>
block|{
DECL|method|FloatFormat (boolean impliedDecimalPosition, int precision, Locale locale)
specifier|public
name|FloatFormat
parameter_list|(
name|boolean
name|impliedDecimalPosition
parameter_list|,
name|int
name|precision
parameter_list|,
name|Locale
name|locale
parameter_list|)
block|{
name|super
argument_list|(
name|impliedDecimalPosition
argument_list|,
name|precision
argument_list|,
name|locale
argument_list|)
expr_stmt|;
block|}
DECL|method|format (Float object)
specifier|public
name|String
name|format
parameter_list|(
name|Float
name|object
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|!
name|super
operator|.
name|hasImpliedDecimalPosition
argument_list|()
condition|?
name|super
operator|.
name|getFormat
argument_list|()
operator|.
name|format
argument_list|(
name|object
argument_list|)
else|:
name|super
operator|.
name|getFormat
argument_list|()
operator|.
name|format
argument_list|(
name|object
operator|*
name|super
operator|.
name|getMultiplier
argument_list|()
argument_list|)
return|;
block|}
DECL|method|parse (String string)
specifier|public
name|Float
name|parse
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|Exception
block|{
name|Float
name|value
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|super
operator|.
name|hasImpliedDecimalPosition
argument_list|()
condition|)
block|{
name|value
operator|=
name|Float
operator|.
name|parseFloat
argument_list|(
name|string
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|BigDecimal
name|tmp
init|=
operator|new
name|BigDecimal
argument_list|(
name|string
operator|.
name|trim
argument_list|()
argument_list|)
decl_stmt|;
name|BigDecimal
name|div
init|=
name|BigDecimal
operator|.
name|valueOf
argument_list|(
name|super
operator|.
name|getMultiplier
argument_list|()
argument_list|)
decl_stmt|;
name|value
operator|=
name|tmp
operator|.
name|divide
argument_list|(
name|div
argument_list|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
block|}
end_class

end_unit

