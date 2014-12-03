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
name|math
operator|.
name|RoundingMode
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DecimalFormat
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
DECL|class|BigDecimalPatternFormat
specifier|public
class|class
name|BigDecimalPatternFormat
extends|extends
name|NumberPatternFormat
argument_list|<
name|BigDecimal
argument_list|>
block|{
DECL|method|BigDecimalPatternFormat ()
specifier|public
name|BigDecimalPatternFormat
parameter_list|()
block|{     }
DECL|method|BigDecimalPatternFormat (String pattern, Locale locale, int precision, String rounding, String decimalSeparator, String groupingSeparator)
specifier|public
name|BigDecimalPatternFormat
parameter_list|(
name|String
name|pattern
parameter_list|,
name|Locale
name|locale
parameter_list|,
name|int
name|precision
parameter_list|,
name|String
name|rounding
parameter_list|,
name|String
name|decimalSeparator
parameter_list|,
name|String
name|groupingSeparator
parameter_list|)
block|{
name|super
argument_list|(
name|pattern
argument_list|,
name|locale
argument_list|,
name|precision
argument_list|,
name|rounding
argument_list|,
name|decimalSeparator
argument_list|,
name|groupingSeparator
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|parse (String string)
specifier|public
name|BigDecimal
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
name|DecimalFormat
name|df
init|=
operator|(
name|DecimalFormat
operator|)
name|getNumberFormat
argument_list|()
decl_stmt|;
name|df
operator|.
name|setParseBigDecimal
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|BigDecimal
name|bd
init|=
operator|(
name|BigDecimal
operator|)
name|df
operator|.
name|parse
argument_list|(
name|string
operator|.
name|trim
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|super
operator|.
name|getPrecision
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|bd
operator|=
name|bd
operator|.
name|setScale
argument_list|(
name|super
operator|.
name|getPrecision
argument_list|()
argument_list|,
name|RoundingMode
operator|.
name|valueOf
argument_list|(
name|super
operator|.
name|getRounding
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|bd
return|;
block|}
else|else
block|{
return|return
operator|new
name|BigDecimal
argument_list|(
name|string
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

