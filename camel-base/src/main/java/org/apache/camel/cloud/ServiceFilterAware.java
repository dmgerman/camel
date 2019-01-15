begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cloud
package|;
end_package

begin_comment
comment|/**  * An interface to represent an object which wishes to be injected with  * a {@link ServiceFilter}  */
end_comment

begin_interface
DECL|interface|ServiceFilterAware
specifier|public
interface|interface
name|ServiceFilterAware
block|{
comment|/**      * Injects the {@link ServiceFilter}      *      * @param serviceFilter the ServiceFilter      */
DECL|method|setServiceFilter (ServiceFilter serviceFilter)
name|void
name|setServiceFilter
parameter_list|(
name|ServiceFilter
name|serviceFilter
parameter_list|)
function_decl|;
comment|/**      * Get the {@link ServiceFilter}      *      * @return the ServiceFilter      */
DECL|method|getServiceFilter ()
name|ServiceFilter
name|getServiceFilter
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

