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
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

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
name|impl
operator|.
name|DefaultCamelContext
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
name|ServiceSupport
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
name|spi
operator|.
name|ClassResolver
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
name|InstrumentationAgent
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
name|InterceptStrategy
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
comment|/**  * JMX agent that registeres Camel lifecycle events in JMX.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|InstrumentationLifecycleStrategy
specifier|public
class|class
name|InstrumentationLifecycleStrategy
implements|implements
name|LifecycleStrategy
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|InstrumentationProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|MANAGED_RESOURCE_CLASSNAME
specifier|private
specifier|static
specifier|final
name|String
name|MANAGED_RESOURCE_CLASSNAME
init|=
literal|"org.springframework.jmx.export.annotation.ManagedResource"
decl_stmt|;
DECL|field|agent
specifier|private
name|InstrumentationAgent
name|agent
decl_stmt|;
DECL|field|namingStrategy
specifier|private
name|CamelNamingStrategy
name|namingStrategy
decl_stmt|;
DECL|field|initialized
specifier|private
name|boolean
name|initialized
decl_stmt|;
DECL|field|registeredRoutes
specifier|private
specifier|final
name|Map
argument_list|<
name|Endpoint
argument_list|,
name|InstrumentationProcessor
argument_list|>
name|registeredRoutes
init|=
operator|new
name|HashMap
argument_list|<
name|Endpoint
argument_list|,
name|InstrumentationProcessor
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|InstrumentationLifecycleStrategy ()
specifier|public
name|InstrumentationLifecycleStrategy
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|DefaultInstrumentationAgent
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|InstrumentationLifecycleStrategy (InstrumentationAgent agent)
specifier|public
name|InstrumentationLifecycleStrategy
parameter_list|(
name|InstrumentationAgent
name|agent
parameter_list|)
block|{
name|this
operator|.
name|agent
operator|=
name|agent
expr_stmt|;
block|}
comment|/**      * Constructor for camel context that has been started.      *      * @param agent    the agent      * @param context  the camel context      */
DECL|method|InstrumentationLifecycleStrategy (InstrumentationAgent agent, CamelContext context)
specifier|public
name|InstrumentationLifecycleStrategy
parameter_list|(
name|InstrumentationAgent
name|agent
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|agent
operator|=
name|agent
expr_stmt|;
name|onContextStart
argument_list|(
name|context
argument_list|)
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
comment|// register camel context
if|if
condition|(
name|context
operator|instanceof
name|DefaultCamelContext
condition|)
block|{
try|try
block|{
name|initialized
operator|=
literal|true
expr_stmt|;
name|DefaultCamelContext
name|dc
init|=
operator|(
name|DefaultCamelContext
operator|)
name|context
decl_stmt|;
comment|// call addService so that context will start and stop the agent
name|dc
operator|.
name|addService
argument_list|(
name|agent
argument_list|)
expr_stmt|;
name|namingStrategy
operator|=
operator|new
name|CamelNamingStrategy
argument_list|(
name|agent
operator|.
name|getMBeanObjectDomainName
argument_list|()
argument_list|)
expr_stmt|;
name|ManagedService
name|ms
init|=
operator|new
name|ManagedService
argument_list|(
name|dc
argument_list|)
decl_stmt|;
name|agent
operator|.
name|register
argument_list|(
name|ms
argument_list|,
name|getNamingStrategy
argument_list|()
operator|.
name|getObjectName
argument_list|(
name|dc
argument_list|)
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
block|}
comment|/**      * If the endpoint is an instance of ManagedResource then register it with the      * mbean server, if it is not then wrap the endpoint in a {@link ManagedEndpoint} and      * register that with the mbean server.      * @param endpoint the Endpoint attempted to be added      */
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
comment|// see if the spring-jmx is on the classpath
name|Class
name|annotationClass
init|=
name|resolveManagedAnnotation
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotationClass
operator|==
literal|null
condition|)
block|{
comment|// no its not so register the endpoint as a new managed endpoint
name|registerEndpointAsManagedEndpoint
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// see if the endpoint have been annotation with a spring JMX annotation
name|Object
name|annotation
init|=
name|endpoint
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|annotationClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotation
operator|==
literal|null
condition|)
block|{
comment|// no its not so register the endpoint as a new managed endpoint
name|registerEndpointAsManagedEndpoint
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// there is already a spring JMX annotation so attempt to register it
name|attemptToRegisterManagedResource
argument_list|(
name|endpoint
argument_list|,
name|annotation
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|resolveManagedAnnotation (Endpoint endpoint)
specifier|private
name|Class
name|resolveManagedAnnotation
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|CamelContext
name|context
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|ClassResolver
name|resolver
init|=
name|context
operator|.
name|getClassResolver
argument_list|()
decl_stmt|;
return|return
name|resolver
operator|.
name|resolveClass
argument_list|(
name|MANAGED_RESOURCE_CLASSNAME
argument_list|)
return|;
block|}
DECL|method|attemptToRegisterManagedResource (Endpoint endpoint, Object annotation)
specifier|private
name|void
name|attemptToRegisterManagedResource
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|annotation
parameter_list|)
block|{
try|try
block|{
name|Method
name|m
init|=
name|annotation
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"objectName"
argument_list|)
decl_stmt|;
name|String
name|objectNameStr
init|=
operator|(
name|String
operator|)
name|m
operator|.
name|invoke
argument_list|(
name|annotation
argument_list|)
decl_stmt|;
name|ObjectName
name|objectName
init|=
operator|new
name|ObjectName
argument_list|(
name|objectNameStr
argument_list|)
decl_stmt|;
name|agent
operator|.
name|register
argument_list|(
name|endpoint
argument_list|,
name|objectName
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
name|debug
argument_list|(
literal|"objectName method not present, wrapping endpoint in ManagedEndpoint instead"
argument_list|)
expr_stmt|;
name|registerEndpointAsManagedEndpoint
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|registerEndpointAsManagedEndpoint (Endpoint endpoint)
specifier|private
name|void
name|registerEndpointAsManagedEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
try|try
block|{
name|ManagedEndpoint
name|me
init|=
operator|new
name|ManagedEndpoint
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|agent
operator|.
name|register
argument_list|(
name|me
argument_list|,
name|getNamingStrategy
argument_list|()
operator|.
name|getObjectName
argument_list|(
name|me
argument_list|)
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
try|try
block|{
name|ManagedRoute
name|mr
init|=
operator|new
name|ManagedRoute
argument_list|(
name|route
argument_list|)
decl_stmt|;
comment|// retrieve the per-route intercept for this route
name|InstrumentationProcessor
name|processor
init|=
name|registeredRoutes
operator|.
name|get
argument_list|(
name|route
operator|.
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|processor
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Route has not been instrumented for endpoint: "
operator|+
name|route
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// let the instrumentation use our route counter
name|processor
operator|.
name|setCounter
argument_list|(
name|mr
argument_list|)
expr_stmt|;
block|}
name|agent
operator|.
name|register
argument_list|(
name|mr
argument_list|,
name|getNamingStrategy
argument_list|()
operator|.
name|getObjectName
argument_list|(
name|mr
argument_list|)
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
block|}
block|}
DECL|method|onServiceAdd (CamelContext context, Service service)
specifier|public
name|void
name|onServiceAdd
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|service
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
comment|// register consumer
if|if
condition|(
name|service
operator|instanceof
name|ServiceSupport
operator|&&
name|service
operator|instanceof
name|Consumer
condition|)
block|{
comment|// TODO: add support for non-consumer services?
try|try
block|{
name|ManagedService
name|ms
init|=
operator|new
name|ManagedService
argument_list|(
operator|(
name|ServiceSupport
operator|)
name|service
argument_list|)
decl_stmt|;
name|agent
operator|.
name|register
argument_list|(
name|ms
argument_list|,
name|getNamingStrategy
argument_list|()
operator|.
name|getObjectName
argument_list|(
name|context
argument_list|,
name|ms
argument_list|)
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
literal|"Could not register Service MBean"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
name|PerformanceCounter
argument_list|>
name|registeredCounters
init|=
operator|new
name|HashMap
argument_list|<
name|ProcessorDefinition
argument_list|,
name|PerformanceCounter
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
comment|// TODO: This only registers counters for the first outputs in the route
comment|// all the chidren of the outputs is not registered
comment|// we should leverge the Channel for this to ensure we register all processors
comment|// in the entire route graph
comment|// register all processors
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
continue|continue;
block|}
name|ObjectName
name|name
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// get the mbean name
name|name
operator|=
name|getNamingStrategy
argument_list|()
operator|.
name|getObjectName
argument_list|(
name|routeContext
argument_list|,
name|processor
argument_list|)
expr_stmt|;
comment|// register mbean wrapped in the performance counter mbean
name|PerformanceCounter
name|pc
init|=
operator|new
name|PerformanceCounter
argument_list|()
decl_stmt|;
name|agent
operator|.
name|register
argument_list|(
name|pc
argument_list|,
name|name
argument_list|)
expr_stmt|;
comment|// add to map now that it has been registered
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
catch|catch
parameter_list|(
name|MalformedObjectNameException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not create MBean name: "
operator|+
name|name
argument_list|,
name|e
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
literal|"Could not register PerformanceCounter MBean: "
operator|+
name|name
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|// add intercept strategy that executes the JMX instrumentation for performance metrics
name|routeContext
operator|.
name|addInterceptStrategy
argument_list|(
operator|new
name|InstrumentationInterceptStrategy
argument_list|(
name|registeredCounters
argument_list|)
argument_list|)
expr_stmt|;
comment|// instrument the route endpoint
specifier|final
name|Endpoint
name|endpoint
init|=
name|routeContext
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
comment|// only needed to register on the first output as all rotues will pass through this one
name|ProcessorDefinition
name|out
init|=
name|routeContext
operator|.
name|getRoute
argument_list|()
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// add an intercept strategy that counts when the route sends to any of its outputs
name|out
operator|.
name|addInterceptStrategy
argument_list|(
operator|new
name|InterceptStrategy
argument_list|()
block|{
specifier|public
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
name|ProcessorDefinition
name|processorDefinition
parameter_list|,
name|Processor
name|target
parameter_list|,
name|Processor
name|nextTarget
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|registeredRoutes
operator|.
name|containsKey
argument_list|(
name|endpoint
argument_list|)
condition|)
block|{
comment|// do not double wrap
return|return
name|target
return|;
block|}
name|InstrumentationProcessor
name|wrapper
init|=
operator|new
name|InstrumentationProcessor
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|wrapper
operator|.
name|setType
argument_list|(
name|processorDefinition
operator|.
name|getShortName
argument_list|()
argument_list|)
expr_stmt|;
name|wrapper
operator|.
name|setProcessor
argument_list|(
name|target
argument_list|)
expr_stmt|;
comment|// register our wrapper
name|registeredRoutes
operator|.
name|put
argument_list|(
name|endpoint
argument_list|,
name|wrapper
argument_list|)
expr_stmt|;
return|return
name|wrapper
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Instrument"
return|;
block|}
block|}
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
if|if
condition|(
name|agent
operator|instanceof
name|DefaultInstrumentationAgent
condition|)
block|{
name|DefaultInstrumentationAgent
name|dia
init|=
operator|(
name|DefaultInstrumentationAgent
operator|)
name|agent
decl_stmt|;
if|if
condition|(
name|dia
operator|.
name|getOnlyRegisterProcessorWithCustomId
argument_list|()
operator|!=
literal|null
operator|&&
name|dia
operator|.
name|getOnlyRegisterProcessorWithCustomId
argument_list|()
condition|)
block|{
comment|// only register if the processor have an explicy id assigned
return|return
name|processor
operator|.
name|hasCustomIdAssigned
argument_list|()
return|;
block|}
block|}
comment|// fallback to always register it
return|return
literal|true
return|;
block|}
DECL|method|getNamingStrategy ()
specifier|public
name|CamelNamingStrategy
name|getNamingStrategy
parameter_list|()
block|{
return|return
name|namingStrategy
return|;
block|}
DECL|method|setNamingStrategy (CamelNamingStrategy strategy)
specifier|public
name|void
name|setNamingStrategy
parameter_list|(
name|CamelNamingStrategy
name|strategy
parameter_list|)
block|{
name|this
operator|.
name|namingStrategy
operator|=
name|strategy
expr_stmt|;
block|}
DECL|method|setAgent (InstrumentationAgent agent)
specifier|public
name|void
name|setAgent
parameter_list|(
name|InstrumentationAgent
name|agent
parameter_list|)
block|{
name|this
operator|.
name|agent
operator|=
name|agent
expr_stmt|;
block|}
block|}
end_class

end_unit

