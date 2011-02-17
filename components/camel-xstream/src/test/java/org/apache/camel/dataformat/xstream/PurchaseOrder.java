begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.xstream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|xstream
package|;
end_package

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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|PurchaseOrder
specifier|public
class|class
name|PurchaseOrder
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|price
specifier|private
name|double
name|price
decl_stmt|;
DECL|field|amount
specifier|private
name|double
name|amount
decl_stmt|;
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"PurchaseOrder[name: "
operator|+
name|name
operator|+
literal|" amount: "
operator|+
name|amount
operator|+
literal|" price: "
operator|+
name|price
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|PurchaseOrder
condition|)
block|{
name|PurchaseOrder
name|that
init|=
operator|(
name|PurchaseOrder
operator|)
name|o
decl_stmt|;
return|return
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|this
operator|.
name|name
argument_list|,
name|that
operator|.
name|name
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|this
operator|.
name|amount
argument_list|,
name|that
operator|.
name|amount
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|this
operator|.
name|price
argument_list|,
name|that
operator|.
name|price
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
call|(
name|int
call|)
argument_list|(
name|name
operator|.
name|hashCode
argument_list|()
operator|+
operator|(
name|price
operator|*
literal|100
operator|)
operator|+
operator|(
name|amount
operator|*
literal|100
operator|)
argument_list|)
return|;
block|}
DECL|method|getAmount ()
specifier|public
name|double
name|getAmount
parameter_list|()
block|{
return|return
name|amount
return|;
block|}
DECL|method|setAmount (double amount)
specifier|public
name|void
name|setAmount
parameter_list|(
name|double
name|amount
parameter_list|)
block|{
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
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getPrice ()
specifier|public
name|double
name|getPrice
parameter_list|()
block|{
return|return
name|price
return|;
block|}
DECL|method|setPrice (double price)
specifier|public
name|void
name|setPrice
parameter_list|(
name|double
name|price
parameter_list|)
block|{
name|this
operator|.
name|price
operator|=
name|price
expr_stmt|;
block|}
block|}
end_class

end_unit

