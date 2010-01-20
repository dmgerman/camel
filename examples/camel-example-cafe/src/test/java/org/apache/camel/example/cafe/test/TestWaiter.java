begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cafe.test
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
operator|.
name|test
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|example
operator|.
name|cafe
operator|.
name|Delivery
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
name|example
operator|.
name|cafe
operator|.
name|Drink
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
name|example
operator|.
name|cafe
operator|.
name|stuff
operator|.
name|Waiter
import|;
end_import

begin_class
DECL|class|TestWaiter
specifier|public
class|class
name|TestWaiter
extends|extends
name|Waiter
block|{
DECL|field|expectDrinks
specifier|protected
name|List
argument_list|<
name|Drink
argument_list|>
name|expectDrinks
decl_stmt|;
DECL|field|deliveredDrinks
specifier|protected
name|List
argument_list|<
name|Drink
argument_list|>
name|deliveredDrinks
decl_stmt|;
DECL|method|setVerfiyDrinks (List<Drink> drinks)
specifier|public
name|void
name|setVerfiyDrinks
parameter_list|(
name|List
argument_list|<
name|Drink
argument_list|>
name|drinks
parameter_list|)
block|{
name|this
operator|.
name|expectDrinks
operator|=
name|drinks
expr_stmt|;
block|}
DECL|method|deliverCafes (Delivery delivery)
specifier|public
name|void
name|deliverCafes
parameter_list|(
name|Delivery
name|delivery
parameter_list|)
block|{
name|super
operator|.
name|deliverCafes
argument_list|(
name|delivery
argument_list|)
expr_stmt|;
name|deliveredDrinks
operator|=
name|delivery
operator|.
name|getDeliveredDrinks
argument_list|()
expr_stmt|;
block|}
DECL|method|verifyDrinks ()
specifier|public
name|void
name|verifyDrinks
parameter_list|()
block|{
if|if
condition|(
name|expectDrinks
operator|.
name|size
argument_list|()
operator|!=
name|deliveredDrinks
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"Can't get expect size of drinks"
argument_list|)
throw|;
block|}
for|for
control|(
name|Drink
name|drink
range|:
name|expectDrinks
control|)
block|{
if|if
condition|(
operator|!
name|deliveredDrinks
operator|.
name|contains
argument_list|(
name|drink
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"Can't find drink "
operator|+
name|drink
operator|+
literal|" in the deliveredDrinks"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

