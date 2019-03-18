begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.myconverter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|myconverter
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
name|Converter
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
name|TypeConverter
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
name|converter
operator|.
name|PurchaseOrder
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
name|spi
operator|.
name|TypeConverterAware
import|;
end_import

begin_class
annotation|@
name|Converter
DECL|class|PurchaseOrderConverter
specifier|public
class|class
name|PurchaseOrderConverter
implements|implements
name|TypeConverterAware
block|{
DECL|field|converter
specifier|private
name|TypeConverter
name|converter
decl_stmt|;
DECL|method|setTypeConverter (TypeConverter parentTypeConverter)
specifier|public
name|void
name|setTypeConverter
parameter_list|(
name|TypeConverter
name|parentTypeConverter
parameter_list|)
block|{
name|this
operator|.
name|converter
operator|=
name|parentTypeConverter
expr_stmt|;
block|}
annotation|@
name|Converter
DECL|method|toPurchaseOrder (byte[] data)
specifier|public
name|PurchaseOrder
name|toPurchaseOrder
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
name|String
name|s
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|data
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
operator|||
name|s
operator|.
name|length
argument_list|()
operator|<
literal|30
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"data is invalid"
argument_list|)
throw|;
block|}
name|s
operator|=
name|s
operator|.
name|replaceAll
argument_list|(
literal|"##START##"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|s
operator|=
name|s
operator|.
name|replaceAll
argument_list|(
literal|"##END##"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|9
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|String
name|s2
init|=
name|s
operator|.
name|substring
argument_list|(
literal|10
argument_list|,
literal|19
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|String
name|s3
init|=
name|s
operator|.
name|substring
argument_list|(
literal|20
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|BigDecimal
name|price
init|=
operator|new
name|BigDecimal
argument_list|(
name|s2
argument_list|)
decl_stmt|;
name|price
operator|=
name|price
operator|.
name|setScale
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|Integer
name|amount
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|s3
argument_list|)
decl_stmt|;
name|PurchaseOrder
name|order
init|=
operator|new
name|PurchaseOrder
argument_list|(
name|name
argument_list|,
name|price
argument_list|,
name|amount
argument_list|)
decl_stmt|;
return|return
name|order
return|;
block|}
block|}
end_class

end_unit

