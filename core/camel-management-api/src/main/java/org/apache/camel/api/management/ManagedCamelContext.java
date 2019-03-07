begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.api.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
operator|.
name|management
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedCamelContextMBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedProcessorMBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedRouteMBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedStepMBean
import|;
end_import

begin_interface
DECL|interface|ManagedCamelContext
specifier|public
interface|interface
name|ManagedCamelContext
block|{
comment|/**      * Gets the managed Camel CamelContext client api      */
DECL|method|getManagedCamelContext ()
name|ManagedCamelContextMBean
name|getManagedCamelContext
parameter_list|()
function_decl|;
comment|/**      * Gets the managed processor client api from any of the routes which with the given id      *      * @param id id of the processor      * @return the processor or<tt>null</tt> if not found      */
DECL|method|getManagedProcessor (String id)
specifier|default
name|ManagedProcessorMBean
name|getManagedProcessor
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|getManagedProcessor
argument_list|(
name|id
argument_list|,
name|ManagedProcessorMBean
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Gets the managed processor client api from any of the routes which with the given id      *      * @param id id of the processor      * @param type the managed processor type from the {@link org.apache.camel.api.management.mbean} package.      * @return the processor or<tt>null</tt> if not found      * @throws IllegalArgumentException if the type is not compliant      */
DECL|method|getManagedProcessor (String id, Class<T> type)
parameter_list|<
name|T
extends|extends
name|ManagedProcessorMBean
parameter_list|>
name|T
name|getManagedProcessor
parameter_list|(
name|String
name|id
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Gets the managed step client api from any of the routes which with the given id      *      * @param id id of the step      * @return the step or<tt>null</tt> if not found      * @throws IllegalArgumentException if the type is not compliant      */
DECL|method|getManagedStep (String id)
name|ManagedStepMBean
name|getManagedStep
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Gets the managed route client api with the given route id      *      * @param routeId id of the route      * @return the route or<tt>null</tt> if not found      */
DECL|method|getManagedRoute (String routeId)
specifier|default
name|ManagedRouteMBean
name|getManagedRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
block|{
return|return
name|getManagedRoute
argument_list|(
name|routeId
argument_list|,
name|ManagedRouteMBean
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Gets the managed route client api with the given route id      *      * @param routeId id of the route      * @param type the managed route type from the {@link org.apache.camel.api.management.mbean} package.      * @return the route or<tt>null</tt> if not found      * @throws IllegalArgumentException if the type is not compliant      */
DECL|method|getManagedRoute (String routeId, Class<T> type)
parameter_list|<
name|T
extends|extends
name|ManagedRouteMBean
parameter_list|>
name|T
name|getManagedRoute
parameter_list|(
name|String
name|routeId
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

