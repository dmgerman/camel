begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.model.simple.oneclassmath
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
name|model
operator|.
name|simple
operator|.
name|oneclassmath
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
name|CsvRecord
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
name|DataField
import|;
end_import

begin_class
annotation|@
name|CsvRecord
argument_list|(
name|separator
operator|=
literal|","
argument_list|)
DECL|class|Math
specifier|public
class|class
name|Math
block|{
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|1
argument_list|,
name|pattern
operator|=
literal|"00"
argument_list|)
DECL|field|intAmount
specifier|private
name|Integer
name|intAmount
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|2
argument_list|,
name|precision
operator|=
literal|2
argument_list|)
comment|/*        Pattern is not yet supported by BigDecimal.        FormatFactory class -->                 } else if (clazz == BigDecimal.class) {             return new BigDecimalFormat(impliedDecimalSeparator, precision, getLocale(locale));          So we should remove it from the model pattern = "00.00"       */
DECL|field|bigDecimal
specifier|private
name|BigDecimal
name|bigDecimal
decl_stmt|;
DECL|method|getIntAmount ()
specifier|public
name|Integer
name|getIntAmount
parameter_list|()
block|{
return|return
name|intAmount
return|;
block|}
DECL|method|setIntAmount (Integer intAmount)
specifier|public
name|void
name|setIntAmount
parameter_list|(
name|Integer
name|intAmount
parameter_list|)
block|{
name|this
operator|.
name|intAmount
operator|=
name|intAmount
expr_stmt|;
block|}
DECL|method|getBigDecimal ()
specifier|public
name|BigDecimal
name|getBigDecimal
parameter_list|()
block|{
return|return
name|bigDecimal
return|;
block|}
DECL|method|setBigDecimal (BigDecimal bigDecimal)
specifier|public
name|void
name|setBigDecimal
parameter_list|(
name|BigDecimal
name|bigDecimal
parameter_list|)
block|{
name|this
operator|.
name|bigDecimal
operator|=
name|bigDecimal
expr_stmt|;
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
literal|"intAmount : "
operator|+
name|this
operator|.
name|intAmount
operator|+
literal|", "
operator|+
literal|"bigDecimal : "
operator|+
name|this
operator|.
name|bigDecimal
return|;
block|}
block|}
end_class

end_unit

