begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
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

begin_class
DECL|class|PurchaseOrder
specifier|public
class|class
name|PurchaseOrder
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|price
specifier|private
specifier|final
name|BigDecimal
name|price
decl_stmt|;
DECL|field|amount
specifier|private
specifier|final
name|int
name|amount
decl_stmt|;
DECL|method|PurchaseOrder (String name, BigDecimal price, int amount)
specifier|public
name|PurchaseOrder
parameter_list|(
name|String
name|name
parameter_list|,
name|BigDecimal
name|price
parameter_list|,
name|int
name|amount
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|price
operator|=
name|price
expr_stmt|;
name|this
operator|.
name|amount
operator|=
name|amount
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getPrice ()
specifier|public
name|BigDecimal
name|getPrice
parameter_list|()
block|{
return|return
name|price
return|;
block|}
DECL|method|getAmount ()
specifier|public
name|int
name|getAmount
parameter_list|()
block|{
return|return
name|amount
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
literal|"Ordering "
operator|+
name|amount
operator|+
literal|" of "
operator|+
name|name
operator|+
literal|" at total "
operator|+
name|price
return|;
block|}
block|}
end_class

end_unit

