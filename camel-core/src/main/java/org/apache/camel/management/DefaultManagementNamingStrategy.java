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
name|net
operator|.
name|UnknownHostException
import|;
end_import

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
name|builder
operator|.
name|ErrorHandlerBuilderRef
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
name|ManagementNamingStrategy
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
name|InetAddressUtil
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

begin_comment
comment|/**  * Naming strategy used when registering MBeans.  */
end_comment

begin_class
DECL|class|DefaultManagementNamingStrategy
specifier|public
class|class
name|DefaultManagementNamingStrategy
implements|implements
name|ManagementNamingStrategy
block|{
DECL|field|VALUE_UNKNOWN
specifier|public
specifier|static
specifier|final
name|String
name|VALUE_UNKNOWN
init|=
literal|"unknown"
decl_stmt|;
DECL|field|KEY_NAME
specifier|public
specifier|static
specifier|final
name|String
name|KEY_NAME
init|=
literal|"name"
decl_stmt|;
DECL|field|KEY_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|KEY_TYPE
init|=
literal|"type"
decl_stmt|;
DECL|field|KEY_CONTEXT
specifier|public
specifier|static
specifier|final
name|String
name|KEY_CONTEXT
init|=
literal|"context"
decl_stmt|;
DECL|field|TYPE_CONTEXT
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_CONTEXT
init|=
literal|"context"
decl_stmt|;
DECL|field|TYPE_ENDPOINT
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_ENDPOINT
init|=
literal|"endpoints"
decl_stmt|;
DECL|field|TYPE_PROCESSOR
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_PROCESSOR
init|=
literal|"processors"
decl_stmt|;
DECL|field|TYPE_CONSUMER
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_CONSUMER
init|=
literal|"consumers"
decl_stmt|;
DECL|field|TYPE_PRODUCER
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_PRODUCER
init|=
literal|"producers"
decl_stmt|;
DECL|field|TYPE_ROUTE
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_ROUTE
init|=
literal|"routes"
decl_stmt|;
DECL|field|TYPE_COMPONENT
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_COMPONENT
init|=
literal|"components"
decl_stmt|;
DECL|field|TYPE_TRACER
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_TRACER
init|=
literal|"tracer"
decl_stmt|;
DECL|field|TYPE_EVENT_NOTIFIER
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_EVENT_NOTIFIER
init|=
literal|"eventnotifiers"
decl_stmt|;
DECL|field|TYPE_ERRORHANDLER
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_ERRORHANDLER
init|=
literal|"errorhandlers"
decl_stmt|;
DECL|field|TYPE_THREAD_POOL
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_THREAD_POOL
init|=
literal|"threadpools"
decl_stmt|;
DECL|field|TYPE_SERVICE
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_SERVICE
init|=
literal|"services"
decl_stmt|;
DECL|field|domainName
specifier|protected
name|String
name|domainName
decl_stmt|;
DECL|field|hostName
specifier|protected
name|String
name|hostName
init|=
literal|"localhost"
decl_stmt|;
DECL|method|DefaultManagementNamingStrategy ()
specifier|public
name|DefaultManagementNamingStrategy
parameter_list|()
block|{
name|this
argument_list|(
literal|"org.apache.camel"
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultManagementNamingStrategy (String domainName)
specifier|public
name|DefaultManagementNamingStrategy
parameter_list|(
name|String
name|domainName
parameter_list|)
block|{
if|if
condition|(
name|domainName
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|domainName
operator|=
name|domainName
expr_stmt|;
block|}
try|try
block|{
name|hostName
operator|=
name|InetAddressUtil
operator|.
name|getLocalHostName
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownHostException
name|ex
parameter_list|)
block|{
comment|// ignore, use the default "localhost"
block|}
block|}
DECL|method|getObjectNameForCamelContext (String name)
specifier|public
name|ObjectName
name|getObjectNameForCamelContext
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|MalformedObjectNameException
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getContextId
argument_list|(
name|name
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
operator|+
name|TYPE_CONTEXT
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|ObjectName
operator|.
name|quote
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
DECL|method|getObjectNameForCamelContext (CamelContext context)
specifier|public
name|ObjectName
name|getObjectNameForCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|MalformedObjectNameException
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getContextId
argument_list|(
name|context
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
operator|+
name|TYPE_CONTEXT
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|ObjectName
operator|.
name|quote
argument_list|(
name|context
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
DECL|method|getObjectNameForEndpoint (Endpoint endpoint)
specifier|public
name|ObjectName
name|getObjectNameForEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
throws|throws
name|MalformedObjectNameException
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getContextId
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
operator|+
name|TYPE_ENDPOINT
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|ObjectName
operator|.
name|quote
argument_list|(
name|getEndpointId
argument_list|(
name|endpoint
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
DECL|method|getObjectNameForComponent (Component component, String name)
specifier|public
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
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getContextId
argument_list|(
name|component
operator|.
name|getCamelContext
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
operator|+
name|TYPE_COMPONENT
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|ObjectName
operator|.
name|quote
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
DECL|method|getObjectNameForProcessor (CamelContext context, Processor processor, ProcessorDefinition<?> definition)
specifier|public
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
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
throws|throws
name|MalformedObjectNameException
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getContextId
argument_list|(
name|context
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|TYPE_PROCESSOR
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|ObjectName
operator|.
name|quote
argument_list|(
name|definition
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
DECL|method|getObjectNameForErrorHandler (RouteContext routeContext, Processor errorHandler, ErrorHandlerBuilder builder)
specifier|public
name|ObjectName
name|getObjectNameForErrorHandler
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|errorHandler
parameter_list|,
name|ErrorHandlerBuilder
name|builder
parameter_list|)
throws|throws
name|MalformedObjectNameException
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getContextId
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|TYPE_ERRORHANDLER
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
comment|// we want to only register one instance of the various error handler types and thus do some lookup
comment|// if its a ErrorHandlerBuildRef. We need a bit of work to do that as there are potential indirection.
name|String
name|ref
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|builder
operator|instanceof
name|ErrorHandlerBuilderRef
condition|)
block|{
name|ErrorHandlerBuilderRef
name|builderRef
init|=
operator|(
name|ErrorHandlerBuilderRef
operator|)
name|builder
decl_stmt|;
comment|// it has not then its an indirection and we should do some work to lookup the real builder
name|ref
operator|=
name|builderRef
operator|.
name|getRef
argument_list|()
expr_stmt|;
name|builder
operator|=
name|ErrorHandlerBuilderRef
operator|.
name|lookupErrorHandlerBuilder
argument_list|(
name|routeContext
argument_list|,
name|builderRef
operator|.
name|getRef
argument_list|()
argument_list|)
expr_stmt|;
comment|// must do a 2nd lookup in case this is also a reference
comment|// (this happens with spring DSL using errorHandlerRef on<route> as it gets a bit
comment|// complex with indirections for error handler references
if|if
condition|(
name|builder
operator|instanceof
name|ErrorHandlerBuilderRef
condition|)
block|{
name|builderRef
operator|=
operator|(
name|ErrorHandlerBuilderRef
operator|)
name|builder
expr_stmt|;
comment|// does it refer to a non default error handler then do a 2nd lookup
if|if
condition|(
operator|!
name|builderRef
operator|.
name|getRef
argument_list|()
operator|.
name|equals
argument_list|(
name|ErrorHandlerBuilderRef
operator|.
name|DEFAULT_ERROR_HANDLER_BUILDER
argument_list|)
condition|)
block|{
name|builder
operator|=
name|ErrorHandlerBuilderRef
operator|.
name|lookupErrorHandlerBuilder
argument_list|(
name|routeContext
argument_list|,
name|builderRef
operator|.
name|getRef
argument_list|()
argument_list|)
expr_stmt|;
name|ref
operator|=
name|builderRef
operator|.
name|getRef
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|builder
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"(ref:"
operator|+
name|ref
operator|+
literal|")"
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|ObjectName
operator|.
name|quote
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// create a name based on its instance
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|builder
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
operator|.
name|append
argument_list|(
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|builder
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
DECL|method|getObjectNameForConsumer (CamelContext context, Consumer consumer)
specifier|public
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
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getContextId
argument_list|(
name|context
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|TYPE_CONSUMER
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|consumer
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|name
operator|=
literal|"Consumer"
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
operator|.
name|append
argument_list|(
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|consumer
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
DECL|method|getObjectNameForProducer (CamelContext context, Producer producer)
specifier|public
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
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getContextId
argument_list|(
name|context
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|TYPE_PRODUCER
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|producer
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|name
operator|=
literal|"Producer"
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
operator|.
name|append
argument_list|(
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|producer
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
DECL|method|getObjectNameForTracer (CamelContext context, InterceptStrategy tracer)
specifier|public
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
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getContextId
argument_list|(
name|context
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
operator|+
name|TYPE_TRACER
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
literal|"Tracer"
argument_list|)
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
operator|.
name|append
argument_list|(
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|tracer
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
DECL|method|getObjectNameForEventNotifier (CamelContext context, EventNotifier eventNotifier)
specifier|public
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
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getContextId
argument_list|(
name|context
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
operator|+
name|TYPE_EVENT_NOTIFIER
operator|+
literal|","
argument_list|)
expr_stmt|;
if|if
condition|(
name|eventNotifier
operator|instanceof
name|JmxNotificationEventNotifier
condition|)
block|{
comment|// JMX notifier shall have an easy to use name
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
literal|"JmxEventNotifier"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// others can be per instance
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
literal|"EventNotifier"
argument_list|)
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
operator|.
name|append
argument_list|(
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|eventNotifier
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
DECL|method|getObjectNameForRoute (Route route)
specifier|public
name|ObjectName
name|getObjectNameForRoute
parameter_list|(
name|Route
name|route
parameter_list|)
throws|throws
name|MalformedObjectNameException
block|{
name|Endpoint
name|ep
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|route
operator|.
name|getId
argument_list|()
decl_stmt|;
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getContextId
argument_list|(
name|ep
operator|.
name|getCamelContext
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
operator|+
name|TYPE_ROUTE
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|ObjectName
operator|.
name|quote
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
DECL|method|getObjectNameForService (CamelContext context, Service service)
specifier|public
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
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getContextId
argument_list|(
name|context
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
operator|+
name|TYPE_SERVICE
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|service
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
operator|.
name|append
argument_list|(
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|service
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
DECL|method|getObjectNameForThreadPool (CamelContext context, ThreadPoolExecutor threadPool, String id, String sourceId)
specifier|public
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
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getContextId
argument_list|(
name|context
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
operator|+
name|TYPE_THREAD_POOL
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
expr_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
comment|// if no id then use class name as id
name|buffer
operator|.
name|append
argument_list|(
name|threadPool
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|id
operator|==
literal|null
operator|&&
name|sourceId
operator|==
literal|null
condition|)
block|{
comment|// if we don't really know the id or source id then use instance hashcode so the name is unique
comment|// for this particular thread pool
name|buffer
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
operator|.
name|append
argument_list|(
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|threadPool
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sourceId
operator|!=
literal|null
condition|)
block|{
comment|// provide source if we know it, this helps end user to know where the pool is used
name|buffer
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
operator|.
name|append
argument_list|(
name|sourceId
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
DECL|method|getDomainName ()
specifier|public
name|String
name|getDomainName
parameter_list|()
block|{
return|return
name|domainName
return|;
block|}
DECL|method|setDomainName (String domainName)
specifier|public
name|void
name|setDomainName
parameter_list|(
name|String
name|domainName
parameter_list|)
block|{
name|this
operator|.
name|domainName
operator|=
name|domainName
expr_stmt|;
block|}
DECL|method|getHostName ()
specifier|public
name|String
name|getHostName
parameter_list|()
block|{
return|return
name|hostName
return|;
block|}
DECL|method|setHostName (String hostName)
specifier|public
name|void
name|setHostName
parameter_list|(
name|String
name|hostName
parameter_list|)
block|{
name|this
operator|.
name|hostName
operator|=
name|hostName
expr_stmt|;
block|}
DECL|method|getContextId (CamelContext context)
specifier|protected
name|String
name|getContextId
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
return|return
name|getContextId
argument_list|(
name|VALUE_UNKNOWN
argument_list|)
return|;
block|}
else|else
block|{
name|String
name|name
init|=
name|context
operator|.
name|getManagementName
argument_list|()
operator|!=
literal|null
condition|?
name|context
operator|.
name|getManagementName
argument_list|()
else|:
name|context
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
name|getContextId
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
DECL|method|getContextId (String name)
specifier|protected
name|String
name|getContextId
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|hostName
operator|+
literal|"/"
operator|+
operator|(
name|name
operator|!=
literal|null
condition|?
name|name
else|:
name|VALUE_UNKNOWN
operator|)
return|;
block|}
DECL|method|getEndpointId (Endpoint ep)
specifier|protected
name|String
name|getEndpointId
parameter_list|(
name|Endpoint
name|ep
parameter_list|)
block|{
if|if
condition|(
name|ep
operator|.
name|isSingleton
argument_list|()
condition|)
block|{
return|return
name|ep
operator|.
name|getEndpointKey
argument_list|()
return|;
block|}
else|else
block|{
comment|// non singleton then add hashcoded id
name|String
name|uri
init|=
name|ep
operator|.
name|getEndpointKey
argument_list|()
decl_stmt|;
name|int
name|pos
init|=
name|uri
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
decl_stmt|;
name|String
name|id
init|=
operator|(
name|pos
operator|==
operator|-
literal|1
operator|)
condition|?
name|uri
else|:
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
decl_stmt|;
name|id
operator|+=
literal|"?id="
operator|+
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|ep
argument_list|)
expr_stmt|;
return|return
name|id
return|;
block|}
block|}
comment|/**      * Factory method to create an ObjectName escaping any required characters      */
DECL|method|createObjectName (StringBuilder buffer)
specifier|protected
name|ObjectName
name|createObjectName
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|)
throws|throws
name|MalformedObjectNameException
block|{
name|String
name|text
init|=
name|buffer
operator|.
name|toString
argument_list|()
decl_stmt|;
try|try
block|{
return|return
operator|new
name|ObjectName
argument_list|(
name|text
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
operator|new
name|MalformedObjectNameException
argument_list|(
literal|"Could not create ObjectName from: "
operator|+
name|text
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

