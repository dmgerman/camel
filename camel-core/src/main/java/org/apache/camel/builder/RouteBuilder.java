begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|atomic
operator|.
name|AtomicBoolean
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
name|RoutesBuilder
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
name|model
operator|.
name|InterceptDefinition
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
name|InterceptFromDefinition
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
name|InterceptSendToEndpointDefinition
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
name|OnCompletionDefinition
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
name|OnExceptionDefinition
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
name|model
operator|.
name|RoutesDefinition
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
name|rest
operator|.
name|RestDefinition
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
name|rest
operator|.
name|RestsDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/dsl.html">Java DSL</a> which is  * used to build {@link org.apache.camel.impl.DefaultRoute} instances in a {@link CamelContext} for smart routing.  *  * @version   */
end_comment

begin_class
DECL|class|RouteBuilder
specifier|public
specifier|abstract
class|class
name|RouteBuilder
extends|extends
name|BuilderSupport
implements|implements
name|RoutesBuilder
block|{
DECL|field|log
specifier|protected
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|initialized
specifier|private
name|AtomicBoolean
name|initialized
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|restCollection
specifier|private
name|RestsDefinition
name|restCollection
init|=
operator|new
name|RestsDefinition
argument_list|()
decl_stmt|;
DECL|field|routeCollection
specifier|private
name|RoutesDefinition
name|routeCollection
init|=
operator|new
name|RoutesDefinition
argument_list|()
decl_stmt|;
DECL|method|RouteBuilder ()
specifier|public
name|RouteBuilder
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|RouteBuilder (CamelContext context)
specifier|public
name|RouteBuilder
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getRouteCollection
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      *<b>Called on initialization to build the routes using the fluent builder syntax.</b>      *<p/>      * This is a central method for RouteBuilder implementations to implement      * the routes using the Java fluent builder syntax.      *      * @throws Exception can be thrown during configuration      */
DECL|method|configure ()
specifier|public
specifier|abstract
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Creates a new REST service      *      * @return the builder      */
DECL|method|rest ()
specifier|public
name|RestDefinition
name|rest
parameter_list|()
block|{
name|getRestCollection
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|RestDefinition
name|answer
init|=
name|getRestCollection
argument_list|()
operator|.
name|rest
argument_list|()
decl_stmt|;
name|configureRest
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
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
name|getRouteCollection
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|RouteDefinition
name|answer
init|=
name|getRouteCollection
argument_list|()
operator|.
name|from
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|configureRoute
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates a new route from the given URI input      *      * @param uri  the String formatted from uri      * @param args arguments for the string formatting of the uri      * @return the builder      */
DECL|method|fromF (String uri, Object... args)
specifier|public
name|RouteDefinition
name|fromF
parameter_list|(
name|String
name|uri
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
name|getRouteCollection
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|RouteDefinition
name|answer
init|=
name|getRouteCollection
argument_list|()
operator|.
name|from
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|uri
argument_list|,
name|args
argument_list|)
argument_list|)
decl_stmt|;
name|configureRoute
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
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
name|getRouteCollection
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|RouteDefinition
name|answer
init|=
name|getRouteCollection
argument_list|()
operator|.
name|from
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|configureRoute
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates a new route from the given URIs input      *      * @param uris  the from uris      * @return the builder      */
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
name|getRouteCollection
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|RouteDefinition
name|answer
init|=
name|getRouteCollection
argument_list|()
operator|.
name|from
argument_list|(
name|uris
argument_list|)
decl_stmt|;
name|configureRoute
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates a new route from the given endpoint      *      * @param endpoints  the from endpoints      * @return the builder      */
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
name|getRouteCollection
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|RouteDefinition
name|answer
init|=
name|getRouteCollection
argument_list|()
operator|.
name|from
argument_list|(
name|endpoints
argument_list|)
decl_stmt|;
name|configureRoute
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Installs the given<a href="http://camel.apache.org/error-handler.html">error handler</a> builder      *      * @param errorHandlerBuilder  the error handler to be used by default for all child routes      */
DECL|method|errorHandler (ErrorHandlerBuilder errorHandlerBuilder)
specifier|public
name|void
name|errorHandler
parameter_list|(
name|ErrorHandlerBuilder
name|errorHandlerBuilder
parameter_list|)
block|{
if|if
condition|(
operator|!
name|getRouteCollection
argument_list|()
operator|.
name|getRoutes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"errorHandler must be defined before any routes in the RouteBuilder"
argument_list|)
throw|;
block|}
name|getRouteCollection
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|setErrorHandlerBuilder
argument_list|(
name|errorHandlerBuilder
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a route for an interceptor that intercepts every processing step.      *      * @return the builder      */
DECL|method|intercept ()
specifier|public
name|InterceptDefinition
name|intercept
parameter_list|()
block|{
if|if
condition|(
operator|!
name|getRouteCollection
argument_list|()
operator|.
name|getRoutes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"intercept must be defined before any routes in the RouteBuilder"
argument_list|)
throw|;
block|}
name|getRouteCollection
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|getRouteCollection
argument_list|()
operator|.
name|intercept
argument_list|()
return|;
block|}
comment|/**      * Adds a route for an interceptor that intercepts incoming messages on any inputs in this route      *      * @return the builder      */
DECL|method|interceptFrom ()
specifier|public
name|InterceptFromDefinition
name|interceptFrom
parameter_list|()
block|{
if|if
condition|(
operator|!
name|getRouteCollection
argument_list|()
operator|.
name|getRoutes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"interceptFrom must be defined before any routes in the RouteBuilder"
argument_list|)
throw|;
block|}
name|getRouteCollection
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|getRouteCollection
argument_list|()
operator|.
name|interceptFrom
argument_list|()
return|;
block|}
comment|/**      * Adds a route for an interceptor that intercepts incoming messages on the given endpoint.      *      * @param uri  endpoint uri      * @return the builder      */
DECL|method|interceptFrom (String uri)
specifier|public
name|InterceptFromDefinition
name|interceptFrom
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
operator|!
name|getRouteCollection
argument_list|()
operator|.
name|getRoutes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"interceptFrom must be defined before any routes in the RouteBuilder"
argument_list|)
throw|;
block|}
name|getRouteCollection
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|getRouteCollection
argument_list|()
operator|.
name|interceptFrom
argument_list|(
name|uri
argument_list|)
return|;
block|}
comment|/**      * Applies a route for an interceptor if an exchange is send to the given endpoint      *      * @param uri  endpoint uri      * @return the builder      */
DECL|method|interceptSendToEndpoint (String uri)
specifier|public
name|InterceptSendToEndpointDefinition
name|interceptSendToEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
operator|!
name|getRouteCollection
argument_list|()
operator|.
name|getRoutes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"interceptSendToEndpoint must be defined before any routes in the RouteBuilder"
argument_list|)
throw|;
block|}
name|getRouteCollection
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|getRouteCollection
argument_list|()
operator|.
name|interceptSendToEndpoint
argument_list|(
name|uri
argument_list|)
return|;
block|}
comment|/**      *<a href="http://camel.apache.org/exception-clause.html">Exception clause</a>      * for catching certain exceptions and handling them.      *      * @param exception exception to catch      * @return the builder      */
DECL|method|onException (Class<? extends Throwable> exception)
specifier|public
name|OnExceptionDefinition
name|onException
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
name|exception
parameter_list|)
block|{
comment|// is only allowed at the top currently
if|if
condition|(
operator|!
name|getRouteCollection
argument_list|()
operator|.
name|getRoutes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"onException must be defined before any routes in the RouteBuilder"
argument_list|)
throw|;
block|}
name|getRouteCollection
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|getRouteCollection
argument_list|()
operator|.
name|onException
argument_list|(
name|exception
argument_list|)
return|;
block|}
comment|/**      *<a href="http://camel.apache.org/exception-clause.html">Exception clause</a>      * for catching certain exceptions and handling them.      *      * @param exceptions list of exceptions to catch      * @return the builder      */
DECL|method|onException (Class<? extends Throwable>.... exceptions)
specifier|public
name|OnExceptionDefinition
name|onException
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
modifier|...
name|exceptions
parameter_list|)
block|{
name|OnExceptionDefinition
name|last
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
name|ex
range|:
name|exceptions
control|)
block|{
name|last
operator|=
name|last
operator|==
literal|null
condition|?
name|onException
argument_list|(
name|ex
argument_list|)
else|:
name|last
operator|.
name|onException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
name|last
operator|!=
literal|null
condition|?
name|last
else|:
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      *<a href="http://camel.apache.org/oncompletion.html">On completion</a>      * callback for doing custom routing when the {@link org.apache.camel.Exchange} is complete.      *      * @return the builder      */
DECL|method|onCompletion ()
specifier|public
name|OnCompletionDefinition
name|onCompletion
parameter_list|()
block|{
comment|// is only allowed at the top currently
if|if
condition|(
operator|!
name|getRouteCollection
argument_list|()
operator|.
name|getRoutes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"onCompletion must be defined before any routes in the RouteBuilder"
argument_list|)
throw|;
block|}
name|getRouteCollection
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|getRouteCollection
argument_list|()
operator|.
name|onCompletion
argument_list|()
return|;
block|}
comment|// Properties
comment|// -----------------------------------------------------------------------
DECL|method|getContext ()
specifier|public
name|ModelCamelContext
name|getContext
parameter_list|()
block|{
name|ModelCamelContext
name|context
init|=
name|super
operator|.
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|context
operator|=
name|createContainer
argument_list|()
expr_stmt|;
name|setContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
DECL|method|addRoutesToCamelContext (CamelContext context)
specifier|public
name|void
name|addRoutesToCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
comment|// must configure routes before rests
name|configureRoutes
argument_list|(
operator|(
name|ModelCamelContext
operator|)
name|context
argument_list|)
expr_stmt|;
name|configureRests
argument_list|(
operator|(
name|ModelCamelContext
operator|)
name|context
argument_list|)
expr_stmt|;
comment|// but populate rests before routes, as we want to turn rests into routes
name|populateRests
argument_list|()
expr_stmt|;
name|populateRoutes
argument_list|()
expr_stmt|;
block|}
comment|/**      * Configures the routes      *      * @param context the Camel context      * @return the routes configured      * @throws Exception can be thrown during configuration      */
DECL|method|configureRoutes (ModelCamelContext context)
specifier|public
name|RoutesDefinition
name|configureRoutes
parameter_list|(
name|ModelCamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|setContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|checkInitialized
argument_list|()
expr_stmt|;
name|routeCollection
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|routeCollection
return|;
block|}
comment|/**      * Configures the rests      *      * @param context the Camel context      * @return the rests configured      * @throws Exception can be thrown during configuration      */
DECL|method|configureRests (ModelCamelContext context)
specifier|public
name|RestsDefinition
name|configureRests
parameter_list|(
name|ModelCamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|setContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|restCollection
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|restCollection
return|;
block|}
comment|/**      * Includes the routes from the build to this builder.      *<p/>      * This allows you to use other builds as route templates.      * @param routes other builder with routes to include      *      * @throws Exception can be thrown during configuration      */
DECL|method|includeRoutes (RoutesBuilder routes)
specifier|public
name|void
name|includeRoutes
parameter_list|(
name|RoutesBuilder
name|routes
parameter_list|)
throws|throws
name|Exception
block|{
comment|// TODO: We should support including multiple routes so I think invoking configure()
comment|// needs to be deferred to later
if|if
condition|(
name|routes
operator|instanceof
name|RouteBuilder
condition|)
block|{
comment|// if its a RouteBuilder then let it use my route collection and error handler
comment|// then we are integrated seamless
name|RouteBuilder
name|builder
init|=
operator|(
name|RouteBuilder
operator|)
name|routes
decl_stmt|;
name|builder
operator|.
name|setContext
argument_list|(
name|this
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setRouteCollection
argument_list|(
name|this
operator|.
name|getRouteCollection
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setErrorHandlerBuilder
argument_list|(
name|this
operator|.
name|getErrorHandlerBuilder
argument_list|()
argument_list|)
expr_stmt|;
comment|// must invoke configure on the original builder so it adds its configuration to me
name|builder
operator|.
name|configure
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|getContext
argument_list|()
operator|.
name|addRoutes
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|setErrorHandlerBuilder (ErrorHandlerBuilder errorHandlerBuilder)
specifier|public
name|void
name|setErrorHandlerBuilder
parameter_list|(
name|ErrorHandlerBuilder
name|errorHandlerBuilder
parameter_list|)
block|{
name|super
operator|.
name|setErrorHandlerBuilder
argument_list|(
name|errorHandlerBuilder
argument_list|)
expr_stmt|;
name|getRouteCollection
argument_list|()
operator|.
name|setErrorHandlerBuilder
argument_list|(
name|getErrorHandlerBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -----------------------------------------------------------------------
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|checkInitialized ()
specifier|protected
name|void
name|checkInitialized
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|initialized
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
comment|// Set the CamelContext ErrorHandler here
name|ModelCamelContext
name|camelContext
init|=
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|camelContext
operator|.
name|getErrorHandlerBuilder
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setErrorHandlerBuilder
argument_list|(
name|camelContext
operator|.
name|getErrorHandlerBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|configure
argument_list|()
expr_stmt|;
comment|// mark all route definitions as custom prepared because
comment|// a route builder prepares the route definitions correctly already
for|for
control|(
name|RouteDefinition
name|route
range|:
name|getRouteCollection
argument_list|()
operator|.
name|getRoutes
argument_list|()
control|)
block|{
name|route
operator|.
name|markPrepared
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|populateRoutes ()
specifier|protected
name|void
name|populateRoutes
parameter_list|()
throws|throws
name|Exception
block|{
name|ModelCamelContext
name|camelContext
init|=
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|camelContext
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"CamelContext has not been injected!"
argument_list|)
throw|;
block|}
name|getRouteCollection
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addRouteDefinitions
argument_list|(
name|getRouteCollection
argument_list|()
operator|.
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|populateRests ()
specifier|protected
name|void
name|populateRests
parameter_list|()
throws|throws
name|Exception
block|{
name|ModelCamelContext
name|camelContext
init|=
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|camelContext
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"CamelContext has not been injected!"
argument_list|)
throw|;
block|}
name|getRestCollection
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addRestDefinitions
argument_list|(
name|getRestCollection
argument_list|()
operator|.
name|getRests
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getRestCollection ()
specifier|public
name|RestsDefinition
name|getRestCollection
parameter_list|()
block|{
return|return
name|restCollection
return|;
block|}
DECL|method|setRestCollection (RestsDefinition restCollection)
specifier|public
name|void
name|setRestCollection
parameter_list|(
name|RestsDefinition
name|restCollection
parameter_list|)
block|{
name|this
operator|.
name|restCollection
operator|=
name|restCollection
expr_stmt|;
block|}
DECL|method|setRouteCollection (RoutesDefinition routeCollection)
specifier|public
name|void
name|setRouteCollection
parameter_list|(
name|RoutesDefinition
name|routeCollection
parameter_list|)
block|{
name|this
operator|.
name|routeCollection
operator|=
name|routeCollection
expr_stmt|;
block|}
DECL|method|getRouteCollection ()
specifier|public
name|RoutesDefinition
name|getRouteCollection
parameter_list|()
block|{
return|return
name|this
operator|.
name|routeCollection
return|;
block|}
comment|/**      * Factory method      *      * @return the CamelContext      */
DECL|method|createContainer ()
specifier|protected
name|ModelCamelContext
name|createContainer
parameter_list|()
block|{
return|return
operator|new
name|DefaultCamelContext
argument_list|()
return|;
block|}
DECL|method|configureRest (RestDefinition rest)
specifier|protected
name|void
name|configureRest
parameter_list|(
name|RestDefinition
name|rest
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|configureRoute (RouteDefinition route)
specifier|protected
name|void
name|configureRoute
parameter_list|(
name|RouteDefinition
name|route
parameter_list|)
block|{
name|route
operator|.
name|setGroup
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a collection of routes to this context      *      * @param routes the routes      * @throws Exception if the routes could not be created for whatever reason      * @deprecated will be removed in Camel 3.0. Instead use {@link #includeRoutes(org.apache.camel.RoutesBuilder) includeRoutes} instead.      */
annotation|@
name|Deprecated
DECL|method|addRoutes (RoutesBuilder routes)
specifier|protected
name|void
name|addRoutes
parameter_list|(
name|RoutesBuilder
name|routes
parameter_list|)
throws|throws
name|Exception
block|{
name|includeRoutes
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

