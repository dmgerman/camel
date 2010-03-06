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
name|management
operator|.
name|mbean
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
name|ManagedErrorHandler
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
name|ManagedProducer
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
name|ManagementAgent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|commons
operator|.
name|management
operator|.
name|Statistic
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|commons
operator|.
name|management
operator|.
name|basic
operator|.
name|StatisticImpl
import|;
end_import

begin_comment
comment|/**  * A JMX capable {@link org.apache.camel.spi.ManagementStrategy} that Camel by default uses if possible.  *<p/>  * Camel detects whether its possible to use this JMX capable strategy and if<b>not</b> then Camel  * will fallback to the {@link org.apache.camel.management.DefaultManagementStrategy} instead.  *  * @see org.apache.camel.spi.ManagementStrategy  * @see org.apache.camel.management.DefaultManagementStrategy  * @version $Revision$  */
end_comment

begin_class
DECL|class|ManagedManagementStrategy
specifier|public
class|class
name|ManagedManagementStrategy
extends|extends
name|DefaultManagementStrategy
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ManagedManagementStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|ManagedManagementStrategy (CamelContext camelContext)
specifier|public
name|ManagedManagementStrategy
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|DefaultManagementAgent
argument_list|(
name|camelContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|ManagedManagementStrategy (ManagementAgent managementAgent)
specifier|public
name|ManagedManagementStrategy
parameter_list|(
name|ManagementAgent
name|managementAgent
parameter_list|)
block|{
name|setManagementAgent
argument_list|(
name|managementAgent
argument_list|)
expr_stmt|;
block|}
DECL|method|manageObject (Object managedObject)
specifier|public
name|void
name|manageObject
parameter_list|(
name|Object
name|managedObject
parameter_list|)
throws|throws
name|Exception
block|{
name|manageNamedObject
argument_list|(
name|managedObject
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|manageNamedObject (Object managedObject, Object preferedName)
specifier|public
name|void
name|manageNamedObject
parameter_list|(
name|Object
name|managedObject
parameter_list|,
name|Object
name|preferedName
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectName
name|objectName
init|=
name|getObjectName
argument_list|(
name|managedObject
argument_list|,
name|preferedName
argument_list|)
decl_stmt|;
if|if
condition|(
name|objectName
operator|!=
literal|null
condition|)
block|{
name|getManagementAgent
argument_list|()
operator|.
name|register
argument_list|(
name|managedObject
argument_list|,
name|objectName
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getManagedObjectName (Object managedObject, String customName, Class<T> nameType)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getManagedObjectName
parameter_list|(
name|Object
name|managedObject
parameter_list|,
name|String
name|customName
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|nameType
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|managedObject
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ObjectName
name|objectName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|managedObject
operator|instanceof
name|ManagedCamelContext
condition|)
block|{
name|ManagedCamelContext
name|mcc
init|=
operator|(
name|ManagedCamelContext
operator|)
name|managedObject
decl_stmt|;
name|objectName
operator|=
name|getManagementNamingStrategy
argument_list|()
operator|.
name|getObjectNameForCamelContext
argument_list|(
name|mcc
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|managedObject
operator|instanceof
name|ManagedComponent
condition|)
block|{
name|ManagedComponent
name|mc
init|=
operator|(
name|ManagedComponent
operator|)
name|managedObject
decl_stmt|;
name|objectName
operator|=
name|getManagementNamingStrategy
argument_list|()
operator|.
name|getObjectNameForComponent
argument_list|(
name|mc
operator|.
name|getComponent
argument_list|()
argument_list|,
name|mc
operator|.
name|getComponentName
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|managedObject
operator|instanceof
name|ManagedEndpoint
condition|)
block|{
name|ManagedEndpoint
name|me
init|=
operator|(
name|ManagedEndpoint
operator|)
name|managedObject
decl_stmt|;
name|objectName
operator|=
name|getManagementNamingStrategy
argument_list|()
operator|.
name|getObjectNameForEndpoint
argument_list|(
name|me
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|managedObject
operator|instanceof
name|Endpoint
condition|)
block|{
name|objectName
operator|=
name|getManagementNamingStrategy
argument_list|()
operator|.
name|getObjectNameForEndpoint
argument_list|(
operator|(
name|Endpoint
operator|)
name|managedObject
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|managedObject
operator|instanceof
name|ManagedRoute
condition|)
block|{
name|ManagedRoute
name|mr
init|=
operator|(
name|ManagedRoute
operator|)
name|managedObject
decl_stmt|;
name|objectName
operator|=
name|getManagementNamingStrategy
argument_list|()
operator|.
name|getObjectNameForRoute
argument_list|(
name|mr
operator|.
name|getRoute
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|managedObject
operator|instanceof
name|ManagedErrorHandler
condition|)
block|{
name|ManagedErrorHandler
name|meh
init|=
operator|(
name|ManagedErrorHandler
operator|)
name|managedObject
decl_stmt|;
name|objectName
operator|=
name|getManagementNamingStrategy
argument_list|()
operator|.
name|getObjectNameForErrorHandler
argument_list|(
name|meh
operator|.
name|getRouteContext
argument_list|()
argument_list|,
name|meh
operator|.
name|getErrorHandler
argument_list|()
argument_list|,
name|meh
operator|.
name|getErrorHandlerBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|managedObject
operator|instanceof
name|ManagedProcessor
condition|)
block|{
name|ManagedProcessor
name|mp
init|=
operator|(
name|ManagedProcessor
operator|)
name|managedObject
decl_stmt|;
name|objectName
operator|=
name|getManagementNamingStrategy
argument_list|()
operator|.
name|getObjectNameForProcessor
argument_list|(
name|mp
operator|.
name|getContext
argument_list|()
argument_list|,
name|mp
operator|.
name|getProcessor
argument_list|()
argument_list|,
name|mp
operator|.
name|getDefinition
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|managedObject
operator|instanceof
name|ManagedConsumer
condition|)
block|{
name|ManagedConsumer
name|ms
init|=
operator|(
name|ManagedConsumer
operator|)
name|managedObject
decl_stmt|;
name|objectName
operator|=
name|getManagementNamingStrategy
argument_list|()
operator|.
name|getObjectNameForConsumer
argument_list|(
name|ms
operator|.
name|getContext
argument_list|()
argument_list|,
name|ms
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|managedObject
operator|instanceof
name|ManagedProducer
condition|)
block|{
name|ManagedProducer
name|ms
init|=
operator|(
name|ManagedProducer
operator|)
name|managedObject
decl_stmt|;
name|objectName
operator|=
name|getManagementNamingStrategy
argument_list|()
operator|.
name|getObjectNameForProducer
argument_list|(
name|ms
operator|.
name|getContext
argument_list|()
argument_list|,
name|ms
operator|.
name|getProducer
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|managedObject
operator|instanceof
name|ManagedTracer
condition|)
block|{
name|ManagedTracer
name|mt
init|=
operator|(
name|ManagedTracer
operator|)
name|managedObject
decl_stmt|;
name|objectName
operator|=
name|getManagementNamingStrategy
argument_list|()
operator|.
name|getObjectNameForTracer
argument_list|(
name|mt
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|mt
operator|.
name|getTracer
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|managedObject
operator|instanceof
name|ManagedService
condition|)
block|{
comment|// check for managed service should be last
name|ManagedService
name|ms
init|=
operator|(
name|ManagedService
operator|)
name|managedObject
decl_stmt|;
name|objectName
operator|=
name|getManagementNamingStrategy
argument_list|()
operator|.
name|getObjectNameForService
argument_list|(
name|ms
operator|.
name|getContext
argument_list|()
argument_list|,
name|ms
operator|.
name|getService
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|nameType
operator|.
name|cast
argument_list|(
name|objectName
argument_list|)
return|;
block|}
DECL|method|unmanageObject (Object managedObject)
specifier|public
name|void
name|unmanageObject
parameter_list|(
name|Object
name|managedObject
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectName
name|objectName
init|=
name|getManagedObjectName
argument_list|(
name|managedObject
argument_list|,
literal|null
argument_list|,
name|ObjectName
operator|.
name|class
argument_list|)
decl_stmt|;
name|unmanageNamedObject
argument_list|(
name|objectName
argument_list|)
expr_stmt|;
block|}
DECL|method|unmanageNamedObject (Object name)
specifier|public
name|void
name|unmanageNamedObject
parameter_list|(
name|Object
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectName
name|objectName
init|=
name|getObjectName
argument_list|(
literal|null
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|objectName
operator|!=
literal|null
condition|)
block|{
name|getManagementAgent
argument_list|()
operator|.
name|unregister
argument_list|(
name|objectName
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isManaged (Object managableObject, Object name)
specifier|public
name|boolean
name|isManaged
parameter_list|(
name|Object
name|managableObject
parameter_list|,
name|Object
name|name
parameter_list|)
block|{
try|try
block|{
name|ObjectName
name|objectName
init|=
name|getObjectName
argument_list|(
name|managableObject
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|objectName
operator|!=
literal|null
condition|)
block|{
return|return
name|getManagementAgent
argument_list|()
operator|.
name|isRegistered
argument_list|(
name|objectName
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot check whether the managed object is registered. This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|manageProcessor (ProcessorDefinition<?> definition)
specifier|public
name|boolean
name|manageProcessor
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|createStatistic (String name, Object owner, Statistic.UpdateMode updateMode)
specifier|public
name|Statistic
name|createStatistic
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|owner
parameter_list|,
name|Statistic
operator|.
name|UpdateMode
name|updateMode
parameter_list|)
block|{
return|return
operator|new
name|StatisticImpl
argument_list|(
name|updateMode
argument_list|)
return|;
block|}
DECL|method|getObjectName (Object managedObject, Object preferedName)
specifier|private
name|ObjectName
name|getObjectName
parameter_list|(
name|Object
name|managedObject
parameter_list|,
name|Object
name|preferedName
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectName
name|objectName
decl_stmt|;
if|if
condition|(
name|preferedName
operator|!=
literal|null
operator|&&
name|preferedName
operator|instanceof
name|String
condition|)
block|{
name|String
name|customName
init|=
operator|(
name|String
operator|)
name|preferedName
decl_stmt|;
name|objectName
operator|=
name|getManagedObjectName
argument_list|(
name|managedObject
argument_list|,
name|customName
argument_list|,
name|ObjectName
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|preferedName
operator|!=
literal|null
operator|&&
name|preferedName
operator|instanceof
name|ObjectName
condition|)
block|{
name|objectName
operator|=
operator|(
name|ObjectName
operator|)
name|preferedName
expr_stmt|;
block|}
else|else
block|{
name|objectName
operator|=
name|getManagedObjectName
argument_list|(
name|managedObject
argument_list|,
literal|null
argument_list|,
name|ObjectName
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|objectName
return|;
block|}
block|}
end_class

end_unit

