begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
annotation|@
name|XmlRootElement
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|PurchaseOrder
specifier|public
class|class
name|PurchaseOrder
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|price
specifier|private
name|double
name|price
decl_stmt|;
annotation|@
name|XmlAttribute
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
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|name
operator|.
name|hashCode
argument_list|()
operator|+
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|price
operator|*
literal|100
argument_list|)
operator|+
operator|(
name|int
operator|)
name|Math
operator|.
name|round
argument_list|(
name|amount
operator|*
literal|100
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

