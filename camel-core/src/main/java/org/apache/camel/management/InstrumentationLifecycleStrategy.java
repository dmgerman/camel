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
name|ArrayList
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
name|ExceptionType
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
comment|// A map (Endpoint -> InstrumentationProcessor) to facilitate
comment|// adding per-route interceptor and registering ManagedRoute MBean
DECL|field|interceptorMap
specifier|private
name|Map
argument_list|<
name|Endpoint
argument_list|,
name|InstrumentationProcessor
argument_list|>
name|interceptorMap
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
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not register CamelContext MBean"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|onEndpointAdd (Endpoint<? extends Exchange> endpoint)
specifier|public
name|void
name|onEndpointAdd
parameter_list|(
name|Endpoint
argument_list|<
name|?
extends|extends
name|Exchange
argument_list|>
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
literal|"Could not register Endpoint MBean"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
name|interceptor
init|=
name|interceptorMap
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
name|interceptor
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Instrumentation processor not found for route endpoint "
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
name|interceptor
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
name|ProcessorType
argument_list|,
name|PerformanceCounter
argument_list|>
name|counterMap
init|=
operator|new
name|HashMap
argument_list|<
name|ProcessorType
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
name|RouteType
name|route
init|=
name|routeContext
operator|.
name|getRoute
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessorType
name|processor
range|:
name|route
operator|.
name|getOutputs
argument_list|()
control|)
block|{
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
name|counterMap
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
name|routeContext
operator|.
name|addInterceptStrategy
argument_list|(
operator|new
name|InstrumentationInterceptStrategy
argument_list|(
name|counterMap
argument_list|)
argument_list|)
expr_stmt|;
name|routeContext
operator|.
name|setErrorHandlerWrappingStrategy
argument_list|(
operator|new
name|InstrumentationErrorHandlerWrappingStrategy
argument_list|(
name|counterMap
argument_list|)
argument_list|)
expr_stmt|;
comment|// Add an InstrumentationProcessor at the beginning of each route and
comment|// set up the interceptorMap for onRoutesAdd() method to register the
comment|// ManagedRoute MBeans.
name|RouteType
name|routeType
init|=
name|routeContext
operator|.
name|getRoute
argument_list|()
decl_stmt|;
if|if
condition|(
name|routeType
operator|.
name|getInputs
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|routeType
operator|.
name|getInputs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|routeType
operator|.
name|getInputs
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Add InstrumentationProcessor to first input only."
argument_list|)
expr_stmt|;
block|}
name|Endpoint
name|endpoint
init|=
name|routeType
operator|.
name|getInputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|exceptionHandlers
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|// separate out the exception handers in the outputs
for|for
control|(
name|ProcessorType
name|output
range|:
name|routeType
operator|.
name|getOutputs
argument_list|()
control|)
block|{
if|if
condition|(
name|output
operator|instanceof
name|ExceptionType
condition|)
block|{
name|exceptionHandlers
operator|.
name|add
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|outputs
operator|.
name|add
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
block|}
comment|// clearing the outputs
name|routeType
operator|.
name|clearOutput
argument_list|()
expr_stmt|;
comment|// add exception handlers as top children
name|routeType
operator|.
name|getOutputs
argument_list|()
operator|.
name|addAll
argument_list|(
name|exceptionHandlers
argument_list|)
expr_stmt|;
comment|// add an interceptor
name|InstrumentationProcessor
name|processor
init|=
operator|new
name|InstrumentationProcessor
argument_list|()
decl_stmt|;
name|routeType
operator|.
name|intercept
argument_list|(
name|processor
argument_list|)
expr_stmt|;
comment|// add the output
for|for
control|(
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|processorType
range|:
name|outputs
control|)
block|{
name|routeType
operator|.
name|addOutput
argument_list|(
name|processorType
argument_list|)
expr_stmt|;
block|}
name|interceptorMap
operator|.
name|put
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
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

