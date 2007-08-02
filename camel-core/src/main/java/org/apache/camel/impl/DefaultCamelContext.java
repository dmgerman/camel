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
name|ResolveEndpointFailedException
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
name|TypeConverter
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
name|spi
operator|.
name|Language
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
name|LanguageResolver
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
name|Registry
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
name|jndi
operator|.
name|JndiContext
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ServiceHelper
operator|.
name|startServices
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ServiceHelper
operator|.
name|stopServices
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
DECL|field|languageResolver
specifier|private
name|LanguageResolver
name|languageResolver
init|=
operator|new
name|DefaultLanguageResolver
argument_list|()
decl_stmt|;
DECL|field|registry
specifier|private
name|Registry
name|registry
decl_stmt|;
DECL|method|DefaultCamelContext ()
specifier|public
name|DefaultCamelContext
parameter_list|()
block|{     }
comment|/**      * Creates the {@link CamelContext} using the given JNDI      * context as the registry      *      * @param jndiContext      */
DECL|method|DefaultCamelContext (Context jndiContext)
specifier|public
name|DefaultCamelContext
parameter_list|(
name|Context
name|jndiContext
parameter_list|)
block|{
name|setRegistry
argument_list|(
operator|new
name|JndiRegistry
argument_list|(
name|jndiContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|component
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Component cannot be null"
argument_list|)
throw|;
block|}
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
if|if
condition|(
name|component
operator|!=
literal|null
condition|)
block|{
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
name|startServices
argument_list|(
name|component
argument_list|)
expr_stmt|;
block|}
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
DECL|method|getComponent (String name, Class<T> componentType)
specifier|public
parameter_list|<
name|T
extends|extends
name|Component
parameter_list|>
name|T
name|getComponent
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|componentType
parameter_list|)
block|{
name|Component
name|component
init|=
name|getComponent
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|componentType
operator|.
name|isInstance
argument_list|(
name|component
argument_list|)
condition|)
block|{
return|return
name|componentType
operator|.
name|cast
argument_list|(
name|component
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The component is not of type: "
operator|+
name|componentType
operator|+
literal|" but is: "
operator|+
name|component
argument_list|)
throw|;
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
DECL|method|getSingletonEndpoints ()
specifier|public
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|getSingletonEndpoints
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
DECL|method|addSingletonEndpoint (String uri, Endpoint endpoint)
specifier|public
name|Endpoint
name|addSingletonEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|Endpoint
name|oldEndpoint
decl_stmt|;
synchronized|synchronized
init|(
name|endpoints
init|)
block|{
name|startServices
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|oldEndpoint
operator|=
name|endpoints
operator|.
name|remove
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|endpoints
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|stopServices
argument_list|(
name|oldEndpoint
argument_list|)
expr_stmt|;
block|}
return|return
name|oldEndpoint
return|;
block|}
DECL|method|removeSingletonEndpoint (String uri)
specifier|public
name|Endpoint
name|removeSingletonEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|Endpoint
name|oldEndpoint
decl_stmt|;
synchronized|synchronized
init|(
name|endpoints
init|)
block|{
name|oldEndpoint
operator|=
name|endpoints
operator|.
name|remove
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|stopServices
argument_list|(
name|oldEndpoint
argument_list|)
expr_stmt|;
block|}
return|return
name|oldEndpoint
return|;
block|}
comment|/**      * Resolves the given URI to an endpoint      */
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
operator|!=
literal|null
condition|)
block|{
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
comment|// Have the component create the endpoint if it can.
name|answer
operator|=
name|component
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|createEndpoint
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
comment|// If it's a singleton then auto register it.
if|if
condition|(
name|answer
operator|!=
literal|null
operator|&&
name|answer
operator|.
name|isSingleton
argument_list|()
condition|)
block|{
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
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
DECL|method|getEndpoint (String name, Class<T> endpointType)
specifier|public
parameter_list|<
name|T
extends|extends
name|Endpoint
parameter_list|>
name|T
name|getEndpoint
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|endpointType
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|getEndpoint
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpointType
operator|.
name|isInstance
argument_list|(
name|endpoint
argument_list|)
condition|)
block|{
return|return
name|endpointType
operator|.
name|cast
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The endpoint is not of type: "
operator|+
name|endpointType
operator|+
literal|" but is: "
operator|+
name|endpoint
argument_list|)
throw|;
block|}
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
throws|throws
name|Exception
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
if|if
condition|(
name|isStarted
argument_list|()
condition|)
block|{
name|startRoutes
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
comment|// Helper methods
comment|//-----------------------------------------------------------------------
comment|/**      * Resolves a language for creating expressions      */
DECL|method|resolveLanguage (String language)
specifier|public
name|Language
name|resolveLanguage
parameter_list|(
name|String
name|language
parameter_list|)
block|{
return|return
name|getLanguageResolver
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
name|language
argument_list|,
name|this
argument_list|)
return|;
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
DECL|method|getLanguageResolver ()
specifier|public
name|LanguageResolver
name|getLanguageResolver
parameter_list|()
block|{
return|return
name|languageResolver
return|;
block|}
DECL|method|setLanguageResolver (LanguageResolver languageResolver)
specifier|public
name|void
name|setLanguageResolver
parameter_list|(
name|LanguageResolver
name|languageResolver
parameter_list|)
block|{
name|this
operator|.
name|languageResolver
operator|=
name|languageResolver
expr_stmt|;
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
DECL|method|getRegistry ()
specifier|public
name|Registry
name|getRegistry
parameter_list|()
block|{
if|if
condition|(
name|registry
operator|==
literal|null
condition|)
block|{
name|registry
operator|=
name|createRegistry
argument_list|()
expr_stmt|;
block|}
return|return
name|registry
return|;
block|}
DECL|method|setRegistry (Registry registry)
specifier|public
name|void
name|setRegistry
parameter_list|(
name|Registry
name|registry
parameter_list|)
block|{
name|this
operator|.
name|registry
operator|=
name|registry
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
name|forceLazyInitialization
argument_list|()
expr_stmt|;
if|if
condition|(
name|components
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Component
name|component
range|:
name|components
operator|.
name|values
argument_list|()
control|)
block|{
name|startServices
argument_list|(
name|component
argument_list|)
expr_stmt|;
block|}
block|}
name|startRoutes
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|stopServices
argument_list|(
name|servicesToClose
argument_list|)
expr_stmt|;
if|if
condition|(
name|components
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Component
name|component
range|:
name|components
operator|.
name|values
argument_list|()
control|)
block|{
name|stopServices
argument_list|(
name|component
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|startRoutes (Collection<Route> routeList)
specifier|protected
name|void
name|startRoutes
parameter_list|(
name|Collection
argument_list|<
name|Route
argument_list|>
name|routeList
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|routeList
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
name|routeList
control|)
block|{
name|List
argument_list|<
name|Service
argument_list|>
name|services
init|=
name|route
operator|.
name|getServicesForRoute
argument_list|()
decl_stmt|;
name|servicesToClose
operator|.
name|addAll
argument_list|(
name|services
argument_list|)
expr_stmt|;
name|startServices
argument_list|(
name|services
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Lets force some lazy initialization to occur upfront      * before we start any components and create routes      */
DECL|method|forceLazyInitialization ()
specifier|protected
name|void
name|forceLazyInitialization
parameter_list|()
block|{
name|getExchangeConverter
argument_list|()
expr_stmt|;
name|getInjector
argument_list|()
expr_stmt|;
name|getLanguageResolver
argument_list|()
expr_stmt|;
name|getTypeConverter
argument_list|()
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
argument_list|(
name|getInjector
argument_list|()
argument_list|)
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
comment|/**      * Lazily create a default implementation      */
DECL|method|createRegistry ()
specifier|protected
name|Registry
name|createRegistry
parameter_list|()
block|{
return|return
operator|new
name|JndiRegistry
argument_list|()
return|;
block|}
comment|/**      * A pluggable strategy to allow an endpoint to be created without requiring      * a component to be its factory, such as for looking up the URI inside      * some {@link Registry}      *      * @param uri the uri for the endpoint to be created      * @return the newly created endpoint or null if it could not be resolved      */
DECL|method|createEndpoint (String uri)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|Object
name|value
init|=
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Endpoint
condition|)
block|{
return|return
operator|(
name|Endpoint
operator|)
name|value
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Processor
condition|)
block|{
return|return
operator|new
name|ProcessorEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
operator|(
name|Processor
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|convertBeanToEndpoint
argument_list|(
name|uri
argument_list|,
name|value
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Attempt to convert the bean from a {@link Registry} to an      * endpoint using some kind of transformation or wrapper      *      * @param uri  the uri for the endpoint (and name in the registry)      * @param bean the bean to be converted to an endpoint, which will be not null      * @return a new endpoint      */
DECL|method|convertBeanToEndpoint (String uri, Object bean)
specifier|protected
name|Endpoint
name|convertBeanToEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Object
name|bean
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"uri: "
operator|+
name|uri
operator|+
literal|" bean: "
operator|+
name|bean
operator|+
literal|" could not be converted to an Endpoint"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

