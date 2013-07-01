begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.mybatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|mybatis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_comment
comment|/**  * Bean that generates and process orders.  */
end_comment

begin_class
DECL|class|OrderService
specifier|public
class|class
name|OrderService
block|{
DECL|field|counter
specifier|private
name|int
name|counter
decl_stmt|;
DECL|field|ran
specifier|private
name|Random
name|ran
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
comment|/**      * Generates a new order      */
DECL|method|generateOrder ()
specifier|public
name|Order
name|generateOrder
parameter_list|()
block|{
name|Order
name|order
init|=
operator|new
name|Order
argument_list|()
decl_stmt|;
name|order
operator|.
name|setId
argument_list|(
name|counter
operator|++
argument_list|)
expr_stmt|;
name|order
operator|.
name|setItem
argument_list|(
name|counter
operator|%
literal|2
operator|==
literal|0
condition|?
literal|"111"
else|:
literal|"222"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setAmount
argument_list|(
name|ran
operator|.
name|nextInt
argument_list|(
literal|10
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
name|order
operator|.
name|setDescription
argument_list|(
name|counter
operator|%
literal|2
operator|==
literal|0
condition|?
literal|"Camel in Action"
else|:
literal|"ActiveMQ in Action"
argument_list|)
expr_stmt|;
return|return
name|order
return|;
block|}
comment|/**      * Processes the order      *      * @param order  the order      * @return the transformed order      */
DECL|method|processOrder (Order order)
specifier|public
name|String
name|processOrder
parameter_list|(
name|Order
name|order
parameter_list|)
block|{
return|return
literal|"Processed order id "
operator|+
name|order
operator|.
name|getId
argument_list|()
operator|+
literal|" item "
operator|+
name|order
operator|.
name|getItem
argument_list|()
operator|+
literal|" of "
operator|+
name|order
operator|.
name|getAmount
argument_list|()
operator|+
literal|" copies of "
operator|+
name|order
operator|.
name|getDescription
argument_list|()
return|;
block|}
block|}
end_class

end_unit

