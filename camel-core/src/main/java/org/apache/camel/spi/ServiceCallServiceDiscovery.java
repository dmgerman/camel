begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
comment|/**  * Allows SPIs to implement custom Service Discovery for the Service Call EIP.  *  * @see ServiceCallServiceChooser  * @see ServiceCallService  */
end_comment

begin_interface
DECL|interface|ServiceCallServiceDiscovery
specifier|public
interface|interface
name|ServiceCallServiceDiscovery
block|{
comment|/**      * Gets the initial list of services.      *<p/>      * This method may return<tt>null</tt> or an empty list.      *      * @param name the service name      */
DECL|method|getInitialListOfServices (String name)
name|List
argument_list|<
name|ServiceCallService
argument_list|>
name|getInitialListOfServices
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Gets the updated list of services.      *<p/>      * This method can either be called on-demand prior to a service call, or have      * a background job that is scheduled to update the list, or a watcher      * that triggers when the list of services changes.      *      * @param name the service name      */
DECL|method|getUpdatedListOfServices (String name)
name|List
argument_list|<
name|ServiceCallService
argument_list|>
name|getUpdatedListOfServices
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

