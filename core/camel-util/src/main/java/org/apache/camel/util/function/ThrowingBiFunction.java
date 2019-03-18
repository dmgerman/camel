begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.function
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|function
package|;
end_package

begin_interface
annotation|@
name|FunctionalInterface
DECL|interface|ThrowingBiFunction
specifier|public
interface|interface
name|ThrowingBiFunction
parameter_list|<
name|I1
parameter_list|,
name|I2
parameter_list|,
name|R
parameter_list|,
name|T
extends|extends
name|Throwable
parameter_list|>
block|{
DECL|method|apply (I1 in1, I2 in2)
name|R
name|apply
parameter_list|(
name|I1
name|in1
parameter_list|,
name|I2
name|in2
parameter_list|)
throws|throws
name|T
function_decl|;
block|}
end_interface

end_unit

