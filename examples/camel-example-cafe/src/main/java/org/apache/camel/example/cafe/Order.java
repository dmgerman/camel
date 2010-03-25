begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|Order
specifier|public
class|class
name|Order
block|{
DECL|field|orderItems
specifier|private
name|List
argument_list|<
name|OrderItem
argument_list|>
name|orderItems
init|=
operator|new
name|ArrayList
argument_list|<
name|OrderItem
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|number
specifier|private
name|int
name|number
decl_stmt|;
DECL|method|Order (int number)
specifier|public
name|Order
parameter_list|(
name|int
name|number
parameter_list|)
block|{
name|this
operator|.
name|number
operator|=
name|number
expr_stmt|;
block|}
DECL|method|addItem (DrinkType drinkType, int shots, boolean iced)
specifier|public
name|void
name|addItem
parameter_list|(
name|DrinkType
name|drinkType
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
name|orderItems
operator|.
name|add
argument_list|(
operator|new
name|OrderItem
argument_list|(
name|this
argument_list|,
name|drinkType
argument_list|,
name|shots
argument_list|,
name|iced
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getNumber ()
specifier|public
name|int
name|getNumber
parameter_list|()
block|{
return|return
name|number
return|;
block|}
DECL|method|getItems ()
specifier|public
name|List
argument_list|<
name|OrderItem
argument_list|>
name|getItems
parameter_list|()
block|{
return|return
name|this
operator|.
name|orderItems
return|;
block|}
block|}
end_class

end_unit

