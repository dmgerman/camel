begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.groovy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|groovy
package|;
end_package

begin_class
DECL|class|Order
specifier|public
class|class
name|Order
block|{
DECL|field|item
specifier|private
name|String
name|item
decl_stmt|;
DECL|field|amount
specifier|private
name|int
name|amount
decl_stmt|;
DECL|method|Order (String item, int amount)
specifier|public
name|Order
parameter_list|(
name|String
name|item
parameter_list|,
name|int
name|amount
parameter_list|)
block|{
name|this
operator|.
name|item
operator|=
name|item
expr_stmt|;
name|this
operator|.
name|amount
operator|=
name|amount
expr_stmt|;
block|}
DECL|method|getItem ()
specifier|public
name|String
name|getItem
parameter_list|()
block|{
return|return
name|item
return|;
block|}
DECL|method|getAmount ()
specifier|public
name|int
name|getAmount
parameter_list|()
block|{
return|return
name|amount
return|;
block|}
block|}
end_class

end_unit

