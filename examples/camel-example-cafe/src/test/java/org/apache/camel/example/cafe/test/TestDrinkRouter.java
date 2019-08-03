begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|OrderItem
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
name|DrinkRouter
import|;
end_import

begin_class
DECL|class|TestDrinkRouter
specifier|public
class|class
name|TestDrinkRouter
extends|extends
name|DrinkRouter
block|{
DECL|field|testModel
specifier|private
name|boolean
name|testModel
init|=
literal|true
decl_stmt|;
DECL|method|setTestModel (boolean testModel)
specifier|public
name|void
name|setTestModel
parameter_list|(
name|boolean
name|testModel
parameter_list|)
block|{
name|this
operator|.
name|testModel
operator|=
name|testModel
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resolveOrderItemChannel (OrderItem orderItem)
specifier|public
name|String
name|resolveOrderItemChannel
parameter_list|(
name|OrderItem
name|orderItem
parameter_list|)
block|{
if|if
condition|(
name|testModel
condition|)
block|{
return|return
operator|(
name|orderItem
operator|.
name|isIced
argument_list|()
operator|)
condition|?
literal|"mock:coldDrinks"
else|:
literal|"mock:hotDrinks"
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|resolveOrderItemChannel
argument_list|(
name|orderItem
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

