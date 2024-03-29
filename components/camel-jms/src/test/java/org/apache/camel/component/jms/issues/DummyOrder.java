begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|issues
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

begin_comment
comment|/**  * Model for unit test.  */
end_comment

begin_class
DECL|class|DummyOrder
specifier|public
class|class
name|DummyOrder
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
DECL|field|orderId
specifier|private
name|long
name|orderId
decl_stmt|;
DECL|field|itemId
specifier|private
name|long
name|itemId
decl_stmt|;
DECL|field|quantity
specifier|private
name|int
name|quantity
decl_stmt|;
DECL|method|getOrderId ()
specifier|public
name|long
name|getOrderId
parameter_list|()
block|{
return|return
name|orderId
return|;
block|}
DECL|method|setOrderId (long orderId)
specifier|public
name|void
name|setOrderId
parameter_list|(
name|long
name|orderId
parameter_list|)
block|{
name|this
operator|.
name|orderId
operator|=
name|orderId
expr_stmt|;
block|}
DECL|method|getItemId ()
specifier|public
name|long
name|getItemId
parameter_list|()
block|{
return|return
name|itemId
return|;
block|}
DECL|method|setItemId (long itemId)
specifier|public
name|void
name|setItemId
parameter_list|(
name|long
name|itemId
parameter_list|)
block|{
name|this
operator|.
name|itemId
operator|=
name|itemId
expr_stmt|;
block|}
DECL|method|getQuantity ()
specifier|public
name|int
name|getQuantity
parameter_list|()
block|{
return|return
name|quantity
return|;
block|}
DECL|method|setQuantity (int quantity)
specifier|public
name|void
name|setQuantity
parameter_list|(
name|int
name|quantity
parameter_list|)
block|{
name|this
operator|.
name|quantity
operator|=
name|quantity
expr_stmt|;
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
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|DummyOrder
name|that
init|=
operator|(
name|DummyOrder
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|itemId
operator|!=
name|that
operator|.
name|itemId
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|orderId
operator|!=
name|that
operator|.
name|orderId
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|quantity
operator|!=
name|that
operator|.
name|quantity
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
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
name|int
name|result
decl_stmt|;
name|result
operator|=
call|(
name|int
call|)
argument_list|(
name|orderId
operator|^
operator|(
name|orderId
operator|>>>
literal|32
operator|)
argument_list|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
call|(
name|int
call|)
argument_list|(
name|itemId
operator|^
operator|(
name|itemId
operator|>>>
literal|32
operator|)
argument_list|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|quantity
expr_stmt|;
return|return
name|result
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
literal|"DummyOrder{"
operator|+
literal|"orderId="
operator|+
name|orderId
operator|+
literal|", itemId="
operator|+
name|itemId
operator|+
literal|", quantity="
operator|+
name|quantity
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

