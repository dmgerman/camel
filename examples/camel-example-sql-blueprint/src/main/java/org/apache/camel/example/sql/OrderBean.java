begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.sql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|sql
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

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
DECL|class|OrderBean
specifier|public
class|class
name|OrderBean
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
comment|/**      * Generates a new order structured as a {@link Map}      */
DECL|method|generateOrder ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|generateOrder
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|answer
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
name|counter
operator|++
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
literal|"item"
argument_list|,
name|counter
operator|%
literal|2
operator|==
literal|0
condition|?
literal|111
else|:
literal|222
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
literal|"amount"
argument_list|,
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
name|answer
operator|.
name|put
argument_list|(
literal|"description"
argument_list|,
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
name|answer
return|;
block|}
comment|/**      * Processes the order      *      * @param data  the order as a {@link Map}      * @return the transformed order      */
DECL|method|processOrder (Map<String, Object> data)
specifier|public
name|String
name|processOrder
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|data
parameter_list|)
block|{
return|return
literal|"Processed order id "
operator|+
name|data
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
operator|+
literal|" item "
operator|+
name|data
operator|.
name|get
argument_list|(
literal|"item"
argument_list|)
operator|+
literal|" of "
operator|+
name|data
operator|.
name|get
argument_list|(
literal|"amount"
argument_list|)
operator|+
literal|" copies of "
operator|+
name|data
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

