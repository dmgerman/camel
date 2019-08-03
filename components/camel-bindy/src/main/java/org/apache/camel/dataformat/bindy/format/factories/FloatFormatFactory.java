begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.format.factories
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
operator|.
name|factories
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

begin_import
import|import
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
name|Format
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
name|dataformat
operator|.
name|bindy
operator|.
name|FormattingOptions
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
name|dataformat
operator|.
name|bindy
operator|.
name|format
operator|.
name|AbstractNumberFormat
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|FloatFormatFactory
specifier|public
class|class
name|FloatFormatFactory
extends|extends
name|AbstractFormatFactory
block|{
block|{
name|supportedClasses
operator|.
name|add
parameter_list|(
name|float
operator|.
name|class
parameter_list|)
constructor_decl|;
name|supportedClasses
operator|.
name|add
parameter_list|(
name|Float
operator|.
name|class
parameter_list|)
constructor_decl|;
block|}
annotation|@
name|Override
DECL|method|canBuild (FormattingOptions formattingOptions)
specifier|public
name|boolean
name|canBuild
parameter_list|(
name|FormattingOptions
name|formattingOptions
parameter_list|)
block|{
return|return
name|super
operator|.
name|canBuild
argument_list|(
name|formattingOptions
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|formattingOptions
operator|.
name|getPattern
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|build (FormattingOptions formattingOptions)
specifier|public
name|Format
argument_list|<
name|?
argument_list|>
name|build
parameter_list|(
name|FormattingOptions
name|formattingOptions
parameter_list|)
block|{
return|return
operator|new
name|FloatFormat
argument_list|(
name|formattingOptions
operator|.
name|isImpliedDecimalSeparator
argument_list|()
argument_list|,
name|formattingOptions
operator|.
name|getPrecision
argument_list|()
argument_list|,
name|formattingOptions
operator|.
name|getLocale
argument_list|()
argument_list|)
return|;
block|}
DECL|class|FloatFormat
specifier|private
specifier|static
class|class
name|FloatFormat
extends|extends
name|AbstractNumberFormat
argument_list|<
name|Float
argument_list|>
block|{
DECL|method|FloatFormat (boolean impliedDecimalPosition, int precision, Locale locale)
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
annotation|@
name|Override
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
annotation|@
name|Override
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
block|}
end_class

end_unit

