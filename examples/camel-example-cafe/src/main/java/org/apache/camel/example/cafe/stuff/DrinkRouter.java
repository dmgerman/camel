begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_class
DECL|class|DrinkRouter
specifier|public
class|class
name|DrinkRouter
block|{
DECL|method|resolveOrderItemChannel (OrderItem orderItem)
specifier|public
name|String
name|resolveOrderItemChannel
parameter_list|(
name|OrderItem
name|orderItem
parameter_list|)
block|{
return|return
operator|(
name|orderItem
operator|.
name|isIced
argument_list|()
operator|)
condition|?
literal|"seda:coldDrinks"
else|:
literal|"seda:hotDrinks"
return|;
block|}
block|}
end_class

end_unit

