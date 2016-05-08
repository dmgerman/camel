begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy
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
name|annotation
operator|.
name|BindyConverter
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
DECL|class|FormattingOptions
specifier|public
class|class
name|FormattingOptions
block|{
DECL|field|pattern
specifier|private
name|String
name|pattern
decl_stmt|;
DECL|field|locale
specifier|private
name|Locale
name|locale
decl_stmt|;
DECL|field|timezone
specifier|private
name|String
name|timezone
decl_stmt|;
DECL|field|precision
specifier|private
name|int
name|precision
decl_stmt|;
DECL|field|rounding
specifier|private
name|String
name|rounding
decl_stmt|;
DECL|field|impliedDecimalSeparator
specifier|private
name|boolean
name|impliedDecimalSeparator
decl_stmt|;
DECL|field|decimalSeparator
specifier|private
name|String
name|decimalSeparator
decl_stmt|;
DECL|field|groupingSeparator
specifier|private
name|String
name|groupingSeparator
decl_stmt|;
DECL|field|clazz
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
decl_stmt|;
DECL|field|bindyConverter
specifier|private
name|BindyConverter
name|bindyConverter
decl_stmt|;
DECL|method|getPattern ()
specifier|public
name|String
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
DECL|method|getLocale ()
specifier|public
name|Locale
name|getLocale
parameter_list|()
block|{
return|return
name|locale
return|;
block|}
DECL|method|getTimezone ()
specifier|public
name|String
name|getTimezone
parameter_list|()
block|{
return|return
name|timezone
return|;
block|}
DECL|method|getPrecision ()
specifier|public
name|int
name|getPrecision
parameter_list|()
block|{
return|return
name|precision
return|;
block|}
DECL|method|getRounding ()
specifier|public
name|String
name|getRounding
parameter_list|()
block|{
return|return
name|rounding
return|;
block|}
DECL|method|isImpliedDecimalSeparator ()
specifier|public
name|boolean
name|isImpliedDecimalSeparator
parameter_list|()
block|{
return|return
name|impliedDecimalSeparator
return|;
block|}
DECL|method|getDecimalSeparator ()
specifier|public
name|String
name|getDecimalSeparator
parameter_list|()
block|{
return|return
name|decimalSeparator
return|;
block|}
DECL|method|getGroupingSeparator ()
specifier|public
name|String
name|getGroupingSeparator
parameter_list|()
block|{
return|return
name|groupingSeparator
return|;
block|}
DECL|method|withPattern (String pattern)
specifier|public
name|FormattingOptions
name|withPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withLocale (String locale)
specifier|public
name|FormattingOptions
name|withLocale
parameter_list|(
name|String
name|locale
parameter_list|)
block|{
name|this
operator|.
name|locale
operator|=
name|getLocale
argument_list|(
name|locale
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withTimezone (String timezone)
specifier|public
name|FormattingOptions
name|withTimezone
parameter_list|(
name|String
name|timezone
parameter_list|)
block|{
name|this
operator|.
name|timezone
operator|=
name|timezone
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withPrecision (int precision)
specifier|public
name|FormattingOptions
name|withPrecision
parameter_list|(
name|int
name|precision
parameter_list|)
block|{
name|this
operator|.
name|precision
operator|=
name|precision
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withRounding (String rounding)
specifier|public
name|FormattingOptions
name|withRounding
parameter_list|(
name|String
name|rounding
parameter_list|)
block|{
name|this
operator|.
name|rounding
operator|=
name|rounding
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withImpliedDecimalSeparator (boolean impliedDecimalSeparator)
specifier|public
name|FormattingOptions
name|withImpliedDecimalSeparator
parameter_list|(
name|boolean
name|impliedDecimalSeparator
parameter_list|)
block|{
name|this
operator|.
name|impliedDecimalSeparator
operator|=
name|impliedDecimalSeparator
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withDecimalSeparator (String decimalSeparator)
specifier|public
name|FormattingOptions
name|withDecimalSeparator
parameter_list|(
name|String
name|decimalSeparator
parameter_list|)
block|{
name|this
operator|.
name|decimalSeparator
operator|=
name|decimalSeparator
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withGroupingSeparator (String groupingSeparator)
specifier|public
name|FormattingOptions
name|withGroupingSeparator
parameter_list|(
name|String
name|groupingSeparator
parameter_list|)
block|{
name|this
operator|.
name|groupingSeparator
operator|=
name|groupingSeparator
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|forClazz (Class<?> clazz)
specifier|public
name|FormattingOptions
name|forClazz
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|this
operator|.
name|clazz
operator|=
name|clazz
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getClazz ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getClazz
parameter_list|()
block|{
return|return
name|clazz
return|;
block|}
DECL|method|getLocale (String locale)
specifier|private
name|Locale
name|getLocale
parameter_list|(
name|String
name|locale
parameter_list|)
block|{
if|if
condition|(
literal|"default"
operator|.
name|equals
argument_list|(
name|locale
argument_list|)
condition|)
block|{
return|return
name|Locale
operator|.
name|getDefault
argument_list|()
return|;
block|}
name|Locale
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|locale
argument_list|)
condition|)
block|{
name|String
index|[]
name|result
init|=
name|locale
operator|.
name|split
argument_list|(
literal|"-"
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|length
operator|<=
literal|2
condition|)
block|{
name|answer
operator|=
name|result
operator|.
name|length
operator|==
literal|1
condition|?
operator|new
name|Locale
argument_list|(
name|result
index|[
literal|0
index|]
argument_list|)
else|:
operator|new
name|Locale
argument_list|(
name|result
index|[
literal|0
index|]
argument_list|,
name|result
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|withBindyConverter (BindyConverter bindyConverter)
specifier|public
name|FormattingOptions
name|withBindyConverter
parameter_list|(
name|BindyConverter
name|bindyConverter
parameter_list|)
block|{
name|this
operator|.
name|bindyConverter
operator|=
name|bindyConverter
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getBindyConverter ()
specifier|public
name|BindyConverter
name|getBindyConverter
parameter_list|()
block|{
return|return
name|bindyConverter
return|;
block|}
block|}
end_class

end_unit

