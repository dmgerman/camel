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
name|management
operator|.
name|mbean
operator|.
name|ManagedComponent
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
name|management
operator|.
name|mbean
operator|.
name|ManagedConsumer
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
name|management
operator|.
name|mbean
operator|.
name|ManagedEndpoint
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
name|management
operator|.
name|mbean
operator|.
name|ManagedProcessor
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
name|management
operator|.
name|mbean
operator|.
name|ManagedRoute
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
name|management
operator|.
name|mbean
operator|.
name|ManagedService
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
name|management
operator|.
name|mbean
operator|.
name|ManagedTracer
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
comment|/**  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|ManagementNamingStrategy
specifier|public
interface|interface
name|ManagementNamingStrategy
block|{
DECL|method|getObjectName (CamelContext context)
name|ObjectName
name|getObjectName
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectName (ManagedComponent mbean)
name|ObjectName
name|getObjectName
parameter_list|(
name|ManagedComponent
name|mbean
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectName (ManagedEndpoint mbean)
name|ObjectName
name|getObjectName
parameter_list|(
name|ManagedEndpoint
name|mbean
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectName (ManagedProcessor mbean)
name|ObjectName
name|getObjectName
parameter_list|(
name|ManagedProcessor
name|mbean
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectName (ManagedRoute mbean)
name|ObjectName
name|getObjectName
parameter_list|(
name|ManagedRoute
name|mbean
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectName (ManagedConsumer mbean)
name|ObjectName
name|getObjectName
parameter_list|(
name|ManagedConsumer
name|mbean
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
DECL|method|getObjectName (ManagedTracer mbean)
name|ObjectName
name|getObjectName
parameter_list|(
name|ManagedTracer
name|mbean
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
comment|/**      * @deprecated      */
DECL|method|getObjectName (ManagedService mbean)
name|ObjectName
name|getObjectName
parameter_list|(
name|ManagedService
name|mbean
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
comment|/**      * @deprecated      */
DECL|method|getObjectName (RouteContext routeContext, ProcessorDefinition processor)
name|ObjectName
name|getObjectName
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|ProcessorDefinition
name|processor
parameter_list|)
throws|throws
name|MalformedObjectNameException
function_decl|;
block|}
end_interface

end_unit

