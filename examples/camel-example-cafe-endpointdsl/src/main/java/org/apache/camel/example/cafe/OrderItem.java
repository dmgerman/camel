begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cafe
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cafe
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

begin_class
DECL|class|OrderItem
specifier|public
class|class
name|OrderItem
block|{
DECL|field|type
specifier|private
name|DrinkType
name|type
decl_stmt|;
DECL|field|shots
specifier|private
name|int
name|shots
init|=
literal|1
decl_stmt|;
DECL|field|iced
specifier|private
name|boolean
name|iced
decl_stmt|;
DECL|field|order
specifier|private
specifier|final
name|Order
name|order
decl_stmt|;
DECL|method|OrderItem (Order order, DrinkType type, int shots, boolean iced)
specifier|public
name|OrderItem
parameter_list|(
name|Order
name|order
parameter_list|,
name|DrinkType
name|type
parameter_list|,
name|int
name|shots
parameter_list|,
name|boolean
name|iced
parameter_list|)
block|{
name|this
operator|.
name|order
operator|=
name|order
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|shots
operator|=
name|shots
expr_stmt|;
name|this
operator|.
name|iced
operator|=
name|iced
expr_stmt|;
block|}
DECL|method|getOrder ()
specifier|public
name|Order
name|getOrder
parameter_list|()
block|{
return|return
name|this
operator|.
name|order
return|;
block|}
DECL|method|isIced ()
specifier|public
name|boolean
name|isIced
parameter_list|()
block|{
return|return
name|this
operator|.
name|iced
return|;
block|}
DECL|method|getShots ()
specifier|public
name|int
name|getShots
parameter_list|()
block|{
return|return
name|shots
return|;
block|}
DECL|method|getDrinkType ()
specifier|public
name|DrinkType
name|getDrinkType
parameter_list|()
block|{
return|return
name|this
operator|.
name|type
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
operator|(
operator|(
name|this
operator|.
name|iced
operator|)
condition|?
literal|"iced "
else|:
literal|"hot "
operator|)
operator|+
name|this
operator|.
name|shots
operator|+
literal|" shot "
operator|+
name|this
operator|.
name|type
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
name|OrderItem
condition|)
block|{
name|OrderItem
name|that
init|=
operator|(
name|OrderItem
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
name|type
argument_list|,
name|that
operator|.
name|type
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|this
operator|.
name|order
operator|.
name|getNumber
argument_list|()
argument_list|,
name|that
operator|.
name|order
operator|.
name|getNumber
argument_list|()
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|this
operator|.
name|iced
argument_list|,
name|that
operator|.
name|iced
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|this
operator|.
name|shots
argument_list|,
name|that
operator|.
name|shots
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
if|if
condition|(
name|iced
condition|)
block|{
return|return
name|type
operator|.
name|hashCode
argument_list|()
operator|+
name|order
operator|.
name|getNumber
argument_list|()
operator|*
literal|10000
operator|+
name|shots
operator|*
literal|100
return|;
block|}
else|else
block|{
return|return
name|type
operator|.
name|hashCode
argument_list|()
operator|+
name|order
operator|.
name|getNumber
argument_list|()
operator|*
literal|10000
operator|+
name|shots
operator|*
literal|100
operator|+
literal|5
return|;
block|}
block|}
block|}
end_class

end_unit

