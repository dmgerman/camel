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
name|util
operator|.
name|ServiceHelper
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArrayList
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
name|atomic
operator|.
name|AtomicBoolean
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
implements|implements
name|CamelContext
implements|,
name|Service
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DefaultCamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
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
DECL|field|resolvers
specifier|private
name|List
argument_list|<
name|EndpointResolver
argument_list|>
name|resolvers
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|EndpointResolver
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
DECL|field|endpointResolver
specifier|private
name|EndpointResolver
name|endpointResolver
decl_stmt|;
DECL|field|exchangeConverter
specifier|private
name|ExchangeConverter
name|exchangeConverter
decl_stmt|;
DECL|field|started
specifier|private
name|AtomicBoolean
name|started
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
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
if|if
condition|(
name|component
operator|instanceof
name|EndpointResolver
condition|)
block|{
name|resolvers
operator|.
name|add
argument_list|(
operator|(
name|EndpointResolver
operator|)
name|component
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getComponent (String componentName)
specifier|public
name|Component
name|getComponent
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
name|get
argument_list|(
name|componentName
argument_list|)
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
name|IllegalArgumentException
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
name|IllegalArgumentException
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
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|activateEndpoints
argument_list|()
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|deactivateEndpoints
argument_list|()
expr_stmt|;
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
for|for
control|(
name|EndpointResolver
name|resolver
range|:
name|resolvers
control|)
block|{
name|answer
operator|=
name|resolver
operator|.
name|resolveEndpoint
argument_list|(
name|this
argument_list|,
name|uri
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
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
name|EndpointResolver
name|er
init|=
name|getEndpointResolver
argument_list|()
decl_stmt|;
name|answer
operator|=
name|er
operator|.
name|resolveEndpoint
argument_list|(
name|this
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
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
comment|/**      * Activates all the starting endpoints in that were added as routes.      */
DECL|method|activateEndpoints ()
specifier|public
name|void
name|activateEndpoints
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
comment|/**      * Deactivates all the starting endpoints in that were added as routes.      */
DECL|method|deactivateEndpoints ()
specifier|public
name|void
name|deactivateEndpoints
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
DECL|method|addRoutes (List<Route> routes)
specifier|public
name|void
name|addRoutes
parameter_list|(
name|List
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
DECL|method|addRoutes (final RouteFactory factory)
specifier|public
name|void
name|addRoutes
parameter_list|(
specifier|final
name|RouteFactory
name|factory
parameter_list|)
throws|throws
name|Exception
block|{
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|(
name|this
argument_list|)
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|factory
operator|.
name|build
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
comment|// Properties
comment|//-----------------------------------------------------------------------
DECL|method|getEndpointResolver ()
specifier|public
name|EndpointResolver
name|getEndpointResolver
parameter_list|()
block|{
if|if
condition|(
name|endpointResolver
operator|==
literal|null
condition|)
block|{
name|endpointResolver
operator|=
name|createEndpointResolver
argument_list|()
expr_stmt|;
block|}
return|return
name|endpointResolver
return|;
block|}
DECL|method|setEndpointResolver (EndpointResolver endpointResolver)
specifier|public
name|void
name|setEndpointResolver
parameter_list|(
name|EndpointResolver
name|endpointResolver
parameter_list|)
block|{
name|this
operator|.
name|endpointResolver
operator|=
name|endpointResolver
expr_stmt|;
block|}
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
comment|// Implementation methods
comment|//-----------------------------------------------------------------------
comment|/**      * Lazily create a default implementation      */
DECL|method|createEndpointResolver ()
specifier|protected
name|EndpointResolver
name|createEndpointResolver
parameter_list|()
block|{
return|return
operator|new
name|DefaultEndpointResolver
argument_list|()
return|;
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
specifier|private
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
block|}
end_class

end_unit

