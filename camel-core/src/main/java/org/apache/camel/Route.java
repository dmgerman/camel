begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_interface
DECL|interface|Route
specifier|public
interface|interface
name|Route
block|{
DECL|field|ID_PROPERTY
name|String
name|ID_PROPERTY
init|=
literal|"id"
decl_stmt|;
DECL|field|PARENT_PROPERTY
name|String
name|PARENT_PROPERTY
init|=
literal|"parent"
decl_stmt|;
DECL|field|GROUP_PROPERTY
name|String
name|GROUP_PROPERTY
init|=
literal|"group"
decl_stmt|;
comment|/**      * Gets the route id      *      * @return the route id      */
DECL|method|getId ()
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      * Gets the inbound endpoint      */
DECL|method|getEndpoint ()
name|Endpoint
name|getEndpoint
parameter_list|()
function_decl|;
comment|/**      * Sets the inbound endpoint      *      * @param endpoint the endpoint      */
DECL|method|setEndpoint (Endpoint endpoint)
name|void
name|setEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * This property map is used to associate information about the route.      *      * @return properties      */
DECL|method|getProperties ()
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
function_decl|;
comment|/**      * This property map is used to associate information about      * the route. Gets all tbe services for this routes      *      * @return the services      * @throws Exception is thrown in case of error      */
DECL|method|getServicesForRoute ()
name|List
argument_list|<
name|Service
argument_list|>
name|getServicesForRoute
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Returns the additional services required for this particular route      */
DECL|method|getServices ()
name|List
argument_list|<
name|Service
argument_list|>
name|getServices
parameter_list|()
function_decl|;
comment|/**      * Sets the sources for this route      *      * @param services the services      */
DECL|method|setServices (List<Service> services)
name|void
name|setServices
parameter_list|(
name|List
argument_list|<
name|Service
argument_list|>
name|services
parameter_list|)
function_decl|;
comment|/**      * Adds a service to this route      *      * @param service the service      */
DECL|method|addService (Service service)
name|void
name|addService
parameter_list|(
name|Service
name|service
parameter_list|)
function_decl|;
comment|/**      * Returns a navigator to navigate this route by navigating all the {@link Processor}s.      *      * @return a navigator for {@link Processor}.      */
DECL|method|navigate ()
name|Navigate
argument_list|<
name|Processor
argument_list|>
name|navigate
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

