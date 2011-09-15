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
name|DelegateProcessor
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
name|component
operator|.
name|bean
operator|.
name|BeanProcessor
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
name|ManagedBeanProcessor
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
name|ManagedEventNotifier
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
name|ManagedSuspendableRoute
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
name|ManagedThreadPool
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
name|EventNotifier
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
name|ManagementObjectStrategy
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|DefaultManagementObjectStrategy
specifier|public
class|class
name|DefaultManagementObjectStrategy
implements|implements
name|ManagementObjectStrategy
block|{
DECL|method|getManagedObjectForCamelContext (CamelContext context)
specifier|public
name|Object
name|getManagedObjectForCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|ManagedCamelContext
name|mc
init|=
operator|new
name|ManagedCamelContext
argument_list|(
operator|(
name|ModelCamelContext
operator|)
name|context
argument_list|)
decl_stmt|;
name|mc
operator|.
name|init
argument_list|(
name|context
operator|.
name|getManagementStrategy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|mc
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"deprecation"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|getManagedObjectForComponent (CamelContext context, Component component, String name)
specifier|public
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
block|{
if|if
condition|(
name|component
operator|instanceof
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|ManagementAware
condition|)
block|{
return|return
operator|(
operator|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|ManagementAware
argument_list|<
name|Component
argument_list|>
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
name|ManagedComponent
name|mc
init|=
operator|new
name|ManagedComponent
argument_list|(
name|name
argument_list|,
name|component
argument_list|)
decl_stmt|;
name|mc
operator|.
name|init
argument_list|(
name|context
operator|.
name|getManagementStrategy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|mc
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"deprecation"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|getManagedObjectForEndpoint (CamelContext context, Endpoint endpoint)
specifier|public
name|Object
name|getManagedObjectForEndpoint
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
comment|// we only want to manage singleton endpoints
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isSingleton
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|endpoint
operator|instanceof
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|ManagementAware
condition|)
block|{
return|return
operator|(
operator|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|ManagementAware
argument_list|<
name|Endpoint
argument_list|>
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
name|ManagedBrowsableEndpoint
name|me
init|=
operator|new
name|ManagedBrowsableEndpoint
argument_list|(
operator|(
name|BrowsableEndpoint
operator|)
name|endpoint
argument_list|)
decl_stmt|;
name|me
operator|.
name|init
argument_list|(
name|context
operator|.
name|getManagementStrategy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|me
return|;
block|}
else|else
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
name|me
operator|.
name|init
argument_list|(
name|context
operator|.
name|getManagementStrategy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|me
return|;
block|}
block|}
DECL|method|getManagedObjectForErrorHandler (CamelContext context, RouteContext routeContext, Processor errorHandler, ErrorHandlerFactory errorHandlerBuilder)
specifier|public
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
block|{
name|ManagedErrorHandler
name|me
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
name|me
operator|.
name|init
argument_list|(
name|context
operator|.
name|getManagementStrategy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|me
return|;
block|}
DECL|method|getManagedObjectForRoute (CamelContext context, Route route)
specifier|public
name|Object
name|getManagedObjectForRoute
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
name|ManagedRoute
name|mr
decl_stmt|;
if|if
condition|(
name|route
operator|.
name|supportsSuspension
argument_list|()
condition|)
block|{
name|mr
operator|=
operator|new
name|ManagedSuspendableRoute
argument_list|(
operator|(
name|ModelCamelContext
operator|)
name|context
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mr
operator|=
operator|new
name|ManagedRoute
argument_list|(
operator|(
name|ModelCamelContext
operator|)
name|context
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
name|mr
operator|.
name|init
argument_list|(
name|context
operator|.
name|getManagementStrategy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|mr
return|;
block|}
DECL|method|getManagedObjectForThreadPool (CamelContext context, ThreadPoolExecutor threadPool, String id, String sourceId, String routeId, String threadPoolProfileId)
specifier|public
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
block|{
name|ManagedThreadPool
name|mtp
init|=
operator|new
name|ManagedThreadPool
argument_list|(
name|context
argument_list|,
name|threadPool
argument_list|,
name|id
argument_list|,
name|sourceId
argument_list|,
name|routeId
argument_list|,
name|threadPoolProfileId
argument_list|)
decl_stmt|;
name|mtp
operator|.
name|init
argument_list|(
name|context
operator|.
name|getManagementStrategy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|mtp
return|;
block|}
DECL|method|getManagedObjectForEventNotifier (CamelContext context, EventNotifier eventNotifier)
specifier|public
name|Object
name|getManagedObjectForEventNotifier
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|EventNotifier
name|eventNotifier
parameter_list|)
block|{
name|ManagedEventNotifier
name|men
init|=
operator|new
name|ManagedEventNotifier
argument_list|(
name|context
argument_list|,
name|eventNotifier
argument_list|)
decl_stmt|;
name|men
operator|.
name|init
argument_list|(
name|context
operator|.
name|getManagementStrategy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|men
return|;
block|}
DECL|method|getManagedObjectForConsumer (CamelContext context, Consumer consumer)
specifier|public
name|Object
name|getManagedObjectForConsumer
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Consumer
name|consumer
parameter_list|)
block|{
name|ManagedConsumer
name|mc
decl_stmt|;
if|if
condition|(
name|consumer
operator|instanceof
name|ScheduledPollConsumer
condition|)
block|{
name|mc
operator|=
operator|new
name|ManagedScheduledPollConsumer
argument_list|(
name|context
argument_list|,
operator|(
name|ScheduledPollConsumer
operator|)
name|consumer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mc
operator|=
operator|new
name|ManagedConsumer
argument_list|(
name|context
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
name|mc
operator|.
name|init
argument_list|(
name|context
operator|.
name|getManagementStrategy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|mc
return|;
block|}
DECL|method|getManagedObjectForProducer (CamelContext context, Producer producer)
specifier|public
name|Object
name|getManagedObjectForProducer
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Producer
name|producer
parameter_list|)
block|{
name|ManagedProducer
name|mp
init|=
operator|new
name|ManagedProducer
argument_list|(
name|context
argument_list|,
name|producer
argument_list|)
decl_stmt|;
name|mp
operator|.
name|init
argument_list|(
name|context
operator|.
name|getManagementStrategy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|mp
return|;
block|}
DECL|method|getManagedObjectForService (CamelContext context, Service service)
specifier|public
name|Object
name|getManagedObjectForService
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|service
parameter_list|)
block|{
name|ManagedService
name|mc
init|=
operator|new
name|ManagedService
argument_list|(
name|context
argument_list|,
name|service
argument_list|)
decl_stmt|;
name|mc
operator|.
name|init
argument_list|(
name|context
operator|.
name|getManagementStrategy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|mc
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"deprecation"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|getManagedObjectForProcessor (CamelContext context, Processor processor, ProcessorDefinition definition, Route route)
specifier|public
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
name|definition
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
name|ManagedProcessor
name|answer
init|=
literal|null
decl_stmt|;
comment|// unwrap delegates as we want the real target processor
name|Processor
name|target
init|=
name|processor
decl_stmt|;
while|while
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
comment|// skip error handlers
if|if
condition|(
name|target
operator|instanceof
name|ErrorHandler
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// look for specialized processor which we should prefer to use
if|if
condition|(
name|target
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
name|target
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|target
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
name|target
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|target
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
name|target
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|target
operator|instanceof
name|BeanProcessor
condition|)
block|{
name|answer
operator|=
operator|new
name|ManagedBeanProcessor
argument_list|(
name|context
argument_list|,
operator|(
name|BeanProcessor
operator|)
name|target
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|target
operator|instanceof
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|ManagementAware
condition|)
block|{
return|return
operator|(
operator|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|ManagementAware
argument_list|<
name|Processor
argument_list|>
operator|)
name|target
operator|)
operator|.
name|getManagedObject
argument_list|(
name|processor
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
comment|// break out as we found an answer
break|break;
block|}
comment|// no answer yet, so unwrap any delegates and try again
if|if
condition|(
name|target
operator|instanceof
name|DelegateProcessor
condition|)
block|{
name|target
operator|=
operator|(
operator|(
name|DelegateProcessor
operator|)
name|target
operator|)
operator|.
name|getProcessor
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// no delegate so we dont have any target to try next
break|break;
block|}
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
name|target
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
name|answer
operator|.
name|init
argument_list|(
name|context
operator|.
name|getManagementStrategy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

