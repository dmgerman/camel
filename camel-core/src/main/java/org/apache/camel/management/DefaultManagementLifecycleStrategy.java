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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|JMException
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
name|ManagementStatisticsLevel
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
name|builder
operator|.
name|ErrorHandlerBuilder
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
name|impl
operator|.
name|EventDrivenConsumerRoute
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
name|impl
operator|.
name|ScheduledPollConsumer
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
name|ManagedBrowsableEndpoint
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
name|ManagedDelayer
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
name|ManagedPerformanceCounter
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
name|ManagedScheduledPollConsumer
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
name|ManagedSendProcessor
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
name|ManagedThrottler
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
name|AOPDefinition
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
name|InterceptDefinition
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
name|OnCompletionDefinition
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
name|OnExceptionDefinition
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
name|PolicyDefinition
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
name|model
operator|.
name|RouteDefinition
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
name|processor
operator|.
name|Delayer
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
name|processor
operator|.
name|ErrorHandler
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
name|processor
operator|.
name|SendProcessor
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
name|processor
operator|.
name|Throttler
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
name|processor
operator|.
name|interceptor
operator|.
name|Tracer
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
name|BrowsableEndpoint
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
name|LifecycleStrategy
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
name|ManagementAware
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
name|RouteContext
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
name|util
operator|.
name|KeyValueHolder
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
name|util
operator|.
name|ObjectHelper
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

begin_comment
comment|/**  * Default JMX managed lifecycle strategy that registered objects using the configured  * {@link org.apache.camel.spi.ManagementStrategy}.  *  * @see org.apache.camel.spi.ManagementStrategy  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultManagementLifecycleStrategy
specifier|public
class|class
name|DefaultManagementLifecycleStrategy
implements|implements
name|LifecycleStrategy
implements|,
name|Service
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
name|DefaultManagementLifecycleStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|wrappedProcessors
specifier|private
specifier|final
name|Map
argument_list|<
name|Processor
argument_list|,
name|KeyValueHolder
argument_list|<
name|ProcessorDefinition
argument_list|,
name|InstrumentationProcessor
argument_list|>
argument_list|>
name|wrappedProcessors
init|=
operator|new
name|HashMap
argument_list|<
name|Processor
argument_list|,
name|KeyValueHolder
argument_list|<
name|ProcessorDefinition
argument_list|,
name|InstrumentationProcessor
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|field|initialized
specifier|private
name|boolean
name|initialized
decl_stmt|;
DECL|method|DefaultManagementLifecycleStrategy (CamelContext context)
specifier|public
name|DefaultManagementLifecycleStrategy
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
DECL|method|onContextStart (CamelContext context)
specifier|public
name|void
name|onContextStart
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
try|try
block|{
name|initialized
operator|=
literal|true
expr_stmt|;
comment|// call addService so that context will handle lifecycle on the strategy
name|context
operator|.
name|addService
argument_list|(
name|getStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|ManagedCamelContext
name|mc
init|=
operator|new
name|ManagedCamelContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|getStrategy
argument_list|()
operator|.
name|manageObject
argument_list|(
name|mc
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// must rethrow to allow CamelContext fallback to non JMX agent to allow
comment|// Camel to continue to run
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|onContextStop (CamelContext context)
specifier|public
name|void
name|onContextStop
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
comment|// the agent hasn't been started
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
return|return;
block|}
try|try
block|{
name|ManagedCamelContext
name|mc
init|=
operator|new
name|ManagedCamelContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
comment|// the context could have been removed already
if|if
condition|(
name|getStrategy
argument_list|()
operator|.
name|isManaged
argument_list|(
literal|null
argument_list|,
name|mc
argument_list|)
condition|)
block|{
name|getStrategy
argument_list|()
operator|.
name|unmanageObject
argument_list|(
name|mc
argument_list|)
expr_stmt|;
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
literal|"Could not unregister CamelContext MBean"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onComponentAdd (String name, Component component)
specifier|public
name|void
name|onComponentAdd
parameter_list|(
name|String
name|name
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
comment|// the agent hasn't been started
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
return|return;
block|}
try|try
block|{
name|Object
name|mc
init|=
name|getManagedObjectForComponent
argument_list|(
name|name
argument_list|,
name|component
argument_list|)
decl_stmt|;
name|getStrategy
argument_list|()
operator|.
name|manageObject
argument_list|(
name|mc
argument_list|)
expr_stmt|;
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
literal|"Could not register Component MBean"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onComponentRemove (String name, Component component)
specifier|public
name|void
name|onComponentRemove
parameter_list|(
name|String
name|name
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
comment|// the agent hasn't been started
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
return|return;
block|}
try|try
block|{
name|Object
name|mc
init|=
name|getManagedObjectForComponent
argument_list|(
name|name
argument_list|,
name|component
argument_list|)
decl_stmt|;
name|getStrategy
argument_list|()
operator|.
name|unmanageObject
argument_list|(
name|mc
argument_list|)
expr_stmt|;
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
literal|"Could not unregister Component MBean"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getManagedObjectForComponent (String name, Component component)
specifier|private
name|Object
name|getManagedObjectForComponent
parameter_list|(
name|String
name|name
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
if|if
condition|(
name|component
operator|instanceof
name|ManagementAware
condition|)
block|{
return|return
operator|(
operator|(
name|ManagementAware
operator|)
name|component
operator|)
operator|.
name|getManagedObject
argument_list|(
name|component
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ManagedComponent
argument_list|(
name|name
argument_list|,
name|component
argument_list|)
return|;
block|}
block|}
comment|/**      * If the endpoint is an instance of ManagedResource then register it with the      * mbean server, if it is not then wrap the endpoint in a {@link ManagedEndpoint} and      * register that with the mbean server.      *      * @param endpoint the Endpoint attempted to be added      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|onEndpointAdd (Endpoint endpoint)
specifier|public
name|void
name|onEndpointAdd
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
comment|// the agent hasn't been started
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
return|return;
block|}
try|try
block|{
name|Object
name|me
init|=
name|getManagedObjectForEndpoint
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|getStrategy
argument_list|()
operator|.
name|manageObject
argument_list|(
name|me
argument_list|)
expr_stmt|;
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
literal|"Could not register Endpoint MBean for uri: "
operator|+
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onEndpointRemove (Endpoint endpoint)
specifier|public
name|void
name|onEndpointRemove
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
comment|// the agent hasn't been started
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
return|return;
block|}
try|try
block|{
name|Object
name|me
init|=
name|getManagedObjectForEndpoint
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|getStrategy
argument_list|()
operator|.
name|unmanageObject
argument_list|(
name|me
argument_list|)
expr_stmt|;
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
literal|"Could not unregister Endpoint MBean for uri: "
operator|+
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getManagedObjectForEndpoint (Endpoint endpoint)
specifier|private
name|Object
name|getManagedObjectForEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|instanceof
name|ManagementAware
condition|)
block|{
return|return
operator|(
operator|(
name|ManagementAware
operator|)
name|endpoint
operator|)
operator|.
name|getManagedObject
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|instanceof
name|BrowsableEndpoint
condition|)
block|{
return|return
operator|new
name|ManagedBrowsableEndpoint
argument_list|(
operator|(
name|BrowsableEndpoint
operator|)
name|endpoint
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ManagedEndpoint
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
block|}
DECL|method|onServiceAdd (CamelContext context, Service service, Route route)
specifier|public
name|void
name|onServiceAdd
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|service
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
comment|// services can by any kind of misc type but also processors
comment|// so we have special logic when its a processor
comment|// the agent hasn't been started
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
return|return;
block|}
name|Object
name|managedObject
init|=
name|getManagedObjectForService
argument_list|(
name|context
argument_list|,
name|service
argument_list|,
name|route
argument_list|)
decl_stmt|;
if|if
condition|(
name|managedObject
operator|==
literal|null
condition|)
block|{
comment|// service should not be managed
return|return;
block|}
comment|// skip already managed services, for example if a route has been restarted
if|if
condition|(
name|getStrategy
argument_list|()
operator|.
name|isManaged
argument_list|(
name|managedObject
argument_list|,
literal|null
argument_list|)
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"The service is already managed: "
operator|+
name|service
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
try|try
block|{
name|getStrategy
argument_list|()
operator|.
name|manageObject
argument_list|(
name|managedObject
argument_list|)
expr_stmt|;
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
literal|"Could not register service: "
operator|+
name|service
operator|+
literal|" as Service MBean."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onServiceRemove (CamelContext context, Service service, Route route)
specifier|public
name|void
name|onServiceRemove
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|service
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
comment|// the agent hasn't been started
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
return|return;
block|}
name|Object
name|managedObject
init|=
name|getManagedObjectForService
argument_list|(
name|context
argument_list|,
name|service
argument_list|,
name|route
argument_list|)
decl_stmt|;
if|if
condition|(
name|managedObject
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|getStrategy
argument_list|()
operator|.
name|unmanageObject
argument_list|(
name|managedObject
argument_list|)
expr_stmt|;
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
literal|"Could not unregister service: "
operator|+
name|service
operator|+
literal|" as Service MBean."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getManagedObjectForService (CamelContext context, Service service, Route route)
specifier|private
name|Object
name|getManagedObjectForService
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|service
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
name|ManagedService
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|service
operator|instanceof
name|ManagementAware
condition|)
block|{
return|return
operator|(
operator|(
name|ManagementAware
operator|)
name|service
operator|)
operator|.
name|getManagedObject
argument_list|(
name|service
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|service
operator|instanceof
name|Tracer
condition|)
block|{
comment|// special for tracer
return|return
operator|new
name|ManagedTracer
argument_list|(
name|context
argument_list|,
operator|(
name|Tracer
operator|)
name|service
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|service
operator|instanceof
name|Producer
condition|)
block|{
name|answer
operator|=
operator|new
name|ManagedProducer
argument_list|(
name|context
argument_list|,
operator|(
name|Producer
operator|)
name|service
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|service
operator|instanceof
name|ScheduledPollConsumer
condition|)
block|{
name|answer
operator|=
operator|new
name|ManagedScheduledPollConsumer
argument_list|(
name|context
argument_list|,
operator|(
name|ScheduledPollConsumer
operator|)
name|service
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|service
operator|instanceof
name|Consumer
condition|)
block|{
name|answer
operator|=
operator|new
name|ManagedConsumer
argument_list|(
name|context
argument_list|,
operator|(
name|Consumer
operator|)
name|service
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|service
operator|instanceof
name|Processor
condition|)
block|{
comment|// special for processors
return|return
name|getManagedObjectForProcessor
argument_list|(
name|context
argument_list|,
operator|(
name|Processor
operator|)
name|service
argument_list|,
name|route
argument_list|)
return|;
block|}
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setRoute
argument_list|(
name|route
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
else|else
block|{
comment|// not supported
return|return
literal|null
return|;
block|}
block|}
DECL|method|getManagedObjectForProcessor (CamelContext context, Processor processor, Route route)
specifier|private
name|Object
name|getManagedObjectForProcessor
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
comment|// a bit of magic here as the processors we want to manage have already been registered
comment|// in the wrapped processors map when Camel have instrumented the route on route initialization
comment|// so the idea is now to only manage the processors from the map
name|KeyValueHolder
argument_list|<
name|ProcessorDefinition
argument_list|,
name|InstrumentationProcessor
argument_list|>
name|holder
init|=
name|wrappedProcessors
operator|.
name|get
argument_list|(
name|processor
argument_list|)
decl_stmt|;
if|if
condition|(
name|holder
operator|==
literal|null
condition|)
block|{
comment|// skip as its not an well known processor we want to manage anyway, such as Channel/UnitOfWork/Pipeline etc.
return|return
literal|null
return|;
block|}
comment|// get the managed object as it can be a specialized type such as a Delayer/Throttler etc.
name|Object
name|managedObject
init|=
name|createManagedObjectForProcessor
argument_list|(
name|context
argument_list|,
name|processor
argument_list|,
name|holder
operator|.
name|getKey
argument_list|()
argument_list|,
name|route
argument_list|)
decl_stmt|;
comment|// only manage if we have a name for it as otherwise we do not want to manage it anyway
if|if
condition|(
name|managedObject
operator|!=
literal|null
condition|)
block|{
comment|// is it a performance counter then we need to set our counter
if|if
condition|(
name|managedObject
operator|instanceof
name|ManagedPerformanceCounter
condition|)
block|{
name|InstrumentationProcessor
name|counter
init|=
name|holder
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|counter
operator|!=
literal|null
condition|)
block|{
comment|// change counter to us
name|counter
operator|.
name|setCounter
argument_list|(
operator|(
name|ManagedPerformanceCounter
operator|)
name|managedObject
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|managedObject
return|;
block|}
DECL|method|createManagedObjectForProcessor (CamelContext context, Processor processor, ProcessorDefinition definition, Route route)
specifier|private
name|Object
name|createManagedObjectForProcessor
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
name|definition
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
comment|// skip error handlers
if|if
condition|(
name|processor
operator|instanceof
name|ErrorHandler
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ManagedProcessor
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|processor
operator|instanceof
name|Delayer
condition|)
block|{
name|answer
operator|=
operator|new
name|ManagedDelayer
argument_list|(
name|context
argument_list|,
operator|(
name|Delayer
operator|)
name|processor
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|Throttler
condition|)
block|{
name|answer
operator|=
operator|new
name|ManagedThrottler
argument_list|(
name|context
argument_list|,
operator|(
name|Throttler
operator|)
name|processor
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|SendProcessor
condition|)
block|{
name|answer
operator|=
operator|new
name|ManagedSendProcessor
argument_list|(
name|context
argument_list|,
operator|(
name|SendProcessor
operator|)
name|processor
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// fallback to a generic processor
name|answer
operator|=
operator|new
name|ManagedProcessor
argument_list|(
name|context
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|setRoute
argument_list|(
name|route
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|onRoutesAdd (Collection<Route> routes)
specifier|public
name|void
name|onRoutesAdd
parameter_list|(
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
block|{
comment|// the agent hasn't been started
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|ManagedRoute
name|mr
init|=
operator|new
name|ManagedRoute
argument_list|(
name|getStrategy
argument_list|()
argument_list|,
name|context
argument_list|,
name|route
argument_list|)
decl_stmt|;
comment|// skip already managed routes, for example if the route has been restarted
if|if
condition|(
name|getStrategy
argument_list|()
operator|.
name|isManaged
argument_list|(
name|mr
argument_list|,
literal|null
argument_list|)
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"The route is already managed: "
operator|+
name|route
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
comment|// get the wrapped instrumentation processor from this route
comment|// and set me as the counter
if|if
condition|(
name|route
operator|instanceof
name|EventDrivenConsumerRoute
condition|)
block|{
name|EventDrivenConsumerRoute
name|edcr
init|=
operator|(
name|EventDrivenConsumerRoute
operator|)
name|route
decl_stmt|;
name|Processor
name|processor
init|=
name|edcr
operator|.
name|getProcessor
argument_list|()
decl_stmt|;
if|if
condition|(
name|processor
operator|instanceof
name|InstrumentationProcessor
condition|)
block|{
name|InstrumentationProcessor
name|ip
init|=
operator|(
name|InstrumentationProcessor
operator|)
name|processor
decl_stmt|;
name|ip
operator|.
name|setCounter
argument_list|(
name|mr
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|getStrategy
argument_list|()
operator|.
name|manageObject
argument_list|(
name|mr
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not register Route MBean"
argument_list|,
name|e
argument_list|)
expr_stmt|;
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
literal|"Could not create Route MBean"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|onRoutesRemove (Collection<Route> routes)
specifier|public
name|void
name|onRoutesRemove
parameter_list|(
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
block|{
comment|// noop - keep the route in the mbean so its still there, it will still be unregistered
comment|// when camel itself is shutting down
block|}
DECL|method|onErrorHandlerAdd (RouteContext routeContext, Processor errorHandler, ErrorHandlerBuilder errorHandlerBuilder)
specifier|public
name|void
name|onErrorHandlerAdd
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|errorHandler
parameter_list|,
name|ErrorHandlerBuilder
name|errorHandlerBuilder
parameter_list|)
block|{
comment|// the agent hasn't been started
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
return|return;
block|}
name|Object
name|managedObject
init|=
operator|new
name|ManagedErrorHandler
argument_list|(
name|routeContext
argument_list|,
name|errorHandler
argument_list|,
name|errorHandlerBuilder
argument_list|)
decl_stmt|;
comment|// skip already managed services, for example if a route has been restarted
if|if
condition|(
name|getStrategy
argument_list|()
operator|.
name|isManaged
argument_list|(
name|managedObject
argument_list|,
literal|null
argument_list|)
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"The error handler builder is already managed: "
operator|+
name|errorHandlerBuilder
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
try|try
block|{
name|getStrategy
argument_list|()
operator|.
name|manageObject
argument_list|(
name|managedObject
argument_list|)
expr_stmt|;
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
literal|"Could not register error handler builder: "
operator|+
name|errorHandlerBuilder
operator|+
literal|" as ErrorHandlerMBean."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onRouteContextCreate (RouteContext routeContext)
specifier|public
name|void
name|onRouteContextCreate
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
comment|// the agent hasn't been started
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
return|return;
block|}
comment|// Create a map (ProcessorType -> PerformanceCounter)
comment|// to be passed to InstrumentationInterceptStrategy.
name|Map
argument_list|<
name|ProcessorDefinition
argument_list|,
name|ManagedPerformanceCounter
argument_list|>
name|registeredCounters
init|=
operator|new
name|HashMap
argument_list|<
name|ProcessorDefinition
argument_list|,
name|ManagedPerformanceCounter
argument_list|>
argument_list|()
decl_stmt|;
comment|// Each processor in a route will have its own performance counter
comment|// The performance counter are MBeans that we register with MBeanServer.
comment|// These performance counter will be embedded
comment|// to InstrumentationProcessor and wrap the appropriate processor
comment|// by InstrumentationInterceptStrategy.
name|RouteDefinition
name|route
init|=
name|routeContext
operator|.
name|getRoute
argument_list|()
decl_stmt|;
comment|// register performance counters for all processors and its children
for|for
control|(
name|ProcessorDefinition
name|processor
range|:
name|route
operator|.
name|getOutputs
argument_list|()
control|)
block|{
name|registerPerformanceCounters
argument_list|(
name|routeContext
argument_list|,
name|processor
argument_list|,
name|registeredCounters
argument_list|)
expr_stmt|;
block|}
comment|// set this managed intercept strategy that executes the JMX instrumentation for performance metrics
comment|// so our registered counters can be used for fine grained performance instrumentation
name|routeContext
operator|.
name|setManagedInterceptStrategy
argument_list|(
operator|new
name|InstrumentationInterceptStrategy
argument_list|(
name|registeredCounters
argument_list|,
name|wrappedProcessors
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|registerPerformanceCounters (RouteContext routeContext, ProcessorDefinition processor, Map<ProcessorDefinition, ManagedPerformanceCounter> registeredCounters)
specifier|private
name|void
name|registerPerformanceCounters
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|ProcessorDefinition
name|processor
parameter_list|,
name|Map
argument_list|<
name|ProcessorDefinition
argument_list|,
name|ManagedPerformanceCounter
argument_list|>
name|registeredCounters
parameter_list|)
block|{
comment|// traverse children if any exists
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|children
init|=
name|processor
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
name|child
range|:
name|children
control|)
block|{
name|registerPerformanceCounters
argument_list|(
name|routeContext
argument_list|,
name|child
argument_list|,
name|registeredCounters
argument_list|)
expr_stmt|;
block|}
comment|// skip processors that should not be registered
if|if
condition|(
operator|!
name|registerProcessor
argument_list|(
name|processor
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// okay this is a processor we would like to manage so create the
comment|// performance counter that is the base for processors
name|ManagedPerformanceCounter
name|pc
init|=
operator|new
name|ManagedPerformanceCounter
argument_list|(
name|getStrategy
argument_list|()
argument_list|)
decl_stmt|;
comment|// set statistics enabled depending on the option
name|boolean
name|enabled
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getStatisticsLevel
argument_list|()
operator|==
name|ManagementStatisticsLevel
operator|.
name|All
decl_stmt|;
name|pc
operator|.
name|setStatisticsEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
comment|// and add it as a a registered counter that will be used lazy when Camel
comment|// does the instrumentation of the route and adds the InstrumentationProcessor
comment|// that does the actual performance metrics gatherings at runtime
name|registeredCounters
operator|.
name|put
argument_list|(
name|processor
argument_list|,
name|pc
argument_list|)
expr_stmt|;
block|}
comment|/**      * Should the given processor be registered.      */
DECL|method|registerProcessor (ProcessorDefinition processor)
specifier|protected
name|boolean
name|registerProcessor
parameter_list|(
name|ProcessorDefinition
name|processor
parameter_list|)
block|{
comment|// skip on exception
if|if
condition|(
name|processor
operator|instanceof
name|OnExceptionDefinition
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// skip on completion
if|if
condition|(
name|processor
operator|instanceof
name|OnCompletionDefinition
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// skip intercept
if|if
condition|(
name|processor
operator|instanceof
name|InterceptDefinition
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// skip aop
if|if
condition|(
name|processor
operator|instanceof
name|AOPDefinition
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// skip policy
if|if
condition|(
name|processor
operator|instanceof
name|PolicyDefinition
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// only if custom id assigned
if|if
condition|(
name|getStrategy
argument_list|()
operator|.
name|isOnlyManageProcessorWithCustomId
argument_list|()
condition|)
block|{
return|return
name|processor
operator|.
name|hasCustomIdAssigned
argument_list|()
return|;
block|}
comment|// use customer filter
return|return
name|getStrategy
argument_list|()
operator|.
name|manageProcessor
argument_list|(
name|processor
argument_list|)
return|;
block|}
DECL|method|getStrategy ()
specifier|private
name|ManagementStrategy
name|getStrategy
parameter_list|()
block|{
return|return
name|context
operator|.
name|getManagementStrategy
argument_list|()
return|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{     }
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|initialized
operator|=
literal|false
expr_stmt|;
block|}
block|}
end_class

end_unit

