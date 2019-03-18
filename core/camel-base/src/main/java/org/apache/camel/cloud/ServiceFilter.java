begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Allows SPIs to implement custom Service Filter.  *  * @see ServiceDiscovery  */
end_comment

begin_interface
annotation|@
name|FunctionalInterface
DECL|interface|ServiceFilter
specifier|public
interface|interface
name|ServiceFilter
block|{
comment|/**      * Chooses one of the service to use      *      * @param services  list of services      * @return the chosen service to use.      */
DECL|method|apply (List<ServiceDefinition> services)
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|apply
parameter_list|(
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|services
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

