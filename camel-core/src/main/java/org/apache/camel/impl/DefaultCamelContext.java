begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|*
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
name|RouteBuilder
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
name|converter
operator|.
name|DefaultTypeConverter
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
name|ComponentResolver
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
name|ExchangeConverter
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
name|Injector
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
name|NoFactoryAvailableException
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
name|camel
operator|.
name|util
operator|.
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_comment
comment|/**  * Represents the context used to configure routes and the policies to use.  *  * @version $Revision: 520517 $  * @org.apache.xbean.XBean element="container" rootElement="true"  */
end_comment

begin_class
DECL|class|DefaultCamelContext
specifier|public
class|class
name|DefaultCamelContext
extends|extends
name|ServiceSupport
implements|implements
name|CamelContext
implements|,
name|Service
block|{
DECL|field|endpoints
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Endpoint
argument_list|>
name|endpoints
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Endpoint
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|components
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Component
argument_list|>
name|components
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Component
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|routes
specifier|private
name|List
argument_list|<
name|Route
argument_list|>
name|routes
decl_stmt|;
DECL|field|servicesToClose
specifier|private
name|List
argument_list|<
name|Service
argument_list|>
name|servicesToClose
init|=
operator|new
name|ArrayList
argument_list|<
name|Service
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|typeConverter
specifier|private
name|TypeConverter
name|typeConverter
decl_stmt|;
DECL|field|exchangeConverter
specifier|private
name|ExchangeConverter
name|exchangeConverter
decl_stmt|;
DECL|field|injector
specifier|private
name|Injector
name|injector
decl_stmt|;
DECL|field|componentResolver
specifier|private
name|ComponentResolver
name|componentResolver
decl_stmt|;
DECL|field|autoCreateComponents
specifier|private
name|boolean
name|autoCreateComponents
init|=
literal|true
decl_stmt|;
comment|/**      * Adds a component to the container.      */
DECL|method|addComponent (String componentName, final Component component)
specifier|public
name|void
name|addComponent
parameter_list|(
name|String
name|componentName
parameter_list|,
specifier|final
name|Component
name|component
parameter_list|)
block|{
synchronized|synchronized
init|(
name|components
init|)
block|{
if|if
condition|(
name|components
operator|.
name|containsKey
argument_list|(
name|componentName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Component previously added: "
operator|+
name|componentName
argument_list|)
throw|;
block|}
name|component
operator|.
name|setCamelContext
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|components
operator|.
name|put
argument_list|(
name|componentName
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getComponent (String name)
specifier|public
name|Component
name|getComponent
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// synchronize the look up and auto create so that 2 threads can't
comment|// concurrently auto create the same component.
synchronized|synchronized
init|(
name|components
init|)
block|{
name|Component
name|component
init|=
name|components
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|component
operator|==
literal|null
operator|&&
name|autoCreateComponents
condition|)
block|{
try|try
block|{
name|component
operator|=
name|getComponentResolver
argument_list|()
operator|.
name|resolveComponent
argument_list|(
name|name
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|addComponent
argument_list|(
name|name
argument_list|,
name|component
argument_list|)
expr_stmt|;
if|if
condition|(
name|isStarted
argument_list|()
condition|)
block|{
comment|// If the component is looked up after the context is started,
comment|// lets start it up.
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|component
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
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Could not auto create component: "
operator|+
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|component
return|;
block|}
block|}
comment|/**      * Removes a previously added component.      *      * @param componentName      * @return the previously added component or null if it had not been previously added.      */
DECL|method|removeComponent (String componentName)
specifier|public
name|Component
name|removeComponent
parameter_list|(
name|String
name|componentName
parameter_list|)
block|{
synchronized|synchronized
init|(
name|components
init|)
block|{
return|return
name|components
operator|.
name|remove
argument_list|(
name|componentName
argument_list|)
return|;
block|}
block|}
comment|/**      * Gets the a previously added component by name or lazily creates the component      * using the factory Callback.      *      * @param componentName      * @param factory       used to create a new component instance if the component was not previously added.      * @return      */
DECL|method|getOrCreateComponent (String componentName, Callable<Component> factory)
specifier|public
name|Component
name|getOrCreateComponent
parameter_list|(
name|String
name|componentName
parameter_list|,
name|Callable
argument_list|<
name|Component
argument_list|>
name|factory
parameter_list|)
block|{
synchronized|synchronized
init|(
name|components
init|)
block|{
name|Component
name|component
init|=
name|components
operator|.
name|get
argument_list|(
name|componentName
argument_list|)
decl_stmt|;
if|if
condition|(
name|component
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|component
operator|=
name|factory
operator|.
name|call
argument_list|()
expr_stmt|;
if|if
condition|(
name|component
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Factory failed to create the "
operator|+
name|componentName
operator|+
literal|" component, it returned null."
argument_list|)
throw|;
block|}
name|components
operator|.
name|put
argument_list|(
name|componentName
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|component
operator|.
name|setCamelContext
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Factory failed to create the "
operator|+
name|componentName
operator|+
literal|" component"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|component
return|;
block|}
block|}
comment|// Endpoint Management Methods
comment|//-----------------------------------------------------------------------
DECL|method|getEndpoints ()
specifier|public
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|getEndpoints
parameter_list|()
block|{
synchronized|synchronized
init|(
name|endpoints
init|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|Endpoint
argument_list|>
argument_list|(
name|endpoints
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**      * Resolves the given URI to an endpoint      */
DECL|method|resolveEndpoint (String uri)
specifier|public
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|Endpoint
name|answer
decl_stmt|;
synchronized|synchronized
init|(
name|endpoints
init|)
block|{
name|answer
operator|=
name|endpoints
operator|.
name|get
argument_list|(
name|uri
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
try|try
block|{
comment|// Use the URI prefix to find the component.
name|String
name|splitURI
index|[]
init|=
name|ObjectHelper
operator|.
name|splitOnCharacter
argument_list|(
name|uri
argument_list|,
literal|":"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|splitURI
index|[
literal|1
index|]
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid URI, it did not contain a scheme: "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|String
name|scheme
init|=
name|splitURI
index|[
literal|0
index|]
decl_stmt|;
name|Component
name|component
init|=
name|getComponent
argument_list|(
name|scheme
argument_list|)
decl_stmt|;
comment|// Ask the component to resolve the endpoint.
if|if
condition|(
name|component
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|component
operator|.
name|resolveEndpoint
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
comment|// HC: What's the idea behind starting an endpoint?
comment|// I don't think we have any endpoints that are services do we?
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|answer
argument_list|)
expr_stmt|;
name|endpoints
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|answer
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
throw|throw
operator|new
name|ResolveEndpointFailedException
argument_list|(
name|uri
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Looks up the current active endpoint by URI without auto-creating it.      */
DECL|method|getEndpoint (String uri)
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|Endpoint
name|answer
decl_stmt|;
synchronized|synchronized
init|(
name|endpoints
init|)
block|{
name|answer
operator|=
name|endpoints
operator|.
name|get
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|// Route Management Methods
comment|//-----------------------------------------------------------------------
DECL|method|getRoutes ()
specifier|public
name|List
argument_list|<
name|Route
argument_list|>
name|getRoutes
parameter_list|()
block|{
return|return
name|routes
return|;
block|}
DECL|method|setRoutes (List<Route> routes)
specifier|public
name|void
name|setRoutes
parameter_list|(
name|List
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
block|{
name|this
operator|.
name|routes
operator|=
name|routes
expr_stmt|;
block|}
DECL|method|addRoutes (Collection<Route> routes)
specifier|public
name|void
name|addRoutes
parameter_list|(
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|routes
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|routes
operator|=
operator|new
name|ArrayList
argument_list|<
name|Route
argument_list|>
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|routes
operator|.
name|addAll
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addRoutes (RouteBuilder builder)
specifier|public
name|void
name|addRoutes
parameter_list|(
name|RouteBuilder
name|builder
parameter_list|)
throws|throws
name|Exception
block|{
comment|// lets now add the routes from the builder
name|builder
operator|.
name|setContext
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|addRoutes
argument_list|(
name|builder
operator|.
name|getRouteList
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Properties
comment|//-----------------------------------------------------------------------
DECL|method|getExchangeConverter ()
specifier|public
name|ExchangeConverter
name|getExchangeConverter
parameter_list|()
block|{
if|if
condition|(
name|exchangeConverter
operator|==
literal|null
condition|)
block|{
name|exchangeConverter
operator|=
name|createExchangeConverter
argument_list|()
expr_stmt|;
block|}
return|return
name|exchangeConverter
return|;
block|}
DECL|method|setExchangeConverter (ExchangeConverter exchangeConverter)
specifier|public
name|void
name|setExchangeConverter
parameter_list|(
name|ExchangeConverter
name|exchangeConverter
parameter_list|)
block|{
name|this
operator|.
name|exchangeConverter
operator|=
name|exchangeConverter
expr_stmt|;
block|}
DECL|method|getTypeConverter ()
specifier|public
name|TypeConverter
name|getTypeConverter
parameter_list|()
block|{
if|if
condition|(
name|typeConverter
operator|==
literal|null
condition|)
block|{
name|typeConverter
operator|=
name|createTypeConverter
argument_list|()
expr_stmt|;
block|}
return|return
name|typeConverter
return|;
block|}
DECL|method|setTypeConverter (TypeConverter typeConverter)
specifier|public
name|void
name|setTypeConverter
parameter_list|(
name|TypeConverter
name|typeConverter
parameter_list|)
block|{
name|this
operator|.
name|typeConverter
operator|=
name|typeConverter
expr_stmt|;
block|}
DECL|method|getInjector ()
specifier|public
name|Injector
name|getInjector
parameter_list|()
block|{
if|if
condition|(
name|injector
operator|==
literal|null
condition|)
block|{
name|injector
operator|=
name|createInjector
argument_list|()
expr_stmt|;
block|}
return|return
name|injector
return|;
block|}
DECL|method|setInjector (Injector injector)
specifier|public
name|void
name|setInjector
parameter_list|(
name|Injector
name|injector
parameter_list|)
block|{
name|this
operator|.
name|injector
operator|=
name|injector
expr_stmt|;
block|}
DECL|method|getComponentResolver ()
specifier|public
name|ComponentResolver
name|getComponentResolver
parameter_list|()
block|{
if|if
condition|(
name|componentResolver
operator|==
literal|null
condition|)
block|{
name|componentResolver
operator|=
name|createComponentResolver
argument_list|()
expr_stmt|;
block|}
return|return
name|componentResolver
return|;
block|}
DECL|method|setComponentResolver (ComponentResolver componentResolver)
specifier|public
name|void
name|setComponentResolver
parameter_list|(
name|ComponentResolver
name|componentResolver
parameter_list|)
block|{
name|this
operator|.
name|componentResolver
operator|=
name|componentResolver
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-----------------------------------------------------------------------
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|routes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Route
argument_list|<
name|Exchange
argument_list|>
name|route
range|:
name|routes
control|)
block|{
name|Processor
argument_list|<
name|Exchange
argument_list|>
name|processor
init|=
name|route
operator|.
name|getProcessor
argument_list|()
decl_stmt|;
name|Consumer
argument_list|<
name|Exchange
argument_list|>
name|consumer
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
decl_stmt|;
if|if
condition|(
name|consumer
operator|!=
literal|null
condition|)
block|{
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
name|servicesToClose
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|processor
operator|instanceof
name|Service
condition|)
block|{
name|Service
name|service
init|=
operator|(
name|Service
operator|)
name|processor
decl_stmt|;
name|service
operator|.
name|start
argument_list|()
expr_stmt|;
name|servicesToClose
operator|.
name|add
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|servicesToClose
argument_list|)
expr_stmt|;
block|}
comment|/**      * Lazily create a default implementation      */
DECL|method|createExchangeConverter ()
specifier|protected
name|ExchangeConverter
name|createExchangeConverter
parameter_list|()
block|{
return|return
operator|new
name|DefaultExchangeConverter
argument_list|()
return|;
block|}
comment|/**      * Lazily create a default implementation      */
DECL|method|createTypeConverter ()
specifier|protected
name|TypeConverter
name|createTypeConverter
parameter_list|()
block|{
return|return
operator|new
name|DefaultTypeConverter
argument_list|()
return|;
block|}
comment|/**      * Lazily create a default implementation      */
DECL|method|createInjector ()
specifier|protected
name|Injector
name|createInjector
parameter_list|()
block|{
name|FactoryFinder
name|finder
init|=
operator|new
name|FactoryFinder
argument_list|()
decl_stmt|;
try|try
block|{
return|return
operator|(
name|Injector
operator|)
name|finder
operator|.
name|newInstance
argument_list|(
literal|"Injector"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoFactoryAvailableException
name|e
parameter_list|)
block|{
comment|// lets use the default
return|return
operator|new
name|ReflectionInjector
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Lazily create a default implementation      */
DECL|method|createComponentResolver ()
specifier|protected
name|ComponentResolver
name|createComponentResolver
parameter_list|()
block|{
return|return
operator|new
name|DefaultComponentResolver
argument_list|()
return|;
block|}
DECL|method|isAutoCreateComponents ()
specifier|public
name|boolean
name|isAutoCreateComponents
parameter_list|()
block|{
return|return
name|autoCreateComponents
return|;
block|}
DECL|method|setAutoCreateComponents (boolean autoCreateComponents)
specifier|public
name|void
name|setAutoCreateComponents
parameter_list|(
name|boolean
name|autoCreateComponents
parameter_list|)
block|{
name|this
operator|.
name|autoCreateComponents
operator|=
name|autoCreateComponents
expr_stmt|;
block|}
block|}
end_class

end_unit

