begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hdfs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hdfs
package|;
end_package

begin_class
DECL|class|Holder
specifier|public
class|class
name|Holder
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * The value contained in the holder.      **/
DECL|field|value
specifier|public
name|T
name|value
decl_stmt|;
comment|/**      * Creates a new holder with a<code>null</code> value.      **/
DECL|method|Holder ()
specifier|public
name|Holder
parameter_list|()
block|{     }
comment|/**      * Create a new holder with the specified value.      *      * @param value The value to be stored in the holder.      **/
DECL|method|Holder (T value)
specifier|public
name|Holder
parameter_list|(
name|T
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
block|}
end_class

end_unit

