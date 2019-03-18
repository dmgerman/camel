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
name|NumberPatternFormat
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
DECL|class|FloatPatternFormatFactory
specifier|public
class|class
name|FloatPatternFormatFactory
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
name|isNotEmpty
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
name|FloatPatternFormat
argument_list|(
name|formattingOptions
operator|.
name|getPattern
argument_list|()
argument_list|,
name|formattingOptions
operator|.
name|getLocale
argument_list|()
argument_list|)
return|;
block|}
DECL|class|FloatPatternFormat
specifier|private
specifier|static
class|class
name|FloatPatternFormat
extends|extends
name|NumberPatternFormat
argument_list|<
name|Float
argument_list|>
block|{
DECL|method|FloatPatternFormat (String pattern, Locale locale)
name|FloatPatternFormat
parameter_list|(
name|String
name|pattern
parameter_list|,
name|Locale
name|locale
parameter_list|)
block|{
name|super
argument_list|(
name|pattern
argument_list|,
name|locale
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|getNumberFormat
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getNumberFormat
argument_list|()
operator|.
name|parse
argument_list|(
name|string
argument_list|)
operator|.
name|floatValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|Float
operator|.
name|valueOf
argument_list|(
name|string
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

