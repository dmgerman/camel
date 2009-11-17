begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElementRef
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|util
operator|.
name|EndpointHelper
import|;
end_import

begin_comment
comment|/**  * Represents a collection of routes  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"routes"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|RoutesDefinition
specifier|public
class|class
name|RoutesDefinition
extends|extends
name|OptionalIdentifiedDefinition
argument_list|<
name|RoutesDefinition
argument_list|>
implements|implements
name|RouteContainer
block|{
annotation|@
name|XmlElementRef
DECL|field|routes
specifier|private
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|intercepts
specifier|private
name|List
argument_list|<
name|InterceptDefinition
argument_list|>
name|intercepts
init|=
operator|new
name|ArrayList
argument_list|<
name|InterceptDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|interceptFroms
specifier|private
name|List
argument_list|<
name|InterceptFromDefinition
argument_list|>
name|interceptFroms
init|=
operator|new
name|ArrayList
argument_list|<
name|InterceptFromDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|interceptSendTos
specifier|private
name|List
argument_list|<
name|InterceptSendToEndpointDefinition
argument_list|>
name|interceptSendTos
init|=
operator|new
name|ArrayList
argument_list|<
name|InterceptSendToEndpointDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|onExceptions
specifier|private
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|onExceptions
init|=
operator|new
name|ArrayList
argument_list|<
name|OnExceptionDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|onCompletions
specifier|private
name|List
argument_list|<
name|OnCompletionDefinition
argument_list|>
name|onCompletions
init|=
operator|new
name|ArrayList
argument_list|<
name|OnCompletionDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|errorHandlerBuilder
specifier|private
name|ErrorHandlerBuilder
name|errorHandlerBuilder
decl_stmt|;
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Routes: "
operator|+
name|routes
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"routes"
return|;
block|}
comment|// Properties
comment|//-----------------------------------------------------------------------
DECL|method|getRoutes ()
specifier|public
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|getRoutes
parameter_list|()
block|{
return|return
name|routes
return|;
block|}
DECL|method|setRoutes (List<RouteDefinition> routes)
specifier|public
name|void
name|setRoutes
parameter_list|(
name|List
argument_list|<
name|RouteDefinition
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
DECL|method|getInterceptFroms ()
specifier|public
name|List
argument_list|<
name|InterceptFromDefinition
argument_list|>
name|getInterceptFroms
parameter_list|()
block|{
return|return
name|interceptFroms
return|;
block|}
DECL|method|setInterceptFroms (List<InterceptFromDefinition> interceptFroms)
specifier|public
name|void
name|setInterceptFroms
parameter_list|(
name|List
argument_list|<
name|InterceptFromDefinition
argument_list|>
name|interceptFroms
parameter_list|)
block|{
name|this
operator|.
name|interceptFroms
operator|=
name|interceptFroms
expr_stmt|;
block|}
DECL|method|getInterceptSendTos ()
specifier|public
name|List
argument_list|<
name|InterceptSendToEndpointDefinition
argument_list|>
name|getInterceptSendTos
parameter_list|()
block|{
return|return
name|interceptSendTos
return|;
block|}
DECL|method|setInterceptSendTos (List<InterceptSendToEndpointDefinition> interceptSendTos)
specifier|public
name|void
name|setInterceptSendTos
parameter_list|(
name|List
argument_list|<
name|InterceptSendToEndpointDefinition
argument_list|>
name|interceptSendTos
parameter_list|)
block|{
name|this
operator|.
name|interceptSendTos
operator|=
name|interceptSendTos
expr_stmt|;
block|}
DECL|method|getIntercepts ()
specifier|public
name|List
argument_list|<
name|InterceptDefinition
argument_list|>
name|getIntercepts
parameter_list|()
block|{
return|return
name|intercepts
return|;
block|}
DECL|method|setIntercepts (List<InterceptDefinition> intercepts)
specifier|public
name|void
name|setIntercepts
parameter_list|(
name|List
argument_list|<
name|InterceptDefinition
argument_list|>
name|intercepts
parameter_list|)
block|{
name|this
operator|.
name|intercepts
operator|=
name|intercepts
expr_stmt|;
block|}
DECL|method|getOnExceptions ()
specifier|public
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|getOnExceptions
parameter_list|()
block|{
return|return
name|onExceptions
return|;
block|}
DECL|method|setOnExceptions (List<OnExceptionDefinition> onExceptions)
specifier|public
name|void
name|setOnExceptions
parameter_list|(
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|onExceptions
parameter_list|)
block|{
name|this
operator|.
name|onExceptions
operator|=
name|onExceptions
expr_stmt|;
block|}
DECL|method|getOnCompletions ()
specifier|public
name|List
argument_list|<
name|OnCompletionDefinition
argument_list|>
name|getOnCompletions
parameter_list|()
block|{
return|return
name|onCompletions
return|;
block|}
DECL|method|setOnCompletions (List<OnCompletionDefinition> onCompletions)
specifier|public
name|void
name|setOnCompletions
parameter_list|(
name|List
argument_list|<
name|OnCompletionDefinition
argument_list|>
name|onCompletions
parameter_list|)
block|{
name|this
operator|.
name|onCompletions
operator|=
name|onCompletions
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getErrorHandlerBuilder ()
specifier|public
name|ErrorHandlerBuilder
name|getErrorHandlerBuilder
parameter_list|()
block|{
return|return
name|errorHandlerBuilder
return|;
block|}
DECL|method|setErrorHandlerBuilder (ErrorHandlerBuilder errorHandlerBuilder)
specifier|public
name|void
name|setErrorHandlerBuilder
parameter_list|(
name|ErrorHandlerBuilder
name|errorHandlerBuilder
parameter_list|)
block|{
name|this
operator|.
name|errorHandlerBuilder
operator|=
name|errorHandlerBuilder
expr_stmt|;
block|}
comment|// Fluent API
comment|//-------------------------------------------------------------------------
comment|/**      * Creates a new route      *      * @return the builder      */
DECL|method|route ()
specifier|public
name|RouteDefinition
name|route
parameter_list|()
block|{
name|RouteDefinition
name|route
init|=
name|createRoute
argument_list|()
decl_stmt|;
return|return
name|route
argument_list|(
name|route
argument_list|)
return|;
block|}
comment|/**      * Creates a new route from the given URI input      *      * @param uri  the from uri      * @return the builder      */
DECL|method|from (String uri)
specifier|public
name|RouteDefinition
name|from
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|RouteDefinition
name|route
init|=
name|createRoute
argument_list|()
decl_stmt|;
name|route
operator|.
name|from
argument_list|(
name|uri
argument_list|)
expr_stmt|;
return|return
name|route
argument_list|(
name|route
argument_list|)
return|;
block|}
comment|/**      * Creates a new route from the given endpoint      *      * @param endpoint  the from endpoint      * @return the builder      */
DECL|method|from (Endpoint endpoint)
specifier|public
name|RouteDefinition
name|from
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|RouteDefinition
name|route
init|=
name|createRoute
argument_list|()
decl_stmt|;
name|route
operator|.
name|from
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|route
argument_list|(
name|route
argument_list|)
return|;
block|}
comment|/**      * Creates a new route from the given URI inputs      *      * @param uris  the from uri      * @return the builder      */
DECL|method|from (String... uris)
specifier|public
name|RouteDefinition
name|from
parameter_list|(
name|String
modifier|...
name|uris
parameter_list|)
block|{
name|RouteDefinition
name|route
init|=
name|createRoute
argument_list|()
decl_stmt|;
name|route
operator|.
name|from
argument_list|(
name|uris
argument_list|)
expr_stmt|;
return|return
name|route
argument_list|(
name|route
argument_list|)
return|;
block|}
comment|/**      * Creates a new route from the given endpoints      *      * @param endpoints  the from endpoints      * @return the builder      */
DECL|method|from (Endpoint... endpoints)
specifier|public
name|RouteDefinition
name|from
parameter_list|(
name|Endpoint
modifier|...
name|endpoints
parameter_list|)
block|{
name|RouteDefinition
name|route
init|=
name|createRoute
argument_list|()
decl_stmt|;
name|route
operator|.
name|from
argument_list|(
name|endpoints
argument_list|)
expr_stmt|;
return|return
name|route
argument_list|(
name|route
argument_list|)
return|;
block|}
comment|/**      * Creates a new route using the given route      *      * @param route the route      * @return the builder      */
DECL|method|route (RouteDefinition route)
specifier|public
name|RouteDefinition
name|route
parameter_list|(
name|RouteDefinition
name|route
parameter_list|)
block|{
comment|// lets configure the route
name|route
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
comment|// configure intercept
for|for
control|(
name|InterceptDefinition
name|intercept
range|:
name|getIntercepts
argument_list|()
control|)
block|{
comment|// add as first output so intercept is handled before the actual route and that gives
comment|// us the needed head start to init and be able to intercept all the remaining processing steps
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|intercept
argument_list|)
expr_stmt|;
block|}
comment|// configure intercept from
for|for
control|(
name|InterceptFromDefinition
name|intercept
range|:
name|getInterceptFroms
argument_list|()
control|)
block|{
comment|// should we only apply interceptor for a given endpoint uri
name|boolean
name|match
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|intercept
operator|.
name|getUri
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|match
operator|=
literal|false
expr_stmt|;
for|for
control|(
name|FromDefinition
name|input
range|:
name|route
operator|.
name|getInputs
argument_list|()
control|)
block|{
if|if
condition|(
name|EndpointHelper
operator|.
name|matchEndpoint
argument_list|(
name|input
operator|.
name|getUri
argument_list|()
argument_list|,
name|intercept
operator|.
name|getUri
argument_list|()
argument_list|)
condition|)
block|{
name|match
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|match
condition|)
block|{
comment|// add as first output so intercept is handled before the acutal route and that gives
comment|// us the needed head start to init and be able to intercept all the remaining processing steps
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|intercept
argument_list|)
expr_stmt|;
block|}
block|}
comment|// configure intercept send to endpoint
for|for
control|(
name|InterceptSendToEndpointDefinition
name|sendTo
range|:
name|getInterceptSendTos
argument_list|()
control|)
block|{
comment|// add as first output so intercept is handled before the actual route and that gives
comment|// us the needed head start to init and be able to intercept all the remaining processing steps
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|sendTo
argument_list|)
expr_stmt|;
block|}
comment|// add on completions after the interceptors
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|addAll
argument_list|(
name|getOnCompletions
argument_list|()
argument_list|)
expr_stmt|;
comment|// add on exceptions at top since we need to inject this by the error handlers
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|addAll
argument_list|(
literal|0
argument_list|,
name|getOnExceptions
argument_list|()
argument_list|)
expr_stmt|;
name|getRoutes
argument_list|()
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
return|return
name|route
return|;
block|}
comment|/**      * Creates and adds an interceptor that is triggered on every step in the route      * processing.      *      * @return the interceptor builder to configure      */
DECL|method|intercept ()
specifier|public
name|InterceptDefinition
name|intercept
parameter_list|()
block|{
name|InterceptDefinition
name|answer
init|=
operator|new
name|InterceptDefinition
argument_list|()
decl_stmt|;
name|getIntercepts
argument_list|()
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates and adds an interceptor that is triggered when an exchange      * is received as input to any routes (eg from all the<tt>from</tt>)      *      * @return the interceptor builder to configure      */
DECL|method|interceptFrom ()
specifier|public
name|InterceptFromDefinition
name|interceptFrom
parameter_list|()
block|{
name|InterceptFromDefinition
name|answer
init|=
operator|new
name|InterceptFromDefinition
argument_list|()
decl_stmt|;
name|getInterceptFroms
argument_list|()
operator|.
name|add
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates and adds an interceptor that is triggered when an exchange is received      * as input to the route defined with the given endpoint (eg from the<tt>from</tt>)      *      * @param uri uri of the endpoint      * @return the interceptor builder to configure      */
DECL|method|interceptFrom (final String uri)
specifier|public
name|InterceptFromDefinition
name|interceptFrom
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|)
block|{
name|InterceptFromDefinition
name|answer
init|=
operator|new
name|InterceptFromDefinition
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|getInterceptFroms
argument_list|()
operator|.
name|add
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates and adds an interceptor that is triggered when an exchange is      * send to the given endpoint      *      * @param uri uri of the endpoint      * @return  the builder      */
DECL|method|interceptSendToEndpoint (final String uri)
specifier|public
name|InterceptSendToEndpointDefinition
name|interceptSendToEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|)
block|{
name|InterceptSendToEndpointDefinition
name|answer
init|=
operator|new
name|InterceptSendToEndpointDefinition
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|getInterceptSendTos
argument_list|()
operator|.
name|add
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Adds an on exception      *       * @param exception  the exception      * @return the builder      */
DECL|method|onException (Class exception)
specifier|public
name|OnExceptionDefinition
name|onException
parameter_list|(
name|Class
name|exception
parameter_list|)
block|{
name|OnExceptionDefinition
name|answer
init|=
operator|new
name|OnExceptionDefinition
argument_list|(
name|exception
argument_list|)
decl_stmt|;
name|getOnExceptions
argument_list|()
operator|.
name|add
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Adds an on completion      *      * @return the builder      */
DECL|method|onCompletion ()
specifier|public
name|OnCompletionDefinition
name|onCompletion
parameter_list|()
block|{
name|OnCompletionDefinition
name|answer
init|=
operator|new
name|OnCompletionDefinition
argument_list|()
decl_stmt|;
name|getOnCompletions
argument_list|()
operator|.
name|add
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|createRoute ()
specifier|protected
name|RouteDefinition
name|createRoute
parameter_list|()
block|{
name|RouteDefinition
name|route
init|=
operator|new
name|RouteDefinition
argument_list|()
decl_stmt|;
name|ErrorHandlerBuilder
name|handler
init|=
name|getErrorHandlerBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
name|route
operator|.
name|setErrorHandlerBuilderIfNull
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
return|return
name|route
return|;
block|}
block|}
end_class

end_unit

