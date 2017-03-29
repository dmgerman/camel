begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
package|;
end_package

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
name|ExchangePattern
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreamsService
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreamsServiceFactory
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|DispatchCallback
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
name|reactive
operator|.
name|streams
operator|.
name|engine
operator|.
name|ReactiveStreamsEngineConfiguration
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
name|DefaultExchange
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
name|FactoryFinder
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
name|CamelContextHelper
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

begin_class
DECL|class|ReactiveStreamsHelper
specifier|public
specifier|final
class|class
name|ReactiveStreamsHelper
block|{
DECL|method|ReactiveStreamsHelper ()
specifier|private
name|ReactiveStreamsHelper
parameter_list|()
block|{     }
DECL|method|getCallback (Exchange exchange)
specifier|public
specifier|static
name|DispatchCallback
argument_list|<
name|Exchange
argument_list|>
name|getCallback
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ReactiveStreamsConstants
operator|.
name|REACTIVE_STREAMS_CALLBACK
argument_list|,
name|DispatchCallback
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|attachCallback (Exchange exchange, DispatchCallback<Exchange> callback)
specifier|public
specifier|static
name|DispatchCallback
argument_list|<
name|Exchange
argument_list|>
name|attachCallback
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|DispatchCallback
argument_list|<
name|Exchange
argument_list|>
name|callback
parameter_list|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ReactiveStreamsConstants
operator|.
name|REACTIVE_STREAMS_CALLBACK
argument_list|,
name|callback
argument_list|)
expr_stmt|;
return|return
name|callback
return|;
block|}
DECL|method|detachCallback (Exchange exchange)
specifier|public
specifier|static
name|DispatchCallback
argument_list|<
name|Exchange
argument_list|>
name|detachCallback
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|DispatchCallback
argument_list|<
name|Exchange
argument_list|>
name|callback
init|=
name|getCallback
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|callback
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|ReactiveStreamsConstants
operator|.
name|REACTIVE_STREAMS_CALLBACK
argument_list|)
expr_stmt|;
block|}
return|return
name|callback
return|;
block|}
DECL|method|invokeDispatchCallback (Exchange exchange)
specifier|public
specifier|static
name|boolean
name|invokeDispatchCallback
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|invokeDispatchCallback
argument_list|(
name|exchange
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|invokeDispatchCallback (Exchange exchange, Throwable error)
specifier|public
specifier|static
name|boolean
name|invokeDispatchCallback
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|error
parameter_list|)
block|{
name|DispatchCallback
argument_list|<
name|Exchange
argument_list|>
name|callback
init|=
name|getCallback
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|callback
operator|!=
literal|null
condition|)
block|{
name|callback
operator|.
name|processed
argument_list|(
name|exchange
argument_list|,
name|error
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|convertToExchange (CamelContext context, Object data)
specifier|public
specifier|static
name|Exchange
name|convertToExchange
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Object
name|data
parameter_list|)
block|{
name|Exchange
name|exchange
decl_stmt|;
if|if
condition|(
name|data
operator|instanceof
name|Exchange
condition|)
block|{
name|exchange
operator|=
operator|(
name|Exchange
operator|)
name|data
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
DECL|method|findInstance (CamelContext context, String name, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|findInstance
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|?
name|CamelContextHelper
operator|.
name|findByType
argument_list|(
name|context
argument_list|,
name|type
argument_list|)
else|:
name|CamelContextHelper
operator|.
name|lookup
argument_list|(
name|context
argument_list|,
name|name
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      * Helper to lookup/create an instance of {@link CamelReactiveStreamsService}      */
DECL|method|resolveReactiveStreamsService (CamelContext context, String serviceType, ReactiveStreamsEngineConfiguration configuration)
specifier|public
specifier|static
name|CamelReactiveStreamsService
name|resolveReactiveStreamsService
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|serviceType
parameter_list|,
name|ReactiveStreamsEngineConfiguration
name|configuration
parameter_list|)
block|{
comment|// First try to find out if a service has already been bound to the registry
name|CamelReactiveStreamsService
name|service
init|=
name|ReactiveStreamsHelper
operator|.
name|findInstance
argument_list|(
name|context
argument_list|,
name|serviceType
argument_list|,
name|CamelReactiveStreamsService
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|!=
literal|null
condition|)
block|{
comment|// If the service is bound to the registry we assume it is already
comment|// configured so let's return it as it is.
return|return
name|service
return|;
block|}
else|else
block|{
comment|// Then try to find out if a service factory is bound to the registry
name|CamelReactiveStreamsServiceFactory
name|factory
init|=
name|ReactiveStreamsHelper
operator|.
name|findInstance
argument_list|(
name|context
argument_list|,
name|serviceType
argument_list|,
name|CamelReactiveStreamsServiceFactory
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
comment|// Try to find out a service factory with service loader style
comment|// using the provided service with fallback to default one
name|factory
operator|=
name|resolveServiceFactory
argument_list|(
name|context
argument_list|,
name|serviceType
operator|!=
literal|null
condition|?
name|serviceType
else|:
name|ReactiveStreamsConstants
operator|.
name|DEFAULT_SERVICE_NAME
argument_list|)
expr_stmt|;
block|}
return|return
name|factory
operator|.
name|newInstance
argument_list|(
name|context
argument_list|,
name|configuration
argument_list|)
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|resolveServiceFactory (CamelContext context, String serviceType)
specifier|public
specifier|static
name|CamelReactiveStreamsServiceFactory
name|resolveServiceFactory
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|serviceType
parameter_list|)
block|{
try|try
block|{
name|FactoryFinder
name|finder
init|=
name|context
operator|.
name|getFactoryFinder
argument_list|(
name|ReactiveStreamsConstants
operator|.
name|SERVICE_PATH
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|serviceClass
init|=
name|finder
operator|.
name|findClass
argument_list|(
name|serviceType
argument_list|)
decl_stmt|;
return|return
operator|(
name|CamelReactiveStreamsServiceFactory
operator|)
name|context
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|serviceClass
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Class referenced in '"
operator|+
name|ReactiveStreamsConstants
operator|.
name|SERVICE_PATH
operator|+
name|serviceType
operator|+
literal|"' not found"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to create the reactive stream service defined in '"
operator|+
name|ReactiveStreamsConstants
operator|.
name|SERVICE_PATH
operator|+
name|serviceType
operator|+
literal|"'"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

