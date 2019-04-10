begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.pulsar.server
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|pulsar
operator|.
name|server
package|;
end_package

begin_comment
comment|/**  * Our business service.   */
end_comment

begin_comment
comment|// START SNIPPET: e1
end_comment

begin_interface
DECL|interface|Multiplier
specifier|public
interface|interface
name|Multiplier
block|{
comment|/**      * Multiplies the given number by a pre-defined constant.      *      * @param originalNumber The number to be multiplied      * @return The result of the multiplication      */
DECL|method|multiply (int originalNumber)
name|int
name|multiply
parameter_list|(
name|int
name|originalNumber
parameter_list|)
function_decl|;
block|}
end_interface

begin_comment
comment|// END SNIPPET: e1
end_comment

end_unit

