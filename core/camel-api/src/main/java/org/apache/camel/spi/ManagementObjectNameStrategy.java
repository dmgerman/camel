begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|management
operator|.
name|MalformedObjectNameException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|NamedNode
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
name|cluster
operator|.
name|CamelClusterService
import|;
end_import

begin_comment
comment|/**  * Strategy for computing {@link ObjectName} names for the various beans that Camel register for management.  */
end_comment

begin_interface
DECL|interface|ManagementObjectNameStrategy
specifier|public
interface|interface
name|ManagementObjectNameStrategy
block|{
DECL|method|getObjectName (Object managedObject)
name|ObjectName
name|getObjectName
parameter_list|(
name|Object
name|managedObject
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForCamelContext (String managementName, String name)
name|ObjectName
name|getObjectNameForCamelContext
parameter_list|(
name|String
name|managementName
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForCamelHealth (CamelContext context)
name|ObjectName
name|getObjectNameForCamelHealth
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForCamelContext (CamelContext context)
name|ObjectName
name|getObjectNameForCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForRouteController (CamelContext context)
name|ObjectName
name|getObjectNameForRouteController
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForComponent (Component component, String name)
name|ObjectName
name|getObjectNameForComponent
parameter_list|(
name|Component
name|component
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForEndpoint (Endpoint endpoint)
name|ObjectName
name|getObjectNameForEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForDataFormat (CamelContext context, DataFormat endpoint)
name|ObjectName
name|getObjectNameForDataFormat
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|DataFormat
name|endpoint
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForErrorHandler (RouteContext routeContext, Processor errorHandler, ErrorHandlerFactory builder)
name|ObjectName
name|getObjectNameForErrorHandler
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|errorHandler
parameter_list|,
name|ErrorHandlerFactory
name|builder
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForProcessor (CamelContext context, Processor processor, NamedNode definition)
name|ObjectName
name|getObjectNameForProcessor
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|NamedNode
name|definition
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForStep (CamelContext context, Processor processor, NamedNode definition)
name|ObjectName
name|getObjectNameForStep
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|NamedNode
name|definition
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForRoute (Route route)
name|ObjectName
name|getObjectNameForRoute
parameter_list|(
name|Route
name|route
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForConsumer (CamelContext context, Consumer consumer)
name|ObjectName
name|getObjectNameForConsumer
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Consumer
name|consumer
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForProducer (CamelContext context, Producer producer)
name|ObjectName
name|getObjectNameForProducer
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Producer
name|producer
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForTracer (CamelContext context, Service tracer)
name|ObjectName
name|getObjectNameForTracer
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|tracer
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForService (CamelContext context, Service service)
name|ObjectName
name|getObjectNameForService
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|service
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForClusterService (CamelContext context, CamelClusterService service)
name|ObjectName
name|getObjectNameForClusterService
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|CamelClusterService
name|service
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForThreadPool (CamelContext context, ThreadPoolExecutor threadPool, String id, String sourceId)
name|ObjectName
name|getObjectNameForThreadPool
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
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectNameForEventNotifier (CamelContext context, EventNotifier eventNotifier)
name|ObjectName
name|getObjectNameForEventNotifier
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|EventNotifier
name|eventNotifier
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
block|}
end_interface

end_unit

