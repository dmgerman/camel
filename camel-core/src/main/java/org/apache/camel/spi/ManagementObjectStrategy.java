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
name|concurrent
operator|.
name|ThreadPoolExecutor
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
name|Component
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
name|Consumer
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
name|ErrorHandlerFactory
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
name|Producer
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
name|Route
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
name|Service
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
name|ProcessorDefinition
import|;
end_import

begin_comment
comment|/**  * Strategy for creating the managed object for the various beans Camel register for management.  */
end_comment

begin_interface
DECL|interface|ManagementObjectStrategy
specifier|public
interface|interface
name|ManagementObjectStrategy
block|{
DECL|method|getManagedObjectForCamelContext (CamelContext context)
name|Object
name|getManagedObjectForCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
DECL|method|getManagedObjectForComponent (CamelContext context, Component component, String name)
name|Object
name|getManagedObjectForComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Component
name|component
parameter_list|,
name|String
name|name
parameter_list|)
function_decl|;
DECL|method|getManagedObjectForDataFormat (CamelContext context, DataFormat dataFormat)
name|Object
name|getManagedObjectForDataFormat
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|DataFormat
name|dataFormat
parameter_list|)
function_decl|;
DECL|method|getManagedObjectForEndpoint (CamelContext context, Endpoint endpoint)
name|Object
name|getManagedObjectForEndpoint
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
DECL|method|getManagedObjectForErrorHandler (CamelContext context, RouteContext routeContext, Processor errorHandler, ErrorHandlerFactory errorHandlerBuilder)
name|Object
name|getManagedObjectForErrorHandler
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|errorHandler
parameter_list|,
name|ErrorHandlerFactory
name|errorHandlerBuilder
parameter_list|)
function_decl|;
DECL|method|getManagedObjectForRoute (CamelContext context, Route route)
name|Object
name|getManagedObjectForRoute
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Route
name|route
parameter_list|)
function_decl|;
DECL|method|getManagedObjectForConsumer (CamelContext context, Consumer consumer)
name|Object
name|getManagedObjectForConsumer
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Consumer
name|consumer
parameter_list|)
function_decl|;
DECL|method|getManagedObjectForProducer (CamelContext context, Producer producer)
name|Object
name|getManagedObjectForProducer
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Producer
name|producer
parameter_list|)
function_decl|;
DECL|method|getManagedObjectForProcessor (CamelContext context, Processor processor, ProcessorDefinition<?> definition, Route route)
name|Object
name|getManagedObjectForProcessor
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|,
name|Route
name|route
parameter_list|)
function_decl|;
DECL|method|getManagedObjectForService (CamelContext context, Service service)
name|Object
name|getManagedObjectForService
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|service
parameter_list|)
function_decl|;
DECL|method|getManagedObjectForThreadPool (CamelContext context, ThreadPoolExecutor threadPool, String id, String sourceId, String routeId, String threadPoolProfileId)
name|Object
name|getManagedObjectForThreadPool
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ThreadPoolExecutor
name|threadPool
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|sourceId
parameter_list|,
name|String
name|routeId
parameter_list|,
name|String
name|threadPoolProfileId
parameter_list|)
function_decl|;
DECL|method|getManagedObjectForEventNotifier (CamelContext context, EventNotifier eventNotifier)
name|Object
name|getManagedObjectForEventNotifier
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|EventNotifier
name|eventNotifier
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

