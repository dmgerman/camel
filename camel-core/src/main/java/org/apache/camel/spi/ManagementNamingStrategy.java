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
name|model
operator|.
name|ProcessorDefinition
import|;
end_import

begin_comment
comment|/**  * Strategy for computing {@link ObjectName} names for the various beans that Camel register for management.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|ManagementNamingStrategy
specifier|public
interface|interface
name|ManagementNamingStrategy
block|{
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
DECL|method|getObjectNameForProcessor (CamelContext context, Processor processor, ProcessorDefinition definition)
name|ObjectName
name|getObjectNameForProcessor
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
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
DECL|method|getObjectNameForTracer (CamelContext context, InterceptStrategy tracer)
name|ObjectName
name|getObjectNameForTracer
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|InterceptStrategy
name|tracer
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
block|}
end_interface

end_unit

