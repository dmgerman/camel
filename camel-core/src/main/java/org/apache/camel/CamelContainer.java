begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

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
name|DefaultEndpointResolver
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
name|DefaultExchangeConverter
import|;
end_import

begin_comment
comment|/**  * Represents the container used to configure routes and the policies to use.  *  * @version $Revision$  * @org.apache.xbean.XBean element="container" rootElement="true"  */
end_comment

begin_class
DECL|class|CamelContainer
specifier|public
class|class
name|CamelContainer
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
block|{
DECL|field|endpointResolver
specifier|private
name|EndpointResolver
argument_list|<
name|E
argument_list|>
name|endpointResolver
decl_stmt|;
DECL|field|exchangeConverter
specifier|private
name|ExchangeConverter
name|exchangeConverter
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
name|Map
argument_list|<
name|Endpoint
argument_list|<
name|E
argument_list|>
argument_list|,
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|>
name|routes
decl_stmt|;
comment|/**      * Activates all the starting endpoints in that were added as routes.      */
DECL|method|activateEndpoints ()
specifier|public
name|void
name|activateEndpoints
parameter_list|()
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Endpoint
argument_list|<
name|E
argument_list|>
argument_list|,
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|>
name|entry
range|:
name|routes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Processor
argument_list|<
name|E
argument_list|>
name|processor
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|activate
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Deactivates all the starting endpoints in that were added as routes.      */
DECL|method|deactivateEndpoints ()
specifier|public
name|void
name|deactivateEndpoints
parameter_list|()
block|{
for|for
control|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
range|:
name|routes
operator|.
name|keySet
argument_list|()
control|)
block|{
name|endpoint
operator|.
name|deactivate
argument_list|()
expr_stmt|;
block|}
block|}
comment|// Builder APIs
comment|//-----------------------------------------------------------------------
DECL|method|routes (RouteBuilder<E> builder)
specifier|public
name|void
name|routes
parameter_list|(
name|RouteBuilder
argument_list|<
name|E
argument_list|>
name|builder
parameter_list|)
block|{
comment|// lets now add the routes from the builder
name|builder
operator|.
name|setContainer
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|routes
operator|=
name|builder
operator|.
name|getRouteMap
argument_list|()
expr_stmt|;
block|}
DECL|method|routes (final RouteFactory factory)
specifier|public
name|void
name|routes
parameter_list|(
specifier|final
name|RouteFactory
name|factory
parameter_list|)
block|{
name|RouteBuilder
argument_list|<
name|E
argument_list|>
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|<
name|E
argument_list|>
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
name|routes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a component to the container if there is not currently a component already registered.      */
DECL|method|addComponent (String componentName, final Component<E> component)
specifier|public
name|void
name|addComponent
parameter_list|(
name|String
name|componentName
parameter_list|,
specifier|final
name|Component
argument_list|<
name|E
argument_list|>
name|component
parameter_list|)
block|{
comment|// TODO provide a version of this which barfs if the component is registered multiple times
name|getOrCreateComponent
argument_list|(
name|componentName
argument_list|,
operator|new
name|Callable
argument_list|<
name|Component
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|Component
argument_list|<
name|E
argument_list|>
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|component
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**O      * Resolves the given URI to an endpoint      */
DECL|method|endpoint (String uri)
specifier|public
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|EndpointResolver
argument_list|<
name|E
argument_list|>
name|er
init|=
name|getEndpointResolver
argument_list|()
decl_stmt|;
return|return
name|er
operator|.
name|resolveEndpoint
argument_list|(
name|this
argument_list|,
name|uri
argument_list|)
return|;
block|}
comment|// Properties
comment|//-----------------------------------------------------------------------
DECL|method|getEndpointResolver ()
specifier|public
name|EndpointResolver
argument_list|<
name|E
argument_list|>
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
DECL|method|setEndpointResolver (EndpointResolver<E> endpointResolver)
specifier|public
name|void
name|setEndpointResolver
parameter_list|(
name|EndpointResolver
argument_list|<
name|E
argument_list|>
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
comment|// Implementation methods
comment|//-----------------------------------------------------------------------
DECL|method|createEndpointResolver ()
specifier|protected
name|EndpointResolver
argument_list|<
name|E
argument_list|>
name|createEndpointResolver
parameter_list|()
block|{
return|return
operator|new
name|DefaultEndpointResolver
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
comment|/**      * Lazily create a default exchange converter implementation      */
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
DECL|method|getOrCreateComponent (String componentName, Callable<Component<E>> factory)
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
argument_list|<
name|E
argument_list|>
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
name|setContainer
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
return|return
name|component
return|;
block|}
block|}
block|}
end_class

end_unit

