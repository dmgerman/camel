begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
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
name|RuntimeCamelException
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
name|ManagedCamelContext
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
name|model
operator|.
name|ModelCamelContext
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|ManagementStrategy
import|;
end_import

begin_comment
comment|/**  * JMX capable {@link CamelContext}.  */
end_comment

begin_class
DECL|class|ManagedCamelContextImpl
specifier|public
class|class
name|ManagedCamelContextImpl
implements|implements
name|ManagedCamelContext
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|ManagedCamelContextImpl (CamelContext camelContext)
specifier|public
name|ManagedCamelContextImpl
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getManagementStrategy ()
specifier|private
name|ManagementStrategy
name|getManagementStrategy
parameter_list|()
block|{
return|return
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
return|;
block|}
DECL|method|getManagedProcessor (String id, Class<T> type)
specifier|public
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
block|{
comment|// jmx must be enabled
if|if
condition|(
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Processor
name|processor
init|=
name|camelContext
operator|.
name|getProcessor
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|ProcessorDefinition
name|def
init|=
name|camelContext
operator|.
name|adapt
argument_list|(
name|ModelCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getProcessorDefinition
argument_list|(
name|id
argument_list|)
decl_stmt|;
comment|// processor may be null if its anonymous inner class or as lambda
if|if
condition|(
name|def
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|ObjectName
name|on
init|=
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementObjectNameStrategy
argument_list|()
operator|.
name|getObjectNameForProcessor
argument_list|(
name|camelContext
argument_list|,
name|processor
argument_list|,
name|def
argument_list|)
decl_stmt|;
return|return
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|newProxyClient
argument_list|(
name|on
argument_list|,
name|type
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedObjectNameException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getManagedRoute (String routeId, Class<T> type)
specifier|public
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
block|{
comment|// jmx must be enabled
if|if
condition|(
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Route
name|route
init|=
name|camelContext
operator|.
name|getRoute
argument_list|(
name|routeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|route
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|ObjectName
name|on
init|=
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementObjectNameStrategy
argument_list|()
operator|.
name|getObjectNameForRoute
argument_list|(
name|route
argument_list|)
decl_stmt|;
return|return
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|newProxyClient
argument_list|(
name|on
argument_list|,
name|type
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedObjectNameException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getManagedCamelContext ()
specifier|public
name|ManagedCamelContextMBean
name|getManagedCamelContext
parameter_list|()
block|{
comment|// jmx must be enabled
if|if
condition|(
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
name|ObjectName
name|on
init|=
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementObjectNameStrategy
argument_list|()
operator|.
name|getObjectNameForCamelContext
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
return|return
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|newProxyClient
argument_list|(
name|on
argument_list|,
name|ManagedCamelContextMBean
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedObjectNameException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

