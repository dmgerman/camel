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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|Endpoint
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
name|Exchange
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
name|Intercept
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
name|Processor
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
name|model
operator|.
name|FromType
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
name|model
operator|.
name|ProcessorType
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
name|model
operator|.
name|RouteType
import|;
end_import

begin_comment
comment|/**  * The context used to activate new routing rules  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|RouteContext
specifier|public
interface|interface
name|RouteContext
block|{
DECL|method|getEndpoint ()
name|Endpoint
argument_list|<
name|?
extends|extends
name|Exchange
argument_list|>
name|getEndpoint
parameter_list|()
function_decl|;
DECL|method|getFrom ()
name|FromType
name|getFrom
parameter_list|()
function_decl|;
DECL|method|getRoute ()
name|RouteType
name|getRoute
parameter_list|()
function_decl|;
comment|/**      * Gets the CamelContext      */
DECL|method|getCamelContext ()
name|CamelContext
name|getCamelContext
parameter_list|()
function_decl|;
DECL|method|createProcessor (ProcessorType node)
name|Processor
name|createProcessor
parameter_list|(
name|ProcessorType
name|node
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Resolves an endpoint from the URI      */
DECL|method|resolveEndpoint (String uri)
name|Endpoint
argument_list|<
name|?
extends|extends
name|Exchange
argument_list|>
name|resolveEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Resolves an endpoint from either a URI or a named reference      */
DECL|method|resolveEndpoint (String uri, String ref)
name|Endpoint
argument_list|<
name|?
extends|extends
name|Exchange
argument_list|>
name|resolveEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|ref
parameter_list|)
function_decl|;
comment|/**      * lookup an object by name and type      */
DECL|method|lookup (String name, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|lookup
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Lets complete the route creation, creating a single event driven route      * for the current from endpoint with any processors required      */
DECL|method|commit ()
name|void
name|commit
parameter_list|()
function_decl|;
DECL|method|addEventDrivenProcessor (Processor processor)
name|void
name|addEventDrivenProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
function_decl|;
DECL|method|intercept (Intercept interceptor)
name|void
name|intercept
parameter_list|(
name|Intercept
name|interceptor
parameter_list|)
function_decl|;
DECL|method|createProceedProcessor ()
name|Processor
name|createProceedProcessor
parameter_list|()
function_decl|;
comment|/**      * This method retrieves the InterceptStrategy instances this route context.      */
DECL|method|getInterceptStrategies ()
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|getInterceptStrategies
parameter_list|()
function_decl|;
comment|/**      * This method sets the InterceptStrategy instances on this route context.      */
DECL|method|setInterceptStrategies (List<InterceptStrategy> interceptStrategies)
name|void
name|setInterceptStrategies
parameter_list|(
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|interceptStrategies
parameter_list|)
function_decl|;
DECL|method|addInterceptStrategy (InterceptStrategy interceptStrategy)
name|void
name|addInterceptStrategy
parameter_list|(
name|InterceptStrategy
name|interceptStrategy
parameter_list|)
function_decl|;
comment|/**      * This method retrieves the ErrorHandlerWrappingStrategy.      */
DECL|method|getErrorHandlerWrappingStrategy ()
name|ErrorHandlerWrappingStrategy
name|getErrorHandlerWrappingStrategy
parameter_list|()
function_decl|;
comment|/**      * This method sets the ErrorHandlerWrappingStrategy.      */
DECL|method|setErrorHandlerWrappingStrategy (ErrorHandlerWrappingStrategy strategy)
name|void
name|setErrorHandlerWrappingStrategy
parameter_list|(
name|ErrorHandlerWrappingStrategy
name|strategy
parameter_list|)
function_decl|;
comment|/**      * If this flag is true, {@link ProcessorType#addRoutes(RouteContext, java.util.Collection)}      * will not add processor to addEventDrivenProcessor to the RouteContext and it      * will prevent from adding an EventDrivenRoute.      *       */
DECL|method|setIsRouteAdded (boolean value)
name|void
name|setIsRouteAdded
parameter_list|(
name|boolean
name|value
parameter_list|)
function_decl|;
DECL|method|isRouteAdded ()
name|boolean
name|isRouteAdded
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

