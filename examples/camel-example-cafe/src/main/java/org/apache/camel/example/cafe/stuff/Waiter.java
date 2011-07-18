begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cafe.stuff
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
name|stuff
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

begin_class
DECL|class|Waiter
specifier|public
class|class
name|Waiter
block|{
DECL|method|prepareDelivery (List<Drink> drinks)
specifier|public
name|Delivery
name|prepareDelivery
parameter_list|(
name|List
argument_list|<
name|Drink
argument_list|>
name|drinks
parameter_list|)
block|{
return|return
operator|new
name|Delivery
argument_list|(
name|drinks
argument_list|)
return|;
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|delivery
argument_list|)
expr_stmt|;
block|}
DECL|method|checkOrder (Drink drink)
specifier|public
name|int
name|checkOrder
parameter_list|(
name|Drink
name|drink
parameter_list|)
block|{
return|return
name|drink
operator|.
name|getOrderNumber
argument_list|()
return|;
block|}
block|}
end_class

end_unit

